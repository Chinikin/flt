package com.depression.controller.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.MusicClass;
import com.depression.model.MusicSong;
import com.depression.model.api.dto.ApiMusicClass8SongDTO;
import com.depression.model.api.dto.ApiMusicClassDTO;
import com.depression.model.api.dto.ApiMusicSongDTO;
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
		
		List<ApiMusicClassDTO> musicClassDTOs = new ArrayList<ApiMusicClassDTO>();
		Integer count;
		try{
			List<MusicClass> mcs = musicService.obtainMusicClassesEnabled(pageIndex, pageSize);
			for(MusicClass mc : mcs)
			{
				ApiMusicClassDTO musicClassDTO = new ApiMusicClassDTO();
				BeanUtils.copyProperties(mc, musicClassDTO);
				
				//查询歌曲数量
				musicClassDTO.setSongCount(musicService.countMusicSongsEnabledByClass(mc.getMcId()));
				
				//背景图路径
				musicClassDTO.setBackgroundImageAbs(mc.getBackgroundImage());
				
				//避开iOS xcode关键字
				musicClassDTO.setDescriptions(mc.getDescription());
				
				musicClassDTOs.add(musicClassDTO);
			}
			
			count = musicService.countMusicClassesEnabled();
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
	 * 获取分类下的音乐歌曲
	 * @param mcId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainMusicSongs.json")
	@ResponseBody
	public Object obtainMusicSongs(Long mcId, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				mcId,
				pageIndex,
				pageSize
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<ApiMusicSongDTO> musicSongDTOs = new ArrayList<ApiMusicSongDTO>();
		Integer count;
		try{
			List<MusicSong> musicSongs = musicService.obtainMusicSongsEnabledByClass(mcId, pageIndex, pageSize);
			for(MusicSong ms : musicSongs)
			{
				ApiMusicSongDTO musicSongDTO =  new ApiMusicSongDTO();
				BeanUtils.copyProperties(ms, musicSongDTO);
				
				//查询音乐分类名
				MusicClass musicClass = musicService.obtainMusicClassById(musicSongDTO.getMcId());
				musicSongDTO.setMusicClassName(musicClass.getName());
				
				//设置文件路径
				musicSongDTO.setFilePathAbs(ms.getFilePath());
				
				musicSongDTOs.add(musicSongDTO);
			}
			
			count = musicService.countMusicSongsEnabledByClass(mcId);
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
	 * 获取分类和歌曲，限定20条分类和歌曲
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainMusicClassesAndSongs.json")
	@ResponseBody
	public Object obtainMusicClassesAndSongs()
	{
		ResultEntity result = new ResultEntity();
		
		List<ApiMusicClass8SongDTO> musicClass8SongDTOs = new ArrayList<ApiMusicClass8SongDTO>();
		Integer count;
		try{
			List<MusicClass> mcs = musicService.obtainMusicClassesEnabled(1, 20);
			for(MusicClass mc : mcs)
			{
				ApiMusicClass8SongDTO musicClass8SongDTO = new ApiMusicClass8SongDTO();
				BeanUtils.copyProperties(mc, musicClass8SongDTO);
				
				//查询歌曲数量
				musicClass8SongDTO.setSongCount(musicService.countMusicSongsEnabledByClass(mc.getMcId()));
				
				//背景图路径
				musicClass8SongDTO.setBackgroundImageAbs(mc.getBackgroundImage());
				
				//避开iOS xcode关键字
				musicClass8SongDTO.setDescriptions(mc.getDescription());
				
				//查询分类下的歌曲
				List<ApiMusicSongDTO> musicSongDTOs = new ArrayList<ApiMusicSongDTO>();
				List<MusicSong> musicSongs = musicService.obtainMusicSongsEnabledByClass(mc.getMcId(), 1, 20);
				for(MusicSong ms : musicSongs)
				{
					ApiMusicSongDTO musicSongDTO =  new ApiMusicSongDTO();
					BeanUtils.copyProperties(ms, musicSongDTO);
					
					//查询音乐分类名
					MusicClass musicClass = musicService.obtainMusicClassById(musicSongDTO.getMcId());
					musicSongDTO.setMusicClassName(musicClass.getName());
					
					//设置文件路径
					musicSongDTO.setFilePathAbs(ms.getFilePath());
					
					musicSongDTOs.add(musicSongDTO);
				}
				musicClass8SongDTO.setMusicSongDTOs(musicSongDTOs);
				
				
				musicClass8SongDTOs.add(musicClass8SongDTO);
			}
			
			count = musicService.countMusicClassesEnabled();
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		 
		
		result.put("musicClass8SongDTOs", musicClass8SongDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	
	/**
	 * 增加音乐歌曲播放次数
	 * @param msId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/increMusicSongPlayCount.json")
	@ResponseBody
	public Object increMusicSongPlayCount(Long msId)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				msId
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		try{
			musicService.increSongPlayCount(msId);
		}catch (Exception e)
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
	 * 增加音乐歌曲分享次数
	 * @param msId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/increMusicSongShareCount.json")
	@ResponseBody
	public Object increMusicSongShareCount(Long msId)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				msId
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		try{
			musicService.increSongShareCount(msId);
		}catch (Exception e)
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
