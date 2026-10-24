package edu.co.icesi.introspringboot.api;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
public class HealthCheckerRestController {

    @GetMapping
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity
                .status(200)
                .header("Alfa", "Beta")
                .body(
                        Map.of("status","health", "date", new Date().toString())
                );
    }

}
