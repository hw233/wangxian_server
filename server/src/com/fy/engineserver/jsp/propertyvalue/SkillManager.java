package com.fy.engineserver.jsp.propertyvalue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.datasource.language.Translate;

public class SkillManager {
	
	private final static String DATA_FILE = "careerandskills.xml";
	
	File file;
	
	Map<String, SkillTemplate> templates = new HashMap<String, SkillTemplate>();
	
	public final static int CATEGORY_WUDANG_LINE1 = 0;
	public final static int CATEGORY_WUDANG_LINE2 = 1;
	public final static int CATEGORY_EMEI_LINE1 = 2;
	public final static int CATEGORY_EMEI_LINE2 = 3;
	public final static int CATEGORY_MINGJIAO_LINE1 = 4;
	public final static int CATEGORY_MINGJIAO_LINE2 = 5;
	public final static int CATEGORY_WUDU_LINE1 = 6;
	public final static int CATEGORY_WUDU_LINE2 = 7;
	public final static int CATEGORY_SHAOLIN_LINE1 = 8;
	public final static int CATEGORY_SHAOLIN_LINE2 = 9;
	public final static int CATEGORY_MONSTER = 10;
	//public Map<Integer , Career> careers = new HashMap<Integer, Career>();
	
	//public MonsterCategory monsterCategory;
	
	public SkillTemplate commonAttackSkillTemplate;
	//public CommonAttackCategory commonAttackCategory;
	
	public final static int SHAOLIN_LINE1 = 0;
	public final static int SHAOLIN_LINE2 = 1;
	
	public final static int WUDANG_LINE1 = 2;
	public final static int WUDANG_LINE2 = 3;
	
	public final static int EMEI_LINE1 = 4;
	public final static int EMEI_LINE2 = 5;
	
	public final static int MINGJIAO_LINE1 = 6;
	public final static int MINGJIAO_LINE2 = 7;
	
	public final static int WUDU_LINE1 = 8;
	public final static int WUDU_LINE2 = 9;
	
	private static boolean modified;
	private static SkillManager instance;
	
	public static SkillManager getInstance(){
		return instance;
	}
	public static boolean isModified(){
		return modified;
	}
	
	public static void setModified(boolean modified){
		SkillManager.modified = modified;
	}
	
	//技能的分类  包括各个职业 以及 怪物的技能
	public Map<Integer , SkillCategory> categories = new HashMap<Integer, SkillCategory>();
	
	public void init(){
		//主动技能
		initCommonAttackSkill( );
//		initSkillWithoutEffect( );
		initSkillWithoutEffectAndQuickMove( );
		initSkillWithoutTraceAndOnTeamMember( );
		initSkillWithoutTraceAndWithMatrix( );
		initSkillWithoutTraceAndWithRange( );
		initSkillWithoutTraceAndWithTargetOrPosition( );
		initSkillWithTraceAndDirectionOrTarget( );
		//被动技能
		initIncreaseLiliangPassiveSkill( );
		initIncreaseMinjiePassiveSkill( );
		initIncreaseTizhiPassiveSkill( );
		initIncreaseZhiliPassiveSkill( );
		initAssistActiveSkillPassiveSkill( );
		initIncreaseBaoJiPassiveSkill( );
		initIncreaseDuoBiPassiveSkill( );
		initIncreaseFaFangPassiveSkill( );
		initIncreaseFaGongPassiveSkill( );
		initIncreaseHpAndMpPassiveSkill( );
		initIncreaseHpPassiveSkill( );
		initIncreaseHpRecoveryAndMpRecoveryPassiveSkill( );
		initIncreaseHpRecoveryPassiveSkill( );
		initIncreaseMingZhongAndDuoBiPassiveSkill( );
		initIncreaseMingZhongPassiveSkill( );
		initIncreaseMpPassiveSkill( );
		initIncreaseMpRecoveryPassiveSkill( );
		initIncreaseWuFangAndFaFangPassiveSkill( );
		initIncreaseWuFangPassiveSkill( );
		initIncreaseWuGongAndFaGongPassiveSkill( );
		initIncreaseWuGongPassiveSkill( );
		//光环技能
		initAuraSkill( );
	}
	
	private void initAuraSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.AuraSkill");
		template.setDisplayName(Translate.text_4276);
		template.setType(SkillTemplate.SKILL_TYPE_AURA);
		
		//基本参数
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField.setFieldName("rangeWidth");
		skillField.setDefaultValue("30");
		skillField.setDescription(Translate.text_4288);
		skillField.setDisplayName(Translate.text_4289);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("rangeHeight");
		skillField.setDefaultValue("30");
		skillField.setDescription(Translate.text_4290);
		skillField.setDisplayName(Translate.text_4291);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("rangeType");
		skillField.setDefaultValue("30");
		skillField.setDescription(Translate.text_4292);
		skillField.setDisplayName(Translate.text_4293);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1" , "2" , "3" , "4"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4294,Translate.text_4295,Translate.text_4296,Translate.text_4297,Translate.text_4298});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("auraType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4299);
		skillField.setDisplayName(Translate.text_4300);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);

//		DField df = auraMapping.get(0);
//		
//		ArrayList<String> al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		String ss[] = al.toArray(new String[0]);
//		String ssIndex[] = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffName");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4301);
		skillField.setDisplayName(Translate.text_4301);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{""});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491});
		
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
		
		//级别相关
		skillField = new SkillField();
		
		skillField.setFieldName("buffLevel");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4405);
		skillField.setDisplayName(Translate.text_4406);
		skillField.setType(int[].class);
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initAssistActiveSkillPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.AssistActiveSkillPassiveSkill");
		template.setDisplayName(Translate.text_4407);
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField = new SkillField();
		
		skillField.setFieldName("desps");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4408);
		skillField.setDisplayName(Translate.text_4408);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		skillField.setSelectValues(new String[]{""});
		skillField.setSelectDisplayValues(new String[]{""});
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initIncreaseLiliangPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseLiliangPassiveSkill");
		template.setDisplayName(Translate.text_3255);
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField = new SkillField();
		
		skillField.setFieldName("strength");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4409);
		skillField.setDisplayName(Translate.text_4409);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initIncreaseMinjiePassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseMinjiePassiveSkill");
		template.setDisplayName(Translate.text_3262);
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField = new SkillField();
		
		skillField.setFieldName("dexterity");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4410);
		skillField.setDisplayName(Translate.text_4410);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initIncreaseTizhiPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseTizhiPassiveSkill");
		template.setDisplayName(Translate.text_3273);
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField = new SkillField();
		
		skillField.setFieldName("constitution");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4411);
		skillField.setDisplayName(Translate.text_4411);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initIncreaseZhiliPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseZhiliPassiveSkill");
		template.setDisplayName(Translate.text_4412);
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField = new SkillField();
		
		skillField.setFieldName("spellpower");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4413);
		skillField.setDisplayName(Translate.text_4413);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initIncreaseBaoJiPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseBaoJiPassiveSkill");
		template.setDisplayName(Translate.text_3139);
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField = new SkillField();
		
		skillField.setFieldName("baoJi");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4414);
		skillField.setDisplayName(Translate.text_4414);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initIncreaseDuoBiPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseDuoBiPassiveSkill");
		template.setDisplayName(Translate.text_4415);
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField = new SkillField();
		
		skillField.setFieldName("dodgeC");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4416);
		skillField.setDisplayName(Translate.text_4416);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseFaFangPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseFaFangPassiveSkill");
		template.setDisplayName(Translate.text_4417);
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField = new SkillField();
		
		skillField.setFieldName("resistance");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4418);
		skillField.setDisplayName(Translate.text_4418);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseFaGongPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseFaGongPassiveSkill");
		template.setDisplayName(Translate.text_4419);
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField = new SkillField();
		
		skillField.setFieldName("spellDamageUpperLimitC");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4420);
		skillField.setDisplayName(Translate.text_4420);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("spellDamageLowerLimitC");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4421);
		skillField.setDisplayName(Translate.text_4421);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseHpAndMpPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseHpAndMpPassiveSkill");
		template.setDisplayName(Translate.text_4422);
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField = new SkillField();
		
		skillField.setFieldName("totalHpC");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4423);
		skillField.setDisplayName(Translate.text_4423);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("totalMpC");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4424);
		skillField.setDisplayName(Translate.text_4424);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseHpPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseHpPassiveSkill");
		template.setDisplayName(Translate.text_3290);
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField = new SkillField();
		
		skillField.setFieldName("totalHpC");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4423);
		skillField.setDisplayName(Translate.text_4423);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseHpRecoveryAndMpRecoveryPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseHpRecoveryAndMpRecoveryPassiveSkill");
		template.setDisplayName(Translate.text_4425);
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField = new SkillField();
		
		skillField.setFieldName("hpRecovery");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4426);
		skillField.setDisplayName(Translate.text_4427);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("mpRecovery");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4428);
		skillField.setDisplayName(Translate.text_4429);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseHpRecoveryPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseHpRecoveryPassiveSkill");
		template.setDisplayName(Translate.text_4430);
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField = new SkillField();
		
		skillField.setFieldName("hpRecovery");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4426);
		skillField.setDisplayName(Translate.text_4427);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseMingZhongAndDuoBiPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseMingZhongAndDuoBiPassiveSkill");
		template.setDisplayName(Translate.text_4431);
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		
		skillField = new SkillField();
		
		skillField.setFieldName("dodgeC");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4416);
		skillField.setDisplayName(Translate.text_4416);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("attackRatingC");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4432);
		skillField.setDisplayName(Translate.text_4432);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseMingZhongPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseMingZhongPassiveSkill");
		template.setDisplayName(Translate.text_3324);
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField = new SkillField();
		
		skillField.setFieldName("attackRatingC");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4432);
		skillField.setDisplayName(Translate.text_4432);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseMpPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseMpPassiveSkill");
		template.setDisplayName(Translate.text_4433);
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField = new SkillField();
		
		skillField.setFieldName("totalMpC");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4424);
		skillField.setDisplayName(Translate.text_4424);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseMpRecoveryPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseMpRecoveryPassiveSkill");
		template.setDisplayName(Translate.text_4434);
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField = new SkillField();
		
		skillField.setFieldName("mpRecovery");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4428);
		skillField.setDisplayName(Translate.text_4429);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseWuFangAndFaFangPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseWuFangAndFaFangPassiveSkill");
		template.setDisplayName(Translate.text_4435);
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField = new SkillField();
		
		skillField.setFieldName("physicalDefenceC");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4436);
		skillField.setDisplayName(Translate.text_4436);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("resistance");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4437);
		skillField.setDisplayName(Translate.text_4437);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseWuFangPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseWuFangPassiveSkill");
		template.setDisplayName(Translate.text_4438);
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField = new SkillField();
		
		skillField.setFieldName("physicalDefenceC");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4436);
		skillField.setDisplayName(Translate.text_4436);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseWuGongAndFaGongPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseWuGongAndFaGongPassiveSkill");
		template.setDisplayName(Translate.text_4439);
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField = new SkillField();
		
		skillField.setFieldName("physicalDamageUpperLimitC");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4440);
		skillField.setDisplayName(Translate.text_4440);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("physicalDamagerLowerLimitC");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4441);
		skillField.setDisplayName(Translate.text_4441);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("spellDamageUpperLimitC");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4442);
		skillField.setDisplayName(Translate.text_4442);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("spellDamageLowerLimitC");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4443);
		skillField.setDisplayName(Translate.text_4443);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"1%","2%","3%","4%","5%","6%","7%","8%","9%","10%"});
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initIncreaseWuGongPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseWuGongPassiveSkill");
		template.setDisplayName(Translate.text_4444);
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField = new SkillField();
		
		skillField.setFieldName("physicalDamageUpperLimitC");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName(Translate.text_4440);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("physicalDamagerLowerLimitC");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName(Translate.text_4441);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initSkillWithoutEffect(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		//普通攻击技能 
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.activeskills.SkillWithoutEffect");
		template.setDisplayName(Translate.text_4445);
		template.setType(SkillTemplate.SKILL_TYPE_ACTIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("range");
		skillField.setDefaultValue("50");
		skillField.setDescription(Translate.text_4446);
		skillField.setDisplayName(Translate.text_4446);
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField.setFieldName("duration1");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4447);
		skillField.setDisplayName(Translate.text_4448);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "500","550","600","650","700","750","800","850","900","1000","1200","1500","2000","2500","2700","3000","3500","4000","5000","8000","10000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491 , Translate.text_4449,Translate.text_4450,Translate.text_4451,Translate.text_4452,Translate.text_4453,Translate.text_4454,Translate.text_4455,Translate.text_4456,Translate.text_4457,Translate.text_4458,Translate.text_4459,Translate.text_4460,Translate.text_4461,Translate.text_4462,Translate.text_4463,Translate.text_4464,Translate.text_4465,Translate.text_4466,Translate.text_4467,Translate.text_4468,Translate.text_4469});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration2");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4470);
		skillField.setDisplayName(Translate.text_4471);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4472,Translate.text_4473,Translate.text_4474,Translate.text_4475,Translate.text_4476,Translate.text_4477,Translate.text_4478,Translate.text_4479,Translate.text_4480,Translate.text_4481,Translate.text_4482,Translate.text_4483,Translate.text_4484,Translate.text_4485,Translate.text_4486,Translate.text_4487,Translate.text_4488,Translate.text_4489,Translate.text_4490,Translate.text_4491,Translate.text_4492,Translate.text_4493,Translate.text_4494,Translate.text_4495,Translate.text_4496,Translate.text_4497,Translate.text_4498,Translate.text_4499,Translate.text_4500,Translate.text_4501,Translate.text_4502,Translate.text_4503,Translate.text_4504,Translate.text_4505,Translate.text_4506,Translate.text_4507,Translate.text_4508,Translate.text_4509,Translate.text_4510,Translate.text_4511,Translate.text_4512,Translate.text_4513,Translate.text_4514,Translate.text_4515,Translate.text_4516,Translate.text_4517,Translate.text_4518,Translate.text_4519,Translate.text_4520,Translate.text_4521});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration3");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4522);
		skillField.setDisplayName(Translate.text_4523);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1000","2000","3000","4000","5000","6000","7000","8000","9000","10000","11000","12000","13000","14000","15000","16000","17000","18000","19000","20000","21000","22000","23000","24000","25000","26000","27000","28000","29000","30000","31000","32000","33000","34000","35000","36000","37000","38000","39000","40000","41000","42000","43000","44000","45000","46000","47000","48000","49000","50000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4458,Translate.text_4461,Translate.text_4464,Translate.text_4466,Translate.text_4467,Translate.text_4524,Translate.text_4525,Translate.text_4468,Translate.text_4526,Translate.text_4469,Translate.text_4527,Translate.text_4528,Translate.text_4529,Translate.text_4530,Translate.text_4531,Translate.text_4532,Translate.text_4533,Translate.text_4534,Translate.text_4535,Translate.text_4536,Translate.text_4537,Translate.text_4538,Translate.text_4539,Translate.text_4540,Translate.text_4541,Translate.text_4542,Translate.text_4543,Translate.text_4544,Translate.text_4545,Translate.text_4546,Translate.text_4547,Translate.text_4548,Translate.text_4549,Translate.text_4550,Translate.text_4551,Translate.text_4552,Translate.text_4553,Translate.text_4554,Translate.text_4555,Translate.text_4556,Translate.text_4557,Translate.text_4558,Translate.text_4559,Translate.text_4560,Translate.text_4561,Translate.text_4562,Translate.text_4563,Translate.text_4564,Translate.text_4565,Translate.text_4566});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("style1");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4567);
		skillField.setDisplayName(Translate.text_4567);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);

//		DField df = playerMapping.get(5);
//		
//		ArrayList<String> al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		String ss[] = al.toArray(new String[0]);
//		String ssIndex[] = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		
		template.addActiveSkillAnimationField(skillField);

		
		skillField = new SkillField();
		
		skillField.setFieldName("style2");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4568);
		skillField.setDisplayName(Translate.text_4568);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		
//		df = playerMapping.get(5);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectiveTimes");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4569);
		skillField.setDisplayName(Translate.text_4570);
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("enableWeaponType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4571);
		skillField.setDisplayName(Translate.text_4571);
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4572 , Translate.text_4573});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("weaponTypeLimit");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4574);
		skillField.setDisplayName(Translate.text_4575);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		
		
//		df = playerMapping.get(4);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("attackType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4576);
		skillField.setDisplayName(Translate.text_4577);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4578 , Translate.text_4579});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("followByCommonAttack");
		skillField.setDefaultValue("true");
		skillField.setDescription(Translate.text_4580);
		skillField.setDisplayName(Translate.text_4581);
		skillField.setType(Boolean.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"true","false"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_125 , Translate.text_126});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffTargetType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4582);
		skillField.setDisplayName(Translate.text_4583);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4584 , Translate.text_4585});
		
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
		
		//后效参数
//		skillField = new SkillField();
//		
//		skillField.setFieldName("effectType");
//		skillField.setDefaultValue("0");
//		skillField.setDescription("后效的类型，比如闪电，落雷等");
//		skillField.setDisplayName("后效的类型");
//		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		
//		df = summonedMapping.get(0);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
//		
//		template.addEffectAnimationField(skillField);
//		
//		skillField = new SkillField();
//		
//		skillField.setFieldName("effectLastTime");
//		skillField.setDefaultValue("0");
//		skillField.setDescription("后效持续的时间，为毫秒");
//		skillField.setDisplayName("后效持续的时间");
//		skillField.setType(Byte.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		skillField.setSelectValues(new String[]{"0" , "100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
//		skillField.setSelectDisplayValues(new String[]{"无" , "100毫秒","200毫秒","300毫秒","400毫秒","500毫秒","600毫秒","700毫秒","800毫秒","900毫秒","1000毫秒","1100毫秒","1200毫秒","1300毫秒","1400毫秒","1500毫秒","1600毫秒","1700毫秒","1800毫秒","1900毫秒","2000毫秒","2100毫秒","2200毫秒","2300毫秒","2400毫秒","2500毫秒","2600毫秒","2700毫秒","2800毫秒","2900毫秒","3000毫秒","3100毫秒","3200毫秒","3300毫秒","3400毫秒","3500毫秒","3600毫秒","3700毫秒","3800毫秒","3900毫秒","4000毫秒","4100毫秒","4200毫秒","4300毫秒","4400毫秒","4500毫秒","4600毫秒","4700毫秒","4800毫秒","4900毫秒","5000毫秒"});
//		
//		template.addEffectAnimationField(skillField);
//		
//		skillField = new SkillField();
//		
//		skillField.setFieldName("effectExplosionLastTime");
//		skillField.setDefaultValue("0");
//		skillField.setDescription("后效持续的时间过后，爆炸持续的时间");
//		skillField.setDisplayName("爆炸持续的时间");
//		skillField.setType(Byte.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		skillField.setSelectValues(new String[]{"0" , "100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
//		skillField.setSelectDisplayValues(new String[]{"无" , "100毫秒","200毫秒","300毫秒","400毫秒","500毫秒","600毫秒","700毫秒","800毫秒","900毫秒","1000毫秒","1100毫秒","1200毫秒","1300毫秒","1400毫秒","1500毫秒","1600毫秒","1700毫秒","1800毫秒","1900毫秒","2000毫秒","2100毫秒","2200毫秒","2300毫秒","2400毫秒","2500毫秒","2600毫秒","2700毫秒","2800毫秒","2900毫秒","3000毫秒","3100毫秒","3200毫秒","3300毫秒","3400毫秒","3500毫秒","3600毫秒","3700毫秒","3800毫秒","3900毫秒","4000毫秒","4100毫秒","4200毫秒","4300毫秒","4400毫秒","4500毫秒","4600毫秒","4700毫秒","4800毫秒","4900毫秒","5000毫秒"});
//		
//		template.addEffectAnimationField(skillField);
		
		//级别相关的属性
		skillField = new SkillField();
		
		skillField.setFieldName("buffName");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4301);
		skillField.setDisplayName(Translate.text_4301);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{""});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLevel");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4405);
		skillField.setDisplayName(Translate.text_4406);
		skillField.setType(int[].class);
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLastingTime");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4586);
		skillField.setDisplayName(Translate.text_4587);
		skillField.setType(long[].class);
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffProbability");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4588);
		skillField.setDisplayName(Translate.text_4589);
		skillField.setType(int[].class);
		
		template.addLevelSkillField(skillField);

		skillField = new SkillField();
		
		skillField.setFieldName("attackDamages");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4590);
		skillField.setDisplayName(Translate.text_4591);
		skillField.setType(short[].class);
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initSkillWithoutEffectAndQuickMove(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		//普通攻击技能 
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.activeskills.SkillWithoutEffectAndQuickMove");
		template.setDisplayName(Translate.text_4592);
		template.setType(SkillTemplate.SKILL_TYPE_ACTIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("range");
		skillField.setDefaultValue("50");
		skillField.setDescription(Translate.text_4446);
		skillField.setDisplayName(Translate.text_4446);
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("traceType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4593);
		skillField.setDisplayName(Translate.text_4594);
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4595 , Translate.text_4596});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("distance");
		skillField.setDefaultValue("20");
		skillField.setDescription(Translate.text_4597);
		skillField.setDisplayName(Translate.text_4597);
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField.setFieldName("duration1");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4447);
		skillField.setDisplayName(Translate.text_4448);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "500","550","600","650","700","750","800","850","900","1000","1200","1500","2000","2500","2700","3000","3500","4000","5000","8000","10000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491 , Translate.text_4449,Translate.text_4450,Translate.text_4451,Translate.text_4452,Translate.text_4453,Translate.text_4454,Translate.text_4455,Translate.text_4456,Translate.text_4457,Translate.text_4458,Translate.text_4459,Translate.text_4460,Translate.text_4461,Translate.text_4462,Translate.text_4463,Translate.text_4464,Translate.text_4465,Translate.text_4466,Translate.text_4467,Translate.text_4468,Translate.text_4469});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration2");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4470);
		skillField.setDisplayName(Translate.text_4471);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4472,Translate.text_4473,Translate.text_4474,Translate.text_4475,Translate.text_4476,Translate.text_4477,Translate.text_4478,Translate.text_4479,Translate.text_4480,Translate.text_4481,Translate.text_4482,Translate.text_4483,Translate.text_4484,Translate.text_4485,Translate.text_4486,Translate.text_4487,Translate.text_4488,Translate.text_4489,Translate.text_4490,Translate.text_4491,Translate.text_4492,Translate.text_4493,Translate.text_4494,Translate.text_4495,Translate.text_4496,Translate.text_4497,Translate.text_4498,Translate.text_4499,Translate.text_4500,Translate.text_4501,Translate.text_4502,Translate.text_4503,Translate.text_4504,Translate.text_4505,Translate.text_4506,Translate.text_4507,Translate.text_4508,Translate.text_4509,Translate.text_4510,Translate.text_4511,Translate.text_4512,Translate.text_4513,Translate.text_4514,Translate.text_4515,Translate.text_4516,Translate.text_4517,Translate.text_4518,Translate.text_4519,Translate.text_4520,Translate.text_4521});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration3");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4522);
		skillField.setDisplayName(Translate.text_4523);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1000","2000","3000","4000","5000","6000","7000","8000","9000","10000","11000","12000","13000","14000","15000","16000","17000","18000","19000","20000","21000","22000","23000","24000","25000","26000","27000","28000","29000","30000","31000","32000","33000","34000","35000","36000","37000","38000","39000","40000","41000","42000","43000","44000","45000","46000","47000","48000","49000","50000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4458,Translate.text_4461,Translate.text_4464,Translate.text_4466,Translate.text_4467,Translate.text_4524,Translate.text_4525,Translate.text_4468,Translate.text_4526,Translate.text_4469,Translate.text_4527,Translate.text_4528,Translate.text_4529,Translate.text_4530,Translate.text_4531,Translate.text_4532,Translate.text_4533,Translate.text_4534,Translate.text_4535,Translate.text_4536,Translate.text_4537,Translate.text_4538,Translate.text_4539,Translate.text_4540,Translate.text_4541,Translate.text_4542,Translate.text_4543,Translate.text_4544,Translate.text_4545,Translate.text_4546,Translate.text_4547,Translate.text_4548,Translate.text_4549,Translate.text_4550,Translate.text_4551,Translate.text_4552,Translate.text_4553,Translate.text_4554,Translate.text_4555,Translate.text_4556,Translate.text_4557,Translate.text_4558,Translate.text_4559,Translate.text_4560,Translate.text_4561,Translate.text_4562,Translate.text_4563,Translate.text_4564,Translate.text_4565,Translate.text_4566});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("style1");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4567);
		skillField.setDisplayName(Translate.text_4567);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);

//		DField df = playerMapping.get(5);
//		
//		ArrayList<String> al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		String ss[] = al.toArray(new String[0]);
//		String ssIndex[] = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
//		
		
		template.addActiveSkillAnimationField(skillField);

		
		skillField = new SkillField();
		
		skillField.setFieldName("style2");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4568);
		skillField.setDisplayName(Translate.text_4568);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		
//		df = playerMapping.get(5);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectiveTimes");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4569);
		skillField.setDisplayName(Translate.text_4570);
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("enableWeaponType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4571);
		skillField.setDisplayName(Translate.text_4571);
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4572 , Translate.text_4573});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("weaponTypeLimit");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4574);
		skillField.setDisplayName(Translate.text_4575);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		
//		
//		df = playerMapping.get(4);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("attackType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4576);
		skillField.setDisplayName(Translate.text_4577);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4578 , Translate.text_4579});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("followByCommonAttack");
		skillField.setDefaultValue("true");
		skillField.setDescription(Translate.text_4580);
		skillField.setDisplayName(Translate.text_4581);
		skillField.setType(Boolean.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"true","false"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_125 , Translate.text_126});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffTargetType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4582);
		skillField.setDisplayName(Translate.text_4583);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4584 , Translate.text_4585});
		
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
		
		//级别相关的属性
		skillField = new SkillField();
		
		skillField.setFieldName("buffName");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4301);
		skillField.setDisplayName(Translate.text_4301);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{""});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLevel");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4405);
		skillField.setDisplayName(Translate.text_4406);
		skillField.setType(int[].class);
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLastingTime");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4586);
		skillField.setDisplayName(Translate.text_4587);
		skillField.setType(long[].class);
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffProbability");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4588);
		skillField.setDisplayName(Translate.text_4589);
		skillField.setType(int[].class);
		
		template.addLevelSkillField(skillField);

		skillField = new SkillField();
		
		skillField.setFieldName("attackDamages");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4590);
		skillField.setDisplayName(Translate.text_4591);
		skillField.setType(short[].class);
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("mp");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4598);
		skillField.setDisplayName(Translate.text_4599);
		skillField.setType(short[].class);
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initSkillWithoutTraceAndOnTeamMember(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		//普通攻击技能 
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndOnTeamMember");
		template.setDisplayName(Translate.text_4600);
		template.setType(SkillTemplate.SKILL_TYPE_ACTIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("rangeType");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4601);
		skillField.setDisplayName(Translate.text_4293);
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1" , "2" , "3" , "4" , "5" , "6" , "7"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4602 , Translate.text_4603 , Translate.text_4604 , Translate.text_4294 , Translate.text_4295 , Translate.text_4296 , Translate.text_4297 ,  Translate.text_4298});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("rangeWidth");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4605);
		skillField.setDisplayName(Translate.text_4606);
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","323","324","325","326","327","328","329","330","331","332","333","334","335","336","337","338","339","340","341","342","343","344","345","346","347","348","349","350","351","352","353","354","355","356","357","358","359","360","361","362","363","364","365","366","367","368","369","370","371","372","373","374","375","376","377","378","379","380","381","382","383","384","385","386","387","388","389","390","391","392","393","394","395","396","397","398","399","400","401","402","403","404","405","406","407","408","409","410","411","412","413","414","415","416","417","418","419","420","421","422","423","424","425","426","427","428","429","430","431","432","433","434","435","436","437","438","439","440","441","442","443","444","445","446","447","448","449","450","451","452","453","454","455","456","457","458","459","460","461","462","463","464","465","466","467","468","469","470","471","472","473","474","475","476","477","478","479","480","481","482","483","484","485","486","487","488","489","490","491","492","493","494","495","496","497","498","499","500"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","323","324","325","326","327","328","329","330","331","332","333","334","335","336","337","338","339","340","341","342","343","344","345","346","347","348","349","350","351","352","353","354","355","356","357","358","359","360","361","362","363","364","365","366","367","368","369","370","371","372","373","374","375","376","377","378","379","380","381","382","383","384","385","386","387","388","389","390","391","392","393","394","395","396","397","398","399","400","401","402","403","404","405","406","407","408","409","410","411","412","413","414","415","416","417","418","419","420","421","422","423","424","425","426","427","428","429","430","431","432","433","434","435","436","437","438","439","440","441","442","443","444","445","446","447","448","449","450","451","452","453","454","455","456","457","458","459","460","461","462","463","464","465","466","467","468","469","470","471","472","473","474","475","476","477","478","479","480","481","482","483","484","485","486","487","488","489","490","491","492","493","494","495","496","497","498","499","500"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("rangeHeight");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4607);
		skillField.setDisplayName(Translate.text_4608);
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","323","324","325","326","327","328","329","330","331","332","333","334","335","336","337","338","339","340","341","342","343","344","345","346","347","348","349","350","351","352","353","354","355","356","357","358","359","360","361","362","363","364","365","366","367","368","369","370","371","372","373","374","375","376","377","378","379","380","381","382","383","384","385","386","387","388","389","390","391","392","393","394","395","396","397","398","399","400","401","402","403","404","405","406","407","408","409","410","411","412","413","414","415","416","417","418","419","420","421","422","423","424","425","426","427","428","429","430","431","432","433","434","435","436","437","438","439","440","441","442","443","444","445","446","447","448","449","450","451","452","453","454","455","456","457","458","459","460","461","462","463","464","465","466","467","468","469","470","471","472","473","474","475","476","477","478","479","480","481","482","483","484","485","486","487","488","489","490","491","492","493","494","495","496","497","498","499","500"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","323","324","325","326","327","328","329","330","331","332","333","334","335","336","337","338","339","340","341","342","343","344","345","346","347","348","349","350","351","352","353","354","355","356","357","358","359","360","361","362","363","364","365","366","367","368","369","370","371","372","373","374","375","376","377","378","379","380","381","382","383","384","385","386","387","388","389","390","391","392","393","394","395","396","397","398","399","400","401","402","403","404","405","406","407","408","409","410","411","412","413","414","415","416","417","418","419","420","421","422","423","424","425","426","427","428","429","430","431","432","433","434","435","436","437","438","439","440","441","442","443","444","445","446","447","448","449","450","451","452","453","454","455","456","457","458","459","460","461","462","463","464","465","466","467","468","469","470","471","472","473","474","475","476","477","478","479","480","481","482","483","484","485","486","487","488","489","490","491","492","493","494","495","496","497","498","499","500"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField.setFieldName("duration1");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4447);
		skillField.setDisplayName(Translate.text_4448);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "500","550","600","650","700","750","800","850","900","1000","1200","1500","2000","2500","2700","3000","3500","4000","5000","8000","10000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491 , Translate.text_4449,Translate.text_4450,Translate.text_4451,Translate.text_4452,Translate.text_4453,Translate.text_4454,Translate.text_4455,Translate.text_4456,Translate.text_4457,Translate.text_4458,Translate.text_4459,Translate.text_4460,Translate.text_4461,Translate.text_4462,Translate.text_4463,Translate.text_4464,Translate.text_4465,Translate.text_4466,Translate.text_4467,Translate.text_4468,Translate.text_4469});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration2");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4470);
		skillField.setDisplayName(Translate.text_4471);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4472,Translate.text_4473,Translate.text_4474,Translate.text_4475,Translate.text_4476,Translate.text_4477,Translate.text_4478,Translate.text_4479,Translate.text_4480,Translate.text_4481,Translate.text_4482,Translate.text_4483,Translate.text_4484,Translate.text_4485,Translate.text_4486,Translate.text_4487,Translate.text_4488,Translate.text_4489,Translate.text_4490,Translate.text_4491,Translate.text_4492,Translate.text_4493,Translate.text_4494,Translate.text_4495,Translate.text_4496,Translate.text_4497,Translate.text_4498,Translate.text_4499,Translate.text_4500,Translate.text_4501,Translate.text_4502,Translate.text_4503,Translate.text_4504,Translate.text_4505,Translate.text_4506,Translate.text_4507,Translate.text_4508,Translate.text_4509,Translate.text_4510,Translate.text_4511,Translate.text_4512,Translate.text_4513,Translate.text_4514,Translate.text_4515,Translate.text_4516,Translate.text_4517,Translate.text_4518,Translate.text_4519,Translate.text_4520,Translate.text_4521});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration3");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4522);
		skillField.setDisplayName(Translate.text_4523);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1000","2000","3000","4000","5000","6000","7000","8000","9000","10000","11000","12000","13000","14000","15000","16000","17000","18000","19000","20000","21000","22000","23000","24000","25000","26000","27000","28000","29000","30000","31000","32000","33000","34000","35000","36000","37000","38000","39000","40000","41000","42000","43000","44000","45000","46000","47000","48000","49000","50000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4458,Translate.text_4461,Translate.text_4464,Translate.text_4466,Translate.text_4467,Translate.text_4524,Translate.text_4525,Translate.text_4468,Translate.text_4526,Translate.text_4469,Translate.text_4527,Translate.text_4528,Translate.text_4529,Translate.text_4530,Translate.text_4531,Translate.text_4532,Translate.text_4533,Translate.text_4534,Translate.text_4535,Translate.text_4536,Translate.text_4537,Translate.text_4538,Translate.text_4539,Translate.text_4540,Translate.text_4541,Translate.text_4542,Translate.text_4543,Translate.text_4544,Translate.text_4545,Translate.text_4546,Translate.text_4547,Translate.text_4548,Translate.text_4549,Translate.text_4550,Translate.text_4551,Translate.text_4552,Translate.text_4553,Translate.text_4554,Translate.text_4555,Translate.text_4556,Translate.text_4557,Translate.text_4558,Translate.text_4559,Translate.text_4560,Translate.text_4561,Translate.text_4562,Translate.text_4563,Translate.text_4564,Translate.text_4565,Translate.text_4566});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("style1");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4567);
		skillField.setDisplayName(Translate.text_4567);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//
//		DField df = playerMapping.get(5);
//		
//		ArrayList<String> al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		String ss[] = al.toArray(new String[0]);
//		String ssIndex[] = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		
		template.addActiveSkillAnimationField(skillField);

		
		skillField = new SkillField();
		
		skillField.setFieldName("style2");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4568);
		skillField.setDisplayName(Translate.text_4568);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		
//		df = playerMapping.get(5);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectiveTimes");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4569);
		skillField.setDisplayName(Translate.text_4570);
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("enableWeaponType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4571);
		skillField.setDisplayName(Translate.text_4571);
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4572 , Translate.text_4573});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("weaponTypeLimit");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4574);
		skillField.setDisplayName(Translate.text_4575);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		
//		
//		df = playerMapping.get(4);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("attackType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4576);
		skillField.setDisplayName(Translate.text_4577);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4578 , Translate.text_4579});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("followByCommonAttack");
		skillField.setDefaultValue("true");
		skillField.setDescription(Translate.text_4580);
		skillField.setDisplayName(Translate.text_4581);
		skillField.setType(Boolean.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"true","false"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_125 , Translate.text_126});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffTargetType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4582);
		skillField.setDisplayName(Translate.text_4583);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4584 , Translate.text_4585});
		
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
		
		//后效参数
		skillField = new SkillField();
		
		skillField.setFieldName("effectType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4609);
		skillField.setDisplayName(Translate.text_4610);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		
//		df = summonedMapping.get(0);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addEffectAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectLastTime");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4611);
		skillField.setDisplayName(Translate.text_4612);
		skillField.setType(Byte.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491 , Translate.text_4472,Translate.text_4473,Translate.text_4474,Translate.text_4475,Translate.text_4476,Translate.text_4477,Translate.text_4478,Translate.text_4479,Translate.text_4480,Translate.text_4481,Translate.text_4482,Translate.text_4483,Translate.text_4484,Translate.text_4485,Translate.text_4486,Translate.text_4487,Translate.text_4488,Translate.text_4489,Translate.text_4490,Translate.text_4491,Translate.text_4492,Translate.text_4493,Translate.text_4494,Translate.text_4495,Translate.text_4496,Translate.text_4497,Translate.text_4498,Translate.text_4499,Translate.text_4500,Translate.text_4501,Translate.text_4502,Translate.text_4503,Translate.text_4504,Translate.text_4505,Translate.text_4506,Translate.text_4507,Translate.text_4508,Translate.text_4509,Translate.text_4510,Translate.text_4511,Translate.text_4512,Translate.text_4513,Translate.text_4514,Translate.text_4515,Translate.text_4516,Translate.text_4517,Translate.text_4518,Translate.text_4519,Translate.text_4520,Translate.text_4521});
		
		template.addEffectAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectExplosionLastTime");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4613);
		skillField.setDisplayName(Translate.text_4614);
		skillField.setType(Byte.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491 , Translate.text_4472,Translate.text_4473,Translate.text_4474,Translate.text_4475,Translate.text_4476,Translate.text_4477,Translate.text_4478,Translate.text_4479,Translate.text_4480,Translate.text_4481,Translate.text_4482,Translate.text_4483,Translate.text_4484,Translate.text_4485,Translate.text_4486,Translate.text_4487,Translate.text_4488,Translate.text_4489,Translate.text_4490,Translate.text_4491,Translate.text_4492,Translate.text_4493,Translate.text_4494,Translate.text_4495,Translate.text_4496,Translate.text_4497,Translate.text_4498,Translate.text_4499,Translate.text_4500,Translate.text_4501,Translate.text_4502,Translate.text_4503,Translate.text_4504,Translate.text_4505,Translate.text_4506,Translate.text_4507,Translate.text_4508,Translate.text_4509,Translate.text_4510,Translate.text_4511,Translate.text_4512,Translate.text_4513,Translate.text_4514,Translate.text_4515,Translate.text_4516,Translate.text_4517,Translate.text_4518,Translate.text_4519,Translate.text_4520,Translate.text_4521});
		
		template.addEffectAnimationField(skillField);
		
		//级别相关的属性
		skillField = new SkillField();
		
		skillField.setFieldName("buffName");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4301);
		skillField.setDisplayName(Translate.text_4301);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{""});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLevel");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4405);
		skillField.setDisplayName(Translate.text_4406);
		skillField.setType(int[].class);
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLastingTime");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4586);
		skillField.setDisplayName(Translate.text_4587);
		skillField.setType(long[].class);
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffProbability");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4588);
		skillField.setDisplayName(Translate.text_4589);
		skillField.setType(int[].class);
		
		template.addLevelSkillField(skillField);

		skillField = new SkillField();
		
		skillField.setFieldName("attackDamages");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4590);
		skillField.setDisplayName(Translate.text_4591);
		skillField.setType(short[].class);
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("mp");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4598);
		skillField.setDisplayName(Translate.text_4599);
		skillField.setType(short[].class);
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}

	private void initSkillWithoutTraceAndWithMatrix(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		//普通攻击技能 
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithMatrix");
		template.setDisplayName(Translate.text_4615);
		template.setType(SkillTemplate.SKILL_TYPE_ACTIVE);
		
		
		skillField = new SkillField();
		
		skillField.setFieldName("matrixType");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4616);
		skillField.setDisplayName(Translate.text_4617);
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1" , "2" , "3"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4618 , Translate.text_4619 , Translate.text_4620 , Translate.text_4621});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("gapWidth");
		skillField.setDefaultValue("24");
		skillField.setDescription(Translate.text_4622);
		skillField.setDisplayName(Translate.text_4622);
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","323","324","325","326","327","328","329","330","331","332","333","334","335","336","337","338","339","340","341","342","343","344","345","346","347","348","349","350","351","352","353","354","355","356","357","358","359","360","361","362","363","364","365","366","367","368","369","370","371","372","373","374","375","376","377","378","379","380","381","382","383","384","385","386","387","388","389","390","391","392","393","394","395","396","397","398","399","400","401","402","403","404","405","406","407","408","409","410","411","412","413","414","415","416","417","418","419","420","421","422","423","424","425","426","427","428","429","430","431","432","433","434","435","436","437","438","439","440","441","442","443","444","445","446","447","448","449","450","451","452","453","454","455","456","457","458","459","460","461","462","463","464","465","466","467","468","469","470","471","472","473","474","475","476","477","478","479","480","481","482","483","484","485","486","487","488","489","490","491","492","493","494","495","496","497","498","499","500"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","323","324","325","326","327","328","329","330","331","332","333","334","335","336","337","338","339","340","341","342","343","344","345","346","347","348","349","350","351","352","353","354","355","356","357","358","359","360","361","362","363","364","365","366","367","368","369","370","371","372","373","374","375","376","377","378","379","380","381","382","383","384","385","386","387","388","389","390","391","392","393","394","395","396","397","398","399","400","401","402","403","404","405","406","407","408","409","410","411","412","413","414","415","416","417","418","419","420","421","422","423","424","425","426","427","428","429","430","431","432","433","434","435","436","437","438","439","440","441","442","443","444","445","446","447","448","449","450","451","452","453","454","455","456","457","458","459","460","461","462","463","464","465","466","467","468","469","470","471","472","473","474","475","476","477","478","479","480","481","482","483","484","485","486","487","488","489","490","491","492","493","494","495","496","497","498","499","500"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("gapHeight");
		skillField.setDefaultValue("32");
		skillField.setDescription(Translate.text_4623);
		skillField.setDisplayName(Translate.text_4623);
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","323","324","325","326","327","328","329","330","331","332","333","334","335","336","337","338","339","340","341","342","343","344","345","346","347","348","349","350","351","352","353","354","355","356","357","358","359","360","361","362","363","364","365","366","367","368","369","370","371","372","373","374","375","376","377","378","379","380","381","382","383","384","385","386","387","388","389","390","391","392","393","394","395","396","397","398","399","400","401","402","403","404","405","406","407","408","409","410","411","412","413","414","415","416","417","418","419","420","421","422","423","424","425","426","427","428","429","430","431","432","433","434","435","436","437","438","439","440","441","442","443","444","445","446","447","448","449","450","451","452","453","454","455","456","457","458","459","460","461","462","463","464","465","466","467","468","469","470","471","472","473","474","475","476","477","478","479","480","481","482","483","484","485","486","487","488","489","490","491","492","493","494","495","496","497","498","499","500"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","323","324","325","326","327","328","329","330","331","332","333","334","335","336","337","338","339","340","341","342","343","344","345","346","347","348","349","350","351","352","353","354","355","356","357","358","359","360","361","362","363","364","365","366","367","368","369","370","371","372","373","374","375","376","377","378","379","380","381","382","383","384","385","386","387","388","389","390","391","392","393","394","395","396","397","398","399","400","401","402","403","404","405","406","407","408","409","410","411","412","413","414","415","416","417","418","419","420","421","422","423","424","425","426","427","428","429","430","431","432","433","434","435","436","437","438","439","440","441","442","443","444","445","446","447","448","449","450","451","452","453","454","455","456","457","458","459","460","461","462","463","464","465","466","467","468","469","470","471","472","473","474","475","476","477","478","479","480","481","482","483","484","485","486","487","488","489","490","491","492","493","494","495","496","497","498","499","500"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectNum");
		skillField.setDefaultValue("32");
		skillField.setDescription(Translate.text_4624);
		skillField.setDisplayName(Translate.text_4625);
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"});
		
		template.addBasicSkillField(skillField);
		
//		skillField = new SkillField();
//		
//		skillField.setFieldName("rangeWidth");
//		skillField.setDefaultValue("");
//		skillField.setDescription("以玩家自身为中心的，一个矩形，此参数为矩形的宽度");
//		skillField.setDisplayName("矩形宽度");
//		skillField.setType(Integer.class);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","323","324","325","326","327","328","329","330","331","332","333","334","335","336","337","338","339","340","341","342","343","344","345","346","347","348","349","350","351","352","353","354","355","356","357","358","359","360","361","362","363","364","365","366","367","368","369","370","371","372","373","374","375","376","377","378","379","380","381","382","383","384","385","386","387","388","389","390","391","392","393","394","395","396","397","398","399","400","401","402","403","404","405","406","407","408","409","410","411","412","413","414","415","416","417","418","419","420","421","422","423","424","425","426","427","428","429","430","431","432","433","434","435","436","437","438","439","440","441","442","443","444","445","446","447","448","449","450","451","452","453","454","455","456","457","458","459","460","461","462","463","464","465","466","467","468","469","470","471","472","473","474","475","476","477","478","479","480","481","482","483","484","485","486","487","488","489","490","491","492","493","494","495","496","497","498","499","500"});
//		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","323","324","325","326","327","328","329","330","331","332","333","334","335","336","337","338","339","340","341","342","343","344","345","346","347","348","349","350","351","352","353","354","355","356","357","358","359","360","361","362","363","364","365","366","367","368","369","370","371","372","373","374","375","376","377","378","379","380","381","382","383","384","385","386","387","388","389","390","391","392","393","394","395","396","397","398","399","400","401","402","403","404","405","406","407","408","409","410","411","412","413","414","415","416","417","418","419","420","421","422","423","424","425","426","427","428","429","430","431","432","433","434","435","436","437","438","439","440","441","442","443","444","445","446","447","448","449","450","451","452","453","454","455","456","457","458","459","460","461","462","463","464","465","466","467","468","469","470","471","472","473","474","475","476","477","478","479","480","481","482","483","484","485","486","487","488","489","490","491","492","493","494","495","496","497","498","499","500"});
//		
//		template.addBasicSkillField(skillField);
//		
//		skillField = new SkillField();
//		
//		skillField.setFieldName("rangeHeight");
//		skillField.setDefaultValue("");
//		skillField.setDescription("以玩家自身为中心的，一个矩形，此参数为矩形的高度");
//		skillField.setDisplayName("矩形高度");
//		skillField.setType(Integer.class);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","323","324","325","326","327","328","329","330","331","332","333","334","335","336","337","338","339","340","341","342","343","344","345","346","347","348","349","350","351","352","353","354","355","356","357","358","359","360","361","362","363","364","365","366","367","368","369","370","371","372","373","374","375","376","377","378","379","380","381","382","383","384","385","386","387","388","389","390","391","392","393","394","395","396","397","398","399","400","401","402","403","404","405","406","407","408","409","410","411","412","413","414","415","416","417","418","419","420","421","422","423","424","425","426","427","428","429","430","431","432","433","434","435","436","437","438","439","440","441","442","443","444","445","446","447","448","449","450","451","452","453","454","455","456","457","458","459","460","461","462","463","464","465","466","467","468","469","470","471","472","473","474","475","476","477","478","479","480","481","482","483","484","485","486","487","488","489","490","491","492","493","494","495","496","497","498","499","500"});
//		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","323","324","325","326","327","328","329","330","331","332","333","334","335","336","337","338","339","340","341","342","343","344","345","346","347","348","349","350","351","352","353","354","355","356","357","358","359","360","361","362","363","364","365","366","367","368","369","370","371","372","373","374","375","376","377","378","379","380","381","382","383","384","385","386","387","388","389","390","391","392","393","394","395","396","397","398","399","400","401","402","403","404","405","406","407","408","409","410","411","412","413","414","415","416","417","418","419","420","421","422","423","424","425","426","427","428","429","430","431","432","433","434","435","436","437","438","439","440","441","442","443","444","445","446","447","448","449","450","451","452","453","454","455","456","457","458","459","460","461","462","463","464","465","466","467","468","469","470","471","472","473","474","475","476","477","478","479","480","481","482","483","484","485","486","487","488","489","490","491","492","493","494","495","496","497","498","499","500"});
//		
//		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField.setFieldName("duration1");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4447);
		skillField.setDisplayName(Translate.text_4448);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "500","550","600","650","700","750","800","850","900","1000","1200","1500","2000","2500","2700","3000","3500","4000","5000","8000","10000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491 , Translate.text_4449,Translate.text_4450,Translate.text_4451,Translate.text_4452,Translate.text_4453,Translate.text_4454,Translate.text_4455,Translate.text_4456,Translate.text_4457,Translate.text_4458,Translate.text_4459,Translate.text_4460,Translate.text_4461,Translate.text_4462,Translate.text_4463,Translate.text_4464,Translate.text_4465,Translate.text_4466,Translate.text_4467,Translate.text_4468,Translate.text_4469});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration2");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4470);
		skillField.setDisplayName(Translate.text_4471);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		
		String sss[] = new String[40];
		for(int i = 0 ; i < sss.length ; i++){
			sss[i] = (i * 50) + "";
		}
		skillField.setSelectValues(sss);
		
		sss = new String[40];
		for(int i = 0 ; i < sss.length ; i++){
			sss[i] = (i * 50) + Translate.text_4626;
			if(i == 0){
				sss[0] = Translate.text_1491;
			}
		}
		skillField.setSelectDisplayValues(sss);
		
		
//		skillField.setSelectValues(new String[]{"100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
//		skillField.setSelectDisplayValues(new String[]{"100毫秒","200毫秒","300毫秒","400毫秒","500毫秒","600毫秒","700毫秒","800毫秒","900毫秒","1000毫秒","1100毫秒","1200毫秒","1300毫秒","1400毫秒","1500毫秒","1600毫秒","1700毫秒","1800毫秒","1900毫秒","2000毫秒","2100毫秒","2200毫秒","2300毫秒","2400毫秒","2500毫秒","2600毫秒","2700毫秒","2800毫秒","2900毫秒","3000毫秒","3100毫秒","3200毫秒","3300毫秒","3400毫秒","3500毫秒","3600毫秒","3700毫秒","3800毫秒","3900毫秒","4000毫秒","4100毫秒","4200毫秒","4300毫秒","4400毫秒","4500毫秒","4600毫秒","4700毫秒","4800毫秒","4900毫秒","5000毫秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration3");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4522);
		skillField.setDisplayName(Translate.text_4523);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1000","2000","3000","4000","5000","6000","7000","8000","9000","10000","11000","12000","13000","14000","15000","16000","17000","18000","19000","20000","21000","22000","23000","24000","25000","26000","27000","28000","29000","30000","31000","32000","33000","34000","35000","36000","37000","38000","39000","40000","41000","42000","43000","44000","45000","46000","47000","48000","49000","50000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4458,Translate.text_4461,Translate.text_4464,Translate.text_4466,Translate.text_4467,Translate.text_4524,Translate.text_4525,Translate.text_4468,Translate.text_4526,Translate.text_4469,Translate.text_4527,Translate.text_4528,Translate.text_4529,Translate.text_4530,Translate.text_4531,Translate.text_4532,Translate.text_4533,Translate.text_4534,Translate.text_4535,Translate.text_4536,Translate.text_4537,Translate.text_4538,Translate.text_4539,Translate.text_4540,Translate.text_4541,Translate.text_4542,Translate.text_4543,Translate.text_4544,Translate.text_4545,Translate.text_4546,Translate.text_4547,Translate.text_4548,Translate.text_4549,Translate.text_4550,Translate.text_4551,Translate.text_4552,Translate.text_4553,Translate.text_4554,Translate.text_4555,Translate.text_4556,Translate.text_4557,Translate.text_4558,Translate.text_4559,Translate.text_4560,Translate.text_4561,Translate.text_4562,Translate.text_4563,Translate.text_4564,Translate.text_4565,Translate.text_4566});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("style1");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4567);
		skillField.setDisplayName(Translate.text_4567);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//
//		DField df = playerMapping.get(5);
//		
//		ArrayList<String> al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		String ss[] = al.toArray(new String[0]);
//		String ssIndex[] = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		
		template.addActiveSkillAnimationField(skillField);

		
		skillField = new SkillField();
		
		skillField.setFieldName("style2");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4568);
		skillField.setDisplayName(Translate.text_4568);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		
//		df = playerMapping.get(5);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectiveTimes");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4569);
		skillField.setDisplayName(Translate.text_4570);
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("enableWeaponType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4571);
		skillField.setDisplayName(Translate.text_4571);
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4572 , Translate.text_4573});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("weaponTypeLimit");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4574);
		skillField.setDisplayName(Translate.text_4575);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		
//		
//		df = playerMapping.get(4);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("attackType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4576);
		skillField.setDisplayName(Translate.text_4577);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4578 , Translate.text_4579});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("followByCommonAttack");
		skillField.setDefaultValue("true");
		skillField.setDescription(Translate.text_4580);
		skillField.setDisplayName(Translate.text_4581);
		skillField.setType(Boolean.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"true","false"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_125 , Translate.text_126});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffTargetType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4582);
		skillField.setDisplayName(Translate.text_4583);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4584 , Translate.text_4585});
		
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
		
		skillField = new SkillField();
		
		skillField.setFieldName("range");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4627);
		skillField.setDisplayName(Translate.text_4627);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300"});
		
		template.addBasicSkillField(skillField);
		
		//后效参数
		skillField = new SkillField();
		
		skillField.setFieldName("effectType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4609);
		skillField.setDisplayName(Translate.text_4610);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		
//		df = summonedMapping.get(0);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addEffectAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectLastTime");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4611);
		skillField.setDisplayName(Translate.text_4612);
		skillField.setType(Byte.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		
		sss = new String[30];
		for(int i = 0 ; i < sss.length ; i++){
			sss[i] = (i * 50) + "";
		}
		skillField.setSelectValues(sss);
		
		sss = new String[30];
		for(int i = 0 ; i < sss.length ; i++){
			sss[i] = (i * 50) + Translate.text_4626;
			if(i == 0){
				sss[0] = Translate.text_1491;
			}
		}
		skillField.setSelectDisplayValues(sss);
		
		template.addEffectAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectExplosionLastTime");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4613);
		skillField.setDisplayName(Translate.text_4614);
		skillField.setType(Byte.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		
		sss = new String[30];
		for(int i = 0 ; i < sss.length ; i++){
			sss[i] = (i * 50) + "";
		}
		skillField.setSelectValues(sss);
		
		sss = new String[30];
		for(int i = 0 ; i < sss.length ; i++){
			sss[i] = (i * 50) + Translate.text_4626;
			if(i == 0){
				sss[0] = Translate.text_1491;
			}
		}
		skillField.setSelectDisplayValues(sss);
		
		
		template.addEffectAnimationField(skillField);
		
		//级别相关的属性
		skillField = new SkillField();
		
		skillField.setFieldName("buffName");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4301);
		skillField.setDisplayName(Translate.text_4301);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{""});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLevel");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4405);
		skillField.setDisplayName(Translate.text_4406);
		skillField.setType(int[].class);
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLastingTime");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4586);
		skillField.setDisplayName(Translate.text_4587);
		skillField.setType(long[].class);
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffProbability");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4588);
		skillField.setDisplayName(Translate.text_4589);
		skillField.setType(int[].class);
		
		template.addLevelSkillField(skillField);

		skillField = new SkillField();
		
		skillField.setFieldName("attackDamages");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4590);
		skillField.setDisplayName(Translate.text_4591);
		skillField.setType(short[].class);
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("mp");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4598);
		skillField.setDisplayName(Translate.text_4599);
		skillField.setType(short[].class);
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initSkillWithoutTraceAndWithRange(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		//普通攻击技能 
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithRange");
		template.setDisplayName(Translate.text_4628);
		template.setType(SkillTemplate.SKILL_TYPE_ACTIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("rangeType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4629);
		skillField.setDisplayName(Translate.text_4293);
		skillField.setType(Byte.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4630 , Translate.text_4631});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("rangeWidth");
		skillField.setDefaultValue("240");
		skillField.setDescription(Translate.text_4632);
		skillField.setDisplayName(Translate.text_4632);
		skillField.setType(Byte.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("rangeHeight");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4633);
		skillField.setDisplayName(Translate.text_4633);
		skillField.setType(Byte.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxAttackNum");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4634);
		skillField.setDisplayName(Translate.text_4635);
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4572 , Translate.text_4636});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField.setFieldName("duration1");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4447);
		skillField.setDisplayName(Translate.text_4448);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "500","550","600","650","700","750","800","850","900","1000","1200","1500","2000","2500","2700","3000","3500","4000","5000","8000","10000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491 , Translate.text_4449,Translate.text_4450,Translate.text_4451,Translate.text_4452,Translate.text_4453,Translate.text_4454,Translate.text_4455,Translate.text_4456,Translate.text_4457,Translate.text_4458,Translate.text_4459,Translate.text_4460,Translate.text_4461,Translate.text_4462,Translate.text_4463,Translate.text_4464,Translate.text_4465,Translate.text_4466,Translate.text_4467,Translate.text_4468,Translate.text_4469});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration2");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4470);
		skillField.setDisplayName(Translate.text_4471);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4472,Translate.text_4473,Translate.text_4474,Translate.text_4475,Translate.text_4476,Translate.text_4477,Translate.text_4478,Translate.text_4479,Translate.text_4480,Translate.text_4481,Translate.text_4482,Translate.text_4483,Translate.text_4484,Translate.text_4485,Translate.text_4486,Translate.text_4487,Translate.text_4488,Translate.text_4489,Translate.text_4490,Translate.text_4491,Translate.text_4492,Translate.text_4493,Translate.text_4494,Translate.text_4495,Translate.text_4496,Translate.text_4497,Translate.text_4498,Translate.text_4499,Translate.text_4500,Translate.text_4501,Translate.text_4502,Translate.text_4503,Translate.text_4504,Translate.text_4505,Translate.text_4506,Translate.text_4507,Translate.text_4508,Translate.text_4509,Translate.text_4510,Translate.text_4511,Translate.text_4512,Translate.text_4513,Translate.text_4514,Translate.text_4515,Translate.text_4516,Translate.text_4517,Translate.text_4518,Translate.text_4519,Translate.text_4520,Translate.text_4521});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration3");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4522);
		skillField.setDisplayName(Translate.text_4523);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1000","2000","3000","4000","5000","6000","7000","8000","9000","10000","11000","12000","13000","14000","15000","16000","17000","18000","19000","20000","21000","22000","23000","24000","25000","26000","27000","28000","29000","30000","31000","32000","33000","34000","35000","36000","37000","38000","39000","40000","41000","42000","43000","44000","45000","46000","47000","48000","49000","50000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4458,Translate.text_4461,Translate.text_4464,Translate.text_4466,Translate.text_4467,Translate.text_4524,Translate.text_4525,Translate.text_4468,Translate.text_4526,Translate.text_4469,Translate.text_4527,Translate.text_4528,Translate.text_4529,Translate.text_4530,Translate.text_4531,Translate.text_4532,Translate.text_4533,Translate.text_4534,Translate.text_4535,Translate.text_4536,Translate.text_4537,Translate.text_4538,Translate.text_4539,Translate.text_4540,Translate.text_4541,Translate.text_4542,Translate.text_4543,Translate.text_4544,Translate.text_4545,Translate.text_4546,Translate.text_4547,Translate.text_4548,Translate.text_4549,Translate.text_4550,Translate.text_4551,Translate.text_4552,Translate.text_4553,Translate.text_4554,Translate.text_4555,Translate.text_4556,Translate.text_4557,Translate.text_4558,Translate.text_4559,Translate.text_4560,Translate.text_4561,Translate.text_4562,Translate.text_4563,Translate.text_4564,Translate.text_4565,Translate.text_4566});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("style1");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4567);
		skillField.setDisplayName(Translate.text_4567);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//
//		DField df = playerMapping.get(5);
//		
//		ArrayList<String> al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		String ss[] = al.toArray(new String[0]);
//		String ssIndex[] = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		
		template.addActiveSkillAnimationField(skillField);

		
		skillField = new SkillField();
		
		skillField.setFieldName("style2");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4568);
		skillField.setDisplayName(Translate.text_4568);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		
//		df = playerMapping.get(5);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectiveTimes");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4569);
		skillField.setDisplayName(Translate.text_4570);
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("enableWeaponType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4571);
		skillField.setDisplayName(Translate.text_4571);
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4572 , Translate.text_4573});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("weaponTypeLimit");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4574);
		skillField.setDisplayName(Translate.text_4575);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		
//		
//		df = playerMapping.get(4);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("attackType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4576);
		skillField.setDisplayName(Translate.text_4577);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4578 , Translate.text_4579});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("followByCommonAttack");
		skillField.setDefaultValue("true");
		skillField.setDescription(Translate.text_4580);
		skillField.setDisplayName(Translate.text_4581);
		skillField.setType(Boolean.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"true","false"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_125 , Translate.text_126});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffTargetType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4582);
		skillField.setDisplayName(Translate.text_4583);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4584 , Translate.text_4585});
		
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
		
		//后效参数
		skillField = new SkillField();
		
		skillField.setFieldName("effectType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4609);
		skillField.setDisplayName(Translate.text_4610);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		
//		df = summonedMapping.get(0);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addEffectAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectLastTime");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4611);
		skillField.setDisplayName(Translate.text_4612);
		skillField.setType(Byte.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491 , Translate.text_4472,Translate.text_4473,Translate.text_4474,Translate.text_4475,Translate.text_4476,Translate.text_4477,Translate.text_4478,Translate.text_4479,Translate.text_4480,Translate.text_4481,Translate.text_4482,Translate.text_4483,Translate.text_4484,Translate.text_4485,Translate.text_4486,Translate.text_4487,Translate.text_4488,Translate.text_4489,Translate.text_4490,Translate.text_4491,Translate.text_4492,Translate.text_4493,Translate.text_4494,Translate.text_4495,Translate.text_4496,Translate.text_4497,Translate.text_4498,Translate.text_4499,Translate.text_4500,Translate.text_4501,Translate.text_4502,Translate.text_4503,Translate.text_4504,Translate.text_4505,Translate.text_4506,Translate.text_4507,Translate.text_4508,Translate.text_4509,Translate.text_4510,Translate.text_4511,Translate.text_4512,Translate.text_4513,Translate.text_4514,Translate.text_4515,Translate.text_4516,Translate.text_4517,Translate.text_4518,Translate.text_4519,Translate.text_4520,Translate.text_4521});
		
		template.addEffectAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectExplosionLastTime");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4613);
		skillField.setDisplayName(Translate.text_4614);
		skillField.setType(Byte.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491 , Translate.text_4472,Translate.text_4473,Translate.text_4474,Translate.text_4475,Translate.text_4476,Translate.text_4477,Translate.text_4478,Translate.text_4479,Translate.text_4480,Translate.text_4481,Translate.text_4482,Translate.text_4483,Translate.text_4484,Translate.text_4485,Translate.text_4486,Translate.text_4487,Translate.text_4488,Translate.text_4489,Translate.text_4490,Translate.text_4491,Translate.text_4492,Translate.text_4493,Translate.text_4494,Translate.text_4495,Translate.text_4496,Translate.text_4497,Translate.text_4498,Translate.text_4499,Translate.text_4500,Translate.text_4501,Translate.text_4502,Translate.text_4503,Translate.text_4504,Translate.text_4505,Translate.text_4506,Translate.text_4507,Translate.text_4508,Translate.text_4509,Translate.text_4510,Translate.text_4511,Translate.text_4512,Translate.text_4513,Translate.text_4514,Translate.text_4515,Translate.text_4516,Translate.text_4517,Translate.text_4518,Translate.text_4519,Translate.text_4520,Translate.text_4521});
		
		template.addEffectAnimationField(skillField);
		
		//级别相关的属性
		skillField = new SkillField();
		
		skillField.setFieldName("buffName");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4301);
		skillField.setDisplayName(Translate.text_4301);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{""});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLevel");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4405);
		skillField.setDisplayName(Translate.text_4406);
		skillField.setType(int[].class);
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLastingTime");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4586);
		skillField.setDisplayName(Translate.text_4587);
		skillField.setType(long[].class);
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffProbability");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4588);
		skillField.setDisplayName(Translate.text_4589);
		skillField.setType(int[].class);
		
		template.addLevelSkillField(skillField);

		skillField = new SkillField();
		
		skillField.setFieldName("attackDamages");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4590);
		skillField.setDisplayName(Translate.text_4591);
		skillField.setType(short[].class);
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("mp");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4598);
		skillField.setDisplayName(Translate.text_4599);
		skillField.setType(short[].class);
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initSkillWithoutTraceAndWithTargetOrPosition(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		//普通攻击技能 
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithTargetOrPosition");
		template.setDisplayName(Translate.text_4637);
		template.setType(SkillTemplate.SKILL_TYPE_ACTIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("range");
		skillField.setDefaultValue("20");
		skillField.setDescription(Translate.text_4627);
		skillField.setDisplayName(Translate.text_4627);
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField.setFieldName("duration1");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4447);
		skillField.setDisplayName(Translate.text_4448);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "500","550","600","650","700","750","800","850","900","1000","1200","1500","2000","2500","2700","3000","3500","4000","5000","8000","10000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491 , Translate.text_4449,Translate.text_4450,Translate.text_4451,Translate.text_4452,Translate.text_4453,Translate.text_4454,Translate.text_4455,Translate.text_4456,Translate.text_4457,Translate.text_4458,Translate.text_4459,Translate.text_4460,Translate.text_4461,Translate.text_4462,Translate.text_4463,Translate.text_4464,Translate.text_4465,Translate.text_4466,Translate.text_4467,Translate.text_4468,Translate.text_4469});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration2");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4470);
		skillField.setDisplayName(Translate.text_4471);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4472,Translate.text_4473,Translate.text_4474,Translate.text_4475,Translate.text_4476,Translate.text_4477,Translate.text_4478,Translate.text_4479,Translate.text_4480,Translate.text_4481,Translate.text_4482,Translate.text_4483,Translate.text_4484,Translate.text_4485,Translate.text_4486,Translate.text_4487,Translate.text_4488,Translate.text_4489,Translate.text_4490,Translate.text_4491,Translate.text_4492,Translate.text_4493,Translate.text_4494,Translate.text_4495,Translate.text_4496,Translate.text_4497,Translate.text_4498,Translate.text_4499,Translate.text_4500,Translate.text_4501,Translate.text_4502,Translate.text_4503,Translate.text_4504,Translate.text_4505,Translate.text_4506,Translate.text_4507,Translate.text_4508,Translate.text_4509,Translate.text_4510,Translate.text_4511,Translate.text_4512,Translate.text_4513,Translate.text_4514,Translate.text_4515,Translate.text_4516,Translate.text_4517,Translate.text_4518,Translate.text_4519,Translate.text_4520,Translate.text_4521});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration3");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4522);
		skillField.setDisplayName(Translate.text_4523);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1000","2000","3000","4000","5000","6000","7000","8000","9000","10000","11000","12000","13000","14000","15000","16000","17000","18000","19000","20000","21000","22000","23000","24000","25000","26000","27000","28000","29000","30000","31000","32000","33000","34000","35000","36000","37000","38000","39000","40000","41000","42000","43000","44000","45000","46000","47000","48000","49000","50000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4458,Translate.text_4461,Translate.text_4464,Translate.text_4466,Translate.text_4467,Translate.text_4524,Translate.text_4525,Translate.text_4468,Translate.text_4526,Translate.text_4469,Translate.text_4527,Translate.text_4528,Translate.text_4529,Translate.text_4530,Translate.text_4531,Translate.text_4532,Translate.text_4533,Translate.text_4534,Translate.text_4535,Translate.text_4536,Translate.text_4537,Translate.text_4538,Translate.text_4539,Translate.text_4540,Translate.text_4541,Translate.text_4542,Translate.text_4543,Translate.text_4544,Translate.text_4545,Translate.text_4546,Translate.text_4547,Translate.text_4548,Translate.text_4549,Translate.text_4550,Translate.text_4551,Translate.text_4552,Translate.text_4553,Translate.text_4554,Translate.text_4555,Translate.text_4556,Translate.text_4557,Translate.text_4558,Translate.text_4559,Translate.text_4560,Translate.text_4561,Translate.text_4562,Translate.text_4563,Translate.text_4564,Translate.text_4565,Translate.text_4566});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("style1");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4567);
		skillField.setDisplayName(Translate.text_4567);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//
//		DField df = playerMapping.get(5);
//		
//		ArrayList<String> al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		String ss[] = al.toArray(new String[0]);
//		String ssIndex[] = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		
		template.addActiveSkillAnimationField(skillField);

		
		skillField = new SkillField();
		
		skillField.setFieldName("style2");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4568);
		skillField.setDisplayName(Translate.text_4568);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		
//		df = playerMapping.get(5);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectiveTimes");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4569);
		skillField.setDisplayName(Translate.text_4570);
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("enableWeaponType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4571);
		skillField.setDisplayName(Translate.text_4571);
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4572 , Translate.text_4573});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("weaponTypeLimit");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4574);
		skillField.setDisplayName(Translate.text_4575);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		
//		
//		df = playerMapping.get(4);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("attackType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4576);
		skillField.setDisplayName(Translate.text_4577);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4578 , Translate.text_4579});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("followByCommonAttack");
		skillField.setDefaultValue("true");
		skillField.setDescription(Translate.text_4580);
		skillField.setDisplayName(Translate.text_4581);
		skillField.setType(Boolean.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"true","false"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_125 , Translate.text_126});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffTargetType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4582);
		skillField.setDisplayName(Translate.text_4583);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4584 , Translate.text_4585});
		
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
		
		//后效参数
		skillField = new SkillField();
		
		skillField.setFieldName("effectType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4609);
		skillField.setDisplayName(Translate.text_4610);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		
//		df = summonedMapping.get(0);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addEffectAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectLastTime");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4611);
		skillField.setDisplayName(Translate.text_4612);
		skillField.setType(Byte.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491 , Translate.text_4472,Translate.text_4473,Translate.text_4474,Translate.text_4475,Translate.text_4476,Translate.text_4477,Translate.text_4478,Translate.text_4479,Translate.text_4480,Translate.text_4481,Translate.text_4482,Translate.text_4483,Translate.text_4484,Translate.text_4485,Translate.text_4486,Translate.text_4487,Translate.text_4488,Translate.text_4489,Translate.text_4490,Translate.text_4491,Translate.text_4492,Translate.text_4493,Translate.text_4494,Translate.text_4495,Translate.text_4496,Translate.text_4497,Translate.text_4498,Translate.text_4499,Translate.text_4500,Translate.text_4501,Translate.text_4502,Translate.text_4503,Translate.text_4504,Translate.text_4505,Translate.text_4506,Translate.text_4507,Translate.text_4508,Translate.text_4509,Translate.text_4510,Translate.text_4511,Translate.text_4512,Translate.text_4513,Translate.text_4514,Translate.text_4515,Translate.text_4516,Translate.text_4517,Translate.text_4518,Translate.text_4519,Translate.text_4520,Translate.text_4521});
		
		template.addEffectAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectExplosionLastTime");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4613);
		skillField.setDisplayName(Translate.text_4614);
		skillField.setType(Byte.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491 , Translate.text_4472,Translate.text_4473,Translate.text_4474,Translate.text_4475,Translate.text_4476,Translate.text_4477,Translate.text_4478,Translate.text_4479,Translate.text_4480,Translate.text_4481,Translate.text_4482,Translate.text_4483,Translate.text_4484,Translate.text_4485,Translate.text_4486,Translate.text_4487,Translate.text_4488,Translate.text_4489,Translate.text_4490,Translate.text_4491,Translate.text_4492,Translate.text_4493,Translate.text_4494,Translate.text_4495,Translate.text_4496,Translate.text_4497,Translate.text_4498,Translate.text_4499,Translate.text_4500,Translate.text_4501,Translate.text_4502,Translate.text_4503,Translate.text_4504,Translate.text_4505,Translate.text_4506,Translate.text_4507,Translate.text_4508,Translate.text_4509,Translate.text_4510,Translate.text_4511,Translate.text_4512,Translate.text_4513,Translate.text_4514,Translate.text_4515,Translate.text_4516,Translate.text_4517,Translate.text_4518,Translate.text_4519,Translate.text_4520,Translate.text_4521});
		
		template.addEffectAnimationField(skillField);
		
		//级别相关的属性
		skillField = new SkillField();
		
		skillField.setFieldName("buffName");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4301);
		skillField.setDisplayName(Translate.text_4301);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{""});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLevel");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4405);
		skillField.setDisplayName(Translate.text_4406);
		skillField.setType(int[].class);
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLastingTime");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4586);
		skillField.setDisplayName(Translate.text_4587);
		skillField.setType(long[].class);
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffProbability");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4588);
		skillField.setDisplayName(Translate.text_4589);
		skillField.setType(int[].class);
		
		template.addLevelSkillField(skillField);

		skillField = new SkillField();
		
		skillField.setFieldName("attackDamages");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4590);
		skillField.setDisplayName(Translate.text_4591);
		skillField.setType(short[].class);
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("mp");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4598);
		skillField.setDisplayName(Translate.text_4599);
		skillField.setType(short[].class);
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initSkillWithTraceAndDirectionOrTarget(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		//普通攻击技能 
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.activeskills.SkillWithTraceAndDirectionOrTarget");
		template.setDisplayName(Translate.text_4638);
		template.setType(SkillTemplate.SKILL_TYPE_ACTIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("targetType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4639);
		skillField.setDisplayName(Translate.text_4640);
		skillField.setType(Byte.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4641 , Translate.text_4642});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("trackType");
		skillField.setDefaultValue("1");
		skillField.setDescription(Translate.text_4643);
		skillField.setDisplayName(Translate.text_4644);
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4595 , Translate.text_4645});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("speed");
		skillField.setDefaultValue("200");
		skillField.setDescription(Translate.text_4646);
		skillField.setDisplayName(Translate.text_4646);
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("range");
		skillField.setDefaultValue("500");
		skillField.setDescription(Translate.text_4647);
		skillField.setDisplayName(Translate.text_4647);
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("attackWidth");
		skillField.setDefaultValue("32");
		skillField.setDescription(Translate.text_4648);
		skillField.setDisplayName(Translate.text_4648);
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectNum");
		skillField.setDefaultValue("1");
		skillField.setDescription(Translate.text_4625);
		skillField.setDisplayName(Translate.text_4625);
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"});
		skillField.setSelectDisplayValues(new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectInitPositionX");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4649);
		skillField.setDisplayName(Translate.text_4650);
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectInitPositionY");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4651);
		skillField.setDisplayName(Translate.text_4650);
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectInitDirection");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4652);
		skillField.setDisplayName(Translate.text_4653);
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("penetrateNum");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4654);
		skillField.setDisplayName(Translate.text_4655);
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"0","1","2","3","4","5","6","7","8","9","10"});
		
		template.addBasicSkillField(skillField);

		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		
		skillField.setFieldName("duration1");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4447);
		skillField.setDisplayName(Translate.text_4448);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "500","550","600","650","700","750","800","850","900","1000","1200","1500","2000","2500","2700","3000","3500","4000","5000","8000","10000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491 , Translate.text_4449,Translate.text_4450,Translate.text_4451,Translate.text_4452,Translate.text_4453,Translate.text_4454,Translate.text_4455,Translate.text_4456,Translate.text_4457,Translate.text_4458,Translate.text_4459,Translate.text_4460,Translate.text_4461,Translate.text_4462,Translate.text_4463,Translate.text_4464,Translate.text_4465,Translate.text_4466,Translate.text_4467,Translate.text_4468,Translate.text_4469});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration2");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4470);
		skillField.setDisplayName(Translate.text_4471);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4472,Translate.text_4473,Translate.text_4474,Translate.text_4475,Translate.text_4476,Translate.text_4477,Translate.text_4478,Translate.text_4479,Translate.text_4480,Translate.text_4481,Translate.text_4482,Translate.text_4483,Translate.text_4484,Translate.text_4485,Translate.text_4486,Translate.text_4487,Translate.text_4488,Translate.text_4489,Translate.text_4490,Translate.text_4491,Translate.text_4492,Translate.text_4493,Translate.text_4494,Translate.text_4495,Translate.text_4496,Translate.text_4497,Translate.text_4498,Translate.text_4499,Translate.text_4500,Translate.text_4501,Translate.text_4502,Translate.text_4503,Translate.text_4504,Translate.text_4505,Translate.text_4506,Translate.text_4507,Translate.text_4508,Translate.text_4509,Translate.text_4510,Translate.text_4511,Translate.text_4512,Translate.text_4513,Translate.text_4514,Translate.text_4515,Translate.text_4516,Translate.text_4517,Translate.text_4518,Translate.text_4519,Translate.text_4520,Translate.text_4521});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration3");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4522);
		skillField.setDisplayName(Translate.text_4523);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1000","2000","3000","4000","5000","6000","7000","8000","9000","10000","11000","12000","13000","14000","15000","16000","17000","18000","19000","20000","21000","22000","23000","24000","25000","26000","27000","28000","29000","30000","31000","32000","33000","34000","35000","36000","37000","38000","39000","40000","41000","42000","43000","44000","45000","46000","47000","48000","49000","50000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4458,Translate.text_4461,Translate.text_4464,Translate.text_4466,Translate.text_4467,Translate.text_4524,Translate.text_4525,Translate.text_4468,Translate.text_4526,Translate.text_4469,Translate.text_4527,Translate.text_4528,Translate.text_4529,Translate.text_4530,Translate.text_4531,Translate.text_4532,Translate.text_4533,Translate.text_4534,Translate.text_4535,Translate.text_4536,Translate.text_4537,Translate.text_4538,Translate.text_4539,Translate.text_4540,Translate.text_4541,Translate.text_4542,Translate.text_4543,Translate.text_4544,Translate.text_4545,Translate.text_4546,Translate.text_4547,Translate.text_4548,Translate.text_4549,Translate.text_4550,Translate.text_4551,Translate.text_4552,Translate.text_4553,Translate.text_4554,Translate.text_4555,Translate.text_4556,Translate.text_4557,Translate.text_4558,Translate.text_4559,Translate.text_4560,Translate.text_4561,Translate.text_4562,Translate.text_4563,Translate.text_4564,Translate.text_4565,Translate.text_4566});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("style1");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4567);
		skillField.setDisplayName(Translate.text_4567);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//
//		DField df = playerMapping.get(5);
//		
//		ArrayList<String> al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		String ss[] = al.toArray(new String[0]);
//		String ssIndex[] = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		
		template.addActiveSkillAnimationField(skillField);

		
		skillField = new SkillField();
		
		skillField.setFieldName("style2");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4568);
		skillField.setDisplayName(Translate.text_4568);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		
//		df = playerMapping.get(5);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectiveTimes");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4569);
		skillField.setDisplayName(Translate.text_4570);
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("enableWeaponType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4571);
		skillField.setDisplayName(Translate.text_4571);
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4572 , Translate.text_4573});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("weaponTypeLimit");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4574);
		skillField.setDisplayName(Translate.text_4575);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		
//		
//		df = playerMapping.get(4);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("attackType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4576);
		skillField.setDisplayName(Translate.text_4577);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4578 , Translate.text_4579});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("followByCommonAttack");
		skillField.setDefaultValue("true");
		skillField.setDescription(Translate.text_4580);
		skillField.setDisplayName(Translate.text_4581);
		skillField.setType(Boolean.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"true","false"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_125 , Translate.text_126});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffTargetType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4582);
		skillField.setDisplayName(Translate.text_4583);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4584 , Translate.text_4585});
		
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
		
		//后效参数
		skillField = new SkillField();
		
		skillField.setFieldName("effectType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4609);
		skillField.setDisplayName(Translate.text_4610);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		
//		df = summonedMapping.get(0);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addEffectAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("explosionLastingTime");
		skillField.setDefaultValue("800");
		skillField.setDescription(Translate.text_4656);
		skillField.setDisplayName(Translate.text_4656);
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4657,Translate.text_4472,Translate.text_4473,Translate.text_4474,Translate.text_4475,Translate.text_4476,Translate.text_4477,Translate.text_4478,Translate.text_4479,Translate.text_4480,Translate.text_4481,Translate.text_4482,Translate.text_4483,Translate.text_4484,Translate.text_4485,Translate.text_4486,Translate.text_4487,Translate.text_4488,Translate.text_4489,Translate.text_4490,Translate.text_4491,Translate.text_4492,Translate.text_4493,Translate.text_4494,Translate.text_4495,Translate.text_4496,Translate.text_4497,Translate.text_4498,Translate.text_4499,Translate.text_4500,Translate.text_4501,Translate.text_4502,Translate.text_4503,Translate.text_4504,Translate.text_4505,Translate.text_4506,Translate.text_4507,Translate.text_4508,Translate.text_4509,Translate.text_4510,Translate.text_4511,Translate.text_4512,Translate.text_4513,Translate.text_4514,Translate.text_4515,Translate.text_4516,Translate.text_4517,Translate.text_4518,Translate.text_4519,Translate.text_4520,Translate.text_4521});
		
		template.addEffectAnimationField(skillField);
		
		//级别相关的属性
		skillField = new SkillField();
		
		skillField.setFieldName("buffName");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4301);
		skillField.setDisplayName(Translate.text_4301);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{""});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLevel");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4405);
		skillField.setDisplayName(Translate.text_4406);
		skillField.setType(int[].class);
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLastingTime");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4586);
		skillField.setDisplayName(Translate.text_4587);
		skillField.setType(long[].class);
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffProbability");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4588);
		skillField.setDisplayName(Translate.text_4589);
		skillField.setType(int[].class);
		
		template.addLevelSkillField(skillField);

		skillField = new SkillField();
		
		skillField.setFieldName("attackDamages");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4590);
		skillField.setDisplayName(Translate.text_4591);
		skillField.setType(short[].class);
		
		template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("mp");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4598);
		skillField.setDisplayName(Translate.text_4599);
		skillField.setType(short[].class);
		
		template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initCommonAttackSkill(  ){
		commonAttackSkillTemplate = new SkillTemplate();
		
		SkillTemplate template = commonAttackSkillTemplate;
		SkillField skillField = null;
		
		//普通攻击技能 
		template.setClassName("com.fy.engineserver.datasource.skill.activeskills.CommonAttackSkill");
		template.setDisplayName(Translate.text_4658);
		template.setType(SkillTemplate.SKILL_TYPE_ACTIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
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
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("1");
		skillField.setDescription(Translate.text_4282);
		skillField.setDisplayName(Translate.text_4282);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4283});
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue(Translate.text_4280);
		skillField.setDescription(Translate.text_4281);
		skillField.setDisplayName(Translate.text_4281);
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration1");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4447);
		skillField.setDisplayName(Translate.text_4448);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration2");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4470);
		skillField.setDisplayName(Translate.text_4471);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4472,Translate.text_4473,Translate.text_4474,Translate.text_4475,Translate.text_4476,Translate.text_4477,Translate.text_4478,Translate.text_4479,Translate.text_4480,Translate.text_4481,Translate.text_4482,Translate.text_4483,Translate.text_4484,Translate.text_4485,Translate.text_4486,Translate.text_4487,Translate.text_4488,Translate.text_4489,Translate.text_4490,Translate.text_4491,Translate.text_4492,Translate.text_4493,Translate.text_4494,Translate.text_4495,Translate.text_4496,Translate.text_4497,Translate.text_4498,Translate.text_4499,Translate.text_4500,Translate.text_4501,Translate.text_4502,Translate.text_4503,Translate.text_4504,Translate.text_4505,Translate.text_4506,Translate.text_4507,Translate.text_4508,Translate.text_4509,Translate.text_4510,Translate.text_4511,Translate.text_4512,Translate.text_4513,Translate.text_4514,Translate.text_4515,Translate.text_4516,Translate.text_4517,Translate.text_4518,Translate.text_4519,Translate.text_4520,Translate.text_4521});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration3");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4522);
		skillField.setDisplayName(Translate.text_4523);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1000","2000","3000","4000","5000","6000","7000","8000","9000","10000","11000","12000","13000","14000","15000","16000","17000","18000","19000","20000","21000","22000","23000","24000","25000","26000","27000","28000","29000","30000","31000","32000","33000","34000","35000","36000","37000","38000","39000","40000","41000","42000","43000","44000","45000","46000","47000","48000","49000","50000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4458,Translate.text_4461,Translate.text_4464,Translate.text_4466,Translate.text_4467,Translate.text_4524,Translate.text_4525,Translate.text_4468,Translate.text_4526,Translate.text_4469,Translate.text_4527,Translate.text_4528,Translate.text_4529,Translate.text_4530,Translate.text_4531,Translate.text_4532,Translate.text_4533,Translate.text_4534,Translate.text_4535,Translate.text_4536,Translate.text_4537,Translate.text_4538,Translate.text_4539,Translate.text_4540,Translate.text_4541,Translate.text_4542,Translate.text_4543,Translate.text_4544,Translate.text_4545,Translate.text_4546,Translate.text_4547,Translate.text_4548,Translate.text_4549,Translate.text_4550,Translate.text_4551,Translate.text_4552,Translate.text_4553,Translate.text_4554,Translate.text_4555,Translate.text_4556,Translate.text_4557,Translate.text_4558,Translate.text_4559,Translate.text_4560,Translate.text_4561,Translate.text_4562,Translate.text_4563,Translate.text_4564,Translate.text_4565,Translate.text_4566});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("style1");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4567);
		skillField.setDisplayName(Translate.text_4567);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//
//		DField df = playerMapping.get(5);
//		
//		ArrayList<String> al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		String ss[] = al.toArray(new String[0]);
//		String ssIndex[] = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		
		template.addActiveSkillAnimationField(skillField);

		
		skillField = new SkillField();
		
		skillField.setFieldName("style2");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4568);
		skillField.setDisplayName(Translate.text_4568);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		
//		df = playerMapping.get(5);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectiveTimes");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4569);
		skillField.setDisplayName(Translate.text_4570);
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("range");
		skillField.setDefaultValue("30");
		skillField.setDescription(Translate.text_4659);
		skillField.setDisplayName(Translate.text_4660);
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"30","50","70","90","110","130","150","180","200"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_4661,Translate.text_4662,Translate.text_4663,Translate.text_4664,Translate.text_4665,Translate.text_4666,Translate.text_4667,Translate.text_4668,Translate.text_4669});
		
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("weaponTypeLimit");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4670);
		skillField.setDisplayName(Translate.text_4671);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		
//		
//		df = playerMapping.get(4);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		
		//后效参数
		skillField = new SkillField();
		
		skillField.setFieldName("effectType");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4609);
		skillField.setDisplayName(Translate.text_4610);
		skillField.setType(Integer.TYPE);
//		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
//		
//		df = summonedMapping.get(0);
//		
//		al = new ArrayList<String>();
//		for(DValue dv : df){
//			al.add(dv.getName());
//		}
//		
//		ss = al.toArray(new String[0]);
//		ssIndex = new String[ss.length];
//		for(int j = 0 ; j < ssIndex.length ; j++){
//			ssIndex[j] = ""+j;
//		}
//		
//		skillField.setSelectValues(ssIndex);
//		skillField.setSelectDisplayValues(ss);
		
		template.addEffectAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectLastTime");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4611);
		skillField.setDisplayName(Translate.text_4612);
		skillField.setType(Byte.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491 , Translate.text_4472,Translate.text_4473,Translate.text_4474,Translate.text_4475,Translate.text_4476,Translate.text_4477,Translate.text_4478,Translate.text_4479,Translate.text_4480,Translate.text_4481,Translate.text_4482,Translate.text_4483,Translate.text_4484,Translate.text_4485,Translate.text_4486,Translate.text_4487,Translate.text_4488,Translate.text_4489,Translate.text_4490,Translate.text_4491,Translate.text_4492,Translate.text_4493,Translate.text_4494,Translate.text_4495,Translate.text_4496,Translate.text_4497,Translate.text_4498,Translate.text_4499,Translate.text_4500,Translate.text_4501,Translate.text_4502,Translate.text_4503,Translate.text_4504,Translate.text_4505,Translate.text_4506,Translate.text_4507,Translate.text_4508,Translate.text_4509,Translate.text_4510,Translate.text_4511,Translate.text_4512,Translate.text_4513,Translate.text_4514,Translate.text_4515,Translate.text_4516,Translate.text_4517,Translate.text_4518,Translate.text_4519,Translate.text_4520,Translate.text_4521});
		
		template.addEffectAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectExplosionLastTime");
		skillField.setDefaultValue("0");
		skillField.setDescription(Translate.text_4613);
		skillField.setDisplayName(Translate.text_4614);
		skillField.setType(Byte.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{Translate.text_1491 , Translate.text_4472,Translate.text_4473,Translate.text_4474,Translate.text_4475,Translate.text_4476,Translate.text_4477,Translate.text_4478,Translate.text_4479,Translate.text_4480,Translate.text_4481,Translate.text_4482,Translate.text_4483,Translate.text_4484,Translate.text_4485,Translate.text_4486,Translate.text_4487,Translate.text_4488,Translate.text_4489,Translate.text_4490,Translate.text_4491,Translate.text_4492,Translate.text_4493,Translate.text_4494,Translate.text_4495,Translate.text_4496,Translate.text_4497,Translate.text_4498,Translate.text_4499,Translate.text_4500,Translate.text_4501,Translate.text_4502,Translate.text_4503,Translate.text_4504,Translate.text_4505,Translate.text_4506,Translate.text_4507,Translate.text_4508,Translate.text_4509,Translate.text_4510,Translate.text_4511,Translate.text_4512,Translate.text_4513,Translate.text_4514,Translate.text_4515,Translate.text_4516,Translate.text_4517,Translate.text_4518,Translate.text_4519,Translate.text_4520,Translate.text_4521});
		
		template.addEffectAnimationField(skillField);
		
		
		
		
		
		skillField = new SkillField();
		
		skillField.setFieldName("attackDamages");
		skillField.setDefaultValue("");
		skillField.setDescription(Translate.text_4590);
		skillField.setDisplayName(Translate.text_4591);
		skillField.setType(short[].class);
		
		template.addLevelSkillField(skillField);
		
//		templates.put(template.getClassName(), template);
	}
	
	public SkillTemplate[] getSkillTemplates(){
		return templates.values().toArray(new SkillTemplate[0]);
	}

	public SkillTemplate[] getActiveSkillTemplates(){
		SkillTemplate[] skillTemplates = getSkillTemplates();
		List<SkillTemplate> l = new ArrayList<SkillTemplate>();
		for (int i = 0; i < skillTemplates.length; i++) {
			if(skillTemplates[i].getType() == SkillTemplate.SKILL_TYPE_ACTIVE){
				l.add(skillTemplates[i]);
			}
		}
		
		return l.toArray(new SkillTemplate[0]);
	}
	
	public SkillTemplate[] getPassiveSkillTemplates(){
		SkillTemplate[] skillTemplates = getSkillTemplates();
		List<SkillTemplate> l = new ArrayList<SkillTemplate>();
		for (int i = 0; i < skillTemplates.length; i++) {
			if(skillTemplates[i].getType() == SkillTemplate.SKILL_TYPE_PASSIVE){
				l.add(skillTemplates[i]);
			}
		}
		return l.toArray(new SkillTemplate[0]);
	}
	
	public SkillTemplate[] getAuraSkillTemplates(){
		SkillTemplate[] skillTemplates = getSkillTemplates();
		List<SkillTemplate> l = new ArrayList<SkillTemplate>();
		for (int i = 0; i < skillTemplates.length; i++) {
			if(skillTemplates[i].getType() == SkillTemplate.SKILL_TYPE_AURA){
				l.add(skillTemplates[i]);
			}
		}
		
		return l.toArray(new SkillTemplate[0]);
	}
	
	public SkillTemplate getSkillTemplate(String name){
		if(name.equals("com.fy.engineserver.datasource.skill.activeskills.CommonAttackSkill")){
			return commonAttackSkillTemplate;
		}
		return templates.get(name);
	}

	public SkillCategory[] getSkillCategories(){
		return categories.values().toArray(new SkillCategory[0]);
	}

	private final static String FIELD_BUFF_NAME = "buffName";
	public void buffReceived(String[] buffs){
		String[] nbf = new String[buffs.length + 1];
		nbf[0] = "";
		System.arraycopy(buffs, 0, nbf, 1, buffs.length);
		
		String[] nbf2 = new String[nbf.length];
		nbf2[0] = Translate.text_1491;
		System.arraycopy(nbf, 1, nbf2, 1, nbf2.length - 1);
		
		SkillTemplate[] templates = getSkillTemplates();
		
		for (SkillTemplate template : templates) {
			SkillField[] fields = template.getSkillFields();
			for (SkillField field : fields) {
				if(field.getFieldName().equals(FIELD_BUFF_NAME)){
					field.setSelectValues(nbf);
					field.setSelectDisplayValues(nbf2);
				}
			}
		}
	}
}

