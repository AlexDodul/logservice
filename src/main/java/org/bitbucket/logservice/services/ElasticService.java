package org.bitbucket.logservice.services;

import lombok.RequiredArgsConstructor;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.bitbucket.logservice.repositories.ElasticsearchRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ElasticService {

    private final ElasticsearchRepo elasticsearchRepo;

    public ElasticEntity create(ElasticEntity elasticTest){
        return elasticsearchRepo.save(elasticTest);
    }

    public List<ElasticEntity> readAllByKeyWords(List<String> keyWord, Pageable pageable) {
        List<ElasticEntity> n = new ArrayList<>();
        return this.elasticsearchRepo.findAllByKeyWords(keyWord, pageable);
    }

    public void deleteAll() {
        this.elasticsearchRepo.deleteAll();
    }

    public Page<ElasticEntity> findAllPageable (Pageable pageable){
        return this.elasticsearchRepo.findAll(pageable);
    }

    public Iterable<ElasticEntity> insertBulk(List<ElasticEntity> elasticEntity) {
        return elasticsearchRepo.saveAll(elasticEntity);
    }
}
