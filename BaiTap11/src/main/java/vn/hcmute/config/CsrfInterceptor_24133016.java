package vn.hcmute.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Bảo vệ các request thay đổi dữ liệu khi project không sử dụng Spring Security.
 * Token được ràng buộc với session và phải có trong mọi form POST.
 */
@Component
public class CsrfInterceptor_24133016 implements HandlerInterceptor {

    public static final String REQUEST_ATTRIBUTE = "csrfToken";
    private static final String SESSION_ATTRIBUTE = CsrfInterceptor_24133016.class.getName() + ".TOKEN";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        String method = request.getMethod();
        if ("HEAD".equals(method) || "OPTIONS".equals(method) || "TRACE".equals(method)) {
            return true;
        }

        HttpSession session = "GET".equals(method) ? request.getSession(true) : request.getSession(false);
        if (session == null) {
            return reject(response);
        }

        String expectedToken;
        synchronized (session) {
            expectedToken = (String) session.getAttribute(SESSION_ATTRIBUTE);
            if (expectedToken == null && "GET".equals(method)) {
                byte[] bytes = new byte[32];
                RANDOM.nextBytes(bytes);
                expectedToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
                session.setAttribute(SESSION_ATTRIBUTE, expectedToken);
            }
        }
        if (expectedToken != null) {
            request.setAttribute(REQUEST_ATTRIBUTE, expectedToken);
        }

        if ("GET".equals(method)) {
            return true;
        }

        String submittedToken = request.getParameter("_csrf");
        if (submittedToken == null || submittedToken.isBlank()) {
            submittedToken = request.getHeader("X-CSRF-TOKEN");
        }
        if (constantTimeEquals(expectedToken, submittedToken)) {
            return true;
        }

        return reject(response);
    }

    private boolean reject(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write("Yêu cầu không hợp lệ hoặc đã hết phiên. Vui lòng tải lại trang.");
        return false;
    }

    private boolean constantTimeEquals(String expected, String actual) {
        if (actual == null) {
            return false;
        }
        return MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.UTF_8),
                actual.getBytes(StandardCharsets.UTF_8));
    }
}
