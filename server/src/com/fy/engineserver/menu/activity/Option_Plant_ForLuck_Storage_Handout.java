package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.luckfruit.ForLuckFruitManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuFunction;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuMember4Client;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.CHECK_STORAGE_FORLUCK_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

/**
 * 从仓库中向玩家发放祝福果
 * 
 * 
 */
public class Option_Plant_ForLuck_Storage_Handout extends Option_ForLuck implements
		NeedCheckPurview {

	@Override
	public void doSelect(Game game, Player player) {
		if (!player.inSelfSeptStation()) {
			player.sendError(Translate.text_forluck_005);
			return;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		JiazuMember member = JiazuManager.getInstance().getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (!JiazuTitle.hasPermission(member.getTitle(), JiazuFunction.release_forluck)) {// 是否有权限
			player.sendError(Translate.text_forluck_004);
			return;
		}
		// if (getNpc() == null || !getNpc().isInService()) {
		// player.sendError(Translate.text_forluck_019);
		// return;
		// }
		// 判断通过 想客户端发出消息，弹出界面
		if (ForLuckFruitManager.logger.isInfoEnabled()) {
			ForLuckFruitManager.logger.info(player.getLogString() + "[查看家族中的果实][数量:{}][成员:{}]", new Object[] { jiazu.getForLuckFriutNums(), jiazu.getMember4Clients().toArray(new JiazuMember4Client[0]).length });
		}
		CHECK_STORAGE_FORLUCK_REQ req = new CHECK_STORAGE_FORLUCK_REQ(GameMessageFactory.nextSequnceNum(), "", ForLuckFruitManager.getInstance().getFruitName(), jiazu.getForLuckFriutNums(), "", jiazu.getMember4Clients().toArray(new JiazuMember4Client[0]));
		player.addMessageToRightBag(req);
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		if (!player.inSelfSeptStation()) {
			return false;
		}

		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu.getLevel() <= 1) {
			return false;
		}
		JiazuMember jiazuMember = JiazuManager.getInstance().getJiazuMember(player.getId(), player.getJiazuId());
		if (jiazuMember == null) {
			return false;
		}
		return JiazuTitle.hasPermission(jiazuMember.getTitle(), JiazuFunction.release_forluck);
	}
}
