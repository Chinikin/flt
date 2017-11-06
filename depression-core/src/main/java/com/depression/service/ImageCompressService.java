package com.depression.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCompressService
{

	/**
	 * 图片转储
	 * 
	 * @param input
	 * @param file
	 * @throws ImageFormatException
	 * @throws IOException
	 */
	public static void saveImage(byte[] imageBytes, File file) throws IOException
	{
		ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();
		memoryStream.write(imageBytes, 0, imageBytes.length);
		FileOutputStream fs = new FileOutputStream(file);
		memoryStream.writeTo(fs);
		fs.flush();
		fs.close();
	}

}
