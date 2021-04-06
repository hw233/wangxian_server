package com.fy.engineserver.datasource.skill2;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.slf4j.Logger;

import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.career.SkillInfo;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.pet2.PetGrade;

/**
 * 
 * create on 2013年8月30日
 */
public class GenericSkillManager {
	public static Map<String, Integer> attName2id = new HashMap<String, Integer>();
	public Map<Integer, GenericSkill> maps;
	public static GenericSkillManager inst;
	public static GenericSkillManager getInst(){
		if(inst == null){
			new GenericSkillManager();
		}
		return inst;
	}
	/**
	 * 请勿直接调用。
	 */
	public GenericSkillManager(){
		inst = this;
		maps = new HashMap<Integer, GenericSkill>();
		if(attName2id.size() == 0){
			buildAttName2id();
		}
	}
	
	public void buildAttName2id() {
		attName2id.put("定力", GenericBuff.ATT_DingLi);
		attName2id.put("耐力", GenericBuff.ATT_NaiLi);
		attName2id.put("身法", GenericBuff.ATT_ShenFa);
		attName2id.put("力量", GenericBuff.ATT_LiLiang);
		attName2id.put("灵力", GenericBuff.ATT_LinLi);
		attName2id.put("暴击系数", GenericBuff.ATT_BaoJiXiShu);
		attName2id.put("暴击率", GenericBuff.ATT_BaoJiLv);
		attName2id.put("暴击", GenericBuff.ATT_BaoJiPoint);
		attName2id.put("雷", GenericBuff.ATT_LEI);
		attName2id.put("冰", GenericBuff.ATT_LEI);
		attName2id.put("风", GenericBuff.ATT_LEI);
		attName2id.put("人物经验加成", GenericBuff.ATT_PLAYER_EXP_PERCENT);
		attName2id.put("攻击力", GenericBuff.ATT_GongJiLi);
		attName2id.put("外攻", GenericBuff.ATT_GongJiLi);//程序里都按物攻算了。
		attName2id.put("物理防御", GenericBuff.ATT_phy_Defence);
		attName2id.put("物理防御", GenericBuff.ATT_phy_Defence);
		attName2id.put("血量上限", GenericBuff.ATT_HP_MAX);
		attName2id.put("血上限", GenericBuff.ATT_HP_MAX);
		//
		attName2id.put("风属性抗性", GenericBuff.ATT_Feng_Kang);
		attName2id.put("火属性抗性", GenericBuff.ATT_Huo_Kang);
		attName2id.put("雷属性抗性", GenericBuff.ATT_Lei_Kang);
		attName2id.put("冰属性抗性", GenericBuff.ATT_Bing_Kang);
		//
		attName2id.put("风属性攻击", GenericBuff.ATT_Feng_Gong);
		attName2id.put("火属性攻击", GenericBuff.ATT_Huo_Gong);
		attName2id.put("雷属性攻击", GenericBuff.ATT_Lei_Gong);
		attName2id.put("冰属性攻击", GenericBuff.ATT_Bing_Gong);
		attName2id.put("增加自身速度", GenericBuff.ATT_SPEED_POINT);
		attName2id.put("反伤", 0);
		attName2id.put("外功", GenericBuff.ATT_phy_ATK);
		attName2id.put("外攻", GenericBuff.ATT_phy_ATK);
		attName2id.put("内攻", GenericBuff.ATT_magic_ATK);
		attName2id.put("内功", GenericBuff.ATT_magic_ATK);
	}
	public GenericSkill parse(HSSFRow row, Logger log){
		GenericSkill ret = new GenericSkill();
		ret.setId(PetGrade.getInt(row, 0, log));
		ret.setName(PetGrade.getString(row, 1, log));
		ret.setIconId(PetGrade.getString(row, 2, log));
		ret.setDescription(PetGrade.getString(row, 3, log));
		GenericBuff buff = new GenericBuff();
		String attName = PetGrade.getString(row, 4, log);
		buff.attName = attName;
		String performKind = PetGrade.getString(row, 6, log);
		if(attName.equals("-") || attName.isEmpty()){
			ret.setMaxLevel((byte) 1);
			buff.values = new int[0];
		}else if(attName.startsWith("前减后加")){
			buff.attId = GenericBuff.ATT_SubA_AddB;
			int[] values = parseValuesArr(row, log);
			ret.setMaxLevel((byte) values.length);
			buff.values = values;
			parseSubA_addB(buff,attName, log);
		}else if (attName.startsWith("致命恢复")) {
			buff.attId = GenericBuff.ATT_INVINCIBLE;
			int[] values = parseValuesArr(row, log);
			ret.setMaxLevel((byte) values.length);
			buff.values = values;
		}else if(attName.equals("几率加倍伤害")){
			buff.attId = GenericBuff.ATT_DMG_SCALE_RATE;
			buff.attName = performKind;
			parseRatePair(buff, PetGrade.getString(row, 5, log), log);
		}else if (attName.equals("反伤及血上限")) {
			buff.attId = GenericBuff.ATT_HPANDANTI_SCALE_RATE;
			buff.attName = performKind;
			parseRatePair(buff, PetGrade.getString(row, 5, log), log);
		}else if (attName.equals("加血并移除负面状态")) {
			buff.attId = GenericBuff.ATT_REMOVE_DEBUFF;
			buff.attName = performKind;
			parseRatePair(buff, PetGrade.getString(row, 5, log), log);
		}else if (attName.equals("全能法戒")) {
			buff.attId = GenericBuff.ATT_QUANNENGFAJIE;
			buff.attName = performKind;
			parseRatePair(buff, PetGrade.getString(row, 5, log), log);
		}else if (attName.equals("光环技能")) {
			buff.attId = GenericBuff.ATT_AURA_SKILL;
			performKind = PetGrade.getString(row, 9, log);
			buff.attName = performKind;
			String valueStr = PetGrade.getString(row, 5, log);
			String[] aa = valueStr.split("-");
			int[] values = new int[aa.length];
			for (int k=0; k<values.length; k++) {
				values[k] = Integer.parseInt(aa[k]);
			}
			ret.setMaxLevel((byte) values.length);
			buff.values = values;
		}else if(attName.equals("千层塔")){
			buff.attId = GenericBuff.ATT_Tower;
			int[] values = parseValuesArr(row, log);
			ret.setMaxLevel((byte) values.length);
			buff.values = values;
			buff.attName = performKind;
		}else if(attName.equals("属性转换")){
			buff.attId = GenericBuff.ATT_ZHUAN_HUAN;
			int[] values = parseValuesArr(row, log);
			ret.setMaxLevel((byte) values.length);
			buff.values = values;
			buff.attName = performKind;
			log.debug("[宠物技能初始化] [attName:"+attName+"] [技能名："+ret.getName()+"] [buffid:"+buff.attId+"] [==属性==]");
			parseZhuanHuanAttId(buff, performKind, log);
		}else if(attName.equals("特定目标")){
			buff.attId = GenericBuff.ATT_KanRenXiaCai;
			int[] values = parseValuesArr(row, log);
			ret.setMaxLevel((byte) values.length);
			buff.values = values;
			buff.attName = performKind;
			buff.paramInt = PetGrade.getInt(row, 6, log);
		}else if(attName.equals("命中附加伤害")){
			buff.attId = GenericBuff.ATT_APPEND_DAMAGE;
			int[] values = parseValuesArr(row, log);
			ret.setMaxLevel((byte) values.length);
			buff.values = values;
			buff.attName = performKind;
			buff.paramInt = getAttId(performKind, log);
		}else if(attName.equals("buff")){
			buff.attId = GenericBuff.ATT_ADD_SUB_BUFF;
			int[] values = parseValuesArr(row, log);
			ret.setMaxLevel((byte) values.length);
			buff.values = values;
			performKind = PetGrade.getString(row, 9, log);
			buff.attName = performKind;
			BuffTemplate bt = parseBuffTemplate(performKind);
			if(bt == null){
				if(ret.getId() != 110062){
					log.error("parse buff应用配置错误 {}",performKind);
					buff.attId = GenericBuff.ATT_ERROR;
					try {
						throw new Exception("[parse buff出错][buffname:" + performKind + "]");
					} catch (Exception e) {
						// TODO Auto-generated catch block/
						log.error("", e);
					}
				}
			}else{
				buff.setTemplate(bt);
			}
			String cdt = PetGrade.getString(row, 7, log);
			buff.triggerCondtion = parseCondtion(cdt, log);
			try{
				buff.lastMS = Long.parseLong(PetGrade.getString(row, 8, log).replace(".0", ""));
			}catch(NumberFormatException e){
				log.error("GenericSkillManager.parse: sheet {} row {} buff持续时间不对", row.getSheet().getSheetName(), performKind);
			}
			Skill.logger.warn("[宠物技能buff加载] [id:"+ret.getId()+"] [name:"+ret.getName()+"] [desc:"+ret.getDescription()+"] [attName:"+attName+"] [cdt:"+cdt+"] ["+(buff.triggerCondtion!=null?buff.triggerCondtion.conditionType:"null")+"]");
			
		}else{
			buff.attId = getAttId(attName, log);
			int[] values = parseValuesArr(row, log);
			ret.setMaxLevel((byte) values.length);
			buff.values = values;
		}
		log.warn("[宠物技能初始化] [attName:"+attName+"] [技能名："+ret.getName()+"] [buffid:"+buff.attId+"]");
		buff.percent = "百分比".equals(performKind);
		ret.buff = buff;
		maps.put(ret.getId(), ret);
		return ret;
	}
	public void parseSubA_addB(GenericBuff buff, String attName, Logger log) {
		String[] parts = attName.split("-");
		if(parts.length != 3){
			log.error("parseSubA_addB 错误的数值对",attName);
			return;
		}
		buff.paramInt = getAttId(parts[1], log);	
		buff.paramIntB = getAttId(parts[2], log);	
	}
	public void parseRatePair(GenericBuff buff, String string, Logger log) {
		String[] parts = string.split("-");
		if(parts.length != 2){
			log.error("错误的数值对",string);
			return;
		}
		buff.values = new int[]{1,1,1,1,1};//只是为了程序健壮性
		buff.paramInt = Integer.parseInt(parts[0]);
		buff.paramIntB = Integer.parseInt(parts[1]);
	}
	public void parseZhuanHuanAttId(GenericBuff buff, String performKind, Logger log) {
		String[] parts = performKind.split("-");
		if(parts.length != 2){
			log.error("错误的属性转换配置 {} ",performKind);
			return;
		}
		buff.paramInt = getAttId(parts[0], log);
		buff.paramIntB = getAttId(parts[1], log);
	}
	public int[] parseValuesArr(HSSFRow row, Logger log) {
		String valueStr = PetGrade.getString(row, 5, log);
		if(valueStr.equals("-")){
			valueStr = "0";
		}
		String[ ] vs = valueStr.split(",");
		int len = vs.length;
		int[] values = new int[len];
		for(int i=0; i<len; i++){
			try{
				values[i] = (int) Float.parseFloat(vs[i]);
			}catch(Exception e){
				log.error("sheet {} row {} error {}",
						new Object[]{row.getSheet().getSheetName(), row.getRowNum(), e});
			}
		}
		return values;
	}
	public BuffTemplate parseBuffTemplate(String name) {
		BuffTemplateManager bm = BuffTemplateManager.getInstance();
		return bm.getBuffTemplateByName(name);
	}
	public BuffCondition parseCondtion(String cdt, Logger log) {
		if(cdt == null || cdt.isEmpty()){
			log.error("parseCondtion condition string is null");
			return null;
		}
		if(cdt.startsWith("atkTimes")){
			String[] parts = cdt.split(":");
			int v = Integer.parseInt(parts[1]);
			BuffCondition con = new BuffCondition(BuffCondition.CON_ATK_TIMES, v);
			return con;
		}else if(cdt.startsWith("rateWhenAtk")){
			String[] parts = cdt.split(":");
			int v = Integer.parseInt(parts[1]);
			BuffCondition con = new BuffCondition(BuffCondition.CON_RATE_WHEN_ATK, v);
			return con;
		}else if(cdt.startsWith("ratePostHurt")){
			String[] parts = cdt.split(":");
			int v = Integer.parseInt(parts[1]);
			BuffCondition con = new BuffCondition(BuffCondition.CON_RATE_POST_HURT, v);
			return con;
		}
		log.error("parseCondtion 不可识别的触发类型 {}", cdt);
		return null;
	}
	public int getAttId(String string, Logger log) {
		Integer v = attName2id.get(string);
		if(v == null){
			log.error("不可识别的属性 {}", string);
			return 0;
		}
		return v;
	}
	
	public SkillInfo buildSkillInfo(int id, Logger log){
		GenericSkill skill = maps.get(id);
		SkillInfo skillInfos = new SkillInfo();
		if(skill == null){
			if(id>0){
				log.error("buildSkillInfo 没有找到技能 {}", id);
			}
			skillInfos.setId(0);
			skillInfos.setIconId("");
			skillInfos.setName("?");
			return skillInfos;
		}
		skillInfos.setId(id);
		skillInfos.setIconId(skill.getIconId());
		// skillInfos.setInfo(pet, skill);
		skillInfos.setMaxLevel(skill.getMaxLevel());
		skillInfos.setName(skill.getName());
//		skillInfos.setDescription(skill.getDescription());
		skillInfos.setSkillType(skill.getSkillType());
		skillInfos.setColumnIndex((byte) skill.getColIndex());
		skillInfos.setNeedCareerThreadPoints(skill.getNeedCareerThreadPoints());
		return skillInfos;
	}
	
	public String getSkillDesc(int id){
		GenericSkill sk = maps.get(id);
		if(sk == null){
			return "";
		}
		return sk.getName()+"\n"+sk.getDescription();
	}
	public String getSkillIcon(int id){
		GenericSkill sk = maps.get(id);
		if(sk == null){
			return "";
		}
		return sk.getIconId();
	}
}
