package com.xuanzhi.tools.simplejpa.impl;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.xuanzhi.tools.dbpool.ConnectionPool;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.text.DateUtil;

import junit.framework.TestCase;

public class EntityManagerArticleTestCase extends TestCase{

	public void testInsert() throws Exception{
		
		SimpleEntityManager<TTAbstractArticleEntity> em = SimpleEntityManagerFactory.getSimpleEntityManager(TTAbstractArticleEntity.class);
		
		ArticleTypeA_A aa = new ArticleTypeA_A();
		aa.id = em.nextId();
		aa.name = "a_wangtianxin";
		
		ArticleTypeB bb = new ArticleTypeB();
		bb.id = em.nextId();
		bb.name = "b_wangtianxin";

		em.save(aa);
		em.save(bb);
	}
	
	public void testQuery() throws Exception{
		

		SimpleEntityManager<TTAbstractArticleEntity> em = SimpleEntityManagerFactory.getSimpleEntityManager(TTAbstractArticleEntity.class);
		ArticleTypeA_A aa = (ArticleTypeA_A)em.find(1100000000000006145L);
		System.out.println("aa.id = " + aa.id + "," + aa.name);
		
		ArticleTypeB bb = (ArticleTypeB)em.find(1100000000000006146L);
		System.out.println("bb.id = " + bb.id + "," + bb.name);
		
		
	}
	
	public void testUpdate() throws Exception{
		
		
	}
	
	public void testInterval() throws Exception{
		
	
	}
	
	public void testQuery2() throws Exception{
		
	}
	
	public void testbatch_Update() throws Exception{
		
		
	}

	
}
