package org.bitbucket.logservice.payload.request;

import java.util.List;
import lombok.Data;

@Data
public class FilterRequest {

  private String createdAtFrom;

  private String createdAtTo;

  private List<String> keyWords;
}
