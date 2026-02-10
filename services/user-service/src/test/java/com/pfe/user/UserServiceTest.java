package com.pfe.user;

import com.pfe.user.domain.User;
import com.pfe.user.repository.UserRepository;
import com.pfe.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUserSuccessfully() {
        // Arrange
        User user = new User("testuser", "test@example.com", "password", Set.of("ROLE_USER"));

        Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User registeredUser = userService.registerUser(user);

        // Assert
        Assertions.assertNotNull(registeredUser);
        Assertions.assertEquals("testuser", registeredUser.getUsername());
        Assertions.assertEquals("encodedPassword", registeredUser.getPassword());

        Mockito.verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUsernameExists() {
        // Arrange
        User user = new User("existing", "test@example.com", "password", Set.of());
        Mockito.when(userRepository.existsByUsername("existing")).thenReturn(true);

        // Act & Assert
        Assertions.assertThrows(RuntimeException.class, () -> {
            userService.registerUser(user);
        });
    }

    @Test
    void shouldLoginUserSuccessfully() {
        User user = new User("testuser", "test@example.com", "encodedPassword", Set.of());
        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(java.util.Optional.of(user));
        Mockito.when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        User loggedInUser = userService.loginUser("testuser", "password");

        Assertions.assertNotNull(loggedInUser);
        Assertions.assertEquals("testuser", loggedInUser.getUsername());
    }

    @Test
    void shouldFailLoginWithWrongPassword() {
        User user = new User("testuser", "test@example.com", "encodedPassword", Set.of());
        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(java.util.Optional.of(user));
        Mockito.when(passwordEncoder.matches("wrong", "encodedPassword")).thenReturn(false);

        Assertions.assertThrows(RuntimeException.class, () -> {
            userService.loginUser("testuser", "wrong");
        });
    }
}
