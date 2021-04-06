package com.fy.engineserver.datasource.career;

import java.util.Arrays;

import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.datasource.skill.passiveskills.IncreaseTeShuShuXingPassiveSkill;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;

/**
 * 职业
 * 
 * 每个职业中，包含一定数目的技能， 所有职业，是技能集合的一个划分
 * 
 * 
 * 
 * 
 */
public class Career {
	
	public static final int[][]  newxinfaskill = new int[][]{{3000,3100,3200},{4000,4100,4200},{5000,5100,5200},{6000,6100,6200},{7000,7100,7200}};
	
	public static String cleanArticleName = Translate.洗髓精华;

	/**
	 * 职业的编号，从0开始，0为无门无派 1修罗 2影魅 3仙心 4九黎
	 */
	protected int id;

	/**
	 * 职业的名词
	 */
	protected String name;
	
	/**
	 * 允许使用的武器类型
	 */
	protected int[] weaponTypeLimit = new int[0];
//
//	/**
//	 * 允许使用的装备类型
//	 */
//	protected int[] equipmentMaterialTypeLimit = new int[0];

	/**
	 * 是否允许男性加入
	 */
	protected boolean enableMale;

	/**
	 * 是否允许女性加入
	 */
	protected boolean enableFemale;

	//职业的基本技能
	public Skill[] basicSkills = new Skill[0];
	
	//职业发展线路，正常情况下，一个职业都有2条发展线路，在数据结构上我们设计成多条
	//为将来做其他游戏留个后路
	public CareerThread threads[] = new CareerThread[1];

	//职业的怒气技能
	public Skill[] nuqiSkills = new Skill[0];

	//职业的心法技能
	public Skill[] xinfaSkills = new Skill[0];
	
	public Skill[] bianShenSkills = new Skill[0];
	
	/**
	 * 国王技能，不能升级，只有国王可以使用
	 */
	public Skill[] kingSkills = new Skill[0];
	
	public Skill[] getKingSkills() {
		return kingSkills;
	}

	public void setKingSkills(Skill[] kingSkills) {
		this.kingSkills = kingSkills;
	}

	public CareerThread[] getCareerThreads(){
		return threads;
	}
	
	public CareerThread getCareerThread(int index){
		if(index < 0 || index >= threads.length){
			return null;
		}
		return threads[index];
	}
	public Skill getSkillById(int skillId){
		if(basicSkills != null){
			for(int i = 0; i < basicSkills.length; i++){
				Skill skill = basicSkills[i];
				if(skill != null && skill.getId() == skillId){
					return skill;
				}
			}
		}
		if(threads != null){
			for(int ii = 0; ii <threads.length; ii++){
				CareerThread ct = threads[ii];
				if(ct != null){
					Skill[] skills = ct.skills;
					for(int i = 0; i < skills.length; i++){
						Skill skill = skills[i];
						if(skill != null && skill.getId() == skillId){
							return skill;
						}
					}
				}
			}
			
		}
		if(nuqiSkills != null){
			for(int i = 0; i < nuqiSkills.length; i++){
				Skill skill = nuqiSkills[i];
				if(skill != null && skill.getId() == skillId){
					return skill;
				}
			}
		}
		if(xinfaSkills != null){
			for(int i = 0; i < xinfaSkills.length; i++){
				Skill skill = xinfaSkills[i];
				if(skill != null && skill.getId() == skillId){
					return skill;
				}
			}
		}
		if(kingSkills != null){
			for(int i = 0; i < kingSkills.length; i++){
				Skill skill = kingSkills[i];
				if(skill != null && skill.getId() == skillId){
					return skill;
				}
			}
		}
		if(bianShenSkills != null){
			for(int i = 0; i < bianShenSkills.length; i++){
				Skill skill = bianShenSkills[i];
				if(skill != null && skill.getId() == skillId){
					return skill;
				}
			}
		}
		return null;
	}

	public Skill[] getBasicSkills() {
		return basicSkills;
	}

	public void setBasicSkills(Skill[] basicSkills) {
		this.basicSkills = basicSkills;
	}

	public Skill[] getNuqiSkills() {
		return nuqiSkills;
	}

	public void setNuqiSkills(Skill[] nuqiSkills) {
		this.nuqiSkills = nuqiSkills;
	}

	public Skill[] getXinfaSkills() {
		return xinfaSkills;
	}

	public void setXinfaSkills(Skill[] xinfaSkills) {
		this.xinfaSkills = xinfaSkills;
	}

	public Skill[] getBianShenSkills() {
		return this.bianShenSkills;
	}

	public void setBianShenSkills(Skill[] bianShenSkills) {
		this.bianShenSkills = bianShenSkills;
	}

	/**
	 * 通过一个技能ID，获得包含此技能的职业发展线路
	 * 如果没有发现线路包含此技能，返回null
	 * @param skillId
	 * @return
	 */
	public CareerThread getCareerThreadBySkillId(int skillId){
		for(int i = 0 ; i < threads.length ; i++){
			if(threads[i] != null && threads[i].getSkillIndexOf(skillId) != -1){
				return threads[i];
			}
		}
		return null;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[] getWeaponTypeLimit() {
		return weaponTypeLimit;
	}

	public void setWeaponTypeLimit(int[] weaponTypeLimit) {
		this.weaponTypeLimit = weaponTypeLimit;
	}
//
//	public int[] getEquipmentMaterialTypeLimit() {
//		return equipmentMaterialTypeLimit;
//	}
//
//	public void setEquipmentMaterialTypeLimit(int[] equipmentMaterialTypeLimit) {
//		this.equipmentMaterialTypeLimit = equipmentMaterialTypeLimit;
//	}

	public boolean isEnableMale() {
		return enableMale;
	}

	public void setEnableMale(boolean enableMale) {
		this.enableMale = enableMale;
	}

	public boolean isEnableFemale() {
		return enableFemale;
	}

	public void setEnableFemale(boolean enableFemale) {
		this.enableFemale = enableFemale;
	}

	public CareerThread[] getThreads() {
		return threads;
	}

	public void setThreads(CareerThread[] threads) {
		this.threads = threads;
	}

	/**
	 * 判断角色是否能学习或者升级某个技能，不考虑角色是否拥有多余技能点<br>
	 * 1，如果玩家已经学过此技能，判断技能等级是否达到最高级<br>
	 * 2，否则，判断玩家是否满足学习此技能的条件
	 * 如果用书学习那么此技能可以从零级开始，如果是玩家手动点击学习那么此技能必须从1开始
	 * @param player
	 * @param skillIndex
	 * @return 返回null时为能学习技能，否则返回的字符串为不能学习的原因
	 */
	public String isUpgradable(Player player, int skillId, boolean learnByBook) {
		
		Skill skill = this.getSkillById(skillId);
		if(skill == null){
			return Translate.您不能学习这项技能;
		}
		
//		if(kingSkills != null){
//			for(Skill s : kingSkills){
//				if(s != null && s.getId() == skillId){
//					return Translate.国王技能不能升级;
//				}
//			}
//		}
		
		Skill[] skills = getXinfaSkills();
		int xinfaIndex = -1;
		if (skills != null) {
			for (int i = 0; i < skills.length; i++) {
				Skill s = skills[i];
				if (s != null && s.getId() == skillId) {
					xinfaIndex = i;
					break;
				}
			}
		}
		boolean 注魂 = false;
		byte[] tempArr = new byte[12]; 
		if (xinfaIndex >= 5 && xinfaIndex < 17) {
			if (player.getXinfaLevels()[xinfaIndex] >= 1) {
				注魂 = true;
			}
			int upXinFaNum = 0;
			for (int i = 5; i < 17; i++) {
				tempArr[i-5] = player.getXinfaLevels()[i];
			}
			for (int i = 5; i < 17; i++) {
				if (player.getXinfaLevels()[i] > 1) {
					if (xinfaIndex == i) {
						upXinFaNum = 0;
						tempArr[i-5] = (byte) (tempArr[i-5] + 1);
						break;
					}else {
						upXinFaNum++;
					}
				}
			}
			if (upXinFaNum >= IncreaseTeShuShuXingPassiveSkill.skill_num) {
				return Translate.新心法只能学3个;
			}
		}
		if(xinfaIndex >= 17){
			int upXinFaNum = 0;
			for(int i = 17; i < 40; i++) {
				if (player.getXinfaLevels()[i] > 1) {
					if (xinfaIndex == i) {
						upXinFaNum = 0;
						break;
					}else {
						upXinFaNum++;
					}
				}
			}
			if (upXinFaNum >= IncreaseTeShuShuXingPassiveSkill.skill_num2) {
				return Translate.新新心法只能学10个;
			}
		}
		
		byte level = player.getSkillLevel(skillId);

		if(level >= skill.getMaxLevel()){
			return Translate.技能已经到达最高级;
		}
	
		Skill tempSkill ;
		int temp = 0;
		int robberyLevel = 0;
		boolean isNewXinfa0level = false;
		for(int i=0; i<newxinfaskill.length; i++){
			for(int j=1; j<newxinfaskill[i].length; j++){
				temp = j;
				if(newxinfaskill[i][j] == skillId){	
					isNewXinfa0level = true;
					for(int k=temp-1; k>=0; k--){						//判断之前心法有没有学满,最初心法不参与此判断
						tempSkill = this.getSkillById(skillId);
						if(player.getSkillLevel(newxinfaskill[i][k]) < tempSkill.getMaxLevel()){
							return Translate.上一次心法没学满;
						}
					}
					//天劫是否通过判断
					if(player.getLevel() < TransitRobberyManager.openLevel){			//没达到开启天劫技能等级不能学
						return Translate.天劫未通过;
					} else {					
						TransitRobberyEntity trentity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(player.getId());
						robberyLevel = trentity.getCurrentLevel();
						if(robberyLevel < 9 && temp == 1 && level >= RobberyConstant.diyitaoxinfa[robberyLevel]){		//通过天劫数不足以继续学习第一套天劫
							return Translate.天劫未通过;
						} else if(temp == 2 && level >= RobberyConstant.diertaoxinfa[robberyLevel-8]){ 	//通过天劫数不足以继续学习第二套天劫
							if(level < 40) {
								return Translate.天劫未通过;
							} else if(trentity.getFeisheng() < 1) {
								return Translate.飞升心法;
							}
						}
					}
				}
			}
		}
		
		//判断money
		if(skill.getNeedMoney() == null || skill.getNeedMoney().length <= level){
			return Translate.系统配置错误1;
		}
		if(skill.getNeedExp() == null || skill.getNeedExp().length <= level){
			return Translate.系统配置错误2;
		}
		if(skill.getNeedPlayerLevel() == null || skill.getNeedPlayerLevel().length <= level){
			return Translate.系统配置错误3;
		}
		if(skill.getNeedPoint() == null || skill.getNeedPoint().length <= level){
			return Translate.系统配置错误4;
		}
		if(skill.getNeedYuanQi() == null || skill.getNeedYuanQi().length <= level){
			return Translate.系统配置错误5;
		}
		int needPlayerLevel = skill.getNeedPlayerLevel()[level];
		if(player.getSoulLevel() < needPlayerLevel){
			return Translate.级别不足不能学习此技能;
		}
		boolean nuqiSkill = false;
		if(skill instanceof ActiveSkill){
			nuqiSkill = ((ActiveSkill)skill).isNuqiFlag();
		}
		if(nuqiSkill){
			if(!learnByBook){
				return Translate.怒气技能需要技能书才能学习;
			}
		}else{
			if(!learnByBook && level <= 0 && !isNewXinfa0level){
				return Translate.技能会自动学习或需要技能书;
			}
			if(learnByBook && level > 0){
				return Translate.技能书学习限制;
			}
		}

		int needMoney = skill.getNeedMoney()[level];
		long needExp = skill.getNeedExp()[level];

		int needPoint = skill.getNeedPoint()[level];
		int needYuanQi = skill.getNeedYuanQi()[level];
		if(!player.bindSilverEnough(needMoney)){
			return Translate.您今天可使用的绑银不足;
		}
		if(player.getExp() < needExp){
			return Translate.您没有足够的经验;
		}

		if(!learnByBook){
			if(player.getUnallocatedSkillPoint() < needPoint){
				return Translate.您没有足够的技能点;
			}
		}
		if(player.getEnergy() < needYuanQi){
			return Translate.您没有足够的元气点;
		}
		if(注魂) {
			try {
				Arrays.sort(tempArr);
				if (PlayerAimManager.logger.isDebugEnabled()) {
					PlayerAimManager.logger.debug("[目标系统] [统计注魂] [数组:" + Arrays.toString(tempArr));
				}
//				if (tempArr != null && tempArr.length > 2 && (tempArr[tempArr.length-3]) > 1) {
//					EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.任意三个注魂等级, (tempArr[tempArr.length-3])});
//					EventRouter.getInst().addEvent(evt3);
//				}
//				EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.注魂次数, 1L});
//				EventRouter.getInst().addEvent(evt3);
			} catch (Exception eex) {
				PlayerAimManager.logger.error("[目标系统] [统计新心法异常] [" + player.getLogString() + "]", eex);
			}
		}
		return null;
		
	}

	public void init() {
		// 当出现错误时，尽量不返回null，而是返回此错误技能
	}

}
