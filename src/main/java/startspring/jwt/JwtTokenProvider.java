package startspring.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import startspring.common.exceptions.BadRequestException;
import startspring.common.exceptions.NotFoundResourceException;
import startspring.dto.response.TokenResponse;
import startspring.service.dto.TokenDto;
import startspring.service.redis.RedisDao;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Set;

@Component
@Slf4j
public class JwtTokenProvider implements InitializingBean, TokenProvider{
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일
    private static final long THREE_DAYS = 1000 * 60 * 60 * 24 * 3;  // 3일

    private final String secretKey;
    private final long tokenValidityInMilliseconds;
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisDao redisDao;
    private final RedisTemplate redisTemplate;


    private Key key;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds,
            UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider, RedisDao redisDao, RedisTemplate redisTemplate
    ) {
        this.secretKey = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisDao = redisDao;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String decodedKey = new String(Base64.getDecoder().decode(secretKey));
        key = Keys.hmacShaKeyFor(decodedKey.getBytes());
    }

    @Override
    public TokenDto generateTokenDto(String userId) {
        long now = (new Date()).getTime();
        Date expireDate = new Date();

        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(expireDate)
                .claim(AUTHORITIES_KEY, userId)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder().
                setSubject(userId)
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.builder().grantType(BEARER_TYPE).accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Override
    public String createAccessToken(String userId) {
        long now = (new Date()).getTime();
        Date expireDate = new Date();

        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(expireDate)
                .claim(AUTHORITIES_KEY, userId)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return accessToken;
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty: {}", e.getMessage());
        }

        return false;

    }

    @Override
    public String createRefreshToken(Authentication authentication) {
        return "";
    }

    @Override
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    @Override
    public TokenResponse reissue(TokenDto tokenDto) {
        /*
         * Access token이 만료됐는지는 중요치 않음.
         * Access token으로 refresh token이 존재하는 지 확인하고

         * refresh token이 존재하며 아직 유요기간이 남아있다면
         * Access token을 재 발급해서 response 줘야함.
         *
         * 안남아 있으면 Exception처리해서 재로그인 필요를 알려야함.
         *
         * get(tokenDto.getAccessToken()) 했는데 없거나(null이거나) 가져온 refresh token이
         * jwtTokenProvider.validateToken(refreshToken) 이 false거나 실패면 exception 처리 해야함.
         * 20241114 진행예정!
         * 그리고 refresh token의 redis key를 새로 발급받은 access token으로 변경해준다.
         *
         * ps ) Token은 만료되면 안에있는 Subject를 못꺼냄. 그렇기 때문에 refresh token에서 Subject를 꺼내서 userId를 조회해야함.
         * 그걸로 다시 access token 발급
         */

        String oldAccessToken = findAccessTokenByRefreshToken(tokenDto.getRefreshToken());
        Object refreshTokenObject = redisDao.getValues(tokenDto.getAccessToken());
        String refreshToken = redisDao.getStringValue(tokenDto.getRefreshToken());

        if (refreshTokenObject == null) {
            throw new NotFoundResourceException("Not Found refresh token");
        }

        if (!isValidRefreshToken(refreshToken)) {
            throw new BadRequestException("Invalid refresh token, re-login required");
        }

        String userId = extractUserIdFromRefreshToken(refreshToken);

        String newAccessToken = jwtTokenProvider.createAccessToken(userId);

        redisDao.delete(oldAccessToken);
        redisDao.setValues(newAccessToken, refreshToken);

        return new TokenResponse(newAccessToken);
    }

    private boolean isValidRefreshToken(String refreshToken) {
        try {
            return jwtTokenProvider.validateToken(refreshToken);
        } catch (ValidationException e) {
            return false;
        }
    }

    private String extractUserIdFromRefreshToken(String refreshToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();

        return claims.getSubject();
    }

    private String findAccessTokenByRefreshToken(String refreshToken) {
        Set<String> keys = redisTemplate.keys("*");

        for(String key : keys) {
            Object value = redisTemplate.opsForValue().get(refreshToken);

            if(refreshToken.equals(value)) {
                return key;
            }
        }
        return null;


    }
}
