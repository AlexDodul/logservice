package org.bitbucket.logservice.repositories;

import java.util.Optional;
import org.bitbucket.logservice.entity.ApiKeyEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ApiKeyRepo extends ElasticsearchRepository<ApiKeyEntity, String> {

  Optional<ApiKeyEntity> findByApplicationName(String applicationName);

  Optional<ApiKeyEntity> findByApiKey(String apiKey);

}
