package com.company.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import com.company.model.dto.department.DepartmentForFilterDTO;
import com.company.model.entity.Account;
import com.company.model.entity.Account.Role;
import com.company.model.entity.Department;
import com.company.model.form.department.AccountFilterForm;
import com.company.model.form.department.CreatingDepartmentForm;
import com.company.model.form.department.DepartmentFilterForm;
import com.company.model.form.department.ImportedAccountIntoDepartmentForm;
import com.company.model.form.department.UpdatingDepartmentForm;
import com.company.model.specification.department.AccountSpecification;
import com.company.model.specification.department.DepartmentSpecification;
import com.company.repository.IAccountRepository;
import com.company.repository.IDepartmentRepository;

@Service
@Transactional
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

	@Override
	public boolean isDepartmentExistsByName(String name) {
		return departmentRepository.existsByName(name);
	}

	@Override
	public void createDepartment(CreatingDepartmentForm form) {
		// create department
		Department department = Department.builder()
				.name(form.getName())
				.manager(Account.builder().id(form.getManagerId()) .build())
				.creator(securityUtils.getCurrentAccountLogin())
				.createdDateTime(new Date())
				.modifier(securityUtils.getCurrentAccountLogin())
				.updatedDateTime(new Date())
				.build();
		Department entity = departmentRepository.save(department);
		
		// add accounts to new department
		if(form.getEmployeeIds() != null && form.getEmployeeIds().size() > 0) {
			accountRepository.updateDepartment(entity.getId(), form.getEmployeeIds());
		}
		
		// add manager
		Account manager = accountRepository.findById(form.getManagerId()).get();
		manager.setRole(Role.MANAGER);
		manager.setDepartment(entity);
		accountRepository.save(manager);
		
		// update member_size
		department.setMemberSize(1 + (form.getEmployeeIds() == null ? 0 : form.getEmployeeIds().size()));
	}
	
	@Override
	public void updateDepartment(Integer departmentId, UpdatingDepartmentForm form) {
		Department department = departmentRepository.findById(departmentId).get();
		
		Account oldManager = department.getManager();
		if(oldManager.getId() != form.getManagerId()) {
			// update role of old manager			
			oldManager.setRole(Role.EMPLOYEE);
			accountRepository.save(oldManager);
			// update role of new manager
			Account newManager = accountRepository.findById(form.getManagerId()).get();
			newManager.setRole(Role.MANAGER);
			// update manager of department
			department.setManager(newManager);
		}
		
		department.setName(form.getName());
		department.setModifier(securityUtils.getCurrentAccountLogin());
		department.setUpdatedDateTime(new Date());
		departmentRepository.save(department);
	}
	
	@Override
	public boolean isDepartmentHasUser(Integer departmentId) {
		Optional<Department> departmentOptional = departmentRepository.findById(departmentId);
		if (departmentOptional.isEmpty()) {
			return false;
		}
		Department department = departmentOptional.get();
		return department.getMemberSize() != 0;
	}
	
	@Override
	public void deleteDepartment(Integer departmentId) {
		departmentRepository.deleteById(departmentId);
	}
	
	@Override
	public List<DepartmentForFilterDTO> getAllDepartmentsForFilter() {
		// get entity list
		List<Department> entities = departmentRepository.findAll();

		// convert entity to dto page
		List<DepartmentForFilterDTO> dtos = convertListObjectToListObject(entities, DepartmentForFilterDTO.class);

		return dtos;
	}
	
	@Override
	public void importedAccountsIntoDepartment(Integer departmentId, ImportedAccountIntoDepartmentForm form) {
		int addedMember = 0;
		
		// get department & manager
		Department department = departmentRepository.findById(departmentId).get();
		
		// manager
		if(form.getManagerId() != null) {
			// add manager in department table
			Account manager = accountRepository.findById(form.getManagerId()).get();
			department.setManager(manager);
			department.setModifier(securityUtils.getCurrentAccountLogin());
			department.setUpdatedDateTime(new Date());
			departmentRepository.save(department);
			
			// add manager in account table
			manager.setRole(Role.MANAGER);
			manager.setDepartment(department);
			accountRepository.save(manager);
			
			addedMember += 1;
		}
		
		// add employees to department
		accountRepository.updateDepartment(department.getId(), form.getEmployeeIds());
		
		addedMember += form.getEmployeeIds().size();
		
		// update member size of department
		department.setMemberSize(department.getMemberSize() + addedMember);
		departmentRepository.save(department);
	}
}
