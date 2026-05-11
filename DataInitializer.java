package com.emi_hms_microservice.hms_service.config;

import com.emi_hms_microservice.hms_service.model.Department;
import com.emi_hms_microservice.hms_service.model.Doctor;
import com.emi_hms_microservice.hms_service.model.Patient;
import com.emi_hms_microservice.hms_service.repository.DepartmentRepository;
import com.emi_hms_microservice.hms_service.repository.DoctorRepository;
import com.emi_hms_microservice.hms_service.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(DepartmentRepository departmentRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository) {
        return args -> {
            // Check if data already exists
            if (departmentRepository.count() > 0) {
                return;
            }

            System.out.println("Initializing Database with Seed Data...");

            // 1. Create Departments
            Department cardio = new Department();
            cardio.setName("Cardiology");
            cardio.setDescription("Heart and cardiovascular system care");
            cardio.setLocation("Block A, Floor 2");

            Department neuro = new Department();
            neuro.setName("Neurology");
            neuro.setDescription("Brain and nervous system disorders");
            neuro.setLocation("Block B, Floor 1");

            Department ortho = new Department();
            ortho.setName("Orthopedics");
            ortho.setDescription("Treatment of the musculoskeletal system");
            ortho.setLocation("Block A, Floor 1");

            List<Department> savedDepts = departmentRepository.saveAll(Arrays.asList(cardio, neuro, ortho));

            // 2. Create Doctors
            Doctor doc1 = new Doctor();
            doc1.setName("Dr. Sarah Wilson");
            doc1.setEmail("sarah.wilson@hospital.com");
            doc1.setPhone("9876543210");
            doc1.setSpecialization("Cardiologist");
            doc1.setQualification("MBBS, MD (Cardiology)");
            doc1.setExperience(12);
            doc1.setDepartmentId(savedDepts.get(0).getId()); // Cardiology

            Doctor doc2 = new Doctor();
            doc2.setName("Dr. Michael Chen");
            doc2.setEmail("michael.chen@hospital.com");
            doc2.setPhone("9876543211");
            doc2.setSpecialization("Neurologist");
            doc2.setQualification("MBBS, DM (Neurology)");
            doc2.setExperience(8);
            doc2.setDepartmentId(savedDepts.get(1).getId()); // Neurology

            doctorRepository.saveAll(Arrays.asList(doc1, doc2));

            // 3. Create Patients
            Patient p1 = new Patient();
            p1.setName("John Doe");
            p1.setEmail("john.doe@example.com");
            p1.setPhone("1234567890");
            p1.setAddress("123 Maple St, Springfiled");
            p1.setGender("Male");
            p1.setBloodGroup("O+");
            p1.setDob(LocalDate.of(1985, 5, 15));

            Patient p2 = new Patient();
            p2.setName("Jane Smith");
            p2.setEmail("jane.smith@example.com");
            p2.setPhone("1234567891");
            p2.setAddress("456 Oak Ave, Metropolis");
            p2.setGender("Female");
            p2.setBloodGroup("A-");
            p2.setDob(LocalDate.of(1990, 8, 22));

            patientRepository.saveAll(Arrays.asList(p1, p2));

            System.out.println("Database Initialized Successfully.");
        };
    }
}
