package com.fy.engineserver.menu.activity;

import java.util.Collections;
import java.util.List;

import com.fy.engineserver.activity.luckfruit.ForLuckFruitManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuFunction;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuMember4Client;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.SHOW_MEMBER_FORLUCK_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * 族长派发祝福果
 * 
 * 
 */
public class Option_Plant_ForLuck_Tree_Handout extends Option_ForLuck implements NeedCheckPurview,
		Cloneable {

	@Override
	public void doSelect(Game game, Player player) {
		try {
			if (ForLuckFruitManager.logger.isDebugEnabled()) ForLuckFruitManager.logger.debug(player.getLogString() + "[分配祝福果树上的祝福果]");
			if (!player.inSelfSeptStation()) {
				player.sendError(Translate.text_forluck_005);
				return;
			}

			JiazuMember member = JiazuManager.getInstance().getJiazuMember(player.getId(), player.getJiazuId());
			boolean hasPermission = JiazuTitle.hasPermission(member.getTitle(), JiazuFunction.release_forluck);
			if (!hasPermission) {
				player.sendError(Translate.text_forluck_004);
				return;
			}
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());

			List<JiazuMember4Client> jiazuMembers = jiazu.getMember4Clients();
			Collections.sort(jiazuMembers, JiazuManager.orderByTitle);
			boolean[] hasPickuped = new boolean[jiazuMembers.size()];
			for (int i = 0; i < jiazuMembers.size(); i++) {
				hasPickuped[i] = !getNpc().canCollection(jiazuMembers.get(i).getPlayerId());
			}
			String startDes = Translate.尊敬的族长大人你希望帮助哪个族员领取祝福丹;
			String endDes = Translate.每位族员只能在祝福树上摘取一颗祝福丹;
			SHOW_MEMBER_FORLUCK_REQ req = new SHOW_MEMBER_FORLUCK_REQ(GameMessageFactory.nextSequnceNum(), startDes, endDes, getNpc().getId(), jiazuMembers.toArray(new JiazuMember4Client[0]), hasPickuped);
			player.addMessageToRightBag(req);

			if (JiazuSubSystem.logger.isInfoEnabled()) {
				JiazuSubSystem.logger.info(player.getLogString() + "[分配祝福果]");
			}
		} catch (Exception e) {
			ForLuckFruitManager.logger.error(player.getLogString() + "[分配祝福果树上的祝福果] [error]", e);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public boolean canSee(Player player) {
		if (!player.inSelfSeptStation()) {
			return false;
		}
		JiazuMember jiazuMember = JiazuManager.getInstance().getJiazuMember(player.getId(), player.getJiazuId());
		if (jiazuMember == null) {
			return false;
		}
		if (getNpc() == null || !getNpc().isInService()) {
			return false;
		}
		return JiazuTitle.hasPermission(jiazuMember.getTitle(), JiazuFunction.release_forluck);
	}
}
