package com.fy.boss.operation.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity(name="GameActivityPlatform")
public class Platform {
	@SimpleId
	private long id;

	@SimpleVersion
	private int version;
	
	private String lang;
	
	private String color;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public String getLogString()
	{
		return "["+id+"] ["+version+"] ["+color+"] ["+lang+"]";
	}
	

}
