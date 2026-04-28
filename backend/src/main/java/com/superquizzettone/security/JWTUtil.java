package com.superquizzettone.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtil {

    private static final String USERNAME_CLAIM = "username";

    @Value("${jwt_secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long jwtExpirationMs;

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(USERNAME_CLAIM, username);
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        String username = claims.get(USERNAME_CLAIM, String.class);
        return username != null ? username : claims.getSubject();
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        Claims claims = extractAllClaims(token);
        String claimUsername = claims.get(USERNAME_CLAIM, String.class);
        String subjectUsername = claims.getSubject();
        String effectiveUsername = claimUsername != null ? claimUsername : subjectUsername;
        boolean isUsernameConsistent = claimUsername == null || claimUsername.equals(subjectUsername);

        return effectiveUsername != null
                && effectiveUsername.equals(userDetails.getUsername())
                && isUsernameConsistent
                && !isTokenExpired(token);
    }
}
