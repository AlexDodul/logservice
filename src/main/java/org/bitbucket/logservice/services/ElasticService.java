package org.bitbucket.logservice.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.bitbucket.logservice.payload.request.FilterRequest;
import org.bitbucket.logservice.repositories.ElasticsearchRepo;
import org.bitbucket.logservice.utils.DateUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElasticService {

  private final ElasticsearchRepo elasticsearchRepo;

  public List<ElasticEntity> readAllByKeyWords(List<String> keyWord, Pageable pageable,
                                               String appName) {
    return elasticsearchRepo.findAllByKeyWordsAndApplicationName(keyWord, appName, pageable);
  }

  public List<ElasticEntity> readAllByKeyWordsAndDate(
      FilterRequest filterRequest,
      Pageable pageable,
      String appName
  ) {
    List<String> keywords = filterRequest.getKeywords();
    String createdAtFrom = DateUtils.convertToEpoch(filterRequest.getCreatedAtFrom());
    String createdAtTo = DateUtils.convertToEpoch(filterRequest.getCreatedAtTo());

    if (createdAtFrom != null && createdAtTo != null && keywords != null) {
      return elasticsearchRepo.findAllByCreatedAtBetweenAndKeyWordsAndApplicationName(
          createdAtFrom,
          createdAtTo,
          keywords,
          appName,
          pageable
      );
    }
    if (createdAtFrom == null && createdAtTo != null && keywords != null) {
      return elasticsearchRepo.findAllByKeyWordsAndCreatedAtBeforeAndApplicationName(
          keywords,
          createdAtTo,
          appName,
          pageable
      );
    }
    if (createdAtFrom != null && createdAtTo == null && keywords != null) {
      return elasticsearchRepo.findAllByKeyWordsAndCreatedAtAfterAndApplicationName(
          keywords,
          createdAtFrom,
          appName,
          pageable
      );
    }
    if (createdAtFrom != null && createdAtTo != null && keywords == null) {
      return elasticsearchRepo.findAllByCreatedAtBetweenAndApplicationName(
          createdAtFrom,
          createdAtTo,
          appName,
          pageable
      );
    }
    return elasticsearchRepo.findAllByKeyWordsAndApplicationName(keywords, appName, pageable);
  }

  public void deleteAll() {
    elasticsearchRepo.deleteAll();
  }

  public Page<ElasticEntity> findAllPageable(Pageable pageable) {
    return elasticsearchRepo.findAll(pageable);
  }

  public List<ElasticEntity> findAll() {
    return elasticsearchRepo.findAll();
  }

  public Iterable<ElasticEntity> saveListOfLogs(List<ElasticEntity> elasticEntity) {
    return elasticsearchRepo.saveAll(elasticEntity);
  }

  public ElasticEntity saveLogInTable(ElasticEntity elasticEntity, String appName) {
    elasticEntity.setApplicationName(appName);
    return elasticsearchRepo.save(elasticEntity);
  }

  public List<ElasticEntity> findAllByDate(String at, String to) {
    return elasticsearchRepo.findAllByCreatedAtBetween(at, to);
  }
}
