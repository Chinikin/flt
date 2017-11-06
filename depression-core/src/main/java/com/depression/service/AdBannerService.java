package com.depression.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.AdBannerMapper;
import com.depression.model.AdBanner;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * banner页
 * 
 * @author fanxinhui
 * @date 2016/11/21
 */
@Service
public class AdBannerService
{
	@Autowired
	private AdBannerMapper adBannerMapper;

	/**
	 * 分页数据
	 * 
	 * @param record
	 * @return
	 */
	public Page<AdBanner> getPageList(AdBanner record)
	{
		/*return this.adBannerMapper.selectSelectiveWithPage(record);*/
		PageHelper.startPage(record.getPageIndex(), record.getPageSize());
		return this.adBannerMapper.selectSelectiveWithPageCustom(record);
	}

	/**
	 * 分页总条数
	 * 
	 * @param record
	 * @return
	 */
	public int getPageCounts(AdBanner record)
	{
		return this.adBannerMapper.countSelective(record);
	}
	
	public List<AdBanner> selectSelective(AdBanner record)
	{
		return this.adBannerMapper.selectSelective(record);
	}
	
	/**
	 * 新增
	 * 
	 * @param record
	 * @return
	 */
	public int insert(AdBanner record)
	{
		return this.adBannerMapper.insertSelective(record);
	}
	
	/**
	 * 根据主键查询记录
	 * 
	 * @param bannerId
	 * @return
	 */
	public AdBanner selectByPrimaryKey(Long bannerId)
	{
		return this.adBannerMapper.selectByPrimaryKey(bannerId);
	}
	/**
	 * 更新
	 * @param record
	 * @return
	 */
	public int update(AdBanner record)
	{
		return this.adBannerMapper.updateByPrimaryKeySelective(record);
	}
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	public int deleteByPrimaryKeyBulk(List<Long> ids)
	{
		return this.adBannerMapper.deleteByPrimaryKeyBulk(ids);
	}
	
	/**
	 * 获取所有有效的banner
	 * 
	 * @return
	 */
	public List<AdBanner> selectAllBanner(Byte showLocation)
	{
		AdBanner record = new AdBanner();
		record.setShowLocation(showLocation);
		record.setIsEnable(new Byte("0"));
		return this.adBannerMapper.selectSelective(record);
	}

	public int deleteByPrimaryKey(Long bannerId) {
		return adBannerMapper.deleteByPrimaryKey(bannerId);
	}

	public List<AdBanner> selectIndexBannerInEap(Byte showLocation, Long releaseFrom) {
		return adBannerMapper.selectIndexBannerInEap(showLocation,releaseFrom);
	}
}
