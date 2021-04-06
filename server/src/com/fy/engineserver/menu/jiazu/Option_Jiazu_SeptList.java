package com.fy.engineserver.menu.jiazu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.JIAZU_SHOW_JIAZU_FUNCTION_RES;
import com.fy.engineserver.sprite.Player;

/**
 * 通知客户端查询可申请的家族驻地列表
 * 
 * 
 */
public class Option_Jiazu_SeptList extends Option implements NeedCheckPurview {

	public Option_Jiazu_SeptList() {

	}

	@Override
	public void doSelect(Game game, Player player) {
		JIAZU_SHOW_JIAZU_FUNCTION_RES res = new JIAZU_SHOW_JIAZU_FUNCTION_RES(GameMessageFactory.nextSequnceNum(), (byte) 2);
		player.addMessageToRightBag(res);
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		if (player.getJiazuId() <= 0) {
			return false;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			return false;
		}
		if (jiazu.getBaseID() > 0) {
			return false;
		}
		JiazuMember jiazuMember = JiazuManager.getInstance().getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (jiazuMember.getTitle() == JiazuTitle.master) {
			return true;
		}
		return false;
	}
}
