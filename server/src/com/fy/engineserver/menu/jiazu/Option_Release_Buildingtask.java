package com.fy.engineserver.menu.jiazu;

import java.util.List;
import java.util.Random;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.septbuilding.entity.SeptBuildingEntity;
import com.fy.engineserver.septstation.SeptStationMapTemplet;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

/***
 * 家族接取发布的建设任务
 * 
 * 
 */
public class Option_Release_Buildingtask extends Option implements NeedCheckPurview {

	static Random RANDOM = new Random();

	@Override
	public void doSelect(Game game, Player player) {
		try {
			if (player.inSelfSeptStation()) {
				Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
				SeptBuildingEntity sbe = jiazu.getInbuildingEntity();
				if (sbe != null) {
					String collectionName = SeptStationMapTemplet.getInstance().getBuildingTaskCollection();
					if (JiazuSubSystem.logger.isInfoEnabled()) {
						JiazuSubSystem.logger.info("[完成发布家族建设任务][任务集合:{}]", new Object[] { collectionName });
					}

					List<Task> tasks = TaskManager.getInstance().getTaskCollectionsByName(collectionName);
					if (tasks != null && !tasks.isEmpty()) {
						Task task = tasks.get(RANDOM.nextInt(tasks.size()));
						CompoundReturn cr = player.takeOneTask(task, false, null);
						if (JiazuSubSystem.logger.isInfoEnabled()) {
							JiazuSubSystem.logger.info("[完成发布家族建设任务结果:{}]", new Object[] { cr.getIntValue() });
						}
						if (cr.getBooleanValue()) {
							player.addTaskByServer(task);
						} else {
							player.sendError(TaskSubSystem.getInstance().getInfo(cr.getIntValue()));
						}
					} else {
						JiazuSubSystem.logger.error("[完成发布家族建设任务][任务不存在]");
						return;
					}
				} else {
					player.sendError(Translate.text_jiazu_179);
					return;
				}
			} else {
				JiazuSubSystem.logger.error("[完成发布家族建设任务][不在自己家族]");
			}
		} catch (Exception e) {
			JiazuSubSystem.logger.error("[完成发布家族建设任务异常]", e);
		}
	}

	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		if (player.inSelfSeptStation()) {
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			SeptBuildingEntity sbe = jiazu.getInbuildingEntity();
			if (sbe != null) {
				return true;
			}
		}
		return false;
	}
}
