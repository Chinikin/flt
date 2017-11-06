package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.UbSearchWordsMapper;
import com.depression.model.Member;
import com.depression.model.Page;
import com.depression.model.UbSearchWords;

/**
 * 
 * @author ax
 *
 */
@Service
public class UbSearchWordsService
{
	@Autowired
	UbSearchWordsMapper ubSearchWordsMapper;
	@Autowired
	MemberService memberService;
	
	public void insertUbSearchWords(Long mid,String words){
		UbSearchWords sw=new UbSearchWords();
		sw.setCreateTime(new Date());
		sw.setMid(mid);
		sw.setWords(words);
		sw.setIsDelete((byte)0);
		ubSearchWordsMapper.insert(sw);
	}

	
	/**
	 * 通过已注册的用户获取搜索记录列表
	 * @param ids 已注册用户id
	 * @param pageIndex  
	 * @param pageSize
	 * @return
	 */
	
	public List<UbSearchWords> getUbSearchWordsInEap(Long eeId,Long mid,
			Integer pageIndex, Integer pageSize) {
		Page page=new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		return ubSearchWordsMapper.selectUbSearchWordsInEap(eeId,mid, page.getPageStartNum(), pageSize);
	}


	public Integer countUbSearchWordsInEap(Long eeId,Long mid) {
		return ubSearchWordsMapper.countUbSearchWordsInEap(eeId,mid);
	}
	
	public Integer countUbSearchWordsByMid(Long mid){
		UbSearchWords u=new UbSearchWords();
		u.setMid(mid);
		return ubSearchWordsMapper.countSelective(u);
	}
	

}
