package vn.iotstar.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.iotstar.entities.User;
import vn.iotstar.services.UserService;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // API Lấy thông tin tài khoản hiện tại từ Token
    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    // API Lấy danh sách tất cả người dùng (Chỉ truy cập được khi đã đăng nhập có Token)
    @GetMapping
    public ResponseEntity<List<User>> allUsers() {
        List<User> users = userService.allUsers();
        return ResponseEntity.ok(users);
    }

    // API Khóa tài khoản: Chỉ Quản trị viên (ROLE_ADMIN) mới có quyền khóa, và không được tự khóa chính mình
    @PatchMapping("/{id}/lock")
    public ResponseEntity<User> lockUser(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new AccessDeniedException("Bạn không có quyền thực hiện thao tác này. Chỉ ROLE_ADMIN mới được phép khóa tài khoản!");
        }

        if (currentUser.getId().equals(id)) {
            throw new AccessDeniedException("Bạn không thể tự khóa tài khoản của chính mình!");
        }

        User lockedUser = userService.lockUser(id);
        return ResponseEntity.ok(lockedUser);
    }

    // API Mở khóa tài khoản: Chỉ Quản trị viên (ROLE_ADMIN) mới có quyền mở khóa
    @PatchMapping("/{id}/unlock")
    public ResponseEntity<User> unlockUser(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new AccessDeniedException("Bạn không có quyền thực hiện thao tác này. Chỉ ROLE_ADMIN mới được phép mở khóa tài khoản!");
        }

        User unlockedUser = userService.unlockUser(id);
        return ResponseEntity.ok(unlockedUser);
    }
}
