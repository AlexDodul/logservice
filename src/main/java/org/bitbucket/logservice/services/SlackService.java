package org.bitbucket.logservice.services;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackService {

  private static final String NEW_LINE = "\n";

  @Value("${slack.webhook}")
  private String urlSlackWebHook;

  public void sendMessageToSlack(ElasticEntity message) {
    process("Create At - " + message.getCreatedAt()
        + NEW_LINE
        + "Key Words - " + message.getKeyWords()
        + NEW_LINE
        + "Body Log - " + message.getBodyLog());
  }

  private void process(String message) {
    Payload payload = Payload.builder()
        .text(message)
        .build();
    try {
      Slack.getInstance().send(
          urlSlackWebHook, payload);
    } catch (IOException e) {
      log.error("Unexpected Error! WebHook:" + urlSlackWebHook);
    }
  }
}
