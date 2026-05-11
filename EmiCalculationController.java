package com.emi_hms_microservice.hms_service.controller;

import com.emi_hms_microservice.hms_service.dto.EmiCalculationRequest;
import com.emi_hms_microservice.hms_service.dto.EmiCalculationResponse;
import com.emi_hms_microservice.hms_service.service.EmiCalculationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emi/calculate")
@CrossOrigin(origins = "*")
public class EmiCalculationController {
    
    @Autowired
    private EmiCalculationService emiCalculationService;
    
    @PostMapping
    public ResponseEntity<EmiCalculationResponse> calculateEmi(
            @Valid @RequestBody EmiCalculationRequest request) {
        EmiCalculationResponse response = emiCalculationService.calculateEmi(request);
        return ResponseEntity.ok(response);
    }
}



