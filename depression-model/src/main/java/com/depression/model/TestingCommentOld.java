package com.depression.model;

import java.util.Date;

/**
 * 问卷评论
 *
 * @author ax
 */
public class TestingCommentOld extends Page
{

    private Integer commentId;
    private Long testingId;
    private Long mid;
    private Integer testCommentId;
    private Integer isDelete;
    private String commentContent;
    private Date commentTime;

    // 以下字段用于返回给客户端显示

    // 会员昵称
    private String nickname;
    //会员类型
    private Byte userType;

    // 被艾特的会员id
    private Long eitMid;
    // 被艾特的会员类型
    private Byte eitUserType;
    // 被艾特的会员昵称
    private String eitNickname;
    // 会员头像
    private String avatar;
    // 会员头像缩略图
    private String avatarThumbnail;
    // 被艾特的会员头像
    private String eitAvatar;
    // 被艾特的会员头像缩略图
    private String eitAvatarThumbnail;
    // 会员im账号
    private String imAccount;
    // 被艾特的会员im账号
    private String eitImAccount;

    public Integer getCommentId()
    {
        return commentId;
    }

    public void setCommentId(Integer commentId)
    {
        this.commentId = commentId;
    }

    public Long getTestingId()
    {
        return testingId;
    }

    public void setTestingId(Long testingId)
    {
        this.testingId = testingId;
    }

    public Long getMid()
    {
        return mid;
    }

    public void setMid(Long mid)
    {
        this.mid = mid;
    }

    public Integer getTestCommentId()
    {
        return testCommentId;
    }

    public void setTestCommentId(Integer testCommentId)
    {
        this.testCommentId = testCommentId;
    }

    public Integer getIsDelete()
    {
        return isDelete;
    }

    public String getCommentContent()
    {
        return commentContent;
    }

    public void setCommentContent(String commentContent)
    {
        this.commentContent = commentContent;
    }

    public Date getCommentTime()
    {
        return commentTime;
    }

    public void setCommentTime(Date commentTime)
    {
        this.commentTime = commentTime;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public Long getEitMid()
    {
        return eitMid;
    }

    public void setEitMid(Long eitMid)
    {
        this.eitMid = eitMid;
    }

    public String getEitNickname()
    {
        return eitNickname;
    }

    public void setEitNickname(String eitNickname)
    {
        this.eitNickname = eitNickname;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getEitAvatar()
    {
        return eitAvatar;
    }

    public void setEitAvatar(String eitAvatar)
    {
        this.eitAvatar = eitAvatar;
    }

    public void setIsDelete(Integer isDelete)
    {
        this.isDelete = isDelete;
    }

    public String getImAccount()
    {
        return imAccount;
    }

    public void setImAccount(String imAccount)
    {
        this.imAccount = imAccount;
    }

    public String getEitImAccount()
    {
        return eitImAccount;
    }

    public void setEitImAccount(String eitImAccount)
    {
        this.eitImAccount = eitImAccount;
    }

    public String getAvatarThumbnail()
    {
        return avatarThumbnail;
    }

    public void setAvatarThumbnail(String avatarThumbnail)
    {
        this.avatarThumbnail = avatarThumbnail;
    }

    public String getEitAvatarThumbnail()
    {
        return eitAvatarThumbnail;
    }

    public void setEitAvatarThumbnail(String eitAvatarThumbnail)
    {
        this.eitAvatarThumbnail = eitAvatarThumbnail;
    }

    public Byte getUserType()
    {
        return userType;
    }

    public void setUserType(Byte userType)
    {
        this.userType = userType;
    }

    public Byte getEitUserType()
    {
        return eitUserType;
    }

    public void setEitUserType(Byte eitUserType)
    {
        this.eitUserType = eitUserType;
    }
}
