package com.colendi.demo.service;

import com.colendi.demo.exception.MicroException;
import com.colendi.demo.model.User;
import com.colendi.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    @Test
    void findById_whenGivenValidUserId_thenReturnUser() {
        Long userId = 1L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.findById(userId);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository).findById(userId);
    }

    @Test
    void findById_whenGivenNull_thenThrowsUserIdCannotBeNull() {
        Long userId = null;

        MicroException thrownException = assertThrows(MicroException.class, () -> userService.findById(userId));
        assertEquals("User id cannot be null", thrownException.getMessage());
        verify(userRepository, never()).findById(any());
    }

    @Test
    void findById_whenGivenNotValidUserId_thenThrowsUserNotFound() {
        Long userId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        MicroException thrownException = assertThrows(MicroException.class, () -> userService.findById(userId));
        assertEquals("User not found", thrownException.getMessage());
        verify(userRepository).findById(userId);
    }
}
