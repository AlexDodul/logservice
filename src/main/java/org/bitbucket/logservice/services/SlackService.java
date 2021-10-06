package org.bitbucket.logservice.services;

import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Attachment;
import java.io.IOException;
import java.util.Date;
import java.util.List;
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
        + "Message Level - " + message.getMessageLevel()
        + NEW_LINE
        + "Key Words - " + message.getKeyWords()
        + NEW_LINE
        + "Body Log - " + message.getBodyLog(), channelId, message.getMessageLevel());
  }

  private void process(String message, String channelId, String messageLevel) {
    App app = new App();
    try {
      app.client().chatPostMessage(r -> r.attachments(
          List.of(Attachment.builder().fallback("Text").color(getColor(messageLevel)).text(message)
              .build()))
          .token("zSvbmQH5WVfJVPt9PVxdUShC")
          .channel(channelId)

      );
    } catch (IOException | SlackApiException e) {
      e.printStackTrace();
    }
  }

  public String getColor(String messageLevel) {
    switch (messageLevel) {
      case "INFO":
        return "#32CD32";
      case "DEBUG":
        return "#1E90FF";
      case "WARN":
        return "#FFD700";
      case "ERROR":
        return "#FF0000";
      default:
        return "#DDDDDD";
    }
  }

  public void sendFileDescription(ElasticEntity entity, String channelId) {
    App app = new App();
    try {
      app.client().chatPostMessage(r -> r.attachments(
          List.of(Attachment.builder().fallback("Text").color(getColor(entity.getMessageLevel()))
              .text("File")
              .build()))
          .token(botToken)
          .channel(channelId)
      );
    } catch (IOException | SlackApiException e) {
      e.printStackTrace();
    }
  }
}
