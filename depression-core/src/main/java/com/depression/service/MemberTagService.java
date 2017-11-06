package com.depression.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.MemberTagMapMapper;
import com.depression.dao.MemberTagMapper;
import com.depression.model.MemberTag;
import com.depression.model.MemberTagMap;

@Service
public class MemberTagService
{
	@Autowired
	MemberTagMapper memberTagMapper;
	@Autowired
	MemberTagMapMapper memberTagMapMapper;
	
	private static byte TAG_TYPE_PSYCHO = (byte)0;
	private static byte TAG_TYPE_USER = (byte)1;
	
	/**
	 * 标签分为心理医生和用户两种类型
	 * 在新建标签时，如果相同类型且描述相同的标签存在，直接取回
	 * @param phrase
	 * @return 标签id
	 */
	public Long insertNewTag4Psycho(String phrase)
	{
		MemberTag mt = new MemberTag();
		mt.setType(TAG_TYPE_PSYCHO);
		mt.setPhrase(phrase);
		List<MemberTag> mts = memberTagMapper.selectSelective(mt);
		if(mts.size()>0){
			return mts.get(0).getTagId();
		}else{
			memberTagMapper.insertSelective(mt);
			return mt.getTagId();
		}
	}
	
	public Integer updateTag(MemberTag memberTag)
	{
		return memberTagMapper.updateByPrimaryKeySelective(memberTag);
	}
	
	public Integer deleteTagBulk(List<Long> ids)
	{
		if(ids.size()>0)
		{
			return memberTagMapper.deleteByPrimaryKeyBulk(ids);
		}else{
			return 0;
		}
	}
	
	/**
	 * 根据短语获取心理医生标签
	 * @param phrase
	 * @return
	 */
	public MemberTag getTag4Psycho(String phrase)
	{
		MemberTag mt = new MemberTag();
		mt.setType(TAG_TYPE_PSYCHO);
		mt.setPhrase(phrase);
		List<MemberTag> mts = memberTagMapper.selectSelective(mt);
		if(mts.size()>0){
			return mts.get(0);
		}else
		{
			return null;
		}
	}
	
	public void updateTagRefCount(Long tagId)
	{
		MemberTag memberTag = memberTagMapper.selectByPrimaryKeyLock(tagId);
		if(memberTag != null){
			MemberTag m = new MemberTag();
			m.setTagId(tagId);
			m.setRefCount(memberTag.getRefCount()+1);
			memberTagMapper.updateByPrimaryKeySelective(m);
		}
	}
	
	/**
	 * 相同映射已经存在则返回原映射
	 * @param mid
	 * @param tagId
	 * @return 映射id
	 */
	public Long insertNewTagMap(Long mid, Long tagId)
	{
		MemberTagMap mtm = new MemberTagMap();
		mtm.setMid(mid);
		mtm.setTagId(tagId);
		
		//引用+1
		updateTagRefCount(tagId);
		
		List<MemberTagMap> mtms = memberTagMapMapper.selectSelective(mtm);
		if(mtms.size()>0){
			return mtms.get(0).getMapId();
		}else{
			memberTagMapMapper.insertSelective(mtm);
			return mtm.getMapId();
		}		
	}
	
	/**
	 * 给心理医生添加标签
	 * @param mid
	 * @param phrase
	 * @return
	 */
	public Long insertNewTagMap4Psycho(Long mid, String phrase)
	{
		return insertNewTagMap(mid, insertNewTag4Psycho(phrase));
	}
	
	/**
	 * 根据点击量排序, 获取咨询师的标签列表
	 * @param pageIndex
	 * @param pageSize
	 * @param isEnable null全部  0启用 1禁用
	 * @return
	 */
	public List<MemberTag> getTagList4Psycho(Integer pageIndex, Integer pageSize, Integer isEnable)
	{
		MemberTag mt = new MemberTag();
		mt.setPageIndex(pageIndex);
		mt.setPageSize(pageSize);
		mt.setType(TAG_TYPE_PSYCHO);
		mt.setIsEnable(isEnable);
		return memberTagMapper.selectByTypeOrderHitCount(mt);
	}
	
	/**
	 * 咨询师标签数量
	 * @param isEnable null全部  0启用 1禁用
	 * @return
	 */
	public Integer getTagCount4Psycho(Integer isEnable)
	{
		MemberTag mt = new MemberTag();
		mt.setType(TAG_TYPE_PSYCHO);
		return memberTagMapper.countByType(mt);
	}

	public List<MemberTag> getTagList(Long mid)
	{
		MemberTagMap mtm = new MemberTagMap();
		mtm.setMid(mid);
		
		List<MemberTagMap> mtms = memberTagMapMapper.selectSelective(mtm);
		List<Long> ids = new ArrayList<Long>();
		for(MemberTagMap m : mtms)
		{
			ids.add(m.getTagId());
		}
		if(ids.size()>0){
			return memberTagMapper.selectByPrimaryKeyBulk(ids);
		}else{
			return new ArrayList<MemberTag>();
		}
	}
	
	public void removeAllTagMap(Long mid)
	{
		MemberTagMap mtm = new MemberTagMap();
		mtm.setMid(mid);
		
		List<MemberTagMap> mtms = memberTagMapMapper.selectSelective(mtm);
		List<Long> ids = new ArrayList<Long>();
		for(MemberTagMap m : mtms)
		{
			ids.add(m.getMapId());
		}
		if(ids.size()>0){
			memberTagMapMapper.deleteByPrimaryKeyBulk(ids);
		}
	}
	
	public void updateTagHitCount(Long tagId)
	{
		MemberTag memberTag = memberTagMapper.selectByPrimaryKeyLock(tagId);
		if(memberTag != null){
			MemberTag m = new MemberTag();
			m.setTagId(tagId);
			m.setHitCount(memberTag.getHitCount()+1);
			memberTagMapper.updateByPrimaryKeySelective(m);
		}
	}
	
	public List<Long> getMemberIdsByTag(Long tagId)
	{
		//点击数+1
		updateTagHitCount(tagId);
		
		List<Long> ids = new ArrayList<Long>();
		MemberTagMap mtm = new MemberTagMap();
		mtm.setTagId(tagId);
		List<MemberTagMap> mtms = memberTagMapMapper.selectSelective(mtm);
		for(MemberTagMap m : mtms)
		{
			ids.add(m.getMid());
		}
		
		return ids;
	}
	
	public List<Long> getPsychoIdsByTag(String phrase)
	{
		List<Long> ids = new ArrayList<Long>();
		
		MemberTag mt = new MemberTag();
		mt.setType(TAG_TYPE_PSYCHO);
		mt.setPhrase(phrase);
		List<MemberTag> mts = memberTagMapper.selectSelective(mt);
		if(mts.size()>0){
			Long tagId = mts.get(0).getTagId();
			return getMemberIdsByTag(tagId);
		}
		
		return ids;
	}
	
	public MemberTag selectByPrimaryKey(Long tagId)
	{
		return memberTagMapper.selectByPrimaryKey(tagId);
	}
	
}
