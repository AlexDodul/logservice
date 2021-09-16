package org.bitbucket.logservice.utils;

import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.bitbucket.logservice.payload.request.BodyLogRequest;
import org.bitbucket.logservice.payload.response.LogResponse;
import org.springframework.beans.BeanUtils;

@UtilityClass
public class TransferObject {

    /*public ElasticEntity toElasticEntity(BodyLogRequest bodyLogRequest) {
        return new ElasticEntity(
                bodyLogRequest.getCreatedAt(),
                bodyLogRequest.getPlatformName(),
                bodyLogRequest.getKeywords(),
                bodyLogRequest.getBody()
        );
    }*/

    /*public LogsResponse toLogResponse(List<ElasticEntity> elasticEntity) {
        return new LogsResponse(
            elasticEntity.stream().map(ElasticEntity::getBodyLog).collect(Collectors.toList())
        );
    }*/

    public List<LogResponse> toLogResponse(List<ElasticEntity> elasticEntity) {
        return elasticEntity.stream().map(entity -> new LogResponse(
            entity.getCreatedAt(),
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
