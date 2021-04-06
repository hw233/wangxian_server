package com.fy.engineserver.menu.activity.dig;

import java.util.List;
import java.util.Random;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

public class Option_Dig_Task extends Option implements NeedCheckPurview{
	public static int needSilver = 10000;
//	private String taskName;
	private String collectionName;
	
	static Random RANDOM = new Random();

	@Override
	public void doSelect(Game game, Player player) {
		List<Task> tasks= TaskManager.getInstance().getTaskCollectionsByName(collectionName);
		if(tasks!=null&&!tasks.isEmpty()){
			Task task = tasks.get(RANDOM.nextInt(tasks.size()));
			CompoundReturn cr = player.takeOneTask(task, true, null);
			TaskSubSystem.logger.error(player.getLogString() + "[接取挖宝任务] [{}] [是否接取成功{}] [结果:{}]", new Object[] { task.getName(), cr.getBooleanValue(), TaskSubSystem.getInstance().getInfo(cr.getIntValue()) });
			if (!cr.getBooleanValue()) {
				player.sendError(TaskSubSystem.getInstance().getInfo(cr.getIntValue()));
			} else {
				if (player.getSilver() >= needSilver) {
					try {
						BillingCenter.getInstance().playerExpense(player, needSilver, CurrencyType.YINZI, ExpenseReasonType.接取任务, "挖宝");
						player.sendError(Translate.translateString(Translate.消耗银子提示, new String[][]{{Translate.STRING_1,BillingCenter.getInstance().得到带单位的银两(needSilver)}}));
					} catch (Exception e) {
						TaskSubSystem.logger.error(player.getLogString() + "[扣费异常]");
						e.printStackTrace();
					}
					player.addTaskByServer(task);
				} else {
					player.sendError(Translate.银子不足);
				}
				
			}
		}else{
			TaskSubSystem.logger.error(player.getLogString() + "[任务集合不存在:{}]", new Object[] { collectionName });
			return;
		}
//		Task task = TaskManager.getInstance().getTask(taskName);
//		if (task == null) {
//			TaskSubSystem.logger.error(player.getLogString() + "[任务不存在:{}]", new Object[] { taskName });
//			return;
//		}
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	@Override
	public boolean canSee(Player player) {
		if(player.getLevel() < WindowManager.LMLV){
			return false;
		}
		// TODO Auto-generated method stub
		return true;
	}

//	public String getTaskName() {
//		return taskName;
//	}
//
//	public void setTaskName(String taskName) {
//		this.taskName = taskName;
//	}

}
