package com.fy.engineserver.newBillboard.date.charm;

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
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;

//当月糖果
public class SweetMonthBillboard extends Billboard {

	
//	public void update() throws Exception{
//		super.update();
//		if(this.getDates() == null){
//			//读文件
//			BillboardDate[] date = (BillboardDate[]) BillboardsManager.getInstance().getDisk().get("SweetMonth");
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
			ids = em.queryIds(BillboardStatDate.class, "sweetsNum > ? AND monthSweet = ?",new Object[]{0,month},"sweetsNum desc",1,BillboardsManager.实际条数+1);
		}else{
			ids = em.queryIds(BillboardStatDate.class, "sweetsNum > ? ",new Object[]{0},"sweetsNum desc",1,BillboardsManager.实际条数+1);
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
						values[3] = bbs.getSweetsNum()+"";
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
			LogRecordManager.getInstance().活动记录日志(LogRecordManager.当月糖果, this);
		}
	}
	
	
	public void updatePerMonth() throws Exception{
		
		//读数据库  改变disk
		
		SimpleEntityManager<BillboardStatDate> em = BillboardStatDateManager.em;
		long[] ids = em.queryIds(BillboardStatDate.class, " sweetsNum > ? ",new Object[]{0},"sweetsNum desc ",1,BillboardsManager.实际条数+1);
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
						values[3] = bbs.getSweetsNum()+"";
					}else{
						values[3] = 0+"";
					}
					date.setDateValues(values);
					bbds[i] = date;
				}
				setDates(bbds);
				BillboardsManager.getInstance().getDisk().put("SweetMonth", getDates());
				
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
	
	
	public static void main(String[] args) {
		
		String st = "[WARN] 2012-08-23 00:10:12,930 [BeatHeart-Thread-64] - [设置当日送糖成功] [同一天] [{username:lwm659197925}{id:1100000000000011796}{name:旺兔睡、}{level:150}{channel:UC_MIESHI}] [1998]";
		
		String[] str = st.split(" ");
		String keyS = str[7]; 
		//[{username:lwm659197925}{id:1100000000000011796}{name:旺兔睡、}{level:150}{channel:UC_MIESHI}]
		String[] keys2 = keyS.split(":");
		String[] keys3 = keys2[2].split("}");
		String key = keys3[0];
		
		String values = str[8]; 
		//[1998]
		
		String[] values1 = values.split("\\]");
		String[] values2 = values1[0].split("\\[");
		String value = values2[1];
		
		
		System.err.println("key:"+key+"values"+value);
		
	}
	
}
