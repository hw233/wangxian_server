package com.fy.engineserver.newtask;

import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Callbackable;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.TaskCollectionNPC;

/**
 * 采集读进度条
 * 
 * 
 */
public class TaskCollectionProgressBar implements Callbackable {

	private Player player;
	private TaskCollectionNPC npc;

	public TaskCollectionProgressBar(Player player, TaskCollectionNPC npc) {
		this.player = player;
		this.npc = npc;
	}

	@Override
	public void callback() {
		try {
			if (TaskSubSystem.logger.isDebugEnabled()) {
				TaskSubSystem.logger.debug(player.getLogString() + "[采集NPC读条结束]");
			}
			player.onceCollection(npc);
		} catch (Exception e) {
			TaskSubSystem.logger.error(player.getLogString() + "[采集异常]", e);
		}
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public TaskCollectionNPC getNpc() {
		return npc;
	}

	public void setNpc(TaskCollectionNPC npc) {
		this.npc = npc;
	}
}
