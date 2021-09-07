package org.bitbucket.logservice.entity;

import lombok.Data;
import org.elasticsearch.index.VersionType;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Document(indexName = "elastic_data", versionType = VersionType.INTERNAL)
@Data
public class ElasticEntity {

    @Id
    private String id;

    @Field
    private Date createdAt = new Date();

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
}
