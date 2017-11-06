package com.depression.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.EapMenuMapper;
import com.depression.model.eap.dto.EapMenuDTO;

@Service
public class EapMenuService {

	@Autowired
	EapMenuMapper eapMenuMapper;

	

	public List<EapMenuDTO> getAllMenu(){
		
		List<EapMenuDTO> dto = eapMenuMapper.selectAll();
		//管理系统管理员功能菜单不显示
		for(int i=0;i<dto.size();i++){
		    if(dto.get(i).getMenuId()==1){
		    	dto.remove(i);
		    }
		}		
		return dto;
	}
	
}

