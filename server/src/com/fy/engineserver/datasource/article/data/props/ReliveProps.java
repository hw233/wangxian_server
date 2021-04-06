package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.sprite.Player;


/**
 * 
 * 复活
 * 
 */
public class ReliveProps extends Props{

	/**
	 * 加血百分比
	 */
	private int addTotalHpPresent;
	
	/**
	 * 加蓝百分比
	 */
	private int addTotalMpPresent;
	
	public int getAddTotalHpPresent() {
		return addTotalHpPresent;
	}

	public void setAddTotalHpPresent(int addTotalHpPresent) {
		this.addTotalHpPresent = addTotalHpPresent;
	}

	public int getAddTotalMpPresent() {
		return addTotalMpPresent;
	}

	public void setAddTotalMpPresent(int addTotalMpPresent) {
		this.addTotalMpPresent = addTotalMpPresent;
	}

	/**
	 * 使用复活
	 * 
	 * @param player
	 */
	public boolean use(Game game, Player player, ArticleEntity ae) {
		//TODO;
		if(!super.use(game,player,ae)){
			return false;
		}
		if(player.isCanNotIncHp()){
			return false;
		}
		int totalHP = player.getMaxHP();
		int addHp = (addTotalMpPresent*totalHP)/100;
		player.setHp(addHp);
		int totalMP = player.getMaxMP();
		int oldMp = player.getMp();
		int addMp = ((addTotalMpPresent*totalMP)/100)+oldMp;
		player.setMp(addMp);
		return true;
	}
}
