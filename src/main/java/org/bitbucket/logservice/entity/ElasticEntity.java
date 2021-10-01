package org.bitbucket.logservice.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.index.VersionType;
import org.joda.time.Instant;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(indexName = "elastic_data", versionType = VersionType.INTERNAL)
@Data
@NoArgsConstructor
public class ElasticEntity {

  @Id
  private String id;

  @Field
  private Long createdAt = new Instant().getMillis();

  @Field
  private String applicationName;

  @Field
  private String messageLevel;

  @Field
  private List<String> keyWords;

  @Field
  private String bodyLog;

  public ElasticEntity(List<String> keyWords, String bodyLog) {
    this.keyWords = keyWords;
    this.bodyLog = bodyLog;
  }

  public ElasticEntity(String applicationName, List<String> keyWords,
                       String bodyLog) {
    this.applicationName = applicationName;
    this.keyWords = keyWords;
    this.bodyLog = bodyLog;
  }

  @Override
  public String toString() {
    return "Create At - " + new Date(createdAt) +
        "\nMessage Level - " + messageLevel +
        "\nKey Words - " + keyWords +
        "\nBody Log - " + bodyLog;
  }
}
