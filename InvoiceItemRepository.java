package com.emi_hms_microservice.hms_service.repository;

import com.emi_hms_microservice.hms_service.model.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, UUID> {
    List<InvoiceItem> findByInvoiceId(UUID invoiceId);
    void deleteByInvoiceId(UUID invoiceId);
}

