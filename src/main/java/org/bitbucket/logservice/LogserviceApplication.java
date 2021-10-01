package org.bitbucket.logservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableMongoRepositories(basePackages = "org.bitbucket.logservice.repositories")
@EnableElasticsearchRepositories(basePackages = "org.bitbucket.logservice.repositories")
@ServletComponentScan
public class LogserviceApplication {

  public static void main(String[] args) {
    SpringApplication.run(LogserviceApplication.class, args);
  }

}
