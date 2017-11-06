package com.depression.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.MemberMapper;
import com.depression.dao.PsychoRecommendMapper;
import com.depression.dao.RecommendMapper;
import com.depression.dao.ServiceOrderMapper;
import com.depression.model.Member;
import com.depression.model.PsychoRecommend;
import com.depression.model.Recommend;
import com.depression.model.ServiceOrder;

@Service
public class ElectExpertService {

	@Autowired
	PsychoRecommendMapper psychoRecommendMapper;
	@Autowired
	MemberMapper memberMapper;
	@Autowired
	RecommendMapper recommendMapper;
	@Autowired
	ServiceOrderMapper serviceOrderMapper;
	public List<Member> getElectExpertUser(PsychoRecommend psychoRecommend) {
		
		List<Member> members = new ArrayList<Member>();
		List<PsychoRecommend> dto = psychoRecommendMapper.selectSelectiveWithPage(psychoRecommend);
		if (dto.size() > 0) {
		  for(PsychoRecommend pr:dto){
			  Member record = new Member();
			  record.setMid(pr.getMid());
			 Member member = memberMapper.selectByMidOrMobile(record);
			 record.setNickname(member.getNickname());
			 record.setAvatarThumbnail(member.getAvatarThumbnail());
			 members.add(record);
		  }
		} 
		return members;
	}
	
	public Recommend getRecommend(){
		List<Recommend> recommend = recommendMapper.selectSelective(null);
		if(recommend.size()>0){
			return recommend.get(0);
		}
		return null;
		
	}
	
	public List<Long> getAllMid(){
		return psychoRecommendMapper.selectByIds();
	}
	
	public PsychoRecommend getNum(PsychoRecommend psychoRecommend){
		List<PsychoRecommend> list = psychoRecommendMapper.selectSelective(psychoRecommend);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public void transChangeNum(Long mid)
    {
		PsychoRecommend psychoRecommend = psychoRecommendMapper.selectByMidKeyLock(mid);
		int num = psychoRecommend.getRemainNumber();
		if(num>=1){
			psychoRecommend.setRemainNumber(num-1);
		psychoRecommendMapper.updateByMidKeySelective(psychoRecommend);
		}else{
			psychoRecommend.setRemainNumber(num);
			psychoRecommendMapper.updateByMidKeySelective(psychoRecommend);	
		}     	
    }
	
	public boolean IsOutnumber(Long mid){
		boolean flag = false;
	    ServiceOrder queryServiceOrder = new ServiceOrder();
		queryServiceOrder.setMid(mid);
		queryServiceOrder.setIsRecommend((byte) 1);
		List<ServiceOrder> list = serviceOrderMapper.selectSelective(queryServiceOrder);
		//查询允许最大数
		List<Recommend> recommend = recommendMapper.selectSelective(null);
		
		if(recommend.get(0).getMemberLimit()>list.size()){
		    	flag = true ;
		}			
		return flag ;
	}
}
