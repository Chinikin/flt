package com.depression.dao;

import java.util.List;
import java.util.Map;

import com.depression.model.TestingCarouselPicturesOld;

/**
 * 问卷轮播图片
 * 
 * @author fanxinhui
 * @date 2016/7/11
 */
public interface TestingCarouselPicturesDAO
{

	public int checkTestingCarouselPicturesExits(TestingCarouselPicturesOld testingCarouselPictures);

	public Integer insertTestingCarouselPictures(TestingCarouselPicturesOld testingCarouselPictures);

	public void updateTestingCarouselPictures(TestingCarouselPicturesOld testingCarouselPictures);

	public void updateTestingCarouselPicturesEnableByPrimaryIds(List<String> primaryIds);

	public void updateTestingCarouselPicturesDisableByPrimaryIds(List<String> primaryIds);

	public void deleteTestingCarouselPictures(String primaryIds);

	public List<TestingCarouselPicturesOld> getTestingCarouselPicturesList();

	public TestingCarouselPicturesOld getTestingCarouselPicturesByPrimaryId(Long primaryIds);

	public List<TestingCarouselPicturesOld> getTestingCarouselPicturesListByQuery(Map<String, Object> paramMap);

	public List<TestingCarouselPicturesOld> getTestingCarouselPicturesListByQueryTestingCarouselPictures(TestingCarouselPicturesOld testingCarouselPictures);

	// 分页数据
	public List<TestingCarouselPicturesOld> getPageList(TestingCarouselPicturesOld testingCarouselPictures);

	// 分页总条数
	public Long getPageCounts(TestingCarouselPicturesOld testingCarouselPictures);

}
