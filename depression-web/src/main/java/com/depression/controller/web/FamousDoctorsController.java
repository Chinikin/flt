package com.depression.controller.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ResultEntity;
import com.depression.model.FamousDoctors;
import com.depression.service.FamousDoctorsService;
import com.depression.utils.Configuration;

/**
 * 名医介绍
 * 
 * @author fanxinhui
 * @date 2016/6/20
 */
@Controller
@RequestMapping("/famousDoctors")
public class FamousDoctorsController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	FamousDoctorsService famousDoctorsService;

	/**
	 * 新增
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/add.json")
	@ResponseBody
	public Object add(HttpSession session, HttpServletRequest request, FamousDoctors entity)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (entity == null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			if (entity.getName() == null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("姓名不能为空");
				return result;
			}

			famousDoctorsService.insert(entity);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.error(e);
		}
		return result;
	}

	/**
	 * 更新
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/update.json")
	@ResponseBody
	public Object update(HttpSession session, HttpServletRequest request, FamousDoctors entity)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (entity == null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			if (entity.getDoctId() == null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("id不能为空");
				return result;
			}

			famousDoctorsService.update(entity);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.error(e);
		}
		return result;
	}

	/**
	 * 分页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/list.json")
	@ResponseBody
	public Object list(HttpServletRequest request, FamousDoctors entity)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (entity == null)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("参数错误");
			return result;
		}
		if (entity.getPageIndex() == null)
		{
			result.setCode(-1);
			result.setMsg("分页页码不能为空");
			return result;
		}
		if (entity.getPageSize() == null)
		{
			result.setCode(-1);
			result.setMsg("每页条数不能为空");
			return result;
		}

		FamousDoctors famousDoctors = new FamousDoctors();
		if (entity.getName() != null)
		{
			famousDoctors.setName(entity.getName());
		}
		if (entity.getPageIndex() != null)
		{
			famousDoctors.setPageIndex(entity.getPageIndex());
		}
		if (entity.getPageSize() != null)
		{
			famousDoctors.setPageSize(entity.getPageSize());
		}

		// 查询集合
		Long totalCount = famousDoctorsService.getPageCounts(famousDoctors);
		if (totalCount != 0)
		{
			List<FamousDoctors> list = famousDoctorsService.getPageList(famousDoctors);
			for (FamousDoctors doctors : list)
			{
				// 转换实际文件路径
				if (doctors.getAvatar() != null && !doctors.getAvatar().equals(""))
				{
					doctors.setImgPreviewPath(doctors.getAvatar());
				}
			}
			result.put("list", list);
			result.put("count", totalCount);
		} else
		{
			result.put("list", null);
			result.put("count", totalCount);
		}

		result.setCode(ResultEntity.SUCCESS);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/changeStatus.json")
	@ResponseBody
	public Object changeStatus(HttpServletRequest request, ModelMap modelMap, String doctIds, String isDel)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);

		// 参数检查
		if (doctIds == null || doctIds.equals(""))
		{
			result.setCode(-1);
			result.setMsg("id列表不能为空");
			return result;
		}
		if (!isDel.equals("0") && !isDel.equals("1"))
		{
			result.setCode(-1);
			result.setMsg("状态错误：不允许的状态");
			return result;
		}

		// 处理记录id
		doctIds = doctIds.replace("[", "").replace("]", "");
		String[] idArr = doctIds.split(",");
		List<String> idList = new ArrayList<String>();
		for (int idx = 0; idx < idArr.length; idx++)
		{
			if (idArr[idx] != null && !idArr[idx].equals(""))
			{
				idList.add(idArr[idx]);
			}
		}

		// 修改记录状态：0启用，1禁用
		if (isDel != null && isDel != "")
		{
			if (isDel.equals("0"))
			{
				famousDoctorsService.updateFamousDoctorsEnableByDoctIds(idList);
			} else if (isDel.equals("1"))
			{
				famousDoctorsService.updateFamousDoctorsDisableByDoctIds(idList);
			}
		}

		result.setMsg("状态更新成功");
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/view.json")
	@ResponseBody
	public Object view(HttpServletRequest request, String doctId)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);

		// 参数检查
		if (doctId == null || doctId.equals(""))
		{
			result.setCode(-1);
			result.setMsg("id不能为空");
			return result;
		}

		// 查询记录详情
		FamousDoctors famousDoctors = famousDoctorsService.getFamousDoctorsByDoctId(Integer.parseInt(doctId));
		// 转换实际文件路径
		if (famousDoctors!=null && famousDoctors.getAvatar() != null && !famousDoctors.getAvatar().equals(""))
		{
			famousDoctors.setImgPreviewPath(famousDoctors.getAvatar());
		}

		result.put("obj", famousDoctors);
		return result;
	}
}
