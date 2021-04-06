package com.fy.engineserver.menu.activity.exchange;

import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 选择指定物品真正兑换
 * @author Administrator
 *
 */
public class Option_True_Exchange extends Option {
	
	//0酒，1帖
	private int chooseType;
	private int articleType;
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public int getChooseType() {
		return chooseType;
	}
	public void setChooseType(int chooseType) {
		this.chooseType = chooseType;
	}


	public int getArticleType() {
		return articleType;
	}

	public void setArticleType(int articleType) {
		this.articleType = articleType;
	}

	@Override
	public void doSelect(Game game, Player player) {
		try{
			ActivityManager.getInstance().trueExchange(player, chooseType, articleType);
		}catch (Exception e) {
			ActivitySubSystem.logger.error("[真实兑换异常] ["+player.getLogString()+"]",e);
		}
	}
	
}


