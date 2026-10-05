package vn.hcmute.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import vn.hcmute.entity.User_24133016;
import vn.hcmute.repository.UserRepository_24133016;
import vn.hcmute.service.impl.UserServiceImpl_24133016;
import vn.hcmute.util.PasswordUtil_24133016;

@ExtendWith(MockitoExtension.class)
class UserServiceImpl_24133016Test {

    @Mock
    private UserRepository_24133016 userRepository;

    @Mock
    private IEmailService_24133016 emailUtil;

    private UserServiceImpl_24133016 service;

    @BeforeEach
    void setUp() {
        service = new UserServiceImpl_24133016(userRepository, emailUtil);
    }

    @Test
    void registerRejectsEmailOwnedByAnotherInactiveUsernameWithoutChangingItsId() {
        User_24133016 emailOwner = user("old-user", "old-password", false);
        emailOwner.setEmail("same@example.com");
        when(userRepository.findByUsername("new-user")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("same@example.com")).thenReturn(Optional.of(emailOwner));

        boolean registered = service.register(
                "new-user", "new-password", "same@example.com", "New User", "0900000000");

        assertFalse(registered);
        assertTrue("old-user".equals(emailOwner.getUsername()));
        verify(emailUtil, never()).sendOtpEmail(any(), any(), any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerHashesPasswordAndStoresOtpOnlyAfterEmailWasSent() {
        when(userRepository.findByUsername("new-user")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(emailUtil.generateOtp()).thenReturn("123456");
        when(emailUtil.sendOtpEmail("new@example.com", "123456",
                "Mã xác thực OTP - WebProject_24133016")).thenReturn(true);

        boolean registered = service.register(
                "new-user", "secret", "new@example.com", "New User", "0900000000");

        assertTrue(registered);
        ArgumentCaptor<User_24133016> captor = ArgumentCaptor.forClass(User_24133016.class);
        verify(userRepository).saveAndFlush(captor.capture());
        User_24133016 saved = captor.getValue();
        assertTrue(PasswordUtil_24133016.matches("secret", saved.getPassword()));
        assertNotEquals("secret", saved.getPassword());
        assertTrue("123456".equals(saved.getOtp()));
        assertTrue(saved.getOtpExpiry() != null);
    }

    @Test
    void registerDoesNotPersistUserWhenEmailSendingFails() {
        when(userRepository.findByUsername("new-user")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(emailUtil.generateOtp()).thenReturn("123456");
        when(emailUtil.sendOtpEmail(any(), any(), any())).thenReturn(false);

        assertFalse(service.register(
                "new-user", "secret", "new@example.com", "New User", "0900000000"));
        verify(userRepository, never()).saveAndFlush(any());
    }

    @Test
    void successfulLoginUpgradesLegacyPlaintextPassword() {
        User_24133016 user = user("legacy", "plain-secret", true);
        when(userRepository.findByUsername("legacy")).thenReturn(Optional.of(user));

        User_24133016 loggedIn = service.login("legacy", "plain-secret");

        assertSame(user, loggedIn);
        assertTrue(PasswordUtil_24133016.isHashed(user.getPassword()));
        assertTrue(PasswordUtil_24133016.matches("plain-secret", user.getPassword()));
        verify(userRepository).save(user);
    }

    @Test
    void passwordHashRejectsWrongPassword() {
        String hashed = PasswordUtil_24133016.hash("correct-password");
        assertTrue(PasswordUtil_24133016.matches("correct-password", hashed));
        assertFalse(PasswordUtil_24133016.matches("wrong-password", hashed));
        assertFalse(PasswordUtil_24133016.matches("anything", "pbkdf2_sha256$invalid"));
    }

    private User_24133016 user(String username, String password, boolean active) {
        User_24133016 user = new User_24133016();
        user.setUsername(username);
        user.setPassword(password);
        user.setActive(active);
        user.setAdmin(false);
        return user;
    }
}
