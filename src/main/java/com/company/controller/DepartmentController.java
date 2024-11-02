package com.company.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.model.dto.department.AccountDTO;
import com.company.model.dto.department.DepartmentDTO;
import com.company.model.dto.department.DepartmentDetailDTO;
import com.company.model.form.department.AccountFilterForm;
import com.company.model.form.department.DepartmentFilterForm;
import com.company.model.validation.account.AccountIdExists;
import com.company.model.validation.department.DepartmentIdExists;
import com.company.service.DepartmentService;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping(value = "/api/v1/departments")
@Validated
@Log4j2
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;

	@GetMapping
	public Page<DepartmentDTO> getAllDepartments(Pageable pageable, @Valid DepartmentFilterForm form) {
		return departmentService.getAllDepartments(pageable, form);
	}

	// get detail department
	@GetMapping("/{id}")
	public DepartmentDetailDTO getDepartmentById(@PathVariable(name = "id") @DepartmentIdExists Integer id) {
		return departmentService.getDepartmentById(id);
	}

	// Get List Account trong Department
	@GetMapping("/{id}/accounts")
	public Page<AccountDTO> getAllAccountsByDepartmentId(
			@PathVariable(name = "id") @DepartmentIdExists Integer departmentId, Pageable pageable,
			@Valid AccountFilterForm form) {
		return departmentService.getAllAccountsByDepartmentId(departmentId, pageable, form);
	}

	// Delete account trong department
	@DeleteMapping("/accounts/{accountIds}")
	public String removeAccountsInDepartment(
			@PathVariable(name = "accountIds") List<@AccountIdExists Integer> accountIds) {
		departmentService.removeAccountsInDepartment(accountIds);
		return "Remove accounts successfully!";
	}

}
