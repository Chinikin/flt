package com.depression.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.dao.PunishmentDisableMessageMapper;
import com.depression.model.PunishmentDisableMessage;

@Service
public class PunishmentService
{
	@Autowired
	PunishmentDisableMessageMapper disableMessageMapper;
	
	/**
	 * 增加对用户的禁言天数，如当前处于处罚期中，则延长处罚天数
	 * @param mid	用户id
	 * @param days	禁言天数
	 * @return
	 */
	public Integer addDisableMessageDays(Long mid, Integer days)
	{
		PunishmentDisableMessage pdm = new PunishmentDisableMessage();
		pdm.setMid(mid);
		List<PunishmentDisableMessage> pdms = disableMessageMapper.selectSelective(pdm);
		
		if(pdms.size() == 0)
		{//无禁言记录
			PunishmentDisableMessage disableMessage = new PunishmentDisableMessage();
			disableMessage.setMid(mid);
			disableMessage.setTimeBegin(new Date());
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, days);
			disableMessage.setTimeEnd(cal.getTime());
			
			disableMessage.setCreateTime(new Date());
			return disableMessageMapper.insertSelective(disableMessage);
		}else{
			PunishmentDisableMessage disableMessage = pdms.get(0);
			if(disableMessage.getTimeEnd().before(new Date()))
			{//禁言记录过期，等同无记录
				disableMessage.setTimeBegin(new Date());
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, days);
				disableMessage.setTimeEnd(cal.getTime());
				
				disableMessage.setUpdateTime(new Date());
				return disableMessageMapper.updateByPrimaryKey(disableMessage);
			}else
			{//处于处罚期内，增加处罚天数
				Calendar cal = Calendar.getInstance();
				cal.setTime(disableMessage.getTimeEnd());
				cal.add(Calendar.DAY_OF_MONTH, days);
				disableMessage.setTimeEnd(cal.getTime());
				
				disableMessage.setUpdateTime(new Date());
				return disableMessageMapper.updateByPrimaryKey(disableMessage);
			}
		}
	}
	
	/**
	 * 清楚禁言处罚天数
	 * @param mid 会员id
	 * @return
	 */
	public Integer clearDisableMessageDays(Long mid)
	{
		PunishmentDisableMessage pdm = new PunishmentDisableMessage();
		pdm.setMid(mid);
		List<PunishmentDisableMessage> pdms = disableMessageMapper.selectSelective(pdm);
		if(pdms.size() > 0)
		{
			PunishmentDisableMessage disableMessage = pdms.get(0);
			if(disableMessage.getTimeEnd().after(new Date()))
			{
				disableMessage.setTimeEnd(new Date());
				disableMessage.setUpdateTime(new Date());
				return disableMessageMapper.updateByPrimaryKey(disableMessage);
			}
		}
		
		return 0;
	}
	
	/**
	 * 获取剩余的禁言天数
	 * @param mid 会员id
	 * @return
	 */
	public Integer obtainDisableMessageDays(Long mid)
	{
		PunishmentDisableMessage pdm = new PunishmentDisableMessage();
		pdm.setMid(mid);
		List<PunishmentDisableMessage> pdms = disableMessageMapper.selectSelective(pdm);
		
		if(pdms.size() == 0)
		{//无禁言记录
			return 0;
		}else{
			PunishmentDisableMessage disableMessage = pdms.get(0);
			if(disableMessage.getTimeEnd().before(new Date()))
			{//禁言记录过期，等同无记录
				return 0;
			}else
			{//处于处罚期内，计算剩余天数
				Date now = new Date();
				Long ms = (disableMessage.getTimeEnd().getTime() - now.getTime());
				Long days = ms/(24*60*60*1000);
				days = ms%(24*60*60*1000)==0 ? days : days+1;//余数向上取整
				
				return days.intValue();
			}
		}
	}
}
