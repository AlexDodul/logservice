package org.bitbucket.logservice.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class APIKeyProvider {

    @Value("{app.security.secret-key}")
    private String SECRET_KEY;

    public String generateAPIKey(String applicationName) {
        return Jwts.builder()
                .setSubject(applicationName)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, this.SECRET_KEY)
                .compact();
    }

    public boolean validateAPIKey(String apiKey) {
        try {
            Jwts.parser()
                    .setSigningKey(this.SECRET_KEY)
                    .parseClaimsJws(apiKey);
            return true;
        } catch (SignatureException | MalformedJwtException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public String getApplicationName(String apiKey) {
        Claims body = Jwts.parser()
                .setSigningKey(this.SECRET_KEY)
                .parseClaimsJws(apiKey)
                .getBody();
        return body.getSubject();
    }
}
