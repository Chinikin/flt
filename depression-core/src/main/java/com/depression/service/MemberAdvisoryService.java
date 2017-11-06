package com.depression.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.depression.dao.AdvisoryTagMapper;
import com.depression.dao.MemberAdvisoryCurationImgsMapper;
import com.depression.dao.MemberAdvisoryCurationMapper;
import com.depression.dao.MemberAdvisoryMapper;
import com.depression.model.AdvisoryTag;
import com.depression.model.MemberAdvisory;
import com.depression.model.MemberAdvisoryCuration;
import com.depression.model.MemberAdvisoryCurationImgs;
import com.depression.model.api.dto.ApiMemberAdvisoryCurationDTO;
import com.depression.model.api.dto.ApiMemberAdvisoryCurationImgsDTO;
import com.depression.utils.Configuration;

/**
 * 精选问答
 * 
 * @author ax
 * 
 */
@Service
public class MemberAdvisoryService
{
	@Autowired
	private MemberAdvisoryMapper memberAdvisoryMapper;

	/**
	 * 根据用户id查询提问数量
	 * @param mid 用户Id
	 */
	
	public Integer countMemberAdvisoryByMid(Long mid){
		MemberAdvisory ma=new MemberAdvisory();
		ma.setMid(mid);
		return memberAdvisoryMapper.countSelective(ma);
	}
}
