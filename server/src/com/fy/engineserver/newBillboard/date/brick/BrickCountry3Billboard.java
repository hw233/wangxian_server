package com.fy.engineserver.newBillboard.date.brick;

import java.util.List;

import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardStatDate;
import com.fy.engineserver.newBillboard.BillboardStatDateManager;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.newBillboard.IBillboardPlayerInfo;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;

//万法
public class BrickCountry3Billboard extends Billboard {

	
//	于title对应
	//	private String[] dateValues;  显示内容：角色ID、国家、积分；
	
	public void update()throws Exception {
		super.update();
		SimpleEntityManager<BillboardStatDate> em = BillboardStatDateManager.em;
		long[] ids = em.queryIds(BillboardStatDate.class, " country = ? ",new Object[]{3},"brickNum desc ",1,BillboardsManager.实际条数+1);
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

					String[] values = new String[3];
					values[0] = info.getName();
					values[1] = CountryManager.得到国家名(info.getCountry());
					
					BillboardStatDate bbs = BillboardStatDateManager.getInstance().getBillboardStatDate(info.getId());
					if(bbs != null){
						values[2] = bbs.getBrickNum()+"";
					}else{
						values[2] = 0+"";
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
