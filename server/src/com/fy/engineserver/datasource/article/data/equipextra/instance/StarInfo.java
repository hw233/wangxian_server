package com.fy.engineserver.datasource.article.data.equipextra.instance;

import java.util.Arrays;
import java.util.List;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipextra.costEnum.AddLuckyCostEnum;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class StarInfo {
	/** 装备星级 */
	private int equipStar;
	/** 消耗的可加幸运值的物品数量 */
	private int[] costNums = new int[AddLuckyCostEnum.values().length];
	
	
	public StarInfo() {
		super();
	}

	public StarInfo(int equipStar) {
		super();
		this.equipStar = equipStar;
	}

	public int countLuckNum(EquipmentEntity ee) {
		int result = 0;
		for (int i=0; i<costNums.length; i++) {
			result += AddLuckyCostEnum.values()[i].getLuckNum(equipStar) * costNums[i];
		}
		return result;
	}
	
	/**
	 * 练星成功以后清除之前所有的记录
	 */
	public void succeed(EquipmentEntity ee) {
		int[] oldData = Arrays.copyOf(costNums, costNums.length);
		Arrays.fill(costNums, 0);
		if (ArticleManager.logger.isInfoEnabled()) {
			ArticleManager.logger.info("[练星成功] [" + ee.getId() + "] [star:"+ee.getStar()+"] [data:" + Arrays.toString(oldData) + "->" + Arrays.toString(costNums) + "]");
		}
	}
	/**
	 * 练星失败
	 * @param ee
	 * @param costList
	 */
	public void failure(EquipmentEntity ee, List<ArticleEntity> costList) {
		int[] oldData = Arrays.copyOf(costNums, costNums.length);
		for (ArticleEntity ae : costList) {
			Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
			AddLuckyCostEnum ace = AddLuckyCostEnum.valueOf(a.getName_stat(), ae.getColorType());
			if (ace != null) {
				costNums[ace.getIndex()] += 1;
			}
		}
		if (ArticleManager.logger.isInfoEnabled()) {
			ArticleManager.logger.info("[练星失败] [" + ee.getId() + "] [star:"+ee.getStar()+"] [data:" + Arrays.toString(oldData) + "->" + Arrays.toString(costNums) + "]");
		}
	}
	
	
	public int getEquipStar() {
		return equipStar;
	}
	public void setEquipStar(int equipStar) {
		this.equipStar = equipStar;
	}
	public int[] getCostNums() {
		return costNums;
	}
	public void setCostNums(int[] costNums) {
		this.costNums = costNums;
	}
}
