package com.depression.service;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 图片存储
 * 
 * @author ax
 * 
 */
@Service
public class ImageStorageService
{
	@Autowired
	private EncryptService encryptService;

	private final static long maxImageSize = 1024 * 1024 * 4;

	/**
	 * 将提供的图片数据经过压缩处理后保存到指定的路径
	 * 
	 * @param imageBytes
	 *            图片数据
	 * @param uploadPath
	 *            保存地址(绝对路径)
	 * @return 所保存图片的MD5
	 * @throws IOException
	 */
	public String saveAsLocal(byte[] imageBytes, String uploadPath)
	{
		if (imageBytes == null)
		{
			throw new RuntimeException("请上传图片");
		}
		if (imageBytes.length > maxImageSize)
		{
			return "图片大小不能超过4M";
		}

		// 获取上传图片的目录
		File uploadDir = new File(uploadPath);
		File dest = null;
		if (!uploadDir.exists())
		{
			uploadDir.mkdirs();
		}

		try
		{
			// 生成md5校验码
			String MD5CheckCode = encryptService.encodeMd5(imageBytes) + ".jpg";
			dest = new File(uploadDir, MD5CheckCode);

			// 保存
			ImageCompressService.saveImage(imageBytes, dest);
			return MD5CheckCode;
		} catch (IOException e)
		{
			throw new RuntimeException("上传的文件不符合要求，请重新上传");
		}
	}

}
