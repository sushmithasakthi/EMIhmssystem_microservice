package com.emi_hms_microservice.hms_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "emi_payments")
public class EmiPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotNull(message = "EMI Plan ID is required")
    @Column(name = "emi_plan_id")
    private UUID emiPlanId;
    
    @NotNull(message = "Payment amount is required")
    @Positive(message = "Payment amount must be positive")
    @Column(name = "payment_amount", precision = 19, scale = 2)
    private BigDecimal paymentAmount;
    
    @NotNull(message = "Payment date is required")
    @Column(name = "payment_date")
    private LocalDate paymentDate;
    
    @Column(name = "principal_component", precision = 19, scale = 2)
    private BigDecimal principalComponent;
    
    @Column(name = "interest_component", precision = 19, scale = 2)
    private BigDecimal interestComponent;
    
    @Column(name = "remaining_principal", precision = 19, scale = 2)
    private BigDecimal remainingPrincipal;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;
    
    private String paymentMethod;
    private String transactionId;
    private String notes;
    
    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, REFUNDED
    }
    
    // Constructors
    public EmiPayment() {
        this.paymentStatus = PaymentStatus.PENDING;
    }
    
    public EmiPayment(UUID emiPlanId, BigDecimal paymentAmount, LocalDate paymentDate, 
                      String paymentMethod, String transactionId) {
        this.emiPlanId = emiPlanId;
        this.paymentAmount = paymentAmount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.paymentStatus = PaymentStatus.PENDING;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getEmiPlanId() {
        return emiPlanId;
    }
    
    public void setEmiPlanId(UUID emiPlanId) {
        this.emiPlanId = emiPlanId;
    }
    
    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }
    
    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
    
    public LocalDate getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public BigDecimal getPrincipalComponent() {
        return principalComponent;
    }
    
    public void setPrincipalComponent(BigDecimal principalComponent) {
        this.principalComponent = principalComponent;
    }
    
    public BigDecimal getInterestComponent() {
        return interestComponent;
    }
    
    public void setInterestComponent(BigDecimal interestComponent) {
        this.interestComponent = interestComponent;
    }
    
    public BigDecimal getRemainingPrincipal() {
        return remainingPrincipal;
    }
    
    public void setRemainingPrincipal(BigDecimal remainingPrincipal) {
        this.remainingPrincipal = remainingPrincipal;
    }
    
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @Override
    public String toString() {
        return "EmiPayment{" +
                "id=" + id +
                ", emiPlanId=" + emiPlanId +
                ", paymentAmount=" + paymentAmount +
                ", paymentDate=" + paymentDate +
                ", principalComponent=" + principalComponent +
                ", interestComponent=" + interestComponent +
                ", remainingPrincipal=" + remainingPrincipal +
                ", paymentStatus=" + paymentStatus +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}



