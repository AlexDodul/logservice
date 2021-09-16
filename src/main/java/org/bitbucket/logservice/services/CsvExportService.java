package org.bitbucket.logservice.services;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.bitbucket.logservice.exception.NoValuePresentException;
import org.bitbucket.logservice.payload.request.FilterRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvExportService {

  private final ElasticService elasticService;

  public void writeEmployeesToCsv(HttpServletResponse response,
                                  Pageable pageable,
                                  FilterRequest filterRequest,
                                  String applicationName
  ) {
    List<ElasticEntity> elasticEntities =
        elasticService.readAllByKeyWordsAndDate(filterRequest, pageable, applicationName);
    if (elasticEntities.isEmpty()) throw new NoValuePresentException("Data is empty", HttpStatus.NO_CONTENT.value());
    try (CSVPrinter csvPrinter = new CSVPrinter(response.getWriter(), CSVFormat.DEFAULT)) {
      for (ElasticEntity elasticEntity : elasticEntities) {
        csvPrinter.printRecord(
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
