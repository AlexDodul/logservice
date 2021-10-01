package org.bitbucket.logservice.services;

import java.nio.charset.StandardCharsets;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FilesUploadService {

  @Value("${slack.token}")
  private String botToken;

  public void sendFile(String log, String channelId) {
    try {
      String url = "https://slack.com/api/files.upload";
      HttpClient httpclient = HttpClientBuilder.create().disableContentCompression().build();
      HttpPost httppost = new HttpPost(url);
      MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
      reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
      reqEntity
          .addBinaryBody("file", log.getBytes(StandardCharsets.UTF_8), ContentType.DEFAULT_BINARY,
              "example");
      reqEntity.addTextBody("channels", channelId, ContentType.DEFAULT_BINARY);
      reqEntity.addTextBody("token", botToken, ContentType.DEFAULT_BINARY);
      reqEntity.addTextBody("media", "file", ContentType.DEFAULT_BINARY);

      httppost.setEntity(reqEntity.build());
      HttpResponse execute = httpclient.execute(httppost);
      System.out.println(execute.getStatusLine().getReasonPhrase());
      System.out.println(execute.getStatusLine().getStatusCode());
    } catch (Exception ec) {
      System.out.println(ec);
    }
  }
}
