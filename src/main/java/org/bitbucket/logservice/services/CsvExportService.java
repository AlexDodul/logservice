package org.bitbucket.logservice.services;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.bitbucket.logservice.payload.request.KeyWordsRequest;
import org.bitbucket.logservice.repositories.ElasticsearchRepo;
import org.bitbucket.logservice.security.ApiKeyProvider;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CsvExportService {

    private final ElasticsearchRepo elasticsearchRepo;

    private final ApiKeyProvider apiKeyProvider;

    public CsvExportService(ElasticsearchRepo elasticsearchRepo,
                            ApiKeyProvider apiKeyProvider) {
        this.elasticsearchRepo = elasticsearchRepo;
        this.apiKeyProvider = apiKeyProvider;
    }

    public void writeEmployeesToCsv(Writer writer, Pageable pageable, KeyWordsRequest keyWordsRequest, String apiKey) {
        String applicationName = apiKeyProvider.getApplicationName(apiKey);
        List<ElasticEntity> elasticEntities = elasticsearchRepo.findAllByKeyWordsAndApplicationName(keyWordsRequest.getKeywords(), applicationName, pageable);
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            for (ElasticEntity elasticEntity : elasticEntities) {
                csvPrinter.printRecord(
                        elasticEntity.getApplicationName(),
                        elasticEntity.getCreatedAt(),
                        elasticEntity.getKeyWords(),
                        elasticEntity.getBodyLog()
                        );
            }
        } catch (IOException e) {
            log.error("Error While writing CSV ", e);
        }
    }
}
