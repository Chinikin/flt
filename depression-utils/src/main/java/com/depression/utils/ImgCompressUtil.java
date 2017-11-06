package com.depression.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * @author wuyu
 * @versions 1.0 图片压缩工具类 提供的方法中可以设定生成的 缩略图片的大小尺寸等
 */
public class ImgCompressUtil
{
	/**
	 * 程序入口 将图片按照指定的图片尺寸压缩
	 * 
	 * @param srcImgPath
	 *            源图片路径
	 * @param outImgPath
	 *            输出的压缩图片的路径
	 * @param new_w
	 *            压缩后的图片宽
	 * @param new_h
	 *            压缩后的图片高
	 */
	public static String compressImage(InputStream inputStream, String outImgPath, int new_w, int new_h, String fileExt)
	{
		System.err.println(outImgPath);
		BufferedImage src = InputImage(inputStream);
		return disposeImage(src, outImgPath, new_w, new_h, fileExt);
	}

	/**
	 * 读取图片
	 */
	private static BufferedImage InputImage(InputStream inputStream)
	{
		BufferedImage srcImage = null;
		try
		{
			srcImage = ImageIO.read(inputStream);
		} catch (IOException e)
		{
			System.out.println("读取图片文件出错！" + e.getMessage());
			e.printStackTrace();
		}
		return srcImage;
	}

	/**
	 * 处理图片
	 * 
	 * @param src
	 * @param outImgPath
	 * @param new_w
	 * @param new_h
	 */
	private synchronized static String disposeImage(BufferedImage src, String outImgPath, int new_w, int new_h, String fileExt)
	{
		// 得到源图宽
		int old_w = src.getWidth();
		// 得到源图长
		int old_h = src.getHeight();

		if (old_w / old_h < new_w / new_h)
		{
			new_h = (int) (old_h * new_w / old_w);
		} else
		{
			new_w = (int) (old_w * new_h / old_h);
		}

		BufferedImage newImg = null;
		// 判断输入图片的类型
		switch (src.getType())
		{
		case 13:
			// png,gif
			newImg = new BufferedImage(new_w, new_h, BufferedImage.TYPE_4BYTE_ABGR);
			break;
		default:
			newImg = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
			break;
		}
		Graphics2D g = newImg.createGraphics();
		
		// 从原图上取颜色绘制新图
		g.drawImage(src, 0, 0, old_w, old_h, null);
		g.dispose();
		
		// 根据图片尺寸压缩比得到新图的尺寸
		newImg.getGraphics().drawImage(src.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0, 0, null);
		
		// 调用方法输出图片文件
		String ext = "-" + new_w + "x" + new_h + fileExt;
		outImgPath += ext;
		OutImage(outImgPath, newImg);

		return ext;
	}

	/**
	 * 将图片文件输出到指定的路径，并可设定压缩质量
	 * 
	 * @param outImgPath
	 * @param newImg
	 * @param
	 */
	private static void OutImage(String outImgPath, BufferedImage newImg)
	{
		// 判断输出的文件夹路径是否存在，不存在则创建
		File file = new File(outImgPath);
		if (!file.getParentFile().exists())
		{
			file.getParentFile().mkdirs();
		} // 输出到文件流

		try
		{
			ImageIO.write(newImg, outImgPath.substring(outImgPath.lastIndexOf(".") + 1), new File(outImgPath));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
