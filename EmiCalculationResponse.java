package com.emi_hms_microservice.hms_service.dto;

import java.math.BigDecimal;
import java.util.List;

public class EmiCalculationResponse {
    private BigDecimal principalAmount;
    private BigDecimal interestRate;
    private Integer tenureMonths;
    private BigDecimal emiAmount;
    private BigDecimal totalAmount;
    private BigDecimal totalInterest;
    private List<EmiScheduleItem> schedule;
    
    // Constructors
    public EmiCalculationResponse() {
    }
    
    public EmiCalculationResponse(BigDecimal principalAmount, BigDecimal interestRate, 
                                  Integer tenureMonths, BigDecimal emiAmount, 
                                  BigDecimal totalAmount, BigDecimal totalInterest) {
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
        this.emiAmount = emiAmount;
        this.totalAmount = totalAmount;
        this.totalInterest = totalInterest;
    }
    
    // Getters and Setters
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
    
    public List<EmiScheduleItem> getSchedule() {
        return schedule;
    }
    
    public void setSchedule(List<EmiScheduleItem> schedule) {
        this.schedule = schedule;
    }
    
    // Inner class for schedule items
    public static class EmiScheduleItem {
        private Integer installmentNumber;
        private BigDecimal emiAmount;
        private BigDecimal principalComponent;
        private BigDecimal interestComponent;
        private BigDecimal remainingPrincipal;
        
        public EmiScheduleItem() {
        }
        
        public EmiScheduleItem(Integer installmentNumber, BigDecimal emiAmount, 
                              BigDecimal principalComponent, BigDecimal interestComponent, 
                              BigDecimal remainingPrincipal) {
            this.installmentNumber = installmentNumber;
            this.emiAmount = emiAmount;
            this.principalComponent = principalComponent;
            this.interestComponent = interestComponent;
            this.remainingPrincipal = remainingPrincipal;
        }
        
        public Integer getInstallmentNumber() {
            return installmentNumber;
        }
        
        public void setInstallmentNumber(Integer installmentNumber) {
            this.installmentNumber = installmentNumber;
        }
        
        public BigDecimal getEmiAmount() {
            return emiAmount;
        }
        
        public void setEmiAmount(BigDecimal emiAmount) {
            this.emiAmount = emiAmount;
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
    }
}



