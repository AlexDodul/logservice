package org.bitbucket.logservice.payload.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogsResponse {

  List<String> body;
}
