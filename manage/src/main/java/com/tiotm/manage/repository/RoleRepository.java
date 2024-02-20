package com.tiotm.manage.repository;


import java.util.Collection;

import com.tiotm.manage.domain.Role;

public interface RoleRepository<T extends Role> {
    /*Basic CRUD Operations */
    T create(T data);
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T data);
    Boolean delete(Long id);

    /* More Complex Operations */
    void addRoleToUser(Long userId, String roleName);
}