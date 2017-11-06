package com.depression.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.PersonalityHarmonyDescriptionMapper;
import com.depression.model.PersonalityHarmonyDescription;

@Service
public class PersonalityHarmonyDescriptionService
{
	@Autowired
	PersonalityHarmonyDescriptionMapper mapper;

	public PersonalityHarmonyDescription selectByPrimaryKey(Long harmonyId)
	{
		return mapper.selectByPrimaryKey(harmonyId);
	}

}
