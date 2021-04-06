package com.xuanzhi.tools.monitor;

import java.io.*;
import java.util.*;

public class MonitorData {

	protected int httpResponseCode;
	protected Properties props = new Properties();
	
	public MonitorData(int httpResponseCode,byte[] data){
		this.httpResponseCode = httpResponseCode;
		try {
			props.load(new ByteArrayInputStream(data));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String[] getPropKeys(){
		return props.keySet().toArray(new String[0]);
	}
	public int getResponseCode(){
		return httpResponseCode;
	}
	
	public String getData(String key){
		return props.getProperty(key);
	}
	
	public int getDataAsInteger(String key,int defualtValue){
		String s = props.getProperty(key);
		if(s == null) return defualtValue;
		try{
			return Integer.parseInt(s);
		}catch(Exception e){
			return defualtValue;
		}
	}
	
	public long getDataAsLong(String key,long defualtValue){
		String s = props.getProperty(key);
		if(s == null) return defualtValue;
		try{
			return Long.parseLong(s);
		}catch(Exception e){
			return defualtValue;
		}
	}
	
	public double getDataAsDouble(String key,double defualtValue){
		String s = props.getProperty(key);
		if(s == null) return defualtValue;
		try{
			return Double.parseDouble(s);
		}catch(Exception e){
			return defualtValue;
		}
	}
	
	public boolean getDataAsBoolean(String key,boolean defualtValue){
		String s = props.getProperty(key);
		if(s == null) return defualtValue;
		try{
			return Boolean.parseBoolean(s);
		}catch(Exception e){
			return defualtValue;
		}
	}
	
	public void setData(String key,String value){
		props.setProperty(key,value);
	}

}
