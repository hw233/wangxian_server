package com.fy.engineserver.lineup;

import com.fy.engineserver.sprite.Player;

/**
 * 排队的玩家
 * 
 *
 */
public class LineupPlayer {
	/**
	 * 玩家
	 */
	public Player player;
	
	/**
	 * 在队伍中的角色
	 */
	public byte teamrole;

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public byte getTeamrole() {
		return teamrole;
	}

	public void setTeamrole(byte teamrole) {
		this.teamrole = teamrole;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof LineupPlayer) {
			LineupPlayer lp = (LineupPlayer)obj;
			if(player.getId() == lp.player.getId()) {
				return true;
			}
		}
		return false;
	}
}
