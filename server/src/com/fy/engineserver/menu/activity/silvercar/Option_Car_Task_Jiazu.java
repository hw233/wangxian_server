package com.fy.engineserver.menu.activity.silvercar;

import java.util.List;

import com.fy.engineserver.activity.silvercar.SilvercarManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuFunction;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.instance.BiaoCheEntity;
import com.fy.engineserver.jiazu2.manager.BiaocheEntityManager;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.jiazu.Option_Jiazu_CallIn;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.BiaoCheNpc;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.RandomTool;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.RandomTool.RandomType;
import com.fy.engineserver.util.TimeTool.TimeDistance;

public class Option_Car_Task_Jiazu extends Option {

	private String taskName;

	@Override
	public void doSelect(Game game, Player player) {
		if (SilvercarManager.logger.isDebugEnabled()) {
			SilvercarManager.logger.debug(player.getLogString() + "[点击任务:{}]", new Object[] { taskName });
		}
		Task task = TaskManager.getInstance().getTask(taskName,player.getCountry());
		if (task == null) {
			player.sendError(Translate.text_silverCar_002);
			SilvercarManager.logger.error(player.getLogString() + "[接取任务:{}][任务不存在]", new Object[] { taskName });
			return;
		}
		if (player.getFollowableNPC() != null) {
			player.sendError(Translate.text_silverCar_001);
			return;
		}

		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		synchronized (jiazu) {
			if (jiazu != null) {
				if (jiazu.getJiazuStatus() == JiazuManager2.封印家族功能) {
					player.sendError(Translate.家族资金不足封印);
					return ;
				}
				if (TimeTool.instance.isSame(jiazu.getLastJiazuSilverCartime(), SystemTime.currentTimeMillis(), TimeDistance.DAY)) {
					player.sendError(Translate.你的家族今天已经压过镖了明天再来吧);
					return;
				}
				JiazuMember member = JiazuManager.getInstance().getJiazuMember(player.getId(), player.getJiazuId());
				if (!JiazuTitle.hasPermission(member.getTitle(), JiazuFunction.release_silvercar)) {
					player.sendError(Translate.text_silverCar_024);
					return;
				}
				CompoundReturn cr = player.addTaskByServer(task);
				if (cr.getBooleanValue()) {
					int color = RandomTool.getResultIndexs(RandomType.groupRandom, SilvercarManager.getInstance().getRefreshRate(), 1).get(0);// 设置颜色
					player.getFollowableNPC().setObjectScale((short) (1000 * SilvercarManager.getInstance().getCarSize()[color]));
					player.getFollowableNPC().setNameColor(ArticleManager.color_article[color]);
					player.getFollowableNPC().setGrade(color);
					jiazu.setLastJiazuSilverCartime(SystemTime.currentTimeMillis());
					SilvercarManager.sneerAt(player, color);
					((BiaoCheNpc) player.getFollowableNPC()).setJiazuCar(true);
					BiaocheEntityManager.instance.addAttr2Biaoche(player);
					// {
					// // 召唤
					// List<Player> onlines = jiazu.getOnLineMembers();
					// for (Player p : onlines) {
					// if (p.getId() != player.getId()) {
					// MenuWindow wm = WindowManager.getInstance().createTempMenuWindow((int) TimeTool.TimeDistance.MINUTE.getTimeMillis());
					// wm.setDescriptionInUUB(player.getName() + "接取了家族镖车!");
					// wm.setTitle("一起运镖");
					// Option_Cancel cancel = new Option_Cancel();
					// cancel.setText(Translate.text_jiazu_004);
					// Option_Jiazu_CallIn callIn = new Option_Jiazu_CallIn(player.getCurrentGame(), player);
					// callIn.setText(Translate.text_jiazu_003);
					// wm.setOptions(new Option[] { callIn, cancel });
					// p.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), wm, wm.getOptions()));
					// }
					// }
					// }
					if (SilvercarManager.logger.isWarnEnabled()) SilvercarManager.logger.warn(player.getLogString() + "[家族ID:" + jiazu.getJiazuID() + "] [家族名字:" + jiazu.getName() + "] [接取运镖任务{" + task.getName() + "}][成功]");
				} else {
					player.sendError(TaskSubSystem.getInstance().getInfo(cr.getIntValue()));
					return;
				}
			}
		}
	}

	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
}
