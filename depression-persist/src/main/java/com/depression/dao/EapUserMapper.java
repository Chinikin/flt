package com.depression.dao;

import java.util.List;

import com.depression.model.eap.vo.EapUserVO;


public interface EapUserMapper {
    int deleteByPrimaryKey(Long eapUserId);

    int insert(EapUserVO record);

    int insertSelective(EapUserVO record);

    EapUserVO selectByPrimaryKey(Long eapUserId);

    int updateByPrimaryKeySelective(EapUserVO record);

    int updateByPrimaryKey(EapUserVO record);
    
    int selectCountByEapUser(EapUserVO record);
    
    List<EapUserVO> selectSelective(EapUserVO record);
    
    List<EapUserVO> selectByPage(EapUserVO record);
    
    int updateBymobilePhone(EapUserVO record);
    
    int updateByEeId(EapUserVO record);
    
    EapUserVO selectBymobilePhone(String mobilePhone);
}