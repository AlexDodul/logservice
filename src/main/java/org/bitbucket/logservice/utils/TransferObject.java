package org.bitbucket.logservice.utils;

import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.bitbucket.logservice.payload.request.BodyLogRequest;
import org.bitbucket.logservice.payload.response.LogsResponse;

@UtilityClass
public class TransferObject {

    public ElasticEntity toElasticEntity(BodyLogRequest bodyLogRequest) {
        return new ElasticEntity(
                bodyLogRequest.getCreatedAt(),
                bodyLogRequest.getPlatformName(),
                bodyLogRequest.getKeywords(),
                bodyLogRequest.getBody()
        );
    }

    public LogsResponse toLogResponse(List<ElasticEntity> elasticEntity) {
        return new LogsResponse(
                elasticEntity.stream().map(ElasticEntity::getBodyLog).collect(Collectors.toList())
        );
    }
}
