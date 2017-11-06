package com.depression.base.neteaseim;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.log4j.Logger;

/**
 * @author Firrela
 * @time 2016/3/7.
 */
public class NIMPost {

	private static Logger logger = Logger.getLogger(NIMPost.class);

    public static String postNIMServer(String url, final HttpEntity entity, String appKey, String appSecret)
            throws IOException {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        HttpPost post = httpClientUtil.createPost(url, entity, null);

        // addHeader
        HttpClientUtil.addHeader(post, "AppKey", appKey);
        String nonce = UUIDUtil.getUUID();
        String curTime = String.valueOf(System.currentTimeMillis() / 1000);
        HttpClientUtil.addHeader(post, "Nonce", nonce);
        HttpClientUtil.addHeader(post, "CurTime", curTime);
        String checksum = getCheckSum(nonce, curTime, appSecret);
        HttpClientUtil.addHeader(post, "CheckSum", checksum);

        // logger
        logger.info(String.format("Nonce %s | CurlTime %s | CheckSum %s", nonce, curTime, checksum));
        return httpClientUtil.fetchData(post);
    }

    private static String getCheckSum(String nonce, String curTime, String appSecret) {
        String plaintext = new StringBuffer(appSecret).append(nonce).append(curTime).toString();
        return EncodeUtil.encode(plaintext, "SHA1");
    }
}
