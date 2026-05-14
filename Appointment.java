package com.emi_hms_microservice.hms_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotNull(message = "Patient ID is required")
    @Column(name = "patient_id")
    private UUID patientId;
    
    @NotNull(message = "Doctor ID is required")
    @Column(name = "doctor_id")
    private UUID doctorId;
    
    @NotNull(message = "Appointment date and time is required")
    @Column(name = "appointment_date")
    private LocalDateTime appointmentDate;
    
    @Column(name = "appointment_status")
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
    
    private String reason;
    
    private String notes;
    
    public enum AppointmentStatus {
        SCHEDULED, CONFIRMED, COMPLETED, CANCELLED
    }
    
    // Constructors
    public Appointment() {
        this.status = AppointmentStatus.SCHEDULED;
    }
    
    public Appointment(UUID patientId, UUID doctorId, LocalDateTime appointmentDate, 
                       String reason, String notes) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.reason = reason;
        this.notes = notes;
        this.status = AppointmentStatus.SCHEDULED;
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
    
    public UUID getDoctorId() {
        return doctorId;
    }
    
    public void setDoctorId(UUID doctorId) {
        this.doctorId = doctorId;
    }
    
    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }
    
    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    
    public AppointmentStatus getStatus() {
        return status;
    }
    
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", appointmentDate=" + appointmentDate +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}


