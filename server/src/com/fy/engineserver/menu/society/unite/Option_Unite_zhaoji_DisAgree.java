package com.fy.engineserver.menu.society.unite;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;

public class Option_Unite_zhaoji_DisAgree extends Option {
	
	
	private long inviteId;
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		
		try {
			player.send_HINT_REQ(Translate.你放弃了传送);
			Player invite = PlayerManager.getInstance().getPlayer(inviteId);
//			invite.send_HINT_REQ(player.getName()+Translate.放弃了传送);
			invite.send_HINT_REQ(Translate.translateString(Translate.XX放弃了传送, new String[][]{{Translate.STRING_1,player.getName()}}));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	public long getInviteId() {
		return inviteId;
	}

	public void setInviteId(long inviteId) {
		this.inviteId = inviteId;
	}
	
}
