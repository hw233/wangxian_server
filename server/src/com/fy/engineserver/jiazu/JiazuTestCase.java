package com.fy.engineserver.jiazu;

import junit.framework.TestCase;

import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class JiazuTestCase extends TestCase {

	public void testJiazu() throws Exception {
		SimpleEntityManager<Jiazu> em = SimpleEntityManagerFactory.getSimpleEntityManager(Jiazu.class);
		Jiazu jiazu = new Jiazu();
		jiazu.setJiazuID(em.nextId());
		em.save(jiazu);
		System.out.println(em.count());
	}
}
