package com.depression.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.LicenseTypeMapper;
import com.depression.dao.PrimaryDomainMapper;
import com.depression.dao.PsychoInfoMapper;
import com.depression.model.LicenseType;
import com.depression.model.Page;
import com.depression.model.PrimaryDomain;
import com.depression.model.PsychoInfo;

@Service
public class PsychoInfoService
{
	@Autowired
	PsychoInfoMapper psychoInfoMapper;
	@Autowired
	LicenseTypeMapper licenseTypeMapper;
	@Autowired
	PrimaryDomainMapper primaryDomainMapper;
	
	public static byte AUDIT_STATUS_UNAUDIT = 0; //未审核
	public static byte AUDIT_STATUS_PASS = 1; //审核通过
	public static byte AUDIT_STATUS_NOGO = 2; //审核未通过
	
	/**
	 * 获取执照类型列表
	 * @return
	 */
	public List<LicenseType> getAllLicenseTypes()
	{
		LicenseType record = new LicenseType();
		return licenseTypeMapper.selectSelective(record);
	}
	
	/**
	 * 根据执照类型id获取执照类型
	 * @param ltid
	 * @return
	 */
	public LicenseType getLicenseTypeByPrimaryKey(Long ltid)
	{
		return licenseTypeMapper.selectByPrimaryKey(ltid);
	}
	
	/**
	 * 新建专家入住信息
	 * @param psychoInfo
	 * @param domains
	 */
	public void newPsychoInfo(PsychoInfo psychoInfo, List<String> domains)
	{
		psychoInfo.setCreateTime(new Date());
		psychoInfoMapper.insertSelective(psychoInfo);
		for(String d : domains)
		{
			PrimaryDomain primaryDomain = new PrimaryDomain();
			primaryDomain.setDomainName(d);
			primaryDomain.setPiid(psychoInfo.getPiid());
			primaryDomainMapper.insertSelective(primaryDomain);
		}
	}
	
	/**
	 * 修改专家入住信息
	 * @param psychoInfo
	 * @param domains
	 */
	public void updatePsychoInfo(PsychoInfo psychoInfo, List<String> domains)
	{
		psychoInfoMapper.updateByPrimaryKeySelective(psychoInfo);
		
		if(domains != null)
		{
			//删除原擅长领域记录
			PrimaryDomain pd = new PrimaryDomain();
			pd.setPiid(psychoInfo.getPiid());
			List<PrimaryDomain> primaryDomains = primaryDomainMapper.selectSelective(pd);
			
			List<Long> ids = new ArrayList<Long>();
			for(PrimaryDomain p : primaryDomains)
			{
				ids.add(p.getPdid());
			}
			primaryDomainMapper.deleteByPrimaryKeyBulk(ids);
			//新建擅长领域记录
			for(String d : domains)
			{
				PrimaryDomain primaryDomain = new PrimaryDomain();
				primaryDomain.setDomainName(d);
				primaryDomain.setPiid(psychoInfo.getPiid());
				primaryDomainMapper.insertSelective(primaryDomain);
			}
		}
	}
	
	/**
	 * 更新mid字段
	 * @param piid
	 * @param mid
	 * @return
	 */
	public Integer updateFieldMid(Long piid, Long mid)
	{
		PsychoInfo pi =  new PsychoInfo();
		pi.setPiid(piid);
		pi.setMid(mid);
		return psychoInfoMapper.updateByPrimaryKeySelective(pi);
	}
	
	/**
	 * 根据专家入住信息id获取擅长领域列表
	 * @param piid
	 * @return
	 */
	public List<PrimaryDomain> getPrimaryDomainsByPiid(Long piid)
	{
		PrimaryDomain primaryDomain = new PrimaryDomain();
		primaryDomain.setPiid(piid);
		return primaryDomainMapper.selectSelective(primaryDomain);
	}
	
	/**
	 * 分页获取专家入住信息列表
	 * @param pageIndex
	 * @param pageSize
	 * @param auditStatus
	 * @return
	 */
	public List<PsychoInfo> getPsychoInfosWithPage(Integer pageIndex, Integer pageSize, Byte auditStatus)
	{
		PsychoInfo psychoInfo = new PsychoInfo();
		psychoInfo.setPageIndex(pageIndex);
		psychoInfo.setPageSize(pageSize);
		psychoInfo.setAuditStatus(auditStatus);
		
		return psychoInfoMapper.selectSelectiveWithPageOrderBy(psychoInfo);
	}
	
	/**
	 * 获取专家入住信息条数
	 * @param auditStatus
	 * @return
	 */
	public Integer countPsychoInfos(Byte auditStatus)
	{
		PsychoInfo psychoInfo = new PsychoInfo();
		psychoInfo.setAuditStatus(auditStatus);
		
		return psychoInfoMapper.countSelective(psychoInfo);
	}
	
	/**
	 * 分页获取未审核专家入住信息列表
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<PsychoInfo> getUnauditPsychoInfosWithPage(Integer pageIndex, Integer pageSize)
	{
		return getPsychoInfosWithPage(pageIndex, pageSize, AUDIT_STATUS_UNAUDIT);
	}
	
	/**
	 * 分页获取审核通过(专家库)专家入住信息列表
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<PsychoInfo> getPassPsychoInfosWithPage(Integer pageIndex, Integer pageSize)
	{
		return getPsychoInfosWithPage(pageIndex, pageSize, AUDIT_STATUS_PASS);
	}
	
	/**
	 * 分页获取审核未通过专家入住信息列表
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<PsychoInfo> getNogoPsychoInfosWithPage(Integer pageIndex, Integer pageSize)
	{
		return getPsychoInfosWithPage(pageIndex, pageSize, AUDIT_STATUS_NOGO);
	}
	
	/**
	 * 分页搜索专家入住信息列表
	 * @param words
	 * @param auditStatus
	 * @param pageStartNum
	 * @param pageSize
	 * @param createTimeDirection
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<PsychoInfo> search(String words, Byte auditStatus,
			Integer pageIndex, Integer pageSize, Byte createTimeDirection,
			Date begin, Date end)
	{
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		return psychoInfoMapper.search(words, auditStatus, page.getPageStartNum(), pageSize, createTimeDirection, begin, end);
	}
	
	/**
	 * 统计搜索专家入住信息条数
	 * @param words
	 * @param auditStatus
	 * @param begin
	 * @param end
	 * @return
	 */
	public Integer countSearch(String words, Byte auditStatus, 
			Date begin, Date end)
	{
		return psychoInfoMapper.countSearch(words, auditStatus, begin, end);
	}
	
	/**
	 * 根据主键获取专家入住信息
	 * @param piid
	 * @return
	 */
	public PsychoInfo getPsychoInfoByKey(Long piid)
	{
		return psychoInfoMapper.selectByPrimaryKey(piid);
	}
	
	/**
	 * 根据主键删除专家入住信息
	 * @param piid
	 * @return
	 */
	public Integer deletePsychoInfoByKey(Long piid)
	{
		return psychoInfoMapper.deleteByPrimaryKey(piid);
	}
	
	/**
	 * 修改审核状态
	 * @param auditStatus
	 * @param reason
	 * @return
	 */
	public Integer auditPsychoInfo(Long piid, Byte auditStatus, String reason)
	{
		PsychoInfo psychoInfo = new PsychoInfo();
		psychoInfo.setPiid(piid);
		psychoInfo.setAuditStatus(auditStatus);
		psychoInfo.setNogoReason(reason);		
		psychoInfo.setAuditTime( new Date());		
		return psychoInfoMapper.updateByPrimaryKeySelective(psychoInfo);
	}
	
	/**
	 * 根据手机号确认是否存在有效的入住信息(未审核或者审核通过)
	 * @param mobilePhone
	 * @return 1 存在 0 不存在
	 */
	public int checkValidPsychoInfo(String mobilePhone)
	{
		PsychoInfo psychoInfo = new PsychoInfo();
		psychoInfo.setMobilePhone(mobilePhone);
		psychoInfo.setAuditStatus(AUDIT_STATUS_UNAUDIT);
		if (psychoInfoMapper.countSelective(psychoInfo)!=0) return 1;
		
		psychoInfo = new PsychoInfo();
		psychoInfo.setMobilePhone(mobilePhone);
		psychoInfo.setAuditStatus(AUDIT_STATUS_PASS);
		if (psychoInfoMapper.countSelective(psychoInfo)!=0) return 1;
		return 0;
	}

	public PsychoInfo getPsychoInfoByMid(Long mid) {
		PsychoInfo pi=new PsychoInfo();
		pi.setMid(mid);
		List<PsychoInfo> list=psychoInfoMapper.selectSelective(pi);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
}
