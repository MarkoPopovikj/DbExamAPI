package finki.ukim.mk.dbexamapi.web.common.controllers;

import finki.ukim.mk.dbexamapi.service.api.ApiStatusService;
import finki.ukim.mk.dbexamapi.web.common.responses.ApiStatusResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/status")
public class ApiStatusController {

    private final ApiStatusService apiStatusService;

    public ApiStatusController(ApiStatusService apiStatusService) {
        this.apiStatusService = apiStatusService;
    }

    @GetMapping
    public ResponseEntity<ApiStatusResponse> status() {
        ApiStatusResponse response = apiStatusService.isDatabaseReachable()
                ? new ApiStatusResponse("ok", "Service is operational", Instant.now(), "connected")
                : new ApiStatusResponse("error", "Service is operational but database is unreachable", Instant.now(), "disconnected");
        return ResponseEntity.ok(response);
    }
}
