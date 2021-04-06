package com.fy.engineserver.menu.society;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;


/**
 * 添加好友成功后，向对方发送是否关注自己（同意）
 * @author Administrator
 *
 */
public class Option_Interest_Agree extends Option{

	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	// 添加你的人
	private long playerA;

	@Override
	public void doSelect(Game game, Player player) {

		
		try {
			Player p = PlayerManager.getInstance().getPlayer(playerA);
			SocialManager.getInstance().addFriend(player, p);
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
	}

	public long getPlayerA() {
		return playerA;
	}

	public void setPlayerA(long playerA) {
		this.playerA = playerA;
	}
	
	
}
