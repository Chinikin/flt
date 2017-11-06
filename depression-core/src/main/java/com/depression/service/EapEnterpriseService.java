package com.depression.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.EapEnterpriseMapper;
import com.depression.dao.MemberMapper;
import com.depression.dao.TestingResultMapper;
import com.depression.model.EapEnterprise;
import com.depression.model.Member;
import com.depression.model.Page;
import com.depression.model.eap.dto.EapTestingResultCustom;

@Service
public class EapEnterpriseService
{
	
	public final static Integer TYPE_COMPANY = 1;
	
	public final static Integer TYPE_COLLEGE = 0;
	
	@Autowired
	EapEnterpriseMapper eapEnterpriseMapper;
	@Autowired
	TestingResultMapper testingResultMapper;
	@Autowired
	MemberMapper memberMapper;
	
	/**
	 * 新增Eap企业客户
	 * @param eapEnterprise
	 * @return
	 */
	public Integer newEnterprise(EapEnterprise eapEnterprise)
	{
		eapEnterprise.setCreateTime(new Date());
		return eapEnterpriseMapper.insertSelective(eapEnterprise);
	}
	
	/**
	 * 根据主键获取Eap企业信息
	 * @param eeId
	 * @return
	 */
	public EapEnterprise obtainEnterpriseByKey(Long eeId)
	{
		return eapEnterpriseMapper.selectByPrimaryKey(eeId);
	}
	
	/**
	 * 根据咨询师组id获取企业信息
	 * @param pgId
	 * @return
	 */
	public List<EapEnterprise> obtainEnterpriseBypgId(Long pgId)
	{
		EapEnterprise ee = new EapEnterprise();
		ee.setPgId(pgId);
		
		return eapEnterpriseMapper.selectSelective(ee);
	}
	
	/**
	 * 获取企业信息
	 * @param EapEnterprise
	 * @return
	 */
	public EapEnterprise obtainEnterpriseByParam(EapEnterprise eapEnterprise)
	{		
		List<EapEnterprise> eap  =  eapEnterpriseMapper.selectSelective(eapEnterprise);
		if (eap.size() > 0) {
			return eap.get(0);
		} else {
			return null;
		}
	}
	/**
	 * 根据名称或者联系人搜索Eap企业客户
	 * @param words 关键词
	 * @param pageIndex 页码
	 * @param pageSize 页大小
	 * @return
	 */
	public List<EapEnterprise> searchEnterprise(String words, Integer pageIndex, Integer pageSize)
	{
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		
		return eapEnterpriseMapper.searchWithPage(words, page.getPageStartNum(), pageSize);
	}
	
	/**
	 * 获取搜索结果数量
	 * @param words
	 * @return
	 */
	public Integer countSearchEnterprise(String words)
	{
		return eapEnterpriseMapper.countSearch(words);
	}
	
	/**
	 * 更新EAP企业资料
	 * @param eapEnterprise
	 * @return
	 */
	public Integer updateEnterprise(EapEnterprise eapEnterprise)
	{
		eapEnterprise.setUpdateTime(new Date());
		return eapEnterpriseMapper.updateByPrimaryKeySelective(eapEnterprise);
	}
	
	/**
	 * 启用/禁用EAP企业
	 * @param ids 企业id（eeId）
	 * @param isEnable 0启用 1禁用
	 * @return
	 */
	public Integer enableEnterprise(List<Long> ids, Byte isEnable)
	{
		return eapEnterpriseMapper.enableByPrimaryKeyBulk(ids, isEnable);
	}

	
	
	
	/**
	 * 获取 按时间倒序（分页）的测试公共信息 包括testingId,mid,testTime
	 * @param eeId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<EapTestingResultCustom> getEapTestingResultCustomByEeId(Long eeId,Long mid,
			Integer pageIndex, Integer pageSize) {
		List<Long> eemIds=new ArrayList<Long>();
		if(mid != null){
			//查询某个用户的信息
			eemIds.add(mid);
		}else{
			//获取该企业所有注册用户
			eemIds=eapEnterpriseMapper.selectRegedEmployee(eeId);
			if( eemIds.size() == 0){
				return null;
			}
		}
		
		Page page=new Page();
		page.setPageSize(pageSize);
		page.setPageIndex(pageIndex);
		//查询用户mid和测试时间（包括计分和跳转，根据参数设置是否分页）
		return testingResultMapper.selectResultByMids(eemIds,page.getPageStartNum(),page.getPageSize());
	}
	
	public Integer countEapTestingResultCustomByEeId(Long eeId,Long mid) {
		List<Long> eemIds=new ArrayList<Long>();
		if(mid != null){
			//查询某个用户的信息
			eemIds.add(mid);
		}else{
			//获取该企业所有注册用户
			eemIds=eapEnterpriseMapper.selectRegedEmployee(eeId);
		}
		//获取该企业所有注册用户
		return testingResultMapper.countResultByMids(eemIds);
	}
	
	/**
	 * 获取注册过的用户id
	 * @param eeId
	 * @return
	 */
	public List<Long> getRegedEmployee(Long eeId){
		return eapEnterpriseMapper.selectRegedEmployee(eeId);
	}

	public EapEnterprise selectByPrimaryKey(Long eeId) {
		return eapEnterpriseMapper.selectByPrimaryKey(eeId);
	}

	public List<Member> obtainPsyByEeIdAndName(Long eeId,String name) {
		return memberMapper.obtainPsyByEeIdAndName(eeId,name);
	}
	
}
