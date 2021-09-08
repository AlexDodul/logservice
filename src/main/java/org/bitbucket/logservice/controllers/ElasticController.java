package org.bitbucket.logservice.controllers;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.bitbucket.logservice.entity.APIKeyEntity;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.bitbucket.logservice.payload.request.ApplicationNameRequest;
import org.bitbucket.logservice.payload.request.LogRequest;
import org.bitbucket.logservice.payload.response.APIKeyResponse;
import org.bitbucket.logservice.security.APIKeyProvider;
import org.bitbucket.logservice.services.APIKeyService;
import org.bitbucket.logservice.services.CsvExportService;
import org.bitbucket.logservice.services.ElasticService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/elastic")
public class ElasticController {
    private final ElasticService elasticService;
    private final APIKeyService apiKeyService;
    private final APIKeyProvider apiKeyProvider;

    String keyWords = "kryurtyuh7pol65tru reui rt iretu";

    private final CsvExportService csvExportService;

    /*@GetMapping
    public ResponseEntity<Object> getLogsByKeyWords() {
        Date date = new Date();
        List<ElasticEntity> result = this.elasticService.readAllByKeyWords(List.of("infoService"*//*, "Alex", "data", "test"*//*));
        System.out.println(new Date().getTime() - date.getTime());
        return ResponseEntity.ok(result);
    }*/

    @GetMapping("/keywords")
    public ResponseEntity<Object> getPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2000") int size) {
        Pageable pageable = PageRequest.of(page, size);
        System.out.println(pageable.toOptional().stream().count());
        List<ElasticEntity> result = this.elasticService.readAllByKeyWords(List.of("infoService"/*, "Alex", "data", "test"*/), pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Object> saveLog(@RequestBody LogRequest request) {
        Date date = new Date();
        for (int i = 0; i < 1000; i++) {
            if (i % 10 == 0) {
                keyWords = "infoService";
            } else if (i % 9 == 0) {
                keyWords = "infoService Alex data";
            } else {
                keyWords = "dfjghj dsfgh dsfg dfghjoiuyeig dfujh";
            }
            this.elasticService.create(new ElasticEntity(List.of(keyWords.split(" ")), RandomString.make(128)));
        }
        return ResponseEntity.ok(new Date().getTime() - date.getTime());
    }

    @PostMapping("/save")
    public void insertBulk(@RequestBody List<ElasticEntity> elasticEntity) {
        elasticService.insertBulk(elasticEntity);
    }

    @PostMapping("/generate-api-key")
    public ResponseEntity<Object> generateApiKey(@RequestBody ApplicationNameRequest applicationNameRequest){
//        String apiKey = this.apiKeyProvider.generateAPIKey(applicationNameRequest.getApplicationName());
        APIKeyEntity apiKey = this.apiKeyService.createApiKey(applicationNameRequest);
        return ResponseEntity.ok(new APIKeyResponse(apiKey.getApiKey()));
    }

    @DeleteMapping
    public void deleteAll(){
        this.elasticService.deleteAll();
    }

    @RequestMapping(path = "/csv")
    public void getAllEmployeesInCsv(HttpServletResponse servletResponse, @PageableDefault Pageable pageable) throws IOException {
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition","attachment; filename=\"app-name-2021-09-05.csv\"");
        csvExportService.writeEmployeesToCsv(servletResponse.getWriter(), pageable);
    }
}
