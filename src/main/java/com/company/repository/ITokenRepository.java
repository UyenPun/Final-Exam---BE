package com.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.company.model.entity.Account;
import com.company.model.entity.Token;
import com.company.model.entity.Token.Type;

public interface ITokenRepository extends JpaRepository<Token, Integer> {

	@Modifying
	void deleteByTypeAndAccount(Type type, Account account);

	Token findBykeyAndType(String key, Type type);
}
