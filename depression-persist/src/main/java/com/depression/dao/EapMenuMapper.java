package com.depression.dao;

import java.util.List;

import com.depression.model.eap.dto.EapMenuDTO;


public interface EapMenuMapper {
    int deleteByPrimaryKey(Long menuId);

    int insert(EapMenuDTO record);

    int insertSelective(EapMenuDTO record);

    EapMenuDTO selectByPrimaryKey(Long menuId);
    
    List<EapMenuDTO> selectAll();
    
    int updateByPrimaryKeySelective(EapMenuDTO record);

    int updateByPrimaryKey(EapMenuDTO record);
}