package startspring.jwt;

import org.springframework.security.core.Authentication;

public interface TokenProvider {

    // 액세스 토큰 생성
    String createAccessToken(Authentication authentication);

    // 토큰 유효성 검사
    boolean validateToken(String token);

    // 리프레시 토큰 생성
    String createRefreshToken(Authentication authentication);

    // 토큰 통해 authentication 객체 가져옴
    Authentication getAuthentication(String token);
}
