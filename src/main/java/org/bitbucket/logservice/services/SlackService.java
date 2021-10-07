package org.bitbucket.logservice.services;

import static org.bitbucket.logservice.entity.Colors.BLUE;
import static org.bitbucket.logservice.entity.Colors.GREEN;
import static org.bitbucket.logservice.entity.Colors.GREY;
import static org.bitbucket.logservice.entity.Colors.ORANGE;
import static org.bitbucket.logservice.entity.Colors.RED;

import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Attachment;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.bitbucket.logservice.repositories.ChannelRepo;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackService {

  private static final String NEW_LINE = "\n";
  private final ChannelRepo channelRepo;

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
          .token(channelRepo.findByChannelIdContains(channelId)
              .orElseThrow(() -> new EntityNotFoundException("Entity not found")).getAccessToken())
          .channel(channelId)
      );
    } catch (IOException | SlackApiException e) {
      e.printStackTrace();
    }
  }

  public String getColor(String messageLevel) {
    switch (messageLevel) {
      case "INFO":
        return GREEN.getMessage();
      case "DEBUG":
        return BLUE.getMessage();
      case "WARN":
        return ORANGE.getMessage();
      case "ERROR":
        return RED.getMessage();
      default:
        return GREY.getMessage();
    }
  }

  public void sendFileDescription(ElasticEntity entity, String channelId) {
    App app = new App();
    try {
      app.client().chatPostMessage(r -> r.attachments(
          List.of(Attachment.builder().fallback("Text").color(getColor(entity.getMessageLevel()))
              .text("File")
              .build()))
          .token(channelRepo.findByChannelIdContains(channelId)
              .orElseThrow(() -> new EntityNotFoundException("Entity not found")).getAccessToken())
          .channel(channelId)
      );
    } catch (IOException | SlackApiException e) {
      e.printStackTrace();
    }
  }
}
