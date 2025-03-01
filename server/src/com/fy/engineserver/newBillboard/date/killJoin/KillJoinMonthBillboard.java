package com.fy.engineserver.newBillboard.date.killJoin;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardStatDate;
import com.fy.engineserver.newBillboard.BillboardStatDateManager;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.newBillboard.IBillboardPlayerInfo;
import com.fy.engineserver.newBillboard.BillboardsManager.JiazuSimpleInfo;
import com.fy.engineserver.newBillboard.monitorLog.LogRecordManager;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;

//当月连斩
public class KillJoinMonthBillboard extends Billboard {

	
//	public void update() throws Exception{
//		super.update();
//		if(this.getDates() == null){
//			//读文件
//			BillboardDate[] date = (BillboardDate[]) BillboardsManager.getInstance().getDisk().get("KillJoinMonth");
//			if(date != null){
//				setDates(date);
//			}else{
//				
//				//第一次读数据库
////				this.updatePerMonth();
//				BillboardDate[] bbds = new BillboardDate[0];
//				setDates(bbds);
//				
//			}
//		}
//	}
	public void update() throws Exception{
		
		super.update();
		long[] ids = null;
		SimpleEntityManager<BillboardStatDate> em = BillboardStatDateManager.em;
		if(BillboardsManager.是真当月){
			int month = BillboardsManager.getInstance().得到当月时间月();
			ids = em.queryIds(BillboardStatDate.class, "killJoinNum > ? AND monthKillJoin = ?",new Object[]{0,month},"killJoinNum desc,killJoinScore desc ",1,BillboardsManager.实际条数+1);
		}else{
			ids = em.queryIds(BillboardStatDate.class, "killJoinNum > ? ",new Object[]{0},"killJoinNum desc,killJoinScore desc ",1,BillboardsManager.实际条数+1);
		}
		//排行榜统计数据
		if(ids != null && ids.length > 0){
			
			List<IBillboardPlayerInfo> playerList = this.getBillboardPlayerInfo(ids);
			
			if(playerList != null && playerList.size() > 0){
				BillboardDate[] bbds = new BillboardDate[playerList.size()];
				for(int i=0;i<playerList.size();i++){
					IBillboardPlayerInfo info = playerList.get(i);
					BillboardDate date = new BillboardDate();
					date.setDateId(info.getId());
					date.setType(BillboardDate.玩家);

					String[] values = new String[4];
					values[0] = info.getName();
					values[1] = CountryManager.得到国家名(info.getCountry());
					JiazuSimpleInfo jiazu = null;
					if(info.getJiazuId() > 0){
						jiazu = BillboardsManager.getInstance().getJiazuSimpleInfo(info.getJiazuId());
					}
					if(jiazu == null){
						values[2] = Translate.无;
					}else{
						values[2] = jiazu.getName();
					}
					BillboardStatDate bbs = BillboardStatDateManager.getInstance().getBillboardStatDate(info.getId());
					if(bbs != null){
						values[3] = bbs.getKillJoinNum()+"";
					}else{
						values[3] = 0+"";
					}
					date.setDateValues(values);
					bbds[i] = date;
				}
				setDates(bbds);
				BillboardsManager.logger.warn("[更新榜单数据成功] ["+this.getLogString()+"]");
			}else{
				BillboardsManager.logger.error("[查询榜单数据没有记录] ["+this.getLogString()+"]");
			}
			
		}else{
			BillboardDate[] bbds = new BillboardDate[0];
			setDates(bbds);
			BillboardsManager.logger.error("[查询榜单数据错误] [没有记录] ["+this.getLogString()+"]");
		}

		if(LogRecordManager.getInstance() != null){
			LogRecordManager.getInstance().活动记录日志(LogRecordManager.连斩, this);
		}
		if(LogRecordManager.getInstance() != null){
			LogRecordManager.getInstance().活动记录日志(LogRecordManager.当月连斩, this);
		}
	}
	
	
	public void updatePerMonth() throws Exception{
		
		//读数据库  改变disk
		SimpleEntityManager<BillboardStatDate> em = BillboardStatDateManager.em;
		long[] ids = em.queryIds(BillboardStatDate.class, "killJoinNum > ? ",new Object[]{0},"killJoinNum desc,killJoinScore desc ",1,BillboardsManager.实际条数+1);
		//排行榜统计数据
		if(ids != null && ids.length > 0){
			
			List<IBillboardPlayerInfo> playerList = this.getBillboardPlayerInfo(ids);
			
			if(playerList != null && playerList.size() > 0){
				BillboardDate[] bbds = new BillboardDate[playerList.size()];
				for(int i=0;i<playerList.size();i++){
					IBillboardPlayerInfo info = playerList.get(i);
					BillboardDate bbdate = new BillboardDate();
					bbdate.setDateId(info.getId());
					bbdate.setType(BillboardDate.玩家);

					String[] values = new String[4];
					values[0] = info.getName();
					values[1] = CountryManager.得到国家名(info.getCountry());
					JiazuSimpleInfo jiazu = null;
					if(info.getJiazuId() > 0){
						jiazu = BillboardsManager.getInstance().getJiazuSimpleInfo(info.getJiazuId());
					}
					if(jiazu == null){
						values[2] = Translate.无;
					}else{
						values[2] = jiazu.getName();
					}
					BillboardStatDate bbs = BillboardStatDateManager.getInstance().getBillboardStatDate(info.getId());
					if(bbs != null){
						values[3] = bbs.getKillJoinNum()+"";
					}else{
						values[3] = 0+"";
					}
					bbdate.setDateValues(values);
					bbds[i] = bbdate;
				}
				setDates(bbds);
				
				BillboardsManager.getInstance().getDisk().put("KillJoinMonth", getDates());
				
				BillboardsManager.logger.warn("[更新榜单数据成功] ["+this.getLogString()+"]");
				
			}else{
				BillboardsManager.logger.error("[查询榜单数据没有记录] ["+this.getLogString()+"]");
			}
			
		}else{
			
			BillboardDate[] bbds = new BillboardDate[0];
			setDates(bbds);
			BillboardsManager.logger.error("[查询榜单数据错误] [没有记录] ["+this.getLogString()+"]");
		}
		
	}
	
	
	static class TestDate implements Serializable{
		
		private static final long serialVersionUID = 1L;
		String st;
		TestDate2 date2;
		
	}
	
	static class TestDate2 implements Serializable{
		
		private static final long serialVersionUID = 1L;
		int a;
		
	}
	
	public static void main(String[] args) {
		
		String diskFile = "F:\\file.ddc";
		File file = new File(diskFile);
		DiskCache disk = new DefaultDiskCache(file,null,"billboards", 100L * 365 * 24 * 3600 * 1000L);
		
		
		TestDate2 date2 = new TestDate2();
		date2.a = 100;
		
		TestDate date = new TestDate();
		date.date2 = date2;
		date.st = "date1";
		
		Object o = disk.get("date");
		if(o == null){
			System.out.println("没有数据");
			disk.put("date", date);
		}else{
			TestDate td = (TestDate)o;
			System.out.println(td.st);
			System.out.println(td.date2.a);
		}
		
	}
	
}
