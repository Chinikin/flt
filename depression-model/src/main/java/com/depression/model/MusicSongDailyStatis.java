package com.depression.model;

import java.util.Date;

public class MusicSongDailyStatis extends Page {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column music_song_daily_statis.mscd_id
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    /* @Comment(主键) */
    private Long mscdId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column music_song_daily_statis.ms_id
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    /* @Comment(音乐类别) */
    private Long msId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column music_song_daily_statis.record_date
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    /* @Comment(记录日期) */
    private Date recordDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column music_song_daily_statis.play_count
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    /* @Comment(播发次数) */
    private Integer playCount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column music_song_daily_statis.create_time
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    /* @Comment(创建时间) */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column music_song_daily_statis.modify_time
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    /* @Comment(修改时间) */
    private Date modifyTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column music_song_daily_statis.is_enable
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    /* @Comment(0启用，1禁用) */
    private Byte isEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column music_song_daily_statis.is_delete
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    /* @Comment(0正常，1删除) */
    private Byte isDelete;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column music_song_daily_statis.mscd_id
     *
     * @return the value of music_song_daily_statis.mscd_id
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    public Long getMscdId() {
        return mscdId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column music_song_daily_statis.mscd_id
     *
     * @param mscdId the value for music_song_daily_statis.mscd_id
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    public void setMscdId(Long mscdId) {
        this.mscdId = mscdId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column music_song_daily_statis.ms_id
     *
     * @return the value of music_song_daily_statis.ms_id
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    public Long getMsId() {
        return msId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column music_song_daily_statis.ms_id
     *
     * @param msId the value for music_song_daily_statis.ms_id
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    public void setMsId(Long msId) {
        this.msId = msId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column music_song_daily_statis.record_date
     *
     * @return the value of music_song_daily_statis.record_date
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    public Date getRecordDate() {
        return recordDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column music_song_daily_statis.record_date
     *
     * @param recordDate the value for music_song_daily_statis.record_date
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column music_song_daily_statis.play_count
     *
     * @return the value of music_song_daily_statis.play_count
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    public Integer getPlayCount() {
        return playCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column music_song_daily_statis.play_count
     *
     * @param playCount the value for music_song_daily_statis.play_count
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column music_song_daily_statis.create_time
     *
     * @return the value of music_song_daily_statis.create_time
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column music_song_daily_statis.create_time
     *
     * @param createTime the value for music_song_daily_statis.create_time
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column music_song_daily_statis.modify_time
     *
     * @return the value of music_song_daily_statis.modify_time
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column music_song_daily_statis.modify_time
     *
     * @param modifyTime the value for music_song_daily_statis.modify_time
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column music_song_daily_statis.is_enable
     *
     * @return the value of music_song_daily_statis.is_enable
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    public Byte getIsEnable() {
        return isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column music_song_daily_statis.is_enable
     *
     * @param isEnable the value for music_song_daily_statis.is_enable
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    public void setIsEnable(Byte isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column music_song_daily_statis.is_delete
     *
     * @return the value of music_song_daily_statis.is_delete
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    public Byte getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column music_song_daily_statis.is_delete
     *
     * @param isDelete the value for music_song_daily_statis.is_delete
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}