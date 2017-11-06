package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.PsychoGroupMapper;
import com.depression.dao.PsychoGroupMemberMapper;
import com.depression.model.PsychoGroup;
import com.depression.model.PsychoGroupMember;

@Service
public class PsychoGroupService
{
	@Autowired
    PsychoGroupMapper psychoGroupMapper;
	@Autowired
	PsychoGroupMemberMapper psychoGroupMemberMapper;
	
	/**
	 * 获取所有咨询师分组
	 * @return
	 */
	public List<PsychoGroup> obtainPsychoGroupList()
	{
		PsychoGroup psychoGroup = new PsychoGroup();
		
		return psychoGroupMapper.selectSelective(psychoGroup);
	}
	
	/**
	 * 添加咨询师组
	 * @param psychoGroup
	 * @return
	 */
	public Integer addPsychoGroup(PsychoGroup psychoGroup)
	{
		psychoGroup.setCreateTime(new Date());
		return psychoGroupMapper.insertSelective(psychoGroup);
	}
	
	/**
	 * 删除咨询师组
	 * @param pgId
	 * @return
	 */
	public Integer removePsychoGroup(Long pgId)
	{
		return psychoGroupMapper.deleteByPrimaryKey(pgId);
	}
	
	/**
	 * 根据主键获取咨询师分组
	 * @param pgId 分组id
	 * @return
	 */
	public PsychoGroup obtainPsychoGroup(Long pgId)
	{
		return psychoGroupMapper.selectByPrimaryKey(pgId);
	}
	
	/**
	 * 添加咨询师到分组, 插入前先检查是否已经添加过
	 * @param pgId 分组id
	 * @param mid 咨询师id
	 * @return
	 */
	public Integer addMemberToGroup(Long pgId, Long mid)
	{
		PsychoGroupMember pgm =  new PsychoGroupMember();
		pgm.setPgId(pgId);
		pgm.setMid(mid);
		
		List<PsychoGroupMember> pgms = psychoGroupMemberMapper.selectSelective(pgm);
		if(pgms.size() > 0)
		{//组内已有该成员
			return 0;
		}
		
		pgm.setCreateTime(new Date());
		return psychoGroupMemberMapper.insertSelective(pgm);
	}
	
	/**
	 * 分页获取分组内咨询师
	 * @param pgId 分组id
	 * @param pageIndex 页码
	 * @param pageSize 页大小
	 * @return
	 */
	public List<PsychoGroupMember> getPsychoGroupMemberList(Long pgId, Integer pageIndex, Integer pageSize)
	{
		PsychoGroupMember pgm = new PsychoGroupMember();
		pgm.setPgId(pgId);
		pgm.setPageIndex(pageIndex);
		pgm.setPageSize(pageSize);
		
		return psychoGroupMemberMapper.selectSelectiveWithPageOrderBy(pgm);
	}
	
	/**
	 * 获取组内所有咨询师
	 * @param pgId
	 * @return
	 */
	public List<PsychoGroupMember> getPsychoGroupMemberList(Long pgId)
	{
		PsychoGroupMember pgm = new PsychoGroupMember();
		pgm.setPgId(pgId);
		
		return psychoGroupMemberMapper.selectSelective(pgm);
	}	
	
	/**
	 * 获取分组内咨询师id列表
	 * @param pgId 分组id
	 * @return
	 */
	public List<Long> getPsychoIdOfGroup(Long pgId)
	{
		return psychoGroupMemberMapper.selectPsychoIdOfGroup(pgId);
	}
	
	/**
	 * 获取分组内咨询师总数
	 * @param pgId 分组id
	 * @return
	 */
	public Integer countPsychoGroupMember(Long pgId)
	{
		PsychoGroupMember pgm = new PsychoGroupMember();
		pgm.setPgId(pgId);
		return psychoGroupMemberMapper.countSelective(pgm);
	}
	
	/**
	 * 删除分组内的咨询师
	 * @param ids 组内成员id
	 * @return
	 */
	public Integer removeMembersFromGroup(List<Long> ids)
	{
		return psychoGroupMemberMapper.deleteByPrimaryKeyBulk(ids);
	}
	
	public Integer removeMembersFromGroup(Long pgId, Long mid)
	{
		PsychoGroupMember pgm = new PsychoGroupMember();
		pgm.setPgId(pgId);
		pgm.setMid(mid);
		
		List<PsychoGroupMember> pgms = psychoGroupMemberMapper.selectSelective(pgm);
		for(PsychoGroupMember p : pgms)
		{
			psychoGroupMemberMapper.deleteByPrimaryKey(p.getPgmId());
		}
		
		return pgms.size();
	}
	
	/**
	 * 咨询师是否在组内
	 * @param pgId
	 * @param mid
	 * @return 0 不在组内  1在组内
	 */
	public Integer isPsychoInGroup(Long pgId, Long mid)
	{
		PsychoGroupMember pgm = new PsychoGroupMember();
		pgm.setPgId(pgId);
		pgm.setMid(mid);
		
		List<PsychoGroupMember> pgms = psychoGroupMemberMapper.selectSelective(pgm);
		
		return pgms.size()==0?0:1;
	}
	
	/**
	 * 启用/禁用组内咨询师
	 * @param ids 组内成员id
	 * @param isEnable 0启用 1禁用
	 * @return
	 */
	public Integer enablePsychoGroupMembers(List<Long> ids, byte isEnable)
	{
		return psychoGroupMemberMapper.enableByPrimaryKeyBulk(ids, isEnable);
	}
}
