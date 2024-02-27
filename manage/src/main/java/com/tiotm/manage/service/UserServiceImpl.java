package com.tiotm.manage.service;

import org.springframework.stereotype.Service;

import com.tiotm.manage.domain.User;
import com.tiotm.manage.dto.UserDTO;
import com.tiotm.manage.repository.UserRepository;
import com.tiotm.manage.rowmapper.UserDTOMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository<User> userRepository;

    @Override
    public UserDTO createUser(User user) {
        log.info("Creating User");
        return UserDTOMapper.fromUser(userRepository.create(user));
    }

}
