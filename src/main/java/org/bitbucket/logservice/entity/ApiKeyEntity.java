package org.bitbucket.logservice.entity;

import java.util.List;
import javax.persistence.Id;
import lombok.Data;
import org.elasticsearch.index.VersionType;
import org.joda.time.Instant;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Data
@Document(indexName = "api-key", versionType = VersionType.INTERNAL)
public class ApiKeyEntity {

  @Id
  private String id;

  @Field
  private Long createdAt = new Instant().getMillis();

  @Field
  private String applicationName;

  @Field
  private String apiKey;

  @Field
  private List<String> channelId;

  public ApiKeyEntity(String applicationName, String apiKey) {
    this.applicationName = applicationName;
    this.apiKey = apiKey;
  }

}
