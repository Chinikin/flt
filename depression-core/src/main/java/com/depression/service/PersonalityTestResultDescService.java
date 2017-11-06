package com.depression.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.PersonalityTestResultDescMapper;
import com.depression.model.PersonalityTestResultDesc;

@Service
public class PersonalityTestResultDescService
{
	@Autowired
	PersonalityTestResultDescMapper mapper;
	
	public List<PersonalityTestResultDesc> selectSelective(PersonalityTestResultDesc record)
	{
		return mapper.selectSelective(record);
	}

}
