package com.emi_hms_microservice.hms_service.repository;

import com.emi_hms_microservice.hms_service.model.EmiPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmiPaymentRepository extends JpaRepository<EmiPayment, UUID> {
    List<EmiPayment> findByEmiPlanId(UUID emiPlanId);
    List<EmiPayment> findByPaymentStatus(EmiPayment.PaymentStatus paymentStatus);
    List<EmiPayment> findByEmiPlanIdAndPaymentStatus(UUID emiPlanId, EmiPayment.PaymentStatus paymentStatus);
}



