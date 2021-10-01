package org.bitbucket.logservice.payload.request;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ApplicationNameRequest {
  @Size(min = 4, message = "The minimum application name value is 4")
  private String applicationName;
}
