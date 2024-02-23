package com.tiotm.manage.repository.implementation;

import static com.tiotm.manage.enumeration.VerificationType.ACCOUNT;
import static com.tiotm.manage.query.UserQuery.COUNT_USER_EMAIL_QUERY;
import static com.tiotm.manage.query.UserQuery.INSERT_ACCOUNT_VERIFICATION_URL_QUERY;
import static com.tiotm.manage.query.UserQuery.INSERT_USER_QUERY;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.dao.EmptyResultDataAccessException;
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
import com.tiotm.manage.enumeration.RoleType;
import com.tiotm.manage.exceptions.ApiException;
import com.tiotm.manage.repository.RoleRepository;
import com.tiotm.manage.repository.UserRepository;
import com.tiotm.manage.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository<User> {
    private final NamedParameterJdbcTemplate jdbc;
    private final RoleRepository<Role> roleRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User create(User user) {
        // Check the email is unique
        if (getEmailCount(user.getEmail().trim().toLowerCase()) > 0) throw new ApiException("Email already. Please enter a unique email address.");
        // Save the new user
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(user);
            jdbc.update(INSERT_USER_QUERY, parameters, keyHolder);
            user.setUserId(Objects.requireNonNull(keyHolder.getKey()).longValue());
            // Add roles to the user
            roleRepository.addRoleToUser(user.getUserId(), RoleType.ROLE_USER.name());
            // Send verification email
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
            // Save URL in verification table
            jdbc.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY, Map.of("userId", "url", "type", "key", "createdAt", "expiresAt"));
            // Send email to user with verification URL
            //EmailService.sendVerificationUrl(user.getFirstName(), user.getEmail(), verificationUrl, ACCOUNT.getType());
            user.setEnabled(false);
            user.setIsNotLocked(true);
            // Return the newly created user
            return user;
        }catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No Role found by name: " + RoleType.ROLE_USER.name());
        }catch (Exception exception) { 
            throw new ApiException("Error occurred while saving user: " + exception.getMessage());
        }
    }

    @Override
    public Collection<User> list(int page, int pageSize) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'list'");
    }

    @Override
    public User get(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public User update(User data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Boolean delete(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    private Integer getEmailCount(String email) {
        // TODO Auto-generated method stub
        return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email", email), Integer.class);
    }

    private SqlParameterSource getSqlParameterSource(User user) {
        // TODO Auto-generated method stub
        return new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                
        throw new UnsupportedOperationException("Unimplemented method 'getSqlParameterSource'");
    }

    private String getVerificationUrl(String key, String type) {
        
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("user/verify/" + type + "/" + key).toUriString();
    }
}
