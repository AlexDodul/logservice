package org.bitbucket.logservice.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bitbucket.logservice.entity.APIKeyEntity;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.bitbucket.logservice.payload.request.ApplicationNameRequest;
import org.bitbucket.logservice.payload.request.BodyLogRequest;
import org.bitbucket.logservice.payload.request.KeyWordsRequest;
import org.bitbucket.logservice.payload.response.APIKeyResponse;
import org.bitbucket.logservice.security.ApiKeyProvider;
import org.bitbucket.logservice.services.APIKeyService;
import org.bitbucket.logservice.services.CsvExportService;
import org.bitbucket.logservice.services.ElasticService;
import org.bitbucket.logservice.utils.HttpServletUtils;
import org.bitbucket.logservice.utils.TransferObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/elastic")
public class ElasticController {
    private final ElasticService elasticService;
    private final APIKeyService apiKeyService;
    private final ApiKeyProvider apiKeyProvider;

    private final CsvExportService csvExportService;

    @GetMapping("/keywords")
    public ResponseEntity<Object> searchByKeywords(
        @RequestBody KeyWordsRequest keyWordsRequest,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        HttpServletRequest httpServletRequest
    ) {
        Pageable pageable = PageRequest.of(page, size);
        List<ElasticEntity> result = this.elasticService.readAllByKeyWords(
            keyWordsRequest.getKeywords(),
            pageable,
            HttpServletUtils.getCompanyName(httpServletRequest)
        );
        return ResponseEntity.ok(TransferObject.toLogResponse(result));
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        Date date = new Date();
        List<ElasticEntity> ok = (this.elasticService.findAll());
        System.out.println(System.currentTimeMillis() - date.getTime());
        return ResponseEntity.ok(ok);
    }

    @PostMapping("/save")
    public ResponseEntity<Object> createLog(@RequestBody BodyLogRequest bodyLogRequest, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(elasticService.saveLogInTable(TransferObject.toElasticEntity(bodyLogRequest), HttpServletUtils.getCompanyName(httpServletRequest)));
    }

    @PostMapping("/save/list")
    public void insertBulk(@RequestBody List<ElasticEntity> elasticEntity) {
        elasticService.saveListOfLogs(elasticEntity);
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
    public void getAllEmployeesInCsv(
        HttpServletResponse servletResponse,
        @PageableDefault(size = 20) Pageable pageable,
        @RequestBody KeyWordsRequest keyWordsRequest,
        @RequestHeader(name = "X-Api-Key") String apiKey
    ) throws IOException {
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=\"app-name-2021-09-05.csv\"");
        csvExportService.writeEmployeesToCsv(servletResponse.getWriter(), pageable, keyWordsRequest, apiKey);
    }
}
