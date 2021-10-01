package org.bitbucket.logservice.utils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.bitbucket.logservice.payload.request.BodyLogRequest;
import org.bitbucket.logservice.payload.response.LogResponse;
import org.springframework.beans.BeanUtils;

@UtilityClass
public class TransferObject {

  public List<LogResponse> toLogResponse(List<ElasticEntity> elasticEntity) {
    return elasticEntity.stream().map(entity -> new LogResponse(
        new Date(entity.getCreatedAt()),
        entity.getMessageLevel(),
        entity.getKeyWords(),
        entity.getBodyLog()
    )).collect(Collectors.toList());
  }

  public static ElasticEntity dtoToEntity(BodyLogRequest bodyLogRequest) {
    ElasticEntity elasticEntity = new ElasticEntity();
    BeanUtils.copyProperties(bodyLogRequest, elasticEntity);
    return elasticEntity;
  }
}
