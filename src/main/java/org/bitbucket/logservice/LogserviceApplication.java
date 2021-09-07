package org.bitbucket.logservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.sql.DataSource;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class/*, JpaRepositoriesAutoConfiguration.class, HibernateJpaAutoConfiguration.class*/})
@EnableMongoRepositories(basePackages = "org.bitbucket.logservice.repositories")
@EnableElasticsearchRepositories(basePackages = "org.bitbucket.logservice.repositories")
public class LogserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogserviceApplication.class, args);
    }

}
