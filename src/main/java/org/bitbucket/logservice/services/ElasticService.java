package org.bitbucket.logservice.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.bitbucket.logservice.payload.request.BodyLogRequest;
import org.bitbucket.logservice.payload.request.FilterRequest;
import org.bitbucket.logservice.repositories.ElasticsearchRepo;
import org.bitbucket.logservice.utils.DateUtils;
import org.bitbucket.logservice.utils.FilesUpload;
import org.bitbucket.logservice.utils.TransferObject;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
public class ElasticService {

  private final ElasticsearchRepo elasticsearchRepo;

  private final SlackService slackService;

  private final FilesUpload filesUpload;

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

  public ElasticEntity saveLogInTable(BodyLogRequest bodyLogRequest, String appName) {
    ElasticEntity entity = TransferObject.dtoToEntity(bodyLogRequest);
    entity.setApplicationName(appName);
    if (bodyLogRequest.getBodyLog().length() <= 3500){

      slackService.sendMessageToSlack(entity);
    } else if (bodyLogRequest.getBodyLog().length() > 3500){
      filesUpload.sendFile(entity.toString());
    }
    System.out.println(bodyLogRequest.getBodyLog().length());
    return elasticsearchRepo.save(entity);
  }

  public List<ElasticEntity> readAllLogs() {
    return elasticsearchRepo.findAll();
  }

  public void deleteRequest() {
    DeleteRequest request = new DeleteRequest("elastic_data", "id");
    request.timeout(TimeValue.timeValueHours(2));
  }

  // "@hourly"
  @Scheduled(cron = "@daily")
  public void removeOldDate() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -60);
    Date previousMonth = cal.getTime();
    elasticsearchRepo.deleteAllByCreatedAtBefore(previousMonth.getTime());
    log.info("Delete old data");
  }
}
