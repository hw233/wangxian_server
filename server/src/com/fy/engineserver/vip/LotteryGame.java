package com.fy.engineserver.vip;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.shop.ActivityPropHasRate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.VIP_LOTTERY_CLICK_RES;
import com.fy.engineserver.sprite.Player;

/**
 * 转盘游戏
 */
public class LotteryGame {

	/** 固定奖励 */
	private ActivityPropHasRate fixedGiven;
	/** 随机奖励 */
	private List<ActivityPropHasRate> randomGiven = new ArrayList<ActivityPropHasRate>();
	/** 总的权值 */
	private int totalWeigh;

	static Random random = new Random();

	public void init() {
		for (ActivityPropHasRate ap : randomGiven) {
			if (ap != null) {
				totalWeigh += ap.getWight();
			}
		}
	}

	/**
	 * 玩家旋转一次转盘
	 * @param player
	 * @return
	 */
	public void play(Player player) {
		if (player.getVipAgent().canLottery(Calendar.getInstance())) {
			synchronized (player) {
				if (player.getVipAgent().canLottery(Calendar.getInstance())) {
					ActivityPropHasRate[] given = new ActivityPropHasRate[2];
					int randomIndex = getOnceResultIndex();
					List<Player> players = new ArrayList<Player>();
					players.add(player);

					given[0] = fixedGiven;
					given[1] = randomGiven.get(randomIndex);
					
					
					// TODO 邮件内容
					ActivityManager.sendMailForActivity(players, given, VipManager.mailTitle, VipManager.mailContent, "vip转盘");
					player.getVipAgent().setLastLotterytime(System.currentTimeMillis());
					player.setVipAgent(player.getVipAgent());
					if (VipManager.logger.isWarnEnabled()) {
						VipManager.logger.warn("[VIP转盘] [玩家获奖] [" + player.getLogString() + "]");
					}
					VIP_LOTTERY_CLICK_RES res = new VIP_LOTTERY_CLICK_RES(GameMessageFactory.nextSequnceNum(), fixedGiven.getTempArticleEntity().getId(),given[1] == null ? -1 : randomIndex);
					player.addMessageToRightBag(res);
				}
			}
		}
	}

	public int getOnceResultIndex() {

		int result = random.nextInt(totalWeigh);
		if (VipManager.logger.isInfoEnabled()) {
			VipManager.logger.info("[转动转盘] [totalWeigh:" + totalWeigh + "] [result:" + result + "] [" + randomGiven.size() + "]");
		}
		int noniusLeft = 0;// 游标的左端
		int noniusRight = 0;// 游标的右端
		int index = 0;
		for (int i = 0 ;i < randomGiven.size();i++) {
			ActivityPropHasRate ap = randomGiven.get(i);
			noniusLeft = noniusRight;
			noniusRight += ap.getWight();
			if (result >= noniusLeft && result < noniusRight) {// 左闭右开区间
				if (VipManager.logger.isInfoEnabled()) {
					VipManager.logger.info("[转动转盘] [中] [第:" + index + "个] [" + ap.getArticleCNName() + "]");
				}
				return i;
			} else {
				if (VipManager.logger.isInfoEnabled()) {
					VipManager.logger.info("[转动转盘] [--] [第:" + index + "个] [" + ap.getArticleCNName() + "]");
				}
			}
		}
		return -1;
	}

	public ActivityPropHasRate getFixedGiven() {
		return fixedGiven;
	}

	public void setFixedGiven(ActivityPropHasRate fixedGiven) {
		this.fixedGiven = fixedGiven;
	}

	public List<ActivityPropHasRate> getRandomGiven() {
		return randomGiven;
	}

	public void setRandomGiven(List<ActivityPropHasRate> randomGiven) {
		this.randomGiven = randomGiven;
	}
}