package vn.iotstar.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.ParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception) {
        ProblemDetail errorDetail;

        // 1. Thông tin đăng nhập không hợp lệ -> HTTP 401
        if (exception instanceof BadCredentialsException) {
            errorDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatusCode.valueOf(HttpStatus.UNAUTHORIZED.value()),
                    "Tên người dùng hoặc mật khẩu không chính xác"
            );
            errorDetail.setProperty("description", "Thông tin đăng nhập không hợp lệ");
            return errorDetail;
        }

        // 2. JWT đã hết hạn -> HTTP 401
        if (exception instanceof JwtExpiredException) {
            errorDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatusCode.valueOf(HttpStatus.UNAUTHORIZED.value()),
                    "Mã xác thực JWT đã hết hạn hiệu lực. Vui lòng đăng nhập lại."
            );
            errorDetail.setProperty("description", "JWT đã hết hạn");
            return errorDetail;
        }

        // 3. JWT không hợp lệ (sai chữ ký, sai định dạng) -> HTTP 401
        if (exception instanceof JwtInvalidException || exception instanceof ParseException) {
            errorDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatusCode.valueOf(HttpStatus.UNAUTHORIZED.value()),
                    exception.getMessage() != null ? exception.getMessage() : "Chuỗi Token JWT không đúng định dạng hoặc sai chữ ký"
            );
            errorDetail.setProperty("description", "JWT không hợp lệ");
            return errorDetail;
        }

        // 4. Chưa xác thực (thiếu token khi truy cập endpoint bảo vệ) -> HTTP 401
        if (exception instanceof InsufficientAuthenticationException) {
            errorDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatusCode.valueOf(HttpStatus.UNAUTHORIZED.value()),
                    "Yêu cầu mã xác thực Token hợp lệ để truy cập tài nguyên này"
            );
            errorDetail.setProperty("description", "Chưa xác thực");
            return errorDetail;
        }

        // 5. Tài khoản bị khóa -> HTTP 403
        if (exception instanceof AccountStatusException) {
            errorDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatusCode.valueOf(HttpStatus.FORBIDDEN.value()),
                    "Tài khoản của bạn đã bị khóa hoặc vô hiệu hóa"
            );
            errorDetail.setProperty("description", "Tài khoản bị khóa");
            return errorDetail;
        }

        // 6. Không có quyền truy cập vào tài nguyên -> HTTP 403
        if (exception instanceof AccessDeniedException) {
            String detailMsg = (exception.getMessage() != null && !exception.getMessage().equalsIgnoreCase("Access Denied"))
                    ? exception.getMessage()
                    : "Bạn không có quyền truy cập vào tài nguyên này";
            errorDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatusCode.valueOf(HttpStatus.FORBIDDEN.value()),
                    detailMsg
            );
            errorDetail.setProperty("description", "Không được phép truy cập vào tài nguyên");
            return errorDetail;
        }

        // Các lỗi xác thực khác của Spring Security
        if (exception instanceof AuthenticationException) {
            errorDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatusCode.valueOf(HttpStatus.UNAUTHORIZED.value()),
                    exception.getMessage()
            );
            errorDetail.setProperty("description", "Lỗi xác thực người dùng");
            return errorDetail;
        }

        // Mặc định lỗi 500
        errorDetail = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                exception.getMessage() != null ? exception.getMessage() : "Lỗi hệ thống máy chủ"
        );
        errorDetail.setProperty("description", "Lỗi nội bộ server không xác định");
        return errorDetail;
    }
}
