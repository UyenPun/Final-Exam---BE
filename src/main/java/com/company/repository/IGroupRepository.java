package com.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.model.entity.Group;

public interface IGroupRepository extends JpaRepository<Group, Integer> {
}