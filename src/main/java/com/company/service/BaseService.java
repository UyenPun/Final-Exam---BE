package com.company.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class BaseService {

	@Autowired
	protected ModelMapper modelMapper;

	// convert type1 --> type2
	public <TYPE1, TYPE2> TYPE2 convertObjectToObject(final TYPE1 type1, final Class<TYPE2> type2Class) {
		return modelMapper.map(type1, type2Class);
	}

	// convert list type1 --> type2
	public <TYPE1, TYPE2> List<TYPE2> convertListObjectToListObject(final List<TYPE1> type1List, final Class<TYPE2> type2Class) {
		return type1List.stream()
				.map(type1 -> convertObjectToObject(type1, type2Class))
				.collect(Collectors.toList());
	}

	// convert page type1 --> type2
	protected <TYPE1, TYPE2> Page<TYPE2> convertObjectPageToObjectPage(final Page<TYPE1> type1Page, final Pageable pageable, final Class<TYPE2> type2Class) {
		List<TYPE1> type1List = type1Page.getContent();

		List<TYPE2> type2List = convertListObjectToListObject(type1List, type2Class);

		return new PageImpl<>(type2List, pageable, type1Page.getTotalElements());
	}
}

