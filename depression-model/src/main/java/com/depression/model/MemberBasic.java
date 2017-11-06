package com.depression.model;

public class MemberBasic extends Page
{
    private Long mid;
    // 会员头像
    private String avatar;
    // 会员头像縮略圖
    private String avatarThumbnail;
    // 会员昵称
    private String nickname;
    // 会员im账号
    private String imAccount;
    //用户类型
    private Byte userType;

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

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getImAccount()
    {
        return imAccount;
    }

    public void setImAccount(String imAccount)
    {
        this.imAccount = imAccount;
    }

    public String getAvatarThumbnail()
    {
        return avatarThumbnail;
    }

    public void setAvatarThumbnail(String avatarThumbnail)
    {
        this.avatarThumbnail = avatarThumbnail;
    }

    public Byte getUserType()
    {
        return userType;
    }

    public void setUserType(Byte userType)
    {
        this.userType = userType;
    }
}
