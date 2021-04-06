package com.fy.engineserver.vip;

import org.slf4j.Logger;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;

/**
 * 同意VIP召唤
 * 
 *  create on 2013年9月3日
 */
public class OptionVipPullAgree extends Option {
	public long invitTime;
	public long inviteId;
	public boolean tpOk;

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void doSelect(Game game, Player player) {
		Logger log = VipManager.logger;
		Player invite = null;
		try {
			invite = PlayerManager.getInstance().getPlayer(inviteId);
		} catch (Exception e) {
			log.error("响应同意传送时为找到发起者 {}", inviteId);
			log.error("exception ", e);
			return;
		}
		if (invite.isOnline() == false) {
			player.sendError(Translate
					.translateString(Translate.xx不在线不能传送, new String[][] { {
							Translate.STRING_1, invite.getName() } }));
			return;
		}
		OptionVipPullAgree request = VipManager.requestMap.get(inviteId);
		if(request != this){
			String msg = com.fy.engineserver.datasource.language.Translate.text_2364;
			player.sendError(msg);
			return;
		}
		if (!VipManager.getInstance().vipPullCheck(invite, player)) {
			player.sendError(Translate.邀请者进入限制地图);
			return;
		}
		//
		Game vipAtGame = invite.getCurrentGame();
		if (vipAtGame == null) {
			log.error("OptionVipPullAgree doSelect 发起者地图为null");
			return;
		}
		int country = invite.getTransferGameCountry();
		player.setTransferGameCountry(country);
		game.transferGame(player, new TransportData(0, 0, 0, 0,
				vipAtGame.gi.name, invite.getX(), invite.getY()));
		tpOk = true;
		if (log.isInfoEnabled()) {
			log.info("已执行传送 {} 至 {}", player.getName(),
					vipAtGame.getGameInfo().displayName);
		}
	}

	public long getInviteId() {
		return inviteId;
	}

	public void setInviteId(long inviteId) {
		this.inviteId = inviteId;
	}
}
