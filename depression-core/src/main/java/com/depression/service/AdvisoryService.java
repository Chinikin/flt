package com.depression.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.MemberAdvisoryCommentMapper;
import com.depression.dao.MemberAdvisoryDetailMapper;
import com.depression.dao.MemberAdvisoryImgsMapper;
import com.depression.dao.MemberAdvisoryMapper;
import com.depression.dao.MemberAdvisoryPraiseNumMapper;
import com.depression.model.MemberAdvisory;
import com.depression.model.MemberAdvisory4Psycho;
import com.depression.model.MemberAdvisory4PsychoV1;
import com.depression.model.MemberAdvisoryComment;
import com.depression.model.MemberAdvisoryDetail;
import com.depression.model.MemberAdvisoryImgs;
import com.depression.model.MemberAdvisoryPraiseNum;
import com.depression.model.Page;

/**
 * 会员咨询
 * 
 * @author hongqian_li
 * @date 2015/05/10
 */
@Service
public class AdvisoryService
{
	@Autowired
	MemberAdvisoryMapper advisoryMapper;
	@Autowired
	MemberAdvisoryDetailMapper detailMapper;
	@Autowired
	MemberAdvisoryImgsMapper imgsMapper;
	@Autowired	
	MemberAdvisoryCommentMapper commentMapper;
	@Autowired		
	MemberAdvisoryPraiseNumMapper praiseMapper;
	/**
	 * 添加一条类型数据
	 * 
	 * @param entity
	 * @return
	 */
	public int createAdvisory(MemberAdvisory advisory)
	{
		advisory.setCreateTime(new Date());
		advisory.setModifyTime(new Date());
		return advisoryMapper.insertSelective(advisory);
	}
	
	/**
	 * 更新咨询
	 * @param advisory
	 * @return
	 */
	public int updateAdvisory(MemberAdvisory advisory)
	{
		advisory.setModifyTime(new Date());
		return advisoryMapper.updateByPrimaryKeySelective(advisory);
	}

	/**
	 * 根据ID查询
	 * @param advisoryId
	 * @return
	 */
	public MemberAdvisory obtainAdvisoryById(Long advisoryId)
	{
		return advisoryMapper.selectByPrimaryKey(advisoryId);
	}
	
	/**
	 * 分页查询 根据条件查找相关内容
	 * 
	 * @param entity
	 * @return
	 */
	public List<MemberAdvisory> obtainAdvisory3Page0TmDesc(MemberAdvisory advisory)
	{
		return advisoryMapper.selectSelective3Page0TmDesc(advisory);
	}

	public Integer countAdvisory(MemberAdvisory advisory)
	{
		return advisoryMapper.countSelective(advisory);
	}
	
	public List<MemberAdvisory> obtainAdvisoryByTag3Page0TmDesc(Integer pageIndex, Integer pageSize, Long tagId)
	{
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		
		return advisoryMapper.select8Tag3Page0TmDesc(page.getPageStartNum(), pageSize, tagId);
	}
	
	public long countAdvisoryByTag(Long tagId)
	{
		return advisoryMapper.count8Tag(tagId);
	}

	/**
	 * 更新数据
	 * 
	 * @param entity
	 * @return
	 */
	public int modifyAdvisory(MemberAdvisory advisory)
	{
		advisory.setModifyTime(new Date());
		return advisoryMapper.updateByPrimaryKeySelective(advisory);
	}
	
	/**
	 * 获取咨询师详情页的回答过的咨询列表
	 * @param mid
	 * @param pageStartNum
	 * @param pageSize
	 * @return
	 */
	public List<MemberAdvisory4Psycho> obtainAdvisory4Psychos(Long mid, Integer pageIndex, 
			Integer pageSize)
	{
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		return advisoryMapper.selectAdvisory4Psychos(mid, page.getPageStartNum(), pageSize);
	}
	
	public Integer countAdvisory4Psychos(Long mid)
	{
		return advisoryMapper.countAdvisory4Psychos(mid);
	}
	
	/**
	 * 查询咨询师回答（始祖级评论）过的咨询
	 * @param pid 咨询师id
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<MemberAdvisory4PsychoV1> obtainAdvisory4PsychosV1(Long pid, Integer pageIndex, 
			Integer pageSize)
	{
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		return advisoryMapper.selectAdvisory4PsychosV1(pid, page.getPageStartNum(), pageSize);
	}
	
	public Integer countAdvisory4PsychosV1(Long pid)
	{
		return advisoryMapper.countAdvisory4PsychosV1(pid);
	}
	
	/**
	 * 咨询分页查询列表
	 * 
	 * @param tagId
	 * @param pageStartNum
	 * @param pageSize
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<MemberAdvisory> obtainAdvisoryByTimeSliceWithPage(Long tagId, Integer pageStartNum, Integer pageSize, Date begin, Date end)
	{
		return advisoryMapper.selectAdvisoryByTimeSliceWithPage(tagId, pageStartNum, pageSize, begin, end);
	}
	
	public List<MemberAdvisory> obtainAdvisoryByTimeSliceWithPageOrderBy(Long tagId, Integer pageStartNum, Integer pageSize, Date begin, Date end)
	{
		return advisoryMapper.selectAdvisoryByTimeSliceWithPageOrderBy(tagId, pageStartNum, pageSize, begin, end);
	}

	/**
	 * 咨询分页查询条数
	 * 
	 * @param tagId
	 * @param begin
	 * @param end
	 * @return
	 */
	public Integer countAdvisoryByTimeSlice(Long tagId, Date begin, Date end)
	{
		return advisoryMapper.countAdvisoryByTimeSlice(tagId, begin, end);
	}
	
	/**
	 * 在主键列表中搜索启用的咨询
	 * @param ids
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<MemberAdvisory> obtainAdvisory8Keys3PageEn(List<Long> ids, Integer pageIndex,
			Integer pageSize)
	{
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		if(ids.size()==0)
		{
			return new ArrayList<MemberAdvisory>();
		}
		return advisoryMapper.select8Keys3PageEn(ids, page.getPageStartNum(), pageSize);
	}
	
	public Integer countAdvisory8KeysEn(List<Long> ids)
	{
		if(ids.size()==0)
		{
			return 0;
		}
		return advisoryMapper.count8KeysEn(ids);
	}
	
	/**
	 * 搜索咨询列表, 按创建时间倒序
	 * @param words 模糊匹配词
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<MemberAdvisory> searchAdvisory(String words, Date begin, Date end, Integer pageIndex,
			Integer pageSize)
	{
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		
		return advisoryMapper.searchAdvisory(page.getPageStartNum(), pageSize, begin, end, words);
	}
	
	public Integer countSearchAdvisory(String words, Date begin, Date end)
	{
		return advisoryMapper.countSearchAdvisory(begin, end, words);
	}
	
	/**
	 * 改变咨询推荐状态
	 * @param advisoryId
	 * @param isRecommended
	 * @return
	 */
	public Integer changeAdvisoryRecommendStatus(Long advisoryId, Byte isRecommended)
	{
		MemberAdvisory ma = new MemberAdvisory();
		ma.setAdvisoryId(advisoryId);
		ma.setIsRecommended(isRecommended);
		
		return advisoryMapper.updateByPrimaryKeySelective(ma);
	}
	
	/**
	 * 删除咨询id
	 * @param advisoryId
	 * @return
	 */
	public Integer deleteAdvisory(Long advisoryId)
	{
		return advisoryMapper.deleteByPrimaryKey(advisoryId);
	}
	
	/**
	 * 增加咨询阅读数量
	 * @param advisoryId
	 * @return
	 */
	public Integer transAdvisoryReadCount(Long advisoryId)
	{
		MemberAdvisory ma = advisoryMapper.selectByPrimaryKeyLock(advisoryId);
		ma.setReadCount(ma.getReadCount() + 1);
		
		return advisoryMapper.updateByPrimaryKeySelective(ma);
	}
	/**
	 * 增加咨询分享数量
	 * @param advisoryId
	 * @return
	 */
	public Integer transAdvisoryShareCount(Long advisoryId)
	{
		MemberAdvisory ma = advisoryMapper.selectByPrimaryKeyLock(advisoryId);
		ma.setShareCount(ma.getShareCount() + 1);
		
		return advisoryMapper.updateByPrimaryKeySelective(ma);
	}
	
	/**
	 * 搜索咨询，返回列表
	 * @param pageIndex
	 * @param pageSize
	 * @param isRecommended 是否被推荐 0未推荐 1被推荐
	 * @param tagId 标签id
	 * @param sortMode 1 默认 2最多阅读 3最多回复 4最多分享
	 * @return
	 */
	public List<MemberAdvisory> searchAdvisoryV1(
			Integer pageIndex, Integer pageSize,
			Byte isRecommended, Long tagId,
			Byte sortMode,Long releaseFrom
			)
	{
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		
		return advisoryMapper.searchAdvisoryV1(page.getPageStartNum(), pageSize, isRecommended, tagId, sortMode,releaseFrom);
	}
	
	public Integer countSearchAdvisoryV1(Byte isRecommended, Long tagId,Long releaseFrom)
	{
		return advisoryMapper.countSearchAdvisoryV1(isRecommended, tagId,releaseFrom);
	}
	
	/**
	 * 分页查询咨询
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<MemberAdvisory> obtainAdvisory3PageV1(Integer pageIndex, Integer pageSize)
	{
		MemberAdvisory ma = new MemberAdvisory();
		ma.setPageIndex(pageIndex);
		ma.setPageSize(pageSize);
		return advisoryMapper.selectSelectiveWithPage(ma);
	}
	

	/**
	 * 添加一条类型数据
	 * 
	 * @param entity
	 * @return
	 */
	public int createDetail(MemberAdvisoryDetail detail)
	{
		return detailMapper.insertSelective(detail);
	}

	/**
	 * 根据咨询id获取咨询详情
	 * @param advisoryId
	 * @return
	 */
	public MemberAdvisoryDetail obtainDetailByAdvisoryId(Long advisoryId)
	{
		MemberAdvisoryDetail mad = new MemberAdvisoryDetail();
		mad.setAdvisoryId(advisoryId);
		
		List<MemberAdvisoryDetail> mads = detailMapper.selectSelective(mad);
		if(mads.size() > 0)
		{
			return mads.get(0);
		}
		
		return null;
	}

	/**
	 * 分页查询 根据条件查找相关内容
	 * 
	 * @param entity
	 * @return
	 */
	public List<MemberAdvisoryDetail> obtainDetailWithPage(MemberAdvisoryDetail detail)
	{
		return detailMapper.selectSelectiveWithPage(detail);
	}

	public long countDetail(MemberAdvisoryDetail detail)
	{
		return detailMapper.countSelective(detail);
	}

	/**
	 * 添加一条类型数据
	 * 
	 * @param entity
	 * @return
	 */
	public int createImgs(MemberAdvisoryImgs imgs)
	{
		return imgsMapper.insertSelective(imgs);
	}

	/**
	 * 根据咨询id获取
	 * @param advisoryId
	 * @return
	 */
	public List<MemberAdvisoryImgs> obtainImgsByAdvisoryId(Long advisoryId)
	{
		MemberAdvisoryImgs mai = new MemberAdvisoryImgs();
		mai.setAdvisoryId(advisoryId);
		
		return imgsMapper.selectSelective(mai);
	}

	/**
	 * 删除咨询中的图片
	 * @param advisoryImgId
	 * @return
	 */
	public int deleteImgs(Long advisoryImgId)
	{
		return imgsMapper.deleteByPrimaryKey(advisoryImgId);
	}

	/**
	 * 添加一条类型数据
	 * 
	 * @param entity
	 * @return
	 */
	public int createComment(MemberAdvisoryComment comment)
	{
		comment.setCommentTime(new Date());
		return commentMapper.insertSelective(comment);
	}

	/**
	 * 根据咨询id获取评论列表
	 * @param advisoryId
	 * @return
	 */
	public List<MemberAdvisoryComment> obtainCommentsByAdvisoryId(Long advisoryId)
	{
		MemberAdvisoryComment mac = new MemberAdvisoryComment();
		mac.setAdvisoryId(advisoryId);
		
		return commentMapper.selectSelective(mac);
	}

	/**
	 * 根据咨询id分页获取评论列表
	 * @param advisoryId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<MemberAdvisoryComment> obtainCommentsWithPage(Long advisoryId, Integer pageIndex, Integer pageSize)
	{
		MemberAdvisoryComment mac = new MemberAdvisoryComment();
		mac.setAdvisoryId(advisoryId);
		mac.setPageIndex(pageIndex);
		mac.setPageSize(pageSize);
		return commentMapper.selectSelectiveWithPage(mac);
	}
	
	public List<MemberAdvisoryComment> obtainCommentsWithPage(Long advisoryId, Long ancestorId, Integer pageIndex, Integer pageSize)
	{
		MemberAdvisoryComment mac = new MemberAdvisoryComment();
		mac.setAdvisoryId(advisoryId);
		mac.setPageIndex(pageIndex);
		mac.setPageSize(pageSize);
		mac.setAncestorId(ancestorId);
		return commentMapper.selectSelectiveWithPage(mac);
	}
	
	public List<MemberAdvisoryComment> obtainComments3Page0TmDesc(Long advisoryId, Long ancestorId, Integer pageIndex, Integer pageSize)
	{
		MemberAdvisoryComment mac = new MemberAdvisoryComment();
		mac.setAdvisoryId(advisoryId);
		mac.setPageIndex(pageIndex);
		mac.setPageSize(pageSize);
		mac.setAncestorId(ancestorId);
		return commentMapper.selectSelective3Page0TmDesc(mac);
	}

	/**
	 * 查询评论数量
	 * @param advisoryId
	 * @return
	 */
	public long countComments(Long advisoryId)
	{
		MemberAdvisoryComment mac = new MemberAdvisoryComment();
		mac.setAdvisoryId(advisoryId);
		
		return commentMapper.countSelective(mac);
	}
	
	public Integer countComments(Long advisoryId, Long ancestorId)
	{
		MemberAdvisoryComment mac = new MemberAdvisoryComment();
		mac.setAdvisoryId(advisoryId);
		mac.setAncestorId(ancestorId);
		
		return commentMapper.countSelective(mac);
	}

	/**
	 * 根据主键查询评论
	 * @param commentId
	 * @return
	 */
	public MemberAdvisoryComment obtainCommentById(Long commentId)
	{
		return commentMapper.selectByPrimaryKey(commentId);
	}

	/**
	 * 修改评论
	 * 
	 * @param entity
	 * @return
	 */
	public int modifyComment(MemberAdvisoryComment entity)
	{
		return commentMapper.updateByPrimaryKeySelective(entity);
	}

	/**
	 * 获取点赞最多的咨询师评论（底层隐含条件，第一级回答）
	 * @param advisoryId
	 * @return
	 */
	public MemberAdvisoryComment obtainCommentPraisestByAdvisoryId(Long advisoryId)
	{
		return commentMapper.selectPraisest8AdvisoryId(advisoryId);
	}

	/**
	 * 按点赞数量倒序获取咨询师的回答（底层隐含条件，第一级回答）
	 * @param pageIndex
	 * @param pageSize
	 * @param advisoryId
	 * @return
	 */
	public List<MemberAdvisoryComment> obtainComment8AdvisoryId3Page0Praise(
			Integer pageIndex, Integer pageSize, Long advisoryId)
	{
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		return commentMapper.select8AdvisoryId3Page0Praise(page.getPageStartNum(), pageSize, advisoryId);
	}
	/**
	 * 删除评论
	 * @param commentId
	 * @return
	 */
	public Integer deleteComment(Long commentId)
	{
		return commentMapper.deleteByPrimaryKey(commentId);
	}
	
	/**
	 * 修改评论点赞数
	 * @param commentId
	 * @param delta 修改量可正负
	 * @return
	 */
	public Integer transCommentPraiseNum(Long commentId, int delta)
	{
		MemberAdvisoryComment mac = commentMapper.selectByPrimaryKeyLock(commentId);
		mac.setPraiseNum(mac.getPraiseNum() + delta);
		
		return commentMapper.updateByPrimaryKeySelective(mac);
	}

	/**
	 * 创建咨询评论点赞
	 * 
	 * @param entity
	 * @return
	 */
	public int createCommentPraise(MemberAdvisoryPraiseNum praise)
	{
		praise.setCreateTime(new Date());
		praise.setModifyTime(new Date());
		return praiseMapper.insertSelective(praise);
	}
	
	/**
	 * 对评论点赞
	 * @param mid
	 * @param commentId
	 * @return
	 */
	public void praiseComment(Long mid, Long commentId)
	{
		MemberAdvisoryPraiseNum praise = new MemberAdvisoryPraiseNum();
		praise.setMid(mid);
		praise.setCommentId(commentId);
		praise.setCreateTime(new Date());
		praise.setModifyTime(new Date());
		praiseMapper.insertSelective(praise);
		
		transCommentPraiseNum(commentId, 1);
	}
	
	/**
	 * 删除评论点赞
	 * @param mid
	 * @param commentId
	 * @return
	 */
	public void unpraiseComment(Long mid, Long commentId)
	{
		MemberAdvisoryPraiseNum map = new MemberAdvisoryPraiseNum();
		map.setCommentId(commentId);
		map.setMid(mid);
		List<MemberAdvisoryPraiseNum> praises =  praiseMapper.selectSelective(map);
		
		for(MemberAdvisoryPraiseNum praise : praises)
		{
			praiseMapper.deleteByPrimaryKey(praise.getPraiseNumId());
		}
		
		transCommentPraiseNum(commentId, -1);
	}

	/**
	 * 确认评论是否已经点赞
	 * @param mid
	 * @param commentId
	 * @return 0 未点赞 1已点赞
	 */
	public int isPraisedComment(Long mid, Long commentId)
	{
		MemberAdvisoryPraiseNum map = new MemberAdvisoryPraiseNum();
		map.setCommentId(commentId);
		map.setMid(mid);
		
		int count = praiseMapper.countSelective(map);
		return count == 0? 0 : 1;
	}

	/**
	 * 搜索eap中的员工咨询信息
	 * @param words 关键字
	 * @param eeId 企业id
	 * @param begin
	 * @param end
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<MemberAdvisory> searchAdvisoryInEap(String words,Long eeId,Long mid, Date begin,
			Date end, Integer pageIndex, Integer pageSize) {
		Page page=new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
	    return advisoryMapper.searchAdvisoryInEap(page.getPageStartNum(), pageSize, begin, end, words,eeId,mid);
	}
	
	/**
	 * 统计eap中的员工咨询信息
	 * @param words
	 * @param eeId
	 * @param begin
	 * @param end
	 * @return
	 */
	public Integer countSearchAdvisory(String words,Long eeId,Long mid, Date begin, Date end){
		return advisoryMapper.countSearchAdvisoryInEap(words, eeId,mid,begin,end);
	}
	
}
