package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.PersonalityCharactersSimilarityMapper;
import com.depression.model.PersonalityCharactersSimilarity;
import com.depression.model.web.dto.WebPersonalityCharactersSimilarityDTO;

@Service
public class PersonalityCharactersSimilarityService
{
	@Autowired
	PersonalityCharactersSimilarityMapper mapper;
	
	public int insertSelective(PersonalityCharactersSimilarity record)
	{
		record.setCreateTime(new Date());
		record.setModifyTime(new Date());
		return mapper.insertSelective(record);
	}
	
	public List<WebPersonalityCharactersSimilarityDTO> selectSelectiveOrderBySimDesc(PersonalityCharactersSimilarity record)
	{
		return mapper.selectSelectiveOrderBySimDesc(record);
	}
	
	public List<PersonalityCharactersSimilarity> selectSelective(PersonalityCharactersSimilarity record)
	{
		return mapper.selectSelective(record);
	}
	
	public PersonalityCharactersSimilarity selectByPrimaryKey(Long pcsId)
	{
		return mapper.selectByPrimaryKey(pcsId);
	}

}
