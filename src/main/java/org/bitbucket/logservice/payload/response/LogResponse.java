package org.bitbucket.logservice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LogResponse {
    List<String> body;
}
