package com.fy.engineserver.jsp.propertyvalue;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.datasource.language.Translate;


public class SkillTemplate {
	/** 技能类别 **/
	int type;

	public final static int SKILL_TYPE_ACTIVE = 0;
	public final static int SKILL_TYPE_PASSIVE = 1;
	public final static int SKILL_TYPE_AURA = 2;

	/** 该技能模板对应的类名 **/
	String className;

	/** 该技能模板的显示名称 **/
	String displayName;

	/** 基本信息参数 **/
	Map<String , SkillField> basicFields = new Hashtable<String, SkillField>();

	/** 主动技能 动画参数 **/
	Map<String , SkillField> activeSkillAnimationFields = new Hashtable<String, SkillField>();

	/** 后效动画参数 **/
	Map<String , SkillField> effectFields = new Hashtable<String, SkillField>();

	/** 等级相关的参数 **/
	Map<String , SkillField> levelFields = new Hashtable<String, SkillField>();

	/** 被动修改主动的Map **/
	Map<String , SkillField> mapFields = new Hashtable<String, SkillField>();

	/** 伤害数值 **/
	public Map<String , SkillField> dataFields = new Hashtable<String, SkillField>();

	public SkillTemplate(){

	}

	public void addBasicFields(){
		SkillTemplate template = this;
		SkillField skillField = null;

		skillField = new SkillField();

		skillField.setFieldName("shortDescription");
		skillField.setDefaultValue("");
		skillField.setDisplayName(Translate.text_4672);
		skillField.setDescription(Translate.text_4673);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);

		template.addBasicSkillField(skillField);

		skillField = new SkillField();

		skillField.setFieldName("colIndex");
		skillField.setDefaultValue("0");
		skillField.setDisplayName(Translate.text_4674);
		skillField.setDescription(Translate.text_4674);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);

		template.addBasicSkillField(skillField);

		skillField = new SkillField();

		skillField.setFieldName("iconId");
		skillField.setDefaultValue("-1");
		skillField.setDescription(Translate.text_4277);
		skillField.setDisplayName(Translate.text_4278);
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);

		template.addBasicSkillField(skillField);

		skillField = new SkillField();

		skillField.setFieldName("name");
		skillField.setDefaultValue(Translate.text_103);
		skillField.setDisplayName(Translate.text_4279);
		skillField.setDescription(Translate.text_4279);
		skillField.setType(String.class);

		template.addBasicSkillField(skillField);

		skillField = new SkillField();

		skillField.setFieldName("description");
		skillField.setDefaultValue(Translate.text_4280);
		skillField.setDescription(Translate.text_4281);
		skillField.setDisplayName(Translate.text_4281);
		skillField.setType(String.class);

		template.addBasicSkillField(skillField);

		skillField = new SkillField();

		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription(Translate.text_4282);
		skillField.setDisplayName(Translate.text_4282);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4283,Translate.text_4284,Translate.text_4285,Translate.text_4286,Translate.text_4287});

		template.addBasicSkillField(skillField);

		skillField = new SkillField();

		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4302);
		skillField.setDisplayName(Translate.text_4303);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4304 , Translate.text_4305,Translate.text_4306,Translate.text_4307,Translate.text_4308,Translate.text_4309,Translate.text_4310,Translate.text_4311,Translate.text_4312,Translate.text_4313,Translate.text_4314,Translate.text_4315,Translate.text_4316,Translate.text_4317 ,Translate.text_4318,Translate.text_4319,Translate.text_4320,Translate.text_4321,Translate.text_4322,Translate.text_4323,Translate.text_4324,Translate.text_4325,Translate.text_4326,Translate.text_4327,Translate.text_4328,Translate.text_4329,Translate.text_4330,Translate.text_4331,Translate.text_4332,Translate.text_4333,Translate.text_4334,Translate.text_4335,Translate.text_4336,Translate.text_4337,Translate.text_4338,Translate.text_4339,Translate.text_4340,Translate.text_4341,Translate.text_4342,Translate.text_4343,Translate.text_4344,Translate.text_4345,Translate.text_4346,Translate.text_4347,Translate.text_4348,Translate.text_4349,Translate.text_4350,Translate.text_4351,Translate.text_4352,Translate.text_4353,Translate.text_4354,Translate.text_4355,Translate.text_4356,Translate.text_4357,Translate.text_4358,Translate.text_4359,Translate.text_4360,Translate.text_4361,Translate.text_4362,Translate.text_4363,Translate.text_4364,Translate.text_4365,Translate.text_4366,Translate.text_4367,Translate.text_4368,Translate.text_4369,Translate.text_4370,Translate.text_4371,Translate.text_4372,Translate.text_4373,Translate.text_4374,Translate.text_4375,Translate.text_4376,Translate.text_4377,Translate.text_4378,Translate.text_4379,Translate.text_4380,Translate.text_4381,Translate.text_4382,Translate.text_4383,Translate.text_4384,Translate.text_4385,Translate.text_4386,Translate.text_4387,Translate.text_4388,Translate.text_4389,Translate.text_4390,Translate.text_4391,Translate.text_4392,Translate.text_4393,Translate.text_4394,Translate.text_4395,Translate.text_4396,Translate.text_4397,Translate.text_4398,Translate.text_4399,Translate.text_4400,Translate.text_4401,Translate.text_4402,Translate.text_4403,Translate.text_4404});

		template.addBasicSkillField(skillField);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	public void addBasicSkillField(SkillField skillField){
		basicFields.put(skillField.getFieldName(), skillField);
	}
	
	public void addActiveSkillAnimationField(SkillField skillField){
		activeSkillAnimationFields.put(skillField.getFieldName(), skillField);
	}
	
	public SkillField[] getActiveSkillAnimationFields(){
		return activeSkillAnimationFields.values().toArray(new SkillField[0]);
	}
	
	public SkillField getActiveSkillAnimationField(String name){
		return activeSkillAnimationFields.get(name);
	}
	
	public void addEffectAnimationField(SkillField skillField){
		effectFields.put(skillField.getFieldName(), skillField);
	}
	
	public SkillField[] getEffectAnimationFields(){
		return effectFields.values().toArray(new SkillField[0]);
	}
	
	public SkillField[] getBasicSkillFields(){
		return basicFields.values().toArray(new SkillField[0]);
	}
	
	public void addMapFields(SkillField skillField){
		mapFields.put(skillField.getFieldName(), skillField);
	}
	
	public SkillField[] getMapFields(){
		return mapFields.values().toArray(new SkillField[0]);
	}
	
	public void addDataFields(SkillField skillField){
		dataFields.put(skillField.getFieldName(), skillField);
	}
	
	public SkillField[] getDataFields(){
		return dataFields.values().toArray(new SkillField[0]);
	}
	
	public void addLevelSkillField(SkillField skillField){
		levelFields.put(skillField.getFieldName(), skillField);
	}
	
	public SkillField[] getLevelFields(){
		return levelFields.values().toArray(new SkillField[0]);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public SkillField[] getSkillFields(){
		List<SkillField> fields = new ArrayList<SkillField>();

		fields.addAll(basicFields.values());
		fields.addAll(levelFields.values());
		fields.addAll(activeSkillAnimationFields.values());
		fields.addAll(effectFields.values());
		fields.addAll(mapFields.values());
		fields.addAll(dataFields.values());

		return fields.toArray(new SkillField[0]);
	}

	public SkillField getSkillFieldByName(String name){
		SkillField[] fields = getSkillFields();
		for (int i = 0; i < fields.length; i++) {
			if(fields[i].getFieldName().equals(name)){
				return fields[i];
			}
		}
		return null;
	}

	public boolean hasField(String fieldName){
		if(fieldName.equals("id") || fieldName.equals("name")){
			return true;
		}

		if(mapFields.containsKey(fieldName) || dataFields.containsKey(fieldName)){
			return false;
		}

		SkillField[] fs = basicFields.values().toArray(new SkillField[0]);
		for (int i = 0; i < fs.length; i++) {
			if(fs[i].getFieldName().equals(fieldName)){
				return true;
			}
		}

		fs = levelFields.values().toArray(new SkillField[0]);
		for (int i = 0; i < fs.length; i++) {
			if(fs[i].getFieldName().equals(fieldName)){
				return true;
			}
		}

		fs = activeSkillAnimationFields.values().toArray(new SkillField[0]);
		for (int i = 0; i < fs.length; i++) {
			if(fs[i].getFieldName().equals(fieldName)){
				return true;
			}
		}

		fs = effectFields.values().toArray(new SkillField[0]);
		for (int i = 0; i < fs.length; i++) {
			if(fs[i].getFieldName().equals(fieldName)){
				return true;
			}
		}

		fs = mapFields.values().toArray(new SkillField[0]);
		for (int i = 0; i < fs.length; i++) {
			if(fs[i].getFieldName().equals(fieldName)){
				return true;
			}
		}

		fs = dataFields.values().toArray(new SkillField[0]);
		for (int i = 0; i < fs.length; i++) {
			if(fs[i].getFieldName().equals(fieldName)){
				return true;
			}
		}

		return false;
	}
}
