package com.fy.engineserver.menu.fireActivity;

import com.fy.engineserver.activity.fireActivity.FireActivityNpc;
import com.fy.engineserver.activity.fireActivity.FireActivityNpcEntity;
import com.fy.engineserver.activity.fireActivity.FireManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Player;

/**
 * 篝火 加柴
 * @author Administrator
 * 
 */
public class Option_FireActivity_AddWood extends Option {

	private int activityLevel;
	public static long[] activityCost = { 0L, 100000, 200000, 500000 };

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	/*
	 * (non-Javadoc)
	 * @see com.fy.engineserver.menu.Option#doSelect(com.fy.engineserver.core.Game, com.fy.engineserver.sprite.Player)
	 */
	@Override
	public void doSelect(Game game, Player player) {

		long jiazuId = player.getJiazuId();
		if (jiazuId <= 0) {
			player.send_HINT_REQ(Translate.你还没有家族);
			return;
		}
		Jiazu jz = JiazuManager.getInstance().getJiazu(jiazuId);
		if (jz == null) {
			if (FireManager.logger.isInfoEnabled()) FireManager.logger.info("[篝火活动加柴] [指定家族不存在] [" + jiazuId + "]");
			return;
		} else {
			if (jz.getBaseID() > 0) {
//				List<JiazuMember> list = JiazuManager.getInstance().getJiazuMember(jz.getJiazuID(), JiazuTitle.master);

//				if (list == null || list.size() == 0) {
//					player.send_HINT_REQ(Translate.你们家族还没有设置加柴的人不能加柴);
//					return;
//				}
				boolean bln = true;			//所有人都可以加柴           2014年8月6日16:38:06  
//				for (JiazuMember jm : list) {
//					if (jm.getPlayerID() == player.getId()) {
//						bln = true;
//						break;
//					}
//				}
				if (!bln) {
					player.send_HINT_REQ(Translate.你不是加柴人不能加柴);
					return;
				}
				LivingObject[] los = game.getLivingObjects();
				long septId = -1;
				if (los != null) {
					for (LivingObject lo : los) {
						if (lo instanceof FireActivityNpc) {
							septId = ((FireActivityNpc) lo).getSeptId();
							break;
						}
					}
				}
				if (septId > 0) {
					if (jz.getBaseID() == septId) {

						SeptStation ss = SeptStationManager.getInstance().getSeptStationById(septId);
						if (ss == null) {
							if (FireManager.logger.isInfoEnabled()) FireManager.logger.info("[篝火活动加柴] [指定的驻地为null] [" + septId + "]");
							return;
						}
						FireActivityNpcEntity fe = ss.getFireActivityNpcEntity();
						if (fe != null) {
							long cost = activityCost[activityLevel];
							boolean moneyEnough = false;
							if (player.getSilver() >= cost) {
								moneyEnough = true;
							} else {
								return;
							}
							if (moneyEnough && fe.加柴(player, activityLevel)) {
								player.send_HINT_REQ(Translate.添加灵石成功);
								try {
									BillingCenter.getInstance().playerExpense(player, cost, CurrencyType.YINZI, ExpenseReasonType.家族篝火, "家族篝火");
								} catch (Exception e) {
									e.printStackTrace();
									FireManager.logger.error(player.getLogString() + "[添加家族篝火:"+activityLevel+"] [异常]",e);
								}
							}
						} else {
							if (FireManager.logger.isInfoEnabled()) FireManager.logger.info("[篝火活动加柴] [驻地的篝火实体为null] [" + septId + "]");
						}

					} else {
						player.send_HINT_REQ(Translate.不是在你家族驻地);
					}
				} else {
					if (FireManager.logger.isInfoEnabled()) FireManager.logger.info("[篝火活动加柴] [没有找到篝火npc] [" + septId + "]");
				}

			} else {
				player.send_HINT_REQ(Translate.你还没有驻地);
			}
		}
	}

	public int getActivityLevel() {
		return activityLevel;
	}

	public void setActivityLevel(int activityLevel) {
		this.activityLevel = activityLevel;
	}
}
