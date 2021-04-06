package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.Monster;

/**
 * 进入商店
 * 
 * 
 *
 */
public class Option_DiceForTeam extends Option{

	public static final int 需求 = 1;
	public static final int 贪婪 = 2;
	public static final int 放弃 = 0;
	
	// 0 标识需求
	// 1 标识贪婪
	// 2 标识放弃
	int dictType;
	

	Monster monster;
	
	ArticleEntity entity;
	
	public Monster getMonster() {
		return monster;
	}

	public void setMonster(Monster monster) {
		this.monster = monster;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_掷骰子;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_4836 + ":" + monster.getName();
	}

	public int getDictType() {
		return dictType;
	}

	public void setDictType(int dictType) {
		this.dictType = dictType;
	}

	public ArticleEntity getEntity() {
		return entity;
	}

	public void setEntity(ArticleEntity entity) {
		this.entity = entity;
	}
}
