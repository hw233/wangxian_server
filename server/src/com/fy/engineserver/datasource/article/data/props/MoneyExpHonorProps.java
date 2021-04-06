package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.sprite.Player;

/**
 * 金钱经验荣誉
 *
 */
public class MoneyExpHonorProps extends Props{
	
	/**
	 * 金钱倍率 1.0为1倍，1.5为1.5倍为0是不给金钱
	 */
	private float moneyRate;
	
	/**
	 * 经验倍率 1.0为1倍，1.5为1.5倍为0是不给经验
	 */
	private float expRate;
	
	/**
	 * 荣誉倍率 1.0为1倍，1.5为1.5倍为0是不给荣誉
	 */
	private float honorRate;

	public float getMoneyRate() {
		return moneyRate;
	}


	public void setMoneyRate(float moneyRate) {
		this.moneyRate = moneyRate;
	}


	public float getExpRate() {
		return expRate;
	}


	public void setExpRate(float expRate) {
		this.expRate = expRate;
	}


	public float getHonorRate() {
		return honorRate;
	}


	public void setHonorRate(float honorRate) {
		this.honorRate = honorRate;
	}


	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		if(!super.use(game,player,ae)){
			return false;
		}
		moneyExpHonor(player,ae);
		return true;
	}


	/**
	 * 公式(int)((3375+375*playLevel)*rate);
	 * 
	 * @param playLevel
	 * @param offlineTime
	 * @return
	 */
	public void moneyExpHonor(Player player,ArticleEntity ae){}
}
