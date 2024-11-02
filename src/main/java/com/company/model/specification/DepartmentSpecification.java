package com.company.model.specification;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.company.model.entity.Department;
import com.company.model.form.department.DepartmentFilterForm;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public class DepartmentSpecification {

	@SuppressWarnings("deprecation")
	public static Specification<Department> buildWhere(DepartmentFilterForm form) {

		Specification<Department> where = null;
		
		if (form == null) return where;

		if (form.getQ() != null && !StringUtils.isEmpty(form.getQ().trim())) {
			String search = form.getQ().trim();
			CustomSpecification departmentNameSpecification = new CustomSpecification("departmentName", search);
			CustomSpecification managerFullnameSpecification = new CustomSpecification("managerFullname", search);
			CustomSpecification managerEmailSpecification = new CustomSpecification("managerEmail", search);
			CustomSpecification managerUsernameSpecification = new CustomSpecification("managerUsername", search);
			
			where = Specification
						.where(departmentNameSpecification
								.or(managerFullnameSpecification)
								.or(managerEmailSpecification)
								.or(managerUsernameSpecification)
						);
		}

		if (form.getMinCreatedDate() != null) {
			CustomSpecification minCreatedDateSpecification = new CustomSpecification("minCreatedDate", form.getMinCreatedDate());
			if (where == null) {
				where = minCreatedDateSpecification;
			} else {
				where = where.and(minCreatedDateSpecification);
			}
		}

		if (form.getMaxCreatedDate() != null) {
			CustomSpecification maxCreatedDateSpecification = new CustomSpecification("maxCreatedDate", form.getMaxCreatedDate());
			if (where == null) {
				where = maxCreatedDateSpecification;
			} else {
				where = where.and(maxCreatedDateSpecification);
			}
		}

		if (form.getMinMemberSize() != null) {
			CustomSpecification minMemberSizeSpecification = new CustomSpecification("minMemberSize", form.getMinMemberSize());
			if (where == null) {
				where = minMemberSizeSpecification;
			} else {
				where = where.and(minMemberSizeSpecification);
			}
		}
		
		if (form.getMaxMemberSize() != null) {
			CustomSpecification maxMemberSizeSpecification = new CustomSpecification("maxMemberSize", form.getMaxMemberSize());
			if (where == null) {
				where = maxMemberSizeSpecification;
			} else {
				where = where.and(maxMemberSizeSpecification);
			}
		}

		return where;
	}

	@SuppressWarnings("serial")
	@RequiredArgsConstructor
	static class CustomSpecification implements Specification<Department> {

		@NonNull
		private String field;

		@NonNull
		private Object value;

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public Predicate toPredicate(
				Root<Department> root, 
				CriteriaQuery<?> query, 
				CriteriaBuilder criteriaBuilder) {

			Join managerJoin = root.join("manager", JoinType.LEFT);
			
			if (field.equalsIgnoreCase("departmentName")) {
				return criteriaBuilder.like(root.get("name"), "%" + value.toString() + "%");
			}
			
			if (field.equalsIgnoreCase("managerFullname")) {
				return criteriaBuilder.like(managerJoin.get("fullname"), "%" + value.toString() + "%");
			}
			
			if (field.equalsIgnoreCase("managerEmail")) {
				return criteriaBuilder.like(managerJoin.get("email"), "%" + value.toString() + "%");
			}
			
			if (field.equalsIgnoreCase("managerUsername")) {
				return criteriaBuilder.like(managerJoin.get("username"), "%" + value.toString() + "%");
			}
			
			if (field.equalsIgnoreCase("minCreatedDate")) {
				return criteriaBuilder.greaterThanOrEqualTo(
						root.get("createdDateTime").as(java.sql.Date.class), 
						(Date) value);
			}

			if (field.equalsIgnoreCase("maxCreatedDate")) {
				return criteriaBuilder.lessThanOrEqualTo(
						root.get("createdDateTime").as(java.sql.Date.class), 
						(Date) value);
			}

			if (field.equalsIgnoreCase("minMemberSize")) {
				return criteriaBuilder.greaterThanOrEqualTo(
						root.get("memberSize"), value.toString());
			}

			if (field.equalsIgnoreCase("maxMemberSize")) {
				return criteriaBuilder.lessThanOrEqualTo(
						root.get("memberSize"), value.toString());
			}

			return null;
		}
	}

}
