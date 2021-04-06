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
/*
 * 棉花糖
 */
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
/**
 * 棉花糖排行榜
 * 
 *
 */
public class MianHuaTangDayBillboard extends Billboard{
	
	
	public void update()throws Exception {
		super.update();
		
		int day = BillboardsManager.getInstance().得到当日时间天();
		SimpleEntityManager<BillboardStatDate> em = BillboardStatDateManager.em;
		long[] ids = em.queryIds(BillboardStatDate.class, " dayFlowersMHTNum > ? AND dayFlowerMHT = ? ",new Object[]{0,day},"dayFlowersMHTNum desc ",1,BillboardsManager.实际条数+1);
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
						values[3] = bbs.getDayFlowersMHTNum()+"";
					}else{
						values[3] = 0+"";
					}
					date.setDateValues(values);
					bbds[i] = date;
				}
				setDates(bbds);
				BillboardsManager.logger.warn("[更新榜单数据成功] ["+this.getLogString()+"]");
				if(LogRecordManager.getInstance() != null){
					LogRecordManager.getInstance().活动记录日志(LogRecordManager.当日棉花糖, this);
				}
			}else{
				BillboardsManager.logger.error("[查询榜单数据没有记录] ["+this.getLogString()+"]");
			}
			
		}else{
			BillboardDate[] bbds = new BillboardDate[0];
			setDates(bbds);
			BillboardsManager.logger.error("[查询榜单数据错误] [没有记录] ["+this.getLogString()+"]");
		}
		
	}
	

}
