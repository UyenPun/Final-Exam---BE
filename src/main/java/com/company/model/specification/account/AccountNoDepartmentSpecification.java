package com.company.model.specification.account;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.company.model.entity.Account;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public class AccountNoDepartmentSpecification {

	@SuppressWarnings("deprecation")
	public static Specification<Account> buildWhere(String q) {

		Specification<Account> where = Specification.where(new CustomSpecification("noDepartment"));
		
		if (q != null && !StringUtils.isEmpty(q.trim())) {
			String search = q.trim();
			CustomSpecification usernameSpecification = new CustomSpecification("username", search);
			CustomSpecification fullnameSpecification = new CustomSpecification("fullname", search);
			
			where = where.and(usernameSpecification.or(fullnameSpecification));
		}
		
		return where;
	}

	@SuppressWarnings("serial")
	@RequiredArgsConstructor
	static class CustomSpecification implements Specification<Account> {

		@NonNull
		private String field;

		private Object value;
		
		public CustomSpecification(String field, Object value) {
			this.field = field;
			this.value = value;
		}

		@Override
		public Predicate toPredicate(
				Root<Account> root, 
				CriteriaQuery<?> query, 
				CriteriaBuilder criteriaBuilder) {

			if (field.equalsIgnoreCase("noDepartment")) {
				return criteriaBuilder.isNull(root.get("department"));
			}
			
			if (field.equalsIgnoreCase("username")) {
				return criteriaBuilder.like(root.get("username"), "%" + value.toString() + "%");
			}
			
			if (field.equalsIgnoreCase("fullname")) {
				return criteriaBuilder.like(root.get("fullname"), "%" + value.toString() + "%");
			}
			
			return null;
		}
	}

}
