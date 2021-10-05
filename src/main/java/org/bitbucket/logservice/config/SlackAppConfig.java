package org.bitbucket.logservice.config;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import lombok.RequiredArgsConstructor;
import org.bitbucket.logservice.services.SlackAppService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SlackAppConfig {

  private final SlackAppService slackAppService;

  @Value("${slack.signing-secret}")
  private String slackSigningSecret;

  @Bean
  public App initSlackApp() {
    AppConfig appConfig = new AppConfig();
    appConfig.setSigningSecret(slackSigningSecret);
    App app = new App(appConfig);
    app.command("/subscribe", slackAppService.reg());
    return app;
  }

}
