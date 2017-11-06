package com.depression.controller.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.entity.VoipUser;
import com.depression.model.LicenseType;
import com.depression.model.Member;
import com.depression.model.NeteaseUinfoEx;
import com.depression.model.PrimaryDomain;
import com.depression.model.PsychoInfo;
import com.depression.model.api.dto.ApiLicenseTypeDTO;
import com.depression.model.web.dto.WebPsychoInfoDTO;
import com.depression.model.web.vo.WebPsychoInfoVO;
import com.depression.service.MemberService;
import com.depression.service.PsychoInfoService;
import com.depression.utils.Configuration;
import com.depression.utils.IMUtil;
import com.depression.utils.MD5Util;
import com.depression.utils.PropertyUtils;
import com.depression.utils.SmsUtil;

/**
 * 咨询师入住
 * @author caizj
 *
 */
@Controller
@RequestMapping("/PsychoInfo")
public class PsychoInfoController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	PsychoInfoService psychoInfoService;
	
	@Autowired
	MemberService memberService;
	
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception
	{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}
	
	/**
	 * 获取执照类型列表
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getLicenseTypeList.json")
	@ResponseBody
	public Object getLicenseTypeList()
	{
		ResultEntity result = new ResultEntity();
		
		List<LicenseType> licenseTypes;
		try{
			licenseTypes = psychoInfoService.getAllLicenseTypes();
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		List<ApiLicenseTypeDTO> apiLicenseTypeDTOs = new ArrayList<ApiLicenseTypeDTO>();
		for(LicenseType lt : licenseTypes)
		{
			ApiLicenseTypeDTO apiLicenseTypeDTO = new ApiLicenseTypeDTO();
			BeanUtils.copyProperties(lt, apiLicenseTypeDTO);
			apiLicenseTypeDTOs.add(apiLicenseTypeDTO);
		}
		
		result.put("licenseTypes", apiLicenseTypeDTOs);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	WebPsychoInfoDTO fillWebPsychoInfoDTO(PsychoInfo pi)
	{
		WebPsychoInfoDTO dto = new WebPsychoInfoDTO();
		BeanUtils.copyProperties(pi, dto);
		//处理照片路径
		dto.setPhotoCandid(pi.getPhotoCandidRel());
		dto.setPhotoCandidPreview(pi.getPhotoCandidPreviewRel());
		dto.setPhotoCertification(pi.getPhotoCertificationRel());
		dto.setPhotoCertificationPreview(pi.getPhotoCertificationPreviewRel());
		dto.setPhotoIdentityCard(pi.getPhotoIdentityCardRel());
		dto.setPhotoIdentityCardPreview(pi.getPhotoIdentityCardPreviewRel());
		
		dto.setPhotoAvatar(pi.getPhotoAvatarRel());
		dto.setPhotoAvatarPreview(pi.getPhotoAvatarPreviewRel());
		dto.setPhotoCandidDealt(pi.getPhotoCandidDealtRel());
		dto.setPhotoCandidDealtPreview(pi.getPhotoCandidDealtPreviewRel());
		dto.setPhotoCertificationDealt(pi.getPhotoCertificationDealtRel());
		dto.setPhotoCertificationDealtPreview(pi.getPhotoCertificationDealtPreviewRel());
		dto.setPhotoIdentityCardDealt(pi.getPhotoIdentityCardDealtRel());
		dto.setPhotoIdentityCardDealtPreview(pi.getPhotoIdentityCardDealtPreviewRel());
	
		//处理执照类型
		dto.setLicenseName(psychoInfoService.getLicenseTypeByPrimaryKey(pi.getLtid()).getLicenseName());
		
		//处理擅长领域
		List<String> domainStrs = new ArrayList<String>();
		List<PrimaryDomain> primaryDomains = psychoInfoService.getPrimaryDomainsByPiid(pi.getPiid());
		for(PrimaryDomain pd : primaryDomains)
		{
			domainStrs.add(pd.getDomainName());
		}
		dto.setPrimaryDomains(domainStrs.toArray(new String[domainStrs.size()]));
		
		return dto;
	}
	
	/**
	 * 搜索专家入住信息列表
	 * @param words
	 * @param auditStatus
	 * @param pageIndex
	 * @param pageSize
	 * @param createTimeDirection
	 * @param begin
	 * @param end
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/searchPsychoInfoList.json")
	@ResponseBody
	public Object searchPsychoInfoList(String words, Byte auditStatus,
			Integer pageIndex, Integer pageSize, Byte createTimeDirection,
			Date begin, Date end)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(pageIndex, pageSize, auditStatus)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<WebPsychoInfoDTO> psychoInfoDTOs = new ArrayList<WebPsychoInfoDTO>();
		Integer count;
		try{
			List<PsychoInfo> psychoInfos = psychoInfoService.search(words, auditStatus, pageIndex, pageSize, createTimeDirection, begin, end);
			count = psychoInfoService.countSearch(words, auditStatus, begin, end);
			
			for(PsychoInfo pi : psychoInfos)
			{
				WebPsychoInfoDTO dto = fillWebPsychoInfoDTO(pi);
				
				psychoInfoDTOs.add(dto);
			}
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("count", count);
		result.put("psychoInfos", psychoInfoDTOs);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 获取专家入住信息列表
	 * @param pageIndex
	 * @param pageSize
	 * @param auditStatus 0 未审核， 1 审核通过， 2审核不通过
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getPsychoInfoList.json")
	@ResponseBody
	public Object getPsychoInfoList(Integer pageIndex, Integer pageSize, Byte auditStatus)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(pageIndex, pageSize, auditStatus)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<WebPsychoInfoDTO> psychoInfoDTOs = new ArrayList<WebPsychoInfoDTO>();
		Integer count;
		try{
			List<PsychoInfo> psychoInfos = psychoInfoService.getPsychoInfosWithPage(pageIndex, pageSize, auditStatus);
			count = psychoInfoService.countPsychoInfos(auditStatus);
			
			for(PsychoInfo pi : psychoInfos)
			{
				WebPsychoInfoDTO dto = fillWebPsychoInfoDTO(pi);
				
				psychoInfoDTOs.add(dto);
			}
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("count", count);
		result.put("psychoInfos", psychoInfoDTOs);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	/**
	 * 根据主键获取专家入住信息
	 * @param piid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getPsychoInfoByKey.json")
	@ResponseBody
	public Object getPsychoInfoByKey(Long piid)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(piid)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		WebPsychoInfoDTO psychoInfoDTO = null;
		try{
			PsychoInfo psychoInfo = psychoInfoService.getPsychoInfoByKey(piid);
			
			if(psychoInfo != null)
			{
				psychoInfoDTO = fillWebPsychoInfoDTO(psychoInfo);
			}

		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("psychoInfo", psychoInfoDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 修改专家入住信息
	 * @param apiPsychoInfoVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/modifyPsychoInfo.json")
	@ResponseBody
	public Object modifyPsychoInfo(WebPsychoInfoVO webPsychoInfoVO)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(
				webPsychoInfoVO,
				webPsychoInfoVO.getPiid()
				))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		//填充信息
		PsychoInfo psychoInfo =  new PsychoInfo();
		BeanUtils.copyProperties(webPsychoInfoVO, psychoInfo);
		
		try{
			List<String> domains = JSONArray.parseArray(webPsychoInfoVO.getPrimaryDomains(), String.class);
			psychoInfoService.updatePsychoInfo(psychoInfo, domains);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 删除专家入住信息
	 * @param piid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/deletePsychoInfoByKey.json")
	@ResponseBody
	public Object deletePsychoInfoByKey(Long piid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(
				piid
				))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			psychoInfoService.deletePsychoInfoByKey(piid);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 审核专家入住信息，通过则创建咨询师账号
	 * @param piid
	 * @param auditStatus
	 * @param reason
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/auditPsychoInfo.json")
	@ResponseBody
	public Object auditPsychoInfo(Long piid, Byte auditStatus, String reason)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(
				piid, auditStatus
				))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		if(auditStatus == PsychoInfoService.AUDIT_STATUS_NOGO)
		{//审核不通过
			if(reason == null)
			{
				reason = "资料不完整";
			}
			
			psychoInfoService.auditPsychoInfo(piid, auditStatus, reason);
			PsychoInfo psychoInfo = psychoInfoService.getPsychoInfoByKey(piid);
			//TODO 发送通知短信

			SmsUtil.sendSms(psychoInfo.getMobilePhone(), "140583", psychoInfo.getName(), reason);
		}else if(auditStatus == PsychoInfoService.AUDIT_STATUS_PASS)
		{//审核通过

			//绑定或生成咨询师账户
			PsychoInfo psychoInfo = psychoInfoService.getPsychoInfoByKey(piid);
			Member queryMember = memberService.selectMemberByMobilePhone(psychoInfo.getMobilePhone());
			if(queryMember != null)
			{//已注册普通会员
				psychoInfoService.updateFieldMid(piid, queryMember.getMid());
				Member member = new Member();
				member.setMid(queryMember.getMid());
				member.setNickname(psychoInfo.getName());
				member.setTitle(psychoInfo.getTitle());
				member.setWorkYears(psychoInfo.getWorkYears());
				member.setCaseHours(psychoInfo.getCaseHours());
				member.setLtid(psychoInfo.getLtid());
				member.setProfile(psychoInfo.getBrief());
				member.setLocation(psychoInfo.getLocation());
				member.setAvatar(psychoInfo.getPhotoAvatarRel());
				member.setAvatarThumbnail(psychoInfo.getPhotoAvatarPreviewRel());
				member.setCandidPhoto(psychoInfo.getPhotoCandidDealtRel());
				member.setPhotoCertificationDealtRel(psychoInfo.getPhotoCertificationDealtRel());
				member.setPhotoCertificationDealtPreviewRel(psychoInfo.getPhotoCertificationDealtPreviewRel());
				member.setPhotoIdentityCardDealtRel(psychoInfo.getPhotoIdentityCardDealtRel());
				member.setPhotoCertificationDealtPreviewRel(psychoInfo.getPhotoCertificationDealtPreviewRel());
				member.setPrimaryDomain(psychoInfo.getPrimaryDomain());
				member.setWxAccount(psychoInfo.getWxAccount());
				//设置级别
				member.setUserType(MemberService.USER_TYPE_PSYCHO);
				member.setpLevel(psychoInfo.getpLevel());
				

				memberService.updateToPsycho(member);
				
				//网易名片
				Member m = memberService.selectMemberByMid(member.getMid());
				NeteaseUinfoEx uinfoEx = new NeteaseUinfoEx();
				uinfoEx.setMid(m.getMid());
				uinfoEx.setUserType(m.getUserType()==null?1:m.getUserType());
				uinfoEx.setTitle(m.getTitle()==null?"":m.getTitle());
				String icon = m.getAvatar()==null?"":m.getAvatar();
				IMUtil.neteaseUserUpdateUinfo(m.getImAccount(), m.getNickname(), icon, JSON.toJSONString(uinfoEx));
			
				//TODO 发送通知短信
				SmsUtil.sendSms(psychoInfo.getMobilePhone(), "158056", psychoInfo.getName());
			}else{//全新用户
				Member member = new Member();
				// 设置用户为正式会员
				member.setIsTemp((byte) 0);
				member.setMobilePhone(psychoInfo.getMobilePhone());
				member.setUserPassword(MD5Util.getMD5String("123456"));
						
				// 设置个人信息
				member.setNickname(psychoInfo.getName());
				member.setTitle(psychoInfo.getTitle());
				member.setWorkYears(psychoInfo.getWorkYears());
				member.setCaseHours(psychoInfo.getCaseHours());
				member.setLtid(psychoInfo.getLtid());
				member.setProfile(psychoInfo.getBrief());
				member.setLocation(psychoInfo.getLocation());
				member.setAvatar(psychoInfo.getPhotoAvatarRel());
				member.setAvatarThumbnail(psychoInfo.getPhotoAvatarPreviewRel());
				member.setCandidPhoto(psychoInfo.getPhotoCandidDealtRel());
				member.setPrimaryDomain(psychoInfo.getPrimaryDomain());
				member.setWxAccount(psychoInfo.getWxAccount());
				
				// 注册网易im账号
				String icon = member.getAvatar()==null?"":member.getAvatar();
				VoipUser voipUser = IMUtil.neteaseUserCreate(member.getNickname(), icon, null);
				if (null == voipUser)
				{
					member.setImAccount("");
					member.setImPsw("");
				}else
				{
					member.setImAccount(voipUser.getVoipAccount());
					member.setImPsw(voipUser.getVoipPwd());
				}
				
				//设置级别
				member.setUserType(MemberService.USER_TYPE_PSYCHO);
				member.setpLevel(psychoInfo.getpLevel());
				memberService.insert(member);

				memberService.updateToPsycho(member);
				psychoInfoService.updateFieldMid(piid, member.getMid());
				
				//网易名片
				NeteaseUinfoEx uinfoEx = new NeteaseUinfoEx();
				uinfoEx.setMid(member.getMid());
				uinfoEx.setUserType(member.getUserType()==null?1:member.getUserType());
				uinfoEx.setTitle(member.getTitle()==null?"":member.getTitle());
				IMUtil.neteaseUserUpdateUinfo(member.getImAccount(), null, null, JSON.toJSONString(uinfoEx));
				
				//TODO 发送通知短信
				SmsUtil.sendSms(psychoInfo.getMobilePhone(), "158055", psychoInfo.getName());
			}
			
			psychoInfoService.auditPsychoInfo(piid, auditStatus, null);

		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
}
