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
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ResultEntity;
import com.depression.model.MemberMoodIndex;
import com.depression.model.MemberSleepQuality;
import com.depression.service.MemberSleepQualityService;

/**
 * @author:ziye_huang
 * @date:2016年5月11日
 */
@Controller
@RequestMapping("/MemberSleepQuality")
public class MemberSleepQualityController
{
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
	
	@RequestMapping(method = RequestMethod.POST, value = "/addMemberSleepQuality.json")
	@ResponseBody
	public Object addMemberSleepQuality(HttpSession session, HttpServletRequest request, MemberSleepQuality memberSleepQuality)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (null == memberSleepQuality || memberSleepQuality.getSleepQuality() == null
					||memberSleepQuality.getMid() == null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}
			
			if(memberSleepQuality.getRecordDate()==null)
			{
				memberSleepQuality.setRecordDate(new Date());
			}

			Integer ret = memberSleepQualityService.insert(memberSleepQuality);
			if (1 != ret)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("添加睡眠质量失败");
				return result;
			}
			
			Integer sleepQuality = memberSleepQualityService.getMsqDayAverage(memberSleepQuality.getMid(),
					memberSleepQuality.getRecordDate());

			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("添加成功");
			result.put("sleepQuality", sleepQuality);
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			return result;
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getMemberSleepQuality.json")
	@ResponseBody
	public Object getMemberSleepQuality(HttpSession session, HttpServletRequest request, MemberSleepQuality memberSleepQuality)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (null == memberSleepQuality)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}

			List<MemberSleepQuality> mmiLists = memberSleepQualityService.getMemberSleepQuality(memberSleepQuality);
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
	
	/**
	 * 获取时间段内的天数，左右封闭区间
	 * @param begin
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public Integer daysBetween(Date begin,Date end) throws ParseException    
    {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        begin=sdf.parse(sdf.format(begin));  
        end=sdf.parse(sdf.format(end));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(begin);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(end);    
        long time2 = cal.getTimeInMillis();         
        Long between_days=(time2-time1)/(1000*3600*24) + 1; //左右封闭区间 
            
       return between_days.intValue();           
    }   
	
	
	/**
	 * 获取设定天数的睡眠质量指数曲线，曲线值根据当天多个记录进行公式计算
	 * @param session
	 * @param request
	 * @param mid
	 * @param begin
	 * @param end
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getMemberSleepQualityCurve.json")
	@ResponseBody
	public Object getMemberSleepQualityCurve(HttpSession session, HttpServletRequest request, Long mid, Date begin, Date end)
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
			
			if(null == begin || null == end)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("曲线天数不能为空且必须大于0");
				return result;
			}
			
			Integer days = daysBetween(begin, end);
			if(days <= 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("曲线天数必须大于0");
				return result;
			}

			List<MemberSleepQuality> msqList = memberSleepQualityService.getMsqByTimeSlice(mid, begin, end);
			
			//将心情记录按天归档
			Map<String, List<MemberSleepQuality>> day2MsqList = new HashMap<String, List<MemberSleepQuality>>();
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime(begin);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			for(int i=0; i<days; i++)
			{
				String key = df.format(cal.getTime());
				day2MsqList.put(key, new ArrayList<MemberSleepQuality>());
				cal.add(Calendar.DATE, 1);
			}
			
			for(MemberSleepQuality msq : msqList)
			{
				String key = df.format(msq.getRecordDate());
				List<MemberSleepQuality> ml = day2MsqList.get(key);
				ml.add(msq);
			}
			
			//根据公式计算每天的睡眠质量, 暂时简单求均值
			Map<String, Integer> curvePoint = new TreeMap<String, Integer>();
			for(Entry<String, List<MemberSleepQuality>> entry : day2MsqList.entrySet())
			{
				Integer index = 0;
				for(MemberSleepQuality msq : entry.getValue())
				{
					index += msq.getSleepQuality();
				}
				if(entry.getValue().size() > 0)
				{
					index /= entry.getValue().size();
				}else
				{
					index = -1;
				}
				curvePoint.put(entry.getKey(), index);
			}
			
			result.setCode(ResultEntity.SUCCESS);
			result.put("curve", curvePoint);
			result.setCount(curvePoint.size());
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
	 * 获取设定天数的睡眠质量指数饼图，值根据当天多个记录进行公式计算
	 * @param session
	 * @param request
	 * @param mid
	 * @param begin
	 * @param end
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getMemberSleepQualityPie.json")
	@ResponseBody
	public Object getMemberSleepQualityPie(HttpSession session, HttpServletRequest request, Long mid, Date begin, Date end)
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
			
			if(null == begin || null == end)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("曲线天数不能为空且必须大于0");
				return result;
			}
			
			Integer days = daysBetween(begin, end);
			if(days <= 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("曲线天数必须大于0");
				return result;
			}

			List<MemberSleepQuality> msqList = memberSleepQualityService.getMsqByTimeSlice(mid, begin, end);
			
			//将心情记录按天归档
			Map<String, List<MemberSleepQuality>> day2MsqList = new HashMap<String, List<MemberSleepQuality>>();
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime(begin);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			for(int i=0; i<days; i++)
			{
				String key = df.format(cal.getTime());
				day2MsqList.put(key, new ArrayList<MemberSleepQuality>());
				cal.add(Calendar.DATE, 1);
			}
			
			for(MemberSleepQuality msq : msqList)
			{
				String key = df.format(msq.getRecordDate());
				List<MemberSleepQuality> ml = day2MsqList.get(key);
				ml.add(msq);
			}
			
			//根据公式计算每天的心情指数, 暂时简单求均值
			Map<String, Integer> curvePoint = new TreeMap<String, Integer>();
			for(Entry<String, List<MemberSleepQuality>> entry : day2MsqList.entrySet())
			{
				Integer index = 0;
				for(MemberSleepQuality msq : entry.getValue())
				{
					index += msq.getSleepQuality();
				}
				if(entry.getValue().size() > 0)
				{
					index /= entry.getValue().size();
				}else
				{
					index = -1;
				}
				curvePoint.put(entry.getKey(), index);
			}
			
			//根据分数统计心情饼图
			Map<String, Integer> pieCount = new HashMap<String, Integer>();
			Integer unrecorder = 0;
			Integer well = 0;
			Integer normal = 0;
			Integer bad = 0;
			for(Entry<String,Integer> e : curvePoint.entrySet())
			{
				if(e.getValue() < 0)
				{
					unrecorder++;
				}
				else if(e.getValue()>=0 && e.getValue()<=33)
				{
					bad++;
				}
				else if(e.getValue()>=34 && e.getValue()<=67)
				{
					normal++;
				}
				else
				{
					well++;
				}
			}
			pieCount.put("unrecorder", unrecorder);
			pieCount.put("well", well);
			pieCount.put("normal", normal);
			pieCount.put("bad", bad);
			
			result.setCode(ResultEntity.SUCCESS);
			result.put("pie", pieCount);
			result.setCount(pieCount.size());
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			return result;
		}
	}
	
	
}
