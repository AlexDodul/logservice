package org.bitbucket.logservice.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bitbucket.logservice.entity.ApiKeyEntity;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.bitbucket.logservice.payload.request.BodyLogRequest;
import org.bitbucket.logservice.payload.request.FilterRequest;
import org.bitbucket.logservice.repositories.ApiKeyRepo;
import org.bitbucket.logservice.repositories.ElasticsearchRepo;
import org.bitbucket.logservice.utils.DateUtils;
import org.bitbucket.logservice.utils.TransferObject;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
public class ElasticService {
  private static final int CHARACTER_LIMIT_FOR_MESSAGE = 7000;

  private final ElasticsearchRepo elasticsearchRepo;

  private final SlackService slackService;

  private final FilesUploadService filesUploadService;

  private final ApiKeyRepo apiKeyRepo;

  public List<ElasticEntity> readAllByKeyWords(List<String> keyWord, Pageable pageable,
                                               String appName) {
    return elasticsearchRepo.findAllByKeyWordsAndApplicationName(keyWord, appName, pageable);
  }

  public List<ElasticEntity> readAllByKeyWordsAndDate(
      FilterRequest filterRequest,
      Pageable pageable,
      String appName
  ) {
    Long createdAtFrom = DateUtils.convertToEpoch(filterRequest.getCreatedAtFrom());
    Long createdAtTo = DateUtils.convertToEpoch(filterRequest.getCreatedAtTo());
    String messageLevel = filterRequest.getMessageLevel();
    List<String> keyWords = filterRequest.getKeyWords();

    if (createdAtFrom != null && createdAtTo == null && keyWords == null && messageLevel != null) {
      return elasticsearchRepo.findAllByCreatedAtAfterAndMessageLevelAndApplicationName(
          createdAtFrom,
          messageLevel,
          appName,
          pageable
      );
    }

    if (createdAtFrom == null && createdAtTo != null && keyWords == null && messageLevel != null) {
      return elasticsearchRepo.findAllByCreatedAtBeforeAndMessageLevelAndApplicationName(
          createdAtTo,
          messageLevel,
          appName,
          pageable
      );
    }

    if (createdAtFrom != null && createdAtTo == null && keyWords != null && messageLevel != null) {
      return elasticsearchRepo.findAllByCreatedAtAfterAndMessageLevelAndKeyWordsAndApplicationName(
          createdAtFrom,
          messageLevel,
          keyWords,
          appName,
          pageable
      );
    }

    if (createdAtFrom == null && createdAtTo == null && keyWords != null && messageLevel != null) {
      return elasticsearchRepo.findAllByMessageLevelAndKeyWordsAndApplicationName(
          messageLevel,
          keyWords,
          appName,
          pageable
      );
    }

    if (createdAtFrom == null && createdAtTo != null && keyWords != null && messageLevel != null) {
      return elasticsearchRepo.findAllByCreatedAtBeforeAndMessageLevelAndKeyWordsAndApplicationName(
          createdAtTo,
          messageLevel,
          keyWords,
          appName,
          pageable
      );
    }

    if (createdAtFrom != null && createdAtTo != null && keyWords != null && messageLevel == null) {
      return elasticsearchRepo.findAllByCreatedAtBetweenAndKeyWordsAndApplicationName(
          createdAtFrom,
          createdAtTo,
          keyWords,
          appName,
          pageable
      );
    }

    if (createdAtFrom != null && createdAtTo != null && keyWords != null) {
      return elasticsearchRepo
          .findAllByCreatedAtBetweenAndMessageLevelAndKeyWordsAndApplicationName(
              createdAtFrom,
              createdAtTo,
              messageLevel,
              keyWords,
              appName,
              pageable
          );
    }

    if (createdAtFrom != null && createdAtTo != null && messageLevel != null) {
      return elasticsearchRepo
          .findAllByCreatedAtBetweenAndMessageLevelAndApplicationName(
              createdAtFrom,
              createdAtTo,
              messageLevel,
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

    if (createdAtFrom != null && createdAtTo != null) {
      return elasticsearchRepo.findAllByCreatedAtBetweenAndApplicationName(
          createdAtFrom,
          createdAtTo,
          appName,
          pageable
      );
    }

    if (createdAtFrom == null && createdAtTo == null && keyWords == null && messageLevel != null) {
      return elasticsearchRepo.findAllByMessageLevelAndApplicationName(
          messageLevel,
          appName,
          pageable
      );
    }

    return elasticsearchRepo.findAllByKeyWordsAndApplicationName(keyWords, appName, pageable);
  }

  public ElasticEntity saveLogInTable(BodyLogRequest bodyLogRequest, String appName) {
    ElasticEntity entity = TransferObject.dtoToEntity(bodyLogRequest);
    entity.setApplicationName(appName);

    ApiKeyEntity apiKeyEntity = apiKeyRepo.findByApplicationName(appName).orElseThrow();
    List<String> channelsId = apiKeyEntity.getChannelId();

    if (channelsId != null) {
      if (bodyLogRequest.getBodyLog().length() <= CHARACTER_LIMIT_FOR_MESSAGE) {
        for (String channelId : channelsId) {

          slackService.sendMessageToSlack(entity, channelId);
        }
      } else {
        for (String channelId : channelsId) {
          slackService.sendFileDescription(entity, channelId);
          filesUploadService.sendFile(entity.toString(), channelId);
        }
      }
    }
    return elasticsearchRepo.save(entity);
  }

  public List<ElasticEntity> readAllLogs() {
    return elasticsearchRepo.findAll();
  }

  @Scheduled(cron = "@daily")
  public void removeOldDate() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -5);
    Date previousMonth = cal.getTime();
    elasticsearchRepo.deleteAllByCreatedAtBefore(previousMonth.getTime());
    log.info("Delete old data");
  }
}
