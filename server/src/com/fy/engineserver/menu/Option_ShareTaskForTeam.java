package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

/**
 * 进入商店
 * 
 * 
 *
 */
public class Option_ShareTaskForTeam extends Option{


//	Task task;
	
	Player invitePlayer;
	
	
//
//	public Task getTask() {
//		return task;
//	}
//
//	public void setTask(Task task) {
//		this.task = task;
//	}

	public Player getInvitePlayer() {
		return invitePlayer;
	}

	public void setInvitePlayer(Player invitePlayer) {
		this.invitePlayer = invitePlayer;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_共享任务;
	}

	public void setOId(int oid) {
	}
	
}
