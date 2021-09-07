package org.bitbucket.logservice.services;

import lombok.RequiredArgsConstructor;
import org.bitbucket.logservice.entity.MongoTest;
import org.bitbucket.logservice.repositories.MongoRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MongoService {
    private final MongoRepo mongoRepo;

    public MongoTest create(MongoTest mongoTest) {
        return this.mongoRepo.save(mongoTest);
    }

    public List<MongoTest> readAllByKeyWords(List<String> keyWord) {
        return this.mongoRepo.findAllByKeyWordsContains(keyWord).stream()
                .filter(mongoTest -> mongoTest.getKeyWords().containsAll(keyWord))
                .collect(Collectors.toList());
    }

    public List<MongoTest> readAll() {
        return this.mongoRepo.findAll();
    }

    public void deleteAll() {
        this.mongoRepo.deleteAll();
    }

}
