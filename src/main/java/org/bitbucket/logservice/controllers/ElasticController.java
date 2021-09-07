package org.bitbucket.logservice.controllers;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.bitbucket.logservice.payload.request.LogRequest;
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
@RequestMapping("/elastic")
public class ElasticController {
    private final ElasticService elasticService;

    String keyWords = "kryurtyuh7pol65tru reui rt iretu";

    private final CsvExportService csvExportService;

    /*@GetMapping
    public ResponseEntity<Object> getLogsByKeyWords() {
        Date date = new Date();
        List<ElasticEntity> result = this.elasticService.readAllByKeyWords(List.of("infoService"*//*, "Alex", "data", "test"*//*));
        System.out.println(new Date().getTime() - date.getTime());
        return ResponseEntity.ok(result);
    }*/

    @GetMapping("/entity")
    public ResponseEntity<Object> getPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        System.out.println(pageable.toOptional().stream().count());
        List<ElasticEntity> result = this.elasticService.readAllByKeyWords(List.of("infoService", "Alex", "data"/*, "test"*/), pageable);
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
