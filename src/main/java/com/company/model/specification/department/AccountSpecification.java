package com.company.model.specification.department;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.company.model.entity.Account;
import com.company.model.form.department.AccountFilterForm;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public class AccountSpecification {

	@SuppressWarnings("deprecation")
	public static Specification<Account> buildWhere(Integer departmentId, AccountFilterForm form) {

		// lấy những acc trong department
		// Bắt buộc phải có department
		Specification<Account> where = Specification.where(new CustomSpecification("departmentId", departmentId));

		if (form == null)
			return where;

		if (form.getQ() != null && !StringUtils.isEmpty(form.getQ().trim())) {
			String search = form.getQ().trim();
			CustomSpecification usernameSpecification = new CustomSpecification("username", search);
			CustomSpecification fullnameSpecification = new CustomSpecification("fullname", search);
			where = where.and(usernameSpecification.or(fullnameSpecification));
		}

		if (form.getMinCreatedDate() != null) {
			CustomSpecification minCreatedDateSpecification = new CustomSpecification("minCreatedDate",
					form.getMinCreatedDate());
			where = where.and(minCreatedDateSpecification);
		}

		if (form.getMaxCreatedDate() != null) {
			CustomSpecification maxCreatedDateSpecification = new CustomSpecification("maxCreatedDate",
					form.getMaxCreatedDate());
			where = where.and(maxCreatedDateSpecification);
		}

		if (form.getRole() != null) {
			CustomSpecification roleSpecification = new CustomSpecification("role", form.getRole());
			where = where.and(roleSpecification);
		}

		if (form.getStatus() != null) {
			CustomSpecification statusSpecification = new CustomSpecification("status", form.getStatus());
			where = where.and(statusSpecification);
		}

		return where;
	}

	@SuppressWarnings("serial")
	@RequiredArgsConstructor
	static class CustomSpecification implements Specification<Account> {

		@NonNull
		private String field;

		@NonNull
		private Object value;

		@Override
		public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

			if (field.equalsIgnoreCase("departmentId")) {
				return criteriaBuilder.like(root.get("department").get("id"), value.toString());
			}

			if (field.equalsIgnoreCase("username")) {
				return criteriaBuilder.like(root.get("username"), "%" + value.toString() + "%");
			}

			if (field.equalsIgnoreCase("fullname")) {
				return criteriaBuilder.like(root.get("fullname"), "%" + value.toString() + "%");
			}

			if (field.equalsIgnoreCase("minCreatedDate")) {
				return criteriaBuilder.greaterThanOrEqualTo(root.get("createdDateTime").as(java.sql.Date.class),
						(Date) value);
			}

			if (field.equalsIgnoreCase("maxCreatedDate")) {
				return criteriaBuilder.lessThanOrEqualTo(root.get("createdDateTime").as(java.sql.Date.class),
						(Date) value);
			}

			if (field.equalsIgnoreCase("role")) {
				return criteriaBuilder.equal(root.get("role"), value);
			}

			if (field.equalsIgnoreCase("status")) {
				return criteriaBuilder.equal(root.get("status"), value);
			}

			return null;
		}
	}

}
