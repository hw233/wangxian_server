package com.fy.engineserver.trade.requestbuy;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.trade.boothsale.TradeItem;
import com.fy.engineserver.trade.requestbuy.service.RequestBuyManager;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimplePostLoad;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 求购信息
 * 
 * 
 */
@SimpleEntity
public class RequestBuy implements Comparable<RequestBuy>, Cacheable {
	
	public static String equ_icon = "xiuluoyifu11";
	public static String zuoqi_icon = "zq_zb_anzi";

	@SimpleId
	private long id;
	/** 求购发布者ID */
	
	@SimpleVersion
	private int versionRB;
	
	private long releaserId;
	/** 求购发布者NAME */
	@SimpleColumn(length=100)
	private String releaserName;
	/** 求购物品 */
	private TradeItem item;			//求购发布后只有求购物品的数量会变化
	/** 求购规则 */
	private RequestBuyRule rule;
	/** 求购结束 */
	private transient boolean beOver = false;
	
	private transient RequestBuyInfo4Client buyInfo4Client;
	
	public RequestBuy(){}

	public boolean isBeOver() {
		return beOver;
	}

	public boolean isTimeOut() {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		return item.getEndTime() <= now || item.getEntityNum() <= 0;
	}

	public void setBeOver(boolean beOver) {
		this.beOver = beOver;
	}

	public long getReleaserId() {
		return releaserId;
	}

	public void setReleaserId(long releaserId) {
		this.releaserId = releaserId;
	}

	public TradeItem getItem() {
		return item;
	}

	public void setItem(TradeItem item) {
		this.item = item;
	}

	public RequestBuyRule getRule() {
		return rule;
	}

	public void setRule(RequestBuyRule rule) {
		this.rule = rule;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getReleaserName() {
		return releaserName;
	}

	public void setReleaserName(String releaserName) {
		this.releaserName = releaserName;
		RequestBuyManager.getInstance().em.notifyFieldChange(this, "releaserName");
	}

	@Override
	public int compareTo(RequestBuy o) {
		if(o.getItem().getPerPrice()>this.getItem().getPerPrice()){
			return 1;
		}
		return 0;
	}

	public static RequestBuy createRequestBuy() {
		RequestBuy buy = new RequestBuy();
		try {
			buy.setId(RequestBuyManager.getInstance().em.nextId());
		} catch (Exception e) {
			RequestBuyManager.logger.error("求购生成ID出错", e);
			return null;
		}
		return buy;
	}

	public RequestBuy init(long releaserId, String releaserName, TradeItem item, RequestBuyRule templet) {
		setReleaserId(releaserId);
		setReleaserName(releaserName);
		this.item = item;
		this.rule = templet;
		return this;
	}

	@Override
	public int getSize() {
		return 0;
	}
	
	public String getLogString(){
		StringBuffer bf = new StringBuffer("");
		bf.append("[求购ID=").append(id)
		.append("] [玩家ID=").append(releaserId)
		.append("] [玩家Name=").append(releaserName).append("] ");
		if (rule != null) {
			bf.append("[一级=").append(rule.getMainType())
			.append("] [二级=").append(rule.getSubType())
			.append("] [名字=").append(rule.getArticleName())
			.append("] [品质=").append(rule.getColor())
			.append("] [等级=").append(rule.getGrade()).append("-").append(rule.getMaxGrade())
			.append("] ");
		}
		if (item != null) {
			Date date = new Date(item.getStartTime());
			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
			String timeString = format.format(date);
			bf.append("[数量=").append(item.getEntityNum())
			.append("] [单价=").append(item.getPerPrice())
			.append("] [开始时间=").append(timeString).append("]");
			;
		}
		return bf.toString();
	}
	
	@SimplePostLoad
	public void postLoad(){
		buyInfo4Client = new RequestBuyInfo4Client();
		buyInfo4Client.setArticleName(getRule().getOutshowName());
		buyInfo4Client.setRequestBuyId(getId());
		buyInfo4Client.setColor(getRule().getColor());
		buyInfo4Client.setGrade(getRule().getGrade());
		buyInfo4Client.setGrademax(getRule().getMaxGrade());
		buyInfo4Client.setReleasePlayerName(getReleaserName());
		buyInfo4Client.setMainType(getRule().getMainType());
		buyInfo4Client.setSubType(getRule().getSubType());
		if(getRule().getMainType().equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类])||getRule().getMainType().equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_马匹装备类])){
			buyInfo4Client.setEntityId(-1);
			if(getRule().getMainType().equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类])){
				buyInfo4Client.setIcon(equ_icon);
			}else{
				buyInfo4Client.setIcon(zuoqi_icon);
			}
		}else{
			Article article = ArticleManager.getInstance().getArticle(getRule().getArticleName());
//			if(getRule().getMainType().equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_宝石类])){
//				if(article == null){
//					for(){
//						
//					}	
//				}
//			}
			ArticleEntity articleEntity = RequestBuyManager.getInstance().getTempEntity(article, rule.getColor());
			buyInfo4Client.setEntityId(articleEntity.getId());
			buyInfo4Client.setIcon(article.getIconId());
		}
		buyInfo4Client.setReleasePlayerName(getReleaserName());
		buyInfo4Client.setPerPrice(getItem().getPerPrice());
	}

	// 可优化
	public RequestBuyInfo4Client getRequestBuyInfo4Client() {
		if (buyInfo4Client == null) {
			postLoad();
		}
		buyInfo4Client.setLeftNum(getItem().getEntityNum());
		buyInfo4Client.setLeftTime(RequestBuyManager.timeDistanceLong2String(getItem().getEndTime()-com.fy.engineserver.gametime.SystemTime.currentTimeMillis()));
		return buyInfo4Client;
	}

	public void setVersionRB(int versionRB) {
		this.versionRB = versionRB;
	}

	public int getVersionRB() {
		return versionRB;
	}
}
