package org.bitbucket.logservice.repositories;

import org.bitbucket.logservice.entity.MongoTest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoRepo extends MongoRepository<MongoTest, String> {
    MongoTest findByKeyWords(List<String> keyWords);

    List<MongoTest> findAllByKeyWordsContains(List<String> keyWords);

    MongoTest findByKeyWordsContains(List<String> keyWords);
}
