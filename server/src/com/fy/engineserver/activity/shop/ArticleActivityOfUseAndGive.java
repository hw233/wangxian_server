package com.fy.engineserver.activity.shop;

import java.util.ArrayList;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
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
 * 物品使用赠送活动
 * 
 * 
 */
public class ArticleActivityOfUseAndGive extends ShopActivity {

	private ActivityProp needBuyProp;
	
	private long activityId;
	/** 0普通使用   1合成   -1都可以 */
	private byte useType = 0;
	
	public ArticleActivityOfUseAndGive(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
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

	public RepayType getRepayType() {
		return repayType;
	}

	public void setRepayType(RepayType repayType) {
		this.repayType = repayType;
	}

	private ActivityProp giveProp;
	private RepayType repayType;
	
	public void setOtherVar(RepayType repayType, ActivityProp needBuyProp, ActivityProp giveProp, String mailTitle, String mailContent, long tempId, byte useType) {
		this.setMailTitle(mailTitle);
		this.setMailContent(mailContent);
		this.needBuyProp = needBuyProp;
		this.repayType = repayType;
		this.activityId = tempId;
		this.giveProp = giveProp;
		this.useType = useType;
	}

	@Override
	public void afterUseArticle(Player player, ArrayList<ArticleEntity> articles, byte useType) {
		int useNum = 0;
		ArticleEntity temp = null;
		if(this.useType != -1 && this.useType != useType) {
			if(ActivitySubSystem.logger.isDebugEnabled()) {
				ActivitySubSystem.logger.debug("[用送活动传过来有空值][传过来的使用类型:" + useType + "][配表需求：" + this.useType + "]");
			}
			return;
		}
		for (ArticleEntity at : articles) {
			if (at == null || needBuyProp == null) {
				ActivitySubSystem.logger.warn("[用送活动传过来有空值][传过来的物品:" + at.getArticleName() + "][配表需求：" + needBuyProp.getArticleCNName() + "]");
				continue;
			}
			Article article = ArticleManager.getInstance().getArticle(at.getArticleName());
			if (article == null) {
				ActivitySubSystem.logger.warn("[使用赠送活动] [获取物品为空:" + at.getArticleName() + "]");
				continue;
			}
			if(ActivitySubSystem.logger.isDebugEnabled()) {
				ActivitySubSystem.logger.debug("[所有活动管理] [有活动通知:" + article.getName_stat() + "][" + at.getColorType() + "]");
			}
			if (needBuyProp.getArticleCNName().equals(article.getName_stat()) && (needBuyProp.getArticleColor() < 0 || needBuyProp.getArticleColor() == at.getColorType())) { // 判断物品、颜色、绑定属性是否满足
				if(needBuyProp.isBind() == at.isBinded() || needBuyProp.isWithOutBind()) {
					useNum++;
					if (temp == null) {
						temp = at;
					}
				}
			}
		}
		if (useNum > 0 && temp != null) {
			afterUseArticle(player, temp, useNum);
		}
	}

	@Override
	public void afterUseArticle(Player player, ArticleEntity a, int useNum) {
		Article article = ArticleManager.getInstance().getArticle(a.getArticleName());
		if (article == null) {
			return;
		}
		if (needBuyProp.getArticleCNName().equals(article.getName_stat())) {
			if (a.getColorType() == needBuyProp.getArticleColor() || needBuyProp.getArticleColor() <= 0) {
				// 非绑定的白色、绿色、蓝色、紫色、橙色强化石、每使用5个同品质的即返还1个相同品质的（使用合成不返还）。不限量不封顶
				// 就是说练星使用的强化石，比如使用了5个蓝色的强化石，邮件返还一个蓝色强化石
				// 奖励是对应颜色的，如果想实现指定颜色，再扩展
				try {
					int tempColor = needBuyProp.getArticleColor() >= 0 ? needBuyProp.getArticleColor() : article.getColorType();
					UseStat stat = (UseStat) ActivityManagers.getInstance().getDdc().get(player.getId() + "shop20130919" + tempColor + a.getArticleName() + this.getActivityId());
//					UseStat stat = (UseStat) ActivityManagers.getInstance().getDdc().get(player.getId() + "shop20130919" + needBuyProp.getArticleColor() + a.getArticleName() + this.getActivityId());
					if (stat == null) {
						UseStat st = new UseStat(new int[] { 0, 0, 0, 0, 0, 0, 0 });
						ActivityManagers.getInstance().getDdc().put(player.getId() + "shop20130919" + tempColor + a.getArticleName() + this.getActivityId(), st);// 白，绿，蓝....
						stat = (UseStat) ActivityManagers.getInstance().getDdc().get(player.getId() + "shop20130919" + tempColor + a.getArticleName() + this.getActivityId());
//						stat = (UseStat) ActivityManagers.getInstance().getDdc().get(player.getId() + "shop20130919" + needBuyProp.getArticleColor() + a.getArticleName() + this.getActivityId());
					}
					int oldvalue = stat.colorvalue[tempColor];
//					int oldvalue = stat.colorvalue[needBuyProp.getArticleColor()];
					int currvalue = oldvalue + useNum;
					int tempNum = currvalue / needBuyProp.getArticleNum();
					if(tempNum <= 0) {
						tempNum = 1;
					}
					ActivitySubSystem.logger.warn("[使用赠送活动] [使用了 ：" + useNum + " 个 " + article.getName_stat() + "] [赠送次数:" + tempNum + "]");
					for(int ii=0; ii<tempNum; ii++) {
						ActivitySubSystem.logger.warn("[物品使用赠送活动] [color:" + a.getColorType() + "] [oldvalue:" + oldvalue + "] [currvalue:" + currvalue + "] [" + player.getLogString() + "]");
						if (currvalue >= needBuyProp.getArticleNum()) {
							Article prize = ArticleManager.getInstance().getArticleByCNname(giveProp.getArticleCNName());
							if (prize == null) {
								ActivitySubSystem.logger.warn("[物品使用赠送活动] [失败] [原因奖励物品" + giveProp.getArticleCNName() + "不存在!] [" + player.getLogString() + "]");
								return;
							}
							switch (repayType) {
							case 指定数量:
								ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(prize, giveProp.isBind(), 1, player, giveProp.getArticleColor(), giveProp.getArticleNum(), true);
								long mailId = MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { giveProp.getArticleNum() }, this.getMailTitle(), this.getMailContent(), 0L, 0L, 0L, "物品使用赠送活动");
								ActivitySubSystem.logger.warn("[物品使用赠送活动] [指定数量] [成功] [老值：" + oldvalue + "] [新值：" + currvalue + "]  [使用的物品：" + article.getLogString() + "] [颜色：" + a.getColorType() + "] [奖励物品:" + giveProp.toString() + "] [发送邮件成功:" + mailId + "] [" + player.getLogString() + "]");
								currvalue -= needBuyProp.getArticleNum();
								break;
							case 匹配购买数量:
								for (int i = 0; i < needBuyProp.getArticleNum(); i++) {
									ArticleEntity aae = ArticleEntityManager.getInstance().createEntity(prize, giveProp.isBind(), 1, player, giveProp.getArticleColor(), needBuyProp.getArticleNum(), true);
									long mailIdd = MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { aae }, new int[] { 1 }, this.getMailTitle(), this.getMailContent(), 0L, 0L, 0L, "物品使用赠送活动");
									ActivitySubSystem.logger.warn("[物品使用赠送活动] [匹配购买数量] [成功]  [老值：" + oldvalue + "] [新值：" + currvalue + "] [使用的物品：" + article.getLogString() + "] [颜色：" + a.getColorType() + "] [奖励物品:" + giveProp.toString() + "] [发送邮件成功:" + mailIdd + "] [" + player.getLogString() + "]");
								}
								currvalue -= needBuyProp.getArticleNum();
								break;
							default:
								break;
							}
						}
						stat.colorvalue[tempColor] = currvalue;
//						stat.colorvalue[needBuyProp.getArticleColor()] = currvalue;
						ActivityManagers.getInstance().getDdc().put(player.getId() + "shop20130919" + tempColor + a.getArticleName() + this.getActivityId(), stat);
//						ActivityManagers.getInstance().getDdc().put(player.getId() + "shop20130919" + needBuyProp.getArticleColor() + a.getArticleName() + this.getActivityId(), stat);
					}
				} catch (Exception e) {
					ActivitySubSystem.logger.warn("[物品使用赠送活动] [异常]  [" + player.getLogString() + "]", e);
				}
			}
		}
	}

	@Override
	public void afterBuyGoods(Player player, Shop shop, Goods goods, int buyNum) {
	}

	public long getActivityId() {
		return activityId;
	}

	public void setActivityId(long activityId) {
		this.activityId = activityId;
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
		sb.append("[活动类型:使用赠送]");
		sb.append("[活动内容 :使用" + needBuyProp + "]<br>[赠送 : " + giveProp + "]<br>");
		String rType = "未知类型";
		if(repayType.type == 0) {
			rType = "指定数量";
		} else if(repayType.type == 1) {
			rType = "匹配购买数量";
		}
		String useType = "默认使用";
		if(this.useType == -1) {
			useType = "使用合成都算";
		} else if(this.useType == 1) {
			useType = "合成";
		}
		sb.append("[赠送类型:" + rType + "]");
		sb.append("[使用类型:" + useType + "]");
		return sb.toString();
	}

	public byte getUseType() {
		return useType;
	}

	public void setUseType(byte useType) {
		this.useType = useType;
	}

}
