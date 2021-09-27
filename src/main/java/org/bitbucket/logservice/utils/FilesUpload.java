package org.bitbucket.logservice.utils;

import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class FilesUpload {

  private final String token =
      "xapp-1-A02GFG5RK8Q-2536179931107-8786762d1cc5b799777008127153fe27bd6fcc2a17c084408e3ad7b6767383e0";

  RestTemplate restTemplate = new RestTemplate();

  public void sendFile(String file) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    headers.setBearerAuth(token); // pass generated token here
    MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
    bodyMap.add("file", file.getBytes(StandardCharsets.UTF_8));
    // convert file to ByteArrayOutputStream and pass with toByteArray() or pass with new File()
    bodyMap.add("initial_comment", "message"); // pass comments with file
    bodyMap.add("channels", "C02FF3NNQA0"); // pass channel codeID
    HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(bodyMap, headers);
    ResponseEntity<Object>
        responseEntity = restTemplate.postForEntity("https://slack.com/api/files.upload", entity,
        Object.class);
    System.out.println(
        "sendEmailWithAttachment() response status: " + responseEntity.getStatusCode());
  }

}
