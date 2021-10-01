package org.bitbucket.logservice.repositories;

import java.util.List;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface ElasticsearchRepo extends ElasticsearchRepository<ElasticEntity, String> {

  List<ElasticEntity> findAllByKeyWordsAndApplicationName(
      List<String> keyWords,
      String applicationName,
      Pageable pageable
  );

  List<ElasticEntity> findAll();

  List<ElasticEntity> findAllByCreatedAtBetweenAndApplicationName(
      Long createdAtFrom,
      Long createdAtTo,
      String applicationName,
      Pageable pageable
  );

  List<ElasticEntity> findAllByCreatedAtBetweenAndMessageLevelAndApplicationName(
      Long createdAtFrom,
      Long createdAtTo,
      String messageLevel,
      String applicationName,
      Pageable pageable
  );

  List<ElasticEntity> findAllByCreatedAtAfterAndMessageLevelAndApplicationName(
      Long createdAtFrom,
      String messageLevel,
      String applicationName,
      Pageable pageable
  );

  List<ElasticEntity> findAllByCreatedAtBeforeAndMessageLevelAndApplicationName(
      Long createdAtTo,
      String messageLevel,
      String applicationName,
      Pageable pageable
  );

  List<ElasticEntity> findAllByCreatedAtBetweenAndMessageLevelAndKeyWordsAndApplicationName(
      Long createdFrom,
      Long createdTo,
      String messageLevel,
      List<String> keyWords,
      String applicationName,
      Pageable pageable
  );

  List<ElasticEntity> findAllByCreatedAtAfterAndMessageLevelAndKeyWordsAndApplicationName(
      Long createdAtFrom,
      String messageLevel,
      List<String> keyWords,
      String applicationName,
      Pageable pageable
  );

  List<ElasticEntity> findAllByCreatedAtBeforeAndMessageLevelAndKeyWordsAndApplicationName(
      Long createdAtTo,
      String messageLevel,
      List<String> keyWords,
      String applicationName,
      Pageable pageable
  );

  List<ElasticEntity> findAllByCreatedAtBetweenAndKeyWordsAndApplicationName(
      Long createdFrom,
      Long createdTo,
      List<String> keyWords,
      String applicationName,
      Pageable pageable
  );

  List<ElasticEntity> findAllByKeyWordsAndCreatedAtAfterAndApplicationName(
      List<String> keyWords,
      Long createdAtFrom,
      String applicationName,
      Pageable pageable
  );

  List<ElasticEntity> findAllByKeyWordsAndCreatedAtBeforeAndApplicationName(
      List<String> keyWords,
      Long createdAtTo,
      String applicationName,
      Pageable pageable
  );

  @Modifying
  void deleteAllByCreatedAtBefore(Long oldDate);
}
