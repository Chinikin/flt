package com.depression.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.EvaluationLabelMapper;
import com.depression.dao.ServiceOrderEvaluationLabelMappingMapper;
import com.depression.dao.ServiceOrderEvaluationMapper;
import com.depression.model.EvaluationLabel;
import com.depression.model.ServiceOrderEvaluation;
import com.depression.model.ServiceOrderEvaluationLabelMapping;

@Service
public class ServiceOrderEvaluationService
{
	@Autowired
	private ServiceOrderEvaluationMapper serviceOrderEvaluationMapper;
	
	@Autowired
	private EvaluationLabelMapper evaluationLabelMapper;
	
	@Autowired
	private ServiceOrderEvaluationLabelMappingMapper evaluationLabelMappingMapper;

	/**
	 * 获取所有标签
	 * 
	 * @param record
	 * @return
	 */
	public List<EvaluationLabel> selectAllLabel()
	{
		EvaluationLabel record = new EvaluationLabel();
		record.setIsEnable(new Byte("0"));
		return evaluationLabelMapper.selectSelective(record);
	}
	
	/**
	 * 根据id获取标签
	 * @param elId
	 * @return
	 */
	public EvaluationLabel obtainLabelById(Long elId)
	{
		return evaluationLabelMapper.selectByPrimaryKey(elId);
	}
	
	/**
	 * 新增评论标签
	 * @param soid
	 * @param elId
	 * @return
	 */
	public Integer newLabelMapping(Long soid, Long soeId, Long elId)
	{
		ServiceOrderEvaluationLabelMapping sorlp  = new ServiceOrderEvaluationLabelMapping();
		sorlp.setSoid(soid);
		sorlp.setElId(elId);
		sorlp.setSoeId(soeId);
		sorlp.setCreateTime(new Date());
		
		return evaluationLabelMappingMapper.insertSelective(sorlp);
	}
	
	/**
	 * 新建评价记录
	 * 
	 * @param record
	 * @return
	 */
	public int newEvaluation(ServiceOrderEvaluation record)
	{
		return serviceOrderEvaluationMapper.insertSelective(record);
	}
	
	/**
	 * 根据订单id获取评分
	 * @param soid
	 * @return
	 */
	public ServiceOrderEvaluation obtainEvaluationBySoid(Long soid)
	{
		ServiceOrderEvaluation soe = new ServiceOrderEvaluation();
		soe.setSoid(soid);
		List<ServiceOrderEvaluation> soes = serviceOrderEvaluationMapper.selectSelective(soe);
		if(soes.size()>0)
		{
			return soes.get(0);
		}
		return null;
	}
	
	/**
	 * 获取咨询师订单评价列表，按时间倒序
	 * @param pid 咨询师会员id
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<ServiceOrderEvaluation> obtainEvaluations4Psycho(Long pid, Integer pageIndex, Integer pageSize)
	{
		ServiceOrderEvaluation soe = new ServiceOrderEvaluation();
		soe.setPid(pid);
		soe.setIsDelete((byte) 0);
		soe.setPageIndex(pageIndex);
		soe.setPageSize(pageSize);
		
		return serviceOrderEvaluationMapper.selectSelectiveWithPageOrder(soe);
	}
	
	/**
	 * 获取咨询师订单评价列表数量
	 * @param pid 咨询师会员id
	 * @return
	 */
	public Integer countEvaluations4Psycho(Long pid)
	{
		ServiceOrderEvaluation soe = new ServiceOrderEvaluation();
		soe.setPid(pid);
		soe.setIsDelete((byte) 0);
		
		return serviceOrderEvaluationMapper.countSelective(soe);
	}
	
	/**
	 * 获取订单评价的标签
	 * @param soeId 评价id
	 * @return
	 */
	public List<EvaluationLabel> obtainLabels4Evaluation(Long soeId)
	{
		//查询映射
		ServiceOrderEvaluationLabelMapping sorlp  = new ServiceOrderEvaluationLabelMapping();
		sorlp.setSoeId(soeId);
		sorlp.setIsEnable((byte) 0);;
		List<ServiceOrderEvaluationLabelMapping> mappings = 
				evaluationLabelMappingMapper.selectSelective(sorlp);
		//查询标签
		List<Long> elIds = new ArrayList<Long>();
		for(ServiceOrderEvaluationLabelMapping m : mappings)
		{
			elIds.add(m.getElId());
		}
		if(elIds.size() > 0)
		{
			return evaluationLabelMapper.selectByPrimaryKeyBulk(elIds);
		}else
		{
			return new ArrayList<EvaluationLabel>();
		}
	}
	
	/**
	 * 获取咨询师的某标签数量
	 * @param elId 标签id
	 * @param pid 咨询师id
	 * @return
	 */
	public Integer countLabels4Psycho(Long elId, Long pid)
	{
		return evaluationLabelMappingMapper.countLabelOfPsycho(elId, pid);
	}
	
	
	/**
	 * 获取订单评价
	 */
	public List<ServiceOrderEvaluation> selectBySoidsBulk(List<Long> soids){
		return serviceOrderEvaluationMapper.selectBySoidsBulk(soids);
	}
	
	
}
