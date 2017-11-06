package com.depression.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.PersonalityTestStatisticsMapper;
import com.depression.model.PersonalityTestStatistics;

@Service
public class PersonalityTestStatisticsService
{
	@Autowired
	PersonalityTestStatisticsMapper mapper;

	public List<PersonalityTestStatistics> selectSelective(PersonalityTestStatistics record)
	{
		return mapper.selectSelective(record);
	}

}
