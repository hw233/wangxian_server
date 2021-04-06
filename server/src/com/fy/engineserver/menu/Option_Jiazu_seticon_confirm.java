package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuFunction;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.JIAZU_SET_ICON_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;

public class Option_Jiazu_seticon_confirm extends Option {
	JIAZU_SET_ICON_REQ req;

	public Option_Jiazu_seticon_confirm(JIAZU_SET_ICON_REQ req) {
		this.req = req;
	}

	@Override
	public int getOId() {
		// TODO Auto-generated method stub
		return OptionConstants.SERVER_FUNCTION_CONFIRM_SETICON_JIAZU;
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
		if (player.getJiazuId() <= 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_6307);
			player.addMessageToRightBag(nreq);

		}
		if (jiazu == null) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_6199);
			player.addMessageToRightBag(nreq);
			player.setJiazuName(null);
		}
		JiazuMember member = jiazuManager.getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (member == null) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_6129);
			player.addMessageToRightBag(nreq);

		}

		if (!JiazuTitle.hasPermission(member.getTitle(), JiazuFunction.set_icon)) {
			HINT_REQ res = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_6158);
			player.addMessageToRightBag(res);
		}
		// TODO 需升级为龙图阁LV2才可以进行此设置。

		if (player.getBindSilver() < JiazuSubSystem.MODIFY_ICON_COIN) {
			HINT_REQ res = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_6186);
			player.addMessageToRightBag(res);
		}

		// 保存icon,异步
		jiazu.setIconID(req.getIcon_id());
		
		Player ps[] = PlayerManager.getInstance().getOnlinePlayerByJiazu(jiazu);
		for (Player p : ps) {
			p.initJiazuTitleAndIcon();
		}
		HINT_REQ res = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_6189);
		player.addMessageToRightBag(res);
	}
}
