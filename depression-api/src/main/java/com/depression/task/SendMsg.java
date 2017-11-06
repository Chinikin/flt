package com.depression.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.depression.entity.MembersOnlineStatus;
import com.depression.model.ServiceCallRecord;
import com.depression.service.MemberService;
import com.depression.service.ServiceCallRecordService;
import com.depression.utils.AliyunIMUtil;
import com.depression.utils.SmsUtil;

public class SendMsg {

	private static Logger log = Logger.getLogger(SendMsg.class);

	@Autowired
	ServiceCallRecordService serviceCallRecordService;
	@Autowired
	MemberService memberService;
	public void excute() {
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
	    Date newDate =  new Date();
		try {		
		Calendar datees = Calendar.getInstance();
		datees.setTime(newDate);
		datees.set(Calendar.DATE, datees.get(Calendar.HOUR) - 1);
		newDate = df.parse(df.format(datees.getTime()));
		
		//查询当前时间前60分钟内的通话记录，并且正在通话中
		ServiceCallRecord serviceCall  = new ServiceCallRecord();
		serviceCall.setIsCalling((byte) 1);
	    serviceCall.setBeginTime(newDate);

	    List<ServiceCallRecord> list = serviceCallRecordService.selectByDate(serviceCall);
	    
	    //根据订单录音名称进行转存录音
	    for(ServiceCallRecord record : list){
	    	//比较当前时间和允许时间大小，剩余五分钟进行发送短信提醒
	    	Date date = new Date();
	    	long nowTime = date.getTime();
	    	long lowTime = record.getBeginTime().getTime()-5*60*1000;
	    	 
	    	if((nowTime>lowTime)&&(record.getIsCalling()==1)){
	    		//发送短信并更新记录为超时状态
	    		String MaxMinutes = record.getOrderId().split("@")[1];
	    		SmsUtil.sendSms(record.getCalled(), "181436", MaxMinutes);
	    		SmsUtil.sendSms(record.getCaller(), "181436", MaxMinutes);
	    		record.setIsCalling((byte) 2);
	    		serviceCallRecordService.updateByPrimaryKey(record);
	    	}	    		    				
	    }	
	  //查询当前时间前60分钟内的通话记录，并且正在通话中
	  		ServiceCallRecord serviceCalls  = new ServiceCallRecord();
	  		serviceCalls.setIsCalling((byte) 2);
	  	    serviceCalls.setBeginTime(newDate);

	  	    List<ServiceCallRecord> lists = serviceCallRecordService.selectByDate(serviceCalls);
	  	  for(ServiceCallRecord record : lists){		    	
		    	Date dates = new Date();
		    	long nowTimes = dates.getTime();
		    	long lowTimes = record.getBeginTime().getTime();
		    	if((record.getIsCalling()==2)&&(nowTimes>lowTimes)){
		    		//发送短信并更新记录为超时状态
		    		String MaxMinutes = record.getOrderId().split("@")[1];
		    		SmsUtil.sendSms(record.getCalled(), "182210", MaxMinutes);
		    		SmsUtil.sendSms(record.getCaller(), "182210", MaxMinutes);
		    		record.setIsCalling((byte) 3);
		    		serviceCallRecordService.updateByPrimaryKey(record);
		    	}
		    	//最终状态的回执
		    	long lastTime = record.getBeginTime().getTime()+10*60*1000;
		    	if((record.getIsCalling()==2)&&(nowTimes>lastTime)){
					memberService.transCasStatus(record.getServiceOrderId(),MembersOnlineStatus.STATUS_IN_THE_CALL.getCode(),MembersOnlineStatus.STATUS_ONLINE.getCode());
		    	}		    	
		    }	
		} catch (Exception e) {
			log.info("发送短信异常...........");
			e.printStackTrace();
		}
	  } 
}
