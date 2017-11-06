package com.depression.model.api.dto;

import java.math.BigDecimal;
import java.util.List;

public class ApiEvaluationStatisticsDTO
{
	//评价数量
	private Integer times;
	//有效率
	private BigDecimal effectiveRate;
	//平均评分
	private BigDecimal score;
	//标签
	private List<ApiEvaluationLabelDTO> labelDTOs;
	
	public Integer getTimes()
	{
		return times;
	}

	public void setTimes(Integer times)
	{
		this.times = times;
	}

	public BigDecimal getEffectiveRate()
	{
		return effectiveRate;
	}

	public void setEffectiveRate(BigDecimal effectiveRate)
	{
		this.effectiveRate = effectiveRate;
	}

	public BigDecimal getScore()
	{
		return score;
	}

	public void setScore(BigDecimal score)
	{
		this.score = score;
	}

	public List<ApiEvaluationLabelDTO> getLabelDTOs()
	{
		return labelDTOs;
	}

	public void setLabelDTOs(List<ApiEvaluationLabelDTO> labelDTOs)
	{
		this.labelDTOs = labelDTOs;
	}
	
	
}
