package com.fy.engineserver.activity.peekandbrick;

import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.Callbackable;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.RandomTool;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.RandomTool.RandomType;

public class BrickTimeBar implements Callbackable {

	private Player player;
	private String taskName;

	public BrickTimeBar(Player player, String taskName) {
		this.player = player;
		this.taskName = taskName;
	}

	@Override
	public synchronized void callback() {
		try {
			int country = player.getCurrentGameCountry();
			PeekAndBrickManager manager = PeekAndBrickManager.getInstance();
			BuffTemplateManager bm = BuffTemplateManager.getInstance();
			String buffName = manager.getBrickBuffName()[country - 1];
			Buff buff = player.getBuffByName(buffName);

			int level = RandomTool.getResultIndexs(RandomType.groupRandom, manager.getPeekBuffRate(), 1).get(0);
			Task task = TaskManager.getInstance().getTask(taskName,player.getCountry());
			if (task != null) {
				player.getTaskEntity(task.getId()).setScore(level);
			}

			if (PeekAndBrickManager.logger.isWarnEnabled()) {
				PeekAndBrickManager.logger.warn(player.getLogString() + "[偷砖][buff名字{}][颜色:{}][原来同名的buff:{}]", new Object[] { buffName, level, buff });
			}

			if (buff != null) {
				buff.setInvalidTime(0);
			}

			BuffTemplate bt = bm.getBuffTemplateByName(buffName);
			buff = bt.createBuff(level + 1);
			if (buff != null) {
				buff.setStartTime(SystemTime.currentTimeMillis());
				buff.setInvalidTime(buff.getStartTime() + 12 * TimeTool.TimeDistance.HOUR.getTimeMillis());
				buff.setCauser(player);
				player.placeBuff(buff);
				if (PeekAndBrickManager.logger.isWarnEnabled()) {
					PeekAndBrickManager.logger.warn(player.getLogString() + "[获得偷砖buff]" + buff.toString());
				}
				if (level >= ArticleManager.equipment_color_紫) {// 紫色和紫色以上的通知
					ChatMessage msg = new ChatMessage();
					StringBuffer sbf = new StringBuffer();
					int currentCountry = player.getCurrentGameCountry();
					String notice = Translate.translateString(Translate.恭喜获得城砖, new String[][] { { Translate.STRING_1, CountryManager.得到国家名(player.getCountry()) }, { Translate.STRING_2, player.getName() }, { Translate.STRING_3, CountryManager.得到国家名(currentCountry) }, { Translate.STRING_4, ArticleManager.color_article_Strings[level] } });
					sbf.append("<f color='").append(ArticleManager.color_article[level]).append("'>").append(notice).append("</f>");
					msg.setMessageText(sbf.toString());
					if (PeekAndBrickManager.logger.isWarnEnabled()) {
						PeekAndBrickManager.logger.warn(player.getLogString() + "[换到了城砖颜色:{}][发送消息:{}]", new Object[] { level, msg.getMessageText() });
					}
					try {
						ChatMessageService.getInstance().sendMessageToSystem(msg);
					} catch (Exception e) {
						PeekAndBrickManager.logger.error(player.getLogString() + "[偷砖发送广播] [异常]", e);
					}
				}
			} else {
				PeekAndBrickManager.logger.error("[偷砖buff异常][buff不存在:{}]", new Object[] { buffName });
			}
			player.sendError(Translate.text_peekAndBrick_001 + ":<f color='" + ArticleManager.color_article[level] + "'>" + buffName + "</f>");
		} catch (Exception e) {
			PeekAndBrickManager.logger.error("[偷砖buff异常]", e);
		}
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	public String toString() {
		return "BrickTimeBar [player=" + player + "]";
	}
}
