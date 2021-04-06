package com.fy.engineserver.menu.activity.jiazu2;

import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.jiazu2.model.JiazuRenwuModel;
import com.fy.engineserver.jiazu2.model.JiazuTaskModel4Client;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.JIAZU_TASK_WINDOW_OPEN_RES;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.prizes.TaskPrize;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfExp;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.targets.TaskTarget;
import com.fy.engineserver.sprite.Player;

public class Open_takeJiazuTaskWindow extends Option{
	@Override
	public void doSelect(Game game, Player player) {
		String result = JiazuManager2.checkJiazu(player);
		if (result != null) {
			player.sendError(result);
			return;
		}
		JiazuMember jm = JiazuManager.getInstance().getJiazuMember(player.getId(), player.getJiazuId());
		List<Long> taskIdList = JiazuManager2.instance.getPlayerJiazuTask(player);
		JiazuTaskModel4Client[] tasks = new JiazuTaskModel4Client[taskIdList.size()];
		for (int i=0; i<taskIdList.size(); i++) {
			Task task = TaskManager.getInstance().getTask(taskIdList.get(i));
			JiazuRenwuModel jrm = JiazuManager2.instance.taskMap.get(taskIdList.get(i));
			tasks[i] = new JiazuTaskModel4Client();
			tasks[i].setTaskId(taskIdList.get(i));
			tasks[i].setTaskName(task.getName());
			tasks[i].setDes(task.getDes());
			tasks[i].setStar(jrm.getStar());
			String rewardDes = JiazuManager2.instance.translate.get(2);
			long addExp = 0;
			TaskPrize[] prise = task.getPrizes();
			if (prise != null) {
				for (TaskPrize tp : prise) {
					if (tp instanceof TaskPrizeOfExp) {
						addExp = ((TaskPrizeOfExp)tp).getExp(player);
						break;
					}
				}
			}
			tasks[i].setRewardDes(String.format(rewardDes, jrm.getAddXiulian() + "", jrm.getAddJiazuZiyuan()/1000 + "", addExp + ""));
			TaskTarget[] tg = task.getTargets();
			String des4Tar = "";
			int targetLevel = -1;
			if (tg != null && tg.length > 0) {
				des4Tar = tg[0].gettarDes();
				targetLevel = tg[0].gettarLevel();
			}
			tasks[i].setTargetDes(des4Tar);
			tasks[i].setTargetLevel(targetLevel);
		}
		int[] tempCom = JiazuManager2.instance.getAlreadyCompTaskNumAndMaxNum(player);
		String str = String.format(JiazuManager2.instance.translate.get(1), BillingCenter.得到带单位的银两(JiazuManager2.instance.base.getCost4RefreshTask()));
		JIAZU_TASK_WINDOW_OPEN_RES resp = new JIAZU_TASK_WINDOW_OPEN_RES(GameMessageFactory.nextSequnceNum(), jm.getJiazuSalary(), str, tempCom[0]-tempCom[1], tempCom[0], tasks);
		player.addMessageToRightBag(resp);
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
