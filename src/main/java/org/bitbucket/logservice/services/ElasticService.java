package org.bitbucket.logservice.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.bitbucket.logservice.repositories.ElasticsearchRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElasticService {

    private final ElasticsearchRepo elasticsearchRepo;

    private final ElasticsearchOperations elasticsearchOperations;

    public List<ElasticEntity> readAllByKeyWords(List<String> keyWord, Pageable pageable, String appName) {
        return this.elasticsearchRepo.findAllByKeyWordsAndApplicationName(keyWord, appName, pageable);
    }

    public void deleteAll() {
        this.elasticsearchRepo.deleteAll();
    }

    public Page<ElasticEntity> findAllPageable(Pageable pageable) {
        return this.elasticsearchRepo.findAll(pageable);
    }

    public List<ElasticEntity> findAll() {
        return this.elasticsearchRepo.findAll();
    }

    public Iterable<ElasticEntity> saveListOfLogs(List<ElasticEntity> elasticEntity) {
        return elasticsearchRepo.saveAll(elasticEntity);
    }

    public ElasticEntity saveLogInTable(ElasticEntity elasticEntity, String appName) {
        elasticEntity.setApplicationName(appName);
        return elasticsearchRepo.save(elasticEntity);
    }
}
