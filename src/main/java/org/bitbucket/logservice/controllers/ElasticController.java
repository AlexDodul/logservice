package org.bitbucket.logservice.controllers;

import lombok.RequiredArgsConstructor;
import org.bitbucket.logservice.entity.APIKeyEntity;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.bitbucket.logservice.payload.request.ApplicationNameRequest;
import org.bitbucket.logservice.payload.request.BodyLogRequest;
import org.bitbucket.logservice.payload.request.KeyWordsRequest;
import org.bitbucket.logservice.payload.response.APIKeyResponse;
import org.bitbucket.logservice.security.APIKeyProvider;
import org.bitbucket.logservice.services.APIKeyService;
import org.bitbucket.logservice.services.CsvExportService;
import org.bitbucket.logservice.services.ElasticService;
import org.bitbucket.logservice.utils.TransferObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/elastic")
public class ElasticController {
    private final ElasticService elasticService;
    private final APIKeyService apiKeyService;
    private final APIKeyProvider apiKeyProvider;

    private final CsvExportService csvExportService;

    @GetMapping("/keywords")
    public ResponseEntity<Object> getPage(@RequestBody KeyWordsRequest keyWordsRequest, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2000") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<ElasticEntity> result = this.elasticService.readAllByKeyWords(keyWordsRequest.getKeywords(), pageable);
        return ResponseEntity.ok(TransferObject.toLogResponse(result));
    }

    @PostMapping("/save")
    public ResponseEntity<Object> createLog(@RequestBody BodyLogRequest bodyLogRequest) {
        return ResponseEntity.ok(elasticService.createElasticEntity(TransferObject.toElasticEntity(bodyLogRequest)));
    }

    @PostMapping("/save/list")
    public void insertBulk(@RequestBody List<ElasticEntity> elasticEntity) {
        elasticService.insertBulk(elasticEntity);
    }

    @PostMapping("/generate-api-key")
    public ResponseEntity<Object> generateApiKey(@RequestBody ApplicationNameRequest applicationNameRequest) {
        APIKeyEntity apiKey = this.apiKeyService.createApiKey(applicationNameRequest);
        return ResponseEntity.ok(new APIKeyResponse(apiKey.getApiKey()));
    }

    @DeleteMapping
    public void deleteAll() {
        this.elasticService.deleteAll();
    }

    @RequestMapping(path = "/csv")
    public void getAllEmployeesInCsv(HttpServletResponse servletResponse, @PageableDefault Pageable pageable) throws IOException {
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=\"app-name-2021-09-05.csv\"");
        csvExportService.writeEmployeesToCsv(servletResponse.getWriter(), pageable);
    }
}
