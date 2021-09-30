package org.bitbucket.logservice.payload.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogResponse {
  private Long createdAt;

  private List<String> keyWords;

  private String messageLevel;

  private String bodyLog;
}
