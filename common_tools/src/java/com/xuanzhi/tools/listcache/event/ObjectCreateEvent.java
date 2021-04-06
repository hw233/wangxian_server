package com.xuanzhi.tools.listcache.event;

import java.util.EventObject;

public class ObjectCreateEvent extends EventObject{

	private static final long serialVersionUID = -7830778342673728556L;
	
	protected String nameScope;
	protected String properties[];
	
	/**
	 * 创建新的对象
	 *  
	 * 到底通知哪些ListCache的是由nameScope来控制了。也就是说本实现并不知道要通知哪些listcache
	 * 需要有用户来指定。而指定的方式，就是通知设置nameScope。
	 * nameScope的格式如下：
	 * 		name;name;...;name
	 * 其中name支持后缀的通配符*，比如下面就是一个合法的nameScope。
	 * 		video.list.all;video.list.cars.*
	 * 
	 * 所有，要想准确的进行通知，用户必须小心设置ListCache的名称，属性值已经这里的nameScope。 
	 * @param source
	 * @param properties
	 * @param nameScope
	 */
	
	public ObjectCreateEvent(Object source,String properties[],String nameScope) {
		super(source);
		this.nameScope = nameScope;
		this.properties = properties;
	}
	
	public String getNameScope(){
		return nameScope;
	}
	
	public String[] getProperties(){
		return properties;
	}

}
