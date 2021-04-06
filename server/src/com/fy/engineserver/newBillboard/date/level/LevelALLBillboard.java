package com.fy.engineserver.newBillboard.date.level;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.newBillboard.IBillboardPlayerInfo;
import com.fy.engineserver.newBillboard.BillboardsManager.JiazuSimpleInfo;
import com.fy.engineserver.newBillboard.monitorLog.LogRecordManager;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

//全部
public class LevelALLBillboard extends Billboard {

	static class PlayerLevelCompare implements Comparator<IBillboardPlayerInfo> {

		@Override
		public int compare(IBillboardPlayerInfo o1, IBillboardPlayerInfo o2) {

			if (o2.getLevel() > o1.getLevel()) {
				return 1;
			} else if (o2.getLevel() == o1.getLevel()) {
				if (o2.getExp() > o1.getExp()) {
					return 1;
				} else if (o2.getExp() == o1.getExp()) {
					if (o2.getLastUpdateExpTime() < o1.getLastUpdateExpTime()) {
						return 1;
					}
				}
			}
			return -1;
		}

	}

	// //于title对应
	// private String[] dateValues; 显示内容：角色ID、国家、家族、职业、级别

	public void update() throws Exception {
		super.update();
		SimpleEntityManager<Player> em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
		long[] ids = em.queryIds(Player.class, "", new Object[] {}, "level desc,exp desc", 1, BillboardsManager.实际条数 + 1);
		List<IBillboardPlayerInfo> list = this.getBillboardPlayerInfo(ids);
		if (list != null && list.size() > 0) {

			Collections.sort(list, new LevelALLBillboard.PlayerLevelCompare());
			BillboardDate[] bbds = new BillboardDate[list.size()];
			for (int i = 0; i < list.size(); i++) {
				IBillboardPlayerInfo info = list.get(i);
				BillboardDate date = new BillboardDate();
				date.setDateId(info.getId());
				date.setType(BillboardDate.玩家);
				String[] values = new String[5];
				values[0] = info.getName();
				values[1] = CountryManager.得到国家名(info.getCountry());
				JiazuSimpleInfo jiazu = null;
				if (info.getJiazuId() > 0) {
					jiazu = BillboardsManager.getInstance().getJiazuSimpleInfo(info.getJiazuId());
				}
				if (jiazu == null) {
					values[2] = Translate.无;
				} else {
					values[2] = jiazu.getName();
				}
				values[3] = CareerManager.getInstance().getCareer(info.getMainCareer()).getName();
				values[4] = RobberyConstant.getLevelDes(info.getLevel());
				date.setDateValues(values);
				bbds[i] = date;
			}
			setDates(bbds);
			BillboardsManager.logger.warn("[更新榜单玩家数据成功] [" + this.getLogString() + "]");

			for (BillboardDate bd : bbds) {
				ArticleManager.logger.error("[为活动打印日志] [国家等级] [" + bd.getDateId() + "] [" + bd.getType() + "] [" + Arrays.toString(bd.getDateValues()) + "]");
			}

			if (LogRecordManager.getInstance() != null) {
				LogRecordManager.getInstance().活动记录日志(LogRecordManager.等级, this);
			}
		} else {
			BillboardsManager.logger.error("[查询榜单玩家数据没有记录] [" + this.getLogString() + "]");
		}
	}

}
