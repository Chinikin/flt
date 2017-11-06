package com.depression.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 文件信息实体
 * 
 * @author fanxinhui
 * 
 */
@JsonIgnoreProperties({ "bytes" })
public class FileMeta
{
	private String fileName;
	private String fileSize;
	private String fileType;
	private String filePath;
	private String fileRelPath;
	private String filePreviewPath;
	private String filePreviewRelPath;

	public String getFilePreviewPath()
	{
		return filePreviewPath;
	}

	public void setFilePreviewPath(String filePreviewPath)
	{
		this.filePreviewPath = filePreviewPath;
	}

	public String getFilePreviewRelPath()
	{
		return filePreviewRelPath;
	}

	public void setFilePreviewRelPath(String filePreviewRelPath)
	{
		this.filePreviewRelPath = filePreviewRelPath;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getFileSize()
	{
		return fileSize;
	}

	public void setFileSize(String fileSize)
	{
		this.fileSize = fileSize;
	}

	public String getFileType()
	{
		return fileType;
	}

	public void setFileType(String fileType)
	{
		this.fileType = fileType;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public String getFileRelPath()
	{
		return fileRelPath;
	}

	public void setFileRelPath(String fileRelPath)
	{
		this.fileRelPath = fileRelPath;
	}

}
