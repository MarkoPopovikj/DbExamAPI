package finki.ukim.mk.dbexamapi.web.common.security;

import finki.ukim.mk.dbexamapi.domain.dtos.auth.TokenPayload;
import finki.ukim.mk.dbexamapi.domain.enums.TokenType;
import finki.ukim.mk.dbexamapi.domain.exceptions.UnauthorizedException;
import finki.ukim.mk.dbexamapi.service.auth.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenService tokenService;

    public JwtAuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            TokenPayload payload = tokenService.verifyToken(authHeader.substring(BEARER_PREFIX.length()), TokenType.ACCESS);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        payload.userId(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + payload.role().name()))
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (UnauthorizedException exception) {
            // Invalid or expired token: continue unauthenticated; the entry point answers 401
        }

        filterChain.doFilter(request, response);
    }
}
