package uz.pdp.springhrmanagementsystem.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import uz.pdp.springhrmanagementsystem.entity.User;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTProvider {
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getEmail())
                .claim("roles", principal.getAuthorities())
                .setId(principal.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration * 1000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            System.out.println("No'to'g'ri yaratilgan to'ken");
        } catch (UnsupportedJwtException e) {
            System.out.println("Token qo'llab-quvvatlanmaydi");
        } catch (ExpiredJwtException e) {
            System.out.println("Muddati o'tgan token");
        } catch (IllegalArgumentException e) {
            System.out.println("Bo'sh to'ken");
        } catch (SignatureException e) {
            System.out.println("Haqiqiy bo'lmagan token");
        }
        return false;
    }

    public Claims getClaimsObjectFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
