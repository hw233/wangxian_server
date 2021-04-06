package com.fy.engineserver.menu.fateActivity;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 仙缘活动  对象不同意
 * @author Administrator
 *
 */
public class Option_FateActivity_Disagree extends Option {
	
	public Player invite;
	public byte type;
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		
//		player.send_HINT_REQ("你拒绝了"+invite.getName());
		player.send_HINT_REQ(Translate.translateString(Translate.你拒绝了xx, new String[][]{{Translate.STRING_1,invite.getName()}}));
		if(invite.isOnline()){
//			invite.send_HINT_REQ(player.getName()+"拒绝了你");
			invite.send_HINT_REQ(Translate.translateString(Translate.xx拒绝了你, new String[][]{{Translate.STRING_1,player.getName()}}));
		}
	}

	public Player getInvite() {
		return invite;
	}

	public void setInvite(Player invite) {
		this.invite = invite;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}
	
}
