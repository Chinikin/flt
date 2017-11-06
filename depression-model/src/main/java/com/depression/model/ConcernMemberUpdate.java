package com.depression.model;

import java.util.Date;
import java.util.List;

/**
 * 会员动态
 * 
 * @author hongqian_li
 * @date 2016/05/07
 */
public class ConcernMemberUpdate extends Page
{
	private Long updateId;
	private String content;
	private Long embraceNum;
	private String writeLocation;
	private Date createTime;
	private Long mid;
	private String avatar;
	private String avatarThumbnail;
	private String imAccount;
	private Long mLevel;
	private Integer userType;
	private String nickname;
	private String userName;
	private Long commentCount;
	private Integer isEmbrace;

	private List<MemberUpdateImgs> imgs;

	public Long getUpdateId()
	{
		return updateId;
	}

	public void setUpdateId(Long updateId)
	{
		this.updateId = updateId;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public Long getEmbraceNum()
	{
		return embraceNum;
	}

	public void setEmbraceNum(Long embraceNum)
	{
		this.embraceNum = embraceNum;
	}

	public String getWriteLocation()
	{
		return writeLocation;
	}

	public void setWriteLocation(String writeLocation)
	{
		this.writeLocation = writeLocation;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Long getMid()
	{
		return mid;
	}

	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	public String getAvatar()
	{
		return avatar;
	}

	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}

	public String getImAccount()
	{
		return imAccount;
	}

	public void setImAccount(String imAccount)
	{
		this.imAccount = imAccount;
	}

	public Long getmLevel()
	{
		return mLevel;
	}

	public void setmLevel(Long mLevel)
	{
		this.mLevel = mLevel;
	}


	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public Integer getUserType()
	{
		return userType;
	}

	public void setUserType(Integer userType)
	{
		this.userType = userType;
	}

	public Long getCommentCount()
	{
		return commentCount;
	}

	public void setCommentCount(Long commentCount)
	{
		this.commentCount = commentCount;
	}

	public Integer getIsEmbrace()
	{
		return isEmbrace;
	}

	public void setIsEmbrace(Integer isEmbrace)
	{
		this.isEmbrace = isEmbrace;
	}

	public List<MemberUpdateImgs> getImgs()
	{
		return imgs;
	}

	public void setImgs(List<MemberUpdateImgs> imgs)
	{
		this.imgs = imgs;
	}
	
	public String getAvatarThumbnail()
	{
		return avatarThumbnail;
	}

	public void setAvatarThumbnail(String avatarThumbnail)
	{
		this.avatarThumbnail = avatarThumbnail;
	}
	@Override
	public String toString()
	{
		return "ConcernMemberUpdate [updateId=" + updateId + ", content=" + content + ", embraceNum=" + embraceNum + ", writeLocation=" + writeLocation + ", createTime=" + createTime + ", mid=" + mid
				+ ", avatar=" + avatar + ", imAccount=" + imAccount + ", mLevel=" + mLevel + ", userType=" + userType + ", nickname=" + nickname + ", userName=" + userName + ", commentCount="
				+ commentCount + ", isEmbrace=" + isEmbrace + ", imgs=" + imgs + "]";
	}

}
