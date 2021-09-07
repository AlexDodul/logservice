package org.bitbucket.logservice.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Document
@Data
public class MongoTest {

    private String _id;

    @Field
    private Date createdAt = new Date();

    @Field
    private String applicationName;

    @Field
    //@Indexed (name = "keyName")
    private List<String> keyWords;

    @Field
    private String bodyLog;

    public MongoTest(List<String> keyWords, String bodyLog) {
        this.keyWords = keyWords;
        this.bodyLog = bodyLog;
    }
}
