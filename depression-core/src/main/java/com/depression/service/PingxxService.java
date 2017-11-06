package com.depression.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.depression.entity.ErrorCode;
import com.depression.utils.Configuration;
import com.depression.utils.PropertyUtils;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.APIConnectionException;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.exception.AuthenticationException;
import com.pingplusplus.exception.ChannelException;
import com.pingplusplus.exception.InvalidRequestException;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Transfer;
import com.pingplusplus.model.Webhooks;

@Service
public class PingxxService
{
	Logger log = Logger.getLogger(this.getClass());
	String pingxxAppId = Configuration.PINGXX_APPID;
	PublicKey pingxxPublicKey;
	{
		Pingpp.apiKey = Configuration.PINGXX_APIKEY;
		Pingpp.privateKeyPath = getFilePath(Configuration.PINGXX_OURPRIVATEKEYFILE);
		
		try
		{
			pingxxPublicKey = getPubKey(getFilePath(Configuration.PINGXX_PINGXXPUBLICKEYFILE));
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			log.error("init publicKey failed", e);;
		}
	}
	
	/**
	 * 根据文件名，搜索资源路劲
	 * @param filename
	 * @return
	 */
	String getFilePath(String filename)
	{
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try {
			Resource[] resources=resolver.getResources("classpath*:" + filename);
			return resources[0].getURI().getPath();
		} catch (IOException e) {
			log.error(ErrorCode.ERROR_SYSTEM_IO.getMessage(), e);
			return null;
		}
	}
	
	public boolean checkChargeChannel(String channel)
	{
		if(channel==null) return false;
		if(channel.equals("alipay") || channel.equals("wx") || channel.equals("upacp")) return true;
		return false;
	}

	public boolean checkTransferChannel(String channel)
	{
		if(channel==null) return false;
		if(channel.equals("wx") || channel.equals("wx_pub") || channel.equals("unionpay")) return true;
		return false;
	}
	
	/**
	 * 发起支付请求，获取charge对象
	 * @param orderNo
	 * @param amount		！！！此处以分为单位！！！
	 * @param channel
	 * @param clientIp
	 * @param subject
	 * @param body
	 * @return
	 */
	public Charge startCharge(String orderNo, Integer amount, String channel, String clientIp,
			String subject, String body)
	{
		
		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("order_no",  orderNo);
		chargeParams.put("amount", amount);//订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100）
		Map<String, String> app = new HashMap<String, String>();
		app.put("id", pingxxAppId);
		chargeParams.put("app", app);
		chargeParams.put("channel",  channel);
		chargeParams.put("currency", "cny");
		chargeParams.put("client_ip",  clientIp);
		chargeParams.put("subject",  subject);
		chargeParams.put("body",  body);
		try
		{
			return Charge.create(chargeParams);
		} catch (AuthenticationException | InvalidRequestException | APIConnectionException | APIException | ChannelException e)
		{
			// TODO Auto-generated catch block
			log.error(ErrorCode.ERROR_PINGXX_CHARGE.getMessage(), e);
			return null;
		}	
	}
	
	/**
	 * 企业付款（微信）
	 * @param orderNo
	 * @param amount
	 * @param openId
	 * @param description
	 * @param userName
	 * @return
	 */
	public Transfer wxTransfer(String orderNo, Integer amount,String openId,
			String description, String userName)
	{
		Map<String, Object> transferMap = new HashMap<String, Object>();
		transferMap.put("channel", "wx");//此处的 wx 为微信新渠道，针对 2015 年 3 月份之后申请的微信开放平台的支付
		transferMap.put("order_no", orderNo);
		transferMap.put("amount", amount.toString());//订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100）
		transferMap.put("type", "b2c");
		transferMap.put("currency", "cny");
		transferMap.put("recipient", openId);//企业付款给指定用户的 open_id
		transferMap.put("description", description);
		Map<String, String> app = new HashMap<String, String>();
		app.put("id", pingxxAppId);
		transferMap.put("app", app);
		Map<String, Object> extra = new HashMap<String, Object>();
		extra.put("user_name", userName);
		extra.put("force_check", true);
		transferMap.put("extra", extra);
		
		try
		{
			return Transfer.create(transferMap);
		} catch (AuthenticationException | InvalidRequestException | APIConnectionException | APIException | ChannelException e)
		{
			// TODO Auto-generated catch block
			log.error(ErrorCode.ERROR_PINGXX_TRANSFER.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * 企业付款（银行卡）
	 * @param orderNo
	 * @param amount
	 * @param description
	 * @param userName
	 * @param cardNum
	 * @param openBankCode
	 * @return
	 */
	public Transfer unionpayTransfer(String orderNo, Integer amount,
			String description, String userName, String cardNum, String openBankCode)
	{
		Map<String, Object> transferMap = new HashMap<String, Object>();
		transferMap.put("channel", "unionpay");// 企业付款（银行卡）
		transferMap.put("order_no", orderNo);
		transferMap.put("amount", amount.toString());//订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100）
		transferMap.put("type", "b2c");
		transferMap.put("currency", "cny");
		transferMap.put("description", description);
		Map<String, String> app = new HashMap<String, String>();
		app.put("id", pingxxAppId);
		transferMap.put("app", app);
		Map<String, Object> extra = new HashMap<String, Object>();
		extra.put("card_number", cardNum);
		extra.put("user_name", userName);
		extra.put("open_bank_code", openBankCode);
		transferMap.put("extra", extra);
		
		try
		{
			return Transfer.create(transferMap);
		} catch (AuthenticationException | InvalidRequestException | APIConnectionException | APIException | ChannelException e)
		{
			// TODO Auto-generated catch block
			log.error(ErrorCode.ERROR_PINGXX_TRANSFER.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * 企业付款
	 * @param orderNo
	 * @param amount
	 * @param channel
	 * @param openId
	 * @param description
	 * @param userName
	 * @param cardNum
	 * @param openBankCode
	 * @return
	 */
	public Transfer startTransfer(String orderNo, Integer amount, String channel,String openId,
			String description, String userName, String cardNum, String openBankCode)
	{
		if("wx_pub".equals(channel)){
			if(PropertyUtils.examineOneNull(orderNo, amount, openId, description, userName)){ 
				return null;
			}
			return wxTransfer(orderNo, amount, openId, description, userName);
		}else if("unionpay".equals(channel))
		{
			if(PropertyUtils.examineOneNull(orderNo, amount, description, userName, cardNum, openBankCode)){ 
				return null;
			}
			return unionpayTransfer(orderNo, amount, description, userName, cardNum, openBankCode);
		}
		return null;
	}
	
    /**
     * 读取文件, 部署 web 程序的时候, 签名和验签内容需要从 request 中获得
     * @param filePath
     * @return
     * @throws Exception
     */
    public String getStringFromFile(String filePath) throws Exception {
        FileInputStream in = new FileInputStream(filePath);
        InputStreamReader inReader = new InputStreamReader(in, "UTF-8");
        BufferedReader bf = new BufferedReader(inReader);
        StringBuilder sb = new StringBuilder();
        String line;
        do {
            line = bf.readLine();
            if (line != null) {
                if (sb.length() != 0) {
                    sb.append("\n");
                }
                sb.append(line);
            }
        } while (line != null);

        return sb.toString();
    }

	/**
	 * 获得公钥
	 * @return
	 * @throws Exception
	 */
	public PublicKey getPubKey(String pubKeyPath) throws Exception {
		String pubKeyString = getStringFromFile(pubKeyPath);
        pubKeyString = pubKeyString.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "");
        byte[] keyBytes = Base64.decodeBase64(pubKeyString);

		// generate public key
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(spec);
		return publicKey;
	}

	/**
	 * 验证签名
	 * @param dataString
	 * @param signatureString
	 * @param publicKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	public boolean verifyData(String dataString, String signatureString, PublicKey publicKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        byte[] signatureBytes = Base64.decodeBase64(signatureString);
        Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initVerify(publicKey);
		signature.update(dataString.getBytes("UTF-8"));
		return signature.verify(signatureBytes);
	}
	
	/**
	 * 验证签名并解析webhook event
	 * @param eventStr
	 * @param signatureString
	 * @return
	 */
	public Event eventVerifyAndParse(String eventStr, String signatureString)
	{
		try
		{
			if(!verifyData(eventStr, signatureString, pingxxPublicKey)){
				return null;
			}
		} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException | UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			log.error("verify data failed", e);
			return null;
		}
		
		Event event = Webhooks.eventParse(eventStr);
		return event;
	}
	
}
