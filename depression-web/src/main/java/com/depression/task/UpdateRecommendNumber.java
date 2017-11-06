package com.depression.task;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.depression.model.PsychoRecommend;
import com.depression.service.PsychoRecommendService;

public class UpdateRecommendNumber {

	@Autowired
	PsychoRecommendService psychoRecommendService;
	public void excute() {
		
		Date date = new Date();
		PsychoRecommend pr = new PsychoRecommend();
		try {
			List<PsychoRecommend> list = psychoRecommendService.selectRecommendPsychos(pr);
			//更新剩余库存
			for(PsychoRecommend p:list){
				Integer number =p.getDailyNumber();
				if(number != null && number != 0 ){
					p.setRemainNumber(number);
					p.setUpdateTime(date);
					psychoRecommendService.updatePsychoRecommend(p);
				}
			}
		} catch (Exception e) {
		}
		
		
	  } 
}
