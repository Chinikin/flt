package com.depression.controller.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.taobao.api.internal.tmc.Message;
import com.taobao.api.internal.tmc.MessageStatus;
import com.taobao.api.internal.tmc.MessageHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;

import com.depression.entity.Constant;
import com.depression.entity.ErrorCode;
import com.depression.entity.MembersOnlineStatus;
import com.depression.entity.OrderState;
import com.depression.entity.ResultEntity;
import com.depression.model.CapitalCommissionRate;
import com.depression.model.CapitalCouponEntity;
import com.depression.model.CapitalIncomeExpenses;
import com.depression.model.CapitalPersonalAssets;
import com.depression.model.Member;
import com.depression.model.Recommend;
import com.depression.model.ServiceCallRecord;
import com.depression.model.ServiceGoods;
import com.depression.model.ServiceOrder;
import com.depression.model.api.dto.ApiServiceCallDTO;
import com.depression.push.CustomMsgType;
import com.depression.service.CapitalCommissionRateService;
import com.depression.service.CapitalCouponService;
import com.depression.service.CapitalIncomeExpenseService;
import com.depression.service.CapitalPersonalAssetsService;
import com.depression.service.EapService;
import com.depression.service.ElectExpertService;
import com.depression.service.IMMessageService;
import com.depression.service.MemberService;
import com.depression.service.PushService;
import com.depression.service.QiniuService;
import com.depression.service.ServiceCallRecordService;
import com.depression.service.ServiceGoodsService;
import com.depression.service.ServiceOrderService;
import com.depression.service.ServiceStatisticsService;
import com.depression.utils.AliyunIMUtil;
import com.depression.utils.CallCommunicationUtil;
import com.depression.utils.Configuration;
import com.depression.utils.HideCallUtil;
import com.depression.utils.PropertyUtils;
import com.taobao.api.internal.tmc.TmcClient;
import com.taobao.api.internal.toplink.LinkException;

/**
 * 容联双向电话
 * 
 * @author ax
 *
 */
@Controller
@RequestMapping("/callcore")
public class VoiceCallController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private MemberService memberService;

	@Autowired
	private ServiceGoodsService serviceGoodsService;

	@Autowired
	private ServiceOrderService serviceOrderService;

	@Autowired
	private CapitalCouponService capitalCouponService;

	@Autowired
	private CapitalPersonalAssetsService capitalPersonalAssetsService;

	@Autowired
	private CapitalCommissionRateService capitalCommissionRateService;

	@Autowired
	private ServiceCallRecordService serviceCallRecordService;
	
	@Autowired
	private EapService eapService;
	
	@Autowired
	private ServiceStatisticsService serviceStatisticsService;
	@Autowired
	PushService pushService;
	
	@Autowired
	private CapitalIncomeExpenseService capitalIncomeExpenseService;
    @Autowired    
    QiniuService qiniuService;
    @Autowired
    ElectExpertService electExpertService;
    @Autowired
    IMMessageService imMessageService;

	private void dealOrderWhenHangup(Long orderId, Integer duration) throws Exception
	{
		// 以下处理订单逻辑
		ServiceOrder serviceOrder = serviceOrderService.selectOrderByPrimaryKey(orderId);
		if (serviceOrder != null)
		{			
			// 更新被叫人状态
			Member calledMember = memberService.selectMemberByMid(serviceOrder.getServiceProviderId());
			// 通话中状态，才设置为在线状态
			memberService.transCasStatus(serviceOrder.getServiceProviderId(), 
					MembersOnlineStatus.STATUS_IN_THE_CALL.getCode(),
					MembersOnlineStatus.STATUS_ONLINE.getCode()
					);
			
			// 累计实时通话时长
			Long sgid = serviceOrder.getSgid();
			ServiceGoods serviceGoods = serviceOrderService.selectGoodsByPrimaryKey(sgid);
			if (serviceOrder.getPracticalDuration() == null)
			{
				serviceOrder.setPracticalDuration(duration);
			} else
			{
				serviceOrder.setPracticalDuration(serviceOrder.getPracticalDuration() + duration);
			}

			// 查询用户优惠券账号中最大面值的优惠券
			CapitalCouponEntity capitalCoupon = capitalCouponService.getValuabestUsableCashCouponEntity(serviceOrder.getMid());

			// 查询主叫人（客户）账户信息
			CapitalPersonalAssets callerAssets = capitalPersonalAssetsService.selectByMid(serviceOrder.getMid());
			if (callerAssets == null)
			{
				return;
			}

			// 查询被叫人（服务提供者）账户信息
			CapitalPersonalAssets calledAssets = capitalPersonalAssetsService.selectByMid(serviceOrder.getServiceProviderId());
			if (calledAssets == null)
			{
				return;
			}

			// 查找平台佣金比例
			CapitalCommissionRate queryCommissionRate = new CapitalCommissionRate();
			queryCommissionRate.setType(serviceGoods.getType().intValue());
			CapitalCommissionRate curCommissionRate = capitalCommissionRateService.selectSelective(queryCommissionRate);
			if (curCommissionRate == null)
			{
				return;
			}

			log.info("=====================开始业务处理==================");
			// 更新通话订单
			Integer callDurAmount = serviceOrder.getPracticalDuration();
			if (callDurAmount != null && callDurAmount.intValue() != 0 && callDurAmount.intValue() >= 300)// 通话时长超过五分钟，进行扣款，否则不予扣款
			{
				serviceOrder.setStatus(OrderState.STATE_CALL_IN_PROGRESS.getCode()); // 通话时长已超过五分钟，将订单状态设置为“电话进行中”
				if (serviceOrder.getOrderType().intValue() == 0) // 普通订单，需要扣款，eap客户不扣款
				{
					CapitalIncomeExpenses capitalIncomeExpenses = capitalIncomeExpenseService.getIncomeExpensesByOrderNo(serviceOrder.getNo());
					if (capitalIncomeExpenses == null) // 避免二次扣款（明细中无扣款记录才进行扣款）
					{
						serviceOrderService.updateNewTransactions(serviceOrder, serviceGoods, capitalCoupon, callerAssets, calledAssets, curCommissionRate);
					}
				}
				if(serviceOrder.getOrderType().intValue() == 2) // 活动订单，需要扣款
				{
					CapitalIncomeExpenses capitalIncomeExpenses = capitalIncomeExpenseService.getIncomeExpensesByOrderNo(serviceOrder.getNo());
					if (capitalIncomeExpenses == null) // 避免二次扣款（明细中无扣款记录才进行扣款）
					{	
						serviceGoods.setPrice(serviceOrder.getCost());
						serviceOrderService.updateNewTransactions(serviceOrder, serviceGoods, capitalCoupon, callerAssets, calledAssets, curCommissionRate);
					}
				}
				
			} else if (callDurAmount != null && callDurAmount.intValue() != 0 && callDurAmount.intValue() < 300)
			{
				serviceOrder.setStatus(OrderState.STATE_CALL_NOT_CHARGEBECKS.getCode()); // 通话时长未超过五分钟，将订单状态设置为“电话未扣款”
			} else if (callDurAmount != null && callDurAmount.intValue() == 0)
			{
				serviceOrder.setStatus(OrderState.STATE_CALL_NOT_CONNECTED.getCode()); // 通话时长为0，将订单状态设置为“电话未接听”
			}
			serviceOrderService.updateByPrimaryKeySelective(serviceOrder);
			
			// 刷新订单状态
			serviceOrderService.refreshOrderStatus(serviceOrder.getSoid());
			serviceOrder = serviceOrderService.selectOrderByPrimaryKey(orderId);
			
			if (duration > 0)
			{//订单已完成
				log.info("订单类型："+serviceOrder.getOrderType().longValue());
				log.info("本次通话时长："+duration);
				// 将订单状态设置为“电话未评价”
				if (serviceOrder.getOrderType().longValue() == 1) // eap订单
				{
					serviceStatisticsService.updateDuration(serviceOrder.getMid(), serviceOrder.getServiceProviderId(), duration.longValue(), 1, serviceOrder.getEeId());
				} else 
				{
					if(serviceOrder.getOrderType().longValue() == 2)//活动订单
					{
						serviceStatisticsService.updateDuration(serviceOrder.getMid(), serviceOrder.getServiceProviderId(), duration.longValue(), 2, serviceOrder.getEeId());
					}	
					if(serviceOrder.getOrderType().longValue() == 0)// 普通订单
					{
					serviceStatisticsService.updateDuration(serviceOrder.getMid(), serviceOrder.getServiceProviderId(), duration.longValue(), 0, serviceOrder.getEeId());
					}
					// 实时订单推送
					JSONObject obj = new JSONObject();
					obj.put("status", serviceOrder.getStatus());
					obj.put("cashAmount", serviceOrder.getCashAmount());
					obj.put("practicalDuration", serviceOrder.getPracticalDuration());
					int remainingDuration = serviceGoods.getDuration() * 60 - serviceOrder.getPracticalDuration();
					if (remainingDuration < 0)
					{
						remainingDuration = 0;
					}
					obj.put("remainingDuration", remainingDuration);
					obj.put("serviceProviderId", serviceOrder.getServiceProviderId());
					obj.put("orderType",serviceOrder.getOrderType());
					obj.put("orderNo", serviceOrder.getNo());
					obj.put("soid", serviceOrder.getSoid());
					if (null != calledMember)
					{
						obj.put("specialistName", calledMember.getNickname());
						obj.put("specialistTitle", calledMember.getTitle());
						String avatar = calledMember.getAvatar();
						if (!StringUtils.isEmpty(avatar))
						{
							obj.put("specialistAvatar", avatar);
						} else
						{
							obj.put("specialistAvatar", "");
						}
					}
					
					// 推送字段log
					log.info("话单推送：");
					log.info("---status--- " + serviceOrder.getStatus());
					log.info("---cashAmount--- " + serviceOrder.getCashAmount());
					log.info("---practicalDuration--- " + serviceOrder.getPracticalDuration());
					log.info("---remainingDuration--- " + (serviceGoods.getDuration() * 60 - serviceOrder.getPracticalDuration()) );
					log.info("---serviceProviderId--- " + serviceOrder.getServiceProviderId());
					log.info("---orderNo--- " + serviceOrder.getNo());
					log.info("---soid--- " + serviceOrder.getSoid());
					if (null != calledMember)
					{
						log.info("---specialistName--- " + calledMember.getNickname());
						log.info("---specialistTitle--- " + calledMember.getTitle());
						String avatar = calledMember.getAvatar();
						log.info("---specialistAvatar--- " + avatar);
					}
					
					//pushService.pushSingleDevice(CustomMsgType.CALL_BACK_MSG, serviceOrder.getMid(), obj.toString());
					//IM推送
					Member member = memberService.selectMemberByMid(serviceOrder.getMid());
					imMessageService.sendOrderMsg(obj.toString(), member.getImAccount());
				}
			}
			else{
				if (serviceOrder.getOrderType().longValue() == 1) // eap订单
				{
					return ;
				}
			// 实时订单推送
			JSONObject obj = new JSONObject();
			obj.put("status", serviceOrder.getStatus());
			obj.put("cashAmount", serviceOrder.getCashAmount());
			obj.put("practicalDuration", serviceOrder.getPracticalDuration());
			int remainingDuration = serviceGoods.getDuration() * 60 - serviceOrder.getPracticalDuration();
			if (remainingDuration < 0)
			{
				remainingDuration = 0;
			}
			obj.put("remainingDuration", remainingDuration);
			obj.put("serviceProviderId", serviceOrder.getServiceProviderId());
			obj.put("orderNo", serviceOrder.getNo());
			obj.put("orderType",serviceOrder.getOrderType());
			obj.put("soid", serviceOrder.getSoid());
			if (null != calledMember)
			{
				obj.put("specialistName", calledMember.getNickname());
				obj.put("specialistTitle", calledMember.getTitle());
				String avatar = calledMember.getAvatar();
				if (!StringUtils.isEmpty(avatar))
				{
					obj.put("specialistAvatar", avatar);
				} else
				{
					obj.put("specialistAvatar", "");
				}
			}
			
			// 推送字段log
			log.info("话单推送：");
			log.info("---status--- " + serviceOrder.getStatus());
			log.info("---cashAmount--- " + serviceOrder.getCashAmount());
			log.info("---practicalDuration--- " + serviceOrder.getPracticalDuration());
			log.info("---remainingDuration--- " + (serviceGoods.getDuration() * 60 - serviceOrder.getPracticalDuration()) );
			log.info("---serviceProviderId--- " + serviceOrder.getServiceProviderId());
			log.info("---orderNo--- " + serviceOrder.getNo());
			log.info("---soid--- " + serviceOrder.getSoid());
			if (null != calledMember)
			{
				log.info("---specialistName--- " + calledMember.getNickname());
				log.info("---specialistTitle--- " + calledMember.getTitle());
				String avatar = calledMember.getAvatar();
				log.info("---specialistAvatar--- " + avatar);
			}
			
			//pushService.pushSingleDevice(CustomMsgType.CALL_BACK_MSG, serviceOrder.getMid(), obj.toString());
			//IM推送
			Member member = memberService.selectMemberByMid(serviceOrder.getMid());
			imMessageService.sendOrderMsg(obj.toString(), member.getImAccount());
			}
		}
	}
	
	/**
	 * 结束通话通知接口
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/callback.json")
	@ResponseBody
	public Object callback(HttpSession session, HttpServletRequest request)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			// 解析流，打印log
			InputStream in = request.getInputStream();
			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			String str = null;
			StringBuffer xmlfile = new StringBuffer();
			while ((str = bf.readLine()) != null)
			{
				xmlfile.append(str);
			}
			log.info(" --- xml body --- :" + xmlfile);
			Document doc = DocumentHelper.parseText(xmlfile.toString());

			// 读取并解析XML文档
			Element rootElt = doc.getRootElement(); // 获取根节点
			Element orderid = rootElt.element("orderid");
			log.info("orderid:" + orderid.getText());

			// 主叫方
			Element callerCdr = rootElt.element("CallerCdr");
			Element callSid = callerCdr.element("callSid");
			Element duration = callerCdr.element("duration");
			Element byetype = callerCdr.element("byetype");

			// 被叫方
			Element calledCdr = rootElt.element("CalledCdr");
			Element caller = calledCdr.element("caller");
			Element called = calledCdr.element("called");
			Element starttime = calledCdr.element("starttime");
			Element endtime = calledCdr.element("endtime");

			// 录音文件地址
			Element recordurl = rootElt.element("recordurl");

			// 查询通话记录状态
			ServiceCallRecord queryServiceCallRecord = new ServiceCallRecord();
			queryServiceCallRecord.setCallsid(callSid.getText());
			List<ServiceCallRecord> recList = serviceCallRecordService.selectSelective(queryServiceCallRecord);
			if (recList != null && recList.size() > 0)
			{
				// 更新通话记录
				ServiceCallRecord callRecord = recList.get(0);
				callRecord.setCaller(caller.getText());
				callRecord.setCalled(called.getText());
				callRecord.setCalledDuration(Long.parseLong(duration.getText()));
				callRecord.setByetype(Long.parseLong(byetype.getText()));
				if (starttime != null && starttime.getText() != null && !starttime.getText().equals(""))
				{
					Date beginTime = new SimpleDateFormat("yyyyMMddHHmmss").parse(starttime.getText());
					callRecord.setBeginTime(beginTime);
				}
				if (endtime != null && endtime.getText() != null && !endtime.getText().equals(""))
				{
					Date endTime = new SimpleDateFormat("yyyyMMddHHmmss").parse(endtime.getText());
					callRecord.setEndTime(endTime);
				}
				if (recordurl != null)
				{
					callRecord.setRecordUrl(recordurl.getText());
				}
				serviceCallRecordService.updateByPrimaryKey(callRecord);

				// 以下处理订单逻辑
				dealOrderWhenHangup(callRecord.getServiceOrderId(), Integer.parseInt(duration.getText()));
			}
			log.info("-----callback-----");
			log.info("   callSid:" + callSid.getText());
			log.info("   duration:" + duration.getText());
			log.info("   byetype:" + byetype.getText());
			log.info("   caller:" + caller.getText());
			log.info("   called:" + called.getText());

		} catch (Exception e)
		{
			e.printStackTrace();
			log.error("接收容联双向电话回调接口失败:" + e);
		}

		return result;
	}
	
	//直拨都走消息订阅
    @RequestMapping(method = RequestMethod.GET, value = "/startCallServer.json")
	@ResponseBody
	public void alidayuCallBack(){
		TmcClient client = new TmcClient(Configuration.ALIDAYU_APPKEY, Configuration.ALIDAYU_APPSECRET, "default"); // 关于default参考消息分组说明			
		client.setMessageHandler(new MessageHandler() {
			public void onMessage(Message message, MessageStatus status) {
		        try {		        			           		            
		            JSONObject  jasonObject = JSONObject.fromObject(message.getContent());
		            log.info("callback："+jasonObject.toString());
		            ResultEntity result = new ResultEntity();
		    		try{
		    			String subsId = jasonObject.get("extend").toString();
		    			log.info("******************话单消息接收开始******************");
		    			log.info("******************流水号："+subsId);
		    			//获取callSid
		    			ServiceCallRecord queryServiceCallRecord = new ServiceCallRecord();
		    			queryServiceCallRecord.setCallsid(subsId);
		    			List<ServiceCallRecord> recList = serviceCallRecordService.selectSelective(queryServiceCallRecord);
		    			if (recList != null && recList.size() > 0)
		    			{
		    				// 更新通话记录
		    				ServiceCallRecord callRecord = recList.get(0);
		    				callRecord.setCaller(callRecord.getCaller());
		    				callRecord.setCalled(callRecord.getCalled());	
		    				String callId = jasonObject.get("biz_id").toString();
		    				String createTime = null;
		    				if(jasonObject.containsKey("start_time")){
		    					createTime = jasonObject.getString("start_time");
		    				}
		    				if (createTime != null && !createTime.equals(""))
		    				{
		    					Date callTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTime);
		    					callRecord.setCreateTime(callTime);
		    				}
		    				callRecord.setCallsid(callId);
		    				String endtime = null;
		    				if(jasonObject.containsKey("end_time")){
		    					endtime = jasonObject.getString("end_time");
		    				}
		    				//通话时长（单位秒）		    				
		    				long  duration = Long.valueOf(jasonObject.get("duration").toString());		    				
		    				if (endtime != null && !endtime.equals(""))
		    				{
		    					Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endtime);
		    					callRecord.setEndTime(endTime);
		    				}
		    				//解除绑定关系
		    				callRecord.setIsCalling((byte) 0);	 
		    				callRecord.setCalledDuration(duration);
		    				serviceCallRecordService.updateByPrimaryKey(callRecord);
                            		    				
		    				// 以下处理订单逻辑
		    				dealOrderWhenHangup(callRecord.getServiceOrderId(),Integer.valueOf(String.valueOf(duration)));
		    			}		    			
		    			log.info("-----callback-----");
		    			log.info("   callSid:"+"//////////////////////////");
		    			log.info("   caller:" + "//////////////////////////");
		    			log.info("   called:" + "//////////////////////////");
		    			result.setCode(ResultEntity.SUCCESS);
		    			result.setMsg("处理成功");
		    		} catch(Exception e){
		    			e.printStackTrace();
		    			log.error("回拨通话回调接口失败:" + e);
		    		}		            
		        } catch (Exception e) {
		            e.printStackTrace();
		            status.fail(); // 消息处理失败回滚，服务端需要重发
		          // 重试注意：不是所有的异常都需要系统重试。 
		          // 对于字段不全、主键冲突问题，导致写DB异常，不可重试，否则消息会一直重发
		          // 对于，由于网络问题，权限问题导致的失败，可重试。
		          // 重试时间 5分钟不等，不要滥用，否则会引起雪崩
		        }
		    }

			
		});	
		try {
			//client.connect("ws://mc.api.tbsandbox.com/");
			client.connect("ws://mc.api.taobao.com");//线上
		} catch (LinkException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // 消息环境地址：ws://mc.api.tbsandbox.com/
	}	
	
	
	@RequestMapping(value = "/ucpaasCallback")
	public void ucpaasCallback(HttpServletRequest request, HttpServletResponse response)
	{
		try{			
			ApiServiceCallDTO callDTO = new ApiServiceCallDTO();
			// 解析流，打印log
			InputStream in = request.getInputStream();
			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			String str = null;
			StringBuffer xmlfile = new StringBuffer();
			while ((str = bf.readLine()) != null)
			{
				xmlfile.append(str);
			}
			log.info(" --- xml body --- :" + xmlfile);
			Document doc = DocumentHelper.parseText(xmlfile.toString());
			
			// 读取并解析XML文档
			Element rootElt = doc.getRootElement(); // 获取根节点
			if("callhangup".equals(rootElt.element("event").getText()))
			{
				// 主叫方
				Element callSid = rootElt.element("callid");
				Element duration = rootElt.element("length");
	
				// 被叫方
				Element caller = rootElt.element("caller");
				Element called = rootElt.element("called");
				Element starttime = rootElt.element("starttime");
				Element endtime = rootElt.element("stoptime");
	
				// 录音文件地址
				Element recordurl = rootElt.element("recordurl");
				
				// 查询通话记录状态
				ServiceCallRecord queryServiceCallRecord = new ServiceCallRecord();
				queryServiceCallRecord.setCallsid(callSid.getText());
				List<ServiceCallRecord> recList = serviceCallRecordService.selectSelective(queryServiceCallRecord);
				if (recList != null && recList.size() > 0)
				{
					// 更新通话记录
					ServiceCallRecord callRecord = recList.get(0);
					callRecord.setCaller(caller.getText());
					callRecord.setCalled(called.getText());										
					callRecord.setCalledDuration(Long.parseLong(duration.getText()));
					if (starttime != null && starttime.getText() != null && !starttime.getText().equals(""))
					{
						Date beginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(starttime.getText());
						callRecord.setBeginTime(beginTime);
					}
					if (endtime != null && endtime.getText() != null && !endtime.getText().equals(""))
					{
						Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endtime.getText());
						callRecord.setEndTime(endTime);
					}
					if (recordurl != null)
					{
						callRecord.setRecordUrl(recordurl.getText());
						//拼接带签名的url
						String sigedRecordUrl = CallCommunicationUtil.ucpassRecordUrlSig(callRecord.getRecordUrl(), callRecord.getCallsid());
						//上传到七牛
						String filename = String.format("call_record/%d_%s_%s.mp3", callRecord.getServiceOrderId(), starttime.getText(), callRecord.getCalled());
						qiniuService.fetchFile(sigedRecordUrl, filename);
					}
					serviceCallRecordService.updateByPrimaryKey(callRecord);

					// 以下处理订单逻辑
					dealOrderWhenHangup(callRecord.getServiceOrderId(), Integer.parseInt(duration.getText()));
				}
				log.info("-----callback-----");
				log.info("   callSid:" + callSid.getText());
				log.info("   duration:" + duration.getText());
				log.info("   caller:" + caller.getText());
				log.info("   called:" + called.getText());
				
				response.setContentType("text/xml");
				response.setCharacterEncoding("UTF-8");
				PrintWriter out = response.getWriter();
				String xmlstr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <response> <retcode>0</retcode></response>";
				out.println(xmlstr);
			}
		}catch (Exception e)
		{
			e.printStackTrace();
			log.error("接收云之讯双向电话回调接口失败:" + e);
		}
	}
	
	@RequestMapping(value = "/ucpaasHideCallback")
	public void ucpaasHideCallback(HttpServletRequest request, HttpServletResponse response)
	{
		try{			
			ApiServiceCallDTO callDTO = new ApiServiceCallDTO();
			// 解析流，打印log
			InputStream in = request.getInputStream();
			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			String str = null;
			StringBuffer xmlfile = new StringBuffer();
			while ((str = bf.readLine()) != null)
			{
				xmlfile.append(str);
			}
			log.info(" --- xml body --- :" + xmlfile);
			Document doc = DocumentHelper.parseText(xmlfile.toString());
			
			// 读取并解析XML文档
			Element rootElt = doc.getRootElement(); // 获取根节点			
				// 主叫方
				Element callSid = rootElt.element("requestId");
				Element duration = rootElt.element("calleeDuration");
	
				// 被叫方
				Element caller = rootElt.element("caller");
				Element called = rootElt.element("callee");
				Element starttime = rootElt.element("dstAcceptTime");
				Element endtime = rootElt.element("endCallTime");
				Element midId = rootElt.element("bindId");	
				// 查询通话记录状态
				ServiceCallRecord queryServiceCallRecord = new ServiceCallRecord();
			    String callid = callSid.getText().split("@")[1];				
				queryServiceCallRecord.setCallsid(callid);
				List<ServiceCallRecord> recList = serviceCallRecordService.selectSelective(queryServiceCallRecord);
				if (recList != null && recList.size() > 0)
				{
					// 更新通话记录
					ServiceCallRecord callRecord = recList.get(0);
					callRecord.setCaller(caller.getText());
					callRecord.setCalled(called.getText());
					//结束通话删除双方绑定关系				
					//结束通话删除双方绑定关系				
					boolean isSuccess = (boolean) HideCallUtil.ucpaasDeleteCallService(caller.getText().toString(), called.getText().toString(),midId.getText().toString());
					if(!isSuccess){
						log.error("调用删除双向绑定关系接口失败");
					}					
					callRecord.setCalledDuration(Long.parseLong(duration.getText()));
					if (starttime != null && starttime.getText() != null && !starttime.getText().equals(""))
					{
						Date beginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(starttime.getText());
						callRecord.setBeginTime(beginTime);
					}
					if (endtime != null && endtime.getText() != null && !endtime.getText().equals(""))
					{
						Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endtime.getText());
						callRecord.setEndTime(endTime);
					}				
					serviceCallRecordService.updateByPrimaryKey(callRecord);
					// 以下处理订单逻辑
					dealOrderWhenHangup(callRecord.getServiceOrderId(), Integer.parseInt(duration.getText()));
				}
				log.info("-----callback-----");
				log.info("   callSid:" + callSid.getText());
				log.info("   duration:" + duration.getText());
				log.info("   caller:" + caller.getText());
				log.info("   called:" + called.getText());
				
				response.setContentType("text/xml");
				response.setCharacterEncoding("UTF-8");
				PrintWriter out = response.getWriter();
				String xmlstr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <response> <retcode>0</retcode></response>";
				out.println(xmlstr);
			
		}catch (Exception e)
		{
			e.printStackTrace();
			log.error("接收云之讯双向电话回调接口失败:" + e);
		}
	}
	
    
	/**
	 * 接收录音回调地址
	 * 
	 */
	@RequestMapping(value = "/ucpaasRecordUrlCallback")
	public void ucpaasRecordUrlCallback(HttpServletRequest request, HttpServletResponse response)
	{
		try{			
			// 解析流，打印log
			InputStream in = request.getInputStream();
			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			String str = null;
			StringBuffer xmlfile = new StringBuffer();
			while ((str = bf.readLine()) != null)
			{
				xmlfile.append(str);
			}
			log.info(" --- xml body --- :" + xmlfile);
			Document doc = DocumentHelper.parseText(xmlfile.toString());
			
			// 读取并解析XML文档
			Element rootElt = doc.getRootElement(); // 获取根节点
			
				// 主叫方
				Element callSid = rootElt.element("requestId");
				// 录音文件地址
				Element recordurl = rootElt.element("recordurl");

				// 查询通话记录状态
				ServiceCallRecord queryServiceCallRecord = new ServiceCallRecord();
				String callid = callSid.getText().split("@")[1];
				String starttime = callSid.getText().split("@")[0];
				queryServiceCallRecord.setCallsid(callid);
				List<ServiceCallRecord> recList = serviceCallRecordService.selectSelective(queryServiceCallRecord);
				if (recList != null && recList.size() > 0)
				{
					// 更新通话记录
					ServiceCallRecord callRecord = recList.get(0);					
					if (recordurl != null)
					{
						callRecord.setRecordUrl(recordurl.getText());
						//拼接带签名的url
						String sigedRecordUrl = CallCommunicationUtil.ucpassRecordUrlSig(callRecord.getRecordUrl(), callRecord.getCallsid());
						//上传到七牛
						String filename = String.format("call_record/%d_%s_%s.mp3", callRecord.getServiceOrderId(), starttime, callRecord.getCalled());
						qiniuService.fetchFile(sigedRecordUrl, filename);
					}
					serviceCallRecordService.updateByPrimaryKey(callRecord);

				}
				log.info("-----callback-----");
				log.info("   callSid:" + callSid.getText());
				log.info("   recordUrl:" + recordurl.getText());
				
				response.setContentType("text/xml");
				response.setCharacterEncoding("UTF-8");
				PrintWriter out = response.getWriter();
				String xmlstr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <response> <retcode>0</retcode></response>";
				out.println(xmlstr);
			
		}catch (Exception e)
		{
			e.printStackTrace();
			log.error("接收云之讯录音回调接口失败:" + e);
		}
	}
	
	
	
	/**
	 * 检查电话服务是否超时
	 * 
	 * @param serviceGoods
	 * @param existServiceOrder
	 * @return
	 */
	private boolean checkCallServiceTimeout(ServiceGoods serviceGoods, ServiceOrder existServiceOrder)
	{
		long curInterval = (new Date().getTime() - existServiceOrder.getServiceRealityBeginTime().getTime()) / 1000;
		boolean timeoutFlag = false;
		if (curInterval >= 3 * 24 * 60 * 60)// 超过3天，订单结束
		{
			timeoutFlag = true;
		}

		Long maxDuration = Long.parseLong(serviceGoods.getDuration() * 60 + "");
		Long curDur = existServiceOrder.getPracticalDuration().longValue();
		if (curDur >= maxDuration.longValue()) // 超过订单最大时长，订单结束
		{
			timeoutFlag = true;
		}
		return timeoutFlag;
	}

	/**
	 * 计算电话服务剩余时间
	 * 
	 * @param serviceGoods
	 * @param existServiceOrder
	 * @return
	 */
	private long countCallTimeRemind(ServiceGoods serviceGoods, ServiceOrder existServiceOrder)
	{
		Long maxDuration = Long.parseLong(serviceGoods.getDuration() * 60 + "");
		Long curDur = existServiceOrder.getPracticalDuration().longValue();
		Long timeRemind = maxDuration - curDur;
		return timeRemind;
	}
	
	/**
	 * 正常首次拨打电话 正常创建订单 然后拨打电话
	 * 
	 * @param session
	 * @param request
	 * @return
	 */	
	//新版本
	@RequestMapping(value = "/V1/callStartByaliyun.json")
	@ResponseBody
	public Object callStartByaliyun(HttpSession session, HttpServletRequest request, Long callerId, Long calledId,Long thisEeId, Integer callType,Integer callFlag)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());

		// 参数检查		
		if (PropertyUtils.examineOneNull(callerId, calledId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}		

		// 判断EEID是否存在
		if (thisEeId == null)
		{
			result.setCode(ErrorCode.ERROR_CALLER_INFO_INEXISTENT.getCode());
			result.setMsg("EEID错误");
			return result;
		}
		// 查询主叫人信息
		Member caller = memberService.selectMemberByMid(callerId);
		if (caller == null || caller.getMobilePhone() == null)
		{
			result.setCode(ErrorCode.ERROR_CALLER_INFO_INEXISTENT.getCode());
			result.setMsg("主叫人信息未找到");
			return result;
		}

		// 查询被叫人信息
		Member called = memberService.selectMemberByMid(calledId);
		if (called == null || called.getMobilePhone() == null)
		{
			result.setCode(ErrorCode.ERROR_CALLED_INFO_INEXISTENT.getCode());
			result.setMsg("被叫人信息未找到");
			return result;
		}
		//咨询师是否被禁用
		if (called.getIsEnable().intValue() == 1)
		{
			result.setCode(ErrorCode.ERROR_CALLED_INFO_INEXISTENT.getCode());
			result.setMsg("此专家违反平台规则，已被禁止提供咨询服务");
			return result;
		}

		// 查询咨询师是否离线
		Byte calledStatus = called.getStatus();
		if (calledStatus.equals(MembersOnlineStatus.STATUS_NOT_ONLINE.getCode()))
		{
			result.setCode(ErrorCode.ERROR_SERVICE_PROVIDER_OFFLINE_ERROR.getCode());
			result.setMsg("被叫人离线");
			return result;
		}

		// 查询服务提供者提供的服务，目前只有一条商品记录
		// 该记录在咨询师设置服务价格时，由系统自动插入
		ServiceGoods serviceGoods = serviceGoodsService
				.selectImmVoiceByMid(calledId);
		if (serviceGoods == null) {
			result.setCode(ErrorCode.ERROR_SERVICE_GOODS_INFO_INEXISTENT
					.getCode());
			result.setMsg("服务商品信息未找到");
			return result;
		}

		try
		{
		// 加锁抢占咨询师
		Byte oldStatus = memberService.transCasStatus(calledId,
				MembersOnlineStatus.STATUS_ONLINE.getCode(),
				MembersOnlineStatus.STATUS_IN_THE_CALL.getCode());
		if (oldStatus.equals(MembersOnlineStatus.STATUS_IN_THE_CALL.getCode())) {
			result.setCode(ErrorCode.ERROR_SERVICE_PROVIDER_BUSY_ERROR
					.getCode());
			result.setMsg("被叫方正忙，请稍后再拨");
			return result;
		} else if (oldStatus.equals(MembersOnlineStatus.STATUS_NOT_ONLINE
				.getCode())) {
			result.setCode(ErrorCode.ERROR_SERVICE_PROVIDER_OFFLINE_ERROR
					.getCode());
			result.setMsg("被叫人不在线");
			return result;
		}

		// 查询是否尚有未完成订单
		ServiceOrder queryServiceOrder = new ServiceOrder();
		queryServiceOrder.setMid(caller.getMid());// 主叫人id
		queryServiceOrder.setServiceProviderId(called.getMid());// 被叫人id
		queryServiceOrder.setEeId(thisEeId);
		ServiceOrder existServiceOrder = serviceOrderService.selectUnCompleteOrderByServiceProviderId(queryServiceOrder);
		if (existServiceOrder != null) // 订单已创建
		{
			//刷新订单状态
			serviceOrderService.refreshOrderStatus(existServiceOrder.getSoid()); 
			//重新查询订单
			existServiceOrder = serviceOrderService.selectOrderByPrimaryKey(existServiceOrder.getSoid());
			// 检查订单是否已经结束
			if (serviceOrderService.isUncompletedStatus(existServiceOrder.getStatus()))
			{//订单未结束，继续订单流程
				long timeRemind = countCallTimeRemind(serviceGoods, existServiceOrder); // 电话剩余时长
				ApiServiceCallDTO callDTO = CallCommunicationUtil.callServiceByalidayu(caller.getMobilePhone(), called.getMobilePhone(), Integer.parseInt(timeRemind + ""),callFlag);
				if (callDTO == null || callDTO.getCallsid() == null)
				{//拨打失败，还原咨询师状态
					memberService.transCasStatus(calledId,MembersOnlineStatus.STATUS_IN_THE_CALL.getCode(),MembersOnlineStatus.STATUS_ONLINE.getCode());
					result.setCode(ErrorCode.ERROR_CALL_SERVICE_CONNECTION_ERROR.getCode());
					result.setMsg(ErrorCode.ERROR_CALL_SERVICE_CONNECTION_ERROR.getMessage());
					return result;
				}
				//计算允许通话时间戳
				Date lowDate = AliyunIMUtil.getLowDate(timeRemind);
				// 新增通话记录		
				String[] str = callDTO.getRecordUrl().split("@");
				ServiceCallRecord serviceCallRecord = new ServiceCallRecord();
				serviceCallRecord.setServiceOrderId(existServiceOrder.getSoid());
				serviceCallRecord.setCallsid(callDTO.getCallsid());
				serviceCallRecord.setOrderId(callDTO.getOrderId()+"@"+serviceGoods.getDuration());
				serviceCallRecord.setCaller(callDTO.getCaller());
				serviceCallRecord.setCalled(callDTO.getCalled());
				serviceCallRecord.setRecordUrl(callDTO.getRecordUrl());
				serviceCallRecord.setBeginTime(lowDate);
				// 判断通话时间是否只剩五分钟
				Date dates = new Date();
		    	long nowTimes = dates.getTime()-5*60*1000;
				long lowTime = lowDate.getTime();
				if(nowTimes>lowTime){
				serviceCallRecord.setIsCalling((byte) 1);
				}else{
				serviceCallRecord.setIsCalling((byte) 2);
				}
				serviceCallRecordService.insertSelective(serviceCallRecord);
				result.put("orderNo", existServiceOrder.getNo());
				result.put("specific_x",str[2]);
				log.info("-------------------正常返回流水号------------："+callDTO.getCallsid());
				return result;
			}			
		 }  
		} catch (Exception e)
		{//拨打失败，还原咨询师状态
			memberService.transCasStatus(calledId, 
					MembersOnlineStatus.STATUS_IN_THE_CALL.getCode(),
					MembersOnlineStatus.STATUS_ONLINE.getCode()
					);
			result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
			result.setMsg(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
			return result;
		}
			
		// 新订单
		{
			// 新建订单对象
			Map<String, Long> mapStatusAndEeId = eapService.obtainServiceStatusAndEeId(callerId, calledId);
			Long serviceStatus = mapStatusAndEeId.get("status");
			Long eeId = mapStatusAndEeId.get("eeId");
			ServiceOrder serviceOrder = new ServiceOrder();
			serviceOrder.setSgid(serviceGoods.getSgid());
			serviceOrder.setMid(callerId);
			serviceOrder.setServiceProviderId(calledId);
			serviceOrder.setNo(Constant.idWorker.nextId() + "");
			serviceOrder.setCreateTime(new Date());
			serviceOrder.setServiceBeginTime(new Date());
			serviceOrder.setServiceRealityBeginTime(new Date());
			serviceOrder.setGoodsQuantity(1); // 目前商品数量设置为1，后期需求有可能会变为多个商品数量
			serviceOrder.setCost(serviceGoods.getPrice().multiply(new BigDecimal(serviceOrder.getGoodsQuantity())));
			serviceOrder.setStatus(OrderState.STATE_CALL_NOT_CONNECTED.getCode()); // 首次拨打，将订单状态设置为“未接通”
			try
			{
				if (serviceStatus.longValue() == 1) // eap订单
				{
					serviceOrder.setOrderType(new Byte("1"));
					serviceOrder.setEeId(eeId);
					serviceStatisticsService.updateTimes(callerId, calledId, 1, eeId);
				} else // 普通订单
				{
					serviceOrder.setOrderType(new Byte("0"));
					serviceOrder.setEeId((long) 0);
					serviceStatisticsService.updateTimes(callerId, calledId, 0, eeId);
				}
				serviceOrderService.insert(serviceOrder);

				// 拨打电话
				ApiServiceCallDTO callDTO = CallCommunicationUtil.callServiceByalidayu(caller.getMobilePhone(), called.getMobilePhone(), serviceGoods.getDuration() * 60,callFlag);
				if (callDTO == null || callDTO.getCallsid() == null)
				{//拨打失败，还原咨询师状态
					memberService.transCasStatus(calledId, 
							MembersOnlineStatus.STATUS_IN_THE_CALL.getCode(),
							MembersOnlineStatus.STATUS_ONLINE.getCode()
							);
					result.setCode(ErrorCode.ERROR_CALL_SERVICE_CONNECTION_ERROR.getCode());
					result.setMsg(ErrorCode.ERROR_CALL_SERVICE_CONNECTION_ERROR.getMessage());
					return result;
				}
				//计算允许通话时间戳
				Date lowDate = AliyunIMUtil.getLowDate(serviceGoods.getDuration() * 60);
				// 新增订单通话记录
				ServiceCallRecord serviceCallRecord = new ServiceCallRecord();
				serviceCallRecord.setServiceOrderId(serviceOrder.getSoid());
				serviceCallRecord.setCallsid(callDTO.getCallsid());
				serviceCallRecord.setOrderId(callDTO.getOrderId()+"@"+serviceGoods.getDuration());
				serviceCallRecord.setCaller(callDTO.getCaller());
				serviceCallRecord.setCalled(callDTO.getCalled());
				serviceCallRecord.setRecordUrl(callDTO.getRecordUrl());
				serviceCallRecord.setBeginTime(lowDate);
				serviceCallRecord.setIsCalling((byte) 1);
				serviceCallRecordService.insertSelective(serviceCallRecord);
				String[] str = callDTO.getRecordUrl().split("@");
                result.put("specific_x",str[2]);
                //记录callsid
				//session.setAttribute("callsid", callDTO.getCallsid());
				// 更新咨询数量
				if (serviceGoods != null)
				{
					if (serviceGoods.getTimes() != null)
					{
						serviceGoods.setTimes(serviceGoods.getTimes() + 1);
					} else
					{
						serviceGoods.setTimes(1);
					}
				}
				serviceGoodsService.updateByPrimaryKey(serviceGoods);
				log.info("---------------正常创建新订单,订单类型--------------："+serviceOrder.getOrderType());
			} catch (Exception e)
			{//拨打失败，还原咨询师状态
				memberService.transCasStatus(calledId, 
						MembersOnlineStatus.STATUS_IN_THE_CALL.getCode(),
						MembersOnlineStatus.STATUS_ONLINE.getCode()
						);
				result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
				result.setMsg(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
				return result;
			}
			
			result.put("orderNo", serviceOrder.getNo());
		}
		
		return result;
	}

	/**
	 * 处于进行中的订单，还在时效范围内， 再次拨打电话
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/V1/callReStartByaliyun.json")
	@ResponseBody
	public Object callReStartByaliyun(HttpSession session, HttpServletRequest request, String orderId,Integer callType,Integer callFlag)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(orderId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		// 查询订单
		ServiceOrder serviceOrder = serviceOrderService.selectByNo(orderId);
		if (serviceOrder == null)
		{
			result.setCode(ErrorCode.ERROR_ORDER_NO_INEXISTENT.getCode());
			result.setMsg(ErrorCode.ERROR_ORDER_NO_INEXISTENT.getMessage());
			return result;
		}
		
		//刷新订单状态
		serviceOrderService.refreshOrderStatus(serviceOrder.getSoid());
		//重新查询
		serviceOrder = serviceOrderService.selectOrderByPrimaryKey(serviceOrder.getSoid());
		// 检查订单是否已经结束
		if (!serviceOrderService.isUncompletedStatus(serviceOrder.getStatus()))
		{
			result.setCode(ErrorCode.ERROR_CALL_SERVICE_IS_OVER.getCode());
			result.setMsg(ErrorCode.ERROR_CALL_SERVICE_IS_OVER.getMessage());
			return result;
		}

		// 查询订单的商品信息
		ServiceGoods serviceGoods = serviceOrderService.selectGoodsByPrimaryKey(serviceOrder.getSgid());

		// 查询主叫人信息
		Member caller = memberService.selectMemberByMid(serviceOrder.getMid());
		if (caller == null || caller.getMobilePhone() == null)
		{
			result.setCode(ErrorCode.ERROR_CALLER_INFO_INEXISTENT.getCode());
			result.setMsg(ErrorCode.ERROR_CALLER_INFO_INEXISTENT.getMessage());
			return result;
		}

		// 查询被叫人状态
		Member called = memberService.selectMemberByMid(serviceOrder.getServiceProviderId());
		if (called == null || called.getMobilePhone() == null)
		{
			result.setCode(ErrorCode.ERROR_CALLED_INFO_INEXISTENT.getCode());
			result.setMsg(ErrorCode.ERROR_CALLED_INFO_INEXISTENT.getMessage());
			return result;
		}
		
		if (called.getIsEnable().intValue() == 1) {
			result.setCode(ErrorCode.ERROR_CALLED_INFO_INEXISTENT.getCode());
			result.setMsg("此专家违反平台规则，已被禁止提供咨询服务");
			return result;
		}

		try
		{
		// 加锁抢占咨询师
		Byte oldStatus = memberService.transCasStatus(
				serviceOrder.getServiceProviderId(),
				MembersOnlineStatus.STATUS_ONLINE.getCode(),
				MembersOnlineStatus.STATUS_IN_THE_CALL.getCode());
		if (oldStatus.equals(MembersOnlineStatus.STATUS_IN_THE_CALL.getCode())) {
			result.setCode(ErrorCode.ERROR_SERVICE_PROVIDER_BUSY_ERROR.getCode());
			result.setMsg("被叫方正忙，请稍后再拨");
			return result;
		} else if (oldStatus.equals(MembersOnlineStatus.STATUS_NOT_ONLINE.getCode())) {
			result.setCode(ErrorCode.ERROR_SERVICE_PROVIDER_OFFLINE_ERROR.getCode());
			result.setMsg("被叫人不在线");
			return result;
		}
					
			// 拨打电话
			long timeRemind = countCallTimeRemind(serviceGoods, serviceOrder); // 电话剩余时长
			ApiServiceCallDTO callDTO = CallCommunicationUtil.callServiceByalidayu(caller.getMobilePhone(), called.getMobilePhone(), Integer.parseInt(timeRemind + ""),callFlag);
			if (callDTO == null || callDTO.getCallsid() == null)
			{   //拨打失败，还原咨询师状态
				memberService.transCasStatus(serviceOrder.getServiceProviderId(),MembersOnlineStatus.STATUS_IN_THE_CALL.getCode(),MembersOnlineStatus.STATUS_ONLINE.getCode());				
				result.setCode(ErrorCode.ERROR_CALL_SERVICE_CONNECTION_ERROR.getCode());
				result.setMsg(ErrorCode.ERROR_CALL_SERVICE_CONNECTION_ERROR.getMessage());
				return result;
			}
			//计算允许通话时间戳
			Date lowDate = AliyunIMUtil.getLowDate(timeRemind);
			// 新增通话记录
			String[] str = callDTO.getRecordUrl().split("@");
			ServiceCallRecord serviceCallRecord = new ServiceCallRecord();
			serviceCallRecord.setServiceOrderId(serviceOrder.getSoid());
			serviceCallRecord.setCallsid(callDTO.getCallsid());
			serviceCallRecord.setOrderId(callDTO.getOrderId()+"@"+serviceGoods.getDuration());
			serviceCallRecord.setCaller(callDTO.getCaller());
			serviceCallRecord.setCalled(callDTO.getCalled());
			serviceCallRecord.setRecordUrl(callDTO.getRecordUrl());
			serviceCallRecord.setBeginTime(lowDate);
			serviceCallRecord.setIsCalling((byte) 1);
			serviceCallRecordService.insertSelective(serviceCallRecord);
			result.put("orderNo", serviceOrder.getNo());
            result.put("specific_x",str[2]);
		} catch (Exception e)
		{
			//发生异常  回滚咨询师状态
			memberService.transCasStatus(serviceOrder.getServiceProviderId(), 
					MembersOnlineStatus.STATUS_IN_THE_CALL.getCode(),
					MembersOnlineStatus.STATUS_ONLINE.getCode()
					);
			log.error(ErrorCode.ERROR_SYSTEM_ERROR.getMessage(), e);
			result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
			result.setMsg(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
			return result;
		}

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	//老版本
	@RequestMapping(value = "/callStart.json")
	@ResponseBody
	public Object callStart(HttpSession session, HttpServletRequest request, Long callerId, Long calledId, Integer callType)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());

		// 参数检查		
		if (PropertyUtils.examineOneNull(callerId, calledId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}		

		// 查询主叫人信息
		Member caller = memberService.selectMemberByMid(callerId);
		if (caller == null || caller.getMobilePhone() == null)
		{
			result.setCode(ErrorCode.ERROR_CALLER_INFO_INEXISTENT.getCode());
			result.setError("主叫人信息未找到");
			return result;
		}

		// 查询被叫人信息
		Member called = memberService.selectMemberByMid(calledId);
		if (called == null || called.getMobilePhone() == null)
		{
			result.setCode(ErrorCode.ERROR_CALLED_INFO_INEXISTENT.getCode());
			result.setError("被叫人信息未找到");
			return result;
		}
		//咨询师是否被禁用
		if (called.getIsEnable().intValue() == 1)
		{
			result.setCode(ErrorCode.ERROR_CALLED_INFO_INEXISTENT.getCode());
			result.setMsg("此专家违反平台规则，已被禁止提供咨询服务");
			return result;
		}

		// 查询咨询师是否离线
		Byte calledStatus = called.getStatus();
		if (calledStatus.equals(MembersOnlineStatus.STATUS_NOT_ONLINE.getCode()))
		{
			result.setCode(ErrorCode.ERROR_SERVICE_PROVIDER_OFFLINE_ERROR.getCode());
			result.setMsg("被叫人离线");
			return result;
		}

		// 查询服务提供者提供的服务，目前只有一条商品记录
		// 该记录在咨询师设置服务价格时，由系统自动插入
		ServiceGoods serviceGoods = serviceGoodsService.selectImmVoiceByMid(calledId);
		if (serviceGoods == null)
		{
			result.setCode(ErrorCode.ERROR_SERVICE_GOODS_INFO_INEXISTENT.getCode());
			result.setError("服务商品信息未找到");
			return result;
		}
		
		// 加锁抢占咨询师
		Byte oldStatus = memberService.transCasStatus(calledId, 
				MembersOnlineStatus.STATUS_ONLINE.getCode(), 
				MembersOnlineStatus.STATUS_IN_THE_CALL.getCode());
	    if (oldStatus.equals(MembersOnlineStatus.STATUS_IN_THE_CALL.getCode()))
		{
			result.setCode(ErrorCode.ERROR_SERVICE_PROVIDER_BUSY_ERROR.getCode());
			result.setMsg("被叫方正忙，请稍后再拨");
			return result;
		} else if(oldStatus.equals(MembersOnlineStatus.STATUS_NOT_ONLINE.getCode()))
		{
			result.setCode(ErrorCode.ERROR_SERVICE_PROVIDER_OFFLINE_ERROR.getCode());
			result.setMsg("被叫人不在线");
			return result;
		}
		
		// 查询是否尚有未完成订单
		ServiceOrder queryServiceOrder = new ServiceOrder();
		queryServiceOrder.setMid(caller.getMid());// 主叫人id
		queryServiceOrder.setServiceProviderId(called.getMid());// 被叫人id
		ServiceOrder existServiceOrder = serviceOrderService.selectUnCompleteOrderByServiceProviderId(queryServiceOrder);
		if (existServiceOrder != null) // 订单已创建
		{
			//刷新订单状态
			serviceOrderService.refreshOrderStatus(existServiceOrder.getSoid()); 
			//重新查询订单
			existServiceOrder = serviceOrderService.selectOrderByPrimaryKey(existServiceOrder.getSoid());
			// 检查订单是否已经结束
			if (serviceOrderService.isUncompletedStatus(existServiceOrder.getStatus()))
			{//订单未结束，继续订单流程
				long timeRemind = countCallTimeRemind(serviceGoods, existServiceOrder); // 电话剩余时长
				ApiServiceCallDTO callDTO = CallCommunicationUtil.callService(caller.getMobilePhone(), called.getMobilePhone(), Integer.parseInt(timeRemind + ""));
				if (callDTO == null || callDTO.getCallsid() == null)
				{//拨打失败，还原咨询师状态
					memberService.transCasStatus(calledId, 
							MembersOnlineStatus.STATUS_IN_THE_CALL.getCode(),
							MembersOnlineStatus.STATUS_ONLINE.getCode()
							);
					result.setCode(ErrorCode.ERROR_CALL_SERVICE_CONNECTION_ERROR.getCode());
					result.setMsg(ErrorCode.ERROR_CALL_SERVICE_CONNECTION_ERROR.getMessage());
					return result;
				}
				
				// 新增通话记录
				ServiceCallRecord serviceCallRecord = new ServiceCallRecord();
				serviceCallRecord.setServiceOrderId(existServiceOrder.getSoid());
				serviceCallRecord.setCallsid(callDTO.getCallsid());
				serviceCallRecord.setOrderId(callDTO.getOrderId());
				serviceCallRecord.setCaller(callDTO.getCaller());
				serviceCallRecord.setCalled(callDTO.getCalled());
				serviceCallRecordService.insertSelective(serviceCallRecord);

				result.put("orderNo", existServiceOrder.getNo());
				return result;
			}			
		}  
			
		// 新订单
		{
			// 新建订单对象
			Map<String, Long> mapStatusAndEeId = eapService.obtainServiceStatusAndEeId(callerId, calledId);
			Long serviceStatus = mapStatusAndEeId.get("status");
			Long eeId = mapStatusAndEeId.get("eeId");
			ServiceOrder serviceOrder = new ServiceOrder();
			serviceOrder.setSgid(serviceGoods.getSgid());
			serviceOrder.setMid(callerId);
			serviceOrder.setServiceProviderId(calledId);
			serviceOrder.setNo(Constant.idWorker.nextId() + "");
			serviceOrder.setCreateTime(new Date());
			serviceOrder.setServiceBeginTime(new Date());
			serviceOrder.setServiceRealityBeginTime(new Date());
			serviceOrder.setGoodsQuantity(1); // 目前商品数量设置为1，后期需求有可能会变为多个商品数量
			serviceOrder.setCost(serviceGoods.getPrice().multiply(new BigDecimal(serviceOrder.getGoodsQuantity())));
			serviceOrder.setStatus(OrderState.STATE_CALL_NOT_CONNECTED.getCode()); // 首次拨打，将订单状态设置为“未接通”
			try
			{
				if (serviceStatus.longValue() == 1) // eap订单
				{
					serviceOrder.setOrderType(new Byte("1"));
					serviceOrder.setEeId(eeId);
					serviceStatisticsService.updateTimes(callerId, calledId, 1, eeId);
				} else // 普通订单
				{
					serviceOrder.setOrderType(new Byte("0"));
					serviceStatisticsService.updateTimes(callerId, calledId, 0, eeId);
				}
				serviceOrderService.insert(serviceOrder);

				// 拨打电话
				ApiServiceCallDTO callDTO = CallCommunicationUtil.callService(caller.getMobilePhone(), called.getMobilePhone(), serviceGoods.getDuration() * 60);
				if (callDTO == null || callDTO.getCallsid() == null)
				{//拨打失败，还原咨询师状态
					memberService.transCasStatus(calledId, 
							MembersOnlineStatus.STATUS_IN_THE_CALL.getCode(),
							MembersOnlineStatus.STATUS_ONLINE.getCode()
							);
					result.setCode(ErrorCode.ERROR_CALL_SERVICE_CONNECTION_ERROR.getCode());
					result.setError(ErrorCode.ERROR_CALL_SERVICE_CONNECTION_ERROR.getMessage());
					return result;
				}
				
				// 新增订单通话记录
				ServiceCallRecord serviceCallRecord = new ServiceCallRecord();
				serviceCallRecord.setServiceOrderId(serviceOrder.getSoid());
				serviceCallRecord.setCallsid(callDTO.getCallsid());
				serviceCallRecord.setOrderId(callDTO.getOrderId());
				serviceCallRecord.setCaller(callDTO.getCaller());
				serviceCallRecord.setCalled(callDTO.getCalled());
				serviceCallRecordService.insertSelective(serviceCallRecord);

				// 更新咨询数量
				if (serviceGoods != null)
				{
					if (serviceGoods.getTimes() != null)
					{
						serviceGoods.setTimes(serviceGoods.getTimes() + 1);
					} else
					{
						serviceGoods.setTimes(1);
					}
				}
				serviceGoodsService.updateByPrimaryKey(serviceGoods);
			} catch (Exception e)
			{//拨打失败，还原咨询师状态
				memberService.transCasStatus(calledId, 
						MembersOnlineStatus.STATUS_IN_THE_CALL.getCode(),
						MembersOnlineStatus.STATUS_ONLINE.getCode()
						);
				result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
				result.setMsg(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
				return result;
			}
			
			result.put("orderNo", serviceOrder.getNo());
		}
		
		return result;
	}

	/**
	 * 处于进行中的订单，还在时效范围内， 再次拨打电话
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/callReStart.json")
	@ResponseBody
	public Object callReStart(HttpSession session, HttpServletRequest request, String orderId)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(orderId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		// 查询订单
		ServiceOrder serviceOrder = serviceOrderService.selectByNo(orderId);
		if (serviceOrder == null)
		{
			result.setCode(ErrorCode.ERROR_ORDER_NO_INEXISTENT.getCode());
			result.setMsg(ErrorCode.ERROR_ORDER_NO_INEXISTENT.getMessage());
			return result;
		}
		
		//刷新订单状态
		serviceOrderService.refreshOrderStatus(serviceOrder.getSoid());
		//重新查询
		serviceOrder = serviceOrderService.selectOrderByPrimaryKey(serviceOrder.getSoid());
		// 检查订单是否已经结束
		if (!serviceOrderService.isUncompletedStatus(serviceOrder.getStatus()))
		{
			result.setCode(ErrorCode.ERROR_CALL_SERVICE_IS_OVER.getCode());
			result.setMsg(ErrorCode.ERROR_CALL_SERVICE_IS_OVER.getMessage());
			return result;
		}

		// 查询订单的商品信息
		ServiceGoods serviceGoods = serviceOrderService.selectGoodsByPrimaryKey(serviceOrder.getSgid());

		// 查询主叫人信息
		Member caller = memberService.selectMemberByMid(serviceOrder.getMid());
		if (caller == null || caller.getMobilePhone() == null)
		{
			result.setCode(ErrorCode.ERROR_CALLER_INFO_INEXISTENT.getCode());
			result.setMsg(ErrorCode.ERROR_CALLER_INFO_INEXISTENT.getMessage());
			return result;
		}

		// 查询被叫人状态
		Member called = memberService.selectMemberByMid(serviceOrder.getServiceProviderId());
		if (called == null || called.getMobilePhone() == null)
		{
			result.setCode(ErrorCode.ERROR_CALLED_INFO_INEXISTENT.getCode());
			result.setMsg(ErrorCode.ERROR_CALLED_INFO_INEXISTENT.getMessage());
			return result;
		}

		if (called.getIsEnable().intValue() == 1) {
			result.setCode(ErrorCode.ERROR_CALLED_INFO_INEXISTENT.getCode());
			result.setMsg("此专家违反平台规则，已被禁止提供咨询服务");
			return result;
		}

		// 加锁抢占咨询师
		Byte oldStatus = memberService.transCasStatus(
				serviceOrder.getServiceProviderId(),
				MembersOnlineStatus.STATUS_ONLINE.getCode(),
				MembersOnlineStatus.STATUS_IN_THE_CALL.getCode());
		if (oldStatus.equals(MembersOnlineStatus.STATUS_IN_THE_CALL.getCode())) {
			result.setCode(ErrorCode.ERROR_SERVICE_PROVIDER_BUSY_ERROR
					.getCode());
			result.setMsg("被叫方正忙，请稍后再拨");
			return result;
		} else if (oldStatus.equals(MembersOnlineStatus.STATUS_NOT_ONLINE
				.getCode())) {
			result.setCode(ErrorCode.ERROR_SERVICE_PROVIDER_OFFLINE_ERROR
					.getCode());
			result.setMsg("被叫人不在线");
			return result;
		}
		try
		{
			// 拨打电话				
			long timeRemind = countCallTimeRemind(serviceGoods, serviceOrder); // 电话剩余时长
			ApiServiceCallDTO callDTO = CallCommunicationUtil.callService(caller.getMobilePhone(), called.getMobilePhone(), Integer.parseInt(timeRemind + ""));
			if (callDTO == null || callDTO.getCallsid() == null)
			{   //拨打失败，还原咨询师状态
				memberService.transCasStatus(serviceOrder.getServiceProviderId(), 
						MembersOnlineStatus.STATUS_IN_THE_CALL.getCode(),
						MembersOnlineStatus.STATUS_ONLINE.getCode()
						);
				
				result.setCode(ErrorCode.ERROR_CALL_SERVICE_CONNECTION_ERROR.getCode());
				result.setMsg(ErrorCode.ERROR_CALL_SERVICE_CONNECTION_ERROR.getMessage());
				return result;
			}
				
			// 新增通话记录
			ServiceCallRecord serviceCallRecord = new ServiceCallRecord();
			serviceCallRecord.setServiceOrderId(serviceOrder.getSoid());
			serviceCallRecord.setCallsid(callDTO.getCallsid());
			serviceCallRecord.setOrderId(callDTO.getOrderId());
			serviceCallRecord.setCaller(callDTO.getCaller());
			serviceCallRecord.setCalled(callDTO.getCalled());
			serviceCallRecordService.insertSelective(serviceCallRecord);
	
			result.put("orderNo", serviceOrder.getNo());
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_SYSTEM_ERROR.getMessage(), e);
			result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
			result.setMsg(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
			return result;
		}

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	/**
	 * 咨询师手动关闭订单
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/closeCallOrder.json")
	@ResponseBody
	public Object closeCallOrder(HttpSession session, HttpServletRequest request, String orderNo)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());

		// 参数检查
		if (orderNo == null)
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg("订单id不能为空");
			return result;
		}

		// 查询订单
		ServiceOrder queryRecord = new ServiceOrder();
		queryRecord.setNo(orderNo);
		List<ServiceOrder> serviceOrderList = serviceOrderService.selectSelective(queryRecord);
		if (serviceOrderList != null && serviceOrderList.size() > 0)
		{
			// 关闭订单
			ServiceOrder serviceOrder = serviceOrderList.get(0);
			if (serviceOrder.getStatus().equals(OrderState.STATE_CALL_IN_PROGRESS.getCode()))
			{
				serviceOrder.setStatus(OrderState.STATE_CALL_NOT_EVALUATION.getCode());
			}
			serviceOrderService.updateByPrimaryKeySelective(serviceOrder);
		}

		return result;
	}

}
