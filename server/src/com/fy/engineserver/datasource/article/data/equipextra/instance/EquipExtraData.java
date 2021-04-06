package com.fy.engineserver.datasource.article.data.equipextra.instance;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipextra.EquipStarManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 装备其他属性
 */
@SimpleEntity
public class EquipExtraData {
	@SimpleId
	private long id;			//物品id
	@SimpleVersion
	private int version;
	/** 练星消耗数据 */
	private List<StarInfo> starInfos = new ArrayList<StarInfo>();
	/**
	 * 练星成功
	 * @param ee
	 */
	public void success(EquipmentEntity ee) {
		if (ee.getStar() < 26) {		//只有羽化5重开始才会有幸运值
			return; 
		}
		for (StarInfo info : starInfos) {
			if (info.getEquipStar() == ee.getStar()) {
				info.succeed(ee);
				break;
			}
		}
		EquipStarManager.em.notifyFieldChange(this, "starInfos");
	}	
	
	public void failure(EquipmentEntity ee, List<ArticleEntity> costList) {
		if (ee.getStar() < 26) {		//只有羽化5重开始才会有幸运值
			return;
		}
		boolean exist = false;
		for (StarInfo info : starInfos) {
			if (info.getEquipStar() == ee.getStar()) {
				info.failure(ee, costList);
				exist = true;
				break;
			}
		}
		if (!exist) {
			StarInfo info = new StarInfo(ee.getStar());
			info.failure(ee, costList);
			starInfos.add(info);
		}
		EquipStarManager.em.notifyFieldChange(this, "starInfos");
	}
	/**
	 * 返回为概率。 练星成功率最高为 ArticleManager.TOTAL_LUCK_VALUE  根据幸运值调整
	 * @param ee
	 * @return
	 */
	public int getLuckNum(EquipmentEntity ee) {
		if (ee.getStar() < 26) {		//只有羽化5重开始才会有幸运值
			return 0;
		}
		for (StarInfo info : starInfos) {
			if (info.getEquipStar() == ee.getStar()) {
				int result = info.countLuckNum(ee);
				if (result >= ArticleManager.TOTAL_LUCK_VALUE) {
					result = ArticleManager.TOTAL_LUCK_VALUE;
				}
				return result;		
			}
		}
		return 0;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public List<StarInfo> getStarInfos() {
		return starInfos;
	}
	public void setStarInfos(List<StarInfo> starInfos) {
		this.starInfos = starInfos;
		EquipStarManager.em.notifyFieldChange(this, "starInfos");
	}
}
