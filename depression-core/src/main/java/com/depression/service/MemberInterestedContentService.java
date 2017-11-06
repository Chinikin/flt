package com.depression.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.MemberInterestedContentMapper;
import com.depression.dao.PsychologyContentCategoryMapper;
import com.depression.model.MemberInterestedContent;
import com.depression.model.PsychologyContentCategory;

@Service
public class MemberInterestedContentService
{
	@Autowired
	PsychologyContentCategoryMapper pcCategoryMapper;
	@Autowired
	MemberInterestedContentMapper miContentMapper;
	
	/**
	 * 获取所有心理内容分类，按sortOrder排序
	 * @return
	 */
	public List<PsychologyContentCategory> obtainAllPsyConCategory()
	{
		return pcCategoryMapper.selectAllSortByOrder();
	}
	
	/**
	 * 保存用户感兴趣的内容, 如与历史记录一致直接返回
	 * @param mid
	 * @param pccIds
	 * @return
	 */
	public Integer storeMiContents(Long mid, List<Long> pccIds)
	{
		Integer ret = 0;
		
		//查询旧的感兴趣内容
		MemberInterestedContent mic = new MemberInterestedContent();
		mic.setMid(mid);
		List<MemberInterestedContent> oldMics = miContentMapper.selectSelective(mic);
		if(oldMics.size() > 0)
		{//存在就的记录
			List<Long> oldPccIds = new ArrayList<Long>();
			List<Long> oldmicIds = new ArrayList<Long>();
			for(MemberInterestedContent oldm : oldMics)
			{
				oldPccIds.add(oldm.getPccId());
				oldmicIds.add(oldm.getMicId());
			}
			
			if(pccIds.size()==oldPccIds.size() && !oldPccIds.retainAll(pccIds))
			{//没有变化,直接返回
				return 0;
			}
			
			//删除历史记录
			miContentMapper.deleteByPrimaryKeyBulk(oldmicIds);
		}
		
		for(Long pccId : pccIds)
		{
			MemberInterestedContent miContent = new MemberInterestedContent();
			miContent.setMid(mid);
			miContent.setPccId(pccId);
			miContent.setCreateTime(new Date());
			
			ret += miContentMapper.insertSelective(miContent);
		}
		
		return ret;
	}
	
	/**
	 * 获取用户感兴趣的内容列表
	 * @param mid
	 * @return
	 */
	public List<PsychologyContentCategory> obtainMiContents(Long mid)
	{
		MemberInterestedContent mic = new MemberInterestedContent();
		mic.setMid(mid);
		List<MemberInterestedContent> mics = miContentMapper.selectSelective(mic);
		
		if(mics.size() == 0)
		{
			return new ArrayList<PsychologyContentCategory>();
		}
		
		List<Long> pccIds = new ArrayList<Long>();
		for(MemberInterestedContent m : mics)
		{
			pccIds.add(m.getPccId());
		}
		
		return pcCategoryMapper.selectByPrimaryKeyBulk(pccIds);
	}
}
