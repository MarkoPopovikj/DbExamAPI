package finki.ukim.mk.dbexamapi.web.common.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    public static final String AUTH_REFRESH_HEADER = "X-Auth-Refresh";

    private final SecurityErrorResponseWriter errorResponseWriter;

    public RestAuthenticationEntryPoint(SecurityErrorResponseWriter errorResponseWriter) {
        this.errorResponseWriter = errorResponseWriter;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setHeader(AUTH_REFRESH_HEADER, "true");
        errorResponseWriter.write(request, response, HttpStatus.UNAUTHORIZED, "Please authenticate.");
    }
}
