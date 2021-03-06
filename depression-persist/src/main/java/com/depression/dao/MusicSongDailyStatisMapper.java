package com.depression.dao;

import com.depression.model.MusicSongDailyStatis;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MusicSongDailyStatisMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table music_song_daily_statis
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    int insert(MusicSongDailyStatis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table music_song_daily_statis
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    int insertSelective(MusicSongDailyStatis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table music_song_daily_statis
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    MusicSongDailyStatis selectByPrimaryKey(Long mscdId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table music_song_daily_statis
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    MusicSongDailyStatis selectByPrimaryKeyLock(Long mscdId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table music_song_daily_statis
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    List<MusicSongDailyStatis> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table music_song_daily_statis
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    int deleteByPrimaryKey(Long mscdId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table music_song_daily_statis
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table music_song_daily_statis
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    int enableByPrimaryKeyBulk(@Param("ids") List<Long> ids, @Param("enable") Byte enable);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table music_song_daily_statis
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    int updateByPrimaryKey(MusicSongDailyStatis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table music_song_daily_statis
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    int updateByPrimaryKeyWithBLOBs(MusicSongDailyStatis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table music_song_daily_statis
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    int updateByPrimaryKeySelective(MusicSongDailyStatis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table music_song_daily_statis
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    List<MusicSongDailyStatis> selectSelective(MusicSongDailyStatis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table music_song_daily_statis
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    List<MusicSongDailyStatis> selectSelectiveWithPage(MusicSongDailyStatis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table music_song_daily_statis
     *
     * @mbggenerated Thu Mar 02 16:42:22 CST 2017
     */
    int countSelective(MusicSongDailyStatis record);
}