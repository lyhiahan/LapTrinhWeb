package vn.iotstar.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import vn.iotstar.exceptions.JwtExpiredException;
import vn.iotstar.exceptions.JwtInvalidException;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, JWTClaimsSet::getSubject);
    }

    public <T> T extractClaim(String token, Function<JWTClaimsSet, T> claimsResolver) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return claimsResolver.apply(signedJWT.getJWTClaimsSet());
        } catch (ParseException e) {
            throw new JwtInvalidException("Lỗi phân tích cú pháp chuỗi JWT", e);
        }
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                    .subject(userDetails.getUsername())
                    .issueTime(new Date(System.currentTimeMillis()))
                    .expirationTime(new Date(System.currentTimeMillis() + jwtExpiration));

            extraClaims.forEach(builder::claim);
            SignedJWT signedJWT = new SignedJWT(header, builder.build());
            signedJWT.sign(new MACSigner(secretKey.getBytes()));
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Lỗi ký mã xác thực thông báo JWT", e);
        }
    }

    /**
     * Sinh token có chữ ký thật 100% bằng secret key của hệ thống nhưng hết hạn trong quá khứ
     * Phục vụ kiểm thử chính xác ngoại lệ JwtExpiredException (HTTP 401)
     */
    public String generateExpiredToken(String subject) {
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(subject != null ? subject : "testuser@gmail.com")
                    .issueTime(new Date(System.currentTimeMillis() - 7200000))
                    .expirationTime(new Date(System.currentTimeMillis() - 3600000)) // Hết hạn 1 giờ trước
                    .build();

            SignedJWT signedJWT = new SignedJWT(header, claimsSet);
            signedJWT.sign(new MACSigner(secretKey.getBytes()));
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Lỗi sinh expired token", e);
        }
    }

    /**
     * Xác thực chuỗi JWT và trích xuất username.
     * Ném JwtInvalidException nếu sai cú pháp hoặc sai chữ ký (HTTP 401).
     * Ném JwtExpiredException nếu token đã quá hạn (HTTP 401).
     */
    public String validateAndExtractUsername(String token) {
        SignedJWT signedJWT;
        try {
            signedJWT = SignedJWT.parse(token);
        } catch (ParseException e) {
            throw new JwtInvalidException("Chuỗi Token JWT không đúng định dạng", e);
        }

        try {
            boolean verified = signedJWT.verify(new MACVerifier(secretKey.getBytes()));
            if (!verified) {
                throw new JwtInvalidException("Chữ ký JWT không hợp lệ (Signature verification failed)");
            }
        } catch (JOSEException e) {
            throw new JwtInvalidException("Lỗi xác minh chữ ký JWT", e);
        }

        Date expirationTime;
        try {
            expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        } catch (ParseException e) {
            throw new JwtInvalidException("Không thể đọc thời gian hết hạn của JWT", e);
        }

        if (expirationTime == null || expirationTime.before(new Date())) {
            throw new JwtExpiredException("JWT đã hết hạn");
        }

        try {
            String username = signedJWT.getJWTClaimsSet().getSubject();
            if (username == null || username.trim().isEmpty()) {
                throw new JwtInvalidException("JWT không chứa thông tin chủ thể");
            }
            return username;
        } catch (ParseException e) {
            throw new JwtInvalidException("Không thể trích xuất chủ thể từ JWT", e);
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = validateAndExtractUsername(token);
            return username.equals(userDetails.getUsername());
        } catch (Exception e) {
            return false;
        }
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }
}
