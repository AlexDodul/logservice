package org.bitbucket.logservice.security;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.bitbucket.logservice.services.ApiKeyService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ApiResponse(description = "")
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

  private final ApiKeyService apiKeyService;

  private String principalRequestHeader = "X-Api-Key";

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    ApiKeyAuthFilter filter = new ApiKeyAuthFilter(principalRequestHeader);
    filter.setAuthenticationManager(new AuthenticationManager() {

      @Override
      public Authentication authenticate(Authentication authentication)
          throws AuthenticationException {
        String principal = String.valueOf(authentication.getPrincipal());
        if (!apiKeyService.verification(principal)) {
          throw new BadCredentialsException("The API key was not found or not the expected value.");
        }

        authentication.setAuthenticated(true);
        return authentication;
      }
    });
    httpSecurity.
        antMatcher("/api/**").
        csrf().disable().
        sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
        and().addFilter(filter).authorizeRequests().anyRequest().authenticated();
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/api/logs/generate-api-key");
    web.ignoring().antMatchers("/swagger-ui/**", "/swagger-ui**", "/v3/api-docs/**");
  }
}
