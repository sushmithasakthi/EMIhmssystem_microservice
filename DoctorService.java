package com.emi_hms_microservice.hms_service.service;

import com.emi_hms_microservice.hms_service.model.Doctor;
import com.emi_hms_microservice.hms_service.repository.DoctorRepository;
import com.emi_hms_microservice.hms_service.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DoctorService {
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    public Doctor createDoctor(Doctor doctor) {
        if (doctorRepository.existsByEmail(doctor.getEmail())) {
            throw new RuntimeException("Doctor with email " + doctor.getEmail() + " already exists");
        }
        if (!departmentRepository.existsById(doctor.getDepartmentId())) {
            throw new RuntimeException("Department not found with id: " + doctor.getDepartmentId());
        }
        return doctorRepository.save(doctor);
    }
    
    public Doctor getDoctorById(UUID id) {
        return doctorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
    }
    
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
    
    public List<Doctor> getDoctorsByDepartment(UUID departmentId) {
        return doctorRepository.findByDepartmentId(departmentId);
    }
    
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }
    
    public Doctor updateDoctor(UUID id, Doctor doctor) {
        Doctor existingDoctor = doctorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        
        if (!existingDoctor.getEmail().equals(doctor.getEmail()) && 
            doctorRepository.existsByEmail(doctor.getEmail())) {
            throw new RuntimeException("Doctor with email " + doctor.getEmail() + " already exists");
        }
        
        if (!departmentRepository.existsById(doctor.getDepartmentId())) {
            throw new RuntimeException("Department not found with id: " + doctor.getDepartmentId());
        }
        
        existingDoctor.setName(doctor.getName());
        existingDoctor.setEmail(doctor.getEmail());
        existingDoctor.setPhone(doctor.getPhone());
        existingDoctor.setSpecialization(doctor.getSpecialization());
        existingDoctor.setDepartmentId(doctor.getDepartmentId());
        existingDoctor.setQualification(doctor.getQualification());
        existingDoctor.setExperience(doctor.getExperience());
        
        return doctorRepository.save(existingDoctor);
    }
    
    public void deleteDoctor(UUID id) {
        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("Doctor not found with id: " + id);
        }
        doctorRepository.deleteById(id);
    }
}



