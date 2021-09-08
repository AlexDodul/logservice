package org.bitbucket.logservice.entity;

import lombok.Data;
import org.elasticsearch.index.VersionType;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Date;

@Data
@Document(indexName = "api-key", versionType = VersionType.INTERNAL)
public class APIKeyEntity {

    @Id
    private String id;

    @Field
    private Date createdAt = new Date();

    @Field
    private String applicationName;

    @Field
    private String apiKey;

    public APIKeyEntity(String applicationName, String apiKey) {
        this.applicationName = applicationName;
        this.apiKey = apiKey;
    }
}
