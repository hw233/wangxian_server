package com.fy.engineserver.newBillboard.date.activity;

import java.util.List;

import com.fy.engineserver.activity.doomsday.DoomsdayManager;
import com.fy.engineserver.activity.doomsday.DoomsdayManager.ContributeData;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.newBillboard.IBillboardPlayerInfo;
import com.fy.engineserver.newBillboard.BillboardsManager.JiazuSimpleInfo;

public class MoShiActivityBillboard1 extends Billboard {

	//昆仑
	
	public void update()throws Exception {
		super.update();
		
		List<ContributeData> cdatas = DoomsdayManager.getInstance().getSortedContributeDatas(1);
		if (cdatas != null) {
			long[] ids = new long[cdatas.size()];
			for (int i= 0; i < ids.length; i++) {
				ids[i] = cdatas.get(i).getPlayerId();
			}
			if (ids != null) {
				List<IBillboardPlayerInfo> playerList = this.getBillboardPlayerInfo(ids);
				if (playerList != null) {
					BillboardDate[] bbds = new BillboardDate[playerList.size()];
					for (int i = 0 ; i < playerList.size(); i++) {
						IBillboardPlayerInfo info = playerList.get(i);
						BillboardDate date = new BillboardDate();
						date.setDateId(info.getId());
						date.setType(BillboardDate.玩家);
						
						String[] values = new String[5];
						values[0] = info.getName();
						JiazuSimpleInfo jiazu = null;
						if(info.getJiazuId() > 0){
							jiazu = BillboardsManager.getInstance().getJiazuSimpleInfo(info.getJiazuId());
						}
						if(jiazu == null){
							values[1] = Translate.无;
						}else{
							values[1] = jiazu.getName();
						}
						values[2] = CareerManager.getInstance().getCareer(info.getMainCareer()).getName();;
						values[3] = "" + info.getLevel();
						values[4] = "" + cdatas.get(i).getContributeCardNum();
						date.setDateValues(values);
						bbds[i] = date;
					}
					setDates(bbds);
				}else {
					setDates(new BillboardDate[0]);
				}
			}
			BillboardsManager.logger.warn("[更新榜单数据成功] ["+this.getLogString()+"] [" + ids.length + "]");
		}
	}
}
