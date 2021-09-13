package org.bitbucket.logservice.services;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.bitbucket.logservice.entity.APIKeyEntity;
import org.bitbucket.logservice.exception.EntityExistException;
import org.bitbucket.logservice.payload.request.ApplicationNameRequest;
import org.bitbucket.logservice.repositories.APIKeyRepo;
import org.bitbucket.logservice.security.ApiKeyProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class APIKeyService {
    private final APIKeyRepo apiKeyRepo;
    private final ApiKeyProvider provider;

    public APIKeyEntity createApiKey(ApplicationNameRequest applicationNameRequest) {
        if (Objects.nonNull(this.apiKeyRepo.findByApplicationName(applicationNameRequest.getApplicationName()).orElse(null))) {
            throw new EntityExistException("Token exists");
        }
        String apiKey = this.provider.generateApiKey(applicationNameRequest.getApplicationName());
        return apiKeyRepo.save(new APIKeyEntity(applicationNameRequest.getApplicationName(), apiKey));
    }

    public APIKeyEntity getApiKey(String applicationName) {
        return apiKeyRepo.findByApplicationName(applicationName).orElseThrow();
    }

    public boolean verify(String apiKey) {
        System.out.println(this.provider.getApplicationName(apiKey));
        return Objects.nonNull(this.apiKeyRepo.findByApiKey(apiKey).orElse(null));
    }
}
