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
import com.depression.model.AdvisoryTag;
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
public class MemberAdvisoryCurationService
{
	@Autowired
	private MemberAdvisoryCurationMapper memberAdvisoryCurationMapper;

	@Autowired
	private MemberAdvisoryCurationImgsMapper memberAdvisoryCurationImgsMapper;

	@Autowired
	private AdvisoryTagMapper advisoryTagMapper;

	/**
	 * 添加精品问答信息
	 * 
	 * @param memberAdvisoryCuration
	 * @param tagId
	 * @param jsonForImgs
	 * @return
	 */
	public int insertMemberAdvisoryCuration(MemberAdvisoryCuration memberAdvisoryCuration, String jsonForImgs)
	{
		// 添加问答记录
		memberAdvisoryCurationMapper.insertSelective(memberAdvisoryCuration);
		Long askId = memberAdvisoryCuration.getAskId();

		// 添加图片
		if (null != jsonForImgs && jsonForImgs.length() > 0)
		{
			JSONArray jsonArr = new JSONArray();
			jsonArr = JSON.parseArray(jsonForImgs);
			for (int idx = 0; idx < jsonArr.size(); idx++)
			{
				JSONObject obj = (JSONObject) jsonArr.get(idx);
				String imgPath = obj.getString("imgPath");
				String imgPreviewPath = obj.getString("imgPath");
				MemberAdvisoryCurationImgs memberAdvisoryCurationImgs = new MemberAdvisoryCurationImgs();
				memberAdvisoryCurationImgs.setImgPath(imgPath);
				memberAdvisoryCurationImgs.setImgPreviewPath(imgPreviewPath);
				memberAdvisoryCurationImgs.setAskId(askId);
				memberAdvisoryCurationImgsMapper.insertSelective(memberAdvisoryCurationImgs);
			}

		}

		return 0;
	}

	/**
	 * 更新精品问答信息
	 * 
	 * @param memberAdvisoryCuration
	 * @param tagId
	 * @param jsonForImgs
	 * @return
	 */
	public int updateMemberAdvisoryCuration(MemberAdvisoryCuration memberAdvisoryCuration, String jsonForImgs)
	{
		if (memberAdvisoryCuration.getAskId() != null)
		{
			// 更新问答记录
			memberAdvisoryCurationMapper.updateByPrimaryKeySelective(memberAdvisoryCuration);
			Long askId = memberAdvisoryCuration.getAskId();

			// 删除已存在的图片记录
			MemberAdvisoryCurationImgs queryMemberAdvisoryCurationImgs = new MemberAdvisoryCurationImgs();
			queryMemberAdvisoryCurationImgs.setAskId(askId);
			List<MemberAdvisoryCurationImgs> imgList = memberAdvisoryCurationImgsMapper.selectSelective(queryMemberAdvisoryCurationImgs);
			for (MemberAdvisoryCurationImgs img : imgList)
			{
				memberAdvisoryCurationImgsMapper.deleteByPrimaryKey(img.getAdvisoryImgId());
			}

			// 添加图片记录
			if (null != jsonForImgs && jsonForImgs.length() > 0)
			{
				JSONArray jsonArr = new JSONArray();
				jsonArr = JSON.parseArray(jsonForImgs);
				for (int idx = 0; idx < jsonArr.size(); idx++)
				{
					JSONObject obj = (JSONObject) jsonArr.get(idx);
					String imgPath = obj.getString("imgPath");
					String imgPreviewPath = obj.getString("imgPath");
					MemberAdvisoryCurationImgs memberAdvisoryCurationImgs = new MemberAdvisoryCurationImgs();
					memberAdvisoryCurationImgs.setImgPath(imgPath);
					memberAdvisoryCurationImgs.setImgPreviewPath(imgPreviewPath);
					memberAdvisoryCurationImgs.setAskId(askId);
					memberAdvisoryCurationImgsMapper.insertSelective(memberAdvisoryCurationImgs);
				}

			}
		} else
		{
			return -1;
		}

		return 0;
	}

	public Integer enableAdvisoryCurationBulk(List<Long> ids)
	{
		if (ids.size() > 0)
		{
			return memberAdvisoryCurationMapper.enableByPrimaryKeyBulk(ids, new Byte("0"));
		} else
		{
			return 0;
		}
	}

	public Integer disableAdvisoryCurationBulk(List<Long> ids)
	{
		if (ids.size() > 0)
		{
			return memberAdvisoryCurationMapper.enableByPrimaryKeyBulk(ids, new Byte("1"));
		} else
		{
			return 0;
		}
	}

	/**
	 * 检查标签是否存在
	 * 
	 * @param tagId
	 * @return
	 */
	public boolean checkTagExist(Long tagId)
	{
		AdvisoryTag advisoryTag = advisoryTagMapper.selectByPrimaryKey(tagId);
		if (advisoryTag != null && advisoryTag.getIsEnable().intValue() == 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * 精选问答分页查询列表(根据标签id和时间区间)
	 * 
	 * @param tagId
	 * @param pageStartNum
	 * @param pageSize
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<MemberAdvisoryCuration> selectCurationByTimeSliceWithPage(Long tagId, Integer pageStartNum, Integer pageSize, Date begin, Date end)
	{
		return memberAdvisoryCurationMapper.selectCurationByTimeSliceWithPage(tagId, pageStartNum, pageSize, begin, end);
	}

	/**
	 * 精选问答分页查询条数(根据标签id和时间区间)
	 * 
	 * @param tagId
	 * @param begin
	 * @param end
	 * @return
	 */
	public Integer countCurationByTimeSlice(Long tagId, Date begin, Date end)
	{
		return memberAdvisoryCurationMapper.countCurationByTimeSlice(tagId, begin, end);
	}

	/**
	 * 精选问答分页查询列表(根据标签id)
	 * 
	 * @param tagId
	 * @param pageStartNum
	 * @param pageSize
	 * @return
	 */
	public List<MemberAdvisoryCuration> selectCurationByTagIdWithPage(Long tagId, Integer pageStartNum, Integer pageSize)
	{
		return memberAdvisoryCurationMapper.selectCurationByTagIdWithPage(tagId, pageStartNum, pageSize);
	}

	/**
	 * 精选问答分页查询条数(根据标签id)
	 * 
	 * @param tagId
	 * @return
	 */
	public Integer countCurationByTagId(Long tagId)
	{
		return memberAdvisoryCurationMapper.countCurationByTagId(tagId);
	}

	/**
	 * 精选问答详情
	 * 
	 * @param tagId
	 * @return
	 */
	public ApiMemberAdvisoryCurationDTO getMemberAdvisoryCurationDetail(Long askId)
	{
		MemberAdvisoryCuration memberAdvisoryCuration = memberAdvisoryCurationMapper.selectByPrimaryKey(askId);
		ApiMemberAdvisoryCurationDTO apiMemberAdvisoryCurationDTO = new ApiMemberAdvisoryCurationDTO();
		BeanUtils.copyProperties(memberAdvisoryCuration, apiMemberAdvisoryCurationDTO);

		MemberAdvisoryCurationImgs MemberAdvisoryCurationImgs = new MemberAdvisoryCurationImgs();
		MemberAdvisoryCurationImgs.setAskId(askId);
		List<MemberAdvisoryCurationImgs> imgList = memberAdvisoryCurationImgsMapper.selectSelective(MemberAdvisoryCurationImgs);
		List<ApiMemberAdvisoryCurationImgsDTO> imgListDTO = new ArrayList<ApiMemberAdvisoryCurationImgsDTO>();
		for (MemberAdvisoryCurationImgs memberAdvisoryCurationImgs : imgList)
		{
			ApiMemberAdvisoryCurationImgsDTO apiMemberAdvisoryCurationImgsDTO = new ApiMemberAdvisoryCurationImgsDTO();
			BeanUtils.copyProperties(memberAdvisoryCurationImgs, apiMemberAdvisoryCurationImgsDTO);
			imgListDTO.add(apiMemberAdvisoryCurationImgsDTO);
		}
		apiMemberAdvisoryCurationDTO.setImgList(imgListDTO);

		return apiMemberAdvisoryCurationDTO;
	}
	
	/**
	 * 根据主键查询
	 * 
	 * @param askId
	 * @return
	 */
	public MemberAdvisoryCuration selectByPrimaryKey(Long askId)
	{
		return memberAdvisoryCurationMapper.selectByPrimaryKey(askId);
	}
	
	/**
	 * 根据主键更新
	 * @param record
	 * @return
	 */
	public int updateByPrimaryKeySelective(MemberAdvisoryCuration record)
	{
		return memberAdvisoryCurationMapper.updateByPrimaryKeySelective(record);
	}
	
	/**
	 * 查询图片列表
	 * 
	 * @param tagId
	 * @return
	 */
	public List<MemberAdvisoryCurationImgs> getAdvisoryCurationImgList(Long askId)
	{
		MemberAdvisoryCurationImgs MemberAdvisoryCurationImgs = new MemberAdvisoryCurationImgs();
		MemberAdvisoryCurationImgs.setAskId(askId);
		List<MemberAdvisoryCurationImgs> imgList = memberAdvisoryCurationImgsMapper.selectSelective(MemberAdvisoryCurationImgs);
		return imgList;
	}
}
