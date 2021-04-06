package com.xuanzhi.tools.simplejpa.impl;

import java.util.ArrayList;
import java.util.List;

import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

import junit.framework.TestCase;

public class MysqlJPATestCase extends TestCase{

	public void testInitTables() throws Exception {
		
		SimpleEntityManager<MysqlPlayer> em = SimpleEntityManagerFactory.getSimpleEntityManager(MysqlPlayer.class);
		
		System.out.println("总行数："+em.count());
	}
	
	public void testInsert() throws Exception{
		SimpleEntityManager<MysqlPlayer> em = SimpleEntityManagerFactory.getSimpleEntityManager(MysqlPlayer.class);
		int n = 3;
		for(int i = 0 ; i < n ; i++){
			MysqlPlayer p = new MysqlPlayer();
			p.username = "wyg_"+i;
			p.playerName = "王玉刚_"+i;
			p.hp2 = i;
			p.level2 = i;
			p.levelOn2 = i;
			p.exp2 = i * 1000;
			
			for(int j = 0 ; j < 100 ; j++){
				p.taskMap.put("task_"+j, "tastentity_"+j);
			}
			
			p.id = em.nextId();
			
			em.save(p);
		}
		
		System.out.println("进入"+n+"行后的，总行数："+em.count());
	}
	
	public void testCount() throws Exception{
		SimpleEntityManager<MysqlPlayer> em = SimpleEntityManagerFactory.getSimpleEntityManager(MysqlPlayer.class);
		long c = em.count(MysqlPlayer.class, "level2 > 100");
		
		System.out.println("level2 > 100的数量：" + c);
		
		c = em.count(MysqlPlayer.class, "level2 >= 200");
		
		System.out.println("level2 >= 200的数量：" + c);
		
		c = em.count(MysqlPlayer.class,"level2 >= ?",new Object[]{200});
		System.out.println("level2 >= ?, ? = 200的数量：" + c);
	}
	
	public void testFind() throws Exception{
		SimpleEntityManager<MysqlPlayer> em = SimpleEntityManagerFactory.getSimpleEntityManager(MysqlPlayer.class);
	
		long c = em.count(MysqlPlayer.class,"level2 >= ?",new Object[]{200});
		System.out.println("level2 >= ?, ? = 200的数量：" + c);
		int start = 1;
		int p = 15;
		int n = (int)c/15 + 1;
		for(int i = 0 ; i < n ; i++){
			long ids[] = em.queryIds(MysqlPlayer.class, "level2 >= ?", new Object[]{200}, "hp2 desc", start, start+p);
			
			start += p;
			
			for(int j = 0 ; j < ids.length ; j++){
				MysqlPlayer pp = em.find(ids[j]);
				System.out.println("[加载Player] [id:"+pp.id+"] [level:"+pp.level2+"] [hp:"+pp.hp2+"] ["+pp.username+"] ");
			}
		}
		
	}
	
	public void testQuery() throws Exception{
		SimpleEntityManager<MysqlPlayer> em = SimpleEntityManagerFactory.getSimpleEntityManager(MysqlPlayer.class);
		long c = em.count(MysqlPlayer.class,"level2 >= ?",new Object[]{200});
		
		List<MysqlPlayer> list = em.query(MysqlPlayer.class, "level2 >= ?", new Object[]{200}, "hp2 desc", 1, 1+c);
		
		for(MysqlPlayer pp : list){
			System.out.println("[加载Player] [id:"+pp.id+"] [level:"+pp.level2+"] [hp:"+pp.hp2+"] ["+pp.username+"] ");
		}
	}
	
	public void testQuery2() throws Exception{
		SimpleEntityManager<MysqlPlayer> em = SimpleEntityManagerFactory.getSimpleEntityManager(MysqlPlayer.class);
		long c = em.count(MysqlPlayer.class,"level2 >= 150");
		
		List<MysqlPlayer> list = em.query(MysqlPlayer.class, "level2 >= 150","hp2 desc", 1, 1+c);
		
		for(MysqlPlayer pp : list){
			System.out.println("[加载Player] [id:"+pp.id+"] [level:"+pp.level2+"] [hp:"+pp.hp2+"] ["+pp.username+"] ");
		}
	}
	
	public static interface MysqlPlayerInfo{
		public long getId();
		public int getHp2();
		public int getLevel2();
		public String getUsername();
		public String getPlayerName();
	}
	
	public void testQueryField() throws Exception{
		SimpleEntityManager<MysqlPlayer> em = SimpleEntityManagerFactory.getSimpleEntityManager(MysqlPlayer.class);
		
		long ids[] = em.queryIds(MysqlPlayer.class, "level2 >= ? or exp2 >= ?", new Object[]{100,120*1000},"hp2 desc", 1, 1000);
		
		List<MysqlPlayerInfo> piList = em.queryFields(MysqlPlayerInfo.class,ids);
		
		for(MysqlPlayerInfo pi : piList){
			System.out.println("[加载简单的Player] [id:"+pi.getId()+"] [level:"+pi.getHp2()+"] [hp:"+pi.getLevel2()+"] ["+pi.getUsername()+"] ");
		}
		
	}
	
	public void testRemove() throws Exception{
		SimpleEntityManager<MysqlPlayer> em = SimpleEntityManagerFactory.getSimpleEntityManager(MysqlPlayer.class);
		long ids[] = em.queryIds(MysqlPlayer.class, "level2 >= ? or exp2 >= ?", new Object[]{200,220*1000},"hp2 desc", 1, 1000);
		
		for(int i = 0 ; i < ids.length ; i++){
			MysqlPlayer p = em.find(ids[i]);
			em.remove(p);
		}
	}
	
	public void testNotify() throws Exception{
		SimpleEntityManager<MysqlPlayer> em = SimpleEntityManagerFactory.getSimpleEntityManager(MysqlPlayer.class);
		long ids[] = em.queryIds(MysqlPlayer.class, "level2 >= ? or exp2 >= ?", new Object[]{100,100*1000},"hp2 desc", 1, 1000);
		ArrayList<MysqlPlayer> al = new ArrayList<MysqlPlayer>();
		for(int i = 0 ; i < ids.length ; i++){
			MysqlPlayer p = em.find(ids[i]);
			
			p.hp2 = p.hp2 + 100;
			em.notifyFieldChange(p, "hp2");
			p.isGM = true;
			em.notifyFieldChange(p, "isGM");
			p.mp2 = 1000;
			em.notifyFieldChange(p, "mp2");
			
			al.add(p);
		}
		for(int i = 0 ; i < 100 ; i++){
			MysqlPlayer p = new MysqlPlayer();
			p.id = em.nextId();
			p.username = "wangtianxin_"+i;
			p.playerName = "wangtianxin_" + i;
			em.notifyNewObject(p);
			
			al.add(p);
		}
		
		System.out.println("========================等待60s保存数据========================== ");
		for(int i = 0 ; i < 60 ; i ++){
			Thread.sleep(1000);
			System.out.print(".");
		}
		System.out.println("========================等待保存数据完毕========================== ");
	}
	
	
}
