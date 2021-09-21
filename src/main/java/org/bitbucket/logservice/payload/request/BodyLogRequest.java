package org.bitbucket.logservice.payload.request;

import java.util.List;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class BodyLogRequest {

  private List<String> keyWords;

  @Size(max = 10000, message = "The maximum body log value is 10000")
  private String bodyLog;
}
