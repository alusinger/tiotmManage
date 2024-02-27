package com.tiotm.manage.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.tiotm.manage.domain.Role;

public class RoleRowMapper implements RowMapper<Role>{

    @Override
    public Role mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Role.builder()
        .id(resultSet.getLong("id"))
        .roleName(resultSet.getString("name"))
        .permission(resultSet.getString("role_permission"))
        .build();
    }


}
