package com.fy.engineserver.datasource.article.data.props;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.shop.ShopActivityManager;
import com.fy.engineserver.billboard.special.SpecialEquipmentManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

/**
 * 等级随机礼包.
 * 
 * 
 */
public class LevelRandomPackage extends Props {

	private static Map<Integer, List<Article>> allmap = new HashMap<Integer, List<Article>>();
	
	private static List<Article> equlist = new ArrayList<Article>();

	private static List<Article> jiulist = new ArrayList<Article>();

	private static List<Article> tielist = new ArrayList<Article>();

	// private static List<Article> petlist = new ArrayList<Article>();

	public static Map<Integer, List<Article>> petPinzhiMap = new HashMap<Integer, List<Article>>();
	public static Map<Integer, Map<Integer, List<Article>>> equipmentMap = new HashMap<Integer, Map<Integer, List<Article>>>();// <zhiye,<dengji,zhuangbei>>
	public static Map<Integer, List<Article>> horseEquipmentMap = new HashMap<Integer, List<Article>>();// <dengji, zhuangbei>

	public static void initAllMap() {
		// 分类型加载物品
		ArticleManager am = ArticleManager.getInstance();
		Article a = null;
		Article[] allarticles = am.getAllArticles();
		for (int i = 0; i < allarticles.length; i++) {
			a = allarticles[i];
			if (a instanceof Equipment) {
				Equipment e = (Equipment) a;
				if(e.getOfLevelPackage == 1){
					continue;
				}
				if (e.getEquipmentType() < 10) {
					if (e.getSpecial() == SpecialEquipmentManager.鸿天帝宝 || e.getSpecial() == SpecialEquipmentManager.伏天古宝) {
						continue;
					}
					// equlist.add(a);
					if (!equipmentMap.containsKey(e.getCareerLimit())) {
						equipmentMap.put(e.getCareerLimit(), new HashMap<Integer, List<Article>>());
					}
					int levelIndex = getLevelIndex(e.getPlayerLevelLimit());
					if (!equipmentMap.get(e.getCareerLimit()).containsKey(levelIndex)) {
						equipmentMap.get(e.getCareerLimit()).put(levelIndex, new ArrayList<Article>());
					}
					equipmentMap.get(e.getCareerLimit()).get(levelIndex).add(a);
				} else if (e.getEquipmentType() >= 10 && e.getEquipmentType() <= 14) {		//坐骑装备
					if (e.getSpecial() == SpecialEquipmentManager.鸿天帝宝 || e.getSpecial() == SpecialEquipmentManager.伏天古宝) {
						continue;
					}
					int levelIndex = e.getPlayerLevelLimit();
					if (!horseEquipmentMap.containsKey(levelIndex)) {
						horseEquipmentMap.put(levelIndex, new ArrayList<Article>());
					}
					horseEquipmentMap.get(levelIndex).add(a);
				}
			} else if (Translate.酒.equals(a.get物品二级分类())) {
				jiulist.add(a);
			} else if ("封魔录".equals(a.get物品二级分类())) {
				if (a instanceof TaskProps) {
					TaskProps tp = (TaskProps) a;
					if (tp.getType() == 0) {
						tielist.add(a);
					}
				}
			} else if (a instanceof PetEggProps) {
				PetEggProps pe = (PetEggProps) a;
				if (pe.getType() != 0) {//只有普通宠物
					continue;
				}
				if (!petPinzhiMap.containsKey(pe.getRarity())) {
					petPinzhiMap.put(pe.getRarity(), new ArrayList<Article>());
				}
				petPinzhiMap.get(pe.getRarity()).add(a);
			}
		}
		allmap.put(0, equlist);
		allmap.put(1, jiulist);
		allmap.put(2, tielist);
	}

	/** 内部物品类型 0:装备,1酒,2帖,3宠物蛋 */
	private int insideType;
	/** 开出各种颜色的概率 */
	private Integer[] rate;
	/** 开出物品的个数 */
	private int outputNum;
	/** 开出物品是否绑定 */
	private boolean outputBind;

	private int totalRate;

	static Random random = new Random();

	public static int[] petLevel = new int[] { 1, 45, 90, 135, 180 };
	public static int[] petLevel_player_MIN = new int[] { 1, 45, 90, 135, 180 };
	public static int[] petLevel_player_MAX = new int[] { 44, 89, 134, 179, 230 };
	public static int[] horse_article_level = new int[] {70, 90, 110, 130, 150, 170, 190, 210, 1000};
	
	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		boolean articleIsBind = ae.isBinded();
//		initAllMap();
		int emptyNum = player.getKnapsack_common().getEmptyNum();
		if (emptyNum < outputNum) {
			player.sendError(Translate.你的背包空间不足请整理后再使用);
			return false;
		}
		if (player.getLevel() < this.getLevelLimit()) {
			player.sendError(Translate.text_5292);
			return false;
		}
		List<String> list = new ArrayList<String>();
		List<ArticleEntity> aes = new ArrayList<ArticleEntity>();
		String randomArticle = "";
		ArticleEntity aee = null;
		if (outputNum > 0) {
			Article a = null;
			for (int i = 0; i < outputNum; i++) {
				// 如果是宠物 color表示是稀有度
				int randomColor = getRandomColor();
				try {
					if (insideType == 0) {
						if(equipmentMap!=null && equipmentMap.size()==0){
							initAllMap();
						}
						int level = player.getLevel();
						if(player.getLevel()>=220){
							level = 220;
						}
						List<Article> equipList = equipmentMap.get((int) player.getCareer()).get(getLevelIndex(level));
						int number = Math.abs((int)equipList.size());
						randomArticle = equipList.get(random.nextInt(number)).getName();
						a = ArticleManager.getInstance().getArticle(randomArticle);
						aee = ArticleEntityManager.getInstance().createEntity(a, articleIsBind | outputBind, ArticleEntityManager.CREATE_REASON_USE_PACKAGEPROPS, player, randomColor, 1, true);
						aes.add(aee);
					} else if(insideType == 4) {		//宠物装备
						int levelIndex = 70;
						if(horseEquipmentMap!=null && horseEquipmentMap.size()==0){
							initAllMap();
						}
						for(int j=0; j<horse_article_level.length; j++) {
							if(player.getLevel() <= horse_article_level[j]) {
								int index = player.getLevel() == horse_article_level[j] ? j : (j-1);
								levelIndex = horse_article_level[index];
								break;
							}
						}
						int number = Math.abs((int)horseEquipmentMap.get(levelIndex).size());
						randomArticle = horseEquipmentMap.get(levelIndex).get(random.nextInt(number)).getName();
						a = ArticleManager.getInstance().getArticle(randomArticle);
						aee = ArticleEntityManager.getInstance().createEntity(a, articleIsBind | outputBind, ArticleEntityManager.CREATE_REASON_USE_PACKAGEPROPS, player, randomColor, 1, true);
						aes.add(aee);
					}else if (insideType == 1) {
						list = getArticleByPlayerLevel(insideType, player);
						int number = Math.abs((int)list.size());
						randomArticle = list.get(random.nextInt(number));
						a = ArticleManager.getInstance().getArticle(randomArticle);
						aee = ArticleEntityManager.getInstance().createEntity(a, articleIsBind | outputBind, ArticleEntityManager.CREATE_REASON_USE_PACKAGEPROPS, player, randomColor, 1, true);
						aes.add(aee);
					} else if (insideType == 2) {
						list = getArticleByPlayerLevel(insideType, player);
						int number = Math.abs((int)list.size());
						randomArticle = list.get(random.nextInt((int)number));
						a = ArticleManager.getInstance().getArticle(randomArticle);
						aee = ArticleEntityManager.getInstance().createEntity(a, articleIsBind | outputBind, ArticleEntityManager.CREATE_REASON_USE_PACKAGEPROPS, player, randomColor, 1, true);
						aes.add(aee);
					} else if (insideType == 3) {
						//TODO maybe NullPointerException
						if(petPinzhiMap!=null && petPinzhiMap.size()==0){
							initAllMap();
						}
						List<Article> pets = petPinzhiMap.get(randomColor);
						List<String> playerCanUseArticles = new ArrayList<String>();
						int index = 0;
						for (int ii = 0; ii < petLevel_player_MIN.length; ii++) {
							if (player.getLevel() >= petLevel_player_MIN[ii] && player.getLevel() <= petLevel_player_MAX[ii]) {
								index = ii;
								break;
							}
						}
						for (Article p : pets) {
							PetEggProps pp = (PetEggProps) p;
							if (pp.getLevelLimit() == petLevel[index]) {
								playerCanUseArticles.add(p.getName());
							}
						}
						list = playerCanUseArticles;
						int number = Math.abs((int)list.size());
						randomArticle = list.get(random.nextInt(number));
						a = ArticleManager.getInstance().getArticle(randomArticle);
						aee = ArticleEntityManager.getInstance().createEntity(a, articleIsBind | outputBind, ArticleEntityManager.CREATE_REASON_USE_PACKAGEPROPS, player, randomColor, 1, true);
						aes.add(aee);
					}
				} catch (Exception e) {
					e.printStackTrace();
					ArticleManager.logger.warn("[等级随机礼包] [" + player.getLogString() + "] [异常]", e);
					//TODO
					return false;
				}
			}
		}
		if(aes.size()>0){
			ArticleManager.logger.warn("[等级随机礼包] [type:"+insideType+"] [aes:"+aes.size()+"] [礼包:"+ae.getArticleName()+"] [" + player.getLogString() + "] [成功]");
			player.putAll(aes.toArray(new ArticleEntity[] {}), "随机礼包");
		}
		try {
			ArrayList<ArticleEntity> strongMaterialEntitys = new ArrayList<ArticleEntity>();
			strongMaterialEntitys.add(ae);
			ShopActivityManager.getInstance().noticeUseSuccess(player, strongMaterialEntitys);
			ActivitySubSystem.logger.warn("[使用赠送活动] [type:"+insideType+"] [aes:"+aes.size()+"] [使用随机礼包:" + ae.getArticleName() + "] [" + player.getLogString() + "]");
		} catch (Exception e) {
			ActivitySubSystem.logger.error("[使用赠送活动] [使用随机礼包] [" + player.getLogString() + "]", e);
		}
		return super.use(game, player, ae);
	}

	/**
	 * 获得玩家可用等级范围的物品
	 * @param articletype
	 * @param plevel
	 * @return
	 */
	public List<String> getArticleByPlayerLevel(int articletype, Player player) {
		int plevel = player.getLevel();
		List<String> playerCanUseArticles = new ArrayList<String>();
		if (allmap == null || allmap.size() == 0) {
			initAllMap();
		}
		List<Article> list = allmap.get(articletype);
		if (articletype == 2) {
			for (Article a : list) {
				if (plevel >= a.getArticleLevel()) {
					if(plevel<a.getArticleLevel()+20){
						playerCanUseArticles.add(a.getName());
					}
				}else{
					if(a.getArticleLevel()==25){
						playerCanUseArticles.add(a.getName());
					}
				}
			}
		} else if (articletype == 1) {
			if (player.getLevel() < 81) {
				playerCanUseArticles.add(Translate.白玉泉);
			} else if (player.getLevel() < 161) {
				playerCanUseArticles.add(Translate.金浆醒);
			} else if (player.getLevel() < 221) {
				playerCanUseArticles.add(Translate.香桂郢酒);
			} else if (player.getLevel() < 281) {
				playerCanUseArticles.add(Translate.琼浆玉液);
			} else {
				playerCanUseArticles.add(Translate.诸神玉液);
			}
		}
		return playerCanUseArticles;
	}

	public static int getLevelIndex(int level) {
		return (level - 1) / 20;
	}

	public int getTopLimit(int pid) {
		return (pid / 20 + 1) * 20;
	}

	public int getLowLimit(int pid) {
		return pid / 20 * 20;
	}

	/**
	 * 获得随机颜色值
	 * @param num
	 * @return
	 */
	private int getRandomColor() {
		int nextInt = random.nextInt(totalRate) + 1;
		int low = 0;
		for (int i = 0; i < rate.length; i++) {
			if (low < nextInt && rate[i] + low >= nextInt) {
				return i;
			}
			low += rate[i];
		}
		return 0;// 默认返回0
	}

	public int getInsideType() {
		return insideType;
	}

	public void setInsideType(int insideType) {
		this.insideType = insideType;
	}

	public Integer[] getRate() {
		return rate;
	}

	public void setRate(Integer[] rate) {
		this.rate = rate;
	}

	public int getOutputNum() {
		return outputNum;
	}

	public void setOutputNum(int outputNum) {
		this.outputNum = outputNum;
	}

	public boolean isOutputBind() {
		return outputBind;
	}

	public void setOutputBind(boolean outputBind) {
		this.outputBind = outputBind;
	}

	public void init() {
		totalRate = 0;
		for (int i = 0; i < rate.length; i++) {
			totalRate += rate[i];
		}
	}
}
