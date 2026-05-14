package com.emi_hms_microservice.hms_service.repository;

import com.emi_hms_microservice.hms_service.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    Optional<Doctor> findByEmail(String email);
    List<Doctor> findByDepartmentId(UUID departmentId);
    List<Doctor> findBySpecialization(String specialization);
    boolean existsByEmail(String email);
}



