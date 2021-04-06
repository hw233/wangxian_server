package com.fy.engineserver.datasource.skill2;

import org.slf4j.Logger;

import com.fy.engineserver.constants.Event;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.buff.BuffTemplate_ChouXie;
import com.fy.engineserver.datasource.buff.BuffTemplate_FanShang;
import com.fy.engineserver.datasource.buff.BuffTemplate_JingYan;
import com.fy.engineserver.datasource.buff.BuffTemplate_OnceAttributeAttack;
import com.fy.engineserver.datasource.buff.BuffTemplate_PoFangPercent;
import com.fy.engineserver.datasource.buff.BuffTemplate_YiDiXue;
import com.fy.engineserver.datasource.buff.BuffTemplate_ZengGong;
import com.fy.engineserver.datasource.buff.BuffTemplate_petAddHpAndAnti;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTIFY_EVENT_REQ;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet2.Pet2SkillCalc;


/**
 * 
 * create on 2013年8月30日
 */
public class GenericBuffCalc {
	public static GenericBuffCalc inst;
	public GenericBuffCalc(){
		inst = this;
	}
	public static GenericBuffCalc getInst(){
		if(inst == null){
			new GenericBuffCalc();
		}
		return inst;
	}
	/**
	 * 处理那些根据攻击次数计算的buff，如：
	 * 每5次攻击后下次攻击队目标造成流血状态，每秒造成伤害2W，持续3秒
	 * @param target
	 * @param buff
	 * @param atkTimes
	 * @param log
	 */
	public void procBuff(Fighter target, Pet pet, long atkTimes, Logger log) {
//		if(log.isDebugEnabled())		log.debug("procBuff "+buff+" $");
		GenericBuff buff = pet.pet2buff;
		
		while(buff != null){
			do{//do while(false) 是为了调整代码结构，因为最后 一句  buff = buff.next; 必须执行。
				if(buff.attId != GenericBuff.ATT_ADD_SUB_BUFF){
					if(log.isDebugEnabled())		log.debug("procBuff {} {} not buff", buff.srcSkName,buff.attId);
					break;
				}
				BuffCondition con = buff.triggerCondtion;
				if(con == null){
					if(log.isDebugEnabled()){
						log.debug("not condition");
					}
					break;
				}
				
				log.warn("[in usingSkill..] [in procBuff...] [con.conditionType:"+con.conditionType+"] [atkTimes:"+atkTimes+"] [con.paramInt:"+con.paramInt+"] [buff:"+(buff==null?"":buff.attId)+"] [atkTimes:"+atkTimes+"] ["+(target!=null?target.getName():"null")+"] [pet:"+(pet!=null?pet.getName():"null")+"]");
				if(con.conditionType == BuffCondition.CON_ATK_TIMES){
					//atkTimes加1后才执行到这里
					if(atkTimes % con.paramInt == 0){
						if(log.isDebugEnabled())		log.debug("procBuff times ok {}", buff.srcSkName);
						appendBuff(target, buff, log, pet);
					}
					else if(log.isDebugEnabled())		log.debug("procBuff times NOT ok", buff.srcSkName);
				}else if(con.conditionType == BuffCondition.CON_RATE_WHEN_ATK){
					int r = ActiveSkill.random.nextInt(100);
					if(r<con.paramInt){
						appendBuff(target, buff, log, pet);
						log.debug("GenericBuffCalc.procBuff: 攻击后触发几率性buff {} {}",buff.attName,con.paramInt);
					}else{
						log.debug("GenericBuffCalc.procBuff: 攻击后《未>触发几率性buff {} r{} {}",new Object[]{buff.attName,r,con.paramInt});
					}
				}
			}while(false);
			//
			buff = buff.next;
		}
		log.debug("GenericBuffCalc.procBuff: end.buffis"+(buff==null?"null":buff.attId)+"");
	}
	public static String[] BUFF_NAMES = new String[]{"千层减命", "千层封魔", "千层减速"};
	public static int[] BUFF_LEVELS = new int[]{49, 0, 1};
	
	public void appendBuff(Fighter target, GenericBuff buff, Logger log, Pet pet) {
		BuffTemplate t = buff.getTemplate();
		if(t == null){
			if(log.isDebugEnabled()){
				log.debug("template null {}",buff.srcSkName);
			}
			return;
		}
		int buffLv = buff.v;
		if (log.isDebugEnabled()) {
			log.debug("["+pet.getId()+"] ["+pet.getName()+"] ["+(t instanceof BuffTemplate_OnceAttributeAttack)+"] [buffLv:" + buffLv + "]");
		}
		if (t instanceof BuffTemplate_OnceAttributeAttack && buffLv == -1) {		//buff等级-1代表随机
			buffLv = Pet2SkillCalc.rnd2.nextInt(4);
			if (log.isDebugEnabled()) {
				log.debug("["+pet.getId()+"] ["+pet.getName()+"] [randomLv:" + buffLv + "]");
			}
		}
		if (buffLv == -2) {			//更换buff模板为 降低命中率，封魔，减速
			int ranIndex = Pet.random.nextInt(3);
			BuffTemplateManager btm = BuffTemplateManager.getInstance();
			t = btm.getBuffTemplateByName(BUFF_NAMES[ranIndex]);
			buffLv = BUFF_LEVELS[ranIndex];
		}
		Buff newB = t.createBuff(buffLv);
		if(newB == null){
			if(log.isDebugEnabled()){
				log.debug("create buff null {}", t.getName());
			}
			return;
		}
		Fighter buffTo = target;
		if(t instanceof BuffTemplate_ChouXie){//鬼饮
			buffTo = pet;
		}else if(t instanceof BuffTemplate_JingYan){//开悟
//			buff.lastMS = Integer.MAX_VALUE;
			buffTo = pet.getMaster();
		}else if(t instanceof BuffTemplate_ZengGong){//宠物撕咬
			buffTo = pet;
		}else if(t instanceof BuffTemplate_FanShang){//高级反震
			buffTo = pet;
		}else if(t instanceof BuffTemplate_PoFangPercent){//无视
			buffTo = pet;
		}else if(t instanceof BuffTemplate_YiDiXue){//索命，检查
			boolean isWorldBoss = false;
			if(target instanceof Monster){
				Monster boss = (Monster) target;
				int cateId = boss.getSpriteCategoryId();
			}
			if(isWorldBoss){
				log.debug("索命对世界boss无效 {}",target.getName());
				return;
			}else if(target != null && target.getClass() == BossMonster.class){
				log.debug("索命对boss无效 {}",target.getName());
				return;
			}else if(Pet2SkillCalc.getInst().isProtectedNPC(target)){
				return;
			}
		} else if (t instanceof BuffTemplate_petAddHpAndAnti) {
			buffTo = pet;
		} else if (t instanceof BuffTemplate_OnceAttributeAttack) {
			buffTo = pet;
		}
		//
		if(buffTo == null){
			log.debug("GenericBuffCalc.appendBuff: buff to is null {}",buff.srcSkName);
			return;
		}
		long curT = SystemTime.currentTimeMillis();
		newB.setStartTime(curT);
		newB.setInvalidTime(curT + buff.lastMS);
		newB.setCauser(pet);
		buffTo.placeBuff(newB);
//		HunshiManager.getInstance().dealWithInfectSkill(pet, buffTo, newB);
		if(log.isInfoEnabled()){
			log.info("触发buff {} 在 {}",
					t.getName(), buffTo.getName());
		}		
	}
	/**
	 * 对他人造成伤害，处理是否有附加伤害。
	 * @param pet
	 * @param target
	 * @param damage
	 * @param damageType
	 * @param buff
	 */
	public void damageFeedback(Fighter src, Fighter target, int damage,
			int damageType, GenericBuff buff) {
		Logger log = Skill.logger;
		log.debug("check append damage type {} value {}", damageType, damage);
		if(target == null){
			log.debug("GenericBuffCalc damageFeedback target is null");
			return;
		}
		switch(damageType){
		case Fighter.DAMAGETYPE_PHYSICAL:
		case Fighter.DAMAGETYPE_SPELL:
		case Fighter.DAMAGETYPE_PHYSICAL_CRITICAL:
		case Fighter.DAMAGETYPE_SPELL_CRITICAL:
			break;
		default:
			log.debug("GenericBuffCalc damageFeedback don't care type {}", damageType);
			return;
		}
		while(buff != null){
			do{
				switch(buff.attId){
				case GenericBuff.ATT_APPEND_DAMAGE:
					//FIXME 根据 buff的paramInt确定附加哪种伤害（雷雨风电）
					target.causeDamage(src, buff.v, 0, Fighter.DAMAGETYPE_PHYSICAL);
					if(src instanceof Pet){
						Pet pet = (Pet)src;
						if(pet.getMaster() != null){
							byte targetType = 0;
							if (target instanceof Player) {
								targetType = 0;
							} else if (target instanceof Sprite) {
								targetType = 1;
							}
							NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, target.getId(), (byte) Event.HP_DECREASED_PHYSICAL_4_PET, buff.v);
							pet.getMaster().addMessageToRightBag(req);
							log.debug("GenericBuffCalc.damageFeedback: 通知主人造成伤害 ");
						}
					}
					log.debug("append damage {} to {}", buff.v, target.getName());
					break;
				case GenericBuff.ATT_ADD_SUB_BUFF:
					BuffCondition con = buff.triggerCondtion;
					if(con != null){
						switch(con.conditionType){
						case BuffCondition.CON_RATE_POST_HURT:
							if(damage<=0){
								break;
							}
							int r = ActiveSkill.random.nextInt(100);
							if(r<con.paramInt){
								log.debug("GenericBuffCalc.damageFeedback: trigger {} {}",buff.attName, r);
								BuffTemplate t = buff.getTemplate();
								if(t == null){
									log.debug("GenericBuffCalc.damageFeedback: template is null {}",buff.attName);
									break;//swith
								}
								Buff newB = t.createBuff(buff.v);
								if(newB == null){
									if(log.isDebugEnabled()){
										log.debug("create buff null {}", t.getName());
									}
									break;
								}
								long curT = SystemTime.currentTimeMillis();
								newB.setStartTime(curT);
								newB.setInvalidTime(curT + buff.lastMS);
//								newB.setCauser(causer);
								target.placeBuff(newB);
								log.debug("GenericBuffCalc.damageFeedback: place done {} {}",buff.attName, t.getName());
							}else{
								log.debug("GenericBuffCalc.damageFeedback: not trigger {} {}",r, con.paramInt);
							}
							break;
						}
					}
					break;
				}
			}while(false);
			//
			buff = buff.next;
		}
	}
}
