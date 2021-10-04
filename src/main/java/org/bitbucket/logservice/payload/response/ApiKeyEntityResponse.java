package org.bitbucket.logservice.payload.response;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiKeyEntityResponse {
  private Map<String, String> apiKey;
}
