package org.bitbucket.logservice.services;

import com.slack.api.bolt.handler.builtin.SlashCommandHandler;
import com.slack.api.bolt.response.ResponseTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityExistsException;
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
          repo.findByApplicationName(req.getPayload().getText()).orElseThrow(EntityExistsException::new);
      if (apiKeyEntity.getApplicationName().isEmpty() || apiKeyEntity.getApplicationName() == null){
        return ctx.ack(r -> r.text(
            "This application doesn't exist '" + req.getPayload().getText() +
                '\'').responseType(ResponseTypes.inChannel));
      }
      List<String> channelId = apiKeyEntity.getChannelId();

      if (Objects.isNull(channelId)) {
        channelId = new ArrayList<>();
      }
      if (channelId.contains(req.getPayload().getChannelId())) {
        return ctx.ack(r -> r.text(
            "This channel already subscribe on this application '" + req.getPayload().getText() +
                '\'').responseType(ResponseTypes.inChannel));
      }
      channelId.add(req.getPayload().getChannelId());
      apiKeyEntity.setChannelId(channelId);
      repo.save(apiKeyEntity);

      System.out.println(req.getPayload().getText());
      System.out.println(req.getPayload().getChannelId());
      return ctx.ack(r -> r.text("Application '" + req.getPayload().getText() + "' registered successfully" )
          .responseType(ResponseTypes.inChannel));
    };
  }
}
