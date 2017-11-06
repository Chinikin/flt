package com.depression.dao;

import java.util.Date;
import java.util.List;

import com.depression.model.MemberStepCounter;

/**
 * @author:ziye_huang
 * @date:2016年5月10日
 */

public interface MemberStepCounterDAO
{

	public Integer insert(MemberStepCounter memberStepCounter);

	public Integer delete(MemberStepCounter memberStepCounter);

	public Integer update(MemberStepCounter memberStepCounter);

	public List<MemberStepCounter> getMemberStepByParams(MemberStepCounter memberStepCounter);

}
