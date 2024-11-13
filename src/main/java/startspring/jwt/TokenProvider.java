package startspring.jwt;

import org.springframework.security.core.Authentication;
import startspring.service.dto.TokenDto;

public interface TokenProvider {

    // 리프레시 액세스 토큰 생성
    //String createAccessToken(Authentication authentication);
    TokenDto generateTokenDto(String userId);

    // 액세스 토큰 생성
    String createAccessToken(String userId);

    // 토큰 유효성 검사
    boolean validateToken(String token);

    // 리프레시 토큰 생성
    String createRefreshToken(Authentication authentication);

    // 토큰 통해 authentication 객체 가져옴
    Authentication getAuthentication(String token);

}
