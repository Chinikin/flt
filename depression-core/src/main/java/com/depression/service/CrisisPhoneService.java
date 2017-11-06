package com.depression.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.CrisisPhoneMapper;
import com.depression.model.CrisisPhone;

@Service
public class CrisisPhoneService
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	CrisisPhoneMapper crisisPhoneMapper;
	
	private Integer initCrisisPhone()
	{
		CrisisPhone crisisPhone = new CrisisPhone();
		crisisPhone.setPhoneNum("010-82951332");
		
		return crisisPhoneMapper.insertSelective(crisisPhone);
	}
	
	/**
	 * 获取干预电话
	 * @return
	 */
	public CrisisPhone obtainCrisisPhone()
	{
		CrisisPhone cp = new CrisisPhone();
		
		List<CrisisPhone> cps = crisisPhoneMapper.selectSelective(cp);
		if(cps.size() == 0)
		{
			initCrisisPhone();
			cps = crisisPhoneMapper.selectSelective(cp);
		}
		
		return cps.get(0);
	}
	
}
