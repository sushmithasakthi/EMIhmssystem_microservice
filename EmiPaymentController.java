package com.emi_hms_microservice.hms_service.controller;

import com.emi_hms_microservice.hms_service.model.EmiPayment;
import com.emi_hms_microservice.hms_service.service.EmiPaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/emi/payments")
@CrossOrigin(origins = "*")
public class EmiPaymentController {
    
    @Autowired
    private EmiPaymentService emiPaymentService;
    
    @PostMapping
    public ResponseEntity<EmiPayment> createPayment(@Valid @RequestBody EmiPayment payment) {
        EmiPayment createdPayment = emiPaymentService.createPayment(payment);
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EmiPayment> getPaymentById(@PathVariable UUID id) {
        EmiPayment payment = emiPaymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }
    
    @GetMapping
    public ResponseEntity<List<EmiPayment>> getAllPayments() {
        List<EmiPayment> payments = emiPaymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/plan/{emiPlanId}")
    public ResponseEntity<List<EmiPayment>> getPaymentsByEmiPlan(@PathVariable UUID emiPlanId) {
        List<EmiPayment> payments = emiPaymentService.getPaymentsByEmiPlan(emiPlanId);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmiPayment>> getPaymentsByStatus(@PathVariable EmiPayment.PaymentStatus status) {
        List<EmiPayment> payments = emiPaymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(payments);
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<EmiPayment> updatePaymentStatus(
            @PathVariable UUID id, 
            @RequestParam EmiPayment.PaymentStatus status) {
        EmiPayment updatedPayment = emiPaymentService.updatePaymentStatus(id, status);
        return ResponseEntity.ok(updatedPayment);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable UUID id) {
        emiPaymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}



