package com.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.model.entity.Department;

public interface IDepartmentRepository extends JpaRepository<Department, Integer> {

}
