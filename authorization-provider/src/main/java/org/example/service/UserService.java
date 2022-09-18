package org.example.service;

import org.example.domain.User;

import java.util.Optional;

public interface UserService {
    User findUserByNameAndPassword(String name, String password);

    User findUserByName(String name);
}
