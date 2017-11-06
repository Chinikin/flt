package com.depression.task;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.depression.model.ServiceCallRecord;
import com.depression.service.ServiceCallRecordService;
import com.depression.service.ServiceOrderService;
import com.depression.utils.AliyunIMUtil;

public class CallRecordTask {
	 @Autowired
	 ServiceOrderService serviceOrderService;
	 @Autowired
	 ServiceCallRecordService serviceCallRecordService;
	 public void excute() throws IOException { 
		    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
		    Date newDate =  new Date();
			try {		
			Calendar date = Calendar.getInstance();
			date.setTime(newDate);
			date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
			newDate = df.parse(df.format(date.getTime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    ServiceCallRecord serviceCallRecord = new ServiceCallRecord();		    
		    serviceCallRecord.setBeginTime(newDate);
		    List<ServiceCallRecord> list = serviceCallRecordService.selectByDate(serviceCallRecord);
		    //根据订单录音名称进行转存录音
		    for(ServiceCallRecord record : list){
		    	try {
		    		if(record.getRecordUrl()!=null&&record.getRecordUrl().contains("@")){
		    		String[] str = record.getRecordUrl().split("@");
					AliyunIMUtil.getRecordByDoubleCall(str[0], str[1]);
					System.out.println("=======================完成录音下载："+str[0]+"============================");
					}
				} catch (Exception e) {
					System.out.println("----------------录音文件转存异常："+record.getRecordUrl()+"--------------");
					e.printStackTrace();
				}
		    }
		  } 

}
