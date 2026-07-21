package finki.ukim.mk.dbexamapi.service.auth;

import finki.ukim.mk.dbexamapi.domain.dtos.auth.AuthDto;
import finki.ukim.mk.dbexamapi.domain.dtos.auth.RegisterDto;
import finki.ukim.mk.dbexamapi.domain.dtos.auth.TokenWrapperDto;

public interface AuthService {

    AuthDto register(RegisterDto registerDto);

    AuthDto login(String email, String password);

    TokenWrapperDto refresh(String refreshToken);
}
