package com.emi_hms_microservice.hms_service.controller;

import com.emi_hms_microservice.hms_service.model.EmiPlan;
import com.emi_hms_microservice.hms_service.service.EmiPlanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/emi/plans")
@CrossOrigin(origins = "*")
public class EmiPlanController {
    
    @Autowired
    private EmiPlanService emiPlanService;
    
    @PostMapping
    public ResponseEntity<EmiPlan> createEmiPlan(@Valid @RequestBody EmiPlan emiPlan) {
        EmiPlan createdPlan = emiPlanService.createEmiPlan(emiPlan);
        return new ResponseEntity<>(createdPlan, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EmiPlan> getEmiPlanById(@PathVariable UUID id) {
        EmiPlan emiPlan = emiPlanService.getEmiPlanById(id);
        return ResponseEntity.ok(emiPlan);
    }
    
    @GetMapping
    public ResponseEntity<List<EmiPlan>> getAllEmiPlans() {
        List<EmiPlan> plans = emiPlanService.getAllEmiPlans();
        return ResponseEntity.ok(plans);
    }
    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<EmiPlan>> getEmiPlansByPatient(@PathVariable UUID patientId) {
        List<EmiPlan> plans = emiPlanService.getEmiPlansByPatient(patientId);
        return ResponseEntity.ok(plans);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmiPlan>> getEmiPlansByStatus(@PathVariable EmiPlan.EmiStatus status) {
        List<EmiPlan> plans = emiPlanService.getEmiPlansByStatus(status);
        return ResponseEntity.ok(plans);
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<EmiPlan> updateEmiPlanStatus(
            @PathVariable UUID id, 
            @RequestParam EmiPlan.EmiStatus status) {
        EmiPlan updatedPlan = emiPlanService.updateEmiPlanStatus(id, status);
        return ResponseEntity.ok(updatedPlan);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmiPlan(@PathVariable UUID id) {
        emiPlanService.deleteEmiPlan(id);
        return ResponseEntity.noContent().build();
    }
}



