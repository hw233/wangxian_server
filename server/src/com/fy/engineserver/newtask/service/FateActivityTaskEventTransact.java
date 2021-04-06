package com.fy.engineserver.newtask.service;

import com.fy.engineserver.activity.fateActivity.FateManager;
import com.fy.engineserver.activity.fateActivity.FateTemplate;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.sprite.Player;

import static com.fy.engineserver.datasource.language.Translate.*;

/**
 * 仙缘任务
 *
 */
public class FateActivityTaskEventTransact extends AbsTaskEventTransact {

	@Override
	public String[] getWannaDealWithTaskNames(Taskoperation action) {
		switch (action) {
		case accept:
//			return new String[] {"国内仙缘(15体)","国外仙缘(20体)","国内仙缘(30体)","国内仙缘(60体)","国外仙缘(40体)","国外仙缘(80体)",
//					"国内论道(15体)","国外论道(20体)","国内论道(30体)","国内论道(60体)","国外论道(40体)","国外论道(80体)"};
			return new String[] {国内仙缘15体,国外仙缘20体,国内仙缘30体,国内仙缘60体,国外仙缘40体,国外仙缘80体,
					国内论道15体,国外论道20体,国内论道30体,国内论道60体,国外论道40体,国外论道80体};
		case drop:
//			return new String[] {"国内仙缘","国外仙缘","国内论道","国外论道"};
			return new String[] {国内仙缘15体,国外仙缘20体,国内仙缘30体,国内仙缘60体,国外仙缘40体,国外仙缘80体,
					国内论道15体,国外论道20体,国内论道30体,国内论道60体,国外论道40体,国外论道80体};
		};
		return null;
	}

	public void init() {

	}

	@Override
	public void handleAccept(Player player, Task task, Game game) {
		
		if(FateManager.logger.isWarnEnabled())
			FateManager.logger.warn("[玩家接受任务] ["+task.getName()+"] ["+player.getLogString()+"]");
		String[] taskNames = getWannaDealWithTaskNames(Taskoperation.accept);
		if (taskNames != null) {
			for (String taskName : taskNames) {
				FateManager.logger.error("["+player.getLogString()+"] [前 ]["+taskName+"] ["+task.getName()+"]");
				if (task.getName().equals(taskName)) {
					try {
						FateManager.getInstance().beginActivity(player, task.getId());
					} catch (Exception e) {
						FateManager.logger.error("[接取仙缘论道异常] ["+player.getLogString()+"]",e);
					}
					FateManager.logger.error("["+player.getLogString()+"] [后 ]["+taskName+"]");
					break;
				}
			}
		}
	}

	@Override
	public void handleDone(Player player, Task task, Game game) {
		
	}

	@Override
	public void handleDeliver(Player player, Task task, Game game) {

	}

	@Override
	public void handleDrop(Player player, Task task, Game game) {
		
		if(FateManager.logger.isWarnEnabled())
			FateManager.logger.warn("drop"+task.getName());
		String[] taskNames = getWannaDealWithTaskNames(Taskoperation.drop);
		if (taskNames != null) {
			for (String taskName : taskNames) {
				if (task.getName().equals(taskName)) {
					
					long id = task.getId();
					if(FateManager.logger.isDebugEnabled())
						FateManager.logger.debug("[放弃任务id:"+id+"] ["+player.getLogString()+"] []");
					FateTemplate ft = FateManager.getInstance().getMap().get(id);
					if(ft == null){
						ft = FateManager.getInstance().getMap2country().get(player.getCountry()).get(id);
					}
					if(ft != null){
						//国内仙缘  论道 四种
						byte activityType = ft.getType();
						FateManager.getInstance().cancleActivity(player, activityType,true);
						if(FateManager.logger.isWarnEnabled())
							FateManager.logger.warn("[放弃任务id:"+id+"] ["+player.getLogString()+"] [成功] [类型:"+activityType+"]");
					}else{
						if(FateManager.logger.isWarnEnabled())
							FateManager.logger.warn("[放弃任务id:"+id+"] ["+player.getLogString()+"] [没有仙缘模板]");
					}
//					if(task.getName().equals("国内仙缘")){
//						FateManager.getInstance().cancleActivity(player, FateActivityType.国内仙缘.type,true);
//					}else if(task.getName().equals("国外仙缘")){
//						FateManager.getInstance().cancleActivity(player, FateActivityType.国外仙缘.type,true);
//					}else if(task.getName().equals("国内论道")){
//						FateManager.getInstance().cancleActivity(player, FateActivityType.国内论道.type,true);
//					}else if(task.getName().equals("国外论道")){
//						FateManager.getInstance().cancleActivity(player, FateActivityType.国外论道.type,true);
//					}
					break;
				}
			}
		}
	
	}
}
