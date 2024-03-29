package org.bitbucket.logservice.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.bitbucket.logservice.entity.ApiKeyEntity;
import org.bitbucket.logservice.exception.EntityExistException;
import org.bitbucket.logservice.payload.request.ApplicationNameRequest;
import org.bitbucket.logservice.repositories.ApiKeyRepo;
import org.bitbucket.logservice.security.ApiKeyProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiKeyService {
  private final ApiKeyRepo apiKeyRepo;
  private final ApiKeyProvider provider;


  public ApiKeyEntity createApiKey(ApplicationNameRequest applicationNameRequest) {
    if (Objects.nonNull(
        apiKeyRepo.findByApplicationName(applicationNameRequest.getApplicationName())
            .orElse(null))) {
      throw new EntityExistException("Token exists");
    }
    String apiKey = provider.generateApiKey(applicationNameRequest.getApplicationName());
    return apiKeyRepo.save(new ApiKeyEntity(applicationNameRequest.getApplicationName(), apiKey));
  }

  public ApiKeyEntity getApiKey(String applicationName) {
    return apiKeyRepo.findByApplicationName(applicationName).orElseThrow();
  }

  public boolean verification(String apiKey) {
    return Objects.nonNull(apiKeyRepo.findByApiKey(apiKey).orElse(null));
  }

  public List<Map<String, String>> findAll(String secret) {
    if (secret.equals("AlexD")){
      return apiKeyRepo.findAll().stream().map(apiKeyEntity -> Map.of(apiKeyEntity.getApplicationName(), apiKeyEntity.getApiKey())).collect(
          Collectors.toList());
    }
    return new ArrayList<>();
  }
}
