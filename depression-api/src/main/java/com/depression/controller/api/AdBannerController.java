package com.depression.controller.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.AdBanner;
import com.depression.model.Member;
import com.depression.model.MemberAdvisoryComment;
import com.depression.model.ServiceOrder;
import com.depression.model.Testing;
import com.depression.model.api.dto.ApiServiceBroadcastDTO;
import com.depression.model.web.dto.AdBannerDTO;
import com.depression.service.AdBannerService;
import com.depression.service.AdvisoryService;
import com.depression.service.MemberService;
import com.depression.service.ServiceOrderService;
import com.depression.service.TestingService;
import com.depression.utils.PropertyUtils;
import com.github.pagehelper.Page;

/**
 * banner
 * 
 * @author fanxinhui
 * 
 */
@Controller
@RequestMapping("/adBanner")
public class AdBannerController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private AdBannerService adBannerService;
	
	@Autowired
	private TestingService testingService;
	@Autowired	
	ServiceOrderService serviceOrderService;
	@Autowired		
	MemberService memberService;
	@Autowired
	AdvisoryService advisoryService;

	/**
	 * 分页列表
	 * 
	 * @param request
	 * @param modelMap
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/list.json")
	@ResponseBody
	public Object list(HttpServletRequest request, Byte showLocation,Long releaseFrom)
	{
		ResultEntity result = new ResultEntity();
		
		//默认返回首页，兼容老版本
		if(showLocation==null)
		{
			showLocation = 0;
		}
		
		if (PropertyUtils.examineOneNull(showLocation))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		if(releaseFrom == null ){
			releaseFrom = 0L;
		}
		
		List<AdBannerDTO> dtoList = new ArrayList<AdBannerDTO>();
		List<AdBanner> list = new Page<AdBanner>();
		if(showLocation == 0){
			// 查询首页
			list = adBannerService.selectIndexBannerInEap(showLocation,releaseFrom);
		}else{
			//查询eap自定义的banner
			AdBanner query = new AdBanner();
			query.setReleaseFrom(releaseFrom);
			query.setShowLocation(showLocation);
			query.setIsEnable((byte)0);
			list = adBannerService.selectSelective(query);
			
			//如果有则直接返回 没有显示平台的图
			if(list.size() == 0){
				query.setReleaseFrom(0L);
				query.setShowLocation(showLocation);
				query.setIsEnable((byte)0);
				list = adBannerService.selectSelective(query);
			}
		}
		
		
		
		for (AdBanner adBanner : list)
		{
			AdBannerDTO adBannerDTO = new AdBannerDTO();
			BeanUtils.copyProperties(adBanner, adBannerDTO);
			// 转换实际文件路径
			if (adBanner.getPicPath() != null && !adBanner.getPicPath().equals(""))
			{
				adBannerDTO.setFilePath(adBanner.getPicPath());
			}
			
			if(adBanner.getInsideLinkType() == new Byte("2"))
			{
				if(adBanner.getInsideLinkId() != null)
				{
					Testing testing = testingService.getTestingById(adBanner.getInsideLinkId());
					if (testing != null)
					{
						adBannerDTO.setCalcMethod4Testing(testing.getCalcMethod());
					}
				}
			}
			dtoList.add(adBannerDTO);
		}
		result.put("list", dtoList);
		result.put("count", dtoList.size());

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 获取订单播报条码，取15条
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/obtainServiceBroadcasts.json")
	@ResponseBody
	public Object obtainServiceBroadcastsV1()
	{
		ResultEntity result = new ResultEntity();
		List<ApiServiceBroadcastDTO> broadcastDTOs = new ArrayList<ApiServiceBroadcastDTO>();
		try{
			//订单
			List<ServiceOrder> sos = serviceOrderService.search(null, null, 1, 15, (byte) 1, null, null);
			for(ServiceOrder so : sos)
			{
				ApiServiceBroadcastDTO broadcastDTO = new ApiServiceBroadcastDTO();
				Member user = memberService.selectMemberByMid(so.getMid());
				Member psycho = memberService.selectMemberByMid(so.getServiceProviderId());
				if(user==null || psycho==null)
				{
					continue;
				}
				String userName = user.getNickname();
				String bc = String.format("咨询师%s收到%s****的语音咨询", psycho.getNickname(), 
						userName.substring(0, userName.length()/2==0?1:userName.length()/2));
				broadcastDTO.setPhrase(bc);
				broadcastDTO.setTime(so.getCreateTime());
				
				broadcastDTOs.add(broadcastDTO);
			}
			//提问
			List<MemberAdvisoryComment> macs = advisoryService.obtainComments3Page0TmDesc(null, 0L, 1, 15);
			for(MemberAdvisoryComment mac : macs)
			{
				ApiServiceBroadcastDTO broadcastDTO = new ApiServiceBroadcastDTO();
				Member psycho = memberService.selectMemberByMid(mac.getMid());
				if(psycho==null || psycho.getUserType()!=2)
				{
					continue;
				}
				String bc = String.format("咨询师%s回答匿名用户的提问", psycho.getNickname());
				broadcastDTO.setPhrase(bc);
				broadcastDTO.setTime(mac.getCommentTime());
				
				broadcastDTOs.add(broadcastDTO);
			}
			//倒序
			Collections.sort(broadcastDTOs);
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("broadcastDTOs", broadcastDTOs);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

}
