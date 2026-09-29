package vn.iotstar.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.impl.AuthServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceOtpTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private OtpService otpService;

    @InjectMocks
    private AuthServiceImpl authService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
            .id(1L)
            .username("testuser")
            .email("test@iotstar.vn")
            .password("encoded_old_password")
            .enabled(true)
            .emailVerified(true)
            .locked(false)
            .build();
    }

    @Test
    @DisplayName("Đặt lại mật khẩu thành công bằng mã OTP hợp lệ")
    void resetPasswordWithOtp_Success() {
        when(userRepository.findByEmail("test@iotstar.vn")).thenReturn(Optional.of(sampleUser));
        when(otpService.verifyResetPasswordOtp("test@iotstar.vn", "123456")).thenReturn(true);
        when(passwordEncoder.encode("newPassword123")).thenReturn("encoded_new_password");

        assertDoesNotThrow(() ->
            authService.resetPasswordWithOtp("test@iotstar.vn", "123456", "newPassword123")
        );

        assertEquals("encoded_new_password", sampleUser.getPassword());
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    @DisplayName("Đặt lại mật khẩu thất bại khi OTP không hợp lệ, không cập nhật CSDL")
    void resetPasswordWithOtp_InvalidOtp_ThrowsException() {
        when(userRepository.findByEmail("test@iotstar.vn")).thenReturn(Optional.of(sampleUser));
        when(otpService.verifyResetPasswordOtp("test@iotstar.vn", "999999")).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            authService.resetPasswordWithOtp("test@iotstar.vn", "999999", "newPassword123")
        );

        assertTrue(ex.getMessage().contains("OTP không hợp lệ"));
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Không cho phép đặt lại mật khẩu cho tài khoản đã bị khóa")
    void resetPasswordWithOtp_LockedUser_ThrowsException() {
        sampleUser.setLocked(true);
        when(userRepository.findByEmail("test@iotstar.vn")).thenReturn(Optional.of(sampleUser));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
            authService.resetPasswordWithOtp("test@iotstar.vn", "123456", "newPassword123")
        );

        assertTrue(ex.getMessage().contains("bị quản trị viên khóa"));
        verify(otpService, never()).verifyResetPasswordOtp(anyString(), anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Kích hoạt đăng ký thành công khi OTP đúng, tách biệt emailVerified")
    void verifyRegister_Success() {
        sampleUser.setEnabled(true);
        sampleUser.setEmailVerified(false);
        sampleUser.setLocked(false);

        when(userRepository.findByEmail("test@iotstar.vn")).thenReturn(Optional.of(sampleUser));
        when(otpService.verifyRegisterOtp("test@iotstar.vn", "123456")).thenReturn(true);

        boolean result = authService.verifyRegister("test@iotstar.vn", "123456");

        assertTrue(result);
        assertTrue(sampleUser.isEmailVerified());
        assertTrue(sampleUser.isEnabled());
        assertFalse(sampleUser.isLocked());
    }

    @Test
    @DisplayName("Tài khoản bị admin khóa không thể tự kích hoạt lại qua OTP đăng ký")
    void verifyRegister_LockedUser_Rejected() {
        sampleUser.setEnabled(false);
        sampleUser.setLocked(true);

        when(userRepository.findByEmail("test@iotstar.vn")).thenReturn(Optional.of(sampleUser));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
            authService.verifyRegister("test@iotstar.vn", "123456")
        );

        assertTrue(ex.getMessage().contains("bị quản trị viên khóa"));
        verify(otpService, never()).verifyRegisterOtp(anyString(), anyString());
    }
}
