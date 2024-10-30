package com.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.model.dto.department.DepartmentDTO;
import com.company.service.DepartmentService;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping(value = "/api/v1/departments")
@Validated
@Log4j2
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;

	@GetMapping
	public Page<DepartmentDTO> getAllDepartments(Pageable pageable) {
		return departmentService.getAllDepartments(pageable);
	}

}
