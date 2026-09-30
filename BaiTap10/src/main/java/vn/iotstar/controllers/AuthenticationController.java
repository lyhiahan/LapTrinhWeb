package vn.iotstar.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.iotstar.entities.User;
import vn.iotstar.models.LoginResponse;
import vn.iotstar.models.LoginUserDto;
import vn.iotstar.models.RegisterUserDto;
import vn.iotstar.services.AuthenticationService;
import vn.iotstar.services.JwtService;

import java.util.Map;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    // API Đăng ký tài khoản mới
    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    // API Đăng nhập sinh mã JWT qua thư viện Nimbus
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();

        return ResponseEntity.ok(loginResponse);
    }

    // API Tiện ích phục vụ kiểm thử: Trả về một chuỗi JWT có chữ ký hợp lệ 100% nhưng thời hạn đã hết hạn trong quá khứ
    @GetMapping("/sample-expired-token")
    public ResponseEntity<Map<String, String>> getSampleExpiredToken() {
        String expiredToken = jwtService.generateExpiredToken("testuser@gmail.com");
        return ResponseEntity.ok(Map.of("expiredToken", expiredToken));
    }
}
