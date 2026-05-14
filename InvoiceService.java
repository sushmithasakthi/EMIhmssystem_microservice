package com.emi_hms_microservice.hms_service.service;

import com.emi_hms_microservice.hms_service.dto.InvoiceDTO;
import com.emi_hms_microservice.hms_service.model.Invoice;
import com.emi_hms_microservice.hms_service.model.InvoiceItem;
import com.emi_hms_microservice.hms_service.repository.InvoiceRepository;
import com.emi_hms_microservice.hms_service.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvoiceService {
    
    @Autowired
    private InvoiceRepository invoiceRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        // Validate patient exists
        if (!patientRepository.existsById(invoiceDTO.getPatientId())) {
            throw new RuntimeException("Patient not found with id: " + invoiceDTO.getPatientId());
        }
        
        // Create invoice entity
        Invoice invoice = new Invoice();
        invoice.setPatientId(invoiceDTO.getPatientId());
        invoice.setAppointmentId(invoiceDTO.getAppointmentId());
        invoice.setEmiPlanId(invoiceDTO.getEmiPlanId());
        invoice.setInvoiceDate(invoiceDTO.getInvoiceDate() != null ? invoiceDTO.getInvoiceDate() : LocalDate.now());
        invoice.setDueDate(invoiceDTO.getDueDate());
        invoice.setTaxRate(invoiceDTO.getTaxRate());
        invoice.setDiscountAmount(invoiceDTO.getDiscountAmount() != null ? invoiceDTO.getDiscountAmount() : BigDecimal.ZERO);
        invoice.setNotes(invoiceDTO.getNotes());
        invoice.setBillingAddress(invoiceDTO.getBillingAddress());
        invoice.setBillingContact(invoiceDTO.getBillingContact());
        invoice.setStatus(Invoice.InvoiceStatus.ISSUED);
        
        // Add invoice items
        if (invoiceDTO.getItems() != null && !invoiceDTO.getItems().isEmpty()) {
            for (InvoiceDTO.InvoiceItemDTO itemDTO : invoiceDTO.getItems()) {
                InvoiceItem item = new InvoiceItem();
                item.setDescription(itemDTO.getDescription());
                item.setQuantity(itemDTO.getQuantity());
                item.setUnitPrice(itemDTO.getUnitPrice());
                item.setItemType(itemDTO.getItemType());
                item.setNotes(itemDTO.getNotes());
                invoice.addItem(item);
            }
        }
        
        // Calculate totals
        invoice.calculateTotals();
        
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return convertToDTO(savedInvoice);
    }
    
    public InvoiceDTO getInvoiceById(UUID id) {
        Invoice invoice = invoiceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        return convertToDTO(invoice);
    }
    
    public InvoiceDTO getInvoiceByNumber(String invoiceNumber) {
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber)
            .orElseThrow(() -> new RuntimeException("Invoice not found with number: " + invoiceNumber));
        return convertToDTO(invoice);
    }
    
    public List<InvoiceDTO> getAllInvoices() {
        return invoiceRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<InvoiceDTO> getInvoicesByPatient(UUID patientId) {
        return invoiceRepository.findByPatientId(patientId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<InvoiceDTO> getInvoicesByStatus(Invoice.InvoiceStatus status) {
        return invoiceRepository.findByStatus(status).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<InvoiceDTO> getUnpaidInvoicesByPatient(UUID patientId) {
        return invoiceRepository.findUnpaidInvoicesByPatient(patientId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public InvoiceDTO updateInvoice(UUID id, InvoiceDTO invoiceDTO) {
        Invoice invoice = invoiceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        
        // Update basic fields
        invoice.setAppointmentId(invoiceDTO.getAppointmentId());
        invoice.setEmiPlanId(invoiceDTO.getEmiPlanId());
        invoice.setInvoiceDate(invoiceDTO.getInvoiceDate());
        invoice.setDueDate(invoiceDTO.getDueDate());
        invoice.setTaxRate(invoiceDTO.getTaxRate());
        invoice.setDiscountAmount(invoiceDTO.getDiscountAmount() != null ? invoiceDTO.getDiscountAmount() : BigDecimal.ZERO);
        invoice.setNotes(invoiceDTO.getNotes());
        invoice.setBillingAddress(invoiceDTO.getBillingAddress());
        invoice.setBillingContact(invoiceDTO.getBillingContact());
        
        // Update items
        invoice.getItems().clear();
        if (invoiceDTO.getItems() != null && !invoiceDTO.getItems().isEmpty()) {
            for (InvoiceDTO.InvoiceItemDTO itemDTO : invoiceDTO.getItems()) {
                InvoiceItem item = new InvoiceItem();
                item.setDescription(itemDTO.getDescription());
                item.setQuantity(itemDTO.getQuantity());
                item.setUnitPrice(itemDTO.getUnitPrice());
                item.setItemType(itemDTO.getItemType());
                item.setNotes(itemDTO.getNotes());
                invoice.addItem(item);
            }
        }
        
        // Recalculate totals
        invoice.calculateTotals();
        
        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return convertToDTO(updatedInvoice);
    }
    
    public InvoiceDTO recordPayment(UUID id, BigDecimal paymentAmount) {
        Invoice invoice = invoiceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        
        BigDecimal currentPaid = invoice.getPaidAmount() != null ? invoice.getPaidAmount() : BigDecimal.ZERO;
        BigDecimal newPaidAmount = currentPaid.add(paymentAmount);
        
        if (newPaidAmount.compareTo(invoice.getTotalAmount()) > 0) {
            throw new RuntimeException("Payment amount exceeds total invoice amount");
        }
        
        invoice.setPaidAmount(newPaidAmount);
        invoice.calculateTotals();
        
        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return convertToDTO(updatedInvoice);
    }
    
    public InvoiceDTO updateInvoiceStatus(UUID id, Invoice.InvoiceStatus status) {
        Invoice invoice = invoiceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        invoice.setStatus(status);
        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return convertToDTO(updatedInvoice);
    }
    
    public void deleteInvoice(UUID id) {
        if (!invoiceRepository.existsById(id)) {
            throw new RuntimeException("Invoice not found with id: " + id);
        }
        invoiceRepository.deleteById(id);
    }
    
    private InvoiceDTO convertToDTO(Invoice invoice) {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setId(invoice.getId());
        dto.setInvoiceNumber(invoice.getInvoiceNumber());
        dto.setPatientId(invoice.getPatientId());
        dto.setAppointmentId(invoice.getAppointmentId());
        dto.setEmiPlanId(invoice.getEmiPlanId());
        dto.setInvoiceDate(invoice.getInvoiceDate());
        dto.setDueDate(invoice.getDueDate());
        dto.setSubtotal(invoice.getSubtotal());
        dto.setTaxAmount(invoice.getTaxAmount());
        dto.setDiscountAmount(invoice.getDiscountAmount());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setPaidAmount(invoice.getPaidAmount());
        dto.setRemainingAmount(invoice.getRemainingAmount());
        dto.setStatus(invoice.getStatus());
        dto.setPaymentStatus(invoice.getPaymentStatus());
        dto.setTaxRate(invoice.getTaxRate());
        dto.setNotes(invoice.getNotes());
        dto.setBillingAddress(invoice.getBillingAddress());
        dto.setBillingContact(invoice.getBillingContact());
        
        // Convert items
        if (invoice.getItems() != null) {
            List<InvoiceDTO.InvoiceItemDTO> itemDTOs = invoice.getItems().stream()
                .map(item -> {
                    InvoiceDTO.InvoiceItemDTO itemDTO = new InvoiceDTO.InvoiceItemDTO();
                    itemDTO.setId(item.getId());
                    itemDTO.setDescription(item.getDescription());
                    itemDTO.setQuantity(item.getQuantity());
                    itemDTO.setUnitPrice(item.getUnitPrice());
                    itemDTO.setItemType(item.getItemType());
                    itemDTO.setNotes(item.getNotes());
                    return itemDTO;
                })
                .collect(Collectors.toList());
            dto.setItems(itemDTOs);
        }
        
        return dto;
    }
}

