/******************************************************************
** 类    名：AppReleaseInfo
** 描    述：
** 创 建 者：bianj
** 创建时间：2016-06-21 14:05:23
******************************************************************/
package com.depression.model;
/**
 * (APP_RELEASE_INFO)
 * 
 * @author bianj
 * @version 1.0.0 2016-06-21
 */
public class AppReleaseInfo extends Page implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 6189421574934996241L;
    
    public static final int OS_TYPE_ANDROID = 0;
    public static final int OS_TYPE_IOS = 1;
    
    /**  */
    private Long releaseId;
    
    /**  */
    private String versionName;
    
    /**  */
    private String versionNum;
    
    /**  */
    private Integer svnNum;
    
    /**  */
    private String path;
    
    private Integer osType;
    
    private String note;
    
    public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	/**  */
    private Integer isDelete;
    
    /**
     * 获取
     * 
     * @return 
     */
    public Long getReleaseId() {
        return this.releaseId;
    }
     
    /**
     * 设置
     * 
     * @param releaseId
     *          
     */
    public void setReleaseId(Long releaseId) {
        this.releaseId = releaseId;
    }
    
    /**
     * 获取
     * 
     * @return 
     */
    public String getVersionName() {
        return this.versionName;
    }
     
    /**
     * 设置
     * 
     * @param versionName
     *          
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
    
    /**
     * 获取
     * 
     * @return 
     */
    public String getVersionNum() {
        return this.versionNum;
    }
     
    /**
     * 设置
     * 
     * @param versionNum
     *          
     */
    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }
    
    /**
     * 获取
     * 
     * @return 
     */
    public Integer getSvnNum() {
        return this.svnNum;
    }
     
    /**
     * 设置
     * 
     * @param svnNum
     *          
     */
    public void setSvnNum(Integer svnNum) {
        this.svnNum = svnNum;
    }
    

    public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public Integer getOsType()
	{
		return osType;
	}

	public void setOsType(Integer osType)
	{
		this.osType = osType;
	}

	/**
     * 获取
     * 
     * @return 
     */
    public Integer getIsDelete() {
        return this.isDelete;
    }
     
    /**
     * 设置
     * 
     * @param isDelete
     *          
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}