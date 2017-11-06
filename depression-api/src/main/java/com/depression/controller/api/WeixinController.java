package com.depression.controller.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.depression.base.wechat.constant.ConstantWeChat;
import com.depression.base.wechat.entity.AccessTokenOAuth;
import com.depression.base.wechat.entity.message.resp.Article;
import com.depression.base.wechat.entity.message.resp.NewsMessage;
import com.depression.base.wechat.entity.message.resp.TextMessage;
import com.depression.base.wechat.entity.user.UserWeiXin;
import com.depression.base.wechat.service.MessageService;
import com.depression.base.wechat.service.OAuthService;
import com.depression.base.wechat.service.SignService;
import com.depression.base.wechat.util.MessageUtil;
import com.depression.base.wechat.util.WeixinUtil;
import com.depression.entity.Constant;
import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.Member;
import com.depression.model.MemberWechat;
import com.depression.model.PersonalityCharactersSimilarity;
import com.depression.model.PersonalityTestMemberMapping;
import com.depression.model.PersonalityTestShareGroup;
import com.depression.model.WecharUserInfo;
import com.depression.service.MemberService;
import com.depression.service.MemberWechatService;
import com.depression.service.PersonalityCharactersSimilarityService;
import com.depression.service.PersonalityTestMemberMappingService;
import com.depression.service.PersonalityTestShareGroupService;
import com.depression.service.TestingQuestionsService;
import com.depression.service.TestingService;
import com.depression.utils.Configuration;

@Controller
@RequestMapping("/weixinCore")
public class WeixinController
{

	private Logger log = Logger.getLogger(WeixinController.class);

	@Autowired
	private MemberService memberService;

	@Autowired
	private PersonalityTestMemberMappingService personalityTestMemberMappingService;

	@Autowired
	private PersonalityTestShareGroupService personalityTestShareGroupService;

	@Autowired
	private PersonalityCharactersSimilarityService personalityCharactersSimilarityService;

	@Autowired
	private TestingService testingService;
	
	@Autowired
	private TestingQuestionsService testingQuestionsService;
	@Autowired
	MemberWechatService memberWechatService;
	@RequestMapping(method = RequestMethod.GET, value = "/api")
	public void get(HttpServletRequest request, HttpServletResponse response)
	{
		// 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");

		PrintWriter out = null;
		try
		{
			out = response.getWriter();
			// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，否则接入失败
			if (SignService.checkSignature(signature, timestamp, nonce))
			{
				out.print(echostr);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			out.close();
			out = null;
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/api")
	public void post(HttpSession session, HttpServletRequest request, HttpServletResponse response)
	{
		// 在这里可处理用户请求
		this.processWebchatRequest(session, request);
	}

	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return String
	 */
	private String processWebchatRequest(HttpSession session, HttpServletRequest request)
	{
		String respMessage = null;
		try
		{
			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			log.info("requestMap ===" + requestMap);

			// 消息类型
			String msgType = requestMap.get("MsgType");
			TextMessage textMessage = (TextMessage) MessageService.bulidBaseMessage(requestMap, ConstantWeChat.RESP_MESSAGE_TYPE_TEXT);
			NewsMessage newsMessage = (NewsMessage) MessageService.bulidBaseMessage(requestMap, ConstantWeChat.RESP_MESSAGE_TYPE_NEWS);

			String respContent = "";
			// 文本消息
			if (msgType.equals(ConstantWeChat.REQ_MESSAGE_TYPE_TEXT))
			{
				// 接收用户发送的文本消息内容
				String content = requestMap.get("Content");
				// 创建图文消息
				List<Article> articleList = new ArrayList<Article>();
				// 单图文消息
				if ("1".equals(content))
				{
					Article article = new Article();
					article.setTitle("我是一条单图文消息");
					article.setDescription("我是描述信息");
					article.setPicUrl("http://cn.bing.com/");
					article.setUrl("http://cn.bing.com/");
					articleList.add(article);
					// 设置图文消息个数
					newsMessage.setArticleCount(articleList.size());
					// 设置图文消息包含的图文集合
					newsMessage.setArticles(articleList);
					// 将图文消息对象转换成xml字符串
					respMessage = MessageService.bulidSendMessage(newsMessage, ConstantWeChat.RESP_MESSAGE_TYPE_NEWS);
				}
				// 多图文消息
				else if ("3".equals(content))
				{

					Article article1 = new Article();
					article1.setTitle("我是一条多图文消息");
					article1.setDescription("");
					article1.setPicUrl("http://cn.bing.com/");
					article1.setUrl("http://cn.bing.com/");

					Article article2 = new Article();
					article2.setTitle("微信公众平台开发教程Java版（二）接口配置 ");
					article2.setDescription("");
					article2.setPicUrl("http://cn.bing.com/");
					article2.setUrl("http://cn.bing.com/");

					Article article3 = new Article();
					article3.setTitle("微信公众平台开发教程Java版(三) 消息接收和发送");
					article3.setDescription("");
					article3.setPicUrl("http://cn.bing.com/");
					article3.setUrl("http://cn.bing.com/");

					articleList.add(article1);
					articleList.add(article2);
					articleList.add(article3);
					newsMessage.setArticleCount(articleList.size());

					newsMessage.setArticles(articleList);
					respMessage = MessageService.bulidSendMessage(newsMessage, ConstantWeChat.RESP_MESSAGE_TYPE_NEWS);
				}
			} else if (msgType.equals(ConstantWeChat.REQ_MESSAGE_TYPE_VOICE))
			{
				textMessage.setContent("您说的是：" + requestMap.get("Recognition"));
				respMessage = MessageService.bulidSendMessage(textMessage, ConstantWeChat.RESP_MESSAGE_TYPE_TEXT);
				// 事件处理开始
			} else if (msgType.equals(ConstantWeChat.REQ_MESSAGE_TYPE_EVENT))
			{
				// 事件类型
				String eventType = requestMap.get("Event");

				if (eventType.equals(ConstantWeChat.EVENT_TYPE_SUBSCRIBE))
				{
					// 关注
					respContent = "感谢您关注,这里会给您提供最新的公司资讯和公告！\n";
					StringBuffer contentMsg = new StringBuffer();
					contentMsg.append("您还可以回复下列数字，体验相应服务").append("\n\n");
					contentMsg.append("1  我就是个测试的").append("\n");
					contentMsg.append("2  我就是个测试的").append("\n");
					contentMsg.append("3  我就是个测试的").append("\n");
					respContent = respContent + contentMsg.toString();

				} else if (eventType.equals(ConstantWeChat.EVENT_TYPE_UNSUBSCRIBE))
				{
					// 取消关注,用户接受不到我们发送的消息了，可以在这里记录用户取消关注的日志信息

				} else if (eventType.equals(ConstantWeChat.EVENT_TYPE_CLICK))
				{

					// 事件KEY值，与创建自定义菜单时指定的KEY值对应
					String eventKey = requestMap.get("EventKey");

					// 自定义菜单点击事件
					if (eventKey.equals("heartTesting"))
					{
						respContent = "天气预报菜单项被点击！";
					} else if (eventKey.equals("12"))
					{
						respContent = "公交查询菜单项被点击！";
					}
				}
				textMessage.setContent(respContent);
				respMessage = MessageService.bulidSendMessage(textMessage, ConstantWeChat.RESP_MESSAGE_TYPE_TEXT);

			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return respMessage;
	}

	/**
	 * 获取人格测试url
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getPersonalityTestUrl")
	@ResponseBody
	public Object getPersonalityTestUrl(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "shareId", required = false) Integer shareId)
	{
		ResultEntity result = new ResultEntity();
		if (shareId != null)
		{
			String redirectUrl = "http://xinmao2.s1.natapp.cc/depression-api/weixinCore/authResponseStuTest?shareId=" + shareId;
			String curUrl = OAuthService.getOauthUrl(redirectUrl, "utf-8", "snsapi_userinfo");
			result.put("url", curUrl);
		} else
		{
			String redirectUrl = "http://api.120xinmao.com/depression-api/weixinCore/authResponseStuTestForSelf";
			String curUrl = OAuthService.getOauthUrl(redirectUrl, "utf-8", "snsapi_userinfo");
			result.put("url", curUrl);
		}

		return result;
	}

	/**
	 * 微信用户信息登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/userLogin")
	@ResponseBody
	public Object userLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam(value = "openid", required = false) String openid)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());

		// 参数检查
		if (openid == null)
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setError(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			log.error(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		// 获取微信用户详细信息
		MemberWechat memberWechat = memberWechatService.obtainWechatByPublicOpenid(openid);

		if (memberWechat != null)
		{
			// 微信用户自动登陆
			session.setAttribute(Constant.LOGINED, memberWechat.getMid());
			log.info("微信用户登陆成功！");

		} else
		{
			result.setCode(ErrorCode.ERROR_WECHAT_LOGIN_FAILED.getCode());
			result.setError(ErrorCode.ERROR_WECHAT_LOGIN_FAILED.getMessage());
			log.error(ErrorCode.ERROR_WECHAT_LOGIN_FAILED.getMessage());
			return result;
		}

		return result;
	}

	/**
	 * 获取微信用户信息，是否已经做过测试
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getWeixinUserInfo")
	@ResponseBody
	public Object getWeixinUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam(value = "openid", required = false) String openid)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());

		// 参数检查
		if (openid == null)
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setError(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			log.error(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		Member curMem = null;
		// 获取微信用户详细信息
		MemberWechat memberWechat = memberWechatService.obtainWechatByPublicOpenid(openid);
		if(memberWechat != null)
		{
			WecharUserInfo wecharUserInfo = new WecharUserInfo();
			BeanUtils.copyProperties(memberWechat, wecharUserInfo);
			wecharUserInfo.setSex(String.valueOf(memberWechat.getSex()));
			wecharUserInfo.setOpenid(memberWechat.getPublicOpenid());
			result.put("obj", wecharUserInfo);
			
			curMem = memberService.selectMemberByMid(memberWechat.getMid());
		}

		// 检查该用户是否已经做过测试
		if (curMem != null)
		{
			PersonalityTestMemberMapping record = new PersonalityTestMemberMapping();
			record.setMid(Long.parseLong(curMem.getMid().toString()));
			List<PersonalityTestMemberMapping> mappingList = personalityTestMemberMappingService.selectSelective(record);
			if (mappingList != null && mappingList.size() > 0)
			{
				result.put("PersonalityTestStatus", 1);
			} else
			{
				result.put("PersonalityTestStatus", 0);
			}
		}

		return result;
	}

	/**
	 * 人格测试微信回调，获取用户信息，并进行自动登录
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/authResponsePersonalityTest")
	public ModelAndView authResponsePersonalityTest(HttpServletRequest request, HttpServletResponse response, HttpSession session, String code,
			@RequestParam(value = "shareId", required = false) Long shareId)
	{

		if (code == null)
		{
			log.error(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			log.error("调用微信登陆接口失败，请重试");
		}

		AccessTokenOAuth accessTokenOAuth = OAuthService.getOAuthAccessToken(code);
		String accessToken = accessTokenOAuth.getAccessToken();
		UserWeiXin userWeiXin = OAuthService.getUserInfoOauth(accessToken, ConstantWeChat.APPID);

		// 查询微信用户
		Long mid = null;
		MemberWechat memberWechat = memberWechatService.obtainWechatByPublicOpenid(userWeiXin.getOpenid());
		if(memberWechat == null)
		{
			// 1.微信用户自动注册为会员
			// 2.登录完成自动登录
			MemberWechat mw = new MemberWechat();
			mw.setNickname(userWeiXin.getNickname());
			mw.setHeadimgurl(userWeiXin.getHeadimgurl());
			mw.setSex(userWeiXin.getSex().byteValue());
			mw.setCountry(userWeiXin.getCountry());
			mw.setProvince(userWeiXin.getProvince());
			mw.setCity(userWeiXin.getCity());
			mw.setPublicOpenid(userWeiXin.getOpenid());
			Member curMem = memberWechatService.transCreateMemberByWechat(mw);
			
			mid = curMem.getMid();
		}else
		{
			mid = memberWechat.getMid();
		}

		// 生成自己的分享分组
		PersonalityTestShareGroup queryGroup = new PersonalityTestShareGroup();
		queryGroup.setMid(mid);
		List<PersonalityTestShareGroup> existGroupList = personalityTestShareGroupService.selectSelective(queryGroup);
		Long gid = 0L;
		if (existGroupList == null || existGroupList.size() == 0)
		{
			PersonalityTestShareGroup group = new PersonalityTestShareGroup();
			group.setMid(mid);
			personalityTestShareGroupService.insertSelective(group);
			gid = group.getgId();
		} else
		{
			gid = existGroupList.get(0).getgId();
		}
		log.info("生成自己的分享分组成功！");

		if (shareId != null)
		{

			// 获取分享人的会员id
			PersonalityTestShareGroup group = personalityTestShareGroupService.selectByPrimaryKey(shareId);
			Long midOther = 0L;
			if (group != null)
			{
				midOther = group.getMid();
			}
			log.info("获取分享人的会员id成功！分享人id：" + midOther);

			// 排除用户自己点击自己分享的链接
			if (mid.longValue() != midOther.longValue())
			{
				// 将自己加入分享人的分组中
				log.info("shareId : " + shareId);
				PersonalityCharactersSimilarity querySimilaritySelf = new PersonalityCharactersSimilarity();
				querySimilaritySelf.setgId(Long.parseLong(shareId.toString()));
				querySimilaritySelf.setMid(mid);
				List<PersonalityCharactersSimilarity> existSimList = personalityCharactersSimilarityService.selectSelective(querySimilaritySelf);
				if (existSimList == null || existSimList.size() == 0)
				{
					PersonalityCharactersSimilarity similaritySelf = new PersonalityCharactersSimilarity();
					similaritySelf.setgId(Long.parseLong(shareId.toString()));
					similaritySelf.setMid(mid);
					personalityCharactersSimilarityService.insertSelective(similaritySelf);
				}
				log.info("将自己加入分享人的分组中成功！");

				// 将分享人加入自己分组中
				querySimilaritySelf = new PersonalityCharactersSimilarity();
				querySimilaritySelf.setgId(gid);
				querySimilaritySelf.setMid(midOther);
				existSimList = personalityCharactersSimilarityService.selectSelective(querySimilaritySelf);
				if (existSimList == null || existSimList.size() == 0)
				{
					PersonalityCharactersSimilarity similarityOther = new PersonalityCharactersSimilarity();
					log.info("自己的分组id：" + gid);
					similarityOther.setgId(gid);
					similarityOther.setMid(midOther);
					personalityCharactersSimilarityService.insertSelective(similarityOther);
				}
				log.info("将分享人加入自己分组中成功！");

				// 检查该用户是否已经做过测试
				if (mid != null)
				{
					PersonalityTestMemberMapping record = new PersonalityTestMemberMapping();
					record.setMid(mid);
					List<PersonalityTestMemberMapping> mappingList = personalityTestMemberMappingService.selectSelective(record);

					// 用户已经做过测试，更新特征数据
					if (mappingList != null && mappingList.size() > 0)
					{
						personalityTestShareGroupService.insertSimilarityAndHarmony(mid, midOther);
					}
				}
			}

		}

		// 此处重定向地址应配置为前端性格测试地址
		log.info("redirect to :" + Configuration.WECHAT_FRONT_APPLICATION_DISC_URL + "?openid=" + userWeiXin.getOpenid());
		ModelAndView mv = new ModelAndView("redirect:" + Configuration.WECHAT_FRONT_APPLICATION_DISC_URL + "?openid=" + userWeiXin.getOpenid());
		return mv;
	}

	/**
	 * 专业测试微信回调，获取用户信息，并进行自动登录
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/authResponseProfessionalTest")
	public ModelAndView authResponseProfessionalTest(HttpServletRequest request, HttpServletResponse response, HttpSession session, String code)
	{

		if (code == null)
		{
			log.error(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			log.error("调用微信登陆接口失败，请重试");
		}

		AccessTokenOAuth accessTokenOAuth = OAuthService.getOAuthAccessToken(code);
		String accessToken = accessTokenOAuth.getAccessToken();
		UserWeiXin userWeiXin = OAuthService.getUserInfoOauth(accessToken, ConstantWeChat.APPID);

		// 查询微信用户
		Long mid = null;
		MemberWechat memberWechat = memberWechatService.obtainWechatByPublicOpenid(userWeiXin.getOpenid());
		if(memberWechat == null)
		{
			// 1.微信用户自动注册为会员
			// 2.登录完成自动登录
			MemberWechat mw = new MemberWechat();
			mw.setNickname(userWeiXin.getNickname());
			mw.setHeadimgurl(userWeiXin.getHeadimgurl());
			mw.setSex(userWeiXin.getSex().byteValue());
			mw.setCountry(userWeiXin.getCountry());
			mw.setProvince(userWeiXin.getProvince());
			mw.setCity(userWeiXin.getCity());
			mw.setPublicOpenid(userWeiXin.getOpenid());
			Member curMem = memberWechatService.transCreateMemberByWechat(mw);
			
			mid = curMem.getMid();
		}else
		{
			mid = memberWechat.getMid();
		}
		
		// 此处重定向地址应配置为前端专业测试地址
		log.info("redirect to :" + Configuration.WECHAT_FRONT_APPLICATION_PROFESSIONAL_TEST_URL + "?mid=" + mid);
		ModelAndView mv = new ModelAndView("redirect:" + Configuration.WECHAT_FRONT_APPLICATION_PROFESSIONAL_TEST_URL + "?mid=" + mid);
		return mv;
	}

	/**
	 * 趣味测试微信回调，获取用户信息，并进行自动登录
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/authResponsefunnyTest")
	public ModelAndView authResponsefunnyTest(HttpServletRequest request, HttpServletResponse response, HttpSession session, String code)
	{

		if (code == null)
		{
			log.error(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			log.error("调用微信登陆接口失败，请重试");
		}

		AccessTokenOAuth accessTokenOAuth = OAuthService.getOAuthAccessToken(code);
		String accessToken = accessTokenOAuth.getAccessToken();
		UserWeiXin userWeiXin = OAuthService.getUserInfoOauth(accessToken, ConstantWeChat.APPID);

		// 查询微信用户
		Long mid = null;
		MemberWechat memberWechat = memberWechatService.obtainWechatByPublicOpenid(userWeiXin.getOpenid());
		if(memberWechat == null)
		{
			// 1.微信用户自动注册为会员
			// 2.登录完成自动登录
			MemberWechat mw = new MemberWechat();
			mw.setNickname(userWeiXin.getNickname());
			mw.setHeadimgurl(userWeiXin.getHeadimgurl());
			mw.setSex(userWeiXin.getSex().byteValue());
			mw.setCountry(userWeiXin.getCountry());
			mw.setProvince(userWeiXin.getProvince());
			mw.setCity(userWeiXin.getCity());
			mw.setPublicOpenid(userWeiXin.getOpenid());
			Member curMem = memberWechatService.transCreateMemberByWechat(mw);
			
			mid = curMem.getMid();
		}else
		{
			mid = memberWechat.getMid();
		}

		// 此处重定向地址应配置为前端趣味测试地址
		log.info("redirect to :" + Configuration.WECHAT_FRONT_APPLICATION_FUNNY_TEST_URL + "?mid=" + mid);
		ModelAndView mv = new ModelAndView("redirect:" + Configuration.WECHAT_FRONT_APPLICATION_FUNNY_TEST_URL + "?mid=" + mid);
		return mv;
	}

	/**
	 * 学生测试微信回调，获取用户信息，并进行自动登录（超级课堂）
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/authResponseStuTest")
	public ModelAndView authResponseStuTest(HttpServletRequest request, HttpServletResponse response, HttpSession session, String code,
			@RequestParam(value = "testingId", required = false) Long testingId)
	{

		if (code == null)
		{
			log.error(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			log.error("调用微信登陆接口失败，请重试");
		}

		AccessTokenOAuth accessTokenOAuth = OAuthService.getOAuthAccessToken(code);
		String accessToken = accessTokenOAuth.getAccessToken();
		UserWeiXin userWeiXin = OAuthService.getUserInfoOauth(accessToken, ConstantWeChat.APPID);

		// 查询微信用户
		Long mid = null;
		MemberWechat memberWechat = memberWechatService.obtainWechatByPublicOpenid(userWeiXin.getOpenid());
		if(memberWechat == null)
		{
			// 1.微信用户自动注册为会员
			// 2.登录完成自动登录
			MemberWechat mw = new MemberWechat();
			mw.setNickname(userWeiXin.getNickname());
			mw.setHeadimgurl(userWeiXin.getHeadimgurl());
			mw.setSex(userWeiXin.getSex().byteValue());
			mw.setCountry(userWeiXin.getCountry());
			mw.setProvince(userWeiXin.getProvince());
			mw.setCity(userWeiXin.getCity());
			mw.setPublicOpenid(userWeiXin.getOpenid());
			Member curMem = memberWechatService.transCreateMemberByWechat(mw);
			
			mid = curMem.getMid();
		}else
		{
			mid = memberWechat.getMid();
		}

		// 此处重定向地址应配置为前端学生测试地址
		String params = "";
		if (testingId != null)
		{
			params = "problem/" + testingId + "?tempMid=" + mid;
		} else
		{
			params = "?tempMid=" + mid;
		}
		log.info("redirect to :" + Configuration.WECHAT_FRONT_APPLICATION_STU_TEST_URL + params);
		ModelAndView mv = new ModelAndView("redirect:" + Configuration.WECHAT_FRONT_APPLICATION_STU_TEST_URL + params);
		return mv;
	}

	/**
	 * 学生测试微信回调，获取用户信息，并进行自动登录（自己微站使用）
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/authResponseStuTestForSelf")
	public ModelAndView authResponseStuTestForSelf(HttpServletRequest request, HttpServletResponse response, HttpSession session, String code,
			@RequestParam(value = "testingId", required = false) Long testingId)
	{

		if (code == null)
		{
			log.error(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			log.error("调用微信登陆接口失败，请重试");
		}

		AccessTokenOAuth accessTokenOAuth = OAuthService.getOAuthAccessToken(code);
		String accessToken = accessTokenOAuth.getAccessToken();
		UserWeiXin userWeiXin = OAuthService.getUserInfoOauth(accessToken, ConstantWeChat.APPID);

		// 查询微信用户
		Long mid = null;
		MemberWechat memberWechat = memberWechatService.obtainWechatByPublicOpenid(userWeiXin.getOpenid());
		if(memberWechat == null)
		{
			// 1.微信用户自动注册为会员
			// 2.登录完成自动登录
			MemberWechat mw = new MemberWechat();
			mw.setNickname(userWeiXin.getNickname());
			mw.setHeadimgurl(userWeiXin.getHeadimgurl());
			mw.setSex(userWeiXin.getSex().byteValue());
			mw.setCountry(userWeiXin.getCountry());
			mw.setProvince(userWeiXin.getProvince());
			mw.setCity(userWeiXin.getCity());
			mw.setPublicOpenid(userWeiXin.getOpenid());
			Member curMem = memberWechatService.transCreateMemberByWechat(mw);
			
			mid = curMem.getMid();
		}else
		{
			mid = memberWechat.getMid();
		}
		
		// 此处重定向地址应配置为前端学生测试地址
		String params = "";
		if (testingId != null)
		{
			params = "problem/" + testingId + "?tempMid=" + mid;
		} else
		{
			params = "?tempMid=" + mid;
		}
		log.info("redirect to :" + Configuration.WECHAT_FRONT_APPLICATION_STU_TEST_SELF_URL + params);
		ModelAndView mv = new ModelAndView("redirect:" + Configuration.WECHAT_FRONT_APPLICATION_STU_TEST_SELF_URL + params);
		return mv;
	}

	/**
	 * JS-SDK使用权限签名
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param url
	 *            签名用的url必须是调用JS接口页面的完整URL
	 * @return
	 */
	@RequestMapping(value = "/getJsSdkSignature")
	@ResponseBody
	public Object getJsSdkSignature(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam(value = "url", required = false) String url)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());

		// 参数检查
		if (url == null)
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setError(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			log.error(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		String jsapi_ticket = WeixinUtil.getTicket();
		String nonce_str = create_nonce_str();
		String timestamp = create_timestamp();
		String strForEncoded;
		String signature = "";

		// 注意这里参数名必须全部小写，且必须有序
		strForEncoded = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
		//System.out.println(strForEncoded);

		// SHA-1签名
		try
		{
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(strForEncoded.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		
		//System.out.println(signature);

		result.put("url", url);
		result.put("jsapi_ticket", jsapi_ticket);
		result.put("nonceStr", nonce_str);
		result.put("timestamp", timestamp);
		result.put("signature", signature);

		return result;
	}

	/**
	 * byte转16进制串
	 * 
	 * @param hash
	 * @return
	 */
	private static String byteToHex(final byte[] hash)
	{
		Formatter formatter = new Formatter();
		for (byte b : hash)
		{
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	/**
	 * 生成签名随机串
	 * 
	 * @return
	 */
	private static String create_nonce_str()
	{
		return UUID.randomUUID().toString();
	}

	/**
	 * 生成时间戳
	 * 
	 * @return
	 */
	private static String create_timestamp()
	{
		return Long.toString(System.currentTimeMillis() / 1000);
	}
}
