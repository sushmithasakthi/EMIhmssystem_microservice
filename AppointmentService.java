package com.emi_hms_microservice.hms_service.service;

import com.emi_hms_microservice.hms_service.model.Appointment;
import com.emi_hms_microservice.hms_service.repository.AppointmentRepository;
import com.emi_hms_microservice.hms_service.repository.DoctorRepository;
import com.emi_hms_microservice.hms_service.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    public Appointment createAppointment(Appointment appointment) {
        // Validate patient exists
        if (!patientRepository.existsById(appointment.getPatientId())) {
            throw new RuntimeException("Patient not found with id: " + appointment.getPatientId());
        }
        
        // Validate doctor exists
        if (!doctorRepository.existsById(appointment.getDoctorId())) {
            throw new RuntimeException("Doctor not found with id: " + appointment.getDoctorId());
        }
        
        // Check if appointment time is in the future
        if (appointment.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Appointment date cannot be in the past");
        }
        
        // Check for conflicting appointments
        LocalDateTime startTime = appointment.getAppointmentDate().minusMinutes(30);
        LocalDateTime endTime = appointment.getAppointmentDate().plusMinutes(30);
        List<Appointment> conflictingAppointments = appointmentRepository
            .findByDoctorIdAndAppointmentDateBetween(
                appointment.getDoctorId(), startTime, endTime);
        
        if (!conflictingAppointments.isEmpty()) {
            throw new RuntimeException("Doctor has a conflicting appointment at this time");
        }
        
        return appointmentRepository.save(appointment);
    }
    
    public Appointment getAppointmentById(UUID id) {
        return appointmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
    }
    
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
    
    public List<Appointment> getAppointmentsByPatient(UUID patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }
    
    public List<Appointment> getAppointmentsByDoctor(UUID doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }
    
    public Appointment updateAppointmentStatus(UUID id, Appointment.AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }
    
    public Appointment updateAppointment(UUID id, Appointment appointment) {
        Appointment existingAppointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        
        if (!patientRepository.existsById(appointment.getPatientId())) {
            throw new RuntimeException("Patient not found with id: " + appointment.getPatientId());
        }
        
        if (!doctorRepository.existsById(appointment.getDoctorId())) {
            throw new RuntimeException("Doctor not found with id: " + appointment.getDoctorId());
        }
        
        existingAppointment.setPatientId(appointment.getPatientId());
        existingAppointment.setDoctorId(appointment.getDoctorId());
        existingAppointment.setAppointmentDate(appointment.getAppointmentDate());
        existingAppointment.setStatus(appointment.getStatus());
        existingAppointment.setReason(appointment.getReason());
        existingAppointment.setNotes(appointment.getNotes());
        
        return appointmentRepository.save(existingAppointment);
    }
    
    public void deleteAppointment(UUID id) {
        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("Appointment not found with id: " + id);
        }
        appointmentRepository.deleteById(id);
    }
}



