package com.fy.engineserver.sprite.npc;

import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;

@SuppressWarnings("serial")
public class TaskFollowableNPC extends FollowableNPC {

	/** 哪个任务刷出来的NPC */
	private String taskName;

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	@Override
	public synchronized void onNearTarget() {
		Player owner = null;
		try {
			owner = GamePlayerManager.getInstance().getPlayer(getOwnerId());
		} catch (Exception e) {
			TaskSubSystem.logger.error("[任务护送npc:{}到达目的地,主人不存在了] [主人ID:{}]", new Object[] { getName(), getOwnerId() }, e);
			this.getCurrentGame().removeSprite(this);
			return;
		}
		if (TaskSubSystem.logger.isDebugEnabled()) {
			TaskSubSystem.logger.debug(getOwnerId() + "[-------------------------护送完成-------------------------] [任务名:{}] [NPC:{}]", new Object[] { taskName, getName() });
		}
		if (this instanceof BiaoCheNpc) {
			CountryManager.getInstance().交付镖车(owner, (BiaoCheNpc) this);
		}
		owner.convoyOneNPC(getName(), getGrade());
		this.getCurrentGame().removeSprite(this);
		owner.setFollowableNPC(null);
	}
}
