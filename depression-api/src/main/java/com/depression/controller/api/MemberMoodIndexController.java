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
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.SimpleFormatter;

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
import com.depression.service.MemberMoodIndexService;

/**
 * @author:ziye_huang
 * @date:2016年5月11日
 */
@Controller
@RequestMapping("/MemberMoodIndex")
public class MemberMoodIndexController
{
	@Autowired
	MemberMoodIndexService memberMoodIndexService;

	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception
	{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/addMemberMoodIndex.json")
	@ResponseBody
	public Object addMemberMoodIndex(HttpSession session, HttpServletRequest request, MemberMoodIndex memberMoodIndex)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (null == memberMoodIndex)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}
			
			if(memberMoodIndex.getRecordDate()==null)
			{
				memberMoodIndex.setRecordDate(new Date());
			}

			Integer ret = memberMoodIndexService.insert(memberMoodIndex);
			if (1 != ret)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("添加心情指数失败");
				return result;
			}
			
			Integer index = memberMoodIndexService.getMmiDayAverage(memberMoodIndex.getMid(),memberMoodIndex.getRecordDate());

			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("添加成功");
			result.put("index", index);
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			return result;
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getMemberMoodIndex.json")
	@ResponseBody
	public Object getMemberMoodIndex(HttpSession session, HttpServletRequest request, MemberMoodIndex memberMoodIndex)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (null == memberMoodIndex)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}

			List<MemberMoodIndex> mmiLists = memberMoodIndexService.getMemberMoodIndex(memberMoodIndex);
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

	/**
	 * 获取设定天数的心情指数曲线，曲线值根据当天多个记录进行公式计算
	 * 
	 * @param session
	 * @param request
	 * @param mid
	 * @param begin
	 * @param end
	 * @return
	 */
	@RequestMapping(value = "/getMemberMoodIndexCurve.json")
	@ResponseBody
	public Object getMemberMoodIndexCurve(HttpSession session, HttpServletRequest request, Long mid, String begin, String end)
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
				result.setError("曲线天数不能为空且必须大于0");
				return result;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date beginDate = sdf.parse(begin);
			Date endDate = sdf.parse(end);
			Integer days = daysBetween(beginDate, endDate);
			if (days <= 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("曲线天数必须大于0");
				return result;
			}

			List<MemberMoodIndex> mmiList = memberMoodIndexService.getMmiByTimeSlice(mid, beginDate, endDate);

			// 将心情记录按天归档
			Map<String, List<MemberMoodIndex>> day2MmiList = new HashMap<String, List<MemberMoodIndex>>();
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime(beginDate);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			for (int i = 0; i < days; i++)
			{
				String key = df.format(cal.getTime());
				day2MmiList.put(key, new ArrayList<MemberMoodIndex>());
				cal.add(Calendar.DATE, 1);
			}

			for (MemberMoodIndex mmi : mmiList)
			{
				String key = df.format(mmi.getRecordDate());
				List<MemberMoodIndex> ml = day2MmiList.get(key);
				ml.add(mmi);
			}

			// 根据公式计算每天的心情指数, 暂时简单求均值
			Map<String, Integer> curvePoint = new TreeMap<String, Integer>();
			for (Entry<String, List<MemberMoodIndex>> entry : day2MmiList.entrySet())
			{
				Integer index = 0;
				for (MemberMoodIndex mmi : entry.getValue())
				{
					index += mmi.getMoodIndex();
				}
				if (entry.getValue().size() > 0)
				{
					index /= entry.getValue().size();
				} else
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
	 * 获取设定天数的心情指数拼图，曲线值根据当天多个记录进行公式计算
	 * @param session
	 * @param request
	 * @param mid
	 * @param begin
	 * @param end
	 * @return
	 */
	@RequestMapping(value = "/getMemberMoodIndexPie.json")
	@ResponseBody
	public Object getMemberMoodIndexPie(HttpSession session, HttpServletRequest request, Long mid, String begin, String end)
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
				result.setError("饼图天数不能为空且必须大于0");
				return result;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date beginDate = sdf.parse(begin);
			Date endDate = sdf.parse(end);
			Integer days = daysBetween(beginDate, endDate);
			if (days <= 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("饼图天数必须大于0");
				return result;
			}
	
			List<MemberMoodIndex> mmiList = memberMoodIndexService.getMmiByTimeSlice(mid, beginDate, endDate);
			
			// 将心情记录按天归档
			Map<String, List<MemberMoodIndex>> day2MmiList = new HashMap<String, List<MemberMoodIndex>>();
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime(beginDate);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			for (int i = 0; i < days; i++)
			{
				String key = df.format(cal.getTime());
				day2MmiList.put(key, new ArrayList<MemberMoodIndex>());
				cal.add(Calendar.DATE, 1);
			}
	
			for (MemberMoodIndex mmi : mmiList)
			{
				String key = df.format(mmi.getRecordDate());
				List<MemberMoodIndex> ml = day2MmiList.get(key);
				ml.add(mmi);
			}
	
			// 根据公式计算每天的心情指数, 暂时简单求均值
			Map<String, Integer> curvePoint = new TreeMap<String, Integer>();
			for (Entry<String, List<MemberMoodIndex>> entry : day2MmiList.entrySet())
			{
				Integer index = 0;
				for (MemberMoodIndex mmi : entry.getValue())
				{
					index += mmi.getMoodIndex();
				}
				if (entry.getValue().size() > 0)
				{
					index /= entry.getValue().size();
				} else
				{
					index = -1;
				}
				curvePoint.put(entry.getKey(), index);
			}
		
			//根据分数统计心情饼图
			Map<String, Integer> pieCount = new HashMap<String, Integer>();
			Integer unrecorder = 0;
			Integer happy = 0;
			Integer normal = 0;
			Integer sad = 0;
			for(Entry<String,Integer> e : curvePoint.entrySet())
			{
				if(e.getValue() < 0)
				{
					unrecorder++;
				}
				else if(e.getValue()>=0 && e.getValue()<=33)
				{
					sad++;
				}
				else if(e.getValue()>=34 && e.getValue()<=67)
				{
					normal++;
				}
				else
				{
					happy++;
				}
			}
			pieCount.put("unrecorder", unrecorder);
			pieCount.put("happy", happy);
			pieCount.put("normal", normal);
			pieCount.put("sad", sad);
			
			result.setCode(ResultEntity.SUCCESS);
			result.put("pie", pieCount);
			result.setCount(pieCount.size());
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
