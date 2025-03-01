package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

/**
 * 使用道具确认
 * 
 * 
 *
 */
public class Option_RemoveItemOfKnapsack extends Option{

	Player owner;
	
	ArticleEntity entity;
	
	int indexOfKnapsack;
	
	
	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public ArticleEntity getEntity() {
		return entity;
	}

	public void setEntity(ArticleEntity entity) {
		this.entity = entity;
	}

	public int getIndexOfKnapsack() {
		return indexOfKnapsack;
	}

	public void setIndexOfKnapsack(int indexOfKnapsack) {
		this.indexOfKnapsack = indexOfKnapsack;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		if(player == owner){
//			player.removeItemOfKnapsack(game, indexOfKnapsack);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_丢弃物品;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_4831 + ":" + this.entity.getArticleName();
	}
}
