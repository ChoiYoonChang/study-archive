package study.jwt.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // 최소 256비트(32바이트) 이상의 시크릿 키 필요
    private final String SECRET = "mySuperSecretKeyForJwtToken1234567890";
    private final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    private final long EXPIRATION_MS = 1000 * 60 * 60; // 1시간

    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String getUsername(String token){
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e){
            System.out.println("토큰이 만료되었습니다: " + e.getMessage());
        } catch (UnsupportedJwtException e){
            System.out.println("지원하지 않는 JWT 형식입니다: " + e.getMessage());
        } catch (MalformedJwtException e){
            System.out.println("잘못된 JWT 토큰입니다: " + e.getMessage());
        } catch (SignatureException e){
            System.out.println("JWT 서명 검증 실패: " + e.getMessage());
        } catch (IllegalArgumentException e){
            System.out.println("JWT Claims가 비어있습니다: " + e.getMessage());
        }
        return false;
    }
}
