package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.horse.Horse;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;

/**
 * 换装道具
 *
 */
public class AvataProps extends Props{
	
	public static final int TYPE_WEAPON = 0;
	
	public static final int TYPE_HEAD = 1;
	
	public static final int TYPE_MARRY = 10;
	
	public static final int TYPE_HORSE = 12;
	
	public static final byte HORSE_FASHION = 1;
	public static final byte PLAYER_FASHION = 0;
	
	/**
	 * 区别人时装还是坐骑时装
	 */
	private byte useType;
	public byte getUseType() {
		return useType;
	}

	public void setUseType(byte useType) {
		this.useType = useType;
	}

	/**
	 * 按位置代表物理攻强，法术攻强，物理防御，法术防御，血，暴击的百分比值，分母为100
	 */
	@SimpleColumn(mysqlName="AvataPropsValues")
	public int[] values;
	
	
	public int[] getValues() {
		return values;
	}

	public void setValues(int[] values) {
		this.values = values;
	}

	/**
	 * 只负责加载玩家时装属性，不管时装外观效果和人物avataPropsId
	 * @param player
	 */
	public void puton(Player player){
		if (player != null && values != null) {
			for (int i = 0; i < values.length; i++) {
				int value = values[i];
				if (value != 0) {
					switch (i) {
					case 0:
						player.setPhyAttackC(player.getPhyAttackC() + value);
						break;
					case 1:
						player.setMagicAttackC(player.getMagicAttackC() + value);
						break;
					case 2:
						player.setPhyDefenceC(player.getPhyDefenceC() + value);
						break;
					case 3:
						player.setMagicDefenceC(player.getMagicDefenceC() + value);
						break;
					case 4:
						player.setMaxHPC(player.getMaxHPC() + value);
						break;
					case 5:
						player.setCriticalHitC(player.getCriticalHitC() + value);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 只负责卸载玩家时装属性，不管时装外观效果和人物avataPropsId
	 * @param player
	 */
	public void takeoffProperty(Player player){
		if (player != null && values != null) {
			for (int i = 0; i < values.length; i++) {
				int value = values[i];
				if (value != 0) {
					switch (i) {
					case 0:
						player.setPhyAttackC(player.getPhyAttackC() - value);
						break;
					case 1:
						player.setMagicAttackC(player.getMagicAttackC() - value);
						break;
					case 2:
						player.setPhyDefenceC(player.getPhyDefenceC() - value);
						break;
					case 3:
						player.setMagicDefenceC(player.getMagicDefenceC() - value);
						break;
					case 4:
						player.setMaxHPC(player.getMaxHPC() - value);
						break;
					case 5:
						player.setCriticalHitC(player.getCriticalHitC() - value);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 卸载玩家时装，包括把时装放入包裹中，时装属性卸载，以及更改玩家的avataPropsId
	 * true表示卸载成功
	 * @param player
	 * @return
	 */
	public static boolean takeoff(Player player){
		if (player != null && player.getAvataPropsId() > 0) {
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			ArticleEntity aee  = aem.getEntity(player.getAvataPropsId());
			if(aee != null){
				if(!player.putToKnapsacks(aee,"卸载时装道具")){
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.背包空间不足);
					player.addMessageToRightBag(hreq);
					return false;
				}
				ArticleManager am = ArticleManager.getInstance();
				Article a = am.getArticle(aee.getArticleName());
				if(a instanceof AvataProps){
					((AvataProps)a).takeoffProperty(player);
				}else{
					if(ArticleManager.logger.isWarnEnabled())
						ArticleManager.logger.warn("[时装道具不存在] [时装道具名:{}] [时装id:{}] [{}] [{}] [{}]", new Object[]{aee.getArticleName(),player.getAvataPropsId(),player.getUsername(),player.getId(),player.getName()});
				}
			}else{
				if(ArticleManager.logger.isWarnEnabled())
					ArticleManager.logger.warn("[时装实体不存在] [时装id:{}] [{}] [{}] [{}]", new Object[]{player.getAvataPropsId(),player.getUsername(),player.getId(),player.getName()});
			}
			//不管物品存不存在都设置为-1
			player.setAvataPropsId(-1);
		}
		return true;
	}
	
	String avata;
	
	/**
	 * 替换部位类型
	 */
	byte type;
	public String getAvata() {
		return avata;
	}
	public void setAvata(String avataSex) {
		this.avata = avataSex;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
@Override
public int getKnapsackType() {
	// TODO Auto-generated method stub
	return Article.KNAP_奇珍;
}
	/**
	 * 使用道具
	 * @param player
	 */
	public boolean use(Game game,Player player, ArticleEntity ae){
		if(!super.use(game,player,ae)){
			return false;
		}
		if(player.getAvataPropsId() > 0){
			if(!takeoff(player)){
				return false;
			}
		}
		player.setAvataPropsId(ae.getId());
		puton(player);
		try{
			ResourceManager rm = ResourceManager.getInstance();
			if(rm != null){
				rm.getAvata(player);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return true;
	}
	@Override
	public String canUse(Player p) {
		// TODO Auto-generated method stub
		String result = super.canUse(p);
		if(this.useType == HORSE_FASHION) result = Translate.这是坐骑装备;
		return result;
	}
	
	public boolean canUse(Horse h) {
		if(this.useType == HORSE_FASHION) return true;
		return false;
	}
	
}
