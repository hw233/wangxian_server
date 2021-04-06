package com.fy.engineserver.menu.activity.jiazu2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.fy.engineserver.message.JIAZU_JINENG_INFO_RES;
import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet.BuildingType;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Player;

public class Option_Open_JiazuXiulian extends Option {
	/**
	 * 1武器坊   0防具坊
	 */
	private byte buildType;
	
	@Override
	public void doSelect(Game game, Player player) {
		String result = JiazuManager2.checkJiazu(player);
		if (result != null) {
			player.sendError(result);
			return;
		}
		
		JiazuMember2 jm2 = JiazuEntityManager2.instance.getEntity(player.getId());
		long xiulianZhi = jm2.getPracticeValue();
		List<JiazuSkillModel> temp = new ArrayList<JiazuSkillModel>();
		JiazuSkillModel[] jiazuSkills = null;
		Iterator<Integer> ite = JiazuManager2.instance.praticeMaps.keySet().iterator();
//		int index = 0;
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		SeptStation station = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
		while(ite.hasNext()) {
			int skillId = ite.next();
			JiazuXiulian xiul = jm2.getJiazuXiulianBySkillId(skillId);
			int currentLevel = 0;
			int currentExp = 0;
			if (xiul != null) {
				currentLevel = xiul.getSkillLevel();
				currentExp = (int) xiul.getSkillExp();
			}
			PracticeModel pm = JiazuManager2.instance.praticeMaps.get(skillId);
			if (pm.getWeaponShopLevel()[1] > 0 && buildType != 1) {
					continue;
			}
			if (pm.getArmorShopLevel()[1] > 0 && buildType != 0) {
				continue;
			}
			JiazuXiulian jxl = jm2.getJiazuXiulianBySkillId(pm.getSkillId());
			String description = "";
			int skLevel = 0;
			if (jxl != null) {
				skLevel = jxl.getSkillLevel();
			}
			if (pm != null ) {
				description = pm.getInfoShow(skLevel);
			}
			JiazuSkillModel jsm = new JiazuSkillModel();
			jsm.setSkillId(pm.getSkillId());
			jsm.setSkillName(pm.getSkillName());
			jsm.setIcon(pm.getIcon());
			int weaponLevel = station.getBuildingLevel(BuildingType.武器坊);
			int armorLevel = station.getBuildingLevel(BuildingType.防具坊);
			PracticeModel model = JiazuManager2.instance.praticeMaps.get(skillId);
			int canLearnLevel = model.getCanLearnLevel(weaponLevel, armorLevel);
			jsm.setMaxLevel(canLearnLevel);
			jsm.setCurrentLevel(currentLevel);
			jsm.setCurrentExp(currentExp);
			if (currentLevel >= pm.getExp4next().length) {
				currentLevel = pm.getExp4next().length - 1;
				jsm.setCurrentExp((int) pm.getExp4next()[currentLevel]);
			} 
			jsm.setNeedExp((int) pm.getExp4next()[currentLevel]);
			jsm.setCostType(pm.getCostSiliverType()[currentLevel]);
			jsm.setCostNum(JiazuManager2.instance.base.getCostSiliverNums());
			jsm.setCurrentLevelDes(description);
			temp.add(jsm);
			if (JiazuManager2.logger.isDebugEnabled()) {
				JiazuManager2.logger.debug("[测试协议返回值] [" + player.getLogString() + "] [" + jsm + "]");
			}
//			jiazuSkills[index++] = jsm;
		}
		jiazuSkills = new JiazuSkillModel[temp.size()];
		for (int i=0; i<temp.size(); i++) {
			jiazuSkills[i] = temp.get(i);
		}
		JIAZU_JINENG_INFO_RES resp = new JIAZU_JINENG_INFO_RES(GameMessageFactory.nextSequnceNum(), xiulianZhi, buildType, jiazuSkills, JiazuManager2.instance.base.getCostXiulian());
		player.addMessageToRightBag(resp);
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public byte getBuildType() {
		return buildType;
	}

	public void setBuildType(byte buildType) {
		this.buildType = buildType;
	}


}
