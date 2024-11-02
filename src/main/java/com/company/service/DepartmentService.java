package com.company.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.company.model.dto.department.AccountDTO;
import com.company.model.dto.department.DepartmentDTO;
import com.company.model.dto.department.DepartmentDetailDTO;
import com.company.model.form.department.AccountFilterForm;
import com.company.model.form.department.DepartmentFilterForm;

public interface DepartmentService {
	Page<DepartmentDTO> getAllDepartments(Pageable pageable, DepartmentFilterForm form);

	boolean isDepartmentExistsById(Integer id);

	DepartmentDetailDTO getDepartmentById(Integer id);

	Page<AccountDTO> getAllAccountsByDepartmentId(Integer departmentId, Pageable pageable, AccountFilterForm form);

	void removeAccountsInDepartment(List<Integer> accountIds);
}