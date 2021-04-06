package com.fy.engineserver.septstation;

import junit.framework.TestCase;

import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class SeptStationTestCase extends TestCase {

	public void testSeptStation() throws Exception {
		SimpleEntityManager<SeptStation> em = SimpleEntityManagerFactory.getSimpleEntityManager(SeptStation.class);
		SeptStation septStation = new SeptStation();
		septStation.setId(em.nextId());
		em.save(septStation);
		System.out.println(em.count());
	}

}
