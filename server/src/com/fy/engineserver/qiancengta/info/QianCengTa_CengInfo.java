package com.fy.engineserver.qiancengta.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.qiancengta.QianCengTa_Ta;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager.MonsterTempalte;

/**
 * 千层塔  层的数据
 * 
 *
 */
public class QianCengTa_CengInfo {
	
	public static ConcurrentHashMap<String, ArticleEntity> tempEntitys = new ConcurrentHashMap<String, ArticleEntity>();

	private int cengIndex;				//第几层
	
	private String cengMapName;			//层用的game地图名称
	
	private long cengTime;			//层的持续时间
	
	private HashMap<Integer, Integer> killMonster = new HashMap<Integer, Integer>();		//需要杀的怪物数目
	
	private ArrayList<QianCengTa_CengMonsterInfo> monsters = new ArrayList<QianCengTa_CengMonsterInfo>();			//怪物数据，并按刷新时间排列好
	
	private String[] monsterNames;
	
	private String[] rewardNames;
	
	private int[] rewardcolorTypes;
	
	private long[] cengRewardIDs;
	
	private String[] rewardRandomNames;
	
	private String[] showCengNames;
	private int[] showCengColors;
	
	//物品掉落集合
	private ArrayList<QianCengTa_FlopSet> flopSets  = new ArrayList<QianCengTa_FlopSet>();
	
	public int getCengIndex() {
		return cengIndex;
	}

	public void setCengIndex(int cengIndex) {
		this.cengIndex = cengIndex;
	}

	public void setFlopSets(ArrayList<QianCengTa_FlopSet> flopSets) {
		this.flopSets = flopSets;
	}

	public ArrayList<QianCengTa_FlopSet> getFlopSets() {
		return flopSets;
	}

	public void setMonsterNames(String[] monsterNames) {
		this.monsterNames = monsterNames;
	}

	public String[] getMonsterNames() {
		if (monsterNames == null) {
			ArrayList<String> monNames = new ArrayList<String>();
			for (QianCengTa_CengMonsterInfo monsterInfo : monsters) {
				MemoryMonsterManager mmm = (MemoryMonsterManager)MemoryMonsterManager.getMonsterManager();
				MonsterTempalte tempalte = mmm.getMonsterTempalteByCategoryId(monsterInfo.getMonsterID());
				String monName = "";
				if (tempalte != null && tempalte.monster != null) {
					monName = tempalte.monster.getName() +"(" + tempalte.monster.getLevel()+Translate.级+")";
				}else{
					monName = Translate.未知;
				}
				if (!monNames.contains(monName)) {
					monNames.add(monName);
				}
			}
			monsterNames = monNames.toArray(new String[0]);
		}
		return monsterNames;
	}

	public void setRewardNames(String[] rewardNames) {
		this.rewardNames = rewardNames;
	}

	public String[] getRewardNames() {
		if (rewardNames == null) {
			rewardNames = new String[flopSets.size()];
			for (int i = 0; i < rewardNames.length; i++) {
				rewardNames[i] = flopSets.get(i).getClientShowName();
			}
		}
		return rewardNames;
	}
	
	public void setCengMapName(String cengMapName) {
		this.cengMapName = cengMapName;
	}

	public String getCengMapName() {
		return cengMapName;
	}

	public void setCengTime(long cengTime) {
		this.cengTime = cengTime;
	}

	public long getCengTime() {
		return cengTime;
	}

	public void setKillMonster(HashMap<Integer, Integer> killMonster) {
		this.killMonster = killMonster;
	}

	public HashMap<Integer, Integer> getKillMonster() {
		return killMonster;
	}
	
	//返回开始的信息
	public String getStartInfo(){
		StringBuffer re = new StringBuffer();
		
		re.append("<f color='0xffff00'>"+Translate.text_qiancengta_027+"</f>\n");
		
		for (int i = 0 ; i < getRewardNames().length; i++) {
			re.append("    ").append("<f color='0xffffff'>"+getRewardRandomNames()[i]+"</f><f color='0x" + Integer.toHexString(ArticleManager.color_article[getRewardcolorTypes()[i]]) + "'>"+ getRewardNames()[i] +"</f>\n");
		}
		
		re.append("<f color='0xffff00'>"+Translate.text_qiancengta_014+"</f>\n");
		if (killMonster.size() > 0) {
			for (Iterator<Integer> it = killMonster.keySet().iterator(); it.hasNext(); ) {
				Integer key = it.next();
				Integer num = killMonster.get(key);
				MemoryMonsterManager mmm = (MemoryMonsterManager)MemoryMonsterManager.getMonsterManager();
				MonsterTempalte tempalte = mmm.getMonsterTempalteByCategoryId(key.intValue());
				if (tempalte != null && tempalte.monster != null) {
					re.append("    ").append(Translate.text_qiancengta_015).append(tempalte.monster.getName()).append(" ").append(num.intValue()).append(Translate.个).append("\n");
				}else {
					re.append("    ").append(Translate.text_qiancengta_017).append(" ").append(num.intValue()).append(Translate.个).append("\n");
				}
			}
		}else {
			if (cengTime <= 0) {
				re.append("    ").append(Translate.text_qiancengta_018).append("\n");
			}else {
				re.append("    ").append(Translate.text_qiancengta_019).append("\n");
			}
		}
		re.append("<f color='0xffff00'>").append(Translate.text_qiancengta_020).append("</f>\n");
		re.append("    ").append(Translate.text_qiancengta_021).append("\n");
		if (cengTime > 0 && killMonster.size() > 0) {
			re.append("    ").append(Translate.text_qiancengta_022).append("\n");
		}
		re.append("<f color='0xffff00'>");re.append(Translate.text_qiancengta_023).append("</f>\n");
		if (cengTime <= 0) {
			re.append("    ").append(Translate.text_qiancengta_024);
		}else {
			long minture = cengTime/1000/60;
			if (minture == 0) {
				minture = 1;
			}
			re.append("    ").append(minture).append(Translate.text_qiancengta_025);
		}
		return re.toString();
	}
	
	public ArrayList<QianCengTa_CengMonsterInfo> getMonsters() {
		return monsters;
	}

	public void setMonsters(ArrayList<QianCengTa_CengMonsterInfo> monsters) {
		this.monsters = monsters;
	}
	
	public void addMonster(QianCengTa_CengMonsterInfo info){
		for (int i = 0 ; i < monsters.size(); i++) {
			if (monsters.get(i).getRefTime() > info.getRefTime()) {
				monsters.add(i, info);
				return;
			}
		}
		monsters.add(info);
	}

	public void setRewardcolorTypes(int[] rewardcolorTypes) {
		this.rewardcolorTypes = rewardcolorTypes;
	}

	public int[] getRewardcolorTypes() {
		if (rewardcolorTypes == null) {
			rewardcolorTypes = new int[flopSets.size()];
			for (int i = 0; i < flopSets.size(); i++) {
				rewardcolorTypes[i] = flopSets.get(i).getColorType();
			}
		}
		return rewardcolorTypes;
	}

	public void setRewardRandomNames(String[] rewardRandomNames) {
		this.rewardRandomNames = rewardRandomNames;
	}

	public String[] getRewardRandomNames() {
		if (rewardRandomNames == null) {
			rewardRandomNames = new String[flopSets.size()];
			for (int i = 0; i < flopSets.size(); i++) {
				if (flopSets.get(i).getRandom() <= 2000) {
					rewardRandomNames[i] = Translate.较小几率+"-";
				}else if (flopSets.get(i).getRandom() <= 5000) {
					rewardRandomNames[i] = Translate.一般几率+"-";
				}else if (flopSets.get(i).getRandom() <= 7000) {
					rewardRandomNames[i] = Translate.较大几率+"-";
				}else{
					rewardRandomNames[i] = Translate.很大几率+"-";
				}
			}
		}
		return rewardRandomNames;
	}

	public void setCengRewardIDs(long[] cengRewardIDs) {
		this.cengRewardIDs = cengRewardIDs;
	}

	public long[] getCengRewardIDs() {
		if (cengRewardIDs == null) {
			cengRewardIDs = new long[showCengNames.length];
			for (int i = 0; i < showCengNames.length; i++) {
				String name = showCengNames[i];
				int color = showCengColors[i];
				ArticleEntity entity = tempEntitys.get(name+"-"+color);
				if (entity == null) {
					Article a = ArticleManager.getInstance().getArticle(name);
					try {
						entity = ArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.CREATE_REASON_QIANCENGTA, null, color);
						tempEntitys.put(name+"-"+color, entity);
					} catch (Exception e) {
						QianCengTa_Ta.logger.error("创建临时物品出错 [" + cengIndex +"] [" + name +"] ["+ color+"]", e);
					}
				}
				if (entity != null) {
					cengRewardIDs[i] = entity.getId();
				}else {
					cengRewardIDs[i] = -1;
				}
			}
		}
		return cengRewardIDs;
	}

	public void setShowCengNames(String[] showCengNames) {
		this.showCengNames = showCengNames;
	}

	public String[] getShowCengNames() {
		return showCengNames;
	}

	public void setShowCengColors(int[] showCengColors) {
		this.showCengColors = showCengColors;
	}

	public int[] getShowCengColors() {
		return showCengColors;
	}
}
