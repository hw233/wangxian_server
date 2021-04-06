package com.fy.engineserver.playerAims.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.GameDataRecord;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.props.HorseProps;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.career.Career;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.datasource.skill.master.SkBean;
import com.fy.engineserver.datasource.skill.master.SkEnhanceManager;
import com.fy.engineserver.datasource.skill.passiveskills.IncreaseFaFangPassiveSkill;
import com.fy.engineserver.datasource.skill.passiveskills.IncreaseFaGongPassiveSkill;
import com.fy.engineserver.datasource.skill.passiveskills.IncreaseHpPassiveSkill;
import com.fy.engineserver.datasource.skill.passiveskills.IncreaseWuFangPassiveSkill;
import com.fy.engineserver.datasource.skill.passiveskills.IncreaseWuGongPassiveSkill;
import com.fy.engineserver.event.Event;
import com.fy.engineserver.event.EventProc;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventOfPlayer;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_PARTICLE_REQ;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.playerAims.clientModel.Article2Client;
import com.fy.engineserver.playerAims.model.ChapterModel;
import com.fy.engineserver.playerAims.model.PlayerAimModel;
import com.fy.engineserver.playerAims.model.RewordArticle;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.playerTitles.PlayerTitle;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.monster.SimpleMonster;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DataBlock;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class PlayerAimManager implements EventProc{
	
	public static Logger logger = AchievementManager.logger;
	public static PlayerAimManager instance;
	/** 配置文件地址 */
	private String fileName;
	/** 目标章节信息(key=章节名) */
	public Map<String, ChapterModel> chapterMaps = new LinkedHashMap<String, ChapterModel>();
	/** 目标信息 (key=目标id)*/
	public Map<Integer, PlayerAimModel> aimMaps = new LinkedHashMap<Integer, PlayerAimModel>();
	/** 系统目标列表 key=目标类型（RecordAction中的类型）*/
	public HashMap<Integer, List<PlayerAimModel>> systemAimsMap = new HashMap<Integer, List<PlayerAimModel>>();
	/** 章节奖励临时物品id Map  key=章节名*/
	public Map<String, List<Article2Client>> chapterRewardMap = new LinkedHashMap<String, List<Article2Client>>();
	/** 每个目标奖励临时物品id Map  key=目标id*/
	public Map<Integer, List<Article2Client>> aimRewardMap = new LinkedHashMap<Integer, List<Article2Client>>();
	/** 其他临时物品（0称号  1绑银  2工资  3功勋） */
	public List<Article2Client> otherTempReward = new ArrayList<Article2Client>();
	/** 称号、绑银、工资、功勋临时物品名 */
	public static String[] tempAname = new String[]{"绑银奖励", "绑银奖励", "工资奖励", "功勋奖励"};
	
	public static int[] tempRate = new int[]{1,1000,1000,1};
	
	public static int[] 特殊处理npcId = new int[]{600146,600147,600160,600163};
	public static int[] 特殊处理npc对应仙录id = new int[]{469,470,471,472};
	public static String[] 特殊处理npc弹窗 = new String[]{Translate.特殊处理NPC对话弹窗1,Translate.特殊处理NPC对话弹窗2,Translate.特殊处理NPC对话弹窗3,Translate.特殊处理NPC对话弹窗4};
	public static RecordAction[] 特殊处理NPC目标 = new RecordAction[]{RecordAction.任务拜访玄武祖师,RecordAction.任务拜访无上玉皇,RecordAction.任务拜访北斗星君,RecordAction.任务拜访林大华};
	
	//0千年妖狐
	public static final int[] 单独统计怪物id = new int[]{100100010, 100100006, 10010518, 10010517,10010519,10010520,10010521, 10010522,
		10010523, 10010524, 10010525, 10010526, 10010527, 10010528,1700072};
	public static final RecordAction[] 对应统计action = new RecordAction[]{RecordAction.杀死千年狐妖, RecordAction.杀死九转元胎, RecordAction.击杀boss增长天, 
		RecordAction.击杀boss广目天, RecordAction.击杀boss多闻天, RecordAction.击杀boss持国天, RecordAction.击杀boss月宫天, RecordAction.击杀boss金刚密迹,
		RecordAction.击杀boss大自在天, RecordAction.击杀boss摩利支天, RecordAction.击杀boss功德天, RecordAction.击杀boss功驮天, RecordAction.击杀boss日宫天,
		RecordAction.击杀boss大梵天,RecordAction.任务暗杀赵公明};
	/** 颜色贴对应统计的action */
	public static final RecordAction[] 颜色贴action = new RecordAction[]{RecordAction.使用白色封魔录次数, null, RecordAction.使用蓝色封魔录次数, null, RecordAction.使用橙色封魔录次数};
	/** 器灵颜色对应action */
	public static final RecordAction[] 器灵颜色action = new RecordAction[]{null, null, RecordAction.装备一个蓝色器灵, RecordAction.装备一个紫色器灵, RecordAction.装备一个橙色器灵};
	/** 器灵练满颜色对应action */
	public static final RecordAction[] 器灵满级颜色action = new RecordAction[]{null, null, RecordAction.练满一个蓝色器灵, RecordAction.练满一个紫色器灵, RecordAction.练满一个橙色器灵};
	/** 进入恶魔城堡action  110,150,190,220 */
	public static final RecordAction[] 进入恶魔城堡action = new RecordAction[]{RecordAction.进入110级恶魔城堡次数, RecordAction.进入150级恶魔城堡次数,RecordAction.进入190级恶魔城堡次数,RecordAction.进入220级恶魔城堡次数};
	/** 宠物升携带等级  45,90,135,180,220 */
	public static final RecordAction[] 升宠物携带等级action = new RecordAction[]{null, RecordAction.宠物携带等级升至90级次数, RecordAction.宠物携带等级升至135级次数, RecordAction.宠物携带等级升至180级次数, RecordAction.宠物携带等级升至220级次数};
	/** 法防和外防心法统计action */
	public static final RecordAction[] 防心法action = new RecordAction[]{RecordAction.内防心法重数, RecordAction.外防心法重数};
	/** 任务集合名对应action */
	public static final String[] 任务集合名 = new String[]{Translate.除妖, Translate.家族建设任务};
	public static final RecordAction[] 任务集合action = new RecordAction[]{RecordAction.完成平定四方次数, RecordAction.完成家族任务次数};
	public static final String[] 任务名 = Translate.家族任务2;
	/** 幽冥幻域action */
	public static final RecordAction[] 幽冥幻域普通action = new RecordAction[]{RecordAction.通天黄泉普通, RecordAction.通关幽冥幻域玄天普通, RecordAction.通关幽冥幻域阴浊普通模式, RecordAction.通关幽冥幻域阳清普通模式, RecordAction.通关幽冥幻域洪荒普通模式, RecordAction.通关幽冥幻域混沌普通模式};
	public static final RecordAction[] 幽冥幻域困难action = new RecordAction[]{RecordAction.通天黄泉困难, RecordAction.通关幽冥幻域玄天困难, RecordAction.通关幽冥幻域阴浊困难模式, RecordAction.通关幽冥幻域阳清困难模式, RecordAction.通关幽冥幻域洪荒困难模式, RecordAction.通关幽冥幻域混沌困难模式};
	public static final RecordAction[] 幽冥幻域深渊action = new RecordAction[]{RecordAction.通天黄泉深渊, RecordAction.通关幽冥幻域玄天深渊, RecordAction.通关幽冥幻域阴浊深渊模式, RecordAction.通关幽冥幻域阳清深渊模式, RecordAction.通关幽冥幻域洪荒深渊模式, RecordAction.通关幽冥幻域混沌深渊模式};
	public static final RecordAction[] 仙界第一职业等级 = new RecordAction[]{RecordAction.仙界第一修罗, RecordAction.仙界第一影魅, RecordAction.仙界第一仙心, RecordAction.仙界第一九黎, RecordAction.仙界第一兽魁};
	
	public static final String[] 称号成就1 = Translate.称号成就1;
	public static final String[] 称号成就2 = Translate.称号成就2;
	public static final String[] 称号成就3 = Translate.称号成就3;
	
	public static final String 金宝蛇皇蛋 = "金宝蛇皇蛋";
	public static final String 金宝蛇皇 = "金宝蛇皇";
	public static final String 黑暗魔龙 = "黑暗魔龙";
	public static final String 光明圣龙 = "光明圣龙";
	public static final String 圣域天龙 = "圣域天龙";
	public static final String 冥域血龙 = "冥域血龙";
	
	public static  String[] 精英怪 = Translate.精英怪;
	public static final int[] 杀死精英怪物成就 = new int[]{100100005,100100006,100100007,100100015,100100016,100100017,100100018,100100019,100100020,
		100100021,10010022,10010023,10010024,10010025,10010027,10010028,10010029,10010030,10010031,10010032,10010033,10010034,
		10010035,10010036};
	public static  String[] 境界怪 = Translate.境界怪;
	public static final int[] 杀死境界怪物成就 = new int[]{100100003,100100004,201003,10010235,10010293,10010236,10010292,10010294,
		10010308,10010414,10010415};
	public static final int[] 击杀仙界俩boss = new int[]{20113374,20113376};
	public static String[] 击杀仙界俩bo = Translate.世界boss2;
	public static String[] 世界boss = Translate.世界boss;
	public static final int[] 杀死漏网的世界boss = new int[]{1001000013,1001000014};
	public static final String[] 小试牛刀怪物 = Translate.小试牛刀怪物;
	public static final int[] 小试牛刀怪物Id = new int[]{20113268, 20113269, 20113270, 20113271, 20113272, 20113273, 20113274, 20113275, 20113276, 20113277, 20113278};
	
	public static String[] 极限任务1 = Translate.极限任务1;
	public static final int[] 极限任务1Id = new int[]{100612, 100613, 100614, 100615, 100616, 100617, 100618, 100619, 100620, 100621, 100622, 100623, 100624, 100625};
	public static String[] 极限任务2 = Translate.极限任务2;
	public static final int[] 极限任务2Id = new int[]{100626, 100627, 100628, 100629, 100630, 100631, 100632, 100633, 100634};
	public static String[] 极限任务3 = Translate.极限任务3;
	public static final int[] 极限任务3Id = new int[]{100635, 100636, 100637};
	public static String[] 极限任务4 = Translate.极限任务4;
	public static final int[] 极限任务4Id = new int[]{100638, 100639, 100640, 100641, 100642};
	public static String[] 极限任务5 = Translate.极限任务5;
	public static final int[] 极限任务5Id = new int[]{100651, 100643, 100644, 100645, 100646, 100647, 100648, 100649, 100650};
	public static String[] 极限任务6 = Translate.极限任务6;
	public static final int[] 极限任务6Id = new int[]{100659, 100652, 100653, 100654, 100655, 100656, 100657, 100658};
	
	
	/** 使用时需要替换  #为点击类型    $为npc所在地图  %npc位置x  ^npc位置y  &npc名字*/
	public static final String replaceStart = "<f onclick='event' size='28' onclickType='*' color='#009cff' map='$' x='%' y='^' name='&'>";
	public static final String replaceEnd = "</f>";
	public static final String nomalStart = "<f  size='28'>";		
	public static final String tempStart = "【";
	public static final String tempEnd = "】";
	
	public static RecordAction[] 需要替换颜色action = new RecordAction[]{RecordAction.击杀漏网的世界BOSS, RecordAction.杀死境界怪物, RecordAction.杀死精英怪};
	
	public static String startColor = "</f><f size='28' color='0x5aff00'>";
	public static String endColor = "</f><f  size='28'>";
	
	private String diskFilePath;
	public DiskCache disk = null;
	public static final String wushengKey = "wushengLiansheng_key";
	public static final String shaguaiKey = "shasiguaiwu_key";
	public static final String renwuKey = "renwuKey_key";
	
	
	public void init() throws Exception {
		
		instance = this;
		initFile();
		initDisk();
		initOtherTempArticle();
		this.doReg();
		ServiceStartRecord.startLog(this);
	}
	
	private void initDisk() throws Exception{
		File file = new File(diskFilePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		disk = new DefaultDiskCache(file, null, "playeraimmanager", 100L * 365 * 24 * 3600 * 1000L);
		List<String> lists = initOtherDisk(file);
		this.deleteOtherDisk(lists);
	}
	
	private void deleteOtherDisk(List<String> list) {
		if (list != null) {
			for (String s : list) {
				File f = new File(s);
				if (f.exists() && !"playerAim.ddc".equals(f.getName())) {
					boolean result = f.delete();
					logger.warn("[导入disk数据] [结束删除文件] [" + s + "] [结果:" + result + "]");
				}
			}
		}
	}
	
	private List<String> initOtherDisk(File file) throws Exception{
		File parentFile = file.getParentFile();
		List<String> list = new ArrayList<String>();
		if (parentFile.listFiles() != null) {
			File files[] = parentFile.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					if (name != null && name.startsWith("playerAim") && name.endsWith(".ddc") && !name.equals("playerAim.ddc")) {
						return true;
					}
					return false;
				}
			});
			if (files != null && files.length > 0) {
				for (File f : files) {
//					DefaultDiskCache d = new DefaultDiskCache(file, null, "tempDisk" + f.getName(), 60L * 1000L);
					DefaultDiskCache d = new DefaultDiskCache(f, null, "tempDisk" + f.getName(), 60L * 1000L);
//					if ("已经导入过此文件".equals(d.get("testInto"))) {
//						continue;
//					}
//					d.put("testInto", "已经导入过此文件");
					Field ff1 = DefaultDiskCache.class.getDeclaredField("hashcodeBlockMap");
					Method m2 = DefaultDiskCache.class.getDeclaredMethod("getReader");
					m2.setAccessible(true);
					Field f2 = DataBlock.class.getDeclaredField("key");
					Field f3 = DataBlock.class.getDeclaredField("value");
					f3.setAccessible(true);
					ff1.setAccessible(true);
					f2.setAccessible(true);
					HashMap<Integer, LinkedList<DataBlock>> hm = (HashMap<Integer, LinkedList<DataBlock>>) ff1.get(d);
					try {
						for (LinkedList<DataBlock> ldb : hm.values()) {
							for (DataBlock dbk : ldb) {
								Method mm = DataBlock.class.getDeclaredMethod("readAllData", RandomAccessFile.class);
								mm.setAccessible(true);
								mm.invoke(dbk, (RandomAccessFile)m2.invoke(d));
								Object key = f2.get(dbk);
								if (key.toString().startsWith("shasiguaiwu")) {
									if (disk.get((Serializable) key) == null) {
										List<Integer> value = (List<Integer>) d.get((Serializable) key);
										disk.put((Serializable)key, (Serializable)value);
										if (logger.isWarnEnabled()) {
											logger.warn("[导入disk数据] [List<Integer>] ["+f.getName()+"] [key:" + key + "] [value:" + value + "]");
										}
									} 
								} else if (key.toString().startsWith("renwuKey")) {
									if (disk.get((Serializable)key) == null) {
									List<Long> value = (List<Long>) d.get((Serializable) key);
										disk.put((Serializable)key, (Serializable)value);
										if (logger.isWarnEnabled()) {
											logger.warn("[导入disk数据] [List<Long>] ["+f.getName()+"] [key:" + key + "] [value:" + value + "]");
										}
									}
								} else if (key.toString().startsWith("wushengLiansheng")) {
									if (disk.get((Serializable)key) == null) {
										Long value = (Long) d.get((Serializable) key);
											disk.put((Serializable)key, (Serializable)value);
											if (logger.isWarnEnabled()) {
												logger.warn("[导入disk数据] [List<Long>] ["+f.getName()+"] [key:" + key + "] [value:" + value + "]");
											}
										}
								}
							}
						}
					} catch (Exception e) {
						logger.warn("[导入disk数据] [异常] [fileName:" + f.getAbsolutePath() + "]", e);
					}
					list.add(f.getAbsolutePath());
				}
			}
		}
		return list;
	}
	/**
	 * 完成任务
	 * @param task
	 * @param player
	 */
	@SuppressWarnings("unchecked")
	public void notifyCompleteTask(Task task, Player player) {
		if (task == null || player == null) {
			return ;
		}
		try {
			List<Long> tempList = null;
			tempList = (List<Long>) disk.get(renwuKey + "_" + player.getId());
			if (tempList == null) {
				tempList = new ArrayList<Long>();
			}
			if (tempList.contains(task.getId())) {
				return ;
			}
			for (int i=0; i<极限任务1Id.length; i++) {
				if (极限任务1Id[i] == task.getId()) {
					tempList.add(task.getId());
					AchievementManager.getInstance().record(player, RecordAction.极限任务组1);
					disk.put(renwuKey + "_" + player.getId(), (Serializable) tempList);
					return ;
				}
			}
			for (int i=0; i<极限任务2Id.length; i++) {
				if (极限任务2Id[i] == task.getId()) {
					tempList.add(task.getId());
					AchievementManager.getInstance().record(player, RecordAction.极限任务组2);
					disk.put(renwuKey + "_" + player.getId(), (Serializable) tempList);
					return;
				}
			}
			for (int i=0; i<极限任务3Id.length; i++) {
				if (极限任务3Id[i] == task.getId()) {
					tempList.add(task.getId());
					AchievementManager.getInstance().record(player, RecordAction.极限任务组3);
					disk.put(renwuKey + "_" + player.getId(), (Serializable) tempList);
					return;
				}
			}
			for (int i=0; i<极限任务4Id.length; i++) {
				if (极限任务4Id[i] == task.getId()) {
					tempList.add(task.getId());
					AchievementManager.getInstance().record(player, RecordAction.极限任务组4);
					disk.put(renwuKey + "_" + player.getId(), (Serializable) tempList);
					return;
				}
			}
			for (int i=0; i<极限任务5Id.length; i++) {
				if (极限任务5Id[i] == task.getId()) {
					tempList.add(task.getId());
					AchievementManager.getInstance().record(player, RecordAction.极限任务组5);
					disk.put(renwuKey + "_" + player.getId(), (Serializable) tempList);
					return;
				}
			}
			for (int i=0; i<极限任务6Id.length; i++) {
				if (极限任务6Id[i] == task.getId()) {
					tempList.add(task.getId());
					AchievementManager.getInstance().record(player, RecordAction.极限任务组6);
					disk.put(renwuKey + "_" + player.getId(), (Serializable) tempList);
					return;
				}
			}
			
		} catch (Exception e) {
			logger.error("[目标系统] [统计玩家完成极限任务] [异常] [" + player.getLogString() + "] [taskName : " + task.getName_stat() + "]", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void notifyKillBossAction(Player player, int monsterId) {
		if (monsterId <= 0 ) {
			return;
		}
		List<Integer> tempList = null;
		tempList = (List<Integer>) disk.get(shaguaiKey + "_" + player.getId());
		if (tempList == null) {
			tempList = new ArrayList<Integer>();
		}
		if (tempList.contains(monsterId)) {
			return ;
		}
		for (int i=0; i<杀死漏网的世界boss.length; i++) {
			if (杀死漏网的世界boss[i] == monsterId) {
				tempList.add(monsterId);
				AchievementManager.getInstance().record(player, RecordAction.击杀漏网的世界BOSS);
				disk.put(shaguaiKey + "_" + player.getId(), (Serializable) tempList);
				return ;
			}
		}
		for (int i=0; i<击杀仙界俩boss.length; i++) {
			if (击杀仙界俩boss[i] == monsterId) {
				tempList.add(monsterId);
				AchievementManager.getInstance().record(player, RecordAction.击杀仙界俩boss);
				disk.put(shaguaiKey + "_" + player.getId(), (Serializable) tempList);
				return ;
			}
		}
		for (int i=0; i<小试牛刀怪物Id.length; i++) {
			if (小试牛刀怪物Id[i] == monsterId) {
				tempList.add(monsterId);
				AchievementManager.getInstance().record(player, RecordAction.杀死固定20只怪);
				disk.put(shaguaiKey + "_" + player.getId(), (Serializable) tempList);
				return ;
			}
		}
		for (int i=0; i<杀死境界怪物成就.length; i++) {
			if (杀死境界怪物成就[i] == monsterId) {
				tempList.add(monsterId);
				AchievementManager.getInstance().record(player, RecordAction.杀死境界怪物);
				disk.put(shaguaiKey + "_" + player.getId(), (Serializable) tempList);
				return;
			}
		}
		for (int i=0; i<杀死精英怪物成就.length; i++) {
			if (杀死精英怪物成就[i] == monsterId) {
				tempList.add(monsterId);
				AchievementManager.getInstance().record(player, RecordAction.杀死精英怪);
				disk.put(shaguaiKey + "_" + player.getId(), (Serializable) tempList);
				return;
			}
		}
	}
	
	public RecordAction getTitleAction(List<PlayerTitle> titleList) {
		RecordAction ra = null;
		if (hasAllTitle(titleList, 称号成就1)) {
			ra = RecordAction.集齐20个称号1;
		} else if (hasAllTitle(titleList, 称号成就2)) {
			ra = RecordAction.集齐20个称号2;
		} else if (hasAllTitle(titleList, 称号成就3)) {
			ra = RecordAction.集齐13个称号;
		}
		return ra;
	}
	
	public boolean hasAllTitle(List<PlayerTitle> titleList, String[] aimList) {
		if (aimList == null) {
			return false;
		}
		if (titleList == null || titleList.size() <= 0) {
			return false;
		}
		for (int i=0; i<aimList.length; i++) {
			boolean has = false;
			for (PlayerTitle pt : titleList) {
				if (pt.getTitleName().equals(aimList[i])) {
					has = true;
					break;
				}
			}
			if (!has) {
				return false;
			}
		}
		return true;
	}
	public int gethasTitleNum(List<PlayerTitle> titleList, String[] aimList) {
		if (aimList == null) {
			return 0;
		}
		if (titleList == null || titleList.size() <= 0) {
			return 0;
		}
		int tempNum = 0;
		for (int i=0; i<aimList.length; i++) {
			for (PlayerTitle pt : titleList) {
				if (pt.getTitleName().equals(aimList[i])) {
					tempNum++;
				}
			}
		}
		return tempNum;
	}
	
	/**
	 * 获取玩家当前所在的章节名
	 * @param player
	 * @return
	 */
	public String getPlayerCurrChapter(Player player) {
		int level = player.getLevel();
		String result = "";
		for(ChapterModel  cm : chapterMaps.values()) {
			if(cm.getLevelLimit() <= level && level <= cm.getMaxLevel()) {
				result = cm.getChapterName();
				break;
			}
		}
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		PlayerAimManager am = new PlayerAimManager();
		am.setFileName("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//playeraim//playeraims.xls");
		am.setDiskFilePath("D:/Documents/Downloads/playerAim.ddc");
		am.initDisk();
//		List<Integer> tempList = (List<Integer>) am.disk.get(shaguaiKey + "_" + "1503000000000112643");
//		for (int i=0; i<杀死精英怪物成就.length; i++) {
//			boolean flag = false;
//			for (int k=0; k<tempList.size(); k++) {
//				if (杀死精英怪物成就[i] == tempList.get(k)) {
//					flag = true;
//					break;
//				}
//			}
//			if (!flag) {
//				System.out.println("没杀的怪:" + 杀死精英怪物成就[i]);
//			}
//		}
//		System.out.println(tempList.size());
//		System.out.println(tempList);
		
//		am.initFile();
	}
	
	public void initOtherTempArticle() throws Exception {
		if(tempAname.length != 4) {
			throw new Exception("[称号、绑银、工资、功勋临时物品没配置] [" + Arrays.toString(tempAname) + "]");
		}
		for(int i=0; i<tempAname.length; i++) {
			Article a = ArticleManager.getInstance().getArticleByCNname(tempAname[i]);
			if (a == null) {
				throw new Exception("[配置物品不存在] [物品统计名:" + tempAname[i] + "]");
			}
			ArticleEntity ae = DefaultArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.目标系统临时, null, a.getColorType());
			Article2Client ac = new Article2Client(ae.getId(), 1);
			otherTempReward.add(ac);
		}
	}
	/**
	 * 加载文件
	 * @throws Exception 
	 */
	private void initFile() throws Exception {
		File f = new File(fileName);
		if(!f.exists()){
			throw new Exception(fileName + " 配表不存在! " + f.getAbsolutePath());
		}
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		int totalSheetNum = workbook.getNumberOfSheets();		//总的sheet数（第一个为章节奖励，后边均为单个章节）
//		System.out.println(totalSheetNum);
		HSSFSheet sheet = workbook.getSheetAt(0);		//章节奖励
		int rows = sheet.getPhysicalNumberOfRows();
		
		List<String> unExistArticle = new ArrayList<String>();
		for(int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				ChapterModel cm = getChapterModel(row, unExistArticle);
				chapterMaps.put(cm.getChapterName(), cm);	
//				System.out.println(cm);
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		
		for(int i=1; i<totalSheetNum; i++) {		//循环读取每章中的目标
			sheet = workbook.getSheetAt(i);
			String chapterName = sheet.getSheetName();
			ChapterModel cm = chapterMaps.get(chapterName);
			if(cm == null) {
				throw new Exception("[章节名对应错误] [" + chapterName + "]");
			}
			rows = sheet.getPhysicalNumberOfRows();
			for(int j=2; j<rows; j++) {
				HSSFRow row = sheet.getRow(j);
				if(row == null){
					continue;
				}
				try{
					PlayerAimModel pam = getPlayerAimModel(row, unExistArticle);
					pam.setChapterName(chapterName);
					if(aimMaps.containsKey(pam.getId())) {
						throw new Exception("[目标系统] [配表目标id有冲突] [" + pam.getId() + "]");
					}
					aimMaps.put(pam.getId(), pam);
//					System.out.println(pam);
					putAim2Chapter(cm, pam);
					putAim2SystemAim(pam);
				}catch(Exception e) {
					throw new Exception(fileName + "第" + (j+1) + "行异常！！[" + sheet.getSheetName() + "]", e);
				}
			}
		}
		
		for(ChapterModel cm : chapterMaps.values()) {
			cm.updateScore();
		}
		if (unExistArticle.size() > 0) {
			throw new Exception("[配置物品不存在] [物品统计名:" + unExistArticle + "]");
		}
	}
	
	private void putAim2Chapter(ChapterModel cm, PlayerAimModel pam) {
		List<PlayerAimModel> list = cm.getAimsList();
		if(list == null) {
			list = new ArrayList<PlayerAimModel>();
		}
		if(!list.contains(pam)) {
			list.add(pam);
		}
		cm.setAimsList(list);
		chapterMaps.put(cm.getChapterName(), cm);
	}
	
	private void putAim2SystemAim(PlayerAimModel pam) {
		if(pam == null) {
			logger.error("[目标系统] [传入空的目标 ] ");
			return ;
		}
		if(systemAimsMap.get(pam.getAction().getType()) == null) {
			List<PlayerAimModel> list = new ArrayList<PlayerAimModel>();
			systemAimsMap.put(pam.getAction().getType(), list);
		} 
		List<PlayerAimModel> list = systemAimsMap.get(pam.getAction().getType());
		if(!list.contains(pam)) {
			list.add(pam);
		}
		systemAimsMap.put(pam.getAction().getType(), list);
	}
	/**
	 * 缓存目标奖励临时物品
	 * @param aimId
	 * @param ac
	 */
	private void putAim2AimRewardMap(int aimId ,Article2Client ac) {
		if(ac == null) {
			logger.error("[目标系统] [传入空的目标 ] ");
			return ;
		}
		if(aimRewardMap.get(aimId) == null) {
			List<Article2Client> list = new ArrayList<Article2Client>();
			aimRewardMap.put(aimId, list);
		} 
		List<Article2Client> list = aimRewardMap.get(aimId);
		if(!list.contains(ac)) {
			list.add(ac);
		}
		aimRewardMap.put(aimId, list);
	}
	/**
	 * 缓存章节奖励临时物品
	 * @param chapterName
	 * @param ac
	 */
	private void putAim2ChapterRewardMap(String chapterName ,Article2Client ac) {
		if(ac == null) {
			logger.error("[目标系统] [传入空的目标 ] ");
			return ;
		}
		if(chapterRewardMap.get(chapterName) == null) {
			List<Article2Client> list = new ArrayList<Article2Client>();
			chapterRewardMap.put(chapterName, list);
		} 
		List<Article2Client> list = chapterRewardMap.get(chapterName);
		if(!list.contains(ac)) {
			list.add(ac);
		}
		chapterRewardMap.put(chapterName, list);
	}
	
	private PlayerAimModel getPlayerAimModel(HSSFRow row, List<String> unExistArticle) throws Exception {
		PlayerAimModel pam = new PlayerAimModel();
		int num = 0;
		pam.setId(ReadFileTool.getInt(row, num++, logger));
		pam.setAction(getAction(ReadFileTool.getInt(row, num++, logger)));
		pam.setAimName(ReadFileTool.getString(row, num++, logger));
		pam.setLevelLimit(ReadFileTool.getInt(row, num++, logger));
		pam.setVipLimit(ReadFileTool.getInt(row, num++, logger));
		pam.setMulReward4Vip(ReadFileTool.getInt(row, num++, logger));
		pam.setDescription(ReadFileTool.getString(row, num++, logger));
		String desCription =ReadFileTool.getString(row, num++, logger);
		int actionType = ReadFileTool.getInt(row, num++, logger);
		String npcMap = ReadFileTool.getString(row, num++, logger);
		String point = ReadFileTool.getString(row, num++, logger);
		if(actionType > 0) {
			String NpcName = desCription.split(tempStart)[1].split(tempEnd)[0];
			String tempRe = replaceStart.replace("*", actionType+"");
			int[] pp = new int[]{0,0};
			if(point != null && !point.isEmpty()) {
				String[] temp = point.split(",");
				if(temp.length != 2) {
					throw new Exception("[导航坐标配置错误] [" + point + "]");
				}
				pp[0] = Integer.parseInt(temp[0]);
				pp[1] = Integer.parseInt(temp[1]);
			}
			desCription = desCription.replace(tempStart, replaceEnd+tempRe).replace("$", npcMap).replace("%", pp[0]+"").replace("^", pp[1]+"").replace("&", NpcName);
			desCription = desCription.replace(tempEnd, replaceEnd+nomalStart);
		}
		if(!desCription.isEmpty()) {
			desCription = nomalStart + desCription + replaceEnd;
		}
		pam.setNavigationDes(desCription);
		pam.setIcon(ReadFileTool.getString(row, num++, logger));
		pam.setFrameColor(ReadFileTool.getInt(row, num++, logger));
		pam.setNum(ReadFileTool.getInt(row, num++, logger));
		pam.setScore(ReadFileTool.getInt(row, num++, logger));;
		String rewardArticles = ReadFileTool.getString(row, num++, logger);
		List<RewordArticle> articleList = new ArrayList<RewordArticle>();
		if(!rewardArticles.isEmpty()) {
			String[] tempA = rewardArticles.split("\\|");
			for(int i=0; i<tempA.length; i++) {
				String[] tempB = tempA[i].split(",");
				if(tempB.length != 4) {			//0物品名  1物品颜色  2物品数量  3是否显示粒子
					throw new Exception("[物品配置格式错误," + rewardArticles + "]");
				}
				boolean hasParticle = Integer.parseInt(tempB[3]) == 1;
				RewordArticle ra = new RewordArticle(tempB[0], Integer.parseInt(tempB[1]), Integer.parseInt(tempB[2]), hasParticle);
				articleList.add(ra);
				Article a = ArticleManager.getInstance().getArticleByCNname(tempB[0]);
				if (a == null) {
					unExistArticle.add(tempB[0]);
//					throw new Exception("[配置物品不存在] [物品统计名:" + tempB[0] + "]");
				} else {
					ArticleEntity ae = DefaultArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.目标系统临时, null, Integer.parseInt(tempB[1]));
					Article2Client ac = new Article2Client(ae.getId(), Integer.parseInt(tempB[2]));
					this.putAim2AimRewardMap(pam.getId(), ac);
				}
			}
		}
		pam.setRewardArticles(articleList);
		pam.setRewardTitle(ReadFileTool.getString(row, num++, logger));
		pam.setRewardGongzi(ReadFileTool.getInt(row, num++, logger));
		pam.setRewardBindYin(ReadFileTool.getInt(row, num++, logger));
		pam.setRewardGongXun(ReadFileTool.getInt(row, num++, logger));
		pam.setShowProgress((byte) ReadFileTool.getInt(row, num++, logger));
		pam.setDisplay(ReadFileTool.getInt(row, num++, logger) != -1);
		pam.setAimLevel(ReadFileTool.getInt(row, num++, logger));
		return pam;
	}
	
	public RecordAction getAction(int actionType) {
		for (RecordAction action : RecordAction.values()) {
			if (action.getType() == actionType) {
				return action;
			}
		}
		throw new IllegalStateException("[无效的actionType:" + actionType + "]");
	}
	
	private ChapterModel getChapterModel(HSSFRow row, List<String> unExistArticle) throws Exception {
		ChapterModel cm = new ChapterModel();
		int num = 0;
		String chapterName = ReadFileTool.getString(row, num++, logger);
		String subTitle = ReadFileTool.getString(row, num++, logger);
		int scoreLimit = ReadFileTool.getInt(row, num++, logger);
		int levelLimit = ReadFileTool.getInt(row, num++, logger);
		int maxLevel = ReadFileTool.getInt(row, num++, logger);
		int vipLimit = ReadFileTool.getInt(row, num++, logger);
		int mul4Vip = ReadFileTool.getInt(row, num++, logger);
		String description = ReadFileTool.getString(row, num++, logger);
		String icon = ReadFileTool.getString(row, num++, logger);
		String rewardArticles = ReadFileTool.getString(row, num++, logger);
		String rewardtitle = ReadFileTool.getString(row, num++, logger);
		long rewardGongzi = ReadFileTool.getInt(row, num++, logger);
		long rewardBindYin = ReadFileTool.getInt(row, num++, logger);
		long rewardGongXun = ReadFileTool.getInt(row, num++, logger);
		cm.setChapterName(chapterName);
		cm.setScoreLimit(scoreLimit);
		cm.setLevelLimit(levelLimit);
		cm.setMaxLevel(maxLevel);
		cm.setSubtitle(subTitle);
		cm.setVipLimit(vipLimit);
		cm.setMulReward4Vip(mul4Vip);
		cm.setDescription(description);
		cm.setIcon(icon);
		cm.setRewardTitle(rewardtitle);
		cm.setRewardBindYin(rewardBindYin);
		cm.setRewardGongXun(rewardGongXun);
		cm.setRewardGongzi(rewardGongzi);
		List<RewordArticle> articleList = new ArrayList<RewordArticle>();
		if(!rewardArticles.isEmpty()) {
			String[] tempA = rewardArticles.split("\\|");
			for(int i=0; i<tempA.length; i++) {
				String[] tempB = tempA[i].split(",");
				if(tempB.length != 4) {			//0物品名  1物品颜色  2物品数量  3是否显示粒子
					throw new Exception("[物品配置格式错误," + rewardArticles + "]");
				}
				boolean hasParticle = Integer.parseInt(tempB[3]) == 1;
				RewordArticle ra = new RewordArticle(tempB[0], Integer.parseInt(tempB[1]), Integer.parseInt(tempB[2]), hasParticle);
				Article a = ArticleManager.getInstance().getArticleByCNname(tempB[0]);
				if (a == null) {
					unExistArticle.add(tempB[0]);
//					throw new Exception("[配置物品不存在] [物品统计名:" + tempB[0] + "]");
				} else {
				ArticleEntity ae = DefaultArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.目标系统临时, null, Integer.parseInt(tempB[1]));
				Article2Client ac = new Article2Client(ae.getId(), Integer.parseInt(tempB[2]));
				this.putAim2ChapterRewardMap(chapterName, ac);
				articleList.add(ra);
				}
			}
		}
		cm.setRewardArticles(articleList);
		return cm;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@Override
	public void proc(Event evt) {
		// TODO Auto-generated method stub
		if (logger.isDebugEnabled()) {
			logger.debug("[目标系统] [收到event] [" + evt.id + "]");
		}
		try {
			switch (evt.id) {
			case Event.PLAYER_LOGIN:			//登陆监测玩家是否有可领取的目标奖励，推送消息给客户端显示按钮粒子
				EventOfPlayer ep = (EventOfPlayer) evt;
				
				{		//检测玩家vip等级 送称号     2014年11月14日15:27:57
					if (ep.player.getVipLevel() >= 12) {
						boolean rewardFlag = false;
						List<PlayerTitle> tempList = ep.player.getPlayerTitles();
						if (tempList != null && tempList.size() > 0) {
							for (PlayerTitle pt : tempList) {
								if (Translate.金蛇圣皇.equals(pt.getTitleName())) {
									rewardFlag = true;
									break;
								}
							}
						}
						if (!rewardFlag) {
							PlayerTitlesManager.getInstance().addTitle(ep.player,Translate.金蛇圣皇,true);
						}
					}	
				}
				
				{			//检测玩家身上装备星级
					long[] eIds = ep.player.getSoul(Soul.SOUL_TYPE_BASE).getEc().getEquipmentIds();
					int start20Nums = 0;
					for (long id : eIds) {
						if (id <= 0) {
							continue;
						}
						ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(id);
						if (ae instanceof EquipmentEntity) {
							if (((EquipmentEntity) ae).getStar() > 20  && ((EquipmentEntity) ae).isInscriptionFlag()) {
								start20Nums ++ ;
							}
						}
					}
					Soul soul2 = ep.player.getSoul(Soul.SOUL_TYPE_SOUL);
					if (soul2 != null ) {
						long[] eIds2 = soul2.getEc().getEquipmentIds();
						for (long id : eIds2) {
							if (id <= 0) {
								continue;
							}
							ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(id);
							if (ae instanceof EquipmentEntity) {
								if (((EquipmentEntity) ae).getStar() > 20 && ((EquipmentEntity) ae).isInscriptionFlag()) {
									start20Nums ++ ;
								}
							}
						}
					}
					if (start20Nums > 0) {
						GameDataRecord gdr = AchievementManager.getInstance().getPlayerDataRecord(ep.player, RecordAction.羽化装备成功次数);
						if (gdr == null) {
							for (int i=0; i<start20Nums; i++) {
								AchievementManager.getInstance().record(ep.player, RecordAction.羽化装备成功次数);
							}
						} else if(start20Nums > gdr.getNum()) {
							int temp = (int) (start20Nums - gdr.getNum());
							for (int i=0; i<temp; i++) {
								AchievementManager.getInstance().record(ep.player, RecordAction.羽化装备成功次数);
							}
						}
					}
				}
				int tempNum = PlayerAimeEntityManager.instance.checkPlayerAimReceiveStatus(ep.player);
				if(tempNum > 0) {
					//通知客户端显示粒子
					NOTICE_PARTICLE_REQ req = new NOTICE_PARTICLE_REQ(GameMessageFactory.nextSequnceNum(), tempNum, 1);
					ep.player.addMessageToRightBag(req);
					if(logger.isDebugEnabled()) {
						logger.debug("[目标系统] [玩家登陆] [通知客户端显示按钮粒子] [" + ep.player.getLogString() + "]");
					}
				}
				try {
					AchievementManager.getInstance().record(ep.player, RecordAction.境界, ep.player.getClassLevel());
				} catch (Exception e) {
					logger.error("[目标系统] [由于表错，登陆时重新检测玩家境界] [异常] [" + ep.player.getLogString() + "]", e);
				}
				
				try {
					Soul soul = ep.player.getSoul(Soul.SOUL_TYPE_BASE);
					byte tempCareer = soul != null ? soul.getCareer() : ep.player.getCareer();
					Career career = CareerManager.getInstance().getCareer(tempCareer);
					if (ep.player.getXinfaLevels() != null && ep.player.getXinfaLevels().length >= 40) {
						byte[] tempArr = new byte[12]; 
						for (int i=0; i<ep.player.getXinfaLevels().length; i++) {
							if (i >= 5 && i < 17) {
								tempArr[i-5] = ep.player.getXinfaLevels()[i];
							}
						}
						
						if(soul != null) {
							try {
								int[] tempSkLv = new int[5];
								Skill skill = null;
								if (career.xinfaSkills != null) {
									for (int i = 0; i < career.xinfaSkills.length; i++) {
										skill = career.xinfaSkills[i];
										if (skill != null) {
											if (ep.player.getXinfaLevels()[i] > 0) {
												if(skill instanceof IncreaseHpPassiveSkill) {
													tempSkLv[0] += ep.player.getXinfaLevels()[i];
												} else if (skill instanceof IncreaseWuGongPassiveSkill) {
													tempSkLv[1] += ep.player.getXinfaLevels()[i];
												} else if (skill instanceof IncreaseWuFangPassiveSkill) {
													tempSkLv[2] += ep.player.getXinfaLevels()[i];
												} else if (skill instanceof IncreaseFaGongPassiveSkill) {
													tempSkLv[3] += ep.player.getXinfaLevels()[i];
												} else if (skill instanceof IncreaseFaFangPassiveSkill) {
													tempSkLv[4] += ep.player.getXinfaLevels()[i];
												}
											}
										}
									}
								}
								if (tempSkLv[2] > 1) {
									AchievementManager.getInstance().record(ep.player, RecordAction.外防心法重数, tempSkLv[2]);
								}
								if (tempSkLv[4] > 1) {
									AchievementManager.getInstance().record(ep.player, RecordAction.内防心法重数, tempSkLv[4]);
								}
								if (logger.isDebugEnabled()) {
									logger.debug("[目标系统] [登陆检测玩家内外防心法] [内防:" + tempSkLv[4] + "] [外防:" + tempSkLv[2] + "] [" + ep.player.getLogString() + "]");
								}
							} catch (Exception exct) {
								PlayerAimManager.logger.error("[目标系统] [统计多个心法等级异常] [" + ep.player.getLogString() + "]", exct);
							}
						}
					}
				} catch (Exception e) {
					PlayerAimManager.logger.error("[目标系统] [内外防心法50重检测] [异常] [" + ep.player.getLogString() + "]", e);
				}
				break;
			case Event.CHECK_PLAYER_AIMS:		//针对老玩家。。监测gamedatarecord中玩家数据自动完成玩家已达成的目标
				EventWithObjParam evtWithObj = (EventWithObjParam) evt;
				Object[] obj = (Object[]) evtWithObj.param;
				long playerId = (Long) obj[0];
				checkGameRecordData(playerId);
				break;
			case Event.RECORD_PLAYER_OPT:
				EventWithObjParam eo = (EventWithObjParam) evt;
				Object[] oj = (Object[]) eo.param;
				long playerId1 = (Long) oj[0];
				RecordAction action = (RecordAction) oj[1];
				long num = 1;
				if (oj.length > 2) {
					num = Long.parseLong(oj[2] + "");
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("[目标系统] [异常] [使用RECORD_PLAYER_OPT少参数] [rc:" + action.getName() + "] [" + playerId1 + "]");
					}
				}
				Player pli = GamePlayerManager.getInstance().getPlayer(playerId1);
				AchievementManager.getInstance().record(pli, action, num, true);
				break;
			case Event.PET_JIN_JIE:
				EventWithObjParam evtWithObj3 = (EventWithObjParam) evt;
				Object[] obj3 = (Object[]) evtWithObj3.param;
				Player p = (Player) obj3[0];
				AchievementManager.getInstance().record(p, RecordAction.宠物进阶次数, 1L, true);
				break;
			case Event.MONSTER_KILLED_Simple:
				EventWithObjParam evtWithObj2 = (EventWithObjParam) evt;
				Object[] obj2 = (Object[]) evtWithObj2.param;
				SimpleMonster monster = (SimpleMonster) obj2[0];
				if (logger.isDebugEnabled()) {
					logger.debug("[目标系统] [触发杀怪怪物event][" + monster + "]");
				}
				Player tempP = GamePlayerManager.getInstance().getPlayer(monster.getOwnerId());
				if (tempP.getLevel() <= (monster.getLevel() + 5)) {
					AchievementManager.getInstance().record(tempP, RecordAction.杀死不低于自身5级怪物数量, 1L, true);
				}
				if (monster.isBoss() && monster.getMapName() != null) {
					if (monster.getMapName().indexOf("fudidongtian") >= 0) {
//						Player tempP = GamePlayerManager.getInstance().getPlayer(monster.getOwnerId());
						AchievementManager.getInstance().record(tempP, RecordAction.福地洞天击杀宝怪, 1L, true);
						return ;
					}
				}
				for (int i=0; i<宠物岛.length; i++) {
					if (monster.getMapName().equals(宠物岛[i]) && monster.getMonsterName().indexOf(Translate.精英) >= 0) {
//						Player tempP = GamePlayerManager.getInstance().getPlayer(monster.getOwnerId());
						AchievementManager.getInstance().record(tempP, RecordAction.宠物岛击杀圣兽精英, 1L, true);
					}
				}
				if (monster.isBoss()) {
					this.notifyKillBossAction(tempP, monster.getSpriteCategoryId());
				}
				break;
			default:
				break;
			}
		} catch(Exception e) {
			logger.error("[目标系统] [处理事件出错] [evtId:" + evt.id+ "]", e);
		}
		
	}
	public static final String[] 宠物岛 = new String[]{"shengshoumicheng", "shengshoumichengerceng", "shengshoumichengsanceng"};
	/**
	 * 仙录二次修改需要对一些老数据的检测
	 * @param playerId
	 */
	public void checkGameRecordData2(long playerId) {
		try {
			
		} catch (Exception e) {
			logger.error("[目标系统] [检测原有玩家数据2] [异常] [id:" + playerId + "]", e );
		}
	}
	
	/**
	 * 检查玩家所有的记录数据--自动完成一些目标
	 * @param playerId
	 */
	public void checkGameRecordData(long playerId) {
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			if (player.getLevel() <= 1) {
				return;
			}
			if(player.getLevel() > 1) {			//老玩家才去查
				try {
					HashMap<Integer, GameDataRecord> gdrMap = AchievementManager.getInstance().getPlayerDataRecords(player);			//得到所有的玩家gamedatarecord数据(遍历挨个去触发事件)
					for(GameDataRecord gdr : gdrMap.values()) {
						PlayerAimeEntityManager.instance.checkPlayerAims(player, gdr, false);
					}
				} catch (Exception e) {
					logger.error("[目标系统] [检查玩家原有GameDataRecord中数据] [异常] [" + player.getLogString() + "]", e);
				}
			}
			if (player.getClassLevel() > 0) {
				// 统计
				AchievementManager.getInstance().record(player, RecordAction.境界, player.getClassLevel());
			}
			if(player.getLevel() >= TransitRobberyManager.openLevel) {			//检测渡劫和大师技能并记录到gamerecord中
				try {
					TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(playerId);
					if(entity != null) {
						AchievementManager.getInstance().record(player, RecordAction.渡劫重数, entity.getCurrentLevel(), false);
					}
					SkBean bean = SkEnhanceManager.getInst().findSkBean(player);
					if(bean != null) {		//大师技能自动导入
						byte maxLevel = 0;
						if(bean.getLevels() != null) {
							for(int i=0; i<bean.getLevels().length; i++) {
								if(bean.getLevels()[i] > 0) {
									for(int ii=0; ii<bean.getLevels()[i]; ii++) {
										AchievementManager.getInstance().record(player, RecordAction.大师技能总重数);
									}
									if(maxLevel < bean.getLevels()[i]) {
										maxLevel = bean.getLevels()[i];
									}
								}
							}
						}
						if(bean.getSoulLevels() != null) {
							for(int i=0; i<bean.getSoulLevels().length; i++) {
								if(bean.getLevels()[i] > 0) {
									for(int ii=0; ii<bean.getLevels()[i]; ii++) {
										AchievementManager.getInstance().record(player, RecordAction.大师技能总重数);
									}
									if(maxLevel < bean.getSoulLevels()[i]) {
										maxLevel = bean.getSoulLevels()[i];
									}
								}
							}
						}
						AchievementManager.getInstance().record(player, RecordAction.大师技能最大重数, maxLevel, false);
					}
				}catch (Exception e) {
					logger.error("[目标系统] [检查玩家渡劫及大师技能数据] [异常] [" + player.getLogString() + "]", e);
				}
			}
			try{				//检查玩家基础技能、心法、以及注魂情况
				Soul soul = player.getSoul(Soul.SOUL_TYPE_BASE);
				byte tempCareer = soul != null ? soul.getCareer() : player.getCareer();
				Career career = CareerManager.getInstance().getCareer(tempCareer);
				int maxTempNum = 0;
				if (career != null) {
					if (soul != null && soul.getCareerBasicSkillsLevels() != null && soul.getCareerBasicSkillsLevels().length >= 2) {
						if (soul.getCareerBasicSkillsLevels()[1] > 0) {
							AchievementManager.getInstance().record(player, RecordAction.第二个基础技能等级, soul.getCareerBasicSkillsLevels()[1], false);
						}
						for (int i=0; i<soul.getCareerBasicSkillsLevels().length; i++) {
							if (soul.getCareerBasicSkillsLevels()[i] >= 10) {
								maxTempNum++;
							}
						}
						if (maxTempNum > 0) {
							AchievementManager.getInstance().record(player, RecordAction.基础技能满级个数, maxTempNum, false);
						}
					}
					if (player.getXinfaLevels() != null && player.getXinfaLevels().length >= 40) {
						byte[] tempArr = new byte[12]; 
						for (int i=0; i<player.getXinfaLevels().length; i++) {
							if (i >= 5 && i < 17) {
								tempArr[i-5] = player.getXinfaLevels()[i];
							}
						}
						try {
							Arrays.sort(tempArr);
//							if (tempArr != null && tempArr.length > 2 && tempArr[tempArr.length-3] > 1) {
//								AchievementManager.getInstance().record(player, RecordAction.任意三个注魂等级, tempArr[tempArr.length-3], false);
//							}
//							if (tempArr[tempArr.length-1] > 0) {
//								AchievementManager.getInstance().record(player, RecordAction.注魂次数, 1L);
//							}
						} catch (Exception eex) {
							PlayerAimManager.logger.error("[目标系统] [统计新心法异常] [" + player.getLogString() + "]", eex);
						}
						
						if(soul != null) {
							try {
								int[] tempSkLv = new int[5];
								int tempMaxLevel = 0;
								Skill skill = null;
								if (career.xinfaSkills != null) {
									for (int i = 0; i < career.xinfaSkills.length; i++) {
										skill = career.xinfaSkills[i];
										if (skill != null) {
											if (player.getXinfaLevels()[i] > 0) {
												if(skill instanceof IncreaseHpPassiveSkill) {
													tempSkLv[0] += player.getXinfaLevels()[i];
												} else if (skill instanceof IncreaseWuGongPassiveSkill) {
													tempSkLv[1] += player.getXinfaLevels()[i];
												} else if (skill instanceof IncreaseWuFangPassiveSkill) {
													tempSkLv[2] += player.getXinfaLevels()[i];
												} else if (skill instanceof IncreaseFaGongPassiveSkill) {
													tempSkLv[3] += player.getXinfaLevels()[i];
												} else if (skill instanceof IncreaseFaFangPassiveSkill) {
													tempSkLv[4] += player.getXinfaLevels()[i];
												}
											}
										}
									}
									tempMaxLevel = tempSkLv[1] > tempSkLv[3] ? tempSkLv[1] : tempSkLv[3];
								}
								Arrays.sort(tempSkLv);
								if (tempSkLv[1] > 1) {
									AchievementManager.getInstance().record(player, RecordAction.心法随意4个到达的最大重数, tempSkLv[1]);
								} 
								if (tempSkLv[3] > 1) {
									AchievementManager.getInstance().record(player, RecordAction.心法随意2个到达的最大重数, tempSkLv[3]);
								}
								if (tempMaxLevel > 0) {
									AchievementManager.getInstance().record(player, RecordAction.内攻或外攻心法最大重数, tempMaxLevel);
								}
								if (tempSkLv[4] > 1) {
									AchievementManager.getInstance().record(player, RecordAction.心法最大重数, tempSkLv[4]);
								}
							} catch (Exception exct) {
								PlayerAimManager.logger.error("[目标系统] [统计多个心法等级异常] [" + player.getLogString() + "]", exct);
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error("[目标系统] [检查玩家基础技能、心法、注魂数据] [异常] [" + player.getLogString() + "]", e);
			}
			
			try {
				Soul soul = player.getSoul(Soul.SOUL_TYPE_BASE);
				if (soul != null && player.getHorseIdList() !=  null && player.getHorseIdList().size() > 0) {
					for (int i=0; i<player.getHorseIdList().size(); i++) {
						Horse horse = HorseManager.getInstance().getHorseByHorseId(player, player.getHorseIdList().get(i), soul);
						if (horse != null) {
							Article harticle = ArticleManager.getInstance().getArticle(horse.getHorseName());
							if (harticle instanceof HorseProps && ((HorseProps)harticle).isFly()) {
								AchievementManager.getInstance().record(player, RecordAction.获得飞行坐骑次数, 1L);
							}
						}
					}
				}
				
			} catch (Exception e) {
				logger.error("[目标系统] [检查玩家飞行坐骑数据] [异常] [" + player.getLogString() + "]", e);
			}
			try {
				if (player.getNuqiSkillsLevels() != null && player.getNuqiSkillsLevels().length > 0) {
					for (byte b : player.getNuqiSkillsLevels()) {
						if (b > 0) {
							AchievementManager.getInstance().record(player, RecordAction.获得怒气技能, 1L);
						}
					}
				}
			} catch (Exception e) {
				logger.error("[目标系统] [检查玩家怒气技能数据] [异常] [" + player.getLogString() + "]", e);
			}
		} catch (Exception e) {
			logger.error("[目标系统] [检查老玩家已完成目标] [异常] [playerId:" + playerId + "]", e);
		}
	}
	
	@Override
	public void doReg() {
		// TODO Auto-generated method stub
		EventRouter.register(Event.PLAYER_LOGIN, this);
		EventRouter.register(Event.CHECK_PLAYER_AIMS, this);
		EventRouter.register(Event.RECORD_PLAYER_OPT, this);
		EventRouter.register(Event.PET_JIN_JIE, this);
		EventRouter.register(Event.MONSTER_KILLED_Simple, this);
	}

	public String getDiskFilePath() {
		return diskFilePath;
	}

	public void setDiskFilePath(String diskFilePath) {
		this.diskFilePath = diskFilePath;
	}
	
}
