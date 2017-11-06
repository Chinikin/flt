package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.MemberMapper;
import com.depression.dao.MemberWechatMapper;
import com.depression.model.Member;
import com.depression.model.MemberWechat;

@Service
public class MemberWechatService
{
	@Autowired
	MemberWechatMapper memberWechatMapper;
	@Autowired
	MemberMapper memberMapper;
	
	public static int WECHAT_OPEN = 0; //开放平台
	public static int WECHAT_PUBLIC = 1; //公众平台
	
	/**
	 * 根据会员id查询微信用户
	 * @param mid
	 * @return 不存在则返回null
	 */
	public MemberWechat obtainWechatByMid(Long mid)
	{
		MemberWechat mw = new MemberWechat();
		mw.setMid(mid);
		List<MemberWechat> mws = memberWechatMapper.selectSelective(mw);
		if(mws.size() > 0)
		{
			return mws.get(0);
		}else
		{
			return null;
		}
	}
	
	/**
	 * 根据微信开放平台openid查询微信用户
	 * @param openOpenid
	 * @return 不存在则返回null
	 */
	public MemberWechat obtainWechatByOpenOpenid(String openOpenid)
	{
		MemberWechat mw = new MemberWechat();
		mw.setOpenOpenid(openOpenid);
		List<MemberWechat> mws = memberWechatMapper.selectSelective(mw);
		if(mws.size() > 0)
		{
			return mws.get(0);
		}else
		{
			return null;
		}		
	}
	
	/**
	 * 根据公众平台openid获取微信用户信息
	 * @param publicOpenid 公众平台openid
	 * @return
	 */
	public MemberWechat obtainWechatByPublicOpenid(String publicOpenid)
	{
		MemberWechat mw = new MemberWechat();
		mw.setPublicOpenid(publicOpenid);
		List<MemberWechat> mws = memberWechatMapper.selectSelective(mw);
		if(mws.size() > 0)
		{
			return mws.get(0);
		}else{
			return null;
		}
	}

	/**
	 * 绑定微信开放平台openid
	 * @param mid 会员id	
	 * @param openOpenid 开放平台openid
	 * @return 0 绑定成功
	 * 		   1 用户id不存在
	 *         2 将绑定的openOpenid已经存在，请先解除绑定
	 */
	public Integer transBindWechatOpen(Long mid, String openOpenid)
	{
		Member member = memberMapper.selectByPrimaryKey(mid);
		if(member == null)
		{//用户id不存在
			return 1;
		}
		
		MemberWechat mw0 = obtainWechatByOpenOpenid(openOpenid);
		if(mw0 != null)
		{//将绑定的openOpenid已经存在，请先解除绑定 。或者进一步合并会员信息
			return 2;
		}
		
		MemberWechat mw1 = obtainWechatByMid(mid);
		if(mw1 == null)
		{//微信用户记录不存在，创建新纪录。
			mw1 = new MemberWechat();
			mw1.setMid(mid);
			mw1.setOpenOpenid(openOpenid);
			//TODO : 从微信服务器获取用户基本信息
			mw1.setCreateTime(new Date());
			memberWechatMapper.insertSelective(mw1);
		}else
		{
			mw1.setOpenOpenid(openOpenid);
			mw1.setModifyTime(new Date());
			memberWechatMapper.updateByPrimaryKeySelective(mw1);
		}
		
		//在会员记录中保存openid，兼容遗留代码中的问题，部分代码从会员记录获取openid
		//重构完成后可以去除
		member.setOpenid(openOpenid);
		member.setModifyTime(new Date());
		memberMapper.updateByPrimaryKeySelective(member);
		
		return 0;
	}
	
	/**
	 * 微信用户创建会员记录
	 * @param memberWechat
	 * @return
	 */
	public Member transCreateMemberByWechat(MemberWechat memberWechat)
	{
		//创建会员记录
		Member member = new Member();
		member.setNickname(memberWechat.getNickname());
		member.setSex(memberWechat.getSex());
		//头像需要先保存微信头像
		//member.setAvatar(avatar);
		//member.setAvatarThumbnail(avatarThumbnail);
		//TODO : 注册IM账号
		member.setRegTime(new Date());
		memberMapper.insertSelective(member);
		
		//创建微信会员记录
		memberWechat.setMid(member.getMid());
		//TODO : 从微信服务器获取unionid
		//memberWechat.setUnionid(unionid);
		memberWechat.setCreateTime(new Date());
		memberWechatMapper.insertSelective(memberWechat);
		
		return member;
	}
}
