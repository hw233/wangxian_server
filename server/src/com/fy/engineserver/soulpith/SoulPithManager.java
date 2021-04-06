package com.fy.engineserver.soulpith;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.disaster.module.TempTranslate;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.soulpith.GourdArticle;
import com.fy.engineserver.datasource.article.data.soulpith.SoulPithArticleLevelData;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.soulpith.module.ArtificeModule;
import com.fy.engineserver.soulpith.module.SoulLevelupExpModule;
import com.fy.engineserver.soulpith.module.SoulPithAritlcePropertyModule;
import com.fy.engineserver.soulpith.module.SoulPithExtraAttrModule;
import com.fy.engineserver.soulpith.module.SoulPithLevelModule;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.util.ExcelDataLoadUtil;
import com.fy.engineserver.util.ServiceStartRecord;

/**
 * 灵髓 
 * 
 * @date on create 2016年3月16日 下午2:57:18
 */
public class SoulPithManager {
	public static Logger logger = LoggerFactory.getLogger(SoulPithManager.class);
	
	public static SoulPithManager inst;
	
	private String fileName;
	/** 玩家等级对应的灵根信息  key=playerLevel */
	public Map<Integer, SoulPithLevelModule> levelModules;
	/** 灵髓点数激活属性 */
	public List<SoulPithExtraAttrModule> extraAttrs;
	/** 灵髓升级经验 */
	public Map<Integer, SoulLevelupExpModule> soulLevelModules;
	/** 翻译 */
	public Map<String, String> translate = new HashMap<String, String>();
	/** 炼化 */
	public Map<Integer, ArtificeModule> articleModules;
	/** 灵髓宝石属性（具体需要根据颜色等级计算） */
//	public Map<Integer, SoulLevelPropertyModule> propertys;
	/** 灵髓宝石对应的属性信息 */
	public Map<String, SoulPithAritlcePropertyModule> soulArticleDatas = new HashMap<String, SoulPithAritlcePropertyModule>();
	/** 吞噬消耗 */
//	public List<DevourCostModule> devourCostList = new ArrayList<DevourCostModule>();
	
	public static long[] gourdIds;
	public static long[] addExps;
	
	public static SoulPithManager getInst() {
		return inst;
	}
	
	public static void main(String[] args) throws Exception {
		SoulPithManager m = new SoulPithManager();
		m.setFileName("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//soulpith//soulpith.xls");
		m.init();
	}
	
	public void init() throws Exception {
		
		inst = this;
		this.loadFile();
		ServiceStartRecord.startLog(this);
		if (SoulPithConstant.gourdNames.length > 0) {
			gourdIds = new long[SoulPithConstant.gourdNames.length];
			addExps = new long[SoulPithConstant.gourdNames.length];
			for (int i=0; i<SoulPithConstant.gourdNames.length; i++) {
				Article a = ArticleManager.getInstance().getArticle(SoulPithConstant.gourdNames[i]);
				ArticleEntity ae = ArticleEntityManager.getInstance().createTempEntity(a, true, 1, null, a.getColorType());
				gourdIds[i] = ae.getId();
				addExps[i] = ((GourdArticle)a).getAddExp();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadFile() throws Exception{
		File file = new File(fileName);
		levelModules = (Map<Integer, SoulPithLevelModule>) ExcelDataLoadUtil.loadExcelData(file, 0, SoulPithLevelModule.class, logger);
		extraAttrs = (List<SoulPithExtraAttrModule>) ExcelDataLoadUtil.loadExcelDataAsList(file, 1, SoulPithExtraAttrModule.class, logger);
		List<SoulPithAritlcePropertyModule> tempList = (List<SoulPithAritlcePropertyModule>) ExcelDataLoadUtil.loadExcelDataAsList(file, 2, SoulPithAritlcePropertyModule.class, logger);
		for (int i=0; i<tempList.size(); i++) {
			SoulPithAritlcePropertyModule temp = tempList.get(i);
			SoulPithAritlcePropertyModule module = soulArticleDatas.get(temp.getArticleCNNName());
			if (module == null) {
				module = new SoulPithAritlcePropertyModule();
			}
			SoulPithArticleLevelData tempData = temp.getSoulPithArticleLevelData();
			module.getLevelData().put(tempData.getLevel(), tempData);
			soulArticleDatas.put(temp.getArticleCNNName(), module);
		}
		articleModules = (Map<Integer, ArtificeModule>) ExcelDataLoadUtil.loadExcelData(file, 3, ArtificeModule.class, logger);
		soulLevelModules = (Map<Integer, SoulLevelupExpModule>) ExcelDataLoadUtil.loadExcelData(file, 4, SoulLevelupExpModule.class, logger);
//		devourCostList = (List<DevourCostModule>) ExcelDataLoadUtil.loadExcelDataAsList(file, 6, SoulPithAritlcePropertyModule.class, logger);
		List<TempTranslate> tempTrans = (List<TempTranslate>) ExcelDataLoadUtil.loadExcelDataAsList(file, 5, TempTranslate.class, logger);
		for (int i=0; i<tempTrans.size(); i++) {
			translate.put(tempTrans.get(i).getKey(), tempTrans.get(i).getValue());
		}
		
	}
	/**
	 * 获取玩家已开启的灵根镶嵌个数
	 * @param player
	 * @return
	 */
	public static int getPlayerFillNum(Player player, int soulType) {
		if (getPlayerLevel(player, soulType) < SoulPithConstant.playerLevels[0]) {
			return 0;
		}
		int result = SoulPithConstant.openSoulNums[0];
		for (int i=0; i<SoulPithConstant.playerLevels.length; i++) {
			if (getPlayerLevel(player, soulType) >= SoulPithConstant.playerLevels[i]) {
				result = SoulPithConstant.openSoulNums[i];
			} else {
				break;
			}
		}
		return result;
	}
	/**
	 * 获取玩家等级，如果有需求，可能会改为玩家level不用soulevel
	 * @param player
	 * @return
	 */
	public static int getPlayerLevel(Player player, int soulType) {
		Soul s = player.getSoul(soulType);
		if (s != null) {
			return s.getGrade();
		}
		return 0;
	}
	
	public String getTranslate(String key, Object ...obj) {
		try {
			String str = translate.get(key);
			if (str == null) {
				return key;
			}
			if (obj.length <= 0) {
				return str;
			}
			return String.format(str,obj);
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug("[获取翻译] [异常] [key:" + key + "] [" + Arrays.toString(obj) + "]", e);
			}
		}
		return key;
	}
	

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
