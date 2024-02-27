package com.tiotm.manage.repository.implementation;

import static com.tiotm.manage.enumeration.RoleType.ROLE_USER;
import static com.tiotm.manage.enumeration.VerificationType.ACCOUNT;
import static com.tiotm.manage.query.UserQuery.COUNT_USER_EMAIL_QUERY;
import static com.tiotm.manage.query.UserQuery.INSERT_ACCOUNT_VERIFICATION_URL_QUERY;
import static com.tiotm.manage.query.UserQuery.INSERT_USER_QUERY;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tiotm.manage.domain.Role;
import com.tiotm.manage.domain.User;
import com.tiotm.manage.exceptions.ApiException;
import com.tiotm.manage.repository.RoleRepository;
import com.tiotm.manage.repository.UserRepository;
//import com.tiotm.manage.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository<User> {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RoleRepository<Role> roleRepository;
    private BCryptPasswordEncoder passwordEncoder;
    //private final EmailService emailService;

    @Override
    public User create(User user) {
        // Check the email is unique
        if (getEmailCount(user.getEmail().trim().toLowerCase()) > 0) throw new ApiException("Email already. Please enter a unique email address.");
        // Save the new user
        try {
            log.info("Creating User");
            KeyHolder keyHolder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(user);
            jdbcTemplate.update(INSERT_USER_QUERY, parameters, keyHolder);
            user.setUserId(Objects.requireNonNull(keyHolder.getKey()).longValue());
            // Add roles to the user
            roleRepository.addRoleToUser(user.getUserId(), ROLE_USER.name());
            // Send verification email
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
            // Save URL in verification table
            jdbcTemplate.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY, Map.of("userId", user.getUserId(), "url", verificationUrl));
            // Send email to user with verification URL
            //emailService.sendVerificationUrl(user.getFirstName(), user.getEmail(), verificationUrl, ACCOUNT.getType());
            user.setEnabled(false);
            user.setIsNotLocked(true);
            // Return the newly created user
            return user;
        }catch (Exception exception) { 
            throw new ApiException("Error occurred while saving user: " + exception.getMessage());
        }
    }

    @Override
    public Collection<User> list(int page, int pageSize) {
        //  Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'list'");
    }

    @Override
    public User get(Long id) {
        // Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public User update(User data) {
        //  Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Boolean delete(Long id) {
        //  Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    private Integer getEmailCount(String email) {
        return jdbcTemplate.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email", email), Integer.class);
    }

    private SqlParameterSource getSqlParameterSource(User user) {
        return new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("password", passwordEncoder.encode(user.getPassword()));
    }

    private String getVerificationUrl(String key, String type) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("user/verify/" + type + "/" + key).toUriString();
    }
}
