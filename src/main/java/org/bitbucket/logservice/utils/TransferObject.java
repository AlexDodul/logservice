package org.bitbucket.logservice.utils;

import lombok.experimental.UtilityClass;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.bitbucket.logservice.payload.request.BodyLogRequest;
import org.bitbucket.logservice.payload.response.LogResponse;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class TransferObject {

    public ElasticEntity toElasticEntity(BodyLogRequest bodyLogRequest) {
        return new ElasticEntity(
                bodyLogRequest.getCreated_at(),
                bodyLogRequest.getPlatform_name(),
                bodyLogRequest.getKeywords(),
                bodyLogRequest.getBody()
        );
    }

    public LogResponse toLogResponse(List<ElasticEntity> elasticEntity) {
        return new LogResponse(
                elasticEntity.stream().map(ElasticEntity::getBodyLog).collect(Collectors.toList())
        );
    }
}
