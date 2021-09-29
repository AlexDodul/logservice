package org.bitbucket.logservice.config;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackApp {



  @Bean
  public App initSlackApp() {
    AppConfig appConfig = new AppConfig();
    appConfig.setSigningSecret("419b9fc3ca6b61bdb270aaa813c8c3ec");
    App app = new App(appConfig);
    System.out.println(app.config().getSingleTeamBotToken());
    app.command("/reg", (req, ctx) -> {
      System.out.println(req.getPayload().getText());
      System.out.println(req.getPayload().getChannelId());
      return ctx.ack(r -> r.text(req.getPayload().getText()));
    });
    return app;
  }
}
