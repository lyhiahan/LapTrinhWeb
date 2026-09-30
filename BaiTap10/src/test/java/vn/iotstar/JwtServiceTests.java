package vn.iotstar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import vn.iotstar.entities.User;
import vn.iotstar.services.JwtService;

public class JwtServiceTests {

    private JwtService jwtService;
    private final String secretKey = "3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b";
    private final long expiration = 3600000;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", expiration);
    }

    @Test
    void testGenerateAndValidateTokenWithNimbus() {
        User user = new User();
        user.setEmail("admin@example.com");
        user.setFullName("Quản trị viên");

        // Sinh mã token bằng Nimbus
        String token = jwtService.generateToken(user);
        Assertions.assertNotNull(token);
        Assertions.assertTrue(token.split("\\.").length == 3, "Token phải có cấu trúc 3 phần header.payload.signature");

        // Trích xuất username
        String extractedEmail = jwtService.extractUsername(token);
        Assertions.assertEquals("admin@example.com", extractedEmail);

        // Kiểm tra tính hợp lệ
        boolean isValid = jwtService.isTokenValid(token, user);
        Assertions.assertTrue(isValid, "Token sinh bởi Nimbus phải hợp lệ đối với user này");

        // Kiểm tra khi sai user
        User otherUser = new User();
        otherUser.setEmail("other@example.com");
        boolean isInvalidUser = jwtService.isTokenValid(token, otherUser);
        Assertions.assertFalse(isInvalidUser, "Token không được hợp lệ khi kiểm tra với user khác");
    }
}
