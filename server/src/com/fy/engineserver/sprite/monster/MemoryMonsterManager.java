package com.fy.engineserver.sprite.monster;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.TransferLanguage;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.bossactions.BossDoNothing;
import com.fy.engineserver.sprite.monster.bossactions.BossStatDownCityFinish;
import com.fy.engineserver.sprite.monster.bossactions.ClearTargetHatred;
import com.fy.engineserver.sprite.monster.bossactions.ClearTargetHatredExceptMin;
import com.fy.engineserver.sprite.monster.bossactions.ExecuteActiveSkill;
import com.fy.engineserver.sprite.monster.bossactions.FlushMonster;
import com.fy.engineserver.sprite.monster.bossactions.RunawayWithGivenPoints;
import com.fy.engineserver.sprite.monster.bossactions.SaySomething;
import com.fy.engineserver.sprite.monster.bossactions.TargetRunAwayAndAttack;
import com.fy.engineserver.util.ServiceStartRecord;

public class MemoryMonsterManager implements MonsterManager {

	private static MonsterManager self;

	public static MonsterManager getMonsterManager() {
		return self;
	}

	public static class MonsterTempalte {
		public int MonsterCategoryId;
		public Monster monster;

		public MonsterTempalte(int id, Monster m) {
			this.MonsterCategoryId = id;
			this.monster = m;
		}

		public Monster newMonster() {
			Monster n = null;
			n = (Monster) monster.clone();
			return n;
		}
	}

	LinkedHashMap<Integer, MonsterTempalte> templates = new LinkedHashMap<Integer, MonsterTempalte>();
	public Map<String, Integer> monsterName = new HashMap<String, Integer>();

	/**
	 * 怪物掉落sheet解析，monster2FlopListProbabilityMap为地图掉落的各个集合的几率分子值，分母值为100000000，概率独立计算
	 */
	LinkedHashMap<String, Byte> monster2FlopTypeMap = new LinkedHashMap<String, Byte>();
	LinkedHashMap<String, FlopSet[]> monster2FlopListMap = new LinkedHashMap<String, FlopSet[]>();
	LinkedHashMap<String, Integer[]> monster2FlopListProbabilityMap = new LinkedHashMap<String, Integer[]>();

	/**
	 * 地图掉落，map2FlopListProbabilityMap为地图掉落的各个集合的几率分子值，分母值为100000000，概率独立计算
	 */
	LinkedHashMap<String, FlopSet[]> map2FlopListMap = new LinkedHashMap<String, FlopSet[]>();
	LinkedHashMap<String, Integer[]> map2FlopListProbabilityMap = new LinkedHashMap<String, Integer[]>();
	public static final double 分母 = 100000000d;

	/**
	 * 等级掉落，level2FlopListProbabilityMap为等级掉落的各个集合的几率分子值，分母值为100000000，概率独立计算
	 */
	LinkedHashMap<int[], FlopSet[]> level2FlopListMap = new LinkedHashMap<int[], FlopSet[]>();
	LinkedHashMap<int[], Integer[]> level2FlopListProbabilityMap = new LinkedHashMap<int[], Integer[]>();

	/**
	 * 限时等级掉落，此掉落限制和等级掉落一一对应
	 */
	LinkedHashMap<int[], DateTimePreciseMinuteSlice> timeLimitlevel2FlopListMap = new LinkedHashMap<int[], DateTimePreciseMinuteSlice>();
	public LinkedHashMap<Integer, BossAction> bossActionMap = new LinkedHashMap<Integer, BossAction>();

	/**
	 * 获得BOSS动作
	 * @return
	 */
	public LinkedHashMap<Integer, BossAction> getBossActionMap() {
		return bossActionMap;
	}

	public BossAction getBossActionById(int actionId) {
		return bossActionMap.get(actionId);
	}

	public LinkedHashMap<String, FlopSet[]> getMap2FlopListMap() {
		return map2FlopListMap;
	}

	public LinkedHashMap<int[], FlopSet[]> getLevel2FlopListMap() {
		return level2FlopListMap;
	}

	public LinkedHashMap<int[], DateTimePreciseMinuteSlice> getTimeLimitlevel2FlopListMap() {
		return timeLimitlevel2FlopListMap;
	}

	public void setTimeLimitlevel2FlopListMap(LinkedHashMap<int[], DateTimePreciseMinuteSlice> timeLimitlevel2FlopListMap) {
		this.timeLimitlevel2FlopListMap = timeLimitlevel2FlopListMap;
	}

	File monsterFile = new File("D:\\mywork\\工作用的资料\\data\\monster.xls");

	File monsterExpFile = new File("D:\\mywork\\工作用的资料\\data\\expData.xls");

	public File getMonsterExpFile() {
		return monsterExpFile;
	}

	public void setMonsterExpFile(File monsterExpFile) {
		this.monsterExpFile = monsterExpFile;
	}

	ArticleManager articleManager;
	ReadSpriteExcelManager readSpriteExcelManager;

	public static final int 一般monster所在sheet = 0;
	public static final int boss所在sheet = 1;

	public static final int monster行为_SaySomething_所在sheet = 2;
	public static final int monster行为_ExecuteActiveSkill_所在sheet = 3;
	public static final int monster行为_空动作_所在sheet = 4;
	public static final int monster行为_召唤_所在sheet = 5;
	public static final int monster行为_如果目标逃跑对其释放技能_所在sheet = 6;
	public static final int monster行为_保留仇恨最低清除其他仇恨_所在sheet = 7;
	public static final int monster行为_清除目标仇恨_所在sheet = 8;
	public static final int monster行为_副本结束调用_所在sheet = 9;
	public static final int 等级掉落_所在sheet = 10;
	public static final int 地图掉落_所在sheet = 11;
	public static final int 怪物掉落集合_所在sheet = 12;
	public static final int monster行为_寻找路径到某个点_所在sheet = 13;

	public static final int 一般monster属性_monsterCategoryId_列 = 0;
	public static final int 一般monster属性_title_列 = 1;
	public static final int 一般monster属性_name_列 = 2;
	public static final int 一般monster属性_monsterCareer_列 = 3;
	public static final int 一般monster属性_icon_列 = 4;
	public static final int 一般monster属性_classLevel = 5;
	public static final int 一般monster属性_monsterMark_列 = 6;
	public static final int 一般monster属性_hpMark_列 = 7;
	public static final int 一般monster属性_apMark_列 = 8;
	public static final int 一般monster属性_color_列 = 9;
	public static final int 一般monster属性_level_列 = 10;
	public static final int 一般monster属性_height_列 = 11;
	public static final int 一般monster属性_avataRace_列 = 12;
	public static final int 一般monster属性_avataSex_列 = 13;
	public static final int 一般monster属性_putOnEquipmentAvata_列 = 14;
	public static final int 一般monster属性_spriteType_列 = 15;
	public static final int 一般monster属性_monsterType_列 = 16;
	public static final int 一般monster属性_commonAttackSpeed_列 = 17;
	public static final int 一般monster属性_commonAttackRange_列 = 18;
	public static final int 一般monster属性_hpRecoverBase_列 = 19;
	public static final int 一般monster属性_WeaponDamage_列 = 20;
	public static final int 一般monster属性_TraceType_列 = 21;
	public static final int 一般monster属性_BackBornPointMoveSpeedPercent_列 = 22;
	public static final int 一般monster属性_PatrolingWidth_列 = 23;
	public static final int 一般monster属性_PatrolingHeight_列 = 24;
	public static final int 一般monster属性_ActivationWidth_列 = 25;
	public static final int 一般monster属性_ActivationHeight_列 = 26;
	public static final int 一般monster属性_PursueWidth_列 = 27;
	public static final int 一般monster属性_PursueHeight_列 = 28;
	public static final int 一般monster属性_BackBornPointHp_列 = 29;
	public static final int 一般monster属性_DeadLastingTime_列 = 30;
	public static final int 一般monster属性_FlushFrequency_列 = 31;
	public static final int 一般monster属性_ActivationType_列 = 32;
	public static final int 一般monster属性_MoveSpeedA_列 = 33;
	public static final int 一般monster属性_ActiveSkillIds_列 = 34;
	public static final int 一般monster属性_ActiveSkillLevels_列 = 35;
	public static final int 一般monster属性_PatrolingTimeInterval_列 = 36;
	public static final int 一般monster属性_objectScale_列 = 37;
	public static final int 一般monster属性_exeTimeLimit_列 = 38;
	public static final int 一般monster属性_minDistance_列 = 39;
	public static final int 一般monster属性_maxDistance_列 = 40;
	public static final int 一般monster属性_hpPercent_列 = 41;
	public static final int 一般monster属性_maxExeTimes_列 = 42;
	public static final int 一般monster属性_exeTimeGap_列 = 43;
	public static final int 一般monster属性_ignoreMoving_列 = 44;
	public static final int 一般monster属性_actionId_列 = 45;
	public static final int 一般monster属性_消失时间_列 = 46;
	public static final int 一般monster属性_掉落等级限制_列 = 47;

	public static final int monsterAction_SaySomething_actionId = 0;
	public static final int monsterAction_SaySomething_description = 1;
	public static final int monsterAction_SaySomething_content = 2;

	public static final int monsterAction_ExecuteActiveSkill_actionId = 0;
	public static final int monsterAction_ExecuteActiveSkill_skillId = 1;
	public static final int monsterAction_ExecuteActiveSkill_skillLevel = 2;
	public static final int monsterAction_ExecuteActiveSkill_description = 3;

	public static final int monsterAction_空动作_actionId = 0;
	public static final int monsterAction_空动作_description = 1;
	public static final int monsterAction_空动作_content = 2;

	public static final int monsterAction_清除目标仇恨_actionId = 0;
	public static final int monsterAction_清除目标仇恨_description = 1;
	public static final int monsterAction_清除目标仇恨_content = 2;

	public static final int monsterAction_副本结束调用_actionId = 0;
	public static final int monsterAction_副本结束调用_description = 1;
	public static final int monsterAction_副本结束调用_content = 2;
	public static final int monsterAction_副本结束调用_monsterIds = 3;

	public static final int monsterAction_保留仇恨最低清除其他仇恨_actionId = 0;
	public static final int monsterAction_保留仇恨最低清除其他仇恨_description = 1;
	public static final int monsterAction_保留仇恨最低清除其他仇恨_content = 2;

	public static final int monsterAction_如果目标逃跑对其释放技能_actionId = 0;
	public static final int monsterAction_如果目标逃跑对其释放技能_description = 1;
	public static final int monsterAction_如果目标逃跑对其释放技能_content = 2;
	public static final int monsterAction_如果目标逃跑对其释放技能_skillId = 3;
	public static final int monsterAction_如果目标逃跑对其释放技能_skillLevel = 4;

	public static final int monsterAction_召唤_actionId = 0;
	public static final int monsterAction_召唤_description = 1;
	public static final int monsterAction_召唤_content = 2;
	public static final int monsterAction_召唤_bossDisappear = 3;
	public static final int monsterAction_召唤_monsterIds = 4;

	public static final int monsterAction_寻找路径到某个点_actionId = 0;
	public static final int monsterAction_寻找路径到某个点_description = 1;
	public static final int monsterAction_寻找路径到某个点_content = 2;
	public static final int monsterAction_寻找路径到某个点_x = 3;
	public static final int monsterAction_寻找路径到某个点_y = 4;

	public static final int 等级掉落_最低级别_列 = 0;
	public static final int 等级掉落_最高级别_列 = 1;
	public static final int 等级掉落_几率值_列 = 2;
	public static final int 等级掉落_flopType_列 = 3;
	public static final int 等级掉落_flopFormat_列 = 4;
	public static final int 等级掉落_color_列 = 5;
	public static final int 等级掉落_articles_列 = 6;
	public static final int 等级掉落_floprates_列 = 7;

	public static final int 地图掉落_地图名_列 = 0;
	public static final int 地图掉落_几率值_列 = 1;
	public static final int 地图掉落_flopType_列 = 2;
	public static final int 地图掉落_flopFormat_列 = 3;
	public static final int 地图掉落_color_列 = 4;
	public static final int 地图掉落_articles_列 = 5;
	public static final int 地图掉落_floprates_列 = 6;

	public static final int 怪物掉落集合_怪物名_列 = 0;
	public static final int 怪物掉落集合_集合flopFormat_列 = 1;
	public static final int 怪物掉落集合_几率值_列 = 2;
	public static final int 怪物掉落集合_flopType_列 = 3;
	public static final int 怪物掉落集合_集合内部flopFormat_列 = 4;
	public static final int 怪物掉落集合_color_列 = 5;
	public static final int 怪物掉落集合_articles_列 = 6;
	public static final int 怪物掉落集合_floprates_列 = 7;

	public static final int 怪物经验表sheet = 1;

	public static final int 怪物等级列 = 0;
	public static final int 怪物经验列 = 1;

	public static HashMap<Integer, Long> levelExpMap = new HashMap<Integer, Long>();

	/**
	 * 所有存活的怪物
	 */
	public Map<Long, Monster> monsterMap = Collections.synchronizedMap(new HashMap<Long, Monster>());

	public File getMonsterFile() {
		return monsterFile;
	}

	public void setMonsterFile(File MonsterFile) {
		this.monsterFile = MonsterFile;
	}

	public void init() throws Exception {
		
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

		if (monsterFile != null && monsterFile.isFile() && monsterFile.exists()) {
			FileInputStream fis = new FileInputStream(monsterFile);
			loadFrom(fis);
			// byte[] buffer = new byte[1024];
			// int len = -1;
			// ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// while((len = fis.read(buffer, 0 , buffer.length)) != -1){
			// baos.write(buffer , 0, len);
			// }
			// byte[] data = baos.toByteArray();
			// loadFrom(new String(data ,0,data.length));
		}

		if (monsterExpFile != null && monsterExpFile.isFile() && monsterExpFile.exists()) {
			FileInputStream fis = new FileInputStream(monsterExpFile);
			loadExpFrom(fis);
		}

		self = this;

		ServiceStartRecord.startLog(this);
	}

	public void loadFrom(InputStream is) throws Exception {
		try {
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);

			HSSFSheet sheet = workbook.getSheetAt(monster行为_SaySomething_所在sheet);
			int rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				SaySomething ba = new SaySomething();
				if (row != null) {
					HSSFCell hc = row.getCell(monsterAction_SaySomething_actionId);
					try {
						int actionId = Integer.parseInt(hc.getStringCellValue().trim());
						ba.setActionId(actionId);
					} catch (Exception ex) {
						int actionId = (int) hc.getNumericCellValue();
						ba.setActionId(actionId);
					}

					hc = row.getCell(monsterAction_SaySomething_description);
					try {
						String description = hc.getStringCellValue().trim();
						ba.setDescription(description);
					} catch (Exception ex) {

					}

					hc = row.getCell(monsterAction_SaySomething_content);
					try {
						String content = hc.getStringCellValue().trim();
						ba.setContent(content);
					} catch (Exception ex) {

					}
				}
				bossActionMap.put(ba.getActionId(), ba);
				// System.out.println("加载monster模板 ["+ba.getActionId()+"] ["+ba.getDescription()+"]");
			}

			sheet = workbook.getSheetAt(monster行为_ExecuteActiveSkill_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				ExecuteActiveSkill ba = new ExecuteActiveSkill();
				if (row != null) {
					HSSFCell hc = row.getCell(monsterAction_ExecuteActiveSkill_actionId);
					try {
						int actionId = Integer.parseInt(hc.getStringCellValue().trim());
						ba.setActionId(actionId);
					} catch (Exception ex) {
						int actionId = (int) hc.getNumericCellValue();
						ba.setActionId(actionId);
					}

					hc = row.getCell(monsterAction_ExecuteActiveSkill_description);
					try {
						String description = hc.getStringCellValue().trim();
						ba.setDescription(description);
					} catch (Exception ex) {

					}

					hc = row.getCell(monsterAction_ExecuteActiveSkill_skillId);
					try {
						int skillId = Integer.parseInt(hc.getStringCellValue().trim());
						ba.setSkillId(skillId);
					} catch (Exception ex) {
						int skillId = (int) hc.getNumericCellValue();
						ba.setSkillId(skillId);
					}

					hc = row.getCell(monsterAction_ExecuteActiveSkill_skillLevel);
					try {
						int skillLevel = Integer.parseInt(hc.getStringCellValue().trim());
						ba.setSkillLevel(skillLevel);
					} catch (Exception ex) {
						int skillLevel = (int) hc.getNumericCellValue();
						ba.setSkillLevel(skillLevel);
					}
				}
				bossActionMap.put(ba.getActionId(), ba);
				// System.out.println("加载monster模板 [" + ba.getActionId() + "] [" + ba.getDescription() + "]");
			}

			sheet = workbook.getSheetAt(monster行为_空动作_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				BossDoNothing ba = new BossDoNothing();
				if (row != null) {
					HSSFCell hc = row.getCell(monsterAction_空动作_actionId);
					try {
						int actionId = Integer.parseInt(hc.getStringCellValue().trim());
						ba.setActionId(actionId);
					} catch (Exception ex) {
						int actionId = (int) hc.getNumericCellValue();
						ba.setActionId(actionId);
					}

					hc = row.getCell(monsterAction_空动作_description);
					try {
						String description = hc.getStringCellValue().trim();
						ba.setDescription(description);
					} catch (Exception ex) {

					}
				}
				bossActionMap.put(ba.getActionId(), ba);
			}

			sheet = workbook.getSheetAt(monster行为_清除目标仇恨_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				ClearTargetHatred ba = new ClearTargetHatred();
				if (row != null) {
					HSSFCell hc = row.getCell(monsterAction_清除目标仇恨_actionId);
					try {
						int actionId = Integer.parseInt(hc.getStringCellValue().trim());
						ba.setActionId(actionId);
					} catch (Exception ex) {
						int actionId = (int) hc.getNumericCellValue();
						ba.setActionId(actionId);
					}

					hc = row.getCell(monsterAction_清除目标仇恨_description);
					try {
						String description = hc.getStringCellValue().trim();
						ba.setDescription(description);
					} catch (Exception ex) {

					}
				}
				bossActionMap.put(ba.getActionId(), ba);
			}

			sheet = workbook.getSheetAt(monster行为_副本结束调用_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				BossStatDownCityFinish ba = new BossStatDownCityFinish();
				if (row != null) {
					HSSFCell hc = row.getCell(monsterAction_副本结束调用_actionId);
					try {
						int actionId = Integer.parseInt(hc.getStringCellValue().trim());
						ba.setActionId(actionId);
					} catch (Exception ex) {
						int actionId = (int) hc.getNumericCellValue();
						ba.setActionId(actionId);
					}

					hc = row.getCell(monsterAction_副本结束调用_description);
					try {
						String description = hc.getStringCellValue().trim();
						ba.setDescription(description);
					} catch (Exception ex) {

					}

					hc = row.getCell(monsterAction_副本结束调用_monsterIds);
					try {
						int id = (int) hc.getNumericCellValue();
						int[] monsterIds = new int[] { id };
						ba.setMonsterIds(monsterIds);
					} catch (Exception e) {
						try {
							String[] ids = hc.getStringCellValue().trim().split(";");
							if (ids.length == 1) {
								ids = hc.getStringCellValue().trim().split("；");
							}
							int[] monsterIds = new int[ids.length];
							for (int i = 0; i < monsterIds.length; i++) {
								monsterIds[i] = Integer.parseInt(ids[i]);
							}
							ba.setMonsterIds(monsterIds);
						} catch (Exception ex) {

						}
					}
				}
				bossActionMap.put(ba.getActionId(), ba);
			}

			sheet = workbook.getSheetAt(monster行为_保留仇恨最低清除其他仇恨_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				ClearTargetHatredExceptMin ba = new ClearTargetHatredExceptMin();
				if (row != null) {
					HSSFCell hc = row.getCell(monsterAction_保留仇恨最低清除其他仇恨_actionId);
					try {
						int actionId = Integer.parseInt(hc.getStringCellValue().trim());
						ba.setActionId(actionId);
					} catch (Exception ex) {
						int actionId = (int) hc.getNumericCellValue();
						ba.setActionId(actionId);
					}

					hc = row.getCell(monsterAction_保留仇恨最低清除其他仇恨_description);
					try {
						String description = hc.getStringCellValue().trim();
						ba.setDescription(description);
					} catch (Exception ex) {

					}
				}
				bossActionMap.put(ba.getActionId(), ba);
			}

			sheet = workbook.getSheetAt(monster行为_如果目标逃跑对其释放技能_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				TargetRunAwayAndAttack ba = new TargetRunAwayAndAttack();
				if (row != null) {
					HSSFCell hc = row.getCell(monsterAction_如果目标逃跑对其释放技能_actionId);
					try {
						int actionId = Integer.parseInt(hc.getStringCellValue().trim());
						ba.setActionId(actionId);
					} catch (Exception ex) {
						int actionId = (int) hc.getNumericCellValue();
						ba.setActionId(actionId);
					}

					hc = row.getCell(monsterAction_如果目标逃跑对其释放技能_description);
					try {
						String description = hc.getStringCellValue().trim();
						ba.setDescription(description);
					} catch (Exception ex) {

					}

					hc = row.getCell(monsterAction_如果目标逃跑对其释放技能_skillId);
					try {
						int skillId = Integer.parseInt(hc.getStringCellValue().trim());
						ba.setSkillId(skillId);
					} catch (Exception ex) {
						int skillId = (int) hc.getNumericCellValue();
						ba.setSkillId(skillId);
					}

					hc = row.getCell(monsterAction_如果目标逃跑对其释放技能_skillLevel);
					try {
						int skillLevel = Integer.parseInt(hc.getStringCellValue().trim());
						ba.setSkillLevel(skillLevel);
					} catch (Exception ex) {
						int skillLevel = (int) hc.getNumericCellValue();
						ba.setSkillLevel(skillLevel);
					}
				}
				bossActionMap.put(ba.getActionId(), ba);
			}

			sheet = workbook.getSheetAt(monster行为_召唤_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				FlushMonster ba = new FlushMonster();
				if (row != null) {
					HSSFCell hc = row.getCell(monsterAction_召唤_actionId);
					try {
						int actionId = Integer.parseInt(hc.getStringCellValue().trim());
						ba.setActionId(actionId);
					} catch (Exception ex) {
						int actionId = (int) hc.getNumericCellValue();
						ba.setActionId(actionId);
					}

					hc = row.getCell(monsterAction_召唤_description);
					try {
						String description = hc.getStringCellValue().trim();
						ba.setDescription(description);
					} catch (Exception ex) {

					}

					hc = row.getCell(monsterAction_召唤_bossDisappear);
					try {
						boolean disappear = hc.getBooleanCellValue();
						ba.afterFlushDisappear = disappear;
					} catch (Exception ex) {

					}

					hc = row.getCell(monsterAction_召唤_monsterIds);
					try {
						String ActiveSkillIds = hc.getStringCellValue().trim();
						String[] ss = ActiveSkillIds.split(",");
						int[] skills = new int[ss.length];
						for (int i = 0; i < ss.length; i++) {
							String s = ss[i];
							skills[i] = Integer.parseInt(s);
						}
						ba.setMonsterCategoryId(skills);
					} catch (Exception ex) {
						try {
							int ActiveSkillIds = (int) hc.getNumericCellValue();
							ba.setMonsterCategoryId(new int[] { ActiveSkillIds });
						} catch (Exception e) {

						}
					}
				}
				bossActionMap.put(ba.getActionId(), ba);
			}

			sheet = workbook.getSheetAt(monster行为_寻找路径到某个点_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				RunawayWithGivenPoints ba = new RunawayWithGivenPoints();
				if (row != null) {
					HSSFCell hc = row.getCell(monsterAction_寻找路径到某个点_actionId);
					try {
						int actionId = Integer.parseInt(hc.getStringCellValue().trim());
						ba.setActionId(actionId);
					} catch (Exception ex) {
						int actionId = (int) hc.getNumericCellValue();
						ba.setActionId(actionId);
					}

					hc = row.getCell(monsterAction_寻找路径到某个点_description);
					try {
						String description = hc.getStringCellValue().trim();
						ba.setDescription(description);
					} catch (Exception ex) {

					}

					hc = row.getCell(monsterAction_寻找路径到某个点_x);
					try {
						int x = (int) hc.getNumericCellValue();
						ba.setPointX(new int[] { x });
					} catch (Exception ex) {

					}

					hc = row.getCell(monsterAction_寻找路径到某个点_y);
					try {
						int y = (int) hc.getNumericCellValue();
						ba.setPointY(new int[] { y });
					} catch (Exception ex) {

					}
				}
				bossActionMap.put(ba.getActionId(), ba);
			}

			// ///////所有action一定要在boss读取的上面完成，否则boss无法执行这个动作

			sheet = workbook.getSheetAt(怪物掉落集合_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				if (row != null) {
					ArrayList<FlopSet> flopSets = new ArrayList<FlopSet>();
					ArrayList<Integer> ints = new ArrayList<Integer>();
					r = 设置怪物掉落集合(flopSets, ints, sheet, r, rows);
					HSSFCell hc = row.getCell(怪物掉落集合_怪物名_列);
					String 怪物名 = (hc.getStringCellValue().trim());

					hc = row.getCell(怪物掉落集合_集合flopFormat_列);
					byte format = 0;
					try {
						format = (byte) hc.getNumericCellValue();
					} catch (Exception ex) {
						format = Byte.parseByte(hc.getStringCellValue().trim());
					}
					monster2FlopTypeMap.put(怪物名, format);
					monster2FlopListMap.put(怪物名, flopSets.toArray(new FlopSet[0]));
					monster2FlopListProbabilityMap.put(怪物名, ints.toArray(new Integer[0]));
//					 System.out.println("加载怪物掉落 "+怪物名);
				}
			}

			sheet = workbook.getSheetAt(一般monster所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				Monster monster = new Monster();
				if (row != null) {
					r = 一般monster属性设置(monster, sheet, r, rows);
					设置sprite属性值(monster);
				}
				MonsterTempalte nt = new MonsterTempalte(monster.getSpriteCategoryId(), monster);
				templates.put(monster.getSpriteCategoryId(), nt);
				monsterName.put(monster.getName(), monster.getSpriteCategoryId());
//				 System.out.println("加载monster模板 ["+monster.getName()+"] ["+monster.getSpriteCategoryId()+"]");
			}

			sheet = workbook.getSheetAt(boss所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				BossMonster monster = new BossMonster();
				if (row != null) {
					r = 一般monster属性设置(monster, sheet, r, rows);

					HSSFCell hc = row.getCell(一般monster属性_消失时间_列);
					monster.setDisappearTime(hc == null ? -1 : ((long) hc.getNumericCellValue()));
					hc = row.getCell(一般monster属性_掉落等级限制_列);
					monster.setDropLevelLimit(hc == null ? -1 : ((int) hc.getNumericCellValue()));

					MemoryMonsterManager.logger.warn("[怪物:" + monster.getName() + "] [消失时间:" + monster.getDisappearTime() + "] [掉落限制:" + monster.getDropLevelLimit() + "]");

					设置sprite属性值(monster);
					ArrayList<BossExecuteItem> items = new ArrayList<BossExecuteItem>();
					r = 设置monster的ai(items, sheet, r, rows);
					monster.setItems(items.toArray(new BossExecuteItem[0]));
				}
				MonsterTempalte nt = new MonsterTempalte(monster.getSpriteCategoryId(), monster);
				templates.put(monster.getSpriteCategoryId(), nt);
				monsterName.put(monster.getName(), monster.getSpriteCategoryId());
				// System.out.println("加载monster模板 ["+monster.getName()+"] ["+monster.getSpriteCategoryId()+"]");
			}

			sheet = workbook.getSheetAt(等级掉落_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				if (row != null) {
					ArrayList<FlopSet> flopSets = new ArrayList<FlopSet>();
					ArrayList<Integer> ints = new ArrayList<Integer>();
					r = 设置等级掉落集合(flopSets, ints, sheet, r, rows);
					HSSFCell hc = row.getCell(等级掉落_最低级别_列);
					int 最低级别 = 0;
					try {
						最低级别 = Integer.parseInt(hc.getStringCellValue().trim());
					} catch (Exception ex) {
						最低级别 = (int) hc.getNumericCellValue();
					}
					hc = row.getCell(等级掉落_最高级别_列);
					int 最高级别 = 0;
					try {
						最高级别 = Integer.parseInt(hc.getStringCellValue().trim());
					} catch (Exception ex) {
						最高级别 = (int) hc.getNumericCellValue();
					}
					int[] levels = new int[] { 最低级别, 最高级别 };
					level2FlopListMap.put(levels, flopSets.toArray(new FlopSet[0]));
					level2FlopListProbabilityMap.put(levels, ints.toArray(new Integer[0]));
					// System.out.println("加载等级掉落 " + levels[0] + " " + levels[1]);
				}
			}

			sheet = workbook.getSheetAt(地图掉落_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				if (row != null) {
					ArrayList<FlopSet> flopSets = new ArrayList<FlopSet>();
					ArrayList<Integer> ints = new ArrayList<Integer>();
					r = 设置地图掉落集合(flopSets, ints, sheet, r, rows);
					HSSFCell hc = row.getCell(地图掉落_地图名_列);
					String 地图名 = hc.getStringCellValue().trim();
					map2FlopListMap.put(地图名, flopSets.toArray(new FlopSet[0]));
					map2FlopListProbabilityMap.put(地图名, ints.toArray(new Integer[0]));
				}
			}
			logger.debug("[怪物加载] [] []");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("出错:", e);
		}
	}

	public void loadExpFrom(InputStream is) throws Exception {
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);

		HSSFSheet sheet = workbook.getSheetAt(怪物经验表sheet);
		int rows = sheet.getPhysicalNumberOfRows();
		HashMap<Integer, Long> levelExpMap = new HashMap<Integer, Long>();
		for (int r = 1; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			if (row != null) {
				HSSFCell hc = row.getCell(怪物等级列);
				int level = (int) hc.getNumericCellValue();
				hc = row.getCell(怪物经验列);
				long exp = (long) hc.getNumericCellValue();
				levelExpMap.put(level, exp);
			}
		}
		MemoryMonsterManager.levelExpMap = levelExpMap;
	}

	/**
	 * 返回当前运行到的行数
	 * 比如只运行了第一行，那么返回0
	 * @param monster
	 * @param sheet
	 * @param rowNum
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	public int 一般monster属性设置(Monster monster, HSSFSheet sheet, int rowNum, int rows) throws Exception {
		HSSFRow row = sheet.getRow(rowNum);
		if (row != null) {
			HSSFCell hc = row.getCell(一般monster属性_monsterCategoryId_列);
			try {
				int monsterCategoryId = Integer.parseInt(hc.getStringCellValue().trim());
				monster.setSpriteCategoryId(monsterCategoryId);
			} catch (Exception ex) {
				int monsterCategoryId = (int) hc.getNumericCellValue();
				monster.setSpriteCategoryId(monsterCategoryId);
			}

			hc = row.getCell(一般monster属性_title_列);
			try {
				String title = (hc.getStringCellValue().trim());
				monster.setTitle(title);
			} catch (Exception ex) {
				monster.setTitle("");
			}

			hc = row.getCell(一般monster属性_name_列);
			try {
				String name = (hc.getStringCellValue().trim());
				monster.setName(name);
			} catch (Exception ex) {
				throw new Exception(sheet.getSheetName() + "" + rowNum + "行" + "异常");
			}

			hc = row.getCell(一般monster属性_monsterCareer_列);
			try {
				byte monsterCareer = Byte.parseByte(hc.getStringCellValue().trim());
				monster.setCareer(monsterCareer);
			} catch (Exception ex) {
				try {
					byte monsterCareer = (byte) hc.getNumericCellValue();
					monster.setCareer(monsterCareer);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_icon_列);
			try {
				String icon = hc.getStringCellValue().trim();
				monster.icon = icon;
			} catch (Exception ex) {

			}

			hc = row.getCell(一般monster属性_classLevel);
			try {
				short classLevel = (short) hc.getNumericCellValue();
				monster.setClassLevel(classLevel);
			} catch (Exception ex) {

			}

			hc = row.getCell(一般monster属性_monsterMark_列);
			try {
				double monsterMark = Double.parseDouble(hc.getStringCellValue().trim());
				monster.setMonsterMark(monsterMark);
			} catch (Exception ex) {
				try {
					double monsterMark = (double) hc.getNumericCellValue();
					monster.setMonsterMark(monsterMark);
				} catch (Exception e) {
					throw e;
				}
			}

			hc = row.getCell(一般monster属性_hpMark_列);
			try {
				double hpMark = Double.parseDouble(hc.getStringCellValue().trim());
				monster.setHpMark(hpMark);
			} catch (Exception ex) {
				try {
					double hpMark = (double) hc.getNumericCellValue();
					monster.setHpMark(hpMark);
				} catch (Exception e) {
					throw e;
				}
			}

			hc = row.getCell(一般monster属性_apMark_列);
			try {
				double apMark = Double.parseDouble(hc.getStringCellValue().trim());
				monster.setApMark(apMark);
			} catch (Exception ex) {
				try {
					double apMark = (double) hc.getNumericCellValue();
					monster.setApMark(apMark);
				} catch (Exception e) {
					throw e;
				}
			}

			hc = row.getCell(一般monster属性_color_列);
			try {
				int color = (int) hc.getNumericCellValue();
				monster.setColor(color);
				monster.objectColor = monster.getColor();
			} catch (Exception ex) {
				ex.printStackTrace();
				Game.logger.warn("[读取怪物颜色1] [异常] [monster:"+monster.getName()+"]",ex);
				try {
					int color = Integer.parseInt(hc.getStringCellValue().trim());
					monster.setColor(color);
					monster.objectColor = monster.getColor();
				} catch (Exception e) {
					e.printStackTrace();
					Game.logger.warn("[读取怪物颜色2] [异常] [monster:"+monster.getName()+"]",e);
				}
			}

			hc = row.getCell(一般monster属性_level_列);
			try {
				int level = Integer.parseInt(hc.getStringCellValue().trim());
				monster.setLevel(level);
			} catch (Exception ex) {
				int level = (int) hc.getNumericCellValue();
				monster.setLevel(level);
			}

			hc = row.getCell(一般monster属性_height_列);
			try {
				int height = Integer.parseInt(hc.getStringCellValue().trim());
				monster.setHeight(height);
			} catch (Exception ex) {
				try {
					int height = (int) hc.getNumericCellValue();
					monster.setHeight(height);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_avataRace_列);
			try {
				String avataRace = hc.getStringCellValue().trim();
				monster.setAvataRace(avataRace);
			} catch (Exception ex) {
				monster.setAvataRace("");
			}

			hc = row.getCell(一般monster属性_avataSex_列);
			try {
				String avataSex = hc.getStringCellValue().trim();
				monster.setAvataSex(avataSex);
			} catch (Exception ex) {
				monster.setAvataSex("");
			}

			hc = row.getCell(一般monster属性_putOnEquipmentAvata_列);
			try {
				String putOnEquipmentAvata = hc.getStringCellValue().trim();
				monster.setPutOnEquipmentAvata(putOnEquipmentAvata);
			} catch (Exception ex) {
				monster.setPutOnEquipmentAvata("");
			}

			hc = row.getCell(一般monster属性_spriteType_列);
			try {
				byte spriteType = Byte.parseByte(hc.getStringCellValue().trim());
				monster.setSpriteType(spriteType);
			} catch (Exception ex) {
				try {
					byte spriteType = (byte) hc.getNumericCellValue();
					monster.setSpriteType(spriteType);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_monsterType_列);
			try {
				byte monsterType = Byte.parseByte(hc.getStringCellValue().trim());
				monster.setMonsterType(monsterType);
			} catch (Exception ex) {
				try {
					byte monsterType = (byte) hc.getNumericCellValue();
					monster.setMonsterType(monsterType);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_commonAttackSpeed_列);
			try {
				int commonAttackSpeed = Integer.parseInt(hc.getStringCellValue().trim());
				monster.setCommonAttackSpeed(commonAttackSpeed);
			} catch (Exception ex) {
				try {
					int commonAttackSpeed = (int) hc.getNumericCellValue();
					monster.setCommonAttackSpeed(commonAttackSpeed);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_commonAttackRange_列);
			try {
				int commonAttackRange = Integer.parseInt(hc.getStringCellValue().trim());
				monster.setCommonAttackRange(commonAttackRange);
			} catch (Exception ex) {
				try {
					int commonAttackRange = (int) hc.getNumericCellValue();
					monster.setCommonAttackRange(commonAttackRange);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_hpRecoverBase_列);
			try {
				int hpRecoverBase = Integer.parseInt(hc.getStringCellValue().trim());
				monster.setHpRecoverBase(hpRecoverBase);
			} catch (Exception ex) {
				try {
					int hpRecoverBase = (int) hc.getNumericCellValue();
					monster.setHpRecoverBase(hpRecoverBase);
				} catch (Exception e) {

				}
			}

			// hc = row.getCell(一般monster属性_moveSpeed_列);
			// try{
			// int moveSpeed = Integer.parseInt(hc.getStringCellValue().trim());
			// monster.setMoveSpeed(moveSpeed);
			// }catch(Exception ex){
			// int moveSpeed = (int)hc.getNumericCellValue();
			// monster.setMoveSpeed(moveSpeed);
			// }

			hc = row.getCell(一般monster属性_icon_列);
			try {
				String icon = hc.getStringCellValue().trim();
				monster.icon = icon;
			} catch (Exception ex) {

			}

			hc = row.getCell(一般monster属性_classLevel);
			try {
				short classLevel = (short) hc.getNumericCellValue();
				monster.setClassLevel(classLevel);
			} catch (Exception ex) {

			}

			hc = row.getCell(一般monster属性_WeaponDamage_列);
			try {
				int WeaponDamage = Integer.parseInt(hc.getStringCellValue().trim());
				monster.setWeaponDamage(WeaponDamage);
			} catch (Exception ex) {
				try {
					int WeaponDamage = (int) hc.getNumericCellValue();
					monster.setWeaponDamage(WeaponDamage);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_TraceType_列);
			try {
				int TraceType = Integer.parseInt(hc.getStringCellValue().trim());
				monster.setTraceType(TraceType);
			} catch (Exception ex) {
				try {
					int TraceType = (int) hc.getNumericCellValue();
					monster.setTraceType(TraceType);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_PatrolingHeight_列);
			try {
				int PatrolingHeight = Integer.parseInt(hc.getStringCellValue().trim());
				monster.setPatrolingHeight(PatrolingHeight);
			} catch (Exception ex) {
				try {
					int PatrolingHeight = (int) hc.getNumericCellValue();
					monster.setPatrolingHeight(PatrolingHeight);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_BackBornPointMoveSpeedPercent_列);
			try {
				int BackBornPointMoveSpeedPercent = Integer.parseInt(hc.getStringCellValue().trim());
				monster.setBackBornPointMoveSpeedPercent(BackBornPointMoveSpeedPercent);
			} catch (Exception ex) {
				try {
					int BackBornPointMoveSpeedPercent = (int) hc.getNumericCellValue();
					monster.setBackBornPointMoveSpeedPercent(BackBornPointMoveSpeedPercent);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_ActivationHeight_列);
			try {
				int ActivationHeight = Integer.parseInt(hc.getStringCellValue().trim());
				monster.setActivationHeight(ActivationHeight);
			} catch (Exception ex) {
				try {
					int ActivationHeight = (int) hc.getNumericCellValue();
					monster.setActivationHeight(ActivationHeight);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_BackBornPointHp_列);
			try {
				int BackBornPointHp = Integer.parseInt(hc.getStringCellValue().trim());
				monster.setBackBornPointHp(BackBornPointHp);
			} catch (Exception ex) {
				try {
					int BackBornPointHp = (int) hc.getNumericCellValue();
					monster.setBackBornPointHp(BackBornPointHp);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_PatrolingWidth_列);
			try {
				int PatrolingWidth = Integer.parseInt(hc.getStringCellValue().trim());
				monster.setPatrolingWidth(PatrolingWidth);
			} catch (Exception ex) {
				try {
					int PatrolingWidth = (int) hc.getNumericCellValue();
					monster.setPatrolingWidth(PatrolingWidth);
				} catch (Exception e) {

				}
			}

			// hc = row.getCell(一般monster属性_CommonAtta3Speed_列);
			// try{
			// int CommonAtta3Speed = Integer.parseInt(hc.getStringCellValue().trim());
			// monster.setCommonAtta3Speed(CommonAtta3Speed);
			// }catch(Exception ex){
			// try{
			// int CommonAtta3Speed = (int)hc.getNumericCellValue();
			// monster.setCommonAtta3Speed(CommonAtta3Speed);
			// }catch(Exception e){
			//
			// }
			// }

			hc = row.getCell(一般monster属性_PursueHeight_列);
			try {
				int PursueHeight = Integer.parseInt(hc.getStringCellValue().trim());
				monster.setPursueHeight(PursueHeight);
			} catch (Exception ex) {
				try {
					int PursueHeight = (int) hc.getNumericCellValue();
					monster.setPursueHeight(PursueHeight);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_DeadLastingTime_列);
			try {
				long DeadLastingTime = Long.parseLong(hc.getStringCellValue().trim());
				monster.setDeadLastingTime(DeadLastingTime);
			} catch (Exception ex) {
				try {
					long DeadLastingTime = (long) hc.getNumericCellValue();
					monster.setDeadLastingTime(DeadLastingTime);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_FlushFrequency_列);
			try {
				long FlushFrequency = Long.parseLong(hc.getStringCellValue().trim());
				monster.setFlushFrequency(FlushFrequency);
			} catch (Exception ex) {
				try {
					long FlushFrequency = (long) hc.getNumericCellValue();
					monster.setFlushFrequency(FlushFrequency);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_ActivationType_列);
			try {
				int ActivationType = Integer.parseInt(hc.getStringCellValue().trim());
				monster.setActivationType(ActivationType);
			} catch (Exception ex) {
				try {
					int ActivationType = (int) hc.getNumericCellValue();
					monster.setActivationType(ActivationType);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_MoveSpeedA_列);
			try {
				int MoveSpeedA = Integer.parseInt(hc.getStringCellValue().trim());
				monster.setSpeedA(MoveSpeedA);
			} catch (Exception ex) {
				try {
					int MoveSpeedA = (int) hc.getNumericCellValue();
					monster.setSpeedA(MoveSpeedA);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_ActiveSkillIds_列);
			try {
				String ActiveSkillIds = hc.getStringCellValue().trim();
				String[] ss = ActiveSkillIds.split(",");
				int[] skills = new int[ss.length];
				for (int i = 0; i < ss.length; i++) {
					String s = ss[i];
					skills[i] = Integer.parseInt(s);
				}
				monster.setActiveSkillIds(skills);
			} catch (Exception ex) {
				try {
					int ActiveSkillIds = (int) hc.getNumericCellValue();
					monster.setActiveSkillIds(new int[] { ActiveSkillIds });
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_ActiveSkillLevels_列);
			try {
				String ActiveSkillLevels = hc.getStringCellValue().trim();
				String[] ss = ActiveSkillLevels.split(",");
				int[] skills = new int[ss.length];
				for (int i = 0; i < ss.length; i++) {
					String s = ss[i];
					skills[i] = Integer.parseInt(s);
				}
				monster.setActiveSkillLevels(skills);
			} catch (Exception ex) {
				try {
					int ActiveSkillLevels = (int) hc.getNumericCellValue();
					monster.setActiveSkillLevels(new int[] { ActiveSkillLevels });
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_ActivationWidth_列);
			try {
				int ActivationWidth = Integer.parseInt(hc.getStringCellValue().trim());
				monster.setActivationWidth(ActivationWidth);
			} catch (Exception ex) {
				try {
					int ActivationWidth = (int) hc.getNumericCellValue();
					monster.setActivationWidth(ActivationWidth);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_PursueWidth_列);
			try {
				int PursueWidth = Integer.parseInt(hc.getStringCellValue().trim());
				monster.setPursueWidth(PursueWidth);
			} catch (Exception ex) {
				try {
					int PursueWidth = (int) hc.getNumericCellValue();
					monster.setPursueWidth(PursueWidth);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_PatrolingTimeInterval_列);
			try {
				long PatrolingTimeInterval = Long.parseLong(hc.getStringCellValue().trim());
				monster.setPatrolingTimeInterval(PatrolingTimeInterval);
			} catch (Exception ex) {
				try {
					long PatrolingTimeInterval = (long) hc.getNumericCellValue();
					monster.setPatrolingTimeInterval(PatrolingTimeInterval);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般monster属性_objectScale_列);
			try {
				short objectScale = (short) hc.getNumericCellValue();
				monster.setObjectScale(objectScale);
			} catch (Exception ex) {

			}

			// hc = row.getCell(一般monster属性_flopFormat_列);
			// try{
			// byte flopFormat = Byte.parseByte(hc.getStringCellValue().trim());
			// monster.setFlopFormat(flopFormat);
			// }catch(Exception ex){
			// try{
			// byte flopFormat = (byte)hc.getNumericCellValue();
			// monster.setFlopFormat(flopFormat);
			// }catch(Exception e){
			//
			// }
			// }
			byte flopFormat = 0;
			try {
				flopFormat = monster2FlopTypeMap.get(monster.getName());
			} catch (Exception e) {
				// System.out.println("monster2FlopTypeMap [" + monster.getName() + "] 为空");
			}
			monster.setFlopFormat(flopFormat);
			monster.setFsList(monster2FlopListMap.get(monster.getName()));
			monster.setFsProbabilitis(monster2FlopListProbabilityMap.get(monster.getName()));
		}
		return rowNum;
	}

	ReadSpriteExcelManager rem;

	public ReadSpriteExcelManager getRem() {
		return rem;
	}

	public void setRem(ReadSpriteExcelManager rem) {
		this.rem = rem;
	}

	public void 设置sprite属性值(Monster sprite) {
		int level = sprite.getLevel();
		byte career = sprite.getCareer();
		ReadSpriteExcelManager rem = ReadSpriteExcelManager.getInstance();
		int[] values = rem.careerExcelDatas[career - 1][level - 1];
		for (int i = 0; i < values.length; i++) {
			int value = values[i];
			// 还需要根据monsterMark和hpMark和apMark来具体决定数值
			if (value != 0) {
				value = (int) (value * sprite.getMonsterMark());
				switch (i) {
				case 0:
					value = (int) (value * (1 + sprite.getHpMark()));
					if (value <= 0) {
						System.out.println("[怪物配置出错] [" + sprite.getName() + "] [血量出问题] [配置表的hp:" + values[i] + "] [hp:" + value + "] [monsterMark:" + sprite.getMonsterMark() + "] [hpMark:" + sprite.getHpMark() + "]");
						value = 200000000;
					}
					sprite.setMaxHPA(value);
					break;
				case 1:
					value = (int) (value * (1 + sprite.getApMark()));
					sprite.setPhyAttackA(value);
					break;
				case 2:
					value = (int) (value * (1 + sprite.getApMark()));
					sprite.setMagicAttackA(value);
					break;
				case 3:
					sprite.setPhyDefenceA(value);
					break;
				case 4:
					sprite.setMagicDefenceA(value);
					break;
				case 5:
					sprite.setMaxMPA(value);
					break;
				case 6:
					sprite.setBreakDefenceA(value);
					break;
				case 7:
					sprite.setHitA(value);
					break;
				case 8:
					sprite.setDodgeA(value);
					break;
				case 9:
					sprite.setAccurateA(value);
					break;
				case 10:
					sprite.setCriticalHitA(value);
					break;
				case 11:
					sprite.setCriticalDefenceA(value);
					break;
				case 12:
					sprite.setFireAttackA(value);
					break;
				case 13:
					sprite.setBlizzardAttackA(value);
					break;
				case 14:
					sprite.setWindAttackA(value);
					break;
				case 15:
					sprite.setThunderAttackA(value);
					break;
				case 16:
					sprite.setFireDefenceA(value);
					break;
				case 17:
					sprite.setBlizzardDefenceA(value);
					break;
				case 18:
					sprite.setWindDefenceA(value);
					break;
				case 19:
					sprite.setThunderDefenceA(value);
					break;
				case 20:
					sprite.setFireIgnoreDefenceA(value);
					break;
				case 21:
					sprite.setBlizzardIgnoreDefenceA(value);
					break;
				case 22:
					sprite.setWindIgnoreDefenceA(value);
					break;
				case 23:
					sprite.setThunderIgnoreDefenceA(value);
					break;
				}
			}
		}

	}

	/**
	 * 返回当前运行到的行数
	 * 比如只运行了第一行，那么返回0
	 * @param monster
	 * @param sheet
	 * @param rowNum
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	public int 设置monster的ai(ArrayList<BossExecuteItem> items, HSSFSheet sheet, int rowNum, int maxNum) {
		int start = rowNum;
		for (int i = start; i < maxNum; i++) {
			if (i != start) {
				HSSFRow row = sheet.getRow(i);
				try {
					HSSFCell hc = row.getCell(一般monster属性_monsterCategoryId_列);
					if (hc.getNumericCellValue() != 0) {
						return i - 1;
					}
				} catch (Exception ex) {

				}
			}
			HSSFRow row = sheet.getRow(i);
			BossExecuteItem item = new BossExecuteItem();
			boolean has = false;
			HSSFCell hc = row.getCell(一般monster属性_exeTimeLimit_列);
			try {
				long exeTimeLimit = Long.parseLong(hc.getStringCellValue().trim());
				item.setExeTimeLimit(exeTimeLimit);
				has = true;
			} catch (Exception ex) {
				long exeTimeLimit = (long) hc.getNumericCellValue();
				item.setExeTimeLimit(exeTimeLimit);
				has = true;
			}

			hc = row.getCell(一般monster属性_minDistance_列);
			try {
				int minDistance = Integer.parseInt(hc.getStringCellValue().trim());
				item.setMinDistance(minDistance);
				has = true;
			} catch (Exception ex) {
				int minDistance = (int) hc.getNumericCellValue();
				item.setMinDistance(minDistance);
				has = true;
			}

			hc = row.getCell(一般monster属性_maxDistance_列);
			try {
				int maxDistance = Integer.parseInt(hc.getStringCellValue().trim());
				item.setMaxDistance(maxDistance);
				has = true;
			} catch (Exception ex) {
				int maxDistance = (int) hc.getNumericCellValue();
				item.setMaxDistance(maxDistance);
				has = true;
			}

			hc = row.getCell(一般monster属性_hpPercent_列);
			try {
				int hpPercent = Integer.parseInt(hc.getStringCellValue().trim());
				item.setHpPercent(hpPercent);
				has = true;
			} catch (Exception ex) {
				int hpPercent = (int) hc.getNumericCellValue();
				item.setHpPercent(hpPercent);
				has = true;
			}

			hc = row.getCell(一般monster属性_maxExeTimes_列);
			try {
				int maxExeTimes = Integer.parseInt(hc.getStringCellValue().trim());
				item.setMaxExeTimes(maxExeTimes);
				has = true;
			} catch (Exception ex) {
				int maxExeTimes = (int) hc.getNumericCellValue();
				item.setMaxExeTimes(maxExeTimes);
				has = true;
			}

			hc = row.getCell(一般monster属性_exeTimeGap_列);
			try {
				long exeTimeGap = Long.parseLong(hc.getStringCellValue().trim());
				item.setExeTimeGap(exeTimeGap);
				has = true;
			} catch (Exception ex) {
				long exeTimeGap = (long) hc.getNumericCellValue();
				item.setExeTimeGap(exeTimeGap);
				has = true;
			}

			hc = row.getCell(一般monster属性_ignoreMoving_列);
			try {
				boolean ignoreMoving = hc.getBooleanCellValue();
				item.setIgnoreMoving(ignoreMoving);
				has = true;
			} catch (Exception ex) {

			}

			hc = row.getCell(一般monster属性_actionId_列);
			try {
				int actionId = Integer.parseInt(hc.getStringCellValue().trim());
				item.setActionId(actionId);
				has = true;
			} catch (Exception ex) {
				int actionId = (int) hc.getNumericCellValue();
				item.setActionId(actionId);
				has = true;
			}
			if (has) {
				if (bossActionMap.get(item.getActionId()) != null) {
					item.setAction(bossActionMap.get(item.getActionId()));
				}
				items.add(item);
			}
		}

		return maxNum - 1;
	}

	/**
	 * 返回当前运行到的行数
	 * 比如只运行了第一行，那么返回0
	 * @param monster
	 * @param sheet
	 * @param rowNum
	 * @param rows
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public int 设置等级掉落集合(ArrayList<FlopSet> flopSets, ArrayList<Integer> ints, HSSFSheet sheet, int rowNum, int maxNum) throws Exception {
		int start = rowNum;
		for (int i = start; i < maxNum; i++) {
			if (i != start) {
				HSSFRow row = sheet.getRow(i);
				try {
					HSSFCell hc = row.getCell(等级掉落_最低级别_列);
					if (hc.getNumericCellValue() != 0) {
						return i - 1;
					}
				} catch (Exception ex) {

				}
			}
			HSSFRow row = sheet.getRow(i);
			FlopSet fs = new FlopSet();
			boolean has = false;
			HSSFCell hc = row.getCell(等级掉落_flopType_列);
			try {
				byte flopType = Byte.parseByte(hc.getStringCellValue().trim());
				fs.setFlopType(flopType);
				has = true;
			} catch (Exception ex) {
				byte flopType = (byte) hc.getNumericCellValue();
				fs.setFlopType(flopType);
				has = true;
			}

			hc = row.getCell(等级掉落_flopFormat_列);
			try {
				byte flopFormat = Byte.parseByte(hc.getStringCellValue().trim());
				fs.setFlopFormat(flopFormat);
				has = true;
			} catch (Exception ex) {
				byte flopFormat = (byte) hc.getNumericCellValue();
				fs.setFlopFormat(flopFormat);
				has = true;
			}

			hc = row.getCell(等级掉落_color_列);
			try {
				int color = Integer.parseInt(hc.getStringCellValue().trim());
				fs.setColor(color);
				has = true;
			} catch (Exception ex) {
				int color = (int) hc.getNumericCellValue();
				fs.setColor(color);
				has = true;
			}

			hc = row.getCell(等级掉落_articles_列);
			String[] articles = null;
			try {
				String articless = (hc.getStringCellValue().trim());
				String a = ";";
				if (articless.indexOf(a) <= 0) {
					a = "；";
				}
				articles = articless.split(a);
				fs.setArticles(articles);
				has = true;
			} catch (Exception ex) {
				throw ex;
			}

			hc = row.getCell(等级掉落_floprates_列);
			try {
				String flopratesss = hc.getStringCellValue().trim();
				String a = ";";
				if (flopratesss.indexOf(a) <= 0) {
					a = "；";
				}
				String[] flopratess = flopratesss.split(a);
				int[] floprates = new int[articles.length];
				for (int j = 0; j < flopratess.length && j < articles.length; j++) {
					floprates[j] = Integer.parseInt(flopratess[j]);
				}
				if (articles.length > flopratess.length) {
					for (int j = flopratess.length; j < articles.length; j++) {
						floprates[j] = Short.parseShort(flopratess[flopratess.length - 1]);
					}
				}

				fs.setFloprates(floprates);
				has = true;
			} catch (Exception ex) {
				int[] floprates = new int[articles.length];
				for (int j = 0; j < articles.length; j++) {
					floprates[j] = (int) hc.getNumericCellValue();
				}
				fs.setFloprates(floprates);
				has = true;
			}
			if (has) {
				flopSets.add(fs);
				hc = row.getCell(等级掉落_几率值_列);
				try {
					int 几率值 = Integer.parseInt(hc.getStringCellValue().trim());
					ints.add(几率值);
				} catch (Exception ex) {
					int 几率值 = (int) hc.getNumericCellValue();
					ints.add(几率值);
				}
			}
		}

		return maxNum - 1;
	}

	/**
	 * 返回当前运行到的行数
	 * 比如只运行了第一行，那么返回0
	 * @param monster
	 * @param sheet
	 * @param rowNum
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	public int 设置地图掉落集合(ArrayList<FlopSet> flopSets, ArrayList<Integer> ints, HSSFSheet sheet, int rowNum, int maxNum) {
		int start = rowNum;
		for (int i = start; i < maxNum; i++) {
			if (i != start) {
				HSSFRow row = sheet.getRow(i);
				try {
					HSSFCell hc = row.getCell(地图掉落_地图名_列);
					if (hc.getStringCellValue().trim() != null && !hc.getStringCellValue().trim().equals("")) {
						return i - 1;
					}
				} catch (Exception ex) {

				}
			}
			HSSFRow row = sheet.getRow(i);
			FlopSet fs = new FlopSet();
			boolean has = false;
			HSSFCell hc = row.getCell(地图掉落_flopType_列);
			try {
				byte flopType = Byte.parseByte(hc.getStringCellValue().trim());
				fs.setFlopType(flopType);
				has = true;
			} catch (Exception ex) {
				byte flopType = (byte) hc.getNumericCellValue();
				fs.setFlopType(flopType);
				has = true;
			}

			hc = row.getCell(地图掉落_flopFormat_列);
			try {
				byte flopFormat = Byte.parseByte(hc.getStringCellValue().trim());
				fs.setFlopFormat(flopFormat);
				has = true;
			} catch (Exception ex) {
				byte flopFormat = (byte) hc.getNumericCellValue();
				fs.setFlopFormat(flopFormat);
				has = true;
			}

			hc = row.getCell(地图掉落_color_列);
			try {
				int color = Integer.parseInt(hc.getStringCellValue().trim());
				fs.setColor(color);
				has = true;
			} catch (Exception ex) {
				int color = (int) hc.getNumericCellValue();
				fs.setColor(color);
				has = true;
			}

			hc = row.getCell(地图掉落_articles_列);
			String[] articles = null;
			try {
				String articless = hc.getStringCellValue().trim();
				String a = ";";
				if (articless.indexOf(a) <= 0) {
					a = "；";
				}
				articles = articless.split(a);
				fs.setArticles(articles);
				has = true;
			} catch (Exception ex) {

			}

			hc = row.getCell(地图掉落_floprates_列);
			try {
				String flopratesss = hc.getStringCellValue().trim();
				String a = ";";
				if (flopratesss.indexOf(a) <= 0) {
					a = "；";
				}
				String[] flopratess = flopratesss.split(a);
				int[] floprates = new int[articles.length];
				for (int j = 0; j < flopratess.length && j < articles.length; j++) {
					floprates[j] = Integer.parseInt(flopratess[j]);
				}
				if (articles.length > flopratess.length) {
					for (int j = flopratess.length; j < articles.length; j++) {
						floprates[j] = Short.parseShort(flopratess[flopratess.length - 1]);
					}
				}

				fs.setFloprates(floprates);
				has = true;
			} catch (Exception ex) {
				int[] floprates = new int[articles.length];
				for (int j = 0; j < articles.length; j++) {
					floprates[j] = (int) hc.getNumericCellValue();
				}
				fs.setFloprates(floprates);
				has = true;
			}
			if (has) {
				flopSets.add(fs);
				hc = row.getCell(地图掉落_几率值_列);
				try {
					int 几率值 = Integer.parseInt(hc.getStringCellValue().trim());
					ints.add(几率值);
				} catch (Exception ex) {
					int 几率值 = (int) hc.getNumericCellValue();
					ints.add(几率值);
				}
			}
		}

		return maxNum - 1;
	}

	/**
	 * 返回当前运行到的行数
	 * 比如只运行了第一行，那么返回0
	 * @param monster
	 * @param sheet
	 * @param rowNum
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	public int 设置怪物掉落集合(ArrayList<FlopSet> flopSets, ArrayList<Integer> ints, HSSFSheet sheet, int rowNum, int maxNum) {
		int start = rowNum;
		for (int i = start; i < maxNum; i++) {
			if (i != start) {
				HSSFRow row = sheet.getRow(i);
				try {
					HSSFCell hc = row.getCell(怪物掉落集合_怪物名_列);
					if (hc.getStringCellValue().trim() != null && !hc.getStringCellValue().trim().equals("")) {
						return i - 1;
					}
				} catch (Exception ex) {

				}
			}
			HSSFRow row = sheet.getRow(i);
			FlopSet fs = new FlopSet();
			boolean has = false;
			HSSFCell hc = row.getCell(怪物掉落集合_flopType_列);
			try {
				byte flopType = Byte.parseByte(hc.getStringCellValue().trim());
				fs.setFlopType(flopType);
				has = true;
			} catch (Exception ex) {
				byte flopType = (byte) hc.getNumericCellValue();
				fs.setFlopType(flopType);
				has = true;
			}

			hc = row.getCell(怪物掉落集合_集合内部flopFormat_列);
			try {
				byte flopFormat = Byte.parseByte(hc.getStringCellValue().trim());
				fs.setFlopFormat(flopFormat);
				has = true;
			} catch (Exception ex) {
				byte flopFormat = (byte) hc.getNumericCellValue();
				fs.setFlopFormat(flopFormat);
				has = true;
			}

			hc = row.getCell(怪物掉落集合_color_列);
			try {
				int color = Integer.parseInt(hc.getStringCellValue().trim());
				fs.setColor(color);
				has = true;
			} catch (Exception ex) {
				try {
					int color = (int) hc.getNumericCellValue();
					fs.setColor(color);
					has = true;
				} catch (Exception e) {
					System.out.println("[怪物数据出现异常]" + sheet.getSheetName() + "第" + i + "行");
					int color = (int) hc.getNumericCellValue();
					fs.setColor(color);
					has = true;
				}

			}

			hc = row.getCell(怪物掉落集合_articles_列);
			String[] articles = null;
			try {
				String articless = (hc.getStringCellValue().trim());
				String a = ";";
				if (articless.indexOf(a) <= 0) {
					a = "；";
				}
				articles = articless.split(a);
				fs.setArticles(articles);
				has = true;
			} catch (Exception ex) {

			}

			hc = row.getCell(怪物掉落集合_floprates_列);
			try {
				String flopratesss = hc.getStringCellValue().trim();
				String a = ";";
				if (flopratesss.indexOf(a) <= 0) {
					a = "；";
				}
				String[] flopratess = flopratesss.split(a);
				int[] floprates = new int[articles.length];
				for (int j = 0; j < flopratess.length && j < articles.length; j++) {
					floprates[j] = Integer.parseInt(flopratess[j]);
				}
				if (articles.length > flopratess.length) {
					for (int j = flopratess.length; j < articles.length; j++) {
						floprates[j] = Short.parseShort(flopratess[flopratess.length - 1]);
					}
				}

				fs.setFloprates(floprates);
				has = true;
			} catch (Exception ex) {
				try {
					int[] floprates = new int[articles.length];
					for (int j = 0; j < articles.length; j++) {
						floprates[j] = (int) hc.getNumericCellValue();
					}
					fs.setFloprates(floprates);
					has = true;
				} catch (Exception e) {
					System.out.println("怪物掉落集合" + row.getRowNum() + "行floprates为空");
					int[] floprates = new int[articles.length];
					for (int j = 0; j < articles.length; j++) {
						floprates[j] = (int) hc.getNumericCellValue();
					}
					fs.setFloprates(floprates);
					has = true;
				}

			}
			if (has) {
				flopSets.add(fs);
				hc = row.getCell(怪物掉落集合_几率值_列);
				try {
					int 几率值 = Integer.parseInt(hc.getStringCellValue().trim());
					ints.add(几率值);
				} catch (Exception ex) {
					int 几率值 = (int) hc.getNumericCellValue();
					ints.add(几率值);
				}
			}
		}

		return maxNum - 1;
	}

	/**
	 * 设置一个对象的属性，需要此对象的属性提供setter和getter方法
	 * @param cl
	 * @param o
	 * @param key
	 * @param value
	 * @throws Exception
	 */

	public static void setObjectValue(Class cl, Object o, String key, String value) throws Exception {
		Field f = null;
		Class clazz = cl;
		while (clazz != null && f == null) {
			try {
				f = clazz.getDeclaredField(key);
			} catch (Exception e) {
				clazz = clazz.getSuperclass();
			}
		}
		if (f == null) {
			throw new Exception("怪物类[" + cl.getName() + "]没有属性[" + key + "]，请检查！");
		}

		Class t = f.getType();
		Method m = null;
		try {
			m = cl.getMethod("set" + java.lang.Character.toUpperCase(key.charAt(0)) + key.substring(1), new Class[] { t });
		} catch (Exception e) {
			throw new Exception("怪物类[" + cl.getName() + "]属性[" + key + "]的设置方法[" + ("set" + java.lang.Character.toUpperCase(key.charAt(0)) + key.substring(1) + "(" + t.getName() + ")") + "]，请检查！");
		}
		Object v = null;
		try {
			if (t == Byte.class || t == Byte.TYPE) {
				v = (byte) Integer.parseInt(value);
			} else if (t == Short.class || t == Short.TYPE) {
				v = Short.parseShort(value);
			} else if (t == Integer.class || t == Integer.TYPE) {
				v = Integer.parseInt(value);
			} else if (t == Long.class || t == Long.TYPE) {
				v = Long.parseLong(value);
			} else if (t == Boolean.class || t == Boolean.TYPE) {
				v = Boolean.parseBoolean(value);
			} else if (t == String.class) {
				value = TransferLanguage.transferString(value);
				v = value;
			} else if (t.isArray()) {
				if (value.trim().length() == 0) {
					v = Array.newInstance(t, 0);
				} else {
					String ss[] = value.split(",");
					ArrayList<String> al = new ArrayList<String>();
					for (int i = 0; i < ss.length; i++) {
						if (ss[i].trim().length() > 0) {
							al.add(ss[i].trim());
						}
					}
					ss = al.toArray(new String[0]);
					t = t.getComponentType();
					v = Array.newInstance(t, ss.length);
					for (int i = 0; i < ss.length; i++) {
						Object v2 = null;
						if (t == Byte.class || t == Byte.TYPE) {
							v2 = Byte.parseByte(ss[i]);
						} else if (t == Short.class || t == Short.TYPE) {
							v2 = Short.parseShort(ss[i]);
						} else if (t == Integer.class || t == Integer.TYPE) {
							v2 = Integer.parseInt(ss[i]);
						} else if (t == Long.class || t == Long.TYPE) {
							v2 = Long.parseLong(ss[i]);
						} else if (t == Boolean.class || t == Boolean.TYPE) {
							v2 = Boolean.parseBoolean(ss[i]);
						} else if (t == String.class) {
							v2 = ss[i];
						} else {
							throw new Exception("怪物类[" + cl.getName() + "]属性[" + key + "]为复杂的数据类型【" + t.getName() + "的数组】，暂时不支持！");
						}
						Array.set(v, i, v2);
					}
				}
			} else {
				throw new Exception("怪物类[" + cl.getName() + "]属性[" + key + "]为复杂的数据类型【" + t.getName() + "】，暂时不支持！");
			}
		} catch (NumberFormatException e) {
			throw new Exception("怪物类[" + cl.getName() + "]属性[" + key + "]为数据【" + value + "】解析出错，请检查配置文件！");
		}
		m.invoke(o, new Object[] { v });
	}

	public MonsterTempalte getMonsterTempalteByCategoryId(int categoryId) {
		return templates.get(categoryId);
	}

	/**
	 * 根据一个种类，创建新的一个个体
	 * @param MonsterCategoryId
	 * @return
	 */
	public Monster createMonster(int MonsterCategoryId) {
		MonsterTempalte st = templates.get(MonsterCategoryId);
		if (st == null) return null;
		Monster s = st.newMonster();
		if (s != null) {
			s.setId(Sprite.nextId());
			s.init();
			monsterMap.put(s.getId(), s);
		}
		return s;
	}

	/**
	 * 根据一个种类，创建新的一个个体
	 * @param MonsterCategoryId
	 * @return
	 */
	public Monster createMonster(Monster template) {
		return createMonster(template.getSpriteCategoryId());
	}

	public void removeMonster(Monster s) {
		monsterMap.remove(s.getId());
		s.notifyRemoved();
	}

	public int getAmountOfMonsters() {
		return monsterMap.size();
	}

	public Monster getMonster(long autoId) {
		return monsterMap.get(autoId);
	}

	public Monster getMonster(String monsterName) {
		for (MonsterTempalte template : templates.values()) {
			Monster monster = template.monster;
			if (monster.getName().equals(monsterName)) return monster;
		}
		return null;
	}

	public Monster[] getMonstersByPage(int start, int size) {
		int end = start + size;
		ArrayList<Monster> al = new ArrayList<Monster>();
		Monster ms[] = monsterMap.values().toArray(new Monster[0]);
		for (int i = start; i < end && i < ms.length; i++) {
			Monster p = ms[i];
			al.add(p);
		}
		return al.toArray(new Monster[0]);
	}

	public boolean isExists(int autoId) {
		return getMonster(autoId) != null;
	}

	public MonsterTempalte getMonsterTempalteById(int categoryId) {
		return templates.get(categoryId);
	}

	public MonsterTempalte[] getMonsterTemaplates() {
		return templates.values().toArray(new MonsterTempalte[0]);
	}

	public ArticleManager getArticleManager() {
		return articleManager;
	}

	public void setArticleManager(ArticleManager articleManager) {
		this.articleManager = articleManager;
	}

	public ReadSpriteExcelManager getReadSpriteExcelManager() {
		return readSpriteExcelManager;
	}

	public void setReadSpriteExcelManager(ReadSpriteExcelManager readSpriteExcelManager) {
		this.readSpriteExcelManager = readSpriteExcelManager;
	}

	public LinkedHashMap<Integer, MonsterTempalte> getTemplates() {
		return templates;
	}

	public void setTemplates(LinkedHashMap<Integer, MonsterTempalte> templates) {
		this.templates = templates;
	}

	public LinkedHashMap<String, Byte> getMonster2FlopTypeMap() {
		return monster2FlopTypeMap;
	}

	public void setMonster2FlopTypeMap(LinkedHashMap<String, Byte> monster2FlopTypeMap) {
		this.monster2FlopTypeMap = monster2FlopTypeMap;
	}

	public LinkedHashMap<String, FlopSet[]> getMonster2FlopListMap() {
		return monster2FlopListMap;
	}

	public void setMonster2FlopListMap(LinkedHashMap<String, FlopSet[]> monster2FlopListMap) {
		this.monster2FlopListMap = monster2FlopListMap;
	}

	public LinkedHashMap<String, Integer[]> getMonster2FlopListProbabilityMap() {
		return monster2FlopListProbabilityMap;
	}

	public void setMonster2FlopListProbabilityMap(LinkedHashMap<String, Integer[]> monster2FlopListProbabilityMap) {
		this.monster2FlopListProbabilityMap = monster2FlopListProbabilityMap;
	}

	public LinkedHashMap<String, Integer[]> getMap2FlopListProbabilityMap() {
		return map2FlopListProbabilityMap;
	}

	public void setMap2FlopListProbabilityMap(LinkedHashMap<String, Integer[]> map2FlopListProbabilityMap) {
		this.map2FlopListProbabilityMap = map2FlopListProbabilityMap;
	}

	public LinkedHashMap<int[], Integer[]> getLevel2FlopListProbabilityMap() {
		return level2FlopListProbabilityMap;
	}

	public void setLevel2FlopListProbabilityMap(LinkedHashMap<int[], Integer[]> level2FlopListProbabilityMap) {
		this.level2FlopListProbabilityMap = level2FlopListProbabilityMap;
	}

	public void setMap2FlopListMap(LinkedHashMap<String, FlopSet[]> map2FlopListMap) {
		this.map2FlopListMap = map2FlopListMap;
	}

	public void setLevel2FlopListMap(LinkedHashMap<int[], FlopSet[]> level2FlopListMap) {
		this.level2FlopListMap = level2FlopListMap;
	}

	public static void main(String[] args) throws Exception {
		MemoryMonsterManager mmm = new MemoryMonsterManager();
		mmm.init();
	}

}
