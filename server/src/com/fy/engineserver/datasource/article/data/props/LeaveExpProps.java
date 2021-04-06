package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

/**
 * 可转化为经验的离线时间
 *
 */
public class LeaveExpProps extends Props{

	/**
	 * 可转化为经验的离线时间
	 */
	private int exchangeableOfflineTime;
	
	/**
	 * 倍率 1.0为1倍，1.5为1.5倍
	 */
	private float rate;

	public int getExchangeableOfflineTime() {
		return exchangeableOfflineTime;
	}

	public void setExchangeableOfflineTime(int exchangeableOfflineTime) {
		this.exchangeableOfflineTime = exchangeableOfflineTime;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		if(!super.use(game,player,ae)){
			return false;
		}
		player.addExp(exchangeExp(player.getLevel(),player.getTotalOfflineTime()), ExperienceManager.ADDEXP_REASON_BUYING);
		if(player.getTotalOfflineTime() > this.exchangeableOfflineTime){
			player.setTotalOfflineTime(player.getTotalOfflineTime() - this.exchangeableOfflineTime);
		}else{
			player.setTotalOfflineTime(0);
		}
		return true;
	}


	/**
	 * 离线兑换经验，公式(int)((3375+375*playLevel)*rate);
	 * 
	 * @param playLevel
	 * @param offlineTime
	 * @return
	 */
	public int exchangeExp(int playLevel,long offlineTime){
		int exp = (int)((3375+20*playLevel*playLevel)*rate);
		if(offlineTime > this.exchangeableOfflineTime){
			exp = exp*exchangeableOfflineTime;
		}else{
			exp = (int)(exp*offlineTime);
		}
		return exp;
	}
	@Override
	public String canUse(Player p) {
		// TODO Auto-generated method stub
		String resultStr =  super.canUse(p);
		if(resultStr == null){
			if(p.getTotalOfflineTime() == 0){
				resultStr = Translate.text_3739;
			}
		}
		return resultStr;
	}
}
