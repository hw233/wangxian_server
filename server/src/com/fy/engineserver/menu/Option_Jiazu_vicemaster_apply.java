package com.fy.engineserver.menu;

import java.util.Set;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.exception.WrongFormatMailException;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.mail.Mail;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.mail.service.concrete.DefaultMailManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;

public class Option_Jiazu_vicemaster_apply extends Option {
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		// oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_CONFIRM_APPLY_MASTER;
	}

	public void setOId(int oid) {
	}

	private void sendMail(Player player) {
		MailManager mailManager = DefaultMailManager.getInstance();
		// 发送邮件
		Mail mail = new Mail();
		mail.setPoster(-1);
		mail.setContent(Translate.text_6163);
		mail.setReceiver(player.getId());
		mail.setTitle(Translate.text_6349);
		try {
			mail = mailManager.createMail(mail);
//			MailManager.logger.info("[创建邮件][投票成族长邮件] [成功] [邮件id:" + mail.getId() + "]  [接受人:" + player.getId() + "/" + player.getName() + "/" + player.getUsername() + "] [" + mail.getTitle() + "] [" + mail.getContent() + "]");
			if(MailManager.logger.isInfoEnabled())
				MailManager.logger.info("[创建邮件][投票成族长邮件] [成功] [邮件id:{}]  [接受人:{}/{}/{}] [{}] [{}]", new Object[]{mail.getId(),player.getId(),player.getName(),player.getUsername(),mail.getTitle(),mail.getContent()});
		} catch (WrongFormatMailException e) {
			mail.setCoins(0);
			mail.setLastModifyDate(new java.util.Date());
			MailManager.logger.error("[创建邮件][投票成族长邮件] [异常,中断群发] [邮件id:" + mail.getId() + "]  [接收人:" + player.getId() + "/" + player.getName() + "/" + player.getUsername() + "]", e);
			String error = e.getMessage();
			if (error == null) {
				error = Translate.text_6195;
			}
			HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, error);
			player.addMessageToRightBag(hint);

		}
	}

	@Override
	public void doSelect(Game game, Player player) {

		JiazuManager jiazuManager = JiazuManager.getInstance();
		Jiazu jiazu = jiazuManager.getJiazu(player.getJiazuName());
		if (jiazu == null) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_6199);
			player.addMessageToRightBag(nreq);
			player.setJiazuName(null);

		}
		JiazuMember member = jiazuManager.getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (member == null) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_6129);
			player.addMessageToRightBag(nreq);

		} else if (member.getTitle() != JiazuTitle.vice_master) {
			HINT_REQ res = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_6158);
			player.addMessageToRightBag(res);
		} else {
			if ((com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - jiazu.getLastVoteTime()) < JiazuSubSystem.REVOTE_MASTER_TIME) {

				HINT_REQ res = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_6163);
				player.addMessageToRightBag(res);
			} else {
				Set<Long> memberSet = jiazu.getMemberSet();
				PlayerManager playerManager = PlayerManager.getInstance();

				for (Long playerId : memberSet) {
					if (playerId == player.getId()) continue;
					try {
						Player jiazuPlayer = playerManager.getPlayer(playerId);
						if (jiazuPlayer.isOnline()) {
							HINT_REQ res = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_6164);
							jiazuPlayer.addMessageToRightBag(res);
						} else {
							this.sendMail(player);
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}

	}
}
