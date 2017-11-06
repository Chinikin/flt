package com.depression.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.EapEmployeeMapper;
import com.depression.dao.EapEnterpriseMapper;
import com.depression.dao.EapValidEnterpriseHistoryMapper;
import com.depression.dao.PsychoGroupMapper;
import com.depression.dao.PsychoGroupMemberMapper;
import com.depression.model.EapEmployee;
import com.depression.model.EapEnterprise;
import com.depression.model.EapValidEnterpriseHistory;
import com.depression.model.Member;
import com.depression.model.PsychoGroup;

/**
 * 实现基于一个员工可能在多个企业的情况，以及一个咨询师在多个分组的情况
 * 一个企业只有一个咨询师组
 * @author caizj
 *
 */
@Service
public class EapService
{
	@Autowired
	EapEmployeeMapper eapEmployeeMapper;
	@Autowired
	EapEnterpriseMapper eapEnterpriseMapper;
	@Autowired
	PsychoGroupMemberMapper psychoGroupMemberMapper;
	@Autowired
	PsychoGroupMapper psychoGroupMapper;
	@Autowired	
	EapValidEnterpriseHistoryMapper eapValidEnterpriseHistoryMapper;
	@Autowired
	PsychoInfoService psychoInfoService;
	@Autowired
	MemberService memberService;
	
	/**
	 * 查询Eap企业是否有效
	 * @param eeId Eap企业id
	 * @return 0 无效 其他值有效
	 */
	Integer isValidEapEnterprise(Long eeId)
	{
		EapEnterprise eapEnterprise = eapEnterpriseMapper.selectByPrimaryKey(eeId);
		if(eapEnterprise == null)
		{//不存在或已删除
			return 0;
		}
		if(eapEnterprise.getIsEnable()==1)
		{//被禁用
			return 0;
		}
		
		if(eapEnterprise.getConsumedOrderAmount().compareTo(
				eapEnterprise.getPurchasedOrderAmount()) >= 0)
		{//购买的订单数量已经消费完
			return 0;
		}
		
		if(eapEnterprise.getServiceStartDate()==null || 
				eapEnterprise.getServiceEndDate()==null)
		{//未设置开始和结束时间
			return 0;
		}
		
		Date current = new Date();
		if(eapEnterprise.getServiceStartDate().before(current) &&
				eapEnterprise.getServiceEndDate().after(current))
		{
			return 1;
		}
		
		return 0;
	}
	
	/**
	 * 查询是否是Eap有效员工
	 * @param mid 员工会员id
	 * @return 0 无效  其他值有效
	 */
	public Integer isValidEapEmployee(Long mid)
	{
		List<Long> eeIds = eapEmployeeMapper.selectEeIdOfEmployee(mid);
		if(eeIds.size()==0)
		{//没有员工记录。因为大部分用户不是EAP员工，所以此处可以高效率返回。
			return 0;
		}
		
		for(Long eeId : eeIds)
		{//检查所有员工关联的企业是否有效
			if(!isValidEapEnterprise(eeId).equals(0))
			{
				return 1;
			}
		}
		
		return 0;
	}
	
	/**
	 * 获取员工EAP有效的企业id
	 * @param mid 员工会员id
	 * @return
	 */
	public List<Long> obtainValidEeIds4EapEmployee(Long mid)
	{
		List<Long> validEeIds = new ArrayList<Long>();
		
		List<Long> eeIds = eapEmployeeMapper.selectEeIdOfEmployee(mid);
		if(eeIds.size()==0)
		{//没有员工记录。因为大部分用户不是EAP员工，所以此处可以高效率返回。
			return validEeIds;
		}
		
		for(Long eeId : eeIds)
		{//检查所有员工关联的企业是否有效
			if(!isValidEapEnterprise(eeId).equals(0))
			{
				validEeIds.add(eeId);
			}
		}
		
		return validEeIds;
	}
	
	
	/**
	 * 获取咨询师EAP有效的企业id
	 * @param mid 员工会员id
	 * @return
	 */
	public List<Long> obtainValidEeIds4EapPsycho(Long mid)
	{
		return eapEnterpriseMapper.selectEeIdOfEapPsycho(mid);
	}
	
	
	
	/**
	 * 获取员工被服务的咨询师组id
	 * @param mid 员工会员id
	 * @return
	 */
	public List<Long> obtainPgIds4EapEmployee(Long mid)
	{
		List<Long> pgIds = new ArrayList<Long>();
		
		List<Long> validEeIds = obtainValidEeIds4EapEmployee(mid);
		for(Long eeId : validEeIds)
		{
			EapEnterprise ee = eapEnterpriseMapper.selectByPrimaryKey(eeId);
			pgIds.add(ee.getPgId());
		}
		
		return pgIds;
	}
	
	/**
	 * 获取企业关联的咨询师组id
	 * @param eeId 企业id
	 * @return
	 */
	public Long obtainPgId4EapEnterprise(Long eeId)
	{
		EapEnterprise ee = eapEnterpriseMapper.selectByPrimaryKey(eeId);
		
		return ee.getPgId();
	}
	
	/**
	 * 查询咨询师组是否有效
	 * @param pgId 咨询师组id
	 * @return 0 无效 其他值有效
	 */
	Integer isValidPsychoGroup(Long pgId)
	{
		PsychoGroup psychoGroup = psychoGroupMapper.selectByPrimaryKey(pgId);
		if(psychoGroup==null)
		{//不存在或者已经删除
			return 0;
		}
		if(psychoGroup.getIsEnable()==1)
		{//被禁用
			return 0;
		}
		
		return 1;
	}
	
	/**
	 * 查询咨询师是否是有效Eap咨询师
	 * @param pid
	 * @return 0 无效 其他值有效
	 */
	public Integer isValidEapPsycho(Long pid)
	{
		List<Long> pgIds = psychoGroupMemberMapper.selectPgIdOfPsycho(pid);
		if(pgIds.size()==0)
		{//没有加入任何组
			return 0;
		}
		
		for(Long pgId : pgIds)
		{//查询参加的组是否有效
			if(isValidPsychoGroup(pgId) != 0)
			{
				return 1;
			}
		}
		
		return 0;
	}
	
	/**
	 * 获取咨询师有效的咨询师组id
	 * @param pid 咨询师会员id
	 * @return
	 */
	public List<Long> obtainValidPgIds4EapPsycho(Long pid)
	{
		List<Long> validPgIds = new ArrayList<Long>();
		
		List<Long> pgIds = psychoGroupMemberMapper.selectPgIdOfPsycho(pid);
		if(pgIds.size()==0)
		{//没有加入任何组
			return validPgIds;
		}
		
		for(Long pgId : pgIds)
		{//查询参加的组是否有效
			if(isValidPsychoGroup(pgId) != 0)
			{
				validPgIds.add(pgId);
			}
		}
		
		return validPgIds;
	}
	
	/**
	 * 查询员工是否和咨询师有服务关系
	 * @param mid 员工会员id
	 * @param pid 咨询师会员id
	 * @return 0 不存在服务关系  1 咨询数未到上限 2咨询数已到上限
	 */
	public Integer isEmployeeServedByPsycho(Long mid, Long pid)
	{
		Integer serviceStatus = 0;
		
		List<Long> eeIds = obtainValidEeIds4EapEmployee(mid);
		if(eeIds.size()==0)
		{//非Eap有效用户
			return serviceStatus;
		}
		List<Long> pgIds = obtainValidPgIds4EapPsycho(pid);
		if(pgIds.size()==0)
		{//非Eap有效咨询师
			return serviceStatus;
		}
		
		for(Long eeId : eeIds)
		{//查询员工所在企业是否和咨询师所在组有服务关系
			//此处查询因为前面已经有条件保证，所以不需要判断直接取唯一值
			EapEmployee eapEmployee = eapEmployeeMapper.selectEmployeeByMidAndEeId(mid, eeId).get(0);
			EapEnterprise eapEnterprise = eapEnterpriseMapper.selectByPrimaryKey(eeId);
			
			if(pgIds.contains(eapEnterprise.getPgId()))
			{
				if(eapEmployee.getEapAdvisoryTimes().compareTo(
						eapEnterprise.getOrderSingleLimit().intValue()) < 0)
				{//咨询数未到上限
					serviceStatus = 1;
					break;
				}else
				{
					serviceStatus = 2;
				}
			}
		}
		
		return serviceStatus;
	}
	
	/**
	 * 获取会员和咨询师的EAP服务关系，以及关联的企业
	 * @param mid 员工会员id
	 * @param pid 咨询师会员id
	 * @return "status" 0 无服务关系 1 未到咨询上限 2 已到咨询上限
	 *         "eeId" 关联企业的id
	 */
	public Map<String, Long> obtainServiceStatusAndEeId(Long mid, Long pid)
	{
		Map<String, Long> statusEeId =  new HashMap<String, Long>();
		Long serviceStatus = 0L;
		Long serviceEeId = 0L;
		
		List<Long> eeIds = obtainValidEeIds4EapEmployee(mid);
		if(eeIds.size()==0)
		{//非Eap有效用户
			statusEeId.put("status", serviceStatus);
			statusEeId.put("eeId", serviceEeId);
			return statusEeId;
		}
		List<Long> pgIds = obtainValidPgIds4EapPsycho(pid);
		if(pgIds.size()==0)
		{//非Eap有效咨询师
			statusEeId.put("status", serviceStatus);
			statusEeId.put("eeId", serviceEeId);
			return statusEeId;
		}
		
		for(Long eeId : eeIds)
		{//查询员工所在企业是否和咨询师所在组有服务关系
			//此处查询因为前面已经有条件保证，所以不需要判断直接取唯一值
			EapEmployee eapEmployee = eapEmployeeMapper.selectEmployeeByMidAndEeId(mid, eeId).get(0);
			EapEnterprise eapEnterprise = eapEnterpriseMapper.selectByPrimaryKey(eeId);
			
			if(pgIds.contains(eapEnterprise.getPgId()))
			{
				if(eapEmployee.getEapAdvisoryTimes().compareTo(
						eapEnterprise.getOrderSingleLimit().intValue()) < 0)
				{//咨询数未到上限
					serviceStatus = 1L;
					serviceEeId = eeId;
					break;
				}else
				{
					if(serviceStatus==0)
					{
						serviceStatus = 2L;
						serviceEeId = eeId;
					}
				}
			}
		}
		
		statusEeId.put("status", serviceStatus);
		statusEeId.put("eeId", serviceEeId);
		return statusEeId;
	}
	
	/**
	 * 获取员工服务失效的企业id列表
	 * @param mid 员工会员id
	 * @return
	 */
	public List<Long> obtainInvalidEeIds4EapEmployee(Long mid)
	{
		EapValidEnterpriseHistory eveh =  new EapValidEnterpriseHistory();
		eveh.setMid(mid);
		List<EapValidEnterpriseHistory> evehs = 
				eapValidEnterpriseHistoryMapper.selectSelective(eveh);
		
		List<Long> historyValidEeIds = new ArrayList<Long>();
		List<Long> evehIds = new ArrayList<Long>();
		for(EapValidEnterpriseHistory e : evehs)
		{
			historyValidEeIds.add(e.getEeId());
			evehIds.add(e.getEvehId());
		}
		
		List<Long> validEeIds =  obtainValidEeIds4EapEmployee(mid);
		//复制historyValidEeIds
		List<Long> cpHves = new ArrayList<Long>();
		cpHves.addAll(historyValidEeIds);
		
		if(historyValidEeIds.size()==validEeIds.size() && !cpHves.retainAll(validEeIds))
		{//有效企业id列表没有变化，大部分在此处返回，EAP企业失效是小概率
			return new ArrayList<Long>();
		}
		
		//删除历史记录
		if(evehIds.size()!=0)
		{
			eapValidEnterpriseHistoryMapper.deleteByPrimaryKeyBulk(evehIds);
		}
		//新建记录
		for(Long eeId : validEeIds)
		{
			EapValidEnterpriseHistory e = new EapValidEnterpriseHistory();
			e.setEeId(eeId);
			e.setMid(mid);
			e.setCreateTime(new Date());
			eapValidEnterpriseHistoryMapper.insertSelective(e);
		}
		
		//计算失效的企业id列表
		historyValidEeIds.removeAll(validEeIds);
		List<Long> invalidEeIds = new ArrayList<Long>();
		for(Long eeId : historyValidEeIds)
		{
			EapEnterprise ee =  eapEnterpriseMapper.selectByPrimaryKey(eeId);
			if(ee != null)
			{
				invalidEeIds.add(eeId);
			}
		}
		
		return invalidEeIds;
	}

	/**
	 * 获取当前企业中的咨询师    如果piId不匹配则该咨询师不在该组织中
	 * @param piId
	 * @param eeId
	 * @return
	 */
	public Member getPsychoInfoByMidAndEeId(Long piId,
			Long eeId) {
		List<Long> ids=obtainValidPgIds4EapPsycho(piId);
		if(ids.size() == 0 || ids == null){
			//咨询师未加入任何组
			return null;
		}
		//查询该企业的咨询师组id
		EapEnterprise ee=eapEnterpriseMapper.selectByPrimaryKey(eeId);
		if(!ids.contains(ee.getPgId())){
			//咨询师未加入该组
			return null;
		}
		//获取该咨询师信息
		return memberService.selectMemberByMid(piId);
	}
}
