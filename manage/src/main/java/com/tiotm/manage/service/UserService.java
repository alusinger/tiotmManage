package com.tiotm.manage.service;

import com.tiotm.manage.domain.User;
import com.tiotm.manage.dto.UserDTO;

public interface UserService {

    UserDTO createUser(User user);
}
