package com.depression.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.AdvisoryTagMapMapper;
import com.depression.dao.AdvisoryTagMapper;
import com.depression.model.AdvisoryTag;
import com.depression.model.AdvisoryTagMap;

@Service
public class AdvisoryTagService
{
	@Autowired
	AdvisoryTagMapper advisoryTagMapper;
	@Autowired
	AdvisoryTagMapMapper advisoryTagMapMapper;
	
	/**
	 * 添加标签
	 * @param phrase
	 * @return tagId
	 */
	public Long newAdvisoryTag(String phrase, Integer order)
	{
		AdvisoryTag advisoryTag = new AdvisoryTag();
		advisoryTag.setPhrase(phrase);
		List<AdvisoryTag> ats = advisoryTagMapper.selectSelective(advisoryTag);
		if(ats.size()>0)
		{
			return ats.get(0).getTagId();
		}
		advisoryTag.setOrderNo(order);
		advisoryTagMapper.insertSelective(advisoryTag);
		
		return advisoryTag.getTagId();
	}
	
	/**
	 * 修改标签
	 * @param tagId
	 * @param phrase
	 * @return 0已存在相同的标签
	 */
	public Integer updateAdvisoryTag(Long tagId, String phrase, Integer order)
	{
		AdvisoryTag advisoryTag = new AdvisoryTag();
		advisoryTag.setPhrase(phrase);
		List<AdvisoryTag> ats = advisoryTagMapper.selectSelective(advisoryTag);
		if(ats.size()>0 && !ats.get(0).getTagId().equals(tagId))
		{
			return 0;
		}
		
		advisoryTag.setTagId(tagId);
		advisoryTag.setOrderNo(order);
		return advisoryTagMapper.updateByPrimaryKeySelective(advisoryTag);
	}
	
	/**
	 * 启用禁用标签
	 * @param ids
	 * @param isEnable
	 * @return
	 */
	public Integer enable(List<Long> ids, Integer isEnable)
	{
		return advisoryTagMapper.enableByPrimaryKeyBulk(ids, isEnable);
	}
	
	/**
	 * 分页获取标签
	 * @param pageIndex
	 * @param pageSize
	 * @param isEnable 0启用 1禁用 null全部
	 * @return
	 */
	public List<AdvisoryTag> selectWithPageOrder(Integer pageIndex, Integer pageSize, Byte isEnable)
	{
		AdvisoryTag advisoryTag = new AdvisoryTag();
		advisoryTag.setPageIndex(pageIndex);
		advisoryTag.setPageSize(pageSize);
		
		return advisoryTagMapper.selectSelectiveWithPageOrder(advisoryTag);
	}
	
	public Integer countSelect(Byte isEnable)
	{
		AdvisoryTag advisoryTag = new AdvisoryTag();
		return advisoryTagMapper.countSelective(advisoryTag);
	}
	
	/**
	 * 获取
	 * @param tagId
	 * @return
	 */
	public AdvisoryTag selectById(Long tagId)
	{
		return advisoryTagMapper.selectByPrimaryKey(tagId);
	}
	
	public void transAdvisoryRef(Long tagId)
	{
		AdvisoryTag advisoryTag =  advisoryTagMapper.selectByPrimaryKeyLock(tagId);
		advisoryTag.setRefCount(advisoryTag.getRefCount()+1);
		advisoryTagMapper.updateByPrimaryKeySelective(advisoryTag);
	}
	
	public void transAdvisoryHit(Long tagId)
	{
		AdvisoryTag advisoryTag =  advisoryTagMapper.selectByPrimaryKeyLock(tagId);
		advisoryTag.setHitCount(advisoryTag.getHitCount()+1);
		advisoryTagMapper.updateByPrimaryKeySelective(advisoryTag);
	}
	
	public void newAdvisoryTagMap(Long advisoryId, Long tagId)
	{
		AdvisoryTagMap map = new AdvisoryTagMap();
		map.setAdvisoryId(advisoryId);
		map.setTagId(tagId);
		advisoryTagMapMapper.insertSelective(map);
		
		transAdvisoryRef(tagId);
	}
	
	public void newAdvisoryTagMaps(Long advisoryId, List<Long> ids)
	{
		for(Long id : ids)
		{
			AdvisoryTagMap map = new AdvisoryTagMap();
			map.setAdvisoryId(advisoryId);
			map.setTagId(id);
			advisoryTagMapMapper.insertSelective(map);
			
			transAdvisoryRef(id);
		}
	}
	
	/**
	 * 获取咨询的标签
	 * @param advisoryId
	 * @return
	 */
	public List<AdvisoryTag> selectAdvisoryTagByAdvisoryId(Long advisoryId)
	{
		AdvisoryTagMap map = new AdvisoryTagMap();
		map.setAdvisoryId(advisoryId);
		List<AdvisoryTagMap> maps = advisoryTagMapMapper.selectSelective(map);
		List<AdvisoryTag> advisoryTags = new ArrayList<AdvisoryTag>();
		for(AdvisoryTagMap m : maps)
		{
			advisoryTags.add(advisoryTagMapper.selectByPrimaryKey(m.getTagId()));
		}
		return advisoryTags;
	}
	
	public AdvisoryTag selectByAdvisoryId(Long advisoryId)
	{
		AdvisoryTagMap map = new AdvisoryTagMap();
		map.setAdvisoryId(advisoryId);
		List<AdvisoryTagMap> maps = advisoryTagMapMapper.selectSelective(map);
		if(maps.size() > 0)
		{
			return advisoryTagMapper.selectByPrimaryKey(maps.get(0).getTagId());
		}else
		{
			return null;
		}
	}
	
	/**
	 * 获取所有咨询标签列表
	 * 
	 * @return
	 */
	public List<AdvisoryTag> selectAllAdvisoryTagList()
	{
		AdvisoryTag advisoryTag = new AdvisoryTag();
		advisoryTag.setIsEnable(0);
		List<AdvisoryTag> list = advisoryTagMapper.selectSelective(advisoryTag);
		return list;
	}
}
