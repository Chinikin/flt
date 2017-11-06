package com.depression.utils;

import com.taobao.api.*;
import com.taobao.api.internal.util.*;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.*;

/**
 * axb录音文件下载client（由于阿里官方的sdk下载有问题，所以这里自己实现）
 */
public class TaobaoAxbDownloadClient extends DefaultTaobaoClient {

    private static final String DEFAULT_CHARSET = Constants.CHARSET_UTF8;
    private static final String METHOD_POST = "POST";
    private static boolean ignoreSSLCheck = true;
    private static boolean ignoreHostCheck = true;

    public TaobaoAxbDownloadClient(String serverUrl, String appKey, String appSecret) {
        super(serverUrl, appKey, appSecret);
    }

    public void excute(TaobaoRequest request, File destFile) throws IOException {
        String session = null;
        RequestParametersHolder requestHolder = new RequestParametersHolder();
        TaobaoHashMap appParams = new TaobaoHashMap(request.getTextParams());
        requestHolder.setApplicationParams(appParams);

        // 添加协议级请求参数
        TaobaoHashMap protocalMustParams = new TaobaoHashMap();
        protocalMustParams.put(Constants.METHOD, request.getApiMethodName());
        protocalMustParams.put(Constants.VERSION, "2.0");
        protocalMustParams.put(Constants.APP_KEY, appKey);
        Long timestamp = request.getTimestamp();
        if (timestamp == null) {
            timestamp = System.currentTimeMillis();
        }

        protocalMustParams.put(Constants.TIMESTAMP, new Date(timestamp));
        requestHolder.setProtocalMustParams(protocalMustParams);

        TaobaoHashMap protocalOptParams = new TaobaoHashMap();
        protocalOptParams.put(Constants.FORMAT, format);
        protocalOptParams.put(Constants.SIGN_METHOD, signMethod);
        protocalOptParams.put(Constants.SESSION, session);
        protocalOptParams.put(Constants.PARTNER_ID,
                getSdkVersion()
        );
        protocalOptParams.put(Constants.TARGET_APP_KEY, request.getTargetAppKey());

        if (this.useSimplifyJson) {
            protocalOptParams.put(Constants.SIMPLIFY, Boolean.TRUE.toString());
        }
        requestHolder.setProtocalOptParams(protocalOptParams);

        // 添加签名参数
        protocalMustParams.put(Constants.SIGN, TaobaoUtils.signTopRequest(requestHolder, appSecret, signMethod));

        String realServerUrl = getServerUrl(this.serverUrl, request.getApiMethodName(), session, appParams);
        String sysMustQuery = WebUtils.buildQuery(requestHolder.getProtocalMustParams(), Constants.CHARSET_UTF8);
        String sysOptQuery = WebUtils.buildQuery(requestHolder.getProtocalOptParams(), Constants.CHARSET_UTF8);
        String fullUrl = WebUtils.buildRequestUrl(realServerUrl, sysMustQuery, sysOptQuery);

        String charset = DEFAULT_CHARSET;
        String ctype = "application/x-www-form-urlencoded;charset=" + charset;
        String query = WebUtils.buildQuery(appParams, charset);
        byte[] content = {};
        if (query != null) {
            content = query.getBytes(charset);
        }
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(Constants.ACCEPT_ENCODING, Constants.CONTENT_ENCODING_GZIP);
        doDownload(fullUrl, ctype, content, connectTimeout, readTimeout, headerMap, destFile);
    }

    private void doDownload(String url, String ctype, byte[] content, int connectTimeout, int readTimeout, Map<String, String> headerMap, File destFile) throws IOException {
        HttpURLConnection conn = null;
        OutputStream out = null;
        try {
            conn = getConnection(new URL(url), METHOD_POST, ctype, headerMap); //SDK已经改为private了，请copy处理吧。
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            out = conn.getOutputStream();
            out.write(content);
            int statusCode = conn.getResponseCode();
            if (statusCode == 200) {
                InputStream is = conn.getInputStream();
                FileUtils.copyInputStreamToFile(is, destFile);
            }
        } finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
    
    /* Copy getConnection form SDK, 路径是： /com/taobao/api/internal/util/WebUtils.java*/
    private static HttpURLConnection getConnection(URL url, String method, String ctype, Map<String, String> headerMap) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (conn instanceof HttpsURLConnection) {
            HttpsURLConnection connHttps = (HttpsURLConnection) conn;
            if (ignoreSSLCheck) {
                try {
                    SSLContext ctx = SSLContext.getInstance("TLS");
                    ctx.init(null, new TrustManager[] { new WebUtils.TrustAllTrustManager() }, new SecureRandom());
                    connHttps.setSSLSocketFactory(ctx.getSocketFactory());
                    connHttps.setHostnameVerifier(new HostnameVerifier() {
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });
                } catch (Exception e) {
                    throw new IOException(e.toString());
                }
            } else {
                if (ignoreHostCheck) {
                    connHttps.setHostnameVerifier(new HostnameVerifier() {
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });
                }
            }
            conn = connHttps;
        }
        
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Host", url.getHost());
        conn.setRequestProperty("Accept", "text/xml,text/javascript");
        conn.setRequestProperty("User-Agent", "top-sdk-java");
        conn.setRequestProperty("Content-Type", ctype);
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        return conn;
    }
    
}
