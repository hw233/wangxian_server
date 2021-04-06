package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.horse2.manager.Horse2Manager;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;

public class Option_CallMonster extends Option implements NeedCheckPurview{
	/** 召唤bossid */
	private int monsterId;
	/** 消耗银子数量 */
	private long costSilverNums;
	/** 使用cd */
	private long commonCD;
	
	private int pointX;
	
	private int pointY;

	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		long now = System.currentTimeMillis();
		boolean canCall = false;
		try {
			if (!Horse2Manager.instance.callBossTimes.containsKey(monsterId)) {
				canCall = true;
			} else {
				long t = Horse2Manager.instance.callBossTimes.get(monsterId);
				if (t + commonCD <= now) {
					canCall = true;
				}
			}
			if (canCall) {
				if (costSilverNums > 0 && player.getSilver() >= costSilverNums) {
					try {
						BillingCenter.getInstance().playerExpense(player, costSilverNums, CurrencyType.YINZI, ExpenseReasonType.召唤boss, "召唤boss消耗");
					} catch (Exception e) {
						ArticleManager.logger.warn("[召唤boss] [失败] [" + player.getLogString() + "]", e);
						player.sendError(Translate.银子不足);
						return ;
					}
					Horse2Manager.instance.callBossTimes.put(monsterId, now);
					Monster sprite = MemoryMonsterManager.getMonsterManager().createMonster(monsterId);
					if (pointX > 0) {
						sprite.setX(pointX);
						sprite.setY(pointY);
					} else {
						sprite.setX(player.getX());
						sprite.setY(player.getY());
					}
					if (sprite instanceof BossMonster) {
						((BossMonster)sprite).lastCauseDamageTime = System.currentTimeMillis();
					}
					sprite.callerId = player.getId();
					sprite.setBornPoint(new Point(sprite.getX(), sprite.getY()));
					game.addSprite(sprite);
					Horse2Manager.instance.playerCallBossMaps.put(player.getId(), now);
					ArticleManager.logger.warn("[召唤boss] [成功] [ " + player.getLogString() + "] [sprite:" + sprite.getName() + "] [" + monsterId + "]");
				} else {
					player.sendError(Translate.银子不足);
				}
			} else {
				player.sendError(Translate.越狱boss休息时间);
			}
		} catch (Exception e) {
			ArticleManager.logger.warn("[召唤boss] [异常] [" + player.getLogString() + "]", e);
		}
		
	}
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

	public int getMonsterId() {
		return monsterId;
	}

	public void setMonsterId(int monsterId) {
		this.monsterId = monsterId;
	}

	public long getCostSilverNums() {
		return costSilverNums;
	}

	public void setCostSilverNums(long costSilverNums) {
		this.costSilverNums = costSilverNums;
	}

	public long getCommonCD() {
		return commonCD;
	}

	public void setCommonCD(long commonCD) {
		this.commonCD = commonCD;
	}

	public int getPointY() {
		return pointY;
	}

	public void setPointY(int pointY) {
		this.pointY = pointY;
	}

	public int getPointX() {
		return pointX;
	}

	public void setPointX(int pointX) {
		this.pointX = pointX;
	}

	@Override
	public boolean canSee(Player player) {
		// TODO Auto-generated method stub
		if (monsterId == 20113387) {
			if (!Horse2Manager.instance.playerCallBossMaps.containsKey(player.getId())) {
				Buff b = player.getBuffByName(CountryManager.囚禁buff名称);
				return b != null;
			}
		}
		return false;
	}
	
	

}
