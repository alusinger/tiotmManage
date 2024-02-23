package com.tiotm.manage.rowmapper;

import org.springframework.jdbc.core.RowMapper;

import com.tiotm.manage.domain.Role;

public class RoleRowMapper implements RowMapper<Role>{
    return Role.builder()
        .id(resultSet.getLong("id"))
        .name(resultSet.getString("name"))
        .permission(resultSet.getString("role_permission"))
        .build();

}
