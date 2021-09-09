package org.bitbucket.logservice.services;

import lombok.RequiredArgsConstructor;
import org.bitbucket.logservice.entity.APIKeyEntity;
import org.bitbucket.logservice.exception.EntityExistException;
import org.bitbucket.logservice.payload.request.ApplicationNameRequest;
import org.bitbucket.logservice.repositories.APIKeyRepo;
import org.bitbucket.logservice.security.APIKeyProvider;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class APIKeyService {
    private final APIKeyRepo apiKeyRepo;
    private final APIKeyProvider provider;

    public APIKeyEntity createApiKey(ApplicationNameRequest applicationNameRequest) {
        if (Objects.nonNull(this.apiKeyRepo.findByApplicationName(applicationNameRequest.getApplicationName()).orElse(null))) {
            throw new EntityExistException("Token exists");
        }
        String apiKey = this.provider.generateAPIKey(applicationNameRequest.getApplicationName());
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
