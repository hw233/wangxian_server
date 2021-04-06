<%@ page contentType="text/html;charset=utf-8"
	import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.datasource.skill.*,com.xuanzhi.tools.transport.*,com.google.gson.Gson,com.fy.engineserver.core.*,java.io.*,java.lang.reflect.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.jsp.propertyvalue.*"%><%! 
	
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
		template.setDisplayName("光环技能");
		template.setType(SkillTemplate.SKILL_TYPE_AURA);
		
		//基本参数
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("rangeWidth");
		skillField.setDefaultValue("30");
		skillField.setDescription("作用范围，以自身为中心的一个矩形 矩形的宽度");
		skillField.setDisplayName("矩形的宽度");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("rangeHeight");
		skillField.setDefaultValue("30");
		skillField.setDescription("作用范围，以自身为中心的一个矩形 矩形的高度");
		skillField.setDisplayName("矩形的高度");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("rangeType");
		skillField.setDefaultValue("30");
		skillField.setDescription("范围的类型： 0 范围内的所有的队友，包括自己 1 范围内的所有的队友，中立方，和自己 2 范围内的所有的中立方 3 范围内的所有的敌方 4 范围内的所有的中立方，敌方");
		skillField.setDisplayName("范围的类型");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1" , "2" , "3" , "4"});
		skillField.setSelectDisplayValues(new String[]{"范围内的所有的队友，包括自己","范围内的所有的队友，中立方，和自己","范围内的所有的中立方","范围内的所有的敌方","范围内的所有的中立方，敌方"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("auraType");
		skillField.setDefaultValue("0");
		skillField.setDescription("光环类型，此数值表示玩家脚下的光环 此值与Mapping编辑器中的下标对应，从0开始");
		skillField.setDisplayName("光环类型");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4"});
		skillField.setSelectDisplayValues(new String[]{"武当","峨眉","明教","五毒","少林"});
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
		skillField.setDescription("使用的Buff的名称");
		skillField.setDisplayName("使用的Buff的名称");
		skillField.setType(Integer.TYPE);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		//级别相关
		skillField = new SkillField();
		
		skillField.setFieldName("buffLevel");
		skillField.setDefaultValue("");
		skillField.setDescription("级BUFF");
		skillField.setDisplayName("BUFF级别");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		
		templates.put(template.getClassName(), template);
	}
	
	private void initAssistActiveSkillPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.AssistActiveSkillPassiveSkill");
		template.setDisplayName("被动技能修改主动技能");
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		/**
		 * 数组的下标为此被动技能的等级
		 * 
		 * HashMap结构为：
		 *         主动技能的名字 -->  ActiveSkillParam
		 *         
		 * 标识此技能要修改的主动技能的属性，可以修改多个主动技能  
		 */
		skillField = new SkillField();
		skillField.setFieldName("maps");
		skillField.setDefaultValue("");
		skillField.setDescription("标识此技能要修改的主动技能的属性，可以修改多个主动技能");
		skillField.setDisplayName("要修改的主动技能的属性");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_MAP);
		template.addMapFields(skillField);
		
		skillField = new SkillField();

		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("desps");
		skillField.setDefaultValue("");
		skillField.setDescription("");
		skillField.setDisplayName("每个级别的描述");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initIncreaseLiliangPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseLiliangPassiveSkill");
		template.setDisplayName("增加力量");
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("strength");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("增加力量的百分比");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initIncreaseMinjiePassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseMinjiePassiveSkill");
		template.setDisplayName("增加敏捷");
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("dexterity");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("增加敏捷百分比");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initIncreaseTizhiPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseTizhiPassiveSkill");
		template.setDisplayName("增加耐力");
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("constitution");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("增加耐力百分比");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initIncreaseZhiliPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseZhiliPassiveSkill");
		template.setDisplayName("增加智力");
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("spellpower");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("增加智力百分比");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initIncreaseBaoJiPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseBaoJiPassiveSkill");
		template.setDisplayName("增加暴击");
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("baoJi");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("会心一击百分比");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initIncreaseDuoBiPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseDuoBiPassiveSkill");
		template.setDisplayName("增加躲避");
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("dodgeC");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("躲避百分比");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseFaFangPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseFaFangPassiveSkill");
		template.setDisplayName("增加法防");
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("resistance");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("法防百分比");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseFaGongPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseFaGongPassiveSkill");
		template.setDisplayName("增加法攻");
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});

		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("spellDamageUpperLimitC");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("道术攻击伤害力的上限百分比");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("spellDamageLowerLimitC");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("道术攻击伤害力的下限百分比");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseHpAndMpPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseHpAndMpPassiveSkill");
		template.setDisplayName("增加生命和法力");
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("totalHpC");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("角色的最大生命值百分比");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("totalMpC");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("角色的最大魔法值百分比");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseHpPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseHpPassiveSkill");
		template.setDisplayName("增加生命值上限");
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("totalHpC");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("角色的最大生命值百分比");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseHpRecoveryAndMpRecoveryPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseHpRecoveryAndMpRecoveryPassiveSkill");
		template.setDisplayName("增加生命和法力恢复");
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("hpRecovery");
		skillField.setDefaultValue("");
		skillField.setDescription("(每五秒恢复一次)");
		skillField.setDisplayName("生命力恢复速度");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("mpRecovery");
		skillField.setDefaultValue("");
		skillField.setDescription("(每五秒恢复一次)");
		skillField.setDisplayName("魔法恢复速度");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseHpRecoveryPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseHpRecoveryPassiveSkill");
		template.setDisplayName("增加生命恢复");
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});		
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("hpRecovery");
		skillField.setDefaultValue("");
		skillField.setDescription("(每五秒恢复一次)");
		skillField.setDisplayName("生命力恢复速度");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseMingZhongAndDuoBiPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseMingZhongAndDuoBiPassiveSkill");
		template.setDisplayName("增加命中和躲避");
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		
		skillField = new SkillField();
		
		skillField.setFieldName("dodgeC");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("躲避百分比");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("attackRatingC");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("命中百分比");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseMingZhongPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseMingZhongPassiveSkill");
		template.setDisplayName("增加命中");
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("attackRatingC");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("命中百分比");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseMpPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseMpPassiveSkill");
		template.setDisplayName("增加法力最大值");
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("totalMpC");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("角色的最大魔法值百分比");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseMpRecoveryPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseMpRecoveryPassiveSkill");
		template.setDisplayName("增加法力恢复速度");
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("mpRecovery");
		skillField.setDefaultValue("");
		skillField.setDescription("(每五秒恢复一次)");
		skillField.setDisplayName("魔法恢复速度");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseWuFangAndFaFangPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseWuFangAndFaFangPassiveSkill");
		template.setDisplayName("增加物防和法防");
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("physicalDefenceC");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("物理防御百分比 ");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("resistance");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("法防百分比 ");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseWuFangPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseWuFangPassiveSkill");
		template.setDisplayName("增加物防");
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("physicalDefenceC");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("物理防御百分比 ");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	private void initIncreaseWuGongAndFaGongPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseWuGongAndFaGongPassiveSkill");
		template.setDisplayName("增加物攻和法攻");
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("physicalDamageUpperLimitC");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("物理攻击上限百分比 ");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("physicalDamagerLowerLimitC");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("物理攻击下限百分比 ");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("spellDamageUpperLimitC");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("道术攻击伤害力的上限百分比 ");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("spellDamageLowerLimitC");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("道术攻击伤害力的下限百分比 ");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initIncreaseWuGongPassiveSkill(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.passiveskills.IncreaseWuGongPassiveSkill");
		template.setDisplayName("增加物攻");
		template.setType(SkillTemplate.SKILL_TYPE_PASSIVE);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("physicalDamageUpperLimitC");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("物理攻击上限百分比 ");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);

		
		skillField = new SkillField();
		
		skillField.setFieldName("physicalDamagerLowerLimitC");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("物理攻击下限百分比 ");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);

		
		templates.put(template.getClassName(), template);
	}
	
	private void initSkillWithoutEffect(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		//普通攻击技能 
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.activeskills.SkillWithoutEffect");
		template.setDisplayName("无后效的平砍技能(不推荐使用)");
		template.setType(SkillTemplate.SKILL_TYPE_ACTIVE);
		
		//类中只有get方法，没有set方法，为了显示方便添加此属性
		skillField = new SkillField();
		skillField.setFieldName("interval");
		skillField.setDefaultValue("50");
		skillField.setDescription("技能释放周期");
		skillField.setDisplayName("技能释放周期");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		template.addBasicSkillField(skillField);
		
		/**
		 * 技能伤害或者加血计算公式：
		 * 
		 * 	 武器伤害 *（param1 + param2 * 技能等级）
		 * + 攻击强度 *（param3 + param4 * 技能等级）
		 * + 法术强度 *（param5 + param6 * 技能等级）
		 * + param7 + param8 * 技能等级
		 */
		skillField = new SkillField();
		
		skillField.setFieldName("param1");
		skillField.setDefaultValue("0");
		skillField.setDescription("param1");
		skillField.setDisplayName("param1");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addDataFields(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("param2");
		skillField.setDefaultValue("0");
		skillField.setDescription("param2");
		skillField.setDisplayName("param2");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addDataFields(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("param3");
		skillField.setDefaultValue("0");
		skillField.setDescription("param3");
		skillField.setDisplayName("param3");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addDataFields(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("param4");
		skillField.setDefaultValue("0");
		skillField.setDescription("param4");
		skillField.setDisplayName("param4");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addDataFields(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("param5");
		skillField.setDefaultValue("0");
		skillField.setDescription("param5");
		skillField.setDisplayName("param5");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addDataFields(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("param6");
		skillField.setDefaultValue("0");
		skillField.setDescription("param6");
		skillField.setDisplayName("param6");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addDataFields(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("param7");
		skillField.setDefaultValue("0");
		skillField.setDescription("param7");
		skillField.setDisplayName("param7");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addDataFields(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("param8");
		skillField.setDefaultValue("0");
		skillField.setDescription("param8");
		skillField.setDisplayName("param8");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addDataFields(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("range");
		skillField.setDefaultValue("50");
		skillField.setDescription("普通攻击的有效距离");
		skillField.setDisplayName("普通攻击的有效距离");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration1");
		skillField.setDefaultValue("");
		skillField.setDescription("施法时间，在这段时间内，播放僵直动画，时间为毫秒，此时间为0时，不会播放僵直动画");
		skillField.setDisplayName("施法时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "500","550","600","650","700","750","800","850","900","1000","1200","1500","2000","2500","2700","3000","3500","4000","5000","8000","10000"});
		skillField.setSelectDisplayValues(new String[]{"无" , "0.5秒","0.55秒","0.6秒","0.65秒","0.7秒","0.75秒","0.8秒","0.85秒","0.9秒","1秒","1.2秒","1.5秒","2秒","2.5秒","2.7秒","3秒","3.5秒","4秒","5秒","8秒","10秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration2");
		skillField.setDefaultValue("");
		skillField.setDescription("攻击动画时间，这段时间内，播放攻击动画，时间为毫秒");
		skillField.setDisplayName("攻击动画时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{"100毫秒","200毫秒","300毫秒","400毫秒","500毫秒","600毫秒","700毫秒","800毫秒","900毫秒","1000毫秒","1100毫秒","1200毫秒","1300毫秒","1400毫秒","1500毫秒","1600毫秒","1700毫秒","1800毫秒","1900毫秒","2000毫秒","2100毫秒","2200毫秒","2300毫秒","2400毫秒","2500毫秒","2600毫秒","2700毫秒","2800毫秒","2900毫秒","3000毫秒","3100毫秒","3200毫秒","3300毫秒","3400毫秒","3500毫秒","3600毫秒","3700毫秒","3800毫秒","3900毫秒","4000毫秒","4100毫秒","4200毫秒","4300毫秒","4400毫秒","4500毫秒","4600毫秒","4700毫秒","4800毫秒","4900毫秒","5000毫秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration3");
		skillField.setDefaultValue("");
		skillField.setDescription("冷却时间，这段时间内，人物状态会回到站立的状态");
		skillField.setDisplayName("冷却时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1000","2000","3000","4000","5000","6000","7000","8000","9000","10000","11000","12000","13000","14000","15000","16000","17000","18000","19000","20000","21000","22000","23000","24000","25000","26000","27000","28000","29000","30000","31000","32000","33000","34000","35000","36000","37000","38000","39000","40000","41000","42000","43000","44000","45000","46000","47000","48000","49000","50000"});
		skillField.setSelectDisplayValues(new String[]{"1秒","2秒","3秒","4秒","5秒","6秒","7秒","8秒","9秒","10秒","11秒","12秒","13秒","14秒","15秒","16秒","17秒","18秒","19秒","20秒","21秒","22秒","23秒","24秒","25秒","26秒","27秒","28秒","29秒","30秒","31秒","32秒","33秒","34秒","35秒","36秒","37秒","38秒","39秒","40秒","41秒","42秒","43秒","44秒","45秒","46秒","47秒","48秒","49秒","50秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("style1");
		skillField.setDefaultValue("0");
		skillField.setDescription("僵直动画的风格");
		skillField.setDisplayName("僵直动画的风格");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9"});
		skillField.setSelectDisplayValues(new String[]{"风格1","风格2","风格3","风格4","风格5","风格6","风格7","风格8","风格9","风格10"});
		//skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);

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
		skillField.setDescription("攻击动画的风格");
		skillField.setDisplayName("攻击动画的风格");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9"});
		skillField.setSelectDisplayValues(new String[]{"风格1","风格2","风格3","风格4","风格5","风格6","风格7","风格8","风格9","风格10"});
		//skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		
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
		skillField.setDescription("一个或多个出招时间，跟等级无关，请填入以逗号分隔的值，例如 100,200,300");
		skillField.setDisplayName("出招时间，跟等级无关");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("enableWeaponType");
		skillField.setDefaultValue("0");
		skillField.setDescription("是否有武器限制");
		skillField.setDisplayName("是否有武器限制");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{"没有限制" , "有限制"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("weaponTypeLimit");
		skillField.setDefaultValue("0");
		skillField.setDescription("武器具体的限制是什么");
		skillField.setDisplayName("武器限制");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"空手","刀","剑","棍","匕首","弓"});
		
		
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
		skillField.setDescription("攻击类型，表示物理攻击还是法术攻击 0 表示物理攻击 1 表示法术攻击");
		skillField.setDisplayName("攻击类型");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{"物理攻击" , "法术攻击"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("followByCommonAttack");
		skillField.setDefaultValue("true");
		skillField.setDescription("该技能用完之后，是否用普通攻击 还是继续用此技能攻击，主要区分 物理系攻击和法系攻击");
		skillField.setDisplayName("继续用普通攻击");
		skillField.setType(Boolean.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"true","false"});
		skillField.setSelectDisplayValues(new String[]{"是" , "否"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffTargetType");
		skillField.setDefaultValue("0");
		skillField.setDescription("buff作用的对象：0 表示作用在目标身上 1 表示作用在施法者身上， 此属性对各个级别的技能有效");
		skillField.setDisplayName("BUFF作用对象");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{"作用于目标" , "作用于施法者"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
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
		skillField.setDescription("使用的Buff的名称");
		skillField.setDisplayName("使用的Buff的名称");
		skillField.setType(Integer.TYPE);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLevel");
		skillField.setDefaultValue("");
		skillField.setDescription("级BUFF");
		skillField.setDisplayName("BUFF级别");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLastingTime");
		skillField.setDefaultValue("");
		skillField.setDescription("毫秒");
		skillField.setDisplayName("BUFF持续时间");
		skillField.setType(long[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffProbability");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("BUFF产生概率");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);

		skillField = new SkillField();
		
		skillField.setFieldName("attackDamages");
		skillField.setDefaultValue("");
		skillField.setDescription("点");
		skillField.setDisplayName("技能的伤害");
		skillField.setType(short[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initSkillWithoutEffectAndQuickMove(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		//普通攻击技能 
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.activeskills.SkillWithoutEffectAndQuickMove");
		template.setDisplayName("冲锋技能");
		template.setType(SkillTemplate.SKILL_TYPE_ACTIVE);
		
		//类中只有get方法，没有set方法，为了显示方便添加此属性
		skillField = new SkillField();
		skillField.setFieldName("interval");
		skillField.setDefaultValue("50");
		skillField.setDescription("技能释放周期");
		skillField.setDisplayName("技能释放周期");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		template.addBasicSkillField(skillField);
		
		/**
		 * 技能伤害或者加血计算公式：
		 * 
		 * 	 武器伤害 *（param1 + param2 * 技能等级）
		 * + 攻击强度 *（param3 + param4 * 技能等级）
		 * + 法术强度 *（param5 + param6 * 技能等级）
		 * + param7 + param8 * 技能等级
		 */
		 skillField = new SkillField();
			
			skillField.setFieldName("param1");
			skillField.setDefaultValue("0");
			skillField.setDescription("param1");
			skillField.setDisplayName("param1");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param2");
			skillField.setDefaultValue("0");
			skillField.setDescription("param2");
			skillField.setDisplayName("param2");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param3");
			skillField.setDefaultValue("0");
			skillField.setDescription("param3");
			skillField.setDisplayName("param3");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param4");
			skillField.setDefaultValue("0");
			skillField.setDescription("param4");
			skillField.setDisplayName("param4");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param5");
			skillField.setDefaultValue("0");
			skillField.setDescription("param5");
			skillField.setDisplayName("param5");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param6");
			skillField.setDefaultValue("0");
			skillField.setDescription("param6");
			skillField.setDisplayName("param6");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param7");
			skillField.setDefaultValue("0");
			skillField.setDescription("param7");
			skillField.setDisplayName("param7");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param8");
			skillField.setDefaultValue("0");
			skillField.setDescription("param8");
			skillField.setDisplayName("param8");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("range");
		skillField.setDefaultValue("50");
		skillField.setDescription("普通攻击的有效距离");
		skillField.setDisplayName("普通攻击的有效距离");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("traceType");
		skillField.setDefaultValue("0");
		skillField.setDescription("路径类型，0表示直线，1表示寻路出来的路径");
		skillField.setDisplayName("路径类型");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1"});
		skillField.setSelectDisplayValues(new String[]{"直线" , "寻路路径"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("distance");
		skillField.setDefaultValue("20");
		skillField.setDescription("距离目标点多远停下来");
		skillField.setDisplayName("距离目标点多远停下来");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration1");
		skillField.setDefaultValue("");
		skillField.setDescription("施法时间，在这段时间内，播放僵直动画，时间为毫秒，此时间为0时，不会播放僵直动画");
		skillField.setDisplayName("施法时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "500","550","600","650","700","750","800","850","900","1000","1200","1500","2000","2500","2700","3000","3500","4000","5000","8000","10000"});
		skillField.setSelectDisplayValues(new String[]{"无" , "0.5秒","0.55秒","0.6秒","0.65秒","0.7秒","0.75秒","0.8秒","0.85秒","0.9秒","1秒","1.2秒","1.5秒","2秒","2.5秒","2.7秒","3秒","3.5秒","4秒","5秒","8秒","10秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration2");
		skillField.setDefaultValue("");
		skillField.setDescription("攻击动画时间，这段时间内，播放攻击动画，时间为毫秒");
		skillField.setDisplayName("攻击动画时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{"100毫秒","200毫秒","300毫秒","400毫秒","500毫秒","600毫秒","700毫秒","800毫秒","900毫秒","1000毫秒","1100毫秒","1200毫秒","1300毫秒","1400毫秒","1500毫秒","1600毫秒","1700毫秒","1800毫秒","1900毫秒","2000毫秒","2100毫秒","2200毫秒","2300毫秒","2400毫秒","2500毫秒","2600毫秒","2700毫秒","2800毫秒","2900毫秒","3000毫秒","3100毫秒","3200毫秒","3300毫秒","3400毫秒","3500毫秒","3600毫秒","3700毫秒","3800毫秒","3900毫秒","4000毫秒","4100毫秒","4200毫秒","4300毫秒","4400毫秒","4500毫秒","4600毫秒","4700毫秒","4800毫秒","4900毫秒","5000毫秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration3");
		skillField.setDefaultValue("");
		skillField.setDescription("冷却时间，这段时间内，人物状态会回到站立的状态");
		skillField.setDisplayName("冷却时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1000","2000","3000","4000","5000","6000","7000","8000","9000","10000","11000","12000","13000","14000","15000","16000","17000","18000","19000","20000","21000","22000","23000","24000","25000","26000","27000","28000","29000","30000","31000","32000","33000","34000","35000","36000","37000","38000","39000","40000","41000","42000","43000","44000","45000","46000","47000","48000","49000","50000"});
		skillField.setSelectDisplayValues(new String[]{"1秒","2秒","3秒","4秒","5秒","6秒","7秒","8秒","9秒","10秒","11秒","12秒","13秒","14秒","15秒","16秒","17秒","18秒","19秒","20秒","21秒","22秒","23秒","24秒","25秒","26秒","27秒","28秒","29秒","30秒","31秒","32秒","33秒","34秒","35秒","36秒","37秒","38秒","39秒","40秒","41秒","42秒","43秒","44秒","45秒","46秒","47秒","48秒","49秒","50秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("style1");
		skillField.setDefaultValue("0");
		skillField.setDescription("僵直动画的风格");
		skillField.setDisplayName("僵直动画的风格");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9"});
		skillField.setSelectDisplayValues(new String[]{"风格1","风格2","风格3","风格4","风格5","风格6","风格7","风格8","风格9","风格10"});
		//skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);

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
		skillField.setDescription("攻击动画的风格");
		skillField.setDisplayName("攻击动画的风格");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9"});
		skillField.setSelectDisplayValues(new String[]{"风格1","风格2","风格3","风格4","风格5","风格6","风格7","风格8","风格9","风格10"});
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
		skillField.setDescription("一个或多个出招时间，跟等级无关，请填入以逗号分隔的值，例如 100,200,300");
		skillField.setDisplayName("出招时间，跟等级无关");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("enableWeaponType");
		skillField.setDefaultValue("0");
		skillField.setDescription("是否有武器限制");
		skillField.setDisplayName("是否有武器限制");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{"没有限制" , "有限制"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("weaponTypeLimit");
		skillField.setDefaultValue("0");
		skillField.setDescription("武器具体的限制是什么");
		skillField.setDisplayName("武器限制");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"空手","刀","剑","棍","匕首","弓"});
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
		skillField.setDescription("攻击类型，表示物理攻击还是法术攻击 0 表示物理攻击 1 表示法术攻击");
		skillField.setDisplayName("攻击类型");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{"物理攻击" , "法术攻击"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("followByCommonAttack");
		skillField.setDefaultValue("true");
		skillField.setDescription("该技能用完之后，是否用普通攻击 还是继续用此技能攻击，主要区分 物理系攻击和法系攻击");
		skillField.setDisplayName("继续用普通攻击");
		skillField.setType(Boolean.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"true","false"});
		skillField.setSelectDisplayValues(new String[]{"是" , "否"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffTargetType");
		skillField.setDefaultValue("0");
		skillField.setDescription("buff作用的对象：0 表示作用在目标身上 1 表示作用在施法者身上， 此属性对各个级别的技能有效");
		skillField.setDisplayName("BUFF作用对象");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{"作用于目标" , "作用于施法者"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		//级别相关的属性
		skillField = new SkillField();
		
		skillField.setFieldName("buffName");
		skillField.setDefaultValue("");
		skillField.setDescription("使用的Buff的名称");
		skillField.setDisplayName("使用的Buff的名称");
		skillField.setType(Integer.TYPE);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLevel");
		skillField.setDefaultValue("");
		skillField.setDescription("级BUFF");
		skillField.setDisplayName("BUFF级别");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLastingTime");
		skillField.setDefaultValue("");
		skillField.setDescription("毫秒");
		skillField.setDisplayName("BUFF持续时间");
		skillField.setType(long[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffProbability");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("BUFF产生概率");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		

		skillField = new SkillField();
		
		skillField.setFieldName("attackDamages");
		skillField.setDefaultValue("");
		skillField.setDescription("点");
		skillField.setDisplayName("技能的伤害");
		skillField.setType(short[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("mp");
		skillField.setDefaultValue("");
		skillField.setDescription("点");
		skillField.setDisplayName("消耗的法力值");
		skillField.setType(short[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		//template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initSkillWithoutTraceAndOnTeamMember(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		//普通攻击技能 
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndOnTeamMember");
		template.setDisplayName("给目标只加BUFF的技能");
		template.setType(SkillTemplate.SKILL_TYPE_ACTIVE);
		
		//类中只有get方法，没有set方法，为了显示方便添加此属性
		skillField = new SkillField();
		skillField.setFieldName("interval");
		skillField.setDefaultValue("50");
		skillField.setDescription("技能释放周期");
		skillField.setDisplayName("技能释放周期");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		template.addBasicSkillField(skillField);
		
		/**
		 * 技能伤害或者加血计算公式：
		 * 
		 * 	 武器伤害 *（param1 + param2 * 技能等级）
		 * + 攻击强度 *（param3 + param4 * 技能等级）
		 * + 法术强度 *（param5 + param6 * 技能等级）
		 * + param7 + param8 * 技能等级
		 */
		 skillField = new SkillField();
			
			skillField.setFieldName("param1");
			skillField.setDefaultValue("0");
			skillField.setDescription("param1");
			skillField.setDisplayName("param1");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param2");
			skillField.setDefaultValue("0");
			skillField.setDescription("param2");
			skillField.setDisplayName("param2");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param3");
			skillField.setDefaultValue("0");
			skillField.setDescription("param3");
			skillField.setDisplayName("param3");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param4");
			skillField.setDefaultValue("0");
			skillField.setDescription("param4");
			skillField.setDisplayName("param4");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param5");
			skillField.setDefaultValue("0");
			skillField.setDescription("param5");
			skillField.setDisplayName("param5");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param6");
			skillField.setDefaultValue("0");
			skillField.setDescription("param6");
			skillField.setDisplayName("param6");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param7");
			skillField.setDefaultValue("0");
			skillField.setDescription("param7");
			skillField.setDisplayName("param7");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param8");
			skillField.setDefaultValue("0");
			skillField.setDescription("param8");
			skillField.setDisplayName("param8");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("rangeType");
		skillField.setDefaultValue("");
		skillField.setDescription("范围的类型：0 范围内的所有的队友，包括自己 1 范围内的所有的队友，中立方，和自己 2 范围内的所有的中立方 3 范围内的所有的敌方 4 范围内的所有的中立方，敌方");
		skillField.setDisplayName("范围的类型");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1" , "2" , "3" , "4" , "5" , "6" , "7"});
		skillField.setSelectDisplayValues(new String[]{"作用于选择的目标，目标必须是队友或中立方，目标必须在范围内" , "作用于选择的目标，目标必须是敌方，目标必须在范围内" , "作用于自己，无视范围" , "范围内的所有的队友，包括自己" , "范围内的所有的队友，中立方，和自己" , "范围内的所有的中立方" , "范围内的所有的敌方" ,  "范围内的所有的中立方，敌方"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("rangeWidth");
		skillField.setDefaultValue("");
		skillField.setDescription("以玩家自身为中心的，一个矩形，此参数为矩形的宽度");
		skillField.setDisplayName("矩形宽度");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","323","324","325","326","327","328","329","330","331","332","333","334","335","336","337","338","339","340","341","342","343","344","345","346","347","348","349","350","351","352","353","354","355","356","357","358","359","360","361","362","363","364","365","366","367","368","369","370","371","372","373","374","375","376","377","378","379","380","381","382","383","384","385","386","387","388","389","390","391","392","393","394","395","396","397","398","399","400","401","402","403","404","405","406","407","408","409","410","411","412","413","414","415","416","417","418","419","420","421","422","423","424","425","426","427","428","429","430","431","432","433","434","435","436","437","438","439","440","441","442","443","444","445","446","447","448","449","450","451","452","453","454","455","456","457","458","459","460","461","462","463","464","465","466","467","468","469","470","471","472","473","474","475","476","477","478","479","480","481","482","483","484","485","486","487","488","489","490","491","492","493","494","495","496","497","498","499","500"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","323","324","325","326","327","328","329","330","331","332","333","334","335","336","337","338","339","340","341","342","343","344","345","346","347","348","349","350","351","352","353","354","355","356","357","358","359","360","361","362","363","364","365","366","367","368","369","370","371","372","373","374","375","376","377","378","379","380","381","382","383","384","385","386","387","388","389","390","391","392","393","394","395","396","397","398","399","400","401","402","403","404","405","406","407","408","409","410","411","412","413","414","415","416","417","418","419","420","421","422","423","424","425","426","427","428","429","430","431","432","433","434","435","436","437","438","439","440","441","442","443","444","445","446","447","448","449","450","451","452","453","454","455","456","457","458","459","460","461","462","463","464","465","466","467","468","469","470","471","472","473","474","475","476","477","478","479","480","481","482","483","484","485","486","487","488","489","490","491","492","493","494","495","496","497","498","499","500"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("rangeHeight");
		skillField.setDefaultValue("");
		skillField.setDescription("以玩家自身为中心的，一个矩形，此参数为矩形的高度");
		skillField.setDisplayName("矩形高度");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","323","324","325","326","327","328","329","330","331","332","333","334","335","336","337","338","339","340","341","342","343","344","345","346","347","348","349","350","351","352","353","354","355","356","357","358","359","360","361","362","363","364","365","366","367","368","369","370","371","372","373","374","375","376","377","378","379","380","381","382","383","384","385","386","387","388","389","390","391","392","393","394","395","396","397","398","399","400","401","402","403","404","405","406","407","408","409","410","411","412","413","414","415","416","417","418","419","420","421","422","423","424","425","426","427","428","429","430","431","432","433","434","435","436","437","438","439","440","441","442","443","444","445","446","447","448","449","450","451","452","453","454","455","456","457","458","459","460","461","462","463","464","465","466","467","468","469","470","471","472","473","474","475","476","477","478","479","480","481","482","483","484","485","486","487","488","489","490","491","492","493","494","495","496","497","498","499","500"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","323","324","325","326","327","328","329","330","331","332","333","334","335","336","337","338","339","340","341","342","343","344","345","346","347","348","349","350","351","352","353","354","355","356","357","358","359","360","361","362","363","364","365","366","367","368","369","370","371","372","373","374","375","376","377","378","379","380","381","382","383","384","385","386","387","388","389","390","391","392","393","394","395","396","397","398","399","400","401","402","403","404","405","406","407","408","409","410","411","412","413","414","415","416","417","418","419","420","421","422","423","424","425","426","427","428","429","430","431","432","433","434","435","436","437","438","439","440","441","442","443","444","445","446","447","448","449","450","451","452","453","454","455","456","457","458","459","460","461","462","463","464","465","466","467","468","469","470","471","472","473","474","475","476","477","478","479","480","481","482","483","484","485","486","487","488","489","490","491","492","493","494","495","496","497","498","499","500"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration1");
		skillField.setDefaultValue("");
		skillField.setDescription("施法时间，在这段时间内，播放僵直动画，时间为毫秒，此时间为0时，不会播放僵直动画");
		skillField.setDisplayName("施法时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "500","550","600","650","700","750","800","850","900","1000","1200","1500","2000","2500","2700","3000","3500","4000","5000","8000","10000"});
		skillField.setSelectDisplayValues(new String[]{"无" , "0.5秒","0.55秒","0.6秒","0.65秒","0.7秒","0.75秒","0.8秒","0.85秒","0.9秒","1秒","1.2秒","1.5秒","2秒","2.5秒","2.7秒","3秒","3.5秒","4秒","5秒","8秒","10秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration2");
		skillField.setDefaultValue("");
		skillField.setDescription("攻击动画时间，这段时间内，播放攻击动画，时间为毫秒");
		skillField.setDisplayName("攻击动画时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{"100毫秒","200毫秒","300毫秒","400毫秒","500毫秒","600毫秒","700毫秒","800毫秒","900毫秒","1000毫秒","1100毫秒","1200毫秒","1300毫秒","1400毫秒","1500毫秒","1600毫秒","1700毫秒","1800毫秒","1900毫秒","2000毫秒","2100毫秒","2200毫秒","2300毫秒","2400毫秒","2500毫秒","2600毫秒","2700毫秒","2800毫秒","2900毫秒","3000毫秒","3100毫秒","3200毫秒","3300毫秒","3400毫秒","3500毫秒","3600毫秒","3700毫秒","3800毫秒","3900毫秒","4000毫秒","4100毫秒","4200毫秒","4300毫秒","4400毫秒","4500毫秒","4600毫秒","4700毫秒","4800毫秒","4900毫秒","5000毫秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration3");
		skillField.setDefaultValue("");
		skillField.setDescription("冷却时间，这段时间内，人物状态会回到站立的状态");
		skillField.setDisplayName("冷却时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1000","2000","3000","4000","5000","6000","7000","8000","9000","10000","11000","12000","13000","14000","15000","16000","17000","18000","19000","20000","21000","22000","23000","24000","25000","26000","27000","28000","29000","30000","31000","32000","33000","34000","35000","36000","37000","38000","39000","40000","41000","42000","43000","44000","45000","46000","47000","48000","49000","50000"});
		skillField.setSelectDisplayValues(new String[]{"1秒","2秒","3秒","4秒","5秒","6秒","7秒","8秒","9秒","10秒","11秒","12秒","13秒","14秒","15秒","16秒","17秒","18秒","19秒","20秒","21秒","22秒","23秒","24秒","25秒","26秒","27秒","28秒","29秒","30秒","31秒","32秒","33秒","34秒","35秒","36秒","37秒","38秒","39秒","40秒","41秒","42秒","43秒","44秒","45秒","46秒","47秒","48秒","49秒","50秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("style1");
		skillField.setDefaultValue("0");
		skillField.setDescription("僵直动画的风格");
		skillField.setDisplayName("僵直动画的风格");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9"});
		skillField.setSelectDisplayValues(new String[]{"风格1","风格2","风格3","风格4","风格5","风格6","风格7","风格8","风格9","风格10"});
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
		skillField.setDescription("攻击动画的风格");
		skillField.setDisplayName("攻击动画的风格");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9"});
		skillField.setSelectDisplayValues(new String[]{"风格1","风格2","风格3","风格4","风格5","风格6","风格7","风格8","风格9","风格10"});
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
		skillField.setDescription("一个或多个出招时间，跟等级无关，请填入以逗号分隔的值，例如 100,200,300");
		skillField.setDisplayName("出招时间，跟等级无关");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("enableWeaponType");
		skillField.setDefaultValue("0");
		skillField.setDescription("是否有武器限制");
		skillField.setDisplayName("是否有武器限制");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{"没有限制" , "有限制"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("weaponTypeLimit");
		skillField.setDefaultValue("0");
		skillField.setDescription("武器具体的限制是什么");
		skillField.setDisplayName("武器限制");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"空手","刀","剑","棍","匕首","弓"});
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
		skillField.setDescription("攻击类型，表示物理攻击还是法术攻击 0 表示物理攻击 1 表示法术攻击");
		skillField.setDisplayName("攻击类型");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{"物理攻击" , "法术攻击"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("followByCommonAttack");
		skillField.setDefaultValue("true");
		skillField.setDescription("该技能用完之后，是否用普通攻击 还是继续用此技能攻击，主要区分 物理系攻击和法系攻击");
		skillField.setDisplayName("继续用普通攻击");
		skillField.setType(Boolean.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"true","false"});
		skillField.setSelectDisplayValues(new String[]{"是" , "否"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffTargetType");
		skillField.setDefaultValue("0");
		skillField.setDescription("buff作用的对象：0 表示作用在目标身上 1 表示作用在施法者身上， 此属性对各个级别的技能有效");
		skillField.setDisplayName("BUFF作用对象");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{"作用于目标" , "作用于施法者"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		//后效参数
		skillField = new SkillField();
		
		skillField.setFieldName("effectType");
		skillField.setDefaultValue("0");
		skillField.setDescription("后效的类型，比如闪电，落雷等");
		skillField.setDisplayName("后效的类型");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32"});
		skillField.setSelectDisplayValues(new String[]{"邪神降身","八部神龙爆","天火焚业","金蟾血蛊","神华贯九天","五神降光明","憾宇天罡印","九天坠残阳","剑引风雷","剑狱烈天穹","噬影剑","邪灵封神箭","中行破十方","太极玄" ,"两仪流","凌波弄影","帘卷西风","秋水万里泓","雷破千峰","昊阳神华","五毒化血刀","神火流星","破尽痴迷","追魂爪","普通攻击后效","神凝厉风","冰封万里","小火球爆炸","旋风斩","顺劈斩","技能光效","附加光效","治疗"});
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
		skillField.setDescription("后效持续的时间，为毫秒");
		skillField.setDisplayName("后效持续的时间");
		skillField.setType(Byte.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{"无" , "100毫秒","200毫秒","300毫秒","400毫秒","500毫秒","600毫秒","700毫秒","800毫秒","900毫秒","1000毫秒","1100毫秒","1200毫秒","1300毫秒","1400毫秒","1500毫秒","1600毫秒","1700毫秒","1800毫秒","1900毫秒","2000毫秒","2100毫秒","2200毫秒","2300毫秒","2400毫秒","2500毫秒","2600毫秒","2700毫秒","2800毫秒","2900毫秒","3000毫秒","3100毫秒","3200毫秒","3300毫秒","3400毫秒","3500毫秒","3600毫秒","3700毫秒","3800毫秒","3900毫秒","4000毫秒","4100毫秒","4200毫秒","4300毫秒","4400毫秒","4500毫秒","4600毫秒","4700毫秒","4800毫秒","4900毫秒","5000毫秒"});
		
		template.addEffectAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectExplosionLastTime");
		skillField.setDefaultValue("0");
		skillField.setDescription("后效持续的时间过后，爆炸持续的时间");
		skillField.setDisplayName("爆炸持续的时间");
		skillField.setType(Byte.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{"无" , "100毫秒","200毫秒","300毫秒","400毫秒","500毫秒","600毫秒","700毫秒","800毫秒","900毫秒","1000毫秒","1100毫秒","1200毫秒","1300毫秒","1400毫秒","1500毫秒","1600毫秒","1700毫秒","1800毫秒","1900毫秒","2000毫秒","2100毫秒","2200毫秒","2300毫秒","2400毫秒","2500毫秒","2600毫秒","2700毫秒","2800毫秒","2900毫秒","3000毫秒","3100毫秒","3200毫秒","3300毫秒","3400毫秒","3500毫秒","3600毫秒","3700毫秒","3800毫秒","3900毫秒","4000毫秒","4100毫秒","4200毫秒","4300毫秒","4400毫秒","4500毫秒","4600毫秒","4700毫秒","4800毫秒","4900毫秒","5000毫秒"});
		
		template.addEffectAnimationField(skillField);
		
		//级别相关的属性
		skillField = new SkillField();
		
		skillField.setFieldName("buffName");
		skillField.setDefaultValue("");
		skillField.setDescription("使用的Buff的名称");
		skillField.setDisplayName("使用的Buff的名称");
		skillField.setType(Integer.TYPE);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLevel");
		skillField.setDefaultValue("");
		skillField.setDescription("级BUFF");
		skillField.setDisplayName("BUFF级别");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLastingTime");
		skillField.setDefaultValue("");
		skillField.setDescription("毫秒");
		skillField.setDisplayName("BUFF持续时间");
		skillField.setType(long[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffProbability");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("BUFF产生概率");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);

		skillField = new SkillField();
		
		skillField.setFieldName("attackDamages");
		skillField.setDefaultValue("");
		skillField.setDescription("点");
		skillField.setDisplayName("技能的伤害");
		skillField.setType(short[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("mp");
		skillField.setDefaultValue("");
		skillField.setDescription("点");
		skillField.setDisplayName("消耗的法力值");
		skillField.setType(short[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}

	private void initSkillWithoutTraceAndWithMatrix(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		//普通攻击技能 
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithMatrix");
		template.setDisplayName("矩阵落雷类型攻击");
		template.setType(SkillTemplate.SKILL_TYPE_ACTIVE);
		
		//类中只有get方法，没有set方法，为了显示方便添加此属性
		skillField = new SkillField();
		skillField.setFieldName("interval");
		skillField.setDefaultValue("50");
		skillField.setDescription("技能释放周期");
		skillField.setDisplayName("技能释放周期");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		template.addBasicSkillField(skillField);
		
		/**
		 * 技能伤害或者加血计算公式：
		 * 
		 * 	 武器伤害 *（param1 + param2 * 技能等级）
		 * + 攻击强度 *（param3 + param4 * 技能等级）
		 * + 法术强度 *（param5 + param6 * 技能等级）
		 * + param7 + param8 * 技能等级
		 */
		 skillField = new SkillField();
			
			skillField.setFieldName("param1");
			skillField.setDefaultValue("0");
			skillField.setDescription("param1");
			skillField.setDisplayName("param1");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param2");
			skillField.setDefaultValue("0");
			skillField.setDescription("param2");
			skillField.setDisplayName("param2");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param3");
			skillField.setDefaultValue("0");
			skillField.setDescription("param3");
			skillField.setDisplayName("param3");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param4");
			skillField.setDefaultValue("0");
			skillField.setDescription("param4");
			skillField.setDisplayName("param4");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param5");
			skillField.setDefaultValue("0");
			skillField.setDescription("param5");
			skillField.setDisplayName("param5");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param6");
			skillField.setDefaultValue("0");
			skillField.setDescription("param6");
			skillField.setDisplayName("param6");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param7");
			skillField.setDefaultValue("0");
			skillField.setDescription("param7");
			skillField.setDisplayName("param7");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param8");
			skillField.setDefaultValue("0");
			skillField.setDescription("param8");
			skillField.setDisplayName("param8");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("matrixType");
		skillField.setDefaultValue("");
		skillField.setDescription("后效分布的类型：0 以自身为中心的正方形排列 1 以自身为中心的菱形排列 2 以目标为中心的正方形排列 3 以目标为中心的菱形排列");
		skillField.setDisplayName("后效分布的类型");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1" , "2" , "3"});
		skillField.setSelectDisplayValues(new String[]{"自身为中心的正方形排列" , "以自身为中心的菱形排列" , "目标为中心的正方形排列" , "目标为中心的菱形排列"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("gapWidth");
		skillField.setDefaultValue("24");
		skillField.setDescription("后效排列之间间距的宽度");
		skillField.setDisplayName("后效排列之间间距的宽度");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","323","324","325","326","327","328","329","330","331","332","333","334","335","336","337","338","339","340","341","342","343","344","345","346","347","348","349","350","351","352","353","354","355","356","357","358","359","360","361","362","363","364","365","366","367","368","369","370","371","372","373","374","375","376","377","378","379","380","381","382","383","384","385","386","387","388","389","390","391","392","393","394","395","396","397","398","399","400","401","402","403","404","405","406","407","408","409","410","411","412","413","414","415","416","417","418","419","420","421","422","423","424","425","426","427","428","429","430","431","432","433","434","435","436","437","438","439","440","441","442","443","444","445","446","447","448","449","450","451","452","453","454","455","456","457","458","459","460","461","462","463","464","465","466","467","468","469","470","471","472","473","474","475","476","477","478","479","480","481","482","483","484","485","486","487","488","489","490","491","492","493","494","495","496","497","498","499","500"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","323","324","325","326","327","328","329","330","331","332","333","334","335","336","337","338","339","340","341","342","343","344","345","346","347","348","349","350","351","352","353","354","355","356","357","358","359","360","361","362","363","364","365","366","367","368","369","370","371","372","373","374","375","376","377","378","379","380","381","382","383","384","385","386","387","388","389","390","391","392","393","394","395","396","397","398","399","400","401","402","403","404","405","406","407","408","409","410","411","412","413","414","415","416","417","418","419","420","421","422","423","424","425","426","427","428","429","430","431","432","433","434","435","436","437","438","439","440","441","442","443","444","445","446","447","448","449","450","451","452","453","454","455","456","457","458","459","460","461","462","463","464","465","466","467","468","469","470","471","472","473","474","475","476","477","478","479","480","481","482","483","484","485","486","487","488","489","490","491","492","493","494","495","496","497","498","499","500"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("gapHeight");
		skillField.setDefaultValue("32");
		skillField.setDescription("后效排列之间间距的高度");
		skillField.setDisplayName("后效排列之间间距的高度");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","323","324","325","326","327","328","329","330","331","332","333","334","335","336","337","338","339","340","341","342","343","344","345","346","347","348","349","350","351","352","353","354","355","356","357","358","359","360","361","362","363","364","365","366","367","368","369","370","371","372","373","374","375","376","377","378","379","380","381","382","383","384","385","386","387","388","389","390","391","392","393","394","395","396","397","398","399","400","401","402","403","404","405","406","407","408","409","410","411","412","413","414","415","416","417","418","419","420","421","422","423","424","425","426","427","428","429","430","431","432","433","434","435","436","437","438","439","440","441","442","443","444","445","446","447","448","449","450","451","452","453","454","455","456","457","458","459","460","461","462","463","464","465","466","467","468","469","470","471","472","473","474","475","476","477","478","479","480","481","482","483","484","485","486","487","488","489","490","491","492","493","494","495","496","497","498","499","500"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","323","324","325","326","327","328","329","330","331","332","333","334","335","336","337","338","339","340","341","342","343","344","345","346","347","348","349","350","351","352","353","354","355","356","357","358","359","360","361","362","363","364","365","366","367","368","369","370","371","372","373","374","375","376","377","378","379","380","381","382","383","384","385","386","387","388","389","390","391","392","393","394","395","396","397","398","399","400","401","402","403","404","405","406","407","408","409","410","411","412","413","414","415","416","417","418","419","420","421","422","423","424","425","426","427","428","429","430","431","432","433","434","435","436","437","438","439","440","441","442","443","444","445","446","447","448","449","450","451","452","453","454","455","456","457","458","459","460","461","462","463","464","465","466","467","468","469","470","471","472","473","474","475","476","477","478","479","480","481","482","483","484","485","486","487","488","489","490","491","492","493","494","495","496","497","498","499","500"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectNum");
		skillField.setDefaultValue("32");
		skillField.setDescription("后效的个数，越多后效排列的约紧密");
		skillField.setDisplayName("后效的个数");
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
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration1");
		skillField.setDefaultValue("");
		skillField.setDescription("施法时间，在这段时间内，播放僵直动画，时间为毫秒，此时间为0时，不会播放僵直动画");
		skillField.setDisplayName("施法时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "500","550","600","650","700","750","800","850","900","1000","1200","1500","2000","2500","2700","3000","3500","4000","5000","8000","10000"});
		skillField.setSelectDisplayValues(new String[]{"无" , "0.5秒","0.55秒","0.6秒","0.65秒","0.7秒","0.75秒","0.8秒","0.85秒","0.9秒","1秒","1.2秒","1.5秒","2秒","2.5秒","2.7秒","3秒","3.5秒","4秒","5秒","8秒","10秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration2");
		skillField.setDefaultValue("");
		skillField.setDescription("攻击动画时间，这段时间内，播放攻击动画，时间为毫秒");
		skillField.setDisplayName("攻击动画时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		
		String sss[] = new String[40];
		for(int i = 0 ; i < sss.length ; i++){
			sss[i] = (i * 50) + "";
		}
		skillField.setSelectValues(sss);
		
		sss = new String[40];
		for(int i = 0 ; i < sss.length ; i++){
			sss[i] = (i * 50) + "毫秒";
			if(i == 0){
				sss[0] = "无";
			}
		}
		skillField.setSelectDisplayValues(sss);
		
		
//		skillField.setSelectValues(new String[]{"100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
//		skillField.setSelectDisplayValues(new String[]{"100毫秒","200毫秒","300毫秒","400毫秒","500毫秒","600毫秒","700毫秒","800毫秒","900毫秒","1000毫秒","1100毫秒","1200毫秒","1300毫秒","1400毫秒","1500毫秒","1600毫秒","1700毫秒","1800毫秒","1900毫秒","2000毫秒","2100毫秒","2200毫秒","2300毫秒","2400毫秒","2500毫秒","2600毫秒","2700毫秒","2800毫秒","2900毫秒","3000毫秒","3100毫秒","3200毫秒","3300毫秒","3400毫秒","3500毫秒","3600毫秒","3700毫秒","3800毫秒","3900毫秒","4000毫秒","4100毫秒","4200毫秒","4300毫秒","4400毫秒","4500毫秒","4600毫秒","4700毫秒","4800毫秒","4900毫秒","5000毫秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration3");
		skillField.setDefaultValue("");
		skillField.setDescription("冷却时间，这段时间内，人物状态会回到站立的状态");
		skillField.setDisplayName("冷却时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1000","2000","3000","4000","5000","6000","7000","8000","9000","10000","11000","12000","13000","14000","15000","16000","17000","18000","19000","20000","21000","22000","23000","24000","25000","26000","27000","28000","29000","30000","31000","32000","33000","34000","35000","36000","37000","38000","39000","40000","41000","42000","43000","44000","45000","46000","47000","48000","49000","50000"});
		skillField.setSelectDisplayValues(new String[]{"1秒","2秒","3秒","4秒","5秒","6秒","7秒","8秒","9秒","10秒","11秒","12秒","13秒","14秒","15秒","16秒","17秒","18秒","19秒","20秒","21秒","22秒","23秒","24秒","25秒","26秒","27秒","28秒","29秒","30秒","31秒","32秒","33秒","34秒","35秒","36秒","37秒","38秒","39秒","40秒","41秒","42秒","43秒","44秒","45秒","46秒","47秒","48秒","49秒","50秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("style1");
		skillField.setDefaultValue("0");
		skillField.setDescription("僵直动画的风格");
		skillField.setDisplayName("僵直动画的风格");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9"});
		skillField.setSelectDisplayValues(new String[]{"风格1","风格2","风格3","风格4","风格5","风格6","风格7","风格8","风格9","风格10"});
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
		skillField.setDescription("攻击动画的风格");
		skillField.setDisplayName("攻击动画的风格");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9"});
		skillField.setSelectDisplayValues(new String[]{"风格1","风格2","风格3","风格4","风格5","风格6","风格7","风格8","风格9","风格10"});
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
		skillField.setDescription("一个或多个出招时间，跟等级无关，请填入以逗号分隔的值，例如 100,200,300");
		skillField.setDisplayName("出招时间，跟等级无关");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("enableWeaponType");
		skillField.setDefaultValue("0");
		skillField.setDescription("是否有武器限制");
		skillField.setDisplayName("是否有武器限制");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{"没有限制" , "有限制"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("weaponTypeLimit");
		skillField.setDefaultValue("0");
		skillField.setDescription("武器具体的限制是什么");
		skillField.setDisplayName("武器限制");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"空手","刀","剑","棍","匕首","弓"});
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
		skillField.setDescription("攻击类型，表示物理攻击还是法术攻击 0 表示物理攻击 1 表示法术攻击");
		skillField.setDisplayName("攻击类型");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{"物理攻击" , "法术攻击"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("followByCommonAttack");
		skillField.setDefaultValue("true");
		skillField.setDescription("该技能用完之后，是否用普通攻击 还是继续用此技能攻击，主要区分 物理系攻击和法系攻击");
		skillField.setDisplayName("继续用普通攻击");
		skillField.setType(Boolean.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"true","false"});
		skillField.setSelectDisplayValues(new String[]{"是" , "否"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffTargetType");
		skillField.setDefaultValue("0");
		skillField.setDescription("buff作用的对象：0 表示作用在目标身上 1 表示作用在施法者身上， 此属性对各个级别的技能有效");
		skillField.setDisplayName("BUFF作用对象");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{"作用于目标" , "作用于施法者"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("range");
		skillField.setDefaultValue("0");
		skillField.setDescription("攻击的有效距离");
		skillField.setDisplayName("攻击的有效距离");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300"});
		
		template.addBasicSkillField(skillField);
		
		//后效参数
		skillField = new SkillField();
		
		skillField.setFieldName("effectType");
		skillField.setDefaultValue("0");
		skillField.setDescription("后效的类型，比如闪电，落雷等");
		skillField.setDisplayName("后效的类型");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32"});
		skillField.setSelectDisplayValues(new String[]{"邪神降身","八部神龙爆","天火焚业","金蟾血蛊","神华贯九天","五神降光明","憾宇天罡印","九天坠残阳","剑引风雷","剑狱烈天穹","噬影剑","邪灵封神箭","中行破十方","太极玄" ,"两仪流","凌波弄影","帘卷西风","秋水万里泓","雷破千峰","昊阳神华","五毒化血刀","神火流星","破尽痴迷","追魂爪","普通攻击后效","神凝厉风","冰封万里","小火球爆炸","旋风斩","顺劈斩","技能光效","附加光效","治疗"});
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
		skillField.setDescription("后效持续的时间，为毫秒");
		skillField.setDisplayName("后效持续的时间");
		skillField.setType(Byte.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		
		sss = new String[30];
		for(int i = 0 ; i < sss.length ; i++){
			sss[i] = (i * 50) + "";
		}
		skillField.setSelectValues(sss);
		
		sss = new String[30];
		for(int i = 0 ; i < sss.length ; i++){
			sss[i] = (i * 50) + "毫秒";
			if(i == 0){
				sss[0] = "无";
			}
		}
		skillField.setSelectDisplayValues(sss);
		
		template.addEffectAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectExplosionLastTime");
		skillField.setDefaultValue("0");
		skillField.setDescription("后效持续的时间过后，爆炸持续的时间");
		skillField.setDisplayName("爆炸持续的时间");
		skillField.setType(Byte.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		
		sss = new String[30];
		for(int i = 0 ; i < sss.length ; i++){
			sss[i] = (i * 50) + "";
		}
		skillField.setSelectValues(sss);
		
		sss = new String[30];
		for(int i = 0 ; i < sss.length ; i++){
			sss[i] = (i * 50) + "毫秒";
			if(i == 0){
				sss[0] = "无";
			}
		}
		skillField.setSelectDisplayValues(sss);
		
		
		template.addEffectAnimationField(skillField);
		
		//级别相关的属性
		skillField = new SkillField();
		
		skillField.setFieldName("buffName");
		skillField.setDefaultValue("");
		skillField.setDescription("使用的Buff的名称");
		skillField.setDisplayName("使用的Buff的名称");
		skillField.setType(Integer.TYPE);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLevel");
		skillField.setDefaultValue("");
		skillField.setDescription("级BUFF");
		skillField.setDisplayName("BUFF级别");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLastingTime");
		skillField.setDefaultValue("");
		skillField.setDescription("毫秒");
		skillField.setDisplayName("BUFF持续时间");
		skillField.setType(long[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffProbability");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("BUFF产生概率");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);

		skillField = new SkillField();
		
		skillField.setFieldName("attackDamages");
		skillField.setDefaultValue("");
		skillField.setDescription("点");
		skillField.setDisplayName("技能的伤害");
		skillField.setType(short[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("mp");
		skillField.setDefaultValue("");
		skillField.setDescription("点");
		skillField.setDisplayName("消耗的法力值");
		skillField.setType(short[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initSkillWithoutTraceAndWithRange(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		//普通攻击技能 
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithRange");
		template.setDisplayName("范围落雷攻击");
		template.setType(SkillTemplate.SKILL_TYPE_ACTIVE);
		
		//类中只有get方法，没有set方法，为了显示方便添加此属性
		skillField = new SkillField();
		skillField.setFieldName("interval");
		skillField.setDefaultValue("50");
		skillField.setDescription("技能释放周期");
		skillField.setDisplayName("技能释放周期");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		template.addBasicSkillField(skillField);
		
		/**
		 * 技能伤害或者加血计算公式：
		 * 
		 * 	 武器伤害 *（param1 + param2 * 技能等级）
		 * + 攻击强度 *（param3 + param4 * 技能等级）
		 * + 法术强度 *（param5 + param6 * 技能等级）
		 * + param7 + param8 * 技能等级
		 */
		 skillField = new SkillField();
			
			skillField.setFieldName("param1");
			skillField.setDefaultValue("0");
			skillField.setDescription("param1");
			skillField.setDisplayName("param1");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param2");
			skillField.setDefaultValue("0");
			skillField.setDescription("param2");
			skillField.setDisplayName("param2");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param3");
			skillField.setDefaultValue("0");
			skillField.setDescription("param3");
			skillField.setDisplayName("param3");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param4");
			skillField.setDefaultValue("0");
			skillField.setDescription("param4");
			skillField.setDisplayName("param4");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param5");
			skillField.setDefaultValue("0");
			skillField.setDescription("param5");
			skillField.setDisplayName("param5");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param6");
			skillField.setDefaultValue("0");
			skillField.setDescription("param6");
			skillField.setDisplayName("param6");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param7");
			skillField.setDefaultValue("0");
			skillField.setDescription("param7");
			skillField.setDisplayName("param7");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param8");
			skillField.setDefaultValue("0");
			skillField.setDescription("param8");
			skillField.setDisplayName("param8");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("rangeType");
		skillField.setDefaultValue("0");
		skillField.setDescription("范围的类型： 0 以施法者自身为中心的矩形，不包括自己 1 正前方的矩形，不包括自己");
		skillField.setDisplayName("范围的类型");
		skillField.setType(Byte.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1"});
		skillField.setSelectDisplayValues(new String[]{"以施法者自身为中心的矩形，不包括自己" , "正前方的矩形，不包括自己"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("rangeWidth");
		skillField.setDefaultValue("240");
		skillField.setDescription("范围的宽度");
		skillField.setDisplayName("范围的宽度");
		skillField.setType(Byte.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("rangeHeight");
		skillField.setDefaultValue("0");
		skillField.setDescription("范围的高度");
		skillField.setDisplayName("范围的高度");
		skillField.setType(Byte.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxAttackNum");
		skillField.setDefaultValue("0");
		skillField.setDescription("最大攻击的人数 0表示没有限制，如果有限制 系统会选择距离最近的攻击");
		skillField.setDisplayName("最大攻击的人数");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1"});
		skillField.setSelectDisplayValues(new String[]{"没有限制" , "系统选择距离最近的攻击"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration1");
		skillField.setDefaultValue("");
		skillField.setDescription("施法时间，在这段时间内，播放僵直动画，时间为毫秒，此时间为0时，不会播放僵直动画");
		skillField.setDisplayName("施法时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "500","550","600","650","700","750","800","850","900","1000","1200","1500","2000","2500","2700","3000","3500","4000","5000","8000","10000"});
		skillField.setSelectDisplayValues(new String[]{"无" , "0.5秒","0.55秒","0.6秒","0.65秒","0.7秒","0.75秒","0.8秒","0.85秒","0.9秒","1秒","1.2秒","1.5秒","2秒","2.5秒","2.7秒","3秒","3.5秒","4秒","5秒","8秒","10秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration2");
		skillField.setDefaultValue("");
		skillField.setDescription("攻击动画时间，这段时间内，播放攻击动画，时间为毫秒");
		skillField.setDisplayName("攻击动画时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{"100毫秒","200毫秒","300毫秒","400毫秒","500毫秒","600毫秒","700毫秒","800毫秒","900毫秒","1000毫秒","1100毫秒","1200毫秒","1300毫秒","1400毫秒","1500毫秒","1600毫秒","1700毫秒","1800毫秒","1900毫秒","2000毫秒","2100毫秒","2200毫秒","2300毫秒","2400毫秒","2500毫秒","2600毫秒","2700毫秒","2800毫秒","2900毫秒","3000毫秒","3100毫秒","3200毫秒","3300毫秒","3400毫秒","3500毫秒","3600毫秒","3700毫秒","3800毫秒","3900毫秒","4000毫秒","4100毫秒","4200毫秒","4300毫秒","4400毫秒","4500毫秒","4600毫秒","4700毫秒","4800毫秒","4900毫秒","5000毫秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration3");
		skillField.setDefaultValue("");
		skillField.setDescription("冷却时间，这段时间内，人物状态会回到站立的状态");
		skillField.setDisplayName("冷却时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1000","2000","3000","4000","5000","6000","7000","8000","9000","10000","11000","12000","13000","14000","15000","16000","17000","18000","19000","20000","21000","22000","23000","24000","25000","26000","27000","28000","29000","30000","31000","32000","33000","34000","35000","36000","37000","38000","39000","40000","41000","42000","43000","44000","45000","46000","47000","48000","49000","50000"});
		skillField.setSelectDisplayValues(new String[]{"1秒","2秒","3秒","4秒","5秒","6秒","7秒","8秒","9秒","10秒","11秒","12秒","13秒","14秒","15秒","16秒","17秒","18秒","19秒","20秒","21秒","22秒","23秒","24秒","25秒","26秒","27秒","28秒","29秒","30秒","31秒","32秒","33秒","34秒","35秒","36秒","37秒","38秒","39秒","40秒","41秒","42秒","43秒","44秒","45秒","46秒","47秒","48秒","49秒","50秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("style1");
		skillField.setDefaultValue("0");
		skillField.setDescription("僵直动画的风格");
		skillField.setDisplayName("僵直动画的风格");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9"});
		skillField.setSelectDisplayValues(new String[]{"风格1","风格2","风格3","风格4","风格5","风格6","风格7","风格8","风格9","风格10"});
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
		skillField.setDescription("攻击动画的风格");
		skillField.setDisplayName("攻击动画的风格");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9"});
		skillField.setSelectDisplayValues(new String[]{"风格1","风格2","风格3","风格4","风格5","风格6","风格7","风格8","风格9","风格10"});
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
		skillField.setDescription("一个或多个出招时间，跟等级无关，请填入以逗号分隔的值，例如 100,200,300");
		skillField.setDisplayName("出招时间，跟等级无关");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("enableWeaponType");
		skillField.setDefaultValue("0");
		skillField.setDescription("是否有武器限制");
		skillField.setDisplayName("是否有武器限制");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{"没有限制" , "有限制"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("weaponTypeLimit");
		skillField.setDefaultValue("0");
		skillField.setDescription("武器具体的限制是什么");
		skillField.setDisplayName("武器限制");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"空手","刀","剑","棍","匕首","弓"});
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
		skillField.setDescription("攻击类型，表示物理攻击还是法术攻击 0 表示物理攻击 1 表示法术攻击");
		skillField.setDisplayName("攻击类型");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{"物理攻击" , "法术攻击"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("followByCommonAttack");
		skillField.setDefaultValue("true");
		skillField.setDescription("该技能用完之后，是否用普通攻击 还是继续用此技能攻击，主要区分 物理系攻击和法系攻击");
		skillField.setDisplayName("继续用普通攻击");
		skillField.setType(Boolean.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"true","false"});
		skillField.setSelectDisplayValues(new String[]{"是" , "否"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffTargetType");
		skillField.setDefaultValue("0");
		skillField.setDescription("buff作用的对象：0 表示作用在目标身上 1 表示作用在施法者身上， 此属性对各个级别的技能有效");
		skillField.setDisplayName("BUFF作用对象");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{"作用于目标" , "作用于施法者"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		//后效参数
		skillField = new SkillField();
		
		skillField.setFieldName("effectType");
		skillField.setDefaultValue("0");
		skillField.setDescription("后效的类型，比如闪电，落雷等");
		skillField.setDisplayName("后效的类型");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32"});
		skillField.setSelectDisplayValues(new String[]{"邪神降身","八部神龙爆","天火焚业","金蟾血蛊","神华贯九天","五神降光明","憾宇天罡印","九天坠残阳","剑引风雷","剑狱烈天穹","噬影剑","邪灵封神箭","中行破十方","太极玄" ,"两仪流","凌波弄影","帘卷西风","秋水万里泓","雷破千峰","昊阳神华","五毒化血刀","神火流星","破尽痴迷","追魂爪","普通攻击后效","神凝厉风","冰封万里","小火球爆炸","旋风斩","顺劈斩","技能光效","附加光效","治疗"});
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
		skillField.setDescription("后效持续的时间，为毫秒");
		skillField.setDisplayName("后效持续的时间");
		skillField.setType(Byte.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{"无" , "100毫秒","200毫秒","300毫秒","400毫秒","500毫秒","600毫秒","700毫秒","800毫秒","900毫秒","1000毫秒","1100毫秒","1200毫秒","1300毫秒","1400毫秒","1500毫秒","1600毫秒","1700毫秒","1800毫秒","1900毫秒","2000毫秒","2100毫秒","2200毫秒","2300毫秒","2400毫秒","2500毫秒","2600毫秒","2700毫秒","2800毫秒","2900毫秒","3000毫秒","3100毫秒","3200毫秒","3300毫秒","3400毫秒","3500毫秒","3600毫秒","3700毫秒","3800毫秒","3900毫秒","4000毫秒","4100毫秒","4200毫秒","4300毫秒","4400毫秒","4500毫秒","4600毫秒","4700毫秒","4800毫秒","4900毫秒","5000毫秒"});
		
		template.addEffectAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectExplosionLastTime");
		skillField.setDefaultValue("0");
		skillField.setDescription("后效持续的时间过后，爆炸持续的时间");
		skillField.setDisplayName("爆炸持续的时间");
		skillField.setType(Byte.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{"无" , "100毫秒","200毫秒","300毫秒","400毫秒","500毫秒","600毫秒","700毫秒","800毫秒","900毫秒","1000毫秒","1100毫秒","1200毫秒","1300毫秒","1400毫秒","1500毫秒","1600毫秒","1700毫秒","1800毫秒","1900毫秒","2000毫秒","2100毫秒","2200毫秒","2300毫秒","2400毫秒","2500毫秒","2600毫秒","2700毫秒","2800毫秒","2900毫秒","3000毫秒","3100毫秒","3200毫秒","3300毫秒","3400毫秒","3500毫秒","3600毫秒","3700毫秒","3800毫秒","3900毫秒","4000毫秒","4100毫秒","4200毫秒","4300毫秒","4400毫秒","4500毫秒","4600毫秒","4700毫秒","4800毫秒","4900毫秒","5000毫秒"});
		
		template.addEffectAnimationField(skillField);
		
		//级别相关的属性
		skillField = new SkillField();
		
		skillField.setFieldName("buffName");
		skillField.setDefaultValue("");
		skillField.setDescription("使用的Buff的名称");
		skillField.setDisplayName("使用的Buff的名称");
		skillField.setType(Integer.TYPE);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLevel");
		skillField.setDefaultValue("");
		skillField.setDescription("级BUFF");
		skillField.setDisplayName("BUFF级别");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLastingTime");
		skillField.setDefaultValue("");
		skillField.setDescription("毫秒");
		skillField.setDisplayName("BUFF持续时间");
		skillField.setType(long[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffProbability");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("BUFF产生概率");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);

		skillField = new SkillField();
		
		skillField.setFieldName("attackDamages");
		skillField.setDefaultValue("");
		skillField.setDescription("点");
		skillField.setDisplayName("技能的伤害");
		skillField.setType(short[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("mp");
		skillField.setDefaultValue("");
		skillField.setDescription("点");
		skillField.setDisplayName("消耗的法力值");
		skillField.setType(short[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initSkillWithoutTraceAndWithTargetOrPosition(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		//普通攻击技能 
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithTargetOrPosition");
		template.setDisplayName("单个目标的落雷攻击");
		template.setType(SkillTemplate.SKILL_TYPE_ACTIVE);
		
		//类中只有get方法，没有set方法，为了显示方便添加此属性
		skillField = new SkillField();
		skillField.setFieldName("interval");
		skillField.setDefaultValue("50");
		skillField.setDescription("技能释放周期");
		skillField.setDisplayName("技能释放周期");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		template.addBasicSkillField(skillField);
		
		/**
		 * 技能伤害或者加血计算公式：
		 * 
		 * 	 武器伤害 *（param1 + param2 * 技能等级）
		 * + 攻击强度 *（param3 + param4 * 技能等级）
		 * + 法术强度 *（param5 + param6 * 技能等级）
		 * + param7 + param8 * 技能等级
		 */
		 skillField = new SkillField();
			
			skillField.setFieldName("param1");
			skillField.setDefaultValue("0");
			skillField.setDescription("param1");
			skillField.setDisplayName("param1");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param2");
			skillField.setDefaultValue("0");
			skillField.setDescription("param2");
			skillField.setDisplayName("param2");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param3");
			skillField.setDefaultValue("0");
			skillField.setDescription("param3");
			skillField.setDisplayName("param3");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param4");
			skillField.setDefaultValue("0");
			skillField.setDescription("param4");
			skillField.setDisplayName("param4");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param5");
			skillField.setDefaultValue("0");
			skillField.setDescription("param5");
			skillField.setDisplayName("param5");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param6");
			skillField.setDefaultValue("0");
			skillField.setDescription("param6");
			skillField.setDisplayName("param6");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param7");
			skillField.setDefaultValue("0");
			skillField.setDescription("param7");
			skillField.setDisplayName("param7");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param8");
			skillField.setDefaultValue("0");
			skillField.setDescription("param8");
			skillField.setDisplayName("param8");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("range");
		skillField.setDefaultValue("20");
		skillField.setDescription("攻击的有效距离");
		skillField.setDisplayName("攻击的有效距离");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration1");
		skillField.setDefaultValue("");
		skillField.setDescription("施法时间，在这段时间内，播放僵直动画，时间为毫秒，此时间为0时，不会播放僵直动画");
		skillField.setDisplayName("施法时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "500","550","600","650","700","750","800","850","900","1000","1200","1500","2000","2500","2700","3000","3500","4000","5000","8000","10000"});
		skillField.setSelectDisplayValues(new String[]{"无" , "0.5秒","0.55秒","0.6秒","0.65秒","0.7秒","0.75秒","0.8秒","0.85秒","0.9秒","1秒","1.2秒","1.5秒","2秒","2.5秒","2.7秒","3秒","3.5秒","4秒","5秒","8秒","10秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration2");
		skillField.setDefaultValue("");
		skillField.setDescription("攻击动画时间，这段时间内，播放攻击动画，时间为毫秒");
		skillField.setDisplayName("攻击动画时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{"100毫秒","200毫秒","300毫秒","400毫秒","500毫秒","600毫秒","700毫秒","800毫秒","900毫秒","1000毫秒","1100毫秒","1200毫秒","1300毫秒","1400毫秒","1500毫秒","1600毫秒","1700毫秒","1800毫秒","1900毫秒","2000毫秒","2100毫秒","2200毫秒","2300毫秒","2400毫秒","2500毫秒","2600毫秒","2700毫秒","2800毫秒","2900毫秒","3000毫秒","3100毫秒","3200毫秒","3300毫秒","3400毫秒","3500毫秒","3600毫秒","3700毫秒","3800毫秒","3900毫秒","4000毫秒","4100毫秒","4200毫秒","4300毫秒","4400毫秒","4500毫秒","4600毫秒","4700毫秒","4800毫秒","4900毫秒","5000毫秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration3");
		skillField.setDefaultValue("");
		skillField.setDescription("冷却时间，这段时间内，人物状态会回到站立的状态");
		skillField.setDisplayName("冷却时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1000","2000","3000","4000","5000","6000","7000","8000","9000","10000","11000","12000","13000","14000","15000","16000","17000","18000","19000","20000","21000","22000","23000","24000","25000","26000","27000","28000","29000","30000","31000","32000","33000","34000","35000","36000","37000","38000","39000","40000","41000","42000","43000","44000","45000","46000","47000","48000","49000","50000"});
		skillField.setSelectDisplayValues(new String[]{"1秒","2秒","3秒","4秒","5秒","6秒","7秒","8秒","9秒","10秒","11秒","12秒","13秒","14秒","15秒","16秒","17秒","18秒","19秒","20秒","21秒","22秒","23秒","24秒","25秒","26秒","27秒","28秒","29秒","30秒","31秒","32秒","33秒","34秒","35秒","36秒","37秒","38秒","39秒","40秒","41秒","42秒","43秒","44秒","45秒","46秒","47秒","48秒","49秒","50秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("style1");
		skillField.setDefaultValue("0");
		skillField.setDescription("僵直动画的风格");
		skillField.setDisplayName("僵直动画的风格");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9"});
		skillField.setSelectDisplayValues(new String[]{"风格1","风格2","风格3","风格4","风格5","风格6","风格7","风格8","风格9","风格10"});
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
		skillField.setDescription("攻击动画的风格");
		skillField.setDisplayName("攻击动画的风格");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9"});
		skillField.setSelectDisplayValues(new String[]{"风格1","风格2","风格3","风格4","风格5","风格6","风格7","风格8","风格9","风格10"});
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
		skillField.setDescription("一个或多个出招时间，跟等级无关，请填入以逗号分隔的值，例如 100,200,300");
		skillField.setDisplayName("出招时间，跟等级无关");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("enableWeaponType");
		skillField.setDefaultValue("0");
		skillField.setDescription("是否有武器限制");
		skillField.setDisplayName("是否有武器限制");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{"没有限制" , "有限制"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("weaponTypeLimit");
		skillField.setDefaultValue("0");
		skillField.setDescription("武器具体的限制是什么");
		skillField.setDisplayName("武器限制");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"空手","刀","剑","棍","匕首","弓"});
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
		skillField.setDescription("攻击类型，表示物理攻击还是法术攻击 0 表示物理攻击 1 表示法术攻击");
		skillField.setDisplayName("攻击类型");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{"物理攻击" , "法术攻击"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("followByCommonAttack");
		skillField.setDefaultValue("true");
		skillField.setDescription("该技能用完之后，是否用普通攻击 还是继续用此技能攻击，主要区分 物理系攻击和法系攻击");
		skillField.setDisplayName("继续用普通攻击");
		skillField.setType(Boolean.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"true","false"});
		skillField.setSelectDisplayValues(new String[]{"是" , "否"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffTargetType");
		skillField.setDefaultValue("0");
		skillField.setDescription("buff作用的对象：0 表示作用在目标身上 1 表示作用在施法者身上， 此属性对各个级别的技能有效");
		skillField.setDisplayName("BUFF作用对象");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{"作用于目标" , "作用于施法者"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		//后效参数
		skillField = new SkillField();
		
		skillField.setFieldName("effectType");
		skillField.setDefaultValue("0");
		skillField.setDescription("后效的类型，比如闪电，落雷等");
		skillField.setDisplayName("后效的类型");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32"});
		skillField.setSelectDisplayValues(new String[]{"邪神降身","八部神龙爆","天火焚业","金蟾血蛊","神华贯九天","五神降光明","憾宇天罡印","九天坠残阳","剑引风雷","剑狱烈天穹","噬影剑","邪灵封神箭","中行破十方","太极玄" ,"两仪流","凌波弄影","帘卷西风","秋水万里泓","雷破千峰","昊阳神华","五毒化血刀","神火流星","破尽痴迷","追魂爪","普通攻击后效","神凝厉风","冰封万里","小火球爆炸","旋风斩","顺劈斩","技能光效","附加光效","治疗"});
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
		skillField.setDescription("后效持续的时间，为毫秒");
		skillField.setDisplayName("后效持续的时间");
		skillField.setType(Byte.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{"无" , "100毫秒","200毫秒","300毫秒","400毫秒","500毫秒","600毫秒","700毫秒","800毫秒","900毫秒","1000毫秒","1100毫秒","1200毫秒","1300毫秒","1400毫秒","1500毫秒","1600毫秒","1700毫秒","1800毫秒","1900毫秒","2000毫秒","2100毫秒","2200毫秒","2300毫秒","2400毫秒","2500毫秒","2600毫秒","2700毫秒","2800毫秒","2900毫秒","3000毫秒","3100毫秒","3200毫秒","3300毫秒","3400毫秒","3500毫秒","3600毫秒","3700毫秒","3800毫秒","3900毫秒","4000毫秒","4100毫秒","4200毫秒","4300毫秒","4400毫秒","4500毫秒","4600毫秒","4700毫秒","4800毫秒","4900毫秒","5000毫秒"});
		
		template.addEffectAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectExplosionLastTime");
		skillField.setDefaultValue("0");
		skillField.setDescription("后效持续的时间过后，爆炸持续的时间");
		skillField.setDisplayName("爆炸持续的时间");
		skillField.setType(Byte.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{"无" , "100毫秒","200毫秒","300毫秒","400毫秒","500毫秒","600毫秒","700毫秒","800毫秒","900毫秒","1000毫秒","1100毫秒","1200毫秒","1300毫秒","1400毫秒","1500毫秒","1600毫秒","1700毫秒","1800毫秒","1900毫秒","2000毫秒","2100毫秒","2200毫秒","2300毫秒","2400毫秒","2500毫秒","2600毫秒","2700毫秒","2800毫秒","2900毫秒","3000毫秒","3100毫秒","3200毫秒","3300毫秒","3400毫秒","3500毫秒","3600毫秒","3700毫秒","3800毫秒","3900毫秒","4000毫秒","4100毫秒","4200毫秒","4300毫秒","4400毫秒","4500毫秒","4600毫秒","4700毫秒","4800毫秒","4900毫秒","5000毫秒"});
		
		template.addEffectAnimationField(skillField);
		
		//级别相关的属性
		skillField = new SkillField();
		
		skillField.setFieldName("buffName");
		skillField.setDefaultValue("");
		skillField.setDescription("使用的Buff的名称");
		skillField.setDisplayName("使用的Buff的名称");
		skillField.setType(Integer.TYPE);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLevel");
		skillField.setDefaultValue("");
		skillField.setDescription("级BUFF");
		skillField.setDisplayName("BUFF级别");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLastingTime");
		skillField.setDefaultValue("");
		skillField.setDescription("毫秒");
		skillField.setDisplayName("BUFF持续时间");
		skillField.setType(long[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffProbability");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("BUFF产生概率");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);

		skillField = new SkillField();
		
		skillField.setFieldName("attackDamages");
		skillField.setDefaultValue("");
		skillField.setDescription("点");
		skillField.setDisplayName("技能的伤害");
		skillField.setType(short[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("mp");
		skillField.setDefaultValue("");
		skillField.setDescription("点");
		skillField.setDisplayName("消耗的法力值");
		skillField.setType(short[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initSkillWithTraceAndDirectionOrTarget(  ){
		SkillTemplate template = null;
		SkillField skillField = null;
		
		//普通攻击技能 
		template = new SkillTemplate();
		template.setClassName("com.fy.engineserver.datasource.skill.activeskills.SkillWithTraceAndDirectionOrTarget");
		template.setDisplayName("多方向火球类攻击");
		template.setType(SkillTemplate.SKILL_TYPE_ACTIVE);
		
		//类中只有get方法，没有set方法，为了显示方便添加此属性
		skillField = new SkillField();
		skillField.setFieldName("interval");
		skillField.setDefaultValue("50");
		skillField.setDescription("技能释放周期");
		skillField.setDisplayName("技能释放周期");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		template.addBasicSkillField(skillField);
		
		/**
		 * 技能伤害或者加血计算公式：
		 * 
		 * 	 武器伤害 *（param1 + param2 * 技能等级）
		 * + 攻击强度 *（param3 + param4 * 技能等级）
		 * + 法术强度 *（param5 + param6 * 技能等级）
		 * + param7 + param8 * 技能等级
		 */
		 skillField = new SkillField();
			
			skillField.setFieldName("param1");
			skillField.setDefaultValue("0");
			skillField.setDescription("param1");
			skillField.setDisplayName("param1");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param2");
			skillField.setDefaultValue("0");
			skillField.setDescription("param2");
			skillField.setDisplayName("param2");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param3");
			skillField.setDefaultValue("0");
			skillField.setDescription("param3");
			skillField.setDisplayName("param3");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param4");
			skillField.setDefaultValue("0");
			skillField.setDescription("param4");
			skillField.setDisplayName("param4");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param5");
			skillField.setDefaultValue("0");
			skillField.setDescription("param5");
			skillField.setDisplayName("param5");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param6");
			skillField.setDefaultValue("0");
			skillField.setDescription("param6");
			skillField.setDisplayName("param6");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param7");
			skillField.setDefaultValue("0");
			skillField.setDescription("param7");
			skillField.setDisplayName("param7");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param8");
			skillField.setDefaultValue("0");
			skillField.setDescription("param8");
			skillField.setDisplayName("param8");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("targetType");
		skillField.setDefaultValue("0");
		skillField.setDescription("目标类型，0 表示对某个目标或者位置点进行攻击，无需配置后效方向 1 表示对周围环境进行攻击，需配置后效方向");
		skillField.setDisplayName("目标类型");
		skillField.setType(Byte.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1"});
		skillField.setSelectDisplayValues(new String[]{"对某个目标或者位置点进行攻击，无需配置后效方向" , "对周围环境进行攻击，需配置后效方向"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("trackType");
		skillField.setDefaultValue("1");
		skillField.setDescription("轨迹的类型，0表示直线，1表示追踪");
		skillField.setDisplayName("轨迹的类型");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1"});
		skillField.setSelectDisplayValues(new String[]{"直线" , "追踪"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("speed");
		skillField.setDefaultValue("200");
		skillField.setDescription("飞行物体的飞行速度");
		skillField.setDisplayName("飞行物体的飞行速度");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"300" , "100","110","120","130","140","150","160","170","180","190","200","210","220","230","240","250","260","270","280","290","300","350","400","450","500","550","600","650","700"});
		skillField.setSelectDisplayValues(new String[]{"300" , "100","110","120","130","140","150","160","170","180","190","200","210","220","230","240","250","260","270","280","290","300","350","400","450","500","550","600","650","700"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("range");
		skillField.setDefaultValue("500");
		skillField.setDescription("飞行物体的飞行距离");
		skillField.setDisplayName("飞行物体的飞行距离");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("attackWidth");
		skillField.setDefaultValue("32");
		skillField.setDescription("攻击的宽度");
		skillField.setDisplayName("攻击的宽度");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		skillField.setSelectDisplayValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159","160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179","180","181","182","183","184","185","186","187","188","189","190","191","192","193","194","195","196","197","198","199","200","201","202","203","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","226","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","253","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectNum");
		skillField.setDefaultValue("1");
		skillField.setDescription("后效的个数");
		skillField.setDisplayName("后效的个数");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"});
		skillField.setSelectDisplayValues(new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectInitPositionX");
		skillField.setDefaultValue("");
		skillField.setDescription("每个后效的初始位置，如果有10个后效，那么此参数应该填10值 如果填5个值，那么后面5个值默认为0 每个数值的含义是：沿施法者面朝的方向，向前多少个像素，如果为负值，本质就是向后。");
		skillField.setDisplayName("后效的初始位置x");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectInitPositionY");
		skillField.setDefaultValue("");
		skillField.setDescription("每个后效的初始位置，如果有10个后效，那么此参数应该填10值。如果填5个值，那么后面5个值默认为0 每个数值的含义是：沿施法者右侧的方向，向右多少个像素，如果为负值，本质就是向左。");
		skillField.setDisplayName("后效的初始位置y");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectInitDirection");
		skillField.setDefaultValue("");
		skillField.setDescription("某个后效的方向 按钟表分12个方向，取值为0~11，0标识12点钟方向，1表示1点钟方向，6表示6点钟方向..");
		skillField.setDisplayName("后效的方向");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("penetrateNum");
		skillField.setDefaultValue("0");
		skillField.setDescription("后效能穿透的次数，0表示不能穿透");
		skillField.setDisplayName("后效穿透次数");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9","10"});
		skillField.setSelectDisplayValues(new String[]{"0","1","2","3","4","5","6","7","8","9","10"});
		
		template.addBasicSkillField(skillField);

		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("5");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration1");
		skillField.setDefaultValue("");
		skillField.setDescription("施法时间，在这段时间内，播放僵直动画，时间为毫秒，此时间为0时，不会播放僵直动画");
		skillField.setDisplayName("施法时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "500","550","600","650","700","750","800","850","900","1000","1200","1500","2000","2500","2700","3000","3500","4000","5000","8000","10000"});
		skillField.setSelectDisplayValues(new String[]{"无" , "0.5秒","0.55秒","0.6秒","0.65秒","0.7秒","0.75秒","0.8秒","0.85秒","0.9秒","1秒","1.2秒","1.5秒","2秒","2.5秒","2.7秒","3秒","3.5秒","4秒","5秒","8秒","10秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration2");
		skillField.setDefaultValue("");
		skillField.setDescription("攻击动画时间，这段时间内，播放攻击动画，时间为毫秒");
		skillField.setDisplayName("攻击动画时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{"100毫秒","200毫秒","300毫秒","400毫秒","500毫秒","600毫秒","700毫秒","800毫秒","900毫秒","1000毫秒","1100毫秒","1200毫秒","1300毫秒","1400毫秒","1500毫秒","1600毫秒","1700毫秒","1800毫秒","1900毫秒","2000毫秒","2100毫秒","2200毫秒","2300毫秒","2400毫秒","2500毫秒","2600毫秒","2700毫秒","2800毫秒","2900毫秒","3000毫秒","3100毫秒","3200毫秒","3300毫秒","3400毫秒","3500毫秒","3600毫秒","3700毫秒","3800毫秒","3900毫秒","4000毫秒","4100毫秒","4200毫秒","4300毫秒","4400毫秒","4500毫秒","4600毫秒","4700毫秒","4800毫秒","4900毫秒","5000毫秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration3");
		skillField.setDefaultValue("");
		skillField.setDescription("冷却时间，这段时间内，人物状态会回到站立的状态");
		skillField.setDisplayName("冷却时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1000","2000","3000","4000","5000","6000","7000","8000","9000","10000","11000","12000","13000","14000","15000","16000","17000","18000","19000","20000","21000","22000","23000","24000","25000","26000","27000","28000","29000","30000","31000","32000","33000","34000","35000","36000","37000","38000","39000","40000","41000","42000","43000","44000","45000","46000","47000","48000","49000","50000"});
		skillField.setSelectDisplayValues(new String[]{"1秒","2秒","3秒","4秒","5秒","6秒","7秒","8秒","9秒","10秒","11秒","12秒","13秒","14秒","15秒","16秒","17秒","18秒","19秒","20秒","21秒","22秒","23秒","24秒","25秒","26秒","27秒","28秒","29秒","30秒","31秒","32秒","33秒","34秒","35秒","36秒","37秒","38秒","39秒","40秒","41秒","42秒","43秒","44秒","45秒","46秒","47秒","48秒","49秒","50秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("style1");
		skillField.setDefaultValue("0");
		skillField.setDescription("僵直动画的风格");
		skillField.setDisplayName("僵直动画的风格");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9"});
		skillField.setSelectDisplayValues(new String[]{"风格1","风格2","风格3","风格4","风格5","风格6","风格7","风格8","风格9","风格10"});
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
		skillField.setDescription("攻击动画的风格");
		skillField.setDisplayName("攻击动画的风格");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9"});
		skillField.setSelectDisplayValues(new String[]{"风格1","风格2","风格3","风格4","风格5","风格6","风格7","风格8","风格9","风格10"});
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
		skillField.setDescription("一个或多个出招时间，跟等级无关，请填入以逗号分隔的值，例如 100,200,300");
		skillField.setDisplayName("出招时间，跟等级无关");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("enableWeaponType");
		skillField.setDefaultValue("0");
		skillField.setDescription("是否有武器限制");
		skillField.setDisplayName("是否有武器限制");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{"没有限制" , "有限制"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("weaponTypeLimit");
		skillField.setDefaultValue("0");
		skillField.setDescription("武器具体的限制是什么");
		skillField.setDisplayName("武器限制");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"空手","刀","剑","棍","匕首","弓"});
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
		skillField.setDescription("攻击类型，表示物理攻击还是法术攻击 0 表示物理攻击 1 表示法术攻击");
		skillField.setDisplayName("攻击类型");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{"物理攻击" , "法术攻击"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("followByCommonAttack");
		skillField.setDefaultValue("true");
		skillField.setDescription("该技能用完之后，是否用普通攻击 还是继续用此技能攻击，主要区分 物理系攻击和法系攻击");
		skillField.setDisplayName("继续用普通攻击");
		skillField.setType(Boolean.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"true","false"});
		skillField.setSelectDisplayValues(new String[]{"是" , "否"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffTargetType");
		skillField.setDefaultValue("0");
		skillField.setDescription("buff作用的对象：0 表示作用在目标身上 1 表示作用在施法者身上， 此属性对各个级别的技能有效");
		skillField.setDisplayName("BUFF作用对象");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1"});
		skillField.setSelectDisplayValues(new String[]{"作用于目标" , "作用于施法者"});
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("needCareerThreadPoints");
		skillField.setDefaultValue("0");
		skillField.setDescription("需要的职业发展线路点数，这是学习此技能的条件");
		skillField.setDisplayName("职业发展线路点数");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"});
		skillField.setSelectDisplayValues(new String[]{"0点" , "1点","2点","3点","4点","5点","6点","7点","8点","9点","10点","11点","12点","13点" ,"14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点","25点","26点","27点","28点","29点","30点","31点","32点","33点","34点","35点","36点","37点","38点","39点","40点","41点","42点","43点","44点","45点","46点","47点","48点","49点","50点","51点","52点","53点","54点","55点","56点","57点","58点","59点","60点","61点","62点","63点","64点","65点","66点","67点","68点","69点","70点","71点","72点","73点","74点","75点","76点","77点","78点","79点","80点","81点","82点","83点","84点","85点","86点","87点","88点","89点","90点","91点","92点","93点","94点","95点","96点","97点","98点","99点","100点"});
		
		template.addBasicSkillField(skillField);
		
		//后效参数
		skillField = new SkillField();
		
		skillField.setFieldName("effectType");
		skillField.setDefaultValue("0");
		skillField.setDescription("后效的类型，比如闪电，落雷等");
		skillField.setDisplayName("后效的类型");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32"});
		skillField.setSelectDisplayValues(new String[]{"邪神降身","八部神龙爆","天火焚业","金蟾血蛊","神华贯九天","五神降光明","憾宇天罡印","九天坠残阳","剑引风雷","剑狱烈天穹","噬影剑","邪灵封神箭","中行破十方","太极玄" ,"两仪流","凌波弄影","帘卷西风","秋水万里泓","雷破千峰","昊阳神华","五毒化血刀","神火流星","破尽痴迷","追魂爪","普通攻击后效","神凝厉风","冰封万里","小火球爆炸","旋风斩","顺劈斩","技能光效","附加光效","治疗"});
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
		skillField.setDescription("后效爆炸持续的时间");
		skillField.setDisplayName("后效爆炸持续的时间");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{"0毫秒","100毫秒","200毫秒","300毫秒","400毫秒","500毫秒","600毫秒","700毫秒","800毫秒","900毫秒","1000毫秒","1100毫秒","1200毫秒","1300毫秒","1400毫秒","1500毫秒","1600毫秒","1700毫秒","1800毫秒","1900毫秒","2000毫秒","2100毫秒","2200毫秒","2300毫秒","2400毫秒","2500毫秒","2600毫秒","2700毫秒","2800毫秒","2900毫秒","3000毫秒","3100毫秒","3200毫秒","3300毫秒","3400毫秒","3500毫秒","3600毫秒","3700毫秒","3800毫秒","3900毫秒","4000毫秒","4100毫秒","4200毫秒","4300毫秒","4400毫秒","4500毫秒","4600毫秒","4700毫秒","4800毫秒","4900毫秒","5000毫秒"});
		
		template.addEffectAnimationField(skillField);
		
		//级别相关的属性
		skillField = new SkillField();
		
		skillField.setFieldName("buffName");
		skillField.setDefaultValue("");
		skillField.setDescription("使用的Buff的名称");
		skillField.setDisplayName("使用的Buff的名称");
		skillField.setType(Integer.TYPE);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLevel");
		skillField.setDefaultValue("");
		skillField.setDescription("级BUFF");
		skillField.setDisplayName("BUFF级别");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffLastingTime");
		skillField.setDefaultValue("");
		skillField.setDescription("毫秒");
		skillField.setDisplayName("BUFF持续时间");
		skillField.setType(long[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("buffProbability");
		skillField.setDefaultValue("");
		skillField.setDescription("%");
		skillField.setDisplayName("BUFF产生概率");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);

		skillField = new SkillField();
		
		skillField.setFieldName("attackDamages");
		skillField.setDefaultValue("");
		skillField.setDescription("点");
		skillField.setDisplayName("技能的伤害");
		skillField.setType(short[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("mp");
		skillField.setDefaultValue("");
		skillField.setDescription("点");
		skillField.setDisplayName("消耗的法力值");
		skillField.setType(short[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
		templates.put(template.getClassName(), template);
	}
	
	private void initCommonAttackSkill(  ){
		commonAttackSkillTemplate = new SkillTemplate();
		
		SkillTemplate template = commonAttackSkillTemplate;
		SkillField skillField = null;
		
		//普通攻击技能 
		template.setClassName("com.fy.engineserver.datasource.skill.activeskills.CommonAttackSkill");
		template.setDisplayName("普通物理攻击");
		template.setType(SkillTemplate.SKILL_TYPE_ACTIVE);
		
		//类中只有get方法，没有set方法，为了显示方便添加此属性
		skillField = new SkillField();
		skillField.setFieldName("interval");
		skillField.setDefaultValue("50");
		skillField.setDescription("技能释放周期");
		skillField.setDisplayName("技能释放周期");
		skillField.setType(Integer.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		template.addBasicSkillField(skillField);
		
		/**
		 * 技能伤害或者加血计算公式：
		 * 
		 * 	 武器伤害 *（param1 + param2 * 技能等级）
		 * + 攻击强度 *（param3 + param4 * 技能等级）
		 * + 法术强度 *（param5 + param6 * 技能等级）
		 * + param7 + param8 * 技能等级
		 */
		 skillField = new SkillField();
			
			skillField.setFieldName("param1");
			skillField.setDefaultValue("0");
			skillField.setDescription("param1");
			skillField.setDisplayName("param1");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param2");
			skillField.setDefaultValue("0");
			skillField.setDescription("param2");
			skillField.setDisplayName("param2");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param3");
			skillField.setDefaultValue("0");
			skillField.setDescription("param3");
			skillField.setDisplayName("param3");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param4");
			skillField.setDefaultValue("0");
			skillField.setDescription("param4");
			skillField.setDisplayName("param4");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param5");
			skillField.setDefaultValue("0");
			skillField.setDescription("param5");
			skillField.setDisplayName("param5");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param6");
			skillField.setDefaultValue("0");
			skillField.setDescription("param6");
			skillField.setDisplayName("param6");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param7");
			skillField.setDefaultValue("0");
			skillField.setDescription("param7");
			skillField.setDisplayName("param7");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
			
			skillField = new SkillField();
			
			skillField.setFieldName("param8");
			skillField.setDefaultValue("0");
			skillField.setDescription("param8");
			skillField.setDisplayName("param8");
			skillField.setType(Integer.class);
			skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
			
			template.addDataFields(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("icon");
		skillField.setDefaultValue("");
		skillField.setDescription("技能的图标图片路径");
		skillField.setDisplayName("图标图片路径");
		skillField.setType(String.class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT_ITEM);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("name");
		skillField.setDefaultValue("技能");
		skillField.setDisplayName("技能的名称");
		skillField.setDescription("技能的名称");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		skillField.setFieldName("maxLevel");
		skillField.setDefaultValue("1");
		skillField.setDescription("技能的最大级别");
		skillField.setDisplayName("技能的最大级别");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"1级","2级","3级","4级","5级"});
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("description");
		skillField.setDefaultValue("技能描述信息");
		skillField.setDescription("技能的描述信息");
		skillField.setDisplayName("技能的描述信息");
		skillField.setType(String.class);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration1");
		skillField.setDefaultValue("");
		skillField.setDescription("施法时间，在这段时间内，播放僵直动画，时间为毫秒，此时间为0时，不会播放僵直动画");
		skillField.setDisplayName("施法时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0"});
		skillField.setSelectDisplayValues(new String[]{"无"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration2");
		skillField.setDefaultValue("");
		skillField.setDescription("攻击动画时间，这段时间内，播放攻击动画，时间为毫秒");
		skillField.setDisplayName("攻击动画时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{"100毫秒","200毫秒","300毫秒","400毫秒","500毫秒","600毫秒","700毫秒","800毫秒","900毫秒","1000毫秒","1100毫秒","1200毫秒","1300毫秒","1400毫秒","1500毫秒","1600毫秒","1700毫秒","1800毫秒","1900毫秒","2000毫秒","2100毫秒","2200毫秒","2300毫秒","2400毫秒","2500毫秒","2600毫秒","2700毫秒","2800毫秒","2900毫秒","3000毫秒","3100毫秒","3200毫秒","3300毫秒","3400毫秒","3500毫秒","3600毫秒","3700毫秒","3800毫秒","3900毫秒","4000毫秒","4100毫秒","4200毫秒","4300毫秒","4400毫秒","4500毫秒","4600毫秒","4700毫秒","4800毫秒","4900毫秒","5000毫秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("duration3");
		skillField.setDefaultValue("");
		skillField.setDescription("冷却时间，这段时间内，人物状态会回到站立的状态");
		skillField.setDisplayName("冷却时间");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"1000","2000","3000","4000","5000","6000","7000","8000","9000","10000","11000","12000","13000","14000","15000","16000","17000","18000","19000","20000","21000","22000","23000","24000","25000","26000","27000","28000","29000","30000","31000","32000","33000","34000","35000","36000","37000","38000","39000","40000","41000","42000","43000","44000","45000","46000","47000","48000","49000","50000"});
		skillField.setSelectDisplayValues(new String[]{"1秒","2秒","3秒","4秒","5秒","6秒","7秒","8秒","9秒","10秒","11秒","12秒","13秒","14秒","15秒","16秒","17秒","18秒","19秒","20秒","21秒","22秒","23秒","24秒","25秒","26秒","27秒","28秒","29秒","30秒","31秒","32秒","33秒","34秒","35秒","36秒","37秒","38秒","39秒","40秒","41秒","42秒","43秒","44秒","45秒","46秒","47秒","48秒","49秒","50秒"});
		
		
		template.addActiveSkillAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("style1");
		skillField.setDefaultValue("0");
		skillField.setDescription("僵直动画的风格");
		skillField.setDisplayName("僵直动画的风格");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9"});
		skillField.setSelectDisplayValues(new String[]{"风格1","风格2","风格3","风格4","风格5","风格6","风格7","风格8","风格9","风格10"});
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
		skillField.setDescription("攻击动画的风格");
		skillField.setDisplayName("攻击动画的风格");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9"});
		skillField.setSelectDisplayValues(new String[]{"风格1","风格2","风格3","风格4","风格5","风格6","风格7","风格8","风格9","风格10"});
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
		skillField.setDescription("一个或多个出招时间，跟等级无关，请填入以逗号分隔的值，例如 100,200,300");
		skillField.setDisplayName("出招时间，跟等级无关");
		skillField.setType(int[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_TEXT);
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("range");
		skillField.setDefaultValue("30");
		skillField.setDescription("攻击的有效距离，单位为像素");
		skillField.setDisplayName("攻击有效距离");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"30","50","70","90","110","130","150","180","200"});
		skillField.setSelectDisplayValues(new String[]{"30像素","50像素","70像素","90像素","110像素","130像素","150像素","180像素","200像素"});
		
		
		template.addBasicSkillField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("weaponTypeLimit");
		skillField.setDefaultValue("0");
		skillField.setDescription("武器具体的限制是什么，每一种普通攻击必须设置一个武器类型");
		skillField.setDisplayName("适用的武器类型");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5"});
		skillField.setSelectDisplayValues(new String[]{"无","刀","剑","棍","匕首","弓"});
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
		skillField.setDescription("后效的类型，比如闪电，落雷等");
		skillField.setDisplayName("后效的类型");
		skillField.setType(Integer.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13" ,"14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32"});
		skillField.setSelectDisplayValues(new String[]{"邪神降身","八部神龙爆","天火焚业","金蟾血蛊","神华贯九天","五神降光明","憾宇天罡印","九天坠残阳","剑引风雷","剑狱烈天穹","噬影剑","邪灵封神箭","中行破十方","太极玄" ,"两仪流","凌波弄影","帘卷西风","秋水万里泓","雷破千峰","昊阳神华","五毒化血刀","神火流星","破尽痴迷","追魂爪","普通攻击后效","神凝厉风","冰封万里","小火球爆炸","旋风斩","顺劈斩","技能光效","附加光效","治疗"});
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
		skillField.setDescription("后效持续的时间，为毫秒");
		skillField.setDisplayName("后效持续的时间");
		skillField.setType(Byte.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{"无" , "100毫秒","200毫秒","300毫秒","400毫秒","500毫秒","600毫秒","700毫秒","800毫秒","900毫秒","1000毫秒","1100毫秒","1200毫秒","1300毫秒","1400毫秒","1500毫秒","1600毫秒","1700毫秒","1800毫秒","1900毫秒","2000毫秒","2100毫秒","2200毫秒","2300毫秒","2400毫秒","2500毫秒","2600毫秒","2700毫秒","2800毫秒","2900毫秒","3000毫秒","3100毫秒","3200毫秒","3300毫秒","3400毫秒","3500毫秒","3600毫秒","3700毫秒","3800毫秒","3900毫秒","4000毫秒","4100毫秒","4200毫秒","4300毫秒","4400毫秒","4500毫秒","4600毫秒","4700毫秒","4800毫秒","4900毫秒","5000毫秒"});
		
		template.addEffectAnimationField(skillField);
		
		skillField = new SkillField();
		
		skillField.setFieldName("effectExplosionLastTime");
		skillField.setDefaultValue("0");
		skillField.setDescription("后效持续的时间过后，爆炸持续的时间");
		skillField.setDisplayName("爆炸持续的时间");
		skillField.setType(Byte.TYPE);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_SELECT);
		skillField.setSelectValues(new String[]{"0" , "100","200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200","3300","3400","3500","3600","3700","3800","3900","4000","4100","4200","4300","4400","4500","4600","4700","4800","4900","5000"});
		skillField.setSelectDisplayValues(new String[]{"无" , "100毫秒","200毫秒","300毫秒","400毫秒","500毫秒","600毫秒","700毫秒","800毫秒","900毫秒","1000毫秒","1100毫秒","1200毫秒","1300毫秒","1400毫秒","1500毫秒","1600毫秒","1700毫秒","1800毫秒","1900毫秒","2000毫秒","2100毫秒","2200毫秒","2300毫秒","2400毫秒","2500毫秒","2600毫秒","2700毫秒","2800毫秒","2900毫秒","3000毫秒","3100毫秒","3200毫秒","3300毫秒","3400毫秒","3500毫秒","3600毫秒","3700毫秒","3800毫秒","3900毫秒","4000毫秒","4100毫秒","4200毫秒","4300毫秒","4400毫秒","4500毫秒","4600毫秒","4700毫秒","4800毫秒","4900毫秒","5000毫秒"});
		
		template.addEffectAnimationField(skillField);
		
		
		
		
		
		skillField = new SkillField();
		
		skillField.setFieldName("attackDamages");
		skillField.setDefaultValue("");
		skillField.setDescription("点");
		skillField.setDisplayName("技能的伤害");
		skillField.setType(short[].class);
		skillField.setInputType(SkillField.INPUTTYPE_INPUT_LEVEL);template.addLevelSkillField(skillField);
		
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
		if("com.fy.engineserver.datasource.skill.activeskills.CommonAttackSkill".equals(name)){
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
		nbf2[0] = "无";
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

	
	
	
	String aaa(String s,int len){
		StringBuffer sb = new StringBuffer();
		char chars[] = s.toCharArray();
		int c = 0;
		for(int i = 0 ; i < chars.length ; i++){
			sb.append(chars[i]);
			c++;
			if( c >= len && (chars[i] == ',' || chars[i] == '{' || chars[i] == '}' || chars[i] == ':')){
				sb.append("<br/>");
				c = 0;
			}
		}
		return sb.toString();
	}
	
%><%
	CareerManager cm = CareerManager.getInstance();
	Skill[] skills = cm.getSkills();
	Gson gson = new Gson();
	Skill skill = null;
	String skillName = "";
	String data = request.getParameter("id");
	if (data != null) {
		if (skills == null) {
			out.println("无法获得技能对象<br/>");
		} else {
			for(Skill s : skills){
				String id = "" + s.getId();
				if(id.equals(data)){
					skill = s;
					skillName = skill.getName();
				}
			}
		}

	}
	
	
%>
<%@include file="IPManager.jsp" %><html>
<head>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<style type="text/css">
<!--
.tablestyle1{
width:96%;
border:0px solid #69c;
border-top:1px solid #69c;
border-right:1px solid #69c;
border-bottom:1px solid #69c;
border-left:1px solid #69c;
border-collapse: collapse;
}
.tablestyle2{
width:100%;
border:0px solid #69c;
border-top:0px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:0px solid #69c;
border-collapse: collapse;
}
td{
border:1px solid #69c;
}
.tdtable{
padding: 0px;
margin:0px;
border-top:0px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:0px solid #69c;
}
.titlecolor{
background-color:#C2CAF5;
text-align:center;
}
.tdwidth{
width:30%;
}
-->
</style>
</HEAD>
<BODY>
<h2>技能《<font color="red"><%= skillName %></font>》的详细数据</h2>
<input width="68" type="button" value="返回" onclick="javascript:history.back()">
<br>
<br>
	<%
		if(skill != null){
			Method ms[] = skill.getClass().getMethods();
	  		ArrayList<Method> al = new ArrayList<Method>();
	  		for(int j = 0 ; j < ms.length ; j++){
	  			if(ms[j].getName().length() > 3 && (ms[j].getName().startsWith("get") || ms[j].getName().startsWith("is")) 
	  					&& (ms[j].getModifiers() & Modifier.PUBLIC) != 0
	  					&& ms[j].getParameterTypes().length == 0){
	  				al.add(ms[j]);
	  			}
	  		}
	  		
	  		//SkillManager sm = new SkillManager();
	  		init();
			
			String className = skill.getClass().getName();
			SkillTemplate skillTemplate = getSkillTemplate(className);
			String basicdes = "";
			if(skillTemplate != null){
				if("com.fy.engineserver.datasource.skill.activeskills.CommonAttackSkill".equals(className)){
					basicdes = "普通攻击";
				}else if(skillTemplate.getType() == SkillTemplate.SKILL_TYPE_ACTIVE){
					basicdes = "主动攻击";
				}else if(skillTemplate.getType() == SkillTemplate.SKILL_TYPE_PASSIVE){
					basicdes = "被动攻击";
				}else if(skillTemplate.getType() == SkillTemplate.SKILL_TYPE_AURA){
					basicdes = "光环";
				}
				
				SkillField[] sfs = skillTemplate.getBasicSkillFields();
	  			/**
	  			 * 技能伤害或者加血计算公式：
	  			 * 
	  			 * 	 武器伤害 *（param1 + param2 * 技能等级）
	  			 * + 攻击强度 *（param3 + param4 * 技能等级）
	  			 * + 法术强度 *（param5 + param6 * 技能等级）
	  			 * + param7 + param8 * 技能等级
	  			 */
	  			int param1 = 0;
	  			int param2 = 0;
	  			int param3 = 0;
	  			int param4 = 0;
	  			int param5 = 0;
	  			int param6 = 0;
	  			int param7 = 0;
	  			int param8 = 0;
				if(sfs != null && sfs.length > 0){
					%>
					<table class="tablestyle1">
					<tr class="titlecolor"><td colspan="2"><%="<font color='red'>"+basicdes+"</font>" %>基本参数(<%=skillTemplate.getDisplayName() %>)</td></tr>
					<%
					for(SkillField sf : sfs){
						if(sf != null){
							%>
							<tr>
							<td class="tdwidth"><%=sf.getDisplayName() %></td>
							<td>
							<%
							int alSize = al.size();
			  				for(int j = 0; j < alSize; j++){
					  				Method method = al.get(j);
					  		  		String name ="";
					  		  		if(method.getName().indexOf("is") == 0){
					  		  			name = method.getName().substring(2);
					  		  		}else{
					  		  			name = method.getName().substring(3);
					  		  		}
					  		  		name = Character.toLowerCase(name.charAt(0)) + name.substring(1);

				  					if(name.equals(sf.getFieldName())){
				  						Object obj = null;
				  						if(method.getReturnType().getName().indexOf("[") == 0){
			  								obj = gson.toJson(method.invoke(skill));
			  							}else{
			  								obj = method.invoke(skill);
			  							}
				  						//处理数据
				  						if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_TEXT){
				  							if("buffName".equals(name) && !"".equals(obj.toString().trim())){
				  								out.println("<a href='buffbyname.jsp?buffName="+obj.toString()+"'>"+obj.toString()+"</a>");
					  						}else if("param1".equals(name) && !"".equals(obj.toString().trim())){
					  							param1 = Integer.parseInt(obj.toString().trim());
					  						}else if("param2".equals(name) && !"".equals(obj.toString().trim())){
					  							param2 = Integer.parseInt(obj.toString().trim());
					  						}else if("param3".equals(name) && !"".equals(obj.toString().trim())){
					  							param3 = Integer.parseInt(obj.toString().trim());
					  						}else if("param4".equals(name) && !"".equals(obj.toString().trim())){
					  							param4 = Integer.parseInt(obj.toString().trim());
					  						}else if("param5".equals(name) && !"".equals(obj.toString().trim())){
					  							param5 = Integer.parseInt(obj.toString().trim());
					  						}else if("param6".equals(name) && !"".equals(obj.toString().trim())){
					  							param6 = Integer.parseInt(obj.toString().trim());
					  						}else if("param7".equals(name) && !"".equals(obj.toString().trim())){
					  							param7 = Integer.parseInt(obj.toString().trim());
					  						}else if("param8".equals(name) && !"".equals(obj.toString().trim())){
					  							param8 = Integer.parseInt(obj.toString().trim());
					  						}else{
					  							out.println(obj.toString());
					  						}
				  						}else if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_SELECT){
				  						
				  							try{
				  								int indexValue = -1;
				  							for(int i = 0; sf.getSelectValues() != null && i < sf.getSelectValues().length; i++){
				  								if(obj.toString().trim().equals(sf.getSelectValues()[i])){
				  									indexValue = i;
				  									break;
				  								}
				  							}
				  							if(indexValue != -1){
				  								out.println(sf.getSelectDisplayValues()[indexValue]);
				  							}else{
				  								out.println("<font color='red'>值出现问题，请联系技术查看数据</font>("+obj.toString()+")");
				  							}
				  							
				  							}catch(Exception e){
				  								out.println(obj.toString());
				  								out.println(gson.toJson(sf.getSelectDisplayValues()));
				  							}
				  						}else if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_LEVEL){

				  						}else if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_MAP){

				  						}else{
				  							out.println(obj.toString());
				  						}
				  						
				  						break;
				  					}

			  				}
								
								
								%>
							
							</td>
							</tr>
							
							<%
						}
					}
					%>
					</table>
					<%
				}
				
				
				sfs = skillTemplate.getActiveSkillAnimationFields();

				if(sfs != null && sfs.length > 0){
					%>
					<table class="tablestyle1">
					<tr class="titlecolor"><td colspan="2"><font color="red">主动攻击动画</font></td></tr>
					<%
					for(SkillField sf : sfs){
						if(sf != null){
							%>
							<tr>
							<td class="tdwidth"><%=sf.getDisplayName() %></td>
							<td>
							<%
							int alSize = al.size();
			  				for(int j = 0; j < alSize; j++){
					  				Method method = al.get(j);
					  		  		String name ="";
					  		  		if(method.getName().indexOf("is") == 0){
					  		  			name = method.getName().substring(2);
					  		  		}else{
					  		  			name = method.getName().substring(3);
					  		  		}
					  		  		name = Character.toLowerCase(name.charAt(0)) + name.substring(1);

				  					if(name.equals(sf.getFieldName())){
				  						Object obj = null;
				  						if(method.getReturnType().getName().indexOf("[") == 0){
			  								obj = gson.toJson(method.invoke(skill));
			  							}else{
			  								obj = method.invoke(skill);
			  							}
				  						//处理数据
				  						if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_TEXT){
				  							if("buffName".equals(name) && !"".equals(obj.toString().trim())){
				  								out.println("<a href='buffbyname.jsp?buffName="+obj.toString()+"'>"+obj.toString()+"</a>");
					  						}else if("param1".equals(name) && !"".equals(obj.toString().trim())){
					  							param1 = Integer.parseInt(obj.toString().trim());
					  						}else if("param2".equals(name) && !"".equals(obj.toString().trim())){
					  							param2 = Integer.parseInt(obj.toString().trim());
					  						}else if("param3".equals(name) && !"".equals(obj.toString().trim())){
					  							param3 = Integer.parseInt(obj.toString().trim());
					  						}else if("param4".equals(name) && !"".equals(obj.toString().trim())){
					  							param4 = Integer.parseInt(obj.toString().trim());
					  						}else if("param5".equals(name) && !"".equals(obj.toString().trim())){
					  							param5 = Integer.parseInt(obj.toString().trim());
					  						}else if("param6".equals(name) && !"".equals(obj.toString().trim())){
					  							param6 = Integer.parseInt(obj.toString().trim());
					  						}else if("param7".equals(name) && !"".equals(obj.toString().trim())){
					  							param7 = Integer.parseInt(obj.toString().trim());
					  						}else if("param8".equals(name) && !"".equals(obj.toString().trim())){
					  							param8 = Integer.parseInt(obj.toString().trim());
					  						}else{
					  							out.println(obj.toString());
					  						}
				  						}else if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_SELECT){
				  						
				  							try{
				  								int indexValue = -1;
				  							for(int i = 0; sf.getSelectValues() != null && i < sf.getSelectValues().length; i++){
				  								if(obj.toString().trim().equals(sf.getSelectValues()[i])){
				  									indexValue = i;
				  									break;
				  								}
				  							}
				  							if(indexValue != -1){
				  								out.println(sf.getSelectDisplayValues()[indexValue]);
				  							}else{
				  								out.println("<font color='red'>值出现问题，请联系技术查看数据</font>("+obj.toString()+")");
				  							}
				  							
				  							}catch(Exception e){
				  								out.println(obj.toString());
				  								out.println(gson.toJson(sf.getSelectDisplayValues()));
				  							}
				  						}else if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_LEVEL){
				  							
				  						}else if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_MAP){
				  							
				  						}else{
				  							out.println(obj.toString());
				  						}
				  						
				  						break;
				  					}

			  				}
								
								
								%>
							
							</td>
							</tr>
							
							<%
						}
					}
					%>
					</table>
					<%
				}
				
				
				sfs = skillTemplate.getEffectAnimationFields();
				if(sfs != null && sfs.length > 0){
					%>
					<table class="tablestyle1">
					<tr class="titlecolor"><td colspan="2"><font color="red">后效动画</font></td></tr>
					<%
					for(SkillField sf : sfs){
						if(sf != null){
							%>
							<tr>
							<td class="tdwidth"><%=sf.getDisplayName() %></td>
							<td>
							<%
							int alSize = al.size();
			  				for(int j = 0; j < alSize; j++){
					  				Method method = al.get(j);
					  		  		String name ="";
					  		  		if(method.getName().indexOf("is") == 0){
					  		  			name = method.getName().substring(2);
					  		  		}else{
					  		  			name = method.getName().substring(3);
					  		  		}
					  		  		name = Character.toLowerCase(name.charAt(0)) + name.substring(1);

				  					if(name.equals(sf.getFieldName())){
				  						Object obj = null;
				  						if(method.getReturnType().getName().indexOf("[") == 0){
			  								obj = gson.toJson(method.invoke(skill));
			  							}else{
			  								obj = method.invoke(skill);
			  							}
				  						//处理数据
				  						if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_TEXT){
				  							if("buffName".equals(name) && !"".equals(obj.toString().trim())){
				  								out.println("<a href='buffbyname.jsp?buffName="+obj.toString()+"'>"+obj.toString()+"</a>");
					  						}else if("param1".equals(name) && !"".equals(obj.toString().trim())){
					  							param1 = Integer.parseInt(obj.toString().trim());
					  						}else if("param2".equals(name) && !"".equals(obj.toString().trim())){
					  							param2 = Integer.parseInt(obj.toString().trim());
					  						}else if("param3".equals(name) && !"".equals(obj.toString().trim())){
					  							param3 = Integer.parseInt(obj.toString().trim());
					  						}else if("param4".equals(name) && !"".equals(obj.toString().trim())){
					  							param4 = Integer.parseInt(obj.toString().trim());
					  						}else if("param5".equals(name) && !"".equals(obj.toString().trim())){
					  							param5 = Integer.parseInt(obj.toString().trim());
					  						}else if("param6".equals(name) && !"".equals(obj.toString().trim())){
					  							param6 = Integer.parseInt(obj.toString().trim());
					  						}else if("param7".equals(name) && !"".equals(obj.toString().trim())){
					  							param7 = Integer.parseInt(obj.toString().trim());
					  						}else if("param8".equals(name) && !"".equals(obj.toString().trim())){
					  							param8 = Integer.parseInt(obj.toString().trim());
					  						}else{
					  							out.println(obj.toString());
					  						}
				  						}else if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_SELECT){
				  						
				  							try{
				  								int indexValue = -1;
				  							for(int i = 0; sf.getSelectValues() != null && i < sf.getSelectValues().length; i++){
				  								if(obj.toString().trim().equals(sf.getSelectValues()[i])){
				  									indexValue = i;
				  									break;
				  								}
				  							}
				  							if(indexValue != -1){
				  								out.println(sf.getSelectDisplayValues()[indexValue]);
				  							}else{
				  								out.println("<font color='red'>值出现问题，请联系技术查看数据</font>("+obj.toString()+")");
				  							}
				  							
				  							}catch(Exception e){
				  								out.println(obj.toString());
				  								out.println(gson.toJson(sf.getSelectDisplayValues()));
				  							}
				  						}else if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_LEVEL){
				  							
				  						}else if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_MAP){
				  							
				  						}else{
				  							out.println(obj.toString());
				  						}
				  						
				  						break;
				  					}

			  				}
								
								
								%>
							
							</td>
							</tr>
							
							<%
						}
					}
					%>
					</table>
					<%
				}
				
				sfs = skillTemplate.getLevelFields();
				if(sfs != null && sfs.length > 0){
					%>
					<table class="tablestyle1">
					<tr class="titlecolor"><td colspan="2"><font color="red">等级数据</font></td></tr>
					<%
					for(SkillField sf : sfs){
						if(sf != null){

							int alSize = al.size();
			  				for(int j = 0; j < alSize; j++){
					  				Method method = al.get(j);
					  		  		String name ="";
					  		  		if(method.getName().indexOf("is") == 0){
					  		  			name = method.getName().substring(2);
					  		  		}else{
					  		  			name = method.getName().substring(3);
					  		  		}
					  		  		name = Character.toLowerCase(name.charAt(0)) + name.substring(1);

				  					if(name.equals(sf.getFieldName())){
				  						Object obj = null;
				  						if(method.getReturnType().getName().indexOf("[") == 0){
			  								obj = gson.toJson(method.invoke(skill));
			  							}else{
			  								obj = method.invoke(skill);
			  							}
				  						//处理数据
				  						if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_TEXT){
				  							if("buffName".equals(name) && !"".equals(obj.toString().trim())){
				  								out.println("<a href='buffbyname.jsp?buffName="+obj.toString()+"'>"+obj.toString()+"</a>");
					  						}else if("param1".equals(name) && !"".equals(obj.toString().trim())){
					  							param1 = Integer.parseInt(obj.toString().trim());
					  						}else if("param2".equals(name) && !"".equals(obj.toString().trim())){
					  							param2 = Integer.parseInt(obj.toString().trim());
					  						}else if("param3".equals(name) && !"".equals(obj.toString().trim())){
					  							param3 = Integer.parseInt(obj.toString().trim());
					  						}else if("param4".equals(name) && !"".equals(obj.toString().trim())){
					  							param4 = Integer.parseInt(obj.toString().trim());
					  						}else if("param5".equals(name) && !"".equals(obj.toString().trim())){
					  							param5 = Integer.parseInt(obj.toString().trim());
					  						}else if("param6".equals(name) && !"".equals(obj.toString().trim())){
					  							param6 = Integer.parseInt(obj.toString().trim());
					  						}else if("param7".equals(name) && !"".equals(obj.toString().trim())){
					  							param7 = Integer.parseInt(obj.toString().trim());
					  						}else if("param8".equals(name) && !"".equals(obj.toString().trim())){
					  							param8 = Integer.parseInt(obj.toString().trim());
					  						}else{
					  							out.println(obj.toString());
					  						}
				  						}else if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_SELECT){
				  						
				  							try{
				  								int indexValue = -1;
				  							for(int i = 0; sf.getSelectValues() != null && i < sf.getSelectValues().length; i++){
				  								if(obj.toString().trim().equals(sf.getSelectValues()[i])){
				  									indexValue = i;
				  									break;
				  								}
				  							}
				  							%>
											<tr>
											<td class="tdwidth"><%=sf.getDisplayName() %></td>
											<td>
											<%
				  							if(indexValue != -1){
				  								out.println(sf.getSelectDisplayValues()[indexValue]);
				  							}else{
				  								out.println("<font color='red'>值出现问题，请联系技术查看数据</font>("+obj.toString()+")");
				  							}
											%>
											</td>
											</tr>
											<%
				  							
				  							}catch(Exception e){
				  								out.println(obj.toString());
				  								out.println(gson.toJson(sf.getSelectDisplayValues()));
				  							}
				  						}else if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_LEVEL){
				  							if(cm.isSkillForSprite(skill) || !(skill instanceof ActiveSkill)  || !sf.getFieldName().equals("attackDamages")){
					  							String s = obj.toString();
					  		  					String strs[] = null;
					  		  					if(s.indexOf(",") >= 0){
					  		  						 strs = s.split(",");
					  		  					}
					  							if(strs != null){
					  								String[] strArray = new String[strs.length];
					  			  					for(int i = 0; i < strs.length; i++){
					  			  						String strTemp = strs[i];
					  			  						String str = "";
					  			  						if(strTemp.indexOf('"') >= 0){
					  			  							str = strTemp.substring(strTemp.indexOf('"')+1,strTemp.lastIndexOf('"'));	
					  			  						}else if(strTemp.indexOf("[") >= 0){
					  			  							str = strTemp.substring(strTemp.indexOf('[')+1,strTemp.length());
					  			  						}else if(strTemp.indexOf("]") >= 0){
					  			  							str = strTemp.substring(0,strTemp.indexOf("]"));
					  			  						}else{
					  			  							str = strTemp;
					  			  						}
					  			  					%>
													<tr>
													<td class="tdwidth"><%="技能"+(i+1)+"级:"+sf.getDisplayName() %></td>
													<td>
													<%
					  			  					out.println(str+sf.getDescription());
					  			  						//resultMap.put(sf.getDisplayName()+"["+(i+1)+"级]",str);
					  			  						//out.println("&nbsp;<a href='buffbyname.jsp?buffName="+str+"'>"+str+"["+(i+1)+"级]</a>&nbsp;");
													%>
													</td>
													</tr>
													<%
					  			  					}
					  		  					}else{
					  		  					%>
												<tr>
												<td class="tdwidth"><%=sf.getDisplayName() %></td>
												<td>
												<%
												if(!(s.indexOf("[]") >= 0)){
					  		  						out.println(s);
												}
												%>
												</td>
												</tr>
												<%
					  		  					}
				  							}
				  							
				  						}else if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_MAP){
				  							
				  						}else{
				  							%>
											<tr>
											<td class="tdwidth"><%=sf.getDisplayName() %></td>
											<td>
											<%
				  							out.println(obj.toString());
											%>
											</td>
											</tr>
											<%
				  						}
				  						
				  						break;
				  						
				  					}

			  				}
						}
					}
					%>
					</table>
					<%
				}
				
				
				sfs = skillTemplate.getMapFields();
				if(sfs != null && sfs.length > 0){
					%>
					<table class="tablestyle1">
					<tr class="titlecolor"><td colspan="2"><font color="red">被动技能修改的主动技能</font></td></tr>
					<%
					for(SkillField sf : sfs){
						if(sf != null){
							%>
							<tr>
							<td class="tdwidth"><%=sf.getDisplayName() %></td>
							<td>
							<%
							int alSize = al.size();
			  				for(int j = 0; j < alSize; j++){
					  				Method method = al.get(j);
					  		  		String name ="";
					  		  		if(method.getName().indexOf("is") == 0){
					  		  			name = method.getName().substring(2);
					  		  		}else{
					  		  			name = method.getName().substring(3);
					  		  		}
					  		  		name = Character.toLowerCase(name.charAt(0)) + name.substring(1);

				  					if(name.equals(sf.getFieldName())){
				  						Object obj = null;
				  						if(method.getReturnType().getName().indexOf("[") == 0){
			  								obj = gson.toJson(method.invoke(skill));
			  							}else{
			  								obj = method.invoke(skill);
			  							}
				  						//处理数据
				  						if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_TEXT){
				  							if("buffName".equals(name) && !"".equals(obj.toString().trim())){
				  								out.println("<a href='buffbyname.jsp?buffName="+obj.toString()+"'>"+obj.toString()+"</a>");
					  						}else if("param1".equals(name) && !"".equals(obj.toString().trim())){
					  							param1 = Integer.parseInt(obj.toString().trim());
					  						}else if("param2".equals(name) && !"".equals(obj.toString().trim())){
					  							param2 = Integer.parseInt(obj.toString().trim());
					  						}else if("param3".equals(name) && !"".equals(obj.toString().trim())){
					  							param3 = Integer.parseInt(obj.toString().trim());
					  						}else if("param4".equals(name) && !"".equals(obj.toString().trim())){
					  							param4 = Integer.parseInt(obj.toString().trim());
					  						}else if("param5".equals(name) && !"".equals(obj.toString().trim())){
					  							param5 = Integer.parseInt(obj.toString().trim());
					  						}else if("param6".equals(name) && !"".equals(obj.toString().trim())){
					  							param6 = Integer.parseInt(obj.toString().trim());
					  						}else if("param7".equals(name) && !"".equals(obj.toString().trim())){
					  							param7 = Integer.parseInt(obj.toString().trim());
					  						}else if("param8".equals(name) && !"".equals(obj.toString().trim())){
					  							param8 = Integer.parseInt(obj.toString().trim());
					  						}else{
					  							out.println(obj.toString());
					  						}
				  						}else if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_SELECT){
				  						
				  							try{
				  								int indexValue = -1;
				  							for(int i = 0; sf.getSelectValues() != null && i < sf.getSelectValues().length; i++){
				  								if(obj.toString().trim().equals(sf.getSelectValues()[i])){
				  									indexValue = i;
				  									break;
				  								}
				  							}
				  							if(indexValue != -1){
				  								out.println(sf.getSelectDisplayValues()[indexValue]);
				  							}else{
				  								out.println("<font color='red'>值出现问题，请联系技术查看数据</font>("+obj.toString()+")");
				  							}
				  							
				  							}catch(Exception e){
				  								out.println(obj.toString());
				  								out.println(gson.toJson(sf.getSelectDisplayValues()));
				  							}
				  						}else if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_LEVEL){
				  							
				  						}else if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_MAP){
				  							if(obj instanceof Map){
				  								Map maps = (Map)obj;
				  								StringBuffer sb = new StringBuffer();
				  								if(maps.keySet() != null){
				  									for(Object o : maps.keySet()){
				  										Skill sk = cm.getSkillById(Integer.parseInt(o.toString()));
				  										if(sk != null){
				  											ActiveSkillParam[] asps = (ActiveSkillParam[])maps.get(o);
		  													sb.append("<a href='skillbyid.jsp?id="+sk.getId()+"&className="+sk.getClass().getName()+"'>"+sk.getName()+"</a>");
		  													sb.append(":<br>");
				  											if(asps != null){
				  												for(int i = 0; i < asps.length; i++){
				  													sb.append("第"+(i+1)+"级[");
				  													ActiveSkillParam asp = asps[i];
				  													if(asp != null){
				  														sb.append("暴击率:"+asp.getBaojiPercent()+"%,");
				  														sb.append("攻击强度:"+asp.getAttackPercent()+"%,");
				  														sb.append("耗蓝:"+asp.getMp()+",");
				  														sb.append("产生buff的几率:"+asp.getBuffProbability()+",");
				  														sb.append("产生的buff持续的时间:"+asp.getBuffLastingTime());
				  													}
				  													sb.append("]<br>");
				  												}
				  											}else{
				  												sb.append("<font color='red'>对应的修改数值为空</font><br>");
				  											}
				  											
				  										}else{
				  											sb.append("<font color='red'>id为("+o.toString()+")的技能为空</font><br>");
				  										}
				  										
				  									}
				  								}
				  								out.println(sb.toString());
				  							}
				  						}else{
				  							out.println(obj.toString());
				  						}
				  						
				  						break;
				  					}

			  				}
								
								
								%>
							
							</td>
							</tr>
							
							<%
						}
					}
					%>
					</table>
					<%
				}
				
				
				sfs = skillTemplate.getDataFields();
				if(sfs != null && sfs.length > 0  && !cm.isSkillForSprite(skill) && skill instanceof ActiveSkill){
					%>
					<table class="tablestyle1">
					<tr class="titlecolor"><td colspan="2"><font color="red">技能伤害或加血计算公式</font></td></tr>
					<%
					for(SkillField sf : sfs){
						if(sf != null){
							%>
							<%
							int alSize = al.size();
			  				for(int j = 0; j < alSize; j++){
					  				Method method = al.get(j);
					  		  		String name ="";
					  		  		if(method.getName().indexOf("is") == 0){
					  		  			name = method.getName().substring(2);
					  		  		}else{
					  		  			name = method.getName().substring(3);
					  		  		}
					  		  		name = Character.toLowerCase(name.charAt(0)) + name.substring(1);

				  					if(name.equals(sf.getFieldName())){
				  						Object obj = null;
				  						if(method.getReturnType().getName().indexOf("[") == 0){
			  								obj = gson.toJson(method.invoke(skill));
			  							}else{
			  								obj = method.invoke(skill);
			  							}
				  						//处理数据
				  						if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_TEXT){
				  							if("buffName".equals(name) && !"".equals(obj.toString().trim())){
				  								out.println("<a href='buffbyname.jsp?buffName="+obj.toString()+"'>"+obj.toString()+"</a>");
					  						}else if("param1".equals(name) && !"".equals(obj.toString().trim())){
					  							param1 = Integer.parseInt(obj.toString().trim());
					  						}else if("param2".equals(name) && !"".equals(obj.toString().trim())){
					  							param2 = Integer.parseInt(obj.toString().trim());
					  						}else if("param3".equals(name) && !"".equals(obj.toString().trim())){
					  							param3 = Integer.parseInt(obj.toString().trim());
					  						}else if("param4".equals(name) && !"".equals(obj.toString().trim())){
					  							param4 = Integer.parseInt(obj.toString().trim());
					  						}else if("param5".equals(name) && !"".equals(obj.toString().trim())){
					  							param5 = Integer.parseInt(obj.toString().trim());
					  						}else if("param6".equals(name) && !"".equals(obj.toString().trim())){
					  							param6 = Integer.parseInt(obj.toString().trim());
					  						}else if("param7".equals(name) && !"".equals(obj.toString().trim())){
					  							param7 = Integer.parseInt(obj.toString().trim());
					  						}else if("param8".equals(name) && !"".equals(obj.toString().trim())){
					  							param8 = Integer.parseInt(obj.toString().trim());
					  						}else{
					  							out.println(obj.toString());
					  						}
				  						}else if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_SELECT){
				  						
				  							try{
				  								int indexValue = -1;
				  							for(int i = 0; sf.getSelectValues() != null && i < sf.getSelectValues().length; i++){
				  								if(obj.toString().trim().equals(sf.getSelectValues()[i])){
				  									indexValue = i;
				  									break;
				  								}
				  							}
				  							if(indexValue != -1){
				  								out.println(sf.getSelectDisplayValues()[indexValue]);
				  							}else{
				  								out.println("<font color='red'>值出现问题，请联系技术查看数据</font>("+obj.toString()+")");
				  							}
				  							
				  							}catch(Exception e){
				  								out.println(obj.toString());
				  								out.println(gson.toJson(sf.getSelectDisplayValues()));
				  							}
				  						}else if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_LEVEL){
				  							
				  						}else if(sf.getInputType() == SkillField.INPUTTYPE_INPUT_MAP){
				  							
				  						}else{
				  							out.println(obj.toString());
				  						}
				  						
				  						break;
				  					}

			  				}
						}
					}
					try{
				  			/**
				  			 * 技能伤害或者加血计算公式：
				  			 * 
				  			 * 	 武器伤害 *（param1 + param2 * 技能等级）
				  			 * + 攻击强度 *（param3 + param4 * 技能等级）
				  			 * + 法术强度 *（param5 + param6 * 技能等级）
				  			 * + param7 + param8 * 技能等级
				  			 */
			  				String[] classStr = new String[1];
				  			StringBuffer sb = new StringBuffer();
				  			if(param1 != 0){
				  				if(param2 != 0){
				  					sb.append("武器伤害*（"+param1 +"+"+ param2 +"*技能等级）");
				  				}else{
				  					sb.append("武器伤害*"+param1);
				  				}
				  			}else{
				  				if(param2 != 0){
				  					sb.append("武器伤害*"+ param2 +"*技能等级");
				  				}
				  			}
				  			if(param3 != 0){
				  				if(param1 != 0 || param2 != 0){
				  					sb.append(" + ");
				  				}
				  				if(param4 != 0){
				  					sb.append("攻击强度*（"+param3 +"+"+ param4 +"*技能等级）");
				  				}else{
				  					sb.append("攻击强度*"+param3);
				  				}
				  			}else{
				  				if(param4 != 0){
				  					if(param1 != 0 || param2 != 0){
					  					sb.append(" + ");
					  				}
				  					sb.append("攻击强度*"+ param4 +"*技能等级");
				  				}
				  			}
				  			if(param5 != 0){
				  				if(param1 != 0 || param2 != 0 || param3 != 0 || param4 != 0){
				  					sb.append(" + ");
				  				}
				  				if(param6 != 0){
				  					sb.append("法术强度*（"+param5 +"+"+ param6 +"*技能等级）");
				  				}else{
				  					sb.append("法术强度*"+param5);
				  				}
				  			}else{
				  				if(param6 != 0){
				  					if(param1 != 0 || param2 != 0 || param3 != 0 || param4 != 0){
					  					sb.append(" + ");
					  				}
				  					sb.append("法术强度*"+ param6 +"*技能等级");
				  				}
				  			}
				  			if(param7 != 0){
				  				if(param1 != 0 || param2 != 0 || param3 != 0 || param4 != 0 || param5 != 0 || param6 != 0){
				  					sb.append(" + "+param7);
				  				}
				  			}
				  			if(param8 != 0){
				  				if(param1 != 0 || param2 != 0 || param3 != 0 || param4 != 0 || param5 != 0 || param6 != 0 || param7 != 0){
				  					sb.append(" + "+param8 +"*技能等级");
				  				}
				  			}
				  			%><tr><td class="tdwidth">技能伤害</td><td><%=sb.toString() %></td></tr>
				  			<%
			  			}catch(Exception e){
			  				
			  			}
					%>
					</table>
					<%
		  			
					
					
					
				}
				
				
			}else{
				out.println("<h2><font color='red'>未知类型，请联系开发人员</font></h2>");
			}
		}else{
			out.println("<h2>技能id不存在</h2>");
		}
	
	%>
<br/>
<input width="68" type="button" value="返回" onclick="javascript:history.back()">
<br>
</BODY>
</html>
