package com.emi_hms_microservice.hms_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.UUID;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Phone is required")
    private String phone;
    
    @NotBlank(message = "Specialization is required")
    private String specialization;
    
    @NotNull(message = "Department ID is required")
    @Column(name = "department_id")
    private UUID departmentId;
    
    @NotBlank(message = "Qualification is required")
    private String qualification;
    
    private Integer experience;
    
    // Constructors
    public Doctor() {
    }
    
    public Doctor(String name, String email, String phone, String specialization, 
                  UUID departmentId, String qualification, Integer experience) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
        this.departmentId = departmentId;
        this.qualification = qualification;
        this.experience = experience;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public UUID getDepartmentId() {
        return departmentId;
    }
    
    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }
    
    public String getQualification() {
        return qualification;
    }
    
    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
    
    public Integer getExperience() {
        return experience;
    }
    
    public void setExperience(Integer experience) {
        this.experience = experience;
    }
    
    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", specialization='" + specialization + '\'' +
                ", departmentId=" + departmentId +
                ", qualification='" + qualification + '\'' +
                ", experience=" + experience +
                '}';
    }
}


