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
import com.xuanzhi.tools.text.JsonUtil;

import junit.framework.TestCase;

public class EntityManagerParamTestCase extends TestCase{

	ConnectionPool pool;
	
	public EntityManagerParamTestCase(){
	
	}
	public void testInsert() throws Exception{
		
		SimpleEntityManager<TTPlayer> em = SimpleEntityManagerFactory.getSimpleEntityManager(TTPlayer.class);
		
		long id = em.nextId();
		
		TTPlayer p = new TTPlayer();
		p.setId2(id);
		p.setUsername("wangtianxin_1234");
		
		HashMap<Long,Long> map = new HashMap<Long,Long>();
		for(int i = 0 ; i < 100; i++){
			map.put(1L*i, 100L*i);
		}
		p.setMap(map);

		long articleIds2[] = new long[1000];
		for(int i = 0 ; i < articleIds2.length ; i++){
			articleIds2[i] = em.nextId();
		}
		p.setArticleIds2(articleIds2);
		
		Knapsack.Cell cells[] = new Knapsack.Cell[80];
		for(int i = 0 ; i < cells.length ; i++){
			cells[i] = new Knapsack.Cell(0,0);
			cells[i].articleId = em.nextId();
			cells[i].count = 10009;
		}
		Knapsack kn = new Knapsack();
		kn.setCells(cells);
		kn.setLength(80);
		
		p.setCommon_knapsack(kn);
		
		p.lastRequestTime = new java.util.Date();
		
		em.save(p);
		
		
		id = em.nextId();
		
		p = new TTPlayer();
		p.setId2(id);
		p.setUsername("wangtianxin_1234");
		
	
		p.setMap(map);

		p.setArticleIds2(articleIds2);
		
		p.setCommon_knapsack(kn);
		
		p.lastRequestTime = new java.util.Date();
		
		em.notifyNewObject(p);
		
		System.out.print("等待SimpleJPA自动存盘");
		for(int i = 0 ; i < 10 ; i++){
			Thread.sleep(1000);
			System.out.print(".");
		}

		System.out.println("等待结束。");
	}
	
	public void testQueryInfo() throws Exception{
		
		SimpleEntityManager<TTPlayer> em = SimpleEntityManagerFactory.getSimpleEntityManager(TTPlayer.class);
		
		long[] ids = em.queryIds(TTPlayer.class, "username='wangtianxin_1234'");
	
		System.out.print("find ids by username='wangtianxin_1234', ids= ");
		for(int i = 0 ; i < ids.length ; i++){
			System.out.print(ids[i]+",");
		}
		System.out.println("");
		
		System.out.println("");
		
		ids = em.queryIds(TTPlayer.class, "username=?",new Object[]{"wangtianxin_1234"});
		
		System.out.print("find ids by username=?, param={\"wangtianxin_1234\"} ids= ");
		for(int i = 0 ; i < ids.length ; i++){
			System.out.print(ids[i]+",");
			
		}
		System.out.println("");
		
		System.out.println("");
		
		long count = em.count(TTPlayer.class, "username=? and lastRequestTime > ? and lastRequestTime <= ?", new Object[]{"wangtianxin_1234",new java.util.Date(System.currentTimeMillis() - 24*3600000),new java.util.Date(System.currentTimeMillis())});
		
		System.out.println("count by username=? and lastRequestTime > ? and lastRequestTime <= ?, result = " + count);
		
		//count = em.count(TTPlayer.class, "username=? and lastRequestTime > to_date(?,'yyyy-mm-dd') and lastRequestTime <= to_date(?,'yyyy-mm-dd')", new Object[]{"wangtianxin_1234", DateUtil.formatDate(new java.util.Date(System.currentTimeMillis()),"yyyy-MM-dd"),DateUtil.formatDate(new java.util.Date(System.currentTimeMillis() + 24*3600000),"yyyy-MM-dd")});
		
		//System.out.println("count by username=? and lastRequestTime > to_date(?,'yyyy-mm-dd') and lastRequestTime <= to_date(?,'yyyy-mm-dd'), result = " + count);
		
		ids = em.queryIds(TTPlayer.class, "username=? and lastRequestTime > ? and lastRequestTime <= ?", new Object[]{"wangtianxin_1234", new java.util.Date(System.currentTimeMillis() - 24*3600000),new java.util.Date(System.currentTimeMillis())}, "", 1, 10);
		
		System.out.print("find ids by 分页条件参数查询1~10 ids= ");
		for(int i = 0 ; i < ids.length ; i++){
			System.out.print(ids[i]+",");
			
			TTPlayer t = em.find(ids[i]);
			
			assertEquals(t.getId2(), ids[i]);
			assertEquals(t.getUsername(),"wangtianxin_1234");
		}
		System.out.println("");
		
		
		List<TTPlayer> list= em.query(TTPlayer.class, "username=? and lastRequestTime > str_to_date(?,'%Y-%m-%d') and lastRequestTime <= str_to_date(?,'%Y-%m-%d')", 
				new Object[]{"wangtianxin_1234", DateUtil.formatDate(new java.util.Date(System.currentTimeMillis()),
						"yyyy-MM-dd"),DateUtil.formatDate(new java.util.Date(System.currentTimeMillis() + 24*3600000),"yyyy-MM-dd")}, 
				"lastRequestTime desc", 1, count+1);
		
		
		System.out.println("find TTPlayer by 分页条件参数查询1~"+(count+1)+" objects: ");
		int index = 1;
		for(TTPlayer tt : list){
			System.out.println("["+index+"] ["+tt.getId2()+"] ["+tt.getUsername()+"] ["+DateUtil.formatDate(tt.getLastRequestTime(), "yyyy-MM-dd HH:mm:ss")+"]");
			index++;
			
			tt.playerName42 ="adsfsdfsdafasfsadfsadfasdfdsfsfdsafd";
			
			em.notifyFieldChange(tt, "username");
			em.notifyFieldChange(tt, "playerName");
			em.notifyFieldChange(tt, "level2");
			em.notifyFieldChange(tt, "playerName42");
		}
		System.out.println("");
		
		System.out.print("等待SimpleJPA自动存盘");
		for(int i = 0 ; i < 10 ; i++){
			Thread.sleep(1000);
			System.out.print(".");
		}

		System.out.println("等待结束。");
		
		for(TTPlayer tt : list){
			em.remove(tt);
		}
		
		count = em.count(TTPlayer.class, "username=? and lastRequestTime > ? and lastRequestTime <= ?", new Object[]{"wangtianxin_1234",new java.util.Date(System.currentTimeMillis() - 24*3600000),new java.util.Date(System.currentTimeMillis())});
		
		assertEquals(count,0);
		
	}
	
	
	
}
