package com.fy.engineserver.datasource.skill;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.skill2.GenericSkill;
import com.fy.engineserver.datasource.skill2.GenericSkillManager;
import com.fy.engineserver.hunshi.HunshiManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;

public class NewAuraSkillAgent {
	/** 光环拥有者 */
	private Fighter owner;			
	/** 拥有者所属国家 */
	private byte country;
	/** 生效的光环技能id */
	private int skillId;
	
	public NewAuraSkillAgent(Fighter fight) {
		this.owner = fight;
	}
	
	public void heartBeat(long heartBeatStartTime, long interval, Game game) {
		try {
			if (skillId <= 0) {
				return ;
			}
			if (owner == null) {
				if (Skill.logger.isWarnEnabled()) {
					Skill.logger.warn("[光环技能] [异常] [代理没有owner] [skillId:"+skillId+"] [id:" + owner.getId() + "] [name:" + owner.getName() + "] ");
				}
				return ;
			}
			byte country = -1;
			if (owner instanceof Pet) {
				Pet p = (Pet) owner;
				Player master = p.getMaster();
				if (master == null) {
					return ;
				}
				country = master.getCountry();
			}
			if (country < 0) {
				if (Skill.logger.isDebugEnabled()) {
					Skill.logger.debug("[光环技能] [不生效] [拥有国家异常] [skillId:"+skillId+"] [id:" + owner.getId() + "] [name:" + owner.getName() + "] ");
				}
				return ;
			}
			GenericSkill skill = GenericSkillManager.getInst().maps.get(skillId);
			if (skill == null) {
				if (Skill.logger.isWarnEnabled()) {
					Skill.logger.warn("[光环技能] [异常] [未找到对应宠物技能] [skillId:"+skillId+"] [id:" + owner.getId() + "] [name:" + owner.getName() + "] ");
				}
				return ;
			}
			String templateName = skill.buff.attName;
			if (templateName == null) {
				if (Skill.logger.isWarnEnabled()) {
					Skill.logger.warn("[光环技能] [异常] [buffName为空] [skillId:"+skillId+"] [id:" + owner.getId() + "] [name:" + owner.getName() + "] ");
				}
				return ;
			}
			BuffTemplateManager btm = BuffTemplateManager.getInstance();
			BuffTemplate bt = btm.getBuffTemplateByName(templateName);
			if (bt == null) {
				if (Skill.logger.isWarnEnabled()) {
					Skill.logger.warn("[光环技能] [异常] [buff不存在] [buffname:"+templateName+"] [skillId:"+skillId+"] [id:" + owner.getId() + "] [name:" + owner.getName() + "] ");
				}
				return ;
			}
			int[] value = skill.buff.values;
			int lv = value[0];		//buff等级
			int templateId = bt.getId();
			int rangeType = value[1];		//作用类型   0敌国玩家   （有其他需求以后再说）
			Fighter fs[] = game.getVisbleFighter((LivingObject)owner, false);		
			for(int i = 0 ; i < fs.length ; i++){
				boolean b = false;
				if (rangeType == 0) {				//只作用于视野范围内不同国家的玩家
					if (fs[i] instanceof Player) {	
						Player player = (Player) fs[i];
						if (player.getCountry() != country) {
							b = true;
						}
						if (b) {
							b = false;
							float dx = owner.getX() - fs[i].getX();
							float dy = owner.getY() - fs[i].getY();
							if(dx >= -NewAuraBuff.range[0] && dx <= NewAuraBuff.range[0] && dy >= -NewAuraBuff.range[1] && dy <= NewAuraBuff.range[1]){
								b = true;
							}
						}
						if (b) {
							Buff buff = fs[i].getBuff(templateId);
							if (buff == null || buff.getLevel() < lv) {
								buff = bt.createBuff(lv);
								buff.setTemplate(bt);
								buff.setGroupId(bt.getGroupId());
								buff.setLevel(lv);
								buff.setInvalidTime(heartBeatStartTime + 600 * 1000L);
								buff.setStartTime(heartBeatStartTime);
								buff.setCauser(owner);
								
								NewAuraBuff ab = new NewAuraBuff(this,buff);
								ab.setCauser(owner);
								ab.setInvalidTime(heartBeatStartTime + 600 * 1000L);
								ab.setStartTime(heartBeatStartTime);
								ab.setIconId(buff.getIconId());
								ab.setLevel(lv);
								ab.setTemplateId(templateId);
								ab.setGroupId(bt.getGroupId());
								fs[i].placeBuff(ab);
//								HunshiManager.getInstance().dealWithInfectSkill(owner, fs[i], buff);
								if (Skill.logger.isDebugEnabled()) {
									Skill.logger.debug("[光环技能] [生效] [加buff] ["+this.getOwner().getId()+"] ["+this.getOwner().getName()+"] [" + player.getLogString() + "]");
								} 
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Skill.logger.warn("[光环技能] [心跳异常] [" + this.getOwner().getId() + "] [" + this.getOwner().getName() + "]", e);
		}
	}
	
	public boolean isValid(Fighter o) {
		Game game = null;
		if (owner instanceof Pet) {
			Pet p = (Pet) owner;
			Player master = p.getMaster();
			if (master == null) {
				return false;
			}
			game = master.getCurrentGame();
			Fighter[] fs = game.getVisbleFighter((LivingObject)owner, false);
			if (fs != null && fs.length > 0) {
				for (Fighter f : fs) {
					if (f.getId() == o.getId()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	
	public Fighter getOwner() {
		return owner;
	}
	public void setOwner(Fighter owner) {
		this.owner = owner;
	}
	public byte getCountry() {
		return country;
	}
	public void setCountry(byte country) {
		this.country = country;
	}



	public int getSkillId() {
		return skillId;
	}



	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	
	
}
