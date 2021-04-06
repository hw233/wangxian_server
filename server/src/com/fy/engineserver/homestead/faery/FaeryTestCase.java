package com.fy.engineserver.homestead.faery;

import junit.framework.TestCase;

import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class FaeryTestCase extends TestCase {

	public void testFaery() throws Exception {
		SimpleEntityManager<Faery> em = SimpleEntityManagerFactory.getSimpleEntityManager(Faery.class);
		Faery faery = new Faery();
		faery.setId(em.nextId());
		em.save(faery);
		System.out.println(em.count());
	}
}
