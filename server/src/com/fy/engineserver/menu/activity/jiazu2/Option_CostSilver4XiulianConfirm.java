package com.fy.engineserver.menu.activity.jiazu2;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.instance.JiazuMember2;
import com.fy.engineserver.jiazu2.instance.JiazuXiulian;
import com.fy.engineserver.jiazu2.manager.JiazuEntityManager2;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.jiazu2.model.JiazuSkillModel;
import com.fy.engineserver.jiazu2.model.PracticeModel;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.JIAZU_JINENG_RES;
import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet.BuildingType;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Player;

public class Option_CostSilver4XiulianConfirm extends Option{
	
	private int skillId;
	private int learnTimes;
	
	
	@Override
	public void doSelect(Game game, Player player) {
		JiazuEntityManager2.instance.upgradePratice(player, skillId, learnTimes, true);
		JiazuSkillModel jiazuSkills = new JiazuSkillModel();
		JiazuMember2 jm2 = JiazuEntityManager2.instance.getEntity(player.getId());
		JiazuXiulian xiul = jm2.getJiazuXiulianBySkillId(skillId);
		int currentLevel = 0;
		int currentExp = 0;
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		SeptStation station = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
		if (xiul != null) {
			currentLevel = xiul.getSkillLevel();
			currentExp = (int) xiul.getSkillExp();
		}
		PracticeModel pm = JiazuManager2.instance.praticeMaps.get(skillId);
		JiazuXiulian jxl = jm2.getJiazuXiulianBySkillId(pm.getSkillId());
		String description = "";
		int skLevel = 0;
		if (jxl != null) {
			skLevel = jxl.getSkillLevel();
		}
		if (pm != null ) {
			description = pm.getInfoShow(skLevel);
		}
		jiazuSkills.setCurrentLevelDes(description);
		jiazuSkills.setSkillId(pm.getSkillId());
		jiazuSkills.setSkillName(pm.getSkillName());
		jiazuSkills.setIcon(pm.getIcon());
		int weaponLevel = station.getBuildingLevel(BuildingType.武器坊);
		int armorLevel = station.getBuildingLevel(BuildingType.防具坊);
		PracticeModel model = JiazuManager2.instance.praticeMaps.get(skillId);
		int canLearnLevel = model.getCanLearnLevel(weaponLevel, armorLevel);
		jiazuSkills.setMaxLevel(canLearnLevel);
		jiazuSkills.setCurrentLevel(currentLevel);
		jiazuSkills.setCurrentExp(currentExp);
		jiazuSkills.setNeedExp((int) pm.getExp4next()[currentLevel]);
		jiazuSkills.setCostType(pm.getCostSiliverType()[currentLevel]);
		jiazuSkills.setCostNum(JiazuManager2.instance.base.getCostSiliverNums());
		JIAZU_JINENG_RES resp = new JIAZU_JINENG_RES(GameMessageFactory.nextSequnceNum(), jm2.getPracticeValue(), jiazuSkills);
		player.addMessageToRightBag(resp);
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public int getLearnTimes() {
		return learnTimes;
	}

	public void setLearnTimes(int learnTimes) {
		this.learnTimes = learnTimes;
	}
	
	
}
