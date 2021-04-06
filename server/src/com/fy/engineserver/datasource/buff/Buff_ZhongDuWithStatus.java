package com.fy.engineserver.datasource.buff;

import org.slf4j.Logger;

import com.fy.engineserver.articleEnchant.AbnormalStateBuff;
import com.fy.engineserver.combat.CombatCaculator;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.res.Constants;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.pet.Pet;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;
/**
 * 与法攻有关的中毒伤害（计算目标法防） 并且附加中毒状态，根据此状态兽魁有些技能需要特殊处理
 * @author Administrator
 *
 */
@SimpleEmbeddable
public class Buff_ZhongDuWithStatus extends Buff implements AbnormalStateBuff{

	private long lastExeTime = 0;
	
	private long LastingTime;

	public void setLastingTime(long lastingTime) {
		LastingTime = lastingTime;
	}


	
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(p.isImmunity()){
				this.setInvalidTime(0);//立即失效
			}else{
				p.setPoison(true);
				if (this.getCauser() instanceof Player) {
					p.changeDuFlag((Player) this.getCauser(), Constants.shihunFlag);
				}
			}
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			if(p.isImmunity()){
				this.setInvalidTime(0);//立即失效
			}else{
				p.setPoison(true);
				if (this.getCauser() instanceof Player) {
					p.changeDuFlag((Player) this.getCauser(), Constants.shihunFlag);
				}
			}
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setPoison(false);
			if (this.getCauser() instanceof Player) {
				p.changeDuFlag((Player) this.getCauser(), (byte) 0);
			}
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			p.setPoison(false);
			if (this.getCauser() instanceof Player) {
				p.changeDuFlag((Player) this.getCauser(), (byte) 0);
			}
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
		Logger log = Skill.logger;
		if(heartBeatStartTime - lastExeTime >= LastingTime){
			lastExeTime = heartBeatStartTime;
			if(owner instanceof Player){
				Player p = (Player)owner;
				p.setPoison(true);
				if(p.isImmunity()){
					this.setInvalidTime(0);//立即失效
				}else{
					int hp = 0;
					int defenderMagicDefenceRate = CombatCaculator.CAL_法术减伤率(p.getMagicDefence(), p.getLevel(), p.getCareer());
					BuffTemplate_ZhongDuWithStatus bt = (BuffTemplate_ZhongDuWithStatus)this.getTemplate();
					if(bt.modulus != null && bt.modulus.length > getLevel()){
						if(getCauser() instanceof Player){
							//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
							hp = (int)(((Player)getCauser()).getMagicAttack()*bt.modulus[getLevel()]/100);	
						}else if(getCauser() instanceof Monster){
							Monster m = (Monster)getCauser();
							hp = (int)(m.getMagicAttack()*bt.modulus[getLevel()]/100);
						}else if(getCauser() instanceof NPC){
							NPC n = (NPC)getCauser();
							hp = (int)(n.getMagicAttack()*bt.modulus[getLevel()]/100);
						}else if(getCauser() instanceof Pet){
							Pet pet = (Pet) getCauser();
							int base = Math.max(pet.getMagicAttack(), pet.getPhyAttack());
							hp = (int)(base*bt.modulus[getLevel()]/100);
						}else{
							log.debug("Buff_ZhongDuFaGong.heartbeat: 未处理的发起者类型 {}",getCauser());
						}
						if(hp > 0) {
							int oldHp = hp;
							hp = (int) ((1000L - defenderMagicDefenceRate) * hp / 1000L);
							if(log.isDebugEnabled()) {
								log.debug("[跟法术强度与法防有关的中毒buff] [计算法防] [计算法放前伤害 : " + oldHp + "] [计算法防后伤害:" + hp + "] [法防率:" + defenderMagicDefenceRate + "] [" + p.getLogString() + "]");
							}
						}
					}
					if(this.getCauser() != null){
						//护盾吸收毒的伤害
						int hudun = owner.getHuDunDamage();
						if(hudun > 0){
							int damage = hp;
							if (hudun >= damage) {
								owner.setHuDunDamage(hudun - damage);

								owner.causeDamage(getCauser(), damage, 10,Fighter.DAMAGETYPE_XISHOU);
								getCauser().damageFeedback(owner, damage, 10,Fighter.DAMAGETYPE_XISHOU);
							} else {
							
								owner.setHuDunDamage(0);

								owner.causeDamage(getCauser(), hudun, 10,Fighter.DAMAGETYPE_XISHOU);
								getCauser().damageFeedback(owner, hudun, 10,Fighter.DAMAGETYPE_XISHOU);

								owner.causeDamage(getCauser(), damage - hudun, 10,Fighter.DAMAGETYPE_ZHAONGDU);
								getCauser().damageFeedback(owner, damage - hudun, 10,Fighter.DAMAGETYPE_ZHAONGDU);
							}
						}else{
							owner.causeDamage(getCauser(), hp, 10,Fighter.DAMAGETYPE_ZHAONGDU);
							getCauser().damageFeedback(owner, hp, 10,Fighter.DAMAGETYPE_ZHAONGDU);
						}
						
					}else {
						Skill.logger.debug("Buff_ZhongDuFaGong.heartbeat: getCauser is null");
					}
					
					//统计
					if(getCauser() instanceof Player){
						p.notifyAttack((Player)getCauser(), this.getTemplateName(), getLevel(),Fighter.DAMAGETYPE_ZHAONGDU, hp);
					}
					log.debug("Buff_ZhongDuFaGong.heartbeat: 12");
				}
				log.debug("Buff_ZhongDuFaGong.heartbeat: 13");
			}else if(owner instanceof Sprite){
				Sprite s = (Sprite)owner;
				s.setPoison(true);
				if(s.isImmunity()){
					this.setInvalidTime(0);//立即失效
				}else{
					int hp = 0;
					BuffTemplate_ZhongDuWithStatus bt = (BuffTemplate_ZhongDuWithStatus)this.getTemplate();
					if(bt.modulus != null && bt.modulus.length > getLevel()){
						if(getCauser() instanceof Player){
							//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
							hp = (int)(((Player)getCauser()).getMagicAttack()*bt.modulus[getLevel()]/100);	
						}else if(getCauser() instanceof Monster){
							Monster m = (Monster)getCauser();
							hp = (int)(m.getMagicAttack()*bt.modulus[getLevel()]/100);
						}else if(getCauser() instanceof NPC){
							NPC n = (NPC)getCauser();
							hp = (int)(n.getMagicAttack()*bt.modulus[getLevel()]/100);
						}else if(getCauser() instanceof Pet){
							Pet pet = (Pet) getCauser();
							int base = Math.max(pet.getMagicAttack(), pet.getPhyAttack());
							hp = (int)(base*bt.modulus[getLevel()]/100);
						}
					}
					if (owner instanceof Pet) {
						hp = ((Pet)owner).checkInjuryAndPosiDamage(hp);
					}
					if(this.getCauser() != null){

						//护盾吸收毒的伤害
						int hudun = owner.getHuDunDamage();
						if(hudun > 0){
							int damage = hp;
							if (hudun >= damage) {
								owner.setHuDunDamage(hudun - damage);

								owner.causeDamage(getCauser(), damage, 10,Fighter.DAMAGETYPE_XISHOU);
								getCauser().damageFeedback(owner, damage, 10,Fighter.DAMAGETYPE_XISHOU);
							} else {
							
								owner.setHuDunDamage(0);

								owner.causeDamage(getCauser(), hudun, 10,Fighter.DAMAGETYPE_XISHOU);
								getCauser().damageFeedback(owner, hudun, 10,Fighter.DAMAGETYPE_XISHOU);

								owner.causeDamage(getCauser(), damage - hudun, 10,Fighter.DAMAGETYPE_ZHAONGDU);
								getCauser().damageFeedback(owner, damage - hudun, 10,Fighter.DAMAGETYPE_ZHAONGDU);
							}
						}else {
							if (owner instanceof BossMonster) {
								owner.causeDamage(getCauser(), hp, 10,Fighter.DAMAGETYPE_SPELL);
								getCauser().damageFeedback(owner, hp, 10,Fighter.DAMAGETYPE_SPELL);
							} else {
								owner.causeDamage(getCauser(), hp, 10,Fighter.DAMAGETYPE_ZHAONGDU);
								getCauser().damageFeedback(owner, hp, 10,Fighter.DAMAGETYPE_ZHAONGDU);
							}
						}
					}else {
						Skill.logger.debug("Buff_ZhongDuFaGong.heartbeat: getCauser is null");
					}
					
					//统计
					if(getCauser() instanceof Player && owner instanceof Monster){
						Monster m = (Monster)owner;
						m.notifyAttack((Player)getCauser(), this.getTemplateName(), getLevel(),Fighter.DAMAGETYPE_ZHAONGDU, hp);
					}
				}
			}
		}
	}
	
	public String getDescription(){
		int hp = 0;
		BuffTemplate_ZhongDuWithStatus bt = (BuffTemplate_ZhongDuWithStatus)this.getTemplate();
		if(bt != null && bt.modulus != null && bt.modulus.length > getLevel()){
			if(getCauser() instanceof Player){
				//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
				hp = (int)(((Player)getCauser()).getMagicAttack()*bt.modulus[getLevel()]/100);	
			}else if(getCauser() instanceof Sprite){
				Sprite s = (Sprite)getCauser();
				hp = (int)(s.getMagicAttack()*bt.modulus[getLevel()]/100);
			}
			return Translate.text_3231+((double)LastingTime/1000)+Translate.text_3234+hp+Translate.text_3275;
		}
		return super.getDescription();
	}
}
