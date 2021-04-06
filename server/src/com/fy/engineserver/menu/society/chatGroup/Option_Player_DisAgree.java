package com.fy.engineserver.menu.society.chatGroup;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;

/**
 * 玩家不同意加入群组
 * @author Administrator
 *
 */
public class Option_Player_DisAgree extends Option {
	
	private Player p;
	private long groupId;
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_加入聊天分组申请;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		
//		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//		if(p.isOnline()){
//			p.send_HINT_REQ(player.getName()+"拒绝了你的邀请");
//		}
	}

	public Player getP() {
		return p;
	}

	public void setP(Player p) {
		this.p = p;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	
}
