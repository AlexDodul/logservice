package org.bitbucket.logservice.payload.request;

import java.util.List;
import lombok.Data;

@Data
public class BodyLogRequest {

  private String createdAt;

  private String platformName;

  private List<String> keywords;

  private String body;
}
