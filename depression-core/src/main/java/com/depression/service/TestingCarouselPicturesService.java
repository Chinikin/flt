package com.depression.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.TestingCarouselPicturesDAO;
import com.depression.dao.TestingCarouselPicturesMapper;
import com.depression.model.TestingCarouselPictures;
import com.depression.model.TestingCarouselPicturesOld;

/**
 * 问卷轮播图片
 * 
 * @author fanxinhui
 * @date 2016/7/11
 */
@Service
public class TestingCarouselPicturesService
{
	@Autowired
	private TestingCarouselPicturesMapper testingCarouselPicturesMapper;
	
	@Autowired
	private TestingCarouselPicturesDAO testingCarouselPicturesDAO;

	

	public Integer insertTestingCarouselPictures(TestingCarouselPictures testingCarouselPictures){
		testingCarouselPictures.setType(0);// 设置为专业测试类型
		testingCarouselPictures.setIsDelete((byte)0);
		return testingCarouselPicturesMapper.insertSelective(testingCarouselPictures );
	}

	public void updateTestingCarouselPictures(TestingCarouselPictures testingCarouselPictures){
		testingCarouselPicturesMapper.updateByPrimaryKeySelective(testingCarouselPictures);
	}


	public TestingCarouselPictures getTestingCarouselPicturesByPrimaryId(Long primaryId){
		return testingCarouselPicturesMapper.selectByPrimaryKey(primaryId);
	}


	public List<TestingCarouselPictures> getTestingCarouselPicturesListByQueryTestingCarouselPictures(TestingCarouselPictures testingCarouselPictures)
	{
		
		return testingCarouselPicturesMapper.selectSelective(testingCarouselPictures);
	}

	// 分页数据
	public List<TestingCarouselPictures> getPageList(TestingCarouselPictures testingCarouselPictures)
	{
		return testingCarouselPicturesMapper.selectSelectiveWithPage(testingCarouselPictures);
	}

	// 分页总条数
	public Integer getPageCounts(TestingCarouselPictures testingCarouselPictures)
	{
		return testingCarouselPicturesMapper.countSelective(testingCarouselPictures);
	}

	public void updateTestingCarouselPicturesStatus(String ids, Byte isDel) {
		
	}

	public void updateTestingCarouselPicturesStatusByPrimaryIds(String ids,Byte isDel) {
		// 处理记录id
				ids = ids.replace("[", "").replace("]", "");
				String[] id = ids.split(",");
				List<Long> idList = new ArrayList<Long>();
				for (int idx = 0; idx < id.length; idx++){
					if (id[idx] != null && !id[idx].equals("")){
						idList.add(Long.parseLong(id[idx]));
					}
				}

				// 修改记录状态：0启用，1禁用
				if (isDel != null){
					testingCarouselPicturesMapper.updateTestingCarouselPicturesStatusByPrimaryIds(idList,isDel );
				}
	}
	// 分页总条数
	
	
}
