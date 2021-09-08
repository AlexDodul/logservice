package org.bitbucket.logservice.repositories;

import org.bitbucket.logservice.entity.APIKeyEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface APIKeyRepo extends ElasticsearchRepository<APIKeyEntity, String> {

    Optional<APIKeyEntity> findByApplicationName(String applicationName);

}
