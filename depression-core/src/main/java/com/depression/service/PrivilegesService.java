package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.depression.dao.SystemMenuMapper;
import com.depression.dao.SystemUserinfoMenuMappingMapper;
import com.depression.model.SystemMenu;
import com.depression.model.SystemUserinfoMenuMapping;

@Service
public class PrivilegesService
{
	@Autowired
	SystemMenuMapper systemMenuMapper;

	@Autowired
	SystemUserinfoMenuMappingMapper systemUserinfoMenuMappingMapper;

	/**
	 * 获取菜单列表
	 * @param userId
	 * @return
	 */
	public String getMenuList(Long userId)
	{
		SystemMenu queryMenu = new SystemMenu();
		queryMenu.setMenuType(Byte.parseByte("0"));
		queryMenu.setIsEnable(Byte.parseByte("0"));
		JSONArray parrentJsonArray = new JSONArray();
		// 查询父目录列表
		List<SystemMenu> parentMenuList = systemMenuMapper.selectSelective(queryMenu);
		for (SystemMenu parentMenu : parentMenuList)
		{
			JSONObject parentJsonObject = new JSONObject();
			parentJsonObject.put("menuId", parentMenu.getMenuId());
			parentJsonObject.put("menuName", parentMenu.getMenuName());

			// 查询菜单是否已选择
			SystemUserinfoMenuMapping queryMappingParent = new SystemUserinfoMenuMapping();
			queryMappingParent.setUserId(userId);
			queryMappingParent.setMenuId(parentMenu.getMenuId());
			queryMappingParent.setIsEnable(Byte.parseByte("0"));
			List<SystemUserinfoMenuMapping> mappingListParent = systemUserinfoMenuMappingMapper.selectSelective(queryMappingParent);
			if (mappingListParent != null && mappingListParent.size() > 0)
			{
				parentJsonObject.put("selected", true);
			} else
			{
				parentJsonObject.put("selected", false);
			}

			SystemMenu queryChildMenu = new SystemMenu();
			queryChildMenu.setIsEnable(Byte.parseByte("0"));
			queryChildMenu.setParentMenuId(parentMenu.getMenuId());
			// 查询子目录列表
			List<SystemMenu> childMenuList = systemMenuMapper.selectSelective(queryChildMenu);
			JSONArray childJsonArray = new JSONArray();
			for (SystemMenu childMenu : childMenuList)
			{
				JSONObject childJsonObject = new JSONObject();
				childJsonObject.put("menuId", childMenu.getMenuId());
				childJsonObject.put("menuName", childMenu.getMenuName());

				// 查询菜单是否已选择
				SystemUserinfoMenuMapping queryMappingChild = new SystemUserinfoMenuMapping();
				queryMappingChild.setUserId(userId);
				queryMappingChild.setMenuId(childMenu.getMenuId());
				queryMappingChild.setIsEnable(Byte.parseByte("0"));
				List<SystemUserinfoMenuMapping> mappingListChild = systemUserinfoMenuMappingMapper.selectSelective(queryMappingChild);
				if (mappingListChild != null && mappingListChild.size() > 0)
				{
					childJsonObject.put("selected", true);
				} else
				{
					childJsonObject.put("selected", false);
				}

				childJsonArray.add(childJsonObject);
			}
			parentJsonObject.put("childMenus", childJsonArray);
			parrentJsonArray.add(parentJsonObject);
		}

		return parrentJsonArray.toJSONString();
	}

	/**
	 * 更新菜单选择结果
	 * @param userId
	 * @param jsonMenu
	 * @return
	 */
	public int updateMenuList(Long userId, String jsonMenu)
	{
		// 禁用旧的选择
		systemUserinfoMenuMappingMapper.modifyStatusByUserId(userId, Byte.parseByte("1"));

		// 菜单选择结果入库
		JSONArray jsonArray = JSONArray.parseArray(jsonMenu);
		for (int i = 0; i < jsonArray.size(); i++)
		{
			JSONObject jsonObj = (JSONObject) jsonArray.get(i);
			Long menuId = Long.parseLong(jsonObj.get("menuId").toString());
			String selected = jsonObj.get("selected").toString();

			if (selected.equals("true"))
			{
				SystemUserinfoMenuMapping systemUserinfoMenuMapping = new SystemUserinfoMenuMapping();
				systemUserinfoMenuMapping.setUserId(userId);
				systemUserinfoMenuMapping.setMenuId(menuId);
				systemUserinfoMenuMapping.setCreateTime(new Date());
				systemUserinfoMenuMappingMapper.insertSelective(systemUserinfoMenuMapping);
			}
			
		}

		return 0;
	}
	
	public List<SystemMenu> getAllMenu(SystemMenu queryMenu){
		List<SystemMenu> menuList = systemMenuMapper.selectSelective(queryMenu);
		return menuList;
	}
	
	public SystemMenu getMenu(Long menuId){
		SystemMenu menu = systemMenuMapper.selectByPrimaryKey(menuId);
		return menu;
	}
}
