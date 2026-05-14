package com.emi_hms_microservice.hms_service.repository;

import com.emi_hms_microservice.hms_service.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    List<Invoice> findByPatientId(UUID patientId);
    List<Invoice> findByAppointmentId(UUID appointmentId);
    List<Invoice> findByEmiPlanId(UUID emiPlanId);
    List<Invoice> findByStatus(Invoice.InvoiceStatus status);
    List<Invoice> findByPaymentStatus(Invoice.PaymentStatus paymentStatus);
    List<Invoice> findByPatientIdAndStatus(UUID patientId, Invoice.InvoiceStatus status);
    List<Invoice> findByInvoiceDateBetween(LocalDate startDate, LocalDate endDate);
    List<Invoice> findByDueDateBeforeAndPaymentStatusNot(LocalDate date, Invoice.PaymentStatus paymentStatus);
    
    @Query("SELECT i FROM Invoice i WHERE i.patientId = :patientId AND i.paymentStatus != 'PAID'")
    List<Invoice> findUnpaidInvoicesByPatient(UUID patientId);
    
    boolean existsByInvoiceNumber(String invoiceNumber);
}

