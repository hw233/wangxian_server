package com.fy.engineserver.menu.activity.silvercar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fy.engineserver.activity.silvercar.SilvercarManager;
import com.fy.engineserver.activity.silvercar.SilvercarTaskCfg;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_UseCancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.trade.requestbuy.service.RequestBuyManager;

public class Option_QueryTask extends Option {

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {

		try {
			TaskManager.logger.error(player.getLogString() + "[要接取押镖任务]");
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(36000);
			mw.setTitle(Translate.王城运镖);
			Option_UseCancel oc = new Option_UseCancel();
			oc.setText(Translate.取消);
			oc.setColor(0xffffff);
			List<Option> options = new ArrayList<Option>();

			TaskManager taskManager = TaskManager.getInstance();
			SilvercarManager manager = SilvercarManager.getInstance();

			if (player.getJiazuId() <= 0) {// 没有家族
				String taskCollections = Translate.国运;
				int[] nums = player.getCycleDeliverInfo(TaskManager.getInstance().getTaskCollectionsByName(taskCollections));
				if (nums[1] >= 1) {
					player.sendError(Translate.你没有家族每天只能做一次);
					return;
				}
			}
			TaskManager.logger.error(player.getLogString() + "[要接取押镖任务] [配置任务个数:" + manager.getTaskCfgMap().size() + "]");
			for (Iterator<String> itor = manager.getTaskCfgMap().keySet().iterator(); itor.hasNext();) {
				String taskName = itor.next();
				SilvercarTaskCfg taskCfg = manager.getTaskCfgMap().get(taskName);
				Task task = taskManager.getTask(taskName,player.getCountry());
				if (task != null && taskCfg != null) {
					if (TaskManager.gradeFit(player, task)) {
						Option_Car_Task car_Task = new Option_Car_Task();
						// car_Task.setText("使用:[" + ArticleManager.color_article_Strings[taskCfg.getNeedArticleColor()] + taskCfg.getNeedArticleName() + "]接取押镖任务");
						car_Task.setText(Translate.translateString(Translate.接镖提示, new String[][] { { Translate.STRING_1, ArticleManager.color_article_Strings[taskCfg.getNeedArticleColor()] }, { Translate.STRING_2, taskCfg.getNeedArticleName() } }));
						car_Task.setTaskName(taskName);
						options.add(car_Task);
					} else {
						TaskManager.logger.error("[押镖任务不符合玩家的等级] [" + taskName + "] [" + player.getLogString() + "]");
						continue;
					}
				} else {
					TaskManager.logger.error("[押镖任务不存在:{}] [配置:{}]", new Object[] { taskName, taskCfg });
				}
			}
			options.add(oc);
			mw.setDescriptionInUUB(Translate.个人运镖提示);
			mw.setOptions(options.toArray(new Option[0]));
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(res);
		} catch (Exception e) {
			SilvercarManager.logger.error(player.getLogString() + "[查询押镖任务异常]", e);
		}
	}
}
