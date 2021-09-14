package org.bitbucket.logservice.repositories;

import java.util.List;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticsearchRepo extends ElasticsearchRepository<ElasticEntity, String> {

  List<ElasticEntity> findAllByKeyWordsAndApplicationName(
      List<String> keyWords,
      String applicationName,
      Pageable pageable
  );

  List<ElasticEntity> findAll();

  List<ElasticEntity> findAllByCreatedAtBetween(
      String createdAtFrom,
      String createdAtTo
  );

  List<ElasticEntity> findAllByCreatedAtBetweenAndApplicationName(
      String createdAtFrom,
      String createdAtTo,
      String applicationName,
      Pageable pageable
  );

  List<ElasticEntity> findAllByCreatedAtBetweenAndKeyWordsAndApplicationName(
      String createdFrom,
      String createdTo,
      List<String> keyWords,
      String applicationName,
      Pageable pageable
  );

  List<ElasticEntity> findAllByKeyWordsAndCreatedAtAfterAndApplicationName(
      List<String> keyWords,
      String createdAtFrom,
      String applicationName,
      Pageable pageable
  );

  List<ElasticEntity> findAllByKeyWordsAndCreatedAtBeforeAndApplicationName(
      List<String> keyWords,
      String createdAtTo,
      String applicationName,
      Pageable pageable
  );

}
