package org.bitbucket.logservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableElasticsearchRepositories(basePackages = "org.bitbucket.logservice.repositories")
@ServletComponentScan
public class LogserviceApplication {

  public static void main(String[] args) {
    System.out.println("Hello, logs!");
    log.error("-=My custom message");
    SpringApplication.run(LogserviceApplication.class, args);
  }

}
