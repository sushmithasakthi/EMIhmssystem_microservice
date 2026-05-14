package com.emi_hms_microservice.hms_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "invoice_number", unique = true, nullable = false)
    private String invoiceNumber;
    
    @NotNull(message = "Patient ID is required")
    @Column(name = "patient_id", nullable = false)
    private UUID patientId;
    
    @Column(name = "appointment_id")
    private UUID appointmentId;
    
    @Column(name = "emi_plan_id")
    private UUID emiPlanId;
    
    @NotNull(message = "Invoice date is required")
    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;
    
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    @Column(name = "subtotal", precision = 19, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "tax_amount", precision = 19, scale = 2)
    private BigDecimal taxAmount;
    
    @Column(name = "discount_amount", precision = 19, scale = 2)
    private BigDecimal discountAmount;
    
    @Column(name = "total_amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal totalAmount;
    
    @Column(name = "paid_amount", precision = 19, scale = 2)
    private BigDecimal paidAmount;
    
    @Column(name = "remaining_amount", precision = 19, scale = 2)
    private BigDecimal remainingAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InvoiceStatus status;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;
    
    @Column(name = "tax_rate", precision = 5, scale = 2)
    private BigDecimal taxRate; // Tax rate percentage
    
    private String notes;
    private String billingAddress;
    private String billingContact;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<InvoiceItem> items = new ArrayList<>();
    
    public enum InvoiceStatus {
        DRAFT, ISSUED, SENT, PAID, PARTIALLY_PAID, OVERDUE, CANCELLED, REFUNDED
    }
    
    public enum PaymentStatus {
        PENDING, PARTIAL, PAID, OVERDUE, CANCELLED
    }
    
    // Constructors
    public Invoice() {
        this.status = InvoiceStatus.DRAFT;
        this.paymentStatus = PaymentStatus.PENDING;
        this.subtotal = BigDecimal.ZERO;
        this.taxAmount = BigDecimal.ZERO;
        this.discountAmount = BigDecimal.ZERO;
        this.totalAmount = BigDecimal.ZERO;
        this.paidAmount = BigDecimal.ZERO;
        this.remainingAmount = BigDecimal.ZERO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Invoice(UUID patientId, LocalDate invoiceDate, LocalDate dueDate) {
        this();
        this.patientId = patientId;
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.invoiceNumber = generateInvoiceNumber();
    }
    
    // Helper method to generate invoice number
    private String generateInvoiceNumber() {
        return "INV-" + System.currentTimeMillis();
    }
    
    // Pre-persist hook to generate invoice number if not set
    @PrePersist
    public void prePersist() {
        if (this.invoiceNumber == null || this.invoiceNumber.isEmpty()) {
            this.invoiceNumber = generateInvoiceNumber();
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Method to calculate totals
    public void calculateTotals() {
        // Calculate subtotal from items
        this.subtotal = items.stream()
            .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate discount
        BigDecimal amountAfterDiscount = subtotal.subtract(discountAmount != null ? discountAmount : BigDecimal.ZERO);
        
        // Calculate tax
        if (taxRate != null && taxRate.compareTo(BigDecimal.ZERO) > 0) {
            this.taxAmount = amountAfterDiscount
                .multiply(taxRate)
                .divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
        } else {
            this.taxAmount = BigDecimal.ZERO;
        }
        
        // Calculate total
        this.totalAmount = amountAfterDiscount.add(taxAmount);
        
        // Calculate remaining amount
        this.remainingAmount = totalAmount.subtract(paidAmount != null ? paidAmount : BigDecimal.ZERO);
        
        // Update payment status
        updatePaymentStatus();
    }
    
    private void updatePaymentStatus() {
        if (paidAmount == null || paidAmount.compareTo(BigDecimal.ZERO) == 0) {
            this.paymentStatus = PaymentStatus.PENDING;
        } else if (paidAmount.compareTo(totalAmount) >= 0) {
            this.paymentStatus = PaymentStatus.PAID;
            this.status = InvoiceStatus.PAID;
        } else {
            this.paymentStatus = PaymentStatus.PARTIAL;
        }
        
        // Check if overdue
        if (dueDate != null && LocalDate.now().isAfter(dueDate) && 
            paymentStatus != PaymentStatus.PAID && paymentStatus != PaymentStatus.CANCELLED) {
            this.paymentStatus = PaymentStatus.OVERDUE;
            if (status == InvoiceStatus.ISSUED || status == InvoiceStatus.SENT) {
                this.status = InvoiceStatus.OVERDUE;
            }
        }
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getInvoiceNumber() {
        return invoiceNumber;
    }
    
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
    
    public UUID getPatientId() {
        return patientId;
    }
    
    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }
    
    public UUID getAppointmentId() {
        return appointmentId;
    }
    
    public void setAppointmentId(UUID appointmentId) {
        this.appointmentId = appointmentId;
    }
    
    public UUID getEmiPlanId() {
        return emiPlanId;
    }
    
    public void setEmiPlanId(UUID emiPlanId) {
        this.emiPlanId = emiPlanId;
    }
    
    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }
    
    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }
    
    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
    
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
    
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public BigDecimal getPaidAmount() {
        return paidAmount;
    }
    
    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
        calculateTotals();
    }
    
    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }
    
    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }
    
    public InvoiceStatus getStatus() {
        return status;
    }
    
    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }
    
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public BigDecimal getTaxRate() {
        return taxRate;
    }
    
    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getBillingAddress() {
        return billingAddress;
    }
    
    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }
    
    public String getBillingContact() {
        return billingContact;
    }
    
    public void setBillingContact(String billingContact) {
        this.billingContact = billingContact;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public List<InvoiceItem> getItems() {
        return items;
    }
    
    public void setItems(List<InvoiceItem> items) {
        this.items = items;
        if (items != null) {
            items.forEach(item -> item.setInvoice(this));
        }
        calculateTotals();
    }
    
    public void addItem(InvoiceItem item) {
        items.add(item);
        item.setInvoice(this);
        calculateTotals();
    }
    
    public void removeItem(InvoiceItem item) {
        items.remove(item);
        item.setInvoice(null);
        calculateTotals();
    }
    
    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", patientId=" + patientId +
                ", appointmentId=" + appointmentId +
                ", invoiceDate=" + invoiceDate +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", paymentStatus=" + paymentStatus +
                '}';
    }
}

