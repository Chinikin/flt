package com.depression.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.IndustryListMapper;
import com.depression.dao.MemberMapper;
import com.depression.dao.MemberPsychoMapper;
import com.depression.dao.MemberTempMapper;
import com.depression.dao.NicknameElementMapper;
import com.depression.dao.PsychoMottoLibraryMapper;
import com.depression.dao.UserSignatureLibraryMapper;
import com.depression.model.IndustryList;
import com.depression.model.Member;
import com.depression.model.MemberBasic;
import com.depression.model.MemberPsycho;
import com.depression.model.MemberTemp;
import com.depression.model.NicknameElement;
import com.depression.model.Page;
import com.depression.model.PsychoMottoLibrary;
import com.depression.model.UserSignatureLibrary;
import com.depression.utils.Configuration;

@Service
public class MemberService
{
    @Autowired
    MemberMapper memberMapper;
    @Autowired
    MemberTempMapper memberTempMapper;
    @Autowired
    MemberPsychoMapper memberPsychoMapper;
    @Autowired
    NicknameElementMapper nicknameElementMapper;
    @Autowired
    IndustryListMapper industryListMapper;
    @Autowired
    UserSignatureLibraryMapper userSignatureLibraryMapper;
    @Autowired
    PsychoMottoLibraryMapper psychoMottoLibraryMapper;

    public static byte USER_TYPE_USER = (byte) 1;
    public static byte USER_TYPE_PSYCHO = (byte) 2;

    public static byte P_LEVEL_PSYCHO = (byte) 0;
    public static byte P_LEVEL_LISTENER = (byte) 1;
    
    public static byte SORT_MODE_DEFAULT = 0; //默认，现在按回答提问数
    public static byte SORT_MODE_MOST_SERVICE = 1; //最多咨询
    public static byte SORT_MODE_MOST_APPRECIATED = 2; //最多感谢
    public static byte SORT_MODE_ONLINE_STATUS = 3; //在线状态， 顺序：在线、通话中、离线
    public static byte SORT_MODE_PRICE_ASC = 4; //价格从低到高
    public static byte SORT_MODE_PRICE_DESC = 5; //价格从高到低
    
    /*<<----------------------------------昵称生成Begin-------------------------------------*/
    //昵称元素在初始化时写入数据库，之后以数据库数据为准
    static String[] nickname_adj_init = {"薄荷味的", "柠檬味的", "酸甜味的", "芒果味的", "蓝莓味的", 
    	"西瓜味的","香蕉味的","草莓味的","苹果味的", "橙子味的" };
    static String[] nickname_noun_init = {"洋葱","蘑菇","土豆","红豆","芒果","蓝莓","草莓","香蕉","橙子",
    	"柠檬","番茄","石竹","春天","夏天","秋天","冬天","晴天","雨天","立春","丽霞","立秋","立冬","小满",
    	"夏至","半夏","白芍","玉竹","木香","白薇","细辛","苏叶","苏子","细辛","茜草","茵陈","白芷","半夏",
    	"竹茹","赤芍","芫花","苏木","杜仲","沉香","诃子","青黛","昆布","香附","桔梗"};
    static byte NICKNAME_ELEMENT_TYPE_ADJ = 0; //昵称元素类型 形容词
    static byte NICKNAME_ELEMENT_TYPE_NOUN = 1; //昵称元素类型 名称
    List<String> nicknameAdjs = null;
    List<String> nicknameNouns = null;
    
    /**
     * 昵称元素数据库表初始化
     */
    void initNicknameElementTable()
    {
    	int i = 1;
    	for(String adj : nickname_adj_init)
    	{
    		NicknameElement ne = new NicknameElement();
    		ne.setPhrase(adj);
    		ne.setType(NICKNAME_ELEMENT_TYPE_ADJ);
    		ne.setSortOrder(i++);
    		
    		nicknameElementMapper.insertSelective(ne);
    	}
    	
    	i = 1;
    	for(String noun : nickname_noun_init)
    	{
    		NicknameElement ne = new NicknameElement();
    		ne.setPhrase(noun);
    		ne.setType(NICKNAME_ELEMENT_TYPE_NOUN);
    		ne.setSortOrder(i++);
    		
    		nicknameElementMapper.insertSelective(ne);
    	}
    }
    
    /**
     * 加载昵称元素数据库表
     */
    void loadNicknameElementTable()
    {
    	{
	    	NicknameElement ne = new NicknameElement();
	    	ne.setType(NICKNAME_ELEMENT_TYPE_ADJ);
	    	ne.setIsEnable((byte)0);
	    	List<NicknameElement> nes = nicknameElementMapper.selectSelective(ne);
	    	if(nes.size() > 0)
	    	{
		    	nicknameAdjs = new ArrayList<String>();
		    	for(NicknameElement n : nes)
		    	{
		    		nicknameAdjs.add(n.getPhrase());
		    	}
	    	}
    	}
    	{
	    	NicknameElement ne = new NicknameElement();
	    	ne.setType(NICKNAME_ELEMENT_TYPE_NOUN);
	    	ne.setIsEnable((byte)0);
	    	List<NicknameElement> nes = nicknameElementMapper.selectSelective(ne);
	    	if(nes.size() > 0)
	    	{
		    	nicknameNouns = new ArrayList<String>();
		    	for(NicknameElement n : nes)
		    	{
		    		nicknameNouns.add(n.getPhrase());
		    	}
	    	}
    	}
    }
    
    /**
     * 生成昵称
     * @param num 根据num生成昵称
     * @return
     */
    public String genNickname(Integer num)
    {
    	if(nicknameAdjs == null)
    	{//初始化昵称元素变量
    		synchronized (MemberService.class)
			{
				if(nicknameAdjs == null)
				{
					//从数据库中加载数据
					loadNicknameElementTable();
					if(nicknameAdjs == null)
					{//如果数据库中没有数据，进行初始化
						initNicknameElementTable();
						//再次加载
						loadNicknameElementTable();
					}
				}
			}
    	}
    	
    	String nickname = nicknameAdjs.get(num%nicknameAdjs.size()) +
    			nicknameNouns.get(num%nicknameNouns.size());
    	
    	return nickname;
    }
    /*----------------------------------昵称生成End------------------------------------->>*/
    
    /*<<----------------------------------行业列表Begin-------------------------------------*/
    static String[] industry_list_init = {"计算机/互联网/通信", "生产/工艺/制造", "医疗/护理/制药","金融/银行/投资/保险",
    	"商业/服务业/个体经营", "文化/广告/传媒", "娱乐/艺术/表演", "律师/法务", "教育/培训", "公务员/行政/事业单位",
    	"学生", "其他职业"};
    
    List<String> industryList = null;
    
    /**
     * 行业列表数据库表初始化
     */
    void initIndustryListTable()
    {
    	int i = 1;
    	for(String indu : industry_list_init)
    	{
    		IndustryList il = new IndustryList();
    		il.setName(indu);
    		il.setSortOrder(i++);
    		
    		industryListMapper.insertSelective(il);
    	}
    }
    
    /**
     * 从数据库加载行业列表
     */
    void loadIndustryListTable()
    {
    	IndustryList il = new IndustryList();
    	il.setIsEnable((byte) 0);
    	
    	List<IndustryList> ils = industryListMapper.selectSelective(il);
    	if(ils.size() > 0)
    	{
    		industryList = new ArrayList<String>();
    		for(IndustryList indu : ils)
    		{
    			industryList.add(indu.getName());
    		}
    	}
    }
    
    /**
     * 获取行业列表
     * @return
     */
    public List<String> obtainIndustryList()
    {
    	if(industryList == null)
    	{//初始化行业列表
    		synchronized (MemberService.class)
			{
				if(industryList == null)
				{
					//从数据库中加载数据
					loadIndustryListTable();
					if(industryList == null)
					{//如果数据库中没有数据，进行初始化
						initIndustryListTable();
						//再次加载
						loadIndustryListTable();
					}
				}
			}
    	}
    	//复制避免返回值被修改
    	List<String> retList = new ArrayList<String>(Arrays.asList(new String[industryList.size()]));  
    	Collections.copy(retList, industryList);
    	return retList;
    }
    
    /*----------------------------------行业列表End------------------------------------->>*/
    
    /*<<----------------------------------用户签名Begin-------------------------------------*/
    static String[] user_signature_init = {"生活总把人逼到死角，可是我们依旧要努力的生存。",
    	"永不假设，永不强求，顺其自然。",
    	"记住，自己的路，自己走，别人只能影响你，但绝对不能决定你！",
    	"心若定，何须追逐；心若安，何须浮躁；心若净，何须飘摇。",
    	"我想我们都是如此的固执，才会用爱折磨着彼此。",
    	"想打电话听听你的声音，才发现，时间在走，你也在走。",
    	"我忍住了眼前没有你，却忍不住梦里没有你。",
    	"做女人可以性感，可以妖艳，可以清纯，唯独不可以平庸。",
    	"我只是喜欢打开音乐，听我喜欢的，然后把你变成回忆。",
    	"即使心痛也要努力的大笑。"};
    
    List<String> user_signature = null;
    /**
     * 初始化用户介绍库数据表
     */
    void initUserSignatureLibraryTable()
    {
    	int i = 1;
    	for(String statement : user_signature_init)
    	{
    		UserSignatureLibrary upl = new UserSignatureLibrary();
    		upl.setStatement(statement);
    		upl.setSortOrder(i++);
    		
    		userSignatureLibraryMapper.insertSelective(upl);
    	}
    }
    
    /**
     * 加载用户介绍库数据表
     */
    void loadUserSignatureLibraryTable()
    {
    	UserSignatureLibrary upl = new UserSignatureLibrary();
    	upl.setIsEnable((byte) 0);
    	
    	List<UserSignatureLibrary> upls = userSignatureLibraryMapper.selectSelective(upl);
    	if(upls.size() > 0)
    	{
    		user_signature = new ArrayList<String>();
    		for(UserSignatureLibrary u : upls)
    		{
    			user_signature.add(u.getStatement());
    		}
    	}
    }
    
    /**
     * 根据随机数生成用户介绍
     * @param num
     * @return
     */
    public String genUserSignature(Integer num)
    {
    	if(user_signature == null)
    	{//初始化用户介绍库
    		synchronized (MemberService.class)
			{
    			if(user_signature == null)
    			{//从数据库加载数据
    				loadUserSignatureLibraryTable();
    				if(user_signature == null)
    				{//数据库表空，初始化数据库表
    					initUserSignatureLibraryTable();
    					//再次加载
    					loadUserSignatureLibraryTable();
    				}
    			}
				
			}
    	}
    	
    	//复制避免返回值被修改
    	String retStr = new String(user_signature.get(num%user_signature.size()));
    	return retStr;
    }
    
    /*----------------------------------用户签名End------------------------------------->>*/
    
    /*<<----------------------------------咨询师铭言Begin-------------------------------------*/
    static String[] psycho_motto_init = {
    	"使人开心只是心理咨询的前奏曲，而使人成长才是心理咨询的主旋律。",
    	"宣泄不良情绪是任何形式咨询的首要任务，去认真地听别人讲话吧。干这行的人最大的奖励，就是别人说你是一个很好的听者。",
    	"授人以鱼，一日享用；教人以渔，终身受用。",
    	"当你努力安慰身边那些遭受伤害的人们时，请你不要简单地说“过去的事情就让它过去吧”之类的话，那 可能是当事人最不爱听的话。",
    	"人越是在比自己成熟或地位高的人面前获得尊重，就越容易消除个人的自卑感。",
    	"有选择地不说出全部的真话，不等于说假话。",
    	"心理咨询关系中需要有一种“距离美”。",
    	"尽力而为，是成是败已不重要。",
    	"心理咨询不仅可以帮助他人成长，也可以帮助自己成长；心理咨询使人更加相信自我，而非更加迷信别人； 心理咨询使人学会多听少言，而非少听多言。",
    	"心理咨询之可贵，就在于它可以推动来访者去积极地认识自我，反省自我，进而提高其自信心与生活的智慧。",
    	"心理咨询之难为，就在于这种来访者对自我的深刻反省与认识应该是自发而成的，而不是由咨询说教而致的。",
    	"心理咨询之巧妙，就在于咨询者不断启发来访者说出自己想让他说出的话。",
    	"心理咨询之高明，就在于来访者不但能独立克服当前面临的困难，也能从中增长人生的智慧。"
    };
    List<String> psycho_motto = null;
    
    /**
     * 初始化咨询师铭言数据库表
     */
    void initPsychoMottoLibrary()
    {
    	int i = 1;
    	for(String statement : psycho_motto_init)
    	{
    		PsychoMottoLibrary pml = new PsychoMottoLibrary();
    		pml.setStatement(statement);;
    		pml.setSortOrder(i++);
    		
    		psychoMottoLibraryMapper.insertSelective(pml);
    	}
    }
    
    /**
     * 从数据库表中加载咨询师铭言
     */
    void loadPsychoMottoLibrary()
    {
    	PsychoMottoLibrary pml = new PsychoMottoLibrary();
    	pml.setIsEnable((byte) 0);
    	List<PsychoMottoLibrary> pmls = psychoMottoLibraryMapper.selectSelective(pml);
    	if(pmls.size() > 0)
    	{
    		psycho_motto = new ArrayList<String>();
    		for(PsychoMottoLibrary p : pmls)
    		{
    			psycho_motto.add(p.getStatement());
    		}
    	}
    }
    
    /**
     * 根据随机数生成咨询师铭言
     * @param num
     * @return
     */
    public String genPyschoMotto(Integer num)
    {
    	if(psycho_motto == null)
    	{//初始化用户介绍库
    		synchronized (MemberService.class)
			{
    			if(psycho_motto == null)
    			{//从数据库加载数据
    				loadPsychoMottoLibrary();
    				if(psycho_motto == null)
    				{//数据库表空，初始化数据库表
    					initPsychoMottoLibrary();
    					//再次加载
    					loadPsychoMottoLibrary();
    				}
    			}
				
			}
    	}
    	
    	//复制避免返回值被修改
    	String retStr = new String(psycho_motto.get(num%psycho_motto.size()));
    	return retStr;
    }
    /*----------------------------------咨询师铭言End------------------------------------->>*/
    
    public Long insert(Member member)
    {
        Long curMid = 0L;
        member.setRegTime(new Date());
        member.setModifyTime(new Date());
        memberMapper.insertSelective(member);
        curMid = member.getMid();
        return curMid;
    }

    /**
     * 不能完全解决并发重复问题，依赖数据字段的unique属性
     *
     * @param mobilePhone
     * @return
     */
    public Integer insertPreemption(String mobilePhone)
    {
        Member m = new Member();
        m.setMobilePhone(mobilePhone);
        Member member = getMember(m);
        if (member != null)
        {
            return 0;
        } else
        {
            insert(m);
            return 1;
        }
    }

    public Member getMember(Member member)
    {
        if (member.getMid() != null)
        {
            return memberMapper.selectByPrimaryKey(member.getMid());
        }

        List<Member> ms = memberMapper.selectSelective(member);
        if (ms.size() == 1)
        {
            return ms.get(0);
        } else
        {
            return null;
        }
    }

    public boolean checkMid(Long mid)
    {
        Member m = new Member();
        m.setMid(mid);
        return memberMapper.selectByPrimaryKey(mid) != null;
    }
    
    public MemberTemp newMemberTemp()
    {
    	MemberTemp mt = new MemberTemp();
    	mt.setCreateTime(new Date());
    	
    	memberTempMapper.insertSelective(mt);
    	
    	return mt;
    }

    public Integer deleteMemberBulk(List<Long> ids)
    {
        if (ids.size() > 0)
        {
            return memberMapper.deleteByPrimaryKeyBulk(ids);
        } else
        {
            return 0;
        }
    }

    public Integer enableMemberBulk(List<Long> ids)
    {
        if (ids.size() > 0)
        {
            return memberMapper.enableByPrimaryKeyBulk(ids, (byte) 0);
        } else
        {
            return 0;
        }
    }

    public Integer disableMemberBulk(List<Long> ids)
    {
        if (ids.size() > 0)
        {
            return memberMapper.enableByPrimaryKeyBulk(ids, (byte) 1);
        } else
        {
            return 0;
        }
    }

    public Integer deleteMember(Long id)
    {
        return memberMapper.deleteByPrimaryKey(id);
    }
    
    public Integer deleteMemberById(Long id)
    {
        return memberMapper.deleteByMid(id);
    }
    
    public Integer update(Member member)
    {
        member.setModifyTime(new Date());
        return memberMapper.updateByPrimaryKeySelective(member);
    }

    public List<Member> selectByPage(Member member)
    {
        return memberMapper.selectSelectiveWithPage(member);
    }

    public Integer selectCount()
    {
        Member m = new Member();
        return memberMapper.countSelective(m);
    }

    public List<Member> selectByMember(Member member)
    {
        return memberMapper.selectSelective(member);
    }

    public Integer selectByMemberCount(Member member)
    {
        return memberMapper.countSelective(member);
    }

    public void fillMemberBasic(Member member, MemberBasic memberBasic)
    {

    	if (member != null)
    	{
    		memberBasic.setMid(member.getMid());
            memberBasic.setAvatar(member.getAvatar());
            memberBasic.setImAccount(member.getImAccount());
            memberBasic.setNickname(member.getNickname());
            memberBasic.setUserType(member.getUserType());
    	}
    }

    public void fillMemberBasic(Long mid, MemberBasic memberBasic)
    {
        Member member = memberMapper.selectByPrimaryKey(mid);
        fillMemberBasic(member, memberBasic);
    }

    public List<MemberBasic> getMembersByImAccounts(List<String> imAccounts)
    {
        List<Member> ml = memberMapper.selectByImAccounts(imAccounts);
        List<MemberBasic> mbl = new ArrayList<MemberBasic>();
        for (Member m : ml)
        {
            MemberBasic memberBasic = new MemberBasic();
            fillMemberBasic(m, memberBasic);
            mbl.add(memberBasic);
        }
        return mbl;
    }

    /**
     * 根据手机号查询该手机号是否已经注册过
     *
     * @param member
     * @return
     */
    public Member selectMemberByMobilePhone(String mobilePhone)
    {
        Member m = new Member();
        m.setMobilePhone(mobilePhone);

        List<Member> ms = memberMapper.selectSelective(m);
        if (ms.size() == 1)
        {
            return ms.get(0);
        } else
        {
            return null;
        }
    }

    public Member selectMemberByMid(Long mid)
    {
        return memberMapper.selectByPrimaryKey(mid);
    }
    
    public String obtainMobilePhoneByMid(Long mid)
    {
    	Member m = memberMapper.selectByPrimaryKey(mid);
    	if(m != null)
    	{
    		return m.getMobilePhone();
    	}
    	return null;
    }

    /**
     * 更新并更改类型为咨询师
     * @param member
     * @return
     */
    public Integer updateToPsycho(Member member)
    {
    	//添加咨询师扩展记录
    	MemberPsycho mp = new MemberPsycho();
    	mp.setMid(member.getMid());
    	mp.setCreateTime(new Date());
    	memberPsychoMapper.insertSelective(mp);
    	
        member.setUserType(USER_TYPE_PSYCHO);
        return update(member);
    }
    
    /**
     * 更新并更改类型普通用户
     * @param member
     * @return
     */
    public Integer updateToUser(Member member)
    {
    	//删除咨询师扩展记录
    	MemberPsycho mp = new MemberPsycho();
    	mp.setMid(member.getMid());
    	List<MemberPsycho> mps = memberPsychoMapper.selectSelective(mp);
    	if(mps.size() > 0)
    	{
    		memberPsychoMapper.deleteByPrimaryKey(mps.get(0).getMpId());
    	}
    	
        member.setUserType(USER_TYPE_USER);
        return update(member);
    }

    public List<Member> getPsychosOrderAnswerCountWithPage(Integer pageIndex, Integer pageSize)
    {
        Member m = new Member();
        m.setUserType(USER_TYPE_PSYCHO);
        m.setPageSize(pageSize);
        m.setPageIndex(pageIndex);

        return memberMapper.selectByTypeOrderAnswerCountWithPage(m);
    }

    public List<Member> getUsersOrderAnswerCountWithPage(Integer pageIndex, Integer pageSize)
    {
        Member m = new Member();
        m.setUserType(USER_TYPE_USER);
        m.setPageSize(pageSize);
        m.setPageIndex(pageIndex);

        return memberMapper.selectByTypeOrderAnswerCountWithPage(m);
    }

    public List<Member> getPsychosOrderAnswerCountWithPageEnabled(Integer pageIndex, Integer pageSize, Byte pLevel)
    {
        Member m = new Member();
        m.setUserType(USER_TYPE_PSYCHO);
        m.setpLevel(pLevel);
        m.setPageSize(pageSize);
        m.setPageIndex(pageIndex);

        return memberMapper.selectByTypeOrderAnswerCountWithPageEnabled(m);
    }
    
    public List<Member> getPsychosSortableWithPageEnabled(Integer pageIndex, Integer pageSize, Byte pLevel, Byte sortMode)
    {
        Page page = new Page();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);

        return memberMapper.selectByTypeSortableWithPageEnabled(USER_TYPE_PSYCHO, page.getPageStartNum(), 
        		pageSize, pLevel, sortMode);
    }

    public List<Member> getPsychosOrderRegTimeWithPageDesc(Integer pageIndex, Integer pageSize)
    {
        Member m = new Member();
        m.setUserType(USER_TYPE_PSYCHO);
        m.setPageSize(pageSize);
        m.setPageIndex(pageIndex);

        return memberMapper.selectByTypeOrderRegTimeWithPageDesc(m);
    }

    public List<Member> getPsychosOrderRegTimeWithPageAsc(Integer pageIndex, Integer pageSize)
    {
        Member m = new Member();
        m.setUserType(USER_TYPE_PSYCHO);
        m.setPageSize(pageSize);
        m.setPageIndex(pageIndex);

        return memberMapper.selectByTypeOrderRegTimeWithPageAsc(m);
    }

    public Integer getPsychosCount()
    {
        return memberMapper.countByTypeOrderAnswerCount(USER_TYPE_PSYCHO);
    }

    public Integer getPsychosCountEnabled(Byte pLevel)
    {
        return memberMapper.countByTypeOrderAnswerCountEnabled(USER_TYPE_PSYCHO, pLevel);
    }

    public List<Member> getUsersOrderRegTimeWithPageDesc(Integer pageIndex, Integer pageSize)
    {
        Member m = new Member();
        m.setUserType(USER_TYPE_USER);
        m.setIsTemp((byte) 0);
        m.setPageSize(pageSize);
        m.setPageIndex(pageIndex);

        return memberMapper.selectByTypeOrderRegTimeWithPageDesc(m);
    }

    public List<Member> getUsersOrderRegTimeWithPageAsc(Integer pageIndex, Integer pageSize)
    {
        Member m = new Member();
        m.setUserType(USER_TYPE_USER);
        m.setIsTemp((byte) 0);
        m.setPageSize(pageSize);
        m.setPageIndex(pageIndex);

        return memberMapper.selectByTypeOrderRegTimeWithPageAsc(m);
    }

    public Integer getUserCount()
    {
        Member m = new Member();
        m.setUserType(USER_TYPE_USER);
        m.setIsTemp((byte) 0);
        return memberMapper.countSelective(m);
    }

    public List<Member> getByKeysOrderAnswerCountWithPage(List<Long> ids, Integer pageIndex,
                                                          Integer pageSize)
    {
        Page page = new Page();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);
        if (ids.size() == 0)
        {
            return new ArrayList<Member>();
        }
        return memberMapper.selectByPrimaryKeysOrderAnswerCountWithPage(ids, page.getPageStartNum(), pageSize);
    }

    public List<Member> getByKeysOrderAnswerCountWithPageEnabled(List<Long> ids, Integer pageIndex,
                                                                 Integer pageSize, Byte pLevel)
    {
        Page page = new Page();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);
        if (ids.size() == 0)
        {
            return new ArrayList<Member>();
        }
        return memberMapper.selectByPrimaryKeysOrderAnswerCountWithPageEnabled(ids, page.getPageStartNum(), pageSize, pLevel);
    }
    
    public List<Member> getByKeysSortableWithPageEnabled(List<Long> ids, Integer pageIndex,
            Integer pageSize, Byte pLevel, Byte sortMode)
	{
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		if (ids.size() == 0)
		{
			return new ArrayList<Member>();
		}
		return memberMapper.selectByPrimaryKeysSortableWithPageEnabled(ids, page.getPageStartNum(), pageSize, pLevel, sortMode);
	}

    public List<Member> getByKeysWithPageEnabled(List<Long> ids, Integer pageIndex,
                                                 Integer pageSize, Byte pLevel)
    {
        Page page = new Page();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);
        if (ids.size() == 0)
        {
            return new ArrayList<Member>();
        }
        return memberMapper.selectByPrimaryKeysWithPageEnabled(ids, page.getPageStartNum(), pageSize, pLevel);
    }

    public Integer getCountByPrimaryKeysEnabled(List<Long> ids, Byte pLevel)
    {
        if (ids.size() == 0)
        {
            return 0;
        }
        return memberMapper.countByPrimaryKeysEnabled(ids, pLevel);
    }

    /**
     * 增加感谢次数
     * @param mid
     */
    public void transIncrAppreciatedCount(Long mid)
    {
        Member m = memberMapper.selectByPrimaryKey(mid);
        Member member = new Member();
        member.setMid(mid);
        member.setAppreciatedCount(m.getAppreciatedCount() + 1);
        memberMapper.updateByPrimaryKeySelective(member);
    }
 
    /**
     * 增加回答次数
     * @param mid
     */
    public void transIncrAnswerCount(Long mid)
    {
        Member m = memberMapper.selectByPrimaryKey(mid);
        Member member = new Member();
        member.setMid(mid);
        member.setAnswerCount(m.getAnswerCount() + 1);
        memberMapper.updateByPrimaryKeySelective(member);
    }
    
    public List<Member> searchPsychos(String words, Integer pageIndex,
                                      Integer pageSize, Integer regTimeDirection,
                                      Date begin, Date end, Byte isAudited)
    {
        Page page = new Page();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);
        return memberMapper.searchMember(words, USER_TYPE_PSYCHO, page.getPageStartNum(), pageSize,
                regTimeDirection, begin, end, null, isAudited);
    }

    public Integer countSearchPsychos(String words, Date begin, Date end, Byte isAudited)
    {
        return memberMapper.countSearchMember(words, USER_TYPE_PSYCHO, begin, end, null, isAudited);
    }

    public List<Member> searchUsers(String words, Integer pageIndex,
                                    Integer pageSize, Integer regTimeDirection,
                                    Date begin, Date end,Byte hasMobile)
    {
        Page page = new Page();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);
        return memberMapper.searchMember(words, USER_TYPE_USER, page.getPageStartNum(), pageSize,
                regTimeDirection, begin, end, hasMobile, null);
    }

    public Integer countSearchUsers(String words, Date begin, Date end, Byte hasMobile)
    {
        return memberMapper.countSearchMember(words, USER_TYPE_USER, begin, end, hasMobile, null);
    }

    public List<Member> selectUsersByTimeSliceWithPage(
            Integer pageIndex, Integer pageSize,
            Date begin, Date end)
    {
        Page page = new Page();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);
        return memberMapper.selectByTimeSliceWithPage(USER_TYPE_USER,
                page.getPageStartNum(), pageSize, begin, end);
    }

    public Integer countUsersByTimeSlice(Date begin, Date end)
    {
        return memberMapper.countByTimeSlice(USER_TYPE_USER, begin, end);
    }

    /**
     * 获取指定注册时间片内的咨询师列表
     *
     * @param pageIndex 页码
     * @param pageSize  页大小
     * @param begin     开始时间
     * @param end       结束时间
     * @return
     */
    public List<Member> selectPsychosByTimeSliceWithPage(
            Integer pageIndex, Integer pageSize,
            Date begin, Date end)
    {
        Page page = new Page();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);

        return memberMapper.selectByTimeSliceWithPage(USER_TYPE_PSYCHO,
                page.getPageStartNum(), pageSize, begin, end);
    }

    /**
     * 获取制定注册时间片内的咨询师数量
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return
     */
    public Integer countPsychosByTimeSlice(Date begin, Date end)
    {
        return memberMapper.countByTimeSlice(USER_TYPE_PSYCHO, begin, end);
    }

    public boolean isPsychos(Long mid)
    {
        Member member = memberMapper.selectByPrimaryKey(mid);
        return member.getUserType() == USER_TYPE_PSYCHO;
    }
    
    public List<Member> searchPsychoByName(String name)
    {
    	return memberMapper.searchPsychoByName(name, USER_TYPE_PSYCHO);
	}
    
    /**
     * 通过咨询师扩展表高效获取咨询师id
     * @return
     */
    public List<Long> selectPyschoIds()
    {
    	return memberPsychoMapper.selectPsychoIds();
    }
    
    /**
     * 根据心猫审核状态获取咨询师id
     * @param isAudited
     * @return
     */
    public List<Long> selectPsychoIds8Audited(Byte isAudited)
    {
    	return memberPsychoMapper.selectPsychoIds8Audited(isAudited);
    }
    
    /**
     * 查询咨询师入住时间
     * @param mid
     * @return
     */
    public Date selectPsychoRegTime(Long mid)
    {
    	MemberPsycho mp = new MemberPsycho();
    	mp.setMid(mid);
    	
    	List<MemberPsycho> mps = memberPsychoMapper.selectSelective(mp);
    	if(mps.size() == 0)
    	{
    		return null;
    	}
    	
    	return mps.get(0).getCreateTime();
    }

    /**
     * 加锁的checkAndSet，设置咨询师状态
     * @param mid
     * @param expectedStatus 期望状态
     * @param newStatus 新状态
     * @return 返回旧状态
     */
    public Byte transCasStatus(Long mid, Byte expectedStatus, Byte newStatus)
    {
    	Member member = memberMapper.selectByPrimaryKeyLock(mid);
    	if(member.getStatus().equals(expectedStatus))
    	{
    		member.setStatus(newStatus);
    		memberMapper.updateByPrimaryKeySelective(member);
    		
    		return expectedStatus;
    	}
    	
    	return member.getStatus();
    }
    
    /**
     * 加锁的checkAndSet，设置咨询师状态
     * @param mobile
     * @param expectedStatus 期望状态
     * @param newStatus 新状态
     * @return 返回旧状态
     */
    public Byte transStatus(String mobile,Byte newStatus)
    {
    	Member member = memberMapper.selectByMobileLock(mobile);
    	
    		member.setStatus(newStatus);
    		memberMapper.updateByPrimaryKeySelective(member);
    	
    	return newStatus;
    }
    
    /**
     * 手機號或者id查詢
     * @param mid
     * @param mobilePhone
     * @return
     */
    public Member selectByMidOrMobile(Member member){
    	return memberMapper.selectByMidOrMobile(member);
    }
    
    
	/**
	 * 搜索咨询师
	 * @param pageIndex
	 * @param pageSize
	 * @param pLevel 0咨询师  1倾听师
	 * @param sortMode 1默认 2在线 3最长从业年限 4价格从高到低 5价格从低到高 6最多经验 7最高效率
	 * @param city 城市
	 * @param tagIds 标签id
	 * @param degreeIds 1博士 2硕士 3本科及以下
	 * @param priceFloor 价格下限
	 * @param priceCeil 价格上限
	 * @return
	 */
    public List<Member> searchPsychoV1(Integer pageIndex, Integer pageSize, 
			Byte pLevel, 
			Byte sortMode, 
			String city, List<Long> tagIds,
			List<Long> licenseIds,List<Long> degreeIds,
			Integer priceFloor,Integer priceCeil,
			Byte sex,
			List<Long> pIds)
	{
    	Page page = new Page();
    	page.setPageIndex(pageIndex);
    	page.setPageSize(pageSize);
    	//列表长度为0，置成null，mybatis 空列表会语法错误
    	if(tagIds!=null && tagIds.size() == 0) tagIds = null;
    	if(licenseIds!=null && licenseIds.size() == 0) licenseIds = null;
    	if(degreeIds!=null && degreeIds.size() == 0) degreeIds = null;
    	
    	if(pIds != null && pIds.size() == 0) 
    	{
    		return new ArrayList<Member>();
    	}
    	
    	return memberMapper.searchPsychoV1(page.getPageStartNum(), pageSize, 
    			pLevel, 
    			sortMode, 
    			city, tagIds, 
    			licenseIds, degreeIds, 
    			priceFloor, priceCeil,
    			sex,
    			pIds);
	}
    
    public Integer countSearchPsychoV1(Byte pLevel, 
			String city, List<Long> tagIds,
			List<Long> licenseIds,List<Long> degreeIds,
			Integer priceFloor,Integer priceCeil,
			Byte sex,
			List<Long> pIds)
    {
    	//列表长度为0，置成null，mybatis 空列表会语法错误
    	if(tagIds!=null && tagIds.size() == 0) tagIds = null;
    	if(licenseIds!=null && licenseIds.size() == 0) licenseIds = null;
    	if(degreeIds!=null && degreeIds.size() == 0) degreeIds = null;
    	
    	if(pIds != null && pIds.size() == 0) 
    	{
    		return 0;
    	}
    	
    	return memberMapper.countSearchPsychoV1(pLevel, 
    											city, tagIds, 
    											licenseIds, degreeIds, 
    											priceFloor, priceCeil,
    											sex,
    											pIds);
    }
}
