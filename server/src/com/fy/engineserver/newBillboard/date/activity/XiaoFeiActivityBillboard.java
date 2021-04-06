package com.fy.engineserver.newBillboard.date.activity;

import java.util.List;

import com.fy.engineserver.activity.newChongZhiActivity.BillXiaoFeiBoardActivity;
import com.fy.engineserver.activity.newChongZhiActivity.MoneyBillBoardActivityData;
import com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager;
import com.fy.engineserver.activity.newChongZhiActivity.NewXiaoFeiActivity;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.newBillboard.IBillboardPlayerInfo;

public class XiaoFeiActivityBillboard extends Billboard {

	public void update()throws Exception {
		super.update();
		
		NewXiaoFeiActivity a = NewChongZhiActivityManager.instance.getBillBoardXiaoFeiActivity();
		if (a == null) {
			return;
		}
		List<MoneyBillBoardActivityData> datas = ((BillXiaoFeiBoardActivity)a).getSortList(200);
		if (datas !=null) {
			long[] ids = new long[datas.size()];
			for (int i= 0; i < ids.length; i++) {
				ids[i] = datas.get(i).getPlayerID();
			}
			if (ids != null && ids.length > 0) {
				List<IBillboardPlayerInfo> playerList = this.getBillboardPlayerInfo(ids);
				if (playerList != null && playerList.size() > 0) {
					BillboardDate[] bbds = new BillboardDate[playerList.size()];
					for (int i = 0 ; i < playerList.size(); i++) {
						IBillboardPlayerInfo info = playerList.get(i);
						BillboardDate date = new BillboardDate();
						date.setDateId(info.getId());
						date.setType(BillboardDate.玩家);
						
						String[] values = new String[4];
						values[0] = info.getName();
						values[1] = CountryManager.得到国家名(info.getCountry());
						values[2] = "" + info.getLevel();
						values[3] = "" + datas.get(i).getMoney();
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
