package com.company.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.model.dto.department.AccountDTO;
import com.company.model.dto.department.DepartmentDTO;
import com.company.model.dto.department.DepartmentDetailDTO;
import com.company.model.form.department.AccountFilterForm;
import com.company.model.form.department.CreatingDepartmentForm;
import com.company.model.form.department.DepartmentFilterForm;
import com.company.model.form.department.UpdatingDepartmentForm;
import com.company.model.validation.account.AccountIdExists;
import com.company.model.validation.department.DepartmentIdExists;
import com.company.model.validation.department.DepartmentNoUser;
import com.company.model.validation.interf.NameNotExistsGroup;
import com.company.service.DepartmentService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping(value = "/api/v1/departments")
@Validated
@Log4j2
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private Validator validator;

	@GetMapping
	public Page<DepartmentDTO> getAllDepartments(Pageable pageable, @Valid DepartmentFilterForm form) {
		return departmentService.getAllDepartments(pageable, form);
	}

	@GetMapping("/{id}")
	public DepartmentDetailDTO getDepartmentById(@PathVariable(name = "id") @DepartmentIdExists Integer id) {
		return departmentService.getDepartmentById(id);
	}

	@GetMapping("/{id}/accounts")
	public Page<AccountDTO> getAllAccountsByDepartmentId(
			@PathVariable(name = "id") @DepartmentIdExists Integer departmentId, Pageable pageable,
			@Valid AccountFilterForm form) {
		return departmentService.getAllAccountsByDepartmentId(departmentId, pageable, form);
	}

	@DeleteMapping("/accounts/{accountIds}")
	public String removeAccountsInDepartment(
			@PathVariable(name = "accountIds") List<@AccountIdExists Integer> accountIds) {
		departmentService.removeAccountsInDepartment(accountIds);
		return "Remove accounts successfully!";
	}

	@GetMapping("/name/exists")
	public boolean isDepartmentExistsByName(String name) {
		return departmentService.isDepartmentExistsByName(name);
	}

	@PostMapping
	public String createDepartment(@RequestBody @Valid CreatingDepartmentForm form) {
		departmentService.createDepartment(form);
		return "create successfully!";
	}

	@PutMapping("/{id}")
	public String updateDepartment(@PathVariable(name = "id") @DepartmentIdExists Integer departmentId,
			@RequestBody @Valid UpdatingDepartmentForm form) {

		// validate department name exists
		if (!departmentService.getDepartmentById(departmentId).getName().equals(form.getName())) {
			Set<ConstraintViolation<UpdatingDepartmentForm>> result = validator.validate(form,
					NameNotExistsGroup.class);
			if (result.size() != 0) {
				throw new ConstraintViolationException(result);
			}
		}

		// update department
		departmentService.updateDepartment(departmentId, form);

		return "update successfully!";
	}

	// Delete
	@DeleteMapping("/{id}")
	public String deleteDepartment(
			@PathVariable(name = "id") @DepartmentIdExists @DepartmentNoUser Integer departmentId) {
		departmentService.deleteDepartment(departmentId);
		return "delete successfully!";
	}
}
