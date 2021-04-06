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

public class EntityManagerTestCase extends TestCase{

	ConnectionPool pool;
	
	public EntityManagerTestCase(){
	
	}
	public void testInsert() throws Exception{
		
		SimpleEntityManager<TTPlayer> em = SimpleEntityManagerFactory.getSimpleEntityManager(TTPlayer.class);
		
		long id = em.nextId();
		
		TTPlayer p = new TTPlayer();
		p.setId2(id);
		p.setUsername("wangtianxin_111");
		
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
		
		p.lastRequestTime = DateUtil.parseDate("2010-10-10 10:10:10","yyyy-MM-dd HH:mm:ss");
		
		em.save(p);

	}
	
	public void testQueryInfo() throws Exception{
		
		SimpleEntityManager<TTPlayer> em = SimpleEntityManagerFactory.getSimpleEntityManager(TTPlayer.class);
		
		List<PlayerInfo> list = em.queryFields(PlayerInfo.class, new long[]{1100000000000038913L,1100000000000015361L,1100000000000032769L,1100000000000034817L});
		
		for(int i = 0 ; i < list.size() ; i++){
			System.out.println("PlayerInfo: id = " + list.get(i).getId2() + ", mp2 = " + list.get(i).getMp2());
		}
		
		TTPlayer p = em.find(1100000000000038913L);
		p.setMp2(p.getMp2() + 100);
		em.notifyFieldChange(p, "mp2");
		
		for(int i = 0 ; i < list.size() ; i++){
			System.out.println("PlayerInfo: id = " + list.get(i).getId2() + ", mp2 = " + list.get(i).getMp2());
		}
		
		p.setMp2(p.getMp2() + 100);
		em.notifyFieldChange(p, "mp2");
		
		p = null;
		
		System.gc();
		
		Thread.sleep(1000);
		System.gc();
		
		for(int i = 0 ; i < list.size() ; i++){
			System.out.println("PlayerInfo: id = " + list.get(i).getId2() + ", mp2 = " + list.get(i).getMp2());
		}
		
	}
	
	public void testQuery() throws Exception{
		
		SimpleEntityManager<TTPlayer> em = SimpleEntityManagerFactory.getSimpleEntityManager(TTPlayer.class);
		
		TTPlayer p = em.find(1100000000000015361L);
		
		System.out.println("p.id = " + p.getId2());
		System.out.println("p.username = " + p.getUsername());
		System.out.println("p.playerName = " + p.getPlayerName());
		System.out.println("p.lastRequestTime = " + DateUtil.formatDate(p.lastRequestTime,"yyyy-MM-dd HH:mm:ss"));
		System.out.println("p.getArticleIds2 = " + p.getArticleIds2());
		System.out.println("p.map = " + p.getMap());
		System.out.println("p.getCommon_knapsack() = " + p.getCommon_knapsack());		
	}
	
	public void testUpdate() throws Exception{
		
		SimpleEntityManager<TTPlayer> em = SimpleEntityManagerFactory.getSimpleEntityManager(TTPlayer.class);
		
		TTPlayer p = em.find(1100000000000015361L);
		
		p.setMp2(p.getMp2() + 2);
		p.setMoney(p.getMoney() + 10000);
		
		p.getCommon_knapsack().setLength(p.getCommon_knapsack().getLength()+1);
		
		em.notifyFieldChange(p, "mp2");
		em.notifyFieldChange(p, "money");
		em.notifyFieldChange(p, "common_knapsack");
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0 ; i < p.testString.size() ; i++){
			System.out.println("p.testString["+i+"] = " + p.testString.get(i));
		}
		
		for(int i = 0 ; i < 100 ; i++ ){
			sb.append("一二三四五六七八九十我们都");
		}
		String s = sb.toString();
		
		System.out.println(" s.length = " + s.length());
		
		//p.testString.add(s);
		
		s = JsonUtil.jsonFromObject(p.testString);
		System.out.println(s);
		System.out.println(" s.length = " + s.length());
		
		em.notifyFieldChange(p, "testString");
		
		em.save(p);
		em.save(p);
		em.save(p);
		em.save(p);
		em.save(p);
		
	}
	
	public void testInterval() throws Exception{
		
		SimpleEntityManager<TTPlayer> em = SimpleEntityManagerFactory.getSimpleEntityManager(TTPlayer.class);
		TTPlayer p = em.find(1100000000000015361L);
		p.setMp2(p.getMp2() + 2);
		p.setMoney(p.getMoney() + 10000);
		
		em.notifyFieldChange(p, "mp2");
		em.notifyFieldChange(p, "money");
		
		System.out.println("===================== wait 10s =====================");
		Thread.sleep(10000);
		
		p.setMp2(p.getMp2() + 2);
		p.setMoney(p.getMoney() + 10000);
		
		em.notifyFieldChange(p, "mp2");
		em.notifyFieldChange(p, "money");
		
		System.out.println("===================== wait 60s =====================");
		Thread.sleep(60000);
	}
	
	public void testQuery2() throws Exception{
		SimpleEntityManager<TTPlayer> em = SimpleEntityManagerFactory.getSimpleEntityManager(TTPlayer.class);
		
		long k = 0;
		
		System.out.println(" count(level2>5 and level2 <= 10 and mp2 <> 0) = " + k);
		
		long ids[] = em.queryIds(TTPlayer.class,"level2>5 and level2 <= 10 and mp2 <> 0");
		
		System.out.println(" queryIds(level2>5 and level2 <= 10 and mp2 <> 0) = " + ids.length);
		
		ids = em.queryIds(TTPlayer.class,"level2>5 AND level2 <= 10 OR mp2 <> 0", "username", 100, 500);
		
		System.out.println(" queryIds('level2>5 AND level2 <= 10 OR mp2 <> 0','username,mp2 desc',100,500) = " + ids.length);
		
		List<TTPlayer> al = em.query(TTPlayer.class,"level2>5 AND level2 <= 10 OR mp2 <> 0", "username,mp2 desc", 100, 200);
		
		for(int i = 0 ; i < al.size() ; i++){
			TTPlayer p = al.get(i);
			System.out.println("["+i+"]="+p.getId2()+","+p.getUsername() + ","+p.getPlayerName()+","+p.getLevel2()+","+p.getMp2());
		}
	}
	
	public void testBatch_Update() throws Exception{
		SimpleEntityManager<TTPlayer> em = SimpleEntityManagerFactory.getSimpleEntityManager(TTPlayer.class);
		
		
		int m = 1003;
		ArrayList<TTPlayer> al = new ArrayList<TTPlayer>();
		for(int k = 0 ; k < m ; k++){
			TTPlayer p = new TTPlayer();
			p.setId2(em.nextId());
			p.setUsername("username-"+k);
			p.setPlayerName("playerName-" + k);
			
			HashMap<Long,Long> map = new HashMap<Long,Long>();
			for(int i = 0 ; i < 100; i++){
				map.put(1L*i, 100L*i);
			}
			p.setMap(map);

			long articleIds2[] = new long[1000];
			for(int i = 0 ; i < articleIds2.length ; i++){
				articleIds2[i] = i;
			}
			p.setArticleIds2(articleIds2);
			
			Knapsack.Cell cells[] = new Knapsack.Cell[80];
			for(int i = 0 ; i < cells.length ; i++){
				cells[i] = new Knapsack.Cell(0,0);
				cells[i].articleId = i;
				cells[i].count = 1;
			}
			Knapsack kn = new Knapsack();
			kn.setCells(cells);
			kn.setLength(80);
			
			p.setCommon_knapsack(kn);
			al.add(p);
			
			em.notifyNewObject(p);
		}
		
		System.out.println("============waiting batch save ==================== ");
		Thread.sleep(10000);
		
		for(TTPlayer p : al){
			em.save(p);
		}
		
		System.out.println("============ sleep 10s ==================== ");
		Thread.sleep(10000);
		
		for(TTPlayer p : al){
			p.getCommon_knapsack().setLength(90);
			em.notifyFieldChange(p, "common_knapsack");
			
			p.setMp2(p.getMp2()+10);
			em.notifyFieldChange(p, "mp2");
			
			p.setHp2(p.getHp2() + 100);
			em.notifyFieldChange(p, "hp2");
			
			p.setLevel2(p.getLevel2() + 10);
			em.notifyFieldChange(p, "level2");
		}
		long count = em.count();
		em.destroy();
		System.out.println("total count is: " + count);
		
	}

	
}
