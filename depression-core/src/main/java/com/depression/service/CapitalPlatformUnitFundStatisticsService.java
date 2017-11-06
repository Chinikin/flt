package com.depression.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.CapitalIncomeExpensesMapper;
import com.depression.dao.CapitalPlatformUnitFundStatisticsMapper;
import com.depression.model.CapitalIncomeExpenses;
import com.depression.model.CapitalPlatformUnitFundStatistics;
import com.depression.model.ServiceOrder;
import com.depression.model.web.dto.CapitalPlatformUnitFundStatisticsDTO;

/**
 * @author:ziye_huang
 * @date:2016年8月30日
 */
@Service
public class CapitalPlatformUnitFundStatisticsService
{
	@Autowired
	CapitalPlatformUnitFundStatisticsMapper capitalPlatformUnitFundStatisticsMapper;
	@Autowired
	CapitalIncomeExpensesMapper capitalIncomeExpenseMapper;
	

	/**
	 * 查询平台收支记录
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<CapitalPlatformUnitFundStatisticsDTO> selectIncomeStatistics(Date startDate, Date endDate)
	{
		return capitalPlatformUnitFundStatisticsMapper.selectIncomeStatistics(startDate, endDate);
	}

	public int selectIncomeStatisticsCount(Date startDate, Date endDate)
	{
		return capitalPlatformUnitFundStatisticsMapper.selectIncomeStatisticsCount(startDate, endDate);
	}
	
	public void calcPlatformUnitFundStatistics()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String today = sdf.format(new Date());
		List<String> cieDateSet = capitalIncomeExpenseMapper.selectDateSet();
		List<String> statisticsDateSet = capitalPlatformUnitFundStatisticsMapper.selectDateSet();
		
		for(String day : cieDateSet)
		{
			if(!statisticsDateSet.contains(day) || day.equals(today))
			{//今天的可以更新
				BigDecimal platfromIncome = new BigDecimal(0);
				BigDecimal platfromExpenses = new BigDecimal(0);
				BigDecimal platfromTopup = new BigDecimal(0);
				BigDecimal platfromWithdraw = new BigDecimal(0);
				
				List<CapitalIncomeExpenses> cies = 
						capitalIncomeExpenseMapper.selectByDay(day);
				for(CapitalIncomeExpenses cie : cies)
				{
					if(cie.getItems() == CapitalIncomeExpenseService.ITEMS_EXPENSES
							|| cie.getItems()==CapitalIncomeExpenseService.ITEMS_SERVICE_RETURN)
					{
						platfromIncome = platfromIncome.add(cie.getAmount());
					}else if(cie.getItems() == CapitalIncomeExpenseService.ITEMS_SERVICE_INCOME
							|| cie.getItems()==CapitalIncomeExpenseService.ITEMS_REFUND)
					{
						platfromExpenses = platfromExpenses.add(cie.getAmount());
					}else if(cie.getItems() == CapitalIncomeExpenseService.ITEMS_TOPUP)
					{
						platfromTopup = platfromTopup.add(cie.getAmount());
					}else if(cie.getItems() == CapitalIncomeExpenseService.ITEMS_WITHDRAW)
					{
						platfromWithdraw = platfromWithdraw.add(cie.getAmount());
					}
				}

				CapitalPlatformUnitFundStatistics statistics = new CapitalPlatformUnitFundStatistics();
				statistics.setExpensesAmount(platfromExpenses);
				statistics.setIncomeAmount(platfromIncome);
				statistics.setTopUpAmount(platfromTopup);
				statistics.setWithdrawAmount(platfromWithdraw);
				try
				{
					statistics.setStatisticsDate(sdf.parse(day));
				} catch (ParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(day.equals(today) && statisticsDateSet.contains(day))
				{//今天的可以更新
					CapitalPlatformUnitFundStatistics s = new CapitalPlatformUnitFundStatistics();
					try
					{
						s.setStatisticsDate(sdf.parse(day));
					} catch (ParseException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					List<CapitalPlatformUnitFundStatistics> statisticsList = capitalPlatformUnitFundStatisticsMapper.selectSelective(s);
					if(statisticsList.size() > 0)
					{
						statistics.setFsid(statisticsList.get(0).getFsid());
						capitalPlatformUnitFundStatisticsMapper.updateByPrimaryKeySelective(statistics);
					}
							
				}else{
					capitalPlatformUnitFundStatisticsMapper.insertSelective(statistics);
				}
			}
		}
		
		
	}

}
