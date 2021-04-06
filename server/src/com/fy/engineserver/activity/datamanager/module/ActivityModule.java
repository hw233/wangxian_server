package com.fy.engineserver.activity.datamanager.module;

import java.util.Arrays;

import com.fy.engineserver.activity.datamanager.ActivityDataManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.util.SimpleKey;
/**
 * 活动界面展示信息
 */
public class ActivityModule {
	/** 活动名 */
	@SimpleKey
	private String activityName;
	/** 活动显示名 */
	private String activityShowName;
	/** 描述 */
	private String description;
	/** 活动产出的物品统计名 */
	private String[] articleCNNNames;
	/** 是否绑定 */
	private boolean[] articleBindstate;
	/** 寻路npc地图 */
	private String mapName;
	/** 国家 */
	private int country;
	/** 寻路npc坐标 */
	private int[] npcPoint;
	/** npc名 */
	private String npcName;
	/** 临时物品id，有玩家获取活动信息时才会创建 */
	private long[] articleIds;
	/** 显示给客户端的描述 */
	private String des4client;
	/** 是否在活动时间 */
	private boolean inActivityTime;
	/** 等级限制 */
	private int levellimit;
	/** 物品颜色 */
	private int[] colortype;	
	@Override
	public String toString() {
		return "ActivityModule [activityName=" + activityName + ", activityShowName=" + activityShowName + ", description=" + description + ", articleCNNNames=" + Arrays.toString(articleCNNNames) + ", articleBindstate=" + Arrays.toString(articleBindstate) + ", mapName=" + mapName + ", country=" + country + ", npcPoint=" + Arrays.toString(npcPoint) + ", npcName=" + npcName + ", articleIds=" + Arrays.toString(articleIds) + ", des4client=" + des4client + ", inActivityTime=" + inActivityTime + ", levellimit=" + levellimit + "]";
	}

	public void buildData(String openDes) {
		try {
			if (articleIds == null && articleCNNNames != null && articleCNNNames.length > 0) {
				articleIds = new long[articleCNNNames.length];
				for (int i=0; i<articleCNNNames.length; i++) {
					Article a = ArticleManager.getInstance().getArticle(articleCNNNames[i]);
					if (a == null) {
						if (!ActivityDataManager.getInst().errorArticle.contains(articleCNNNames[i])) {
							ActivityDataManager.getInst().errorArticle.add(articleCNNNames[i]);
						}
						continue;
					}
					int ct = colortype[i] < 0 ? a.getColorType() : colortype[i];
					ArticleEntity ae = ArticleEntityManager.getInstance().createTempEntity(a, articleBindstate[i], 1, null, ct);
					articleIds[i] = ae.getId();
				}
			}
			des4client = String.format(description, openDes);
		} catch (Exception e) {
			ActivityDataManager.logger.warn("[创建新活动面板信息异常] [" + this.toString() + "] [" + openDes + "]", e);
		}
	}
	
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String[] getArticleCNNNames() {
		return articleCNNNames;
	}
	public void setArticleCNNNames(String[] articleCNNNames) {
		this.articleCNNNames = articleCNNNames;
	}
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public int[] getNpcPoint() {
		return npcPoint;
	}
	public void setNpcPoint(int[] npcPoint) {
		this.npcPoint = npcPoint;
	}
	public long[] getArticleIds() {
		return articleIds;
	}
	public void setArticleIds(long[] articleIds) {
		this.articleIds = articleIds;
	}
	public String getDes4client() {
		return des4client;
	}
	public void setDes4client(String des4client) {
		this.des4client = des4client;
	}

	public String getActivityShowName() {
		return activityShowName;
	}

	public void setActivityShowName(String activityShowName) {
		this.activityShowName = activityShowName;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public String getNpcName() {
		return npcName;
	}

	public void setNpcName(String npcName) {
		this.npcName = npcName;
	}

	public boolean isInActivityTime() {
		return inActivityTime;
	}

	public void setInActivityTime(boolean inActivityTime) {
		this.inActivityTime = inActivityTime;
	}

	public boolean[] getArticleBindstate() {
		return articleBindstate;
	}

	public void setArticleBindstate(boolean[] articleBindstate) {
		this.articleBindstate = articleBindstate;
	}

	public int getLevellimit() {
		return levellimit;
	}

	public void setLevellimit(int levellimit) {
		this.levellimit = levellimit;
	}

	public int[] getColortype() {
		return colortype;
	}

	public void setColortype(int[] colortype) {
		this.colortype = colortype;
	}
}
