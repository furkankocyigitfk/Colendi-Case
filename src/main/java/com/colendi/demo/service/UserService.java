package com.colendi.demo.service;

import com.colendi.demo.exception.MicroException;
import com.colendi.demo.model.User;
import com.colendi.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;

    @Override
    public User findById(Long id) {
        if (Objects.isNull(id)) {
            throw new MicroException("User id cannot be null");
        }
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new MicroException("User not found");
        }
        return user.get();
    }
}
