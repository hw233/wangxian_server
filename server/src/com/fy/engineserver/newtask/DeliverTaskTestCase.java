package com.fy.engineserver.newtask;

import junit.framework.TestCase;

import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class DeliverTaskTestCase extends TestCase {

	public void testDt() throws Exception {
		SimpleEntityManager<DeliverTask> em = SimpleEntityManagerFactory.getSimpleEntityManager(DeliverTask.class);
		DeliverTask deliverTask = new DeliverTask();
		deliverTask.setId(em.nextId());
		em.save(deliverTask);
		System.out.println(em.count());
	}
}
