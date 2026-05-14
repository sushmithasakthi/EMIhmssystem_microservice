package com.emi_hms_microservice.hms_service.service;

import com.emi_hms_microservice.hms_service.dto.EmiCalculationRequest;
import com.emi_hms_microservice.hms_service.dto.EmiCalculationResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmiCalculationService {
    
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    
    /**
     * Calculate EMI using the formula:
     * EMI = [P × R × (1+R)^N] / [(1+R)^N - 1]
     * where P = Principal, R = Monthly Interest Rate, N = Number of months
     */
    public EmiCalculationResponse calculateEmi(EmiCalculationRequest request) {
        BigDecimal principal = request.getPrincipalAmount();
        BigDecimal annualInterestRate = request.getInterestRate();
        Integer tenureMonths = request.getTenureMonths();
        
        // Convert annual interest rate to monthly rate (divide by 12 and 100)
        BigDecimal monthlyInterestRate = annualInterestRate
            .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
            .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        
        // Calculate (1+R)^N
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyInterestRate);
        BigDecimal onePlusRPowerN = power(onePlusR, tenureMonths);
        
        // Calculate numerator: P × R × (1+R)^N
        BigDecimal numerator = principal
            .multiply(monthlyInterestRate)
            .multiply(onePlusRPowerN);
        
        // Calculate denominator: (1+R)^N - 1
        BigDecimal denominator = onePlusRPowerN.subtract(BigDecimal.ONE);
        
        // Calculate EMI
        BigDecimal emiAmount = numerator
            .divide(denominator, SCALE, ROUNDING_MODE);
        
        // Calculate total amount and total interest
        BigDecimal totalAmount = emiAmount.multiply(BigDecimal.valueOf(tenureMonths));
        BigDecimal totalInterest = totalAmount.subtract(principal);
        
        EmiCalculationResponse response = new EmiCalculationResponse(
            principal, annualInterestRate, tenureMonths, 
            emiAmount, totalAmount, totalInterest
        );
        
        // Generate payment schedule
        List<EmiCalculationResponse.EmiScheduleItem> schedule = generateSchedule(
            principal, monthlyInterestRate, emiAmount, tenureMonths);
        response.setSchedule(schedule);
        
        return response;
    }
    
    /**
     * Generate EMI payment schedule showing principal and interest components for each installment
     */
    private List<EmiCalculationResponse.EmiScheduleItem> generateSchedule(
            BigDecimal principal, BigDecimal monthlyInterestRate, 
            BigDecimal emiAmount, Integer tenureMonths) {
        
        List<EmiCalculationResponse.EmiScheduleItem> schedule = new ArrayList<>();
        BigDecimal remainingPrincipal = principal;
        
        for (int i = 1; i <= tenureMonths; i++) {
            // Calculate interest component for this month
            BigDecimal interestComponent = remainingPrincipal
                .multiply(monthlyInterestRate)
                .setScale(SCALE, ROUNDING_MODE);
            
            // Calculate principal component
            BigDecimal principalComponent = emiAmount.subtract(interestComponent);
            
            // Update remaining principal
            remainingPrincipal = remainingPrincipal.subtract(principalComponent);
            
            // Ensure remaining principal doesn't go negative (due to rounding)
            if (remainingPrincipal.compareTo(BigDecimal.ZERO) < 0) {
                remainingPrincipal = BigDecimal.ZERO;
            }
            
            schedule.add(new EmiCalculationResponse.EmiScheduleItem(
                i, emiAmount, principalComponent, interestComponent, remainingPrincipal
            ));
        }
        
        return schedule;
    }
    
    /**
     * Calculate power of BigDecimal (for (1+R)^N calculation)
     */
    private BigDecimal power(BigDecimal base, int exponent) {
        BigDecimal result = BigDecimal.ONE;
        for (int i = 0; i < exponent; i++) {
            result = result.multiply(base);
        }
        return result;
    }
    
    /**
     * Calculate remaining balance after N payments
     */
    public BigDecimal calculateRemainingBalance(
            BigDecimal principal, BigDecimal annualInterestRate, 
            Integer tenureMonths, Integer paidMonths) {
        
        if (paidMonths >= tenureMonths) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal monthlyInterestRate = annualInterestRate
            .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
            .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        
        // Calculate remaining balance using formula:
        // Remaining Balance = P × [(1+R)^N - (1+R)^n] / [(1+R)^N - 1]
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyInterestRate);
        BigDecimal onePlusRPowerN = power(onePlusR, tenureMonths);
        BigDecimal onePlusRPowerNPaid = power(onePlusR, paidMonths);
        
        BigDecimal numerator = principal
            .multiply(onePlusRPowerN.subtract(onePlusRPowerNPaid));
        BigDecimal denominator = onePlusRPowerN.subtract(BigDecimal.ONE);
        
        return numerator.divide(denominator, SCALE, ROUNDING_MODE);
    }
}

