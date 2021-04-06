package com.xuanzhi.tools.listcache.event;

import java.util.EventObject;

public class PropertyChangeEvent extends EventObject {

	private static final long serialVersionUID = -47313585824790703L;
	
	protected String property;
	
	protected Object oldValue;
	
	protected String nameScope;
	
	/**
	 * 对象的某个属性发生了变化，需要通知ListCache进行顺序调整。
	 * 到底通知哪些ListCache的是由nameScope来控制了。也就是说本实现并不知道要通知哪些listcache
	 * 需要有用户来指定。而指定的方式，就是通知设置nameScope。
	 * nameScope的格式如下：
	 * 		name;name;...;name
	 * 其中name支持后缀的通配符*，比如下面就是一个合法的nameScope。
	 * 		video.list.all;video.list.cars.*
	 * 
	 * 所有，要想准确的进行通知，用户必须小心设置ListCache的名称，属性值已经这里的nameScope。
	 * 
	 * @param source
	 * @param property
	 * @param oldValue
	 * @param nameScope
	 */
	public PropertyChangeEvent(Object source,String property,Object oldValue,String nameScope) {
		super(source);
		this.property = property;
		this.oldValue = oldValue;
		this.nameScope = nameScope;
	}
	
	public String getProperty(){
		return property;
	}
	
	public Object getOldValue(){
		return oldValue;
	}
	
	public String getNameScope(){
		return nameScope;
	}
}
