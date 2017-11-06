package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.RecommendMapper;
import com.depression.model.PsychoRecommend;
import com.depression.model.Recommend;

/**
 * 推荐
 * 
 * @author heshuai
 * 
 */
@Service
public class RecommendService
{
	@Autowired
	RecommendMapper recommendMapper;
	
	/**
	 * 推荐表属性
	 * 
	 * @param article
	 * @return
	 */
	public int updateRecommend(Recommend recommend){
		
		//暂时写死
		recommend.setrId(1L);
		return recommendMapper.updateByPrimaryKeySelective(recommend);
	}
	
	/**
	 * 获取当前有效的推荐
	 * @return
	 */
	public Recommend getRecommend(){
		Recommend r =new Recommend();
		List<Recommend> list = recommendMapper.selectSelective(r);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	
	public int deleteRecommendByPrimaryKey(Long rId){
		return recommendMapper.deleteByPrimaryKey(rId);
	}
	
	public int insertRecommend(Recommend recommend){
		return recommendMapper.insertSelective(recommend);
	}
	
	
}
