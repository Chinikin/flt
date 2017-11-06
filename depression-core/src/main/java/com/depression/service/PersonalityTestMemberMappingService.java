package com.depression.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.PersonalityTestMemberMappingMapper;
import com.depression.model.PersonalityTestMemberMapping;
import com.depression.model.web.dto.WebPersonalityTestMemberMappingDTO;

@Service
public class PersonalityTestMemberMappingService
{
	@Autowired
	PersonalityTestMemberMappingMapper mapper;
	
	public List<PersonalityTestMemberMapping> selectSelective(PersonalityTestMemberMapping record)
	{
		return mapper.selectSelective(record);
	}

	public List<WebPersonalityTestMemberMappingDTO> selectTestDetail(PersonalityTestMemberMapping record)
	{
		return mapper.selectTestDetail(record);
	}
}
