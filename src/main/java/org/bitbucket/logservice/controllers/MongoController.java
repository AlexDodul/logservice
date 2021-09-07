package org.bitbucket.logservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.bitbucket.logservice.entity.MongoTest;
import org.bitbucket.logservice.payload.request.LogRequest;
import org.bitbucket.logservice.services.MongoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/log")
public class MongoController {

    private final MongoService mongoService;

    String keyWords = "kryurtyuh7pol65tru reui rt iretu";

    @GetMapping
    public ResponseEntity<Object> getLogsByKeyWords() {
        Date date = new Date();
//        List<MongoTest> result = this.mongoService.readAll().stream()
//                .filter(mongoTest -> mongoTest.getKeyWords().containsAll(List.of("infoService", "data")))
//                .collect(Collectors.toList());
        List<MongoTest> result = this.mongoService.readAllByKeyWords(List.of("infoService"/*, "Alex", "data", "test"*/));
        System.out.println(new Date().getTime() - date.getTime());
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Object> saveLog(@RequestBody LogRequest request) {
        Date date = new Date();
        for (int i = 0; i < 350_000; i++) {
            if (i % 10000 == 0) {
                keyWords = "infoService Alex data test";
            } else if (i % 9999 == 0) {
                keyWords = "infoService Alex data";
            } else {
                keyWords = "dfjghj dsfgh dsfg dfghjoiuyeig dfujh";
            }
            this.mongoService.create(new MongoTest(List.of(keyWords.split(" ")), RandomString.make(128)));
//            log.info(i + " - " + new Date());
        }

        return ResponseEntity.ok(new Date().getTime() - date.getTime());
    }

    @DeleteMapping
    public void deleteAll(){
        this.mongoService.deleteAll();
    }
}
