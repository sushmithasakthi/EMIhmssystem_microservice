package com.emi_hms_microservice.hms_service.service;

import com.emi_hms_microservice.hms_service.dto.EmiCalculationRequest;
import com.emi_hms_microservice.hms_service.dto.EmiCalculationResponse;
import com.emi_hms_microservice.hms_service.model.EmiPlan;
import com.emi_hms_microservice.hms_service.repository.EmiPlanRepository;
import com.emi_hms_microservice.hms_service.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class EmiPlanService {
    
    @Autowired
    private EmiPlanRepository emiPlanRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private EmiCalculationService emiCalculationService;
    
    public EmiPlan createEmiPlan(EmiPlan emiPlan) {
        // Validate patient exists
        if (!patientRepository.existsById(emiPlan.getPatientId())) {
            throw new RuntimeException("Patient not found with id: " + emiPlan.getPatientId());
        }
        
        // Calculate EMI details
        EmiCalculationRequest request = new EmiCalculationRequest(
            emiPlan.getPrincipalAmount(),
            emiPlan.getInterestRate(),
            emiPlan.getTenureMonths()
        );
        
        EmiCalculationResponse calculation = emiCalculationService.calculateEmi(request);
        
        // Set calculated values
        emiPlan.setEmiAmount(calculation.getEmiAmount());
        emiPlan.setTotalAmount(calculation.getTotalAmount());
        emiPlan.setTotalInterest(calculation.getTotalInterest());
        
        // Calculate end date
        if (emiPlan.getStartDate() != null) {
            emiPlan.setEndDate(emiPlan.getStartDate().plusMonths(emiPlan.getTenureMonths()));
        } else {
            emiPlan.setStartDate(LocalDate.now());
            emiPlan.setEndDate(LocalDate.now().plusMonths(emiPlan.getTenureMonths()));
        }
        
        return emiPlanRepository.save(emiPlan);
    }
    
    public EmiPlan getEmiPlanById(UUID id) {
        return emiPlanRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("EMI Plan not found with id: " + id));
    }
    
    public List<EmiPlan> getAllEmiPlans() {
        return emiPlanRepository.findAll();
    }
    
    public List<EmiPlan> getEmiPlansByPatient(UUID patientId) {
        return emiPlanRepository.findByPatientId(patientId);
    }
    
    public List<EmiPlan> getEmiPlansByStatus(EmiPlan.EmiStatus status) {
        return emiPlanRepository.findByStatus(status);
    }
    
    public EmiPlan updateEmiPlanStatus(UUID id, EmiPlan.EmiStatus status) {
        EmiPlan emiPlan = emiPlanRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("EMI Plan not found with id: " + id));
        emiPlan.setStatus(status);
        return emiPlanRepository.save(emiPlan);
    }
    
    public void deleteEmiPlan(UUID id) {
        if (!emiPlanRepository.existsById(id)) {
            throw new RuntimeException("EMI Plan not found with id: " + id);
        }
        emiPlanRepository.deleteById(id);
    }
}



