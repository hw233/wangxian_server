package com.fy.engineserver.menu.jiazu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.JIAZU_SHOW_JIAZU_FUNCTION_RES;
import com.fy.engineserver.sprite.Player;

/**
 * 创建家族
 * 
 * 
 */
public class Option_Jiazu_Create extends Option implements NeedCheckPurview {

	public Option_Jiazu_Create() {
		// TODO Auto-generated constructor stub
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		String jiazuName = player.getJiazuName();
		if (jiazuName != null && jiazuName.trim().length() > 0) {
			player.sendError(Translate.text_6087);
			JiazuSubSystem.logger.error("[创建家族] [失败] [您已经在家族中了，不能创建！] [{}] [{}]", new Object[] { player.getName(), jiazuName });
			return;
		}
		if (player.getLevel() < JiazuSubSystem.CREATE_JIAZU_LEVLE) {
			player.sendError(Translate.text_6088);
			JiazuSubSystem.logger.error("[创建家族] [失败] [您的级别不够40级，不能创建！] [{}] [{}]", new Object[] { player.getName(), player.getLevel() });
			return;
		}
		JIAZU_SHOW_JIAZU_FUNCTION_RES res = new JIAZU_SHOW_JIAZU_FUNCTION_RES(GameMessageFactory.nextSequnceNum(), (byte) 0);
		player.addMessageToRightBag(res);
	}

	@Override
	public boolean canSee(Player player) {
		if (player.getJiazuId() > 0) {
			return false;
		}
		return true;
	}
}
