package com.depression.task;

import org.springframework.beans.factory.annotation.Autowired;
import com.depression.service.LuceneService;

public class LuceneUpdateTask {

	 @Autowired
	 LuceneService luceneService;
	 public void excute() {
		 //分词索引任务执行
		 luceneService.freqUpdateIndex();
		 luceneService.dailyUpdateIndex();
		 luceneService.initUpdateIndex();
	 } 
}
