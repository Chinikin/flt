package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.EapUserMapper;
import com.depression.model.eap.vo.EapUserVO;

@Service
public class EapUserService {

	@Autowired
	EapUserMapper eapUserMapper;

	public EapUserVO getEapUser(EapUserVO eapUser) {
		List<EapUserVO> dto = eapUserMapper.selectSelective(eapUser);
		if (dto.size() > 0) {
			return dto.get(0);
		} else {
			return null;
		}
	}

	public List<EapUserVO> queryEapUser(EapUserVO eapUser){
		List<EapUserVO> dto = eapUserMapper.selectSelective(eapUser);
		return dto;
	}
	
	public List<EapUserVO> selectByPage(EapUserVO eapUser){
		List<EapUserVO> dto = eapUserMapper.selectByPage(eapUser);
		return dto;
	}

	public Integer createUser(EapUserVO eapUser) {
		eapUser.setCreateTime(new Date());
		return eapUserMapper.insertSelective(eapUser);
	}

	public Integer updateByKey(EapUserVO eapUser) {
		eapUser.setModifyTime(new Date());
		return eapUserMapper.updateByPrimaryKeySelective(eapUser);		
	}

	public Integer deleteUser(long eapUserId) {
		return eapUserMapper.deleteByPrimaryKey(eapUserId);
	}

	public EapUserVO selectByPrimaryKey(Long userId) {
		return eapUserMapper.selectByPrimaryKey(userId);
	}

	public Integer selectCountByEapUser(EapUserVO eapUser){
		return eapUserMapper.selectCountByEapUser(eapUser);
	}
	
	public Integer updateBymobilePhone(EapUserVO eapUser) {
		eapUser.setModifyTime(new Date());
		return eapUserMapper.updateBymobilePhone(eapUser);
	}
	
	public Integer updateByEeId(EapUserVO eapUser) {
		eapUser.setModifyTime(new Date());
		return eapUserMapper.updateByEeId(eapUser);
	}
	
	public EapUserVO selectBymobilePhone(String mobilePhone) {
		return eapUserMapper.selectBymobilePhone(mobilePhone);
	}
}
