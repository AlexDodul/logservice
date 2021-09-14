package org.bitbucket.logservice.security;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

public class ApiKeyAuthFilter extends AbstractPreAuthenticatedProcessingFilter {

  private final String principalRequestHeader;

  public ApiKeyAuthFilter(String principalRequestHeader) {
    this.principalRequestHeader = principalRequestHeader;
  }

  @Override
  protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
    return httpServletRequest.getHeader(principalRequestHeader);
  }

  @Override
  protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
    return "N/A";
  }

}
