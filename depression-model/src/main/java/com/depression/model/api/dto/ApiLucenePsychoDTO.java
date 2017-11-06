package com.depression.model.api.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.depression.model.MemberTag;

public class ApiLucenePsychoDTO extends ApiPsychoDTO{

    private Integer luceneFlag;

	public Integer getLuceneFlag()
	{
		return luceneFlag;
	}

	public void setLuceneFlag(Integer luceneFlag)
	{
		this.luceneFlag = luceneFlag;
	}
	
	
}
