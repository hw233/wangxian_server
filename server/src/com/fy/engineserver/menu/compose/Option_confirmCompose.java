package com.fy.engineserver.menu.compose;

import java.util.List;

import com.fy.engineserver.compose.ComposeManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.ENTER_COMPOSE_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

public class Option_confirmCompose extends Option{
	
	private long[] materiaIds;
	
	private int costType;
	
	private int composeType;
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		byte result = 0;
		int resultNum = 0;
		long atId = 0;
		try {
			List<ArticleEntity> rList = ComposeManager.instance.composeArticle(player, composeType, costType, materiaIds, false);
			if(rList != null && rList.size() > 0) {
				result = 1;
				resultNum = rList.size();
				atId = rList.get(0).getId();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ComposeManager.logger.error("[合成系统] [合成物品出错2][" + player.getLogString() + "]", e);
		}
		ENTER_COMPOSE_RES res = new ENTER_COMPOSE_RES(GameMessageFactory.nextSequnceNum(), composeType, result, atId, resultNum);
		player.addMessageToRightBag(res);
	}
	public long[] getMateriaIds() {
		return materiaIds;
	}
	public void setMateriaIds(long[] materiaIds) {
		this.materiaIds = materiaIds;
	}
	public int getCostType() {
		return costType;
	}
	public void setCostType(int costType) {
		this.costType = costType;
	}
	public int getComposeType() {
		return composeType;
	}
	public void setComposeType(int composeType) {
		this.composeType = composeType;
	}
	
	
}
