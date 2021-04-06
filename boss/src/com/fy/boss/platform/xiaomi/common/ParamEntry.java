package com.fy.boss.platform.xiaomi.common;

import java.util.Map.Entry;

public class ParamEntry implements Entry<String, String> {

	String key;
	String val;
	public ParamEntry( String key, String val )
	{
		this.key = key;
		this.val = val;
	}
	@Override
	public String getKey()
	{
		return key;
	}
	@Override
	public String getValue()
	{
		return val;
	}
	@Override
	public String setValue( String object )
	{
		this.val = object;
		return this.val;
	}

}
