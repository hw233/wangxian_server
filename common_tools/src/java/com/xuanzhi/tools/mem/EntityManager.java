package com.xuanzhi.tools.mem;

/**
 * 
 */
public class EntityManager {
	public static EntityManager self;
	
	public static EntityManager getInstance() {
		if(self == null) {
			synchronized(EntityManager.class) {
				if(self == null) {
					self = new EntityManager();
				}
			}
		}
		return self;
	}
	
	public Entity getEntity() {
		Entity entity = new Entity();
		String a = "";
		for(int i=0; i<1000; i++) {
			a = a + "a";
		}
		entity.setName(a);
		return entity;
	}
}
