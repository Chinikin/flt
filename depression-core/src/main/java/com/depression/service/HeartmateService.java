package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.MemberUpdateCommentMapper;
import com.depression.dao.MemberUpdateDetailMapper;
import com.depression.dao.MemberUpdateEmbraceMapper;
import com.depression.dao.MemberUpdateImgsMapper;
import com.depression.dao.MemberUpdateMapper;
import com.depression.model.MemberUpdate;
import com.depression.model.MemberUpdateComment;
import com.depression.model.MemberUpdateDetail;
import com.depression.model.MemberUpdateEmbrace;
import com.depression.model.MemberUpdateImgs;
import com.depression.model.Page;

/**
 * 会员动态
 * 
 * @author hongqian_li
 * 
 */
@Service
public class HeartmateService
{
	@Autowired
	MemberUpdateMapper updateMapper;
	@Autowired	
	MemberUpdateCommentMapper commentMapper;
	@Autowired	
	MemberUpdateDetailMapper detailMapper;
	@Autowired	
	MemberUpdateImgsMapper imgsMapper;
	@Autowired
	MemberUpdateEmbraceMapper embraceMapper;

	/**
	 * 创建一条心友圈动态
	 * 
	 * @param update
	 * @return
	 */
	public int createUpdate(MemberUpdate update)
	{
		update.setCreateTime(new Date());
		update.setModifyTime(new Date());
		return updateMapper.insertSelective(update);
	}
	
	/**
	 * 根据主键获取动态
	 * @param updateId
	 * @return
	 */
	public MemberUpdate obtainUpdateById(Long updateId)
	{
		return updateMapper.selectByPrimaryKey(updateId);
	}

	/**
	 * 搜索心友圈列表, 按创建时间倒序
	 * @param words 模糊匹配词
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<MemberUpdate> searchUpdate(String words, Date begin, Date end, Integer pageIndex,
			Integer pageSize)
	{
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		
		return updateMapper.searchUpdate(page.getPageStartNum(), pageSize, begin, end, words);
	}
	
	public Integer countSearchUpdate(String words, Date begin, Date end)
	{
		return updateMapper.countSearchUpdate(begin, end, words);
	}
	
	/**
	 * 按时间倒序获取心友圈动态
	 * @param isAnony
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<MemberUpdate> obtainUpdate3Page0TmDesc(Byte isAnony, Long mid, Integer pageIndex,	Integer pageSize,Long releaseFrom)
	{
		MemberUpdate update = new MemberUpdate();
		update.setIsAnony(isAnony);
		update.setMid(mid);
		update.setPageIndex(pageIndex);
		update.setPageSize(pageSize);
		update.setReleaseFrom(releaseFrom);
		return updateMapper.selectSelective3Page0TmDesc(update);
	}
	
	public Integer countUpdate(Byte isAnony, Long mid,Long releaseFrom)
	{
		MemberUpdate update = new MemberUpdate();
		update.setMid(mid);
		update.setIsAnony(isAnony);
		update.setReleaseFrom(releaseFrom);
		return updateMapper.countSelective(update);
	}
	
	/**
	 * 获取我关注的人的非匿名动态
	 * @param mid
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<MemberUpdate> obtainUpdateConcerned(Long mid, Integer pageIndex, Integer pageSize,Long releaseFrom)
	{
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		
		return updateMapper.selectConcerned(page.getPageStartNum(), pageSize, mid,releaseFrom);
	}
	
	public Integer countUpdateConcerned(Long mid,Long releaseFrom)
	{
		return updateMapper.countConcerned(mid,releaseFrom);
	}
	
	/**
	 * 删除心友圈更新
	 * @param updateId
	 * @return
	 */
	public Integer deleteUpdate(Long updateId)
	{
		return updateMapper.deleteByPrimaryKey(updateId);
	}
	
	/**
	 * 修改动态拥抱数
	 * @param updateId
	 * @param delta 修改量，可正负
	 * @return
	 */
	public Integer transUpdateEmbraceNum(Long updateId, int delta)
	{
		MemberUpdate update = updateMapper.selectByPrimaryKeyLock(updateId);
		update.setEmbraceNum(update.getEmbraceNum() + delta);
		
		return updateMapper.updateByPrimaryKeySelective(update);
	}
	/**
	 * 动态阅读数加1
	 * @param updateId
	 * @return
	 */
	public Integer transUpdateReadCount(Long updateId)
	{
		MemberUpdate update = updateMapper.selectByPrimaryKeyLock(updateId);
		update.setReadCount(update.getReadCount() + 1);
		
		return updateMapper.updateByPrimaryKeySelective(update);
	}
	
	/**
	 * 创建动态图片
	 * 
	 * @param img
	 * @return
	 */
	public int createImg(MemberUpdateImgs img)
	{
		return imgsMapper.insertSelective(img);
	}

	/**
	 * 根据动态id获取图片
	 * @param updateId
	 * @return
	 */
	public List<MemberUpdateImgs> obtainImgsByUpdateId(Long updateId)
	{
		MemberUpdateImgs img = new MemberUpdateImgs();
		img.setUpdateId(updateId);
		
		return imgsMapper.selectSelective(img);
	}

	/**
	 * 删除心友圈中的图片
	 * @param advisoryImgId
	 * @return
	 */
	public int deleteImg(Long updImgId)
	{
		return imgsMapper.deleteByPrimaryKey(updImgId);
	}

	/**
	 * 创建一条动态评论
	 * 
	 * @param comment
	 * @return
	 */
	public int createComment(MemberUpdateComment comment)
	{
		comment.setCommentTime(new Date());
		return commentMapper.insertSelective(comment);
	}
	
	/**
	 * 根据主键查询评论
	 * @param commentId
	 * @return
	 */
	public MemberUpdateComment obtainCommentById(Long commentId)
	{
		return commentMapper.selectByPrimaryKey(commentId);
	}
	
	/**
	 * 根据动态id分页获取评论
	 * @param updateId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<MemberUpdateComment> obtainComments8UpdateId3Page(Long updateId, Integer pageIndex, Integer pageSize)
	{
		MemberUpdateComment muc = new MemberUpdateComment();
		muc.setUpdateId(updateId);
		muc.setPageIndex(pageIndex);
		muc.setPageSize(pageSize);
		return commentMapper.selectSelectiveWithPageOrderBy(muc);
	}
	
	public Integer countComments8UpdateId(Long updateId)
	{
		MemberUpdateComment muc = new MemberUpdateComment();
		muc.setUpdateId(updateId);
		
		return commentMapper.countSelective(muc);
	}
	
	/**
	 * 获取最近的动态评论
	 * @param updateId
	 * @return
	 */
	public MemberUpdateComment obtainCommentNewest8UpdateId(Long updateId)
	{
		MemberUpdateComment muc = new MemberUpdateComment();
		muc.setUpdateId(updateId);
		muc.setPageIndex(1);
		muc.setPageSize(1);
		
		List<MemberUpdateComment> comments = commentMapper.select3Page0TmDesc(muc);
		if(comments.size() == 0)
		{
			return null;
		}else
		{
			return comments.get(0);
		}
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
	 * 创建一条详情
	 * 
	 * @param detail
	 * @return
	 */
	public int createDetail(MemberUpdateDetail detail)
	{
		return detailMapper.insertSelective(detail);
	}
	
	/**
	 * 根据动态id获取详情
	 * @param updateId
	 * @return
	 */
	public MemberUpdateDetail obtainDetailByUpdateId(Long updateId)
	{
		MemberUpdateDetail detail = new MemberUpdateDetail();
		detail.setUpdateId(updateId);
		
		List<MemberUpdateDetail> details = detailMapper.selectSelective(detail);

		return details.size()==0?null:details.get(0);
	}
	
	/**
	 * 拥抱动态
	 * 
	 * @param updateId
	 * @param mid
	 * @return
	 */
	public void embraceUpdate(Long updateId, Long mid)
	{
		MemberUpdateEmbrace embrace = new MemberUpdateEmbrace();
		embrace.setUpdateId(updateId);
		embrace.setMid(mid);
		
		embraceMapper.insertSelective(embrace);
		
		transUpdateEmbraceNum(updateId, 1);
	}
	
	/**
	 * 取消拥抱动态
	 * @param updateId
	 * @param mid
	 * @return
	 */
	public void unembraceUpdate(Long updateId, Long mid)
	{
		MemberUpdateEmbrace embrace = new MemberUpdateEmbrace();
		embrace.setUpdateId(updateId);
		embrace.setMid(mid);
		
		List<MemberUpdateEmbrace> embraces = embraceMapper.selectSelective(embrace);
		for(MemberUpdateEmbrace emb : embraces)
		{
			embraceMapper.deleteByPrimaryKey(emb.getEmberaceId());
		}
		
		transUpdateEmbraceNum(updateId, -1);
	}
	
	/**
	 * 是否已经拥抱动态 0未拥抱 1已拥抱
	 * @param updateId
	 * @param mid
	 * @return
	 */
	public int isEmbracedUpdate(Long updateId, Long mid)
	{
		MemberUpdateEmbrace embrace = new MemberUpdateEmbrace();
		embrace.setUpdateId(updateId);
		embrace.setMid(mid);
		
		List<MemberUpdateEmbrace> embraces = embraceMapper.selectSelective(embrace);
		
		return embraces.size()==0?0:1;
	}

	
	/**
	 * 搜索eap中的心友圈信息
	 * @param words 
	 * @param eeId 企业id
	 * @param begin
	 * @param end
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<MemberUpdate> searchUpdateInEap(String words, Long eeId,Long mid,
			Date begin, Date end, Integer pageIndex, Integer pageSize) {
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		
		return updateMapper.searchUpdateInEap(page.getPageStartNum(), pageSize, begin, end, words,eeId,mid);
	}

	/**
	 * 统计eap中的心友圈信息
	 * @param words
	 * @param eeId
	 * @param begin
	 * @param end
	 * @return
	 */
	public Integer countSearchUpdateInEap(String words, Long eeId,Long mid, Date begin,
			Date end) {
		return updateMapper.countSearchUpdateInEap(words,eeId,mid,begin,end);
	}
}
