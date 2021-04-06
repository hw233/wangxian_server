package com.xuanzhi.tools.mem;

/**
 *
 * 
 */
public class ReferenceTest {
	
	private long start = 0;
	
	private long hcount = 0;
	
	private Entity entity = null;
	
	public void destroy() {
		long elaped = System.currentTimeMillis()-start;
		System.out.println("[强应用] [完成"+hcount+"次调用] ["+(elaped)+"ms]");
	}
	
	public void runStrongRefTest() {
		start = System.currentTimeMillis();
		while(true) {
			
		}
	}
	
	public Entity getEntity() {
		if(entity == null) {
			entity = EntityManager.getInstance().getEntity();
		}
		return entity;
	}
	
	public static void main(String args[]) {
		
	}
}
