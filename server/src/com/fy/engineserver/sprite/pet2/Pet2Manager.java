package com.fy.engineserver.sprite.pet2;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.PetSubSystem;
import com.fy.engineserver.core.res.Avata;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.PetPropsEntity;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeManager;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.data.props.PetEggProps;
import com.fy.engineserver.datasource.article.data.props.PetProps;
import com.fy.engineserver.datasource.article.data.props.PetSkillProp;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.career.SkillInfo;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.datasource.skill.SkillInfoHelper;
import com.fy.engineserver.datasource.skill.master.SkEnhanceManager;
import com.fy.engineserver.datasource.skill2.GenericSkill;
import com.fy.engineserver.datasource.skill2.GenericSkillManager;
import com.fy.engineserver.event.Event;
import com.fy.engineserver.event.EventProc;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventPlayerDropPet;
import com.fy.engineserver.event.cate.EventPlayerGainPet;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.jiazu.petHouse.PetHouseManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NEW_PET_DETAIL_REQ;
import com.fy.engineserver.message.NEW_PET_DETAIL_RES;
import com.fy.engineserver.message.NEW_PET_LIST_REQ;
import com.fy.engineserver.message.NEW_PET_LIST_RES;
import com.fy.engineserver.message.PET2_HunArticle_REQ;
import com.fy.engineserver.message.PET2_HunArticle_RES;
import com.fy.engineserver.message.PET2_LIAN_DAN_REQ;
import com.fy.engineserver.message.PET2_LIAN_DAN_RES;
import com.fy.engineserver.message.PET2_LIAN_HUN_REQ;
import com.fy.engineserver.message.PET2_LIAN_HUN_REs;
import com.fy.engineserver.message.PET2_LiZi_REQ;
import com.fy.engineserver.message.PET2_LiZi_RES;
import com.fy.engineserver.message.PET2_QUERY_BY_ARTICLE_REQ;
import com.fy.engineserver.message.PET2_QUERY_REQ;
import com.fy.engineserver.message.PET2_QUERY_RES;
import com.fy.engineserver.message.PET2_UI_DESC_REQ;
import com.fy.engineserver.message.PET2_UI_DESC_RES;
import com.fy.engineserver.message.PET2_UP_LV_REQ;
import com.fy.engineserver.message.PET2_UP_LV_RES;
import com.fy.engineserver.message.PET2_XueMai_REQ;
import com.fy.engineserver.message.PET2_XueMai_RES;
import com.fy.engineserver.message.PET3_QUERY_BY_ARTICLE_REQ;
import com.fy.engineserver.message.PET3_QUERY_REQ;
import com.fy.engineserver.message.PET3_QUERY_RES;
import com.fy.engineserver.message.PET_BOOK_UI_REQ;
import com.fy.engineserver.message.PET_BOOK_UI_RES;
import com.fy.engineserver.message.PET_CHONG_BAI_REQ;
import com.fy.engineserver.message.PET_CHONG_BAI_RES;
import com.fy.engineserver.message.PET_DETAIL_REQ;
import com.fy.engineserver.message.PET_DETAIL_RES;
import com.fy.engineserver.message.PET_FIND_CUR_INDEX_REQ;
import com.fy.engineserver.message.PET_FIND_CUR_INDEX_RES;
import com.fy.engineserver.message.PET_JIN_JIE_REQ;
import com.fy.engineserver.message.PET_JIN_JIE_RES;
import com.fy.engineserver.message.PET_LIST_REQ;
import com.fy.engineserver.message.PET_LIST_RES;
import com.fy.engineserver.message.PET_QUERY_BY_ARTICLE_REQ;
import com.fy.engineserver.message.PET_QUERY_REQ;
import com.fy.engineserver.message.PET_QUERY_RES;
import com.fy.engineserver.message.PET_SKILL_TAKE_REQ;
import com.fy.engineserver.message.PET_SKILL_TAKE_RES;
import com.fy.engineserver.message.PET_SKILL_UP_REQ;
import com.fy.engineserver.message.PET_SKILL_UP_RES;
import com.fy.engineserver.message.PET_TALENT_UP_REQ;
import com.fy.engineserver.message.PET_TALENT_UP_RES;
import com.fy.engineserver.message.QUERY_SKILLINFO_PET2_REQ;
import com.fy.engineserver.message.QUERY_SKILLINFO_PET2_RES;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.date.pet.PetALLBillboard;
import com.fy.engineserver.newBillboard.date.pet.PetRankData;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetFlySkillInfo;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.sprite.pet.SkillProp;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

/**
 * 宠物玩法改进。
 * 
 *         create on 2013年8月13日
 */
public class Pet2Manager extends SubSystemAdapter implements EventProc {
	public static boolean watch = false;
	public static Logger log = LoggerFactory.getLogger(PetManager.class.getName());
	public static Pet2Manager inst;
	public SimpleEntityManager<PetsOfPlayer> petsOfPlayerBeanEm;
	/**
	 * 只是为了spring初始化
	 */
	private BuffTemplateManager buffTemplateMgr;
	/**
	 * 所有宠物的名称和icon数组。progName属性实际存储的是宠物道具的名称。
	 */
	public static GradePet[] allPetArr;
	private String file;
	public LruMapCache cache;
	public static boolean syncXueMai = true;
	/**
	 * 请勿修改，会影响客户端。
	 */
	private static String splitKey = "#";
	public static int debugRate = 0;

	//
	public static Pet2Manager getInst() {
		if (inst == null) {
			inst = new Pet2Manager();
		}
		return inst;
	}

	public Pet2Manager() {
		log.info("创建第二代宠物管理器");
	}

	public void init() throws Exception {
		
		petsOfPlayerBeanEm = SimpleEntityManagerFactory.getSimpleEntityManager(PetsOfPlayer.class);
		cache = new LruMapCache(1024 * 1024, 7200000L, false, "PetsOfPlayer-Cache");
		doReg();
//		try {
//			log.info("pet grade conf file is {}", file);
			PetGrade.levels = PetGrade.load(file);
//		} catch (Exception e) {
//			log.error("load fail", e);
//		}
		loadSkill(file);
		buildAllPetArr();
		inst = this;
		log.info("初始化第二代宠物管理器结束。");
		try {
			new SkEnhanceManager().init();
		} catch (Exception e) {
			SkEnhanceManager.log.error("Pet2Manager.init: 创建大师技能管理器出现异常.", e);
			return; 
		}
		ServiceStartRecord.startLog(this);
	}

	public void loadSkill(String file) throws Exception {
		if (file == null) {
			log.error("loadSkill file is null");
			return;
		}
		File parent = new File(file).getParentFile();
		File sf = new File(parent, "petSkill.xls");
		InputStream is = new FileInputStream(sf);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		workbook.setMissingCellPolicy(HSSFRow.CREATE_NULL_AS_BLANK);

		HSSFSheet sheet = workbook.getSheetAt(0);
		Pet2SkillCalc.bornSkillArr = loadSkill(sheet, false);
		sheet = workbook.getSheetAt(1);
		Pet2SkillCalc.tianFuSkillArr = loadSkill(sheet, true);
		sheet = workbook.getSheetAt(2);
		loadPetFlySkill(sheet);
		is.close();
	}
	
	public void loadPetFlySkill(HSSFSheet sheet) throws Exception{
		long startTime = System.currentTimeMillis();
		int rows = sheet.getPhysicalNumberOfRows();
		PetManager.petFlySkills.clear();
		PetFlySkillInfo lastInfo = null;
		for(int r = 1; r < rows; r++){
			HSSFRow row = sheet.getRow(r);
			if (row == null) {
				continue;
			}
			int skillId = PetGrade.getInt(row, 0, log);
			if(skillId > 0){
				lastInfo = new PetFlySkillInfo();
				lastInfo.setSkillId(PetGrade.getInt(row, 0, log));
				lastInfo.setName(PetGrade.getString(row, 1, log));
				lastInfo.setIcon(PetGrade.getString(row, 2, log));
				SkillProp prop = new SkillProp();
				prop.skillDesc = PetGrade.getString(row, 3, log);
				prop.propType = PetGrade.getInt(row, 4, log);
				prop.propValue = PetGrade.getInt(row, 5, log);
				prop.valueType = PetGrade.getInt(row, 6, log);
				prop.effectTarget = PetGrade.getInt(row, 7, log);
				lastInfo.skills = (SkillProp[]) ArrayUtils.add(lastInfo.skills, prop);
				PetManager.petFlySkills.put(skillId, lastInfo);
			}else{
				SkillProp prop = new SkillProp();
				prop.skillDesc = PetGrade.getString(row, 3, log);
				prop.propType = PetGrade.getInt(row, 4, log);
				prop.propValue = PetGrade.getInt(row, 5, log);
				prop.valueType = PetGrade.getInt(row, 6, log);
				prop.effectTarget = PetGrade.getInt(row, 7, log);
				lastInfo.skills = (SkillProp[]) ArrayUtils.add(lastInfo.skills, prop);
			}
			log.warn("[宠物飞升技能加载] [成功] ["+lastInfo+"] [skills:"+lastInfo.skills.length+"]"); 
		}
		log.warn("[宠物飞升技能加载] [成功] [总行数:"+rows+"] [数量:"+PetManager.petFlySkills.size()+"] [耗时:"+(System.currentTimeMillis()-startTime)+"]");
	}

	public GenericSkill[] loadSkill(HSSFSheet sheet, boolean procTianFu) {
		int rows = sheet.getPhysicalNumberOfRows();
		GenericSkill[] arr = new GenericSkill[0];
		GenericSkillManager skillMgr = GenericSkillManager.getInst();
		Pet2SkillCalc.getInst();// init
		for (int r = 1; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			if (row == null) {
				break;
			}
			GenericSkill sk = skillMgr.parse(row, log);
			arr = (GenericSkill[]) ArrayUtils.add(arr, sk);
			//
			if (procTianFu) {
				procTianFuByXg(row, sk);
			}
		}
		log.warn("{} 技能个数 {}", sheet.getSheetName(), arr == null ? 0 : arr.length);
		return arr;
	}

	public void procTianFuByXg(HSSFRow row, GenericSkill sk) {
		String xg = PetGrade.getString(row, 7, log);
		String progName = PetGrade.getString(row, 9, log);// 需要在表里添加一列统计名称。
		// 第一次开蛋天赋技能宠物不能够获得高级技能、终极技能
		if (progName.startsWith("初级")) {
			// 忠诚 精明 谨慎 狡诈 (0,1,2,3)
			// 天生技能的这个列不会是性格字符串
			if (xg.startsWith("忠")) {
				Pet2SkillCalc.skByXingGe[0].add(sk);
			} else if (xg.startsWith("精")) {
				Pet2SkillCalc.skByXingGe[1].add(sk);
			} else if (xg.startsWith("谨")) {
				Pet2SkillCalc.skByXingGe[2].add(sk);
			} else if (xg.startsWith("狡")) {
				Pet2SkillCalc.skByXingGe[3].add(sk);
			} else if (xg.startsWith("通")) {
				Pet2SkillCalc.skByXingGe[4].add(sk);
			}
		}else if (progName.startsWith("高级")) {
			// 忠诚 精明 谨慎 狡诈 (0,1,2,3)
			// 天生技能的这个列不会是性格字符串
			if (xg.startsWith("忠")) {
				Pet2SkillCalc.skByXingGeAndGrade[0].add(sk);
			} else if (xg.startsWith("精")) {
				Pet2SkillCalc.skByXingGeAndGrade[1].add(sk);
			} else if (xg.startsWith("谨")) {
				Pet2SkillCalc.skByXingGeAndGrade[2].add(sk);
			} else if (xg.startsWith("狡")) {
				Pet2SkillCalc.skByXingGeAndGrade[3].add(sk);
			} else if (xg.startsWith("通")) {
				Pet2SkillCalc.skByXingGeAndGrade[4].add(sk);
			}
		}
	}

	public void fillGradablePetName(PetProps pp) {
		GradePet[] arr = PetGrade.petList;
		if (arr == null) {
			log.error("可进阶的宠物列表不该是null");
			return;
		}
		GenericSkillManager skInst = GenericSkillManager.getInst();
		for (GradePet gp : arr) {
			if (pp.getName_stat().equals(gp.progName)) {
				gp.setName(pp.getName());
				gp.bornSkill[0] = pp.talent1skill;
				gp.bornSkill[1] = pp.talent2skill;
				gp.icon = pp.getIconId();
				gp.icons[0] = skInst.getSkillIcon(pp.talent1skill);
				gp.icons[1] = skInst.getSkillIcon(pp.talent2skill);
				gp.skDesc[0] = skInst.getSkillDesc(pp.talent1skill);
				gp.skDesc[1] = skInst.getSkillDesc(pp.talent2skill);
				gp.baseAvatar = pp.getAvataRace().concat("_").concat(pp.getAvataSex());
				//
				byte career = 0;
				for (PetEggProps egg : ArticleManager.getInstance().allPetEggProps) {
					if (egg.getName().equals(pp.getEggAticleName())) {
						gp.setCharacter(egg.getCharacter());
						gp.setTakeLevel(egg.getArticleLevel());
						career = (byte) (egg.getCareer() - 1);
						gp.setRarity(egg.getRarity());
						break;
					}
				}
				//
				Pet fakeOne = new Pet();
				fakeOne.setCareer(career);
				fakeOne.setCharacter((byte) gp.getCharacter());
				fakeOne.setRealTrainLevel(gp.getTakeLevel() == 220 ? 225 : gp.getTakeLevel());
				fakeOne.setRarity((byte) gp.getRarity());
				fakeOne.calcShowAttMinMax();
				//
				gp.maxValues = new int[] { fakeOne.getShowMaxStrengthQuality(), fakeOne.getShowMaxDexterityQuality(), fakeOne.getShowMaxSpellpowerQuality(), fakeOne.getShowMaxConstitutionQuality(), fakeOne.getShowMaxDingliQuality() };
				gp.minValues = new int[] { fakeOne.getShowMinStrengthQuality(), fakeOne.getShowMinDexterityQuality(), fakeOne.getShowMinSpellpowerQuality(), fakeOne.getShowMinConstitutionQuality(), fakeOne.getShowMinDingliQuality() };
				gp.scaleArr[0] = pp.getObjectScale();
				break;
			}
		}
	}

	public void buildAllPetArr() {
		PetProps[] list = ArticleManager.getInstance().allPetProps;
		int len = list.length;
		GradePet[] arr = new GradePet[len];
		for (int i = 0; i < len; i++) {
			PetProps pp = list[i];
			GradePet p = new GradePet();
			// p.name = pp.getName();
			p.progName = pp.getName();
			p.icon = pp.getIconId();
			arr[i] = p;
			fillGradablePetName(pp);
		}
		allPetArr = arr;
	}

	public ResponseMessage jinJie(Player player, PET_JIN_JIE_REQ message, boolean needAsk) {
		// 只有千载难逢宠物可进阶
		PetManager pet1mgr = PetManager.getInstance();
		if (pet1mgr == null) {
			log.error("pet 1 manager is null");
			return null;
		}
		long petPropsId = message.getPetId();
		ArticleEntity artE = player.getArticleEntity(petPropsId);
		if (artE == null) {
			log.error("entity not found {} of {}", petPropsId, player.getName());
			return null;
		}
		if (!(artE instanceof PetPropsEntity)) {
			log.error("{} is not PetPropsEntity, {}", artE.getClass().getSimpleName(), player.getName());
			return null;
		}
		PetPropsEntity entity = (PetPropsEntity) artE;
		long petId = entity.getPetId();
		GradePet gradePet = findGradePetConf(entity.getArticleName());
		if (gradePet == null) {
			log.error("grade pet conf not find petId {}, name {} of {}", new Object[] { petId, entity.getArticleName(), player.getName() });
			log.error("此宠物不在可进阶列表内{}", entity.getArticleName());
			String msg = "notGradablePet";
			msg = getConfStr(msg);
			player.sendError(msg);
			return null;
		}
		Pet pet = pet1mgr.getPet(petId);
		if (pet == null) {
			log.error("pet not found with id {} for player {}", message.getPetId(), player.getName());
			return null;
		}
		if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, pet.getPetPropsId())) {
			player.sendError(Translate.高级锁魂物品不能做此操作);
			return null;
		}
		byte r = pet.getRarity();
		if (r < PetManager.千载难逢) {
			log.warn("非千载难逢 宠物 {} of {}", pet.getName(), player.getName());
			return null;
		}
		PetGrade[] confArr = PetGrade.levels;
		if (confArr == null || confArr.length == 0) {
			log.error("conf data is invalid");
			return null;
		}
		if (confArr[0].lvMin > pet.getLevel()) {
			String msg = getConfStr("petLvTooLow");
			msg = String.format(msg, confArr[0].lvMin);
			player.sendError(msg);
			return null;
		}
		int curPetGrade = pet.grade;
		if (curPetGrade == 0) {
			pet.setGrade(1);
		}
		// check max level
		if (curPetGrade >= confArr[confArr.length - 1].grade || curPetGrade >= gradePet.maxGrade) {
			String msg = getConfStr("reachMaxGrade");
			player.sendError(msg);
			return null;
		}
		int nextGrade = curPetGrade + 1;
		if (nextGrade >= confArr.length) {
			log.error("shouldn't happend since max grade checked before {}", nextGrade);
			return null;
		}
		PetGrade confG = confArr[nextGrade];
		if (confG.lvMin > pet.getLevel()) {
			String msg = getConfStr("petLvTooLow");
			msg = String.format(msg, confG.lvMin);
			player.sendError(msg);
			return null;
		}
		String itemName = confG.itemName;
		Knapsack bag = player.getKnapsack_common();
		if (bag == null) {
			log.error("取得的玩家背包是null {}", player.getName());
			return null;
		}
		int hasCnt = bag.countArticle(itemName);
		log.warn("player {} has {} X {} need {}", new Object[] { player.getName(), itemName, hasCnt, confG.itemCnt });
		if (hasCnt < confG.itemCnt) {
			String msg = getConfStr("itemLackTip");
			msg = String.format(msg, itemName, confG.itemCnt, hasCnt);
			player.sendError(msg);
			return null;
		}
		if (needAsk) {
			sendAsk(player, message, itemName, confG.itemCnt);
			return null;
		}
		log.warn("begin remove from {}", player.getName());
		boolean ok = true;
		boolean useBind = true;
		synchronized (player.tradeKey) {
			for (int i = 0; i < confG.itemCnt; i++) {
				int idx = -1;
				if (useBind) {
					idx = bag.indexOf(itemName, true);
					if (idx < 0) {
						useBind = false;
					}
				}
				if (idx < 0) {
					idx = bag.indexOf(itemName, false);
				}
				ArticleEntity ae = bag.remove(idx, "宠物进阶", true);
				if (ae != null) {
					// 统计
					ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.ARTICLES, 1, "宠物进阶", null);
					log.info("remove player [{}] item [{}] times {}, bind {}", new Object[] { player.getName(), itemName, i, ae.isBinded() });
				} else {
					log.error("格子号正确，但移除失败.player [{}],item [{}]", player.getName(), itemName);
					ok = false;
					break;
				}
			}
		}
		log.warn("finish remove from {}, ok ? {}", player.getName(), ok);
		if (!ok) {
			log.error("check cnt ok but remove fail, {}", player.getName());
			return null;
		}
		pet.setGrade(pet.grade + 1);
//		log.warn("宠物进阶, id {} 宠物名称 {} 玩家名称 {}, 达到阶数 {}", new Object[] { pet.getId(), pet.getName(), player.getName(), pet.getGrade() });
		log.warn("[宠物进阶] [进化] [成功] [{}] {} [达到阶数 :{}]", new Object[] { player.getLogString4Knap(),pet.getLogOfInterest(), pet.getGrade() });
		// 宠物进阶后加点的计算，在
		// com.fy.engineserver.sprite.pet.PetPropertyUtility.计算力量值(Pet)
		addPoints(player, pet, pet.grade);
		player.sendError(getConfStr("jinJieOK"));
		pet.init();
		EventWithObjParam evt = new EventWithObjParam(Event.PET_JIN_JIE, new Object[] { player, pet });
		EventRouter.getInst().addEvent(evt);
		PET_JIN_JIE_RES ret = new PET_JIN_JIE_RES(GameMessageFactory.nextSequnceNum(), pet.getGrade());
		log.debug("Pet2Manager.jinJie: return ret. grade {}", pet.getGrade());
		player.addMessageToRightBag(ret);
		try {
			EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.宠物最大阶数, (pet.grade-1)});
			EventRouter.getInst().addEvent(evt2);
			if (pet.grade >= confArr[confArr.length - 1].grade || curPetGrade >= gradePet.maxGrade) {
				EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.宠物进阶到终阶次数, 1L});
				EventRouter.getInst().addEvent(evt3);
			}
		} catch  (Exception e) {
			PlayerAimManager.logger.error("[目标系统] [统计宠物宠物最大阶异常] [" + player.getLogString() + "]", e);
		}
		return null;
	}

	public void sendAsk(Player player, PET_JIN_JIE_REQ message, String itemName, int itemCnt) {
		log.info("Pet2Manager.sendAsk: {}", player.getName());
		OptionJinJieAgree agree = new OptionJinJieAgree();
		agree.req = message;
		agree.setText(Translate.确定);
		OptionJinJieReject disAgree = new OptionJinJieReject();
		disAgree.setText(Translate.取消);
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		String title = getConfStr("jinJieQueRen");
		mw.setTitle(title);
		String body = getConfStr("jjQrBody");
		// 进阶需要消耗%s x %d，确定？
		body = String.format(body, itemName, itemCnt);
		mw.setDescriptionInUUB(body);
		mw.setOptions(new Option[] { agree, disAgree });
		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());

		player.addMessageToRightBag(res);
		log.info("Pet2Manager.sendAsk: {}   ok", player.getName());
		log.info("Pet2Manager.sendAsk: {} {}", title, body);
	}

	public GradePet findGradePetConf(String artName) {
		GradePet[] petList = PetGrade.petList;
		if (petList == null || petList.length == 0) {
			log.error("grade pet conf is invalid");
			return null;
		}
		GradePet ret = null;
		for (GradePet gp : petList) {
			if (artName.equals(gp.name)) {
				ret = gp;
				break;
			}
		}
		return ret;
	}

	/**
	 * 给宠物增加可分配点数。
	 * @param player
	 * @param pet
	 * @param grade
	 */
	public void addPoints(Player player, Pet pet, int grade) {
		PetGrade[] levels = PetGrade.levels;
		if (levels == null || levels.length == 0 || grade >= levels.length) {
			log.error("wrong data, pet id {} of player {}", pet.getId(), player.getName());
			return;
		}
		PetGrade gd = levels[grade];
		if (gd.point <= 0) {
			log.info("point reward is 0 at grade {}, player {}", grade, player.getName());
			return;
		}
		pet.setUnAllocatedPoints(pet.getUnAllocatedPoints() + gd.point);
		log.info("add point {} to pet id {} of player {}", new Object[] { gd.point, pet.getId(), player.getName() });
	}

	public PetGrade getCurGrade(int level) {
		PetGrade[] arr = PetGrade.levels;
		int len = arr.length - 1;
		PetGrade pg;
		for (int i = len; i >= 0; i--) {
			pg = arr[i];
			if (pg.lvMin <= level) {
				return pg;
			}
		}
		return null;
	}

	/**
	 * 发送宠物图鉴列表。
	 */
	public void petList() {

	}

	/*
	 * 先天技能开启条件
	 * 宠物先天技能
	 * 宠物传承技能
	 */

	/**
	 * 当宠物放生后，玩家将会获得一定数值的血脉值
	 */
	public void fangSheng() {

	}

	/**
	 * 获得某种宠物在排行榜里的第一名。宠物崇拜
	 */
	public void getTop1Pet() {

	}

	public String getConfStr(String str) {
		String ret = PetGrade.translation.get(str);
		if (ret == null) {
			if (str.equals("wuXingStr")) {
				return Translate.悟性;
			} else if (str.equals("gainXuMai")) {
				return Translate.恭喜您获得血脉值;
			} else if ("UI_DESC_SHOUHUN".equals(str)) {
				return HuntLifeManager.instance.translate.get(1);
			}
			log.error("miss translation for key [{}]", str);
			return str;
		} 
		return ret;
	}

	@Override
	public void proc(Event evt) {
		switch (evt.id) {
		case Event.PLAYER_GAIN_PET:
			playerGainPet((EventPlayerGainPet) evt);
			break;
		case Event.PLAYER_DROP_PET:
			playerDropPet((EventPlayerDropPet) evt);
			break;
		case Event.PET_LEVEL_UP:
			petLevelUp(evt);
			break;
		case Event.PET_SCORE_CHANGE:
			updatePetScoreRank(evt);
			break;
		}
	}

	public void updatePetScoreRank(Event evt) {
		EventWithObjParam eo = (EventWithObjParam) evt;
		Pet pet = (Pet) eo.param;
		long petId = pet.getId();
		checkRankData();
		int idx = -1;
		PetRankData[] data = PetALLBillboard.data;
		int len = data.length;
		int curScore = pet.getQualityScore();
		int pre = -1;
		for (int i = 0; i < len; i++) {
			PetRankData d = data[i];
			if (d.score < curScore && idx == -1) {
				idx = i;
				log.info("Pet2Manager.updatePetScoreRank: replace {} master {}", idx, d.getDateValues()[0]);
			}
			if (d.petId == petId && pre == -1) {
				log.info("Pet2Manager.updatePetScoreRank: pre {} master {}", i, d.getDateValues()[0]);
				pre = i;
			}
			if (idx >= 0 && pre >= 0) {
				break;
			}
		}
		if (idx < 0) {
			return;
		}
		if (pre >= 0 && pre < idx) {
			log.debug("不更新，前面的排名比新的大 {}", pet.getName());
			return;
		}
		log.info("Pet2Manager.updatePetScoreRank: pre at {} ", pre);
		PetRankData po;
		PetRankData oldOne = data[idx];
		boolean isSelf = false;
		if (oldOne.petId == pet.getId()) {
			// 自己把自己替了。
			po = oldOne;
			isSelf = true;
		} else {
			if (pre < 0) {
				pre = len - 1;
			}
			for (int i = pre; i > idx; i--) {
				data[i] = data[i - 1];
			}
			po = new PetRankData();
		}
		po.petId = petId;
		po.setDateId(pet.getPetPropsId());
		po.setType(BillboardDate.宠物);
		Player player = null;
		try {
			player = PlayerManager.getInstance().getPlayer(pet.getOwnerId());
		} catch (Exception e1) {
			log.warn("Pet2Manager.updatePetScoreRank: player not find with id {}", pet.getOwnerId());
		}
		String[] values = new String[5];
		values[0] = player == null ? "-" : player.getName();
		values[1] = pet.getPetSort();
		values[2] = pet.getName();
		values[3] = pet.getLevel() + "";
		values[4] = pet.getQualityScore() + "";

		po.setDateValues(values);
		po.category = pet.getCategory();
		po.character = pet.getCharacter();
		po.score = pet.getQualityScore();
		// ;
		data[idx] = po;
		log.info("pet {} {} 进入榜单 {}", new Object[] { pet.getId(), pet.getName(), idx });
		PetALLBillboard.petId2rank.put(pet.getId(), idx);
		if (isSelf) {
			log.debug("Pet2Manager.updatePetScoreRank: is self");
			return;
		}
		Player p = player;
		if (p == null) {
			log.debug("Pet2Manager.updatePetScoreRank: player is null.");
			return;
		}
		String msg = getConfStr("petShowInRank");
		msg = String.format(msg, idx + 1);
		p.sendError(msg);
		//
		if (idx <= 99 && pet.getLevel() > 40) {
			try {
				msg = getConfStr("worldCastShowInRank");
				msg = String.format(msg, p.getName(), pet.getName(), idx + 1);
				ChatMessage msgA = new ChatMessage();
				msgA.setMessageText(msg);
				ChatMessageService.getInstance().sendMessageToSystem(msgA);
				log.info(msg);
				p.sendNotice(msg);
			} catch (Exception e) {
				log.error("发送宠物进入排行公告失败", e);
			}
		}
	}

	public void petLevelUp(Event evt) {
		EventWithObjParam eo = (EventWithObjParam) evt;
		Pet pet = (Pet) eo.param;
		Pet2SkillCalc.getInst().petLevelUp(pet);
	}

	public PetsOfPlayer findPetsOfPlayer(Player p) {
		PetsOfPlayer bean = null;
		long pid = p.getId();
		bean = (PetsOfPlayer) cache.get(pid);
		if (bean != null) {
			if (bean.pets == null) {
				bean.setPets("");
				log.error("Pet2Manager.findPetsOfPlayer: AA fix null for {} {}", pid, p.getName());
			}
			return bean;
		}
		try {
			bean = petsOfPlayerBeanEm.find(pid);
		} catch (Exception e) {
			log.error("find bean fail when proc drop pet by {}", pid);
			return null;
		}
		if (bean == null) {
			bean = new PetsOfPlayer();
			bean.pid = pid;
			bean.pets = comboPetInBag(p);
			try {
				petsOfPlayerBeanEm.save(bean);
			} catch (Exception e) {
				log.debug("Pet2Manager.findPetsOfPlayer: exception ", e);
			}
			log.info("Pet2Manager.findPetsOfPlayer: create pid {}, pets [{}]", pid, bean.pets);
		}
		if (bean.pets == null) {
			bean.setPets("");
			log.error("Pet2Manager.findPetsOfPlayer: BB fix null for {} {}", pid, p.getName());
		}
		cache.put(pid, bean);
		return bean;
	}

	public void playerDropPet(EventPlayerDropPet evt) {
		Player p = evt.player;
		Pet pet = evt.pet;
		PetsOfPlayer bean = findPetsOfPlayer(p);

		int v = 10;
		switch (pet.getRarity()) {
		case 0:
			v = 10;
			break;
		case 1:
			v = 20;
			break;
		case 2:
			v = 30;
			break;
		case 3:
			v = 50;
			break;
		}
		int diff = (int) (v * (1.0f + pet.getLevel() / 50 + (pet.getGeneration() == 1 ? 0.5f : 0) + 0.1f * pet.getVariation()));
		CompoundReturn cr = AllActivityManager.instance.notifySthHappened(AllActivityManager.bloodActivity, pet.getRarity());
		if (cr != null && cr.getBooleanValue() && cr.getDoubleValue() > 0) {
			diff = (int) ((1 + cr.getDoubleValue()) * diff);
		}
		if (ActivitySubSystem.logger.isDebugEnabled()) {
			ActivitySubSystem.logger.debug("[放生血脉增加活动] [放生宠物稀有度:" + pet.getRarity() + "] [cr :"  + cr + "] [" + ( cr != null ? cr.getDoubleValue() + "_" + cr.getBooleanValue() : "" )+ "]");
		}
		// 血脉值=稀有度*（1+等级/K值+繁殖加成+变异加成）
		synchronized (pet) {
			bean.xueMai += diff;
		}
		petsOfPlayerBeanEm.notifyFieldChange(bean, "xueMai");
		try {
			petsOfPlayerBeanEm.save(bean);
		} catch (Exception e) {
			log.error("save fail when add xueMai, pid {}, xueMai {}", p.getId(), bean.xueMai);
		}
		log.warn("player {} drop pet {} gain xueMai {} -> {}", new Object[] { p.getName(), pet.getCategory(), diff, bean.xueMai });
		String msg = getConfStr("gainXuMai");
		try {
			msg = String.format(msg, diff);
		} catch (Exception e) {
			log.error("Pet2Manager.playerDropPet: format exception {}", e.toString());
		}
		p.sendError(msg);
		//
		PetRankData[] arr = PetALLBillboard.data;
		if (arr == null) {
			return;
		}
		int idx = -1;
		int find = -1;
		for (PetRankData d : arr) {
			idx++;
			if (d.petId == pet.getId()) {
				find = idx;
				break;
			}
		}
		if (find >= 0) {
			PetALLBillboard.data = (PetRankData[]) ArrayUtils.remove(arr, find);
			PetALLBillboard.inst.setDates(PetALLBillboard.data);
			log.warn("Pet2Manager.playerDropPet: remove from rank {} pet id {}", find, pet.getId());
		}
		// 同步血脉。崩溃？
		if (syncXueMai) {
			PET2_XueMai_RES res = new PET2_XueMai_RES(GameMessageFactory.nextSequnceNum(), bean.xueMai);
			p.addMessageToRightBag(res, "同步血脉");
			log.debug("Pet2Manager.playerDropPet: 发送同步血脉包。{}", p.getName());
		}
	}

	public String comboPetInBag(Player p) {
		Knapsack bag = p.getPetKnapsack();
		if (bag == null) {
			log.error("player pet bag is null when combo pet names, {}", p.getName());
			return "";
		}
		Cell[] arr = bag.getCells();
		if (arr == null) {
			log.error("player pet bag cell is null when combo pet names, {}", p.getName());
			return "";
		}
		int len = arr.length;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			ArticleEntity e = bag.getArticleEntityByCell(i);
			if (e == null) {
				continue;
			}
			Article a = ArticleManager.getInstance().getArticle(e.getArticleName());
			if (a == null) {
				log.error("article not found for pet {} of player {}", e.getArticleName(), p.getName());
				continue;
			}
			sb.append(splitKey);
			sb.append(a.getName_stat());
		}
		String ret = sb.toString();
		log.info("combo {} for {}", ret, p.getName());
		return ret;
	}

	public void playerGainPet(EventPlayerGainPet evt) {
		Player p = evt.player;
		log.info("{} gain pet {}", p.getName(), evt.petArticle.getName_stat());
		PetsOfPlayer bean = findPetsOfPlayer(p);

		String curKey = splitKey.concat(evt.petArticle.getName_stat());
		if (bean.pets.indexOf(curKey) >= 0) {
			log.info("exists {} pet {}", p.getName(), curKey);
			return;
		}
		log.info("try add pet {} to player {}", curKey, p.getName());
		bean.pets = bean.pets.concat(curKey);
		petsOfPlayerBeanEm.notifyFieldChange(bean, "pets");
		try {
			petsOfPlayerBeanEm.save(bean);
		} catch (Exception e) {
			log.error("save fail.", e);
			return;
		}
		log.info("save ok, pet {} to {}", curKey, p.getName());
		// 检查获得所有宠物图鉴
		boolean miss = false;
		for (GradePet gp : PetGrade.petList) {
			String key = gp.progName;
			if (bean.pets.indexOf(key) < 0) {
				miss = true;
				break;
			}
		}
		if (!miss) {
			log.debug("Pet2Manager.playerGainPet: {} have all pet", p.getName());
		}
	}

	@Override
	public void doReg() {
		EventRouter.register(Event.PLAYER_GAIN_PET, this);
		EventRouter.register(Event.PLAYER_DROP_PET, this);
		EventRouter.register(Event.PET_LEVEL_UP, this);
		EventRouter.register(Event.PET_SCORE_CHANGE, this);
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public String[] getInterestedMessageTypes() {
		return new String[] { "PET_JIN_JIE_REQ", "PET_LIST_REQ", "PET_DETAIL_REQ", "PET_CHONG_BAI_REQ", "PET_BOOK_UI_REQ", "PET_SKILL_TAKE_REQ", "PET_TALENT_UP_REQ", "PET_SKILL_UP_REQ", "PET2_QUERY_BY_ARTICLE_REQ", "PET2_QUERY_REQ", "QUERY_SKILLINFO_PET2_REQ", "PET2_LIAN_HUN_REQ", "PET2_LIAN_DAN_REQ", "PET2_UI_DESC_REQ", "PET2_UP_LV_REQ", "PET2_XueMai_REQ", "PET2_HunArticle_REQ", "PET_FIND_CUR_INDEX_REQ", "PET2_LiZi_REQ", "PET3_QUERY_BY_ARTICLE_REQ" ,"NEW_PET_DETAIL_REQ"};
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		Player p = check(conn, message, log);
		log.debug("{} req {}", p.getName(), message.getTypeDescription());
		if (message instanceof PET_JIN_JIE_REQ) {
			return jinJie(p, (PET_JIN_JIE_REQ) message, true);
		} else if (message instanceof PET_LIST_REQ) {
			return sendPetList(p, (PET_LIST_REQ) message);
		} else if (message instanceof NEW_PET_LIST_REQ) {
			return sendNEWPetList(p, (NEW_PET_LIST_REQ) message);
		}else if (message instanceof PET_DETAIL_REQ) {
			return sendDetail((PET_DETAIL_REQ) message);
		} else if (message instanceof NEW_PET_DETAIL_REQ) {
			return sendNewDetail((NEW_PET_DETAIL_REQ) message);
		}else if (message instanceof PET_CHONG_BAI_REQ) {
			return sendChongBai(conn, p, (PET_CHONG_BAI_REQ) message);
		} else if (message instanceof PET_BOOK_UI_REQ) {
			PET_BOOK_UI_RES s = Pet2SkillCalc.getInst().makeBookRes(p, message.getSequnceNum(), "");
			return s;
		} else if (message instanceof PET_SKILL_TAKE_REQ) {
			return takeSkill(p, (PET_SKILL_TAKE_REQ) message);
		} else if (message instanceof PET_TALENT_UP_REQ) {
			return talentSkillUp(p, (PET_TALENT_UP_REQ) message);
		} else if (message instanceof PET_SKILL_UP_REQ) {
			return skillLvUp(p, (PET_SKILL_UP_REQ) message);
		} else if (message instanceof PET2_QUERY_REQ) {
			return sendPetInfo(conn, (PET2_QUERY_REQ) message, p);
		} else if (message instanceof PET3_QUERY_BY_ARTICLE_REQ) {
			return sendPetQueryByArt3(conn, (PET3_QUERY_BY_ARTICLE_REQ) message, p);
		} else if (message instanceof PET2_QUERY_BY_ARTICLE_REQ) {
			return sendPetQueryByArt(conn, (PET2_QUERY_BY_ARTICLE_REQ) message, p);
		} else if (message instanceof PET2_LIAN_HUN_REQ) {
			return lianHun(p, (PET2_LIAN_HUN_REQ) message);
		} else if (message instanceof PET2_LIAN_DAN_REQ) {
			return lianDan(p, (PET2_LIAN_DAN_REQ) message);
		} else if (message instanceof PET2_UI_DESC_REQ) {
			return sendUIDesc(p, (PET2_UI_DESC_REQ) message);
		} else if (message instanceof QUERY_SKILLINFO_PET2_REQ) {
			return sendSkDesc(conn, (QUERY_SKILLINFO_PET2_REQ) message, p);
		} else if (message instanceof PET2_XueMai_REQ) {
			return sendXueMai(p, (PET2_XueMai_REQ) message);
		} else if (message instanceof PET2_UP_LV_REQ) {
			return hunUp(p, (PET2_UP_LV_REQ) message);
		} else if (message instanceof PET_FIND_CUR_INDEX_REQ) {
			return findCurTuJianIndex(p, message);
		} else if (message instanceof PET2_HunArticle_REQ) {
			PET2_HunArticle_RES ret = new PET2_HunArticle_RES(message.getSequnceNum(), getConfStr("xiulianyaohun"));
			return ret;
		} else if (message instanceof PET2_LiZi_REQ) {
			return sendLiZi((PET2_LiZi_REQ) message, p, conn);
		} else if (message instanceof PET3_QUERY_REQ) {
			return sendPetInfo3(conn, (PET3_QUERY_REQ) message, p);
		} else {
			log.warn("未处理的请求");
		}
		return null;
	}

	public ResponseMessage sendLiZi(PET2_LiZi_REQ message, Player p, Connection conn) {
		long petId = message.getPetId();
		Pet pet = PetManager.getInstance().getPet(petId);
		if (pet == null) {
			log.error("Pet2Manager.sendLiZi: 宠物没有找到{}", petId);
			return null;
		}
		long seq = message.getSequnceNum();
		PET_QUERY_REQ oldReq = new PET_QUERY_REQ(seq, message.getPetId());
		PET_QUERY_RES oldRes = PetSubSystem.getInstance().handle_PET_QUERY_REQ(conn, oldReq, p);
		PET2_QUERY_RES res = makeNewPetQueryRes(seq, oldRes);
		PET2_LiZi_RES ret = makePetInfoWithLizi(pet, seq, res);
		return ret;
	}

	public PET2_LiZi_RES makePetInfoWithLizi(Pet pet, long seq, PET2_QUERY_RES res) {
		PET2_LiZi_RES ret = null;
		if (res != null) {
			ret = new PET2_LiZi_RES(seq, pet, res.getSkillInfos(), res.getPetSkillReleaseProbability(), res.getDescription(), res.getBornSkills(), res.getTalentSkills(), res.getGrade(), res.getWuXing(), res.getAddBooks(), res.getRankIndex(), res.getHunExp(), res.getToNextHunExp(), res.getJinJieAtt());
			log.debug("Pet2Manager.sendLiZi: 发送宠物粒子 {}", pet.getName());
		} else {
			log.debug("构造老协议出错 {}", pet.getName());
		}
		return ret;
	}

	public ResponseMessage findCurTuJianIndex(Player p, RequestMessage message) {
		int level = p.getLevel();
		GradePet[] arr = PetGrade.petList;
		int idx = -1;
		int preTakeLv = 0;
		int find = -1;
		for (GradePet gp : arr) {
			idx++;
			int takeLevel = gp.getTakeLevel();
			if (takeLevel <= level) {
				if (preTakeLv != takeLevel) {// 只取同等级第一个。
					find = idx;
					preTakeLv = takeLevel;
				}
			} else if (takeLevel > level) {
				break;
			}
		}
		PET_FIND_CUR_INDEX_RES ret = new PET_FIND_CUR_INDEX_RES(message.getSequnceNum(), find);
		log.warn("Pet2Manager.findCurTuJianIndex: index:{}--takeLevel:{}", find,preTakeLv);
		return ret;
	}

	public ResponseMessage sendXueMai(Player p, PET2_XueMai_REQ message) {
		PetsOfPlayer bean = findPetsOfPlayer(p);
		if (bean == null) {
			return null;
		}
		PET2_XueMai_RES res = new PET2_XueMai_RES(message.getSequnceNum(), bean.xueMai);
		return res;
	}

	public ResponseMessage hunUp(Player p, PET2_UP_LV_REQ message) {
		long petId = message.getPetId();
		Pet pet = PetManager.getInstance().getPet(petId);
		if (pet == null) {
			log.error("hunUp pet not find {}", petId);
			return null;
		}
		if (!ArticleProtectManager.instance.isCanDo(p, ArticleProtectDataValues.ArticleProtect_High, pet.getPetPropsId())) {
			p.sendError(Translate.高级锁魂物品不能做此操作);
			return null;
		}
		int curLv = pet.getTrainLevel();
		LianHunConf conf = PetGrade.lianHunMap.get(curLv);
		if (conf == null) {
			log.error("Pet2Manager.hunUp: conf not find {}", curLv);
			return null;
		}
		int needV = conf.toNextLvExp;
		if (pet.getHunExp() < needV) {
			return null;
		}
		int newLv = curLv;
		boolean find = false;
		Iterator<LianHunConf> it = PetGrade.lianHunMap.values().iterator();
		int newNeedHun = 0;
		int newRealV = newLv;
		while (it.hasNext()) {
			LianHunConf cc = it.next();
			if (cc.takeLevel > curLv) {
				newLv = cc.takeLevel;
				newRealV = cc.progLevel;
				newNeedHun = cc.toNextLvExp;
				find = true;
				break;
			}
		}
		if (find == false) {
			log.error("Pet2Manager.hunUp: not find next lv, cur {}", curLv);
			p.sendError(getConfStr("xieDaiDengJiYiShiZuiDa"));
			return null;
		}
		int sealLevel = SealManager.getInstance().getSealLevel();
		if (newLv > sealLevel) {
			String msg = "fengYinXianZhi";
			msg = getConfStr(msg);
			p.sendError(msg);
			return null;
		}
		pet.setHunExp(pet.hunExp - needV);
		pet.setRealTrainLevel(newRealV);
		pet.setTrainLevel(newLv);
		pet.init();
		log.info("pet id {} pain hun {} , change take lv to {}", new Object[] { petId, needV, newLv });
		p.sendError(com.fy.engineserver.datasource.language.Translate.操作成功);
		PET2_UP_LV_RES res = new PET2_UP_LV_RES(message.getSequnceNum(), petId, newLv, newNeedHun);
		try {
			int ti = 0;
			if(newRealV == 90) {
				ti = 1;
			}	else if (newRealV == 135) {
				ti = 2;
			}	else if (newRealV == 180) {
				ti = 3;
			}	else if (newLv >= 220) {
				ti = 4;
			}
			RecordAction action = PlayerAimManager.升宠物携带等级action[ti];
			if(action != null) {
				EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { p.getId(), action, 1L});
				EventRouter.getInst().addEvent(evt3);
			}
		} catch (Exception e) {
			PlayerAimManager.logger.error("[目标系统] [统计宠物升携带等级异常] [" + p.getLogString() + "]", e);
		}
		return res;
	}

	public ResponseMessage sendUIDesc(Player p, PET2_UI_DESC_REQ message) {
		String key = message.getDescKey();
		String desc = getConfStr(key);
		PET2_UI_DESC_RES ret = new PET2_UI_DESC_RES(message.getSequnceNum(), key, desc);
		return ret;
	}

	public ResponseMessage lianDan(Player p, PET2_LIAN_DAN_REQ message) {
		long petId = message.getPetId();
		if (petId == p.getActivePetId()) {
			p.sendError(Translate.pet_fight);
			return null;
		}
		Pet pet = PetManager.getInstance().getPet(petId);
		if (pet == null) {
			log.error("lian dan pet not find {}", petId);
			return null;
		}
		for(long pid : p.getPetCell()){
			if(pid == pet.getId()){
				p.sendError(Translate.助战中的宠物不能封印);
				return null;
			}
		}
		
		if(PetHouseManager.getInstance().petIsStore(p, pet)){
			p.sendError(Translate.挂机中的宠物不能封印);
			return null; 
		}
		
		if (pet.isDelete()) {
			log.error("想要炼化删除掉的宠物 {} {}", p.getLogString(), petId);
			return null;
		}
		if (!ArticleProtectManager.instance.isCanDo(p, ArticleProtectDataValues.ArticleProtect_Common, pet.getPetPropsId())) {
			p.sendError(Translate.锁魂物品不能做此操作);
			return null;
		}
		if (!ArticleProtectManager.instance.isCanDo(p, ArticleProtectDataValues.ArticleProtect_High, pet.getPetPropsId())) {
			p.sendError(Translate.高级锁魂物品不能做此操作);
			return null;
		}
		if (pet.getRarity() < PetManager.千载难逢) {
			p.sendError(getConfStr("xiYouDuTaiDi"));
			return null;
		}
		int takeLv = pet.getTrainLevel();
		LianHunConf conf = PetGrade.lianHunMap.get(takeLv);
		if (conf == null) {
			log.error("lianDan conf not find for take lv {}", takeLv);
		}
		Knapsack bag = p.getKnapsack_common();
		int gridLeft = bag.getEmptyNum();
		if (gridLeft < 1) {
			String msg = getConfStr("bagFull");
			p.sendError(msg);
			return null;
		}
		String artName = conf.articleName;
		int reason = ArticleEntityManager.PET2_PET_TO_DanYao;
		Article art = ArticleManager.getInstance().getArticle(artName);
		if (art == null) {
			log.error("lianDan article not find {}", artName);
			return null;
		}
		int color = art.getColorType();
		ArticleEntity ae = null;
		try {
			ae = ArticleEntityManager.getInstance().createEntity(art, false, reason, p, color, 1, true);
		} catch (Exception e) {
			log.error("lianDan create entity fail", e);
			return null;
		}
		
		for(long pid : p.getPetCell()){
			if(pid == pet.getId()){
				p.sendError(Translate.助战的宠物不能炼化);
				return null;
			}
		}
		
		ArticleEntity petEntityInBag = p.getPetKnapsack().removeByArticleId(pet.getPetPropsId(), "宠物炼做丹药", true);
		PetManager.getInstance().deletePet(p, pet);
		log.warn("宠物炼做丹药，移除宠物 {}", petEntityInBag);
		log.warn("宠物炼做丹药，移除宠物 {} 玩家 {}", petId, p.getName());
		bag.put(ae, "宠物转为魂魄丹药");
		p.sendError(String.format(getConfStr("UI_DESC_lianhuachenggong"), ae.getArticleName()));
		//
		PET2_LIAN_DAN_RES res = new PET2_LIAN_DAN_RES(message.getSequnceNum(), petId);
		return res;
	}

	public ResponseMessage lianHun(Player p, PET2_LIAN_HUN_REQ message) {
		long petId = message.getPetId();
		if (petId == p.getActivePetId()) {
			p.sendError(Translate.pet_fight);
			return null;
		}
		long foodId = message.getFoodId();
		Pet pet = PetManager.getInstance().getPet(petId);
		if (pet == null) {
			log.error("lian hun pet not find {}", petId);
			return null;
		}
		
		for(long pid : p.getPetCell()){
			if(pid == pet.getId()){
				p.sendError(Translate.助战中的宠物不能封印);
				return null;
			}
		}
		
		if(PetHouseManager.getInstance().petIsStore(p, pet)){
			p.sendError(Translate.挂机中的宠物不能封印);
			return null; 
		}
		
		ArticleEntity ae = p.getArticleEntity(foodId);
		if (ae == null) {
			log.error("lianHun article entity not find {}", foodId);
			return null;
		}
		LianHunConf conf = findLianHunConf(ae.getArticleName());
		if (conf == null) {
			log.error("lianHun conf not find {}", ae.getArticleName());
			return null;
		}
		LianHunConf confCur = PetGrade.lianHunMap.get(pet.getTrainLevel());
		if (confCur == null) {
			log.error("Pet2Manager.lianHun: 宠物当前携带等级的配置未找到 {}", pet.getTrainLevel());
			return null;
		}
		if (pet.hunExp > confCur.toNextLvExp) {
			String msg = "plzUpHunFirst";
			msg = getConfStr(msg);
			p.sendError(msg);
			return null;
		}
		removeArticle(p, ae, "吃魂道具");
		pet.setHunExp(pet.hunExp + conf.dropExp);
		log.info("宠物id {} 获得魂值 {} , 达到 {}", new Object[] { petId, conf.dropExp, pet.hunExp });

		PET2_LIAN_HUN_REs res = new PET2_LIAN_HUN_REs(message.getSequnceNum(), petId, pet.hunExp, confCur.toNextLvExp);
		return res;
	}

	/**
	 * 根据物品名称查找对应的炼魂道具。
	 * @param articleName
	 * @return
	 */
	public LianHunConf findLianHunConf(String articleName) {
		Iterator<LianHunConf> it = PetGrade.lianHunMap.values().iterator();
		while (it.hasNext()) {
			LianHunConf conf = it.next();
			if (articleName.equals(conf.articleName)) {
				return conf;
			}
		}
		return null;
	}

	public ResponseMessage sendSkDesc(Connection conn, QUERY_SKILLINFO_PET2_REQ req, Player p) {
		PetManager pm = PetManager.getInstance();
		int id = req.getId();
		long petId = req.getPetId();
		if (petId == -1000) {
			GenericSkill sk = GenericSkillManager.getInst().maps.get(id);
			if (sk == null) {
				log.error("Pet2Manager sendSkDesc skill not find with id {}", id);
				return null;
			}
			String desc = sk.getDescription();
			QUERY_SKILLINFO_PET2_RES ret = new QUERY_SKILLINFO_PET2_RES(req.getSequnceNum(), petId, id, desc, 1);
			log.debug("Pet2Manager sendSkDesc {}", desc);
			return ret;
		}
		Pet pet = pm.getPet(petId);
		if (pet == null) {
			log.error("pet is null ");
			return null;
		}
		int level = 1;
		Skill skill = GenericSkillManager.getInst().maps.get(id);
		// 计算等级。
		String gainDesc = "";
		PetProps petProp = pet.petProps;
		if (skill == null) {
			log.info("sendSkDesc not find in GenericSkillManager {}", id);
			CareerManager cm = CareerManager.getInstance();
			int[] skillArr = pet.getSkillIds();
			skill = cm.getSkillById(id);
			if (skill == null) {
				log.error("sendSkDesc not find in CareerManager {}", id);
				return null;
			} else {
				log.debug("Pet2Manager.sendSkDesc: find in CareerManager.");
			}
			if (skillArr == null || skillArr.length == 0) {
				log.error("请求技能描述是没有找到基础技能 {}", id);
				return null;
			}
			for (int i = 0; i < skillArr.length; i++) {
				if (skillArr[i] == id) {
					level = pet.getActiveSkillLevels()[i];
					break;
				}
			}
		} else if (petProp != null && petProp.talent1skill == id) {
			gainDesc = getConfStr("UI_DESC_xiantianjineng") + (pet.talent1Skill <= 0 ? "\n<f color='0xFF0000'>"+Translate.未领悟+"</f>" : "");
		} else if (petProp != null && petProp.talent2skill == id) {
			gainDesc = getConfStr("UI_DESC_houtian") + (pet.talent2Skill <= 0 ? "\n<f color='0xFF0000'>"+Translate.未领悟+"</f>" : "");
		} else {
			int[] tianFuSkIds = pet.tianFuSkIds;
			if (tianFuSkIds != null) {
				for (int i = 0; i < tianFuSkIds.length; i++) {
					if (tianFuSkIds[i] == id) {
						level = pet.tianFuSkIvs[i];
						gainDesc = "1";// 控制描述中不出lv
						break;
					}
				}
			}
		}
		String desc = SkillInfoHelper.generate(pet, skill, level, gainDesc);
		QUERY_SKILLINFO_PET2_RES ret = new QUERY_SKILLINFO_PET2_RES(req.getSequnceNum(), petId, id, desc, level);
		return ret;
	}

	public ResponseMessage sendPetQueryByArt3(Connection conn, PET3_QUERY_BY_ARTICLE_REQ message, Player p) {
		long seq = message.getSequnceNum();
		PET_QUERY_BY_ARTICLE_REQ oldReq = new PET_QUERY_BY_ARTICLE_REQ(seq, message.getPetEntityId(), message.getIsOwner());
		PET_QUERY_RES oldRes = PetSubSystem.getInstance().handle_PET_QUERY_BY_ARTICLE_REQ(conn, oldReq, p);
		log.debug("Pet2Manager.sendPetQueryByArt3: {} 1", oldRes == null ? "null res" : oldRes.getPet().getName());
		PET2_QUERY_RES res = makeNewPetQueryRes(seq, oldRes);
		log.debug("Pet2Manager.sendPetQueryByArt3: {} 2", res == null ? "null ret" : res.getPet().getName());

		PET2_LiZi_RES ret0 = null;
		if (res != null) {
			ret0 = makePetInfoWithLizi(res.getPet(), seq, res);
		}
		return ret0;
	}

	public ResponseMessage sendPetQueryByArt(Connection conn, PET2_QUERY_BY_ARTICLE_REQ message, Player p) {
		long seq = message.getSequnceNum();
		PET_QUERY_BY_ARTICLE_REQ oldReq = new PET_QUERY_BY_ARTICLE_REQ(seq, message.getPetEntityId(), message.getIsOwner());
		PET_QUERY_RES oldRes = PetSubSystem.getInstance().handle_PET_QUERY_BY_ARTICLE_REQ(conn, oldReq, p);
		log.debug("Pet2Manager.sendPetQueryByArt: {} 1", oldRes == null ? "null res" : oldRes.getPet().getName());
		PET2_QUERY_RES ret = makeNewPetQueryRes(seq, oldRes);
		log.debug("Pet2Manager.sendPetQueryByArt: {} 2", ret == null ? "null ret" : ret.getPet().getName());
		return ret;
	}

	public PET3_QUERY_RES makeNewPetQueryRes3(long seq, PET_QUERY_RES oldRes) {
		if (oldRes == null) {
			return null;
		}
		Pet pet = oldRes.getPet();
		GenericSkillManager gSkMgr = GenericSkillManager.getInst();
		int t1id = pet.petProps == null ? 0 : pet.petProps.talent1skill;
		int t2id = pet.petProps == null ? 0 : pet.petProps.talent2skill;
		SkillInfo[] bornSkills = new SkillInfo[] { gSkMgr.buildSkillInfo(t1id, log), gSkMgr.buildSkillInfo(t2id, log) };
		// 客户端通过这个属性控制天生技能是否点亮。
		bornSkills[0].setColumnIndex((byte) (pet.talent1Skill > 0 ? 1 : 0));
		bornSkills[1].setColumnIndex((byte) (pet.talent2Skill > 0 ? 1 : 0));
		int[] tfIds = pet.getTianFuSkIds();
		if (tfIds == null) {
			tfIds = new int[0];
		}
		SkillInfo[] talentSkills = new SkillInfo[tfIds.length];
		for (int i = 0; i < tfIds.length; i++) {
			talentSkills[i] = gSkMgr.buildSkillInfo(tfIds[i], log);
		}
		int grade = pet.grade;
		int times = pet.addBookTimes;
		int wuXing = pet.getWuXing();
		if (wuXing == 0) {
			Pet2SkillCalc.getInst().calcWuXing(pet, log);
		}
		Integer iv = PetALLBillboard.petId2rank.get(pet.getId());
		int rank = -1;
		if (iv != null) {
			rank = iv + 1;
		}
		int hunExp = pet.getHunExp();
		int curLv = pet.getTrainLevel();
		int toNextHunExp = Integer.MAX_VALUE;
		LianHunConf conf = PetGrade.lianHunMap.get(curLv);
		if (conf == null) {
			log.error("Pet2Manager.makeNewPetQueryRes: LianHunConf not find {}", curLv);
		} else {
			toNextHunExp = conf.toNextLvExp;
		}
		int[] jinJieAttArr = null;
		if (PetGrade.levels != null) {
			jinJieAttArr = PetGrade.levels[pet.grade].arrArr;
		} else {
			jinJieAttArr = new int[5];
		}
		PET3_QUERY_RES ret = new PET3_QUERY_RES(seq, pet, oldRes.getSkillInfos(), oldRes.getPetSkillReleaseProbability(), oldRes.getDescription(), bornSkills, talentSkills, grade, wuXing, times, rank, hunExp, toNextHunExp, jinJieAttArr);
		return ret;
	}
	
	public PET2_QUERY_RES makeNewPetQueryRes(long seq, PET_QUERY_RES oldRes) {
		if (oldRes == null) {
			return null;
		}
		Pet pet = oldRes.getPet();
		GenericSkillManager gSkMgr = GenericSkillManager.getInst();
		int t1id = pet.petProps == null ? 0 : pet.petProps.talent1skill;
		int t2id = pet.petProps == null ? 0 : pet.petProps.talent2skill;
		SkillInfo[] bornSkills = new SkillInfo[] { gSkMgr.buildSkillInfo(t1id, log), gSkMgr.buildSkillInfo(t2id, log) };
		// 客户端通过这个属性控制天生技能是否点亮。
		bornSkills[0].setColumnIndex((byte) (pet.talent1Skill > 0 ? 1 : 0));
		bornSkills[1].setColumnIndex((byte) (pet.talent2Skill > 0 ? 1 : 0));
		int[] tfIds = pet.getTianFuSkIds();
		if (tfIds == null) {
			tfIds = new int[0];
		}
		SkillInfo[] talentSkills = new SkillInfo[tfIds.length];
		for (int i = 0; i < tfIds.length; i++) {
			talentSkills[i] = gSkMgr.buildSkillInfo(tfIds[i], log);
		}
		int grade = pet.grade;
		int times = pet.addBookTimes;
		int wuXing = pet.getWuXing();
		if (wuXing == 0) {
			Pet2SkillCalc.getInst().calcWuXing(pet, log);
		}
		Integer iv = PetALLBillboard.petId2rank.get(pet.getId());
		int rank = -1;
		if (iv != null) {
			rank = iv + 1;
		}
		int hunExp = pet.getHunExp();
		int curLv = pet.getTrainLevel();
		int toNextHunExp = Integer.MAX_VALUE;
		LianHunConf conf = PetGrade.lianHunMap.get(curLv);
		if (conf == null) {
			log.error("Pet2Manager.makeNewPetQueryRes: LianHunConf not find {}", curLv);
		} else {
			toNextHunExp = conf.toNextLvExp;
		}
		int[] jinJieAttArr = null;
		if (PetGrade.levels != null) {
			jinJieAttArr = PetGrade.levels[pet.grade].arrArr;
		} else {
			jinJieAttArr = new int[5];
		}
		PET2_QUERY_RES ret = new PET2_QUERY_RES(seq, pet, oldRes.getSkillInfos(), oldRes.getPetSkillReleaseProbability(), oldRes.getDescription(), bornSkills, talentSkills, grade, wuXing, times, rank, hunExp, toNextHunExp, jinJieAttArr);
		return ret;
	}

	/**
	 * 按id查询。
	 * @param conn
	 * @param message
	 * @param p
	 * @return
	 */
	public ResponseMessage sendPetInfo(Connection conn, PET2_QUERY_REQ message, Player p) {
		long seq = message.getSequnceNum();
		PET_QUERY_REQ oldReq = new PET_QUERY_REQ(seq, message.getPetId());
		PET_QUERY_RES oldRes = PetSubSystem.getInstance().handle_PET_QUERY_REQ(conn, oldReq, p);
		PET2_QUERY_RES ret = makeNewPetQueryRes(seq, oldRes);
		log.debug("Pet2Manager.sendPetInfo: {}", ret.getPet() == null ? " null " : ret.getPet().getName());
		return ret;
	}
	
	public ResponseMessage sendPetInfo3(Connection conn, PET3_QUERY_REQ message, Player p) {
		long seq = message.getSequnceNum();
		PET_QUERY_REQ oldReq = new PET_QUERY_REQ(seq, message.getPetId());
		PET_QUERY_RES oldRes = PetSubSystem.getInstance().handle_PET_QUERY_REQ(conn, oldReq, p);
		PET3_QUERY_RES ret = makeNewPetQueryRes3(seq, oldRes);
		log.debug("Pet2Manager.sendPetInfo: {}", ret.getPet() == null ? " null " : ret.getPet().getName());
		return ret;
	}

	/**
	 * 升级宠物的技能，等级升级。
	 * @param p
	 * @param message
	 * @return
	 */
	public ResponseMessage skillLvUp(Player p, PET_SKILL_UP_REQ message) {
		long petId = message.getPetId();
		if (petId == p.getActivePetId()) {
			p.sendError(Translate.pet_fight);
			return null;
		}
		long entityId = message.getBookInBagIdx();
		ArticleEntity ae = p.getArticleEntity(entityId);
		if (ae == null) {
			log.error("article entity not found {} of {}", entityId, p.getName());
			return null;
		}
		Article art = ArticleManager.getInstance().getArticle(ae.getArticleName());
		if (art == null) {
			log.error("article not found,talent up, {}", ae.getArticleName());
			return null;
		}
		PetSkillProp prop = null;
		if (art instanceof PetSkillProp == false) {
			log.error("not PetSkillProp {} {}", art.getClass().getSimpleName(), art.getName_stat());
			return null;
		}
		prop = (PetSkillProp) art;
		int skillId = prop.skillId;
		Pet pet = PetManager.getInstance().getPet(petId);
		if (pet == null) {
			log.error("pet not found when talent up {}", petId);
			return null;
		}
		
		for(long pid : p.getPetCell()){
			if(pid == pet.getId()){
				p.sendError(Translate.助战中的宠物不能封印);
				return null;
			}
		}
		
		if(PetHouseManager.getInstance().petIsStore(p, pet)){
			p.sendError(Translate.挂机中的宠物不能封印);
			return null; 
		}
		
		if (!ArticleProtectManager.instance.isCanDo(p, ArticleProtectDataValues.ArticleProtect_High, pet.getPetPropsId())) {
			p.sendError(Translate.高级锁魂物品不能做此操作);
			return null;
		}

		int[] skillArr = null;
		int[] lvArr = null;
		int wantType = message.getTypeCode();
		// 1：基础技能；2：天赋技能
		log.debug("1：基础技能；2：天赋技能（不提供了） , recv {} ", wantType);
		if (wantType == 1) {
			skillArr = pet.getSkillIds();
			lvArr = pet.getActiveSkillLevels();
			// }else if(wantType == 2){
			// skillArr = pet.getTianFuSkIds();
			// lvArr = pet.getTianFuSkIvs();
		} else {
			log.error("错误的技能分类  {}", wantType);
			return null;
		}
		if (skillArr == null || lvArr == null) {
			log.error("技能数组是null, type {}, pet id {}", wantType, pet.getId());
			return null;
		}
		int changedIndex = -1;
		for (int i = 0; i < skillArr.length; i++) {
			if (skillArr[i] == prop.skillId) {
				changedIndex = i;
				break;
			}
		}
		if (changedIndex < 0) {
			String msg = getConfStr("missThisJiChuSkill");
			p.sendError(msg);
			return null;
		}
		if (changedIndex >= skillArr.length) {
			log.error("目标技能下标越界{}", changedIndex);
			return null;
		}
		int curLv = lvArr[changedIndex];
		if (curLv >= prop.skillLv) {
			log.error("cur lv {} >= book lv {}", curLv, prop.skillLv);
			String msg = "bookSkLvLow";
			msg = getConfStr(msg);
			p.sendError(msg);
			return null;
		}
		if (curLv != prop.skillLv - 1) {
			log.error("cur lv {} != book lv {} - 1", curLv, prop.skillLv);
			String msg = "canNotSpanLv";
			msg = getConfStr(msg);
			p.sendError(msg);
			return null;
		}
		removeArticle(p, ae, "技能升级");
		PET_SKILL_UP_RES ret = new PET_SKILL_UP_RES(message.getSequnceNum(), petId, 0, getConfStr("jiChuJiNengUpOk"));
		lvArr[changedIndex] += 1;
		int tempLevel = lvArr[changedIndex];
		if (wantType == 1) {
			pet.setActiveSkillLevels(lvArr);
			try {
				if (pet.getActiveSkillLevels() != null && pet.getActiveSkillLevels().length > 0) {
					int lv2Num = 0;		//高于二级的技能个数
					int lv3Num = 0;
					for (int i=0; i<pet.getActiveSkillLevels().length; i++) {
						if (pet.getActiveSkillLevels()[i] >= 2) {
							lv2Num ++ ;
							if (pet.getActiveSkillLevels()[i] >= 3) {
								lv3Num ++ ;
							}
						}
					}
					if (lv2Num > 0) {
						if (tempLevel == 2) {
							EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { pet.getOwnerId(), RecordAction.宠物拥有2级基础技能数2, lv2Num });
//							EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { pet.getOwnerId(), RecordAction.宠物拥有2级基础技能个数, lv2Num });
							EventRouter.getInst().addEvent(evt3);
						}
						if (lv3Num > 0 && tempLevel == 3) {
							EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { pet.getOwnerId(), RecordAction.宠物拥有3级基础技能数2, lv3Num });
//							EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { pet.getOwnerId(), RecordAction.宠物拥有3级基础技能个数, lv3Num });
							EventRouter.getInst().addEvent(evt2);
						}
					}
				}
			} catch (Exception e) {
				log.error("[升级宠物基础技能] [异常] [pet.getId():" + pet.getId() + "]", e);
			}
		} else {
			pet.setTianFuSkIvs(lvArr);
		}
//		log.warn("宠物基础技能升级，宠物ID {} 技能 {} 升级到 {}", new Object[] { pet.getId(), skillId, prop.skillLv });
		if(log.isWarnEnabled()){
			log.warn("[宠物基础技能升级] [传承] [成功] [{}] {} [技能:{}] [升级到:{}]", new Object[] {p.getLogString4Knap(), pet.getLogOfInterest(), skillId, prop.skillLv });
		}
		pet.init();
		return ret;
	}

	/**
	 * 天赋技能强化，可以增加技能格子，或者替换技能。
	 * @param p
	 * @param message
	 * @return
	 */
	public ResponseMessage talentSkillUp(Player p, PET_TALENT_UP_REQ message) {
		long petId = message.getPetId();
		if (p.getActivePetId() == petId) {
			p.sendError(Translate.pet_fight);
			return null;
		}
		
		long ticketIdA = message.getTkA();
		long ticketIdB = message.getTkB();
		int skIdxA = message.getBind1Index();
		int skIdxB = message.getBind2Index();
		long entityId = message.getBookInBagIdx();
		log.warn("Pet2Manager.talentSkillUp: bindA at {}, bindB at{}", skIdxA, skIdxB);
		Pet pet = PetManager.getInstance().getPet(petId);
		if (pet == null) {
			log.error("pet not found when talent up {}", petId);
			return null;
		}
		
		for(long pid : p.getPetCell()){
			if(pid == pet.getId()){
				p.sendError(Translate.助战中的宠物不能封印);
				return null;
			}
		}
		
		if(PetHouseManager.getInstance().petIsStore(p, pet)){
			p.sendError(Translate.挂机中的宠物不能封印);
			return null; 
		}
		
		if (!ArticleProtectManager.instance.isCanDo(p, ArticleProtectDataValues.ArticleProtect_High, pet.getPetPropsId())) {
			p.sendError(Translate.高级锁魂物品不能做此操作);
			return null;
		}
		ArticleEntity ae = p.getArticleEntity(entityId);
		if (ae == null) {
			log.error("article entity not found {} of {}", entityId, p.getName());
			return null;
		}
		Article art = ArticleManager.getInstance().getArticle(ae.getArticleName());
		if (art == null) {
			log.error("article not found,talent up, {}", ae.getArticleName());
			return null;
		}
		PetSkillProp prop = null;
		if (art instanceof PetSkillProp == false) {
			log.error("not PetSkillProp {} {}", art.getClass().getSimpleName(), art.getName_stat());
			return null;
		}
		prop = (PetSkillProp) art;
		String xingGe = prop.xingGe;
		// int xgKey = -1;
		// 谨慎 精明 狡诈 忠臣 通用
		// 忠诚 精明 谨慎 狡诈 (0,1,2,3)
		if ("通用".equals(xingGe)) {
		} else if ("忠诚".equals(xingGe) && pet.getCharacter() == 0) {
		} else if ("精明".equals(xingGe) && pet.getCharacter() == 1) {
		} else if ("谨慎".equals(xingGe) && pet.getCharacter() == 2) {
		} else if ("狡诈".equals(xingGe) && pet.getCharacter() == 3) {
		} else {
			String msg = getConfStr("xingGeBuPiPei");
			p.sendError(msg);
			log.debug("Pet2Manager.talentSkillUp: book xingGe{}, pet c{}", xingGe, pet.getCharacter());
			log.debug("Pet2Manager.talentSkillUp: pet c {}", PetManager.得到宠物性格名(pet.getCharacter()));
			return null;
		}
		ArticleEntity ticketA_entity = null;
		if (ticketIdA > 0) {
			ticketA_entity = p.getArticleEntity(ticketIdA);
			if (ticketA_entity == null) {
				log.error("指定的绑定符A不存在。{} {}", p.getName(), ticketIdA);
				return null;
			}
		}
		// else if(ticketIdB>0){
		// //没有使用绑定符A，却使用了B
		// log.warn("use B without A, {}", p.getName());
		// String msg = getConfStr("canNotUseBWithoutUsingA");
		// p.sendError(msg);
		// return null;
		// }
		//
		ArticleEntity ticketB_entity = null;
		if (ticketIdB > 0) {
			ticketB_entity = p.getArticleEntity(ticketIdB);
			if (ticketB_entity == null) {
				log.error("指定的绑定符B不存在。{} {}", p.getName(), ticketIdB);
				return null;
			}
		}
		if (skIdxA == skIdxB && skIdxA >= 0) {
			log.error("两个绑定的技能不能一样");
			return null;
		}
		//
		int skillId = prop.skillId;
		int skillLv = 1;// prop.skillLv;//天赋技能不能升级。
		GenericSkill confSk = GenericSkillManager.getInst().maps.get(skillId);
		if (confSk == null) {
			log.error("talentSkillUp 技能书配置指定的技能没有找到 {}, id {}", prop.getName_stat(), skillId);
			p.sendError(getConfStr("skNotFind"));
			return null;
		}
		if (skillLv > confSk.buff.values.length) {
			log.error("talentSkillUp 技能书指定的等级 超过技能配置 {}, lv {}", prop.getName_stat(), skillLv);
			return null;
		}

		int addBookTimes = pet.addBookTimes;
		int[] skillArr = pet.tianFuSkIds;
		int[] lvArr = pet.tianFuSkIvs;
		if (skillArr != null) {
			int idx = ArrayUtils.indexOf(skillArr, skillId);
			if (idx >= 0) {
				p.sendError(getConfStr("skillAlreadyGain"));
				return null;
			}
			if (lvArr == null) {
				lvArr = new int[skillArr.length];
				Arrays.fill(lvArr, 1);
				log.warn("fix lv arr , fill 1, pet id {}", petId);
			} else if (lvArr.length != skillArr.length) {
				int[] newArr = new int[skillArr.length];
				Arrays.fill(lvArr, 1);
				System.arraycopy(lvArr, 0, newArr, 0, lvArr.length);
				lvArr = newArr;
				pet.setTianFuSkIvs(lvArr);
				log.warn("fix lv arr , extend 1, pet id {}", petId);
			}
		}
		int curLen = skillArr == null ? 0 : skillArr.length;
		boolean newHole = false;
		boolean removeA_art = false;
		boolean removeB_art = false;
		if (curLen < 10) {
			int tempAdd = 0;
			if (ticketA_entity != null) {
				removeA_art = true;
				tempAdd += 1;
			}
			if (ticketB_entity != null) {
				removeB_art = true;
				tempAdd += 1;
			}
			addBookTimes += tempAdd;
			TalentHoleConf conf = PetGrade.talentHoleConf[curLen];
			if (addBookTimes >= conf.maxTimesThenHit) {
				// 添加次数够了，则肯定开新孔。
				newHole = true;
				pet.setAddBookTimes(0);
				log.warn("new hole by times {"+addBookTimes+"} of {"+p.getLogString()+"} {ticketA_entity="+ticketA_entity+"} {ticketB_entity="+ticketB_entity+"}");
			} else if (curLen == 0) {// 还没有天赋技能。
				log.warn("还没有天赋技能 {} ", pet.getId());
				newHole = true;
			} else if (curLen == 1 && (ticketB_entity != null)) {
				newHole = true;
				removeB_art = true;
				log.warn("remove B {} for new hole", ticketB_entity.getArticleName());
			} else if (curLen == 1 && (ticketA_entity != null)) {
				// 已有一个技能，使用了绑定符，开新孔。
				newHole = true;
				removeA_art = true;
				log.warn("remove A {} for new hole", ticketA_entity.getArticleName());
			} else if (curLen == 2 && ticketA_entity != null && ticketB_entity != null) {
				// 有两个技能，使用了两个符，则开新孔，不检查有没有指定要绑的技能。
				newHole = true;
				removeA_art = true;
				removeB_art = true;
				log.warn("remove A {} B {} for new hole", ticketA_entity.getArticleName(), ticketB_entity.getArticleName());
			} else if (curLen >= 3 && ticketA_entity != null && (skIdxA < 0 || skIdxA >= skillArr.length)) {
				// 指定了绑定符，但没有指定技能
				String msg = getConfStr("plzSpecifySkAforLock");
				p.sendError(msg);
				log.error("没有指定第A个绑定技能, {}", p.getName());
				return null;
			} else if (curLen >= 3 && ticketB_entity != null && skIdxB >= skillArr.length) {
				String msg = getConfStr("plzSpecifySkAforLock");
				p.sendError(msg);
				log.error("没有指定第二个绑定技能, {}", p.getName());
				return null;
			} else {
				int r = new Random().nextInt(10000000);// 1000*10000;
				if (r < conf.rate) {
					newHole = true;
					log.warn("new hole by rnd {} < {}", r, conf.rate);
				} else {
					log.warn("Pet2Manager.talentSkillUp: rnd {} miss on {}", r, conf.rate);
					int addPoint = 1;
					if (ticketA_entity != null) {
						removeA_art = true;
						addPoint += 1;
					}
					if (ticketB_entity != null) {
						removeB_art = true;
						addPoint += 1;
					}
					pet.setAddBookTimes(pet.addBookTimes + addPoint);
				}
			}
		}
		removeArticle(p, ae, "天赋技能开孔-替换");
		if (removeA_art) {
			removeArticle(p, ticketA_entity, "技能绑定符A");
		}
		if (removeB_art) {
			removeArticle(p, ticketB_entity, "技能绑定符B");
		}
		//
		int changedIndex = -1;
		int oldSkillId = 0;
		int oldSkillLevel = 0;
		if (newHole) {
			changedIndex = curLen;
			int[] newArr = ArrayUtils.add(skillArr, skillId);
			pet.setTianFuSkIds(newArr);

			lvArr = ArrayUtils.add(lvArr, skillLv);
			pet.setTianFuSkIvs(lvArr);

			log.warn("开启新的宠物天赋技能 {} lv {} for {}", new Object[] { skillId, skillLv, petId });
			p.sendError(getConfStr("UI_DESC_dinggechenggong"));
			try {
				EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { p.getId(), RecordAction.宠物天赋技能最大数, newArr.length});
				EventRouter.getInst().addEvent(evt2);
			} catch (Exception eex) {
				PlayerAimManager.logger.error("[目标系统] [统计宠物最大天赋技能数异常][" + p.getLogString() + "]", eex);
			}
		} else {
			// 上面的检查了排除了因为使用绑定符造成的强制开孔的情况。
			// 保证锁定的技能不被替换
			int[] pool = new int[curLen];
			for (int i = 0; i < curLen; i++) {
				pool[i] = i;
			}
			int largeOne = Math.max(skIdxA, skIdxB);
			int smallOne = Math.min(skIdxA, skIdxB);
			// 上面有代码检查AB不能一样。
			if (largeOne >= 0) {
				pool = ArrayUtils.remove(pool, largeOne);
				log.warn("Pet2Manager.talentSkillUp: remove L {}", largeOne);
			}
			if (smallOne >= 0) {
				pool = ArrayUtils.remove(pool, smallOne);
				log.warn("Pet2Manager.talentSkillUp: remove S {}", smallOne);
			}
			int r = 0;
			if (pool.length > 0) {
				r = Pet2SkillCalc.getInst().rnd.nextInt(pool.length);
			} else {
				log.error("Pet2Manager.talentSkillUp: 不该出现的情况");
			}
			changedIndex = pool[r];
			log.warn("替换天赋技能格子 {}", changedIndex);
			//
			log.warn("原技能ID {} , 等级 {}", skillArr[changedIndex], lvArr[changedIndex]);
			 oldSkillId = skillArr[changedIndex];
			 oldSkillLevel = lvArr[changedIndex];
			skillArr[changedIndex] = skillId;
			lvArr[changedIndex] = skillLv;
			pet.setTianFuSkIds(skillArr);
			pet.setTianFuSkIvs(lvArr);
			log.warn("新技能 id {} 等级 {} ，宠物id {}", new Object[] { skillId, skillLv, petId });
			p.sendError(getConfStr("UI_DESC_dashuchenggong"));
		}
		pet.init();
		if(log.isWarnEnabled()){
			log.warn("[宠物天赋技能升级] [传承] [成功] [{}] {} [原技能ID:{}] [新技能ID:{}] [原等级:{}] [升级到:{}]", new Object[] {p.getLogString4Knap(), pet.getLogOfInterest(),oldSkillId, skillId,oldSkillLevel, skillLv });
		}
		PET_TALENT_UP_RES ret = new PET_TALENT_UP_RES(message.getSequnceNum(), petId, changedIndex, "fakeIcon");
		return ret;
	}

	public void removeArticle(Player p, ArticleEntity ae, String reason) {
		p.removeFromKnapsacks(ae, reason, true);
		ArticleStatManager.addToArticleStat(p, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.ARTICLES, 1, reason, null);
		log.warn("移除玩家物品 [{}] item [{}] ", new Object[] { p.getName(), ae.getArticleName() });
	}

	public ResponseMessage takeSkill(Player p, PET_SKILL_TAKE_REQ message) {
		long petId = message.getPetId();
		Pet pet = PetManager.getInstance().getPet(petId);
		if (pet == null) {
			log.error("pet not found with id {} for player {}", petId, p.getName());
			return null;
		}
		if (!ArticleProtectManager.instance.isCanDo(p, ArticleProtectDataValues.ArticleProtect_Common, pet.getPetPropsId())) {
			p.sendError(Translate.锁魂物品不能做此操作);
			return null;
		}
		if (!ArticleProtectManager.instance.isCanDo(p, ArticleProtectDataValues.ArticleProtect_High, pet.getPetPropsId())) {
			p.sendError(Translate.高级锁魂物品不能做此操作);
			return null;
		}
		int tgtSkillIndex = message.getTgtSkillIndex();
		int skillIndex = tgtSkillIndex;
		if (skillIndex < 0) {
			log.error("skill index < 0");
			String msg = getConfStr("plzSelectSk");
			p.sendError(msg);
			return null;
		}
		int skillArr[] = pet.getTianFuSkIds();
		int curLvArr[] = pet.getTianFuSkIvs();
		if (skillArr == null || curLvArr == null) {
			log.error("skill arr is null, pet id {}", petId);
			return null;
		}
		if (skillIndex >= skillArr.length || skillIndex >= curLvArr.length) {
			log.error("skill index > arr length, pet id {} player {}", petId, p.getName());
			return null;
		}
		long bagIdx = message.getFuBagIndex();// entity id infact
		ArticleEntity ae = p.getArticleEntity(bagIdx);
		if (ae == null) {
			log.error("article not found, idx {} player {}", bagIdx, p.getName());
			return null;
		}
		Article article = ArticleManager.getInstance().getArticle(ae.getArticleName());
		if (article == null) {
			log.error("article {} not found", ae.getArticleName());
			return null;
		}
		String itemNameKey = article.getName_stat();
		TakePetSkillConf curConf = null;
		for (TakePetSkillConf conf : PetGrade.takePetSkillConf) {
			if (itemNameKey.equals(conf.progName)) {
				curConf = conf;
				break;
			}
		}
		if (curConf == null) {
			log.error("take skill conf not found {}", itemNameKey);
			return null;
		}
		int maxSkillLv = curConf.maxLvAllow;
		int curSkillLv = curLvArr[tgtSkillIndex];
		int curSkillId = skillArr[tgtSkillIndex];
		PetSkillProp book = Pet2SkillCalc.getInst().findBook(curSkillId, curSkillLv);
		if (book == null) {
			log.error("book not find id {} lv {}", curSkillId, curSkillLv);
			p.sendError(getConfStr("bookNotFind"));
			return null;
		}
		log.warn("Pet2Manager.takeSkill: book {} lv {}", book.getName(), book.skillLv);
		log.warn("Pet2Manager.takeSkill: ticket {} maxLv {}", itemNameKey, maxSkillLv);
		if (maxSkillLv < book.skillLv) {
			log.error("抽取符的等级{}低于技能等级{}，不能抽取", maxSkillLv, curSkillLv);
			
			try{
				String msg = "takeSkillTicketLvLow";
				msg = getConfStr(msg);
				//
				String skillmes = "";
				if(maxSkillLv == 1){
					skillmes = "chujijineng";
				}else if(maxSkillLv==2){
					skillmes = "gaojijineng";
				}else if(maxSkillLv == 3){
					skillmes = "chaojijineng";
				}else if(maxSkillLv == 4){
					skillmes = "chaojijineng";
				}
				skillmes = getConfStr(skillmes);
				
				msg = String.format(msg, skillmes);
				p.sendError(msg);
				log.warn("[等级不足等技书] [msg:"+msg+"] [skillmes:"+skillmes+"] [maxSkillLv:"+maxSkillLv+"] [curSkillLv:"+curSkillLv+"]");
			}catch(Exception e){
				log.warn("[等级不足等技书] [maxSkillLv:"+maxSkillLv+"] [curSkillLv:"+curSkillLv+"] ["+e+"]");
			}
			return null;
		}
		Knapsack bag = p.getKnapsack_common();
		int gridLeft = bag.getEmptyNum();
		if (gridLeft < 1) {
			String msg = getConfStr("bagFull");
			p.sendError(msg);
			return null;
		}
		log.warn("check book ok {}", book.getName_stat());
		// 移除抽取符
		p.removeFromKnapsacks(ae, "技能抽取", true);
		ArticleStatManager.addToArticleStat(p, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.ARTICLES, 1, "技能抽取", null);
		log.warn("技能抽取，移除道具 {} from {}", ae.getArticleName(), p.getName());
		// 移除技能
		skillArr = ArrayUtils.remove(skillArr, tgtSkillIndex);
		curLvArr = ArrayUtils.remove(curLvArr, tgtSkillIndex);
		pet.setTianFuSkIds(skillArr);
		pet.setTianFuSkIvs(curLvArr);
		try {
			EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { p.getId(), RecordAction.宠物天赋技能最大数, skillArr.length});
			EventRouter.getInst().addEvent(evt2);
		} catch (Exception eex) {
			PlayerAimManager.logger.error("[目标系统] [统计宠物最大天赋技能数异常][" + p.getLogString() + "]", eex);
		}
		log.warn("remove skill ok");
		
		ArticleEntity newBookAe = null;
		try {
			newBookAe = ArticleEntityManager.getInstance().createEntity(book, false, ArticleEntityManager.PET2技能抽取, p, book.getColorType(), 1, true);
		} catch (Exception e) {
			log.error("player {}, skill {} , lv {}", new Object[] { p.getId(), curSkillId, curSkillLv });
			log.error("create book error", e);
			return null;
		}
		// 给予技能书。
		bag.put(newBookAe, "抽取获得技能");
		log.warn("put book to bag ok");
		String msg = String.format(getConfStr("takeSkillOk"), book.getName());
		p.sendError(msg);
		// pet.init();
		ArticleEntity petEntityInBag = p.getPetKnapsack().removeByArticleId(pet.getPetPropsId(), "技能抽取", true);
		log.warn("takeSkill remove pet entity {}", petEntityInBag);
		log.warn("抽取技能后删除宠物 id {} from player {}", petId, p.getName());
		PET_SKILL_TAKE_RES res = new PET_SKILL_TAKE_RES(message.getSequnceNum(), petId, bagIdx, tgtSkillIndex);
		try {
			EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { p.getId(), RecordAction.宠物技能抽取次数, 1L});
			EventRouter.getInst().addEvent(evt);
		} catch  (Exception e) {
			PlayerAimManager.logger.error("[目标系统] [统计宠物技能抽取异常] [" + p.getLogString() + "]", e);
		}
		return res;
	}

	public ResponseMessage sendChongBai(Connection conn, Player p, PET_CHONG_BAI_REQ message) {
		long petId = message.getPetId();
		Pet pet = PetManager.getInstance().getPet(petId);
		if (pet == null) {
			log.error("pet is null , id {} of {}", petId, p.getName());
			return null;
		}
		checkRankData();
		PetRankData[] arr = PetALLBillboard.data;
		if (arr == null || arr.length == 0) {
			log.error("rank data is null");
			return null;
		}
		String curCate = pet.getCategory();
		byte curChar = pet.getCharacter();
		// 查找最佳对象 优先级为当前宠物形象、性格、排行榜第1
		PetRankData topCate = null;
		PetRankData topChar = null;
		for (PetRankData data : arr) {
			if (topCate == null && curCate.equals(data.category)) {
				topCate = data;
				break;
			}
			if (topChar == null && curChar == data.character) {
				topChar = data;
			}
		}
		log.info("cur cate {} char {}, 请求的宠物 pet id {}", new Object[] { curCate, curChar, petId });
		PetRankData perfect = null;
		if (topCate != null) {
			perfect = topCate;
			log.info("use cate {}, getPetPropsId [{}]， petId [{}]", new Object[] { topCate.category, topCate.getDateId(), topCate.petId });
		} else if (topChar != null) {
			perfect = topChar;
			log.info("use character {}", topChar.character);
		} else {
			perfect = arr[0];
			log.info("use top 1 in rank");
		}
		String ownerName = perfect.getDateValues()[0];
		PET_CHONG_BAI_RES res = new PET_CHONG_BAI_RES(message.getSequnceNum(), perfect.petId, ownerName);
		return res;
	}

	public void checkRankData() {
		if (PetALLBillboard.data != null) {
			return;
		}
		PetALLBillboard rank = PetALLBillboard.inst;
		if (rank == null) {
			rank = new PetALLBillboard();
		}
		try {
			synchronized (PetALLBillboard.class) {
				if (PetALLBillboard.data == null) {
					rank.update();
				}
			}
		} catch (Exception e) {
			log.error("更新榜单失败", e);
			return;
		}
		log.info("更新榜单成功");
	}

	public ResponseMessage sendDetail(PET_DETAIL_REQ message) {
		String progName = message.getProgName();
		GradePet curProp = null;
		for (GradePet petDetail : PetGrade.petList) {
			if (petDetail != null && progName.equals(petDetail.getProgName())) {
				curProp = petDetail;
				break;
			}
		}
		if (curProp == null) {
			log.error("pet not find with name {}", progName);
			return null;
		}
		PET_DETAIL_RES ret = new PET_DETAIL_RES(message.getSequnceNum(), curProp);
		return ret;
	}
	
	public ResponseMessage sendNewDetail(NEW_PET_DETAIL_REQ message) {
		String progName = message.getProgName();
		GradePet curProp = null;
		for (GradePet petDetail : PetGrade.petList) {
			if (petDetail != null && progName.equals(petDetail.getProgName())) {
				curProp = petDetail;
				break;
			}
		}
		if (curProp == null) {
			log.error("pet not find with name new {}", progName);
			return null;
		}
		NEW_PET_DETAIL_RES ret = new NEW_PET_DETAIL_RES(message.getSequnceNum(), curProp);
		return ret;
	}

	public ResponseMessage sendPetList(Player p, PET_LIST_REQ message) {
		if (PetGrade.scaleArr == null) {
			log.error("Pet2Manager sendPetList  conf is wrong");
			return null;
		}
		PetsOfPlayer bean = findPetsOfPlayer(p);
		String havePets = bean.pets;
		GradePet[] petList = PetGrade.petList;
		if (petList == null) {
			petList = new GradePet[0];
			log.error("pet list is null");
		}
		if(petList!=null){
			for(int i=0;i<petList.length;i++){
				if(petList[i]==null){
					log.warn("[宠物检查啊] [pet==null] [name:"+petList[i].getName()+"] [propName:" + petList[i].getProgName() + "] [cname:"+petList[i].getProgName()+"] [icon:"+petList[i].getIcon()+"]");
				}else{
					if(petList[i].getIcon()==null){
						log.warn("[宠物检查啊] [icon==null] [name:"+petList[i].getName()+"] [propName:" + petList[i].getProgName() + "] [cname:"+petList[i].getProgName()+"] [icon:"+petList[i].getIcon()+"]");
					}
				}
			}
		}
		
		PET_LIST_RES ret = new PET_LIST_RES(message.getSequnceNum(), havePets, petList, PetGrade.scaleArr);
		return ret;
	}

	public ResponseMessage sendNEWPetList(Player p, NEW_PET_LIST_REQ message) {
		if (PetGrade.scaleArr == null) {
			log.error("Pet2Manager sendPetList  conf is wrongNEW");
			return null;
		}
		PetsOfPlayer bean = findPetsOfPlayer(p);
		String havePets = bean.pets;
		GradePet[] petList = PetGrade.petList;
		if (petList == null) {
			petList = new GradePet[0];
			log.error("pet list is nullNEW");
		}
		if(petList!=null){
			for(int i=0;i<petList.length;i++){
				if(petList[i]==null){
					log.warn("[宠物检查啊NEW] [name:"+petList[i].getName()+"] [cname:"+petList[i].getProgName()+"] [icon:"+petList[i].getIcon()+"]");
				}else{
					if(petList[i].getIcon()==null){
						log.warn("[宠物检查啊NEW] [name:"+petList[i].getName()+"] [cname:"+petList[i].getProgName()+"] [icon:"+petList[i].getIcon()+"]");
					}
				}
			}
		}
		
		NEW_PET_LIST_RES ret = new NEW_PET_LIST_RES(message.getSequnceNum(), havePets, petList, PetGrade.scaleArr);
		return ret;
	}
	
	@Override
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {
		return false;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		log.info("set file path to {}", file);
		this.file = file;
	}

	public void fixJinJieXingXiang(Pet pet) {
		if (pet.getRarity() < PetManager.千载难逢) {
			return;
		}
		PetProps prop = pet.petProps;
		if (prop == null) {
			log.info("pet props is null, pet id {}", pet.getId());
			return;
		}
		GradePet conf = findGradePetConf(prop.getName());
		if (conf == null) {
			log.error("Pet2Manager.fixJinJieXingXiang: conf not find {}", prop.getName());
			return;
		}
		int grade = pet.grade;
		log.debug("Pet2Manager.fixJinJieXingXiang: grade {}", grade);
		if (grade <= 1) {// 默认一阶，没有进阶，不处理。
			if (prop != null) {
				pet.setAvataRace(prop.getAvataRace());
				pet.setAvataSex(prop.getAvataSex());
				pet.setObjectScale(prop.getObjectScale());
				Avata a = ResourceManager.getInstance().getAvata(pet);
				pet.setAvata(a.avata);
				pet.setAvataType(a.avataType);
				log.debug("Pet2Manager.fixJinJieXingXiang: 修正老宠物形象。{}", pet.getName());
			}
			return;
		}
		int scale = pet.getObjectScale();
		if (PetGrade.levels == null || grade >= PetGrade.levels.length) {
			return;
		}
		log.debug("Pet2Manager.fixJinJieXingXiang: 物品表里的缩放 {} {}", scale, pet.getName());
		// 在原有基础上缩放，因为宠物召唤道具上配置的缩放值不全是1000.
		scale = conf.scaleArr[grade - 2];
		String xingXiang = "";
		String icon = "";
		if (grade <= 4) {// 形象不变
			pet.setObjectScale((short) scale);
			log.debug("Pet2Manager.fixJinJieXingXiang: 2、3、4阶只变大小 scale {} {}", scale, pet.getName());
			return;
		} else if (grade < 8) {
			xingXiang = conf.lv4Avatar;
			icon = conf.lv4Icon;
		} else {
			xingXiang = conf.lv7Avatar;
			icon = conf.lv7Icon;
		}
		if (xingXiang.isEmpty()) {
			log.error("进阶形象配置有误{}", conf.progName);
			return;
		}
		String parts[] = xingXiang.split("_");
		if (parts.length != 2) {
			log.error("split 进阶形象配置有误{} {}", conf.progName, xingXiang);
			return;
		}
		log.info("fixJinJieXingXiang set to {} {}", parts[0], parts[1]);
		log.info("fixJinJieXingXiang scale {}", scale);
		pet.setAvataRace(parts[0]);
		pet.setAvataSex(parts[1]);
		String oldIcon = pet.getIcon();
		if (!icon.isEmpty()) {
			pet.setIcon(icon);
		}
		if (log.isDebugEnabled()) {
			log.debug("[调整宠物头像] [原始icon:" + oldIcon + "] [newIcon:" + icon + "]");
		}
		pet.setObjectScale((short) scale);
		//
		Avata a = ResourceManager.getInstance().getAvata(pet);
		pet.setAvata(a.avata);
		pet.setAvataType(a.avataType);
		// 设置粒子
		int partLv = pet.grade;
		if (partLv < 8 && partLv > 5) {
			partLv = 5;
		}
		String partName = conf.partBody[partLv];
		if (partName != null && !partName.isEmpty() && !partName.equals("0.0")) {
			pet.setParticleName(partName);
			pet.setParticleY((short) conf.partBodyY[partLv]);
			log.info("fixJinJieXingXiang: body {} {}", partName, pet.getParticleY());
		}
		partName = conf.partFoot[partLv];
		if (partName != null && !partName.isEmpty() && !partName.equals("0.0")) {
			pet.setFootParticleName(partName);
			pet.setFootParticleY((short) conf.partFootY[partLv]);
			log.info("fixJinJieXingXiang: foot {} {}", partName, pet.getParticleY());
		}
	}

	public BuffTemplateManager getBuffTemplateMgr() {
		return buffTemplateMgr;
	}

	public void setBuffTemplateMgr(BuffTemplateManager buffTemplateMgr) {
		this.buffTemplateMgr = buffTemplateMgr;
	}
	
	public boolean isTianShengDan(ArticleEntity ae) {
		String t1name = getConfStr("talentSk1item");
		String t2name = getConfStr("talentSk2item");
		String t1nameB = getConfStr("talentSk1itemB");
		String t2nameB = getConfStr("talentSk2itemB");
		String itemName = ae.getArticleName();
		if (itemName.equals(t1nameB) || itemName.equals(t2nameB) || itemName.equals(t1name) || itemName.equals(t2name)) {
			return true;
		}
		return false;
	}
	
	public boolean check4TianSheng(Player player, Pet pet, ArticleEntity ae) {
		String t1name = getConfStr("talentSk1item");
		String t2name = getConfStr("talentSk2item");
		String t1nameB = getConfStr("talentSk1itemB");
		String t2nameB = getConfStr("talentSk2itemB");
		String itemName = ae.getArticleName();
		if (itemName.equals(t1nameB) || itemName.equals(t2nameB) || itemName.equals(t1name) || itemName.equals(t2name)) {

		} else {
			return false;
		}
		if ((itemName.equals(t1nameB) || itemName.equals(t2nameB)) && pet.getRarity() != PetManager.万年不遇) {
			player.sendError(getConfStr("notWanNianBuYu"));
			return false;
		}
		if (pet.getRarity() == PetManager.万年不遇) {
			if ((itemName.equals(t1name) || itemName.equals(t2name))) {
				player.sendError(getConfStr("wanNianBuYuDis"));
				return false;
			}
			t1name = t1nameB;
			t2name = t2nameB;
		}
		if (t1name.equals(itemName)) {
			if (pet.talent1Skill > 0) {
				String msg = "alreadyHasTskA";
				msg = getConfStr(msg);
				player.sendError(msg);
				return false;
			} else {
				return true;
			}
		} else if (t2name.equals(itemName)) {
			if (pet.talent2Skill > 0) {
				String msg = "alreadyHasTskB";
				msg = getConfStr(msg);
				player.sendError(msg);
				return false;
			} else if (pet.getGeneration() < 1) {
				String msg = "canNotUse4gen1";
				msg = getConfStr(msg);
				player.sendError(msg);
				return false;
			} else {
				return true;
			}
		}
		return true;
	}

	/**
	 * 使用道具刷新宠物天生技能。
	 * @param player
	 * @param pet
	 * @param ae
	 * @return 0 不是洗天生技能的药物； 1 药物和宠物不匹配 2 吃掉了。
	 */
	public int changeTianSheng(Player player, Pet pet, ArticleEntity ae) {
		String t1name = getConfStr("talentSk1item");
		String t2name = getConfStr("talentSk2item");
		String t1nameB = getConfStr("talentSk1itemB");
		String t2nameB = getConfStr("talentSk2itemB");
		String itemName = ae.getArticleName();
		if (itemName.equals(t1nameB) || itemName.equals(t2nameB) || itemName.equals(t1name) || itemName.equals(t2name)) {

		} else {
			return 0;
		}
		log.debug("Pet2Manager.changeTianSheng: {} {}", itemName, PetManager.得到宠物稀有度名(pet.getRarity()));
		//
		if ((itemName.equals(t1nameB) || itemName.equals(t2nameB)) && pet.getRarity() != PetManager.万年不遇) {
			player.sendError(getConfStr("notWanNianBuYu"));
			return 1;
		}
		if (pet.getRarity() == PetManager.万年不遇) {
			if ((itemName.equals(t1name) || itemName.equals(t2name))) {
				player.sendError(getConfStr("wanNianBuYuDis"));
				return 1;
			}
			t1name = t1nameB;
			t2name = t2nameB;
		}
		int r = Pet2SkillCalc.getInst().rnd.nextInt(1000000);
		int base = pet.getWuXing() * pet.getLevel();
		boolean hit = r < base;
		log.warn("道具刷天生技能: pet id {}, r {}, base {}, wuXing {}, pet lv {}", new Object[] { pet.getId(), r, base, pet.getWuXing(), pet.getLevel() });
		if (t1name.equals(itemName)) {
			if (pet.talent1Skill > 0) {
				String msg = "alreadyHasTskA";
				msg = getConfStr(msg);
				player.sendError(msg);
				return 1;
			} else {
				if (hit) {
					Pet2SkillCalc.getInst().hitSk1(pet, log);
					pet.init();
					player.sendError(getConfStr("useDanYaoOk"));
				} else {
//					log.warn("Pet2Manager.changeTianSheng: missA {} base {}", r, base);
					log.warn("Pet2Manager.changeTianSheng: missA "+ r +" base "+  base +" petId" + pet.getId());
					player.sendError(getConfStr("useDanYaoFail"));
				}
				return 2;
			}
		} else if (t2name.equals(itemName)) {
			r = Pet2SkillCalc.getInst().rnd.nextInt(100000 / 15);
			hit = r < pet.getWuXing();
			if (pet.talent2Skill > 0) {
				String msg = "alreadyHasTskB";
				msg = getConfStr(msg);
				player.sendError(msg);
				return 1;
			} else if (pet.getGeneration() < 1) {
				String msg = "canNotUse4gen1";
				msg = getConfStr(msg);
				player.sendError(msg);
				return 1;
			} else {
				if (hit) {
					Pet2SkillCalc.getInst().hitSk2(pet, log);
					pet.init();
					player.sendError(getConfStr("useDanYaoOk"));
				} else {
//					log.warn("Pet2Manager.changeTianSheng: missB {} base {}", r, base);
					log.warn("Pet2Manager.changeTianSheng: missB "+ r +" base "+  base +" petId" + pet.getId());
					player.sendError(getConfStr("useDanYaoFail"));
				}
				return 2;
			}
		}
		return 1;// 阻止物品消失。
	}

	public void destory() {
		if (petsOfPlayerBeanEm != null) {
			petsOfPlayerBeanEm.destroy();
		}
		log.info("Pet2Manager.destory.");

		if (SkEnhanceManager.getInst() != null) {
			SkEnhanceManager.getInst().destroy();
		}
	}
}
// FIXME PetALLBillboard
// cd /home/game/resin_test/webapps/game_server/admin/pet
// cd /home/game/resin_test/webapps/game_server/WEB-INF/game_init_config/pet2
// plzSpecifySkAforLock
// plzSpecifySkBforLock
// canNotUseBWithoutUsingA
// petShowInRank
// worldCastShowInRank xxx的xxx进入前100。