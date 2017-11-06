package com.depression.controller.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.UbViewHomepage;
import com.depression.model.api.vo.UbViewHomepageVO;
import com.depression.service.IMMessageService;
import com.depression.service.MemberService;
import com.depression.service.PushService;
import com.depression.service.SystemMessageService;
import com.depression.service.UbViewHomepageService;
import com.depression.utils.PropertyUtils;

/**
 * @author:ziye_huang
 * @date:2016年5月5日
 */

@Controller
@RequestMapping("/UbViewHomepage")
public class UbViewHomepageController
{
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	MemberService memberService;
	@Autowired
	SystemMessageService systemMessageService;
	@Autowired
	PushService pushService;
	@Autowired
	IMMessageService imMessageService;
	@Autowired
	UbViewHomepageService ubViewHomepageService;
	

	/**
	 * 
	 * @param session
	 * @param request
	 * @param ubViewHomepageVO
	 * @return
	 */
	@RequestMapping(value = "/addUbViewHomepage.json")
	@ResponseBody
	public Object viewMember(HttpSession session, HttpServletRequest request, UbViewHomepageVO ubViewHomepageVO){
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(
				ubViewHomepageVO.getViewFrom(), 
				ubViewHomepageVO.getViewTo()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
		}
		UbViewHomepage ubViewHomepage=new UbViewHomepage();
		BeanUtils.copyProperties(ubViewHomepageVO, ubViewHomepage);
		try{	
			ubViewHomepageService.addUbViewHomepage(ubViewHomepage);
		} catch (Exception e){
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用关注接口失败");
			return result;
		}
		result.setCode(ResultEntity.SUCCESS);
		result.setMsg("浏览主页成功");
		return result;
	}

}
