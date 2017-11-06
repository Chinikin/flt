package com.depression.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.chenlb.mmseg4j.analysis.MaxWordAnalyzer;
import com.depression.dao.ArticleDetailMapper;
import com.depression.dao.ArticleMapper;
import com.depression.dao.LicenseTypeMapper;
import com.depression.dao.MemberAdvisoryDetailMapper;
import com.depression.dao.MemberAdvisoryMapper;
import com.depression.dao.MemberMapper;
import com.depression.dao.TestingMapper;
import com.depression.dao.TestingTypeDAO;
import com.depression.model.AdvisoryTag;
import com.depression.model.Article;
import com.depression.model.ArticleDetail;
import com.depression.model.LuceneIdFlag;
import com.depression.model.Member;
import com.depression.model.MemberAdvisory;
import com.depression.model.MemberAdvisoryDetail;
import com.depression.model.MemberTag;
import com.depression.model.Testing;
import com.depression.model.TestingType;
import com.depression.utils.Configuration;

@Service
public class LuceneService 
{
    Logger log = Logger.getLogger(this.getClass());
    
	@Autowired
	MemberMapper memberMapper;
	@Autowired
	LicenseTypeMapper licenseTypeMapper;
	@Autowired
	ArticleMapper articleMapper;
	@Autowired
	ArticleDetailMapper articleDetailMapper;
	@Autowired
	TestingMapper testingMapper;
	@Autowired
	TestingTypeDAO testingTypeDAO;
	@Autowired
	MemberAdvisoryMapper memberAdvisoryMapper;
	@Autowired	
	MemberAdvisoryDetailMapper memberAdvisoryDetailMapper;
	@Autowired
	MemberTagService memberTagService;
	@Autowired
	AdvisoryTagService advisoryTagService;
	
	//索引更新时间相关参数
	private String propertiesFile = Configuration.LUCENE_PATH + "/prop.properties";
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Date lastIndexTime = null;
	private Date indexTime = null;
	
	//初始化控制
	private int initStatus = 0;
	
	/**
	 * 从属性文件中获取最后更新时间
	 */
	private void loadLastIndexTime()
	{
		Properties prop = new Properties();//属性集合对象      
		FileInputStream fis;
		try
		{
			fis = new FileInputStream(propertiesFile);
		} catch (FileNotFoundException e)
		{//有没索引记录
			return;
		}
		try
		{
			prop.load(fis);
		} catch (IOException e)
		{//属性读取失败
			File f = new File(propertiesFile);
			if(f.exists()) f.delete();
		}
		String timeStr = prop.getProperty("lastIndexTime");
		if(timeStr!=null)
		{
			try
			{
				lastIndexTime =  sdf.parse(timeStr);
			} catch (ParseException e)
			{//不做处理
			}
		}
	}
	
	/**
	 * 保存最后更新时间到属性文件
	 */
	private void storeLastIndexTime()
	{
		if(lastIndexTime==null) return;
		
		Properties prop = new Properties();//属性集合对象   
		prop.setProperty("lastIndexTime", sdf.format(lastIndexTime));
		FileOutputStream fos;
		try
		{
			fos = new FileOutputStream(propertiesFile);
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		try
		{
			prop.store(fos, "Depression Lucene");
			fos.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新咨询师索引
	 */
	private void updatePsychosIndex()
	{
        // Store the index in memory:
        // Directory directory = new RAMDirectory();
        // To store an index on disk, use this instead:
        Directory directory;
		try
		{
			directory = FSDirectory.open(Paths.get(Configuration.LUCENE_PATH + "/index/psychos"));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

        Analyzer analyzer = new MaxWordAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter iwriter = null;
        try {  
	        iwriter = new IndexWriter(directory, config);
	        List<Member> psychos = memberMapper.selectForLucene((byte)2, lastIndexTime, indexTime);
	        
	        for(Member p : psychos)
	        {
	       		if(lastIndexTime!=null)
	  	        {//删除过时的index
	       			iwriter.deleteDocuments(new Term("mid", p.getMid().toString()));
	  	        }
	        	
	        	if(p.getIsEnable()==1||p.getIsDelete()==1)
	        	{//删除或者禁用的数据

	        	}
	        	else
	        	{//新增索引
		        	Document doc = new Document();
			        doc.add(new Field("mid", p.getMid().toString(), TextField.TYPE_STORED));
			        doc.add(new Field("nickname", p.getNickname(), TextField.TYPE_STORED));
			        doc.add(new Field("title", p.getTitle(), TextField.TYPE_STORED));
			        String tags = "";
			        for(MemberTag mt : memberTagService.getTagList(p.getMid()))
					{
			        	tags += mt.getPhrase();
					}
			        doc.add(new Field("tags", tags, TextField.TYPE_NOT_STORED));
			        if(p.getLtid()!=null)
			        {
			        	String licenseName =
			        		licenseTypeMapper.selectByPrimaryKey(p.getLtid()).getLicenseName();
				        doc.add(new Field("licenseName", licenseName, TextField.TYPE_STORED));
			        }
			        doc.add(new Field("profile", p.getProfile(), TextField.TYPE_NOT_STORED));
			        iwriter.addDocument(doc);
	        	}

	        }
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (iwriter != null) {  
                	iwriter.close();  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  

        }
        
        /**
         * 打印索引信息
         */
        DirectoryReader directoryReader = null;  
        try {  
        	directoryReader = DirectoryReader.open(directory);  
            // 通过reader可以有效的获取到文档的数量  
            // 有效的索引文档  
            log.info("Valid psychos index:" + directoryReader.numDocs());  
            // 总共的索引文档  
            log.info("Total psychos index:" + directoryReader.maxDoc());  
            // 删掉的索引文档，其实不恰当，应该是在回收站里的索引文档  
            log.info("Deleted psychos index:" + directoryReader.numDeletedDocs());  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (directoryReader != null) {  
                	directoryReader.close();  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
	}
	
	
	
	/**
	 * 更新文章索引
	 */
	private void updateArticlesIndex()
	{
	    // Store the index in memory:
	    // Directory directory = new RAMDirectory();
	    // To store an index on disk, use this instead:
	    Directory directory;
		try
		{
			directory = FSDirectory.open(Paths.get(Configuration.LUCENE_PATH + "/index/articles"));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	
	    Analyzer analyzer = new MaxWordAnalyzer();
	    IndexWriterConfig config = new IndexWriterConfig(analyzer);
	    IndexWriter iwriter = null;
	    try {  
	        iwriter = new IndexWriter(directory, config);
	        List<Article> articles = articleMapper.selectForLucene(lastIndexTime, indexTime);
	        
	        for(Article a : articles)
	        {
	       		if(lastIndexTime!=null)
	  	        {//删除过时的index
	       			iwriter.deleteDocuments(new Term("article_id", a.getArticleId().toString()));
	  	        }
	        	
	        	if(a.getIsEnable()==1||a.getIsDelete()==1)
	        	{//删除或者禁用的数据
	
	        	}
	        	else
	        	{//新增索引
		        	Document doc = new Document();
			        doc.add(new Field("article_id", a.getArticleId().toString(), TextField.TYPE_STORED));
			        doc.add(new Field("title", a.getTitle(), TextField.TYPE_STORED));
			        doc.add(new Field("digest", a.getDigest(), TextField.TYPE_STORED));
			        ArticleDetail ad = new ArticleDetail();
			        ad.setArticleId(a.getArticleId());
			        List<ArticleDetail> ads = articleDetailMapper.selectSelective(ad);
			        if(ads.size() > 0)
			        {
			        	doc.add(new Field("detail", ads.get(0).getContent(), TextField.TYPE_STORED));
			        }
			        iwriter.addDocument(doc);
	        	}
	
	        }
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } finally {  
	        try {  
	            if (iwriter != null) {  
	            	iwriter.close();  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	
	    }
	    
	    /**
	     * 打印索引信息
	     */
	    DirectoryReader directoryReader = null;  
	    try {  
	    	directoryReader = DirectoryReader.open(directory);  
	        // 通过reader可以有效的获取到文档的数量  
	        // 有效的索引文档  
	        log.info("Valid articles index:" + directoryReader.numDocs());  
	        // 总共的索引文档  
	        log.info("Total articles index:" + directoryReader.maxDoc());  
	        // 删掉的索引文档，其实不恰当，应该是在回收站里的索引文档  
	        log.info("Deleted articles index:" + directoryReader.numDeletedDocs());  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } finally {  
	        try {  
	            if (directoryReader != null) {  
	            	directoryReader.close();  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }  
	}

	/**
	 * 更新文章索引
	 */
	private void updateTestingsIndex()
	{
	    // Store the index in memory:
	    // Directory directory = new RAMDirectory();
	    // To store an index on disk, use this instead:
	    Directory directory;
		try
		{
			directory = FSDirectory.open(Paths.get(Configuration.LUCENE_PATH + "/index/testings"));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	
	    Analyzer analyzer = new MaxWordAnalyzer();
	    IndexWriterConfig config = new IndexWriterConfig(analyzer);
	    IndexWriter iwriter = null;
	    try {  
	        iwriter = new IndexWriter(directory, config);
	        //删除所有索引
	        iwriter.deleteAll();
	        List<Testing> testings = testingMapper.selectForLucene();
	        
	        for(Testing t : testings)
	        {
	        	if(t.getIsDelete().equals("1"))
	        	{//删除或者禁用的数据
	
	        	}
	        	else
	        	{//新增索引
		        	Document doc = new Document();
			        doc.add(new Field("testing_id", t.getTestingId().toString(), TextField.TYPE_STORED));
			        doc.add(new Field("title", t.getTitle(), TextField.TYPE_STORED));
			        doc.add(new Field("contentExplain", t.getContentExplain(), TextField.TYPE_STORED));
			        
			        TestingType tt = testingTypeDAO.getTestingTypeByTypeId(String.valueOf(t.getTypeId()));
			        if(tt != null)
			        {
			        	doc.add(new Field("testing_type", tt.getTestingName(), TextField.TYPE_STORED));
			        }
			        iwriter.addDocument(doc);
	        	}
	
	        }
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } finally {  
	        try {  
	            if (iwriter != null) {  
	            	iwriter.close();  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	
	    }
	    
	    /**
	     * 打印索引信息
	     */
	    DirectoryReader directoryReader = null;  
	    try {  
	    	directoryReader = DirectoryReader.open(directory);  
	        // 通过reader可以有效的获取到文档的数量  
	        // 有效的索引文档  
	        log.info("Valid testing index:" + directoryReader.numDocs());  
	        // 总共的索引文档  
	        log.info("Total testing index:" + directoryReader.maxDoc());  
	        // 删掉的索引文档，其实不恰当，应该是在回收站里的索引文档  
	        log.info("Deleted testing index:" + directoryReader.numDeletedDocs());  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } finally {  
	        try {  
	            if (directoryReader != null) {  
	            	directoryReader.close();  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }  
	}

    /**
	 * 更新文章索引
	 */
	private void updateAdvisoriesIndex()
	{
	    // Store the index in memory:
	    // Directory directory = new RAMDirectory();
	    // To store an index on disk, use this instead:
	    Directory directory;
		try
		{
			directory = FSDirectory.open(Paths.get(Configuration.LUCENE_PATH + "/index/advisories"));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	
	    Analyzer analyzer = new MaxWordAnalyzer();
	    IndexWriterConfig config = new IndexWriterConfig(analyzer);
	    IndexWriter iwriter = null;
	    try {  
	        iwriter = new IndexWriter(directory, config);
	        List<MemberAdvisory> mas = memberAdvisoryMapper.selectForLucene(lastIndexTime, indexTime);
	        
	        for(MemberAdvisory a : mas)
	        {
	       		if(lastIndexTime!=null)
	  	        {//删除过时的index
	       			iwriter.deleteDocuments(new Term("advisory_id", a.getAdvisoryId().toString()));
	  	        }
	        	
	        	if(a.getIsDelete()==1)
	        	{//删除或者禁用的数据
	
	        	}
	        	else
	        	{//新增索引
		        	Document doc = new Document();
			        doc.add(new Field("advisory_id", a.getAdvisoryId().toString(), TextField.TYPE_STORED));
			        doc.add(new Field("content", a.getContent(), TextField.TYPE_STORED));
			        MemberAdvisoryDetail mad = new MemberAdvisoryDetail();
			        mad.setAdvisoryId(a.getAdvisoryId());
			        List<MemberAdvisoryDetail> mads = memberAdvisoryDetailMapper.selectSelective(mad);
			        doc.add(new Field("detail", mads.get(0).getDetail(), TextField.TYPE_STORED));
			        
			        List<AdvisoryTag> ats = advisoryTagService.selectAdvisoryTagByAdvisoryId(a.getAdvisoryId());
			        String tags = "";
			        for(AdvisoryTag at : ats)
			        {
			        	tags += at.getPhrase();
			        }
			        doc.add(new Field("tags", tags, TextField.TYPE_NOT_STORED));
			        
			        iwriter.addDocument(doc);
	        	}
	
	        }
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } finally {  
	        try {  
	            if (iwriter != null) {  
	            	iwriter.close();  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	
	    }
	    
	    /**
	     * 打印索引信息
	     */
	    DirectoryReader directoryReader = null;  
	    try {  
	    	directoryReader = DirectoryReader.open(directory);  
	        // 通过reader可以有效的获取到文档的数量  
	        // 有效的索引文档  
	        log.info("Valid advisories index:" + directoryReader.numDocs());  
	        // 总共的索引文档  
	        log.info("Total advisories index:" + directoryReader.maxDoc());  
	        // 删掉的索引文档，其实不恰当，应该是在回收站里的索引文档  
	        log.info("Deleted advisories index:" + directoryReader.numDeletedDocs());  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } finally {  
	        try {  
	            if (directoryReader != null) {  
	            	directoryReader.close();  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }  
	}

	//@Scheduled(fixedDelay=86400000)	
	public void initUpdateIndex()
	{
		if(initStatus==0)
		{
	    	log.info("Init lucense Index");
	    	if(lastIndexTime==null) loadLastIndexTime();
	    	log.info("lastIndexTime : " + (lastIndexTime==null?"No Record":sdf.format(lastIndexTime)));
	    	indexTime = new Date();
	        /******************************索引>>开始*****************************/
	    	updatePsychosIndex();
	    	updateArticlesIndex();
	    	updateAdvisoriesIndex();
	    	updateTestingsIndex();
	        /******************************索引<<结束*****************************/       
	        //记录更新时间
	        lastIndexTime = indexTime;
	        storeLastIndexTime();
	        
	        initStatus = 1;
		}
	}
	
	/**
	 * 定时更新索引
	 */
   // @Scheduled(cron="0/30 * * * * ? ")	
	public void freqUpdateIndex()
	{
    	log.info("Update lucense Index");
    	if(lastIndexTime==null) loadLastIndexTime();
    	log.info("lastIndexTime : " + (lastIndexTime==null?"No Record":sdf.format(lastIndexTime)));
    	indexTime = new Date();
        /******************************索引>>开始*****************************/
    	updatePsychosIndex();
    	updateArticlesIndex();
    	updateAdvisoriesIndex();
        /******************************索引<<结束*****************************/       
        //记录更新时间
        lastIndexTime = indexTime;
        storeLastIndexTime();
	}
    
	/**
	 * 每日更新索引
	 */
  //  @Scheduled(cron="0 0 2 * * ? ")	
    public void dailyUpdateIndex()
    {
    	updateTestingsIndex();
    }
    
    /**
     * 搜索咨询师
     * @param words 搜索词
     * @param num 搜索数量
     * @return LuceneIdFlag
     */
    public List<LuceneIdFlag> searchPsychos(String words, Integer num)
    {
    	
    	List<LuceneIdFlag> idFlags = new ArrayList<LuceneIdFlag>();
    	DirectoryReader directoryReader;
		try
		{
			FSDirectory directory = FSDirectory.open(Paths.get(Configuration.LUCENE_PATH + "/index/psychos"));
			directoryReader = DirectoryReader.open(directory);  
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			return idFlags;
		}

    	IndexSearcher searcher = new IndexSearcher(directoryReader);  
    	
    	String[] visibleFields = {"nickname","licenseName", "title", "tags"};  
        BooleanClause.Occur[] visibleClauses = { BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
        		BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD};
    	String[] invisibleFields = {"nickname", "licenseName", "title", "tags", "profile"};  
        BooleanClause.Occur[] invisibleClauses = { BooleanClause.Occur.MUST_NOT, BooleanClause.Occur.MUST_NOT,
        		BooleanClause.Occur.MUST_NOT, BooleanClause.Occur.MUST_NOT, BooleanClause.Occur.SHOULD};
        Query visibleQuery = null;
        Query invisibleQuery = null;
    	try
		{
    		visibleQuery = MultiFieldQueryParser.parse(words ,visibleFields, visibleClauses, new MaxWordAnalyzer());
    		invisibleQuery = MultiFieldQueryParser.parse(words ,invisibleFields, invisibleClauses, new MaxWordAnalyzer());
		} catch (org.apache.lucene.queryparser.classic.ParseException e)
		{
			// TODO Auto-generated catch block
			return idFlags;
		}
    	try
		{
    		TopDocs topDocs = searcher.search(visibleQuery, num);
	    	for(ScoreDoc sd : topDocs.scoreDocs)
	    	{
	    		Document document = searcher.doc(sd.doc);  
	    		LuceneIdFlag luceneIdFlag = new LuceneIdFlag();
	    		luceneIdFlag.setId(Long.parseLong(document.get("mid")));
	    		luceneIdFlag.setFlag(0);;
	    		idFlags.add(luceneIdFlag);
	    	}
	    	topDocs = searcher.search(invisibleQuery, num);
	    	for(ScoreDoc sd : topDocs.scoreDocs)
	    	{
	    		Document document = searcher.doc(sd.doc);  
	    		LuceneIdFlag luceneIdFlag = new LuceneIdFlag();
	    		luceneIdFlag.setId(Long.parseLong(document.get("mid")));
	    		luceneIdFlag.setFlag(1);;
	    		idFlags.add(luceneIdFlag);
	    	}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			return idFlags;
		}  

    	 return idFlags;
    }

	/**
	 * 搜索文章
	 * @param words
	 * @return
	 */
	public List<LuceneIdFlag> searchArticles(String words, Integer num)
	{
    	List<LuceneIdFlag> idFlags = new ArrayList<LuceneIdFlag>();
		DirectoryReader directoryReader;
		try
		{
			FSDirectory directory = FSDirectory.open(Paths.get(Configuration.LUCENE_PATH + "/index/articles"));
			directoryReader = DirectoryReader.open(directory);  
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			return idFlags;
		}
	
		IndexSearcher searcher = new IndexSearcher(directoryReader);  
		
    	String[] visibleFields = {"title"};  
        BooleanClause.Occur[] visibleClauses = { BooleanClause.Occur.SHOULD};
    	String[] invisibleFields = {"title", "digest", "detail"};    
        BooleanClause.Occur[] invisibleClauses = { BooleanClause.Occur.MUST_NOT, BooleanClause.Occur.SHOULD, 
        		BooleanClause.Occur.SHOULD};
        Query visibleQuery = null;
        Query invisibleQuery = null;
    	try
		{
    		visibleQuery = MultiFieldQueryParser.parse(words ,visibleFields, visibleClauses, new MaxWordAnalyzer());
    		invisibleQuery = MultiFieldQueryParser.parse(words ,invisibleFields, invisibleClauses, new MaxWordAnalyzer());
		} catch (org.apache.lucene.queryparser.classic.ParseException e)
		{
			// TODO Auto-generated catch block
			return idFlags;
		}
    	try
		{
    		TopDocs topDocs = searcher.search(visibleQuery, num);
	    	for(ScoreDoc sd : topDocs.scoreDocs)
	    	{
	    		Document document = searcher.doc(sd.doc);  
	    		LuceneIdFlag luceneIdFlag = new LuceneIdFlag();
	    		luceneIdFlag.setId(Long.parseLong(document.get("article_id")));
	    		luceneIdFlag.setFlag(0);;
	    		idFlags.add(luceneIdFlag);
	    	}
	    	topDocs = searcher.search(invisibleQuery, num);
	    	for(ScoreDoc sd : topDocs.scoreDocs)
	    	{
	    		Document document = searcher.doc(sd.doc);  
	    		LuceneIdFlag luceneIdFlag = new LuceneIdFlag();
	    		luceneIdFlag.setId(Long.parseLong(document.get("article_id")));
	    		luceneIdFlag.setFlag(1);;
	    		idFlags.add(luceneIdFlag);
	    	}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			return idFlags;
		}  
	
		 return idFlags;
	}

	/**
	 * 搜索测试
	 * @param words
	 * @return
	 */
	public List<LuceneIdFlag> searchTestings(String words, Integer num)
	{
    	List<LuceneIdFlag> idFlags = new ArrayList<LuceneIdFlag>();
		DirectoryReader directoryReader;
		try
		{
			FSDirectory directory = FSDirectory.open(Paths.get(Configuration.LUCENE_PATH + "/index/testings"));
			directoryReader = DirectoryReader.open(directory);  
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			return idFlags;
		}
	
		IndexSearcher searcher = new IndexSearcher(directoryReader);  
		
    	String[] visibleFields = {"title"};  
        BooleanClause.Occur[] visibleClauses = { BooleanClause.Occur.SHOULD};
    	String[] invisibleFields = {"title", "contentExplain", "contentExplain"};    
        BooleanClause.Occur[] invisibleClauses = { BooleanClause.Occur.MUST_NOT, BooleanClause.Occur.SHOULD, 
        		BooleanClause.Occur.SHOULD};
        Query visibleQuery = null;
        Query invisibleQuery = null;
    	try
		{
    		visibleQuery = MultiFieldQueryParser.parse(words ,visibleFields, visibleClauses, new MaxWordAnalyzer());
    		invisibleQuery = MultiFieldQueryParser.parse(words ,invisibleFields, invisibleClauses, new MaxWordAnalyzer());
		} catch (org.apache.lucene.queryparser.classic.ParseException e)
		{
			// TODO Auto-generated catch block
			return idFlags;
		}
    	try
		{
    		TopDocs topDocs = searcher.search(visibleQuery, num);
	    	for(ScoreDoc sd : topDocs.scoreDocs)
	    	{
	    		Document document = searcher.doc(sd.doc);  
	    		LuceneIdFlag luceneIdFlag = new LuceneIdFlag();
	    		luceneIdFlag.setId(Long.parseLong(document.get("testing_id")));
	    		luceneIdFlag.setFlag(0);;
	    		idFlags.add(luceneIdFlag);
	    	}
	    	topDocs = searcher.search(invisibleQuery, num);
	    	for(ScoreDoc sd : topDocs.scoreDocs)
	    	{
	    		Document document = searcher.doc(sd.doc);  
	    		LuceneIdFlag luceneIdFlag = new LuceneIdFlag();
	    		luceneIdFlag.setId(Long.parseLong(document.get("testing_id")));
	    		luceneIdFlag.setFlag(1);;
	    		idFlags.add(luceneIdFlag);
	    	}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			return idFlags;
		}  
	
		 return idFlags;
	}

	/**
	 * 搜索咨询
	 * @param words
	 * @return
	 */
	public List<LuceneIdFlag> searchAdvisories(String words, Integer num)
	{
    	List<LuceneIdFlag> idFlags = new ArrayList<LuceneIdFlag>();
		DirectoryReader directoryReader;
		try
		{
			FSDirectory directory = FSDirectory.open(Paths.get(Configuration.LUCENE_PATH + "/index/advisories"));
			directoryReader = DirectoryReader.open(directory);  
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			return idFlags;
		}
	
		IndexSearcher searcher = new IndexSearcher(directoryReader);  
		 
		String[] visibleFields = {"content", "tags"};  
        BooleanClause.Occur[] visibleClauses = { BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD};
    	String[] invisibleFields = {"content", "detail", "tags"};    
        BooleanClause.Occur[] invisibleClauses = { BooleanClause.Occur.MUST_NOT, BooleanClause.Occur.SHOULD, 
        		BooleanClause.Occur.MUST_NOT};
        Query visibleQuery = null;
        Query invisibleQuery = null;
    	try
		{
    		visibleQuery = MultiFieldQueryParser.parse(words ,visibleFields, visibleClauses, new MaxWordAnalyzer());
    		invisibleQuery = MultiFieldQueryParser.parse(words ,invisibleFields, invisibleClauses, new MaxWordAnalyzer());
		} catch (org.apache.lucene.queryparser.classic.ParseException e)
		{
			// TODO Auto-generated catch block
			return idFlags;
		}
    	try
		{
    		TopDocs topDocs = searcher.search(visibleQuery, num);
	    	for(ScoreDoc sd : topDocs.scoreDocs)
	    	{
	    		Document document = searcher.doc(sd.doc);  
	    		LuceneIdFlag luceneIdFlag = new LuceneIdFlag();
	    		luceneIdFlag.setId(Long.parseLong(document.get("advisory_id")));
	    		luceneIdFlag.setFlag(0);;
	    		idFlags.add(luceneIdFlag);
	    	}
	    	topDocs = searcher.search(invisibleQuery, num);
	    	for(ScoreDoc sd : topDocs.scoreDocs)
	    	{
	    		Document document = searcher.doc(sd.doc);  
	    		LuceneIdFlag luceneIdFlag = new LuceneIdFlag();
	    		luceneIdFlag.setId(Long.parseLong(document.get("advisory_id")));
	    		luceneIdFlag.setFlag(1);;
	    		idFlags.add(luceneIdFlag);
	    	}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			return idFlags;
		}  
	
		 return idFlags;
	}

}
