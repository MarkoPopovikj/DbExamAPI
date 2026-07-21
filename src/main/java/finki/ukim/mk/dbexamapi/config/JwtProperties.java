package finki.ukim.mk.dbexamapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.security.jwt")
public record JwtProperties(
        String accessSecret,
        String refreshSecret,
        long accessExpirationMinutes,
        long refreshExpirationDays
) {
}
