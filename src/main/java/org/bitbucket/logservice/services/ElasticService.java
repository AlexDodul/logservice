package org.bitbucket.logservice.services;

import lombok.RequiredArgsConstructor;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.bitbucket.logservice.repositories.ElasticsearchRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ElasticService {

    private final ElasticsearchRepo elasticsearchRepo;

    private final ElasticsearchOperations elasticsearchOperations;

    public List<ElasticEntity> readAllByKeyWords(List<String> keyWord, Pageable pageable) {
        return this.elasticsearchRepo.findAllByKeyWords(keyWord, pageable);
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

    public Iterable<ElasticEntity> insertBulk(List<ElasticEntity> elasticEntity) {
        return elasticsearchRepo.saveAll(elasticEntity);
    }

    public ElasticEntity createElasticEntity(ElasticEntity elasticEntity) {
        createElasticIndex(elasticEntity);
        return elasticsearchRepo.save(elasticEntity);
    }

    public void createElasticIndex(ElasticEntity entity) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(entity.getId())
                .withObject(entity).build();

        elasticsearchOperations.index(indexQuery, IndexCoordinates.of(entity.getApplicationName()));
    }
}
