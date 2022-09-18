package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.User;
import org.example.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final List<User> users = List.of(
            User.builder().name("Tom").password("123").roles(List.of("user")).build(),
            User.builder().name("Peter").password("123").roles(List.of("admin")).build(),
            User.builder().name("Niko").password("123").roles(List.of("premium")).build()
    );

    @Override
    public User findUserByNameAndPassword(String name, String password) {
        return users.stream()
                .filter(u -> u.getName().equals(name) && u.getPassword().equals(password))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Password or login does not exist"));
    }

    @Override
    public User findUserByName(String name) {
        return users.stream()
                .filter(u -> u.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Password or login does not exist"));
    }
}
