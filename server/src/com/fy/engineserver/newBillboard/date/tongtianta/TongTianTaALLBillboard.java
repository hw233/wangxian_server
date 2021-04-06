package com.fy.engineserver.newBillboard.date.tongtianta;

import java.util.List;

import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardStatDate;
import com.fy.engineserver.newBillboard.BillboardStatDateManager;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.newBillboard.IBillboardPlayerInfo;
import com.fy.engineserver.newBillboard.BillboardsManager.JiazuSimpleInfo;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;

//全部
public class TongTianTaALLBillboard extends Billboard {
	

//	玩家姓名:国家:家族:等级:界:层
	public void update()throws Exception {
		super.update();
		SimpleEntityManager<BillboardStatDate> em = BillboardStatDateManager.em;
		long[] ids = em.queryIds(BillboardStatDate.class, " ", new Object[]{},"jie desc,ceng desc ,finishTongTianTaTime asc",1,BillboardsManager.实际条数+1);
		if(ids != null && ids.length > 0){
			
			List<IBillboardPlayerInfo> playerList = this.getBillboardPlayerInfo(ids);
			
			if(playerList != null && playerList.size() > 0){
				BillboardDate[] bbds = new BillboardDate[playerList.size()];
				for(int i=0;i<playerList.size();i++){
					IBillboardPlayerInfo info = playerList.get(i);
					BillboardDate date = new BillboardDate();
					date.setDateId(info.getId());
					date.setType(BillboardDate.玩家);
					String[] values = new String[6];
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
					values[3] = RobberyConstant.getLevelDes(info.getLevel());
					
					BillboardStatDate bbs = BillboardStatDateManager.getInstance().getBillboardStatDate(info.getId());
					if(bbs != null){
						values[4] = BillboardsManager.千层塔道[bbs.getJie()]+"";
						values[5] = (bbs.getCeng()+1)+"";
					}else{
						values[4] = 0+"";
						values[5] = 0+"";
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
			BillboardsManager.logger.error("[查询榜单数据错误] [没有记录] ["+this.getLogString()+"]");
		}
		
	}
}
