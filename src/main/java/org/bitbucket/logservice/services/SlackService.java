package org.bitbucket.logservice.services;

import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import java.io.IOException;
import java.util.Date;
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

  @Value("${slack.token}")
  private String botToken;

  public void sendMessageToSlack(ElasticEntity message, String channelId) {
    Date date = new Date(message.getCreatedAt());
    process("Create At - " + date
        + NEW_LINE
        + "Key Words - " + message.getKeyWords()
        + NEW_LINE
        + "Body Log - " + message.getBodyLog(), channelId);
  }

  private void process(String message, String channelId) {
    App app = new App();
    try {
      app.client().chatPostMessage(r -> r
          .token(botToken)
          .channel(channelId)
          .text(message));
    } catch (IOException | SlackApiException e) {
      e.printStackTrace();
    }
  }
}
