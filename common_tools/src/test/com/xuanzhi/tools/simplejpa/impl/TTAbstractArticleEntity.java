package com.xuanzhi.tools.simplejpa.impl;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity(name="TTArtcile")
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public abstract class TTAbstractArticleEntity {

	@SimpleId
	protected long id;
	
	@SimpleVersion
	protected int version;
	
	protected String articleName;
	
	protected int colorType;
}
