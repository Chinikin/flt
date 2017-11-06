package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.depression.dao.EapArticleBlockMapper;
import com.depression.model.EapArticleBlock;
import com.depression.model.UbSearchWords;

/**
 * 
 * @author ax
 *
 */
@Service
public class EapArticleBlockService
{
	@Autowired
	EapArticleBlockMapper eapArticleBlockMapper;
	
	
	public int insertEapArticleBlockByArticleIds(Long eeId,String articleIds,Byte isEnable){
		List<Long> ids = JSON.parseArray(articleIds,Long.class);
		int count =0;
		for(Long articleId : ids){
			EapArticleBlock eab = new EapArticleBlock();
			eab.setArticleId(articleId);
			eab.setEeId(eeId);
			//获取单个禁用信息
			List<EapArticleBlock> eabs=eapArticleBlockMapper.selectSelective(eab);
			eab.setIsEnable(isEnable);
			if(eabs.size() > 0 ){
				eab.setEabId(eabs.get(0).getEabId());
				count+= eapArticleBlockMapper.updateByPrimaryKey(eab);
			}else{
				//没有该禁用信息
				if(isEnable == 1){
					count+=eapArticleBlockMapper.insertSelective(eab);
				}
			}
		}
		return count;
	}


	public EapArticleBlock getArticleBlockByArticleIdAndEeId(Long articleId,Long eeId) {
		EapArticleBlock eab = new EapArticleBlock();
		eab.setArticleId(articleId);
		eab.setEeId(eeId);
		List<EapArticleBlock>  list = eapArticleBlockMapper.selectSelective(eab);
		if(list.size() > 0 ){
			return list.get(0);
		}else{
			return null;
		}
	}

	
	

}
