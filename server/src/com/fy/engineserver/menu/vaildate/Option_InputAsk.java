package com.fy.engineserver.menu.vaildate;

import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.validate.ValidateAsk;

public class Option_InputAsk extends Option {

	private Task task;
	private ValidateAsk validteAsk;
	private Player player;

	public Option_InputAsk(Task task, ValidateAsk validteAsk, Player player) {
		this.task = task;
		this.validteAsk = validteAsk;
		this.player = player;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public ValidateAsk getValidteAsk() {
		return validteAsk;
	}

	public void setValidteAsk(ValidateAsk validteAsk) {
		this.validteAsk = validteAsk;
	}

	@Override
	public void doInput(Game game, Player player, String inputContent) {
		if (player != this.player) {
			if (TaskSubSystem.logger.isWarnEnabled()) {
				TaskSubSystem.logger.warn("[答题任务验证] [答题] [要接取任务:" + task.getName() + "]" + player.getLogString() + "[玩家输入答案:" + inputContent + "] [题目:" + validteAsk.toString() + "] [非本人]");
			}
			return;
		}
		boolean right = TaskSubSystem.validateManager.notifyAnswerAsk(player, validteAsk, inputContent, 0);
		if (right) {
			CompoundReturn cr = player.addTaskByServer(task);
			if (cr.getBooleanValue()) {
				TaskSubSystem.validateManager.notifyTakeOneTask(player);
			}
			long exp = player.getNextLevelExp() / 1000;
			if (exp < 500) {
				exp = 500;
			}
			player.addExp(exp, ExperienceManager.ADDEXP_REASON_QUIZ);
			player.sendError(Translate.translateString(Translate.恭喜你回答正确获得了xx经验, new String[][]{{Translate.COUNT_1, ""+exp}}));
			if (TaskSubSystem.logger.isWarnEnabled()) {
				TaskSubSystem.logger.warn("[答题任务验证] [答题] [要接取任务:" + task.getName() + "]" + player.getLogString() + "[玩家输入答案:" + inputContent + "] [题目:" + validteAsk.toString() + "] [正确] [接取任务状态:" + cr.getBooleanValue() + "]");
			}
		} else {
			player.sendError(Translate.很可惜你回答错误开动脑筋再想想);
			if (TaskSubSystem.logger.isWarnEnabled()) {
				TaskSubSystem.logger.warn("[答题任务验证] [答题] [要接取任务:" + task.getName() + "]" + player.getLogString() + "[玩家输入答案:" + inputContent + "] [题目:" + validteAsk.toString() + "] [失败]");
			}
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
