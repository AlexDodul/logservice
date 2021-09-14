package org.bitbucket.logservice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiKeyResponse {
  private String apiKey;
}

