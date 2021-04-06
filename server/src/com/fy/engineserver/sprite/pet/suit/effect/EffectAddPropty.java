package com.fy.engineserver.sprite.pet.suit.effect;

import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.skill.passivetrigger.PassiveTriggerImmune;
import com.fy.engineserver.soulpith.property.AddPropertyTypes;
import com.fy.engineserver.soulpith.property.Propertys;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.sprite.pet.suit.PetSuitArticle;
import com.fy.engineserver.sprite.pet.suit.PetSuitArticleEntity;
/**
 * 增加宠物属性
 * 
 * @date on create 2016年8月26日 上午10:44:06
 */
public class EffectAddPropty extends AbstractSuitEffect{

	@Override
	public void doEffect(Pet pet, PetSuitArticleEntity pae, int index) {
		// TODO Auto-generated method stub
		PetSuitArticle a = (PetSuitArticle) ArticleManager.getInstance().getArticle(pae.getArticleName());
		int i = index;
//		for (int i=0; i<a.getEffectType().length; i++) {
			Propertys p = Propertys.valueOf(a.getPropertyType()[i]);
			int oldValue = 0;
			int newValue = 0;
			switch (p) {			//物防  法防  物攻 法攻  （命中 闪避 暴击---数值百分比）  半秒回血  五秒回血 
			case HP:
				if (a.getAddTypes()[i] == AddPropertyTypes.ADD_B_NUM) {
					oldValue = pet.getMaxHPB();
					pet.setMaxHPB(pet.getMaxHPB() + a.getPropertyNum()[i]);
					newValue = pet.getMaxHPB();
				} else if (a.getAddTypes()[i] == AddPropertyTypes.ADD_C_NUM) {
					oldValue = pet.getMaxHPC();
					pet.setExtraHpC(pet.getExtraHpC() + a.getPropertyNum()[i] / 10);
					newValue = pet.getExtraHpC();
				}
				break;
			case PHYATTACK:
				if (a.getAddTypes()[i] == AddPropertyTypes.ADD_B_NUM) {
					oldValue = pet.getPhyAttackB();
					pet.setPhyAttackB(pet.getPhyAttackB() + a.getPropertyNum()[i]);
					newValue = pet.getPhyAttackB();
				} else if (a.getAddTypes()[i] == AddPropertyTypes.ADD_C_NUM) {
					oldValue = pet.getPhyAttackC();
					pet.setPhyAttackC(pet.getPhyAttackC() + a.getPropertyNum()[i] / 10);
					newValue = pet.getPhyAttackC();
				}
				pet.setShowPhyAttack(pet.getPhyAttack());
				break;
			case PHYDEFANCE:
				if (a.getAddTypes()[i] == AddPropertyTypes.ADD_B_NUM) {
					oldValue = pet.getPhyDefenceB();
					pet.setPhyDefenceB(pet.getPhyDefenceB() + a.getPropertyNum()[i]);
					newValue = pet.getPhyDefenceB();
				} else if (a.getAddTypes()[i] == AddPropertyTypes.ADD_C_NUM) {
					oldValue = pet.getPhyDefenceC();
					pet.setPhyDefenceC(pet.getPhyDefenceC() + a.getPropertyNum()[i] / 10);
					newValue = pet.getPhyDefenceC();
				}
				break;
			case DODGE:
				if (a.getAddTypes()[i] == AddPropertyTypes.ADD_B_NUM) {
					oldValue = pet.getDodgeB();
					pet.setDodgeB(pet.getDodgeB() + a.getPropertyNum()[i]);
					newValue = pet.getDodgeB();
				} else if (a.getAddTypes()[i] == AddPropertyTypes.ADD_C_NUM) {
					oldValue = pet.getDodgeC();
					pet.setDodgeC(pet.getDodgeC() + a.getPropertyNum()[i] / 10);
					newValue = pet.getDodgeC();
				}
				break;
			case CIRT:
				if (a.getAddTypes()[i] == AddPropertyTypes.ADD_B_NUM) {
					oldValue = pet.getCriticalHitB();
					pet.setCriticalHitB(pet.getCriticalHitB() + a.getPropertyNum()[i]);
					newValue = pet.getCriticalHitB();
				} else if (a.getAddTypes()[i] == AddPropertyTypes.ADD_C_NUM) {
					oldValue = pet.getCriticalHitC();
					pet.setCriticalHitC(pet.getCriticalHitC() + a.getPropertyNum()[i] / 10);
					newValue = pet.getCriticalHitC();
				}
				break;
			case MAGICATTACK:
				if (a.getAddTypes()[i] == AddPropertyTypes.ADD_B_NUM) {
					oldValue = pet.getMagicAttackB();
					pet.setMagicAttackB(pet.getMagicAttackB() + a.getPropertyNum()[i]);
					newValue = pet.getMagicAttackB();
				} else if (a.getAddTypes()[i] == AddPropertyTypes.ADD_C_NUM) {
					oldValue = pet.getMagicAttackC();
					pet.setMagicAttackC(pet.getMagicAttackC() + a.getPropertyNum()[i] / 10);
					newValue = pet.getMagicAttackC();
				}
				pet.setShowMagicAttack(pet.getMagicAttack());
				break;
			case MAGICDEFANCE:
				if (a.getAddTypes()[i] == AddPropertyTypes.ADD_B_NUM) {
					oldValue = pet.getMagicDefenceB();
					pet.setMagicDefenceB(pet.getMagicDefenceB() + a.getPropertyNum()[i]);
					newValue = pet.getMagicDefenceB();
				} else if (a.getAddTypes()[i] == AddPropertyTypes.ADD_C_NUM) {
					oldValue = pet.getMagicDefenceC();
					pet.setMagicDefenceC(pet.getMagicDefenceC() + a.getPropertyNum()[i] / 10);
					newValue = pet.getMagicDefenceC();
				}
				break;
			case HIT:
				if (a.getAddTypes()[i] == AddPropertyTypes.ADD_B_NUM) {
					oldValue = pet.getHitB();
					pet.setHitB(pet.getHitB() + a.getPropertyNum()[i]);
					newValue = pet.getHitB();
				} else if (a.getAddTypes()[i] == AddPropertyTypes.ADD_C_NUM) {
					oldValue = pet.getHitC();
					pet.setHitC(pet.getHitC() + a.getPropertyNum()[i] / 10);
					newValue = pet.getHitC();
				}
				break;
			case DODGE_RATE_OTHER:
				oldValue = pet.getDodgeRateOther();
				pet.setDodgeRateOther(pet.getDodgeRateOther() + a.getPropertyNum()[i]);
				newValue = pet.getDodgeRateOther();
				break;
			case CRITICAL_RATE_OTHER:
				oldValue = pet.getCriticalHitRateOther();
				pet.setCriticalHitRateOther(pet.getCriticalHitRateOther() + a.getPropertyNum()[i]);
				newValue = pet.getCriticalHitRateOther();
				break;
			case HIT_RATE_OTHER:
				oldValue = pet.getHitRateOther();
				pet.setHitRateOther(pet.getHitRateOther() + a.getPropertyNum()[i]);
				newValue = pet.getHitRateOther();
				break;
			case HALF_RECOVER_RATE_OTHER:
				oldValue = pet.getHpRecoverExtend();
				pet.setHpRecoverExtend(pet.getHpRecoverExtend() + a.getPropertyNum()[i]);
				newValue = pet.getHpRecoverExtend();
				break;
			case FIVE_RECOVER_RATE_OTHER:
				oldValue = pet.getHpRecoverBase();
				pet.setHpRecoverBase(pet.getHpRecoverBase() + a.getPropertyNum()[i]);
				newValue = pet.getHpRecoverBase();
				break;
			case FIVE_RECOVER_HPRATE_OTHER:
				oldValue = pet.getHpRecoverExtend2();
				pet.setHpRecoverExtend2(pet.getHpRecoverExtend2() + a.getPropertyNum()[i]);
				newValue = pet.getHpRecoverExtend2();
				break;
			case HP_STEAL_OTHER:
				oldValue = pet.hpStealPercent;
				pet.hpStealPercent = pet.hpStealPercent + a.getPropertyNum()[i];
				newValue = pet.hpStealPercent;
				break;
			case ABNORMAL_TIME_DECREASE:
				oldValue = pet.getDecreaseAbnormalStateTimeRate();
				pet.setDecreaseAbnormalStateTimeRate(a.getPropertyNum()[i]);
				newValue = pet.getDecreaseAbnormalStateTimeRate();
				break;
			case SPECIAL_DAMAGE_DECREASE:
				oldValue = pet.getDecreseSpecialDamage();
				pet.setDecreseSpecialDamage(a.getPropertyNum()[i]);
				newValue = pet.getDecreseSpecialDamage();
				break;
			case ANTIINJURE:
				oldValue = pet.getAntiInjuryRate();
				pet.setAntiInjuryRate(pet.getAntiInjuryRate() + a.getPropertyNum()[i] / 10);
				newValue = pet.getAntiInjuryRate();
				break;
			case PROBGOTFLAG:
				pet.setSignProb(a.getTriggerCondNum()[index]);
				pet.setDamageHpRate(a.getPropertyNum()[index]);
				if (PetManager.logger.isDebugEnabled()) {
					PetManager.logger.debug("["+this.getClass().getSimpleName()+"] [概率加标记] [petId:" + pet.getId() + "] [aeId:" + pae.getId() + "] [" + p.getName() + "] ["+a.getAddTypes()[i].getName()+"] [触发概率:" + pet.getSignProb() + "] [伤害血上限比例：" + pet.getDamageHpRate() + "]");
				}
				break;
			case ADDANTIBYHP:
				pet.setHpDecreaseRate(a.getTriggerCondNum()[index]);
				pet.setAddAntiRate(a.getPropertyNum()[index]);
				pet.setMaxAddAntiRate(a.getMaxLimit()[index]);
				if (PetManager.logger.isDebugEnabled()) {
					PetManager.logger.debug("["+this.getClass().getSimpleName()+"] [掉血加反伤] [petId:" + pet.getId() + "] [aeId:" + pae.getId() + "] [" + p.getName() + "] ["+a.getAddTypes()[i].getName()+"] [血减少比例:" + pet.getHpDecreaseRate()+ "] [增加反伤比例：" + pet.getAddAntiRate() + "] [最大上限:" + pet.getMaxAddAntiRate() + "]");
				}
				break;
			case IMMUDINGSHEN:
				oldValue = pet.getImmuType();
				pet.setImmuType(PassiveTriggerImmune.免疫定身);
				newValue = pet.getImmuType();
			default:
				break;
			}
			if (PetManager.logger.isDebugEnabled()) {
				PetManager.logger.debug("["+this.getClass().getSimpleName()+"] [petId:" + pet.getId() + "] [aeId:" + pae.getId() + "] [" + p.getName() + "] ["+a.getAddTypes()[i].getName()+"] [值:" + oldValue + "->" + newValue + "]");
			}
		}
// 	}

}
