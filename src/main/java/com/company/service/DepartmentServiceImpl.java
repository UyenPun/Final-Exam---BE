package com.company.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.config.security.SecurityUtils;
import com.company.model.dto.department.AccountDTO;
import com.company.model.dto.department.DepartmentDTO;
import com.company.model.dto.department.DepartmentDetailDTO;
import com.company.model.entity.Account;
import com.company.model.entity.Account.Role;
import com.company.model.entity.Department;
import com.company.model.form.department.AccountFilterForm;
import com.company.model.form.department.DepartmentFilterForm;
import com.company.model.specification.department.AccountSpecification;
import com.company.model.specification.department.DepartmentSpecification;
import com.company.repository.IAccountRepository;
import com.company.repository.IDepartmentRepository;

@Service
public class DepartmentServiceImpl extends BaseService implements DepartmentService {

	@Autowired
	private IDepartmentRepository departmentRepository;

	@Autowired
	private IAccountRepository accountRepository;

	@Autowired
	private SecurityUtils securityUtils;

	@Override
	public Page<DepartmentDTO> getAllDepartments(Pageable pageable, DepartmentFilterForm form) {

		Specification<Department> where = DepartmentSpecification.buildWhere(form);

		// get entity page
		Page<Department> entityPage = departmentRepository.findAll(where, pageable);

		// convert entity to dto page
		Page<DepartmentDTO> dtoPage = convertObjectPageToObjectPage(entityPage, pageable, DepartmentDTO.class);

		return dtoPage;
	}

	@Override
	public boolean isDepartmentExistsById(Integer id) {
		return departmentRepository.existsById(id);
	}

	@Override
	public DepartmentDetailDTO getDepartmentById(Integer id) {
		// get entity
		Department entity = departmentRepository.findById(id).get();

		// convert entity to dto
		DepartmentDetailDTO dto = convertObjectToObject(entity, DepartmentDetailDTO.class);

		return dto;
	}

	@Override
	public Page<AccountDTO> getAllAccountsByDepartmentId(Integer departmentId, Pageable pageable,
			AccountFilterForm form) {
		Specification<Account> where = AccountSpecification.buildWhere(departmentId, form);

		// get entity page
		Page<Account> entityPage = accountRepository.findAll(where, pageable);

		// convert entity to dto page
		Page<AccountDTO> dtoPage = convertObjectPageToObjectPage(entityPage, pageable, AccountDTO.class);

		return dtoPage;
	}

	@Transactional
	@Override
	public void removeAccountsInDepartment(List<Integer> accountIds) {
		for (Integer accountId : accountIds) {
			Account account = accountRepository.findById(accountId).get();
			// update member_size & manager in department
			Department department = account.getDepartment();
			department.setMemberSize(department.getMemberSize() - 1);

			// Nếu thằng bị xóa là 'MANAGER' thì phải remove manager_id bên bảng Department
			// vì dưới dtb có trường này
			if (account.getRole().equals(Role.MANAGER)) {
				department.setManager(null);
			}
			departmentRepository.save(department);

			// update department field in account
			account.setDepartment(null);
			if (account.getRole().equals(Role.MANAGER)) {
				account.setRole(Role.EMPLOYEE);
			}
			account.setModifier(securityUtils.getCurrentAccountLogin());
			account.setUpdatedDateTime(new Date());
			accountRepository.save(account);
		}

	}
}
