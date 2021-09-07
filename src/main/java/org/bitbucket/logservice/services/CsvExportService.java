package org.bitbucket.logservice.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.bitbucket.logservice.repositories.ElasticsearchRepo;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Slf4j
@Service
public class CsvExportService {

    private final ElasticsearchRepo elasticsearchRepo;

    public CsvExportService(ElasticsearchRepo elasticsearchRepo) {
        this.elasticsearchRepo = elasticsearchRepo;
    }

    public void writeEmployeesToCsv(Writer writer, Pageable pageable) {
        List<ElasticEntity> elasticEntities = elasticsearchRepo.findAllByKeyWords(List.of("infoService", "Alex", "data"/*, "test"*/), pageable);
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
