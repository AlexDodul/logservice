package org.bitbucket.logservice.controllers;


import com.slack.api.bolt.App;
import com.slack.api.bolt.servlet.SlackAppServlet;
import javax.servlet.annotation.WebServlet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebServlet("/slack/oauth")
public class SlackAppController extends SlackAppServlet {
  public SlackAppController(App app) {
    super(app.asOAuthApp(true));
    log.error("---=== /slack/oauth ===---");
  }
}
