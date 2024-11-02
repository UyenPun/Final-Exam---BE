package com.company.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.company.model.dto.department.DepartmentDTO;
import com.company.model.form.department.DepartmentFilterForm;

public interface DepartmentService {
	Page<DepartmentDTO> getAllDepartments(Pageable pageable, DepartmentFilterForm form);
}