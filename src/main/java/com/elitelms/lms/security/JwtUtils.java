package com.elitelms.lms.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import org.springframework.security.core.Authentication;

@Component
public class JwtUtils {

    private final Key key;
    private final long jwtExpirationMs;

    public JwtUtils(@Value("${app.jwtSecret}") String jwtSecret,
                    @Value("${app.jwtExpirationMs}") long jwtExpirationMs) {
        // Si usas una clave Base64, decodifícala; si usas texto plano, conviértelo en bytes.
        // Aquí asumimos que el secreto puede ser texto plano. Para producción usa Base64 y Keys.hmacShaKeyFor(Base64.getDecoder().decode(...))
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String generateJwtToken(Authentication authentication) {
        // principal debe ser UserDetailsImpl que expone getEmail()
        UsuarioDetailsImpl userPrincipal = (UsuarioDetailsImpl) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())   // usamos email como subject
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("name", userPrincipal.getFullName()) // claims opcionales
                .claim("roles", userPrincipal.getAuthorities())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key)
                .build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // captura: SignatureException, MalformedJwtException, ExpiredJwtException, UnsupportedJwtException
            // loguea si quieres
            return false;
        }
    }
}
