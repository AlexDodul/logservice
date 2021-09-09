package org.bitbucket.logservice.payload.request;

import lombok.Data;

import java.util.List;

@Data
public class BodyLogRequest {
    private String created_at;
    private String platform_name;
    private List<String> keywords;
    private String body;
}
