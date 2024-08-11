package com.colendi.demo.service;

import com.colendi.demo.model.User;

public interface IUserService {
    User findById(Long id);
}
