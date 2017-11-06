package com.depression.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.MemberMapper;
import com.depression.dao.ServiceGoodsMapper;
import com.depression.dao.ServiceGoodsPriceScopeMapper;
import com.depression.model.Member;
import com.depression.model.ServiceGoods;
import com.depression.model.ServiceGoodsPriceScope;

@Service
public class ServiceGoodsService
{
	@Autowired
	ServiceGoodsMapper serviceGoodsMapper;
	@Autowired
	MemberMapper memberMapper;
	@Autowired
	ServiceGoodsPriceScopeMapper priceScopeMapper;

	final static byte SERVICE_TYPE_VOICE_IMM_ADVISORY = 0;
	final static byte SERVICE_TYPE_VOICE_IMM_LISTEN = 1;

	/**
	 * 创建实时咨询商品
	 * 
	 * @param mid
	 *            咨询师id
	 * @param duration
	 *            时长
	 * @param price
	 *            价格
	 * @return
	 */
	public Integer createImmVoiceGoods(Long mid)
	{
		ServiceGoods serviceGoods = new ServiceGoods();
		serviceGoods.setMid(mid);
		Member member = memberMapper.selectByPrimaryKey(mid);
		if (member.getpLevel() == MemberService.P_LEVEL_LISTENER)
		{
			serviceGoods.setType(SERVICE_TYPE_VOICE_IMM_LISTEN);
			serviceGoods.setDuration(25);
			serviceGoods.setPrice(BigDecimal.valueOf(50));
			serviceGoods.setName("倾听");
			serviceGoods.setDescription("倾听师向求助者提供电话倾听服务");
		} else
		{
			serviceGoods.setType(SERVICE_TYPE_VOICE_IMM_ADVISORY);
			serviceGoods.setDuration(25);
			serviceGoods.setPrice(BigDecimal.valueOf(200));
			serviceGoods.setName("咨询");
			serviceGoods.setDescription("咨询师向求助者提供电话咨询服务");
		}

		return serviceGoodsMapper.insertSelective(serviceGoods);
	}

	/**
	 * 修改服务商品价格
	 * 
	 * @param sgid
	 *            服务商品id
	 * @param price
	 *            价格
	 * @return
	 */
	public Integer updatePrice(Long sgid, BigDecimal price)
	{
		ServiceGoods serviceGoods = new ServiceGoods();
		serviceGoods.setSgid(sgid);
		serviceGoods.setPrice(price);
		return serviceGoodsMapper.updateByPrimaryKeySelective(serviceGoods);
	}

	/**
	 * 修改服务商品市场
	 * 
	 * @param sgid
	 *            服务商品id
	 * @param duration
	 *            时长
	 * @return
	 */
	public Integer updateDuration(Long sgid, Integer duration)
	{
		ServiceGoods serviceGoods = new ServiceGoods();
		serviceGoods.setSgid(sgid);
		serviceGoods.setDuration(duration);
		return serviceGoodsMapper.updateByPrimaryKeySelective(serviceGoods);
	}

	/**
	 * 根据服务者（咨询师）id获取商品
	 * 
	 * @param mid
	 *            咨询师id
	 * @param type
	 *            商品类型
	 * @return
	 */
	public ServiceGoods selectByMid(Long mid, Byte type)
	{
		ServiceGoods sg = new ServiceGoods();
		sg.setMid(mid);
		sg.setType(type);
		List<ServiceGoods> sgs = serviceGoodsMapper.selectSelective(sg);
		if (sgs.size() > 0)
			return sgs.get(0);
		return null;
	}

	/**
	 * 根据服务者（咨询师）id获取实时咨询商品,没有则创建默认(50元/25分钟)
	 * 
	 * @param mid
	 * @return
	 */
	public synchronized ServiceGoods selectImmVoiceByMid(Long mid)
	{
		Member member = memberMapper.selectByPrimaryKey(mid);
		Byte type = SERVICE_TYPE_VOICE_IMM_ADVISORY;
		if (member.getpLevel() == MemberService.P_LEVEL_LISTENER)
		{
			type = SERVICE_TYPE_VOICE_IMM_LISTEN;
		}
		ServiceGoods serviceGoods = selectByMid(mid, type);
		if (serviceGoods == null)
		{
			createImmVoiceGoods(mid);
		}
		return selectByMid(mid, type);
	}

	/**
	 * 启用服务商品
	 * 
	 * @param sgid
	 *            服务商品id
	 * @return
	 */
	public Integer enable(Long sgid)
	{
		List<Long> ids = new ArrayList<Long>();
		ids.add(sgid);
		return serviceGoodsMapper.enableByPrimaryKeyBulk(ids, (byte) 0);
	}

	/**
	 * 根据咨询师id，启用立即咨询服务
	 * 
	 * @param mid
	 * @return
	 */
	public Integer enableImmVoiceByMid(Long mid)
	{
		ServiceGoods serviceGoods = selectImmVoiceByMid(mid);
		if (serviceGoods == null)
			return 0;

		List<Long> ids = new ArrayList<Long>();
		ids.add(serviceGoods.getSgid());

		return serviceGoodsMapper.enableByPrimaryKeyBulk(ids, (byte) 0);
	}

	/**
	 * 禁用服务商品
	 * 
	 * @param sgid
	 *            服务商品id
	 * @return
	 */
	public Integer disable(Long sgid)
	{
		List<Long> ids = new ArrayList<Long>();
		ids.add(sgid);
		return serviceGoodsMapper.enableByPrimaryKeyBulk(ids, (byte) 1);
	}

	/**
	 * 根据咨询师id，禁用立即咨询服务
	 * 
	 * @param mid
	 * @return
	 */
	public Integer disableImmVoiceByMid(Long mid)
	{
		ServiceGoods serviceGoods = selectImmVoiceByMid(mid);
		if (serviceGoods == null)
			return 0;

		List<Long> ids = new ArrayList<Long>();
		ids.add(serviceGoods.getSgid());

		return serviceGoodsMapper.enableByPrimaryKeyBulk(ids, (byte) 1);
	}

	/**
	 * 根据主键查询记录
	 * 
	 * @param sgid
	 *            服务商品id
	 * @return
	 */
	public ServiceGoods selectByPrimaryKey(Long sgid)
	{
		return serviceGoodsMapper.selectByPrimaryKey(sgid);
	}

	/**
	 * 根据主键修改服务商品
	 * 
	 * @param record
	 * @return
	 */
	public int updateByPrimaryKey(ServiceGoods record)
	{
		return serviceGoodsMapper.updateByPrimaryKey(record);
	}

	/**
	 * 根据咨询数范围查找
	 * 
	 * @param startAnswerCount
	 * @param endAnswerCount
	 * @return
	 */
	public ServiceGoods selectByTimes(Integer startAnswerCount, Integer endAnswerCount)
	{
		return serviceGoodsMapper.selectByTimes(startAnswerCount, endAnswerCount);
	}
	
	/**
	 * 创建语音轻咨询价格范围
	 * @return
	 */
	public Integer sgpsCreateVoiceListenScope()
	{
		ServiceGoodsPriceScope priceScope = new ServiceGoodsPriceScope();
		priceScope.setType(SERVICE_TYPE_VOICE_IMM_LISTEN);
		priceScope.setFloor(0);
		priceScope.setCeiling(100);
		priceScope.setCreateTime(new Date());
		priceScope.setModifyTime(new Date());
		
		return priceScopeMapper.insertSelective(priceScope);
	}
	
	/**
	 * 创建语音专业咨询价格范围
	 * @return
	 */
	public Integer sgpsCreateVoiceAdvisoryScope()
	{
		ServiceGoodsPriceScope priceScope = new ServiceGoodsPriceScope();
		priceScope.setType(SERVICE_TYPE_VOICE_IMM_ADVISORY);
		priceScope.setFloor(0);
		priceScope.setCeiling(5000);
		priceScope.setCreateTime(new Date());
		priceScope.setModifyTime(new Date());
		
		return priceScopeMapper.insertSelective(priceScope);
	}
	
	/**
	 * 获取语音轻咨询价格范围
	 * @return
	 */
	public ServiceGoodsPriceScope sgpsObtainVoiceListenScope()
	{
		ServiceGoodsPriceScope sgps = new ServiceGoodsPriceScope();
		sgps.setType(SERVICE_TYPE_VOICE_IMM_LISTEN);
		
		List<ServiceGoodsPriceScope> sgpss = priceScopeMapper.selectSelective(sgps);
		if(sgpss.size()==0)
		{
			sgpsCreateVoiceListenScope();
			sgpss = priceScopeMapper.selectSelective(sgps);
		}
		
		return sgpss.get(0);
	}
	
	/**
	 * 获取语音专业咨询价格范围
	 * @return
	 */
	public ServiceGoodsPriceScope sgpsObtainVoiceAdvisoryScope()
	{
		ServiceGoodsPriceScope sgps = new ServiceGoodsPriceScope();
		sgps.setType(SERVICE_TYPE_VOICE_IMM_ADVISORY);
		
		List<ServiceGoodsPriceScope> sgpss = priceScopeMapper.selectSelective(sgps);
		if(sgpss.size()==0)
		{
			sgpsCreateVoiceAdvisoryScope();
			sgpss = priceScopeMapper.selectSelective(sgps);
		}
		
		return sgpss.get(0);
	}
	
	/**
	 * 获取服务商品价格区间
	 * @param sgid 实际商品id
	 * @return
	 */
	public ServiceGoodsPriceScope sgpsObtainPriceScope(Long sgid)
	{
		ServiceGoods serviceGoods = serviceGoodsMapper.selectByPrimaryKey(sgid);
		if(serviceGoods == null)
		{
			return null;
		}
		
		if(serviceGoods.getType().equals(SERVICE_TYPE_VOICE_IMM_LISTEN))
		{
			return sgpsObtainVoiceListenScope();
		}else if(serviceGoods.getType().equals(SERVICE_TYPE_VOICE_IMM_ADVISORY))
		{
			return sgpsObtainVoiceAdvisoryScope();
		}
		
		return null;
	}
	
	/**
	 * 修改价格区间
	 * @param sgpsId 价格区间id
	 * @param floor 下限
	 * @param ceiling 上限
	 * @return
	 */
	public Integer sgpsModifyPriceScope(Long sgpsId, Integer floor, Integer ceiling)
	{
		ServiceGoodsPriceScope sgps = new ServiceGoodsPriceScope();
		sgps.setSgpsId(sgpsId);
		sgps.setFloor(floor);
		sgps.setCeiling(ceiling);
		
		return priceScopeMapper.updateByPrimaryKeySelective(sgps);
	}
}
