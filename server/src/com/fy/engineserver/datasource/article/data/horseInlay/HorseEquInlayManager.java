package com.fy.engineserver.datasource.article.data.horseInlay;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.fy.engineserver.activity.disaster.module.TempTranslate;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.horseInlay.module.HorseEquBaseModule;
import com.fy.engineserver.datasource.article.data.horseInlay.module.InlayModule;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.util.ExcelDataLoadUtil;

/**
 * 坐骑装备打孔
 */
public class HorseEquInlayManager {
	/** 坐骑装备最多可开孔数量 */
	public static final int MAX_INLAY_NUM = 5;
	
	public static Logger logger = HorseManager.logger;
	
	private String fileName;
	
	private static HorseEquInlayManager instance;
	
	public HorseEquBaseModule baseModule;
	
	public Map<Integer, InlayModule> inlayMap;
	
	public Map<String, String> translate = new HashMap<String, String>();
	
	public static HorseEquInlayManager getInst() {
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	public void init() throws Exception {
		instance = this;
		File file = new File(fileName);
		List<HorseEquBaseModule> list = (List<HorseEquBaseModule>) ExcelDataLoadUtil.loadExcelDataAsList(file, 0, HorseEquBaseModule.class, logger);
		baseModule = list.get(0);
		inlayMap = (Map<Integer, InlayModule>) ExcelDataLoadUtil.loadExcelData(file, 1, InlayModule.class, logger);
		List<TempTranslate> tempList = (List<TempTranslate>) ExcelDataLoadUtil.loadExcelDataAsList(file, 2, TempTranslate.class, logger);
		for (TempTranslate t : tempList) {
			translate.put(t.getKey(), t.getValue());
		}
		for (InlayModule m : inlayMap.values()) {
			Article a = ArticleManager.getInstance().getArticle(m.getCost4punch()[0]);
			ArticleEntity ae = ArticleEntityManager.getInstance().createTempEntity(a, false, 1, null, a.getColorType());
			m.setTempAeId(ae.getId());
		}
	}
	
	
	public InlayModule getModuleByIndex(int index) {
		InlayModule m = inlayMap.get(index);
		return m;
	}
	
	public String getTranslate(String key, Object...args) {
		String str = translate.get(key);
		if (str == null) {
			return key;
		}
		if (args != null && args.length > 0) {
			return String.format(str, args);
		} 
		return str;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
