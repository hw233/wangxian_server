package com.fy.engineserver.newBillboard.date.country;

import java.util.List;

import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.newBillboard.IBillboardPlayerInfo;
import com.fy.engineserver.newBillboard.BillboardsManager.JiazuSimpleInfo;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

//万法
public class WarScoreCountry3Billboard extends Billboard {

	
	//于title对应
//	玩家名称:国家:家族:活动积分
	
	public void update()throws Exception {
		super.update();
		SimpleEntityManager<Player> em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
		long[] ids = em.queryIds(Player.class, "country = ?",new Object[]{3},"gongxun desc",1,BillboardsManager.实际条数+1);
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
					values[0] = CountryManager.得到国家名(info.getCountry());
					
					JiazuSimpleInfo jiazu = null;
					if(info.getJiazuId() > 0){
						jiazu = BillboardsManager.getInstance().getJiazuSimpleInfo(info.getJiazuId());
					}
					if(jiazu == null){
						values[1] = Translate.无;
					}else{
						values[1] = jiazu.getName();
					}
					
					values[2] = info.getName();
					
					long gongxun = info.getGongxun();
					if(gongxun > 0){
						values[3] = gongxun+"";
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
			BillboardsManager.logger.error("[查询榜单数据错误] [没有记录] ["+this.getLogString()+"]");
		}
		
	}
	
}
