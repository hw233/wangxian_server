package com.fy.engineserver.menu.activity.doomsday;

import com.fy.engineserver.activity.doomsday.DoomsdayManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 进门的选项,要在windows.xls中配置参数   只能是进BOSS场景
 * 
 * 
 */
public class Option_EnterDoomsday_doors extends Option {

	private String mapName;// 门的名字(要进入地图的名字)

	public Option_EnterDoomsday_doors() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		DoomsdayManager.getInstance().enterBossMap(player, mapName);
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
