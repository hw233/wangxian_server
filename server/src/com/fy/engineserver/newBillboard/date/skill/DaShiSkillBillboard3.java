package com.fy.engineserver.newBillboard.date.skill;

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
import com.fy.engineserver.newBillboard.monitorLog.LogRecordManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;

public class DaShiSkillBillboard3 extends Billboard{

	public void update()throws Exception {
		super.update();
		
		SimpleEntityManager<BillboardStatDate> em = BillboardStatDateManager.em;
		long[] ids = em.queryIds(BillboardStatDate.class, " skillChongNum > ? AND country = ? ",new Object[]{0,3},"skillChongNum desc ",1,BillboardsManager.显示条数+1);
		if(ids != null && ids.length > 0){
			List<IBillboardPlayerInfo> playerList = this.getBillboardPlayerInfo(ids);
			if(playerList != null && playerList.size() > 0){
				BillboardDate[] bbds = new BillboardDate[playerList.size()];
				for(int i=0;i<playerList.size();i++){
					IBillboardPlayerInfo info = playerList.get(i);
					//
					if(info.getCountry()!=3){
						BillboardStatDate bdate = BillboardStatDateManager.getInstance().getBillboardStatDate(info.getId());
						bdate.setCountry(info.getCountry());
						BillboardsManager.logger.error("[大使技能排行榜国家更新] [万法] [成功] [name:"+info.getName()+"] [pid:"+info.getId()+"] [country3:"+info.getCountry()+"]");
						continue;
					}
					
					BillboardDate date = new BillboardDate();
					date.setDateId(info.getId());
					date.setType(BillboardDate.玩家);

					String[] values = new String[5];
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
						values[4] = bbs.getSkillChongNum()+"";
					}else{
						values[4] = 0+"";
					}
					date.setDateValues(values);
					bbds[i] = date;
				}
				setDates(bbds);
				BillboardsManager.logger.warn("[更新榜单数据成功] [大使技能重数] [bbds:"+bbds==null?"0":bbds.length+"] ["+this.getLogString()+"]");
				if(LogRecordManager.getInstance() != null){
					LogRecordManager.getInstance().活动记录日志(LogRecordManager.大使技能重数, this);
				}
			}else{
				BillboardsManager.logger.error("[查询榜单数据没有记录] [大使技能重数] ["+this.getLogString()+"]");
			}
			
		}else{
			BillboardDate[] bbds = new BillboardDate[0];
			setDates(bbds);
			BillboardsManager.logger.error("[查询榜单数据错误] [大使技能重数] [没有记录] ["+this.getLogString()+"]");
		}
		
	}	
}
