package org.bitbucket.logservice.payload.request;

import java.util.List;
import lombok.Data;

@Data
public class BodyLogRequest {

  private List<String> keyWords;

  private String bodyLog;
}
