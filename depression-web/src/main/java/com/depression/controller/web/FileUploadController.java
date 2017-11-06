package com.depression.controller.web;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.depression.entity.ResultEntity;
import com.depression.service.ImageStorageService;
import com.depression.service.TestingTypeService;
import com.depression.utils.Configuration;

@Controller
@RequestMapping("/file")
public class FileUploadController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TestingTypeService testingTypeService;

	@Autowired
	private ImageStorageService imageStorageService;

	@RequestMapping(method = RequestMethod.POST, value = "/image/upload.json")
	@ResponseBody
	public Object imgUpload(MultipartHttpServletRequest request, ModelMap modelMap)
	{
		ResultEntity result = new ResultEntity();

		// 创建迭代器
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		// 检查是否有文件
		if (!itr.hasNext())
		{
			result.setCode(ResultEntity.ERROR);
			result.setMsg("未接收到文件");
			return result;
		}

		// 循环文件
		Map<String, MultipartFile> fileMap = new HashMap<String, MultipartFile>();
		while (itr.hasNext())
		{
			// 遍历文件
			mpf = request.getFile(itr.next());
			fileMap.put(mpf.getOriginalFilename(), mpf);
		}

		String urlStr = Configuration.UPLOAD_SERVICE_SERVER_HOST + "file/image/uploadImageWithPreview.json";
		Map<String, String> textMap = new HashMap<String, String>();
		String rtn = "";
		try
		{
			rtn = formUpload(urlStr, textMap, fileMap);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("文件上传异常");
			log.error("文件上传异常：");
			log.error(e);
		}
		JSONObject obj = JSONObject.parseObject(rtn);

		return obj;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/uploadFiles.json")
	@ResponseBody
	public Object uploadFiles(MultipartHttpServletRequest request, ModelMap modelMap)
	{
		ResultEntity result = new ResultEntity();

		// 创建迭代器
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		// 检查是否有文件
		if (!itr.hasNext())
		{
			result.setCode(ResultEntity.ERROR);
			result.setMsg("未接收到文件");
			return result;
		}

		// 循环文件
		Map<String, MultipartFile> fileMap = new HashMap<String, MultipartFile>();
		while (itr.hasNext())
		{
			// 遍历文件
			mpf = request.getFile(itr.next());
			fileMap.put(mpf.getOriginalFilename(), mpf);
		}

		String urlStr = Configuration.UPLOAD_SERVICE_SERVER_HOST + "file/uploadFiles.json";
		Map<String, String> textMap = new HashMap<String, String>();
		String rtn = "";
		try
		{
			rtn = formUpload(urlStr, textMap, fileMap);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("文件上传异常");
			log.error("文件上传异常：");
			log.error(e);
		}
		JSONObject obj = JSONObject.parseObject(rtn);

		return obj;
	}

	/**
	 * 上传图片
	 * 
	 * @param urlStr
	 * @param textMap
	 * @param fileMap
	 * @return
	 */
	private static String formUpload(String urlStr, Map<String, String> textMap, Map<String, MultipartFile> fileMap) throws Exception
	{
		String res = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符

		URL url = new URL(urlStr);
		conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(1000000);
		conn.setReadTimeout(1000000);
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
		conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

		OutputStream out = new DataOutputStream(conn.getOutputStream());
		// text
		if (textMap != null)
		{
			StringBuffer strBuf = new StringBuffer();
			Iterator iter = textMap.entrySet().iterator();
			while (iter.hasNext())
			{
				Map.Entry entry = (Map.Entry) iter.next();
				String inputName = (String) entry.getKey();
				String inputValue = (String) entry.getValue();
				if (inputValue == null)
				{
					continue;
				}
				strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
				strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
				strBuf.append(inputValue);
			}
			out.write(strBuf.toString().getBytes());
		}

		// file
		if (fileMap != null)
		{
			Iterator iter = fileMap.entrySet().iterator();
			while (iter.hasNext())
			{
				Map.Entry<String, MultipartFile> entry = (Map.Entry<String, MultipartFile>) iter.next();
				String filename = (String) entry.getKey();
				MultipartFile inputValue = entry.getValue();
				if (inputValue == null)
				{
					continue;
				}
				InputStream inputStream = inputValue.getInputStream();
				String contentType = "application/octet-stream";

				StringBuffer strBuf = new StringBuffer();
				strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
				strBuf.append("Content-Disposition: form-data; name=\"" + filename + "\"; filename=\"" + filename + "\"\r\n");
				strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

				out.write(strBuf.toString().getBytes());

				DataInputStream in = new DataInputStream(inputStream);
				int bytes = 0;
				byte[] bufferOut = new byte[1024];
				while ((bytes = in.read(bufferOut)) != -1)
				{
					out.write(bufferOut, 0, bytes);
				}
				in.close();
			}
		}

		byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
		out.write(endData);
		out.flush();
		out.close();

		// 读取返回数据
		StringBuffer strBuf = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line = null;
		while ((line = reader.readLine()) != null)
		{
			strBuf.append(line).append("\n");
		}
		res = strBuf.toString();
		reader.close();
		reader = null;

		return res;
	}
}
