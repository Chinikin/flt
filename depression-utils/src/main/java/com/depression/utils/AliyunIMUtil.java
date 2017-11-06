package com.depression.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.depression.model.api.dto.ApiServiceCallDTO;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.taobao.api.ApiException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.internal.util.StringUtils;
import com.taobao.api.request.AlibabaAliqinFcVoiceNumDoublecallRequest;
import com.taobao.api.request.AlibabaAliqinFcVoiceRecordGeturlRequest;
import com.taobao.api.request.AlibabaAliqinSecretAxbBindFirstRequest;
import com.taobao.api.request.AlibabaAliqinSecretAxbBindRequest;
import com.taobao.api.request.AlibabaAliqinSecretAxbBindSecondRequest;
import com.taobao.api.request.AlibabaAliqinSecretAxbGetRecordfileRequest;
import com.taobao.api.request.AlibabaAliqinSecretAxbUnbindRequest;
import com.taobao.api.response.AlibabaAliqinFcVoiceNumDoublecallResponse;
import com.taobao.api.response.AlibabaAliqinFcVoiceRecordGeturlResponse;
import com.taobao.api.response.AlibabaAliqinSecretAxbBindFirstResponse;
import com.taobao.api.response.AlibabaAliqinSecretAxbBindResponse;
import com.taobao.api.response.AlibabaAliqinSecretAxbGetRecordfileResponse;
import com.taobao.api.response.AlibabaAliqinSecretAxbUnbindResponse;
public class AliyunIMUtil {
	
	private static Logger log = Logger.getLogger(AliyunIMUtil.class);
	//正式环境
	private static final String url = "http://gw.api.taobao.com/router/rest";
	//沙箱环境
	//private static final String url = "http://gw.api.tbsandbox.com/router/rest";
	private static final String appkey = Configuration.ALIDAYU_APPKEY;
	private static final String secret = Configuration.ALIDAYU_APPSECRET;
	private static final String partner_key = "FC100000007262059";
	/**
	 * 创建双向回拨
	 * @return
	 */
	public static ApiServiceCallDTO aliyunCall(String callerMobilePhone,
			String calledMobilePhone, Integer maxCallTime){
		ApiServiceCallDTO callDTO = new ApiServiceCallDTO();
		String orderId = callerMobilePhone+String.valueOf(new Date().getTime());
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinFcVoiceNumDoublecallRequest req = new AlibabaAliqinFcVoiceNumDoublecallRequest();
		req.setSessionTimeOut(String.valueOf(maxCallTime+10*60));
		req.setExtend(orderId);
		req.setCallerNum(callerMobilePhone);
		req.setCallerShowNum(Configuration.ALIDAYU_SHOWNUM);
		req.setCalledNum(calledMobilePhone);
		req.setCalledShowNum(Configuration.ALIDAYU_SHOWNUM);
		AlibabaAliqinFcVoiceNumDoublecallResponse rsp;
		try {
			rsp = client.execute(req);
			log.info("-----------------------阿里大于电话回拨-----------------");
			log.info("-----------------------允许通话时长-----------------"+maxCallTime.toString());
			if(rsp.getResult().getSuccess()){
			JSONObject  jasonObjects = JSONObject.fromObject(rsp.getBody());
			String str = jasonObjects.get("alibaba_aliqin_fc_voice_num_doublecall_response").toString();
		    JSONObject  jasonObject = JSONObject.fromObject(str); 
		    String strs = jasonObject.get("result").toString();
		    JSONObject  jasonObjectes = JSONObject.fromObject(strs); 
		    String recordUrl = jasonObjectes.getString("model");
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		    String timestap = "@"+mSimpleDateFormat.format(System.currentTimeMillis()).toString()+"@"+Configuration.ALIDAYU_SHOWNUM;
				 callDTO = new ApiServiceCallDTO();
				 callDTO.setCallsid(orderId);
				 callDTO.setOrderId(orderId);
				 callDTO.setCaller(callerMobilePhone);
				 callDTO.setCalled(calledMobilePhone);
				 callDTO.setRecordUrl(recordUrl+timestap);
			}	
			log.info("-----------------------正常创建回拨电话-----------------"+callDTO.getCaller());
		} catch (ApiException e) {
			callDTO = null;
			e.printStackTrace();
		}		
		return callDTO;
	}
	
	/**
	 * 创建隐号直拨
	 * @return
	 * @throws ApiException 
	 */
	public static ApiServiceCallDTO createAXB(String callerMobilePhone,String calledMobilePhone, Integer maxCallTime) throws ApiException{
		ApiServiceCallDTO callDTO = new ApiServiceCallDTO();
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinSecretAxbBindRequest req = new AlibabaAliqinSecretAxbBindRequest();
		req.setPartnerKey(partner_key);
		req.setPhoneNoA(callerMobilePhone);
		req.setPhoneNoB(calledMobilePhone);
		//req.setEndDate(StringUtils.parseDateTime(getNewDate(maxCallTime)));
		// 可选字段
		req.setNeedRecord(true);
		AlibabaAliqinSecretAxbBindResponse rsp = client.execute(req);
		log.info("直拨创建："+rsp.getBody());
	    log.info("结束时间："+req.getEndDate().toString()); 
		JSONObject  jasonObjects = JSONObject.fromObject(rsp.getBody());
        String str = jasonObjects.get("alibaba_aliqin_secret_axb_bind_response").toString();
        JSONObject  jasonObject = JSONObject.fromObject(str);       
		String orderId = callerMobilePhone+String.valueOf(new Date().getTime());
		if(jasonObject.get("subs_id").toString()!=null){
			 SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			 String timestap = "@"+mSimpleDateFormat.format(System.currentTimeMillis()).toString();
			 callDTO = new ApiServiceCallDTO();
			 //构造通话ID
			 callDTO.setCallsid(jasonObject.get("subs_id").toString());
			 callDTO.setOrderId(orderId);
			 callDTO.setCaller(callerMobilePhone);
			 callDTO.setCalled(calledMobilePhone);
			 callDTO.setRecordUrl(jasonObject.get("subs_id").toString()+timestap+"@"+jasonObject.get("secret_no_x").toString());
		}	
		return callDTO;
	}
	
	/**
	 * 创建隐号直拨解除关系
	 * @return
	 * @throws ApiException 
	 */
	public static String relieveAXB(String subsId) throws ApiException{
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinSecretAxbUnbindRequest req = new AlibabaAliqinSecretAxbUnbindRequest();
		req.setPartnerKey(partner_key);
		req.setSubsId(Long.valueOf(subsId));
		AlibabaAliqinSecretAxbUnbindResponse rsp = client.execute(req);
		log.info("-----------------------解绑完成---------------------");
		log.info("信息："+rsp.getBody().toString());
		return "OK";
	}
	
	/**
	 * 获取录音文件字节流并且存放到七牛上
	 * @throws ApiException 
	 * @throws IOException 
	 
	public static String getRecordByAlidayu(String subId,String callId,String callTime,String fileName) throws ApiException, IOException{
		TaobaoAxbDownloadClient client = new TaobaoAxbDownloadClient(url, appkey, secret);
		com.depression.utils.AlibabaAliqinSecretAxbGetRecordfileRequest req = new com.depression.utils.AlibabaAliqinSecretAxbGetRecordfileRequest();
		req.setPartnerKey(partner_key);
		req.setSubsId(Long.valueOf(subId));
		req.setCallId(callId);
		req.setCallTime(StringUtils.parseDateTime(callTime));
		req.setFileSelectType(0L);
		String files = fileName+".txt";
		File newFile = new File(files);
		client.excute(req,newFile);
		byte[] context = getContent(files);;
		String recordName = putFileToQiniu(context,fileName);
		deleteFile(files);
		return recordName;
	}
	*/
	
	/**
	 * @throws ApiException 
	 * @throws IOException 
	 * 
	 */
	public static String getRecordByDoubleCall(String callid,String fileName) throws ApiException, IOException{
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinFcVoiceRecordGeturlRequest req = new AlibabaAliqinFcVoiceRecordGeturlRequest();
		req.setCallId(callid);
		AlibabaAliqinFcVoiceRecordGeturlResponse rsp = client.execute(req);
		JSONObject  jasonObjects = JSONObject.fromObject(rsp.getBody());
		String str = jasonObjects.get("alibaba_aliqin_fc_voice_record_geturl_response").toString();
	    JSONObject  jasonObject = JSONObject.fromObject(str); 
	    String strs = jasonObject.get("result").toString();
	    JSONObject  jasonObjectes = JSONObject.fromObject(strs); 
	    String url ="http://alidayu.com/"+ jasonObjectes.getString("model");
	    URL urls = new URL(url);
	    HttpURLConnection connection = (HttpURLConnection) urls.openConnection();
	    InputStream fi = connection.getInputStream();
	    byte[] context = getContent(fi);;
		String recordName = putFileToQiniu(context,fileName);
		return recordName;
	}
	
	/**
	 * 获取允许通话的时间戳  允许用户加长10分钟
	 * @param maxCallTime
	 * @return
	 */
	public static String getNewDate(String maxCallTime){		
    	int time = Integer.valueOf(maxCallTime) + 10*60;          
        return String.valueOf(time);
	}
	
	/**
	 * 计算时间
	 * @param maxCallTime
	 * @return
	 * @throws ParseException 
	 */
	public static long getLast(String date1,String date2) throws ParseException{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
		Date bigDate=simpleDateFormat.parse(date2); 
		Date smallDate = simpleDateFormat.parse(date1);
    	long bigTime = bigDate.getTime();
    	long smallTime = smallDate.getTime();
    	long culTime = (bigTime-smallTime)/1000;    	       
        return culTime;
	}
	
	public static String putFileToQiniu(byte[] context,String filename){
		//构造一个带指定Zone对象的配置类
		com.qiniu.storage.Configuration cfg = new com.qiniu.storage.Configuration(Zone.zone0());
		//...其他参数参考类注释
		UploadManager uploadManager = new UploadManager(cfg);
		//...生成上传凭证，然后准备上传
		String accessKey = Configuration.QINIU_SECRETKEY;
		String secretKey = Configuration.QINIU_ACCESSKEY;
		String bucket = Configuration.QINIU_BUCKET_FILE;
		//默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = filename+".wav";
		byte[] uploadBytes = context;
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket);
		try {
		    Response response = uploadManager.put(uploadBytes, key, upToken);
		    //解析上传成功的结果
		    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
		    System.out.println(putRet.key);
		    System.out.println(putRet.hash);
		    return "OK";
		} catch (QiniuException ex) {
		    Response r = ex.response;
		    System.err.println(r.toString());
		    try {
		        System.err.println(r.bodyString());
		    } catch (QiniuException ex2) {
		        //ignore
		    }
		}
		return key;
	}
	
	public static Date getLowDate(long minutes){
    	long time = new Date().getTime();
    	time = time+minutes*1000;
    	Date date = new Date();  
        date.setTime(time); 
        return date;
	} 
	
	 public static byte[] getContent(InputStream fi) throws IOException {  	         
	        byte[] buffer = new byte[(int) 2147483000];  
	        int offset = 0;  
	        int numRead = 0;  
	        while (offset < buffer.length  
	        && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {  
	            offset += numRead;  
	        }  
	        // 确保所有数据均被读取  
	        if (offset != buffer.length) {  
	        throw new IOException("Could not completely read file ");  
	        }  
	        fi.close();  
	        return buffer;  
	    }
	 /**
	     * 删除单个文件
	     *
	     * @param fileName
	     *            要删除的文件的文件名
	     * @return 单个文件删除成功返回true，否则返回false
	     */
	    public static boolean deleteFile(String fileName) {
	        File file = new File(fileName);
	        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
	        if (file.exists() && file.isFile()) {
	            if (file.delete()) {
	                System.out.println("删除单个文件" + fileName + "成功！");
	                return true;
	            } else {
	                System.out.println("删除单个文件" + fileName + "失败！");
	                return false;
	            }
	        } else {
	            System.out.println("删除单个文件失败：" + fileName + "不存在！");
	            return false;
	        }
	    }
}
