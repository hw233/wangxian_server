package com.fy.engineserver.menu;

import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.sprite.Player;

/**
 * 邀请家族成员
 * 
 * 
 */
public class Option_JiazuInvite_Confirm extends Option {

	long jiazuId;

	public Option_JiazuInvite_Confirm() {

	}

	public Option_JiazuInvite_Confirm(long jiazuId) {
		super();
		this.jiazuId = jiazuId;
	}

	@Override
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_APPLY_JIAZU;
	}

	@Override
	public String getOptionId() {
		return super.getOptionId();
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {

		if (player.getJiazuId() != 0) {
			player.sendNotice(Translate.text_6372);
			return;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuId);
		if (jiazu == null) {
			player.sendNotice(Translate.text_6370);
			return;
		}
		int maxNum = jiazu.maxMember();
		if (jiazu.getMemberNum() >= maxNum) {
			player.sendNotice(Translate.text_6109);
			return;
		}

		if (player.getRequestJiazuName() != null) {
			Jiazu req = JiazuManager.getInstance().getJiazu(player.getRequestJiazuName());
			if (req != null) {
				req.removeRequest(player.getId());
			}
		}

		JiazuMember newmember = null;
		try {
			newmember = JiazuManager.getInstance().createJiauMember(jiazu, player, JiazuTitle.civilian);
		} catch (Exception e) {
			JiazuManager.logger.error(player.getLogString() + "[菜单] [创建家族成员]",e);
		}
		jiazu.addMemberID(newmember.getJiazuMemID());
		//jiazu.initMember();

		jiazu.setMemberModify(true);

		player.setRequestJiazuName(null);
		player.setJiazuName(jiazu.getName());
		player.initJiazuTitleAndIcon();

		ChatMessageService.getInstance().sendMessageToJiazu(jiazu, Translate.text_6368 + player.getName() + Translate.text_6369, Translate.系统);
	}
}
