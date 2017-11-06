package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.UbViewHomepageMapper;
import com.depression.model.UbViewHomepage;

/**
 * 
 * @author ax
 *
 */
@Service
public class UbViewHomepageService
{
	@Autowired
	UbViewHomepageMapper ubViewHomepageMapper;
	
	public void addUbViewHomepage(UbViewHomepage ubViewHomepage){
		ubViewHomepage.setViewTime(new Date());
		ubViewHomepage.setIsDelete((byte) 0);
		ubViewHomepage.setCreateTime(new Date());
		ubViewHomepageMapper.insert(ubViewHomepage);
	}
	
	/**
	 * 获取浏览用户最新的数据
	 * @param UbViewHomepage
	 * @return
	 */
	public List<UbViewHomepage> getLatestUbViewHomepage(UbViewHomepage ubViewHomepage){
		List<UbViewHomepage> list=ubViewHomepageMapper.getLatestUbViewHomepage(ubViewHomepage);
			return list;
	}

}
