package com.fy.engineserver.menu.society;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;


/**
 * 添加好友成功后，向对方发送是否关注自己（不同意）
 * @author Administrator
 *
 */
public class Option_Interest_DisAgree extends Option{

	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	// 添加你的人
	private long playerA;

	@Override
	public void doSelect(Game game, Player player) {

		try {
			Player p = PlayerManager.getInstance().getPlayer(playerA);
//			player.send_HINT_REQ(Translate.text_你拒绝了关注xx+p.getName());
			player.send_HINT_REQ(Translate.translateString(Translate.你拒绝了关注AA, new String[][]{{Translate.STRING_1,p.getName()}}));
		} catch (Exception e) {

			e.printStackTrace();
		}
		
//		super.doSelect(game, player);
		
	}

	public long getPlayerA() {
		return playerA;
	}

	public void setPlayerA(long playerA) {
		this.playerA = playerA;
	}
	
	
}
