package com.fy.engineserver.core;

import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.career.SkillInfo;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NEW_QUERY_CAREER_INFO_BY_ID_RES;
import com.fy.engineserver.message.QUERY_CAREER_INFO_BY_ID_RES;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.AbsTaskEventTransact;
import com.fy.engineserver.newtask.service.Taskoperation;
import com.fy.engineserver.sprite.Player;

public class NewPlayerLeadTaskEvent extends AbsTaskEventTransact {

	private NewPlayerLeadDataManager dataManager;

	@Override
	public String[] getWannaDealWithTaskNames(Taskoperation action) {
		switch (action) {
		case accept:
			return dataManager.getAcceptTaskLead().keySet().toArray(new String[0]);
		case done:
			return dataManager.getCompleteTaskLead().keySet().toArray(new String[0]);
		case deliver:
			return dataManager.getDeliverTaskLead().keySet().toArray(new String[0]);
		default:
			break;
		}
		return null;
	}

	@Override
	public void handleAccept(Player player, Task task, Game game) {
		dataManager.sendLead2Player(player, dataManager.getAcceptTaskLead().get(task.getName()));
		//自动学习变身技能
		if(player.getCareer() == 5 && task.getId() == 100833 && player.getSkillLevel(player.bianShenSkillId) == 0){
			player.addSkillLevel(player.bianShenSkillId);
			Skill skill = CareerManager.getInstance().getSkillById(player.bianShenSkillId);
			if(skill != null){
				SkillInfo si = new SkillInfo();
				si.setInfo(player, skill);
				NEW_QUERY_CAREER_INFO_BY_ID_RES qciRes = new NEW_QUERY_CAREER_INFO_BY_ID_RES(GameMessageFactory.nextSequnceNum(), si);
				player.addMessageToRightBag(qciRes);
			}
			player.noticeShouKuiInfo("接任务-"+task.getName_stat());
		}
	}

	@Override
	public void handleDone(Player player, Task task, Game game) {
		dataManager.sendLead2Player(player, dataManager.getCompleteTaskLead().get(task.getName()));
	}

	@Override
	public void handleDeliver(Player player, Task task, Game game) {
		dataManager.sendLead2Player(player, dataManager.getDeliverTaskLead().get(task.getName()));
	}

	@Override
	public void handleDrop(Player player, Task task, Game game) {

	}

	public NewPlayerLeadDataManager getDataManager() {
		return dataManager;
	}

	public void setDataManager(NewPlayerLeadDataManager dataManager) {
		this.dataManager = dataManager;
	}

	public void init() {

	}
}
