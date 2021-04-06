package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.JIAZU_APPLY_REQ;
import com.fy.engineserver.message.JIAZU_APPLY_RES;
import com.fy.engineserver.sprite.Player;

public class Option_JiazuApply_Confirm extends Option {
	JIAZU_APPLY_REQ req;

	public Option_JiazuApply_Confirm(JIAZU_APPLY_REQ req) {
		this.req = req;
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
		// 用户级别不能小于10
		if (player.getLevel() < 10) {
			JIAZU_APPLY_RES res = new JIAZU_APPLY_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_6099);
			player.addMessageToRightBag(res);
		}
		// 用户不能在另外一个家族之中
		if (player.getJiazuName() != null && player.getJiazuName().length() > 0) {
			JIAZU_APPLY_RES res = new JIAZU_APPLY_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_6102 + player.getJiazuName() + Translate.text_6103);
			player.addMessageToRightBag(res);
		}
		// 离开家族24小时之内不允许加入新的家族
		if ((com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - player.getLeaveJiazuTime()) < JiazuSubSystem.LEAVE_JIAZU_PENALTY_TIME) {
			JIAZU_APPLY_RES res = new JIAZU_APPLY_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_6104);
			player.addMessageToRightBag(res);
		}
		JiazuManager manager = JiazuManager.getInstance();
		// 删除对其它的家族的申请
		if (player.getRequestJiazuName() != null && player.getRequestJiazuName().length() > 0 && !player.getRequestJiazuName().equals(req.getName())) {
			Jiazu jiazu = manager.getJiazu(player.getRequestJiazuName());
			if (jiazu != null) {
				jiazu.removeRequest(player.getId());
			}
		}
		player.setRequestJiazuName(req.getName());
		Jiazu jiazu = manager.getJiazu(req.getName());
		jiazu.addRequest(player.getId());
		player.setGangContribution(0);
		try {
			MailManager.getInstance().sendMail(jiazu.getJiazuMaster(), null, Translate.家族申请, Translate.text_6366, 0, 0, 0, "");
		} catch (Exception e) {
			JiazuManager.logger.error("[申请家族发送邮件异常]", e);
		}

		JIAZU_APPLY_RES res = new JIAZU_APPLY_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_6100 + req.getName() + Translate.text_6101);
		player.addMessageToRightBag(res);

	}

}
