package com.depression.push;

import com.alibaba.fastjson.JSONObject;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToAllRequest;
import com.baidu.yun.push.model.PushMsgToAllResponse;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;
import com.depression.entity.Constant;

/**
 * 针对安卓客户端进行推送
 *
 * @author hongqian_li
 */

public class IOSPushServer
{

    /**
     * 推送消息到单台设备
     */
    public static void pushSingleDevice(PushMsg msg) throws PushClientException, PushServerException
    {
        PushKeyPair pair = new PushKeyPair(Constant.IOS_API_KEY, Constant.IOS_SECRET_KEY);
        BaiduPushClient pushClient = new BaiduPushClient(pair, BaiduPushConstants.CHANNEL_REST_URL);
        pushClient.setChannelLogHandler(new YunLogHandler()
        {
            @Override
            public void onHandle(YunLogEvent event)
            {
                System.out.println(event.getMessage());
            }
        });

        try
        {
            
            // 4. specify request arguments
            // 创建 Android的通知
            JSONObject notification = new JSONObject();
            JSONObject jsonCustormCont = new JSONObject();
			notification.put("title", msg.getTitle());
			notification.put("description", msg.getDescription());
            jsonCustormCont.put("content", msg.getCustomContent()); // 自定义内容，key-value
            notification.put("custom_content", jsonCustormCont);

            PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest().addChannelId(msg.getChannelId()).addMsgExpires(new Integer(3600)). // 设置message的有效时间
                    addMessageType(1).// 1：通知,0:透传消息.默认为0 注：IOS只有通知.
                    addMessage(notification.toString()).addDeployStatus(1). // IOS,
                    // DeployStatus
                    // => 1: Developer
                    // 2: Production.
                            addDeviceType(4);// deviceType => 3:android, 4:ios
            // 5. http request
            PushMsgToSingleDeviceResponse response = pushClient.pushMsgToSingleDevice(request);
            // Http请求结果解析打印
            System.out.println("msgId: " + response.getMsgId() + ",sendTime: " + response.getSendTime());
        } catch (PushClientException e)
        {
            /*
             * ERROROPTTYPE 用于设置异常的处理方式 -- 抛出异常和捕获异常,'true' 表示抛出, 'false' 表示捕获。
			 */
            if (BaiduPushConstants.ERROROPTTYPE)
            {
                throw e;
            } else
            {
                e.printStackTrace();
            }
        } catch (PushServerException e)
        {
            if (BaiduPushConstants.ERROROPTTYPE)
            {
                throw e;
            } else
            {
                System.out.println(String.format("requestId: %d, errorCode: %d, errorMessage: %s", e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
            }
        }
    }
    
    /**
     * 推送消息到所有的Android端用户
     */
    public static void pushMsgToAll(PushMsg msg) throws PushClientException, PushServerException
    {
        PushKeyPair pair = new PushKeyPair(Constant.ANDROID_API_KEY, Constant.ANDROID_SECRET_KEY);
        BaiduPushClient pushClient = new BaiduPushClient(pair, BaiduPushConstants.CHANNEL_REST_URL);

        pushClient.setChannelLogHandler(new YunLogHandler()
        {
            @Override
            public void onHandle(YunLogEvent event)
            {
                System.out.println(event.getMessage());
            }
        });

        try
        {
            JSONObject notification = new JSONObject();
            JSONObject jsonCustormCont = new JSONObject();
			notification.put("title", msg.getTitle());
			notification.put("description", msg.getDescription());
            jsonCustormCont.put("content", msg.getCustomContent()); // 自定义内容，key-value
            notification.put("custom_content", jsonCustormCont);

            PushMsgToAllRequest request = new PushMsgToAllRequest()
		            .addMsgExpires(new Integer(3600 * 5))
		            .addMessageType(1)
		            .addMessage(notification.toJSONString()) // 添加透传消息
		            .addDepolyStatus(2)
                    .addDeviceType(3);
            PushMsgToAllResponse response = pushClient.pushMsgToAll(request);
            // Http请求结果解析打印
            System.out.println("msgId: " + response.getMsgId() + ",sendTime: " + response.getSendTime() + ",timerId: " + response.getTimerId());
        } catch (PushClientException e)
        {
            if (BaiduPushConstants.ERROROPTTYPE)
            {
                throw e;
            } else
            {
                e.printStackTrace();
            }
        } catch (PushServerException e)
        {
            if (BaiduPushConstants.ERROROPTTYPE)
            {
                throw e;
            } else
            {
                System.out.println(String.format("requestId: %d, errorCode: %d, errorMessage: %s", e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
            }
        }
    }
}
