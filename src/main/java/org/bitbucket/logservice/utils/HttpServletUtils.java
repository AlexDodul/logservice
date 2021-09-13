package org.bitbucket.logservice.utils;

import javax.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.bitbucket.logservice.security.ApiKeyProvider;

@UtilityClass
public class HttpServletUtils {

  private final ApiKeyProvider apiKeyProvider = new ApiKeyProvider();

  public String getCompanyName(HttpServletRequest request) {
    return apiKeyProvider.getApplicationName(request.getHeader("X-Api-Key"));
  }
}
