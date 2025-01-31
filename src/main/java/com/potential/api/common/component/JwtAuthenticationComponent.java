package com.potential.api.common.component;

import com.potential.api.model.User;
import com.potential.api.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationComponent extends OncePerRequestFilter {
    private final JwtProviderComponent jwtProviderComponent;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            jwtProviderComponent.addSecurityHeaders(response);

            String token = jwtProviderComponent.extractTokenFromHeader(request);

            if (token != null) {
                String id = jwtProviderComponent.validate(token);

                if (id != null) {
                    Optional<User> user = userRepository.findById(id);

                    if (user.isEmpty()) {
                        throw new ServletException("해당 아이디에서 사용자를 찾을 수 없습니다. : " + id);
                    }

                    String role = String.valueOf(user.get().getRole());

                    if (role == null) {
                        throw new ServletException("해당 아이디에서 역할을 찾을 수 없습니다. : " + id);
                    }

                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(role));

                    String clientIp = getClientIp(request);

                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    AbstractAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(id, null, authorities);

                    authenticationToken.setDetails(clientIp);
                    securityContext.setAuthentication(authenticationToken);
                    SecurityContextHolder.setContext(securityContext);
                }
            }
        } catch (Exception exception) {
            logger.error("JWT 인증 필터에서 예외 발생", exception);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 유효하지 않습니다.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0];
        }

        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        return request.getRemoteAddr();
    }
}
