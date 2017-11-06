package com.depression.controller.api;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ResultEntity;
import com.depression.model.MemberMoodIndex;
import com.depression.model.MemberMoodRecord;
import com.depression.model.MemberSleepQuality;
import com.depression.service.MemberMoodIndexService;
import com.depression.service.MemberMoodRecordService;
import com.depression.service.MemberSleepQualityService;

/**
 * @author:ziye_huang
 * @date:2016年5月11日
 */
@Controller
@RequestMapping("/MemberMoodRecord")
public class MemberMoodRecordController
{
	@Autowired
	MemberMoodRecordService memberMoodRecordService;
	
	@Autowired
	MemberMoodIndexService memberMoodIndexService;
	
	@Autowired
	MemberSleepQualityService memberSleepQualityService;
	
	 @InitBinder
	  protected void initBinder(HttpServletRequest request,
	            ServletRequestDataBinder binder) throws Exception 
	 { 
       DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
       binder.registerCustomEditor(Date.class, dateEditor);
	 }

	 
	/**
	 * 获取时间段内的天数，左右封闭区间
	 * 
	 * @param begin
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public Integer daysBetween(Date begin, Date end) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		begin = sdf.parse(sdf.format(begin));
		end = sdf.parse(sdf.format(end));
		Calendar cal = Calendar.getInstance();
		cal.setTime(begin);
		long time1 = cal.getTimeInMillis();
		cal.setTime(end);
		long time2 = cal.getTimeInMillis();
		Long between_days = (time2 - time1) / (1000 * 3600 * 24) + 1; // 左右封闭区间

		return between_days.intValue();
	}
	 
	@RequestMapping(method = RequestMethod.POST, value = "/addMemberMoodRecord.json")
	@ResponseBody
	public Object addMemberMoodRecord(HttpSession session, HttpServletRequest request, Long mid, String moodRecord, Integer moodIndex,
			Date recordDate)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (null == mid || moodRecord == null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}
			
			if(recordDate == null)
			{
				recordDate = new Date();
			}

			MemberMoodRecord memberMoodRecord = new MemberMoodRecord();
			memberMoodRecord.setMid(mid);
			memberMoodRecord.setMoodRecord(moodRecord);
			memberMoodRecord.setRecordDate(recordDate);
			
			Integer ret = memberMoodRecordService.insert(memberMoodRecord);
			if (1 != ret)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("添加情绪记录失败");
				return result;
			}
			
			if(moodIndex != null)
			{
				MemberMoodIndex memberMoodIndex = new MemberMoodIndex();
				memberMoodIndex.setMid(mid);
				memberMoodIndex.setMoodIndex(moodIndex);
				memberMoodIndex.setRecordDate(recordDate);
				ret = memberMoodIndexService.insert(memberMoodIndex);
				if (1 != ret)
				{
					result.setCode(ResultEntity.ERROR);
					result.setError("添加心情指数失败");
					return result;
				}
			}
			
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("添加成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			return result;
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getMemberMoodRecord.json")
	@ResponseBody
	public Object getMemberMoodRecord(HttpSession session, HttpServletRequest request, MemberMoodRecord memberMoodRecord)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (null == memberMoodRecord)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}

			List<MemberMoodRecord> mmiLists = memberMoodRecordService.getMemberMoodRecord(memberMoodRecord);
			if (null == mmiLists || 0 == mmiLists.size())
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取心情指数为空");
				return result;
			}

			result.setCode(ResultEntity.SUCCESS);
			result.put("list", mmiLists);
			result.setCount(mmiLists.size());
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			return result;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/getDayMoodAndSport.json")
	@ResponseBody
	public Object getDayMoodAndSport(HttpSession session, HttpServletRequest request, Long mid, Date day)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (null == mid || null == day)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("用户传入的数据为空");
				return result;
			}

			MemberMoodRecord memberMoodRecord = new MemberMoodRecord();
			memberMoodRecord.setMid(mid);memberMoodRecord.setRecordDate(day);
			List<MemberMoodRecord> mmiLists = memberMoodRecordService.getMemberMoodRecord(memberMoodRecord);
			
			Integer moodIndex = memberMoodIndexService.getMmiDayAverage(mid, day);
			Integer sleepQuality = memberSleepQualityService.getMsqDayAverage(mid, day);

			result.setCode(ResultEntity.SUCCESS);
			result.put("moodRecord", mmiLists);
			result.put("moodIndex", moodIndex);
			result.put("sleepQuality", sleepQuality);
			result.setCount(mmiLists.size());
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			return result;
		}
	}
	
	
	/**
	 * 获取时间段内的每天是否有心情指数或者心情记录或者睡眠质量记录
	 * @param session
	 * @param request
	 * @param mid
	 * @param begin
	 * @param end
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getMoodAndSportFlags.json")
	@ResponseBody
	public Object getMoodAndSportFlags(HttpSession session, HttpServletRequest request, Long mid, String begin, String end)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (null == mid)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("用户ID不能为空");
				return result;
			}
	
			if (StringUtils.isEmpty(begin) || StringUtils.isEmpty(end))
			// if(null == begin || null == end)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("时间不能为空");
				return result;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date beginDate = sdf.parse(begin);
			Date endDate = sdf.parse(end);
			Integer days = daysBetween(beginDate, endDate);
			if (days <= 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("天数必须大于0");
				return result;
			}
			
			//构建时间轴
			Map<String, Integer> day2Flag = new TreeMap<String, Integer>();
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime(beginDate);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			for (int i = 0; i < days; i++)
			{
				String key = df.format(cal.getTime());
				day2Flag.put(key, -1);
				cal.add(Calendar.DATE, 1);
			}
	
			//查询心情指数
			List<MemberMoodIndex> mmiList = memberMoodIndexService.getMmiByTimeSlice(mid, beginDate, endDate);
			for (MemberMoodIndex mmi : mmiList)
			{
				String key = df.format(mmi.getRecordDate());
				day2Flag.put(key, day2Flag.get(key) + 1);
			}
			
			//查询心情记录
			List<MemberMoodRecord> mmrList = memberMoodRecordService.getMmrByTimeSlice(mid, beginDate, endDate);
			for (MemberMoodRecord mmr : mmrList)
			{
				String key = df.format(mmr.getRecordDate());
				day2Flag.put(key, day2Flag.get(key) + 1);
			}
			//查询睡眠质量
			List<MemberSleepQuality> msqList = memberSleepQualityService.getMsqByTimeSlice(mid, beginDate, endDate);
			for (MemberSleepQuality msq : msqList)
			{
				String key = df.format(msq.getRecordDate());
				day2Flag.put(key, day2Flag.get(key) + 1);
			}
			
			result.setCode(ResultEntity.SUCCESS);
			result.put("flags", day2Flag);
			result.setCount(day2Flag.size());
			return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			return result;
		}
	}

}
