package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.MemberStepCounterDAO;
import com.depression.model.MemberStepCounter;

/**
 * @author:ziye_huang
 * @date:2016年5月10日
 */

@Service
public class MemberStepCounterService
{

	@Autowired
	MemberStepCounterDAO memberStepCounterDAO;

	public Integer insert(MemberStepCounter memberStepCounter)
	{
		memberStepCounter.setIsDelete(0);
		memberStepCounter.setStartTime(new Date());
		memberStepCounter.setEndTime(new Date());
		return memberStepCounterDAO.insert(memberStepCounter);
	}

	public Integer delete(MemberStepCounter memberStepCounter)
	{
		memberStepCounter.setIsDelete(1);
		return memberStepCounterDAO.delete(memberStepCounter);
	}

	public Integer update(MemberStepCounter memberStepCounter)
	{
		memberStepCounter.setEndTime(new Date());
		return memberStepCounterDAO.update(memberStepCounter);
	}

	public List<MemberStepCounter> getMemberStepByParams(MemberStepCounter memberStepCounter)
	{
		if (null == memberStepCounter.getStartTime())
		{
			memberStepCounter.setStartTime(new Date());
		}
		if (null == memberStepCounter.getEndTime())
		{
			memberStepCounter.setEndTime(new Date());
		}
		return memberStepCounterDAO.getMemberStepByParams(memberStepCounter);
	}

}
