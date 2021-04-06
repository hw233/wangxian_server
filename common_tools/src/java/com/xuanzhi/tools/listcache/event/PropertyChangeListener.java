package com.xuanzhi.tools.listcache.event;

public interface PropertyChangeListener {
	
	public String getName();
	
	public String getProperty();
	
	public void propertyChanged(PropertyChangeEvent event);
	
	public void objectDeleted(ObjectDeleteEvent event);
	
	public void objectCreated(ObjectCreateEvent event);

}
