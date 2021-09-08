package org.bitbucket.logservice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class APIKeyResponse {
    private String apiKey;
}

