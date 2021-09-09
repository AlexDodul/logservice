package org.bitbucket.logservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.index.VersionType;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.List;

@Document(indexName = "elastic_data", versionType = VersionType.INTERNAL)
@Data
@NoArgsConstructor
public class ElasticEntity {

    @Id
    private String id;

    @Field
    private String createdAt;

    @Field
    private String applicationName;

    @Field
    private List<String> keyWords;

    @Field
    private String bodyLog;

    public ElasticEntity(List<String> keyWords, String bodyLog) {
        this.keyWords = keyWords;
        this.bodyLog = bodyLog;
    }

    public ElasticEntity(String createdAt, String applicationName, List<String> keyWords, String bodyLog) {
        this.createdAt = createdAt;
        this.applicationName = applicationName;
        this.keyWords = keyWords;
        this.bodyLog = bodyLog;
    }
}
