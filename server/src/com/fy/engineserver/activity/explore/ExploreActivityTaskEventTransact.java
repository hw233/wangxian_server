package com.fy.engineserver.activity.explore;

import static com.fy.engineserver.datasource.language.Translate.国内寻宝;
import static com.fy.engineserver.datasource.language.Translate.国内寻宝15体;
import static com.fy.engineserver.datasource.language.Translate.国内寻宝30体;
import static com.fy.engineserver.datasource.language.Translate.国内寻宝60体;
import static com.fy.engineserver.datasource.language.Translate.国外寻宝;
import static com.fy.engineserver.datasource.language.Translate.国外寻宝20体;
import static com.fy.engineserver.datasource.language.Translate.国外寻宝40体;
import static com.fy.engineserver.datasource.language.Translate.国外寻宝80体;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.AbsTaskEventTransact;
import com.fy.engineserver.newtask.service.Taskoperation;
import com.fy.engineserver.sprite.Player;

/**
 * 寻宝任务
 *
 */
public class ExploreActivityTaskEventTransact extends AbsTaskEventTransact {

	
	@Override
	public String[] getWannaDealWithTaskNames(Taskoperation action) {
		switch (action) {
		case accept:
			return new String[] {国内寻宝15体,国外寻宝20体,国内寻宝30体,国外寻宝40体,国内寻宝60体,国外寻宝80体};
		case drop:
//			return new String[] {"国内寻宝(15体)","国外寻宝(20体)","国内寻宝(30体)","国外寻宝(40体)","国内寻宝(60体)","国外寻宝(80体)"};
			return new String[] {国内寻宝15体,国外寻宝20体,国内寻宝30体,国外寻宝40体,国内寻宝60体,国外寻宝80体};
		};
		return null;
	}
	
	
	public void init() {

	}

	@Override
	public void handleAccept(Player player, Task task, Game game) {
		
		String[] taskNames = getWannaDealWithTaskNames(Taskoperation.accept);
		if (taskNames != null) {
			for (String taskName : taskNames) {
				if (task.getName().equals(taskName)) {
					try {
						
						if(task.getName().contains(国内寻宝)){
							ExploreEntity ee = ExploreManager.getInstance().randomGetExploreEntity((byte)0, player);
							if(ee != null){
								ee.setTaskId(task.getId());
								player.setExploreEntity(ee);
								
							}else{
								ExploreManager.logger.error("[接取国内寻宝错误] ["+player.getLogString()+"] []");
							}

						}else{
							ExploreEntity ee = ExploreManager.getInstance().randomGetExploreEntity((byte)1, player);
							if(ee != null){
								ee.setTaskId(task.getId());
								player.setExploreEntity(ee);
							}else{
								ExploreManager.logger.error("[接取国外寻宝错误] ["+player.getLogString()+"] []");
							}
					
						}
						if(ExploreManager.logger.isInfoEnabled()){
							ExploreManager.logger.info("[接到寻宝任务] [生成寻宝实体] ["+player.getLogString()+"] ["+task.getName()+"]");
						}
					} catch (Exception e) {
						ExploreManager.logger.error("[接到寻宝任务] [生成寻宝实体] ["+player.getLogString()+"] ["+task.getName()+"]",e);
					}
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
		
		String[] taskNames = getWannaDealWithTaskNames(Taskoperation.drop);
		if (taskNames != null) {
			for (String taskName : taskNames) {
				if (task.getName().equals(taskName)) {
					ExploreManager.logger.warn("[放弃寻宝任务] ["+player.getLogString()+"] ["+task.getName()+"]");
					if(task.getName().contains(国内寻宝)){
						boolean bln = ExploreManager.getInstance().giveUpExplore(player);
						ExploreManager.logger.warn("[删除国内寻宝任务] [] ["+task.getName()+"] [物品全删:"+bln+"]");
					}else if(task.getName().contains(国外寻宝)){
						boolean bln = ExploreManager.getInstance().giveUpExplore(player);
						ExploreManager.logger.warn("[删除国外寻宝任务] [] ["+task.getName()+"] [物品全删:"+bln+"]");
					}
					break;
				}
			}
		}
	
	}

}
