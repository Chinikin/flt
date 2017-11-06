package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.PsychoRecommendMapper;
import com.depression.model.Member;
import com.depression.model.PsychoRecommend;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 推荐
 * 
 * @author heshuai
 * 
 */
@Service
public class PsychoRecommendService
{
	@Autowired
	PsychoRecommendMapper psychoRecommendMapper;
	@Autowired
	MemberService memberService;
	
	/**
	 * 邀请专家到推荐表中
	 * 
	 * @param article
	 * @return
	 */
	public int addPsycho2Recommend(List<Long> pids){
		int count = 0;
		for(Long pid:pids){
			PsychoRecommend pr = new PsychoRecommend();
			//查看当前用户是否被禁用
			Member m = memberService.selectMemberByMid(pid);
			if(m != null && m.getIsEnable() == 0){
				pr.setMid(pid);
				List<PsychoRecommend> exitPr = psychoRecommendMapper.selectSelective(pr);
				//判断该专家是否已经添加过
				if(exitPr.size() == 0){
					pr.setCreateTime(new Date());
					pr.setIsEnable((byte)0);
					pr.setIsDelete((byte)0);
					count+=psychoRecommendMapper.insertSelective(pr);
				}
			}
		}
		return count;
	}
	
	
	public int updatePsychoRecommend(PsychoRecommend pr){
		return psychoRecommendMapper.updateByPrimaryKeySelective(pr);
	}
	
	
	/**
	 * 从推荐表移除专家
	 * 
	 * @param prId 主键
	 * @return
	 */
	public int removePsychoRecommend(Long prId){
		return psychoRecommendMapper.deleteByPrimaryKey(prId);
	}
	
	/**
	 * 从推荐表查詢专家
	 * 
	 * @param prId 主键
	 * @return
	 */
	public PsychoRecommend selectPsychoRecommendByPrimaryKey(Long prId){
		return psychoRecommendMapper.selectByPrimaryKey(prId);
	}
	
	
	
	/**
	 * 推荐专家列表(含分页)
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public  Page<PsychoRecommend> selectRecommendPsychos(PsychoRecommend pr){
			PageHelper.startPage(pr.getPageIndex(), pr.getPageSize());
			return psychoRecommendMapper.selectSelectiveV1(pr);
		
	}
	
	/**
	 * 统计专家列表(含分页)
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public  int countRecommendPsychos(PsychoRecommend pr){
			return psychoRecommendMapper.countSelectiveV1(pr);
	}
	
	/**
	 * 推荐专家列表
	 * 
	 */
	public  List<PsychoRecommend> selectSelective(PsychoRecommend pr){
		return  psychoRecommendMapper.selectSelective(pr);
	}

}
