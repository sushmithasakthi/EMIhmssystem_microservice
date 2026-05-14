package com.emi_hms_microservice.hms_service.service;

import com.emi_hms_microservice.hms_service.model.Department;
import com.emi_hms_microservice.hms_service.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DepartmentService {
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    public Department createDepartment(Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new RuntimeException("Department with name " + department.getName() + " already exists");
        }
        return departmentRepository.save(department);
    }
    
    public Department getDepartmentById(UUID id) {
        return departmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }
    
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    
    public Department updateDepartment(UUID id, Department department) {
        Department existingDepartment = departmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        
        if (!existingDepartment.getName().equals(department.getName()) && 
            departmentRepository.existsByName(department.getName())) {
            throw new RuntimeException("Department with name " + department.getName() + " already exists");
        }
        
        existingDepartment.setName(department.getName());
        existingDepartment.setDescription(department.getDescription());
        existingDepartment.setLocation(department.getLocation());
        
        return departmentRepository.save(existingDepartment);
    }
    
    public void deleteDepartment(UUID id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }
}



