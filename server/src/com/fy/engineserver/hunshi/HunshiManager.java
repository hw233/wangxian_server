package com.fy.engineserver.hunshi;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.shop.ShopActivityManager;
import com.fy.engineserver.animation.AnimationManager;
import com.fy.engineserver.articleEnchant.ControlBuff;
import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.constants.Event;
import com.fy.engineserver.core.ParticleData;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.HunshiEntity;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.buff.Buff_ChenMo;
import com.fy.engineserver.datasource.buff.Buff_DingSheng;
import com.fy.engineserver.datasource.buff.Buff_JianShu;
import com.fy.engineserver.datasource.buff.Buff_XuanYun;
import com.fy.engineserver.datasource.buff.Buff_XuanYunAndWeak;
import com.fy.engineserver.datasource.buff.Buff_XuanYunLiuXue;
import com.fy.engineserver.datasource.buff.Buff_ZhuaRenAndXuanYun;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_UseCancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.hunshi.Option_hunshi_jianding;
import com.fy.engineserver.menu.hunshi.Option_hunshi_jianding2;
import com.fy.engineserver.menu.hunshi.Option_hunshi_merge_bind;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.HUNSHI_JIANDING2_REQ;
import com.fy.engineserver.message.HUNSHI_JIANDING2_RES;
import com.fy.engineserver.message.HUNSHI_JIANDING2_SURE_REQ;
import com.fy.engineserver.message.HUNSHI_JIANDING2_SURE_RES;
import com.fy.engineserver.message.HUNSHI_JIANDING_REQ;
import com.fy.engineserver.message.HUNSHI_JIANDING_RES;
import com.fy.engineserver.message.HUNSHI_JIANDING_SURE_REQ;
import com.fy.engineserver.message.HUNSHI_JIANDING_SURE_RES;
import com.fy.engineserver.message.HUNSHI_MERGE_AIM_REQ;
import com.fy.engineserver.message.HUNSHI_MERGE_AIM_RES;
import com.fy.engineserver.message.HUNSHI_MERGE_REQ;
import com.fy.engineserver.message.HUNSHI_MERGE_RES;
import com.fy.engineserver.message.HUNSHI_PUTON_REQ;
import com.fy.engineserver.message.HUNSHI_PUTON_RES;
import com.fy.engineserver.message.HUNSHI_SPECIAL_REQ;
import com.fy.engineserver.message.HUNSHI_SPECIAL_RES;
import com.fy.engineserver.message.NOTICE_CLIENT_PLAY_PARTICLE_RES;
import com.fy.engineserver.message.NOTIFY_EVENT_REQ;
import com.fy.engineserver.message.PLAY_ANIMATION_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.ExcelDataLoadUtil;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;

/**
 * 魂石
 * 
 * 
 */
public class HunshiManager {

	public static Logger logger = LoggerFactory.getLogger(HorseManager.class);

	public static HunshiManager instance;

	public static HunshiManager getInstance() {
		return instance;
	}

	public static int openLevel = 150;

	/**
	 * 0血量,1法力值,2物理攻击,3法术攻击,4物理防御,5法术防御,6闪躲,7免暴,8命中,9暴击,10精准,11破甲,12庚金攻击,13庚金抗性,14庚金减抗,15葵水攻击,16葵水抗性,17葵水减抗,18离火攻击,19离火抗性,20离火减抗,21乙木攻击,22乙木抗性,23乙木减抗,24融合值
	 * 数字为数组下标,按此顺序对应
	 */
	public static String[] propertyInfo = { "血量", "法力值", "物理攻击", "法术攻击", "物理防御", "法术防御", "闪躲", "免暴", "命中", "暴击", "精准", "破甲", "庚金攻击", "庚金抗性", "庚金减抗", "葵水攻击", "葵水抗性", "葵水减抗", "离火攻击", "离火抗性", "离火减抗", "乙木攻击", "乙木抗性", "乙木减抗", "附加融合率" };

	/**
	 * <魂石颜色，鉴定合成花费>
	 */
	public Map<Integer, HunshiJianDing> jianDingCost = new HashMap<Integer, HunshiJianDing>();
	/**
	 * <套装魂石颜色，鉴定合成花费>
	 */
	public Map<Integer, HunshiJianDing> jianDingCost2 = new HashMap<Integer, HunshiJianDing>();
	/**
	 * <魂石镶嵌面板的孔index，开启需要条件>
	 */
	public Map<Integer, HunshiXiangQian> openHole = new HashMap<Integer, HunshiXiangQian>();
	/**
	 * <套装魂石镶嵌面板的孔index，开启需要条件>
	 */
	public Map<Integer, HunshiXiangQian> openHole2 = new HashMap<Integer, HunshiXiangQian>();
	/**
	 * <魂石或套装魂石统计名，主副属性随机库>
	 */
	public Map<String, HunshiPropId> hunshiPropIdMap = new HashMap<String, HunshiPropId>();
	/**
	 * 魂石主属性随机区间
	 * Map<属性id,HunshiPropValue>
	 */
	public Map<Integer, HunshiPropValue> hunshiMainValueMap = new HashMap<Integer, HunshiPropValue>();
	// public Map<Integer, Map<Integer, String>> hunshiMainPropMap = new HashMap<Integer, Map<Integer, String>>();
	/**
	 * 套装魂石主属性随机区间
	 * Map<属性id,HunshiPropValue>>
	 */
	public Map<Integer, HunshiPropValue> hunshi2MainValueMap = new HashMap<Integer, HunshiPropValue>();
	// public Map<Integer, Map<Integer, String>> hunshi2MainPropMap = new HashMap<Integer, Map<Integer, String>>();
	/**
	 * 魂石附加属性随机区间
	 * Map<属性id,Map<魂石颜色,属性值区间（“-”间隔）>>
	 */
	public Map<Integer, HunshiPropValue> hunshiExtraValueMap = new HashMap<Integer, HunshiPropValue>();
	// public Map<Integer, Map<Integer, String>> hunshiPropMap = new HashMap<Integer, Map<Integer, String>>();
	/**
	 * 套装魂石附加属性随机区间
	 * Map<属性id,Map<套装魂石颜色,属性值区间（“-”间隔）>>
	 */
	public Map<Integer, HunshiPropValue> hunshi2ExtraValueMap = new HashMap<Integer, HunshiPropValue>();
	// public Map<Integer, Map<Integer, String>> hunshi2PropMap = new HashMap<Integer, Map<Integer, String>>();

	/**
	 * 套装Map
	 * Map<套装id, HunshiSuit>
	 */
	// public Map<Integer, HunshiSuit> hunshiSuitMap = new HashMap<Integer, HunshiSuit>();
	public Map<String, HunshiSuit> tempMap = new HashMap<String, HunshiSuit>();

	/**
	 * 套装魂石分类
	 * Map<套装名,List<Hunshi2Material>>
	 */
	public Map<String, List<Hunshi2Material>> hunshi2KindMap = new LinkedHashMap<String, List<Hunshi2Material>>();
	/**
	 * 套装技能
	 * Map<技能id, SuitSkill>
	 */
	public Map<Integer, SuitSkill> suitSkillMap = new HashMap<Integer, SuitSkill>();
	/**
	 * 套装技能分类
	 * Map<分类id, SuitSkillType>
	 */
	public Map<Integer, SuitSkillGroup> suitSkillGroupMap = new HashMap<Integer, SuitSkillGroup>();
	/**
	 * 套装技能分类
	 * Map<统计名id, 魂石套装石统计名>
	 */
	public Map<Integer, String> cnNameIdMap = new HashMap<Integer, String>();
	/** 以下两个int数组对应，上面是绿色魂石合成的蓝色魂石的魂石id，下面是概率 */
	public int[] hunshiId = new int[16];
	public int[] hunshiWithHoleProp = new int[16];

	Random random = new Random();
	public String[] 魂石鉴定符 = new String[] { Translate.魂识符 };
	public String 魂石洗炼符 = Translate.灵隐符;
	ArticleEntityManager aem = ArticleEntityManager.getInstance();
	private String filePath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public static void main(String[] args) {
		HunshiManager hm = new HunshiManager();
		hm.setFilePath("E:\\code\\game_mieshi_server\\conf\\game_init_config\\horse2\\hunshi.xls");
		try {
			hm.loadFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void init() throws Exception {
		
		instance = this;
		loadFile();
		ServiceStartRecord.startLog(this);
	}

	@SuppressWarnings("unchecked")
	public void loadFile() throws Exception {
		File file = new File(getFilePath());
		// file = new File(ConfigServiceManager.getInstance().getFilePath(file));
		if (!file.exists()) {
			throw new Exception("配置文件不存在");
		}
		try {
			jianDingCost = (Map<Integer, HunshiJianDing>) ExcelDataLoadUtil.loadExcelData(file, 0, HunshiJianDing.class, logger);
			if (logger.isWarnEnabled()) logger.warn("[加载坐骑魂石] [sheet0] [数据条数:" + jianDingCost.size() + "]");
			jianDingCost2 = (Map<Integer, HunshiJianDing>) ExcelDataLoadUtil.loadExcelData(file, 1, HunshiJianDing.class, logger);
			if (logger.isWarnEnabled()) logger.warn("[加载坐骑魂石] [sheet1] [数据条数:" + jianDingCost2.size() + "]");
			openHole = (Map<Integer, HunshiXiangQian>) ExcelDataLoadUtil.loadExcelData(file, 2, HunshiXiangQian.class, logger);
			if (logger.isWarnEnabled()) logger.warn("[加载坐骑魂石] [sheet2] [数据条数:" + openHole.size() + "]");
			openHole2 = (Map<Integer, HunshiXiangQian>) ExcelDataLoadUtil.loadExcelData(file, 3, HunshiXiangQian.class, logger);
			if (logger.isWarnEnabled()) logger.warn("[加载坐骑魂石] [sheet3] [数据条数:" + openHole2.size() + "]");
			hunshiPropIdMap = (Map<String, HunshiPropId>) ExcelDataLoadUtil.loadExcelData(file, 4, HunshiPropId.class, logger);
			if (logger.isWarnEnabled()) logger.warn("[加载坐骑魂石] [sheet4] [数据条数:" + hunshiPropIdMap.size() + "]");
			hunshiMainValueMap = (Map<Integer, HunshiPropValue>) ExcelDataLoadUtil.loadExcelData(file, 5, HunshiPropValue.class, logger);
			if (logger.isWarnEnabled()) logger.warn("[加载坐骑魂石] [sheet5] [数据条数:" + hunshiMainValueMap.size() + "]");
			hunshi2MainValueMap = (Map<Integer, HunshiPropValue>) ExcelDataLoadUtil.loadExcelData(file, 6, HunshiPropValue.class, logger);
			if (logger.isWarnEnabled()) logger.warn("[加载坐骑魂石] [sheet6] [数据条数:" + hunshi2MainValueMap.size() + "]");
			hunshiExtraValueMap = (Map<Integer, HunshiPropValue>) ExcelDataLoadUtil.loadExcelData(file, 7, HunshiPropValue.class, logger);
			if (logger.isWarnEnabled()) logger.warn("[加载坐骑魂石] [sheet7] [数据条数:" + hunshiExtraValueMap.size() + "]");
			hunshi2ExtraValueMap = (Map<Integer, HunshiPropValue>) ExcelDataLoadUtil.loadExcelData(file, 8, HunshiPropValue.class, logger);
			if (logger.isWarnEnabled()) logger.warn("[加载坐骑魂石] [sheet8] [数据条数:" + hunshi2ExtraValueMap.size() + "]");
			// hunshiSuitMap = (Map<Integer, HunshiSuit>) ExcelDataLoadUtil.loadExcelData(file, 9, HunshiSuit.class, logger);
			List<HunshiSuit> temp = (List<HunshiSuit>) ExcelDataLoadUtil.loadExcelDataAsList(file, 9, HunshiSuit.class, logger);
			for (HunshiSuit s : temp) {
				for (String str : s.getHunshi2CNNames()) {
					tempMap.put(str + "_" + s.getSuitColor(), s);
				}
			}
			if (logger.isWarnEnabled()) logger.warn("[加载坐骑魂石] [sheet9] [数据条数:" + tempMap.size() + "]");
			suitSkillMap = (Map<Integer, SuitSkill>) ExcelDataLoadUtil.loadExcelData(file, 11, SuitSkill.class, logger);
			for (Integer id : suitSkillMap.keySet()) {
				int index = suitSkillMap.get(id).getType();
				SkillType st = getSkillType(index);
				suitSkillMap.get(id).setSkillType(st);
			}
			if (logger.isWarnEnabled()) logger.warn("[加载坐骑魂石] [sheet11] [数据条数:" + suitSkillMap.size() + "]");
			suitSkillGroupMap = (Map<Integer, SuitSkillGroup>) ExcelDataLoadUtil.loadExcelData(file, 12, SuitSkillGroup.class, logger);
			if (logger.isWarnEnabled()) logger.warn("[加载坐骑魂石] [sheet12] [数据条数:" + suitSkillGroupMap.size() + "]");
			HSSFWorkbook hssfWorkbook = null;
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			hssfWorkbook = new HSSFWorkbook(pss);
			HSSFSheet sheet = null;
			// 第14页要在第10页之前加载
			sheet = hssfWorkbook.getSheetAt(14);
			if (sheet == null) return;
			int rows = sheet.getPhysicalNumberOfRows();
			for (int i = 2; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					int id = (int) cell.getNumericCellValue();
					String materialCNName = row.getCell(index++).getStringCellValue();
					cnNameIdMap.put(id, materialCNName);
				}
			}
			if (logger.isWarnEnabled()) logger.warn("[加载坐骑魂石] [sheet15] [数据条数:" + cnNameIdMap.size() + "]");
			// 属性值区间
			sheet = hssfWorkbook.getSheetAt(10);
			if (sheet == null) return;
			rows = sheet.getPhysicalNumberOfRows();
			String name = null;
			String cnName = null;
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					if (cell != null) {
						name = cell.getStringCellValue();
						cnName = row.getCell(index++).getStringCellValue();
					} else {
						index = 2;
					}
					String targetCNName = row.getCell(index++).getStringCellValue();
					int color = (int) row.getCell(index++).getNumericCellValue();
					Integer[] materialCNId = StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ";", Integer.class);
					int[] materialCNIds = new int[materialCNId.length];
					for (int j = 0; j < materialCNId.length; j++) {
						if (materialCNId[j] != null) {
							materialCNIds[j] = materialCNId[j].intValue();
						}
					}
					Integer[] materialColor = StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ";", Integer.class);
					int[] materialColors = new int[materialColor.length];
					for (int j = 0; j < materialColor.length; j++) {
						if (materialColor[j] != null) {
							materialColors[j] = materialColor[j].intValue();
						}
					}
					if (materialCNId.length != materialColor.length) {
						throw new Exception("hunshi.xls物品和颜色数组长度不一致,Sheet10,行标：" + (i + 1));
					}
					long mergeCost = jianDingCost2.get(color).getMergeCost();
					Article a = ArticleManager.getInstance().getArticleByCNname(targetCNName);
					if (a == null) {
						throw new Exception("hunshi.xls物品Article不存在,统计名:" + targetCNName);
					}
					ArticleEntity tempAe = aem.createTempEntity(a, false, aem.坐骑魂石, null, color);
					if (tempAe == null) {
						throw new Exception("hunshi.xls物品ArticleEntity不存在,统计名:" + targetCNName);
					}
					long[] materialTempIds = new long[materialCNIds.length];
					for (int j = 0; j < materialCNIds.length; j++) {
						Article aa = ArticleManager.getInstance().getArticleByCNname(cnNameIdMap.get(materialCNIds[j]));
						if (aa == null) {
							throw new Exception("hunshi.xls物品Article不存在,统计名:" + cnNameIdMap.get(materialCNIds[j]));
						}
						ArticleEntity tempAee = aem.createTempEntity(aa, false, aem.坐骑魂石, null, materialColors[j]);
						if (tempAee == null) {
							throw new Exception("hunshi.xls物品ArticleEntity不存在,统计名:" + cnNameIdMap.get(materialCNIds[j]));
						}
						materialTempIds[j] = tempAee.getId();
					}
					Hunshi2Material material = new Hunshi2Material(targetCNName, color, tempAe.getId(), materialCNIds, materialColors, materialTempIds, mergeCost);
					if (!hunshi2KindMap.containsKey(name)) {
						hunshi2KindMap.put(name, new ArrayList<Hunshi2Material>());
					}
					List<Hunshi2Material> materialList = hunshi2KindMap.get(name);
					materialList.add(material);
					hunshi2KindMap.put(name, materialList);
				}

			}
			if (logger.isWarnEnabled()) logger.warn("[加载坐骑魂石] [sheet10] [数据条数:" + hunshi2KindMap.size() + "]");
			sheet = hssfWorkbook.getSheetAt(13);
			if (sheet == null) return;
			rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					hunshiId[i - 1] = (int) row.getCell(0).getNumericCellValue();
					hunshiWithHoleProp[i - 1] = (int) row.getCell(1).getNumericCellValue();
				}
			}
			if (logger.isWarnEnabled()) logger.warn("[加载坐骑魂石] [sheet13] [数据条数:" + hunshi2KindMap.size() + "]");
		} catch (Exception e) {
			if (logger.isWarnEnabled()) logger.warn("[加载坐骑魂石异常]", e);
			e.printStackTrace();
		}
	}

	/**
	 * 获取技能类型
	 * @param index
	 * @return
	 */
	public SkillType getSkillType(int index) {
		for (SkillType type : SkillType.values()) {
			if (index == type.getIndex()) {
				return type;
			}
		}
		return null;
	}

	/**
	 * 魂石鉴定请求
	 * @param he
	 * @param p
	 */
	public void jianDingQuery(Player p, HUNSHI_JIANDING_SURE_REQ req) {
		if (p != null && req != null && aem != null) {
			long hunshiId = req.getHunshiId();
			long materialIds[] = req.getMaterialIds();
			int materialNums[] = req.getMaterialNums();
			int useSilver = req.getUsesilver();
			// 魂石是否存在
			ArticleEntity ae = aem.getEntity(hunshiId);
			if (ae == null) {
				p.sendError(Translate.translateString(Translate.魂石鉴定需要放入魂石和材料, new String[][] { { Translate.STRING_1, Arrays.toString(魂石鉴定符) } }));
				if (logger.isWarnEnabled()) logger.warn("[魂石鉴定请求] [失败] [魂石不存在] [" + p.getLogString() + "] [hunshiId:" + req.getHunshiId() + "]");
				return;
			}
			if (ae instanceof HunshiEntity) {
				HunshiEntity he = (HunshiEntity) ae;
				if (he.isJianding()) {
					p.sendError(Translate.魂石已鉴定过);
					return;
				}
				if (!ArticleProtectManager.instance.isCanDo(p, ArticleProtectDataValues.ArticleProtect_High, ae.getId())) {
					p.sendError(Translate.高级锁魂物品不能做此操作);
					return;
				}

				if (useSilver == 0) { // 材料鉴定
					// 材料是否存在
					if (materialIds != null && materialIds.length > 0) {
						for (int i = 0; i < materialIds.length; i++) {
							ArticleEntity materialEntity = aem.getEntity(materialIds[i]);
							if (materialEntity == null) {
								p.sendError(Translate.translateString(Translate.魂石鉴定需要放入魂石和材料, new String[][] { { Translate.STRING_1, Arrays.toString(魂石鉴定符) } }));
								if (logger.isWarnEnabled()) logger.warn("[魂石鉴定请求] [失败] [材料不存在] [" + p.getLogString() + "] [hunshiId:" + req.getHunshiId() + "] [cailiaoId:" + materialIds[i] + "]");
								return;
							}
						}
						// 如果魂石为非绑而材料有绑定，弹框提示玩家是否鉴定
						if (!he.isBinded()) {
							for (long id : materialIds) {
								ArticleEntity materialEntity = aem.getEntity(id);
								if (materialEntity.isBinded()) {
									WindowManager windowManager = WindowManager.getInstance();
									MenuWindow mw = windowManager.createTempMenuWindow(600);
									mw.setTitle(Translate.translateString(Translate.确定要开始鉴定吗绑定的, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() }, { Translate.ARTICLE_NAME_2, he.getArticleName() } }));
									mw.setDescriptionInUUB(Translate.translateString(Translate.确定要开始鉴定吗绑定的, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() }, { Translate.ARTICLE_NAME_2, he.getArticleName() } }));
									Option_hunshi_jianding option = new Option_hunshi_jianding(he, materialIds);
									option.setText(Translate.确定);
									Option_UseCancel cancel = new Option_UseCancel();
									cancel.setText(Translate.取消);
									mw.setOptions(new Option[] { option, cancel });
									p.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions()));
									return;
								}
							}
						}
						jianDingConfirm(p, he, materialIds, false, useSilver);
					} else {
						p.sendError(Translate.translateString(Translate.魂石鉴定需要放入魂石和材料, new String[][] { { Translate.STRING_1, Arrays.toString(魂石鉴定符) } }));
						if (logger.isWarnEnabled()) logger.warn("[魂石鉴定请求] [失败] [未放入材料] [" + p.getLogString() + "] [hunshiId:" + req.getHunshiId() + "]");
						return;
					}
				} else if (useSilver == 1) { // 银子鉴定
					long cost = getJianDingCost(p, he);
					if (cost > p.getSilver()) {
						p.sendError(Translate.余额不足);
						return;
					}
					jianDingConfirm(p, he, materialIds, false, useSilver);
				}

			} else {
				p.sendError(Translate.translateString(Translate.查询魂石鉴定请求错误回复, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.空白) } }));
				if (logger.isWarnEnabled()) logger.warn("[魂石鉴定请求] [失败] [不是魂石] [" + p.getLogString() + "] [hunshiId:" + req.getHunshiId() + "]");
				return;
			}
		}

	}

	/**
	 * 魂石神隐洗炼请求
	 * @param p
	 * @param req
	 */
	public void jianDingQuery2(Player p, HUNSHI_JIANDING2_SURE_REQ req) {
		if (p != null && req != null && aem != null) {
			long hunshiId = req.getHunshiId();
			long materialId = req.getMaterialId();
			// 魂石是否存在
			ArticleEntity ae = aem.getEntity(hunshiId);
			if (ae == null) {
				p.sendError(Translate.translateString(Translate.魂石鉴定需要放入魂石和材料, new String[][] { { Translate.STRING_1, 魂石洗炼符 } }));
				if (logger.isWarnEnabled()) logger.warn("[魂石洗炼请求] [失败] [魂石不存在] [" + p.getLogString() + "] [hunshiId:" + req.getHunshiId() + "]");
				return;
			}
			if (ae instanceof HunshiEntity) {
				HunshiEntity he = (HunshiEntity) ae;
				if (he.getExtraPropValue()[he.getExtraPropValue().length - 1] > 0) {
					p.sendError(Translate.魂石已洗炼过);
					return;
				}
				if (!ArticleProtectManager.instance.isCanDo(p, ArticleProtectDataValues.ArticleProtect_High, ae.getId())) {
					p.sendError(Translate.高级锁魂物品不能做此操作);
					return;
				}
				// 材料是否存在
				if (materialId > 0) {
					ArticleEntity materialEntity = aem.getEntity(materialId);
					if (materialEntity == null) {
						p.sendError(Translate.translateString(Translate.魂石鉴定需要放入魂石和材料, new String[][] { { Translate.STRING_1, 魂石洗炼符 } }));
						if (logger.isWarnEnabled()) logger.warn("[魂石洗炼请求] [失败] [材料不存在] [" + p.getLogString() + "] [hunshiId:" + req.getHunshiId() + "] [cailiaoId:" + materialId + "]");
						return;
					}

					// 如果魂石为非绑而材料有绑定，弹框提示玩家是否洗炼
					if (!he.isBinded()) {
						if (materialEntity.isBinded()) {
							WindowManager windowManager = WindowManager.getInstance();
							MenuWindow mw = windowManager.createTempMenuWindow(600);
							mw.setTitle(Translate.translateString(Translate.确定要开始洗炼吗绑定的, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() }, { Translate.ARTICLE_NAME_2, he.getArticleName() } }));
							mw.setDescriptionInUUB(Translate.translateString(Translate.确定要开始洗炼吗绑定的, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() }, { Translate.ARTICLE_NAME_2, he.getArticleName() } }));
							Option_hunshi_jianding2 option = new Option_hunshi_jianding2(he, materialId);
							option.setText(Translate.确定);
							Option_UseCancel cancel = new Option_UseCancel();
							cancel.setText(Translate.取消);
							mw.setOptions(new Option[] { option, cancel });
							p.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions()));
							return;
						}

					}
					jianDingConfirm2(p, he, materialId, false);
				} else {
					p.sendError(Translate.translateString(Translate.魂石鉴定需要放入魂石和材料, new String[][] { { Translate.STRING_1, 魂石洗炼符 } }));
					if (logger.isWarnEnabled()) logger.warn("[魂石洗炼请求] [失败] [未放入材料] [" + p.getLogString() + "] [hunshiId:" + req.getHunshiId() + "]");
					return;
				}

			} else {
				p.sendError(Translate.translateString(Translate.查询魂石洗炼请求错误回复, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.空白) } }));
				if (logger.isWarnEnabled()) logger.warn("[魂石洗炼请求] [失败] [不是魂石] [" + p.getLogString() + "] [hunshiId:" + req.getHunshiId() + "]");
				return;
			}
		}

	}

	/**
	 * 魂石鉴定确认
	 * @param p
	 * @param he
	 * @param materialIds
	 * @param materialNums
	 * @param bind
	 *            true-设置为绑定;false-绑定状态维持原状
	 * @param jiandingType
	 *            0-材料鉴定;1-银子鉴定
	 */
	public void jianDingConfirm(Player p, HunshiEntity he, long[] materialIds, boolean bind, int jiandingType) {
		if (logger.isDebugEnabled()) logger.debug("[魂石鉴定] [测试] [进入方法] [" + p.getLogString() + "] [hunshiId:" + he.getId() + "] [hunshiName:" + he.getArticleName() + "] [jiandingType:" + jiandingType + "]");
		int[] materialNums = null;
		if (he.getType() == 0) {
			materialNums = jianDingCost.get(he.getColorType()).getMaterialNums();
		} else if (he.getType() == 1) {
			materialNums = jianDingCost2.get(he.getColorType()).getMaterialNums();
		}
		if (jiandingType == 1) {
			long cost = getJianDingCost(p, he);
			try {
				if (logger.isDebugEnabled()) logger.debug("[魂石鉴定] [测试] [银子鉴定] [" + p.getLogString() + "] [hunshiId:" + he.getId() + "] [hunshiName:" + he.getArticleName() + "] [jiandingType:" + jiandingType + "]");
				if (cost <= p.getSilver()) {
					BillingCenter.getInstance().playerExpense(p, cost, CurrencyType.YINZI, ExpenseReasonType.魂石鉴定, "魂石鉴定");
				} else {
					p.sendError(Translate.余额不足);
					return;
				}
			} catch (NoEnoughMoneyException e) {
				e.printStackTrace();
			} catch (BillFailedException e) {
				e.printStackTrace();
			}
		} else if (jiandingType == 0) {
			try {
				if (logger.isDebugEnabled()) logger.debug("[魂石鉴定] [测试] [材料鉴定] [" + p.getLogString() + "] [hunshiId:" + he.getId() + "] [hunshiName:" + he.getArticleName() + "] [jiandingType:" + jiandingType + "] [materialIds:" + Arrays.toString(materialIds) + "] [materialNums:" + Arrays.toString(materialNums) + "]");
				// 判断材料够不
				int materialNum = 0;
				for (int i = 0; i < materialIds.length; i++) {
					long articleId = materialIds[i];
					Knapsack knapsack = p.getKnapsack_common();
					if (knapsack != null) {
						int num = knapsack.countArticle(articleId);
						materialNum += num;
					}
				}
				int articleNum = materialNums[0]; // !!!协议设计不合理，这里没传id对应的物品数量，要从背包取，出现相同名字不同id的物品时，数量数组不对应，因为只有一种材料，就暂时取最前面的数量了，如要扩展，需修改这里。
				if (materialNum < articleNum) {
					p.sendError(Translate.text_299); // 材料不足
					return;
				}
				// 扣除鉴定材料
				// p.getKnapsack_common().removeArticleEntity(p, materialIds, materialNums, "魂石鉴定");
				int count = 0;
				for (int i = 0; i < materialIds.length; i++) {
					long articleId = materialIds[i];
					if (logger.isWarnEnabled()) logger.warn("[魂石鉴定] [材料] [" + p.getLogString() + "] [hunshiId:" + he.getId() + "] [hunshiName:" + he.getArticleName() + "] [articleId:" + articleId + "] [articleNum:" + articleNum + "]");
					Knapsack knapsack = p.getKnapsack_common();
					int hasNum = knapsack.countArticle(articleId);
					int needNum = 0;
					if (hasNum > articleNum - count) {
						needNum = articleNum - count;
					} else {
						needNum = hasNum;
					}
					for (int n = 0; n < needNum; n++) {
						ArticleEntity aee = p.removeArticleEntityFromKnapsackByArticleId(articleId, "魂石鉴定", true);
						if (logger.isWarnEnabled()) logger.warn("[魂石鉴定] [已扣除材料] [" + p.getLogString() + "] [hunshiId:" + he.getId() + "] [hunshiName:" + he.getArticleName() + "] [articleId:" + articleId + "]");
						if (aee == null) {
							String description = Translate.删除物品不成功;
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
							p.addMessageToRightBag(hreq);
							if (logger.isWarnEnabled()) logger.warn("[魂石鉴定] [失败] [" + p.getLogString() + "] [hunshiId:" + he.getId() + "] [hunshiName:" + he.getArticleName() + "] [不存在的物品id:" + articleId + "] [" + description + "]");
							return;
						}
						count++;
						// 统计
						ArticleStatManager.addToArticleStat(p, null, aee, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "魂石鉴定", null);
					}
				}
			} catch (Exception e) {
				if (logger.isWarnEnabled()) logger.warn("[魂石鉴定] [扣除材料异常] [" + p.getLogString() + "] [hunshiId:" + he.getId() + "] [hunshiName:" + he.getArticleName() + "]", e);
				e.printStackTrace();
				return;
			}
		} else {
			if (logger.isWarnEnabled()) logger.warn("[魂石鉴定] [类型错误] [" + p.getLogString() + "] [hunshiId:" + he.getId() + "] [hunshiName:" + he.getArticleName() + "] [jiandingType:" + jiandingType + "]");
			return;
		}
		Article a = ArticleManager.getInstance().getArticle(he.getArticleName());
		if (a != null) {
			if (logger.isDebugEnabled()) logger.debug("[魂石鉴定] [测试] [属性处理] [" + p.getLogString() + "] [hunshiId:" + he.getId() + "] [hunshiName:" + he.getArticleName() + "] [jiandingType:" + jiandingType + "]");
			try {
				int[] mainPropValue = he.getMainPropValue();
				int[] extraPropValue = he.getExtraPropValue();
				// 获取主属性
				if (hunshiPropIdMap.containsKey(a.getName_stat())) {
					int[] mainIds = hunshiPropIdMap.get(a.getName_stat()).getMainPropId();
					if (mainIds != null && mainIds.length > 0) {
						for (int index = 0; index < mainIds.length; index++) {
							int mainPropId = mainIds[index];
							if (mainPropId >= 0) {
								int mainProp = getRandomPropValue(hunshiMainValueMap, mainPropId, he, 0);
								if (mainProp > 0) {
									if (logger.isDebugEnabled()) logger.debug("[魂石鉴定] [测试] [主属性处理] [属性id:" + mainPropId + "] [属性值：" + mainProp + "] [" + p.getLogString() + "] [hunshiId:" + he.getId() + "] [hunshiName:" + he.getArticleName() + "] [jiandingType:" + jiandingType + "]");
									mainPropValue[mainPropId] = mainProp;
								}
							}
						}
					}
				}
				he.setMainPropValue(mainPropValue);
				// 获取副属性和融合值
				int 副属性条数 = 0;
				switch (he.getType()) {
				case 0:
					if (he.getColorType() == 0) {
						副属性条数 = 1;
					} else if (he.getColorType() == 1) {
						副属性条数 = 2;
					} else {
						副属性条数 = 3;
					}
					for (int i = 0; i < 副属性条数; i++) {
						boolean goon = true;
						while (goon) {
							if (hunshiPropIdMap.containsKey(a.getName_stat())) {
								int[] extraPropIds = hunshiPropIdMap.get(a.getName_stat()).getExtraPropId();
								if (extraPropIds != null && extraPropIds.length - 1 > 0) {
									int index = random.nextInt(extraPropIds.length);
									int propId = extraPropIds[index];
									if (extraPropValue[propId] == 0) {
										goon = false;// 找到新属性就跳出while
										int propValue = getRandomPropValue(hunshiExtraValueMap, propId, he, 1);
										extraPropValue[propId] = propValue;
										if (logger.isWarnEnabled()) logger.warn("[魂石鉴定] [测试] [副属性处理] [属性id:" + propId + "] [属性值：" + propValue + "] [" + p.getLogString() + "] [hunshiId:" + he.getId() + "] [hunshiName:" + he.getArticleName() + "] [jiandingType:" + jiandingType + "]");
									}
								}
							}
						}
					}
					he.setExtraPropValue(extraPropValue);
					int ronghezhi = getRandomRonghezhi(jianDingCost.get(he.getColorType()));
					if (ronghezhi > 0) {
						he.setRongHeZhi(ronghezhi);
					}
					break;
				case 1:
					for (int i = 0; i < 3; i++) {
						boolean goon = true;
						while (goon) {
							if (hunshiPropIdMap.containsKey(a.getName_stat())) {
								int[] extraPropIds = hunshiPropIdMap.get(a.getName_stat()).getExtraPropId();
								if (extraPropIds != null && extraPropIds.length - 1 > 0) {
									int index = random.nextInt(extraPropIds.length);
									int propId = extraPropIds[index];
									if (extraPropValue[propId] == 0) {
										goon = false;// 找到新属性就跳出while
										int propValue = getRandomPropValue(hunshi2ExtraValueMap, propId, he, 1);
										extraPropValue[propId] = propValue;
										if (logger.isWarnEnabled()) logger.warn("[魂石鉴定] [测试] [副属性处理] [属性id:" + propId + "] [属性值：" + propValue + "] [" + p.getLogString() + "] [hunshiId:" + he.getId() + "] [hunshiName:" + he.getArticleName() + "] [jiandingType:" + jiandingType + "]");
									}
								}
							}
						}
					}
					he.setExtraPropValue(extraPropValue);
					int ronghezhi2 = getRandomRonghezhi(jianDingCost2.get(he.getColorType()));
					if (ronghezhi2 > 0) {
						he.setRongHeZhi(ronghezhi2);
					}
					break;
				default:
					break;
				}
			} catch (Exception e) {
				if (logger.isWarnEnabled()) logger.warn("[魂石鉴定] [属性处理异常] [" + p.getLogString() + "] [hunshiId:" + he.getId() + "] [hunshiName:" + he.getArticleName() + "]", e);
				e.printStackTrace();
			}
			he.setJianding(true);
			if (bind) {
				he.setBinded(true);
			}
			PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备鉴定成功, (byte) 1, AnimationManager.动画播放位置类型_装备鉴定, 0, 0);
			if (pareq != null) {
				p.addMessageToRightBag(pareq);
			}
			p.sendError(Translate.鉴定成功);
			if (logger.isWarnEnabled()) logger.warn("[魂石鉴定] [完成] [" + p.getLogString() + "] [" + he.getId() + "] [" + he.getArticleName() + "]");
			p.addMessageToRightBag(new HUNSHI_JIANDING_SURE_RES(GameMessageFactory.nextSequnceNum(), true));
		}
	}

	/**
	 * 魂石神隐洗练
	 * @param p
	 * @param he
	 */
	public void jianDingConfirm2(Player p, HunshiEntity he, long materialId, boolean bind) {
		if (logger.isDebugEnabled()) logger.debug("[魂石洗练] [测试] [进入方法] [" + p.getLogString() + "] [hunshiId:" + he.getId() + "] [hunshiName:" + he.getArticleName() + "]");
		try {
			if (logger.isDebugEnabled()) logger.debug("[魂石洗练] [测试] [材料洗练] [" + p.getLogString() + "] [hunshiId:" + he.getId() + "] [hunshiName:" + he.getArticleName() + "] [materialId:" + materialId + "]");
			ArticleEntity tempAe = aem.getEntity(materialId);
			if (tempAe != null && tempAe.getColorType() != he.getColorType()) { // 魂石神隐需要相同颜色的材料
				p.sendError(Translate.translateString(Translate.材料错误, new String[][] { { Translate.STRING_1, 魂石洗炼符 } }));
				return;
			}
			// 扣除洗练材料
			ArticleEntity aee = p.removeArticleEntityFromKnapsackByArticleId(materialId, "魂石洗练", true);
			if (aee == null) {
				String description = Translate.删除物品不成功;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				p.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn("[魂石洗练] [失败] [" + p.getLogString() + "] [hunshiId:" + he.getId() + "] [hunshiName:" + he.getArticleName() + "] [不存在的物品id:" + materialId + "] [" + description + "]");
				return;
			}
			// 统计
			ArticleStatManager.addToArticleStat(p, null, aee, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "魂石洗练", null);
		} catch (Exception e) {
			if (logger.isWarnEnabled()) logger.warn("[魂石洗练] [扣除材料异常] [" + p.getLogString() + "] [hunshiId:" + he.getId() + "] [hunshiName:" + he.getArticleName() + "]", e);
			e.printStackTrace();
		}

		Article a = ArticleManager.getInstance().getArticle(he.getArticleName());
		if (a != null) {
			if (logger.isDebugEnabled()) logger.debug("[魂石洗练] [测试] [属性处理] [" + p.getLogString() + "] [hunshiId:" + he.getId() + "] [hunshiName:" + he.getArticleName() + "]");
			try {
				int[] extraPropValue = he.getExtraPropValue();
				int propId = extraPropValue.length - 1;
				int propValue = 0;
				// 获取副属性和融合值
				switch (he.getType()) {
				case 0:
					propValue = getRandomPropValue(hunshiExtraValueMap, propId, he, 1);
					extraPropValue[propId] = propValue;
					he.setExtraPropValue(extraPropValue);
					break;
				case 1:
					propValue = getRandomPropValue(hunshi2ExtraValueMap, propId, he, 1);
					extraPropValue[propId] = propValue;
					he.setExtraPropValue(extraPropValue);
					break;
				default:
					break;
				}
			} catch (Exception e) {
				if (logger.isWarnEnabled()) logger.warn("[魂石洗练] [属性处理异常] [" + p.getLogString() + "] [hunshiId:" + he.getId() + "] [hunshiName:" + he.getArticleName() + "]", e);
				e.printStackTrace();
			}
			he.setJianding(true);
			if (bind) {
				he.setBinded(true);
			}
			StringBuffer sbf = new StringBuffer();
			int basicRhz = he.getRongHeZhi();
			int extraRhz = he.getExtraPropValue()[he.getExtraPropValue().length - 1];
			sbf.append("\n<f color='0xff009cff' size='28'>").append(Translate.基础融合率).append(":").append(getPercent(basicRhz)).append("</f>");
			sbf.append("\n<f color='0xff009cff' size='28'>").append(Translate.附加融合率).append(":").append(getPercent(extraRhz)).append("</f>");
			sbf.append("\n<f color='0xff009cff' size='28'>").append(Translate.总融合率).append(":").append(getPercent(basicRhz + extraRhz)).append("</f>");
			p.sendError(Translate.神隐成功);
			if (logger.isWarnEnabled()) logger.warn("[魂石洗练] [完成] [" + p.getLogString() + "] [" + he.getId() + "] [" + he.getArticleName() + "] [附加融合值：" + he.getExtraPropValue()[he.getExtraPropValue().length - 1] + "]");
			PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备鉴定成功, (byte) 1, AnimationManager.动画播放位置类型_装备鉴定, 0, 0);
			if (pareq != null) {
				p.addMessageToRightBag(pareq);
			}
			p.addMessageToRightBag(new HUNSHI_JIANDING2_SURE_RES(GameMessageFactory.nextSequnceNum(), sbf.toString(), true));
		}
	}

	public String getPercent(int num) {
		float f = (float) num / 100;
		return f + "%";
	}

	/**
	 * 获取魂石鉴定所需费用
	 * @param p
	 * @param he
	 */
	public long getJianDingCost(Player p, HunshiEntity he) {
		long silver = 0;
		switch (he.getType()) {
		case 0:
			if (jianDingCost.containsKey(he.getColorType())) {
				HunshiJianDing cost = jianDingCost.get(he.getColorType());
				if (cost != null) {
					silver = cost.getJianDingCost();
				}
			}
			break;
		case 1:
			if (jianDingCost2.containsKey(he.getColorType())) {
				HunshiJianDing cost2 = jianDingCost2.get(he.getColorType());
				if (cost2 != null) {
					silver = cost2.getJianDingCost();
				}
			}
			break;
		default:
			break;
		}

		return silver;
	}

	/**
	 * 请求鉴定所需材料
	 * @param p
	 * @param req
	 */
	public void getJianDingMaterial(Player p, HUNSHI_JIANDING_REQ req) {
		if (p != null && req != null && aem != null) {
			if (logger.isDebugEnabled()) logger.debug("[查询魂石鉴定请求] [成功] [" + p.getLogString() + "] [魂石id：" + req.getHunshiId() + "]");
			ArticleEntity ae = aem.getEntity(req.getHunshiId());
			if (ae instanceof HunshiEntity) {
				HunshiEntity he = (HunshiEntity) ae;
				if (he.isJianding()) {
					p.send_HINT_REQ(Translate.魂石已鉴定过);
					return;
				}
				HunshiJianDing material = getJianDingMaterials(he);
				String 材料 = "";
				for (String name : material.getMaterialNames()) {
					Article a = ArticleManager.getInstance().getArticle(name);
					if (a != null) {
						int colorValue = ArticleManager.getColorValue(a, a.getColorType());
						材料 = 材料 + Translate.translateString(Translate.带颜色的道具, new String[][] { { Translate.STRING_1, "" + colorValue }, { Translate.STRING_2, Arrays.toString(魂石鉴定符) } });
					}
				}
				String descript = Translate.translateString(Translate.鉴定的提示, new String[][] { { Translate.STRING_1, 材料 } }) + BillingCenter.得到带单位的银两(material.getJianDingCost());
				if (material != null) {
					HUNSHI_JIANDING_RES res = new HUNSHI_JIANDING_RES(GameMessageFactory.nextSequnceNum(), req.getHunshiId(), descript, material.getMaterialNames(), material.getMaterialNums());
					p.addMessageToRightBag(res);
					if (HorseManager.logger.isWarnEnabled()) HorseManager.logger.warn("[查询魂石鉴定] [成功] [" + p.getLogString() + "] [魂石id：" + req.getHunshiId() + "]");
				}
			} else {
				String description = Translate.空白;
				try {
					description = Translate.translateString(Translate.查询魂石鉴定请求错误回复, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.空白) } });
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				p.addMessageToRightBag(hreq);
				if (HorseManager.logger.isWarnEnabled()) HorseManager.logger.warn("[查询魂石鉴定] [失败] [" + p.getLogString() + "] [不是魂石：" + req.getHunshiId() + "]");
			}
		}
	}

	/**
	 * 魂石神隐洗炼材料请求
	 * @param p
	 * @param req
	 */
	public void getJianDing2Material(Player p, HUNSHI_JIANDING2_REQ req) {
		if (p != null && req != null && aem != null) {
			if (HorseManager.logger.isWarnEnabled()) HorseManager.logger.warn("[查询魂石洗炼请求] [" + p.getLogString() + "] [魂石id：" + req.getHunshiId() + "]");
			ArticleEntity ae = aem.getEntity(req.getHunshiId());
			if (ae instanceof HunshiEntity) {
				HunshiEntity he = (HunshiEntity) ae;
				if (he.getExtraPropValue()[he.getExtraPropValue().length - 1] > 0) {
					p.sendError(Translate.魂石已洗炼过);
					return;
				}
				String 材料 = "";
				Article a = ArticleManager.getInstance().getArticle(魂石洗炼符);
				if (a != null) {
					int colorValue = ArticleManager.getColorValue(a, he.getColorType());
					材料 = 材料 + Translate.translateString(Translate.带颜色的道具, new String[][] { { Translate.STRING_1, "" + colorValue }, { Translate.STRING_2, "[" + 魂石洗炼符 + "]" } });
				}
				String 魂石神隐洗炼提示 = Translate.translateString(Translate.魂石神隐洗炼提示, new String[][] { { Translate.STRING_1, 材料 } });
				StringBuffer sbf = new StringBuffer();
				int basicRhz = he.getRongHeZhi();
				sbf.append("\n<f color='0xFFFFFF' size='28'>").append(Translate.基础融合率).append(":").append(getPercent(basicRhz)).append("</f>");
				sbf.append("\n<f color='0xFFFFFF' size='28'>").append(Translate.附加融合率).append(":").append(Translate.未洗炼).append("</f>");

				HUNSHI_JIANDING2_RES res = new HUNSHI_JIANDING2_RES(req.getSequnceNum(), 魂石神隐洗炼提示, sbf.toString(), 魂石洗炼符, he.getColorType());
				p.addMessageToRightBag(res);
				if (HorseManager.logger.isWarnEnabled()) HorseManager.logger.warn("[查询魂石洗炼] [成功] [" + p.getLogString() + "] [魂石id：" + req.getHunshiId() + "]");
			} else {
				String description = Translate.空白;
				try {
					description = Translate.translateString(Translate.查询魂石洗炼请求错误回复, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.空白) } });
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				p.addMessageToRightBag(hreq);
				if (HorseManager.logger.isWarnEnabled()) HorseManager.logger.warn("[查询魂石洗炼] [失败] [" + p.getLogString() + "] [不是魂石：" + req.getHunshiId() + "]");
			}
		}
	}

	/**
	 * 获取鉴定材料
	 * @param he
	 * @return
	 */
	public HunshiJianDing getJianDingMaterials(HunshiEntity he) {
		HunshiJianDing material = null;
		if (he.getType() == 0) {
			material = jianDingCost.get(he.getColorType());
		} else if (he.getType() == 1) {
			material = jianDingCost2.get(he.getColorType());
		}
		return material;
	}

	/**
	 * 随机融合值
	 * @param map
	 * @param color
	 * @return
	 */
	public int getRandomRonghezhi(HunshiJianDing jianding) {
		String[] ronghezhis = jianding.getRonghezhi();
		int[] rates = jianding.getRonghezhiRate();
		int allValue = 0;
		for (int i = 0; i < rates.length; i++) {
			allValue = allValue + rates[i];
		}
		int randomNum = random.nextInt(allValue) + 1;
		int nowValue = 0;
		for (int i = 0; i < rates.length; i++) {
			if (nowValue < randomNum && randomNum <= nowValue + rates[i]) {
				String[] range = ronghezhis[i].split("-");
				int result = random.nextInt(Integer.valueOf(range[1]) - Integer.valueOf(range[0])) + Integer.valueOf(range[0]);
				if (logger.isDebugEnabled()) logger.debug("[魂石鉴定] [测试] [获取融合值成功] [融合值：" + result + "]");
				return result;
			}
			nowValue = nowValue + rates[i];
		}
		if (logger.isDebugEnabled()) logger.debug("[魂石鉴定] [测试] [获取融合值失败]");
		return 0;
	}

	/**
	 * 随机获取一条属性
	 * @param propMap
	 * @param propId
	 * @param propType
	 *            0-主属性；1-副属性
	 * @param he
	 * @return
	 */
	public int getRandomPropValue(Map<Integer, HunshiPropValue> propMap, Integer propId, HunshiEntity he, int propType) {
		if (propType == 0) {
			if (he.getMainPropValue()[propId] != 0) {
				if (logger.isWarnEnabled()) logger.warn("[魂石鉴定异常] [已有该主属性] [" + he.getId() + "] [" + he.getArticleName() + "] [propId:" + propId + "]");
				return 0;
			}
		} else if (propType == 1) {
			if (he.getExtraPropValue()[propId] != 0) {
				if (logger.isWarnEnabled()) logger.warn("[魂石鉴定异常] [已有该副属性] [" + he.getId() + "] [" + he.getArticleName() + "] [propId:" + propId + "]");
				return 0;
			}
		}
		HunshiPropValue propValue = propMap.get(propId);
		if (propValue != null) {
			int[] propRange = propValue.getPropValue(he.getColorType());
			int value = random.nextInt(propRange[1] - propRange[0] + 1) + propRange[0];
			if (logger.isWarnEnabled()) logger.warn("[魂石洗练] [测试] [属性处理] [hunshiId:" + he.getId() + "] [hunshiName:" + he.getArticleName() + "] [propId:" + propId + "] [value：" + value + "]");
			return value;
		}
		if (logger.isWarnEnabled()) logger.warn("[魂石鉴定异常] [获得属性值] [" + he.getId() + "] [" + he.getArticleName() + "]");
		return 0;
	}

	/**
	 * 放入一件材料时，请求目标物品
	 * @param p
	 * @param req
	 */
	public void handle_HUNSHI_MERGE_AIM_REQ(Player p, HUNSHI_MERGE_AIM_REQ req) {
		long materialId = req.getMaterialId();
		ArticleEntity ae = aem.getEntity(materialId);
		if (ae != null && ae instanceof HunshiEntity) {
			HunshiEntity he = (HunshiEntity) ae;
			if (!he.isJianding()) {
				p.sendError(Translate.未鉴定不能合成);
				return;
			}

			// if (!(he.getExtraPropValue()[he.getExtraPropValue().length - 1] > 0)) {
			// p.sendError(Translate.未神隐不能合成);
			// return;
			// }
			Article a = null;
			if (ae.getColorType() == 0) {
				a = ArticleManager.getInstance().getArticle(Translate.凝神魂石);
			} else if (ae.getColorType() == 1) {
				a = ArticleManager.getInstance().getArticle(Translate.随机带窍魂石);
			} else {
				a = ArticleManager.getInstance().getArticle(ae.getArticleName());
			}
			if (a != null) {
				try {
					ArticleEntity tempAe = aem.createTempEntity(a, ae.isBinded(), ArticleEntityManager.坐骑魂石, null, ae.getColorType() + 1);
					if (tempAe != null) {
						long silver = jianDingCost.get(tempAe.getColorType()).getMergeCost();
						p.addMessageToRightBag(new HUNSHI_MERGE_AIM_RES(GameMessageFactory.nextSequnceNum(), tempAe.getId(), silver));
						if (logger.isWarnEnabled()) logger.warn("[魂石合成] [请求合成目标] [" + p.getLogString() + "] [" + a.getName_stat() + "]");
					}
				} catch (Exception e) {
					if (logger.isWarnEnabled()) logger.warn("[魂石] [创建临时物品异常] [" + p.getLogString() + "] [" + a.getName_stat() + "]");
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 魂石特有信息查询
	 * @param req
	 */
	public void handle_HUNSHI_SPECIAL_REQ(Player p, HUNSHI_SPECIAL_REQ req) {
		long[] materialIds = req.getMaterialId();
		boolean jianding[] = new boolean[materialIds.length];
		int ronghezhi[] = new int[materialIds.length];
		int index[] = new int[materialIds.length];
		for (int i = 0; i < materialIds.length; i++) {
			ArticleEntity ae = aem.getEntity(materialIds[i]);
			if (ae != null && ae instanceof HunshiEntity) {
				HunshiEntity he = (HunshiEntity) ae;
				jianding[i] = he.isJianding();
				ronghezhi[i] = he.getRongHeZhi() + he.getExtraPropValue()[he.getExtraPropValue().length - 1];
				index[i] = he.getHoleIndex();
			} else {
				p.sendError(Translate.请放入正确物品);
				return;
			}
		}
		HUNSHI_SPECIAL_RES res = new HUNSHI_SPECIAL_RES(GameMessageFactory.nextSequnceNum(), materialIds, jianding, ronghezhi, index);
		if (res != null) {
			p.addMessageToRightBag(res);
		} else {
			if (logger.isDebugEnabled()) logger.debug("[魂石特有信息查询返回协议] [测试] [null] [" + p.getLogString() + "]");
		}
	}

	public void handle_HUNSHI_MERGE_REQ(Player p, HUNSHI_MERGE_REQ req) {
		try {
			long aimTempId = req.getAimTempId();
			long[] materialIds = req.getMaterialId();
			int ronghezhi = 0;
			long silver = 0;
			boolean bind = false;
			for (long id : materialIds) { // 计算融合值
				ArticleEntity ae = aem.getEntity(id);
				if (ae != null && ae instanceof HunshiEntity) {
					HunshiEntity he = (HunshiEntity) ae;
					ronghezhi += he.getRongHeZhi() + he.getExtraPropValue()[he.getExtraPropValue().length - 1];
					if (aimTempId > 0) { // 合成套装魂石,取套装魂石对应颜色的合成费用
						ArticleEntity tempAe = aem.getEntity(aimTempId);
						if (tempAe != null) {
							silver = jianDingCost2.get(tempAe.getColorType()).getMergeCost();
						}
					} else {// 合成普通魂石,取（材料颜色+1）对应的合成费用
						silver = jianDingCost.get(ae.getColorType() + 1).getMergeCost();
					}
					bind = bind || ae.isBinded();
				} else {
					p.sendError(Translate.请放入正确物品);
					return;
				}
			}
			if (req.getSilverType() == 0) {
				if (silver > p.getBindSilver()) {
					p.sendError(Translate.绑银不足);
					return;
				}
				if (!bind) { // 用了绑银+非绑定材料
					WindowManager windowManager = WindowManager.getInstance();
					MenuWindow mw = windowManager.createTempMenuWindow(600);
					mw.setTitle(Translate.合成提示信息);
					mw.setDescriptionInUUB(Translate.translateString(Translate.合成消耗绑银绑定, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(silver) } }));
					Option_hunshi_merge_bind option = new Option_hunshi_merge_bind(aimTempId, materialIds, silver, ronghezhi, req.getSilverType(), true);
					option.setText(Translate.确定);
					Option_UseCancel cancel = new Option_UseCancel();
					cancel.setText(Translate.取消);
					mw.setOptions(new Option[] { option, cancel });
					p.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions()));
					return;
				} else {
					hunshiMergeSure(p, aimTempId, materialIds, silver, ronghezhi, req.getSilverType(), true);
				}
			} else if (req.getSilverType() == 1) {
				if (silver > p.getSilver()) {
					p.sendError(Translate.银子不足);
					return;
				}
				if (bind) { // 用了非绑银+绑定材料
					WindowManager windowManager = WindowManager.getInstance();
					MenuWindow mw = windowManager.createTempMenuWindow(600);
					mw.setTitle(Translate.合成提示信息);
					mw.setDescriptionInUUB(Translate.translateString(Translate.合成消耗元宝提示绑定信息, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(silver) } }));
					Option_hunshi_merge_bind option = new Option_hunshi_merge_bind(aimTempId, materialIds, silver, ronghezhi, req.getSilverType(), true);
					option.setText(Translate.确定);
					Option_UseCancel cancel = new Option_UseCancel();
					cancel.setText(Translate.取消);
					mw.setOptions(new Option[] { option, cancel });
					p.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions()));
					return;
				} else {
					hunshiMergeSure(p, aimTempId, materialIds, silver, ronghezhi, req.getSilverType(), false);
				}
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) logger.error("[魂石合成请求] [异常]" + p.getLogString(), e);
			e.printStackTrace();
		}
	}

	/**
	 * 魂石合成确认
	 * @param p
	 * @param aimId
	 *            套装石的目标物品id，魂石合成的时候写0
	 * @param materialIds
	 * @param silver
	 * @param ronghezhi
	 * @param silverType
	 *            0-绑银合成；1-银子合成
	 * @param isBind
	 */
	public void hunshiMergeSure(Player p, long aimId, long[] materialIds, long silver, int ronghezhi, int silverType, boolean isBind) {
		if (logger.isDebugEnabled()) logger.debug(p.getLogString() + "[魂石合成] [aimid:" + aimId + "]");
		ArticleManager am = ArticleManager.getInstance();
		if (am == null || aem == null) {
			return;
		}
		int mergeType = 0; // 0-魂石合成；1-套装魂石合成
		if (aimId > 0) mergeType = 1;
		boolean aimGreen = false;
		boolean aimBlue = false;
		// for (long mid : materialIds) {
		// ArticleEntity tempAe = aem.getEntity(mid);
		// if (tempAe != null && tempAe instanceof HunshiEntity) {
		// HunshiEntity he = (HunshiEntity) tempAe;
		// if (!(he.getExtraPropValue()[he.getExtraPropValue().length - 1] > 0)) {
		// p.sendError(Translate.未神隐不能合成);
		// return;
		// }
		// }
		// }
		Set<Long> sameIdSet = new HashSet<Long>();
		ArrayList<ArticleEntity> strongMaterialEntitys = new ArrayList<ArticleEntity>();
		for (long mid : materialIds) {
			sameIdSet.add(mid);
			ArticleEntity aee = p.removeArticleEntityFromKnapsackByArticleId(mid, "魂石合成", true);
			if (aee == null) {
				String description = Translate.删除物品不成功;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				p.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn(p.getLogString() + "[魂石合成] [失败] [材料不存在] [" + p.getLogString() + "] [不存在的物品id:" + mid + "] [" + description + "]");
				return;
			} else {
				strongMaterialEntitys.add(aee);
			}
			if (aee.getColorType() == 0) {
				aimGreen = true;
			}
			if (aee.getColorType() == 1) {// 绿色合蓝色要特殊处理
				aimBlue = true;
			}
			if (aee instanceof HunshiEntity) {
				HunshiEntity hee = (HunshiEntity) aee;
				if (logger.isWarnEnabled()) logger.warn(p.getLogString() + "[魂石合成] [扣除材料] [融合值:" + hee.getRongHeZhi() + hee.getExtraPropValue()[hee.getExtraPropValue().length - 1] + "] [articleName:" + aee.getArticleName() + "] [id:" + mid + "] [color:" + aee.getColorType() + "]");
			}
			if (aimId <= 0) {
				aimId = aee.getId();
			}
			if (logger.isDebugEnabled()) logger.debug(p.getLogString() + "[魂石合成] [绿合蓝特殊处理] [aimBlue:" + aimBlue + "] [mergeType：" + mergeType + "]");
			// 统计
			ArticleStatManager.addToArticleStat(p, null, aee, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "魂石合成", null);
		}
		try {
			ShopActivityManager.getInstance().noticeUseSuccess(p, strongMaterialEntitys, (byte) 1);
		} catch (Exception e) {
			ActivitySubSystem.logger.error("[合成物品] [使用赠送--合成物品--异常] [" + p.getLogString() + "]", e);
		}
		try {
			if (silverType == 0) {
				if (silver <= p.getBindSilver()) {
					BillingCenter.getInstance().playerExpense(p, silver, CurrencyType.BIND_YINZI, ExpenseReasonType.魂石合成, "魂石合成");
				} else {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.余额不足);
					p.addMessageToRightBag(hreq);
					return;
				}
			} else if (silverType == 1) {
				if (silver <= p.getSilver()) {
					BillingCenter.getInstance().playerExpense(p, silver, CurrencyType.YINZI, ExpenseReasonType.魂石鉴定, "魂石鉴定");
				} else {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.余额不足);
					p.addMessageToRightBag(hreq);
					return;
				}
			}
		} catch (NoEnoughMoneyException e) {
			p.sendError(Translate.余额不足);
			e.printStackTrace();
		} catch (BillFailedException e) {
			e.printStackTrace();
		}
		try {
			ArticleEntity ae = aem.getEntity(aimId);
			int resultNumber = random.nextInt(10000) + 1;
			if (sameIdSet.size() < materialIds.length) {
				if (logger.isErrorEnabled()) logger.error(p.getLogString() + "[魂石合成] [失败] [刷魂石] [重复id个数：" + (materialIds.length - sameIdSet.size() + 1) + "]");
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.合成失败);
				p.addMessageToRightBag(hreq);
				p.addMessageToRightBag(new HUNSHI_MERGE_RES(GameMessageFactory.nextSequnceNum(), false));
				return;
			}
			if (resultNumber > ronghezhi) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.合成失败);
				p.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn(p.getLogString() + "[魂石合成] [完成] [融合值:" + ronghezhi + "] [随机值：" + resultNumber + "]");
				p.addMessageToRightBag(new HUNSHI_MERGE_RES(GameMessageFactory.nextSequnceNum(), false));
				return;
			}
			if (ae != null) {
				if (mergeType == 0) {// 魂石合成
					if (ae.getColorType() < ArticleManager.notEquipmentColorMaxValue) {
						Article a = am.getArticle(ae.getArticleName());
						if (logger.isDebugEnabled()) logger.debug(p.getLogString() + "[魂石合成] [绿合蓝特殊处理] [是否蓝：" + aimBlue + "]");
						if (aimGreen) {
							a = ArticleManager.getInstance().getArticle(Translate.凝神魂石);
						}
						if (aimBlue) {
							int allValue = 0;
							for (int i = 0; i < hunshiWithHoleProp.length; i++) {
								allValue = allValue + hunshiWithHoleProp[i];
							}
							int randomNum = random.nextInt(allValue) + 1;
							if (logger.isDebugEnabled()) logger.debug(p.getLogString() + "[魂石合成] [绿合蓝特殊处理] [随机值：" + randomNum + "]");
							int nowValue = 0;
							for (int i = 0; i < hunshiWithHoleProp.length; i++) {
								if (logger.isDebugEnabled()) logger.debug(p.getLogString() + "[魂石合成] [绿合蓝特殊处理] [index：" + i + "]");
								if (nowValue < randomNum && randomNum <= nowValue + hunshiWithHoleProp[i]) {
									a = ArticleManager.getInstance().getArticle(cnNameIdMap.get(hunshiId[i]));
									if (logger.isDebugEnabled()) logger.debug(p.getLogString() + "[魂石合成] [绿合蓝特殊处理] [要合成：" + a.getName_stat() + "]");
								}
								nowValue = nowValue + hunshiWithHoleProp[i];
							}
						}
						if (a != null) {
							try {
								ArticleEntity aimAe = aem.createEntity(a, isBind, ArticleEntityManager.CREATE_REASON_COMPOSE_ARTICLE, p, ae.getColorType() + 1, 1, true);
								p.putToKnapsacks(aimAe, "魂石合成");
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.合成成功);
								p.addMessageToRightBag(hreq);
								p.addMessageToRightBag(new HUNSHI_MERGE_RES(GameMessageFactory.nextSequnceNum(), true));
								if (logger.isWarnEnabled()) logger.warn(p.getLogString() + "[魂石合成] [成功] [融合值:" + ronghezhi + "] [随机值：" + resultNumber + "] [articleName:" + aimAe.getArticleName() + "] [id:" + aimAe.getId() + "] [color:" + aimAe.getColorType() + "]");
								// 统计
								ArticleStatManager.addToArticleStat(null, p, aimAe, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "魂石合成", null);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				} else if (mergeType == 1) {// 套装魂石合成
					if (ae.getColorType() < ArticleManager.color_magicweapon_Strings.length) {
						Article a = am.getArticle(ae.getArticleName());
						if (a != null) {
							try {
								ArticleEntity aimAe = aem.createEntity(a, isBind, ArticleEntityManager.CREATE_REASON_COMPOSE_ARTICLE, p, ae.getColorType(), 1, true);
								p.putToKnapsacks(aimAe, "魂石合成");
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.合成成功);
								p.addMessageToRightBag(hreq);
								p.addMessageToRightBag(new HUNSHI_MERGE_RES(GameMessageFactory.nextSequnceNum(), true));
								ArticleStatManager.addToArticleStat(null, p, aimAe, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "魂石合成", null);
								if (logger.isWarnEnabled()) logger.warn(p.getLogString() + "[魂石合成] [成功] [融合值:" + ronghezhi + "] [随机值：" + resultNumber + "] [articleName:" + aimAe.getArticleName() + "] [id:" + aimAe.getId() + "] [color:" + aimAe.getColorType() + "]");
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) logger.error("[魂石合成确认] [异常]" + p.getLogString(), e);
			e.printStackTrace();
		}
	}

	/**
	 * 创建一个更高一级颜色的临时物品
	 * @param aeId
	 * @return
	 */
	public ArticleEntity getTempAe(long aeId) {
		ArticleEntity ae = aem.getEntity(aeId);
		if (ae != null) {
			Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
			if (a != null) {
				ArticleEntity tempAe;
				try {
					tempAe = aem.createTempEntity(a, ae.isBinded(), ArticleEntityManager.坐骑魂石, null, ae.getColorType() + 1);
					return tempAe;
				} catch (Exception e) {
					if (logger.isWarnEnabled()) logger.warn("[魂石] [创建临时物品异常] [" + a.getName_stat() + "]");
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public void handle_HUNSHI_PUTON_REQ(Player p, HUNSHI_PUTON_REQ req) {
		long start = System.currentTimeMillis();
		long horseId = req.getHorseId();
		int index = req.getIndex();

		logger.debug("[镶嵌魂石] [index:" + index + "] [" + p.getLogString() + "] [horseId:" + horseId + "]");
		long hunshiId = req.getHunshiId();
		int hunshiType = req.getHunshiType();
		// if (h != null) {
		ArticleEntity ae = aem.getEntity(hunshiId);
		if (ae != null) {
			synchronized (p.tradeKey) {
				Horse h = HorseManager.getInstance().getHorseById(horseId, p);
				if (h == null) {
					if (logger.isInfoEnabled()) {
						logger.info("[镶嵌魂石] [失败] [坐骑不存在] [" + p.getLogString() + "] [horseId:" + horseId + "]");
					}
					return;
				}
				if (ae instanceof HunshiEntity) {
					HunshiEntity he = (HunshiEntity) ae;
					if (!he.isJianding()) {
						p.sendError(Translate.未鉴定不能镶嵌);
						return;
					}
					if (hunshiType != he.getType()) {
						p.sendError(Translate.不能镶嵌或镶嵌位置错误);
						return;
					}
					if (req.getOpType() == 0) { // 镶嵌
						boolean hasMaterial = false;
						Knapsack[] knapsacks = p.getKnapsacks_common();
						if (knapsacks != null) {
							for (Knapsack knapsack : knapsacks) {
								if (knapsack != null) {
									if (!hasMaterial) {
										int index2 = knapsack.hasArticleEntityByArticleId(hunshiId);
										if (index2 != -1) {
											hasMaterial = true;
											break;
										}
									}
								}
							}
						}
						if(!hasMaterial){
							return;
						}
						long[] hunshiArray = h.getHunshiArray();
						if (hunshiType == 1) { // 套装石镶嵌
							boolean[] hunshi2Open = h.getHunshi2Open();
							if (hunshi2Open != null && hunshi2Open[index]) {
								hunshiArray = h.getHunshi2Array();
							} else {
								logger.debug("[镶嵌魂石] [hunshi2Open:" + hunshi2Open + "] [" + p.getLogString() + "] [horseId:" + horseId + "]");
//								p.sendError(Translate.尚未开启);
								return;
							}
						}
						if (hunshiArray == null || hunshiArray.length <= index) {
							String description = Translate.不能镶嵌或镶嵌位置错误;
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
							p.addMessageToRightBag(hreq);
							if (logger.isWarnEnabled()) logger.warn("[type:镶嵌] [result:失败] [" + p.getLogString() + "] [魂石名:" + he.getArticleName() + "] [魂石id:" + hunshiId + "] [孔(0-15):" + index + "] [描述:" + description + "] [" + (System.currentTimeMillis() - start) + "ms]");
							return;
						}
						long oldId = hunshiArray[index];
						if (hasMaterial) {
							if (!ArticleProtectManager.instance.isCanDo(p, ArticleProtectDataValues.ArticleProtect_High, he.getId())) {
								p.sendError(Translate.锁魂物品不能做此操作);
								return;
							}
							for (long hunshi2Id : hunshiArray) {
								ArticleEntity ae2 = aem.getEntity(hunshi2Id);
								HunshiEntity he2 = (HunshiEntity) ae2;
								if (ae2 != null && he2.getType() == 1 && ae2.getArticleName().equals(he.getArticleName())) {
									p.sendError(Translate.不可重复镶嵌同类型套装魂石);
									return;
								}
							}
							// if (oldId > 0) {
							// h.initHunshiAttr(-1);
							// }
							// 镶嵌位置没有魂石，直接镶嵌
							ArticleEntity aee = p.removeArticleEntityFromKnapsackByArticleId(hunshiId, "镶嵌", false);
							if (aee != null) {
								if (ArticleManager.logger.isWarnEnabled()) {
									ArticleManager.logger.warn("[镶嵌魂石前]" + p.getPlayerPropsString());
								}
								// 计算魂石和套装魂石属性
								if (p.isIsUpOrDown() && !p.isFlying()) {
									p.removeHorseProperty(h);
								}
								h.initHunshiAttr(-1);
								hunshiArray[index] = hunshiId;
								if (hunshiType == 0) {
									h.setHunshiArray(hunshiArray);
								} else if (hunshiType == 1) {
									h.setHunshi2Array(hunshiArray);
									Hunshi2Cell hcell = h.getHunshi2Cells()[index];
									if (hcell != null) {
										hcell.setHunshiId(hunshiId);
									}
								}
								h.initHunshiAttr(1);
								if (p.isIsUpOrDown() && !p.isFlying()) {
									p.addHorseProperty(h);
								}
								// 魂石镶嵌成功
								PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备镶嵌成功, (byte) 1, AnimationManager.动画播放位置类型_镶嵌, 0, 0);
								if (pareq != null) {
									p.addMessageToRightBag(pareq);
								}
								String description = Translate.镶嵌完成;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, description);
								p.addMessageToRightBag(hreq);
								p.addMessageToRightBag(new HUNSHI_PUTON_RES(req.getSequnceNum(), req.getHunshiId(), req.getIndex(), req.getOpType(), true));
								if (logger.isWarnEnabled()) logger.warn("[type:镶嵌] [result:成功] [" + p.getLogString() + "] [魂石名:" + he.getArticleName() + "] [魂石id:" + hunshiId + "] [孔(0-15):" + index + "] [" + (System.currentTimeMillis() - start) + "ms]");
								if (ArticleManager.logger.isWarnEnabled()) {
									ArticleManager.logger.warn("[镶嵌魂石后]" + p.getPlayerPropsString());
								}
							} else {
								if (logger.isErrorEnabled()) logger.error("[type:镶嵌] [result:扣除失败] [" + p.getLogString() + "] [魂石名:" + he.getArticleName() + "] [魂石id:" + hunshiId + "] [孔(0-15):" + index + "] [" + (System.currentTimeMillis() - start) + "ms]");
								return;
							}
							if (oldId > 0) { // 镶嵌位置已有魂石，替换

								ArticleEntity hunshiEntity = aem.getEntity(oldId);
								if (hunshiEntity != null) {
									if (hunshiEntity instanceof HunshiEntity) {
										Knapsack ks = p.getKnapsack_common();
										if (ks == null) {
											String description = Translate.服务器物品出现错误;
											HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
											p.addMessageToRightBag(hreq);
											if (logger.isWarnEnabled()) {
												logger.warn("[type:拆除] [result:失败] [" + p.getLogString() + "] [魂石id:" + oldId + "] [孔(0-15):" + index + "] [reason:找不到放置物品的背包] [描述:" + description + "] [" + (System.currentTimeMillis() - start) + "ms]");
											}
											return;
										}
										if (ks.isFull()) {
											String description = Translate.背包已满;
											HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
											p.addMessageToRightBag(hreq);
											if (logger.isWarnEnabled()) {
												logger.warn("[type:拆除] [result:失败] [" + p.getLogString() + "] [魂石id:" + oldId + "] [孔(0-15):" + index + "] [reason:背包已满] [" + (System.currentTimeMillis() - start) + "ms]");
											}
											return;
										}

										// 计算魂石和套装魂石基础属性
										// h.initHunshiAttr(-1);
										// hunshiArray[index] = -1;
										// for (int i = 0; i < he.getMainPropValue().length; i++) {
										// addRemoveProp(h, i, -he.getMainPropValue()[i]);
										// }
										// for (int i = 0; i < he.getExtraPropValue().length; i++) {
										// addRemoveProp(h, i, -he.getExtraPropValue()[i]);
										// }
										// if (hunshiType == 0) {
										// h.setHunshiArray(hunshiArray);
										// } else if (hunshiType == 1) {
										// // 计算套装属性
										// HunshiSuit suit = canSplitSuit(hunshiArray, he);
										// if (suit != null) {
										// for (int i = 0; i < suit.getPropValue().length; i++) {
										// addRemoveProp(h, suit.getPropId()[i], -suit.getPropValue()[i]);
										// }
										// }
										// h.setHunshi2Array(hunshiArray);
										// Hunshi2Cell hcell = h.getHunshi2Cells()[index];
										// if (hcell != null) {
										// hcell.setHunshiId(-1);
										// }
										// }
										// 计算魂石和套装魂石属性
										// if (p.isIsUpOrDown() && !p.isFlying()) {
										// p.removeHorseProperty(h);
										// }
										// h.initHunshiAttr(-1);
										// hunshiArray[index] = -1;
										// if (hunshiType == 0) {
										// h.setHunshiArray(hunshiArray);
										// } else if (hunshiType == 1) {
										// h.setHunshi2Array(hunshiArray);
										// Hunshi2Cell hcell = h.getHunshi2Cells()[index];
										// if (hcell != null) {
										// hcell.setHunshiId(-1);
										// }
										// }
										// h.initHunshiAttr(1);
										// if (p.isIsUpOrDown() && !p.isFlying()) {
										// p.addHorseProperty(h);
										// }
										if (p.putToKnapsacks(hunshiEntity, "拆除")) {
											if (logger.isWarnEnabled()) logger.warn("[type:拆除] [result:成功] [" + p.getLogString() + "] [魂石id:" + hunshiEntity.getId() + "] [孔(0-15):" + index + "] [" + (System.currentTimeMillis() - start) + "ms]");
										} else {
											if (logger.isWarnEnabled()) logger.warn("[type:拆除] [result:成功但放置失败] [" + p.getLogString() + "] [魂石id:" + hunshiEntity.getId() + "] [孔(0-15):" + index + "] [" + (System.currentTimeMillis() - start) + "ms]");
										}
									} else {
										String description = Translate.服务器物品出现错误;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										p.addMessageToRightBag(hreq);
										if (logger.isWarnEnabled()) logger.warn("[type:拆除] [result:失败] [" + p.getLogString() + "] [reason:id为" + hunshiArray[index] + "的物品不是宝石] [孔(0-15):" + index + "] [描述:" + description + "] [" + (System.currentTimeMillis() - start) + "ms]");
										return;
									}
								} else {
									String description = Translate.服务器物品出现错误;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									p.addMessageToRightBag(hreq);
									if (logger.isWarnEnabled()) logger.warn("[type:拆除] [result:失败] [" + p.getLogString() + "] [reason:id为" + hunshiArray[index] + "的物品为空] [孔(0-15):" + index + "] [" + (System.currentTimeMillis() - start) + "ms]");
									return;
								}
							}

						}

					} else if (req.getOpType() == -1) { // 拆除

						if (p != null && aem != null) {
							if (ArticleManager.logger.isWarnEnabled()) {
								ArticleManager.logger.warn("[挖取魂石前] " + p.getPlayerPropsString());
							}
							long[] hunshiArray = h.getHunshiArray();
							if (hunshiType == 1) { // 套装石拆除
								hunshiArray = h.getHunshi2Array();
							}
							if (hunshiArray == null || hunshiArray.length <= index) {
								String description = Translate.不能挖取或挖取位置错误;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								p.addMessageToRightBag(hreq);
								if (logger.isWarnEnabled()) logger.warn("[type:拆除] [result:失败] [" + p.getLogString() + "] [魂石id:" + hunshiId + "] [孔(0-15):" + index + "] [描述:" + description + "] [" + (System.currentTimeMillis() - start) + "ms]");
								return;
							}
							if (hunshiArray[index] <= 0) {
								String description = Translate.挖取位置上没有魂石;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								p.addMessageToRightBag(hreq);
								if (logger.isWarnEnabled()) logger.warn("[type:拆除] [result:失败] [" + p.getLogString() + "] [魂石id:" + hunshiId + "] [孔(0-15):" + index + "] [描述:" + description + "] [" + (System.currentTimeMillis() - start) + "ms]");
								return;
							}
							long oldId = hunshiArray[index];

							Knapsack ks = p.getKnapsack_common();
							if (ks == null) {
								String description = Translate.服务器物品出现错误;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								p.addMessageToRightBag(hreq);
								if (logger.isWarnEnabled()) logger.warn("[type:拆除] [result:失败] [" + p.getLogString() + "] [魂石id:" + hunshiId + "] [孔(0-15):" + index + "] [reason:找不到放置物品的背包] [描述:" + description + "] [" + (System.currentTimeMillis() - start) + "ms]");
								return;
							}
							if (ks.isFull()) {
								String description = Translate.背包已满;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								p.addMessageToRightBag(hreq);
								if (logger.isWarnEnabled()) logger.warn("[type:拆除] [result:失败] [" + p.getLogString() + "] [魂石id:" + hunshiId + "] [孔(0-15):" + index + "] [描述:" + description + "] [" + (System.currentTimeMillis() - start) + "ms]");
								return;
							}
							ArticleEntity oldAe = DefaultArticleEntityManager.getInstance().getEntity(oldId);
							if (oldAe == null) {
								if (logger.isWarnEnabled()) {
									logger.warn("[type:拆除] [result:失败] [物品存在] [" + p.getLogString() + "] [id:" + oldId + "]");
								}
								return;
							}
							// 计算魂石和套装魂石属性
							if (p.isIsUpOrDown() && !p.isFlying()) {
								p.removeHorseProperty(h);
							}
							h.initHunshiAttr(-1);
							hunshiArray[index] = -1;
							if (hunshiType == 0) {
								h.setHunshiArray(hunshiArray);
							} else if (hunshiType == 1) {
								h.setHunshi2Array(hunshiArray);
								Hunshi2Cell hcell = h.getHunshi2Cells()[index];
								if (hcell != null) {
									hcell.setHunshiId(-1);
								}
							}
							h.initHunshiAttr(1);
							if (p.isIsUpOrDown() && !p.isFlying()) {
								p.addHorseProperty(h);
							}
							p.addMessageToRightBag(new HUNSHI_PUTON_RES(req.getSequnceNum(), hunshiId, index, req.getOpType(), true));
							if (p.putToKnapsacks(oldAe, "拆除")) {
								if (logger.isWarnEnabled()) logger.warn("[type:拆除] [result:成功] [" + p.getLogString() + "] [魂石id:" + hunshiId + "] [孔(0-15):" + index + "] [" + (System.currentTimeMillis() - start) + "ms]");
							} else {
								if (logger.isWarnEnabled()) logger.warn("[type:拆除] [result:成功但放置出问题] [" + p.getLogString() + "] [魂石id:" + hunshiId + "] [孔(0-15):" + index + "] [" + (System.currentTimeMillis() - start) + "ms]");
							}
							if (ArticleManager.logger.isWarnEnabled()) {
								ArticleManager.logger.warn("[挖取魂石后] " + p.getPlayerPropsString());
							}
							return;

						}

					}

				} else {
					p.sendError(Translate.物品类型错误);
					return;
				}
			}
		} else {
			String description = Translate.空白;
			description = Translate.物品不存在;
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
			p.addMessageToRightBag(hreq);
			if (logger.isWarnEnabled()) logger.warn("[type:镶嵌] [result:失败] [" + p.getLogString() + "] [魂石id:" + hunshiId + "] [" + description + "] [孔:" + index + "] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start) + "ms] [" + (System.currentTimeMillis() - start) + "ms]");
		}
		// }
	}

	/**
	 * 加减属性
	 * @param player
	 * @param soulType
	 * @param propType
	 *            属性类型
	 * @param value
	 *            属性值,正值为加属性，负值为减属性
	 */
	public void addRemoveProp(Horse horse, int propType, int value) {
		try {
			Player p = horse.owner;
			if (p != null) {
				if (value != 0) {
					if (logger.isDebugEnabled()) logger.debug("[属性处理] [" + p.getLogString() + "] [" + horse.getLogString() + "] [propType:" + propType + "] [value：" + value + "]");
				}
				switch (propType) {
				case 0:
					horse.setMaxHPB(horse.getMaxHPB() + value);
					break;
				case 1:
					horse.setMaxMPB(horse.getMaxMPB() + value);
					break;
				case 2:
					horse.setPhyAttackB(horse.getPhyAttackB() + value);
					break;
				case 3:
					horse.setMagicAttackB(horse.getMagicAttackB() + value);
					break;
				case 4:
					horse.setPhyDefenceB(horse.getPhyDefenceB() + value);
					break;
				case 5:
					horse.setMagicDefenceB(horse.getMagicDefenceB() + value);
					break;
				case 6:
					horse.setDodgeB(horse.getDodgeB() + value);
					break;
				case 7:
					horse.setCriticalDefenceB(horse.getCriticalDefenceB() + value);
					break;
				case 8:
					horse.setHitB(horse.getHitB() + value);
					break;
				case 9:
					horse.setCriticalHitB(horse.getCriticalHitB() + value);
					break;
				case 10:
					horse.setAccurateB(horse.getAccurateB() + value);
					break;
				case 11:
					horse.setBreakDefenceB(horse.getBreakDefenceB() + value);
					break;
				case 12:
					horse.setFireAttackB(horse.getFireAttackB() + value);
					break;
				case 13:
					horse.setFireDefenceB(horse.getFireDefenceB() + value);
					break;
				case 14:
					horse.setFireIgnoreDefenceB(horse.getFireIgnoreDefenceB() + value);
					break;
				case 15:
					horse.setBlizzardAttackB(horse.getBlizzardAttackB() + value);
					break;
				case 16:
					horse.setBlizzardDefenceB(horse.getBlizzardDefenceB() + value);
					break;
				case 17:
					horse.setBlizzardIgnoreDefenceB(horse.getBlizzardIgnoreDefenceB() + value);
					break;
				case 18:
					horse.setWindAttackB(horse.getWindAttackB() + value);
					break;
				case 19:
					horse.setWindDefenceB(horse.getWindDefenceB() + value);
					break;
				case 20:
					horse.setWindIgnoreDefenceB(horse.getWindIgnoreDefenceB() + value);
					break;
				case 21:
					horse.setThunderAttackB(horse.getThunderAttackB() + value);
					break;
				case 22:
					horse.setThunderDefenceB(horse.getThunderDefenceB() + value);
					break;
				case 23:
					horse.setThunderIgnoreDefenceB(horse.getThunderIgnoreDefenceB() + value);
					break;
				default:
					break;
				}

			}
		} catch (Exception e) {
			if (logger.isWarnEnabled()) logger.warn("[属性处理异常]", e);
			e.printStackTrace();
		}
	}

	public HunshiSuit getHunshiSuit(ArticleEntity ae) {
		Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
		String key = a.getName_stat() + "_" + ae.getColorType();
		HunshiSuit suit = tempMap.get(key);
		if (logger.isDebugEnabled()) logger.debug("[获取套装] [suitName：" + suit.getSuitCNName() + "] [suitColor:" + suit.getSuitColor() + "] [suitid:" + suit.getSuitId() + "]");
		return suit;
	}

	public HunshiSuit getHunshiSuitForLowColor(ArticleEntity ae) {
		Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
		String key = a.getName_stat() + "_" + (ae.getColorType() - 1);
		HunshiSuit suit = tempMap.get(key);
		if (logger.isDebugEnabled()) logger.debug("[获取降级套装] [suitName：" + suit.getSuitCNName() + "] [suitColor:" + suit.getSuitColor() + "] [suitid:" + suit.getSuitId() + "]");
		return suit;
	}

	public boolean isAvailibale(HunshiSuit s, List<ArticleEntity> ll) {
		boolean findAll = true;
		for (String name : s.getHunshi2Names()) {
			boolean findOne = false;
			for (ArticleEntity ae : ll) {
				if (ae.getArticleName().equals(name) && (ae.getColorType() == s.getSuitColor() || ae.getColorType() - 1 == s.getSuitColor())) {
					findOne = true;
				}
			}
			findAll = findAll && findOne;
			if (!findAll) {
				return false;
			}
		}
		return findAll;
	}

	/**
	 * 获取魂石套装list
	 * @param hunshi2Array
	 *            镶嵌面板的套装石数组
	 * @return
	 */
	public List<HunshiSuit> getHunshiSuitList(long[] hunshi2Array) {
		List<HunshiSuit> hunshiSuitList = new ArrayList<HunshiSuit>();

		List<ArticleEntity> ll = new ArrayList<ArticleEntity>();
		for (int i = 0; i < hunshi2Array.length; i++) {
			if (hunshi2Array[i] > 0) {
				ArticleEntity ae = aem.getEntity(hunshi2Array[i]);
				ll.add(ae);
			}
		}
		for (ArticleEntity ae : ll) {
			HunshiSuit suit = getHunshiSuit(ae);
			if (isAvailibale(suit, ll) && !hunshiSuitList.contains(suit)) {
				hunshiSuitList.add(suit);
			}
		}
		if (logger.isDebugEnabled()) logger.debug("[获取套装列表] [可组成套装个数:" + hunshiSuitList.size() + "]");

		List<HunshiSuit> rmHunshiSuitList = new ArrayList<HunshiSuit>();
		for (int i = 0; i < hunshiSuitList.size(); i++) {
			for (int j = i + 1; j < hunshiSuitList.size(); j++) {
				if (hunshiSuitList.get(i).getSuitName().equals(hunshiSuitList.get(j).getSuitName())) {
					if (hunshiSuitList.get(i).getSuitColor() == hunshiSuitList.get(j).getSuitColor()) {
						rmHunshiSuitList.add(hunshiSuitList.get(i));
					} else {
						if (hunshiSuitList.get(i).getSuitColor() < hunshiSuitList.get(j).getSuitColor()) {
							rmHunshiSuitList.add(hunshiSuitList.get(i));
						} else {
							rmHunshiSuitList.add(hunshiSuitList.get(j));
						}
					}
				}
			}
		}
		if (logger.isDebugEnabled()) logger.debug("[获取套装列表] [移除套装个数:" + rmHunshiSuitList.size() + "]");
		for (HunshiSuit hs : rmHunshiSuitList) {
			if (hunshiSuitList.contains(hs)) {
				hunshiSuitList.remove(hs);
			}
		}
		if (logger.isWarnEnabled()) logger.warn("[获取套装列表] [生效套装个数:" + hunshiSuitList.size() + "]");

		/*
		 * List<Integer> looked = new ArrayList<Integer>(); // 已经遍历过的
		 * for (int i = 0; i < hunshi2Array.length; i++) {
		 * if (hunshi2Array[i] > 0 && !looked.contains(i)) {
		 * ArticleEntity ae = aem.getEntity(hunshi2Array[i]);
		 * if (ae != null && ae instanceof HunshiEntity) {
		 * HunshiEntity he = (HunshiEntity) ae;
		 * for (HunshiSuit suit : hunshiSuitMap.values()) {
		 * // HunshiSuit suit = hunshiSuitMap.get(suitId);
		 * if (suit != null) {
		 * if (suit.getSuitColor() == he.getColorType()) {
		 * String[] hunshi2Names = suit.getHunshi2Names();
		 * List<String> hunshi2List = Arrays.asList(hunshi2Names);
		 * if (hunshi2List != null && hunshi2List.contains(he.getArticleName())) {
		 * boolean find1 = true;
		 * for (int j = 0; j < hunshi2List.size(); j++) {
		 * if (find1 && !hunshi2List.get(j).equals(he.getArticleName())) {
		 * boolean find2 = false;
		 * for (int k = j + 1; k < hunshi2Array.length; k++) {
		 * if (hunshi2Array[k] > 0 && !looked.contains(k)) {
		 * ArticleEntity ae2 = aem.getEntity(hunshi2Array[i]);
		 * if (ae2 != null && ae2.getColorType() == ae.getColorType() && ae2 instanceof HunshiEntity) {
		 * HunshiEntity he2 = (HunshiEntity) ae;
		 * if (he2.getArticleName().equals(hunshi2List.get(j))) {
		 * looked.add(k);
		 * find2 = true;
		 * }
		 * }
		 * }
		 * }
		 * find1 = find1 && find2;
		 * }
		 * }
		 * if (find1 && hunshiSuitList.size() > 0) {
		 * for (HunshiSuit oldSuit : hunshiSuitList) {
		 * if (suit.getSuitName() == oldSuit.getSuitName() && suit.getSuitColor() > oldSuit.getSuitColor()) {
		 * hunshiSuitList.add(suit);
		 * }
		 * }
		 * }
		 * }
		 * }
		 * }
		 * }
		 * } else {
		 * if (logger.isWarnEnabled()) logger.warn("[获取套装出错] [物品为null或不是魂石] [魂石id:" + hunshi2Array[i] + "]");
		 * }
		 * }
		 * looked.add(i);
		 * }
		 */
		return hunshiSuitList;
	}

	/**
	 * 获取套装魂石所属的套装
	 * @param he
	 * @return
	 */
	// public HunshiSuit getSuit(HunshiEntity he) {
	// for (Integer id : hunshiSuitMap.keySet()) {
	// HunshiSuit suit = hunshiSuitMap.get(id);
	// if (suit.getSuitColor() == he.getColorType()) {
	// String[] hunshi2Names = suit.getHunshi2Names();
	// if (hunshi2Names != null && hunshi2Names.length > 0) {
	// for (String name : hunshi2Names) {
	// if (name.equals(he.getArticleName())) {
	// return suit;
	// }
	// }
	// }
	// }
	// }
	// return null;
	// }

	/**
	 * 判断新加某个套装石之后能否合成新套装
	 * @param hunshi2Array
	 * @param he
	 * @return
	 */
	public HunshiSuit canFormSuit(long[] hunshi2Array, HunshiEntity he) {
		HunshiSuit suit = getHunshiSuit(he);
		if (suit != null) {
			String[] hunshi2Names = suit.getHunshi2Names();
			List<String> hunshi2List = new ArrayList<String>();
			for (int i = 0; i < hunshi2Names.length; i++) {
				hunshi2List.add(hunshi2Names[i]);
			}
			int removeIndex = 0;
			if (hunshi2List != null) {
				for (int i = 0; i < hunshi2List.size(); i++) {
					if (hunshi2List.get(i).equals(he.getArticleName())) {
						removeIndex = i;
					}
				}
				hunshi2List.remove(removeIndex);
			}
			boolean hasAll = true;
			for (String name : hunshi2List) {
				boolean find = false;
				for (long id : hunshi2Array) {
					ArticleEntity ae = aem.getEntity(id);
					if (ae != null) {
						if (ae.getArticleName().equals(name)) {
							find = true;
						}
					}
				}
				hasAll = hasAll && find;
			}
			if (hasAll) {
				return suit;
			}
		}
		return null;
	}

	/**
	 * 判断拆除某个套装石后玩家是否拆分套装
	 * @param hunshi2Array
	 * @param he
	 * @return
	 */
	public HunshiSuit canSplitSuit(long[] hunshi2Array, HunshiEntity he) {
		HunshiSuit suit = getHunshiSuit(he);
		if (suit != null) {
			List<HunshiSuit> suitList = getHunshiSuitList(hunshi2Array);
			if (suitList != null && suitList.size() > 0) {
				for (HunshiSuit suit2 : suitList) {
					if (suit.getSuitId() == suit2.getSuitId()) {
						return suit;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 玩家升级时判断是否要开启魂石格子
	 * @param p
	 */
	// public void openCell(Player p) {
	// Soul soul = p.getCurrSoul();
	// if (soul != null) {
	// List<Long> horseList = soul.getHorseArr();
	// if (horseList != null && horseList.size() > 0) {
	// long id = horseList.get(0);
	// Horse h = HorseManager.getInstance().getHorseById(id, p);
	// boolean[] hunshiOpen = h.getHunshiOpen();
	// if (hunshiOpen == null) {
	// hunshiOpen = new boolean[16];
	// }
	// for (int i = 0; i < hunshiOpen.length; i++) {
	// if (hunshiOpen[i] == false && openHole.get(i).getPlayerLevel() <= p.getCurrSoul().getGrade() && openHole.get(i).getNeedSilver() == 0) {
	// hunshiOpen[i] = true;
	// }
	// }
	// h.setHunshiOpen(hunshiOpen);
	// boolean[] hunshi2Open = h.getHunshi2Open();
	// if (hunshi2Open == null) {
	// hunshi2Open = new boolean[16];
	// }
	// for (int i = 0; i < hunshi2Open.length; i++) {
	// if (hunshi2Open[i] == false && openHole.get(i).getPlayerLevel() <= p.getCurrSoul().getGrade() && openHole.get(i).getNeedSilver() == 0) {
	// hunshi2Open[i] = true;
	// }
	// }
	// h.setHunshi2Open(hunshi2Open);
	// }
	// }
	// }

	public static void fireBuff(Player player, String buffName, int buffLevel, long time) {
		try {
			BuffTemplateManager btm = BuffTemplateManager.getInstance();
			BuffTemplate bt = btm.getBuffTemplateByName(buffName);
			Buff buff = bt.createBuff(buffLevel);
			buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + time);
			buff.setCauser(player);
			player.placeBuff(buff);
			logger.debug("[fireBuff] [成功] [" + player.getLogString() + "] [" + buffName + "] [" + buffLevel + "]");
		} catch (Exception e) {
			logger.error("[fireBuff] [异常] [" + player.getLogString() + "] [" + buffName + "] [" + buffLevel + "]", e);
		}
	}

	public static void fireBuff(Fighter caster, Fighter target, String buffName, int buffLevel, long time) {
		try {
			if (caster != null && target != null) {
				BuffTemplateManager btm = BuffTemplateManager.getInstance();
				BuffTemplate bt = btm.getBuffTemplateByName(buffName);
				Buff buff = bt.createBuff(buffLevel);
				buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				buff.setInvalidTime(time);
				buff.setCauser(caster);
				
				target.placeBuff(buff);
				logger.debug("[fireBuff] [成功] [" + caster.getName() + "] [" + target.getName() + "] [" + buffName + "] [" + buffLevel + "]");
			}
		} catch (Exception e) {
			logger.error("[fireBuff] [异常] [" + buffName + "] [" + buffLevel + "]", e);
		}
	}

	/**
	 * 技能cd是否结束
	 * @param p
	 * @param ss
	 * @return
	 */
	public boolean cdTimeEnd(Player p, SuitSkill ss) {
		boolean cdTimeEnd = false; // 无cd=cd结束
		if (ss != null) {
			int index = ss.getSkillType().getIndex();
			long cdTime = 0;
			if (suitSkillGroupMap.get(ss.getSkillGroup()).getSkillCD() != 0) {// 如果技能有cd
				if (!p.suitSkillCDTime.containsKey(index)) {
					p.suitSkillCDTime.put(index, 0l);
				}
				cdTime = p.suitSkillCDTime.get(index);
				if (System.currentTimeMillis() > cdTime) {
					cdTimeEnd = true;
				}
			} else {
				cdTimeEnd = true;
			}
		}
		return cdTimeEnd;
	}

	/**
	 * 技能持续时间是否没结束
	 * @param p
	 * @param ss
	 * @return
	 */
	public boolean lastingTimeNotEnd(Player p, SuitSkill ss) {
		boolean lastingTimeNotEnd = false;// 无持续时间=持续时间没结束
		if (ss != null) {
			int index = ss.getSkillType().getIndex();
			long lastingTime = 0;
			if (ss.getLastingTime() != 0) {// 如果有持续时间
				if (!p.suitSkillLastingTime.containsKey(index)) {
					p.suitSkillLastingTime.put(index, 0l);
				}
				lastingTime = p.suitSkillLastingTime.get(index);
				if (System.currentTimeMillis() < lastingTime) {
					lastingTimeNotEnd = true;
				}
			} else {
				lastingTimeNotEnd = true;
			}
		}
		return lastingTimeNotEnd;
	}

	/**
	 * 无伤害攻击的处理
	 * @param p
	 * @param target
	 * @param target
	 *            是否单体攻击
	 */
	public void getDamage(Player p, Fighter target, boolean single) {
		try {
			logger.debug("[getDamage1] [" + p.getLogString() + "] [进入处理方法]");
			if (!p.isIsUpOrDown() || p.isFlying()) {
				return;
			}
			Horse h = HorseManager.getInstance().getHorseById(p.getRidingHorseId(), p);
			// for (long horseId : p.getCurrSoul().getHorseArr()) {
			// h = HorseManager.getInstance().getHorseById(horseId, p);
			// if (h != null) {
			// break;
			// }
			// }
			if (h != null) {
				List<Integer> hunshiSkills = h.getHunshiSkills();
				for (Integer skillId : hunshiSkills) {
					SuitSkill ss = suitSkillMap.get(skillId);
					if (ss != null) {
						int index = ss.getSkillType().getIndex();
						boolean cdTimeEnd = cdTimeEnd(p, ss); // 无cd=cd结束
						/*
						 * boolean lastingTimeNotEnd = lastingTimeNotEnd(p, ss);// 无持续时间=持续时间没结束
						 * if (!cdTimeEnd && !lastingTimeNotEnd) {
						 * return;
						 * } else if (cdTimeEnd) { // 超过CD时间，可以激活
						 * SuitSkillGroup sst = suitSkillGroupMap.get(ss.getType());
						 * if (sst.getSkillCD() != 0) {
						 * p.suitSkillCDTime.put(sst.getSkillGroup(), System.currentTimeMillis() + sst.getSkillCD());
						 * }
						 * if (ss.getLastingTime() != 0) {
						 * p.suitSkillLastingTime.put(index, System.currentTimeMillis() + ss.getLastingTime());
						 * }
						 * }
						 */
						if (!cdTimeEnd) {
							return;
						}
						SuitSkillGroup sst = suitSkillGroupMap.get(ss.getType());
						switch (index) {
						case 1:
							if (cdTimeEnd && target instanceof Monster && !(target instanceof BossMonster)) {
								logger.debug("[getDamage1] [" + p.getLogString() + "] [攻击怪物时有几率触发暴击]");
								// buff 攻击怪物时有几率触发暴击
								if (random.nextInt(100) < ss.getActiveRate()) {
									if (!ss.getBuffName().equals("null")) {
										p.suitSkillCDTime.put(sst.getSkillGroup(), System.currentTimeMillis() + sst.getSkillCD());
										fireBuff(p, ss.getBuffName(), ss.getBuffLevel(), ss.getLastingTime());
										logger.debug("[getDamage1] [" + p.getLogString() + "] [skillName:" + ss.getName() + "] [BuffName:" + ss.getBuffName() + "] [BuffLevel:" + ss.getBuffLevel() + "]");
									}
								}
							}
							break;
						case 9:
							logger.debug("[getDamage1] [" + p.getLogString() + "] [skillName:" + ss.getName() + "] [是否单体:" + single + "]");
							if (single && cdTimeEnd && target instanceof Player) {
								if (random.nextInt(100) < ss.getActiveRate()) {
									// buff
									logger.debug("[getDamage1] [" + p.getLogString() + "] [触发目标无法回血]");
									if (!ss.getBuffName().equals("null")) {
										p.suitSkillCDTime.put(sst.getSkillGroup(), System.currentTimeMillis() + sst.getSkillCD());
										fireBuff((Player) target, ss.getBuffName(), ss.getBuffLevel(), ss.getLastingTime());
										logger.debug("[getDamage1] [" + p.getLogString() + "] [BuffName:" + ss.getBuffName() + "] [BuffLevel:" + ss.getBuffLevel() + "]");
									}
								}
							}
							break;
						default:
							break;
						}
					}

				}
			}
		} catch (Exception e) {
			if (logger.isWarnEnabled()) logger.warn("[魂石套装技能] [异常]" + p.getLogString(), e);
			e.printStackTrace();
		}
	}

	/**
	 * 玩家为攻击方时伤害值处理
	 * @param p
	 * @param target
	 * @param damage
	 * @param single
	 *            是否只对单体有效
	 * @return
	 */
	public int getDamage(Player p, Fighter target, int damage, boolean single) {
		try {
			logger.debug("[getDamage2] [" + p.getLogString() + "] [进入处理方法] [target:" + target.toString() + "] [damage:" + damage + "] [single：" + true + "]");
			if (!p.isIsUpOrDown() || p.isFlying()) {
				logger.debug("[getDamage2] [" + p.getLogString() + "] [进入处理方法1]");
				return damage;
			}
			if (target == null) {
				logger.debug("[getDamage2] [" + p.getLogString() + "] [进入处理方法2]");
				return damage;
			}
			if (target instanceof BossMonster) {
				logger.debug("[getDamage2] [" + p.getLogString() + "] [进入处理方法3]");
				return damage;
			}
			Horse h = HorseManager.getInstance().getHorseById(p.getRidingHorseId(), p);
			if (h == null) {
				logger.debug("[getDamage2] [" + p.getLogString() + "] [进入处理方法4]");
				return damage;
			}
			if (h.getEnergy() == 0) {
				logger.debug("[getDamage2] [" + p.getLogString() + "] [进入处理方法5]");
				return damage;
			}
			List<Integer> hunshiSkills = h.getHunshiSkills();
			for (Integer skillId : hunshiSkills) {
				SuitSkill ss = suitSkillMap.get(skillId);
				if (ss != null) {
					int index = ss.getSkillType().getIndex();
					boolean cdTimeEnd = cdTimeEnd(p, ss); // 无cd=cd结束
					SuitSkillGroup sst = suitSkillGroupMap.get(ss.getType());
					switch (index) {
					case 5:
						float rate5 = (float) p.getHp() / p.getMaxHP();
						if (rate5 * 100 > ss.getLifeRate()) {
							float damage5 = damage * (1 + (float) ss.getAffectValue() / 100);
							logger.debug("[getDamage2] [" + p.getLogString() + "] [skillName:" + ss.getName() + "] [damage:" + damage + "] [计算damage:" + damage5 + "]");
							damage = (int) damage5;
						}
						break;
					case 7:
						logger.debug("[getDamage2] [" + p.getLogString() + "] [skillName:" + ss.getName() + "] [是否单体：" + single + "] [cdTimeEnd:" + cdTimeEnd + "] [damage:" + damage + "]");
						if (single && cdTimeEnd) {
							float rate7 = (float) target.getHp() / target.getMaxHP();
							logger.debug("[getDamage2] [" + p.getLogString() + "] [skillName:" + ss.getName() + "] [目标当前血量比：" + rate7 * 100 + "] [设定血量比:" + ss.getLifeRate() + "] [damage:" + damage + "]");
							if (rate7 * 100 < ss.getLifeRate()) {
								int rate = random.nextInt(100);
								logger.debug("[getDamage2] [" + p.getLogString() + "] [skillName:" + ss.getName() + "] [随机概率：" + rate7 * 100 + "] [设定概率:" + ss.getLifeRate() + "] [damage:" + damage + "]");
								if (rate < ss.getActiveRate()) {
									p.suitSkillCDTime.put(sst.getSkillGroup(), System.currentTimeMillis() + sst.getSkillCD());
									// 远程职业伤害减半
									if (p.getCareer() == 3 || p.getCareer() == 4) {
										damage = ss.getAffectValue() / 2;
									} else {
										damage = ss.getAffectValue();
									}
								}
							}
						}
						logger.debug("[getDamage2] [" + p.getLogString() + "] [skillName:" + ss.getName() + "] [计算后damage:" + damage + "]");
						break;
					default:
						break;
					}
				}
			}
		} catch (Exception e) {
			if (logger.isWarnEnabled()) logger.warn("[魂石套装技能] [异常2]" + p.getLogString(), e);
			e.printStackTrace();
		}
		return damage;
	}

	/**
	 * 玩家被攻击时伤害值处理
	 * @param p
	 * @param damage
	 * @return
	 */
	public int getDamage(Player p, int damage) {
		try {
			logger.debug("[getDamage3] [" + p.getLogString() + "] [进入处理方法]");
			if (!p.isIsUpOrDown() || p.isFlying()) {
				return damage;
			}
			Horse h = HorseManager.getInstance().getHorseById(p.getRidingHorseId(), p);
			if (h == null) {
				return damage;
			}
			if (h.getEnergy() == 0) {
				return damage;
			}
			List<Integer> hunshiSkills = h.getHunshiSkills();
			for (Integer skillId : hunshiSkills) {
				SuitSkill ss = suitSkillMap.get(skillId);
				if (ss != null) {
					int index = ss.getSkillType().getIndex();
					boolean cdTimeEnd = cdTimeEnd(p, ss); // 无cd=cd结束
					SuitSkillGroup sst = suitSkillGroupMap.get(ss.getType());
					switch (index) {
					case 2:
						if (cdTimeEnd) {
							float damage2 = p.getMaxHP() * (float) ss.getAffectValue() / 100;
							logger.debug("[getDamage3] [" + p.getLogString() + "] [skillName:" + ss.getName() + "] [damage:" + damage + "] [计算damage:" + damage2 + "]");
							if (damage > damage2) {
								p.suitSkillCDTime.put(sst.getSkillGroup(), System.currentTimeMillis() + sst.getSkillCD());
								damage = (int) damage2;
							}
							logger.debug("[getDamage3] [" + p.getLogString() + "] [skillName:" + ss.getName() + "] [减伤后damage:" + damage + "] [计算damage:" + damage2 + "]");
						}
						break;
					case 3:
						if (p.isHold() || p.isSilence() || p.isStun() || p.isJiansuState() || p.isCanNotIncHp() || p.getHitRateOther() > 0) {// stun jiansuState
							float damage3 = damage * (1 - (float) ss.getAffectValue() / 100);
							logger.debug("[getDamage3] [" + p.getLogString() + "] [skillName:" + ss.getName() + "] [damage:" + damage + "] [计算damage:" + damage3 + "]");
							damage = (int) damage3;
						}
						break;
					case 4:
						if (cdTimeEnd) {
							// buff自身生命值X低于X受伤害降低Y
							float rate4 = (float) p.getHp() / p.getMaxHP();
							if (rate4 * 100 < ss.getLifeRate()) {
								if (!ss.getBuffName().equals("null")) {
									p.suitSkillCDTime.put(sst.getSkillGroup(), System.currentTimeMillis() + sst.getSkillCD());
									fireBuff(p, ss.getBuffName(), ss.getBuffLevel(), ss.getLastingTime());
									logger.debug("[getDamage2] [" + p.getLogString() + "] [skillName:" + ss.getName() + "] [buffName:" + ss.getBuffName() + "] [buffLevel:" + ss.getBuffLevel() + "]");
								}
							}
						}
						break;
					case 6:
						if (cdTimeEnd) {
							// buff被攻击有几率增加移动速度
							if (random.nextInt(100) < ss.getActiveRate()) {
								if (!ss.getBuffName().equals("null")) {
									p.suitSkillCDTime.put(sst.getSkillGroup(), System.currentTimeMillis() + sst.getSkillCD());
									fireBuff(p, ss.getBuffName(), ss.getBuffLevel(), ss.getLastingTime());
									logger.debug("[getDamage3] [" + p.getLogString() + "] [skillName:" + ss.getName() + "] [buffName:" + ss.getBuffName() + "] [buffLevel:" + ss.getBuffLevel() + "]");
								}
							}
						}
						break;
					case 8:
						if (cdTimeEnd) {
							if (random.nextInt(100) < ss.getActiveRate()) {
								if (p.isCanNotIncHp()) {
									logger.debug("[无法回血状态] [屏蔽魂石套装技能8] [" + p.getLogString() + "] [血]");
								} else {
									p.suitSkillCDTime.put(sst.getSkillGroup(), System.currentTimeMillis() + sst.getSkillCD());
									float addHp = damage * (float) ss.getAffectValue() / 100;
									p.setHp(p.getHp() + (int) addHp);
									NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p.getId(), (byte) Event.HP_INCREASED, (int) addHp);
									p.addMessageToRightBag(req);
									damage = 0;
									logger.debug("[getDamage3] [" + p.getLogString() + "] [skillName:" + ss.getName() + "] [addHp:" + addHp + "]");
								}
							}
						}
						break;
					case 11:
						if (cdTimeEnd) {

						}
						break;
					default:
						break;
					}
				}
			}
		} catch (Exception e) {
			if (logger.isWarnEnabled()) logger.warn("[魂石套装技能] [异常3]" + p.getLogString(), e);
			e.printStackTrace();
		}
		return damage;
	}

	/**
	 * 套装技能奇经八脉、血脉凝结
	 * @param p
	 * @param buff
	 */
	public boolean dealWithBadBuff(Player p, Buff buff) {
		boolean buffEnd = false;
		try {
			if (p.isIsUpOrDown() && !p.isFlying()) {
				Horse h = HorseManager.getInstance().getHorseById(p.getRidingHorseId(), p);
				if (h != null) {
					List<Integer> hunshiSkills = h.getHunshiSkills();
					SuitSkill ss = null;
					if (hunshiSkills.contains(19)) {
						ss = suitSkillMap.get(19);
					} else if (hunshiSkills.contains(20)) {
						ss = suitSkillMap.get(20);
					}
					if (ss != null) {
						SuitSkillGroup sst = suitSkillGroupMap.get(ss.getType());
						boolean cdTimeEnd = cdTimeEnd(p, ss);
						float rate10 = (float) p.getHp() / p.getMaxHP();
						if (rate10 * 100 < ss.getLifeRate()) {
							if (cdTimeEnd) {
								p.suitSkillCDTime.put(sst.getSkillGroup(), System.currentTimeMillis() + sst.getSkillCD());
								p.suitSkillLastingTime.put(sst.getSkillGroup(), System.currentTimeMillis() + ss.getLastingTime());
								for (Buff b : p.getBuffs()) {
									if (!b.getTemplate().isAdvantageous()) {
										logger.debug("[设置buff失效时间为0] [" + b + "]");
										b.setInvalidTime(0);
									}
								}
								if (buff != null && !buff.getTemplate().isAdvantageous()) {
									buffEnd = true;
								}
								ParticleData[] particleDatas = new ParticleData[1];
								particleDatas[0] = new ParticleData(p.getId(), "坐骑魂石/免疫盾", 8 * 1000, 1, 1, 1);
								NOTICE_CLIENT_PLAY_PARTICLE_RES resp = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
								p.addMessageToRightBag(resp);
								logger.debug("[dealWithBadBuff] [" + p.getLogString() + "] [skillName:" + ss.getName() + "] [触发]");
							} else {
								if (lastingTimeNotEnd(p, ss)) {
									for (Buff b : p.getBuffs()) {
										if (!b.getTemplate().isAdvantageous()) {
											logger.debug("[设置buff失效时间为0] [" + b + "]");
											b.setInvalidTime(0);
										}
									}
									if (buff != null && !buff.getTemplate().isAdvantageous()) {
										buffEnd = true;
									}
									logger.debug("[dealWithBadBuff] [" + p.getLogString() + "] [skillName:" + ss.getName() + "] [持续]");
								}
							}
						}
					}

				}
			}
		} catch (Exception e) {
			if (logger.isWarnEnabled()) logger.warn("[魂石套装技能10] [异常]" + p.getLogString(), e);
		}
		return buffEnd;
	}

	/**
	 * 套装技能21,22
	 * @param p
	 */
	public void checkInfectSkill(Player p, int skillId) {
		try {
			p.setInfect1(false);
			p.setInfect2(false);
			if (p.isIsUpOrDown() && !p.isFlying()) {
				Horse h = HorseManager.getInstance().getHorseById(p.getRidingHorseId(), p);
				if (h != null) {
					List<Integer> hunshiSkills = h.getHunshiSkills();
					SuitSkill ss = null;
					if (hunshiSkills.contains(21)) {
						ss = suitSkillMap.get(21);
					} else if (hunshiSkills.contains(22)) {
						ss = suitSkillMap.get(22);
					}
					if (ss != null) {
						logger.debug("[dealWithInfectSkill] [" + p.getLogString() + "] [skillName:" + ss.getName() + "] [获得套装]");
						SuitSkillGroup sst = suitSkillGroupMap.get(ss.getType());
						boolean cdTimeEnd = cdTimeEnd(p, ss);
						if (cdTimeEnd) {
							if (ss.getSkillGroup() == 11 && skillId == 21) {
								p.setInfect1(true);
								p.suitSkillCDTime.put(sst.getSkillGroup(), System.currentTimeMillis() + sst.getSkillCD());
								p.suitSkillLastingTime.put(sst.getSkillGroup(), System.currentTimeMillis() + ss.getLastingTime());
								logger.debug("[dealWithInfectSkill] [" + p.getLogString() + "] [skillName:" + ss.getName() + "] [触发]");
							} else if (ss.getSkillGroup() == 12 && skillId == 22) {
								p.setInfect2(true);
								p.suitSkillCDTime.put(sst.getSkillGroup(), System.currentTimeMillis() + sst.getSkillCD());
								p.suitSkillLastingTime.put(sst.getSkillGroup(), System.currentTimeMillis() + ss.getLastingTime());
								logger.debug("[dealWithInfectSkill] [" + p.getLogString() + "] [skillName:" + ss.getName() + "] [触发]");
							}
						} else {
							logger.debug("[dealWithInfectSkill] [" + p.getLogString() + "] [skillName:" + ss.getName() + "] [CD中]");
						}
					}

				}
			}
		} catch (Exception e) {
			if (logger.isWarnEnabled()) logger.warn("[魂石套装技能11和12] [异常]" + p.getLogString(), e);
		}
	}

	public void dealWithInfectSkill(Fighter caster, Fighter target, Buff buff) {
		try {
			// 如果是控制状态且玩家有满足条件的坐骑魂石套装，则给施加者返加一个相同控制状态
			if (target instanceof Player) {
				Player p = (Player) target;
				if (caster != null) {
					if (caster instanceof Player || caster instanceof Pet) {
						if (buff instanceof Buff_JianShu || buff instanceof Buff_XuanYun || buff instanceof Buff_XuanYunAndWeak || buff instanceof Buff_XuanYunLiuXue || buff instanceof Buff_ZhuaRenAndXuanYun) {
							HunshiManager.getInstance().checkInfectSkill(p, 21);
							if (HorseManager.logger.isDebugEnabled()) HorseManager.logger.debug("[dealWithInfectSkill] [处理ControlBuff--1] [" + p.getLogString() + "] [buffName:" + buff.getTemplate().getName() + "]");
							if (p.isInfect1()) {// 以彼之道
								if (HorseManager.logger.isDebugEnabled()) HorseManager.logger.debug("[dealWithInfectSkill] [处理ControlBuff--11] [" + p.getLogString() + "] [buffName:" + buff.getTemplate().getName() + "]");
								fireBuff(target, caster, buff.getTemplateName(), buff.getLevel(), buff.getInvalidTime());
								if (HorseManager.logger.isDebugEnabled()) HorseManager.logger.debug("[dealWithInfectSkill] [" + p.getLogString() + "] [buffName:" + buff.getTemplate().getName() + "]");
							}
						}
						if (buff instanceof ControlBuff || buff instanceof Buff_ZhuaRenAndXuanYun) {
							HunshiManager.getInstance().checkInfectSkill(p, 22);
							if (HorseManager.logger.isDebugEnabled()) HorseManager.logger.debug("[dealWithInfectSkill] [处理ControlBuff--2] [" + p.getLogString() + "] [buffName:" + buff.getTemplate().getName() + "]");
							if (p.isInfect2()) {// 还之彼身
								if (HorseManager.logger.isDebugEnabled()) HorseManager.logger.debug("[dealWithInfectSkill] [处理ControlBuff--21] [" + p.getLogString() + "] [buffName:" + buff.getTemplate().getName() + "]");
								fireBuff(target, caster, buff.getTemplateName(), buff.getLevel(), buff.getInvalidTime());
								if (HorseManager.logger.isDebugEnabled()) HorseManager.logger.debug("[dealWithInfectSkill] [" + p.getLogString() + "] [buffName:" + buff.getTemplate().getName() + "]");
							}
						}
					} else {
						if (HorseManager.logger.isDebugEnabled()) HorseManager.logger.debug("[dealWithInfectSkill] [caster = null] [" + p.getLogString() + "] [buffName:" + buff.getTemplate().getName() + "]");
					}
				} else {
					if (HorseManager.logger.isDebugEnabled()) HorseManager.logger.debug("[dealWithInfectSkill] [施加者不是人或宠物] [" + p.getLogString() + "] [buffName:" + buff.getTemplate().getName() + "]");
				}
			}

		} catch (Exception e) {
			if (HorseManager.logger.isErrorEnabled()) HorseManager.logger.error("套装技能异常", e);
		}
	}
}
