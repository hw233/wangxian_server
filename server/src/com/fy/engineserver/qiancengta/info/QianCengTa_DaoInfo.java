package com.fy.engineserver.qiancengta.info;

import java.util.ArrayList;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.qiancengta.QianCengTa_Ta;
/**
 * 千层塔    道的数据
 * 
 *
 */
public class QianCengTa_DaoInfo {
	public static int[] QianCengDaoExp = new int[]{324576, 634271, 1090741, 1718635, 2540881, 3578689};
	public static int[] QianCengDaoHiddenExp = new int[]{324576, 634271, 1090741, 1718635, 2540881, 3578689};
	
	public static String[] QianCengDaoName = new String[]{Translate.text_qiancengta_DaoNames[0], Translate.text_qiancengta_DaoNames[1], Translate.text_qiancengta_DaoNames[2], Translate.text_qiancengta_DaoNames[3], Translate.text_qiancengta_DaoNames[4], Translate.text_qiancengta_DaoNames[5]};
//	public static String[] QianCengDaoName = new String[]{"地狱道", "饿鬼道", "畜生道", "修罗道", "人间道", "天道"};
	
	private ArrayList<QianCengTa_CengInfo> cengList = new ArrayList<QianCengTa_CengInfo>();		//道中层的数据
	
	private QianCengTa_CengInfo hiddenCeng = null;				//隐藏层
	
	private int daoIndex;										//第几道
	
	//奖励物品名字
	private ArrayList<String> rewardArticleNames = new ArrayList<String>();
	//奖励物品颜色
	private ArrayList<Integer> rewardArticleColors = new ArrayList<Integer>();

	//临时物品，提供给客户端查看泡泡用
	private ArrayList<ArticleEntity> tempEntitys = new ArrayList<ArticleEntity>();
	
	private String[] firstRewardArticleNames;
	private int[] firstRewardArticleNums;
	private int[] firstRewardArticleColors;
	private boolean[] firstRewardArticleBinds;
	private ArticleEntity[] firstTempEntitys;
	
	public void setCengList(ArrayList<QianCengTa_CengInfo> cengList) {
		this.cengList = cengList;
	}

	public ArrayList<QianCengTa_CengInfo> getCengList() {
		return cengList;
	}

	public ArrayList<Integer> getRewardArticleColors() {
		return rewardArticleColors;
	}

	public void setRewardArticleColors(ArrayList<Integer> rewardArticleColors) {
		this.rewardArticleColors = rewardArticleColors;
	}

	public void setHiddenCeng(QianCengTa_CengInfo hiddenCeng) {
		this.hiddenCeng = hiddenCeng;
	}

	public QianCengTa_CengInfo getHiddenCeng() {
		return hiddenCeng;
	}

	public void setDaoIndex(int daoIndex) {
		this.daoIndex = daoIndex;
	}

	public int getDaoIndex() {
		return daoIndex;
	}
	
	public String getMapName(){
		return QianCengDaoName[daoIndex];
	}

	public void setRewardArticleNames(ArrayList<String> rewardArticleNames) {
		this.rewardArticleNames = rewardArticleNames;
	}

	public ArrayList<String> getRewardArticleNames() {
		return rewardArticleNames;
	}

	public void setTempEntitys(ArrayList<ArticleEntity> tempEntitys) {
		this.tempEntitys = tempEntitys;
	}

	public ArrayList<ArticleEntity> getTempEntitys() {
		if (rewardArticleNames.size() > 0 && tempEntitys.size() <= 0) {
			for (int i = 0; i< rewardArticleNames.size(); i++) {
				String name = rewardArticleNames.get(i);
				try {
					Article a = ArticleManager.getInstance().getArticle(name);
					ArticleEntity temp = ArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.CREATE_REASON_QIANCENGTA, null, rewardArticleColors.get(i));
					tempEntitys.add(temp);
				} catch (Exception e) {
					QianCengTa_Ta.logger.error("创建临时物品出错:" + name + "~" + i, e);
				}
			}
		}
		return tempEntitys;
	}
	
	public long[] getTempEntityIds() {
		ArrayList<ArticleEntity> entitys = getTempEntitys();
		long[] ids = new long[entitys.size()];
		for (int i = 0; i < entitys.size(); i++) {
			ArticleEntity en = entitys.get(i);
			ids[i] = en.getId();
		}
		return ids;
	}

	public void setFirstRewardArticleNames(String[] firstRewardArticleNames) {
		this.firstRewardArticleNames = firstRewardArticleNames;
	}

	public String[] getFirstRewardArticleNames() {
		return firstRewardArticleNames;
	}

	public void setFirstRewardArticleNums(int[] firstRewardArticleNums) {
		this.firstRewardArticleNums = firstRewardArticleNums;
	}

	public int[] getFirstRewardArticleNums() {
		return firstRewardArticleNums;
	}

	public void setFirstRewardArticleColors(int[] firstRewardArticleColors) {
		this.firstRewardArticleColors = firstRewardArticleColors;
	}

	public int[] getFirstRewardArticleColors() {
		return firstRewardArticleColors;
	}

	public void setFirstRewardArticleBinds(boolean[] firstRewardArticleBinds) {
		this.firstRewardArticleBinds = firstRewardArticleBinds;
	}

	public boolean[] getFirstRewardArticleBinds() {
		return firstRewardArticleBinds;
	}

	public void setFirstTempEntitys(ArticleEntity[] firstTempEntitys) {
		this.firstTempEntitys = firstTempEntitys;
	}

	public ArticleEntity[] getFirstTempEntitys() {
		if (firstTempEntitys == null) {
			firstTempEntitys = new ArticleEntity[firstRewardArticleNames.length];
			for (int i = 0; i < firstRewardArticleNames.length; i++) {
				try {
					Article a = ArticleManager.getInstance().getArticle(firstRewardArticleNames[i]);
					ArticleEntity temp = ArticleEntityManager.getInstance().createTempEntity(a, firstRewardArticleBinds[i], ArticleEntityManager.CREATE_REASON_QIANCENGTA, null, firstRewardArticleColors[i]);
					firstTempEntitys[i] = temp;
				} catch (Exception e) {
					QianCengTa_Ta.logger.error("创建临时物品出错:" + firstRewardArticleNames[i] + "~" + i, e);
				}
			}
		}
		return firstTempEntitys;
	}
	
	public long[] getFirstEntityIDs() {
		ArticleEntity[] entitys = getFirstTempEntitys();
		long[] ids = new long[entitys.length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = entitys[i].getId();
		}
		return ids;
	}
}
