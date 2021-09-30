package org.bitbucket.logservice.config;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import lombok.RequiredArgsConstructor;
import org.bitbucket.logservice.services.SlackAppService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SlackAppConfig {

  private final SlackAppService slackAppService;

  @Bean
  public App initSlackApp() {
    AppConfig appConfig = new AppConfig();
    appConfig.setSigningSecret("419b9fc3ca6b61bdb270aaa813c8c3ec");
    App app = new App(appConfig);
    System.out.println(app.config().getSingleTeamBotToken());
    app.command("/subscribe", slackAppService.reg());
    return app;
  }

}
