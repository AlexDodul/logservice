package org.bitbucket.logservice.repositories;

import org.bitbucket.logservice.entity.ElasticEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ElasticsearchRepo extends ElasticsearchRepository<ElasticEntity, String> {

    List<ElasticEntity> findAllByKeyWords(List<String> keyWords, Pageable pageable);

    List<ElasticEntity> findAll();

}
