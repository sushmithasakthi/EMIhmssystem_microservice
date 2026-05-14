package com.emi_hms_microservice.hms_service.service;

import com.emi_hms_microservice.hms_service.dto.PatientDTO;
import com.emi_hms_microservice.hms_service.model.Patient;
import com.emi_hms_microservice.hms_service.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PatientService {
    
    @Autowired
    private PatientRepository patientRepository;
    
    public PatientDTO createPatient(PatientDTO patientDTO) {
        if (patientRepository.existsByEmail(patientDTO.getEmail())) {
            throw new RuntimeException("Patient with email " + patientDTO.getEmail() + " already exists");
        }
        if (patientRepository.existsByPhone(patientDTO.getPhone())) {
            throw new RuntimeException("Patient with phone " + patientDTO.getPhone() + " already exists");
        }
        
        Patient patient = new Patient(
            patientDTO.getName(),
            patientDTO.getEmail(),
            patientDTO.getPhone(),
            patientDTO.getAddress(),
            patientDTO.getGender(),
            patientDTO.getDob(),
            patientDTO.getBloodGroup()
        );
        
        Patient savedPatient = patientRepository.save(patient);
        return convertToDTO(savedPatient);
    }
    
    public PatientDTO getPatientById(UUID id) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        return convertToDTO(patient);
    }
    
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public PatientDTO updatePatient(UUID id, PatientDTO patientDTO) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        
        // Check email uniqueness if changed
        if (!patient.getEmail().equals(patientDTO.getEmail()) && 
            patientRepository.existsByEmail(patientDTO.getEmail())) {
            throw new RuntimeException("Patient with email " + patientDTO.getEmail() + " already exists");
        }
        
        // Check phone uniqueness if changed
        if (!patient.getPhone().equals(patientDTO.getPhone()) && 
            patientRepository.existsByPhone(patientDTO.getPhone())) {
            throw new RuntimeException("Patient with phone " + patientDTO.getPhone() + " already exists");
        }
        
        patient.setName(patientDTO.getName());
        patient.setEmail(patientDTO.getEmail());
        patient.setPhone(patientDTO.getPhone());
        patient.setAddress(patientDTO.getAddress());
        patient.setGender(patientDTO.getGender());
        patient.setDob(patientDTO.getDob());
        patient.setBloodGroup(patientDTO.getBloodGroup());
        
        Patient updatedPatient = patientRepository.save(patient);
        return convertToDTO(updatedPatient);
    }
    
    public void deletePatient(UUID id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }
    
    private PatientDTO convertToDTO(Patient patient) {
        return new PatientDTO(
            patient.getId(),
            patient.getName(),
            patient.getEmail(),
            patient.getPhone(),
            patient.getAddress(),
            patient.getGender(),
            patient.getDob(),
            patient.getBloodGroup()
        );
    }
}



