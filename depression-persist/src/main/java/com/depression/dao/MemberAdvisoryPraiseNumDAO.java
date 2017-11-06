package com.depression.dao;

import java.util.List;

import com.depression.model.MemberAdvisoryPraiseNum;
import com.depression.model.MemberUpdateEmbrace;

/**
 * 会员咨询点赞
 * 
 * @author hongqian_li
 * @date 2016/05/10
 * 
 */
public interface MemberAdvisoryPraiseNumDAO
{
	/**
	 * 添加一条类型数据
	 * 
	 * @param entity
	 * @return
	 */
	public int insert(MemberAdvisoryPraiseNum entity);

	/**
	 * 根据条件查找相关内容
	 * 
	 * @param entity
	 * @return
	 */
	public List<MemberAdvisoryPraiseNum> selectBy(MemberAdvisoryPraiseNum entity);

	/**
	 * 分页查询 根据条件查找相关内容
	 * 
	 * @param entity
	 * @return
	 */
	public List<MemberAdvisoryPraiseNum> selectByPage(MemberAdvisoryPraiseNum entity);

	/**
	 * 分页查询count
	 * 
	 * @param entity
	 * @return
	 */
	public long selectCount(MemberAdvisoryPraiseNum entity);

	/**
	 * 更新数据
	 * 
	 * @param entity
	 * @return
	 */
	public int update(MemberAdvisoryPraiseNum entity);
}
