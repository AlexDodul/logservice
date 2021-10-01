package org.bitbucket.logservice.payload.request;

import java.util.List;
import lombok.Data;

@Data
public class KeyWordsRequest {
  private List<String> keyWords;
}
