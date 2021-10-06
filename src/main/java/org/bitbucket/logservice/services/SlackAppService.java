package org.bitbucket.logservice.services;

import com.slack.api.bolt.handler.builtin.SlashCommandHandler;
import com.slack.api.bolt.response.ResponseTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.bitbucket.logservice.entity.ApiKeyEntity;
import org.bitbucket.logservice.repositories.ApiKeyRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlackAppService {

  private final ApiKeyRepo repo;

  public SlashCommandHandler reg() {
    return (req, ctx) -> {
      ApiKeyEntity apiKeyEntity =
          repo.findByApiKey(req.getPayload().getText()).orElse(null);
      if (Objects.isNull(apiKeyEntity)) {
        return ctx.ack(r -> r.text(
            "This application doesn't exist '" + req.getPayload().getText() +
                '\'').responseType(ResponseTypes.inChannel));
      }
      List<String> channelId = apiKeyEntity.getChannelId();

      if (Objects.isNull(channelId)) {
        channelId = new ArrayList<>();
      }
      req.getPayload().getToken();
      if (channelId.contains(req.getPayload().getChannelId())) {
        return ctx.ack(r -> r.text(
            "This channel already subscribe on this application '  " + req.getPayload().getUserId() +
                apiKeyEntity.getApplicationName() +
                '\'').responseType(ResponseTypes.inChannel));
      }
      channelId.add(req.getPayload().getChannelId());
      apiKeyEntity.setChannelId(channelId);
      repo.save(apiKeyEntity);

      Object botId = req.getContext().getBotId();
      Object token = req.getContext().getBotToken();

      System.out.println(botId + " -  " + token);
      ctx.ack(r -> r.text(botId + " -  " + token));

      return ctx.ack(r -> r
          .text("Application '" + apiKeyEntity.getApplicationName() +
              "' registered successfully     " + req.getPayload().getApiAppId())
          .responseType(ResponseTypes.inChannel));
    };
  }
}
