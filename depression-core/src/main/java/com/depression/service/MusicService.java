package com.depression.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.MusicClassDailyStatisMapper;
import com.depression.dao.MusicClassMapper;
import com.depression.dao.MusicSongDailyStatisMapper;
import com.depression.dao.MusicSongMapper;
import com.depression.model.MusicClass;
import com.depression.model.MusicClassDailyStatis;
import com.depression.model.MusicSong;
import com.depression.model.MusicSongDailyStatis;
import com.depression.model.Page;

@Service
public class MusicService
{
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	MusicClassMapper classMapper;
	@Autowired	
	MusicClassDailyStatisMapper classDailyStatisMapper;
	@Autowired	
	MusicSongMapper songMapper;
	@Autowired	
	MusicSongDailyStatisMapper songDailyStatisMapper;
	
	/**
	 * 创建音乐分类
	 * 	默认禁用
	 * @param musicClass 
	 * @return
	 */
	public Integer createMusicClass(MusicClass musicClass)
	{
		musicClass.setCreateTime(new Date());
		musicClass.setIsEnable((byte)1);
		
		return classMapper.insertSelective(musicClass);
	}
	
	/**
	 * 修改音乐分类
	 * @param musicClass
	 * @return
	 */
	public Integer modifyMusicClass(MusicClass musicClass)
	{
		musicClass.setModifyTime(new Date());
		
		return classMapper.updateByPrimaryKeySelective(musicClass);
	}
	
	/**
	 * 获取音乐分类列表
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<MusicClass> obtainMusicClasses(Integer pageIndex, Integer pageSize)
	{
		MusicClass musicClass = new MusicClass();
		musicClass.setPageIndex(pageIndex);
		musicClass.setPageSize(pageSize);
		
		return classMapper.selectSelectiveWithPage(musicClass);
	}
	
	public Integer countMusicClasses()
	{
		MusicClass musicClass = new MusicClass();
		return classMapper.countSelective(musicClass);
	}
	
	
	/**
	 * 获取启用的音乐分类列表
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<MusicClass> obtainMusicClassesEnabled(Integer pageIndex, Integer pageSize)
	{
		MusicClass musicClass = new MusicClass();
		musicClass.setPageIndex(pageIndex);
		musicClass.setPageSize(pageSize);
		musicClass.setIsEnable((byte) 0);
		
		return classMapper.selectSelectiveWithPage(musicClass);
	}
	
	public Integer countMusicClassesEnabled()
	{
		MusicClass musicClass = new MusicClass();
		musicClass.setIsEnable((byte) 0);
		return classMapper.countSelective(musicClass);
	}
	
	/**
	 * 启用/禁用音乐分类
	 * @param mcId 音乐分类id
	 * @param isEnable 0启用 1禁用
	 * @return
	 */
	public Integer enableMusicClass(Long mcId, Byte isEnable)
	{
		List<Long> ids = new ArrayList<Long>();
		ids.add(mcId);
		return classMapper.enableByPrimaryKeyBulk(ids, isEnable);
	}
	
	/**
	 * 删除音乐分类
	 * @param mcId
	 * @return
	 */
	public Integer deleteMusicClass(Long mcId)
	{
		return classMapper.deleteByPrimaryKey(mcId);
	}
	
	/**
	 * 获取音乐分类，根据id
	 * @param mcId
	 * @return
	 */
	public MusicClass obtainMusicClassById(Long mcId)
	{
		return classMapper.selectByPrimaryKey(mcId);
	}
	
	/**
	 * 日期保留年月日
	 * @param date null时返回当天
	 * @return
	 */
	public Date dateRetainYmd(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		if(date != null)
		{
			calendar.setTime(date);
		}
		
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return calendar.getTime();
	}
	
	/**
	 * 日期保留年月日， 返回当天
	 * @return
	 */
	public Date dateRetainYmd()
	{
		return dateRetainYmd(null);
	}
	
	/**
	 * 创建音乐分类每日统计
	 * @param mcId
	 * @param date null时创建今日统计
	 * @return
	 */
	public Long createClassDailyStatis(Long mcId, Date date)
	{
		MusicClassDailyStatis classDailyStatis = new MusicClassDailyStatis();
		classDailyStatis.setMcId(mcId);
		//保留年月日
		Date today = dateRetainYmd(date);
		classDailyStatis.setRecordDate(today);
		
		classDailyStatis.setCreateTime(new Date());
		
		classDailyStatisMapper.insertSelective(classDailyStatis);	
		
		return classDailyStatis.getMcdsId();
	}
	
	/**
	 * 创建音乐分类今日统计
	 * @param mcId
	 * @return
	 */
	public Long createClassTodayStatis(Long mcId)
	{
		return createClassDailyStatis(mcId, null);
	}
	
	/**
	 * 获取音乐分类某日统计
	 * @param mcId 音乐分类id
	 * @param date 日期
	 * @return
	 */
	public MusicClassDailyStatis obtainClassDailyStatis(Long mcId, Date date)
	{
		MusicClassDailyStatis mcds = new MusicClassDailyStatis();
		mcds.setMcId(mcId);
		mcds.setRecordDate(dateRetainYmd(date));
		
		List<MusicClassDailyStatis> mcdss = classDailyStatisMapper.selectSelective(mcds);
		if(mcdss.size() > 0)
		{
			return mcdss.get(0);
		}else
		{
			Long mcds1 = createClassDailyStatis(mcId, date);
			return classDailyStatisMapper.selectByPrimaryKey(mcds1);
		}
	}
	
	/**
	 * 获取音乐分类今日统计
	 * @param mcId
	 * @return
	 */
	public MusicClassDailyStatis obtainClassTodayStatis(Long mcId)
	{
		return obtainClassDailyStatis(mcId, null);
	}
	
	/**
	 * 增加音乐分类今日播放次数，带锁
	 * @param mcId 音乐分类id
	 * @return
	 */
	public Integer transIncreClassTodayPlayCount(Long mcId)
	{
		MusicClassDailyStatis mcds = obtainClassTodayStatis(mcId);

		//锁定记录
		MusicClassDailyStatis classDailyStatis = classDailyStatisMapper.selectByPrimaryKeyLock(mcds.getMcdsId());
		//增加播放计数
		classDailyStatis.setPlayCount(classDailyStatis.getPlayCount() + 1);
		
		classDailyStatis.setModifyTime(new Date());
		return classDailyStatisMapper.updateByPrimaryKeySelective(classDailyStatis);
	}
	
	/**
	 * 增加音乐分类历史播放次数
	 * @param mcId
	 * @return
	 */
	public Integer transIncreClassHistoryPlayCount(Long mcId)
	{
		MusicClass musicClass = classMapper.selectByPrimaryKeyLock(mcId);
		
		musicClass.setHistoryPlayCount(musicClass.getHistoryPlayCount() + 1);
		musicClass.setModifyTime(new Date());
		
		return classMapper.updateByPrimaryKeySelective(musicClass);
	}
	
	/**
	 * 增加音乐分类播放次数
	 * @param mcId
	 */
	private void increClassPlayCount(Long mcId)
	{
		transIncreClassTodayPlayCount(mcId);
		transIncreClassHistoryPlayCount(mcId);
	}
	
	/**
	 * 查询某个音乐分类下启用的歌曲
	 * @param mcId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<MusicSong> obtainMusicSongsEnabledByClass(Long mcId, Integer pageIndex, Integer pageSize)
	{
		MusicSong musicSong = new MusicSong();
		musicSong.setMcId(mcId);
		musicSong.setPageIndex(pageIndex);
		musicSong.setPageSize(pageSize);
		musicSong.setIsEnable((byte) 0);
		
		return songMapper.selectSelectiveWithPage(musicSong);
	}
	
	/**
	 * 查询某个音乐分类下启用的歌曲数量
	 * @param mcId 音乐分类id
	 * @return
	 */
	public Integer countMusicSongsEnabledByClass(Long mcId)
	{
		MusicSong musicSong = new MusicSong();
		musicSong.setMcId(mcId);
		musicSong.setIsEnable((byte) 0);
		
		return songMapper.countSelective(musicSong);
	}
	
	/**
	 * 查询某个音乐分类下个歌曲数量
	 * @param mcId 音乐分类id
	 * @return
	 */
	public Integer countMusicSongsByClass(Long mcId)
	{
		MusicSong musicSong = new MusicSong();
		musicSong.setMcId(mcId);
		
		return songMapper.countSelective(musicSong);
	}
	
	/**
	 * 新建音乐歌曲
	 * @param musicSong
	 * @return
	 */
	public Integer createMusicSong(MusicSong musicSong)
	{
		musicSong.setCreateTime(new Date());
		return songMapper.insertSelective(musicSong);
	}
	
	/**
	 * 修改音乐歌曲
	 * @param musicSong
	 * @return
	 */
	public Integer modifyMusicSong(MusicSong musicSong)
	{
		musicSong.setModifyTime(new Date());
		
		return songMapper.updateByPrimaryKeySelective(musicSong);
	}
	
	/**
	 * 搜索音乐歌曲，按历史播放数倒序
	 * @param words 关键词
	 * @param mcId 音乐分类id
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<MusicSong> searchMusicSong(String words, Long mcId,
			Integer pageIndex, Integer pageSize)
	{
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		
		return songMapper.searchWithPage(words, mcId, page.getPageStartNum(), pageSize);
	}
	
	public Integer countSearch(String words, Long mcId)
	{
		return songMapper.countSearch(words, mcId);
	}
	
	/**
	 * 根据条件获取排名
	 * @param words 关键词
	 * @param mcId 音乐分类id
	 * @param historyPlayCount 排名条件：历史播放数
	 * @param todayPlayCount 排名条件：今日播放数
	 * @param historyShareCount 排名条件：历史分享数
	 * @return
	 */
	public Integer ranking(String words, Long mcId, 
			Integer historyPlayCount,
			Integer todayPlayCount,
			Integer weekPlayCount,
			Integer historyShareCount)
	{
		return songMapper.ranking(words, mcId, historyPlayCount, todayPlayCount, weekPlayCount, historyShareCount);
	}
	
	/**
	 * 启用/禁用音乐歌曲
	 * @param msId
	 * @param isEnable
	 * @return
	 */
	public Integer enableMusicSongs(List<Long> msIds, Byte isEnable)
	{
		return songMapper.enableByPrimaryKeyBulk(msIds, isEnable);
	}
	
	/**
	 * 删除音乐歌曲
	 * @param msId
	 * @return
	 */
	public Integer deleteMusicSongs(List<Long> msIds)
	{
		return songMapper.deleteByPrimaryKeyBulk(msIds);
	}

	/**
	 * 创建音乐歌曲每日统计
	 * @param mcId
	 * @param date null时创建今日统计
	 * @return
	 */
	public Long createSongDailyStatis(Long msId, Date date)
	{
		MusicSongDailyStatis songDailyStatis = new MusicSongDailyStatis();
		songDailyStatis.setMsId(msId);
		//保留年月日
		Date today = dateRetainYmd(date);
		songDailyStatis.setRecordDate(today);
		
		songDailyStatis.setCreateTime(new Date());
		
		songDailyStatisMapper.insertSelective(songDailyStatis);	
		
		return songDailyStatis.getMscdId();
	}

	/**
	 * 创建音乐歌曲今日统计
	 * @param msId
	 * @return
	 */
	public Long createSongTodayStatis(Long msId)
	{
		return createSongDailyStatis(msId, null);
	}

	/**
	 * 获取音乐歌曲某日统计
	 * @param msId 音乐歌曲id
	 * @param date 日期
	 * @return
	 */
	public MusicSongDailyStatis obtainSongDailyStatis(Long msId, Date date)
	{
		MusicSongDailyStatis mcds = new MusicSongDailyStatis();
		mcds.setMsId(msId);
		mcds.setRecordDate(dateRetainYmd(date));
		
		List<MusicSongDailyStatis> mcdss = songDailyStatisMapper.selectSelective(mcds);
		if(mcdss.size() > 0)
		{
			return mcdss.get(0);
		}else
		{
			Long mcds1 = createSongDailyStatis(msId, date);
			return songDailyStatisMapper.selectByPrimaryKey(mcds1);
		}
	}

	/**
	 * 获取音乐歌曲今日统计
	 * @param msId
	 * @return
	 */
	public MusicSongDailyStatis obtainSongTodayStatis(Long msId)
	{
		return obtainSongDailyStatis(msId, null);
	}
	
	/**
	 * 获取音乐歌曲一周播放数
	 * @param msId
	 * @return
	 */
	public Integer obtainSongWeekPlayCount(Long msId)
	{
		return songMapper.obtainWeekPlayCount(msId);
	}

	/**
	 * 增加音乐歌曲今日播放次数, 带锁
	 * @param msId 音乐歌曲id
	 * @return
	 */
	public Integer transIncreSongTodayPlayCount(Long msId)
	{
		MusicSongDailyStatis msds = obtainSongTodayStatis(msId);
	
		//锁定记录
		MusicSongDailyStatis songDailyStatis = songDailyStatisMapper.selectByPrimaryKeyLock(msds.getMscdId());
		//增加播放计数
		songDailyStatis.setPlayCount(songDailyStatis.getPlayCount() + 1);
		
		songDailyStatis.setModifyTime(new Date());
		return songDailyStatisMapper.updateByPrimaryKeySelective(songDailyStatis);
	}
	
	/**
	 * 增加音乐歌曲历史播放次数, 带锁
	 * @param msId
	 * @return
	 */
	public Integer transIncreSongHistoryPlayCount(Long msId)
	{
		MusicSong musicSong = songMapper.selectByPrimaryKeyLock(msId);
		
		musicSong.setHistoryPlayCount(musicSong.getHistoryPlayCount() + 1);
		musicSong.setModifyTime(new Date());
		
		return songMapper.updateByPrimaryKeySelective(musicSong);
	}
	
	/**
	 * 增加音乐歌曲播放次数
	 * @param msId
	 * @return
	 */
	public void increSongPlayCount(Long msId)
	{
		transIncreSongTodayPlayCount(msId);
		transIncreSongHistoryPlayCount(msId);
		
		//同时增加分类的播放次数
		MusicSong musicSong = songMapper.selectByPrimaryKey(msId);		
		increClassPlayCount(musicSong.getMcId());
	}
	
	
	/**
	 * 增加音乐歌曲历史分享次数, 带锁
	 * @param msId
	 * @return
	 */
	public Integer transIncreSongHistoryShareCount(Long msId)
	{
		MusicSong musicSong = songMapper.selectByPrimaryKeyLock(msId);
		
		musicSong.setHistoryShareCount(musicSong.getHistoryShareCount() + 1);
		musicSong.setModifyTime(new Date());
		
		return songMapper.updateByPrimaryKeySelective(musicSong);
	}
	
	/**
	 * 加音乐歌曲分享次数
	 * @param msId
	 */
	public void increSongShareCount(Long msId)
	{
		transIncreSongHistoryShareCount(msId);
	}
}
