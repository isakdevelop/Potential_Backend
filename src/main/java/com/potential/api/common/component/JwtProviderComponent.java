package com.potential.api.common.component;

import com.potential.api.common.enums.Error;
import com.potential.api.common.enums.Role;
import com.potential.api.common.exception.PotentialException;
import com.potential.api.model.RefreshToken;
import com.potential.api.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class JwtProviderComponent {
    @Value("${secret-key}")
    private String secretKey;

    @Value("${Auth_Header}")
    private String authHeader;

    @Value("${bearer_Prefix}")
    private String bearerPrefix;

    @Value("${security.headers.strict-transport-security}")
    private String strictTransportSecurity;

    @Value("${security.headers.x-content-type-options}")
    private String xContentTypeOptions;

    @Value("${security.headers.x-frame-options}")
    private String xFrameOptions;

    @Value("${security.headers.x-xss-protection}")
    private String xXssProtection;

    @Value("${security.headers.content-security-policy}")
    private String contentSecurityPolicy;


    private final RefreshTokenRepository refreshTokenRepository;

    public String createAccessToken(String id, Role role) {
        Instant expiredDate = Instant.now().plus(3, ChronoUnit.HOURS);
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .header().add("type", "jwt").and()
                .subject("access-token")
                .claim("id", id)
                .claim("role", role.name())
                .signWith(key)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(expiredDate))
                .compact();
    }


    public String createRefreshToken(String id) {
        String refreshToken = UUID.randomUUID().toString();
        RefreshToken redis = RefreshToken.builder()
                .refreshToken(refreshToken)
                .requestId(id)
                .build();
        refreshTokenRepository.save(redis);
        return refreshToken;
    }

    public String validate(String jwt) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwt);

            return claims.getPayload().get("id", String.class);

        } catch (Exception exception) {
            throw new PotentialException(Error.FORBIDDEN.getStatus(), "토큰에 대한 접근 권한이 존재하지 않습니다!");
        }
    }

    public void addSecurityHeaders(HttpServletResponse response) {
        response.addHeader("Strict-Transport-Security", strictTransportSecurity);
        response.addHeader("X-Content-Type-Options", xContentTypeOptions);
        response.addHeader("X-Frame-Options", xFrameOptions);
        response.addHeader("X-XSS-Protection", xXssProtection);
        response.addHeader("Content-Security-Policy", contentSecurityPolicy);
    }

    public String extractTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(authHeader);
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(bearerPrefix)) {
            return authorizationHeader.substring(bearerPrefix.length());
        }
        return null;
    }
}