package org.bitbucket.logservice.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.bitbucket.logservice.payload.request.BodyLogRequest;
import org.bitbucket.logservice.payload.request.FilterRequest;
import org.bitbucket.logservice.repositories.ElasticsearchRepo;
import org.bitbucket.logservice.utils.DateUtils;
import org.bitbucket.logservice.utils.TransferObject;
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
    List<String> keyWords = filterRequest.getKeyWords();
    Long createdAtFrom = DateUtils.convertToEpoch(filterRequest.getCreatedAtFrom());
    Long createdAtTo = DateUtils.convertToEpoch(filterRequest.getCreatedAtTo());

    if (createdAtFrom != null && createdAtTo != null && keyWords != null) {
      return elasticsearchRepo.findAllByCreatedAtBetweenAndKeyWordsAndApplicationName(
          createdAtFrom,
          createdAtTo,
          keyWords,
          appName,
          pageable
      );
    }
    if (createdAtFrom == null && createdAtTo != null && keyWords != null) {
      return elasticsearchRepo.findAllByKeyWordsAndCreatedAtBeforeAndApplicationName(
          keyWords,
          createdAtTo,
          appName,
          pageable
      );
    }
    if (createdAtFrom != null && createdAtTo == null && keyWords != null) {
      return elasticsearchRepo.findAllByKeyWordsAndCreatedAtAfterAndApplicationName(
          keyWords,
          createdAtFrom,
          appName,
          pageable
      );
    }
    if (createdAtFrom != null && createdAtTo != null && keyWords == null) {
      return elasticsearchRepo.findAllByCreatedAtBetweenAndApplicationName(
          createdAtFrom,
          createdAtTo,
          appName,
          pageable
      );
    }
    return elasticsearchRepo.findAllByKeyWordsAndApplicationName(keyWords, appName, pageable);
  }

  public void deleteAll() {
    elasticsearchRepo.deleteAll();
  }

/*  public ElasticEntity saveLogInTable(ElasticEntity elasticEntity, String appName) {
    elasticEntity.setApplicationName(appName);
    return elasticsearchRepo.save(elasticEntity);
  }*/

  public ElasticEntity saveLogInTable(BodyLogRequest bodyLogRequest, String appName) {
    ElasticEntity entity = TransferObject.dtoToEntity(bodyLogRequest);
    entity.setApplicationName(appName);
    return elasticsearchRepo.save(entity);
  }

  public List<ElasticEntity> readAllLogs() {
    return elasticsearchRepo.findAll();
  }
}
