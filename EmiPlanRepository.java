package com.emi_hms_microservice.hms_service.repository;

import com.emi_hms_microservice.hms_service.model.EmiPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmiPlanRepository extends JpaRepository<EmiPlan, UUID> {
    List<EmiPlan> findByPatientId(UUID patientId);
    List<EmiPlan> findByStatus(EmiPlan.EmiStatus status);
    List<EmiPlan> findByPatientIdAndStatus(UUID patientId, EmiPlan.EmiStatus status);
}



