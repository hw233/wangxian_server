package com.xuanzhi.tools.listcache.concrete;

public class OldObject {
	
	public Object obj;
	public Object oldValue;
	public String property;
	
	public OldObject(Object obj,Object oldValue,String property){
		this.obj = obj;
		this.oldValue = oldValue;
		this.property = property;
	}
	
	public boolean equals(Object o){
		return (super.equals(o) || this.obj.equals(o));
	}
}
