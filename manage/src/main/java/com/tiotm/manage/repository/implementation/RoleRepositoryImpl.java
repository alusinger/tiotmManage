package com.tiotm.manage.repository.implementation;

import static com.tiotm.manage.query.RoleQuery.SELECT_ROLE_BY_NAME_QUERY;
import static com.tiotm.manage.query.RoleQuery.INSERT_ROLE_TO_USER_QUERY;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tiotm.manage.domain.Role;
import static com.tiotm.manage.enumeration.RoleType.ROLE_USER;
import com.tiotm.manage.exceptions.ApiException;
import com.tiotm.manage.repository.RoleRepository;
import com.tiotm.manage.rowmapper.RoleRowMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleRepositoryImpl implements RoleRepository<Role> {
        private final NamedParameterJdbcTemplate jdbc;
        
        @Override
        public Role create(Role data) {
            log.info("Implement this!");
            return null;
        }
    
        @Override
        public Collection<Role> list(int page, int pageSize) {
            return null;
        }
    
        @Override
        public Role get(Long id) {
            return null;
        }
    
        @Override
        public Role update(Role data) {
            return null;
        }
    
        @Override
        public Boolean delete(Long id) {
            return null;
        }
    
        @Override
        public void addRoleToUser(Long userId, String roleName) {
            log.info("Adding role {} to user id: {}", roleName, userId);
            try {
                Role role = jdbc.queryForObject(SELECT_ROLE_BY_NAME_QUERY, Map.of("roleName", roleName), new RoleRowMapper());
                jdbc.update(INSERT_ROLE_TO_USER_QUERY, Map.of("userId", userId, "roleId", Objects.requireNonNull(role).getId()));
            } catch (EmptyResultDataAccessException exception ) {
                throw new ApiException("No role found by name: " + ROLE_USER.name());
            } catch (Exception exception) {
                throw new ApiException("An error occured. Please try again.");
            }
        }

        @Override
        public Role getRoleByUserId(Long userId) {
            //  Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getRoleByUserId'");
        }

        @Override
        public Role getRoleByUserEmail(String email) {
            //  Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getRoleByUserEmail'");
        }

        @Override
        public void updateUserRole(Long userId, String roleName) {
            //  Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'updateUserRole'");
        }

}
