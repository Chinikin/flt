package com.depression.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.depression.entity.QuestionResult;

class Option
{
	public Option(String m, String l, String description)
	{
		super();
		M = m;
		L = l;
		this.description = description;
	}
	String M;
	String L;
	String description;
}
class Question
{
	
	public Question(List<Option> options)
	{
		super();
		this.options = options;
	}

	List<Option> options;
}

class Paper
{
	
	public Paper(List<Question> questions)
	{
		super();
		this.questions = questions;
	}

	List<Question> questions;
}

public class DiscUtil
{	
	
	public static String paperMaterial[][][] = new String[][][]{
		{//Question
			{"S", "S", "我很容易相处"},
			{"I", "I", "我容易相信别人"},
			{"O", "D", "我喜欢冒险"},
			{"C", "C", "我总是很宽容"},
		},
		{//Question
			{"C", "O", "我温文尔雅"},
			{"D", "D", "我乐观向上"},
			{"O", "I", "我是人群焦点"},
			{"S", "S", "我总是避免冲突"},
		},
		{//Question
			{"I", "I", "我善于鼓舞士气"},
			{"O", "C", "我追求尽善尽美"},
			{"O", "S", "我喜欢与人一起工作"},
			{"D", "O", "我目标总是很明确"},
		},
		{//Question
			{"C", "C", "我害怕受挫"},
			{"S", "S", "我隐忍内敛"},
			{"O", "I", "我善于阐述"},
			{"D", "D", "我敢于说不"},
		},
		{//Question
			{"I", "O", "我能言善辩"},
			{"D", "D", "我坚定不移"},
			{"S", "S", "我追求平衡"},
			{"O", "C", "我循规蹈矩"},
		},
		{//Question
			{"C", "O", "我善于管理时间"},
			{"D", "D", "我匆匆忙忙压力山大"},
			{"I", "I", "我重视交际"},
			{"S", "S", "我有始有终"},
		},
		{//Question
			{"S", "O", "我拒绝突变"},
			{"I", "I", "我承诺过多"},
			{"O", "C", "我扛不住重压"},
			{"O", "D", "我不怕争论"},
		},
		{//Question
			{"I", "I", "我是鼓励小能手"},
			{"S", "S", "我是聆听小能手"},
			{"C", "C", "我是分析小能手"},
			{"D", "D", "我是分配小能手"},
		},
		{//Question
			{"D", "D", "我总是关注结果"},
			{"C", "C", "我追求完美"},
			{"O", "I", "我享受过程"},
			{"O", "S", "我注重团体协作"},
		},
		{//Question
			{"O", "C", "能不买就不买"},
			{"D", "D", "想买就买买买"},
			{"S", "S", "打折了才会买"},
			{"I", "O", "有需要才会买"},
		},
		{//Question
			{"S", "O", "我很有亲和力"},
			{"O", "I", "我不守常规"},
			{"D", "D", "我总是很活跃"},
			{"C", "C", "我注重细节"},
		},
		{//Question
			{"O", "S", "我容易让步"},
			{"C", "O", "我太注意细节"},
			{"I", "I", "我容易临时变卦"},
			{"D", "D", "我总是诸多要求"},
		},	
		{//Question
			{"D", "D", "我渴望提升"},
			{"S", "O", "我满意当前"},
			{"I", "O", "我情绪外露"},
			{"O", "C", "我谦虚低调"},
		},	
		{//Question
			{"C", "C", "我冷静保守"},
			{"I", "I", "我开心快乐"},
			{"S", "O", "我充满善意"},
			{"D", "D", "我敢作敢为"},
		},	
		{//Question
			{"S", "S", "跟团安逸的旅行"},
			{"C", "O", "做好攻略的旅行"},
			{"I", "I", "说走就走的旅行"},
			{"D", "D", "目标明确的旅行"},
		},	
		{//Question
			{"O", "D", "我认为规则可以挑战"},
			{"C", "O", "我认为规则带来公平"},
			{"I", "I", "我认为规则令人约束"},
			{"S", "S", "我认为规则保人安全"},
		},	
		{//Question
			{"O", "C", "我喜欢学习"},
			{"D", "D", "我喜欢获奖"},
			{"S", "S", "我喜欢稳妥"},
			{"I", "O", "我喜欢社交"},
		},	
		{//Question
			{"D", "D", "我敢于承担"},
			{"O", "I", "我热情外向"},
			{"O", "S", "我情绪稳定"},
			{"C", "O", "我小心谨慎"},
		},	
		{//Question
			{"D", "D", "我一贯意志坚强"},
			{"S", "O", "我喜欢依照指示"},
			{"I", "I", "我总是充满激情"},
			{"O", "C", "我向来井井有条"},
		},	
		{//Question
			{"D", "O", "我一马当先"},
			{"S", "S", "我跟随到底"},
			{"I", "I", "我说服别人"},
			{"C", "O", "我注重事实"},
		},	
		{//Question
			{"S", "S", "我先人后己"},
			{"D", "D", "我争强好胜"},
			{"I", "I", "我乐观进取"},
			{"O", "C", "我注重逻辑"},
		},	
		{//Question
			{"S", "S", "我迎合主流"},
			{"O", "I", "我生性活泼"},
			{"D", "D", "我勇敢胆大"},
			{"C", "C", "我安静拘谨"},
		},	
		{//Question
			{"O", "D", "我喜欢掌控一切"},
			{"I", "O", "我喜欢尝试新机会"},
			{"S", "S", "我避免正面冲突"},
			{"O", "C", "我喜欢清晰的方向"},
		},	
		{//Question
			{"O", "S", "可依靠 能信任"},
			{"I", "I", "爱创新 有个性"},
			{"D", "O", "有节操 重结果"},
			{"O", "O", "高标准 严要求"},
		},	
	};
	
	public static Paper paper;
	static{
		List<Question> questions = new ArrayList<Question>();
		for(String[][] questionMaterial : paperMaterial)
		{
			List<Option> options = new ArrayList<Option>(); 
			for(String[] optionMaterial : questionMaterial)
			{
				options.add(new Option(optionMaterial[0], optionMaterial[1], optionMaterial[2]));
			}
			questions.add(new Question(options));
		}
		paper = new Paper(questions);
	}
	

	
	public static Integer[] countDisc(List<QuestionResult> questionResults)
	{
		Integer[] disc = {0, 0, 0, 0};
		for(int i=0; i<questionResults.size(); i++)
		{
			for(Option o : paper.questions.get(i).options)
			{
				//m选项
				if(questionResults.get(i).a.equals(o.description)){
					if(o.M.equals("D")){
						disc[0]++;
					}else if(o.M.equals("I")){
						disc[1]++;
					}else if(o.M.equals("S")){
						disc[2]++;
					}else if(o.M.equals("C")){
						disc[3]++;
					}
				}
				//l选项
				if(questionResults.get(i).b.equals(o.description)){
					if(o.L.equals("D")){
						disc[0]--;
					}else if(o.L.equals("I")){
						disc[1]--;
					}else if(o.L.equals("S")){
						disc[2]--;
					}else if(o.L.equals("C")){
						disc[3]--;
					}
				}
			}
		}
		return calibrateDisc(disc);
	}
	
	/**
	 * disc 扣除人群平均测试结果
	 * @param disc
	 * @return  校正后的disc值；传入数组长度不等于4时返回null；
	 */
	public static Integer[] calibrateDisc(Integer[] disc)
	{
		//人群测试平均值                          D   I   S  C
		Integer[] standard = { 0 , 0 , 1 ,0 };
		
		if(disc.length != 4) return null;
		
		disc[0] -= standard[0];
		disc[1] -= standard[1];
		disc[2] -= standard[2];
		disc[3] -= standard[3];
		
		return disc;
	}
	
	/**
	 * 分析disc的特性，只支持最多两个显性因子
	 * @param disc
	 * @return disc特性字符串；传入数组长度不等于4时返回null；
	 */
	public static String analyzeCharacter(Integer[] disc)
	{
		if(disc.length != 4) return null;
		
		Map<String, Integer> discMap = new HashMap<String, Integer>();
		discMap.put("D", disc[0]);
		discMap.put("I", disc[1]);
		discMap.put("S", disc[2]);
		discMap.put("C", disc[3]);
		
		List<Map.Entry<String, Integer>> discInfo =
			    new ArrayList<Map.Entry<String, Integer>>(discMap.entrySet());
		
		//排序
		Collections.sort(discInfo, new Comparator<Map.Entry<String, Integer>>() {   
		    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {      
		       return (o2.getValue() - o1.getValue()); 
		    }
		}); 
		
		//将显性（正）的因子合并
		String character = "";
		for(Map.Entry<String, Integer> e : discInfo)
		{
			if(e.getValue()>0)
			{
				character += e.getKey();
			}
		}
		
		if(character.length()>2 || character.length()==0)
		{
			return "OTHER";
		}
		
		return character;
	}

	/**
	 * 计算相似度，
	 * @param discA
	 * @param discB
	 * @return 相似度（cos()值）(0~1)；  传入数组长度不等于4时返回-2； disc全部为0时返回-3；
	 */
	public static double calcSimilarity(Integer[] discA, Integer[] discB)
	{
		if(discA.length != 4 || discB.length != 4) return -2;
		
		double numerator = discA[0]*discB[0] + discA[1]*discB[1] + 
				discA[2]*discB[2] + discA[3]*discB[3];
		
		double denominator = Math.sqrt(
				(discA[0]*discA[0] + discA[1]*discA[1] + discA[2]*discA[2] + discA[3]*discA[3])*
				(discB[0]*discB[0] + discB[1]*discB[1] + discB[2]*discB[2] + discB[3]*discB[3])
				);
		
		if(denominator == 0) return -3;
		
		return (numerator/denominator + 1.0)/2;
	}
	
	static String analyzeDominantOne(Integer[] disc)
	{
		if(disc.length != 4) return null;
		
		Map<String, Integer> discMap = new HashMap<String, Integer>();
		discMap.put("D", disc[0]);
		discMap.put("I", disc[1]);
		discMap.put("S", disc[2]);
		discMap.put("C", disc[3]);
		
		List<Map.Entry<String, Integer>> discInfo =
			    new ArrayList<Map.Entry<String, Integer>>(discMap.entrySet());
		
		//排序
		Collections.sort(discInfo, new Comparator<Map.Entry<String, Integer>>() {   
		    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {      
		       return (o2.getValue() - o1.getValue()); 
		    }
		}); 
		
		if( discInfo.get(0).getValue()>0 ) return discInfo.get(0).getKey();
		
		return null;
	}
	
	/**
	 * 计算合拍度，
	 * @param discA
	 * @param discB
	 * @return 传入数组长度不等于4时返回null；
	 */
	public static String analyzeHarmony(Integer[] discA, Integer[] discB)
	{
		if(discA.length != 4 || discB.length != 4) return null;
		String dominantA = analyzeDominantOne(discA);
		String dominantB = analyzeDominantOne(discB);
		
		if(dominantA==null || dominantB==null) return "OTHER";
		
		return dominantA + dominantB;
	}
}
