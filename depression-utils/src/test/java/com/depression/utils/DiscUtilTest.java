package com.depression.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.depression.entity.ErrorCode;
import com.depression.entity.QuestionResult;

public class DiscUtilTest
{
	@Test
	public void calibrateDiscTest() throws Exception{
		Integer[] disc = {5, 1, -1, 0};
		DiscUtil.calibrateDisc(disc);
		System.out.println(Arrays.asList(disc));
	}
	
	@Test
	public void analyzeCharacterTest() throws Exception{
		Integer[] disc = {2, 0, 1, 10};
		System.out.println(DiscUtil.analyzeCharacter(disc));
	}

	@Test
	public void calcSimilarityTest() throws Exception{
		Integer[] discA = {2, 2, 10, 30};
		Integer[] discB = {2, 0, 1, 10};
		System.out.println(DiscUtil.calcSimilarity(discA, discB));
	}
	
	@Test
	public void countDiscTest() throws Exception{
		String resultJson = "{result:[{\"a\":\"我很容易相处\",\"b\":\"我总是很宽容\"},{\"a\":\"我是人群焦点\",\"b\":\"我总是避免冲突\"},{\"a\":\"我喜欢与人一起工作\",\"b\":\"我目标总是很明确\"}]}";
		try{
			JSONObject json =  JSONObject.parseObject(resultJson);
			System.out.println(json.getString("result"));
			List<QuestionResult> questionResults = 
					JSON.parseObject(json.getString("result"),  new TypeReference<ArrayList<QuestionResult>>(){});
			System.out.println(questionResults.size());
			System.out.println(Arrays.asList(DiscUtil.countDisc(questionResults)));
		}catch (Exception e){
			System.out.println(ErrorCode.ERROR_PARAM_JSON.getMessage());
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void sendMessage() throws Exception{
		SmsUtil.sendSms("13735514728", "181436", "25");
		System.out.println("发送成功");	
	}
	
}
