package com.fy.engineserver.sprite.horse2.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;

import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.horse2.model.BloodStarModel;
import com.fy.engineserver.sprite.horse2.model.HorseAttrModel;
import com.fy.engineserver.sprite.horse2.model.HorseBaseModel;
import com.fy.engineserver.sprite.horse2.model.HorseColorModel;
import com.fy.engineserver.sprite.horse2.model.HorseRankModel;
import com.fy.engineserver.sprite.horse2.model.HorseSkillModel;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.server.TestServerConfigManager;

public class Horse2Manager {
	public static Horse2Manager instance;
	
	public static Logger logger = HorseManager.logger;
	
	private String fileName;
	
	/** 基础花费等数据 */
	public HorseBaseModel baseModel ;
	/** 性格坐骑对应基础数值(计算用)  key=性格(1修罗，2影魅 3仙心 4九黎) (坐骑属性=(血脉星级+阶星级)*(成长+技能加成)+技能数值)*/
	public Map<Byte, HorseAttrModel> naturalBaseData = new HashMap<Byte, HorseAttrModel>();
	/** 坐骑阶星级  key=当前阶星级 */
	public Map<Integer, HorseRankModel> rankModelMap = new HashMap<Integer, HorseRankModel>();
	/** 血脉星级modelMap key=当前血脉星级   */
	public Map<Integer, BloodStarModel> bloodStarMap = new HashMap<Integer, BloodStarModel>();
	/** 性格颜色对应成长率 <颜色,成长率>> */
	public Map<Integer, HorseColorModel> naturalRateMap = new HashMap<Integer, HorseColorModel>();
	/** key=horseskillId */
	public Map<Integer, HorseSkillModel> horseSkillMap = new LinkedHashMap<Integer, HorseSkillModel>();
	/** 相关翻译等 (1技能界面描述   2普通技能icon  3高级技能icon) */
	public Map<Integer, String> translate = new HashMap<Integer, String>();
	/** 刷新坐骑技能需要临时道具 */
	public long[] articleId4Skill = new long[2];
	/** 培养所需道具临时id */
	public long rankPeiyangArticleId ;
	/** 技能格开启阶级 */
	public static String[] needRankLevel = Translate.FIELD_INDEX_NAME2;
	
	public static int[] levelLimit4Rank = new int[]{70,110,150};
	public static int[] maxRank = new int[]{30,40,60};	//70级才可升到30阶   110到40阶
	
	public static int[] levelLimit4Color = new int[]{70,110};
	public static int[] maxColor = new int[]{1,2};			//70级才可以升成蓝色  110到紫色
	public static final int horseEquiptRankLimit = 31;
	
	public Map<Integer, Long> callBossTimes = new Hashtable<Integer, Long>();
	
	public Map<Long, Long> playerCallBossMaps = new Hashtable<Long, Long>();
	
	public static String[] colorDes = new String[]{"庸品","良品","优品","名品","神品"};
	
	public void init() throws Exception {
		
		instance = this;
		initFile();
		ServiceStartRecord.startLog(this);
		
		{				//测试服去掉封印赐福
			if (TestServerConfigManager.isTestServer()) {
				PlayerManager.开启赐福标记 = false;
			}
		}
	}
	
	/**
	 * 
	 * @param skillId
	 * @return  -1配表没有    0初级  1高级
	 */
	public int getSkillType(int skillId) {
		HorseSkillModel hsm = this.horseSkillMap.get(skillId);
		if (hsm != null) {
			return hsm.getSkillType();
		}
		return -1;
	}
	
	/**
	 * 得到技能icon
	 * @param skillId
	 * @return
	 */
	public String getIconBySkillId(int skillId, int level) {
		HorseSkillModel model = horseSkillMap.get(skillId);
		if(model != null && model.getIcon().length > level) {
			return model.getIcon()[level];
		}
		return "";
	}
	
	/**
	 * 获得技能池技能列表
	 * @param levelType(0初级  1高级)
	 * @return
	 */
	public int[] getSkillsByLevel(int levelType) {
		int[] skills = new int[0];
		for(HorseSkillModel hsm : horseSkillMap.values()) {
			if(hsm.getSkillType() == levelType) {
				skills = Arrays.copyOf(skills, skills.length+1);
				skills[skills.length - 1] = hsm.getId();
			}
		}
		return skills;
	}
	/**
	 * 获得所有技能
	 * @return
	 */
	public int[] getAllSkill() {
		int[] skills = new int[0];
		for(HorseSkillModel hsm : horseSkillMap.values()) {
			skills = Arrays.copyOf(skills, skills.length+1);
			skills[skills.length - 1] = hsm.getId();
		}
		return skills;
	}
	
	
	public static String [] 阶 = {Translate.坐骑一阶,Translate.坐骑二阶,Translate.坐骑三阶,Translate.坐骑四阶,Translate.坐骑五阶,Translate.坐骑六阶,Translate.坐骑七阶,Translate.坐骑八阶,Translate.坐骑九阶, Translate.坐骑十阶, Translate.坐骑十一阶, Translate.坐骑十二阶, Translate.坐骑十三阶, Translate.坐骑十四阶};
	public static String [] 星 = {Translate.坐骑一星,Translate.坐骑二星,Translate.坐骑三星,Translate.坐骑四星,Translate.坐骑五星,Translate.坐骑六星,Translate.坐骑七星,Translate.坐骑八星,Translate.坐骑九星, Translate.坐骑十星};
	
	public static String getJieJiMess(int level) {
		String mess = "";
		int p = level % 10 - 1;
		if(p < 0) {
			p = 9;
		}
		int j = (level - 1) / 10;
		mess = 阶[j] + 星[p];
		return mess;
	}
	
	public static void main(String[] args) throws Exception {
//		Horse2Manager hm = new Horse2Manager();
//		hm.setFileName("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//horse2//horse2Data.xls");
//		hm.initFile();
//		System.out.println(Arrays.toString("a|b|ss|s,s".split("\\|")));
		for (int i=1; i<=80; i++) {
			System.out.println("["+i+"] [ "  + getJieJiMess(i) + "]");
		}
	}
	
	/**
	 * 获得坐骑属性（装备另算）
	 * @param natural 性格（1修罗 2影魅 3仙心 4九黎 ）
	 * @param colorType 颜色
	 * @param rankStar	阶星级
	 * @param bloodStar 血脉星级
	 * @param skills	宠物拥有技能
	 * @return
	 */
	public HorseAttrModel getNewHorseAttrModel(long horseId, byte natural, int colorType, int rankStar, int bloodStar, int[] skills, int[] skillLevels) {
		try {
			HorseAttrModel result = new HorseAttrModel();
			HorseAttrModel attrModel = naturalBaseData.get(natural);		//基础数值
			HorseRankModel rankModel = rankModelMap.get(rankStar);			//阶星级model
			BloodStarModel bloodStarModel = bloodStarMap.get(bloodStar);	//血脉星级model
			HorseColorModel growRateModel = naturalRateMap.get(colorType);	//颜色对应成长率
			List<Integer[]> tempAttrList = attrModel.getAttrNumAndType();
			for (int i=0; i<tempAttrList.size(); i++) {
				int totalValue = 0;
				int skillValue = 0;			//技能增加的具体数
				int xuemaiValue = (int) (tempAttrList.get(i)[1] * 0.1d * bloodStarModel.getBloodDataRate());
				int rankValue = (int) (tempAttrList.get(i)[1] * 0.3d * rankModel.getRankDataRate());
				double growRate = growRateModel.getGrowUpRate();
				double otherRate = 1;
				if (skills != null) {
					for (int k=0; k<skills.length; k++) {
						HorseSkillModel hsm = horseSkillMap.get(skills[k]);
						if (hsm != null) {
							if (hsm.getAddAttrType() != tempAttrList.get(i)[0]) {
								continue;
							}
							if (skillLevels[k] >= hsm.getAddNum().length) {
								logger.error("[新坐骑系统] [技能等级出错] [horseId :" + horseId + "] [skillId :" + skills[k] + "][skillLevel:" + skillLevels[k] + "]");	
								continue;
							}
							if (hsm.getAddType() == 0) {
								skillValue += hsm.getAddNum()[skillLevels[k]];
							} else if (hsm.getAddType() == 1) {
//								growRate += hsm.getAddNum()[skillLevels[k]] / 1000d;
								otherRate += hsm.getAddNum()[skillLevels[k]] / 1000d;
							} else {
								logger.error("[新坐骑系统] [未知的技能数值增加类型] [horseId :" + horseId + "] [skillId :" + skills[k] + "]");	
							}
						} else {
							logger.error("[新坐骑系统] [没有获取到坐骑对应技能model] [horseId :" + horseId + "] [skillId :" + skills[k] + "]");
						}
					}
				}
//				totalValue = (int) ((xuemaiValue+rankValue) * growRate + skillValue);
				totalValue = (int) (((xuemaiValue+rankValue) * growRate) * otherRate + skillValue);	//2014年9月10日12:12:17  技能调整为真的加百分比不是加成长
				result.setValue(tempAttrList.get(i)[0], totalValue);
//				if (totalValue <=0) {
////					if (logger.isDebugEnabled()) {
////						logger.debug("[新坐骑系统] [增加的基础数值为0] [增加类型:" + tempAttrList.get(i)[0] + "] [坐骑id:" + horseId + "] [natural:" + natural + "] [colorType:" + colorType + "] [rankStar"
////					+ rankStar + "] [bloodStar:" + bloodStar + "] [skills :" + Arrays.toString(skills) + "] [skillLevel:" + Arrays.toString(skillLevels) + "]");
////					}
//				}
			}
			return result;
 		} catch (Exception e) {
			logger.error("[新坐骑系统] [计算坐骑属性异常] [horseId:" + horseId + "] [natural:" + natural + "] [colorType:" + colorType + "] [rankStar"
					+ rankStar + "] [bloodStar:" + bloodStar + "] [skills :"  + Arrays.toString(skills) + "] [skillLevel:" + Arrays.toString(skillLevels) + "]", e);
			return null;
		}
	}
	
	private void initFile() throws Exception {
		File f = new File(fileName);
		if(!f.exists()){
			throw new Exception(fileName + " 配表不存在! " + f.getAbsolutePath());
		}
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(0);	//基础配置
		int rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				int index = 0;
				baseModel = new HorseBaseModel();
				baseModel.setFreeTimes4Rank(ReadFileTool.getInt(row, index++, logger));
				baseModel.setNeedArticle4Rank(ReadFileTool.getString(row, index++, logger));
				baseModel.setFreeTimes4Skill(ReadFileTool.getInt(row, index++, logger));
				baseModel.setExp4Rank(ReadFileTool.getInt(row, index++, logger));
				baseModel.setCost4Rank(ReadFileTool.getString(row, index++, logger));
				baseModel.setNums4Rank(ReadFileTool.getString(row, index++, logger));
				baseModel.setNeedArticle4NomalSkill(ReadFileTool.getString(row, index++, logger));
				baseModel.setNeedArticle4SpecSkill(ReadFileTool.getString(row, index++, logger));
				Article a = ArticleManager.getInstance().getArticleByCNname(baseModel.getNeedArticle4NomalSkill());
				Article a2 = ArticleManager.getInstance().getArticleByCNname(baseModel.getNeedArticle4SpecSkill());
				Article a3 = ArticleManager.getInstance().getArticleByCNname(baseModel.getNeedArticle4Rank());
				ArticleEntity ae = DefaultArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.目标系统临时, null, a.getColorType());
				ArticleEntity ae2 = DefaultArticleEntityManager.getInstance().createTempEntity(a2, true, ArticleEntityManager.目标系统临时, null, a2.getColorType());
				ArticleEntity ae3 = DefaultArticleEntityManager.getInstance().createTempEntity(a3, true, ArticleEntityManager.目标系统临时, null, a3.getColorType());
				articleId4Skill[0] = ae.getId();
				articleId4Skill[1] = ae2.getId();
				rankPeiyangArticleId = ae3.getId();
			}catch(Exception e) {
				throw new Exception("[" + fileName + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		sheet = workbook.getSheetAt(1);	//基础属性
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				HorseAttrModel hm = getHorseAttrModel(row);
				naturalBaseData.put((byte) hm.getNatural(), hm);
			}catch(Exception e) {
				throw new Exception("[" + fileName + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		sheet = workbook.getSheetAt(2);	//阶培养
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				HorseRankModel hm = getHorseRankModel(row);
				logger.warn("[记载坐骑星阶] [hm:"+hm+"]");
				rankModelMap.put(hm.getRankLevel(), hm);
			}catch(Exception e) {
				throw new Exception("[" + fileName + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		sheet = workbook.getSheetAt(3);	//血脉培养
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				BloodStarModel bsm = getBloodStarModel(row);
				bloodStarMap.put(bsm.getStartLevel(), bsm);
//				System.out.println(bsm);
			}catch(Exception e) {
				throw new Exception("[" + fileName + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		sheet = workbook.getSheetAt(4);	//颜色成长
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				HorseColorModel hcm = getHorseColorModel(row);
				naturalRateMap.put(hcm.getColorType(), hcm);
			}catch(Exception e) {
				throw new Exception("[" + fileName + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		sheet = workbook.getSheetAt(5);	//坐骑技能
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				HorseSkillModel hsm = getHorseSkillModel(row);
				horseSkillMap.put(hsm.getId(), hsm);
			}catch(Exception e) {
				throw new Exception("[" + fileName + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		
		sheet = workbook.getSheetAt(6);
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				int tempIndex = 0;
				int key = ReadFileTool.getInt(row, tempIndex++, logger);
				String value = ReadFileTool.getString(row, tempIndex++, logger);
				if (translate.containsKey(key)) {
					throw new Exception("[翻译sheet出错] [有相同的key:" + key + "]");
				}
				translate.put(key, value);
			}catch(Exception e) {
				throw new Exception("[" + fileName + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
	}
	
	private HorseSkillModel getHorseSkillModel(HSSFRow row) throws Exception {
		HorseSkillModel hsm = new HorseSkillModel();
		int index = 0;
		hsm.setId(ReadFileTool.getInt(row, index++, logger));
		hsm.setName(ReadFileTool.getString(row, index++, logger));
		hsm.setSkillType(ReadFileTool.getInt(row, index++, logger));
		hsm.setMaxLevel(ReadFileTool.getInt(row, index++, logger));
		hsm.setNeedArticleName(ReadFileTool.getString(row, index++, logger).split(","));
		hsm.setNeedArticleNum(ReadFileTool.getIntArrByString(row, index++, logger, ","));
		if(hsm.getNeedArticleName().length != hsm.getNeedArticleNum().length) {
			throw new Exception("[升级需要物品和数量不匹配]["+hsm.getNeedArticleName() + "][" + hsm.getNeedArticleNum()+"]");
		}
		hsm.setUpGradeRate(ReadFileTool.getIntArrByString(row, index++, logger, ","));
		if (hsm.getNeedArticleName().length != hsm.getNeedArticleNum().length) {
			throw new Exception("[升级需要物品和升级技能成功率数量不匹配]["+Arrays.toString(hsm.getNeedArticleName()) + "] [ " + Arrays.toString(hsm.getUpGradeRate()));
		}
		hsm.setProbabbly4Free(ReadFileTool.getDouble(row, index++, logger));
		hsm.setProbabbly4Nomal(ReadFileTool.getDouble(row, index++, logger));
		hsm.setProbabbly4Special(ReadFileTool.getDouble(row, index++, logger));
		String temp = ReadFileTool.getString(row, index++, logger);
		if (logger.isDebugEnabled()) {
			logger.debug("[temp] [" + temp + "]");
		}
		hsm.setAddAttrType(MagicWeaponConstant.mappingValueKey.get(temp));
		if(hsm.getAddAttrType() <= 0) {
			throw new Exception("[技能附加属性类型错误]["+temp + "]");
		}
		hsm.setAddType(ReadFileTool.getInt(row, index++, logger));
		hsm.setAddNum(ReadFileTool.getDoubleArrByString(row, index++, logger, ","));
		hsm.setDescription(ReadFileTool.getString(row, index++, logger));
		hsm.setIcon(ReadFileTool.getString(row, index++, logger).split("\\|"));
		
		long[] tempArticleIds = new long[hsm.getNeedArticleName().length];
		for (int i=0; i<hsm.getNeedArticleName().length; i++) {
			Article a = ArticleManager.getInstance().getArticleByCNname(hsm.getNeedArticleName()[i]);
			ArticleEntity ae = DefaultArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.目标系统临时, null, a.getColorType());
			tempArticleIds[i] = ae.getId();
		}
		hsm.setTempArticleIds(tempArticleIds);;
		return hsm;
	}
	
	private HorseColorModel getHorseColorModel(HSSFRow row) throws Exception {
		HorseColorModel hcm = new HorseColorModel();
		int index = 0;
		hcm.setColorType(ReadFileTool.getInt(row, index++, logger));
		hcm.setArticleCNNName(ReadFileTool.getString(row, index++, logger));
		hcm.setNeedNum(ReadFileTool.getInt(row, index++, logger));
		hcm.setGrowUpRate(ReadFileTool.getDouble(row, index++, logger));
		hcm.setMaxStar(ReadFileTool.getInt(row, index++, logger));
		Article a = ArticleManager.getInstance().getArticleByCNname(hcm.getArticleCNNName());
		ArticleEntity ae = DefaultArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.目标系统临时, null, a.getColorType());
		hcm.setTempArticleId(ae.getId());
		return hcm;
	}
	
	private BloodStarModel getBloodStarModel(HSSFRow row) {
		BloodStarModel bsm = new BloodStarModel();
		int index = 0;
		bsm.setStartLevel(ReadFileTool.getInt(row, index++, logger));
		bsm.setBloodDataRate(ReadFileTool.getDouble(row, index++, logger));
		bsm.setNeedBloodNum(ReadFileTool.getInt(row, index++, logger));
		return bsm;
	}
	
	private HorseRankModel getHorseRankModel(HSSFRow row) {
		HorseRankModel hrm = new HorseRankModel();
		int index = 0;
		hrm.setRankLevel(ReadFileTool.getInt(row, index++, logger));
		hrm.setRankDataRate(ReadFileTool.getDouble(row, index++, logger));
		hrm.setNeedExp(ReadFileTool.getInt(row, index++, logger));
		hrm.setFreeMulProbabbly(ReadFileTool.getIntArrByString(row, index++, logger, ","));
		hrm.setFreeMul(ReadFileTool.getIntArrByString(row,index++,logger, ","));
		hrm.setFree4Next(ReadFileTool.getInt(row, index++, logger));
		hrm.setCostMulProbabbly(ReadFileTool.getIntArrByString(row, index++, logger, ","));
		hrm.setCostMul(ReadFileTool.getIntArrByString(row, index++, logger, ","));
		hrm.setCost4Next(ReadFileTool.getInt(row, index++, logger));
		hrm.setOpenSkillNum(ReadFileTool.getInt(row, index++, logger));
		hrm.setAvatar(ReadFileTool.getString(row, index++, logger).split(","));		
		hrm.setMonthAvatar(ReadFileTool.getString(row, index++, logger).split(","));		
		hrm.setNewAvatar(ReadFileTool.getString(row, index++, logger).split(","));
		hrm.setPartical(ReadFileTool.getString(row, index++, logger).split(","));
		hrm.setSpeed(ReadFileTool.getInt(row, index++, logger));
		hrm.setHorseName(ReadFileTool.getString(row, index++, logger).split(","));
		return hrm;
	}
	
	private HorseAttrModel getHorseAttrModel(HSSFRow row) {
		int index = 0;
		HorseAttrModel hm = new HorseAttrModel();
		hm.setNatural(ReadFileTool.getInt(row, index++, logger));
		hm.setHp(ReadFileTool.getInt(row, index++, logger));
		hm.setMp(ReadFileTool.getInt(row, index++, logger));
		int attack = ReadFileTool.getInt(row, index++, logger);
		hm.setPhyAttack(attack);
		hm.setMagicAttack(attack);
		int defance = ReadFileTool.getInt(row, index++, logger);
		hm.setPhyDefance(defance);
		hm.setMagicDefance(defance);
		hm.setBreakDefance(ReadFileTool.getInt(row, index++, logger));
		hm.setHit(ReadFileTool.getInt(row, index++, logger));
		hm.setDodge(ReadFileTool.getInt(row, index++, logger));
		hm.setAccurate(ReadFileTool.getInt(row, index++, logger));
		hm.setCritical(ReadFileTool.getInt(row, index++, logger));
		hm.setCriticalDefence(ReadFileTool.getInt(row, index++, logger));
		hm.setFireAttack(ReadFileTool.getInt(row, index++, logger));
		hm.setBlizzAttack(ReadFileTool.getInt(row, index++, logger));
		hm.setWindAttack(ReadFileTool.getInt(row, index++, logger));
		hm.setThunderAttack(ReadFileTool.getInt(row, index++, logger));
		hm.setFireDefance(ReadFileTool.getInt(row, index++, logger));
		hm.setBlizzDefance(ReadFileTool.getInt(row, index++, logger));
		hm.setWindDefance(ReadFileTool.getInt(row, index++, logger));
		hm.setThunderDefance(ReadFileTool.getInt(row, index++, logger));
		hm.setFireBreak(ReadFileTool.getInt(row, index++, logger));
		hm.setBlizzBreak(ReadFileTool.getInt(row, index++, logger));
		hm.setWindBreak(ReadFileTool.getInt(row, index++, logger));
		hm.setThunderBreak(ReadFileTool.getInt(row, index++, logger));
		return hm;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
}
