package com.fy.engineserver.talent;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_TALENT_BUTTON_RES;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.AbsTaskEventTransact;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.Taskoperation;
import com.fy.engineserver.sprite.Player;

public class TalentStartTaskEvent extends AbsTaskEventTransact{

	public String deliverTasks = Translate.赐予仙婴;
	
	@Override
	public String[] getWannaDealWithTaskNames(Taskoperation action) {
		switch (action) {
		case accept:
			break;
		case done:
			break;
		case drop:
			break;
		case deliver:
			return new String[]{deliverTasks};
		}
		return null;
	}

	@Override
	public void handleAccept(Player player, Task task, Game game) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleDeliver(Player player, Task task, Game game) {
		long now = System.currentTimeMillis();
		TalentData data = FlyTalentManager.getInstance().getTalentData(player.getId());
		if(data == null){
			data = new TalentData();
		}
		data.setXyButtonState(1);
		
		long cdUseTime = 0;
		if(data.getCdEndTime() > now){
			cdUseTime = data.getCdEndTime() - now;
		}
		FlyTalentManager.getInstance().saveTalentData(player, data);
		TaskManager.logger.warn("[通知开启仙婴按钮] [任务:{}] [deliverTasks:{}] [cdUseTime:{}] [玩家:{}]",new Object[]{task.getName(),deliverTasks,cdUseTime,player.getLogString()});
		NOTICE_TALENT_BUTTON_RES res = new NOTICE_TALENT_BUTTON_RES(GameMessageFactory.nextSequnceNum(), 1,0, FlyTalentManager.TALENT_SKILL_CD_TIME);
		player.addMessageToRightBag(res);
	}

	@Override
	public void handleDone(Player player, Task task, Game game) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleDrop(Player player, Task task, Game game) {
		// TODO Auto-generated method stub
		
	}

	public void init(){
		
	}
	
}
