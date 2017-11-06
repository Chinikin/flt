package com.depression.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.EapEmployeeMapper;
import com.depression.model.EapEmployee;
import com.depression.model.EapEmployeeCustom;
import com.depression.model.Page;
import com.depression.model.eap.vo.EapEmployeeVO;

@Service
public class EapEmployeeService
{
	@Autowired
	EapEmployeeMapper eapEmployeeMapper;
	@Autowired
	EapEnterpriseService eapEnterpriseService;
	
	/**
	 * 计算企业员工数量
	 * @param eeId  企业id
	 * @return
	 */
	public Integer countEmployeeInEmterprise(Long eeId)
	{
		EapEmployee eem = new EapEmployee();
		eem.setEeId(eeId);
		
		return eapEmployeeMapper.countSelective(eem);
	}
	
	/**
	 * 确认企业员工是否已存在
	 * @param eeId 企业id
	 * @param phoneNum 手机号码
	 * @return
	 */
	public Integer isEmployeeExisting(Long eeId, String phoneNum)
	{
		EapEmployee eem = new EapEmployee();
		eem.setEeId(eeId);
		eem.setPhoneNum(phoneNum);
		
		List<EapEmployee> eems = eapEmployeeMapper.selectSelective(eem);
		
		return eems.size();
	}
	
	/**
	 * 新增企业员工, 插入前检查是否已经存在
	 * @param eeId	企业id
	 * @param name	员工名
	 * @param phoneNum	员工手机号
	 * @param number 学/工号
	 * @return
	 */
	public Integer newEmployee(Long eeId, String name, String phoneNum, String number)
	{
		if(isEmployeeExisting(eeId, phoneNum) > 0)
		{
			return 0;
		}
		
		EapEmployee eem = new EapEmployee();
		eem.setEeId(eeId);
		eem.setPhoneNum(phoneNum);		
		eem.setName(name);
		eem.setNumber(number);
		eem.setCreateTime(new Date());
		return eapEmployeeMapper.insertSelective(eem);
	}
	
	/**
	 * 新增员工, 插入前检查是否已经存在
	 * @param eem
	 * @return
	 */
	public Integer newEmployee(EapEmployee eem)
	{
		if(isEmployeeExisting(eem.getEeId(), eem.getPhoneNum()) > 0){
			return 0;
		}
		return eapEmployeeMapper.insertSelective(eem);
	}
	
	
	
	/**
	 * 启用/禁用企业员工
	 * @param ids 企业员工id（eemId）列表
	 * @param isEnable 0 启用 1禁用
	 * @return
	 */
	public Integer enableEmployee(List<Long> ids, Byte isEnable)
	{
		return eapEmployeeMapper.enableByPrimaryKeyBulk(ids, isEnable);
	}
	
	/**
	 * 删除企业员工
	 * @param ids 企业员工id（eemId）列表
	 * @return
	 */
	public Integer deleteEmployee(List<Long> ids)
	{
		return eapEmployeeMapper.deleteByPrimaryKeyBulk(ids);
	}
	
	/**
	 * 根据名称或者手机号搜索员工
	 * @param words 关键词
	 * @param pageIndex 页码
	 * @param pageSize 页大小
	 * @return
	 */
	public List<EapEmployee> searchEapEmployee(Long eeId, String words, Integer pageIndex, Integer pageSize)
	{
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		
		return eapEmployeeMapper.searchWithPage(eeId, words, page.getPageStartNum(), pageSize);
	}
	
	/**
	 * 获取搜索结果数量
	 * @param words
	 * @return
	 */
	public Integer countSearchEapEmployee(Long eeId, String words)
	{
		return eapEmployeeMapper.countSearch(eeId, words);
	}
	
	/**
	 * 删除企业下所有员工信息
	 * @param eeId 企业id
	 * @return
	 */
	public Integer deleteEmployeeByEeId(Long eeId)
	{
		return eapEmployeeMapper.deleteEmployeeByEeId(eeId);
	}
	
	/**
	 * 获取企业虾所有员工信息
	 * @param eeId 企业id
	 * @return
	 */
	public List<EapEmployee> obtainEmployeeByEeId(Long eeId)
	{
		EapEmployee eem = new EapEmployee();
		eem.setEeId(eeId);
		
		return eapEmployeeMapper.selectSelective(eem);
	}

	
	/**
	 * 搜索员工列表
	 * @param eemVO 搜索参数
	 * @param sortTag 排序方式
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<EapEmployeeCustom> getEapEmployeeList(EapEmployeeVO eemVO,Integer sortTag,
			Integer pageIndex, Integer pageSize) {
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		return eapEmployeeMapper.selectWithQuery(eemVO.getEeId(), eemVO.getWords(),eemVO.getGrade(),
												  eemVO.getStartTime(),eemVO.getEndTime(),eemVO.getCollege(),
												  eemVO.getIsEnable(),page.getPageStartNum(), pageSize,sortTag);
	}

	
	/**
	 * 统计搜索员工总数
	 * @param eemVO 搜索参数
	 * @return
	 */
	public Integer countEapEmployeeList(EapEmployeeVO eemVO) {
		return eapEmployeeMapper.countEapEmployeeList(eemVO.getEeId(), eemVO.getWords(),eemVO.getGrade(),
				  									  eemVO.getStartTime(),eemVO.getEndTime(),eemVO.getCollege(),
				  									  eemVO.getIsEnable());
	}

	/**
	 * 获取学校的所有学院
	 * @param eeId 学校id
	 * @return
	 */
	public List<String> getCollegesByEeId(Long eeId) {
		
		return eapEmployeeMapper.selectCollegesByEeId(eeId);
	}
	
	/**
	 * 获取学校的所有年级
	 * @param eeId 学校id
	 * @return
	 */
	public List<Long> getGradesByEeId(Long eeId) {
		
		return eapEmployeeMapper.selectGradesByEeId(eeId);
	}
	

	/**
	 * 更新员工信息
	 * @param eem
	 * @return
	 */
	public Integer updateEapEmployeeByEemId(EapEmployee eem) {
		/*if(eem.getPhoneNum() != null){
			if(isEmployeeExisting(eem.getEeId(), eem.getPhoneNum()) > 0){
				return 0;
			}
		}*/
		eem.setUpdateTime(new Date());
		return eapEmployeeMapper.updateByPrimaryKeySelective(eem);
	}

	/**
	 * 根据手机号  和企业id  获取员工信息
	 * @param mobilePhone
	 * @param eeId
	 * @return
	 */
	public EapEmployee getEapEmployeeListByPhoneNumAndEeId(String mobilePhone,Long eeId) {
		EapEmployee eem=new EapEmployee();
		eem.setPhoneNum(mobilePhone);
		eem.setEeId(eeId);
		List<EapEmployee> list=eapEmployeeMapper.selectSelective(eem);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
