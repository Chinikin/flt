package com.depression.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.depression.model.ImageInfo;
import com.depression.utils.Configuration;
import com.depression.utils.FileUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.util.Auth;
import com.qiniu.util.UrlSafeBase64;

@Service
public class QiniuService
{
	Logger log = Logger.getLogger(this.getClass());
	
	private final Auth auth = Auth.create(Configuration.QINIU_ACCESSKEY, Configuration.QINIU_SECRETKEY);
	private final String bucketFile = Configuration.QINIU_BUCKET_FILE;
	private final String bucketPiture = Configuration.QINIU_BUCKET_PICTURE;
	private final String domainFile = Configuration.QINIU_DOMAIN_FILE;
	private final String domainPiture = Configuration.QINIU_DOMAIN_PICTURE;
	
	/**
	 * 获取七牛文件上传的授权token，有效期3600s
	 * @return token
	 */
	public String obtainFileUploadToken()
	{
		return auth.uploadToken(bucketFile);
	}
	
	/**
	 * 获取七牛图片上传的授权token，有效期3600s
	 * @return token
	 */
	public String obtainPictureUploadToken()
	{
		return auth.uploadToken(bucketPiture);
	}
	
	/**
	 * 拼接没有Scheme的公共url，（不含Scheme部分，即去除http://）
	 * @param filename
	 * @return
	 */
	private String jointUrlWithoutScheme(String domain, String filename)
	{
		return domain+"/"+filename;
	}
	
	/**
	 * 拼接公共的url
	 * @param filename
	 * @return baseUrl
	 */
	private String jointUrl(String domain, String filename)
	{
		return "http://"+jointUrlWithoutScheme(domain, filename);
	}
	
	/**
	 * 获取文件的url
	 * @param filename
	 * @return
	 */
	public String obtainFileUrl(String filename)
	{
		return jointUrl(domainFile, filename);
	}
	
	/**
	 * 获取图片的url
	 * @param filename
	 * @return
	 */
	public String obtainPictureUrl(String filename)
	{
		return jointUrl(domainPiture, filename);
	}
	
	/**
	 * 从url向七牛上放文件
	 * @param bucket bucket名称
	 * @param url 文件所在的url
	 * @param filename 指定的文件名，不指定则etag做为文件名
	 * @return
	 */
	private int fetch(String bucket, String url, String filename)
	{
		 Zone z = Zone.autoZone();
		 com.qiniu.storage.Configuration c = new com.qiniu.storage.Configuration(z);
		 //实例化一个BucketManager对象
	     BucketManager bucketManager = new BucketManager(auth, c);
	     
	     try {
             //调用fetch方法抓取文件
	    	 if(filename == null)
	    	 {
	    		 bucketManager.fetch(url, bucket);
	    	 }else
	    	 {
	    		 bucketManager.fetch(url, bucket, filename);
	    	 }
	    	 
	    	 return 0;
	     } catch (QiniuException e) {
             //捕获异常信息
             Response r = e.response;
             System.out.println(r.toString());
             return 1;
	     }
	}
	
	/**
	 * 从url向七牛文件bucket上放文件
	 * @param url 文件所在的url
	 * @param filename 指定的文件名，不指定则etag做为文件名
	 * @return
	 */
	public int fetchFile(String url, String filename)
	{
		return fetch(bucketFile, url, filename);
	}
	
	/**
	 * 从url向七牛图片bucket上放文件
	 * @param url 文件所在的url
	 * @param filename 指定的文件名，不指定则etag做为文件名
	 * @return
	 */
	public int fetchPicture(String url, String filename)
	{
		return fetch(bucketPiture, url, filename);
	}
	
}
