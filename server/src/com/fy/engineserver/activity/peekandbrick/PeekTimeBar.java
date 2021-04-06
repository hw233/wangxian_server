package com.fy.engineserver.activity.peekandbrick;

import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.Callbackable;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.RandomTool;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.RandomTool.RandomType;

public class PeekTimeBar implements Callbackable {

	private Player player;
	private String taskName;

	public PeekTimeBar(Player player, String taskName) {
		this.player = player;
		this.taskName = taskName;
	}

	@Override
	public void callback() {
		try {
			if (PeekAndBrickManager.logger.isWarnEnabled()) PeekAndBrickManager.logger.warn(player.getLogString() + "[刺探结束]");
			Task task = TaskManager.getInstance().getTask(taskName,player.getCountry());
			int status = player.getTaskStatus(task);
			if (status == TaskConfig.TASK_STATUS_COMPLETE || status == TaskConfig.TASK_STATUS_GET) {
				boolean broadcast = true;
				boolean isShouXunReward = CountryManager.getInstance().isShouXunReward(player);// 是授勋的
				PeekAndBrickManager manager = PeekAndBrickManager.getInstance();
				int color = 0;
				if (isShouXunReward) {
					color = 4;
				} else {
					color = RandomTool.getResultIndexs(RandomType.groupRandom, manager.getPeekBuffRate(), 1).get(0);
				}
				String buffName = manager.getPeekBuffName()[player.getCurrentGameCountry() - 1];
				Buff buff = player.getBuffByName(buffName);
				player.getTaskEntity(task.getId()).setScore(color);
				player.setLastPeekTime(SystemTime.currentTimeMillis());
				if (PeekAndBrickManager.logger.isWarnEnabled()) {
					PeekAndBrickManager.logger.warn(player.getLogString() + "[刺探结束] [获得buff:{}][颜色:{}] [是否是授勋:{}] [是否广播:{}]", new Object[] { buffName, color, isShouXunReward, broadcast });
				}
				if (buff != null) {
					buff.setInvalidTime(0);
					if (isShouXunReward) {
						broadcast = false;
					}
				}
				BuffTemplateManager bm = BuffTemplateManager.getInstance();
				BuffTemplate bt = bm.getBuffTemplateByName(buffName);
				buff = bt.createBuff(color + 1);
				buff.setStartTime(SystemTime.currentTimeMillis());
				buff.setInvalidTime(buff.getStartTime() + 12*TimeTool.TimeDistance.HOUR.getTimeMillis());
				buff.setCauser(player);
				player.placeBuff(buff);

				if (broadcast) {
					player.sendError(Translate.text_peekAndBrick_002 + Translate.text_peekAndBrick_008 +":<f color='" + ArticleManager.color_article[color] + "'>" + buffName + "</f>");
				}

			}
		} catch (Exception e) {
			PeekAndBrickManager.logger.error("[刺探异常]", e);
		}
	}
}
