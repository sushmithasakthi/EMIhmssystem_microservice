package com.emi_hms_microservice.hms_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "emi_plans")
public class EmiPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotNull(message = "Patient ID is required")
    @Column(name = "patient_id")
    private UUID patientId;
    
    @NotNull(message = "Principal amount is required")
    @Positive(message = "Principal amount must be positive")
    @Column(name = "principal_amount", precision = 19, scale = 2)
    private BigDecimal principalAmount;
    
    @NotNull(message = "Interest rate is required")
    @Positive(message = "Interest rate must be positive")
    @Column(name = "interest_rate", precision = 5, scale = 2)
    private BigDecimal interestRate; // Annual interest rate percentage
    
    @NotNull(message = "Tenure in months is required")
    @Positive(message = "Tenure must be positive")
    @Column(name = "tenure_months")
    private Integer tenureMonths;
    
    @Column(name = "emi_amount", precision = 19, scale = 2)
    private BigDecimal emiAmount;
    
    @Column(name = "total_amount", precision = 19, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(name = "total_interest", precision = 19, scale = 2)
    private BigDecimal totalInterest;
    
    @NotNull(message = "Start date is required")
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EmiStatus status;
    
    private String description;
    
    public enum EmiStatus {
        ACTIVE, COMPLETED, CANCELLED, OVERDUE
    }
    
    // Constructors
    public EmiPlan() {
        this.status = EmiStatus.ACTIVE;
    }
    
    public EmiPlan(UUID patientId, BigDecimal principalAmount, BigDecimal interestRate, 
                   Integer tenureMonths, LocalDate startDate, String description) {
        this.patientId = patientId;
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
        this.startDate = startDate;
        this.description = description;
        this.status = EmiStatus.ACTIVE;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getPatientId() {
        return patientId;
    }
    
    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }
    
    public BigDecimal getPrincipalAmount() {
        return principalAmount;
    }
    
    public void setPrincipalAmount(BigDecimal principalAmount) {
        this.principalAmount = principalAmount;
    }
    
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    
    public Integer getTenureMonths() {
        return tenureMonths;
    }
    
    public void setTenureMonths(Integer tenureMonths) {
        this.tenureMonths = tenureMonths;
    }
    
    public BigDecimal getEmiAmount() {
        return emiAmount;
    }
    
    public void setEmiAmount(BigDecimal emiAmount) {
        this.emiAmount = emiAmount;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public BigDecimal getTotalInterest() {
        return totalInterest;
    }
    
    public void setTotalInterest(BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public EmiStatus getStatus() {
        return status;
    }
    
    public void setStatus(EmiStatus status) {
        this.status = status;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return "EmiPlan{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", principalAmount=" + principalAmount +
                ", interestRate=" + interestRate +
                ", tenureMonths=" + tenureMonths +
                ", emiAmount=" + emiAmount +
                ", totalAmount=" + totalAmount +
                ", totalInterest=" + totalInterest +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }
}



