package com.emi_hms_microservice.hms_service.service;

import com.emi_hms_microservice.hms_service.model.EmiPayment;
import com.emi_hms_microservice.hms_service.model.EmiPlan;
import com.emi_hms_microservice.hms_service.repository.EmiPaymentRepository;
import com.emi_hms_microservice.hms_service.repository.EmiPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class EmiPaymentService {

    @Autowired
    private EmiPaymentRepository emiPaymentRepository;

    @Autowired
    private EmiPlanRepository emiPlanRepository;

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    public EmiPayment createPayment(EmiPayment payment) {

        // 1️⃣ Validate Payment Input
        if (payment == null) {
            throw new RuntimeException("Payment cannot be null");
        }

        if (payment.getEmiPlanId() == null) {
            throw new RuntimeException("EMI Plan ID cannot be null");
        }

        if (payment.getPaymentAmount() == null ||
                payment.getPaymentAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Payment amount must be greater than zero");
        }

        // 2️⃣ Fetch EMI Plan
        EmiPlan emiPlan = emiPlanRepository.findById(payment.getEmiPlanId())
                .orElseThrow(() ->
                        new RuntimeException("EMI Plan not found with id: " + payment.getEmiPlanId()));

        if (emiPlan.getStatus() != EmiPlan.EmiStatus.ACTIVE) {
            throw new RuntimeException("Cannot make payment for inactive EMI plan");
        }

        // 3️⃣ Get Previous Completed Payments
        List<EmiPayment> previousPayments =
                emiPaymentRepository.findByEmiPlanIdAndPaymentStatus(
                        payment.getEmiPlanId(),
                        EmiPayment.PaymentStatus.COMPLETED
                );

        BigDecimal totalPrincipalPaid = previousPayments.stream()
                .map(EmiPayment::getPrincipalComponent)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal remainingPrincipal =
                emiPlan.getPrincipalAmount()
                        .subtract(totalPrincipalPaid)
                        .setScale(SCALE, ROUNDING_MODE);

        if (remainingPrincipal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("EMI already fully paid");
        }

        // 4️⃣ Calculate Monthly Interest Rate
        BigDecimal monthlyInterestRate =
                emiPlan.getInterestRate()
                        .divide(BigDecimal.valueOf(100), 10, ROUNDING_MODE)
                        .divide(BigDecimal.valueOf(12), 10, ROUNDING_MODE);

        // 5️⃣ Calculate Interest Component
        BigDecimal interestComponent =
                remainingPrincipal
                        .multiply(monthlyInterestRate)
                        .setScale(SCALE, ROUNDING_MODE);

        // 6️⃣ Validate Payment >= Interest
        if (payment.getPaymentAmount().compareTo(interestComponent) < 0) {
            throw new RuntimeException("Payment amount is less than interest component");
        }

        // 7️⃣ Calculate Principal Component
        BigDecimal principalComponent =
                payment.getPaymentAmount()
                        .subtract(interestComponent)
                        .setScale(SCALE, ROUNDING_MODE);

        // 8️⃣ Handle Overpayment
        if (principalComponent.compareTo(remainingPrincipal) > 0) {
            principalComponent = remainingPrincipal;
        }

        // 9️⃣ Update Remaining Principal
        remainingPrincipal =
                remainingPrincipal
                        .subtract(principalComponent)
                        .setScale(SCALE, ROUNDING_MODE);

        if (remainingPrincipal.compareTo(BigDecimal.ZERO) < 0) {
            remainingPrincipal = BigDecimal.ZERO;
        }

        // 🔟 Set Payment Fields
        payment.setPrincipalComponent(principalComponent);
        payment.setInterestComponent(interestComponent);
        payment.setRemainingPrincipal(remainingPrincipal);
        payment.setPaymentStatus(EmiPayment.PaymentStatus.COMPLETED);

        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDate.now());
        }

        EmiPayment savedPayment = emiPaymentRepository.save(payment);

        // 1️⃣1️⃣ Close EMI Plan If Completed
        if (remainingPrincipal.compareTo(BigDecimal.ZERO) == 0) {
            emiPlan.setStatus(EmiPlan.EmiStatus.COMPLETED);
            emiPlanRepository.save(emiPlan);
        }

        return savedPayment;
    }

    public EmiPayment getPaymentById(UUID id) {
        return emiPaymentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("EMI Payment not found with id: " + id));
    }

    public List<EmiPayment> getAllPayments() {
        return emiPaymentRepository.findAll();
    }

    public List<EmiPayment> getPaymentsByEmiPlan(UUID emiPlanId) {
        return emiPaymentRepository.findByEmiPlanId(emiPlanId);
    }

    public List<EmiPayment> getPaymentsByStatus(EmiPayment.PaymentStatus status) {
        return emiPaymentRepository.findByPaymentStatus(status);
    }

    public EmiPayment updatePaymentStatus(UUID id, EmiPayment.PaymentStatus status) {
        EmiPayment payment = emiPaymentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("EMI Payment not found with id: " + id));

        payment.setPaymentStatus(status);
        return emiPaymentRepository.save(payment);
    }

    public void deletePayment(UUID id) {
        if (!emiPaymentRepository.existsById(id)) {
            throw new RuntimeException("EMI Payment not found with id: " + id);
        }
        emiPaymentRepository.deleteById(id);
    }
}