package com.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.company.model.dto.department.DepartmentDTO;
import com.company.model.entity.Department;
import com.company.repository.IDepartmentRepository;

@Service
public class DepartmentServiceImpl extends BaseService implements DepartmentService {

	@Autowired
	private IDepartmentRepository departmentRepository;

	@Override
	public Page<DepartmentDTO> getAllDepartments(Pageable pageable) {

		// get entity page
		Page<Department> entityPage = departmentRepository.findAll(pageable);

		// convert entity to dto page
		Page<DepartmentDTO> dtoPage = convertObjectPageToObjectPage(entityPage, pageable, DepartmentDTO.class);

		return dtoPage;
	}
}
