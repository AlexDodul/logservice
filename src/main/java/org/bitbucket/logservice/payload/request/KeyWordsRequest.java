package org.bitbucket.logservice.payload.request;

import lombok.Data;

import java.util.List;

@Data
public class KeyWordsRequest {
    private List<String> keywords;
}
