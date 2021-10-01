package org.bitbucket.logservice.payload.response;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogResponse {
  private Date createdAt;

  private String messageLevel;

  private List<String> keyWords;

  private String bodyLog;
}
