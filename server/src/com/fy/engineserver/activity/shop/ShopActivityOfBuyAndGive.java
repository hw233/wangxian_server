package com.fy.engineserver.activity.shop;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.shop.ShopActivityManager.RepayType;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.shop.Goods;
import com.fy.engineserver.shop.Shop;
import com.fy.engineserver.sprite.Player;

/**
 * 商店活动之买送
 * 
 */
public class ShopActivityOfBuyAndGive extends ShopActivity {

	private ActivityProp needBuyProp;
	private ActivityProp giveProp;
	private String shopName;
	private RepayType repayType;
	private boolean useGoodsId = false;
	
	public String toString() {
		return "shopName:" + getShopName() + "<br/>needBuyProp:" + needBuyProp + "<br/>giveProp:" + giveProp + "<br/>repayType:" + repayType;
	}
	public ShopActivityOfBuyAndGive(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
	}
	
	public void setOtherVar(RepayType repayType, String shopName, ActivityProp needBuyProp, ActivityProp giveProp, String mailTitle, String mailContent) {
		this.repayType = repayType;
		this.shopName = shopName;
		this.needBuyProp = needBuyProp;
		this.giveProp = giveProp;
		this.setMailTitle(mailTitle);
		this.setMailContent(mailContent);
	}

	@Override
	public void afterBuyGoods(Player player, Shop shop, Goods goods, int buyNum) {
		if(ActivitySubSystem.logger.isDebugEnabled()) {
			ActivitySubSystem.logger.debug("[商店买送][" + player.getLogString() + "][goodsId:" + goods.getId() + "][useGoodsId :" + useGoodsId + "][needBuyProp:" + needBuyProp.getGoodsId() + "]");
		}
		if (shop.getName().equals(getShopName())) {
			boolean flag = false;
			if(!useGoodsId && goods.getColor() == needBuyProp.getArticleColor()) {		//配表可填写具体物品或商品id
				flag = true;
			} else if(useGoodsId && goods.getId() == needBuyProp.getGoodsId()){
				flag = true;
			}
			if (flag) {
				int totalBuynum = goods.getBundleNum() * buyNum;
				boolean numFit = false;
				int multiple = 1;
				switch (repayType) {
				case 指定数量:
					numFit = totalBuynum >= needBuyProp.getArticleNum();
					multiple = totalBuynum / needBuyProp.getArticleNum();
					break;
				case 匹配购买数量:
					numFit = true;
					multiple = 1;
					break;
				default:
					break;
				}
				if (numFit) {
					Article article = ArticleManager.getInstance().getArticleByCNname(needBuyProp.getArticleCNName());
					if (article == null) {
						ActivitySubSystem.logger.warn(player.getLogString() + " [在商店:" + shop.getLogString() + "] [购买道具:" + goods.getArticleName() + "] [购买数量:" + totalBuynum + "] [需求物品不存在:" + needBuyProp.getArticleCNName() + "]");
						return;
					}
					if (article.getName().equals(goods.getArticleName())) {
						// 给奖励
						Article prize = ArticleManager.getInstance().getArticleByCNname(giveProp.getArticleCNName());
						if (prize == null) {
							ActivitySubSystem.logger.warn(player.getLogString() + " [在商店:" + shop.getLogString() + "] [购买道具:" + goods.getArticleName() + "] [购买数量:" + totalBuynum + "] [奖励物品不存在:" + giveProp.getArticleCNName() + "]");
							return;
						}
						try {
							switch (repayType) {
							case 指定数量:
								for (int i = 0; i < multiple; i++) {
									ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(prize, giveProp.isBind(), 1, player, giveProp.getArticleColor(), giveProp.getArticleNum(), true);
									long mailId = MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { giveProp.getArticleNum() }, this.getMailTitle(), this.getMailContent(), 0L, 0L, 0L, "商城购买活动");
									ActivitySubSystem.logger.warn(player.getLogString() + " [在商店:" + shop.getLogString() + "] [购买道具:" + goods.getArticleName() + "] [购买数量:" + totalBuynum + "] [奖励物品:" + giveProp.toString() + "] [发送邮件成功:" + mailId + "] [" + ( i + 1) + "/" + multiple + "]");
								}
								break;
							case 匹配购买数量:
								for (int i = 0; i < totalBuynum; i++) {
									ArticleEntity aae = ArticleEntityManager.getInstance().createEntity(prize, giveProp.isBind(), 1, player, giveProp.getArticleColor(), 1, true);
									long mailIdd = MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { aae }, new int[] { 1 }, this.getMailTitle(), this.getMailContent(), 0L, 0L, 0L, "商城购买活动");
									ActivitySubSystem.logger.warn(player.getLogString() + " [在商店:" + shop.getLogString() + "] [购买道具:" + goods.getArticleName() + "] [购买数量:" + totalBuynum + "] [奖励物品:" + giveProp.toString() + "] [发送邮件成功:" + mailIdd + "]");
								}
								break;

							default:
								break;
							}
						} catch (Exception e) {
							ActivitySubSystem.logger.warn(player.getLogString() + " [在商店:" + shop.getLogString() + "] [购买道具:" + goods.getArticleName() + "] [购买数量:" + totalBuynum + "] [奖励物品:" + giveProp.getArticleCNName() + "] [创建异常]", e);
							return;
						}
					}
				}
			}
		}
	}

	public ActivityProp getNeedBuyProp() {
		return needBuyProp;
	}

	public void setNeedBuyProp(ActivityProp needBuyProp) {
		this.needBuyProp = needBuyProp;
	}

	public ActivityProp getGiveProp() {
		return giveProp;
	}

	public void setGiveProp(ActivityProp giveProp) {
		this.giveProp = giveProp;
	}

	@Override
	public void afterUseArticle(Player player, ArticleEntity articlename, int useNum) {
		// TODO Auto-generated method stub

	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	@Override
	public boolean platFormFit() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String getInfoShow() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("[活动类型:商店购买赠送]");
		sb.append("[是否匹配物品id:" + useGoodsId + "]");
		sb.append("[物品id:" + needBuyProp.getGoodsId() + "]");
		sb.append("[活动内容 :一次性购买" + needBuyProp + "]<br>[赠送 : " + giveProp + "]<br>");
		String rType = "未知类型";
		if(repayType.type == 0) {
			rType = "指定数量";
		} else if(repayType.type == 1) {
			rType = "匹配购买数量";
		}
		sb.append("[赠送类型:" + rType + "]");
		sb.append("[开放商店:" + this.getShopName() + "]");
		return sb.toString();
	}
	public boolean isUseGoodsId() {
		return useGoodsId;
	}
	public void setUseGoodsId(boolean useGoodsId) {
		this.useGoodsId = useGoodsId;
	}

}
