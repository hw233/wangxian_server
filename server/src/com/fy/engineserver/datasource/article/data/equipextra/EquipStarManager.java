package com.fy.engineserver.datasource.article.data.equipextra;

import java.io.File;
import java.util.Map;

import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipextra.costEnum.AddLuckyCostEnum;
import com.fy.engineserver.datasource.article.data.equipextra.instance.EquipExtraData;
import com.fy.engineserver.datasource.article.data.equipextra.module.EquipStarModule;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.util.ExcelDataLoadUtil;
import com.fy.engineserver.util.server.TestServerConfigManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class EquipStarManager {

	private static EquipStarManager inst;
	
	private String fileName;
	/** key=物品索引  */
	public Map<Integer, EquipStarModule> dataModule = null;
	
	public static SimpleEntityManager<EquipExtraData> em;
	
	public static boolean 是否开启幸运值系统 = false;
	
	public static EquipStarManager getInst() {
		return inst;
	}
	
	@SuppressWarnings("unchecked")
	public void init() throws Exception {
		inst = this;
		File f =  new File(fileName);
		dataModule = (Map<Integer, EquipStarModule>) ExcelDataLoadUtil.loadExcelData(f, 0, EquipStarModule.class, ArticleManager.logger);
		for (EquipStarModule m : dataModule.values()) {
			if (AddLuckyCostEnum.valueOf(m.getArticleCNNName(), m.getColorType()) == null) {
				throw new Exception("[配置的物品服务器枚举没有实现] [" + m + "]");
			}
		}
		em = SimpleEntityManagerFactory.getSimpleEntityManager(EquipExtraData.class);
		if (TestServerConfigManager.isTestServer()) {
			是否开启幸运值系统 = true;
		}
	}
	
	public void destory() {
		try {
			em.destroy();
		} catch (Exception e) {
			
		}
	}
	
	public EquipExtraData getEntity(EquipmentEntity ee) {
		if (!是否开启幸运值系统) {
			return null;
		}
		if (ee.getExtraData() != null) {
			return ee.getExtraData();
		}
		if (ee.getStar() < 26 && ee.getOnceMaxStar() < 26) {
			return null;
		}
		EquipExtraData extraData = null;
		try {
			extraData = em.find(ee.getId());
			if (extraData == null) {
				extraData = new EquipExtraData();
				extraData.setId(ee.getId());
				em.notifyNewObject(extraData);
			}
		} catch (Exception e) {
			ArticleManager.logger.warn("[获取装备练星信息] [异常] [" + ee.getId() + "]", e);
		}
		ee.setExtraData(extraData);
		return ee.getExtraData();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
