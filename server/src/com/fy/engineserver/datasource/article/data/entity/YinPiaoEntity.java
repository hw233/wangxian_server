package com.fy.engineserver.datasource.article.data.entity;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.trade.TradeManager;
import com.xuanzhi.tools.cache.CacheSizes;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;

@SimpleEntity
public class YinPiaoEntity extends ArticleEntity {

	public static final long serialVersionUID = 96885312395040L;
	
	public YinPiaoEntity() {}
	
	public YinPiaoEntity(long id) {
		super(id);
	}
	
	public void setHaveMoney(long haveMoney) {
		this.haveMoney = haveMoney;
		saveData("haveMoney");
	}

	public long getHaveMoney() {
		return haveMoney;
	}

	private long haveMoney;
	
	/**
	 * 重写玩家获得物品详细信息
	 * @return
	 */
	public String getInfoShow(Player p) {
		Article a = ArticleManager.getInstance().getArticle(articleName);
		StringBuffer sb = new StringBuffer("");
		sb.append("现有非流通银子:\n");
		sb.append(TradeManager.putMoneyToMyText(haveMoney));
		if(isBinded()){
			sb.append("\n<f color='0xFFFF00' size='28'>").append(Translate.已绑定).append("</f>");
		}
		if(a.getUseMethod() != null && !a.getUseMethod().trim().equals("")){
			sb.append("\n<f color='0xFFFF00' size='28'>").append(a.getUseMethod()).append("</f>");
		}
		if(a.getGetMethod() != null && !a.getGetMethod().trim().equals("")){
			sb.append("\n<f color='0xFFFF00' size='28'>").append(a.getGetMethod()).append("</f>");
		}
		return sb.toString();
	}
	
	@Override
	public int getSize() {
		int size =  super.getSize();
		size += CacheSizes.sizeOfLong();		//leftUsingTimes
		return size;
	}
}
