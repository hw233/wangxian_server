package com.fy.engineserver.datasource.career;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.TransferLanguage;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillParam;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.datasource.skill.activeskills.CommonAttackSkill;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithTraceAndDirectionOrTarget;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutEffect;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutEffectAndQuickMove;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutMorePoints;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndOnTeamMember;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithMatrix;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithRange;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithSummonNPC;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithTargetOrPosition;
import com.fy.engineserver.datasource.skill.passiveskills.AssistActiveSkillPassiveSkill;
import com.fy.engineserver.datasource.skill.passiveskills.IncreaseBaoJiPassiveSkill;
import com.fy.engineserver.datasource.skill.passiveskills.IncreaseDuoBiPassiveSkill;
import com.fy.engineserver.datasource.skill.passiveskills.IncreaseFaFangPassiveSkill;
import com.fy.engineserver.datasource.skill.passiveskills.IncreaseFaGongPassiveSkill;
import com.fy.engineserver.datasource.skill.passiveskills.IncreaseHpPassiveSkill;
import com.fy.engineserver.datasource.skill.passiveskills.IncreaseTeShuShuXingPassiveSkill;
import com.fy.engineserver.datasource.skill.passiveskills.IncreaseWuFangPassiveSkill;
import com.fy.engineserver.datasource.skill.passiveskills.IncreaseWuGongPassiveSkill;
import com.fy.engineserver.datasource.skill.passivetrigger.PassiveTriggerImmune;
import com.fy.engineserver.message.QUERY_XINFA_SKILL_IDLIST_REQ;
import com.fy.engineserver.message.QUERY_XINFA_SKILL_IDLIST_RES;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * 
 * 职业管理器，此方法中包含了所有的职业，以及各个职业的职业发展线路，
 * 以及各个发展线路中的技能，外加上怪的技能，npc的技能
 * 
 * 
 *
 */
public class CareerManager {
	
	private final static String ENCODING = "utf-8";
	
	public final static String[] careerNames = new String[]{Translate.通用,Translate.修罗,Translate.影魅,Translate.仙心,Translate.九黎, Translate.兽魁};
	/*
	 * 门派只为声望设置
	 */
	public final static String[] careerNamesForRepute = new String[]{Translate.text_1154,Translate.text_1155,Translate.text_3404,Translate.text_1157,Translate.text_1158};
	public final static String[] careerMapNames = new String[]{Translate.text_3405,Translate.text_3406,Translate.text_1156,Translate.text_3407,Translate.text_1158};

	private static final String tempLevel = null;
	
	public static int[] newCareerFlag = new int[]{5};
	public static int[] shoukuiCommenSkillIds = new int[2];			//兽魁普通攻击技能id，[0]人形态  [1]兽形态
	
	public int[] disasterSkills = new int[0];
	
	public static final String 兽魁进阶任务 = "领悟要诀";
	public static final String 兽魁进阶道具 = "魅影穷奇丹";
	
	public static Map<Byte, Integer[]> careerBuffs = new HashMap<Byte, Integer[]>();
	
	static {
		careerBuffs.put((byte)1, new Integer[]{1380,1492,1473});			//修罗，  狂印  血印 吸血（狂印大师级能）
		careerBuffs.put((byte)2, new Integer[]{1382,1556});			//影魅    熊印  狼印
		careerBuffs.put((byte)3, new Integer[]{1381,1361});			//仙心    灵印  炎印
		careerBuffs.put((byte)4, new Integer[]{1384,1555});			//九黎   骨印  妖印
//		careerBuffs.put((byte)5, new Integer[]{});			//兽魁不会出现本尊元神不同职业 
	}
	
	public static boolean canAddBuff(Player player, int buffId) {
		if (player.getLevel() < 60) {
			return true;
		}
		Soul base = player.getSoul(Soul.SOUL_TYPE_BASE);
		Soul soul = player.getSoul(Soul.SOUL_TYPE_SOUL);
		if (base != null && soul != null && base.getCareer() != soul.getCareer()) {		//只检查本尊元神不同职业的玩家
			Iterator<Byte> ite = careerBuffs.keySet().iterator();
			while(ite.hasNext()) {
				byte key = ite.next();
				if (key == player.getCareer()) {
					continue;
				}
				Integer[] buffs = careerBuffs.get(key);
				for (Integer buff : buffs) {
					if (buff == buffId) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/*
	 * 门派
	 */
	public static String careerNameByType(int type){
		if(type >= 0 && type < careerNames.length){
			return careerNames[type];
		}
		return "";
	}

	/*
	 * 门派只为声望设置
	 */
	public String careerNameForReputeByType(int type){
		if(type >= 0 && type < careerNamesForRepute.length){
			return careerNamesForRepute[type];
		}
		return "";
	}

	/**
	 * 门派所在地图
	 * @param type
	 * @return
	 */
	public String careerMapNameByType(int type){
		if(type >= 0 && type < careerMapNames.length){
			return careerMapNames[type];
		}
		return "";
	}

	protected static CareerManager self;

	public static CareerManager getInstance() {
		return self;
	}

	protected Career careers[] = new Career[0];

	protected HashMap<Integer,Skill> skillMapForInteger = new HashMap<Integer,Skill>();
	
	protected HashMap<String,Skill> skillMapForString = new HashMap<String,Skill>();
	
	protected CommonAttackSkill [] commonAttackSkills = new CommonAttackSkill[0];
	
	protected Skill [] monsterSkills = new Skill[0];
	
	public List<Skill> passiveTriSkills = new ArrayList<Skill>();

	private File configFile;

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}

	public void init() throws Exception {
		
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try{
			load(configFile);
		}catch(Exception e){
			System.out.println("[系统初始化] [职业技能管理器] [异常] ["+e+"]");
			Game.logger.error("出错~~~~", e);
			throw new Exception(e);
		}	
		self = this;
		
		ServiceStartRecord.startLog(this);
	}
	
	public static int 基本攻击技能_sheet = 0;
	public static int 攻击无后效技能_sheet = 1;
	public static int 无后效冲锋技能_sheet = 2;
	/**
	 * 作用于团队，中立方，或者敌方的技能，比如用于加血 此技能不计算伤害输入，只种植Buff
	 */
	public static int 无路径作用团队SkillWithoutTraceAndOnTeamMember技能_sheet = 3;
	public static int 无轨迹范围攻击SkillWithoutTraceAndWithMatrix技能_sheet = 4;
	public static int 无轨迹范围攻击SkillWithoutTraceAndWithRange技能_sheet = 5;
	public static int 无轨迹有目标攻击SkillWithoutTraceAndWithTargetOrPosition技能_sheet = 6;
	public static int 有轨迹的攻击指定目标或者方向的技能SkillWithTraceAndDirectionOrTarget技能_sheet = 7;
	public static int 无轨迹召唤NPCSkillWithoutTraceAndWithSummonNPC技能_sheet = 8;
	public static int 被动增加暴击IncreaseBaoJiPassiveSkill技能_sheet = 9;
	public static int 被动增加闪避IncreaseDuoBiPassiveSkill技能_sheet = 10;
	public static int 被动增加IncreaseHpPassiveSkill技能_sheet = 11;
	public static int 被动增加IncreaseWuGongPassiveSkill技能_sheet = 12;
	public static int 被动增加IncreaseWuFangPassiveSkill技能_sheet = 13;
	public static int 被动增加IncreaseFaGongPassiveSkill技能_sheet = 14;
	public static int 被动增加IncreaseFaFangPassiveSkill技能_sheet = 15;
	public static int 被动增加IncreaseTeShuShuXingPassiveSkill技能_sheet = 16;
	
	public static int 职业_sheet = 17;
	public static int 仙界技能_sheet = 18;
	@Deprecated
	public static int 多坐标配置技能_sheet = 18;
	
	public static int 技能文件最大sheet = 19;
	
	public static int 基本攻击技能_id_所在列 = 0;
	public static int 基本攻击技能_name_所在列 = 1;
	public static int 基本攻击技能_iconId_所在列 = 2;
	public static int 基本攻击技能_description_所在列 = 3;
	public static int 基本攻击技能_shortDescription_所在列 = 4;
	public static int 基本攻击技能_maxLevel_所在列 = 5;
	public static int 基本攻击技能_needMoney_所在列 = 6;
	public static int 基本攻击技能_needYuanQi_所在列 = 7;
	public static int 基本攻击技能_needExp_所在列 = 8;
	public static int 基本攻击技能_needPoint_所在列 = 9;
	public static int 基本攻击技能_needPlayerLevel_所在列 = 10;
	public static int 基本攻击技能_needCareerThreadPoints_所在列 = 11;
	public static int 基本攻击技能_colIndex_所在列 = 12;
	
	public static int 主动攻击技能_duration1_所在列 = 13;
	public static int 主动攻击技能_duration2_所在列 = 14;
	public static int 主动攻击技能_duration3_所在列 = 15;
	public static int 主动攻击技能_calDamageTime_所在列 = 16;
	public static int 主动攻击技能_style1_所在列 = 17;
	public static int 主动攻击技能_style2_所在列 = 18;
	public static int 主动攻击技能_effectiveTimes_所在列 = 19;
	public static int 主动攻击技能_enableWeaponType_所在列 = 20;
	public static int 主动攻击技能_weaponTypeLimit_所在列 = 21;
	public static int 主动攻击技能_attackType_所在列 = 22;
	public static int 主动攻击技能_followByCommonAttack_所在列 = 23;
	public static int 主动攻击技能_ignoreStun_所在列 = 24;
	public static int 主动攻击技能_param1_所在列 = 25;
	public static int 主动攻击技能_param2_所在列 = 26;
	public static int 主动攻击技能_param3_所在列 = 27;
	public static int 主动攻击技能_param4_所在列 = 28;
	public static int 主动攻击技能_param5_所在列 = 29;
	public static int 主动攻击技能_param6_所在列 = 30;
	public static int 主动攻击技能_param7_所在列 = 31;
	public static int 主动攻击技能_param8_所在列 = 32;
	public static int 主动攻击技能_hateParam_所在列 = 33;
	public static int 主动攻击技能_buffTargetType_所在列 = 34;
	public static int 主动攻击技能_buffName_所在列 = 35;
	public static int 主动攻击技能_buffLevel_所在列 = 36;
	public static int 主动攻击技能_buffLastingTime_所在列 = 37;
	public static int 主动攻击技能_buffProbability_所在列 = 38;
	public static int 主动攻击技能_attackDamages_所在列 = 39;
	public static int 主动攻击技能_guajiFalg_所在列 = 40;
	public static int 主动攻击技能_flySound_所在列 = 41;
	public static int 主动攻击技能_explodeSound_所在列 = 42;
	public static int 主动攻击技能_flyParticle_所在列 = 43;
	public static int 主动攻击技能_angle_所在列 = 44;
	public static int 主动攻击技能_explodeParticle_所在列 = 45;
	public static int 主动攻击技能_explodePercent_所在列 = 46;
	public static int 主动攻击技能_targetFootParticle_所在列 = 47;
	public static int 主动攻击技能_bodyParticle_所在列 = 48;
	public static int 主动攻击技能_bodyPercent_所在列 = 49;
	public static int 主动攻击技能_bodyParticleOffset_所在列 = 50;
	public static int 主动攻击技能_bodyParticlePlayStart_所在列 = 51;
	public static int 主动攻击技能_bodyParticlePlayEnd_所在列 = 52;
	public static int 主动攻击技能_bodyPartPath_所在列 = 53;
	public static int 主动攻击技能_bodyPartAnimation_所在列 = 54;
	public static int 主动攻击技能_bodyPartAnimationPercent_所在列 = 55;
	public static int 主动攻击技能_bodyPartAnimationOffset_所在列 = 56;
	public static int 主动攻击技能_bodyPartAnimationPlayStart_所在列 = 57;
	public static int 主动攻击技能_bodyPartAnimationPlayEnd_所在列 = 58;
	public static int 主动攻击技能_footParticle_所在列 = 59;
	public static int 主动攻击技能_footParticleOffset_所在列 = 60;
	public static int 主动攻击技能_footParticlePlayStart_所在列 = 61;
	public static int 主动攻击技能_footParticlePlayEnd_所在列 = 62;
	public static int 主动攻击技能_footPartPath_所在列 = 63;
	public static int 主动攻击技能_footPartAnimation_所在列 = 64;
	public static int 主动攻击技能_footPartAnimationOffset_所在列 = 65;
	public static int 主动攻击技能_footPartAnimationPlayStart_所在列 = 66;
	public static int 主动攻击技能_footPartAnimationPlayEnd_所在列 = 67;

	
	public static int 一般攻击技能_avataRace_所在列 = 68;
	public static int 一般攻击技能_avataSex_所在列 = 69;
	public static int 一般攻击技能_effectType_所在列 = 70;
	public static int 一般攻击技能_effectLastTime_所在列 = 71;
	public static int 一般攻击技能_effectExplosionLastTime_所在列 = 72;
	public static int 一般攻击技能_range_所在列 = 73;
	
	public static int 攻击无后效技能_range_所在列 = 68;
	
	public static int 无后效冲锋技能_range_所在列 = 68;
	public static int 无后效冲锋技能_traceType_所在列 = 69;
	public static int 无后效冲锋技能_distance_所在列 = 70;
	public static int 无后效冲锋技能_mp_所在列 = 71;
	public static int 无后效冲锋技能_skillType_所在列 = 72;
	
	public static int 无路径作用团队技能_rangeType_所在列 = 68;
	public static int 无路径作用团队技能_rangeWidth_所在列 = 69;
	public static int 无路径作用团队技能_rangeHeight_所在列 = 70;
	public static int 无路径作用团队技能_avataRace_所在列 = 71;
	public static int 无路径作用团队技能_avataSex_所在列 = 72;
	public static int 无路径作用团队技能_effectType_所在列 = 73;
	public static int 无路径作用团队技能_effectLastTime_所在列 = 74;
	public static int 无路径作用团队技能_effectExplosionLastTime_所在列 = 75;
	public static int 无路径作用团队技能_mp_所在列 = 76;
	
	public static int 无轨迹范围攻击技能_matrixType_所在列 = 68;
	public static int 无轨迹范围攻击技能_maxAttackNums_所在列 = 69;
	public static int 无轨迹范围攻击技能_gapWidth_所在列 = 70;
	public static int 无轨迹范围攻击技能_gapHeight_所在列 = 71;
	public static int 无轨迹范围攻击技能_effectNum_所在列 = 72;
	public static int 无轨迹范围攻击技能_avataRace_所在列 = 73;
	public static int 无轨迹范围攻击技能_avataSex_所在列 = 74;
	public static int 无轨迹范围攻击技能_effectType_所在列 = 75;
	public static int 无轨迹范围攻击技能_effectLastTime_所在列 = 76;
	public static int 无轨迹范围攻击技能_effectExplosionLastTime_所在列 = 77;
	public static int 无轨迹范围攻击技能_mp_所在列 = 78;
	public static int 无轨迹范围攻击技能_range_所在列 = 79;
	
	public static int 无轨迹范围Range攻击技能_rangeType_所在列 = 68;
	public static int 无轨迹范围Range攻击技能_rangeWidth_所在列 = 69;
	public static int 无轨迹范围Range攻击技能_rangeHeight_所在列 = 70;
	public static int 无轨迹范围Range攻击技能_maxAttackNums_所在列 = 71;
	public static int 无轨迹范围Range攻击技能_avataRace_所在列 = 72;
	public static int 无轨迹范围Range攻击技能_avataSex_所在列 = 73;
	public static int 无轨迹范围Range攻击技能_effectType_所在列 = 74;
	public static int 无轨迹范围Range攻击技能_effectLastTime_所在列 = 75;
	public static int 无轨迹范围Range攻击技能_effectExplosionLastTime_所在列 = 76;
	public static int 无轨迹范围Range攻击技能_mp_所在列 = 77;
	
	public static int 无轨迹有目标攻击技能_avataRace_所在列 = 68;
	public static int 无轨迹有目标攻击技能_avataSex_所在列 = 69;
	public static int 无轨迹有目标攻击技能_effectType_所在列 = 70;
	public static int 无轨迹有目标攻击技能_effectLastTime_所在列 = 71;
	public static int 无轨迹有目标攻击技能_effectExplosionLastTime_所在列 = 72;
	public static int 无轨迹有目标攻击技能_mp_所在列 = 73;
	public static int 无轨迹有目标攻击技能_range_所在列 = 74;
	
	public static int 有轨迹的攻击指定目标或者方向的技能_targetType_所在列 = 68;
	public static int 有轨迹的攻击指定目标或者方向的技能_avataRace_所在列 = 69;
//	public static int 有轨迹的攻击指定目标或者方向的技能_avataSex_所在列 = 47;
	public static int 有轨迹的攻击指定目标或者方向的技能_effectType_所在列 = 70;
	public static int 有轨迹的攻击指定目标或者方向的技能_trackType_所在列 = 71;
	public static int 有轨迹的攻击指定目标或者方向的技能_sendEffectTime_所在列 = 72;
	public static int 有轨迹的攻击指定目标或者方向的技能_speed_所在列 = 73;
	public static int 有轨迹的攻击指定目标或者方向的技能_range_所在列 = 74;
	public static int 有轨迹的攻击指定目标或者方向的技能_attackWidth_所在列 = 75;
	public static int 有轨迹的攻击指定目标或者方向的技能_explosionLastingTime_所在列 = 76;
	public static int 有轨迹的攻击指定目标或者方向的技能_effectNum_所在列 = 77;
	public static int 有轨迹的攻击指定目标或者方向的技能_effectInitPositionX_所在列 = 78;
	public static int 有轨迹的攻击指定目标或者方向的技能_effectInitPositionY_所在列 = 79;
	public static int 有轨迹的攻击指定目标或者方向的技能_effectInitDirection_所在列 = 80;
	public static int 有轨迹的攻击指定目标或者方向的技能_mp_所在列 = 81;
	public static int 有轨迹的攻击指定目标或者方向的技能_penetrateNum_所在列 = 82;
	
	public static int 多坐标技能坐标点_所在列 = 68;
	public static int 多坐标技能召唤数量_所在列 = 69;

	
	public static int 召唤NPC技能_matrixType_所在列 = 68;
	public static int 召唤NPC技能_attackNum_所在列 = 69;
	public static int 召唤NPC技能_maxTimeLength_所在列 = 70;
	public static int 召唤NPC技能_speed_所在列 = 71;
	public static int 召唤NPC技能_angle_所在列 = 72;
	public static int 召唤NPC技能_heigth_所在列 = 73;
	public static int 召唤NPC技能_maxParticleEachTime_所在列 = 74;
	public static int 召唤NPC技能_intervalTimeAttack_所在列 = 75;
	public static int 召唤NPC技能_gapWidth_所在列 = 76;
	public static int 召唤NPC技能_gapHeight_所在列 = 77;
	public static int 召唤NPC技能_range_所在列 = 78;
	public static int 召唤NPC技能_mp_所在列 = 79;
	public static int 召唤NPC技能_可否对人造成伤害_所在列 = 80;
	
	public static int 被动增加暴击_baoJi_所在列 = 13;
	
	public static int 被动增加闪避_dodge_所在列 = 13;
	
	public static int 被动增加血_HP_所在列 = 13;
	
	public static int 被动增加物攻_phyAttack_所在列 = 13;
	
	public static int 被动增加物防_phyDef_所在列 = 13;
	
	public static int 被动增加法攻_magicAttack_所在列 = 13;
	
	public static int 被动增加法防_magicDef_所在列 = 13;
	
	public static int 被动增加属性类型_attributeType_所在列 = 13;
	public static int 被动增加属性_addAttribute_所在列 = 14;
	
	public static int 职业_id_所在列 = 0;
	public static int 职业_name_所在列 = 1;
	public static int 职业_weaponTypeLimit_所在列 = 2;
	public static int 职业_enableMale_所在列 = 3;
	public static int 职业_enableFemale_所在列 = 4;
	public static int 职业_basicSkills_所在列 = 5;
	public static int 职业_nuqiSkills_所在列 = 6;
	public static int 职业_xinfaSkills_所在列 = 7;
	public static int 职业_kingSkills_所在列 = 8;
	public static int 职业_职业发展线路1name_所在列 = 9;
	public static int 职业_description1_所在列 = 10;
	public static int 职业_skills1_所在列 = 11;
	public static int 职业_变身技能_所在列 = 12;
	public static int 职业_变身按钮_所在列 = 13;
	public static int 职业_需要消耗或增加豆技能_所在列 = 14;
	public static int 职业_对应消耗或增加豆数量_所在列 = 15;
	public static int 职业_根据状态触发效果_所在列 = 16;
	public static int 职业_对应需要玩家所处状态_所在列 = 17;
	public static int 职业_触发类型_所在列 = 18;
	public static int 职业_具体效果_所在列 = 19;
	public static int 职业_兽魁普通攻击技能_所在列 = 20;
	
	
	public static final int 触发类型_百分比增加技能伤害 = 1;
	public static final int 触发类型_传播buff = 2;
	public static final int 触发类型_技能造成伤害吸血 = 3;
	
	public Skill[] getSkills(){
		return skillMapForInteger.values().toArray(new Skill[0]);
	}
	
	public Skill getSkillById(int skillId){
		return this.skillMapForInteger.get(skillId);
	}
	
	public Skill getSkillByName(String name){
		return this.skillMapForString.get(name);
	}

	public Object getSkillsByClass(Class<?> c) {
		if (c != null) {
			Skill[] ss = getSkills();
			ArrayList<Skill> al = new ArrayList<Skill>();
			for (Skill s : ss) {
				if (s.getClass() == c) {
					al.add(s);
				}
			}
			Object skills = java.lang.reflect.Array.newInstance(c, al.size());
			for (int i = 0; i < al.size(); i++) {
				java.lang.reflect.Array.set(skills, i, al.get(i));
			}
			return skills;
		}
		return null;
	}
	/*



	<property name="skillPlayType" type="byte" comment="技能类型，0为冲锋，1为闪现"></property>*/
	public static ActiveSkill getSkillInfo(int skillId, Player player, ActiveSkill clazz){
		ActiveSkill result = null;
		try {
			ActiveSkill skill = (ActiveSkill) CareerManager.getInstance().getSkillById(skillId);
			result = new SkillWithoutEffectAndQuickMove();
			result.setStyle2(skill.getStyle2());
			result.setEffectiveTimes(skill.getEffectiveTimes());
			result.setEnableWeaponType(skill.getEnableWeaponType());
			result.setWeaponTypeLimit(skill.getWeaponTypeLimit());
			result.setFollowByCommonAttack(skill.getFollowByCommonAttack());
			result.setIgnoreStun(skill.isIgnoreStun());
			result.setId(skill.getId());
			result.setDuration1(skill.getDuration1());
			result.setDuration2(skill.getDuration2());
			result.setDuration3(skill.getDuration3());
			result.setStyle1(skill.getStyle1());
			result.setBodyPercent(skill.getBodyPercent());
			result.setBodyParticle(skill.getBodyParticle());
			result.setBodyParticleOffset(skill.getBodyParticleOffset());
			result.setBodyParticlePlayStart(skill.getBodyParticlePlayStart());
			result.setBodyParticlePlayEnd(skill.getBodyParticlePlayEnd());
			result.setBodyPartPath(skill.getBodyPartPath());
			result.setBodyPartAnimation(skill.getBodyPartAnimation());
			result.setBodyPartAnimationPercent(skill.getBodyPartAnimationPercent());
			result.setBodyPartAnimationOffset(skill.getBodyPartAnimationOffset());
			result.setBodyPartAnimationPlayStart(skill.getBodyPartAnimationPlayStart());
			result.setBodyPartAnimationPlayEnd(skill.getBodyPartAnimationPlayEnd());
			result.setFootParticle(skill.getFootParticle());
			result.setFootParticleOffset(skill.getFootParticleOffset());
			result.setFootParticlePlayStart(skill.getFootParticlePlayStart());
			result.setFootParticlePlayEnd(skill.getFootParticlePlayEnd());
			result.setFootPartPath(skill.getFootPartPath());
			result.setFootPartAnimation(skill.getFootPartAnimation());
			result.setFootPartAnimationOffset(skill.getFootPartAnimationOffset());
			result.setFootPartAnimationPlayStart(skill.getFootPartAnimationPlayStart());
			result.setFootPartAnimationPlayEnd(skill.getFootPartAnimationPlayEnd());
			result.setAngle(skill.getAngle());
			result.setTargetFootParticle(skill.getTargetFootParticle());
			result.setGuajiFlag(skill.isGuajiFlag());
			result.setBuffName(skill.getBuffName());
			if (clazz instanceof SkillWithoutEffectAndQuickMove) {
				int skillLv = player.getSkillLevel(skillId);
				
				((SkillWithoutEffectAndQuickMove)result).setSkillPlayType(((SkillWithoutEffectAndQuickMove)skill).getSkillPlayType());
				((SkillWithoutEffectAndQuickMove)result).setTraceType(((SkillWithoutEffectAndQuickMove)skill).getTraceType());
				((SkillWithoutEffectAndQuickMove)result).setDistance(((SkillWithoutEffectAndQuickMove)skill).getDistance());
				((SkillWithoutEffectAndQuickMove)result).setMp(((SkillWithoutEffectAndQuickMove)skill).getMp());
				((SkillWithoutEffectAndQuickMove)result).setRange(((SkillWithoutEffectAndQuickMove)skill).getRange(skillLv));
				
			}
		} catch (Exception e) {
			Skill.logger.warn("[getSkillInfo] [异常] [" + player.getLogString() + "] ", e);
		}
		
		return result;
	}
	
	/**
	 * 从文件中读取career的信息
	 * 
	 * @param file
	 * @throws Exception
	 */
	public void load(File file) throws Exception {

		FileInputStream fis = new FileInputStream(file);
		loadFrom(fis);
		
		for (Career career : careers) {
			career.init();
		}
	}
	
	/**
	 * 加载职业数据，只有加载完全成功后，才修改服务器内存中的数据
	 * 
	 * @param content
	 * @throws Exception
	 */
	public void loadFroms(String content) throws Exception{

		Document dom = XmlUtil.loadString(content);
		Element root = dom.getDocumentElement();
		
		Element careerEles[] = XmlUtil.getChildrenByName(root, "career");
		
		ArrayList<Career> careerList = new ArrayList<Career>();
		try {
			for(int i = 0 ; i < careerEles.length ; i++){
				Element e = careerEles[i];
				Element fes[] = XmlUtil.getChildrenByName(e, "field");
				Career c = new Career();
				for(int j = 0 ; j < fes.length ; j++){
					String name = XmlUtil.getAttributeAsString(fes[j], "name", TransferLanguage.getMap());
					String value = XmlUtil.getAttributeAsString(fes[j], "value","", TransferLanguage.getMap());
					setObjectValue(c.getClass(),c,name,value);
				}
				
				String categoryNames[] = new String[]{"skillcategory1","skillcategory2"};
				c.threads = new CareerThread[categoryNames.length];
				
				for(int x = 0 ; x < categoryNames.length ; x++){
					Element se = XmlUtil.getChildByName(e,categoryNames[x]);
					CareerThread ct = new CareerThread();
					ct.career = c;
					ct.indexOfCareer = x;
					fes = XmlUtil.getChildrenByName(se, "field");
					for(int j = 0 ; j < fes.length ; j++){
						String name = XmlUtil.getAttributeAsString(fes[j], "name", TransferLanguage.getMap());
						String value = XmlUtil.getAttributeAsString(fes[j], "value","", TransferLanguage.getMap());
						setObjectValue(ct.getClass(),ct,name,value);
					}
					
					ArrayList<Skill> al = new ArrayList<Skill>();
					Element skillEles[] = XmlUtil.getChildrenByName(se, "skill");
					for(int j = 0 ; j < skillEles.length ; j++){
						Element m = skillEles[j];
						String templateClassName = XmlUtil.getAttributeAsString(m, "template", TransferLanguage.getMap());
						Class cl = Class.forName(templateClassName);
						Skill skill = (Skill)cl.newInstance();
						
						fes = XmlUtil.getChildrenByName(m, "field");
						for(int k = 0 ; k < fes.length ; k++){
							String name = XmlUtil.getAttributeAsString(fes[k], "name", TransferLanguage.getMap());
							String value = XmlUtil.getAttributeAsString(fes[k], "value","", TransferLanguage.getMap());
							setObjectValue(cl,skill,name,value);
						}
						al.add(skill);
						
						if(cl == AssistActiveSkillPassiveSkill.class){
							AssistActiveSkillPassiveSkill passiveSkill = (AssistActiveSkillPassiveSkill)skill;
							
							fes = XmlUtil.getChildrenByName(m, "refskill");
							for(int k = 0 ; k < fes.length ; k++){
								Element r = fes[k];
								int skillId = XmlUtil.getAttributeAsInteger(r, "id");
								
								ActiveSkillParam params[] = new ActiveSkillParam[skill.getMaxLevel()];
								
								for(int l = 0 ; l < params.length ; l++){
									params[l] = new ActiveSkillParam();
								}
								
								Element ps[] = XmlUtil.getChildrenByName(r, "prop");
								for(int l = 0 ; l < ps.length ; l ++){
									String name = XmlUtil.getAttributeAsString(ps[l], "name", TransferLanguage.getMap());
									
									Element les[] = XmlUtil.getChildrenByName(ps[l], "level");
									for(int h = 0 ; h < les.length ; h++){
										int level = XmlUtil.getAttributeAsInteger(les[h], "id");
										int value = XmlUtil.getAttributeAsInteger(les[h], "value",0);
										if(level < params.length){
											setObjectValue(ActiveSkillParam.class,params[level],name,""+value);
										}
									}
								}
								
								
								passiveSkill.getMaps().put(skillId, params);
							}
						}
						
					}
					
					Skill skills[] = al.toArray(new Skill[0]);
					Arrays.sort(skills,new Comparator<Skill>(){
						
						public int compare(Skill o1, Skill o2) {
							if(o1.getNeedCareerThreadPoints() < o2.getNeedCareerThreadPoints()){
								return -1;
							}else if(o1.getNeedCareerThreadPoints() > o2.getNeedCareerThreadPoints()){
								return 1;
							}else{
								if(o1.getColIndex() < o2.getColIndex()){
									return -1;
								}else if(o1.getColIndex() > o2.getColIndex()){
									return 1;
								}else if(o1.getId() < o2.getId()){
									return -1;
								}else if(o1.getId() > o2.getId()){
									return 1;
								}else{
									return 0;
								}
							}
							
						}
						
					});
					
					ct.skills = skills;
					
					c.threads[x] = ct;
				}
				careerList.add(c);
			}
		} catch (Exception e) {
			Game.logger.error("1111出错:", e);
		}
		
		Element e = XmlUtil.getChildByName(root, "monstercategory");
		Element eles[] = XmlUtil.getChildrenByName(e, "skill");
		
		ArrayList<Skill> monsterSkillList = new ArrayList<Skill>();

		for(int i = 0 ; i < eles.length ; i++){
			Element m = eles[i];
			String templateClassName = XmlUtil.getAttributeAsString(m, "template", TransferLanguage.getMap());
			Class cl = Class.forName(templateClassName);
			Skill skill = (Skill)cl.newInstance();
				
			Element [] fes = XmlUtil.getChildrenByName(m, "field");
			for(int k = 0 ; k < fes.length ; k++){
				String name = XmlUtil.getAttributeAsString(fes[k], "name", TransferLanguage.getMap());
				String value = XmlUtil.getAttributeAsString(fes[k], "value","", TransferLanguage.getMap());
				setObjectValue(cl,skill,name,value);
			}
			monsterSkillList.add(skill);
//			Game.logger.debug("[初始化怪物技能] ["+skill.getName()+"] ["+skill.getId()+"]");
			if (Game.logger.isDebugEnabled()) {
				Game.logger.debug("[初始化怪物技能] [{}] [{}]", new Object[]{skill.getName(),skill.getId()});
			}
		}
		
		Element be = XmlUtil.getChildByName(root, "bosscategory");
		if(be != null){
			Element bles[] = XmlUtil.getChildrenByName(be, "skill");
			
			for(int i = 0 ; i < bles.length ; i++){
				Element m = bles[i];
				String templateClassName = XmlUtil.getAttributeAsString(m, "template", TransferLanguage.getMap());
				Class cl = Class.forName(templateClassName);
				Skill skill = (Skill)cl.newInstance();
					
				Element [] fes = XmlUtil.getChildrenByName(m, "field");
				for(int k = 0 ; k < fes.length ; k++){
					String name = XmlUtil.getAttributeAsString(fes[k], "name", TransferLanguage.getMap());
					String value = XmlUtil.getAttributeAsString(fes[k], "value","", TransferLanguage.getMap());
					setObjectValue(cl,skill,name,value);
				}
				monsterSkillList.add(skill);
			}
		}
				
		Element fe = XmlUtil.getChildByName(root, "fabaocategory");
		if(fe != null){
			Element fles[] = XmlUtil.getChildrenByName(fe, "skill");
			
			for(int i = 0 ; i < fles.length ; i++){
				Element m = fles[i];
				String templateClassName = XmlUtil.getAttributeAsString(m, "template", TransferLanguage.getMap());
				Class cl = Class.forName(templateClassName);
				Skill skill = (Skill)cl.newInstance();
					
				Element [] fes = XmlUtil.getChildrenByName(m, "field");
				for(int k = 0 ; k < fes.length ; k++){
					String name = XmlUtil.getAttributeAsString(fes[k], "name", TransferLanguage.getMap());
					String value = XmlUtil.getAttributeAsString(fes[k], "value","", TransferLanguage.getMap());
					setObjectValue(cl,skill,name,value);
				}
				monsterSkillList.add(skill);
			}
		}
		
		e = XmlUtil.getChildByName(root, "commonattackcategory");
		eles = XmlUtil.getChildrenByName(e, "skill");
		
		ArrayList<CommonAttackSkill> commonAttackSkillList = new ArrayList<CommonAttackSkill>();

		for(int i = 0 ; i < eles.length ; i++){
			Element m = eles[i];
			Class cl = CommonAttackSkill.class;
			Skill skill = (Skill)cl.newInstance();
				
			Element [] fes = XmlUtil.getChildrenByName(m, "field");
			for(int k = 0 ; k < fes.length ; k++){
				String name = XmlUtil.getAttributeAsString(fes[k], "name", TransferLanguage.getMap());
				String value = XmlUtil.getAttributeAsString(fes[k], "value","", TransferLanguage.getMap());
				setObjectValue(cl,skill,name,value);
			}
			commonAttackSkillList.add((CommonAttackSkill)skill);
		}
		
		skillMapForInteger.clear();
		skillMapForString.clear();
		
		this.careers = new Career[careerList.size()];
		for(int i = 0 ; i < careerList.size() ; i++){
			Career c = careerList.get(i);
			careers[c.getId()] = c;
		}
		
		for(Career career : careerList){
			for(CareerThread ct : career.threads){
				for(Skill s : ct.skills){
					skillMapForInteger.put(s.getId(), s);
					skillMapForString.put(s.getName(),s);
				}
			}
		}
		for(Skill s : commonAttackSkillList){
			skillMapForInteger.put(s.getId(), s);
			skillMapForString.put(s.getName(),s);
		}
		
		for(Skill s : monsterSkillList){
			skillMapForInteger.put(s.getId(), s);
			skillMapForString.put(s.getName(),s);
		}
		
		this.commonAttackSkills = commonAttackSkillList.toArray(new CommonAttackSkill[0]);
		this.monsterSkills = monsterSkillList.toArray(new Skill[0]);
		
		System.out.println("[初始化职业技能数据] [普通技能："+commonAttackSkills.length+"] [怪物技能："+monsterSkills.length+"]");
		
//		String weaponTypeNames[] = Weapon.WEAPON_TYPE_NAMES;
//		for(int i = 0 ; i < weaponTypeNames.length ; i++){
//			CommonAttackSkill c = getCommonAttackSkill((byte)i);
//			if(c == null){
//				System.out.println("【警告】：技能数据加载中发现错误，武器【" + weaponTypeNames[i] + "】对应的普通攻击技能不存在！");
//			}
//		}
		
	}
	
	/**
	 * 加载职业数据，只有加载完全成功后，才修改服务器内存中的数据
	 * 
	 * @param content
	 * @throws Exception
	 */
	public void loadFrom(InputStream is) throws Exception{
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		ArrayList<CommonAttackSkill> commonAttackSkillList = new ArrayList<CommonAttackSkill>();
		ArrayList<Career> careerList = new ArrayList<Career>();
		ArrayList<Skill> monsterSkillList = new ArrayList<Skill>();
		try {
			for (int i = 0; i < 技能文件最大sheet; i++) {
				Game.logger.warn("技能文件最大sheet~~~~~~~~~~~~~~["+i+"]");
				HSSFSheet sheet = workbook.getSheetAt(i);
				int rows = sheet.getPhysicalNumberOfRows();
				if (i == 基本攻击技能_sheet) {
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							CommonAttackSkill skill = new CommonAttackSkill();
							所有技能的基本数据(skill, row);
							所有主动技能的数据(skill, row);
							HSSFCell cell = row.getCell(一般攻击技能_avataRace_所在列);
							String avataRace = "";
							try {
								avataRace = cell.getStringCellValue().trim();
								skill.setAvataRace(avataRace);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							
							cell = row.getCell(一般攻击技能_avataSex_所在列);
							String avataSex = "";
							try {
								if(cell != null)
									avataSex = cell.getStringCellValue().trim();
								skill.setAvataSex(avataSex);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							
							cell = row.getCell(一般攻击技能_effectType_所在列);
							try {
								String effectType = cell.getStringCellValue().trim();
								skill.setEffectType(effectType);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							
							cell = row.getCell(一般攻击技能_effectLastTime_所在列);
							try {
								int effectLastTime = (int)cell.getNumericCellValue();
								skill.setEffectLastTime(effectLastTime);
							} catch (Exception ex) {
								try{
									int effectLastTime = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setEffectLastTime(effectLastTime);
								}catch(Exception e){
									
								}
							}
							
							cell = row.getCell(一般攻击技能_effectExplosionLastTime_所在列);
							try {
								int effectExplosionLastTime = (int)cell.getNumericCellValue();
								skill.setEffectExplosionLastTime(effectExplosionLastTime);
							} catch (Exception ex) {
								try{
									int effectExplosionLastTime = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setEffectExplosionLastTime(effectExplosionLastTime);
								}catch(Exception e){
									
								}
							}
							
							cell = row.getCell(一般攻击技能_range_所在列);
							try {
								int range = (int)cell.getNumericCellValue();
								skill.setRange(range);
							} catch (Exception ex) {
								try{
									int range = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setRange(range);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							commonAttackSkillList.add(skill);
							if(Game.logger.isDebugEnabled()) {
								Game.logger.debug("[加载普通攻击技能] ["+skill.getId()+"] ["+skill.getName()+"]");
							}
						}
					}
				}else if (i == 攻击无后效技能_sheet) {
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							SkillWithoutEffect skill = new SkillWithoutEffect();
							所有技能的基本数据(skill, row);
							所有主动技能的数据(skill, row);
							HSSFCell cell = row.getCell(攻击无后效技能_range_所在列);
							try {
								int range = (int)cell.getNumericCellValue();
								skill.setRange(range);
							} catch (Exception ex) {
								try{
									int range = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setRange(range);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							monsterSkillList.add(skill);
							if(Game.logger.isDebugEnabled()) {
								Game.logger.debug("[加载怪物技能] ["+skill.getId()+"] ["+skill.getName()+"]");
							}
						}
					}
				}else if (i == 无后效冲锋技能_sheet) {
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							SkillWithoutEffectAndQuickMove skill = new SkillWithoutEffectAndQuickMove();
							所有技能的基本数据(skill, row);
							所有主动技能的数据(skill, row);
							HSSFCell cell = row.getCell(无后效冲锋技能_range_所在列);
							try {
								int range = (int)cell.getNumericCellValue();
								skill.setRange(range);
							} catch (Exception ex) {
								try{
									int range = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setRange(range);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							
							cell = row.getCell(无后效冲锋技能_traceType_所在列);
							try {
								int traceType = (int)cell.getNumericCellValue();
								skill.setTraceType(traceType);
							} catch (Exception ex) {
								try{
									int traceType = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setTraceType(traceType);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							
							cell = row.getCell(无后效冲锋技能_distance_所在列);
							try {
								int distance = (int)cell.getNumericCellValue();
								skill.setDistance(distance);
							} catch (Exception ex) {
								try{
									int distance = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setDistance(distance);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							
							cell = row.getCell(无后效冲锋技能_mp_所在列);
							String mps = "";
							try {
								mps = cell.getStringCellValue().trim();
								String[] mp = mps.split(";");
								if(mp.length == 1){
									mp = mps.split("；");
								}
								short[] efs = new short[skill.getMaxLevel()];
								for(int j = 0; j < mp.length && j < skill.getMaxLevel(); j++){
									efs[j] = Short.parseShort(mp[j]);
								}
								if(skill.getMaxLevel() > mp.length){
									for(int j = mp.length; j < skill.getMaxLevel(); j++){
										efs[j] = Short.parseShort(mp[mp.length - 1]);
									}
								}
								skill.setMp(efs);
							} catch (Exception ex) {
								short[] efs = new short[skill.getMaxLevel()];
								for(int j = 0; j < skill.getMaxLevel(); j++){
									efs[j] = (short)cell.getNumericCellValue();
								}
								skill.setMp(efs);
							}
							
							
							cell = row.getCell(无后效冲锋技能_skillType_所在列);
							try {
								byte skillPlayType = (byte)cell.getNumericCellValue();
								skill.setSkillPlayType(skillPlayType);
							} catch (Exception ex) {
								
							}
							monsterSkillList.add(skill);
							if(Game.logger.isDebugEnabled()) {
								Game.logger.debug("[加载怪物技能] ["+skill.getId()+"] ["+skill.getName()+"]");
							}
						}
					}
				}else if (i == 无路径作用团队SkillWithoutTraceAndOnTeamMember技能_sheet) {
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							SkillWithoutTraceAndOnTeamMember skill = new SkillWithoutTraceAndOnTeamMember();
							所有技能的基本数据(skill, row);
							所有主动技能的数据(skill, row);
							HSSFCell cell = row.getCell(无路径作用团队技能_rangeType_所在列);
							try {
								byte rangeType = (byte)cell.getNumericCellValue();
								skill.setRangeType(rangeType);
							} catch (Exception ex) {
								try{
									byte rangeType = Byte.parseByte(cell.getStringCellValue().trim());
									skill.setRangeType(rangeType);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"---"+r+"--e:"+e);	
								}
							}
							
							cell = row.getCell(无路径作用团队技能_rangeWidth_所在列);
							try {
								int rangeWidth = (int)cell.getNumericCellValue();
								skill.setRangeWidth(rangeWidth);
							} catch (Exception ex) {
								try{
									int rangeWidth = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setRangeWidth(rangeWidth);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							
							cell = row.getCell(无路径作用团队技能_rangeHeight_所在列);
							try {
								int rangeHeight = (int)cell.getNumericCellValue();
								skill.setRangeHeight(rangeHeight);
							} catch (Exception ex) {
								try{
									int rangeHeight = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setRangeHeight(rangeHeight);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							cell = row.getCell(无路径作用团队技能_avataRace_所在列);
							String avataRace = "";
							try {
								avataRace = cell.getStringCellValue().trim();
								skill.setAvataRace(avataRace);
							} catch (Exception ex) {
								
							}
							
							cell = row.getCell(无路径作用团队技能_avataSex_所在列);
							String avataSex = "";
							try {
								avataSex = cell.getStringCellValue().trim();
								skill.setAvataSex(avataSex);
							} catch (Exception ex) {
								
							}
							
							cell = row.getCell(无路径作用团队技能_effectType_所在列);
							try {
								String effectType = cell.getStringCellValue().trim();
								skill.setEffectType(effectType);
							} catch (Exception ex) {
								
							}
							
							cell = row.getCell(无路径作用团队技能_effectLastTime_所在列);
							try {
								int effectLastTime = (int)cell.getNumericCellValue();
								skill.setEffectLastTime(effectLastTime);
							} catch (Exception ex) {
								try{
									int effectLastTime = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setEffectLastTime(effectLastTime);
								}catch(Exception e){
									
								}
							}
							
							cell = row.getCell(无路径作用团队技能_effectExplosionLastTime_所在列);
							try {
								int effectExplosionLastTime = (int)cell.getNumericCellValue();
								skill.setEffectExplosionLastTime(effectExplosionLastTime);
							} catch (Exception ex) {
								try{
									int effectExplosionLastTime = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setEffectExplosionLastTime(effectExplosionLastTime);
								}catch(Exception e){
									
								}
							}
							
							cell = row.getCell(无路径作用团队技能_mp_所在列);
							String mps = "";
							try {
								mps = cell.getStringCellValue().trim();
								String[] mp = mps.split(";");
								if(mp.length == 1){
									mp = mps.split("；");
								}
								short[] efs = new short[skill.getMaxLevel()];
								for(int j = 0; j < mp.length && j < skill.getMaxLevel(); j++){
									efs[j] = Short.parseShort(mp[j]);
								}
								if(skill.getMaxLevel() > mp.length){
									for(int j = mp.length; j < skill.getMaxLevel(); j++){
										efs[j] = Short.parseShort(mp[mp.length - 1]);
									}
								}
								skill.setMp(efs);
							} catch (Exception ex) {
								short[] efs = new short[skill.getMaxLevel()];
								for(int j = 0; j < skill.getMaxLevel(); j++){
									efs[j] = (short)cell.getNumericCellValue();
								}
								skill.setMp(efs);
							}
							monsterSkillList.add(skill);
							if(Game.logger.isDebugEnabled()) {
								Game.logger.debug("[加载怪物技能] ["+skill.getId()+"] ["+skill.getName()+"]");
							}
						}
					}
				}else if (i == 无轨迹范围攻击SkillWithoutTraceAndWithMatrix技能_sheet) {
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							SkillWithoutTraceAndWithMatrix skill = new SkillWithoutTraceAndWithMatrix();
							所有技能的基本数据(skill, row);
							所有主动技能的数据(skill, row);
							HSSFCell cell = row.getCell(无轨迹范围攻击技能_matrixType_所在列);
							try {
								byte matrixType = (byte)cell.getNumericCellValue();
								skill.setMatrixType(matrixType);
							} catch (Exception ex) {
								try{
									byte matrixType = Byte.parseByte(cell.getStringCellValue().trim());
									skill.setMatrixType(matrixType);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							
							cell = row.getCell(无轨迹范围攻击技能_maxAttackNums_所在列);
							String maxAttackNumss = "";
							try {
								maxAttackNumss = cell.getStringCellValue().trim();
								String[] maxAttackNums = maxAttackNumss.split(";");
								if(maxAttackNums.length == 1){
									maxAttackNums = maxAttackNumss.split("；");
								}
								short[] efs = new short[skill.getMaxLevel()];
								for(int j = 0; j < maxAttackNums.length && j < skill.getMaxLevel(); j++){
									efs[j] = Short.parseShort(maxAttackNums[j]);
								}
								if(skill.getMaxLevel() > maxAttackNums.length){
									for(int j = maxAttackNums.length; j < skill.getMaxLevel(); j++){
										efs[j] = Short.parseShort(maxAttackNums[maxAttackNums.length - 1]);
									}
								}
								skill.setMaxAttackNums(efs);
							} catch (Exception ex) {
								short[] efs = new short[skill.getMaxLevel()];
								for(int j = 0; j < skill.getMaxLevel(); j++){
									efs[j] = (short)cell.getNumericCellValue();
								}
								skill.setMaxAttackNums(efs);
							}
							
							cell = row.getCell(无轨迹范围攻击技能_gapWidth_所在列);
							try {
								int gapWidth = (int)cell.getNumericCellValue();
								skill.setGapWidth(gapWidth);
							} catch (Exception ex) {
								try{
									int gapWidth = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setGapWidth(gapWidth);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							
							cell = row.getCell(无轨迹范围攻击技能_gapHeight_所在列);
							try {
								int gapHeight = (int)cell.getNumericCellValue();
								skill.setGapHeight(gapHeight);
							} catch (Exception ex) {
								try{
									int gapHeight = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setGapHeight(gapHeight);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							
							cell = row.getCell(无轨迹范围攻击技能_effectNum_所在列);
							try {
								int effectNum = (int)cell.getNumericCellValue();
								skill.setEffectNum(effectNum);
							} catch (Exception ex) {
								try{
									int effectNum = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setEffectNum(effectNum);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							cell = row.getCell(无轨迹范围攻击技能_avataRace_所在列);
							String avataRace = "";
							try {
								avataRace = cell.getStringCellValue().trim();
								skill.setAvataRace(avataRace);
							} catch (Exception ex) {
								
							}
							
							cell = row.getCell(无轨迹范围攻击技能_avataSex_所在列);
							String avataSex = "";
							try {
								avataSex = cell.getStringCellValue().trim();
								skill.setAvataSex(avataSex);
							} catch (Exception ex) {
								
							}
							
							cell = row.getCell(无轨迹范围攻击技能_effectType_所在列);
							try {
								String effectType = cell.getStringCellValue().trim();
								skill.setEffectType(effectType);
							} catch (Exception ex) {
								
							}
							
							cell = row.getCell(无轨迹范围攻击技能_effectLastTime_所在列);
							try {
								int effectLastTime = (int)cell.getNumericCellValue();
								skill.setEffectLastTime(effectLastTime);
							} catch (Exception ex) {
								try{
									int effectLastTime = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setEffectLastTime(effectLastTime);
								}catch(Exception e){
									
								}
							}
							
							cell = row.getCell(无轨迹范围攻击技能_effectExplosionLastTime_所在列);
							try {
								int effectExplosionLastTime = (int)cell.getNumericCellValue();
								skill.setEffectExplosionLastTime(effectExplosionLastTime);
							} catch (Exception ex) {
								try{
									int effectExplosionLastTime = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setEffectExplosionLastTime(effectExplosionLastTime);
								}catch(Exception e){
									
								}
							}
							
							cell = row.getCell(无轨迹范围攻击技能_mp_所在列);
							String mps = "";
							try {
								mps = cell.getStringCellValue().trim();
								String[] mp = mps.split(";");
								if(mp.length == 1){
									mp = mps.split("；");
								}
								short[] efs = new short[skill.getMaxLevel()];
								for(int j = 0; j < mp.length && j < skill.getMaxLevel(); j++){
									efs[j] = Short.parseShort(mp[j]);
								}
								if(skill.getMaxLevel() > mp.length){
									for(int j = mp.length; j < skill.getMaxLevel(); j++){
										efs[j] = Short.parseShort(mp[mp.length - 1]);
									}
								}
								skill.setMp(efs);
							} catch (Exception ex) {
								short[] efs = new short[skill.getMaxLevel()];
								for(int j = 0; j < skill.getMaxLevel(); j++){
									efs[j] = (short)cell.getNumericCellValue();
								}
								skill.setMp(efs);
							}
							
							cell = row.getCell(无轨迹范围攻击技能_range_所在列);
							try {
								int range = (int)cell.getNumericCellValue();
								skill.setRange(range);
							} catch (Exception ex) {
								try{
									int range = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setRange(range);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							monsterSkillList.add(skill);
							if(Game.logger.isDebugEnabled()) {
								Game.logger.debug("[加载怪物技能] ["+skill.getId()+"] ["+skill.getName()+"]");
							}
						}
					}
				}else if (i == 无轨迹范围攻击SkillWithoutTraceAndWithRange技能_sheet) {
					for (int r = 1; r < rows; r++) {
						Game.logger.warn("无轨迹范围攻击_sheet~~~~~~["+r+"]");
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							SkillWithoutTraceAndWithRange skill = new SkillWithoutTraceAndWithRange();
							所有技能的基本数据(skill, row);
							所有主动技能的数据(skill, row);
							HSSFCell cell = row.getCell(无轨迹范围Range攻击技能_rangeType_所在列);
							try {
								byte rangeType = (byte)cell.getNumericCellValue();
								skill.setRangeType(rangeType);
							} catch (Exception ex) {
								try{
									byte rangeType = Byte.parseByte(cell.getStringCellValue().trim());
									skill.setRangeType(rangeType);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							
							cell = row.getCell(无轨迹范围Range攻击技能_rangeWidth_所在列);
							try {
								int rangeWidth = (int)cell.getNumericCellValue();
								skill.setRangeWidth(rangeWidth);
							} catch (Exception ex) {
								try{
									int rangeWidth = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setRangeWidth(rangeWidth);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							
							cell = row.getCell(无轨迹范围Range攻击技能_rangeHeight_所在列);
							try {
								int rangeHeight = (int)cell.getNumericCellValue();
								skill.setRangeHeight(rangeHeight);
							} catch (Exception ex) {
								try{
									int rangeHeight = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setRangeHeight(rangeHeight);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							
							cell = row.getCell(无轨迹范围Range攻击技能_maxAttackNums_所在列);
							String maxAttackNumss = "";
							try {
								maxAttackNumss = cell.getStringCellValue().trim();
								String[] maxAttackNums = maxAttackNumss.split(";");
								if(maxAttackNums.length == 1){
									maxAttackNums = maxAttackNumss.split("；");
								}
								short[] efs = new short[skill.getMaxLevel()];
								for(int j = 0; j < maxAttackNums.length && j < skill.getMaxLevel(); j++){
									efs[j] = Short.parseShort(maxAttackNums[j]);
								}
								if(skill.getMaxLevel() > maxAttackNums.length){
									for(int j = maxAttackNums.length; j < skill.getMaxLevel(); j++){
										efs[j] = Short.parseShort(maxAttackNums[maxAttackNums.length - 1]);
									}
								}
								skill.setMaxAttackNums(efs);
							} catch (Exception ex) {
								short[] efs = new short[skill.getMaxLevel()];
								for(int j = 0; j < skill.getMaxLevel(); j++){
									efs[j] = (short)cell.getNumericCellValue();
								}
								skill.setMaxAttackNums(efs);
							}
							
							cell = row.getCell(无轨迹范围Range攻击技能_avataRace_所在列);
							String avataRace = "";
							try {
								avataRace = cell.getStringCellValue().trim();
								skill.setAvataRace(avataRace);
							} catch (Exception ex) {
								
							}
							
							cell = row.getCell(无轨迹范围Range攻击技能_avataSex_所在列);
							String avataSex = "";
							try {
								avataSex = cell.getStringCellValue().trim();
								skill.setAvataSex(avataSex);
							} catch (Exception ex) {
								
							}
							
							cell = row.getCell(无轨迹范围Range攻击技能_effectType_所在列);
							try {
								String effectType = cell.getStringCellValue().trim();
								skill.setEffectType(effectType);
							} catch (Exception ex) {
								
							}
							
							cell = row.getCell(无轨迹范围Range攻击技能_effectLastTime_所在列);
							try {
								int effectLastTime = (int)cell.getNumericCellValue();
								skill.setEffectLastTime(effectLastTime);
							} catch (Exception ex) {
								try{
									int effectLastTime = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setEffectLastTime(effectLastTime);
								}catch(Exception e){
									
								}
							}
							
							cell = row.getCell(无轨迹范围Range攻击技能_effectExplosionLastTime_所在列);
							try {
								int effectExplosionLastTime = (int)cell.getNumericCellValue();
								skill.setEffectExplosionLastTime(effectExplosionLastTime);
							} catch (Exception ex) {
								try{
									int effectExplosionLastTime = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setEffectExplosionLastTime(effectExplosionLastTime);
								}catch(Exception e){
									
								}
							}
							
							cell = row.getCell(无轨迹范围Range攻击技能_mp_所在列);
							String mps = "";
							try {
								mps = cell.getStringCellValue().trim();
								String[] mp = mps.split(";");
								if(mp.length == 1){
									mp = mps.split("；");
								}
								short[] efs = new short[skill.getMaxLevel()];
								for(int j = 0; j < mp.length && j < skill.getMaxLevel(); j++){
									efs[j] = Short.parseShort(mp[j]);
								}
								if(skill.getMaxLevel() > mp.length){
									for(int j = mp.length; j < skill.getMaxLevel(); j++){
										efs[j] = Short.parseShort(mp[mp.length - 1]);
									}
								}
								skill.setMp(efs);
							} catch (Exception ex) {
								short[] efs = new short[skill.getMaxLevel()];
								for(int j = 0; j < skill.getMaxLevel(); j++){
									efs[j] = (short)cell.getNumericCellValue();
								}
								skill.setMp(efs);
							}
							monsterSkillList.add(skill);
							if(Game.logger.isDebugEnabled()) {
								Game.logger.debug("[加载怪物技能] ["+skill.getId()+"] ["+skill.getName()+"]");
							}
						}
					}
				}else if (i == 无轨迹有目标攻击SkillWithoutTraceAndWithTargetOrPosition技能_sheet) {
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							SkillWithoutTraceAndWithTargetOrPosition skill = new SkillWithoutTraceAndWithTargetOrPosition();
							所有技能的基本数据(skill, row);
							所有主动技能的数据(skill, row);
							HSSFCell cell = row.getCell(无轨迹有目标攻击技能_avataRace_所在列);
							String avataRace = "";
							try {
								avataRace = cell.getStringCellValue().trim();
								skill.setAvataRace(avataRace);
							} catch (Exception ex) {
								
							}
							
							cell = row.getCell(无轨迹有目标攻击技能_avataSex_所在列);
							String avataSex = "";
							try {
								avataSex = cell.getStringCellValue().trim();
								skill.setAvataSex(avataSex);
							} catch (Exception ex) {
								
							}
							
							cell = row.getCell(无轨迹有目标攻击技能_effectType_所在列);
							try {
								String effectType = cell.getStringCellValue().trim();
								skill.setEffectType(effectType);
							} catch (Exception ex) {
								
							}
							
							cell = row.getCell(无轨迹有目标攻击技能_effectLastTime_所在列);
							try {
								int effectLastTime = (int)cell.getNumericCellValue();
								skill.setEffectLastTime(effectLastTime);
							} catch (Exception ex) {
								try{
									int effectLastTime = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setEffectLastTime(effectLastTime);
								}catch(Exception e){
									
								}
							}
							
							cell = row.getCell(无轨迹有目标攻击技能_effectExplosionLastTime_所在列);
							try {
								int effectExplosionLastTime = (int)cell.getNumericCellValue();
								skill.setEffectExplosionLastTime(effectExplosionLastTime);
							} catch (Exception ex) {
								try{
									int effectExplosionLastTime = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setEffectExplosionLastTime(effectExplosionLastTime);
								}catch(Exception e){
									
								}
							}
							
							cell = row.getCell(无轨迹有目标攻击技能_mp_所在列);
							String mps = "";
							try {
								mps = cell.getStringCellValue().trim();
								String[] mp = mps.split(";");
								if(mp.length == 1){
									mp = mps.split("；");
								}
								short[] efs = new short[skill.getMaxLevel()];
								for(int j = 0; j < mp.length && j < skill.getMaxLevel(); j++){
									efs[j] = Short.parseShort(mp[j]);
								}
								if(skill.getMaxLevel() > mp.length){
									for(int j = mp.length; j < skill.getMaxLevel(); j++){
										efs[j] = Short.parseShort(mp[mp.length - 1]);
									}
								}
								skill.setMp(efs);
							} catch (Exception ex) {
								short[] efs = new short[skill.getMaxLevel()];
								for(int j = 0; j < skill.getMaxLevel(); j++){
									efs[j] = (short)cell.getNumericCellValue();
								}
								skill.setMp(efs);
							}
							
							cell = row.getCell(无轨迹有目标攻击技能_range_所在列);
							try {
								int range = (int)cell.getNumericCellValue();
								skill.setRange(range);
							} catch (Exception ex) {
								try{
									int range = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setRange(range);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							monsterSkillList.add(skill);
							if(Game.logger.isDebugEnabled()) {
								Game.logger.debug("[加载怪物技能] ["+skill.getId()+"] ["+skill.getName()+"]");
							}
						}
					}
				}else if (i == 有轨迹的攻击指定目标或者方向的技能SkillWithTraceAndDirectionOrTarget技能_sheet) {
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							SkillWithTraceAndDirectionOrTarget skill = new SkillWithTraceAndDirectionOrTarget();
							所有技能的基本数据(skill, row);
							所有主动技能的数据(skill, row);
							HSSFCell cell = row.getCell(有轨迹的攻击指定目标或者方向的技能_targetType_所在列);
							try {
								byte targetType = (byte)cell.getNumericCellValue();
								skill.setTargetType(targetType);
							} catch (Exception ex) {
								try{
									byte targetType = Byte.parseByte(cell.getStringCellValue().trim());
									skill.setTargetType(targetType);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							
							cell = row.getCell(有轨迹的攻击指定目标或者方向的技能_avataRace_所在列);
							String avataRace = "";
							try {
								avataRace = cell.getStringCellValue().trim();
								skill.setAvataRace(avataRace);
							} catch (Exception ex) {
								
							}
							
							cell = row.getCell(有轨迹的攻击指定目标或者方向的技能_effectType_所在列);
							try {
								String effectType = cell.getStringCellValue().trim();
								skill.setEffectType(effectType);
							} catch (Exception ex) {
								
							}
							
							cell = row.getCell(有轨迹的攻击指定目标或者方向的技能_trackType_所在列);
							try {
								byte trackType = (byte)cell.getNumericCellValue();
								skill.setTrackType(trackType);
							} catch (Exception ex) {
								try{
									byte trackType = Byte.parseByte(cell.getStringCellValue().trim());
									skill.setTrackType(trackType);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							
							cell = row.getCell(有轨迹的攻击指定目标或者方向的技能_sendEffectTime_所在列);
							try {
								long sendEffectTime = (long)cell.getNumericCellValue();
								skill.setSendEffectTime(sendEffectTime);
							} catch (Exception ex) {
								try{
									long sendEffectTime = Long.parseLong(cell.getStringCellValue().trim());
									skill.setSendEffectTime(sendEffectTime);
								}catch(Exception e){
									
								}
							}
							
							cell = row.getCell(有轨迹的攻击指定目标或者方向的技能_speed_所在列);
							try {
								int speed = (int)cell.getNumericCellValue();
								skill.setSpeed(speed);
							} catch (Exception ex) {
								try{
									int speed = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setSpeed(speed);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							
							cell = row.getCell(有轨迹的攻击指定目标或者方向的技能_range_所在列);
							try {
								int range = (int)cell.getNumericCellValue();
								skill.setRange(range);
							} catch (Exception ex) {
								try{
									int range = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setRange(range);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							
							cell = row.getCell(有轨迹的攻击指定目标或者方向的技能_attackWidth_所在列);
							try {
								int attackWidth = (int)cell.getNumericCellValue();
								skill.setAttackWidth(attackWidth);
							} catch (Exception ex) {
								try{
									int attackWidth = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setAttackWidth(attackWidth);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							
							cell = row.getCell(有轨迹的攻击指定目标或者方向的技能_explosionLastingTime_所在列);
							try {
								int explosionLastingTime = (int)cell.getNumericCellValue();
								skill.setExplosionLastingTime(explosionLastingTime);
							} catch (Exception ex) {
								try{
									int explosionLastingTime = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setExplosionLastingTime(explosionLastingTime);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							
							cell = row.getCell(有轨迹的攻击指定目标或者方向的技能_effectNum_所在列);
							try {
								int effectNum = (int)cell.getNumericCellValue();
								skill.setEffectNum(effectNum);
							} catch (Exception ex) {
								try{
									int effectNum = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setEffectNum(effectNum);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							
							cell = row.getCell(有轨迹的攻击指定目标或者方向的技能_effectInitPositionX_所在列);
							String effectInitPositionXs = "";
							try {
								effectInitPositionXs = cell.getStringCellValue().trim();
								String[] effectInitPositionX = effectInitPositionXs.split(";");
								if(effectInitPositionX.length == 1){
									effectInitPositionX = effectInitPositionXs.split("；");
								}
								int[] efs = new int[effectInitPositionX.length];
								for(int ii = 0; ii < effectInitPositionX.length; ii++){
									efs[ii] = Integer.parseInt(effectInitPositionX[ii]);
								}
								skill.setEffectInitPositionX(efs);
							} catch (Exception ex) {
								skill.setEffectInitPositionX(new int[]{(int)cell.getNumericCellValue()});
							}
							
							cell = row.getCell(有轨迹的攻击指定目标或者方向的技能_effectInitPositionY_所在列);
							String effectInitPositionYs = "";
							try {
								effectInitPositionYs = cell.getStringCellValue().trim();
								String[] effectInitPositionY = effectInitPositionYs.split(";");
								if(effectInitPositionY.length == 1){
									effectInitPositionY = effectInitPositionYs.split("；");
								}
								int[] efs = new int[effectInitPositionY.length];
								for(int ii = 0; ii < effectInitPositionY.length; ii++){
									efs[ii] = Integer.parseInt(effectInitPositionY[ii]);
								}
								skill.setEffectInitPositionY(efs);
							} catch (Exception ex) {
								skill.setEffectInitPositionY(new int[]{(int)cell.getNumericCellValue()});
							}
							
							cell = row.getCell(有轨迹的攻击指定目标或者方向的技能_effectInitDirection_所在列);
							String effectInitDirections = "";
							try {
								effectInitDirections = cell.getStringCellValue().trim();
								String[] effectInitDirection = effectInitDirections.split(";");
								if(effectInitDirection.length == 1){
									effectInitDirection = effectInitDirections.split("；");
								}
								byte[] efs = new byte[effectInitDirection.length];
								for(int ii = 0; ii < effectInitDirection.length; ii++){
									efs[ii] = Byte.parseByte(effectInitDirection[ii]);
								}
								skill.setEffectInitDirection(efs);
							} catch (Exception ex) {
								skill.setEffectInitDirection(new byte[]{(byte)cell.getNumericCellValue()});
							}
							
							cell = row.getCell(有轨迹的攻击指定目标或者方向的技能_mp_所在列);
							String mps = "";
							try {
								mps = cell.getStringCellValue().trim();
								String[] mp = mps.split(";");
								if(mp.length == 1){
									mp = mps.split("；");
								}
								short[] efs = new short[skill.getMaxLevel()];
								for(int j = 0; j < mp.length && j < skill.getMaxLevel(); j++){
									efs[j] = Short.parseShort(mp[j]);
								}
								if(skill.getMaxLevel() > mp.length){
									for(int j = mp.length; j < skill.getMaxLevel(); j++){
										efs[j] = Short.parseShort(mp[mp.length - 1]);
									}
								}
								skill.setMp(efs);
							} catch (Exception ex) {
								short[] efs = new short[skill.getMaxLevel()];
								for(int j = 0; j < skill.getMaxLevel(); j++){
									efs[j] = (short)cell.getNumericCellValue();
								}
								skill.setMp(efs);
							}
							
							cell = row.getCell(有轨迹的攻击指定目标或者方向的技能_penetrateNum_所在列);
							try {
								int penetrateNum = (int)cell.getNumericCellValue();
								skill.setPenetrateNum(penetrateNum);
							} catch (Exception ex) {
								try{
									int penetrateNum = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setPenetrateNum(penetrateNum);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							
							cell = row.getCell(有轨迹的攻击指定目标或者方向的技能_penetrateNum_所在列+1);
							if (cell != null) {
								try {
									int penetrateNum = (int)cell.getNumericCellValue();
									skill.setSpecialSkillType(penetrateNum);
									disasterSkills = Arrays.copyOf(disasterSkills, disasterSkills.length + 1);
									disasterSkills[disasterSkills.length - 1] = skill.getId();
									Game.logger.warn("[加载金猴天灾技能] [" + skill.getId() + "," + skill.getName() + "] [" + skill.getSpecialSkillType() + "]");
								} catch (Exception ex) {
									try{
										int penetrateNum = Integer.parseInt(cell.getStringCellValue().trim());
										skill.setSpecialSkillType(penetrateNum);
										disasterSkills = Arrays.copyOf(disasterSkills, disasterSkills.length + 1);
										disasterSkills[disasterSkills.length - 1] = skill.getId();
										Game.logger.warn("[加载金猴天灾技能] [" + skill.getId() + "," + skill.getName() + "] [" + skill.getSpecialSkillType() + "]");
									}catch(Exception e){
										throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
									}
								}
							}
							
							monsterSkillList.add(skill);
							if(Game.logger.isDebugEnabled()) {
								Game.logger.debug("[加载怪物技能] ["+skill.getId()+"] ["+skill.getName()+"]");
							}
						}
					}
				}else if(i == 无轨迹召唤NPCSkillWithoutTraceAndWithSummonNPC技能_sheet){
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							SkillWithoutTraceAndWithSummonNPC skill = new SkillWithoutTraceAndWithSummonNPC();
							所有技能的基本数据(skill, row);
							所有主动技能的数据(skill, row);
							HSSFCell cell = row.getCell(召唤NPC技能_matrixType_所在列);
							try {
								byte matrixType = (byte)cell.getNumericCellValue();
								skill.setMatrixType(matrixType);
							} catch (Exception ex) {
								try{
									byte matrixType = Byte.parseByte(cell.getStringCellValue().trim());
									skill.setMatrixType(matrixType);
								}catch(Exception e){
									throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							
							cell = row.getCell(召唤NPC技能_attackNum_所在列);
							String attackNums = "";
							try {
								attackNums = cell.getStringCellValue().trim();
								String[] attackNum = attackNums.split(";");
								if(attackNum.length == 1){
									attackNum = attackNums.split("；");
								}
								short[] efs = new short[attackNum.length];
								for(int ii = 0; ii < attackNum.length; ii++){
									efs[ii] = Short.parseShort(attackNum[ii]);
								}
								skill.setAttackNum(efs);
							} catch (Exception ex) {
								skill.setAttackNum(new short[]{(short)cell.getNumericCellValue()});
							}
							
							cell = row.getCell(召唤NPC技能_gapWidth_所在列);
							try {
								int gapWidth = (int)cell.getNumericCellValue();
								skill.setGapWidth(gapWidth);
							} catch (Exception ex) {
								try{
									int gapWidth = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setGapWidth(gapWidth);
								}catch(Exception e){
									
								}
							}
							
							cell = row.getCell(召唤NPC技能_gapHeight_所在列);
							try {
								int gapHeight = (int)cell.getNumericCellValue();
								skill.setGapHeight(gapHeight);
							} catch (Exception ex) {
								try{
									int gapHeight = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setGapHeight(gapHeight);
								}catch(Exception e){
									
								}
							}
							
							cell = row.getCell(召唤NPC技能_maxTimeLength_所在列);
							try {
								long maxTimeLength = (long)cell.getNumericCellValue();
								skill.setMaxTimeLength(maxTimeLength);
							} catch (Exception ex) {
								try{
									long maxTimeLength = Long.parseLong(cell.getStringCellValue().trim());
									skill.setMaxTimeLength(maxTimeLength);
								}catch(Exception e){
									
								}
							}
							
							cell = row.getCell(召唤NPC技能_speed_所在列);
							try {
								short speed = (short)cell.getNumericCellValue();
								skill.setSpeed(speed);
							} catch (Exception ex) {
								try{
									short speed = Short.parseShort(cell.getStringCellValue().trim());
									skill.setSpeed(speed);
								}catch(Exception e){
									
								}
							}
							
							cell = row.getCell(召唤NPC技能_angle_所在列);
							try {
								short angle = (short)cell.getNumericCellValue();
								skill.setAngle(angle);
							} catch (Exception ex) {
								try{
									short angle = Short.parseShort(cell.getStringCellValue().trim());
									skill.setAngle(angle);
								}catch(Exception e){
									
								}
							}
							
							cell = row.getCell(召唤NPC技能_heigth_所在列);
							try {
								short heigth = (short)cell.getNumericCellValue();
								skill.setHeigth(heigth);
							} catch (Exception ex) {
								try{
									short heigth = Short.parseShort(cell.getStringCellValue().trim());
									skill.setHeigth(heigth);
								}catch(Exception e){
									
								}
							}
							
							cell = row.getCell(召唤NPC技能_maxParticleEachTime_所在列);
							try {
								short maxParticleEachTime = (short)cell.getNumericCellValue();
								skill.setMaxParticleEachTime(maxParticleEachTime);
							} catch (Exception ex) {
								try{
									short maxParticleEachTime = Short.parseShort(cell.getStringCellValue().trim());
									skill.setMaxParticleEachTime(maxParticleEachTime);
								}catch(Exception e){
									
								}
							}
							
							cell = row.getCell(召唤NPC技能_intervalTimeAttack_所在列);
							try {
								long intervalTimeAttack = (long)cell.getNumericCellValue();
								skill.setIntervalTimeAttack(intervalTimeAttack);
							} catch (Exception ex) {
								try{
									long intervalTimeAttack = Long.parseLong(cell.getStringCellValue().trim());
									skill.setIntervalTimeAttack(intervalTimeAttack);
								}catch(Exception e){
									
								}
							}
							
							cell = row.getCell(召唤NPC技能_range_所在列);
							try {
								int range = (int)cell.getNumericCellValue();
								skill.setRange(range);
							} catch (Exception ex) {
								try{
									int range = Integer.parseInt(cell.getStringCellValue().trim());
									skill.setRange(range);
								}catch(Exception e){
									
								}
							}
							
							cell = row.getCell(召唤NPC技能_mp_所在列);
							String mps = "";
							try {
								mps = cell.getStringCellValue().trim();
								String[] mp = mps.split(";");
								if(mp.length == 1){
									mp = mps.split("；");
								}
								short[] efs = new short[skill.getMaxLevel()];
								for(int j = 0; j < mp.length && j < skill.getMaxLevel(); j++){
									efs[j] = Short.parseShort(mp[j]);
								}
								if(skill.getMaxLevel() > mp.length){
									for(int j = mp.length; j < skill.getMaxLevel(); j++){
										efs[j] = Short.parseShort(mp[mp.length - 1]);
									}
								}
								skill.setMp(efs);
							} catch (Exception ex) {
								short[] efs = new short[skill.getMaxLevel()];
								for(int j = 0; j < skill.getMaxLevel(); j++){
									efs[j] = (short)cell.getNumericCellValue();
								}
								skill.setMp(efs);
							}
							
							cell = row.getCell(召唤NPC技能_可否对人造成伤害_所在列);
							try {
								boolean 可否对人造成伤害 = cell.getBooleanCellValue();
								skill.可以对玩家造成伤害 = 可否对人造成伤害;
							} catch (Exception ex) {
								
							}
							
							monsterSkillList.add(skill);
							if(Game.logger.isWarnEnabled()) {
								Game.logger.warn("[加载怪物技能] ["+skill.getId()+"] ["+skill.getName()+"]");
							}
						}
					}
				}else if (i == 被动增加暴击IncreaseBaoJiPassiveSkill技能_sheet) {
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							IncreaseBaoJiPassiveSkill skill = new IncreaseBaoJiPassiveSkill();
							所有技能的基本数据(skill, row);
							HSSFCell cell = row.getCell(被动增加暴击_baoJi_所在列);
							String values = "";
							try {
								values = cell.getStringCellValue().trim();
								String[] value = values.split(";");
								if(value.length == 1){
									value = values.split("；");
								}
								int[] efs = new int[skill.getMaxLevel()];
								for(int j = 0; j < value.length && j < skill.getMaxLevel(); j++){
									efs[j] = Integer.parseInt(value[j]);
								}
								if(skill.getMaxLevel() > value.length){
									for(int j = value.length; j < skill.getMaxLevel(); j++){
										efs[j] = Integer.parseInt(value[value.length - 1]);
									}
								}
								skill.setBaoJi(efs);
							} catch (Exception ex) {
								int[] efs = new int[skill.getMaxLevel()];
								for(int j = 0; j < skill.getMaxLevel(); j++){
									efs[j] = (int)cell.getNumericCellValue();
								}
								skill.setBaoJi(efs);
							}
							
							monsterSkillList.add(skill);
							if(Game.logger.isDebugEnabled()) {
								Game.logger.debug("[加载怪物技能] ["+skill.getId()+"] ["+skill.getName()+"]");
							}
						}
					}
				}else if (i == 被动增加闪避IncreaseDuoBiPassiveSkill技能_sheet) {
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							IncreaseDuoBiPassiveSkill skill = new IncreaseDuoBiPassiveSkill();
							所有技能的基本数据(skill, row);
							HSSFCell cell = row.getCell(被动增加闪避_dodge_所在列);
							String values = "";
							try {
								values = cell.getStringCellValue().trim();
								String[] value = values.split(";");
								if(value.length == 1){
									value = values.split("；");
								}
								int[] efs = new int[skill.getMaxLevel()];
								for(int j = 0; j < value.length && j < skill.getMaxLevel(); j++){
									efs[j] = Integer.parseInt(value[j]);
								}
								if(skill.getMaxLevel() > value.length){
									for(int j = value.length; j < skill.getMaxLevel(); j++){
										efs[j] = Integer.parseInt(value[value.length - 1]);
									}
								}
								skill.setDodge(efs);
							} catch (Exception ex) {
								int[] efs = new int[skill.getMaxLevel()];
								for(int j = 0; j < skill.getMaxLevel(); j++){
									efs[j] = (int)cell.getNumericCellValue();
								}
								skill.setDodge(efs);
							}
							
							monsterSkillList.add(skill);
							if(Game.logger.isDebugEnabled()) {
								Game.logger.debug("[加载怪物技能] ["+skill.getId()+"] ["+skill.getName()+"]");
							}
						}
					}
				}else if (i == 被动增加IncreaseHpPassiveSkill技能_sheet) {
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							IncreaseHpPassiveSkill skill = new IncreaseHpPassiveSkill();
							所有技能的基本数据(skill, row);
							HSSFCell cell = row.getCell(被动增加血_HP_所在列);
							String values = "";
							try {
								values = cell.getStringCellValue().trim();
								String[] value = values.split(";");
								if(value.length == 1){
									value = values.split("；");
								}
								int[] efs = new int[skill.getMaxLevel()];
								for(int j = 0; j < value.length && j < skill.getMaxLevel(); j++){
									efs[j] = Integer.parseInt(value[j]);
								}
								if(skill.getMaxLevel() > value.length){
									for(int j = value.length; j < skill.getMaxLevel(); j++){
										efs[j] = Integer.parseInt(value[value.length - 1]);
									}
								}
								skill.setTotalHpB(efs);
							} catch (Exception ex) {
								int[] efs = new int[skill.getMaxLevel()];
								for(int j = 0; j < skill.getMaxLevel(); j++){
									efs[j] = (int)cell.getNumericCellValue();
								}
								skill.setTotalHpB(efs);
							}
							
							monsterSkillList.add(skill);
							if(Game.logger.isDebugEnabled()) {
								Game.logger.debug("[加载怪物技能] ["+skill.getId()+"] ["+skill.getName()+"]");
							}
						}
					}
				}else if (i == 被动增加IncreaseWuGongPassiveSkill技能_sheet) {
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							IncreaseWuGongPassiveSkill skill = new IncreaseWuGongPassiveSkill();
							所有技能的基本数据(skill, row);
							HSSFCell cell = row.getCell(被动增加物攻_phyAttack_所在列);
							String values = "";
							try {
								values = cell.getStringCellValue().trim();
								String[] value = values.split(";");
								if(value.length == 1){
									value = values.split("；");
								}
								int[] efs = new int[skill.getMaxLevel()];
								for(int j = 0; j < value.length && j < skill.getMaxLevel(); j++){
									efs[j] = Integer.parseInt(value[j]);
								}
								if(skill.getMaxLevel() > value.length){
									for(int j = value.length; j < skill.getMaxLevel(); j++){
										efs[j] = Integer.parseInt(value[value.length - 1]);
									}
								}
								skill.setPhyAttackB(efs);
							} catch (Exception ex) {
								int[] efs = new int[skill.getMaxLevel()];
								for(int j = 0; j < skill.getMaxLevel(); j++){
									efs[j] = (int)cell.getNumericCellValue();
								}
								skill.setPhyAttackB(efs);
							}
							
							monsterSkillList.add(skill);
							if(Game.logger.isDebugEnabled()) {
								Game.logger.debug("[加载怪物技能] ["+skill.getId()+"] ["+skill.getName()+"]");
							}
						}
					}
				}else if (i == 被动增加IncreaseWuFangPassiveSkill技能_sheet) {
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							IncreaseWuFangPassiveSkill skill = new IncreaseWuFangPassiveSkill();
							所有技能的基本数据(skill, row);
							HSSFCell cell = row.getCell(被动增加物防_phyDef_所在列);
							String values = "";
							try {
								values = cell.getStringCellValue().trim();
								String[] value = values.split(";");
								if(value.length == 1){
									value = values.split("；");
								}
								int[] efs = new int[skill.getMaxLevel()];
								for(int j = 0; j < value.length && j < skill.getMaxLevel(); j++){
									efs[j] = Integer.parseInt(value[j]);
								}
								if(skill.getMaxLevel() > value.length){
									for(int j = value.length; j < skill.getMaxLevel(); j++){
										efs[j] = Integer.parseInt(value[value.length - 1]);
									}
								}
								skill.setPhysicalDefenceB(efs);
							}catch(NumberFormatException nfe){
								System.out.println("CareerManager.loadFrom() 技能表有错！！！！！！");
								nfe.printStackTrace(System.out);
								nfe.printStackTrace(System.err);
								System.exit(1);
							} catch (Exception ex) {
								ex.printStackTrace(System.out);
								int[] efs = new int[skill.getMaxLevel()];
								for(int j = 0; j < skill.getMaxLevel(); j++){
									efs[j] = (int)cell.getNumericCellValue();
								}
								skill.setPhysicalDefenceB(efs);
							}
							
							monsterSkillList.add(skill);
							if(Game.logger.isDebugEnabled()) {
								Game.logger.debug("[加载怪物技能] ["+skill.getId()+"] ["+skill.getName()+"]");
							}
						}
					}
				}else if (i == 被动增加IncreaseFaGongPassiveSkill技能_sheet) {
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							IncreaseFaGongPassiveSkill skill = new IncreaseFaGongPassiveSkill();
							所有技能的基本数据(skill, row);
							HSSFCell cell = row.getCell(被动增加法攻_magicAttack_所在列);
							String values = "";
							try {
								values = cell.getStringCellValue().trim();
								String[] value = values.split(";");
								if(value.length == 1){
									value = values.split("；");
								}
								int[] efs = new int[skill.getMaxLevel()];
								for(int j = 0; j < value.length && j < skill.getMaxLevel(); j++){
									efs[j] = Integer.parseInt(value[j]);
								}
								if(skill.getMaxLevel() > value.length){
									for(int j = value.length; j < skill.getMaxLevel(); j++){
										efs[j] = Integer.parseInt(value[value.length - 1]);
									}
								}
								skill.setMagicAttackB(efs);
							} catch (Exception ex) {
								int[] efs = new int[skill.getMaxLevel()];
								for(int j = 0; j < skill.getMaxLevel(); j++){
									efs[j] = (int)cell.getNumericCellValue();
								}
								skill.setMagicAttackB(efs);
							}
							
							monsterSkillList.add(skill);
							if(Game.logger.isDebugEnabled()) {
								Game.logger.debug("[加载怪物技能] ["+skill.getId()+"] ["+skill.getName()+"]");
							}
						}
					}
				}else if (i == 被动增加IncreaseFaFangPassiveSkill技能_sheet) {
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							IncreaseFaFangPassiveSkill skill = new IncreaseFaFangPassiveSkill();
							所有技能的基本数据(skill, row);
							HSSFCell cell = row.getCell(被动增加法防_magicDef_所在列);
							String values = "";
							try {
								values = cell.getStringCellValue().trim();
								String[] value = values.split(";");
								if(value.length == 1){
									value = values.split("；");
								}
								int[] efs = new int[skill.getMaxLevel()];
								for(int j = 0; j < value.length && j < skill.getMaxLevel(); j++){
									efs[j] = Integer.parseInt(value[j]);
								}
								if(skill.getMaxLevel() > value.length){
									for(int j = value.length; j < skill.getMaxLevel(); j++){
										efs[j] = Integer.parseInt(value[value.length - 1]);
									}
								}
								skill.setMagicDefB(efs);
							} catch (Exception ex) {
								int[] efs = new int[skill.getMaxLevel()];
								for(int j = 0; j < skill.getMaxLevel(); j++){
									efs[j] = (int)cell.getNumericCellValue();
								}
								skill.setMagicDefB(efs);
							}
							
							monsterSkillList.add(skill);
							if(Game.logger.isDebugEnabled()) {
								Game.logger.debug("[加载怪物技能] ["+skill.getId()+"] ["+skill.getName()+"]");
							}
						}
					}
				}else if (i == 被动增加IncreaseTeShuShuXingPassiveSkill技能_sheet) {
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							IncreaseTeShuShuXingPassiveSkill skill = new IncreaseTeShuShuXingPassiveSkill();
							所有技能的基本数据(skill, row);
							try{
								HSSFCell cell = row.getCell(被动增加属性类型_attributeType_所在列);
								String values = "";
								values = cell.getStringCellValue().trim();
								skill.setAttributeType(Integer.parseInt(values));
							}catch(Exception e) {
								System.out.println("新心法技能1出错" + r + " " + e);
							}
							try {
								HSSFCell cell = row.getCell(被动增加属性_addAttribute_所在列);
								String values = cell.getStringCellValue().trim();
								String[] value = values.split(";");
								if(value.length == 1){
									value = values.split("；");
								}
								int[] addvalue = new int[value.length];
								for(int j = 0; j < value.length && j < skill.getMaxLevel(); j++){
									addvalue[j] = Integer.parseInt(value[j]);
								}
								if(skill.getMaxLevel() > value.length){
									for(int j = value.length; j < skill.getMaxLevel(); j++){
										addvalue[j] = Integer.parseInt(value[value.length - 1]);
									}
								}
								skill.setAddAttribute(addvalue);
							}catch (Exception e) {
								System.out.println("新心法技能2出错" + e);
							}
							monsterSkillList.add(skill);
							if(Game.logger.isDebugEnabled()) {
								Game.logger.debug("[加载怪物技能] ["+skill.getId()+"] ["+skill.getName()+"]");
							}
						}
					}
				} else if (i == 仙界技能_sheet) {
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							PassiveTriggerImmune skill = new PassiveTriggerImmune();
							所有技能的基本数据(skill, row);
							skill.setProbabbly(ReadFileTool.getIntArrByString(row, (基本攻击技能_colIndex_所在列+1), Skill.logger, ","));
							skill.setBuffName(ReadFileTool.getString(row, 基本攻击技能_colIndex_所在列+2, Skill.logger));
							String tempLevel = ReadFileTool.getString(row, 基本攻击技能_colIndex_所在列+3, Skill.logger);
							if (tempLevel.split(",").length > 1) {
								skill.setBuffLevel(ReadFileTool.getIntArrByString(row, 基本攻击技能_colIndex_所在列+3, Skill.logger, ","));
							} else {
								int[] tt = new int[skill.getProbabbly().length];
								for (int kk=0; kk<tt.length; kk++) {
									tt[kk] = ReadFileTool.getInt(row, 基本攻击技能_colIndex_所在列+3, Skill.logger);
								}
								skill.setBuffLevel(tt);
							}
							String tempTime = ReadFileTool.getString(row, 基本攻击技能_colIndex_所在列+4, Skill.logger);
							if (tempTime.split(",").length > 1) {
								skill.setBuffLevel(ReadFileTool.getIntArrByString(row, 基本攻击技能_colIndex_所在列+4, Skill.logger, ","));
							} else {
								int[] tt = new int[skill.getProbabbly().length];
								for (int kk=0; kk<tt.length; kk++) {
									tt[kk] = ReadFileTool.getInt(row, 基本攻击技能_colIndex_所在列+4, Skill.logger);
								}
								skill.setLastTime(tt);
							}
							String tempcd = ReadFileTool.getString(row, 基本攻击技能_colIndex_所在列+5, Skill.logger);
							if (tempcd.split(",").length > 1) {
								skill.setBuffLevel(ReadFileTool.getIntArrByString(row, 基本攻击技能_colIndex_所在列+5, Skill.logger, ","));
							} else {
								int[] tt = new int[skill.getProbabbly().length];
								for (int kk=0; kk<tt.length; kk++) {
									tt[kk] = ReadFileTool.getInt(row, 基本攻击技能_colIndex_所在列+5, Skill.logger);
								}
								skill.setCds(tt);
							}
							passiveTriSkills.add(skill);
							
						}
					}
				} else if(i == 多坐标配置技能_sheet){
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							SkillWithoutMorePoints skill = new SkillWithoutMorePoints();
							所有技能的基本数据(skill, row);
							所有主动技能的数据(skill, row);
							HSSFCell cell = row.getCell(多坐标技能召唤数量_所在列);
							try {
								int matrixType = (int)cell.getNumericCellValue();
								skill.npcNums = matrixType;
							} catch (Exception ex) {
								try{
									int matrixType = Integer.parseInt(cell.getStringCellValue().trim());
									skill.npcNums = matrixType;
								}catch(Exception e){
									e.printStackTrace();
									throw new Exception("多坐标技能召唤数量_所在列row:"+row.getRowNum()+"--e:"+e);	
								}
							}
							
							cell = row.getCell(多坐标技能坐标点_所在列);
							String attackNums = "";
							try {
								attackNums = cell.getStringCellValue().trim();
								if(attackNums!=null){
									String[] attackNum = attackNums.split(";");
									if(attackNum!=null){
										int[][] efs = new int[attackNum.length][];
										for(int ii = 0; ii < attackNum.length; ii++){
											String str = attackNum[ii];
											if(str!=null){
												String [] xys = str.split(",");
												int points[] = new int[xys.length];
												for(int j=0;j<xys.length;j++){
													if(xys[j]!=null && xys[j].length()>0){
														points[j] = Integer.parseInt(xys[j]);
													}
												}
												efs[ii] = points;
											}else{
												System.out.println("str为空");
											}
										}
										skill.points = efs;
									}else{
										System.out.println("attackNum为空");
									}
								}else{
									System.out.println("attackNums为空");
								}
								
							} catch (Exception ex) {
								ex.printStackTrace();
								throw new Exception("多坐标技能召唤数量_所在列pointsrow:"+row.getRowNum()+"--e:"+ex);	
							}
							
							monsterSkillList.add(skill);
							if(Game.logger.isWarnEnabled()) {
								Game.logger.warn("[加载怪物技能] [多坐标技能] [npcNums:"+skill.npcNums+"] ["+(skill.points==null?"null":Arrays.toString(skill.points))+"] ["+skill.getId()+"] ["+skill.getName()+"]");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Game.logger.error("111出错:", e);
			e.printStackTrace();
			throw new Exception(e);
		}
		Game.logger.warn("111111111~~~~~~~~~ok");
		try {
			
			skillMapForInteger.clear();
			skillMapForString.clear();
			
			
			//先把技能解析完成再解析职业
			for(Skill s : commonAttackSkillList){
				skillMapForInteger.put(s.getId(), s);
				skillMapForString.put(s.getName(),s);
			}
			
			for(Skill s : monsterSkillList){
				skillMapForInteger.put(s.getId(), s);
				skillMapForString.put(s.getName(),s);
			}
			
			for (Skill s : passiveTriSkills) {
				skillMapForInteger.put(s.getId(), s);
				skillMapForString.put(s.getName(),s);
			}
			
			
			HSSFSheet sheet = workbook.getSheetAt(职业_sheet);
			int rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				if (row != null) {
					Career career = new Career();
					HSSFCell cell = row.getCell(职业_id_所在列);
					try {
						int id = (int)cell.getNumericCellValue();
						career.setId(id);
					} catch (Exception ex) {
						try{
							int id = Integer.parseInt(cell.getStringCellValue().trim());
							career.setId(id);
						}catch(Exception e){
							throw new Exception("row:"+row.getRowNum()+"--e:"+e);
						}
					}
					
					cell = row.getCell(职业_name_所在列);
					String name = "";
					try {
						name = cell.getStringCellValue().trim();
						career.setName(name);
					} catch (Exception ex) {
						throw new Exception("row:"+row.getRowNum()+"--e:"+ex);
					}
					
					cell = row.getCell(职业_weaponTypeLimit_所在列);
					String weaponTypeLimits = "";
					try {
						weaponTypeLimits = cell.getStringCellValue().trim();
						String[] weaponTypeLimit = weaponTypeLimits.split(";");
						if(weaponTypeLimit.length == 1){
							weaponTypeLimit = weaponTypeLimits.split("；");
						}
						int[] wtl = new int[weaponTypeLimit.length];
						for(int ii = 0; ii < weaponTypeLimit.length; ii++){
							wtl[ii] = Integer.parseInt(weaponTypeLimit[ii]);
						}
						career.setWeaponTypeLimit(wtl);
					} catch (Exception ex) {
						career.setWeaponTypeLimit(new int[]{(int)cell.getNumericCellValue()});
					}
					
					cell = row.getCell(职业_enableMale_所在列);
					try {
						boolean enableMale = cell.getBooleanCellValue();
						career.setEnableMale(enableMale);
					} catch (Exception ex) {
						
					}
					
					cell = row.getCell(职业_enableFemale_所在列);
					try {
						boolean enableFemale = cell.getBooleanCellValue();
						career.setEnableFemale(enableFemale);
					} catch (Exception ex) {
						
					}
					
					cell = row.getCell(职业_basicSkills_所在列);
					String basicSkillss = "";
					try {
						basicSkillss = cell.getStringCellValue().trim();
						Game.logger.error("基础技能~~~~~~~~~~~~["+basicSkillss+"]");
						String[] basicSkills = basicSkillss.split(";");
						if(basicSkills.length == 1){
							basicSkills = basicSkillss.split("；");
						}
						int[] bss = new int[basicSkills.length];
						for(int ii = 0; ii < basicSkills.length; ii++){
							bss[ii] = Integer.parseInt(basicSkills[ii]);
						}
						Skill[] skills = new Skill[bss.length];
						for(int ii = 0; ii < bss.length; ii++){
							if(skillMapForInteger.get(bss[ii]) == null){
								Game.logger.error("["+r+"] ["+bss[ii]+"]");
							}
							skills[ii] = skillMapForInteger.get(bss[ii]);
							if(Game.logger.isDebugEnabled()) {
								Game.logger.debug("[给职业配置普通技能] ["+career.getName()+"] ["+skills[ii].getId()+"] ["+skills[ii].getName()+"]");
							}
						}
						career.setBasicSkills(skills);
					} catch (Exception ex) {
						int skillId = (int)cell.getNumericCellValue();
						if(skillMapForInteger.get(skillId) == null){
							throw new Exception("id为"+skillId+"的技能不存在");
						}
						career.setBasicSkills(new Skill[]{skillMapForInteger.get(skillId)});
					}
					
					cell = row.getCell(职业_nuqiSkills_所在列);
					String nuqiSkillss = "";
					try {
						nuqiSkillss = cell.getStringCellValue().trim();
						Game.logger.error("怒气技能~~~~~~~~~~~~["+nuqiSkillss+"]");
						String[] nuqiSkills = nuqiSkillss.split(";");
						if(nuqiSkills.length == 1){
							nuqiSkills = nuqiSkillss.split("；");
						}
						int[] bss = new int[nuqiSkills.length];
						for(int ii = 0; ii < nuqiSkills.length; ii++){
							bss[ii] = Integer.parseInt(nuqiSkills[ii]);
						}
						Skill[] skills = new Skill[bss.length];
						for(int ii = 0; ii < bss.length; ii++){
							skills[ii] = skillMapForInteger.get(bss[ii]);
							if(skillMapForInteger.get(bss[ii]) == null){
								throw new Exception("id为"+bss[ii]+"的技能不存在");
							}
							if(skills[ii] instanceof ActiveSkill){
								((ActiveSkill)skills[ii]).setNuqiFlag(true);
							}
							if(Game.logger.isDebugEnabled()) {
								Game.logger.debug("[给职业配置怒气技能] ["+career.getName()+"] ["+skills[ii].getId()+"] ["+skills[ii].getName()+"]");
							}
						}
						career.setNuqiSkills(skills);
					} catch (Exception ex) {
						int skillId = 0;
						try {
							skillId = (int)cell.getNumericCellValue();
						} catch (Exception e) {
							skillId = Integer.valueOf(cell.getStringCellValue());
						}
						if(skillMapForInteger.get(skillId) == null){
							throw new Exception("id为"+skillId+"的技能不存在");
						}
						if(skillMapForInteger.get(skillId) instanceof ActiveSkill){
							((ActiveSkill)skillMapForInteger.get(skillId)).setNuqiFlag(true);
						}
						career.setNuqiSkills(new Skill[]{skillMapForInteger.get(skillId)});
					}
					int bianshenBtn = ReadFileTool.getInt(row, 职业_变身按钮_所在列, Game.logger);
					if (bianshenBtn > 0) {
						if(skillMapForInteger.get(bianshenBtn) == null){
							throw new Exception("id为"+bianshenBtn+"的技能不存在");
						}
						if(skillMapForInteger.get(bianshenBtn) instanceof ActiveSkill){
							((ActiveSkill)skillMapForInteger.get(bianshenBtn)).setBianshenBtn(true);
						}
					}
					int[] needDou = ReadFileTool.getIntArrByString(row, 职业_需要消耗或增加豆技能_所在列, Game.logger, ";");
					int[] douNums = ReadFileTool.getIntArrByString(row, 职业_对应消耗或增加豆数量_所在列, Game.logger, ";");
					if (needDou.length != douNums.length) {
						throw new Exception("["+needDou+"] ["+douNums+"] [需要消耗豆的技能与消耗个数不匹配]");
					}
					if (needDou.length > 0) {
						for (int kk=0; kk<needDou.length; kk++) {
							if(skillMapForInteger.get(needDou[kk]) == null){
								throw new Exception("id为"+needDou[kk]+"的技能不存在");
							}
							if(skillMapForInteger.get(needDou[kk]) instanceof ActiveSkill){
								((ActiveSkill)skillMapForInteger.get(needDou[kk])).setDouFlag(true);
								((ActiveSkill)skillMapForInteger.get(needDou[kk])).setPointNum(douNums[kk]);
							}
						}
					}
					int[] specialStatuIds = ReadFileTool.getIntArrByString(row, 职业_根据状态触发效果_所在列, Game.logger, ";");
					byte[] needStatus = ReadFileTool.getByteArrByString(row, 职业_对应需要玩家所处状态_所在列, Game.logger, ";");
					int[] trigerType = ReadFileTool.getIntArrByString(row, 职业_触发类型_所在列, Game.logger, ";");
					String[] trigerResult = ReadFileTool.getStringArr(row, 职业_具体效果_所在列, Game.logger, ";");
					int[] shoukuiCommenSkillIds = ReadFileTool.getIntArrByString(row, 职业_兽魁普通攻击技能_所在列, Game.logger, ";");
					if (shoukuiCommenSkillIds != null &&  shoukuiCommenSkillIds.length > 0) {
						for (int kk=0; kk<shoukuiCommenSkillIds.length; kk++) {
							if(skillMapForInteger.get(shoukuiCommenSkillIds[kk]) == null){
								throw new Exception("id为"+shoukuiCommenSkillIds[kk]+"的技能不存在");
							}
							Skill sk1 = skillMapForInteger.get(shoukuiCommenSkillIds[kk]);
							if (sk1 instanceof CommonAttackSkill && ((CommonAttackSkill) sk1).getWeaponTypeLimit() == 5) {
								CareerManager.shoukuiCommenSkillIds[kk] = sk1.getId();
							}
						}
					}
					if (specialStatuIds.length != needStatus.length || needStatus.length != trigerType.length || trigerType.length != trigerResult.length) {
						throw new Exception("["+specialStatuIds+"] ["+needStatus+"] ["+trigerType+"] ["+trigerResult+"] [触发特殊效果技能格式错误]");
					}
					if (specialStatuIds.length > 0) {
						for (int kk=0; kk<specialStatuIds.length; kk++) {
							if(skillMapForInteger.get(specialStatuIds[kk]) == null){
								throw new Exception("id为"+specialStatuIds[kk]+"的技能不存在");
							}
							if(skillMapForInteger.get(specialStatuIds[kk]) instanceof ActiveSkill){
								((ActiveSkill)skillMapForInteger.get(specialStatuIds[kk])).setSpecialSkillFlag(true);
								((ActiveSkill)skillMapForInteger.get(specialStatuIds[kk])).setNeedStatus(needStatus[kk]);
								if (trigerType[kk] == 触发类型_百分比增加技能伤害) {
									((ActiveSkill)skillMapForInteger.get(specialStatuIds[kk])).setExtraDmgRate(Long.parseLong(trigerResult[kk]));
								} else if (trigerType[kk] == 触发类型_传播buff) {
									((ActiveSkill)skillMapForInteger.get(specialStatuIds[kk])).setSpreadBuffName(trigerResult[kk]);
								} else if (trigerType[kk] == 触发类型_技能造成伤害吸血) {
									((ActiveSkill)skillMapForInteger.get(specialStatuIds[kk])).setRecoverHpRate(Long.parseLong(trigerResult[kk]));
								}
							}
						}
					}
					
					
					cell = row.getCell(职业_xinfaSkills_所在列);
					String xinfaSkillss = "";
					try {
						xinfaSkillss = cell.getStringCellValue().trim();
						String[] xinfaSkills = xinfaSkillss.split(";");
						if(xinfaSkills.length == 1){
							xinfaSkills = xinfaSkillss.split("；");
						}
						int[] bss = new int[xinfaSkills.length];
						for(int ii = 0; ii < xinfaSkills.length; ii++){
							bss[ii] = Integer.parseInt(xinfaSkills[ii]);
						}
						Skill[] skills = new Skill[bss.length];
						for(int ii = 0; ii < bss.length; ii++){
							if(skillMapForInteger.get(bss[ii]) == null){
								throw new Exception("id为"+bss[ii]+"的技能不存在");
							}
							skills[ii] = skillMapForInteger.get(bss[ii]);
							if(Game.logger.isDebugEnabled()) {
								Game.logger.debug("[给职业配置心法技能] ["+career.getName()+"] ["+skills[ii].getId()+"] ["+skills[ii].getName()+"]");
							}
						}
						career.setXinfaSkills(skills);
					} catch (Exception ex) {						
						ex.printStackTrace();
						int skillId = (int)cell.getNumericCellValue();
						if(skillMapForInteger.get(skillId) == null){
							throw new Exception("id为"+skillId+"的技能不存在");
						}
						career.setXinfaSkills(new Skill[]{skillMapForInteger.get(skillId)});
					}
					
					cell = row.getCell(职业_kingSkills_所在列);
					String kingSkills = "";
					try {
						kingSkills = cell.getStringCellValue().trim();
						String[] xinfaSkills = kingSkills.split(";");
						if(xinfaSkills.length == 1){
							xinfaSkills = kingSkills.split("；");
						}
						int[] bss = new int[xinfaSkills.length];
						for(int ii = 0; ii < xinfaSkills.length; ii++){
							bss[ii] = Integer.parseInt(xinfaSkills[ii]);
						}
						Skill[] skills = new Skill[bss.length];
						for(int ii = 0; ii < bss.length; ii++){
							if(skillMapForInteger.get(bss[ii]) == null){
								throw new Exception("id为"+bss[ii]+"的技能不存在");
							}
							skills[ii] = skillMapForInteger.get(bss[ii]);
							if(Game.logger.isDebugEnabled()) {
								Game.logger.debug("[给职业配置国王技能] ["+career.getName()+"] ["+skills[ii].getId()+"] ["+skills[ii].getName()+"]");
							}
						}
						career.setKingSkills(skills);
					} catch (Exception ex) {
						int skillId = (int)cell.getNumericCellValue();
						if(skillMapForInteger.get(skillId) == null){
							throw new Exception("id为"+skillId+"的技能不存在");
						}
						career.setKingSkills(new Skill[]{skillMapForInteger.get(skillId)});
					}
					
					CareerThread ct1 = new CareerThread();
					cell = row.getCell(职业_职业发展线路1name_所在列);
					String name1 = "";
					try {
						name1 = (cell.getStringCellValue().trim());
						ct1.setName(name1);
					} catch (Exception ex) {
						
					}
					
					cell = row.getCell(职业_description1_所在列);
					String description1 = "";
					try {
						description1 = cell.getStringCellValue().trim();
						ct1.setDescription(description1);
					} catch (Exception ex) {
						
					}
					
					cell = row.getCell(职业_skills1_所在列);
					String skills1s = "";
					try {
						skills1s = cell.getStringCellValue().trim();
						String[] skills1 = skills1s.split(";");
						if(skills1.length == 1){
							skills1 = skills1s.split("；");
						}
						int[] bss = new int[skills1.length];
						for(int ii = 0; ii < skills1.length; ii++){
							bss[ii] = Integer.parseInt(skills1[ii]);
						}
						Skill[] skills = new Skill[bss.length];
						for(int ii = 0; ii < bss.length; ii++){
							skills[ii] = skillMapForInteger.get(bss[ii]);
							skills[ii].pageIndex = ii;
						}
						ct1.setSkills(skills);
					} catch (Exception ex) {
						int skillId = (int)cell.getNumericCellValue();
						ct1.setSkills(new Skill[]{skillMapForInteger.get(skillId)});
					}
					
					cell = row.getCell(职业_变身技能_所在列);
					if(cell != null){
						try{
							String skillStr = cell.getStringCellValue().trim();
							String[] skills1 = skillStr.split(";");
							if(skills1.length == 1){
								skills1 = skillStr.split("；");
							}
							int[] bss = new int[skills1.length];
							for(int ii = 0; ii < skills1.length; ii++){
								bss[ii] = Integer.parseInt(skills1[ii]);
							}
							Skill[] skills = new Skill[bss.length];
							for(int ii = 0; ii < bss.length; ii++){
								skills[ii] = skillMapForInteger.get(bss[ii]);
								skills[ii].pageIndex = -1;
								if (skills[ii] instanceof ActiveSkill) {
									((ActiveSkill)skills[ii]).setBianshenSkill(true);
								}
							}
							career.setBianShenSkills(skills);
							
						}catch(Exception e){
							/*int skillId = (int)cell.getNumericCellValue();
							if(skillMapForInteger.get(skillId) == null){
								throw new Exception("id为"+skillId+"的技能不存在");
							}
							career.setBianShenSkills(new Skill[]{skillMapForInteger.get(skillId)});*/
						}
					}
					
					career.setThreads(new CareerThread[]{ct1});
					careerList.add(career);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			Game.logger.error("2222出错L~~~~~~~~~", e);
			throw new Exception(e);
		}
		Game.logger.warn("22222222222222---------------ok");
		if (is != null) {
			is.close();
		}
		

		this.careers = new Career[careerList.size()];
		for(int i = 0 ; i < careerList.size() ; i++){
			Career c = careerList.get(i);
			careers[c.getId()] = c;
		}

		this.commonAttackSkills = commonAttackSkillList.toArray(new CommonAttackSkill[0]);
		this.monsterSkills = monsterSkillList.toArray(new Skill[0]);
		
		System.out.println("[初始化职业技能数据] [普通技能："+commonAttackSkills.length+"] [怪物技能："+monsterSkills.length+"]");

	}
	
	public void 所有技能的基本数据(Skill skill,HSSFRow row) throws Exception{
		HSSFCell cell = row.getCell(基本攻击技能_id_所在列);
		try {
			int id = (int)cell.getNumericCellValue();
			skill.setId(id);
		} catch (Exception ex) {
			try{
				int id = Integer.parseInt(cell.getStringCellValue().trim());
				skill.setId(id);
			}catch(Exception e){
				throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
			}
			
		}

		cell = row.getCell(基本攻击技能_name_所在列);
		String name = "";
		try {
			name = cell.getStringCellValue().trim();
			skill.setName(name);
		} catch (Exception ex) {
			
		}

		cell = row.getCell(基本攻击技能_iconId_所在列);
		String iconId = "";
		try {
			iconId = cell.getStringCellValue().trim();
			skill.setIconId(iconId);
		} catch (Exception ex) {
			
		}
		

		cell = row.getCell(基本攻击技能_description_所在列);
		String description = "";
		try {
			description = (cell.getStringCellValue().trim());
			skill.setDescription(description);
		} catch (Exception ex) {
			
		}
		

		cell = row.getCell(基本攻击技能_shortDescription_所在列);
		String shortDescription = "";
		try {
			shortDescription = (cell.getStringCellValue().trim());
			skill.setShortDescription(shortDescription);
		} catch (Exception ex) {
			
		}
		

		cell = row.getCell(基本攻击技能_maxLevel_所在列);
		byte maxLevel = 0;
		try {
			maxLevel = (byte)cell.getNumericCellValue();
			skill.setMaxLevel(maxLevel);
		} catch (Exception ex) {
			try{
			maxLevel = Byte.parseByte(cell.getStringCellValue().trim());
			skill.setMaxLevel(maxLevel);
			}catch(Exception e){
				throw new Exception("row:"+row.getRowNum()+"--e:"+e);
			}
		}
		

		cell = row.getCell(基本攻击技能_needMoney_所在列);
		String needMoneys = "";
		try {
			needMoneys = cell.getStringCellValue().trim();
			String[] needMoney = needMoneys.split(";");
			if(needMoney.length == 1){
				needMoney = needMoneys.split("；");
			}
			int[] efs = new int[maxLevel];
			for(int i = 0; i < needMoney.length && i < maxLevel; i++){
				efs[i] = Integer.parseInt(needMoney[i]);
			}
			if(maxLevel > needMoney.length){
				for(int i = needMoney.length; i < maxLevel; i++){
					efs[i] = Integer.parseInt(needMoney[needMoney.length - 1]);
				}
			}
			skill.setNeedMoney(efs);
		} catch (Exception ex) {
			int[] efs = new int[maxLevel];
			for(int i = 0; i < maxLevel; i++){
				efs[i] = (int)cell.getNumericCellValue();
			}
			skill.setNeedMoney(efs);
		}

		cell = row.getCell(基本攻击技能_needYuanQi_所在列);
		String needYuanQis = "";
		try {
			needYuanQis = cell.getStringCellValue().trim();
			String[] needYuanQi = needYuanQis.split(";");
			if(needYuanQi.length == 1){
				needYuanQi = needYuanQis.split("；");
			}
			int[] efs = new int[maxLevel];
			for(int i = 0; i < needYuanQi.length && i < maxLevel; i++){
				efs[i] = Integer.parseInt(needYuanQi[i]);
			}
			if(maxLevel > needYuanQi.length){
				for(int i = needYuanQi.length; i < maxLevel; i++){
					efs[i] = Integer.parseInt(needYuanQi[needYuanQi.length - 1]);
				}
			}
			skill.setNeedYuanQi(efs);
		} catch (Exception ex) {
			int[] efs = new int[maxLevel];
			for(int i = 0; i < maxLevel; i++){
				efs[i] = (int)cell.getNumericCellValue();
			}
			skill.setNeedYuanQi(efs);
		}

		cell = row.getCell(基本攻击技能_needExp_所在列);
		String needExps = "";
		try {
			needExps = cell.getStringCellValue().trim();
			String[] needExp = needExps.split(";");
			if(needExp.length == 1){
				needExp = needExps.split("；");
			}
			long[] efs = new long[maxLevel];
			for(int i = 0; i < needExp.length && i < maxLevel; i++){
				efs[i] = Long.parseLong(needExp[i]);
			}
			if(maxLevel > needExp.length){
				for(int i = needExp.length; i < maxLevel; i++){
					efs[i] = Long.parseLong(needExp[needExp.length - 1]);
				}
			}
			skill.setNeedExp(efs);
		} catch (Exception ex) {
			long[] efs = new long[maxLevel];
			for(int i = 0; i < maxLevel; i++){
				efs[i] = (long)cell.getNumericCellValue();
			}
			skill.setNeedExp(efs);
		}

		cell = row.getCell(基本攻击技能_needPoint_所在列);
		String needPoints = "";
		try {
			needPoints = cell.getStringCellValue().trim();
			String[] needPoint = needPoints.split(";");
			if(needPoint.length == 1){
				needPoint = needPoints.split("；");
			}
			int[] efs = new int[maxLevel];
			for(int i = 0; i < needPoint.length && i < maxLevel; i++){
				efs[i] = Integer.parseInt(needPoint[i]);
			}
			if(maxLevel > needPoint.length){
				for(int i = needPoint.length; i < maxLevel; i++){
					efs[i] = Integer.parseInt(needPoint[needPoint.length - 1]);
				}
			}
			skill.setNeedPoint(efs);
		} catch (Exception ex) {
			int[] efs = new int[maxLevel];
			for(int i = 0; i < maxLevel; i++){
				efs[i] = (int)cell.getNumericCellValue();
			}
			skill.setNeedPoint(efs);
		}

		cell = row.getCell(基本攻击技能_needPlayerLevel_所在列);
		String needPlayerLevels = "";
		try {
			needPlayerLevels = cell.getStringCellValue().trim();
			String[] needPlayerLevel = needPlayerLevels.split(";");
			if(needPlayerLevel.length == 1){
				needPlayerLevel = needPlayerLevels.split("；");
			}
			int[] efs = new int[maxLevel];
			for(int i = 0; i < needPlayerLevel.length && i < maxLevel; i++){
				efs[i] = Integer.parseInt(needPlayerLevel[i]);
			}
			if(maxLevel > needPlayerLevel.length){
				for(int i = needPlayerLevel.length; i < maxLevel; i++){
					efs[i] = Integer.parseInt(needPlayerLevel[needPlayerLevel.length - 1]);
				}
			}
			skill.setNeedPlayerLevel(efs);
		} catch (Exception ex) {
			int[] efs = new int[maxLevel];
			for(int i = 0; i < maxLevel; i++){
				efs[i] = (int)cell.getNumericCellValue();
			}
			skill.setNeedPlayerLevel(efs);
		}

		cell = row.getCell(基本攻击技能_needCareerThreadPoints_所在列);
		byte needCareerThreadPoints = 0;
		try {
			needCareerThreadPoints = (byte)cell.getNumericCellValue();
			skill.setNeedCareerThreadPoints(needCareerThreadPoints);
		} catch (Exception ex) {
			try{
				needCareerThreadPoints = Byte.parseByte(cell.getStringCellValue().trim());
				skill.setNeedCareerThreadPoints(needCareerThreadPoints);
			}catch(Exception e){
				throw new Exception("row:"+row.getRowNum()+"--e:"+e);
			}
		}
		

		cell = row.getCell(基本攻击技能_colIndex_所在列);
		int colIndex = 0;
		try {
			colIndex = (int)cell.getNumericCellValue();
			skill.setColIndex(colIndex);
		} catch (Exception ex) {
			try{
				colIndex = Integer.parseInt(cell.getStringCellValue().trim());
				skill.setColIndex(colIndex);
			}catch(Exception e){
//				throw new Exception("row:"+row.getRowNum()+"--e:"+e);
			}
		}
	}
	
	public void 所有主动技能的数据(ActiveSkill skill,HSSFRow row) throws Exception{
		HSSFCell cell = row.getCell(主动攻击技能_duration1_所在列);
		try {
			int duration1 = (int)cell.getNumericCellValue();
			skill.setDuration1(duration1);
		} catch (Exception ex) {
			try{
				int duration1 = Integer.parseInt(cell.getStringCellValue().trim());
				skill.setDuration1(duration1);
			}catch(Exception e){
				throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
			}
			
		}
		
		cell = row.getCell(主动攻击技能_duration2_所在列);
		try {
			int duration2 = (int)cell.getNumericCellValue();
			skill.setDuration2(duration2);
		} catch (Exception ex) {
			try{
				int duration2 = Integer.parseInt(cell.getStringCellValue().trim());
				skill.setDuration2(duration2);
			}catch(Exception e){
				throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
			}
			
		}
		
		cell = row.getCell(主动攻击技能_duration3_所在列);
		try {
			int duration3 = (int)cell.getNumericCellValue();
			skill.setDuration3(duration3);
		} catch (Exception ex) {
			try{
				int duration3 = Integer.parseInt(cell.getStringCellValue().trim());
				skill.setDuration3(duration3);
			}catch(Exception e){
				throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
			}
			
		}
		
		cell = row.getCell(主动攻击技能_calDamageTime_所在列);
		try {
			int calDamageTime = (int)cell.getNumericCellValue();
			skill.setCalDamageTime(calDamageTime);
		} catch (Exception ex) {
			try{
				int calDamageTime = Integer.parseInt(cell.getStringCellValue().trim());
				skill.setCalDamageTime(calDamageTime);
			}catch(Exception e){
				throw new Exception("row:"+row.getRowNum()+"--e:"+e);	
			}
			
		}

		cell = row.getCell(主动攻击技能_style1_所在列);
		String style1 = "";
		try {
			style1 = cell.getStringCellValue().trim();
			skill.setStyle1(style1);
		} catch (Exception ex) {
			
		}
		
		cell = row.getCell(主动攻击技能_style2_所在列);
		String style2 = "";
		try {
			style2 = cell.getStringCellValue().trim();
			skill.setStyle2(style2);
		} catch (Exception ex) {
			
		}

		cell = row.getCell(主动攻击技能_effectiveTimes_所在列);
		String effectiveTimess = "";
		try {
			effectiveTimess = cell.getStringCellValue().trim();
			String[] effectiveTimes = effectiveTimess.split(";");
			if(effectiveTimes.length == 1){
				effectiveTimes = effectiveTimess.split("；");
			}
			int[] efs = new int[effectiveTimes.length];
			for(int i = 0; i < effectiveTimes.length; i++){
				efs[i] = Integer.parseInt(effectiveTimes[i]);
			}
			skill.setEffectiveTimes(efs);
		} catch (Exception ex) {
			skill.setEffectiveTimes(new int[]{(int)cell.getNumericCellValue()});
		}
		

		cell = row.getCell(主动攻击技能_enableWeaponType_所在列);
		try {
			byte enableWeaponType = (byte)cell.getNumericCellValue();
			skill.setEnableWeaponType(enableWeaponType);
		} catch (Exception ex) {
			try{
			byte enableWeaponType = Byte.parseByte(cell.getStringCellValue().trim());
			skill.setEnableWeaponType(enableWeaponType);
			}catch(Exception e){
				throw new Exception("row:"+row.getRowNum()+"--e:"+e);
			}
		}
		

		cell = row.getCell(主动攻击技能_weaponTypeLimit_所在列);
		try {
			byte weaponTypeLimit = (byte)cell.getNumericCellValue();
			skill.setWeaponTypeLimit(weaponTypeLimit);
		} catch (Exception ex) {
			try{
			byte weaponTypeLimit = Byte.parseByte(cell.getStringCellValue().trim());
			skill.setWeaponTypeLimit(weaponTypeLimit);
			}catch(Exception e){
				throw new Exception("row:"+row.getRowNum()+"--e:"+e);
			}
		}
		

		cell = row.getCell(主动攻击技能_attackType_所在列);
		try {
			byte attackType = (byte)cell.getNumericCellValue();
			skill.setAttackType(attackType);
		} catch (Exception ex) {
			try{
				byte attackType = Byte.parseByte(cell.getStringCellValue().trim());
				skill.setAttackType(attackType);
			}catch(Exception e){
				throw new Exception("row:"+row.getRowNum()+"--e:"+e);
			}
		}

		cell = row.getCell(主动攻击技能_followByCommonAttack_所在列);
		try {
			byte followByCommonAttack = (byte)cell.getNumericCellValue();
			skill.setFollowByCommonAttack(followByCommonAttack);
		} catch (Exception ex) {
			try{
				byte followByCommonAttack = Byte.parseByte(cell.getStringCellValue().trim());
				skill.setFollowByCommonAttack(followByCommonAttack);
			}catch(Exception e){
				throw new Exception("row:"+row.getRowNum()+"--e:"+e);
			}
		}

		cell = row.getCell(主动攻击技能_ignoreStun_所在列);
		try {
			boolean ignoreStun = cell.getBooleanCellValue();
			skill.setIgnoreStun(ignoreStun);
		} catch (Exception ex) {
			throw new Exception("row:"+row.getRowNum()+"--e:"+ex);
		}

		cell = row.getCell(主动攻击技能_param1_所在列);
		try {
			float param1 = (float)cell.getNumericCellValue();
			skill.setParam1(param1);
		} catch (Exception ex) {
			try{
				float param1 = Float.parseFloat(cell.getStringCellValue().trim());
				skill.setParam1(param1);
			}catch(Exception e){

			}
		}

		cell = row.getCell(主动攻击技能_param2_所在列);
		try {
			float param2 = (float)cell.getNumericCellValue();
			skill.setParam2(param2);
		} catch (Exception ex) {
			try{
				float param2 = Float.parseFloat(cell.getStringCellValue().trim());
				skill.setParam2(param2);
			}catch(Exception e){

			}
		}

		cell = row.getCell(主动攻击技能_param3_所在列);
		try {
			float param3 = (float)cell.getNumericCellValue();
			skill.setParam3(param3);
		} catch (Exception ex) {
			try{
				float param3 = Float.parseFloat(cell.getStringCellValue().trim());
				skill.setParam3(param3);
			}catch(Exception e){

			}
		}

		cell = row.getCell(主动攻击技能_param4_所在列);
		try {
			float param4 = (float)cell.getNumericCellValue();
			skill.setParam4(param4);
		} catch (Exception ex) {
			try{
				float param4 = Float.parseFloat(cell.getStringCellValue().trim());
				skill.setParam4(param4);
			}catch(Exception e){

			}
		}

		cell = row.getCell(主动攻击技能_param5_所在列);
		try {
			float param5 = (float)cell.getNumericCellValue();
			skill.setParam5(param5);
		} catch (Exception ex) {
			try{
				float param5 = Float.parseFloat(cell.getStringCellValue().trim());
				skill.setParam5(param5);
			}catch(Exception e){

			}
		}

		cell = row.getCell(主动攻击技能_param6_所在列);
		try {
			float param6 = (float)cell.getNumericCellValue();
			skill.setParam6(param6);
		} catch (Exception ex) {
			try{
				float param6 = Float.parseFloat(cell.getStringCellValue().trim());
				skill.setParam6(param6);
			}catch(Exception e){

			}
		}

		cell = row.getCell(主动攻击技能_param7_所在列);
		try {
			float param7 = (float)cell.getNumericCellValue();
			skill.setParam7(param7);
		} catch (Exception ex) {
			try{
				float param7 = Float.parseFloat(cell.getStringCellValue().trim());
				skill.setParam7(param7);
			}catch(Exception e){

			}
		}

		cell = row.getCell(主动攻击技能_param8_所在列);
		try {
			float param8 = (float)cell.getNumericCellValue();
			skill.setParam8(param8);
		} catch (Exception ex) {
			try{
				float param8 = Float.parseFloat(cell.getStringCellValue().trim());
				skill.setParam8(param8);
			}catch(Exception e){

			}
		}

		cell = row.getCell(主动攻击技能_hateParam_所在列);
		try {
			int hateParam = (int)cell.getNumericCellValue();
			skill.setHateParam(hateParam);
		} catch (Exception ex) {
			try{
				int hateParam = Integer.parseInt(cell.getStringCellValue().trim());
				skill.setHateParam(hateParam);
			}catch(Exception e){
				throw new Exception("row:"+row.getRowNum()+"--e:"+e);
			}
		}

		cell = row.getCell(主动攻击技能_buffTargetType_所在列);
		try {
			int buffTargetType = (int)cell.getNumericCellValue();
			skill.setBuffTargetType(buffTargetType);
		} catch (Exception ex) {
			try{
				int buffTargetType = Integer.parseInt(cell.getStringCellValue().trim());
				skill.setBuffTargetType(buffTargetType);
			}catch(Exception e){

			}
		}

		cell = row.getCell(主动攻击技能_buffName_所在列);
		try {
			String buffName = cell.getStringCellValue();
			skill.setBuffName(buffName.trim());
		} catch (Exception ex) {
			
		}

		cell = row.getCell(主动攻击技能_buffLevel_所在列);
		String buffLevels = "";
		try {
			buffLevels = cell.getStringCellValue().trim();
			String[] buffLevel = buffLevels.split(";");
			if(buffLevel.length == 1){
				buffLevel = buffLevels.split("；");
			}
			int[] bfs = new int[skill.getMaxLevel()];
			for(int i = 0; i < buffLevel.length && i < skill.getMaxLevel(); i++){
				bfs[i] = Integer.parseInt(buffLevel[i]);
			}
			if(skill.getMaxLevel() > buffLevel.length){
				for(int i = buffLevel.length; i < skill.getMaxLevel(); i++){
					bfs[i] = Integer.parseInt(buffLevel[buffLevel.length - 1]);
				}
			}
			skill.setBuffLevel(bfs);
		} catch (Exception ex) {
			try{
				int[] fds = new int[skill.getMaxLevel()];
				for(int i = 0; i < skill.getMaxLevel(); i++){
					fds[i] = (int)cell.getNumericCellValue();
				}
				skill.setBuffLevel(fds);
			}catch(Exception e){
				
			}
		}

		cell = row.getCell(主动攻击技能_buffLastingTime_所在列);
		String buffLastingTimes = "";
		try {
			buffLastingTimes = cell.getStringCellValue().trim();
			String[] buffLastingTime = buffLastingTimes.split(";");
			if(buffLastingTime.length == 1){
				buffLastingTime = buffLastingTimes.split("；");
			}
			long[] bfs = new long[skill.getMaxLevel()];
			for(int i = 0; i < buffLastingTime.length && i < skill.getMaxLevel(); i++){
				bfs[i] = Long.parseLong(buffLastingTime[i]);
			}
			if(skill.getMaxLevel() > buffLastingTime.length){
				for(int i = buffLastingTime.length; i < skill.getMaxLevel(); i++){
					bfs[i] = Long.parseLong(buffLastingTime[buffLastingTime.length - 1]);
				}
			}
			skill.setBuffLastingTime(bfs);
		} catch (Exception ex) {
			try{
				long[] fds = new long[skill.getMaxLevel()];
				for(int i = 0; i < skill.getMaxLevel(); i++){
					fds[i] = (long)cell.getNumericCellValue();
				}
				skill.setBuffLastingTime(fds);
			}catch(Exception e){
				
			}
		}

		cell = row.getCell(主动攻击技能_buffProbability_所在列);
		String buffProbabilitys = "";
		try {
			buffProbabilitys = cell.getStringCellValue().trim();
			String[] buffProbability = buffProbabilitys.split(";");
			if(buffProbability.length == 1){
				buffProbability = buffProbabilitys.split("；");
			}
			int[] bfs = new int[skill.getMaxLevel()];
			for(int i = 0; i < buffProbability.length && i < skill.getMaxLevel(); i++){
				bfs[i] = Integer.parseInt(buffProbability[i]);
			}
			if(skill.getMaxLevel() > buffProbability.length){
				for(int i = buffProbability.length; i < skill.getMaxLevel(); i++){
					bfs[i] = Integer.parseInt(buffProbability[buffProbability.length - 1]);
				}
			}
			skill.setBuffProbability(bfs);
		} catch (Exception ex) {
			try{
				int[] fds = new int[skill.getMaxLevel()];
				for(int i = 0; i < skill.getMaxLevel(); i++){
					fds[i] = (int)cell.getNumericCellValue();
				}
				skill.setBuffProbability(fds);
			}catch(Exception e){
				
			}
		}

		cell = row.getCell(主动攻击技能_attackDamages_所在列);
		String attackDamagess = "";
		try {
			attackDamagess = cell.getStringCellValue().trim();
			String[] attackDamages = attackDamagess.split(";");
			if(attackDamages.length == 1){
				attackDamages = attackDamagess.split("；");
			}
			int[] ads = new int[skill.getMaxLevel()];
			for(int i = 0; i < attackDamages.length && i < skill.getMaxLevel(); i++){
				ads[i] = Integer.parseInt(attackDamages[i]);
			}
			if(skill.getMaxLevel() > attackDamages.length){
				for(int i = attackDamages.length; i < skill.getMaxLevel(); i++){
					ads[i] = Integer.parseInt(attackDamages[attackDamages.length - 1]);
				}
			}
			skill.setAttackDamages(ads);
		} catch (Exception ex) {
			try{
				int[] ads = new int[skill.getMaxLevel()];
				for(int i = 0; i < skill.getMaxLevel(); i++){
					ads[i] = (int)cell.getNumericCellValue();
				}
				skill.setAttackDamages(ads);
			}catch(Exception e){
				
			}
		}
		
		cell = row.getCell(主动攻击技能_guajiFalg_所在列);
		try {
			boolean guajiFlag = cell.getBooleanCellValue();
			skill.setGuajiFlag(guajiFlag);
		} catch (Exception ex) {

		}

		cell = row.getCell(主动攻击技能_flySound_所在列);
		try {
			String flySound = cell.getStringCellValue().trim();
			skill.setFlySound(flySound);
		} catch (Exception ex) {

		}

		cell = row.getCell(主动攻击技能_explodeSound_所在列);
		try {
			String explodeSound = cell.getStringCellValue().trim();
			skill.setExplodeSound(explodeSound);
		} catch (Exception ex) {

		}

		cell = row.getCell(主动攻击技能_flyParticle_所在列);
		String[] flyParticle = null;
		try {
			if(!cell.getStringCellValue().trim().equals("")){
				flyParticle = cell.getStringCellValue().trim().split(";");
				if(flyParticle.length == 1){
					flyParticle = cell.getStringCellValue().trim().split("；");
				}
				skill.setFlyParticle(flyParticle);
			}

		} catch (Exception ex) {

		}
		
		cell = row.getCell(主动攻击技能_angle_所在列);
		try {
			short angle = (short)cell.getNumericCellValue();
			skill.setAngle(angle);
		} catch (Exception ex) {

		}

		cell = row.getCell(主动攻击技能_explodeParticle_所在列);
		String[] explodeParticle = null;
		try {
			explodeParticle = cell.getStringCellValue().trim().split(";");
			if(explodeParticle.length == 1){
				explodeParticle = cell.getStringCellValue().trim().split("；");
			}
			skill.setExplodeParticle(explodeParticle);
		} catch (Exception ex) {

		}

		cell = row.getCell(主动攻击技能_explodePercent_所在列);
		try {
			short explodePercent = (short)cell.getNumericCellValue();
			skill.setExplodePercent(explodePercent);
		} catch (Exception ex) {
			try{
				short explodePercent = Short.parseShort(cell.getStringCellValue().trim());
				skill.setExplodePercent(explodePercent);
			}catch(Exception e){
				
			}
		}
		
		cell = row.getCell(主动攻击技能_targetFootParticle_所在列);
		String[] targetFootParticle = null;
		try {
			targetFootParticle = cell.getStringCellValue().trim().split(";");
			if(targetFootParticle.length == 1){
				targetFootParticle = cell.getStringCellValue().trim().split("；");
			}
			skill.setTargetFootParticle(targetFootParticle);
		} catch (Exception ex) {

		}

		cell = row.getCell(主动攻击技能_bodyParticle_所在列);
		String[] bodyParticle = null;
		try {
			bodyParticle = cell.getStringCellValue().trim().split(";");
			if(bodyParticle.length == 1){
				bodyParticle = cell.getStringCellValue().trim().split("；");
			}
			skill.setBodyParticle(bodyParticle);
		} catch (Exception ex) {

		}

		cell = row.getCell(主动攻击技能_bodyPercent_所在列);
		try {
			short bodyPercent = (short)cell.getNumericCellValue();
			skill.setBodyPercent(bodyPercent);
		} catch (Exception ex) {
			try{
				short bodyPercent = Short.parseShort(cell.getStringCellValue().trim());
				skill.setBodyPercent(bodyPercent);
			}catch(Exception e){
				
			}
		}

		cell = row.getCell(主动攻击技能_bodyParticleOffset_所在列);
		try {
			String[] bodyParticleOffsets = cell.getStringCellValue().trim().split(";");
			if(bodyParticleOffsets.length == 1){
				bodyParticleOffsets = cell.getStringCellValue().trim().split("；");
			}
			short[] ads = new short[bodyParticleOffsets.length];
			for(int i = 0; i < bodyParticleOffsets.length; i++){
				ads[i] = Short.parseShort(bodyParticleOffsets[i]);
			}
			skill.setBodyParticleOffset(ads);
		} catch (Exception ex) {

		}

		cell = row.getCell(主动攻击技能_bodyParticlePlayStart_所在列);
		try {
			long bodyParticlePlayStart = (long)cell.getNumericCellValue();
			skill.setBodyParticlePlayStart(bodyParticlePlayStart);
		} catch (Exception ex) {
			try{
				long bodyParticlePlayStart = Long.parseLong(cell.getStringCellValue().trim());
				skill.setBodyParticlePlayStart(bodyParticlePlayStart);
			}catch(Exception e){
				
			}
		}

		cell = row.getCell(主动攻击技能_bodyParticlePlayEnd_所在列);
		try {
			long bodyParticlePlayEnd = (long)cell.getNumericCellValue();
			skill.setBodyParticlePlayEnd(bodyParticlePlayEnd);
		} catch (Exception ex) {
			try{
				long bodyParticlePlayEnd = Long.parseLong(cell.getStringCellValue().trim());
				skill.setBodyParticlePlayEnd(bodyParticlePlayEnd);
			}catch(Exception e){
				
			}
		}

		cell = row.getCell(主动攻击技能_bodyPartPath_所在列);
		try {
			String bodyPartPath = cell.getStringCellValue().trim();
			skill.setBodyPartPath(bodyPartPath);
		} catch (Exception ex) {

		}

		cell = row.getCell(主动攻击技能_bodyPartAnimation_所在列);
		try {
			String bodyPartAnimation = cell.getStringCellValue().trim();
			skill.setBodyPartAnimation(bodyPartAnimation);
		} catch (Exception ex) {

		}

		cell = row.getCell(主动攻击技能_bodyPartAnimationPercent_所在列);
		try {
			short bodyPartAnimationPercent = (short)cell.getNumericCellValue();
			skill.setBodyPartAnimationPercent(bodyPartAnimationPercent);
		} catch (Exception ex) {
			try{
				short bodyPartAnimationPercent = Short.parseShort(cell.getStringCellValue().trim());
				skill.setBodyPartAnimationPercent(bodyPartAnimationPercent);
			}catch(Exception e){
				
			}
		}

		cell = row.getCell(主动攻击技能_bodyPartAnimationOffset_所在列);
		try {
			String[] bodyPartAnimationOffsets = cell.getStringCellValue().trim().split(";");
			if(bodyPartAnimationOffsets.length == 1){
				bodyPartAnimationOffsets = cell.getStringCellValue().trim().split("；");
			}
			short[] ads = new short[bodyPartAnimationOffsets.length];
			for(int i = 0; i < bodyPartAnimationOffsets.length; i++){
				ads[i] = Short.parseShort(bodyPartAnimationOffsets[i]);
			}
			skill.setBodyPartAnimationOffset(ads);
		} catch (Exception ex) {

		}

		cell = row.getCell(主动攻击技能_bodyPartAnimationPlayStart_所在列);
		try {
			long bodyPartAnimationPlayStart = (long)cell.getNumericCellValue();
			skill.setBodyPartAnimationPlayStart(bodyPartAnimationPlayStart);
		} catch (Exception ex) {
			try{
				long bodyPartAnimationPlayStart = Long.parseLong(cell.getStringCellValue().trim());
				skill.setBodyPartAnimationPlayStart(bodyPartAnimationPlayStart);
			}catch(Exception e){
				
			}
		}

		cell = row.getCell(主动攻击技能_bodyPartAnimationPlayEnd_所在列);
		try {
			long bodyPartAnimationPlayEnd = (long)cell.getNumericCellValue();
			skill.setBodyPartAnimationPlayEnd(bodyPartAnimationPlayEnd);
		} catch (Exception ex) {
			try{
				long bodyPartAnimationPlayEnd = Long.parseLong(cell.getStringCellValue().trim());
				skill.setBodyPartAnimationPlayEnd(bodyPartAnimationPlayEnd);
			}catch(Exception e){
				
			}
		}

		cell = row.getCell(主动攻击技能_footParticle_所在列);
		String[] footParticle = null;
		try {
			footParticle = cell.getStringCellValue().trim().split(";");
			if(footParticle.length == 1){
				footParticle = cell.getStringCellValue().trim().split("；");
			}
			skill.setFootParticle(footParticle);
		} catch (Exception ex) {

		}

		cell = row.getCell(主动攻击技能_footParticleOffset_所在列);
		try {
			String[] footParticleOffsets = cell.getStringCellValue().trim().split(";");
			if(footParticleOffsets.length == 1){
				footParticleOffsets = cell.getStringCellValue().trim().split("；");
			}
			short[] ads = new short[footParticleOffsets.length];
			for(int i = 0; i < footParticleOffsets.length; i++){
				ads[i] = Short.parseShort(footParticleOffsets[i]);
			}
			skill.setFootParticleOffset(ads);
		} catch (Exception ex) {

		}

		cell = row.getCell(主动攻击技能_footParticlePlayStart_所在列);
		try {
			long footParticlePlayStart = (long)cell.getNumericCellValue();
			skill.setFootParticlePlayStart(footParticlePlayStart);
		} catch (Exception ex) {
			try{
				long footParticlePlayStart = Long.parseLong(cell.getStringCellValue().trim());
				skill.setFootParticlePlayStart(footParticlePlayStart);
			}catch(Exception e){
				
			}
		}

		cell = row.getCell(主动攻击技能_footParticlePlayEnd_所在列);
		try {
			long footParticlePlayEnd = (long)cell.getNumericCellValue();
			skill.setFootParticlePlayEnd(footParticlePlayEnd);
		} catch (Exception ex) {
			try{
				long footParticlePlayEnd = Long.parseLong(cell.getStringCellValue().trim());
				skill.setFootParticlePlayEnd(footParticlePlayEnd);
			}catch(Exception e){
				
			}
		}

		cell = row.getCell(主动攻击技能_footPartPath_所在列);
		try {
			String footPartPath = cell.getStringCellValue().trim();
			skill.setFootPartPath(footPartPath);
		} catch (Exception ex) {

		}

		cell = row.getCell(主动攻击技能_footPartAnimation_所在列);
		try {
			String footPartAnimation = cell.getStringCellValue().trim();
			skill.setFootPartAnimation(footPartAnimation);
		} catch (Exception ex) {

		}

		cell = row.getCell(主动攻击技能_footPartAnimationOffset_所在列);
		try {
			String[] footPartAnimationOffsets = cell.getStringCellValue().trim().split(";");
			if(footPartAnimationOffsets.length == 1){
				footPartAnimationOffsets = cell.getStringCellValue().trim().split("；");
			}
			short[] ads = new short[footPartAnimationOffsets.length];
			for(int i = 0; i < footPartAnimationOffsets.length; i++){
				ads[i] = Short.parseShort(footPartAnimationOffsets[i]);
			}
			skill.setFootPartAnimationOffset(ads);
		} catch (Exception ex) {

		}

		cell = row.getCell(主动攻击技能_footPartAnimationPlayStart_所在列);
		try {
			long footPartAnimationPlayStart = (long)cell.getNumericCellValue();
			skill.setFootPartAnimationPlayStart(footPartAnimationPlayStart);
		} catch (Exception ex) {
			try{
				long footPartAnimationPlayStart = Long.parseLong(cell.getStringCellValue().trim());
				skill.setFootPartAnimationPlayStart(footPartAnimationPlayStart);
			}catch(Exception e){
				
			}
		}

		cell = row.getCell(主动攻击技能_footPartAnimationPlayEnd_所在列);
		try {
			long footPartAnimationPlayEnd = (long)cell.getNumericCellValue();
			skill.setFootPartAnimationPlayEnd(footPartAnimationPlayEnd);
		} catch (Exception ex) {
			try{
				long footPartAnimationPlayEnd = Long.parseLong(cell.getStringCellValue().trim());
				skill.setFootPartAnimationPlayEnd(footPartAnimationPlayEnd);
			}catch(Exception e){
				
			}
		}

	}
	
	public CommonAttackSkill getCommonAttackSkill(Player player) {
		if (player.getCareer() != 5) {
			return getCommonAttackSkill(player.getCareer());
		}
		for(int i = 0 ; i < commonAttackSkills.length ; i++){
			if(commonAttackSkills[i].getWeaponTypeLimit() == player.getCareer()){
				if (player.isShouStatus() && commonAttackSkills[i].getId() == shoukuiCommenSkillIds[1]) {
					return commonAttackSkills[i];
				} else if (!player.isShouStatus() && commonAttackSkills[i].getId() == shoukuiCommenSkillIds[0]) {
					return commonAttackSkills[i];
				}
			}
		}
		
		return getCommonAttackSkill(player.getCareer());
	}
	
	/**
	 * 应策划要求把武器限制改成了职业限制，这样武器限制值当做职业限制值来使用
	 * @param career
	 * @return
	 */
	public CommonAttackSkill getCommonAttackSkill(byte career) {
		for(int i = 0 ; i < commonAttackSkills.length ; i++){
			if(commonAttackSkills[i].getWeaponTypeLimit() == career){
				return commonAttackSkills[i];
			}
		}
		
		return null;
	}
	
	/**
	 * 根据ID获得门派，id必须是门派的连续编号
	 * 
	 * @param id
	 * @return
	 */
	public Career getCareer(int id) {
		if (id >= 0 && id < careers.length)
			return careers[id];
		return null;
	}

	public Career getCareer(String name) {
		for (int i = 0; i < careers.length; i++) {
			if (careers[i] != null && name.equals(careers[i].name))
				return careers[i];
		}
		return null;
	}

	public Career[] getCareers() {
		return careers;
	}
	
	/**
	 * 设置一个对象的属性，需要此对象的属性提供setter和getter方法
	 * @param cl
	 * @param o
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	
	public static void setObjectValue(Class cl,Object o,String key,String value) throws Exception{
		Field f = null;
		Class clazz = cl;
		
		while(clazz != null && f == null){
			try{
				f = clazz.getDeclaredField(key);
			}catch(Exception e){
				clazz = clazz.getSuperclass();
			}
		}
		
		if(f == null){
			System.out.println("初始化职业技能数据，出现出错：技能类["+cl.getName()+"]没有属性["+key+"]，请检查！");
			return;
		}
		
		Class t = f.getType();
		Method m = null;
		try{
			m = cl.getMethod("set" + java.lang.Character.toUpperCase(key.charAt(0)) + key.substring(1), new Class[]{t});
		}catch(Exception e){
			throw new Exception("技能类["+cl.getName()+"]属性["+key+"]的设置方法["+("set" + java.lang.Character.toUpperCase(key.charAt(0)) + key.substring(1)+"("+t.getName()+")")+"]，请检查！");
		}
		Object v = null;
		try{
			if(t == Byte.class || t == Byte.TYPE){
				v = Byte.parseByte(value);
			}else if(t == Short.class || t == Short.TYPE){
				v = Short.parseShort(value);
			}else if(t == Integer.class || t == Integer.TYPE){
				v = Integer.parseInt(value);
			}else if(t == Long.class || t == Long.TYPE){
				v = Long.parseLong(value);
			}else if(t == Boolean.class || t == Boolean.TYPE){
				v = Boolean.parseBoolean(value);
			}else if(t == Float.class || t == Float.TYPE){
				v = Float.parseFloat(value);
			}else if(t == String.class){
				v = value;
			}else if(t.isArray()){
				if(value.trim().length() == 0){
					v = Array.newInstance(t.getComponentType(), 0);
				}else{
					String ss[] = value.split(",");
					ArrayList<String> al = new ArrayList<String>();
					for(int i = 0 ; i < ss.length ; i++){
						if(ss[i].trim().length() > 0){
							al.add(ss[i].trim());
						}
					}
					ss = al.toArray(new String[0]);
					t = t.getComponentType();
					v = Array.newInstance(t, ss.length);
					for(int i = 0 ; i < ss.length ; i++){
						Object v2 = null;
						if(t == Byte.class || t == Byte.TYPE){
							v2 = Byte.parseByte(ss[i]);
						}else if(t == Short.class || t == Short.TYPE){
							v2 = Short.parseShort(ss[i]);
						}else if(t == Integer.class || t == Integer.TYPE){
							v2 = Integer.parseInt(ss[i]);
						}else if(t == Long.class || t == Long.TYPE){
							v2 = Long.parseLong(ss[i]);
						}else if(t == Float.class || t == Float.TYPE){
							v2 = Float.parseFloat(ss[i]);
						}else if(t == Boolean.class || t == Boolean.TYPE){
							v2 = Boolean.parseBoolean(ss[i]);
						}else if(t == String.class){
							ss[i] = TransferLanguage.transferString(ss[i]);
							v2 = ss[i];
						}else{
							throw new Exception("技能类["+cl.getName()+"]属性["+key+"]为复杂的数据类型【"+t.getName()+"的数组】，暂时不支持！");
						}
						Array.set(v, i, v2);
					}
				}
			}else{
				throw new Exception("技能类["+cl.getName()+"]属性["+key+"]为复杂的数据类型【"+t.getName()+"】，暂时不支持！");
			}
		}catch(NumberFormatException e){
			throw new Exception("技能类["+cl.getName()+"]属性["+key+"]为数据【"+value+"】解析出错，请检查配置文件！");
		}
		try{
			m.invoke(o, new Object[]{v});
		}catch(Exception e){
			throw new Exception("技能类["+cl.getName()+"]属性["+key+"]的数据【"+value+"】设置出错：",e);
		}
	}

	public CommonAttackSkill[] getCommonAttackSkills() {
		return commonAttackSkills;
	}

	public Skill[] getMonsterSkills() {
		return monsterSkills;
	}

	/**
	 * 判断某个技能是否为怪物或者NPC使用的技能
	 * @param s
	 * @return
	 */
	public boolean isSkillForSprite(Skill s){
		for(int i = 0 ; monsterSkills != null && i < monsterSkills.length ; i++){
			if(monsterSkills[i].getId() == s.getId()){
				return true;
			}
		}
		return false;
	}
	
	public QUERY_XINFA_SKILL_IDLIST_RES handle_QUERY_XINFA_SKILL_IDLIST_REQ(QUERY_XINFA_SKILL_IDLIST_REQ req, Player player) {
		Career cc = getCareer(player.getCareer());
		long[] ids = new long[cc.getXinfaSkills().length];
		for (int i = 0; i < cc.getXinfaSkills().length; i++) {
			ids[i] = cc.getXinfaSkills()[i].getId();
		}
		QUERY_XINFA_SKILL_IDLIST_RES res = new QUERY_XINFA_SKILL_IDLIST_RES(req.getSequnceNum(), ids);
		return res;
	}
}
