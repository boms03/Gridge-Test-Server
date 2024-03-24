package com.example.demo.src.health;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    /**
     * AWS Elasticbeanstalk health 체크용 API
     * [GET] /health
     * @return String
     */
    @Operation(summary = "Get AWS Elasticbeanstalk health 체크")
    @GetMapping("/health")
    public String health(){
        return "healthChecked";
    }
}
