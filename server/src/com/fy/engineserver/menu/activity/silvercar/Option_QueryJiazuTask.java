package com.fy.engineserver.menu.activity.silvercar;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.activity.silvercar.SilvercarManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuFunction;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_UseCancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;

public class Option_QueryJiazuTask extends Option implements NeedCheckPurview {

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		try {
			TaskManager taskManager = TaskManager.getInstance();
			SilvercarManager manager = SilvercarManager.getInstance();
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(36000);
			mw.setTitle(Translate.text_silverCar_017);
			mw.setDescriptionInUUB(Translate.text_silverCar_018);
			// mw.setDescriptionInUUB("---运镖任务---");
			Option_UseCancel oc = new Option_UseCancel();
			oc.setText(Translate.text_364);
			oc.setColor(0xffffff);
			List<Option> options = new ArrayList<Option>();
			// 判断家族运镖
			
			if (player.getJiazuId() > 0) {
				Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
				if (jiazu != null) {
					if (TimeTool.instance.isSame(jiazu.getLastJiazuSilverCartime(), SystemTime.currentTimeMillis(), TimeDistance.DAY)) {
						player.sendError(Translate.text_silverCar_016);
						return;
					}
					JiazuMember member = JiazuManager.getInstance().getJiazuMember(player.getId(), player.getJiazuId());
					
					if (JiazuTitle.hasPermission(member.getTitle(), JiazuFunction.release_silvercar)) {// 有权限的才能看到任务
						//家族镖车任务
						Task task = null;
						try {
							if (jiazu.getLevel() <= 10) {
								task = taskManager.getTaskGroupByGroupName(manager.getJiazuCarTaskGroupName()).get(0);
							} else if (jiazu.getLevel() <= 20) {
								task = taskManager.getTaskGroupByGroupName(manager.getJiazuCarTaskGroupName()).get(1);
							} else {
								task = taskManager.getTaskGroupByGroupName(manager.getJiazuCarTaskGroupName()).get(2);
							}
						} catch (Exception e) {
							task = taskManager.getTaskGroupByGroupName(manager.getJiazuCarTaskGroupName()).get(0);
						}
						CompoundReturn cr = player.takeOneTask(task, false, null);
						if (cr.getIntValue() == 0) {
							Option_Car_Task_Jiazu car_Task = new Option_Car_Task_Jiazu();
							car_Task.setText(task.getName());
							car_Task.setTaskName(task.getName());
							options.add(car_Task);
						}
					}
				}
				options.add(oc);
				mw.setOptions(options.toArray(new Option[0]));
				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
				player.addMessageToRightBag(res);
			}
		} catch (Exception e) {
			SilvercarManager.logger.error("[家族运镖]", e);
		}
	}

	// 此处让所有人都看到,让大家知道有这么个东西
	@Override
	public boolean canSee(Player player) {
		if(player.getLevel() < WindowManager.LMLV){
			return false;
		}
		if (player.getJiazuId() <= 0) {
			return false;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null || jiazu.getLevel() <= 2) {
			return false;
		}
		return true;
	}
}
