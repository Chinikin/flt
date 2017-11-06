package com.depression.controller.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.entity.VoipUser;
import com.depression.model.LicenseType;
import com.depression.model.Member;
import com.depression.model.MemberTag;
import com.depression.model.NeteaseUinfoEx;
import com.depression.model.ServiceGoods;
import com.depression.model.api.dto.ApiLicenseTypeDTO;
import com.depression.model.api.dto.ApiPsychoDTO;
import com.depression.model.web.dto.WebMemberTagDTO;
import com.depression.model.web.dto.WebPsychoDTO;
import com.depression.model.web.vo.WebIdsVO;
import com.depression.model.web.vo.WebPsychoVO;
import com.depression.service.MemberService;
import com.depression.service.MemberTagService;
import com.depression.service.PsychoInfoService;
import com.depression.service.ServiceGoodsService;
import com.depression.utils.Configuration;
import com.depression.utils.IMUtil;
import com.depression.utils.MD5Util;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/Psychor")
public class PsychoController
{
    Logger log = Logger.getLogger(this.getClass());
	@Autowired
	MemberService memberService;
	@Autowired
	MemberTagService memberTagService;
	@Autowired
	ServiceGoodsService serviceGoodsService;
	@Autowired
	PsychoInfoService psychoInfoService;
	
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

	@RequestMapping(method = RequestMethod.POST, value = "/addMember.json")
	@ResponseBody
	public Object addMember(HttpSession session, HttpServletRequest request, WebPsychoVO psychoVO,
			WebIdsVO idsVo)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				psychoVO,
				psychoVO.getNickname(),
				psychoVO.getMobilePhone()
				//psychoVO.getUserPassword(),
				//psychoVO.getTitle(),
				//psychoVO.getAvatar(),
				//psychoVO.getCandidPhoto(),
				//psychoVO.getLocation(),
				//psychoVO.getProfile()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try
		{
			Integer preemption = memberService.insertPreemption(psychoVO.getMobilePhone());

			Member queryMember = memberService.selectMemberByMobilePhone(psychoVO.getMobilePhone());

			if(queryMember.getUserType() == 2)
			{
				result.setCode(ErrorCode.ERROR_MOBILEPHONE_EXISTED.getCode());
				result.setMsg(ErrorCode.ERROR_MOBILEPHONE_EXISTED.getMessage());
				return result;
			}
			
			Member member = new Member();
			member.setMid(queryMember.getMid());
			// 设置用户为正式会员
			if (1 == preemption)
			{//未注册过会员时
				member.setIsTemp((byte) 0);
				member.setUserPassword(MD5Util.getMD5String(psychoVO.getUserPassword()));
			}
			
			// 设置个人信息
			member.setNickname(psychoVO.getNickname());
			member.setTitle(psychoVO.getTitle());
			member.setProfile(psychoVO.getProfile());
			member.setLocation(psychoVO.getLocation());
			member.setAvatar(psychoVO.getAvatar());
			member.setAvatarThumbnail(psychoVO.getAvatarThumbnail());
			member.setCandidPhoto(psychoVO.getCandidPhoto());
			member.setLtid(psychoVO.getLtid());
			member.setSex(psychoVO.getSex());
			member.setpLevel(psychoVO.getpLevel());
			member.setWxAccount(psychoVO.getWxAccount());
			member.setWorkYears(psychoVO.getWorkYears());
			member.setCaseHours(psychoVO.getCaseHours());
			member.setPrimaryDomain(psychoVO.getPrimaryDomain());
			member.setPhotoCertificationDealtPreviewRel(psychoVO.getPhotoCertificationDealtPreviewRel());
			member.setPhotoCertificationDealtRel(psychoVO.getPhotoCertificationDealtRel());
			member.setPhotoIdentityCardDealtPreviewRel(psychoVO.getPhotoIdentityCardDealtPreviewRel());
			member.setPhotoIdentityCardDealtRel(psychoVO.getPhotoIdentityCardDealtRel());
			
			// 注册网易im账号
			if (1 == preemption)
			{//未注册过会员时
				String icon = member.getAvatar()==null?"": member.getAvatar();
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
			}
			
			memberService.updateToPsycho(member);
			//添加标签
			for(Long id: idsVo.getIds())
			{
				memberTagService.insertNewTagMap(member.getMid(), id);
			}
			
			//网易名片
			NeteaseUinfoEx uinfoEx = new NeteaseUinfoEx();
			uinfoEx.setMid(member.getMid());
			uinfoEx.setUserType(member.getUserType()==null?1:member.getUserType());
			uinfoEx.setTitle(member.getTitle()==null?"":member.getTitle());
			String icon = member.getAvatar()==null?"": member.getAvatar();
			IMUtil.neteaseUserUpdateUinfo(member.getImAccount(), member.getNickname(), icon, JSON.toJSONString(uinfoEx));

			result.setCode(ErrorCode.SUCCESS.getCode());
			result.setMsg(ErrorCode.SUCCESS.getMessage());
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			log.error(ErrorCode.ERROR_SYSTEM_IO.getMessage(), e);
			result.setCode(ErrorCode.ERROR_SYSTEM_IO.getCode());
			result.setMsg(ErrorCode.ERROR_SYSTEM_IO.getMessage());
			return result;
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/updateMember.json")
	@ResponseBody
	public Object updateMember(HttpSession session, HttpServletRequest request, WebPsychoVO psychoVO,
			WebIdsVO idsVO)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				psychoVO,
				psychoVO.getMid()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try
		{
			
			Member queryMember = memberService.selectMemberByMid(psychoVO.getMid());
			if(queryMember == null)
			{
				result.setCode(ErrorCode.ERROR_USER_INEXISTENT.getCode());
				result.setMsg(ErrorCode.ERROR_USER_INEXISTENT.getMessage());
				return result;
			}

			Member member = new Member();
			// 设置用户
			member.setMid(psychoVO.getMid());
			if(psychoVO.getUserPassword()!=null && psychoVO.getUserPassword()!="")
			{
				if (!psychoVO.getUserPassword().equals(queryMember.getUserPassword()))
				{
					member.setUserPassword(MD5Util.getMD5String(psychoVO.getUserPassword()));
				}	 
			}
			
			// 设置个人信息
			member.setNickname(psychoVO.getNickname());
			member.setMobilePhone(psychoVO.getMobilePhone());
			member.setTitle(psychoVO.getTitle());
			member.setProfile(psychoVO.getProfile());
			member.setLocation(psychoVO.getLocation());
			member.setAvatar(psychoVO.getAvatar());
			member.setAvatarThumbnail(psychoVO.getAvatarThumbnail());
			member.setCandidPhoto(psychoVO.getCandidPhoto());
			member.setLtid(psychoVO.getLtid());
			member.setSex(psychoVO.getSex());
			member.setpLevel(psychoVO.getpLevel());
			member.setWxAccount(psychoVO.getWxAccount());
			member.setWorkYears(psychoVO.getWorkYears());
			member.setCaseHours(psychoVO.getCaseHours());
			member.setPrimaryDomain(psychoVO.getPrimaryDomain());
			member.setPhotoCertificationDealtPreviewRel(psychoVO.getPhotoCertificationDealtPreviewRel());
			member.setPhotoCertificationDealtRel(psychoVO.getPhotoCertificationDealtRel());
			member.setPhotoIdentityCardDealtPreviewRel(psychoVO.getPhotoIdentityCardDealtPreviewRel());
			member.setPhotoIdentityCardDealtRel(psychoVO.getPhotoIdentityCardDealtRel());
			
			memberService.update(member);
			
			//修改网易im信息
			if(psychoVO.getAvatar()!=null || psychoVO.getNickname()!=null)
			{
				Member m = memberService.selectMemberByMid(psychoVO.getMid());
				String icon = m.getAvatar()==null?"":m.getAvatar();
				String ex = null;
				if(psychoVO.getTitle()!=null)
				{
					NeteaseUinfoEx uinfoEx = new NeteaseUinfoEx();
					uinfoEx.setMid(m.getMid());
					uinfoEx.setUserType(m.getUserType()==null?1:m.getUserType());
					uinfoEx.setTitle(m.getTitle()==null?"":m.getTitle());
					ex = JSON.toJSONString(uinfoEx);
				}
				IMUtil.neteaseUserUpdateUinfo(m.getImAccount(), m.getNickname(), icon, ex);
			}
			
			//更新标签
			memberTagService.removeAllTagMap(member.getMid());
			for(Long id: idsVO.getIds())
			{
				memberTagService.insertNewTagMap(member.getMid(), id);
			}

			result.setCode(ErrorCode.SUCCESS.getCode());
			result.setMsg(ErrorCode.SUCCESS.getMessage());
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			log.error(ErrorCode.ERROR_SYSTEM_IO.getMessage(), e);
			result.setCode(ErrorCode.ERROR_SYSTEM_IO.getCode());
			result.setMsg(ErrorCode.ERROR_SYSTEM_IO.getMessage());
			return result;
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/deleteMemberBulk.json")
	@ResponseBody
	public Object deleteMemberBulk(HttpSession session, HttpServletRequest request, @RequestParam("ids[]") Long[] ids)
	{
		ResultEntity result = new ResultEntity();
		if(ids==null){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{//暂时禁用删除操作，避免风险
			//memberService.deleteMemberBulk(Arrays.asList(ids));
		}catch(Exception e){
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;

	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/enableMemberBulk.json")
	@ResponseBody
	public Object enableMemberBulk(HttpSession session, HttpServletRequest request, WebIdsVO pIds)
	{
		ResultEntity result = new ResultEntity();
		if(pIds==null){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			memberService.enableMemberBulk(pIds.getIds());
		}catch(Exception e){
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;

	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/disableMemberBulk.json")
	@ResponseBody
	public Object disableMemberBulk(HttpSession session, HttpServletRequest request, WebIdsVO pIds)
	{
		ResultEntity result = new ResultEntity();
		if(pIds==null){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			memberService.disableMemberBulk(pIds.getIds());
		}catch(Exception e){
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;

	}

	private void member2PsychoDTO(Member m, WebPsychoDTO p)
	{
		BeanUtils.copyProperties(m, p);
		
		p.setAvatar(m.getAvatar());
		p.setAvatarAbs(m.getAvatar());
		p.setAvatarThumbnail(m.getAvatarThumbnail());
		p.setAvatarThumbnailAbs(m.getAvatarThumbnail());
		p.setCandidPhoto(m.getCandidPhoto());
		p.setCandidPhotoAbs(m.getCandidPhoto());
		if(m.getPhotoCertificationDealtPreviewRel() != null)
		{
			p.setPhotoCertificationDealtPreviewAbs(m.getPhotoCertificationDealtPreviewRel());
		}
		if(m.getPhotoCertificationDealtRel() != null)
		{
			p.setPhotoCertificationDealtAbs(m.getPhotoCertificationDealtRel());
		}
		if(m.getPhotoIdentityCardDealtPreviewRel() != null)
		{
			p.setPhotoIdentityCardDealtPreviewAbs(m.getPhotoIdentityCardDealtPreviewRel());
		}
		if(m.getPhotoIdentityCardDealtRel() != null)
		{
			p.setPhotoIdentityCardDealtAbs(m.getPhotoIdentityCardDealtRel());
		}
		
		//查询tags
		List<WebMemberTagDTO> psychoTagDTOs = new ArrayList<WebMemberTagDTO>();
		for(MemberTag mt : memberTagService.getTagList(m.getMid()))
		{
			WebMemberTagDTO tagDTO = new WebMemberTagDTO();
			BeanUtils.copyProperties(mt, tagDTO);
			psychoTagDTOs.add(tagDTO);
		}
		p.setTags(psychoTagDTOs);
		
		// 查询语音服务信息
		ServiceGoods serviceGood = serviceGoodsService.selectImmVoiceByMid(m.getMid());
		if (serviceGood != null)
		{
			p.setPhoneCount(serviceGood.getTimes());
			p.setPhoneCountShow(serviceGood.getTimes() + m.getPhoneCountIncrement());
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getAllMember.json")
	@ResponseBody
	public Object getAllMember(HttpSession session, HttpServletRequest request, 
			Integer pageIndex, Integer pageSize, Integer byRegTime)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				pageIndex,
				pageSize
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<Member> members;
		try{
			if(byRegTime!=null && byRegTime==1)
			{
				members = memberService.getPsychosOrderRegTimeWithPageDesc(pageIndex, pageSize);
			}else if(byRegTime!=null && byRegTime==0){
				members = memberService.getPsychosOrderRegTimeWithPageAsc(pageIndex, pageSize);
			}else{			
				members = memberService.getPsychosOrderAnswerCountWithPage(pageIndex, pageSize);
			}
		}catch(Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		List<WebPsychoDTO> psychoDTOs = new ArrayList<WebPsychoDTO>();
		for(Member m:members)
		{
			WebPsychoDTO psychoDTO = new WebPsychoDTO();
			member2PsychoDTO(m, psychoDTO);
			
			psychoDTOs.add(psychoDTO);
		}
		
		result.put("psychoDTOs", psychoDTOs);
		result.put("count", memberService.getPsychosCount());
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getMember.json")
	@ResponseBody
	public Object getMember(HttpSession session, HttpServletRequest request, Long id)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				id
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		Member member;
		try{
			member = memberService.selectMemberByMid(id);
		}catch(Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		WebPsychoDTO psychoDTO = new WebPsychoDTO();
		member2PsychoDTO(member, psychoDTO);
		
		result.put("psychoDTO", psychoDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getMemberListByTagId.json")
	@ResponseBody
	public Object getMemberListByTagId(HttpSession session, HttpServletRequest request, 
			Integer pageIndex, Integer pageSize, Long tagId)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				pageIndex,
				pageSize,
				tagId
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<Long> ids = new ArrayList<Long>();
		try{
			ids = memberTagService.getMemberIdsByTag(tagId);
		}catch(Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		List<Member> members;
		try{
			members = memberService.getByKeysOrderAnswerCountWithPage(ids, pageIndex, pageSize);
		}catch(Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		List<WebPsychoDTO> psychoDTOs = new ArrayList<WebPsychoDTO>();
		for(Member m:members)
		{
			//拷贝属性
			WebPsychoDTO psychoDTO = new WebPsychoDTO();
			member2PsychoDTO(m, psychoDTO);
			
			psychoDTOs.add(psychoDTO);
		}
		
		result.put("psychoDTOs", psychoDTOs);
		result.put("count", ids.size());
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 原先只匹配标签，扩展根据描述语进行模糊手机号，昵称等
	 * @param session
	 * @param request
	 * @param pageIndex
	 * @param pageSize
	 * @param phrase
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/searchMember.json")
	@ResponseBody
	public Object searchMember(HttpSession session, HttpServletRequest request, 
			Integer pageIndex, Integer pageSize, String words, Integer regTimeDirection, 
			Date begin, Date end, Byte isAudited)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				pageIndex,
				pageSize
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<Member> members;
		try{
			//模糊搜索
			members = memberService.searchPsychos(words, pageIndex, pageSize, regTimeDirection,
					begin, end, isAudited);
		}catch(Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		List<WebPsychoDTO> psychoDTOs = new ArrayList<WebPsychoDTO>();
		for(Member m:members)
		{
			//拷贝属性
			WebPsychoDTO psychoDTO = new WebPsychoDTO();


			member2PsychoDTO(m, psychoDTO);
			
			psychoDTOs.add(psychoDTO);
		}
		
		result.put("psychoDTOs", psychoDTOs);
		result.put("count", memberService.countSearchPsychos(words, begin, end, isAudited));
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}	
	
	@RequestMapping(method = RequestMethod.POST, value = "/getMemberListByTimeSlice.json")
	@ResponseBody
	public Object getMemberListByTimeSlice(HttpSession session, HttpServletRequest request, 
			Integer pageIndex, Integer pageSize, Date begin, Date end)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				pageIndex,
				pageSize
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<Member> members;
		Integer count;
		try{
			members = memberService.selectPsychosByTimeSliceWithPage(pageIndex, pageSize, begin, end);
			count = memberService.countPsychosByTimeSlice(begin, end);
		}catch(Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		List<WebPsychoDTO> psychoDTOs = new ArrayList<WebPsychoDTO>();
		for(Member m:members)
		{
			WebPsychoDTO psychoDTO = new WebPsychoDTO();
			member2PsychoDTO(m, psychoDTO);		
			psychoDTOs.add(psychoDTO);
		}
		
		result.put("psychoDTOs", psychoDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 增加电话咨询增量，
	 * @param mid 咨询师id
	 * @param increment 本次刷单增量
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/addPhoneCountIncrement.json")
	@ResponseBody
	public Object addPhoneCountIncrement(Long mid, Integer increment)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				mid,
				increment
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			Member m = memberService.selectMemberByMid(mid);
			if(m != null)
			{
				Member member = new Member();
				member.setMid(mid);
				member.setPhoneCountIncrement(m.getPhoneCountIncrement() + increment);
				
				memberService.update(member);
			}
		}catch(Exception e){
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
	 * 审核Eap企业的咨询师入住心猫
	 * @param pid
	 * @param isPass
	 * @param reason
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/auditEapPsycho.json")
	@ResponseBody
	public Object auditEapPsychoV1(Long pid, Byte isPass, String reason)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				pid,
				isPass
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			Member psycho = memberService.selectMemberByMid(pid);
			if(isPass == 0)
			{//通过
				psycho.setIsAudited((byte) 0);
				//通知咨询师和企业
			}else
			{//不通过
				psycho.setIsAudited((byte) 1);
				//通知咨询师和企业
			}
			
			memberService.update(psycho);
		}catch(Exception e){
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
}
