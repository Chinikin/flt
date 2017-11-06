package com.depression.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.PersonalityCharactersSimilarityMapper;
import com.depression.dao.PersonalityHarmonyDescriptionMapper;
import com.depression.dao.PersonalityTestMemberMappingMapper;
import com.depression.dao.PersonalityTestResultDescMapper;
import com.depression.dao.PersonalityTestShareGroupMapper;
import com.depression.dao.PersonalityTestStatisticsMapper;
import com.depression.model.PersonalityCharactersSimilarity;
import com.depression.model.PersonalityHarmonyDescription;
import com.depression.model.PersonalityTestMemberMapping;
import com.depression.model.PersonalityTestResultDesc;
import com.depression.model.PersonalityTestShareGroup;
import com.depression.model.PersonalityTestStatistics;
import com.depression.utils.DiscUtil;

@Service
public class PersonalityTestShareGroupService
{
	private Logger log = Logger.getLogger(PersonalityTestShareGroupService.class);
	
	@Autowired
	PersonalityTestShareGroupMapper groupMapper;
	@Autowired
	PersonalityCharactersSimilarityMapper similarityMapper;
	@Autowired
	PersonalityTestStatisticsMapper statisticsMapper;
	@Autowired
	PersonalityTestResultDescMapper descMapper;
	@Autowired
	PersonalityTestMemberMappingMapper mappingMapper;
	@Autowired
	PersonalityHarmonyDescriptionMapper harmonyMapper;
	
	public int insertSelective(PersonalityTestShareGroup record)
	{
		record.setCreateTime(new Date());
		record.setModifyTime(new Date());
		return groupMapper.insertSelective(record);
	}
	
	public List<PersonalityTestShareGroup> selectSelective(PersonalityTestShareGroup record)
	{
		return groupMapper.selectSelective(record);
	}
	
	public PersonalityTestShareGroup selectByPrimaryKey(Long gId)
	{
		return groupMapper.selectByPrimaryKey(gId);
	}
	
	public int insertSimilarityAndHarmony(Long idSelf, Long idFriend)
	{
		//查询测试记录
		PersonalityTestStatistics pp = new PersonalityTestStatistics();
		pp.setMid(idSelf);
		List<PersonalityTestStatistics> ptsList = statisticsMapper.selectSelective(pp);
		if(ptsList.size()==0) return -1;
		PersonalityTestStatistics ptsSelf = ptsList.get(0);
		
		Integer[] discSelf = {	
				ptsSelf.getdVal(), 
				ptsSelf.getiVal(),
				ptsSelf.getsVal(),
				ptsSelf.getcVal()};
		
		pp = new PersonalityTestStatistics();
		pp.setMid(idFriend);
		ptsList = statisticsMapper.selectSelective(pp);
		if(ptsList.size()==0) return -1;
		PersonalityTestStatistics ptsFriend = ptsList.get(0);
		
		Integer[] discFriend = {	
				ptsFriend.getdVal(), 
				ptsFriend.getiVal(),
				ptsFriend.getsVal(),
				ptsFriend.getcVal()};
		
		//插入相似度和合拍度到自己的分享组
		{
			PersonalityTestShareGroup p = new PersonalityTestShareGroup();
			p.setMid(idSelf);
			PersonalityTestShareGroup ptsgSelf = groupMapper.selectSelective(p).get(0);
			Long gidSelf = ptsgSelf.getgId();
			
			PersonalityCharactersSimilarity ppp = new PersonalityCharactersSimilarity();
			ppp.setMid(idFriend);
			ppp.setgId(gidSelf);
			List<PersonalityCharactersSimilarity> pcsFriendList = similarityMapper.selectSelective(ppp);
			if(pcsFriendList.size() > 0)
			{
				PersonalityCharactersSimilarity pcs = pcsFriendList.get(0);
				
				double similarity = DiscUtil.calcSimilarity(discSelf, discFriend);
				pcs.setSimilarity(BigDecimal.valueOf(similarity));
				//合拍度
				String harmony = DiscUtil.analyzeHarmony(discSelf, discFriend);
				PersonalityHarmonyDescription phd = new PersonalityHarmonyDescription();
				phd.setType(harmony);
				List<PersonalityHarmonyDescription> phds = harmonyMapper.selectSelective(phd);
				if(phds.size()>0){
					pcs.setHarmonyId(phds.get(0).getHarmonyId());
				}
				
				similarityMapper.updateByPrimaryKeySelective(pcs);
				log.info("插入相似度和合拍度到自己的分享组成功！");
			}
		}
	
		//插入相似度和合拍度到朋友的分享组
		{
			PersonalityTestShareGroup p = new PersonalityTestShareGroup();
			p.setMid(idFriend);
			PersonalityTestShareGroup ptsgFriend = groupMapper.selectSelective(p).get(0);
			Long gidFriend = ptsgFriend.getgId();
			
			PersonalityCharactersSimilarity ppp = new PersonalityCharactersSimilarity();
			ppp.setMid(idSelf);
			ppp.setgId(gidFriend);
			List<PersonalityCharactersSimilarity> pcsSelfList = similarityMapper.selectSelective(ppp);
			if(pcsSelfList.size() > 0)
			{
				PersonalityCharactersSimilarity pcs = pcsSelfList.get(0);
				
				double similarity = DiscUtil.calcSimilarity(discSelf, discFriend);
				pcs.setSimilarity(BigDecimal.valueOf(similarity));
				//合拍度
				String harmony = DiscUtil.analyzeHarmony(discFriend, discSelf);
				PersonalityHarmonyDescription phd = new PersonalityHarmonyDescription();
				phd.setType(harmony);
				List<PersonalityHarmonyDescription> phds = harmonyMapper.selectSelective(phd);
				if(phds.size()>0){
					pcs.setHarmonyId(phds.get(0).getHarmonyId());
				}
				
				similarityMapper.updateByPrimaryKeySelective(pcs);
				log.info("插入相似度和合拍度到朋友的分享组！");
			}
		}
		
		return 0;
		
	}
	
	/**
	 * 计算相似度，更新自己的加入的分享列表，以及自己的分析列表
	 * @param mid
	 * @param disc
	 * @return
	 */
	public int saveSimilarityAndHarmony(Long mid, Integer[] disc)
	{
		if(disc.length != 4) return -1;
		
		/*--------------------遍历自己加入的分享组----------------------*/
		PersonalityCharactersSimilarity p = new PersonalityCharactersSimilarity();
		p.setMid(mid);
		List<PersonalityCharactersSimilarity> pcsSelfList = similarityMapper.selectSelective(p);
		
		if (pcsSelfList != null)
		{
			for(PersonalityCharactersSimilarity pcs : pcsSelfList)
			{
				PersonalityTestShareGroup ptsg = groupMapper.selectByPrimaryKey(pcs.getgId());
				PersonalityTestStatistics pp = new PersonalityTestStatistics();
				pp.setMid(ptsg.getMid());
				List<PersonalityTestStatistics> ptsList = statisticsMapper.selectSelective(pp);
				if(ptsList.size()==0) continue;
				
				//group disc值
				Integer[] discG = {	ptsList.get(0).getdVal(), 
									ptsList.get(0).getiVal(),
									ptsList.get(0).getsVal(),
									ptsList.get(0).getcVal()};
				
				double similarity = DiscUtil.calcSimilarity(discG, disc);
				pcs.setSimilarity(BigDecimal.valueOf(similarity));
				//合拍度
				String harmony = DiscUtil.analyzeHarmony(discG, disc);
				PersonalityHarmonyDescription phd = new PersonalityHarmonyDescription();
				phd.setType(harmony);
				List<PersonalityHarmonyDescription> phds = harmonyMapper.selectSelective(phd);
				if(phds.size()>0){
					pcs.setHarmonyId(phds.get(0).getHarmonyId());
				}
				
				similarityMapper.updateByPrimaryKeySelective(pcs);
			}
		}
		
		/*--------------------遍历自己的分享组----------------------*/
		PersonalityTestShareGroup ppp = new PersonalityTestShareGroup();
		ppp.setMid(mid);
		PersonalityTestShareGroup ptsgSelf = groupMapper.selectSelective(ppp).get(0);
		PersonalityCharactersSimilarity pppp = new PersonalityCharactersSimilarity();
		pppp.setgId(ptsgSelf.getgId());
		List<PersonalityCharactersSimilarity> pcsFriendList = similarityMapper.selectSelective(pppp);
		if (pcsFriendList != null)
		{
			for(PersonalityCharactersSimilarity pcs : pcsFriendList)
			{
				PersonalityTestStatistics pp = new PersonalityTestStatistics();
				pp.setMid(pcs.getMid());
				List<PersonalityTestStatistics> ptsList = statisticsMapper.selectSelective(pp);
				if(ptsList.size()==0) continue;
				
				Integer[] discF = {	ptsList.get(0).getdVal(), 
						ptsList.get(0).getiVal(),
						ptsList.get(0).getsVal(),
						ptsList.get(0).getcVal()};

				double similarity = DiscUtil.calcSimilarity(discF, disc);
				pcs.setSimilarity(BigDecimal.valueOf(similarity));
				
				//合拍度
				String harmony = DiscUtil.analyzeHarmony(disc, discF);
				PersonalityHarmonyDescription phd = new PersonalityHarmonyDescription();
				phd.setType(harmony);
				List<PersonalityHarmonyDescription> phds = harmonyMapper.selectSelective(phd);
				if(phds.size()>0){
					pcs.setHarmonyId(phds.get(0).getHarmonyId());
				}
				
				similarityMapper.updateByPrimaryKeySelective(pcs);
			}
		}
		
		return 0;
		
	}
	
	/**
	 * 分析特征，并保存数值结果
	 * @param mid
	 * @param disc
	 * @return
	 */
	public int saveCharacterAndDisc(Long mid, Integer[] disc)
	{
		if(disc.length != 4) return -1;
		
		String character = DiscUtil.analyzeCharacter(disc);
		PersonalityTestResultDesc p = new PersonalityTestResultDesc();
		p.setType(character);
		List<PersonalityTestResultDesc> ptrdList = descMapper.selectSelective(p);
		if(ptrdList.size() > 0){
			//保存特征结果
			PersonalityTestMemberMapping ptmm = new PersonalityTestMemberMapping();
			ptmm.setMid(mid);
			ptmm.setPtrdId(ptrdList.get(0).getPtrdId());
			List<PersonalityTestMemberMapping> ptmmList = mappingMapper.selectSelective(ptmm);
			if(ptmmList.size()==0){
				ptmm.setCreateTime(new Date());
				mappingMapper.insertSelective(ptmm);
			}else{
				ptmm.setPtrdId(ptmmList.get(0).getPtrdId());
				ptmm.setModifyTime(new Date());
			}
		}
		
		//保存测试结果数值
		PersonalityTestStatistics pp = new PersonalityTestStatistics();
		pp.setMid(mid);
		List<PersonalityTestStatistics> ptsList = statisticsMapper.selectSelective(pp);
		if(ptsList.size()==0){
			//新建
			PersonalityTestStatistics pts = new PersonalityTestStatistics();
			pts.setMid(mid);
			pts.setdVal(disc[0]);
			pts.setiVal(disc[1]);
			pts.setsVal(disc[2]);
			pts.setcVal(disc[3]);
			pts.setCreateTime(new Date());
			
			statisticsMapper.insertSelective(pts);
		}else{
			PersonalityTestStatistics pts = new PersonalityTestStatistics();
			pts.setPtsId(ptsList.get(0).getPtsId());
			pts.setdVal(disc[0]);
			pts.setiVal(disc[1]);
			pts.setsVal(disc[2]);
			pts.setcVal(disc[3]);
			pts.setModifyTime(new Date());
			statisticsMapper.updateByPrimaryKeySelective(pts);
		}
		
		return 0;
	}

}
