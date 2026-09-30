package vn.iotstar;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.test.util.ReflectionTestUtils;
import vn.iotstar.entities.User;
import vn.iotstar.exceptions.GlobalExceptionHandler;
import vn.iotstar.exceptions.JwtExpiredException;
import vn.iotstar.exceptions.JwtInvalidException;
import vn.iotstar.services.JwtService;

public class SecurityExceptionTests {

    private JwtService jwtService;
    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String secretKey = "3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600000L);
    }

    @Test
    void testPasswordNeverExposedInJsonSerialization() throws Exception {
        User user = new User();
        user.setId(1);
        user.setFullName("Nguyen Van A");
        user.setEmail("test@gmail.com");
        user.setPassword("$2a$10$hashed_super_secret_password");

        String json = objectMapper.writeValueAsString(user);

        // Đảm bảo không chứa trường password hay password hash trong JSON trả về
        Assertions.assertFalse(json.contains("password"), "JSON phản hồi không được chứa trường password!");
        Assertions.assertFalse(json.contains("hashed_super_secret_password"), "JSON không được chứa password hash!");
        Assertions.assertTrue(json.contains("test@gmail.com"));
    }

    @Test
    void testInvalidTokenThrows401() {
        // Token sai cú pháp hoàn toàn
        Assertions.assertThrows(JwtInvalidException.class, () -> {
            jwtService.validateAndExtractUsername("invalid.token.structure");
        });

        // Xử lý qua GlobalExceptionHandler
        ProblemDetail problem = exceptionHandler.handleSecurityException(new JwtInvalidException("Token sai cú pháp"));
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), problem.getStatus());
        Assertions.assertEquals("JWT không hợp lệ", problem.getProperties().get("description"));
    }

    @Test
    void testExpiredTokenThrows401() {
        ProblemDetail problem = exceptionHandler.handleSecurityException(new JwtExpiredException("JWT đã hết hạn"));
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), problem.getStatus());
        Assertions.assertEquals("JWT đã hết hạn", problem.getProperties().get("description"));
    }

    @Test
    void testBadCredentialsThrows401() {
        ProblemDetail problem = exceptionHandler.handleSecurityException(new BadCredentialsException("Sai mật khẩu"));
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), problem.getStatus());
        Assertions.assertEquals("Thông tin đăng nhập không hợp lệ", problem.getProperties().get("description"));
    }

    @Test
    void testLockedAccountThrows403() {
        ProblemDetail problem = exceptionHandler.handleSecurityException(new LockedException("Tài khoản đã bị khóa"));
        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), problem.getStatus());
        Assertions.assertEquals("Tài khoản bị khóa", problem.getProperties().get("description"));
    }

    @Test
    void testAccessDeniedThrows403WithCustomMessage() {
        ProblemDetail problem = exceptionHandler.handleSecurityException(new org.springframework.security.access.AccessDeniedException("Bạn không thể tự khóa tài khoản của chính mình!"));
        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), problem.getStatus());
        Assertions.assertEquals("Bạn không thể tự khóa tài khoản của chính mình!", problem.getDetail());
        Assertions.assertEquals("Không được phép truy cập vào tài nguyên", problem.getProperties().get("description"));
    }
}
