package org.bitbucket.logservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bitbucket.logservice.entity.ApiKeyEntity;
import org.bitbucket.logservice.entity.ElasticEntity;
import org.bitbucket.logservice.payload.request.ApplicationNameRequest;
import org.bitbucket.logservice.payload.request.BodyLogRequest;
import org.bitbucket.logservice.payload.request.FilterRequest;
import org.bitbucket.logservice.payload.request.KeyWordsRequest;
import org.bitbucket.logservice.payload.response.ApiKeyResponse;
import org.bitbucket.logservice.payload.response.LogResponse;
import org.bitbucket.logservice.security.ApiKeyProvider;
import org.bitbucket.logservice.services.ApiKeyService;
import org.bitbucket.logservice.services.CsvExportService;
import org.bitbucket.logservice.services.ElasticService;
import org.bitbucket.logservice.utils.HttpServletUtils;
import org.bitbucket.logservice.utils.TransferObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/elastic")
@SecurityScheme(
    type = SecuritySchemeType.APIKEY,
    name = "X-Api-Key",
    description = "Access by all methods",
    in = SecuritySchemeIn.HEADER
)
public class ElasticController {
  private final ElasticService elasticService;
  private final ApiKeyService apiKeyService;
  private final ApiKeyProvider apiKeyProvider;

  private final CsvExportService csvExportService;


  @Operation(summary = "Search only by Keywords", description = "Search by keywords only, without using dates")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search was successful", content = @Content(schema = @Schema(hidden = true))),
      @ApiResponse(responseCode = "400", description = "Bad request. Check passed parameters", content = @Content(schema = @Schema(hidden = true))),
      @ApiResponse(responseCode = "403", description = "Forbidden. No access rights. Needed ApiKey", content = @Content(schema = @Schema(hidden = true))),
      @ApiResponse(responseCode = "404", description = "Not Found. Requested resource was not found.", content = @Content(schema = @Schema(hidden = true))),
      @ApiResponse(responseCode = "500", description = "Internal Server Error. Some internal error was occurred.", content = @Content(schema = @Schema(hidden = true)))
  })
  @PostMapping(value = "/keywords", produces = MediaType.APPLICATION_JSON_VALUE)
  @SecurityRequirement(name = "X-Api-Key")
  public ResponseEntity<Object> searchByKeywords(
      @RequestBody KeyWordsRequest keyWordsRequest,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      HttpServletRequest httpServletRequest
  ) {
    Pageable pageable = PageRequest.of(page, size);
    List<ElasticEntity> result = elasticService.readAllByKeyWords(
        keyWordsRequest.getKeywords(),
        pageable,
        HttpServletUtils.getCompanyName(httpServletRequest)
    );
    return ResponseEntity.ok(TransferObject.toLogResponse(result));
  }

  @Operation(summary = "Search by keywords, dates using pagination", description = "The search is carried out by keywords, dates using pagination. All fields are optional. The company name is substituted automatically. I take information about the company from the apikey, which is in the request header")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search was successful", content = @Content(schema = @Schema(implementation = LogResponse.class))),
      @ApiResponse(responseCode = "400", description = "Bad request. Check passed parameters", content = @Content(schema = @Schema(hidden = true))),
      @ApiResponse(responseCode = "403", description = "Forbidden. No access rights. Needed ApiKey", content = @Content(schema = @Schema(hidden = true))),
      @ApiResponse(responseCode = "404", description = "Not Found. Requested resource was not found.", content = @Content(schema = @Schema(hidden = true))),
      @ApiResponse(responseCode = "500", description = "Internal Server Error. Some internal error was occurred.", content = @Content(schema = @Schema(hidden = true)))
  })
  @PostMapping("/filter")
  @SecurityRequirement(name = "X-Api-Key")
  public ResponseEntity<Object> searchByFilter(
      @RequestBody FilterRequest filterRequest,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      HttpServletRequest httpServletRequest
  ) {
    Pageable pageable = PageRequest.of(page, size);
    List<ElasticEntity> result = elasticService.readAllByKeyWordsAndDate(
        filterRequest,
        pageable,
        HttpServletUtils.getCompanyName(httpServletRequest)
    );
    return ResponseEntity.ok(TransferObject.toLogResponse(result));
  }

  @PostMapping("/save")
  @SecurityRequirement(name = "X-Api-Key")
  public ResponseEntity<Object> saveLogInTable(@RequestBody BodyLogRequest bodyLogRequest,
                                               HttpServletRequest httpServletRequest) {
    return ResponseEntity.ok(elasticService.saveLogInTable(
        TransferObject.toElasticEntity(bodyLogRequest),
        HttpServletUtils.getCompanyName(httpServletRequest))
    );
  }

  @PostMapping("/generate-api-key")
  public ResponseEntity<ApiKeyResponse> generateApiKey(
      @Valid @RequestBody ApplicationNameRequest applicationNameRequest) {
    ApiKeyEntity apiKey = apiKeyService.createApiKey(applicationNameRequest);
    return ResponseEntity.ok(new ApiKeyResponse(apiKey.getApiKey()));
  }

  @PostMapping(path = "/csv")
  @SecurityRequirement(name = "X-Api-Key")
  public void getAllEmployeesInCsv(
      HttpServletResponse servletResponse,
      @PageableDefault(size = 20) Pageable pageable,
      @RequestBody FilterRequest filterRequest,
      @RequestHeader(name = "X-Api-Key") String apiKey
  ) throws IOException {
    servletResponse.setContentType("text/csv");
    servletResponse.addHeader("Content-Disposition",
        "attachment; filename=" + '"' + apiKeyProvider.getApplicationName(apiKey) + ".csv" + '"');
    csvExportService
        .writeEmployeesToCsv(servletResponse.getWriter(), pageable, filterRequest, apiKey);
  }

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  @SecurityRequirement(name = "X-Api-Key")
  public ResponseEntity<List<ElasticEntity>> allLogs() {
    List<ElasticEntity> result = elasticService.readAllLogs();
    return ResponseEntity.ok(result);
  }

  @DeleteMapping
  @SecurityRequirement(name = "X-Api-Key")
  public void deleteAll() {
    elasticService.deleteAll();
  }
}
