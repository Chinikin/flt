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

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.MusicClass;
import com.depression.model.MusicClassDailyStatis;
import com.depression.model.MusicSong;
import com.depression.model.MusicSongDailyStatis;
import com.depression.model.web.dto.WebMusicClassDTO;
import com.depression.model.web.dto.WebMusicSongDTO;
import com.depression.model.web.vo.WebIdsVO;
import com.depression.model.web.vo.WebMusicClassVO;
import com.depression.model.web.vo.WebMusicSongVO;
import com.depression.service.MusicService;
import com.depression.utils.Configuration;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/Music")
public class MusicController
{
    Logger log = Logger.getLogger(this.getClass());
    
	@Autowired
	MusicService musicService;
	
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception
	{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}
	
	/**
	 * 创建音乐分类
	 * @param musicClassVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/createMusicClass.json")
	@ResponseBody
	public Object createMusicClass(WebMusicClassVO musicClassVO)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				musicClassVO,
				musicClassVO.getName(),
				musicClassVO.getBackgroundImage()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		//防止传入id
		musicClassVO.setMcId(null);
		
		try{
			MusicClass musicClass = new MusicClass();
			BeanUtils.copyProperties(musicClassVO, musicClass);
			
			musicService.createMusicClass(musicClass);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_INSERT.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_INSERT.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_INSERT.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	
	/**
	 * 修改音乐分类
	 * @param musicClassVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/modifyMusicClass.json")
	@ResponseBody
	public Object modifyMusicClass(WebMusicClassVO musicClassVO)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				musicClassVO,
				musicClassVO.getMcId()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			MusicClass musicClass = new MusicClass();
			BeanUtils.copyProperties(musicClassVO, musicClass);
			
			musicService.modifyMusicClass(musicClass);
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
	 * 获取音乐分类列表
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainMusicClasses.json")
	@ResponseBody
	public Object obtainMusicClasses(Integer pageIndex, Integer pageSize)
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
		
		List<WebMusicClassDTO> musicClassDTOs = new ArrayList<WebMusicClassDTO>();
		Integer count;
		try{
			List<MusicClass> mcs = musicService.obtainMusicClasses(pageIndex, pageSize);
			for(MusicClass mc : mcs)
			{
				WebMusicClassDTO musicClassDTO = new WebMusicClassDTO();
				BeanUtils.copyProperties(mc, musicClassDTO);
				//查询今日播放次数
				MusicClassDailyStatis mcds = musicService.obtainClassTodayStatis(mc.getMcId());
				musicClassDTO.setTodayPlayCount(mcds.getPlayCount());
				
				//查询歌曲数量
				musicClassDTO.setSongCount(musicService.countMusicSongsByClass(mc.getMcId()));
				
				//背景图路径
				musicClassDTO.setBackgroundImageAbs(mc.getBackgroundImage());
				
				musicClassDTOs.add(musicClassDTO);
			}
			
			count = musicService.countMusicClasses();
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		 
		
		result.put("musicClassDTOs", musicClassDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	
	/**
	 * 删除音乐分类
	 * @param mcId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/enableMusicClass.json")
	@ResponseBody
	public Object enableMusicClass(Long mcId, Byte isEnable)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				mcId,
				isEnable
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			musicService.enableMusicClass(mcId, isEnable);
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
	 * 删除音乐分类
	 * @param mcId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/deleteMusicClass.json")
	@ResponseBody
	public Object deleteMusicClass(Long mcId)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				mcId
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			Integer songCount = musicService.countMusicSongsByClass(mcId);
			if(songCount > 0)
			{//分类下歌曲不允许删除
				result.setCode(ErrorCode.ERROR_MUSIC_CLASS_HAS_SONGS.getCode());
				result.setMsg(ErrorCode.ERROR_MUSIC_CLASS_HAS_SONGS.getMessage());
				return result;
			}
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		try{
			musicService.deleteMusicClass(mcId);
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
	 * 创建音乐歌曲
	 * @param musicSongVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/createMusicSong.json")
	@ResponseBody
	public Object createMusicSong(WebMusicSongVO musicSongVO)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				musicSongVO,
				musicSongVO.getName(),
				musicSongVO.getMcId(),
				musicSongVO.getFilePath(),
				musicSongVO.getFileType(),
				musicSongVO.getIsEnable()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		//防止传入id
		musicSongVO.setMsId(null);
		
		try{
			MusicSong musicSong = new MusicSong();
			BeanUtils.copyProperties(musicSongVO, musicSong);
			
			musicService.createMusicSong(musicSong);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_INSERT.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_INSERT.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_INSERT.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	
	/**
	 * 修改音乐歌曲
	 * @param musicSongVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/modifyMusicSong.json")
	@ResponseBody
	public Object modifyMusicSong(WebMusicSongVO musicSongVO)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				musicSongVO,
				musicSongVO.getMsId()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			MusicSong musicSong = new MusicSong();
			BeanUtils.copyProperties(musicSongVO, musicSong);
			
			musicService.modifyMusicSong(musicSong);
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
	
	@RequestMapping(method = RequestMethod.POST, value = "/searchMusicSongs.json")
	@ResponseBody
	public Object searchMusicSongs(String words, Long mcId, Integer pageIndex, Integer pageSize)
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
		
		List<WebMusicSongDTO> musicSongDTOs = new ArrayList<WebMusicSongDTO>();
		Integer count;
		try{
			List<MusicSong> musicSongs = musicService.searchMusicSong(words, mcId, pageIndex, pageSize);
			for(MusicSong ms : musicSongs)
			{
				WebMusicSongDTO musicSongDTO =  new WebMusicSongDTO();
				BeanUtils.copyProperties(ms, musicSongDTO);
				
				//查询历史播放数排名
				if(musicSongDTO.getHistoryPlayCount() > 0)
				{
					Integer ranking = musicService.ranking(words, mcId, musicSongDTO.getHistoryPlayCount(), null, null, null);
					musicSongDTO.setHistoryPlayCountRanking(ranking);
				}
				//查询今日播放数及排名
				MusicSongDailyStatis msds = musicService.obtainSongTodayStatis(ms.getMsId());
				musicSongDTO.setTodayPlayCount(msds.getPlayCount());
				if(musicSongDTO.getTodayPlayCount() > 0)
				{
					Integer ranking = musicService.ranking(words, mcId, null, musicSongDTO.getTodayPlayCount(), null, null);
					musicSongDTO.setTodayPlayCountRanking(ranking);
				}
				//查询一周播放数及排名
				Integer weekPlayCount = musicService.obtainSongWeekPlayCount(ms.getMsId());
				musicSongDTO.setWeekPlayCount(weekPlayCount);
				if(musicSongDTO.getWeekPlayCount() > 0)
				{
					Integer ranking = musicService.ranking(words, mcId, null, null, musicSongDTO.getWeekPlayCount(), null);
					musicSongDTO.setWeekPlayCountRanking(ranking);
				}
				//查询历史分享数排名
				if(musicSongDTO.getHistoryShareCount() > 0)
				{
					Integer ranking = musicService.ranking(words, mcId, null, null, null, musicSongDTO.getHistoryShareCount());
					musicSongDTO.setHistoryShareCountRanking(ranking);
				}
				//查询音乐分类名
				MusicClass musicClass = musicService.obtainMusicClassById(musicSongDTO.getMcId());
				musicSongDTO.setMusicClassName(musicClass.getName());
				
				//设置文件路径
				musicSongDTO.setFilePathAbs(ms.getFilePath());
				
				musicSongDTOs.add(musicSongDTO);
			}
			
			count = musicService.countSearch(words, mcId);
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("musicSongDTOs", musicSongDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	
	/**
	 * 启用/禁用音乐歌曲
	 * @param msId
	 * @param isEnable 0启用 1禁用
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/enableMusicSong.json")
	@ResponseBody
	public Object enableMusicSong(WebIdsVO msIds, Byte isEnable)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				msIds,
				isEnable
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			musicService.enableMusicSongs(msIds.getIds(), isEnable);
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
	 * 删除音乐歌曲
	 * @param msId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/deleteMusicSong.json")
	@ResponseBody
	public Object deleteMusicSong(WebIdsVO msIds)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				msIds,
				msIds.getIds()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			musicService.deleteMusicSongs(msIds.getIds());
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
}
