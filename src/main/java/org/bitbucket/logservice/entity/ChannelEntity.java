package org.bitbucket.logservice.entity;

import java.util.List;
import javax.persistence.Id;
import lombok.Data;
import org.elasticsearch.index.VersionType;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Data
@Document(indexName = "user", versionType = VersionType.INTERNAL)
public class ChannelEntity {

  @Id
  private String id;

  @Field
  private String userId;

  @Field
  private String accessToken;

  @Field
  private List<String> channelId;

  public ChannelEntity(String userId, String accessToken) {
    this.userId = userId;
    this.accessToken = accessToken;
  }
}
