package com.fy.engineserver.datasource.skill.master;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UnknownFormatConversionException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.vipExpActivity.VipExpActivityManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.equipments.Weapon;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.buff.BuffTemplate_ChouXie;
import com.fy.engineserver.datasource.buff.BuffTemplate_HuDunPercent;
import com.fy.engineserver.datasource.buff.BuffTemplate_JiaXue;
import com.fy.engineserver.datasource.buff.BuffTemplate_JianMingZhong;
import com.fy.engineserver.datasource.buff.BuffTemplate_XuRuo;
import com.fy.engineserver.datasource.buff.BuffTemplate_XuanYun;
import com.fy.engineserver.datasource.buff.BuffTemplate_ZengShu;
import com.fy.engineserver.datasource.buff.BuffTemplate_ZhongDu;
import com.fy.engineserver.datasource.buff.Buff_JianMingZhong;
import com.fy.engineserver.datasource.buff.Buff_XuLuo;
import com.fy.engineserver.datasource.buff.Buff_ZhongDu;
import com.fy.engineserver.datasource.career.Career;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.career.CareerThread;
import com.fy.engineserver.datasource.career.SkillInfo;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillEntity;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.datasource.skill.SkillInfoHelper;
import com.fy.engineserver.datasource.skill.activeskills.CommonAttackSkill;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithTraceAndDirectionOrTarget;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutEffect;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutEffectAndQuickMove;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndOnTeamMember;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithMatrix;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithRange;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithSummonNPC;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithTargetOrPosition;
import com.fy.engineserver.datasource.skill.passivetrigger.PassiveTriggerImmune;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.event.Event;
import com.fy.engineserver.event.EventProc;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventPlayerLogin;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.gateway.GameNetworkFramework;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.green.GreenServerManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NEW_QUERY_CAREER_INFO_BY_ID_RES;
import com.fy.engineserver.message.SEND_ACTIVESKILL_REQ;
import com.fy.engineserver.message.SKILL_CD_MODIFY_REQ;
import com.fy.engineserver.message.SKILL_POINT_SHOW_RES;
import com.fy.engineserver.message.SkEnh_Add_point_REQ;
import com.fy.engineserver.message.SkEnh_Add_point_RES;
import com.fy.engineserver.message.SkEnh_Detail_REQ;
import com.fy.engineserver.message.SkEnh_Detail_RES;
import com.fy.engineserver.message.SkEnh_Exp2point_REQ;
import com.fy.engineserver.message.SkEnh_Exp2point_RES;
import com.fy.engineserver.message.SkEnh_INFO_REQ;
import com.fy.engineserver.message.SkEnh_INFO_RES;
import com.fy.engineserver.message.SkEnh_exINFO_REQ;
import com.fy.engineserver.message.SkEnh_exINFO_RES;
import com.fy.engineserver.message.SkEnh_jj_REQ;
import com.fy.engineserver.message.SkEnh_jj_RES;
import com.fy.engineserver.newBillboard.BillboardStatDate;
import com.fy.engineserver.newBillboard.BillboardStatDateManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.pet2.Pet2Manager;
import com.fy.engineserver.sprite.pet2.PetGrade;
import com.fy.engineserver.util.TimeTool;
import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

/**
 * 大师技能管理器。
 * 
 *         create on 2013年9月7日
 */
public class SkEnhanceManager extends SubSystemAdapter implements EventProc {
	public static Logger log = LoggerFactory.getLogger(Skill.class.getName());
	public static ExchangeConf exchangeConf[];
	public static SimpleEntityManager<SkBean> em;
	public static SkEnhanceManager inst;
	public LruMapCache cache;
	public static boolean syncSkInfo = true;
	public static Map<String, String> translation = new LinkedHashMap<String, String>();
	public static Skill jjSkArr[];
	public static int debugRateAdd = 0;
	public static boolean open = true;

//	public static Map<Integer, Integer> 技能抵抗次数 = new HashMap<Integer, Integer>();
	public static Map<String, Integer> 技能抵抗次数 = new HashMap<String, Integer>();
	protected static Platform[] 反大师技能点的平台 = { Platform.官方,Platform.台湾 };

	// map<pid,map<skillid,num>>
	public static Map<Long, LinkedHashMap<Integer, Integer>> recordnum = new LinkedHashMap<Long, LinkedHashMap<Integer, Integer>>();
	public static Map<Long, Integer> allskillnums = new LinkedHashMap<Long, Integer>();

	public static SkEnhanceManager getInst() {
		if(inst == null){
			inst = new SkEnhanceManager();
		}
		return inst;
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public String[] getInterestedMessageTypes() {
		return new String[] { "SkEnh_Exp2point_REQ", "SkEnh_Add_point_REQ", "SkEnh_INFO_REQ", "SkEnh_Detail_REQ", "SkEnh_exINFO_REQ", "SkEnh_jj_REQ" };
	}

	public SkEnhanceManager() {
		log.info("SkEnhanceManager.SkEnhanceManager: created.");
		cache = new LruMapCache(1024 * 1024, 7200000L, false, "skMaster-Cache");
		inst = this;
		doReg();
	}

	public void init() {
		try {
			String rf = Pet2Manager.getInst().getFile();
			loadConf(rf);
		} catch (IOException e) {
			log.error("SkEnhanceManager.init: exception load conf", e);
			return;
		}
		log.info("SkEnhanceManager.init: init em...");
		em = SimpleEntityManagerFactory.getSimpleEntityManager(SkBean.class);
		log.info("SkEnhanceManager.init: ok");
	}
	
	public static String getSkSkillDes(byte level) {
		if (level <= 10) {
			return "人阶"+level+"重";
		} else if (level <= 20) {
			return "地阶" + (level-10) + "重";
		} else {
			return "天阶" + (level-20) + "重";
		}
	}

	public void loadConf(String rf) throws IOException {
		log.info("SkEnhanceManager.loadConf: rf {}", rf);
		String dir = new File(rf).getParentFile().getParent();
		log.info("SkEnhanceManager.loadConf: dir {}", dir);

		File file = new File(dir, "transitRobbery/skEnhance.xls");
		log.info("SkEnhanceManager.loadConf: target file {}", file.getAbsolutePath());
		//
		if (CareerManager.getInstance() == null) {
			CareerManager ccm = new CareerManager();
			ccm.setConfigFile(new File(dir, "skills.xls"));
			try {
				ccm.init();
			} catch (Exception e) {
				log.error("SkEnhanceManager.loadConf: load career manager fail.", e);
			}
		}
		//
		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		workbook.setMissingCellPolicy(HSSFRow.CREATE_NULL_AS_BLANK);

		HSSFSheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		exchangeConf = new ExchangeConf[rows - 1];
		for (int r = 1; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			if (row == null) {
				break;
			}
			ExchangeConf c = new ExchangeConf();
			c.startTimes = PetGrade.getInt(row, 0, log);
			c.endTimes = PetGrade.getInt(row, 1, log);
			c.needExp = PetGrade.getInt(row, 2, log) * 10000;
			c.needMoney = PetGrade.getInt(row, 3, log);
			exchangeConf[r - 1] = c;
		}
		loadSkEnConf(workbook.getSheetAt(1));
		loadTranslation(workbook.getSheetAt(2), log);
		is.close();
		log.info("SkEnhanceManager.loadConf: ok.");
	}

	public void loadSkEnConf(HSSFSheet sheet) {
		int rows = sheet.getPhysicalNumberOfRows();
		SkEnConf[][] arr = new SkEnConf[5][];
		Career[] ccrs = CareerManager.getInstance().getCareers();
		for (int i = 1; i <= 5; i++) {
			int len = ccrs[i].threads[0].skills.length;
			arr[i - 1] = new SkEnConf[len];
			log.debug("SkEnhanceManager.loadSkEnConf: len {}", len);
		}
		int ccIndex = -1;
		int skIdx = 0;
		String preZhiYe = "";
		// int[] jjIds = new int[]{0,55006,55007,50019,50020};
		CareerManager ccInst = CareerManager.getInstance();
		jjSkArr = new Skill[] { ccInst.getSkillById(55006), ccInst.getSkillById(55007), ccInst.getSkillById(50019), ccInst.getSkillById(50020), };// .getSkillById(jjIds[r]);
		for (int r = 1; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			if (row == null) {
				break;
			}
			SkEnConf c = new SkEnConf();
			c.zhiYe = PetGrade.getString(row, 0, log);
			if (c.zhiYe.isEmpty()) {
				break;
			}
			c.baseId = PetGrade.getInt(row, 1, log);
			c.baseName = PetGrade.getString(row, 2, log);
			c.desc = new String[] { PetGrade.getString(row, 3, log), PetGrade.getString(row, 5, log), PetGrade.getString(row, 7, log), };
			c.openTip = PetGrade.getString(row, 9, log);
			c.levelAddPoint = parseArr(PetGrade.getString(row, 10, log));
			c.levelDesc = PetGrade.getString(row, 11, log);
			// check desc
			try {
				String.format(c.levelDesc, 0);
			} catch (Exception e) {
				log.debug("SkEnhanceManager.loadSkEnConf: error format {} {}", c.levelDesc, e.toString());
			}
			c.levelNeedPoint = parseArr(PetGrade.getString(row, 12, log));
			c.levelNeedMoney = parseArr(PetGrade.getString(row, 13, log));
			c.specilAddPoint = parseDoubleArr(PetGrade.getString(row, 14, log));
			if (c.levelAddPoint.length < 30) {
				log.error("SkEnhanceManager.loadSkEnConf: 长度不够  levelAddPoint{} {}", c.baseName, c.levelAddPoint.length);
			}
			if (c.levelNeedMoney.length < 30) {
				log.error("SkEnhanceManager.loadSkEnConf: 长度不够 levelNeedMoney {} {}", c.baseName, c.levelNeedMoney.length);
			}
			if (c.levelNeedPoint.length < 30) {
				log.error("SkEnhanceManager.loadSkEnConf: 长度不够 levelNeedPoint {} {}", c.baseName, c.levelNeedPoint.length);
			}
			if (preZhiYe.equals(c.zhiYe) == false) {
				ccIndex++;
				skIdx = 0;
				preZhiYe = c.zhiYe;
				log.debug("SkEnhanceManager.loadSkEnConf: change at row {}, {}", row.getRowNum(), preZhiYe);
			} else {
			}
			arr[ccIndex][skIdx] = c;
			skIdx++;
		}
		SkEnConf.conf = arr;
	}

	public double[] parseDoubleArr(String string) {
		if (string == null || string.isEmpty()) {
			return new double[0];
		}
		
		String parts[] = null;
		if (string.contains(",")) {
			parts = string.split(",");
		} else if (string.contains("；")) {
			parts = string.split("；");
		} else if (string.contains(";")) {
			parts = string.split(";");
		} else if (string.contains("，")) {
			parts = string.split("，");
		} else {
			throw new IllegalArgumentException("无法分割 " + string);
		}
		double[] ret = new double[parts.length];
		for (int i = 0; i < parts.length; i++) {
			ret[i] = Double.parseDouble(parts[i]);
		}
		return ret;
	}
	public int[] parseArr(String string) {

		String parts[] = null;
		if (string.contains(",")) {
			parts = string.split(",");
		} else if (string.contains("；")) {
			parts = string.split("；");
		} else if (string.contains(";")) {
			parts = string.split(";");
		} else if (string.contains("，")) {
			parts = string.split("，");
		} else {
			throw new IllegalArgumentException("无法分割 " + string);
		}
		int[] ret = new int[parts.length];
		for (int i = 0; i < parts.length; i++) {
			ret[i] = Integer.parseInt(parts[i]);
		}
		return ret;
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		Player p = check(conn, message, log);
		log.debug("SkEnhanceManager.handleReqeustMessage: {} ask {}", p.getName(), message.getTypeDescription());
		if (message instanceof SkEnh_Exp2point_REQ) {
			return exchange(p, (SkEnh_Exp2point_REQ) message);
		} else if (message instanceof SkEnh_INFO_REQ) {
			return sendInfo(p, (SkEnh_INFO_REQ) message);
		} else if (message instanceof SkEnh_Add_point_REQ) {
			return addPoint(p, (SkEnh_Add_point_REQ) message);
		} else if (message instanceof SkEnh_exINFO_REQ) {
			return sendExInfo(p, (SkEnh_exINFO_REQ) message);
		} else if (message instanceof SkEnh_jj_REQ) {
			return sendJJInfo(p, (SkEnh_jj_REQ) message);
		} else if (message instanceof SkEnh_Detail_REQ) {
			return sendSkDetail(p, (SkEnh_Detail_REQ) message);
		} else {
			log.debug("SkEnhanceManager.handleReqeustMessage: unknow message {}", message.getTypeDescription());
		}
		return null;
	}

	public ResponseMessage sendJJInfo(Player p, SkEnh_jj_REQ message) {
		if (jjSkArr == null) {
			log.error("SkEnhanceManager.sendJJInfo: null jj skill arr");
			return null;
		}
		int career = p.getCurrSoul().getCareer();
		log.debug("SkEnhanceManager.sendJJInfo: soul c {}, player c {}", career, p.getCareer());
		career -= 1;
		if (career < 0 || career >= jjSkArr.length) {
			log.error("SkEnhanceManager.sendJJInfo: career {} invalid", career);
			return null;
		}
		Skill sk = jjSkArr[career];
		if (sk == null) {
			log.error("SkEnhanceManager.sendJJInfo: skill is null at {}", career);
			return null;
		}
		String desc = Translate.禁忌技能描述;
		StringBuffer sb = new StringBuffer();
		SkillInfoHelper.genActiveSkDesc(p, sk, false, sb, 1);

		StringBuffer sb2 = new StringBuffer();
		SkillInfoHelper.genActiveSkDesc(p, sk, false, sb2, 2);

		SkBean bean = findSkBean(p);
		if (bean == null) return null;
		int sum = bean.sumAddPoint;
		int level = sum >= 300 ? 2 : (sum >= 120 ? 1 : 0);
		String[] stepDesc = new String[] { Translate.大师技能总重数达到120重激活1阶 + " - " + (level >= 1 ? Translate.已激活 : Translate.未激活), Translate.大师技能总重数达到300重激活2阶 + " - " + (level >= 2 ? Translate.已激活 : Translate.未激活) };
		String desc1 = sb.toString();
		String desc2 = sb2.toString();
		SkEnh_jj_RES res = new SkEnh_jj_RES(message.getSequnceNum(), level, sk.getName(), desc, stepDesc, desc1, desc2);
		log.debug("SkEnhanceManager.sendJJInfo: send ok, {}", sk.getName());
		return res;
	}

	public SkEnh_exINFO_RES sendExInfo(Player p, SkEnh_exINFO_REQ message) {
		SkBean bean = findSkBean(p);
		if (bean == null) {
			return null;
		}
		int point = 0;
		int exTimes = 0;
		ExchangeConf[] exConf = null;// exchangeConf;
		point = bean.point;
		exTimes = bean.exchangeTimes;
		ExchangeConf conf = findCurExStep(p, bean.exchangeTimes);
		long leftTimes = 0;
		if (conf == null) {
			// String msg = getConfStr("reachMaxTimes");
			// p.sendError(msg);
			log.warn("SkEnhanceManager.sendExInfo: conf is null, may reach max. {}", exTimes);
			// return null;
		} else {
			leftTimes = conf.endTimes - exTimes;
		}
		exConf = new ExchangeConf[exchangeConf.length];
		String format = getConfStr("exEntryFormat");
		for (int i = 0; i < exConf.length; i++) {
			ExchangeConf template = exchangeConf[i];
			ExchangeConf real = template.clone();
			if (real == null) {
				log.error("SkEnhanceManager.sendInfo: clone fail");
				return null;
			}
			// 第%d-%d次：转换比例：%dW：1，剩余兑换次数%d
			long showLeft = leftTimes;
			if (conf == null || i < conf.index) {
				showLeft = 0;
			} else if (i == conf.index) {
				showLeft = leftTimes;
			} else {
				showLeft = template.endTimes - template.startTimes + 1;
			}
			real.desc = String.format(format, template.startTimes, template.endTimes, template.needExp / 10000, showLeft);
			exConf[i] = real;
		}
		String exchangeTip = getConfStr("exchangeTip");
		/*
		 * "每天可以免费转换100次，付费转换300次
		 * 当前兑换%d次，剩余%d次
		 * 当前付费兑换%d次，剩余%d次--没这个了。
		 * 当前兑换比例%dW:1"
		 */
		int freeUse, freeLeft, payUse, payLeft, moneyPerPoint;
		if (exTimes <= 200) {
			freeUse = exTimes;
			freeLeft = 200 - freeUse;
			payUse = 0;
			payLeft = 1000;
		} else {
			freeUse = 200;
			freeLeft = 0;
			payUse = exTimes - 200;
			payLeft = 1200 - exTimes;
		}
		moneyPerPoint = conf != null ? conf.needMoney : 9999;
		long rate = conf != null ? conf.needExp / 10000 : 99999999;
		exchangeTip = String.format(exchangeTip, freeUse, freeLeft,
		// payUse, payLeft,
		rate);
		if (moneyPerPoint > 0) {
			String mes = Translate.translateString(Translate.每次消耗两, new String[][] { { Translate.STRING_1, moneyPerPoint + "" } });
			exchangeTip += "（" + mes + "）";
			leftTimes = Math.min(leftTimes, 100);// 有阶梯型控制，所以不能一次兑换100点以上。
		}
		SkEnh_exINFO_RES res = new SkEnh_exINFO_RES(message.getSequnceNum(), point, exTimes, exConf, leftTimes, exchangeTip, moneyPerPoint);
		return res;
	}

	public ResponseMessage sendSkDetail(Player p, SkEnh_Detail_REQ message) {
		SkBean bean = findSkBean(p);
		if (bean == null) {
			return null;
		}
		long seqNum = message.getSequnceNum();
		int index = message.getIndex();
		if (index < 0) {
			log.error("SkEnhanceManager.sendSkDetail: invalid index {}", index);
			return null;
		}
		if (index >= 12) {
			p.sendError(getConfStr("jinJiJiNengZaiZheLi"));
			return null;
		}
		int c = p.getCareer() - 1;
		if (SkEnConf.conf == null) {
			log.error("SkEnhanceManager.sendSkDetail: conf is null.");
			return null;
		}
		SkEnConf[] conf = SkEnConf.conf[c];
		SkEnConf skC = conf[index];
		int level = bean.useLevels[index];
		Career career = CareerManager.getInstance().getCareer(p.getCareer());
		if (career == null) {
			log.error("SkEnhanceManager.sendSkDetail: career null");
			return null;
		}
		CareerThread t = career.getCareerThread(0);
		if (t == null) {
			log.error("SkEnhanceManager.sendSkDetail: CareerThread null");
			return null;
		}
		Skill skT = t.getSkillByIndex(index);
		if (skT == null) {
			log.error("SkEnhanceManager.sendSkDetail: skT null, index {}", index);
			return null;
		}
		String name = Translate.大使技能 + skT.getName();
		String step = Translate.当前阶重;
		int grade = ((level - 1) / 10) + 1;
		int layer = ((level - 1) % 10) + 1;
		if (level <= 0) {
			grade = 1;
			layer = 0;
		} else {
			grade = ((level - 1) / 10) + 1;
			layer = ((level - 1) % 10) + 1;
		}
		char gradeName = Translate.人地天.charAt(grade - 1);
		step = String.format(step, gradeName, layer);
		String desc = Translate.这里是大师技能描述;
		String[] stepDesc = new String[skC.desc.length];
		int needLv[] = { 10, 20, 30, 9999 };
		for (int i = 0; i < stepDesc.length; i++) {
			stepDesc[i] = skC.desc[i];
			if (level < needLv[i]) {
				stepDesc[i] = "<f color='0xff0000'>" + stepDesc[i] + " - " + Translate.未激活 + "</f>";
			} else {
				stepDesc[i] = stepDesc[i] + " - " + Translate.已激活;
			}
		}
		String openDesc = skC.openTip;
		String layerDesc = "";
		String nextLayerDesc = "";
		if (skC.levelAddPoint.length < 30) {
			log.debug("SkEnhanceManager.sendSkDetail: 错误的技能加点配置，少于30级。");
			return null;
		} else if (level <= 0) {
			try {
				layerDesc = String.format(skC.levelDesc, skC.levelAddPoint[0]);
				nextLayerDesc = String.format(skC.levelDesc, skC.levelAddPoint[1]);
				if (skC.baseId == 8256 || skC.baseId == 8257 || skC.baseId == 8258) {		//兽印、钢胆、嗜血单独处理
					layerDesc = layerDesc.replaceAll("1", skC.specilAddPoint[0] + "");
					nextLayerDesc = nextLayerDesc.replaceAll("1", skC.specilAddPoint[1] + "");
				}
			} catch (UnknownFormatConversionException e) {
				log.error("SkEnhanceManager.sendSkDetail: 格式错误 {}", e.toString());
				layerDesc = skC.levelDesc;
				nextLayerDesc = skC.levelDesc;
			}
		} else {
			if (level <= 29) {
				try {
					layerDesc = String.format(skC.levelDesc, skC.levelAddPoint[level - 1]);
					nextLayerDesc = String.format(skC.levelDesc, skC.levelAddPoint[level]);
					if (skC.baseId == 8256 || skC.baseId == 8257 || skC.baseId == 8258) {		//兽印、钢胆、嗜血单独处理
						layerDesc = layerDesc.replaceAll("1", skC.specilAddPoint[level - 1] + "");
						nextLayerDesc = nextLayerDesc.replaceAll("1", skC.specilAddPoint[level] + "");
					}
				} catch (UnknownFormatConversionException e) {
					log.error("SkEnhanceManager.sendSkDetail: 格式错误 {}", e.toString());
					layerDesc = skC.levelDesc;
					nextLayerDesc = skC.levelDesc;
				}
			}
		}// 消耗修炼值%d，银子%d两
		String needPay = Translate.已达到满级;
		if (level < 30) {
			needPay = String.format(getConfStr("addPointPay"), skC.levelNeedPoint[level < 0 ? 0 : level], skC.levelNeedMoney[level < 0 ? 0 : level]);
		}
		SkEnh_Detail_RES res = new SkEnh_Detail_RES(seqNum, index, level, name, step, desc, stepDesc, openDesc, layerDesc, needPay, nextLayerDesc);
		return res;
	}

	public SkEnh_INFO_RES sendInfo(Player p, SkEnh_INFO_REQ message) {
		SkBean bean = findSkBean(p);
		int point = 0;
		byte[] levels;
		if (bean == null) {
			levels = new byte[20];
			Arrays.fill(levels, (byte) -1);
		} else {
			point = bean.point;
			levels = bean.useLevels;
		}
		SkEnh_INFO_RES res = new SkEnh_INFO_RES(message.getSequnceNum(), levels, point);
		return res;
	}

	public SkBean findSkBean(Player p) {
		if (p.getLevel() < TransitRobberyManager.openLevel) {
			log.debug("SkEnhanceManager.sendInfo: 等级可能不够 {} < {}", p.getLevel(), TransitRobberyManager.openLevel);
			return null;
		}
		SkBean bean = (SkBean) cache.get(p.getId());
		if (bean != null) {
			checkResetExTimes(p, bean);
			fixSoulAndSumPoint(p, bean);
			return bean;
		}
		try {
			bean = em.find(p.getId());
		} catch (Exception e) {
			log.error("SkEnhanceManager.findSkBean: load bean error", e);
			return null;
		}
		if (bean == null) {
			bean = new SkBean();
			bean.pid = p.getId();
			bean.setLevels(new byte[20]);
			bean.soulLevels = new byte[20];
			Arrays.fill(bean.getLevels(), (byte) -1);
			Arrays.fill(bean.soulLevels, (byte) -1);
			try {
				em.save(bean);
			} catch (Exception e) {
				log.debug("SkEnhanceManager.findSkBean: save new bean fail,", e);
			}
		} else {
			checkResetExTimes(p, bean);
		}
		if (bean.soulLevels == null) {
			bean.setSoulLevels(new byte[20]);
		}
		fixSoulAndSumPoint(p, bean);
		cache.put(p.getId(), bean);
		return bean;
	}

	public void fixSoulAndSumPoint(Player p, SkBean bean) {
		if (p.getCurrSoul().getSoulType() == Soul.SOUL_TYPE_BASE) {
			bean.useLevels = bean.getLevels();
			bean.isSoul = false;
		} else {
			bean.useLevels = bean.soulLevels;
			bean.isSoul = true;
		}
		int sum = 0;
		if (bean != null) {
			for (int i = 0; i < 12; i++) {
				int v = bean.useLevels[i];
				sum += v > 0 ? v : 0;
			}
		}
		bean.sumAddPoint = sum;
	}

	public void checkResetExTimes(Player p, SkBean bean) {
		int curDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
		if (curDay != bean.getLastExchangeDay() && bean.getExchangeTimes() != 0) {
			bean.setExchangeTimes(0);
			log.warn("SkEnhanceManager.findSkBean: 重置兑换次数 {}", p.getName());
		}
	}

	public SkEnh_Add_point_RES addPoint(Player p, SkEnh_Add_point_REQ message) {
		if (open == false) {
			p.sendError(Translate.暂未开放敬请期待);
			return null;
		}
		SkBean bean = findSkBean(p);
		if (bean == null) {
			log.warn("SkEnhanceManager.addPoint: no record when want adding Point, {}", p.getName());
			String msg = getConfStr("youHaveNoPoint");
			p.sendError(msg);
			return null;
		}
		int c = p.getCareer() - 1;
		if (SkEnConf.conf == null) {
			log.error("SkEnhanceManager.sendSkDetail: conf is null.");
			return null;
		}
		SkEnConf[] conf = SkEnConf.conf[c];
		int index = message.getIndex();
		if (index < 0 || index >= 20) {
			log.warn("SkEnhanceManager.addPoint: invalid index {} of {}", index, p.getName());
			return null;
		}
		byte[] lvArr = bean.useLevels;
		if (lvArr == null) {
			lvArr = new byte[20];
		}
		if (lvArr[index] >= 30) {
			String msg = getConfStr("exceed30max");
			p.sendError(msg);
			return null;
		}
		if (lvArr[index] < 0) {
			String msg = getConfStr("notOpen");
			p.sendError(msg);
			return null;
		}
		SkEnConf skC = conf[index];
		int curLv = lvArr[index];
		if (curLv >= skC.levelNeedPoint.length || curLv >= skC.levelNeedMoney.length) {
			log.error("SkEnhanceManager.addPoint: 长度不够。c {}", c);
			p.sendError("confError:length");
			return null;
		}
		int needPoint = skC.levelNeedPoint[curLv];
		if (needPoint <= 0) {
			log.error("SkEnhanceManager.addPoint: 错误的兑换配置。c {}", c);
			return null;
		}
		if (bean.point < needPoint) {
			log.warn("SkEnhanceManager.addPoint: lack point, need {} {}", needPoint, p.getName());
			String msg = getConfStr("lackPoint");
			p.sendError(msg);
			return null;
		}
		int needMoney = skC.levelNeedMoney[curLv] * 1000;// 按两计算
		if (needMoney < 0) {
			log.error("SkEnhanceManager.addPoint: 错误的银子消耗 c{}", c);
			return null;
		}
		if (p.getSilver() < needMoney) {
			p.sendError(getConfStr("lackMoney"));
			log.debug("SkEnhanceManager.addPoint: pre check fail");
			return null;
		}
		BillingCenter billingCenter = BillingCenter.getInstance();
		try {
			billingCenter.playerExpense(p, needMoney, CurrencyType.YINZI, ExpenseReasonType.skMaster大师技能加点, "skMasterAddPoint", VipExpActivityManager.default_add_rmb_expense);
		} catch (NoEnoughMoneyException e1) {
			p.sendError(getConfStr("lackMoney"));
			log.debug("SkEnhanceManager.addPoint: exception fail");
		} catch (BillFailedException e1) {
			log.error("SkEnhanceManager.addPoint: fail exception .", e1);
			return null;
		}
		lvArr[index] += 1;
		EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { p.getId(), RecordAction.大师技能最大重数, lvArr[index]});
		EventRouter.getInst().addEvent(evt);
		EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { p.getId(), RecordAction.大师技能总重数, 1L});
		EventRouter.getInst().addEvent(evt2);
		bean.sumAddPoint += 1;
		bean.setPoint(bean.point - needPoint);
		String bodyType = null;
		if (bean.isSoul) {
			bean.setSoulLevels(lvArr);
			bodyType = Translate.元神;
		} else {
			bean.setLevels(lvArr);
			bodyType = Translate.本尊;
		}
		log.error("大师技能加点: 玩家{} 加点在 {}, 点数达到 {}, 类型 {}", new Object[] { p.getName(), index, lvArr[index], bodyType });
		try {
			em.save(bean);
			log.debug("SkEnhanceManager.addPoint: save ok");
		} catch (Exception e) {
			log.debug("SkEnhanceManager.addPoint: save exception", e);
			return null;
		}
		int sum = bean.sumAddPoint;
		if (lvArr[index] == 10 || lvArr[index] == 20 || lvArr[index] == 30) {
			if (index == 7 && p.getCareer() == 3) {// 仙心耀天,重新计算属性。
				p.setPhyDefenceA(p.getPhyDefenceA());
				p.setMagicDefenceA(p.getMagicDefenceA());
			}
			if (index == 0 && p.getCareer() == 3) {// 仙心灵印
				p.setMagicAttackA(p.getMagicAttackA());
			}
			if (index == 10 && (p.getCareer() == 4 || p.getCareer() == 5)) {// 九黎，怨毒。 兽魁 抽灵
				p.setMaxHPA(p.getMaxHPA());
			}
			if(p.getCareer() == 5){//兽魁 兽印
				p.setMagicAttackA(p.getMagicAttackA());
				p.setPhyDefenceA(p.getPhyDefenceA());
				p.setMagicDefenceA(p.getMagicDefenceA());
			}
			if (index == 0 && p.getCareer() == 4) {// 九黎，骨印
				p.setMagicDefenceA(p.getMagicDefenceA());
			}
			if (index == 3 && p.getCareer() == 4) {// 九黎，妖印
				p.setMagicAttackA(p.getMagicAttackA());
			}
			if (index == 0 && p.getCareer() == 2) {// 影魅，熊印
				p.setPhyDefenceA(p.getPhyDefenceA());
			}
			if (index == 10 && p.getCareer() == 1) {// 修罗21006赤焰,永久提升自身外防2%
				p.setPhyDefenceA(p.getPhyDefenceA());
				log.debug("重新计算修罗外防 {}", p.getName());
			}
			if (index == 0 && p.getCareer() == 1) {
				int oldattacta = p.getPhyAttack();
				p.setPhyAttackA(p.getPhyAttackA());
				log.debug("重新计算修罗外功 {}{}", new Object[] { p.getName(), oldattacta + "-->" + p.getPhyAttack() });
			}
			syncSkillInfo(index, p);
		}
		try {
			BillboardStatDate bData = BillboardStatDateManager.getInstance().getBillboardStatDate(p.getId());
			if (bData == null) {
				log.error("取得玩家排行对象是null {}", p.getId());
			} else {
				bData.通知大使技能重数改变(p, sum);
				log.debug("通知大使技能重数改变ok {} {}", p.getName(), sum);
			}
		} catch (Exception e) {
			log.error("通知排行数据改变出错", e);
		}
		byte mv = 0;
		if (sum == 120) {
			mv = 1;
		} else if (sum == 300) {
			mv = 2;
		}
		if (mv != 0) {
			byte[] arr = p.getNuqiSkillsLevels();
			arr[1] = mv;
			p.setNuqiSkillsLevels(arr);
			log.error("大师技能: 开启禁忌技能 {} {}", p.getName(), mv);
			try {
				Skill skill = CareerManager.getInstance().getCareer(p.getCareer()).nuqiSkills[1];
				refreshClientSkInfo(p, skill);
			} catch (Exception e) {
				log.error("SkEnhanceManager.addPoint: refreshClientSkInfo error.", e);
			}
		}
		try {
			Skill skill = CareerManager.getInstance().getCareer(p.getCareer()).threads[0].skills[index];
			refreshClientSkInfo(p, skill);
		} catch (Exception e) {
			log.error("SkEnhanceManager.addPoint: exception send new skill info.", e);
		}
		SkEnh_Add_point_RES res = new SkEnh_Add_point_RES(message.getSequnceNum(), index, lvArr[index], bean.point);
		return res;
	}

	public void refreshClientSkInfo(Player p, Skill skill) {
		SkillInfo si = new SkillInfo();
		si.setInfo(p, skill);
		NEW_QUERY_CAREER_INFO_BY_ID_RES NEW_qciRes = new NEW_QUERY_CAREER_INFO_BY_ID_RES(GameMessageFactory.nextSequnceNum(), si);
		p.addMessageToRightBag(NEW_qciRes);
	}

	public SkEnh_Exp2point_RES exchange(Player p, SkEnh_Exp2point_REQ msg) {
		if (exchangeConf == null) {
			log.error("SkEnhanceManager.exchange: conf is null");
			return null;
		}
		if (open == false) {
			p.sendError(Translate.暂未开放敬请期待);
			return null;
		}
		SkBean bean = findSkBean(p);
		if (bean == null) {
			return null;
		}
		int preTimes = 0;
		if (bean != null) {
			preTimes = bean.getExchangeTimes();
		}
		ExchangeConf conf = findCurExStep(p, preTimes);
		if (conf == null) {
			log.info("SkEnhanceManager.exchange: conf not find for preTimes {}", preTimes);
			return null;
		}
		int leftTimes = conf.endTimes - preTimes;
		long price = conf.needExp;
		int wantTimes = msg.getTimes();
		if (wantTimes > leftTimes) {
			String msgT = "timesExceedCurSeg";
			p.sendError(msgT);
			return null;
		}
		long needExp = price * wantTimes;
		boolean ret = p.subExp(needExp, "大师技能兑换");
		if (ret == false) {
			String msgT = getConfStr("lackExp");
			p.sendError(msgT);
			log.info("SkEnhanceManager.exchange: lack exp, need {} < has {}", needExp, p.getExp());
			return null;
		}
		int yinZi = conf.needMoney;
		if (yinZi > 0 && wantTimes > 100) {
			wantTimes = 100;
			log.info("SkEnhanceManager.exchange: fix pay max want times to 100, {}", p.getName());
		}
		long needMoney = yinZi * 1000 * wantTimes; // 一次10两
		log.error("大师技能兑换: 单价 {} x {} = 银子 {} , 玩家 {}", new Object[] { yinZi, wantTimes, needMoney, p.getName() });
		if (needMoney < 0) {
			log.error("SkEnhanceManager.exchange: 异常的银子数量 {} of {}", needMoney, p.getName());
			return null;
		}
		if (needMoney > 0) {
			if (p.getSilver() < needMoney) {
				p.sendError(getConfStr("lackMoney"));
				log.debug("SkEnhanceManager.addPoint: pre check fail");
				return null;
			}
			BillingCenter billingCenter = BillingCenter.getInstance();
			try {
				int payType = CurrencyType.YINZI;
				if (GreenServerManager.isBindYinZiServer()) {
					payType = CurrencyType.SHOPYINZI;
				}
				billingCenter.playerExpense(p, needMoney, payType, ExpenseReasonType.skMaster大师技能加点, "skMasterExPoint");
			} catch (NoEnoughMoneyException e1) {
				p.sendError(getConfStr("lackMoney"));
				log.debug("SkEnhanceManager.addPoint: exception fail");
			} catch (BillFailedException e1) {
				log.error("SkEnhanceManager.addPoint: fail exception .", e1);
				return null;
			}
		}
		bean.setExchangeTimes(bean.exchangeTimes + wantTimes);
		bean.setLastExchangeDay(Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
		bean.setPoint(bean.point + wantTimes);
		log.error("大师技能: 玩家 {} 消耗经验 {} 兑换技能点 {} 兑换后技能点 {}", new Object[] { p.getName(), needExp, wantTimes, bean.point });
		try {
			em.save(bean);
			log.info("SkEnhanceManager.exchange: save ok, {} point reach {}", p.getName(), bean.point);
		} catch (Exception e) {
			log.error("SkEnhanceManager.exchange: save fail", e);
		}
		SkEnh_Exp2point_RES res = new SkEnh_Exp2point_RES(msg.getSequnceNum(), bean.getExchangeTimes(), bean.getPoint());
		return res;
	}

	public ExchangeConf findCurExStep(Player p, int preTimes) {
		ExchangeConf conf = null;
		int idx = 0;
		for (ExchangeConf c : exchangeConf) {
			if (c == null) {
				break;
			}
			if (preTimes < c.endTimes) {
				conf = c;
				conf.index = idx;
				break;
			}
			idx++;
		}
		return conf;
	}

	@Override
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {
		return false;
	}

	public void dumpExchangeConf(Writer w) throws IOException {
		w.append("经验兑换点数配置：<br/>");
		if (exchangeConf == null) {
			w.append("配置是 null");
			return;
		}
	}

	public void loadTranslation(HSSFSheet sheet, Logger logger) {
		int rows = sheet.getPhysicalNumberOfRows();
		for (int r = 0; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			HSSFCell cell = row.getCell(0);
			if (cell == null) {
				logger.warn("null cell at row {} col 0", row.getRowNum());
				break;
			}
			String key = cell.getStringCellValue();
			HSSFCell cell1 = row.getCell(1);
			if (cell1 == null) {
				logger.warn("null cell at row {} col 1", row.getRowNum());
				break;
			}
			String v = PetGrade.getString(row, 1, logger);
			if (key != null) {
				key = key.trim();
			}
			translation.put(key, v);
		}
	}

	public String getConfStr(String str) {
		String ret = translation.get(str);
		if (ret == null) {
			log.error("miss translation for key [{}]", str);
			return str;
		}
		return ret;
	}

	@Override
	public void proc(Event evt) {
		switch (evt.id) {
		case Event.SERVER_STARTED:
			log.info("SkEnhanceManager.init: register sub system");
			GameNetworkFramework.getInstance().addSubSystem(this);
			log.info("SkEnhanceManager.proc: register ok");
			break;
		case Event.PLAYER_DU_JIE:
			EventWithObjParam wop = (EventWithObjParam) evt;
			Object[] param = (Object[]) wop.param;
			Long playerId = (Long) param[0];
			Integer lv = (Integer) param[1];
			duJie(playerId, lv);
			break;
		case Event.PLAYER_LOGIN:
			EventPlayerLogin epe = (EventPlayerLogin) evt;
			sendSkInfo(epe.player);
			break;
		}
	}

	public void duJie(Long playerId, Integer lv) {
		Player p = null;
		try {
			p = PlayerManager.getInstance().getPlayer(playerId);
		} catch (Exception e) {
			log.debug("SkEnhanceManager.duJie: ", e);
			return;
		}
		if (p == null) {
			log.debug("SkEnhanceManager.duJie: play is null.");
			return;
		}
		log.info("SkEnhanceManager.duJie: {} jinJie {} ", p.getName(), lv);
		SkBean bean = findSkBean(p);
		if (bean == null) {
			log.debug("SkEnhanceManager.duJie: null bean");
			return;
		}
		int idx = 0;
		switch (lv) {
		case 1:
			idx = 0;
			log.info("SkEnhanceManager.duJie: {} 开启 0,1", p.getName());
			break;
		case 3:
			idx = 2;
			log.info("SkEnhanceManager.duJie: {} 开启 2,3", p.getName());
			break;
		case 5:// 45
			idx = 4;
			log.info("SkEnhanceManager.duJie: {} 开启 4,5", p.getName());
			break;
		case 6://
			idx = 6;
			log.info("SkEnhanceManager.duJie: {} 开启 6,7", p.getName());
			break;
		case 7:
			idx = 8;
			log.info("SkEnhanceManager.duJie: {} 开启 8,9", p.getName());
			break;
		case 8:
			idx = 10;
			bean.useLevels[10] = bean.useLevels[11] = 0;
			log.info("SkEnhanceManager.duJie: {} 开启 10,11", p.getName());
			break;
		default:
			log.info("SkEnhanceManager.duJie: 未处理");
			return;
		}
		// 本尊、元神的大师技能同时开启。
		byte[] arr = bean.getLevels();
		byte[] ar2 = bean.getSoulLevels();
		byte[] ar3 = bean.useLevels;
		arr[idx] = ar2[idx] = ar3[idx] = 0;
		idx += 1;
		arr[idx] = ar2[idx] = ar3[idx] = 0;
		bean.setLevels(arr);
		bean.setSoulLevels(ar2);
		try {
			em.save(bean);
			log.info("SkEnhanceManager.duJie: save ok");
		} catch (Exception e) {
			log.error("SkEnhanceManager.duJie: save fail");
		}
	}

	@Override
	public void doReg() {
		EventRouter.register(Event.SERVER_STARTED, this);
		EventRouter.register(Event.PLAYER_DU_JIE, this);
		EventRouter.register(Event.PLAYER_LOGIN, this);
	}

	public void destroy() {
		if (em != null) {
			em.destroy();
		}
		log.info("SkEnhanceManager.destroy.");
	}

	public boolean checkJJsk(Player player, int skillId) {
		switch (skillId) {
		case 912:
		case 913:
		case 914:
		case 915:
		case 916:
			int lv = player.getNuqiSkillsLevels()[1];
			switch (lv) {
			case 0:
				player.sendError(getConfStr("jjSkWillAutoLvUp"));
				break;
			case 1:
				player.sendError(getConfStr("jihuodiyijietishi"));
				break;
			case 2:
				player.sendError(getConfStr("manjiyihoutishi"));
				break;
			}
			return true;

		}
		return false;
	}

	/**
	 * 处理大师技能3个大阶段的加成。
	 * @param casterP
	 *            可能是NULL
	 * @param target
	 *            可能是NULL
	 * @param activeSkill
	 * 
	 * @param damageType
	 *            -1表示不处理
	 */
	public void fixSkEnStep(Player casterP, Fighter target, ActiveSkill sk, int damageType) {
		if (open == false) return;
		Logger log = Skill.logger;
		log.debug("SkEnhanceManager.fixSkEnStep: 技能 {}", sk.getName());
		if (casterP == null) {
			log.debug("SkEnhanceManager.fixSkEnStep: null caster");
			return;
		}
		if (target == null) {
			log.debug("SkEnhanceManager.fixSkEnStep: null target.");
		}
		log.debug("SkEnhanceManager.fixSkEnStep: {}", casterP.getName());
		SkBean bean = findSkBean(casterP);
		if (bean == null) {
			log.debug("SkEnhanceManager.fixSkEnStep: null bean.");
			return;
		}
		int sumAP = bean.sumAddPoint;
		if (sumAP < 10) {
			log.debug("SkEnhanceManager.fixSkEnStep: sum ap < 10");
			return;
		}
		int skId = sk.getId();
		byte c = casterP.getCareer();
		log.debug("SkEnhanceManager.fixSkEnStep: career {}", c);
		// {Translate.通用,Translate.修罗,Translate.影魅,Translate.仙心,Translate.九黎
		switch (c) {
		case 1: {
			if (casterP == target) {
				log.debug("SkEnhanceManager.fixSkEnStep: same target, break.");
				break;
			}
			/**
			 * 2013-11-17策划改动，去掉之前《双舞》的增加所有技能附带1%几率使目标命中减少80%持续5秒
			 */
			if (skId == 702 && damageType == Fighter.DAMAGETYPE_PHYSICAL_CRITICAL) {
				checkDouLuoMinusSpeed(casterP, target);
			}

			if (skId == 401) {
				// checkSlot3DouLuo(casterP, target, bean, log);//不放在这里了。
			} else if (skId == 601) {
				checkSlot2DouLuo(casterP, target, bean, log);
			}
			// 4 镇魂在其他地方，增加buff时间的。

			/**
			 * 2013-11-17策划改动，去掉《杀戮》：人阶10重:获得所有技能附带1%几率减少目标移动速度50%持续5秒
			 */
			// for (int i = 5; i <= 12; i++) {
			// int point = bean.useLevels[i];
			// if (point < 10) {
			// continue;
			// }
			// checkSlotDouLuo(casterP, target, point / 10, log, i);
			// }
			// if (target == null) {
			// break;
			// }
			/**
			 * 2013-11-17策划改动，去掉《狂印》之前的吸蓝，封魔效果
			 */
			// byte lv = bean.useLevels[0];// 狂印 所有技能附带1%几率吸取目标所有蓝，并且5秒内封魔
			// if (lv < 10) {
			// log.debug("SkEnhanceManager.fixSkEnStep: first slot < 10");
			// break;
			// }
			// Random rnd = ActiveSkill.random;
			// int r = rnd.nextInt(100);
			// int rate = lv / 10;
			// if (r < rate + debugRateAdd) {//
			// target.setMp(0);
			// // 1426
			// BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateById(1426);
			// Buff b = bt.createBuff(1);
			// b.setStartTime(SystemTime.currentTimeMillis());
			// b.setInvalidTime(b.getStartTime() + 5000);
			// b.setCauser(casterP);
			// target.placeBuff(b);
			// log.debug("SkEnhanceManager.fixSkEnStep: 狂印  大段加成 {}", rate);
			// } else {
			// log.debug("SkEnhanceManager.fixSkEnStep: miss r {} base {}", r, rate);
			// }
			break;
		}
		case 2:
			if (skId == 708) {
				checkGuiSha798(casterP, target); // 斩首（减少所有治疗效果30%）持续5秒
			}

			// checkGuiSha606(casterP, target, sk, 2);
			// checkGuiSha407(casterP, target, sk);
			// 709 血毒 人阶10重:获得所有技能有1%触发所有技能减少目标50%命中持续5秒
			// checkGuiSha709(casterP, target, sk);
			break;
		case 3:
			// 仙心 902 坠天 人阶10重:激活所有技能附带3%减速目标减速30%5秒
			// checkLingZun902(casterP, target, sk);
			// 仙心 713 天诛 人阶10重:激活所有技能有1%几率是目标封魔5秒
			// checkGuiSha606(casterP, target, sk, 8);
			// 仙心 805 雪葬 人阶10重:激活所有技能有1%眩晕目标5秒 rate 1,2,3
			// int rate = getSlotStep(casterP, 9);
			// if (rate > 0) {
			// boolean hit01 = rateXuanYun(casterP, target, log, rate);
			// if (hit01) log.debug("SkEnhanceManager.fixSkEnStep: 仙心	805	雪葬	 rate {}", rate);
			// }
			//
			break;
		case 4:
			// checkWuHuang506(casterP, target, sk);// //506散灵 人阶10重:获得所有技能附带2%破甲效果
			// checkWuHuang809(casterP, target, sk);// 809魂击 人阶10重:获得所有技能有1%几率触发减速60%5秒

			break;
		}
	}

	// 709 血毒 人阶10重:获得所有技能有1%触发所有技能减少目标50%命中持续5秒
	public void checkGuiSha709(Player casterP, Fighter target, ActiveSkill sk) {
		if (target == null) {
			return;
		}
		if (casterP == null) {
			return;
		}

		if (casterP.getCareer() != 2) {
			return;
		}
		int step = getSlotStep(casterP, 4);
		if (step < 1) {
			return;
		}
		int r = ActiveSkill.random.nextInt(100);
		if (r >= step + debugRateAdd) {
			return;
		}
		BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateById(1600);
		if (bt == null) {
			log.debug("SkEnhanceManager.checkGuiSha709: BuffTemplate is null");
			return;
		}
		if (bt instanceof BuffTemplate_JianMingZhong == false) {
			log.debug("SkEnhanceManager.checkGuiSha709: it's not BuffTemplate_JianMingZhong");
			return;
		}
		BuffTemplate_JianMingZhong bb = (BuffTemplate_JianMingZhong) bt;
		Buff_JianMingZhong buff = bb.createBuff(0);
		buff.setStartTime(SystemTime.currentTimeMillis());
		buff.setInvalidTime(buff.getStartTime() + 5000);
		buff.setCauser(casterP);
		buff.attackRating = (50) * 10;
		buff.setDescription(Translate.text_3258 + buff.attackRating / 10 + "%");
		target.placeBuff(buff);
		log.debug("SkEnhanceManager.checkGuiSha709: 709	血毒	 {}%触发所有技能减少目标{}%命中持续5秒", step, buff.attackRating);
	}

	public void checkLingZun704(Player casterP, Fighter target) {
		// 获得每使用一次惊雷技能都会减少冲锋技能3秒冷却时间

		if (casterP == null) {
			return;
		}

		if (casterP.getCareer() != 1) {
			return;
		}
		int step = getSlotStep(casterP, 8);
		if (step < 1) {
			return;
		}
		int coldtime = 0;
		if (step > 0) {
			if (step == 1) {
				coldtime = 3;
			} else if (step == 2) {
				coldtime = 4;
			} else if (step == 3) {
				coldtime = 5;
			} else {
				coldtime = 0;
			}
		}
		if (coldtime > 0) {
			ActiveSkillEntity aes[] = casterP.getActiveSkillAgent().getInCoolDownSkills();
			List<String> 冷却技能 = new ArrayList<String>();

			for (int i = 0; i < aes.length; i++) {
				ActiveSkillEntity e = aes[i];
				try {
					冷却技能.add(e.getSkill().getName());
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				if(e.getSkill().getId() != 300){
					continue;
				}
				
				if (e.getSkill().getId() == 300) {
					long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
					long ll = e.getStartTime() + e.getIntonateTime() + e.getAttackTime() + e.getCooldownTime();

					if (e.getStatus() == ActiveSkillEntity.STATUS_COOLDOWN) {
						log.debug("[修罗惊雷造成裁决减少却时间] ["+(ll - now <= coldtime * 1000)+"] [coldtime:"+e.getCooldownTime()+"] [ll-now:" + (ll - now) + "] [coldtime*1000:" + (coldtime * 1000) + "] [" + casterP.getName() + "]");

						if (ll - now <= coldtime * 1000) {
							SKILL_CD_MODIFY_REQ req = new SKILL_CD_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), casterP.getId(), (short) e.getSkill().getId(), (byte) ActiveSkillEntity.STATUS_COOLDOWN, 0);
							casterP.addMessageToRightBag(req);
							e.setStartTime(e.getStartTime() - (ll - now));
//							e.setCooldownTime(e.getCooldownTime()-(ll - now));
							log.debug("[修罗惊雷造成裁决减少却时间] [小于] [技能时间差：" + (ll - now) + "] [减少的时间：" + coldtime * 1000 + "] [技能stat：" + e.getStatus() + "] [施法者：" + casterP.getName() + "] [目标：" + target != null ? target.getName() : "null" + "]");
						} else {
							SKILL_CD_MODIFY_REQ req = new SKILL_CD_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), casterP.getId(), (short) e.getSkill().getId(), (byte) ActiveSkillEntity.STATUS_COOLDOWN, (e.getStartTime() - coldtime * 1000));
							casterP.addMessageToRightBag(req);
							e.setStartTime(e.getStartTime() - coldtime * 1000);
//							e.setCooldownTime(e.getCooldownTime()-coldtime * 1000);
							log.debug("[修罗惊雷造成裁决减少却时间] [大于] [技能时间差：" + (ll - now) + "] [减少的时间：" + coldtime * 1000 + "] [技能stat：" + e.getStatus() + "] [施法者：" + casterP.getName() + "] [目标：" + target != null ? target.getName() : "null" + "]");
						}
					}
				}
			}
			log.debug("[修罗惊雷造成裁决减少却时间] [测试] [" + 冷却技能 + "] [施法者：" + casterP.getName() + "] [目标：" + target != null ? target.getName() : "null" + "]");

		}
	}
	// 兽魁 9034 无痕  天阶10重:激活每次使用无痕技能都会减少幻石技能cd时间5秒
	public void checkShoukui9034(Player casterP, Fighter target) {
		if (target == null) {
			return ;
		}
		int step = getSlotStep(casterP, 9);
		if (step < 1) {
			return;
		}
		int coldtime = 0;
		if (step > 0) {
			if (step == 1) {
				coldtime = 3;
			} else if (step == 2) {
				coldtime = 4;
			} else if (step == 3) {
				coldtime = 5;
			} else {
				coldtime = 0;
			}
		}
		if (coldtime > 0) {
			ActiveSkillEntity aes[] = casterP.getActiveSkillAgent().getInCoolDownSkills();
			for (int i = 0; i < aes.length; i++) {
				ActiveSkillEntity e = aes[i];
				if (e.getSkill().getId() == 18012) {
					long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
					long ll = e.getStartTime() + e.getIntonateTime() + e.getAttackTime() + e.getCooldownTime();

					if (e.getStatus() == ActiveSkillEntity.STATUS_COOLDOWN) {
						if (ll - now <= coldtime * 1000) {
							SKILL_CD_MODIFY_REQ req = new SKILL_CD_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), casterP.getId(), (short) e.getSkill().getId(), (byte) ActiveSkillEntity.STATUS_END, 0);
							casterP.addMessageToRightBag(req);
							if (log.isDebugEnabled()) {
								log.debug("[兽魁减少幻石cd] [小于] [技能时间差：" + (ll - now) + "] [减少的时间：" + coldtime * 1000 + "] [技能stat：" + e.getStatus() + "] [施法者：" + casterP.getName() + "] [目标：" + target.getName() + "]");
							}
						} else {
							SKILL_CD_MODIFY_REQ req = new SKILL_CD_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), casterP.getId(), (short) e.getSkill().getId(), (byte) ActiveSkillEntity.STATUS_COOLDOWN, (e.getStartTime() - coldtime * 1000));
							casterP.addMessageToRightBag(req);
							e.setStartTime(e.getStartTime() - coldtime * 1000);
							if (log.isDebugEnabled()) {
								log.debug("[兽魁减少幻石cd] [大于] [技能时间差：" + (ll - now) + "] [减少的时间：" + coldtime * 1000 + "] [技能stat：" + e.getStatus() + "] [施法者：" + casterP.getName() + "] [目标：" + target.getName() + "]");
							}
						}
					}
				}
			}

		}
	}

	// old 仙心 712 火月 人阶10重:激活所有技能有1%触发减少法攻防御5%持续5秒
	public void checkLingZun712(Player casterP, Fighter target) {
		// new 获得每使用火月一次附带减少星移冷却时间4秒
		if (target == null) {
			return;
		}
		int step = getSlotStep(casterP, 1);
		if (step < 1) {
			return;
		}
		int coldtime = 0;
		if (step > 0) {
			if (step == 1) {
				coldtime = 4;
			} else if (step == 2) {
				coldtime = 7;
			} else if (step == 3) {
				coldtime = 10;
			} else {
				coldtime = 0;
			}
		}
		if (coldtime > 0) {
			ActiveSkillEntity aes[] = casterP.getActiveSkillAgent().getInCoolDownSkills();
			for (int i = 0; i < aes.length; i++) {
				ActiveSkillEntity e = aes[i];
				if (e.getSkill().getId() == 301) {
					long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
					long ll = e.getStartTime() + e.getIntonateTime() + e.getAttackTime() + e.getCooldownTime();

					if (e.getStatus() == ActiveSkillEntity.STATUS_COOLDOWN) {
						if (ll - now <= coldtime * 1000) {
							SKILL_CD_MODIFY_REQ req = new SKILL_CD_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), casterP.getId(), (short) e.getSkill().getId(), (byte) ActiveSkillEntity.STATUS_END, 0);
							casterP.addMessageToRightBag(req);
							log.debug("[仙心火月减星移冷却时间] [小于] [技能时间差：" + (ll - now) + "] [减少的时间：" + coldtime * 1000 + "] [技能stat：" + e.getStatus() + "] [施法者：" + casterP.getName() + "] [目标：" + target.getName() + "]");
						} else {
							SKILL_CD_MODIFY_REQ req = new SKILL_CD_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), casterP.getId(), (short) e.getSkill().getId(), (byte) ActiveSkillEntity.STATUS_COOLDOWN, (e.getStartTime() - coldtime * 1000));
							casterP.addMessageToRightBag(req);
							e.setStartTime(e.getStartTime() - coldtime * 1000);
							log.debug("[仙心火月减星移冷却时间] [大于] [技能时间差：" + (ll - now) + "] [减少的时间：" + coldtime * 1000 + "] [技能stat：" + e.getStatus() + "] [施法者：" + casterP.getName() + "] [目标：" + target.getName() + "]");
						}
					}
				}
			}

		}
	}

	/**
	 * 检查修罗是否释放减速
	 * @param p
	 * @param target
	 * @param sk
	 */
	public void checkDouLuoMinusSpeed(Player p, Fighter target) {
		if (target == null) {
			return;
		}
		try {
			int step = getSlotStep(p, 1);
			if (step < 1) {
				return;
			}
			BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateById(1332);
			Buff b = bt.createBuff(25);
			b.setStartTime(SystemTime.currentTimeMillis());
			b.setInvalidTime(b.getStartTime() + 5000);
			b.setCauser(p);
			target.placeBuff(b);
//			HunshiManager.getInstance().dealWithInfectSkill(p, target, b);
			log.debug("SkEnhanceManager.checkDouLuoMinusSpeed: [成功] 修罗 双舞 减速50 {}{}", new Object[] { p.getName(), target.getName() });
		} catch (Exception e) {
			log.warn("SkEnhanceManager.checkDouLuoMinusSpeed: [异常] 修罗 双舞 减速50 {}{}{}", new Object[] { p.getName(), target.getName(), e });
		}

	}

	// 809 魂击 人阶10重:获得所有技能有1%几率触发减速60%5秒; rate 1,2,3 (%)
	public void checkWuHuang809(Player p, Fighter target, ActiveSkill sk) {
		try{
			
			if(p.getCareer() != 4){
				return;
			}
			
			int step = getSlotStep(p, 4);	//50%,70%,100%
			if (step < 1) {
				return;
			}
			
			int bufflevel = 0;
			if(step==1){
				bufflevel = 20;
			}else if(step==2){
				bufflevel = 25;
			}else if(step==3){
				bufflevel = 35;
			}
			//对城主/boss无效
			
			addAllSkillNums(p,10);
			int recordvalue = allskillnums.get(new Long(p.getId())).intValue();
			
			log.debug("[武皇职业使用任意技能10次触发反噬] [ing] [bufflevel:"+bufflevel+"] [使用技能次数："+recordvalue+"] [技能："+(sk==null?"":sk.getName())+"] [施法者："+p.getName()+"] [目标："+(target!=null?target.getName():"没有目标")+"]");
			if(recordvalue>1 && recordvalue%10==0){
				BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateByName(Translate.初级反震);
				Buff b = bt.createBuff(bufflevel);
				b.setStartTime(SystemTime.currentTimeMillis());
				b.setInvalidTime(b.getStartTime() + 6000);
				b.setCauser(p);
				p.placeBuff(b);
				log.debug("[武皇职业使用任意技能10次触发反噬] [ok] [buffname:"+bt.getName()+"] [bufflevel:"+bufflevel+"] [使用技能次数："+recordvalue+"] [技能："+(sk==null?"":sk.getName())+"] [施法者："+p.getName()+"] [目标："+(target!=null?target.getName():"没有目标")+"]");
			}
		}catch (Exception e) {
			log.debug("[武皇职业使用任意技能10次触发反噬] [异常] [技能："+(sk==null?"":sk.getName())+"] [施法者："+p.getName()+"] [目标："+(target!=null?target.getName():"没有目标")+"]",e);
		}
		
	}

	/**
	 * 仙心，冰陵 获得冰陵技能附带提升自身20%法功持续30秒
	 * @param p
	 * @param target
	 * @param sk
	 */
	public void checkskill8212(Player p, Fighter target) {
		if (p.getCareer() != 3) {
			return;
		}
		try {
			int step = getSlotStep(p, 6);
			if (step == 3) {
				BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateByName(Translate.BOSS增加法术攻击强度);
				Buff b = bt.createBuff(1);
				b.setStartTime(SystemTime.currentTimeMillis());
				b.setInvalidTime(b.getStartTime() + 30000);
				b.setCauser(p);
				p.placeBuff(b);
				log.debug("SkEnhanceManager.checkskill8212: [成功] 仙心  冰陵 提升自身20%法功 {}{}", new Object[] { p.getName(), (target != null ? target.getName() : "") });
			}
		} catch (Exception e) {
			log.warn("SkEnhanceManager.checkskill8212: [异常] 仙心 冰陵 提升自身20%法功 {}{}{}", new Object[] { p.getName(), (target != null ? target.getName() : ""), e });
		}

	}

	/**
	 * 天诛获得任意15次技能后下次攻击技能附带使目标眩晕持续5秒
	 * @param p
	 * @param target
	 * @param sk
	 */
	public void checkWuHuang713(Player p, Fighter target, ActiveSkill sk) {
		try {
			if (p.getCareer() != 3) {
				return;
			}

			int step = getSlotStep(p, 8); // 50%,70%,100%
			if (step < 1) {
				return;
			}
			int bufflevel = 0;
			int usenums = 0;
			if (step == 1) {
				bufflevel = 25;
				usenums = 18 + 1;
			} else if (step == 2) {
				bufflevel = 35;
				usenums = 15 + 1;
			} else if (step == 3) {
				bufflevel = 49;
				usenums = 10 + 1;
			}

			addAllSkillNums(p, usenums);
			int recordvalue = allskillnums.get(new Long(p.getId())).intValue();
			if (target == null) {
				log.debug("[仙心职业使用任意技能10次触发眩晕] [目标不存在] [ing] [bufflevel:" + bufflevel + "] [使用技能次数：" + recordvalue + "] [技能：" + (sk == null ? "" : sk.getName()) + "] [施法者：" + p.getName() + "] [目标：" + (target != null ? target.getName() : "没有目标") + "]");
			} else {
				log.debug("[仙心职业使用任意技能10次触发眩晕] [ing] [bufflevel:" + bufflevel + "] [使用技能次数：" + recordvalue + "] [技能：" + (sk == null ? "" : sk.getName()) + "] [施法者：" + p.getName() + "] [目标：" + (target != null ? target.getName() : "没有目标") + "]");
			}

			if (recordvalue > 0 && recordvalue == usenums && target != null) {
				clearSkillNums(p);
				BuffTemplate tt = BuffTemplateManager.getInstance().getBuffTemplateById(1714);
				if (tt instanceof BuffTemplate_XuanYun == false) {
					log.debug("SkEnhanceManager.rateXuanYun: miss match {}", tt);
					return;
				}
				Buff buff = tt.createBuff(1);
				buff.setStartTime(SystemTime.currentTimeMillis());
				buff.setInvalidTime(buff.getStartTime() + 2000);
				buff.setCauser(p);
				target.placeBuff(buff);
//				HunshiManager.instance.dealWithInfectSkill(p ,target, buff);
				log.debug("[仙心职业使用任意技能10次触发眩晕] [ok] [buffname:" + tt.getName() + "] [bufflevel:" + bufflevel + "] [使用技能次数：" + recordvalue + "] [技能：" + (sk == null ? "" : sk.getName()) + "] [施法者：" + p.getName() + "] [目标：" + (target != null ? target.getName() : "没有目标") + "]");
			}

		} catch (Exception e) {
			log.debug("[仙心职业使用任意技能10次触发眩晕] [异常] [技能：" + (sk == null ? "" : sk.getName()) + "] [施法者：" + p.getName() + "] [目标：" + (target != null ? target.getName() : "没有目标") + "]", e);
		}

	}
	/**
	 * 兽魁  18013 灭魂 激活每10次任意技能后，下次技能附带定身目标2秒
	 * @param p
	 * @param target
	 * @param sk
	 */
	public void checkShoukui18013(Player p, Fighter target, ActiveSkill sk) {
		try {
			if (p.getCareer() != 5) {
				return;
			}

			int step = getSlotStep(p, 8); 
			if (step < 1) {
				return;
			}
			int bufflevel = 0;
			int usenums = 0;
			long lastTime = 1000;
			if (step == 1) {
//				bufflevel = 25;
				usenums = 15 + 1;
			} else if (step == 2) {
//				bufflevel = 35;
				usenums = 10 + 1;
			} else if (step == 3) {
//				bufflevel = 49;
				usenums = 10 + 1;
				lastTime = 2000;
			}
			addAllSkillNums(p, usenums);
			int recordvalue = allskillnums.get(new Long(p.getId())).intValue();
			if (log.isDebugEnabled()) {
				log.debug("[兽魁职业使用任意技能10次触发定身] [ing] [bufflevel:" + bufflevel + "] [使用技能次数：" + recordvalue + "] [需要次数：" + usenums + "] [技能：" + (sk == null ? "" : sk.getName()) + "] [施法者：" + p.getName() + "] [目标：" + (target != null ? target.getName() : "没有目标") + "]");
			}
			if (recordvalue > 0 && recordvalue == usenums && target != null) {
				clearSkillNums(p);
				BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateByName(Translate.千层定身);
				Buff b = bt.createBuff(bufflevel);
				b.setStartTime(SystemTime.currentTimeMillis());
				b.setInvalidTime(b.getStartTime() + lastTime);
				b.setCauser(p);
				target.placeBuff(b);
//				HunshiManager.getInstance().dealWithInfectSkill(p, target, b);
				if (log.isDebugEnabled()) {
					log.debug("[兽魁职业使用任意技能10次触发定身] [ok] [buffname:" + bt.getName() + "] [bufflevel:" + bufflevel + "] [使用技能次数：" + recordvalue + "] [需要次数：" + usenums + "] [技能：" + (sk == null ? "" : sk.getName()) + "] [施法者：" + p.getName() + "] [目标：" + (target != null ? target.getName() : "没有目标") + "]");
				}
			}
			
		} catch (Exception e) {
			log.warn("[兽魁职业使用任意技能10次触发定身] [异常] [技能：" + (sk == null ? "" : sk.getName()) + "] [施法者：" + p.getName() + "] [目标：" + (target != null ? target.getName() : "没有目标") + "]", e);
		}
	}

	/**
	 * 夺命使用任意18次技能后下次攻击技能附带吸取目标所有法力值，并封魔（无法释放任何攻击）持续5秒
	 * @param p
	 * @param target
	 * @param sk
	 */
	public void checkWuHuang705(Player p, Fighter target, ActiveSkill sk) {
		try {

			if (p.getCareer() != 1) {
				return;
			}

			int step = getSlotStep(p, 9); // 50%,70%,100%
			if (step < 1) {
				return;
			}

			int bufflevel = 0;
			int usenums = 0;
			if (step == 1) {
				bufflevel = 25;
				usenums = 18 + 1;
			} else if (step == 2) {
				bufflevel = 35;
				usenums = 15 + 1;
			} else if (step == 3) {
				bufflevel = 49;
				usenums = 10 + 1;
			}

			addAllSkillNums(p, usenums);

			int recordvalue = allskillnums.get(new Long(p.getId())).intValue();
//			if(log.isWarnEnabled()){
				log.debug("[修罗职业使用任意技能10次触发反噬] [ing] [bufflevel:" + bufflevel + "] [使用技能次数：" + recordvalue + "] [需要次数：" + usenums + "] [技能：" + (sk == null ? "" : sk.getName()) + "] [施法者：" + p.getName() + "] [目标：" + (target != null ? target.getName() : "没有目标") + "]");
//			}
			if (recordvalue > 0 && (recordvalue % usenums == 0) && target != null) {
				clearSkillNums(p);
				BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateById(1426);
				Buff b = bt.createBuff(bufflevel);
				b.setStartTime(SystemTime.currentTimeMillis());
				b.setInvalidTime(b.getStartTime() + 2000);
				b.setCauser(p);
				target.placeBuff(b);
				target.setMp(0);
				log.debug("[修罗职业使用任意技能10次触发反噬] [ok] [buffname:" + bt.getName() + "] [bufflevel:" + bufflevel + "] [使用技能次数：" + recordvalue + "] [需要次数：" + usenums + "] [技能：" + (sk == null ? "" : sk.getName()) + "] [施法者：" + p.getName() + "] [目标：" + (target != null ? target.getName() : "没有目标") + "]");
			}
		} catch (Exception e) {
			log.warn("[修罗职业使用任意技能10次触发反噬] [异常] [技能：" + (sk == null ? "" : sk.getName()) + "] [施法者：" + p.getName() + "] [目标：" + (target != null ? target.getName() : "没有目标") + "]", e);
		}

	}

	/**
	 * 夺魂 获得任意10次技能后附带减少目标40%命中持续5秒
	 * 40,60,100
	 * @param p
	 * @param target
	 * @param sk
	 */
	public void checkWuHuang716(Player p, Fighter target, ActiveSkill sk) {
		try {

			if (p.getCareer() != 2) {
				return;
			}

			int step = getSlotStep(p, 9); // 50%,70%,100%
			if (step < 1) {
				return;
			}
			int minz = 0;
			int bufflevel = 0;
			if (step == 1) {
				bufflevel = 20;
				minz = 40;
			} else if (step == 2) {
				bufflevel = 30;
				minz = 60;
			} else if (step == 3) {
				bufflevel = 49;
				minz = 100;
			}

			addAllSkillNums(p, 11);
			int recordvalue = allskillnums.get(new Long(p.getId())).intValue();

			if (target == null) {
				log.debug("[影魅职业使用任意技能10次触发减命中] [出错] [目标为空] [ing] [bufflevel:" + bufflevel + "] [使用技能次数：" + recordvalue + "]  [技能：" + (sk == null ? "" : sk.getName()) + "] [施法者：" + p.getName() + "] [目标：" + (target != null ? target.getName() : "没有目标") + "]");
				return;
			} else {
				log.debug("[影魅职业使用任意技能10次触发减命中] [ing] [bufflevel:" + bufflevel + "] [使用技能次数：" + recordvalue + "]  [技能：" + (sk == null ? "" : sk.getName()) + "] [施法者：" + p.getName() + "] [目标：" + (target != null ? target.getName() : "没有目标") + "]");
			}

			if (recordvalue > 0 && (recordvalue==11)) {
				clearSkillNums(p);
				BuffTemplate tt = BuffTemplateManager.getInstance().getBuffTemplateById(1600);
				if (false == tt instanceof BuffTemplate_JianMingZhong) {
					log.debug("SkEnhanceManager.checkWuHuang716: template error {}", tt);
					return;
				}
				BuffTemplate_JianMingZhong t = (BuffTemplate_JianMingZhong) tt;
				Buff_JianMingZhong buff = t.createBuff(bufflevel);
				buff.setStartTime(SystemTime.currentTimeMillis());
				buff.setInvalidTime(buff.getStartTime() + 2000);
				buff.attackRating = 800;
				buff.setDescription(Translate.text_3258 + minz + "%");
				buff.setCauser(p);
				target.placeBuff(buff);
				log.debug("[影魅职业使用任意技能10次触发减命中] [ok] [buffname:" + tt.getName() + "] [bufflevel:" + bufflevel + "] [使用技能次数：" + recordvalue + "] [技能：" + (sk == null ? "" : sk.getName()) + "] [施法者：" + p.getName() + "] [目标：" + (target != null ? target.getName() : "没有目标") + "]");
			}
		} catch (Exception e) {
			log.debug("[影魅职业使用任意技能10次触发减命中] [异常] [技能：" + (sk == null ? "" : sk.getName()) + "] [施法者：" + p.getName() + "] [目标：" + (target != null ? target.getName() : "没有目标") + "]", e);
		}

	}

	public void checkWuHuang506(Player p, Fighter target, ActiveSkill sk) {
		if (p == null) {
			return;
		}
		if (target == null) {
			return;
		}
		int step = getSlotStep(p, 2);
		if (step < 1) {
			return;
		}
		// 506 散灵 人阶10重:获得所有技能附带2%破甲效果; rate 2,3,4 (%)
		int r = ActiveSkill.random.nextInt(100);
		if (r < step + 1 + debugRateAdd) {
		} else {
			log.debug("SkEnhanceManager.checkWuHuang506: miss rate.506散灵 破甲 {}", r);
			return;
		}
		BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateById(1354);
		if (bt == null) {
			log.debug("SkEnhanceManager.checkWuHuang506: but is null");
			return;
		}
		if (bt instanceof BuffTemplate_XuRuo == false) {
			log.debug("SkEnhanceManager.checkWuHuang506: it's not BuffTemplate_XuRuo");
			return;
		}
		BuffTemplate_XuRuo bb = (BuffTemplate_XuRuo) bt;
		Buff_XuLuo buff = bb.createBuff(0);
		buff.setStartTime(SystemTime.currentTimeMillis());
		buff.setInvalidTime(buff.getStartTime() + 5000);
		buff.setCauser(p);
		buff.defence = 2;
		buff.resistance = 2;
		StringBuffer sb = new StringBuffer();
		sb.append(Translate.text_3382 + 2 + "%");
		if (sb.length() > 0) sb.append("，");
		sb.append(Translate.text_3383 + 2 + "%");
		buff.setDescription(sb.toString());
		target.placeBuff(buff);
		log.debug("SkEnhanceManager.checkWuHuang506:获得所有技能附带{}%破甲效果", step + 1);
	}

	public void addrecord(Player p, ActiveSkill sk) {
		if (sk == null) {
			return;
		}
		int skid = sk.getId();
		if (recordnum.get(new Long(p.getId())) == null) {
			LinkedHashMap<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
			map.put(new Integer(skid), new Integer(1));
			recordnum.put(new Long(p.getId()), map);
		} else {
			if (recordnum.get(new Long(p.getId())).get(new Integer(skid)) == null) {
				LinkedHashMap<Integer, Integer> map = recordnum.get(new Long(p.getId()));
				map.put(new Integer(skid), new Integer(1));
				recordnum.put(new Long(p.getId()), map);
			} else {
				int oldvalue = recordnum.get(new Long(p.getId())).get(new Integer(skid)).intValue();
				LinkedHashMap<Integer, Integer> map = recordnum.get(new Long(p.getId()));
				map.put(new Integer(skid), new Integer(oldvalue + 1));
				recordnum.put(new Long(p.getId()), map);
			}
		}
		if (Skill.logger.isDebugEnabled()) {
			Skill.logger.debug("[更新技能使用记录] [具体技能] [上次使用技能：" + sk.getName() + "] [技能id：" + skid + "] [记录数：" + recordnum.get(new Long(p.getId())).get(new Integer(skid)).intValue() + "] [账号：" + p.getUsername() + "] [角色名：" + p.getName() + "] [id:" + p.getId() + "]");
		}
	}

	public void addAllSkillNums(Player p, int usenums) {
		if (allskillnums.get(new Long(p.getId())) == null) {
			allskillnums.put(new Long(p.getId()), new Integer(1));
		} else {
			int oldvalue = allskillnums.get(new Long(p.getId())).intValue();
			allskillnums.put(new Long(p.getId()), new Integer(1 + oldvalue));
		}

		int recordvalue = allskillnums.get(new Long(p.getId())).intValue();

		String mess = "<f color='0x0012FF'>" + Translate.连击次数 + (recordvalue % usenums) + "</f>";

		if (recordvalue % usenums == 0) {
			mess = "<f color='0xFF0000'>" + Translate.连击次数 + usenums + "</f>";
		}

		SKILL_POINT_SHOW_RES res = new SKILL_POINT_SHOW_RES(GameMessageFactory.nextSequnceNum(), mess);
		p.addMessageToRightBag(res);

		if (Skill.logger.isDebugEnabled()) {
			Skill.logger.debug("[更新技能使用记录] [总的技能] [记录数：" + recordvalue + "] [usenums:" + usenums + "] [账号：" + p.getUsername() + "] [角色名：" + p.getName() + "] [id:" + p.getId() + "]");
		}
	}

	//临时修改，清空技能连击
	public void clearSkillNums(Player player){
		if(allskillnums!=null){
			allskillnums.put(new Long(player.getId()), new Integer(0));
		}
		SKILL_POINT_SHOW_RES res = new SKILL_POINT_SHOW_RES(GameMessageFactory.nextSequnceNum(), "");
		player.addMessageToRightBag(res);
	}
	
	public void clearSkillIdNums(Player player){
		if(recordnum!=null){
			recordnum.clear();
		}
	}
	
	public void clearskillnums(Player p) {
		allskillnums.put(new Long(p.getId()), new Integer(0));
		recordnum.clear();
		Skill.logger.debug("[玩家下线清理大师技能连击数] [name:" + p.getName() + "]");
	}

	/**
	 * 幻石,天阶10重:激活永久提升20%幻石技能造成的伤害
	 * @return
	 */
	public int checkShouKuiSkill18012(Player p, Fighter target, int damage,ActiveSkill sk){
		if(p == null || target == null || damage == 0 || sk == null){
			if(Skill.logger.isInfoEnabled()){
				Skill.logger.info("SkEnhanceManager.checkShouKuiSkill18012:error:{}{}{}",new Object[]{(p==null),(null==null),(target==null),damage});
			}
			return 0;
		}
		int step = getSlotStep(p, sk.pageIndex);
		int adddamage = 0;
		if(step <= 0){
			adddamage = 0;
		}
		if(step == 1){
			adddamage = (int) (damage * 0.1);
		}else if(step == 2){
			adddamage = (int) (damage * 0.15);
		}else if(step == 3){
			adddamage = (int) (damage * 0.2);
		}
		if (log.isDebugEnabled()) {
			log.debug("SkEnhanceManager.fixMagicAttack: 兽魁 18012 幻石 damage {} adddamage {} setp {}", new Object[] {damage, adddamage, step});
		}
		return adddamage;
	}
	
	/**
	 * 兽魁 9113 炼魂  增加目标数
	 */
	public int checkShouKuiSkill9113(Player p, Fighter target, int damage,ActiveSkill sk){
		if(p == null || target == null || damage == 0 || sk == null){
			if(Skill.logger.isInfoEnabled()){
				Skill.logger.info("SkEnhanceManager.checkShouKuiSkill9113:error:{}{}{}",new Object[]{(p==null),(null==null),(target==null),damage});
			}
			return 0;
		}
		int step = getSlotStep(p, sk.pageIndex);
		int adddamage = 0;
		if(step <= 0){
			adddamage = 0;
		}
		if(step == 1){
			adddamage = (int) (damage * 0.03);
		}else if(step == 2){
			adddamage = (int) (damage * 0.06);
		}else if(step == 3){
			adddamage = (int) (damage * 0.1);
		}
		if (log.isDebugEnabled()) {
			log.debug("SkEnhanceManager.fixMagicAttack: 兽魁 9113 炼魂 adddamage {} setp {}", new Object[] { adddamage, step});
		}
		return adddamage;
	}
	
	/**
	 * 获得释放5次断喉技能后下次断喉技能附带3%目标最大生命值上限伤害。
	 * @param p
	 * @param target
	 * @param sk
	 */
	public int checkWuHuang710(Player p, Fighter target, ActiveSkill sk) {
		int damagenum = 0;
		if (p == null) {
			return damagenum;
		}
		if (target == null) {
			return damagenum;
		}
		int step = getSlotStep(p, 8);
		if (step < 1) {
			return damagenum;
		}

		addrecord(p, sk);

		try {
			Integer record = recordnum.get(new Long(p.getId())).get(new Integer(sk.getId()));

			if (record == null) {
				Skill.logger.debug("SkEnhanceManager.checkWuHuang710:error:record==null");
				return damagenum;
			}

			int recordvalue = record.intValue();

			if (recordvalue==6) {
				clearSkillIdNums(p);
				if (step == 1) {
					damagenum = (int) (target.getMaxHP() * 3 / 100);
				} else if (step == 2) {
					damagenum = (int) (target.getMaxHP() * 6 / 100);
				} else if (step == 3) {
					damagenum = (int) (target.getMaxHP() * 10 / 100);
				}
			}
			Skill.logger.debug("SkEnhanceManager.checkWuHuang710:step{} recordvalue:{} damage:{}",new Object[]{step, recordvalue, damagenum});
		} catch (Exception e) {
			e.printStackTrace();
			Skill.logger.error("[影魅断后增加伤害] [异常] [step:" + step + "] [" + (p != null ? p.getName() : "") + "] [" + (sk != null ? sk.getName() : "") + "]", e);
		}
		return damagenum;
	}

	/**
	 * 9044新反噬，增加技能的伤害
	 * @param p
	 * @param target
	 * @param sk
	 * @return
	 */
	public int checkWuHuang9044(Player p, Fighter target, ActiveSkill sk ,int damage) {
		int damagenum = 0;
		Skill.logger.debug("checkWuHuang9044:  adddamage:{},damage:{} ,{}",new Object[]{damagenum,damage,p!=null?p.getLogString():"nul",target!=null?target.getName():"nul"});
		if (p == null) {
			return damagenum;
		}
		if (target == null) {
			return damagenum;
		}
		SkBean bean = SkEnhanceManager.getInst().findSkBean(p);
		if (bean == null) {
			return damagenum;
		}
		int currLevel = 0;
		if (bean != null && bean.useLevels != null && bean.useLevels[6] >= 0) {
			currLevel++;
		}
		if(currLevel > 0){
			damagenum = damage * currLevel /100;
		}
		try {
			Skill.logger.debug("checkWuHuang9044:  adddamage:{},damage:{} ,currLevel:{}",new Object[]{damagenum,damage,currLevel});
		} catch (Exception e) {
			e.printStackTrace();
			Skill.logger.error("[9044新反噬，增加技能的伤害checkWuHuang9044] [异常] [" + (p != null ? p.getName() : "") + "] [" + (sk != null ? sk.getName() : "") + "]", e);
		}
		return damagenum;
	}
	
	public void checkWuHuang714(Player p, Fighter target, ActiveSkill sk) {
		if (p == null) {
			return;
		}
		if (target == null) {
			return;
		}
		int step = getSlotStep(p, 1);
		if (step < 1) {
			return;
		}

		addrecord(p, sk);

		try {
			Integer record = recordnum.get(new Long(p.getId())).get(new Integer(sk.getId()));

			if (record == null) {
				Skill.logger.debug("SkEnhanceManager.checkWuHuang714:error:record==null");
				return;
			}

			int recordvalue = record.intValue();

			Skill.logger.debug("SkEnhanceManager.checkWuHuang714:recordvalue:{}", recordvalue);
			if (recordvalue % 2 == 0) {
				BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateById(1351);
				if (bt == null) {
					Skill.logger.debug("SkEnhanceManager.checkWuHuang714: but is null");
					return;
				}
				if (bt instanceof BuffTemplate_ZhongDu == false) {
					Skill.logger.debug("SkEnhanceManager.checkWuHuang714: it's not BuffTemplate_ZhongDu");
					return;
				}

				int addtime = 0;
				if (step == 1) {
					addtime = 3;
				} else if (step == 2) {
					addtime = 4;
				} else if (step == 3) {
					addtime = 5;
				}

				BuffTemplate_ZhongDu bb = (BuffTemplate_ZhongDu) bt;
				Buff_ZhongDu buff = bb.createBuff(0);
				buff.setStartTime(SystemTime.currentTimeMillis());
				double d = 1;
				if (addtime == 5) {
					d = 1.1;
				}
				buff.setInvalidTime(buff.getStartTime() + (long) (d * addtime * 1000));
				buff.setCauser(p);
				buff.hpFix = target.getMaxHP() * 3 / 100;
				StringBuffer sb = new StringBuffer();
				sb.append(Translate.text_3391 + (1) + Translate.text_3234 + buff.hpFix + Translate.text_3275);
				buff.setLastingTime(1000);
				buff.setDescription(sb.toString());
				target.placeBuff(buff);
				Skill.logger.debug("[武皇降头使目标流血] [ok] [step:" + step + "] [d:" + d + "] [" + (p != null ? p.getName() : "") + "] [" + (sk != null ? sk.getName() : "") + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Skill.logger.error("[武皇降头使目标流血] [异常] [step:" + step + "] [" + (p != null ? p.getName() : "") + "] [" + (sk != null ? sk.getName() : "") + "]", e);
		}

	}

	// 仙心 502 耀天 人阶10重:激活增加2%双防 2,3,5
	public int checkLingZun502(Player p, int value) {
		if (p == null) {
			return 0;
		}
		int step = getSlotStep(p, 7);
		if (step < 1) {
			return 0;
		}
		int r = step == 3 ? 5 : (step + 1);
		value = value * r / 100;
		log.debug("SkEnhanceManager.checkLingZun502:/仙心502	耀天增加{}%双防 = {}", r, value);
		return value;
	}

	// 仙心 902 坠天 人阶10重:激活所有技能附带3%减速目标减速30%5秒 rate 3,4,5
	public void checkLingZun902(Player p, Fighter target, ActiveSkill sk) {
		if (target == null) {
			return;
		}
		int step = getSlotStep(p, 5);
		if (step < 1) {
			return;
		}
		int r = ActiveSkill.random.nextInt(100);
		if (r < step + 2 + debugRateAdd) {
			BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateById(1332);
			Buff b = bt.createBuff(1);
			b.setStartTime(SystemTime.currentTimeMillis());
			b.setInvalidTime(b.getStartTime() + 5000);
			b.setCauser(p);
			target.placeBuff(b);
//			HunshiManager.getInstance().dealWithInfectSkill(p, target, b);
			log.debug("SkEnhanceManager.checkLingZun902:仙心902坠天 减速30{}", target.getName());
		}
	}

	public void checkGuiSha407(Player p, Fighter target, ActiveSkill sk) {
		// 影魅 407 狼印 人阶10重:获得所有技能有2%几率触发100%暴击持续5秒
		if (target == null || p == null) {
			return;
		}
		int step = getSlotStep(p, 3);
		if (step < 1) {
			return;
		}
		int r = ActiveSkill.random.nextInt(100);
		if (r < step + 1 + debugRateAdd) {
			BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateById(1589);
			Buff b = bt.createBuff(1);
			b.setStartTime(SystemTime.currentTimeMillis());
			b.setInvalidTime(b.getStartTime() + 5000);
			b.setCauser(p);
			p.placeBuff(b);
			log.debug("SkEnhanceManager.checkGuiSha798: 影魅	407	狼印	 {} 几率触发100%暴击", target.getName());
		}
	}

	public void checkGuiSha606(Player p, Fighter target, ActiveSkill sk, int slot) {
		// 影魅 606 轮刺 人阶10重:获得所有技能附带1%封魔目标5秒
		if (target == null) {
			return;
		}
		int step = getSlotStep(p, slot);
		if (step < 1) {
			return;
		}
		int r = ActiveSkill.random.nextInt(100);
		if (r < step + debugRateAdd) {
			BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateById(1426);
			Buff b = bt.createBuff(1);
			b.setStartTime(SystemTime.currentTimeMillis());
			b.setInvalidTime(b.getStartTime() + 5000);
			b.setCauser(p);
			target.placeBuff(b);
//			HunshiManager.getInstance().dealWithInfectSkill(p, target, b);
			log.debug("SkEnhanceManager.checkGuiSha606: bt {} slot {}", bt.getName(), slot);
			log.debug("SkEnhanceManager.checkGuiSha606: 影魅606轮刺/仙心713天诛{}几率封魔 {}", sk.getName(), target.getName());
		}
	}

	public void checkGuiSha798(Player p, Fighter target) {
		// 影魅 708 斩首 获得斩首技能附带（减少所有治疗效果30%）持续5秒
		if (target == null) {
			return;
		}
		int step = getSlotStep(p, 1);
		if (step < 1) {
			return;
		}
		BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateByName(Translate.降低治疗);

		if (bt != null) {
			Buff buff = bt.createBuff(step - 1);
			if (buff != null) {
				buff.setStartTime(SystemTime.currentTimeMillis());
				buff.setInvalidTime(buff.getStartTime() + 5000);
				buff.setCauser(p);
				if (target instanceof Player) {
					((Player) target).setLowerCureLevel(step - 1);
				}
				target.placeBuff(buff);
				log.debug("SkEnhanceManager.checkGuiSha798:影魅 sk708斩首降低【治疗效果】; {}bt {}step{}caster{}target{}", new Object[] { target.getName(), bt.getName(), step, p.getName(), target.getName() });
			}
		}
	}

	public void checkSlotDouLuo(Player casterP, Fighter target, int bigStep, Logger log, int idx) {
		if (target == null) {
			return;
		}
		switch (idx) {
		case 5:// 602 杀戮 人阶10重:获得所有技能附带1%几率减少目标移动速度50%5秒( buff level 25)
			BuffTemplate tt = BuffTemplateManager.getInstance().getBuffTemplateById(1357);
			if (tt == null) {
				log.debug("SkEnhanceManager.checkSlotDouLuo: null buff template.");
				break;
			}
			int rate = bigStep + debugRateAdd;
			int r = ActiveSkill.random.nextInt(100);
			if (r >= rate + debugRateAdd) {
				log.debug("SkEnhanceManager.checkSlotDouLuo: 602 杀戮 miss {}, {}", r, rate);
				break;
			}
			Buff buff = tt.createBuff(1);
			buff.setStartTime(SystemTime.currentTimeMillis());
			buff.setInvalidTime(buff.getStartTime() + 5000);
			buff.setCauser(casterP);
			target.placeBuff(buff);
			log.debug("SkEnhanceManager.checkSlotDouLuo: 602 杀戮 buff {}", buff.getTemplateName());
			break;
		default:
			return;
		}
	}

	/**
	 * 401 血印 人阶10重:获得所有技能附带1%吸血
	 * @param casterP
	 * @param target
	 * @param bean
	 * @param log2
	 */
	public void checkSlot3DouLuo(Player casterP, Fighter target, SkBean bean, Logger log2) {
		int lv = bean.useLevels[3];//
		if (lv < 10) {
			log.debug("SkEnhanceManager.checkSlot1DouLuo: 3 slot < 10");
			return;
		}
		lv = lv / 10;
		BuffTemplate ttemp = BuffTemplateManager.getInstance().getBuffTemplateById(1473);
		if (ttemp instanceof BuffTemplate_ChouXie == false) {
			return;
		}
		BuffTemplate_ChouXie tt = (BuffTemplate_ChouXie)ttemp;
		tt.setHpStealPercent(new int[] { 1, 1, 2, 3, 4 });
		Buff buff = tt.createBuff(lv);
		buff.setStartTime(SystemTime.currentTimeMillis());
		buff.setInvalidTime(buff.getStartTime() + 30 * 24 * 3600 * 100);
		buff.setCauser(casterP);
		buff.setSyncWithClient(true);
		buff.setIconId("buff_jiaxue");
		casterP.placeBuff(buff);
		log.debug("SkEnhanceManager.checkSlot2DouLuo: 3 slot 血印 吸血, lv {}", lv);
	}

	// 601 旋风 人阶10重:获得旋风技能有2%眩晕目标5秒
	public void checkSlot2DouLuo(Player casterP, Fighter target, SkBean bean, Logger log) {
		if (target == null) {
			return;
		}
		int lv = bean.useLevels[2];//
		if (lv < 10) {
			log.debug("SkEnhanceManager.checkSlot1DouLuo: 2 slot < 10");
			return;
		}// 2,3,5
			// 1,2,3
		int vs[] = { 0, 2, 3, 5 };
		lv = vs[lv / 10];
		boolean hit = rateXuanYun(casterP, target, log, lv);
		if (hit) {
			log.debug("SkEnhanceManager.checkSlot2DouLuo: 2 slot 旋风");
		}
	}

	public boolean rateXuanYun(Player casterP, Fighter target, Logger log, int lv) {
		int r = ActiveSkill.random.nextInt(100);
		if (r >= lv + debugRateAdd) {
			return false;
		}
		BuffTemplate tt = BuffTemplateManager.getInstance().getBuffTemplateById(1714);
		if (tt instanceof BuffTemplate_XuanYun == false) {
			log.debug("SkEnhanceManager.rateXuanYun: miss match {}", tt);
			return false;
		}
		Buff buff = tt.createBuff(1);
		buff.setStartTime(SystemTime.currentTimeMillis());
		buff.setInvalidTime(buff.getStartTime() + 5000);
		buff.setCauser(casterP);
		target.placeBuff(buff);
//		HunshiManager.instance.dealWithInfectSkill(casterP ,target, buff);
		return true;
	}

	public void checkSlot1DouLuo(Player casterP, Fighter target, SkBean bean, Logger log) {
		if (target == null) {
			log.debug("SkEnhanceManager.checkSlot1DouLuo: target is null.");
			return;
		}
		// 双舞 人阶10重:获得双舞连击+1、且增加所有技能附带1%几率使目标命中减少80%持续5秒
		byte lv = bean.useLevels[1];//
		if (lv < 10) {
			log.debug("SkEnhanceManager.checkSlot1DouLuo: 1 slot < 10");
			return;
		}
		int rate = lv / 10 + debugRateAdd;
		int r = ActiveSkill.random.nextInt(100);
		if (r < rate + debugRateAdd) {//
			BuffTemplate tt = BuffTemplateManager.getInstance().getBuffTemplateById(1600);
			if (false == tt instanceof BuffTemplate_JianMingZhong) {
				log.debug("SkEnhanceManager.checkSlot1DouLuo: template error {}", tt);
				return;
			}
			BuffTemplate_JianMingZhong t = (BuffTemplate_JianMingZhong) tt;
			Buff_JianMingZhong buff = t.createBuff(1);
			buff.setStartTime(SystemTime.currentTimeMillis());
			buff.setInvalidTime(buff.getStartTime() + 5000);
			buff.attackRating = 800;
			buff.setDescription(Translate.text_3258 + buff.attackRating / 10 + "%");
			buff.setCauser(casterP);
			target.placeBuff(buff);
//			HunshiManager.getInstance().dealWithInfectSkill(casterP, target, buff);
			log.debug("SkEnhanceManager.checkSlot1DouLuo: 双舞 {}", buff.getDescription());
		} else {
			log.debug("SkEnhanceManager.checkSlot1DouLuo: miss r {} base {}", r, rate);
		}
		log.debug("SkEnhanceManager.checkSlot1DouLuo: end.");
	}

	public long calcCDSub(Fighter owner, ActiveSkill skill) {
		if (owner instanceof Player == false) {
			log.debug("SkEnhanceManager.calcCDSub: not player {}", owner);
			return 0;
		}
		int vv[] = { 0, 0, 0, 0 };
		switch (skill.getId()) {
		case 8213:// 影魅 8213 遁形 人阶10重:获得遁影技能持续时间+2秒且cd减少20秒 //SkillWithoutTraceAndOnTeamMember
			vv = new int[] { 0, 10, 20, 30 };
			break;
		case 8212:// 仙心 8212 冰陵 人阶10重:激活减少冰陵技能cd20秒 //SkillWithoutTraceAndOnTeamMember
			vv = new int[] { 10, 40, 40, 40 };
			break;
		case 8258:	//兽魁 8258 嗜血 减少技能cd
			vv = new int[] {0, 20, 30, 40};
			break;
		// case 811:// 811 惊魂 人阶10重:获得惊魂技能cd减少2秒 ;2,3,5s //SkillWithoutTraceAndWithTargetOrPosition
		// vv = new int[] { 1, 2, 3, 5 };
		// break;
//		case 704:
//			vv = new int[] { 1, 3, 4, 5 };
//			break;
		default:
			return 0;
		}
		Player player = (Player) owner;
		do {
			if (skill.pageIndex < 0) {
				log.debug("calcCDSub: 1");
				break;
			} 
			if (SkEnhanceManager.open == false) break;
			if (player.getLevel() < TransitRobberyManager.openLevel) {
				log.debug("calcCDSub: 2");
				break;
			}
			SkBean bean = SkEnhanceManager.getInst().findSkBean(player);
			if (bean == null) {
				log.debug("calcCDSub: 3");
				break;
			}
			if (bean.useLevels == null) {
				log.debug("calcCDSub: 4");
				break;
			}
			if (bean.useLevels[skill.pageIndex] < 10) {
				log.debug("SkillInfoHelper.generate: 5");
				break;
			}
			int lv = bean.useLevels[skill.pageIndex] / 10;
			// 20,30,40s
			lv = vv[lv];// 402 普度 人阶10重:激活减少普度技能cd20秒
			log.debug("SkEnhanceManager.calcCDSub:402 {}, cd sub {}", skill.getName(), lv);
			return lv * 1000;
		} while (false);
		return 0;
	}

	public int getLayerAddPoint(Player p, int slot) {
		int lv = getLayerValue(p, slot);
		if (lv < 1) {
			return 0;
		}
		int point = SkEnConf.conf[p.getCareer() - 1][slot].levelAddPoint[lv - 1];
		return point;
	}

	public int getLayerValue(Player p, int slot) {
		if (p == null || slot < 0) {
			return 0;
		}
		if (open == false) {
			return 0;
		}
		int ret = 0;
		SkBean bean = SkEnhanceManager.getInst().findSkBean(p);
		if (bean == null) {
			return ret;
		}
		if (bean != null && bean.useLevels != null) {
			ret = bean.useLevels[slot];
		}
		return ret;
	}

	/**
	 * 开启的大段，返回0,1,2,3
	 * @param p
	 * @param slot
	 * @return
	 */
	public int getSlotStep(Player p, int slot) {
		if (open == false) {
			return 0;
		}
		if (slot < 0) {
			return 0;
		}
		int ret = 0;
		SkBean bean = SkEnhanceManager.getInst().findSkBean(p);
		if (bean == null) {
			return ret;
		}
		if (bean != null && bean.useLevels != null && bean.useLevels[slot] >= 10) {
			ret = bean.useLevels[slot] / 10;
		}
		return ret;
	}

	public int getBuffAddBp(Player p, Skill skill) {
		int ret = 0;
		switch (skill.getId()) {
		case 705: // 修罗 705 夺命 人阶10重:获得夺命触发暴击几率增加5%
			// ret = getSlotStep(p, 9);
			// ret *= 5;// 5,10,15
			break;
		case 715:// 九黎715噬灵人阶10重:获得噬灵技能中毒效果出发几率增加1%
			ret = getSlotStep(p, skill.pageIndex);
			break;
		}
		return ret;
	}

	public int getBuffAddTime(Player p, Skill skill) {
		int ret = 0;
		switch (skill.getId()) {
		case 8213:// c//影魅 8213 遁形 人阶10重:获得遁影技能持续时间+2秒且cd减少20秒
			ret = getSlotStep(p, skill.pageIndex);
			ret = ret + 1;
			break;
		case 716:// 影魅 716 夺魂 人阶10重:获得夺魂眩晕目标持续时间+1秒 , 1.5 ? 2
			// ret = getSlotStep(p, skill.pageIndex);
			break;//
		case 8212:// 仙心8212冰陵 增加持续时间:
			ret = getLayerAddPoint(p, skill.pageIndex);
			log.debug("SkEnhanceManager.getBuffAddTime: 仙心8212冰陵 增加持续时间:{}玩家:{}", ret, p.getName());
			break;
		case 812:// 812 反噬 人阶10重:获得反噬技能持续时间+2秒
		case 903:// 903 血祭 人阶10重:获得血祭技能持续时间+2秒 2,3,5 s
			ret = getSlotStep(p, skill.pageIndex);
			switch (ret) {
			case 1:
				ret = 2;
				break;
			case 2:
				ret = 3;
				break;
			case 3:
				ret = 5;
				break;
			}
			log.debug("SkEnhanceManager.getBuffAddTime: 持续时间增加 {} {}", skill.getName(), ret);
			break;
		}
		return ret;
	}

	public int fixPhyDefence(Player player, int value) {
		if (player.getCareer() == 2) {
			int step = getSlotStep(player, 0);
			if (step < 0) return 0;
			int vv = 0;
			switch (step) {
			case 1:
				vv = 2;
				break;
			case 2:
				vv = 3;
				break;
			case 3:
				vv = 4;
				break;
			}
			int ret = value * vv / 100;
			if (log.isDebugEnabled()) log.debug("影魅熊印提升{}%外防加成,base {} got {}", new Object[] { vv, value, ret });
			return ret;
		}
		if(player.getCareer() == 5){
			int step = getSlotStep(player, 3);
			if (step < 0) return 0;
			int vv = 0;
			switch (step) {
			case 1:
				vv = 1;
				break;
			case 2:
				vv = 2;
				break;
			case 3:
				vv = 3;
				break;
			}
			int ret = value * vv / 100;
			if (log.isDebugEnabled()) log.debug("兽魁钢胆提升{}%外防加成,base {} got {}", new Object[] { vv, value, ret });
			return ret;
		}
		if (player.getCareer() == 3) {
			return checkLingZun502(player, value);
		}
		if (player.getCareer() != 1) {
			return 0;
		}
		// 修罗 21006 赤焰 人阶10重:获得永久提升自身外防2% //2,3,4
		int step = getSlotStep(player, 10);
		if (step < 1) {
			return 0;
		}
		step += 1;
		int ret = value * step / 100;
		if (log.isDebugEnabled()) {
			log.debug("SkEnhanceManager.fixPhyDefence:修罗21006赤焰 rate{} base {} got {}", new Object[] { step, value, ret });
		}
		return ret;
	}

	// //仙心 803 霜暴 人阶10重:激活霜爆伤害加成10% 10,15,20
	public int fixLingZun803(int ud, Player player, ActiveSkill as) {
		if (as.getId() != 803) {
			return 0;
		}
		int step = getSlotStep(player, as.pageIndex);
		if (step <= 0) {
			return 0;
		}
		ud = ud * (step + 1) * 5 / 100;
		log.debug("SkEnhanceManager.fixLingZun803: 仙心	803	霜暴 {}", ud);
		return ud;
	}

	// 仙心 21007 乾蓝 人阶10重:激活乾蓝伤害加成5% 5,8,10
	public int fixLingZun21007(int ud, Player player, ActiveSkill as) {
		if (as.getId() != 21007) {
			return 0;
		}
		int step = getSlotStep(player, as.pageIndex);
		switch (step) {
		case 0:
			return 0;
		case 1:
			step = 5;
			break;
		case 2:
			step = 8;
			break;
		case 3:
			step = 10;
			break;
		}
		ud = ud * step / 100;
		log.debug("SkEnhanceManager.fixLingZun21007:仙心21007乾蓝伤害加成{}% = {}", step, ud);
		return ud;
	}

	// 21008 怨毒 人阶10重:获得永久2%血上限加成 ; 2,3,5(%)
	// 9035 抽灵 人阶10重:获得永久2%血上限加成 ; 3,4,5(%)
	public int fixMaxHP(Player player, int value) {
		if (player.getCareer() != 4 && player.getCareer() != 5) return 0;
		int step = getSlotStep(player, 10);
		if (step < 1) {
			return 0;
		}
		switch (step) {
		case 1:
			step = 2;
			if (player.getCareer() == 5) {
				step = 3;
			}
			break;
		case 2:
			step = 4;
			if (player.getCareer() == 5) {
				step = 4;
			}
			break;
		case 3:
			step = 6;
			break;
		}
		log.debug("SkEnhanceManager.fixMaxHP: 九黎 21008	怨毒 {} ", step);
		return value * step / 100;
	}

	/**
	 * 大师技能额外的外功加成
	 * @return
	 */
	public int morePhyAttack(Player player, int value) {
		if (player == null || value == 0) {
			return 0;
		}
		try {
			if (player.getCareer() == 1) { // 修罗，狂印，外功提升2%，3%，4%
				int slotStep = getSlotStep(player, 0);
				if (slotStep <= 0) {
					return 0;
				}
				slotStep += 1;
				int ret = value * slotStep / 100;
				if (log.isDebugEnabled()) {
					log.debug("[外功加成计算] [OK] [value：{}] [加成：{}] [技能开启阶段：{}] [玩家：{}]", new Object[] { value, ret, slotStep, player.getLogString() });
				}
				return ret;
			}
		
		} catch (Exception e) {
			log.warn("[外功加成计算] [Excepiton] [value：{}] [玩家：{}] [{}]", new Object[] { value, player.getLogString(), e });
		}
		return 0;
	}

	public void hpLowCreatBuff(Player player) {
		checkskillbuff(player);
		check雪葬(player);
	}
	
	/**
	 * 吞噬: 天阶10重:激活永久提升吞噬回血10%
	 */
	public int checkShouKuiSkill9033(Player player ,int value){
		if(player.getCareer() == 5){
			int step = getSlotStep(player, 4);
			if(step <= 0){
				return 0;
			}
			int addvalue = 0;
			if(step == 1){
				addvalue = value*3/100;
			}else if(step == 2){
				addvalue = value*6/100;
			}else if (step == 3){
				addvalue = value*10/100;
			}
			if (log.isDebugEnabled()) {
				log.debug("SkEnhanceManager.fixMagicAttack: 兽魁 吞噬 addvalue {} setp {} value:{}", new Object[] { addvalue, step,value});
			}
			return addvalue;
		}
		return 0;
	}

	/**
	 * 影魅，狼印，血低于30%触发buff
	 * @param player
	 * @param value
	 * @return
	 */
	public void checkskillbuff(Player player) {
		if (player == null) {
			return;
		}
		if (player.getCareer() == 2) {
			try {
				int step = player.getMoreSpeed();
				if (step > 0) {
					Skill.logger.debug("[影魅狼印] [血低于百分三十触发buff] [test] [step:" + step + "] [间隔：" + (System.currentTimeMillis() - player.getLastBuffLength()) + "] [hp:" + player.getHp() + "] [maxhp:" + player.getMaxHP() + "] [触发时间：" + TimeTool.formatter.varChar23.format(player.getLastBuffLength()) + "] [speedA:" + player.getSpeedA() + "] [speed:" + player.getSpeed() + "] [" + player.getLogString() + "] [2]");
					if (System.currentTimeMillis() - player.getLastBuffLength() >= 60000) {
						if (player.getHp() * 3 <= player.getMaxHP()) {
							BuffTemplate tt = BuffTemplateManager.getInstance().getBuffTemplateById(1758);
							if (tt instanceof BuffTemplate_ZengShu == false) {
								log.debug("SkEnhanceManager.checkskillbuff: buff match error{}", tt);
								return;
							}
							int oldspeed = player.getSpeed();
							Buff buff = tt.createBuff(step);
							buff.setStartTime(SystemTime.currentTimeMillis());
							buff.setInvalidTime(buff.getStartTime() + 10000);
							buff.setCauser(player);
							player.placeBuff(buff);
							player.setLastBuffLength(System.currentTimeMillis() + 10000);
							if (Skill.logger.isDebugEnabled()) {
								Skill.logger.debug("[影魅狼印] [血低于百分三十触发buff] [疾 风] [成功] [step:" + step + "] [触发时间：" + TimeTool.formatter.varChar23.format(player.getLastBuffLength()) + "] [hp:" + player.getHp() + "] [maxhp:" + player.getMaxHP() + "] [oldspeed:" + oldspeed + "] [newspeed:" + player.getSpeed() + "] [" + player.getLogString() + "]");
							}
						}
					}
				}
			} catch (Exception e) {
				Skill.logger.error("[影魅狼印] [血低于百分三十触发buff] [异常] [speedA:" + player.getSpeedA() + "] [speed:" + player.getSpeed() + "] [" + player.getLogString() + "]", e);
			}
		}
	}

	/**
	 * 获得当生命少于30%时触发能量罩，能量罩可吸收一定伤害值，吸收的伤害值是自身内功的50%（防护罩被激活后持续8秒后消失）15秒cd触发间隔
	 * new:抵挡X次伤害,持续20秒
	 * @param player
	 */
	public void check雪葬(Player player) {
		if (player == null) {
			return;
		}
		if (player.getCareer() == 3) {
			try {
				int step = getSlotStep(player, 9);
//				Skill.logger.debug("[仙心雪葬] [血低于百分三十触发buff] [test] [step:" + step + "] [hp:" + player.getHp() + "] [maxhp:" + player.getMaxHP() + "] [" + player.getLogString() + "] [1]");
				if (step > 0) {
					Long value = player.skillTouchSpace.get(new Integer(805));
					if (value == null) {
						player.skillTouchSpace.put(new Integer(805), new Long(1111l));
					}
					if (value != null) {
						if (System.currentTimeMillis() - value.longValue() > 60000) {
							Skill.logger.debug("[仙心雪葬] [血低于百分三十触发buff] [test] [step:" + step + "] [hp:" + player.getHp() + "] [maxhp:" + player.getMaxHP() + "] [" + player.getLogString() + "] [2]");
							if (player.getHp() * 3 <= player.getMaxHP()) {
								BuffTemplate tt = BuffTemplateManager.getInstance().getBuffTemplateByName(Translate.千层减伤);
								if (tt instanceof BuffTemplate_HuDunPercent == false) {
									log.debug("SkEnhanceManager.仙心雪葬checkskillbuff: buff match error{}", tt);
									return;
								}
								int num = 0;
								if (step == 1) {
									num = 5;
								} else if (step == 2) {
									num = 6;
								} else if (step == 3) {
									num = 7;
								}

								Buff buff = tt.createBuff(1);
								buff.setStartTime(SystemTime.currentTimeMillis());
								buff.setInvalidTime(buff.getStartTime() + 20000);
								buff.setCauser(player);
								buff.setDescription(Translate.抵抗攻击次数 + ":" + num);
								player.placeBuff(buff);
								player.skillTouchSpace.put(new Integer(805), new Long(System.currentTimeMillis()));
								if (Skill.logger.isInfoEnabled()) {
									Skill.logger.info("[仙心雪葬] [血低于百分三十触发buff] [test] [step:" + step + "] [hp:" + player.getHp() + "] [maxhp:" + player.getMaxHP() + "] [" + player.getLogString() + "] [2]");
								}
							}
						}
					}
				}
			} catch (Exception e) {
				Skill.logger.error("[仙心雪葬] [血低于百分三十触发buff] [异常] [" + player.getLogString() + "]", e);
			}
		}
	}

	public int checkdikangnum(Player player, ActiveSkill skill) {
		if (player == null || skill == null) {
			return 0;
		}
		if (player.getCareer() == 3) {
			try {
				int step = getSlotStep(player, 9);
				if (step > 0) {

					int nums = 0;
					if (step == 1) {
						nums = 6;
					} else if (step == 2) {
						nums = 7;
					} else if (step == 3) {
						nums = 8;
					}

					Buff b = player.getBuffByName(Translate.千层减伤);
					if (b != null) {
						Integer num = 技能抵抗次数.get(805+""+player.getId());
						if (num == null) {
							技能抵抗次数.put(805+""+player.getId(), new Integer(nums));
						}
						num = 技能抵抗次数.get(805+""+player.getId());
						int overnums = num.intValue() - 1;
						if (overnums <= 0) {
							player.removeBuff(b);
							overnums = 0;
						}
						技能抵抗次数.put(805+""+player.getId(), new Integer(overnums));
						log.warn("[抵抗攻击次数] [ok] [减少] [技能：" + skill.getName() + "] [nums:" + nums + "] [剩余：" + overnums + "] [player:" + player.getName() + "]");
						return overnums;
					} else {
						技能抵抗次数.put(805+""+player.getId(), new Integer(nums));
						log.warn("[抵抗攻击次数] [buff==null] [更新] [技能：" + skill.getName() + "] [nums:" + nums + "] [player:" + player.getName() + "]");
					}
				}
			} catch (Exception e) {
				Skill.logger.error("[仙心雪葬] [血低于百分三十触发buff] [异常] [" + player.getLogString() + "]", e);
			}
		}
		return 0;
	}

	/**
	 * 影魅 是否免疫减速
	 * @param casterP
	 * @return
	 */
	public boolean checkIsMinusSpeed(Player casterP) {
		if (casterP.getCareer() == 2) {
			int step = getSlotStep(casterP, 3);
			if (step > 0) {
				Buff b = casterP.getBuffByName(Translate.离火2);
				if (b != null && b.getInvalidTime() > System.currentTimeMillis()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获得使后定身状态下的目标受到的伤害提升10%持续5秒
	 * @param p
	 */
	public double check惊魂(Player p, Fighter tt) {
		if (p == null || tt == null) {
			return 0;
		}
		if (p.getCareer() != 4) {
			return 0;
		}
		int step = getSlotStep(p, 9);
		double values[] = { 0, 0.1, 0.15, 0.2 };

		return values[step];
	}

	// 九黎 409 妖印 人阶10重:获得法攻加成1%
	public int fixMagicAttack(Player player, int value) {
		if (player == null) {
			return 0;
		}
		if (player.getCareer() == 3) {
			int slotStep = getSlotStep(player, 0);
			if (slotStep <= 0) {
				return 0;
			}
			slotStep += 1;// 仙心 404 灵印 人阶10重激活永久提升内功+2%
			int ret = value * slotStep / 100;
			if (log.isDebugEnabled()) {
				log.debug("SkEnhanceManager.fixMagicAttack: 仙心 404 灵印 base {} rate {} add {}", new Object[] { value, slotStep, ret });
			}
			return ret;
		}
		if(player.getCareer() == 5){
			int setp = getSlotStep(player, 0);
			if(setp <= 0){
				return 0;
			}
			int ret = value * setp / 100;
			if (log.isDebugEnabled()) {
				log.debug("SkEnhanceManager.fixMagicAttack: 兽魁 8256 兽印 value {} setp {} add {}", new Object[] { value, setp, ret });
			}
			return ret;
		}
		if (player.getCareer() != 4) {
			return 0;
		}
		int step = getSlotStep(player, 3);
		if (step < 1) {
			return 0;
		}
		if (log.isDebugEnabled()) {
			log.debug("SkEnhanceManager.fixMagicAttack: 基数 {" + value + "}");// , new Exception());
		}
		if (value < 0) {
			return 0;
		}
		int ret = value * step / 100;
		if (log.isDebugEnabled()) {
			log.debug("SkEnhanceManager.fixMagicAttack: 九黎	409	妖印	人阶10重:获得法攻加成 {" + step + "}% = {" + ret + "}");// , new Exception());
		}
		return ret;
	}

	public int fixMagicDefane(Player player, int value) {
		if (player == null) {
			return 0;
		}
		if (player.getCareer() == 3) {
			return checkLingZun502(player, value);
		}
		if(player.getCareer() == 5){
			int step = getSlotStep(player, 3);
			if (step < 0) return 0;
			int vv = 0;
			switch (step) {
			case 1:
				vv = 1;
				break;
			case 2:
				vv = 2;
				break;
			case 3:
				vv = 3;
				break;
			}
			int ret = value * vv / 100;
			if (log.isDebugEnabled()) log.debug("兽魁钢胆提升{}%内防加成,base {} got {}", new Object[] { vv, value, ret });
			return ret;
		}
		if (player.getCareer() != 4) {
			return 0;
		}
		int step = getSlotStep(player, 0);
		if (step < 1) {
			return 0;
		}
		int add = 0;
		switch (step) {
		case 1:
			add = 3;
			break;
		case 2:

			add = 4;
			break;
		case 3:

			add = 5;
			break;

		default:
			break;
		}
		if (log.isDebugEnabled()) {
			log.debug("SkEnhanceManager.fixMagicAttack: 基数 {" + value + "}");// , new Exception());
		}
		if (value < 0) {
			return 0;
		}
		int ret = value * add / 100;
		if (log.isDebugEnabled()) {
			log.debug("SkEnhanceManager.fixMagicAttack: 九黎	408	骨印	人阶10重:获得法攻加成 {" + step + "}% = {" + ret + "}");// , new Exception());
		}
		return ret;
	}

	public int getTargetAdd(int id, Fighter owner, ActiveSkill as) {
		if (id == 603 || id == 21001 || id == 604 || id == 607// 影魅 607 鬼刺 人阶10重:获得鬼刺伤害目标+1
				|| id == 21004// 21004 星雷 人阶10重:获得星雷技能伤害目标+1
				|| id == 507// 507 万蛊 人阶10重:获得万蛊技能伤害目标+1
				|| id == 21003 // //仙心 21003 破浪 人阶10重:激活破浪伤害目标+1
				|| id == 21002 || id == 506 	// 修罗 603 杀阵 人阶10重:激活杀阵伤害目标+1
				|| id == 5022 || id == 5024 || id == 5023) {				
			// 修罗 21001 焚烬 人阶10重:获得焚烬伤害目标+1
			// 影魅 604 鬼幕 人阶10重:获得鬼幕伤害目标+1
			// 影魅 21002 飓风 人阶10重:获得飓风伤害目标+1
			// 兽魁 5022 裂空
			// 兽魁 5024 天罗
			//兽魁 5023 戮杀
			do {
				if (owner == null) {
					break;
				}
				;
				if (owner.getSpriteType() != Sprite.SPRITE_TYPE_PLAYER) {
					break;
				}
				Player p = (Player) owner;
				int step = SkEnhanceManager.getInst().getSlotStep(p, as.pageIndex);
				if (step > 0) {
					Skill.logger.debug("" + " 伤害目标增加  {} {}", as.getName(), step);
					return step;
				}
			} while (false);

		}
		return 0;
	}

	public void sendSkInfo(Player p) {
		if (p == null) {
			return;
		}
		Skill[] arr = CareerManager.getInstance().getCareers()[p.getCareer()].getCareerThread(0).skills;
		for (Skill sk : arr) {
			syncSkillInfo(sk.pageIndex, p);
		}
		log.debug("SkEnhanceManager.sendSkInfo: 总体发送完毕 {}", p.getName());
	}

	Map<Long, Long> skillCD = new HashMap<Long, Long>();
 	
	public void addSkillBuff(Player target){
		{// 修改普度大师技能，被定身或者眩晕后5秒内回复自身10%血量,人：5%血量 地：7%血量 天：10%血量
			if (target != null) {
				if (target.getCareer() == 1) {
					if (target.isHold() || target.isStun()) {
						int step = SkEnhanceManager.getInst().getSlotStep(((Player) target), 6);
						if (step > 0) {
							Long lastAddSkillBuffTime = skillCD.get(target.getId());
							if(lastAddSkillBuffTime == null){
								skillCD.put(target.getId(), 1L);
								lastAddSkillBuffTime = skillCD.get(target.getId());
							}
							if(System.currentTimeMillis() - lastAddSkillBuffTime > 10 * 1000){
								skillCD.put(target.getId(), System.currentTimeMillis());
								BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateById(1858);
								if (bt == null || !(bt instanceof BuffTemplate_JiaXue)) {
									Skill.logger.warn("[触发大师技能普度] [出错:没找到buff模板] [" + target.getName() + "]");
								} else {
									Buff b = bt.createBuff(step);
									b.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
									b.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 6000);
									target.placeBuff(b);
									if (Skill.logger.isWarnEnabled()) {
										Skill.logger.warn("[触发大师技能普度] [成功] [眩晕:"+target.isStun()+"] [定身:"+target.isHold()+"] [step:" + step + "] [GroupId:" + b.getGroupId() + "] [TemplateId:" + b.getTemplateId() + "] [buffLevel:" + b.getLevel() + "] [buff:" + b.getDescription() + "] [目标:" + target.getName() + "]");
									}
								}
	
							}
						}
					}
				}
			}
		}
	}
	
	public void syncSkillInfo(int index, Player p) {
		if (!syncSkInfo) {
			return;
		}
		if (p == null) {
			log.error("SkEnhanceManager.syncSkillInfo: 玩家是null");
			return;
		}
		Skill sk = CareerManager.getInstance().getCareers()[p.getCareer()].getCareerThread(0).getSkillByIndex(index);
		if (sk instanceof PassiveTriggerImmune) {		//被动技能不需要计算
			return;
		}
		if (sk == null) {
			log.error("SkEnhanceManager.syncSkillInfo:技能没有找到 {} {}", index, p.getName());
			return;
		}
		if (index == 0 && p.getCareer() == 3) {// 仙心灵印
			p.setMagicAttackA(p.getMagicAttackA());// 重新计算属性，以获得百分比加成。
		}
		if (index == 10 && p.getCareer() == 4) {// 九黎，怨毒。
			p.setMaxHPA(p.getMaxHPA());
		}
		
		
		long cdSub = calcCDSub(p, (ActiveSkill) sk);
		CareerManager cm = CareerManager.getInstance();
		SkillWithoutTraceAndWithMatrix[] matrixArr = new SkillWithoutTraceAndWithMatrix[0];
		SkillWithoutTraceAndWithRange[] rangeArr = new SkillWithoutTraceAndWithRange[0];
		SkillWithoutTraceAndOnTeamMember[] teamMemberArr = new SkillWithoutTraceAndOnTeamMember[0];
		SkillWithoutTraceAndWithTargetOrPosition[] targetOrPosArr = new SkillWithoutTraceAndWithTargetOrPosition[0];
		SkillWithoutTraceAndWithSummonNPC[] callNPCArr = new SkillWithoutTraceAndWithSummonNPC[0];
		SkillWithTraceAndDirectionOrTarget[] directionOrTarget = new SkillWithTraceAndDirectionOrTarget[0];
		SkillWithoutEffectAndQuickMove[] chongfen = new SkillWithoutEffectAndQuickMove[0];
		if (sk.getClass() == SkillWithoutTraceAndWithRange.class) {
			SkillWithoutTraceAndWithRange range = (SkillWithoutTraceAndWithRange) sk;
			int add = getTargetAdd(sk.getId(), p, range);
			if (add <= 0) {
				log.debug("SkEnhanceManager.syncSkillInfo: 目标不增A{}", sk.getName());
				return;
			}
			SkillWithoutTraceAndWithRange clone = range.clone();
			short[] arr = clone.getMaxAttackNums();
			short[] newArr = new short[arr.length];
			for (int i = 0; i < arr.length; i++) {
				newArr[i] = (short) (arr[i] + add);
			}
			clone.setMaxAttackNums(newArr);
			rangeArr = new SkillWithoutTraceAndWithRange[] { clone };
			if (rangeArr[0] == null) {
				return;
			}
		} else if (sk.getClass() == SkillWithoutTraceAndWithMatrix.class) {
			SkillWithoutTraceAndWithMatrix mt = (SkillWithoutTraceAndWithMatrix) sk;
			int add = getTargetAdd(sk.getId(), p, mt);
			if (add <= 0) {
				log.debug("SkEnhanceManager.syncSkillInfo: 目标不增B{}", sk.getName());
				return;
			}
			SkillWithoutTraceAndWithMatrix clone = mt.clone();
			short[] arr = clone.getMaxAttackNums();
			short[] newArr = new short[arr.length];
			for (int i = 0; i < arr.length; i++) {
				newArr[i] = (short) (arr[i] + add);
			}
			clone.setMaxAttackNums(newArr);
			matrixArr = new SkillWithoutTraceAndWithMatrix[] { clone };
			if (matrixArr[0] == null) return;
		} else if (sk.getClass() == SkillWithoutTraceAndWithTargetOrPosition.class) {
			SkillWithoutTraceAndWithTargetOrPosition mm = (SkillWithoutTraceAndWithTargetOrPosition) sk;
			if (cdSub > 0) {
				mm = mm.clone();
				mm.setDuration3(mm.getDuration3() - (int) cdSub);
				targetOrPosArr = new SkillWithoutTraceAndWithTargetOrPosition[] { mm };
				log.debug("SkEnhanceManager.syncSkillInfo: CD减少 {} {}", sk.getName(), cdSub);
			} else {
				return;
			}
		} else if (sk.getClass() == SkillWithoutTraceAndOnTeamMember.class) {
			// if(sk.getId() == 712){//获得每使用火月一次附带减少星移冷却时间4秒
			// if(cdSub>0){
			// SkillWithoutEffectAndQuickMove cf = (SkillWithoutEffectAndQuickMove) CareerManager.getInstance().getSkillById(301);
			// cf.setDuration3(cf.getDuration3() - (int)cdSub);
			// chongfen = new SkillWithoutEffectAndQuickMove[]{cf};
			// log.debug("[仙心火月减星移冷却] [cdSub:"+cdSub+"] [施法技能："+(sk==null?"": sk.getName())+"] [减少cd技能："+(cf==null?"": cf.getName())+"]");
			// }else{
			// return;
			// }
			// }else{
			SkillWithoutTraceAndOnTeamMember mm = (SkillWithoutTraceAndOnTeamMember) sk;
			List<SkillWithoutTraceAndOnTeamMember> ll = new ArrayList<SkillWithoutTraceAndOnTeamMember>();
			if (cdSub > 0) {
				mm = mm.clone();
				mm.setDuration3(mm.getDuration3() - (int) cdSub);
//				teamMemberArr = new SkillWithoutTraceAndOnTeamMember[] { mm };
//				teamMemberArr = ll.toArray(new SkillWithoutTraceAndOnTeamMember[]{});
				log.debug("SkEnhanceManager.syncSkillInfo: [技能免疫控制] CD减少 {} {}", sk.getName(), cdSub);
			} 
			if(sk.getId()==8212){
				//冰陵地阶以上免疫眩晕
				boolean canuse = false;
				boolean oldv = false;
				try{
					canuse = SkEnhanceManager.getInst().isCanUseSkill(p, sk.getId());
					if(canuse){
						oldv = mm.isIgnoreStun();
						mm.setIgnoreStun(canuse);
						p.isStunServer = true;
					}
					log.debug("[技能免疫控制] [ok] [是否免疫:"+canuse+"] [teamMemberArr:"+ll.size()+"] ["+oldv+"-->"+mm.isIgnoreStun()+"] ["+(p!=null?p.getName():"")+"] ["+(sk!=null?sk.getName():"")+"]");
				}catch(Exception e){
					log.debug("[技能免疫控制] [error] [是否免疫:"+canuse+"] [teamMemberArr:"+ll.size()+"] ["+(p!=null?p.getName():"")+"] ["+(p!=null?p.getId():"")+"] ["+(sk!=null?sk.getName():"")+"] ["+e+"]");
				}
			}
			ll.add(mm);
			if(ll.size()>0){
				teamMemberArr = ll.toArray(new SkillWithoutTraceAndOnTeamMember[]{});
			}
			else{
				return;
			}
			// }
		} else if (sk.getClass() == SkillWithoutTraceAndWithSummonNPC.class) {
			if (sk.getId() == 903) {
				long addT = SkEnhanceManager.getInst().getBuffAddTime(p, sk);
				if (addT > 0) {
					SkillWithoutTraceAndWithSummonNPC mm = (SkillWithoutTraceAndWithSummonNPC) sk;
					mm = mm.clone();
					mm.maxTimeLength += addT * 1000;
					callNPCArr = new SkillWithoutTraceAndWithSummonNPC[] { mm };
					log.debug("syncSkillInfo 903血祭人阶10重:获得血祭技能持续时间%d秒", addT);// 2,3,5 s
				}
			}
			if (callNPCArr.length == 0) {
				return;
			}
		}
		// else if(sk.getClass() == SkillWithTraceAndDirectionOrTarget.class){
		// if(cdSub>0){
		// SkillWithTraceAndDirectionOrTarget mm = (SkillWithTraceAndDirectionOrTarget) sk;
		// mm = mm.clone();
		// mm.setDuration3(mm.getDuration3() - (int)cdSub);
		// directionOrTarget = new SkillWithTraceAndDirectionOrTarget[]{mm};
		// log.debug("syncSkillInfo {} 冷却时间减少%d",sk.getName(), cdSub);// 2,3,5 s
		// //九黎 惊魂
		// }else{
		// return;
		// }
		// }
		else {
			if (log.isDebugEnabled()) {
				log.debug("SkEnhanceManager.syncSkillInfo: 不是群体技能");
			}
			return;
		}
		int ids[] = new int[Weapon.WEAPONTYPE_NAME.length];
		for (int i = 0; i < ids.length; i++) {
			CommonAttackSkill skill = cm.getCommonAttackSkill((byte) i);
			if (skill != null) {
				ids[i] = skill.getId();
			}
		}

		SEND_ACTIVESKILL_REQ req1 = new SEND_ACTIVESKILL_REQ(GameMessageFactory.nextSequnceNum(), ids, new CommonAttackSkill[0], new SkillWithoutEffect[0], chongfen, rangeArr, targetOrPosArr, callNPCArr, directionOrTarget, matrixArr, teamMemberArr);
		p.addMessageToRightBag(req1);
		if (log.isDebugEnabled()) {
			log.debug("SkEnhanceManager.syncSkillInfo: 发送成功 {} {}", p.getName(), sk.getName());
		}
	}

	/**
	 * 切换元神时，重新刷新技能信息（目的是改变大师技能产生的描述）。
	 * @param player
	 */
	public void syncSkillDesc(Player p) {
		if (p == null || p.getSkillOneLevels() == null) {// 后者防止第一次进入时报错。
			return;
		}
		SkBean bean = findSkBean(p);
		if (bean == null) {
			return;
		}
		Skill[] arr = CareerManager.getInstance().getCareers()[p.getCareer()].getCareerThread(0).skills;
		byte[] arrBase = bean.getLevels();
		byte[] arrSoul = bean.getSoulLevels();
		int i = 0;
		for (Skill sk : arr) {
			if (arrBase[i] != arrSoul[i]) {
				refreshClientSkInfo(p, sk);
				log.debug("大师技能描述刷新 {} {}", p.getName(), sk.getName());
			}
			i++;
		}
	}

	/**
	 * 使用技能不受眩晕，定身，封魔限制
	 * @param p
	 * @param skillId
	 * @return
	 */
	public boolean isCanUseSkill(Player p, int skillId) {
		log.warn("[isCanUseSkill] [skillId:"+skillId+"] [name:"+p.getName()+"] [career:"+p.getCareer()+"]");
		if (p == null || skillId == 0) {
			return false;
		}
		if (p.getCareer() == 1) {

		} else if (p.getCareer() == 2) {

		}
		if (p.getCareer() == 3) {
			if (skillId == 8212) { // 冰陵 地节10重 以上定身，眩晕，可以使用冰陵仅能
				int step = getSlotStep(p, 6);
				log.warn("[isCanUseSkill] [冰凌] [skillId:"+skillId+"] [step:"+step+"] [name:"+p.getName()+"] [career:"+p.getCareer()+"]");
				if (step >= 2) {
					return true;
				}
			}
		} else if (p.getCareer() == 4) {

		}

		return false;
	}

	// 攻击次数检查
	public void cheakAttNums(Player p, Fighter target, ActiveSkill skill) {
		if(log.isInfoEnabled()){
			log.info("[cheakAttNums] [name:" + p.getName() + "] [career:" + p.getCareer() + "] [target:" + (target == null ? "null" : target.getName()) + "] [skill:" + (skill == null ? "null" : skill.getName()) + "]");
		}
		SkEnhanceManager.getInst().checkWuHuang809(p, target, skill);
		SkEnhanceManager.getInst().checkWuHuang713(p, target, skill);
		SkEnhanceManager.getInst().checkWuHuang716(p, target, skill);
		SkEnhanceManager.getInst().checkWuHuang705(p, target, skill);
		SkEnhanceManager.getInst().checkShoukui18013(p, target, skill);
		if (skill.getId() == 704 && p != null) {
			SkEnhanceManager.getInst().checkLingZun704((Player) p, target);
		}
	}

	/**
	 * 返回技能点,总会返一次
	 * @throws Exception
	 */
	public void skillPonitReturn(Player p) {
		try {
			if (PlatformManager.getInstance().isPlatformOf(反大师技能点的平台)) {
				SkBean bean = SkEnhanceManager.getInst().findSkBean(p);
				if (bean != null) {
					if (bean.getReturnPointNum() == 0) {

						if (SkEnConf.conf == null) {
							log.error("SkEnhanceManager.skillPonitReturn: conf is null." + p.getLogString());
							return;
						}

						int allponits = 0; // 总的修炼值
						Soul ss[] = p.getSouls();
						if (ss != null) {
							for (int i = 0; i < ss.length; i++) {
								Soul soul = ss[i];
								if (soul != null) {
									int c = soul.getCareer() - 1;
									SkEnConf[] conf = SkEnConf.conf[c];
									byte[] useLevels = new byte[0];
									if (soul.getSoulType() == 0) {
										useLevels = bean.getLevels();
									} else {
										useLevels = bean.getSoulLevels();
									}
									if (useLevels != null) {
										for (int j = 0; j < 12; j++) {
											SkEnConf skC = conf[j];
											if (skC != null && useLevels[j] >= 1) {
												for (int k = 0; k < useLevels[j]; k++) {
													allponits += skC.levelNeedPoint[k];
												}
											}
										}
									}
								}
							}
						}

						if (allponits > 0) {
							TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(p.getId());
							if (entity != null) {
								bean.setPoint(bean.getPoint() + allponits);
								bean.setLevels(new byte[20]);
								bean.useLevels = new byte[20];
								bean.setSoulLevels(new byte[20]);
								Arrays.fill(bean.getLevels(), (byte) -1);
								Arrays.fill(bean.soulLevels, (byte) -1);
								Arrays.fill(bean.useLevels, (byte) -1);
								int dujielevel = entity.getCurrentLevel(); // 渡劫层数(1,3,5,6,7,8)影响大师技能的开启
								if (dujielevel == 2) {
									dujielevel = 1;
								} else if (dujielevel == 4) {
									dujielevel = 3;
								}

								for (int i = 1; i <= dujielevel; i++) {
									duJie(p.getId(), i);
								}

								bean.setReturnPointNum(1);
								MailManager.getInstance().sendMail(p.getId(), new ArticleEntity[] {}, Translate.大师技能修炼点返还, Translate.大师技能修炼点返还_, 0, 0, 0, "大师技能修炼点返还");
								Skill.logger.warn("[大师技能返点] [成功] [dujielevel:" + dujielevel + "] [返回总点数：" + allponits + "] [剩余点数：" + bean.getPoint() + "] [levels:" + (Arrays.toString(bean.getLevels())) + "] [soullevels:" + (Arrays.toString(bean.getSoulLevels())) + "] [" + p.getLogString() + "]");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Skill.logger.warn("[大师技能返点] [异常] [" + p.getLogString() + "] [" + e + "]");
			e.printStackTrace();
		}

	}

}
// 九黎的血祭技能加强，增加持续时间，属性是SkillWithoutTraceAndWithSummonNPC.maxTimeLength
// 修罗,Translate.影魅,Translate.仙心,Translate.九黎

