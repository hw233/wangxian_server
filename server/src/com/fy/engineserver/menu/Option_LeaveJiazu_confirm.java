package com.fy.engineserver.menu;

import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.JIAZU_LEAVE_RES;
import com.fy.engineserver.newtask.service.TaskConfig.ModifyType;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;

public class Option_LeaveJiazu_confirm extends Option {
	@Override
	public int getOId() {
		// TODO Auto-generated method stub
		return OptionConstants.SERVER_FUNCTION_LEAVE_JIAZU;
	}

	@Override
	public String getOptionId() {
		// TODO Auto-generated method stub
		return super.getOptionId();
	}

	@Override
	public byte getOType() {
		// TODO Auto-generated method stub
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		JiazuManager jiazuManager = JiazuManager.getInstance();
		Jiazu jiazu = jiazuManager.getJiazu(player.getJiazuName());
		if (jiazu == null) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_6199);
			player.addMessageToRightBag(nreq);
			player.setJiazuName(null);
			return;
		}
		JiazuMember member = jiazuManager.getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (member == null) {
			JIAZU_LEAVE_RES res = new JIAZU_LEAVE_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_6129);
			player.addMessageToRightBag(res);
			return;
		}
		JiazuTitle title = member.getTitle();
		if (title == JiazuTitle.master) {
			JIAZU_LEAVE_RES res = new JIAZU_LEAVE_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_6130);
			player.addMessageToRightBag(res);
			return;
		}
		try {
			jiazuManager.removeMember(player.getId(), jiazu);
			player.setJiazuId(0);
			player.setLeaveJiazuTime(System.currentTimeMillis());
			player.setJiazuName(null);
			player.initJiazuTitleAndIcon();
			player.setGangContribution(0);
			jiazu.getMembers().remove(member);
			
			jiazu.setMemberModify(true);
			JIAZU_LEAVE_RES res = new JIAZU_LEAVE_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.translateString(Translate.text_jiazu_021, new String[][] { { Translate.STRING_1, jiazu.getName() } }));
			player.addMessageToRightBag(res);
			ZongPaiManager.getInstance().oneLeaveJiazuUpdateZongpai(player, jiazu.getJiazuID());
			player.checkFunctionNPCModify(ModifyType.JIAZU_LOST);
			if (false) {// 未做优化之前此处无意义
				player.checkFunctionNPCModify(ModifyType.JIAZU_TITLE_MODIFY_CURRENT);
			}
			ChatMessageService.getInstance().sendMessageToJiazu(jiazu, Translate.translateString(Translate.text_jiazu_013, new String[][] { { Translate.STRING_1, player.getName() } }), "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
