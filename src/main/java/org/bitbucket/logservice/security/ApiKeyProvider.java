package org.bitbucket.logservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApiKeyProvider {

  private static final String SECRET_KEY = "SecretKeyGrizzly";

  public String generateApiKey(String applicationName) {
    return Jwts.builder()
        .setSubject(applicationName)
        .setIssuedAt(new Date())
        .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
        .compact();
  }

  public boolean validateApiKey(String apiKey) {
    try {
      Jwts.parser()
          .setSigningKey(SECRET_KEY)
          .parseClaimsJws(apiKey);
      return true;
    } catch (SignatureException | MalformedJwtException | IllegalArgumentException e) {
      log.error(e.getMessage());
      return false;
    }
  }

  public String getApplicationName(String apiKey) {
    Claims body = Jwts.parser()
        .setSigningKey(SECRET_KEY)
        .parseClaimsJws(apiKey)
        .getBody();
    return body.getSubject();
  }
}
