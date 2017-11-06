package com.depression.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.AdStartupPageMapper;
import com.depression.model.AdStartupPage;

/**
 * 启动页
 * 
 * @author fanxinhui
 * @date 2016/11/22
 */
@Service
public class AdStartupPageService
{
	@Autowired
	private AdStartupPageMapper adStartupPageMapper;

	/**
	 * 分页数据
	 * 
	 * @param record
	 * @return
	 */
	public List<AdStartupPage> getPageList(AdStartupPage adStartupPage)
	{
		return this.adStartupPageMapper.selectSelectiveWithPage(adStartupPage);
	}

	/**
	 * 分页总条数
	 * 
	 * @param record
	 * @return
	 */
	public int getPageCounts(AdStartupPage adStartupPage)
	{
		return this.adStartupPageMapper.countSelective(adStartupPage);
	}
	
	/**
	 * 新增
	 * 
	 * @param record
	 * @return
	 */
	public int insert(AdStartupPage adStartupPage)
	{
		return this.adStartupPageMapper.insertSelective(adStartupPage);
	}
	
	/**
	 * 根据主键查询记录
	 * 
	 * @param bannerId
	 * @return
	 */
	public AdStartupPage selectByPrimaryKey(Long bannerId)
	{
		return this.adStartupPageMapper.selectByPrimaryKey(bannerId);
	}
	/**
	 * 更新
	 * @param record
	 * @return
	 */
	public int update(AdStartupPage adStartupPage)
	{
		return this.adStartupPageMapper.updateByPrimaryKeySelective(adStartupPage);
	}
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	public int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids)
	{
		return this.adStartupPageMapper.deleteByPrimaryKeyBulk(ids);
	}
	
	/**
     * 搜索列表
     * 
     * @param pageTitle 标题关键字
     * @param pageStartNum 
     * @param pageSize 
     * @param type 0.未开始，1.进行中，2.已结束
     * @param curDate 现在时间
     * @return
     */
    public List<AdStartupPage> searchAdStartupPageList(String pageTitle,Integer pageStartNum, Integer pageSize , 
			Byte type, Date curDate)
	{
    	return this.adStartupPageMapper.searchAdStartupPageList(pageTitle, pageStartNum, pageSize, type, curDate);
	}
    
    /**
     * 搜索列表条数
     * 
     * @param pageTitle 标题关键字
     * @param pageStartNum 
     * @param pageSize 
     * @param type 0.未开始，1.进行中，2.已结束
     * @param curDate 现在时间
     * @return
     */
	public Integer countAdStartupPageList(String pageTitle,Integer pageStartNum, Integer pageSize , 
			Byte type, Date curDate)
	{
		return this.adStartupPageMapper.countAdStartupPageList(pageTitle, pageStartNum, pageSize, type, curDate);
	}
	
	/**
	 * 查询进行中的广告启动页
	 * 
	 * @param curDate 现在时间
	 * @return
	 */
	public List<AdStartupPage> selectInProgressStartPage(@Param("curDate")Date curDate)
	{
		return this.adStartupPageMapper.selectInProgressStartPage(curDate);
	}
}
