package com.fy.engineserver.newBillboard.date.robbery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.newBillboard.IBillboardPlayerInfo;
import com.fy.engineserver.newBillboard.BillboardsManager.JiazuSimpleInfo;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

/**
 * 昆仑
 * @author Administrator
 *
 */
public class RobberyCountry1BillBoard extends Billboard{
	@Override
	public void update() throws Exception {
		// TODO Auto-generated method stub
		SimpleEntityManager<TransitRobberyEntity> em = SimpleEntityManagerFactory.getSimpleEntityManager(TransitRobberyEntity.class);
		long[] ids = em.queryIds(TransitRobberyEntity.class, "", new Object[] {}, "currentLevel desc,passLayer desc", 1, BillboardsManager.实际条数 + 1);
		TransitRobberyEntity entity = null;
		List<IBillboardPlayerInfo> list = this.getBillboardPlayerInfo(ids);
		List<BillboardDate> blist = new ArrayList<BillboardDate>();
		if(list != null && list.size() > 0) {
			for(int i=0; i<list.size(); i++) {
				IBillboardPlayerInfo info = list.get(i);
				if(info.getCountry() != 1) {
					continue;
				}
				entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(info.getId());
				if(entity != null && entity.getCurrentLevel() >= 1) {
					if(entity.getCurrentLevel() == 1 && entity.getPassLayer() == 0) {
						continue;
					}
					BillboardDate date = new BillboardDate();
					date.setDateId(info.getId());
					date.setType(BillboardDate.玩家);
					String[] values = new String[6];
					values[0] = info.getName();
					JiazuSimpleInfo jiazu = null;
					if (info.getJiazuId() > 0) {
						jiazu = BillboardsManager.getInstance().getJiazuSimpleInfo(info.getJiazuId());
					}
					if (jiazu == null) {
						values[1] = Translate.无;
					} else {
						values[1] = jiazu.getName();
					}
					values[2] = CareerManager.getInstance().getCareer(info.getMainCareer()).getName();
					values[3] = RobberyConstant.getLevelDes(info.getLevel());
					int currentLevel = entity.getCurrentLevel();
					if((entity.getPassLayer() >= 200 && entity.getPassLayer() < 300) || entity.getPassLayer() == 0) {
						currentLevel = currentLevel >= 9 ? 9 : (currentLevel+1);
					}
					values[4] = currentLevel + "";
					values[5] = entity.getPassLayer()%100 + "";
					date.setDateValues(values);
					blist.add(date);
				}
			}
			int len = blist.size() > RobberyConstant.国家排行显示数 ? RobberyConstant.国家排行显示数 : blist.size();
			blist = RobberyAllBillBoard.sortByLevelAndLayer(blist,4, 5);
			BillboardDate[] bbds = new BillboardDate[len];
			for(int i=0; i<len; i++) {
				bbds[i] = blist.get(i);
			}
			setDates(bbds);
		} else {
			BillboardsManager.logger.error("[查询渡劫榜单玩家数据没有记录] [" + this.getLogString() + "]");
			TransitRobberyManager.logger.error("[查询渡劫榜单玩家数据没有记录] [" + this.getLogString() + "]");
		}
	}
}
