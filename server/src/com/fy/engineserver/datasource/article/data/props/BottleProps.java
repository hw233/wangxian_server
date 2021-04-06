package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.BottlePropsEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.message.BOTTLE_OPEN_INFO_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

/**
 * 特殊道具：宝瓶
 * 宝瓶中有一些物品，不同颜色的宝瓶可获得宝瓶内物品的数量不同
 *
 */
public class BottleProps extends Props implements Gem{

	public static final int DIS_BOTTLE_COUNT = 8;
	
	/**
	 * 包裹中的物品数组
	 */
	private ArticleProperty[][] articles = new ArticleProperty[0][];
	
	/**
	 * 对应概率数组的第一维，概率数组的第一维下标和这个数组的第一维下标一致
	 * 第二维表示玩家级别数组，最小级别，最大级别，包含最小最大值
	 */
	public static int[][] 玩家级别配置 = new int[][]{{0,20},{21,40},{41,60},{61,80},{81,100},{101,120},{121,140},{141,160},{161,180},{181,200},{201,220},{221,240},{241,260},{261,280},{281,300}};
	public static final int 数组长度 = 15;

	public ArticleProperty[] 根据级别得到获得物品概率数组(int level){
		int index = 0;
		for(int i = 0; i < 玩家级别配置.length; i++){
			if(玩家级别配置[i][0] <= level && 玩家级别配置[i][1] >= level){
				index = i;
				break;
			}
		}
		if(articles != null && articles.length > 0){
			if(articles.length > index){
				return articles[index];
			}else{
				return articles[0];
			}
		}
		return null;
	}

	public ArticleProperty[][] getArticles() {
		return articles;
	}

	public void setArticles(ArticleProperty[][] articles) {
		this.articles = articles;
	}

	@Override
	public boolean isUsedUndisappear() {
		// TODO Auto-generated method stub
		return true;
	}
	
	/**
	 * 各个颜色对应的开出物品的个数
	 */
	public static final int[][] COLOR_OPEN_COUNT = new int[][]{{1,2},{2,3,4},{3,4,5},{5,6},{6}};

	/**
	 * 打开包裹方法(重写父类方法)
	 * @param player
	 */
	public boolean use(Game game,Player player, ArticleEntity aee){
		if(!super.use(game,player,aee)){
			return false;
		}
		ArticleManager am = ArticleManager.getInstance();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if(!(aee instanceof BottlePropsEntity)){
			return false;
		}
		BottlePropsEntity bpe = (BottlePropsEntity)aee;
		if(bpe.getOpenedArticles() != null){
			long[] allArticleIds = new long[bpe.getAllArticles().length];
			long[] articleIds = new long[bpe.getOpenedArticles().length];
			int[] indexs = new int[bpe.getOpenedArticles().length];
			int[] articleNums = new int[bpe.getAllArticles().length];
			for(int i = 0; i < bpe.getOpenedArticles().length; i++){
				ArticleProperty ap = bpe.getOpenedArticles()[i];
				if(ap != null){
					Article a = am.getArticle(ap.articleName);
					if(a != null){
						try{
							ArticleEntity ae = aem.createTempEntity(a, ap.binded, ArticleEntityManager.CREATE_REASON_BOTTLE, player, ap.color);
							if(ae != null){
								allArticleIds[i] = ae.getId();
								articleIds[i] = ae.getId();
								indexs[i] = i;
								articleNums[i] = ap.count;
							}
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
				}
			}
			for(int i = bpe.getOpenedArticles().length; i < bpe.getAllArticles().length; i++){
				ArticleProperty ap = bpe.getAllArticles()[i];
				if(ap != null){
					Article a = am.getArticle(ap.articleName);
					if(a != null){
						try{
							ArticleEntity ae = aem.createTempEntity(a, ap.binded, ArticleEntityManager.CREATE_REASON_BOTTLE, player, ap.color);
							if(ae != null){
								allArticleIds[i] = ae.getId();
								articleNums[i] = ap.count;
							}
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
				}
			}
			BOTTLE_OPEN_INFO_REQ req = new BOTTLE_OPEN_INFO_REQ(GameMessageFactory.nextSequnceNum(), aee.getId(), allArticleIds, articleNums, indexs, bpe.isOpen());
			player.addMessageToRightBag(req);
		}
		return true;
	}

	/**
	 * 判断某个玩家是否可以使用此物品 子类可以重载此方法
	 * 返回null表示可以使用
	 * 返回字符串表示不能使用
	 * 字符串为不能使用的详细信息
	 * @param p
	 * @return
	 */
	public String canUse(Player p) {

		String resultStr = super.canUse(p);
		if(resultStr == null){}
		return resultStr;
	}

}
