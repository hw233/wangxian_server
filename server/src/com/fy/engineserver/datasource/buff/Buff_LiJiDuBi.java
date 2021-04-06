package com.fy.engineserver.datasource.buff;

import java.util.ArrayList;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.ConvoyNPC;
import com.fy.engineserver.sprite.npc.GuardNPC;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 立即使目标身上的某一种毒发作，使目标受到这种毒的所有伤害。该毒必须为该施法者施加的
 *
 */
@SimpleEmbeddable
public class Buff_LiJiDuBi extends Buff{

	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		BuffTemplateManager btm = BuffTemplateManager.getInstance();
		if(btm == null){
			return;
		}
		if(owner instanceof Player){
			Player p = (Player)owner;
			Buff buffs[] = p.getActiveBuffs();
			for(int i = 0 ; i < buffs.length ; i++ ){
				if(buffs[i].getCauser() == this.getCauser() && (buffs[i] instanceof Buff_ZhongDu || buffs[i] instanceof Buff_ZhongDuWuGong || buffs[i] instanceof Buff_ZhongDuFaGong)){
					int dotDamage = 0;
					long start = buffs[i].getStartTime();
					long end = buffs[i].getInvalidTime();
					long lastingTime = 0;
					if(end > start){
						lastingTime = end - start;
					}
					if(buffs[i] instanceof Buff_ZhongDu){
						if(lastingTime != 0){
							BuffTemplate bt = btm.getBuffTemplateByName(buffs[i].getTemplateName());
							if(bt != null && bt instanceof BuffTemplate_ZhongDu && ((BuffTemplate_ZhongDu)bt).getLastingTime() != null && ((BuffTemplate_ZhongDu)bt).getLastingTime().length > buffs[i].getLevel() && ((BuffTemplate_ZhongDu)bt).getHp() != null && ((BuffTemplate_ZhongDu)bt).getHp().length > buffs[i].getLevel()){
								long jiangeTime = ((BuffTemplate_ZhongDu)bt).getLastingTime()[buffs[i].getLevel()];
								int damage = ((BuffTemplate_ZhongDu)bt).getHp()[buffs[i].getLevel()];
								if(jiangeTime != 0){
									dotDamage = (int)(lastingTime*damage/jiangeTime);
								}
							}
						}
						
					}else if(buffs[i] instanceof Buff_ZhongDuWuGong){
						if(lastingTime != 0){
							BuffTemplate bt = btm.getBuffTemplateByName(buffs[i].getTemplateName());
							if(bt != null && bt instanceof BuffTemplate_ZhongDuWuGong && ((BuffTemplate_ZhongDuWuGong)bt).getLastingTime() != null && ((BuffTemplate_ZhongDuWuGong)bt).getLastingTime().length > buffs[i].getLevel() && ((BuffTemplate_ZhongDuWuGong)bt).getModulus() != null && ((BuffTemplate_ZhongDuWuGong)bt).getModulus().length > buffs[i].getLevel()){
								long jiangeTime = ((BuffTemplate_ZhongDuWuGong)bt).getLastingTime()[buffs[i].getLevel()];
								int modulus = ((BuffTemplate_ZhongDuWuGong)bt).getModulus()[buffs[i].getLevel()];
								int damage = 0;
								if(getCauser() instanceof Player){
									//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
									damage = (int)(((Player)getCauser()).getPhyAttack()*modulus/100);	
								}
								if(jiangeTime != 0){
									dotDamage = (int)(lastingTime*damage/jiangeTime);
								}
							}
						}
					}else if(buffs[i] instanceof Buff_ZhongDuFaGong){
						if(lastingTime != 0){
							BuffTemplate bt = btm.getBuffTemplateByName(buffs[i].getTemplateName());
							if(bt != null && bt instanceof BuffTemplate_ZhongDuFaGong && ((BuffTemplate_ZhongDuFaGong)bt).getLastingTime() != null && ((BuffTemplate_ZhongDuFaGong)bt).getLastingTime().length > buffs[i].getLevel() && ((BuffTemplate_ZhongDuFaGong)bt).getModulus() != null && ((BuffTemplate_ZhongDuFaGong)bt).getModulus().length > buffs[i].getLevel()){
								long jiangeTime = ((BuffTemplate_ZhongDuFaGong)bt).getLastingTime()[buffs[i].getLevel()];
								int modulus = ((BuffTemplate_ZhongDuFaGong)bt).getModulus()[buffs[i].getLevel()];
								int damage = 0;
								if(getCauser() instanceof Player){
									//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
									damage = (int)(((Player)getCauser()).getMagicAttack()*modulus/100);	
								}
								if(jiangeTime != 0){
									dotDamage = (int)(lastingTime*damage/jiangeTime);
								}
							}
						}
					}
					if(this.getCauser() != null && dotDamage != 0){
						//护盾吸收伤害
						int hudun = owner.getHuDunDamage();
						if(hudun > 0){
							int damage = dotDamage;
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
							owner.causeDamage(getCauser(), dotDamage, 10,Fighter.DAMAGETYPE_ZHAONGDU);
							getCauser().damageFeedback(owner, dotDamage, 10,Fighter.DAMAGETYPE_ZHAONGDU);
						}
						
					}
					buffs[i].setInvalidTime(0);
					break;
				}
			}
		}else if(owner instanceof Monster){
			Monster s = (Monster)owner;
			ArrayList<Buff> buffs = s.getBuffs();
			for(int i = 0 ; i < buffs.size() ; i++ ){
				Buff buff = buffs.get(i);

				if(buff.getCauser() == this.getCauser() && (buff instanceof Buff_ZhongDu || buff instanceof Buff_ZhongDuWuGong || buff instanceof Buff_ZhongDuFaGong)){
					int dotDamage = 0;
					long start = buff.getStartTime();
					long end = buff.getInvalidTime();
					long lastingTime = 0;
					if(end > start){
						lastingTime = end - start;
					}
					if(buff instanceof Buff_ZhongDu){
						if(lastingTime != 0){
							BuffTemplate bt = btm.getBuffTemplateByName(buff.getTemplateName());
							if(bt != null && bt instanceof BuffTemplate_ZhongDu && ((BuffTemplate_ZhongDu)bt).getLastingTime() != null && ((BuffTemplate_ZhongDu)bt).getLastingTime().length > buff.getLevel() && ((BuffTemplate_ZhongDu)bt).getHp() != null && ((BuffTemplate_ZhongDu)bt).getHp().length > buff.getLevel()){
								long jiangeTime = ((BuffTemplate_ZhongDu)bt).getLastingTime()[buff.getLevel()];
								int damage = ((BuffTemplate_ZhongDu)bt).getHp()[buff.getLevel()];
								if(jiangeTime != 0){
									dotDamage = (int)(lastingTime*damage/jiangeTime);
								}
							}
						}
						
					}else if(buff instanceof Buff_ZhongDuWuGong){
						if(lastingTime != 0){
							BuffTemplate bt = btm.getBuffTemplateByName(buff.getTemplateName());
							if(bt != null && bt instanceof BuffTemplate_ZhongDuWuGong && ((BuffTemplate_ZhongDuWuGong)bt).getLastingTime() != null && ((BuffTemplate_ZhongDuWuGong)bt).getLastingTime().length > buff.getLevel() && ((BuffTemplate_ZhongDuWuGong)bt).getModulus() != null && ((BuffTemplate_ZhongDuWuGong)bt).getModulus().length > buff.getLevel()){
								long jiangeTime = ((BuffTemplate_ZhongDuWuGong)bt).getLastingTime()[buff.getLevel()];
								int modulus = ((BuffTemplate_ZhongDuWuGong)bt).getModulus()[buff.getLevel()];
								int damage = 0;
								if(getCauser() instanceof Player){
									//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
									damage = (int)(((Player)getCauser()).getPhyAttack()*modulus/100);	
								}
								if(jiangeTime != 0){
									dotDamage = (int)(lastingTime*damage/jiangeTime);
								}
							}
						}
					}else if(buff instanceof Buff_ZhongDuFaGong){
						if(lastingTime != 0){
							BuffTemplate bt = btm.getBuffTemplateByName(buff.getTemplateName());
							if(bt != null && bt instanceof BuffTemplate_ZhongDuFaGong && ((BuffTemplate_ZhongDuFaGong)bt).getLastingTime() != null && ((BuffTemplate_ZhongDuFaGong)bt).getLastingTime().length > buff.getLevel() && ((BuffTemplate_ZhongDuFaGong)bt).getModulus() != null && ((BuffTemplate_ZhongDuFaGong)bt).getModulus().length > buff.getLevel()){
								long jiangeTime = ((BuffTemplate_ZhongDuFaGong)bt).getLastingTime()[buff.getLevel()];
								int modulus = ((BuffTemplate_ZhongDuFaGong)bt).getModulus()[buff.getLevel()];
								int damage = 0;
								if(getCauser() instanceof Player){
									//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
									damage = (int)(((Player)getCauser()).getMagicAttack()*modulus/100);	
								}
								if(jiangeTime != 0){
									dotDamage = (int)(lastingTime*damage/jiangeTime);
								}
							}
						}
					}
					if(this.getCauser() != null && dotDamage != 0){
						//护盾吸收伤害
						int hudun = owner.getHuDunDamage();
						if(hudun > 0){
							int damage = dotDamage;
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
							owner.causeDamage(getCauser(), dotDamage, 10,Fighter.DAMAGETYPE_ZHAONGDU);
							getCauser().damageFeedback(owner, dotDamage, 10,Fighter.DAMAGETYPE_ZHAONGDU);
						}
					}
					buff.setInvalidTime(0);
					break;
				}
			}
		}else if(owner instanceof GuardNPC){
			GuardNPC s = (GuardNPC)owner;
			ArrayList<Buff> buffs = s.getBuffs();
			for(int i = 0 ; i < buffs.size() ; i++ ){
				Buff buff = buffs.get(i);

				if(buff.getCauser() == this.getCauser() && (buff instanceof Buff_ZhongDu || buff instanceof Buff_ZhongDuWuGong || buff instanceof Buff_ZhongDuFaGong)){
					int dotDamage = 0;
					long start = buff.getStartTime();
					long end = buff.getInvalidTime();
					long lastingTime = 0;
					if(end > start){
						lastingTime = end - start;
					}
					if(buff instanceof Buff_ZhongDu){
						if(lastingTime != 0){
							BuffTemplate bt = btm.getBuffTemplateByName(buff.getTemplateName());
							if(bt != null && bt instanceof BuffTemplate_ZhongDu && ((BuffTemplate_ZhongDu)bt).getLastingTime() != null && ((BuffTemplate_ZhongDu)bt).getLastingTime().length > buff.getLevel() && ((BuffTemplate_ZhongDu)bt).getHp() != null && ((BuffTemplate_ZhongDu)bt).getHp().length > buff.getLevel()){
								long jiangeTime = ((BuffTemplate_ZhongDu)bt).getLastingTime()[buff.getLevel()];
								int damage = ((BuffTemplate_ZhongDu)bt).getHp()[buff.getLevel()];
								if(jiangeTime != 0){
									dotDamage = (int)(lastingTime*damage/jiangeTime);
								}
							}
						}
						
					}else if(buff instanceof Buff_ZhongDuWuGong){
						if(lastingTime != 0){
							BuffTemplate bt = btm.getBuffTemplateByName(buff.getTemplateName());
							if(bt != null && bt instanceof BuffTemplate_ZhongDuWuGong && ((BuffTemplate_ZhongDuWuGong)bt).getLastingTime() != null && ((BuffTemplate_ZhongDuWuGong)bt).getLastingTime().length > buff.getLevel() && ((BuffTemplate_ZhongDuWuGong)bt).getModulus() != null && ((BuffTemplate_ZhongDuWuGong)bt).getModulus().length > buff.getLevel()){
								long jiangeTime = ((BuffTemplate_ZhongDuWuGong)bt).getLastingTime()[buff.getLevel()];
								int modulus = ((BuffTemplate_ZhongDuWuGong)bt).getModulus()[buff.getLevel()];
								int damage = 0;
								if(getCauser() instanceof Player){
									//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
									damage = (int)(((Player)getCauser()).getPhyAttack()*modulus/100);	
								}
								if(jiangeTime != 0){
									dotDamage = (int)(lastingTime*damage/jiangeTime);
								}
							}
						}
					}else if(buff instanceof Buff_ZhongDuFaGong){
						if(lastingTime != 0){
							BuffTemplate bt = btm.getBuffTemplateByName(buff.getTemplateName());
							if(bt != null && bt instanceof BuffTemplate_ZhongDuFaGong && ((BuffTemplate_ZhongDuFaGong)bt).getLastingTime() != null && ((BuffTemplate_ZhongDuFaGong)bt).getLastingTime().length > buff.getLevel() && ((BuffTemplate_ZhongDuFaGong)bt).getModulus() != null && ((BuffTemplate_ZhongDuFaGong)bt).getModulus().length > buff.getLevel()){
								long jiangeTime = ((BuffTemplate_ZhongDuFaGong)bt).getLastingTime()[buff.getLevel()];
								int modulus = ((BuffTemplate_ZhongDuFaGong)bt).getModulus()[buff.getLevel()];
								int damage = 0;
								if(getCauser() instanceof Player){
									//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
									damage = (int)(((Player)getCauser()).getMagicAttack()*modulus/100);	
								}
								if(jiangeTime != 0){
									dotDamage = (int)(lastingTime*damage/jiangeTime);
								}
							}
						}
					}
					if(this.getCauser() != null && dotDamage != 0){
						//护盾吸收伤害
						int hudun = owner.getHuDunDamage();
						if(hudun > 0){
							int damage = dotDamage;
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
							owner.causeDamage(getCauser(), dotDamage, 10,Fighter.DAMAGETYPE_ZHAONGDU);
							getCauser().damageFeedback(owner, dotDamage, 10,Fighter.DAMAGETYPE_ZHAONGDU);
						}
					}
					buff.setInvalidTime(0);
					break;
				}
			}
		}else if(owner instanceof ConvoyNPC){
			ConvoyNPC s = (ConvoyNPC)owner;
			ArrayList<Buff> buffs = s.getBuffs();
			for(int i = 0 ; i < buffs.size() ; i++ ){
				Buff buff = buffs.get(i);

				if(buff.getCauser() == this.getCauser() && (buff instanceof Buff_ZhongDu || buff instanceof Buff_ZhongDuWuGong || buff instanceof Buff_ZhongDuFaGong)){
					int dotDamage = 0;
					long start = buff.getStartTime();
					long end = buff.getInvalidTime();
					long lastingTime = 0;
					if(end > start){
						lastingTime = end - start;
					}
					if(buff instanceof Buff_ZhongDu){
						if(lastingTime != 0){
							BuffTemplate bt = btm.getBuffTemplateByName(buff.getTemplateName());
							if(bt != null && bt instanceof BuffTemplate_ZhongDu && ((BuffTemplate_ZhongDu)bt).getLastingTime() != null && ((BuffTemplate_ZhongDu)bt).getLastingTime().length > buff.getLevel() && ((BuffTemplate_ZhongDu)bt).getHp() != null && ((BuffTemplate_ZhongDu)bt).getHp().length > buff.getLevel()){
								long jiangeTime = ((BuffTemplate_ZhongDu)bt).getLastingTime()[buff.getLevel()];
								int damage = ((BuffTemplate_ZhongDu)bt).getHp()[buff.getLevel()];
								if(jiangeTime != 0){
									dotDamage = (int)(lastingTime*damage/jiangeTime);
								}
							}
						}
						
					}else if(buff instanceof Buff_ZhongDuWuGong){
						if(lastingTime != 0){
							BuffTemplate bt = btm.getBuffTemplateByName(buff.getTemplateName());
							if(bt != null && bt instanceof BuffTemplate_ZhongDuWuGong && ((BuffTemplate_ZhongDuWuGong)bt).getLastingTime() != null && ((BuffTemplate_ZhongDuWuGong)bt).getLastingTime().length > buff.getLevel() && ((BuffTemplate_ZhongDuWuGong)bt).getModulus() != null && ((BuffTemplate_ZhongDuWuGong)bt).getModulus().length > buff.getLevel()){
								long jiangeTime = ((BuffTemplate_ZhongDuWuGong)bt).getLastingTime()[buff.getLevel()];
								int modulus = ((BuffTemplate_ZhongDuWuGong)bt).getModulus()[buff.getLevel()];
								int damage = 0;
								if(getCauser() instanceof Player){
									//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
									damage = (int)(((Player)getCauser()).getPhyAttack()*modulus/100);	
								}
								if(jiangeTime != 0){
									dotDamage = (int)(lastingTime*damage/jiangeTime);
								}
							}
						}
					}else if(buff instanceof Buff_ZhongDuFaGong){
						if(lastingTime != 0){
							BuffTemplate bt = btm.getBuffTemplateByName(buff.getTemplateName());
							if(bt != null && bt instanceof BuffTemplate_ZhongDuFaGong && ((BuffTemplate_ZhongDuFaGong)bt).getLastingTime() != null && ((BuffTemplate_ZhongDuFaGong)bt).getLastingTime().length > buff.getLevel() && ((BuffTemplate_ZhongDuFaGong)bt).getModulus() != null && ((BuffTemplate_ZhongDuFaGong)bt).getModulus().length > buff.getLevel()){
								long jiangeTime = ((BuffTemplate_ZhongDuFaGong)bt).getLastingTime()[buff.getLevel()];
								int modulus = ((BuffTemplate_ZhongDuFaGong)bt).getModulus()[buff.getLevel()];
								int damage = 0;
								if(getCauser() instanceof Player){
									//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
									damage = (int)(((Player)getCauser()).getMagicAttack()*modulus/100);	
								}
								if(jiangeTime != 0){
									dotDamage = (int)(lastingTime*damage/jiangeTime);
								}
							}
						}
					}
					if(this.getCauser() != null && dotDamage != 0){
						//护盾吸收伤害
						int hudun = owner.getHuDunDamage();
						if(hudun > 0){
							int damage = dotDamage;
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
							owner.causeDamage(getCauser(), dotDamage, 10,Fighter.DAMAGETYPE_ZHAONGDU);
							getCauser().damageFeedback(owner, dotDamage, 10,Fighter.DAMAGETYPE_ZHAONGDU);
						}
					}
					buff.setInvalidTime(0);
					break;
				}
			}
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
	
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	}

}
