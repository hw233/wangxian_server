package com.xuanzhi.tools.simplejpa.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.fasterxml.jackson.core.type.TypeReference;
import com.xuanzhi.tools.dbpool.ConnectionPool;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.JsonUtil;

import junit.framework.TestCase;

public class JsonTestCase extends TestCase{

	public static class AAAAA{
		List al = new ArrayList();
		Knapsack kn;
		
		HashMap<Long,long[][][]> map = new HashMap<Long,long[][][]>();
	}
	public void testInsert() throws Exception{
		AAAAA aaaaa = new AAAAA();
		
		aaaaa.al.add(new ArticleTypeA());
		aaaaa.al.add(new ArticleTypeB());
		
		System.out.println("al = " + JsonUtil.mapper.writer().writeValueAsString(aaaaa.al));
		
		Knapsack.Cell cells[] = new Knapsack.Cell[3];
		for(int i = 0 ; i < cells.length ; i++){
			cells[i] = new Knapsack.Cell(0,0);
			cells[i].articleId = i;
			cells[i].count = 10009;
		}
		aaaaa.kn = new Knapsack();
		aaaaa.kn.setCells(cells);
		aaaaa.kn.setLength(80);
		
		System.out.println("kn = " + JsonUtil.mapper.writer().writeValueAsString(aaaaa.kn));
		
		
		
		System.out.println("al = " + JsonUtil.mapper.writer().withType(new TypeReference<List<TTAbstractArticleEntity>>(){}).writeValueAsString(aaaaa.al));
		
		System.out.println("kn = " + JsonUtil.mapper.writer().withType(new TypeReference<Knapsack>(){}).writeValueAsString(aaaaa.kn));
		
		
		Field f = AAAAA.class.getDeclaredField("al");
		Object o = f.get(aaaaa);
		
		System.out.println("o = " + JsonUtil.jsonFromObject(o, f.getGenericType()));
		
		f = AAAAA.class.getDeclaredField("kn");
		o = f.get(aaaaa);
		System.out.println("o = " + JsonUtil.jsonFromObject(o, f.getGenericType()));
		
		
		
		f = AAAAA.class.getDeclaredField("map");
		
		assertTrue(Utils.isCollectionType(f.getType()));
		
		System.out.println("getGenericType() = " + f.getGenericType());
		
		Type type = f.getGenericType();

		System.out.println("type.getClass()= " + type.getClass());
		
		assertTrue(type instanceof java.lang.reflect.ParameterizedType);
		
		java.lang.reflect.ParameterizedType pp = (java.lang.reflect.ParameterizedType)type;
		
		sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl tt = (sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl)type;
		
		
		
		Utils.checkCollectionType(AAAAA.class, f, type);
		
	}
	
	public static class TaskEntity{
		private int id;
		private String name;

		
		public int getId(){
			System.out.println(" ============== getId() ");
			return 10999;
		}
		
		public boolean isCompleted(){
			System.out.println(" ============== isCompleted() ");
			return true;
		}
		
		public String getUserName(){
			System.out.println(" ============== getUserName() ");
			return "wangtianxin";
		}
		
		public void setUserName(String s){
			
		}
	}
	
	public void testTaskEntity() throws Exception{
		TaskEntity te = new TaskEntity();
		
		System.out.println(JsonUtil.jsonFromObject(te));
		
		TaskEntity te2 = JsonUtil.objectFromJson(JsonUtil.jsonFromObject(te), TaskEntity.class);
		
		assertEquals(te.id,te2.id);
		assertEquals(te.name,te2.name);
	}
	
	
}
