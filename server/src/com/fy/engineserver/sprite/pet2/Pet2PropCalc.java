package com.fy.engineserver.sprite.pet2;

import org.slf4j.Logger;

import com.fy.engineserver.datasource.skill2.GenericBuff;
import com.fy.engineserver.sprite.pet.Pet;

/**
 * 
 * create on 2013年8月24日
 */
public class Pet2PropCalc {
	/**
	 *  请勿直接使用。
	 */
	public static Pet2PropCalc inst;
	
	public static Pet2PropCalc getInst(){
		if(inst == null){
			inst = new Pet2PropCalc();
		}
		return inst;
	}
	
	public Pet2PropCalc(){
		
	}
	
	
	/**
	 * 计算力量
	 * @param pet
	 * @return
	 */
	public int calcStr(Pet  pet){
		//
		PetGrade[] levels = PetGrade.levels;
		if(levels == null || levels.length == 0 || pet.getGrade()>=levels.length){
			return 0;
		}
		//
		return levels[pet.getGrade()].liLiang;
	}

	public int calcShenFa(Pet pet) {
		//
		PetGrade[] levels = PetGrade.levels;
		if(levels == null || levels.length == 0 || pet.getGrade()>=levels.length){
			return 0;
		}
		//
		return levels[pet.getGrade()].shenFa;
	}

	public int calcLinLi(Pet pet) {
		//
		PetGrade[] levels = PetGrade.levels;
		if(levels == null || levels.length == 0 || pet.getGrade()>=levels.length){
			return 0;
		}
		//
		return levels[pet.getGrade()].linLi;
	}

	public int calcNaiLi(Pet pet) {
		//
		PetGrade[] levels = PetGrade.levels;
		if(levels == null || levels.length == 0 || pet.getGrade()>=levels.length){
			return 0;
		}
		//
		return levels[pet.getGrade()].naiLi;
	}

	public int calcDingLi(Pet pet) {
		//
		PetGrade[] levels = PetGrade.levels;
		if(levels == null || levels.length == 0 || pet.getGrade()>=levels.length){
			return 0;
		}
		//
		return levels[pet.getGrade()].dingLi;
	}
	
	public int calcBuff(Pet pet, int attId, int base){
		int ret = sumPoint(pet, attId);
		int percent = sumPercent(pet, attId);
		if(percent != 0){
			ret += base * percent / 100;
		}
		return ret;
	}
	
	public int sumPoint(Pet pet, int attId){
		int ret = 0;
		GenericBuff head = pet.pet2buff;
		while(head != null){
			if(head.percent){
				
			}else if(head.attId == attId){
				ret += head.v;
			}
			head = head.next;
		}
		return ret;
	}
	
	public int sumPercent(Pet pet, int attId){
		int ret = 0;
		GenericBuff head = pet.pet2buff;
		while(head != null){
			if(head.percent && head.attId == attId){
				ret += head.v;
			}
			head = head.next;
		}
		return ret;
	}

	public void calcFightingAtt(Pet pet, Logger log) {
//		int addHit = sumPoint(pet, GenericBuff.ATT_BaoJiLv);
//		if(addHit!=0){
//			Pet2Manager.log.debug("Pet init add hit {} to {}", addHit, pet.getName());
//			pet.setCriticalHitRateOther(pet.getCriticalHitRateOther() + addHit);
//		}		
		//处理属性转换。
		GenericBuff buff = pet.pet2buff;
		log.warn("[处理属性转换] [buff:"+(buff==null)+"] [pet:"+pet.getName()+"] [attid:"+(buff==null?"no":buff.attId)+"] [=======]");
		while(buff != null){
			do{
				if(buff.attId != GenericBuff.ATT_ZHUAN_HUAN){
					break;
				}
				log.debug("Pet2PropCalc.calcFightingAtt: 处理属性转换 {}",buff.attName);
				int srcId = buff.paramInt;
				int destId = buff.paramIntB;
				int rate = buff.v;
				if(rate <= 0){
					log.debug("Pet2PropCalc.calcFightingAtt: 错误的转换比例 {} at {}",rate,buff.attName);
					break;
				}
				int value = 0;
				switch(srcId){
				case GenericBuff.ATT_BaoJiPoint:
					value = pet.getCriticalHit();
					break;
				case GenericBuff.ATT_phy_Defence:
					value = pet.getPhyDefence();
					break;
				case GenericBuff.ATT_HP_MAX:
					value = pet.getMaxHP();
					break;
				default:
					log.debug("Pet2PropCalc.calcFightingAtt: 未处理的属性转换类型 源 {}",srcId);
					break;//break swith
				}
				if(value == 0){
					log.debug("Pet2PropCalc.calcFightingAtt: value is 0.");
					break;
				}
				log.debug("Pet2PropCalc.calcFightingAtt: base value {}, rate {}",value, rate);
				value = value * rate / 100;
				log.debug("Pet2PropCalc.calcFightingAtt: 转换属性 源 {} value {}",srcId,value);
				switch(destId){
				case GenericBuff.ATT_phy_ATK:
				case GenericBuff.ATT_GongJiLi://攻击力按物攻处理，要么在配置文件中指定
					log.debug("Pet2PropCalc.calcFightingAtt: pre {}",pet.getPhyAttack());
					pet.setPhyAttack(pet.getPhyAttack() + value);
					log.debug("Pet2PropCalc.calcFightingAtt: 增加物理攻击 加了 {} -> {}",value, pet.getPhyAttack());
					pet.setShowPhyAttack(pet.getPhyAttack());
					break;
				case GenericBuff.ATT_magic_ATK:
					pet.setMagicAttack(pet.getMagicAttack() + value);
					pet.setShowMagicAttack(pet.getMagicAttack());
					log.debug("Pet2PropCalc.calcFightingAtt: 增加法术攻击 加了 {} -> {}",value, pet.getMagicAttack());
					break;
				default:
					log.debug("Pet2PropCalc.calcFightingAtt: 未处理的属性转换类型 标 {}",destId);
					break;
				}
			}while(false);
			//
			buff=buff.next;
		}
		log.debug("Pet2PropCalc.calcFightingAtt: end");
	}
}
