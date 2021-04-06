package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.horse2.manager.Horse2Manager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class Buff_StealFruit extends Buff{
	int offsetRate;		//偷取产生额外物品比率
	int offsetTimes;	//可偷取次数
	int articleNum;	//产出数量
	/**
	 * 产出物品名   需要在创建buff的时候手动添加
	 */
	private String articleName;	
	/** 产出非绑定物品概率，需要再创建buff时手动添加 */
	private int unBindProb;

	@Override
	public void start(Fighter owner) {
		// TODO Auto-generated method stub
		if(owner instanceof Player){
			Player p = (Player) owner;
			BuffTemplate_StealFruit bt = (BuffTemplate_StealFruit) getTemplate();
			if(getLevel() > bt.getRate().length){
				return;
			}
			if (offsetTimes <= 0) {
				offsetTimes = bt.getDdAmount()[this.getLevel()];
			}
			offsetRate = bt.getRate()[getLevel()];
			articleNum = bt.getGiveNum()[getLevel()];
			if (Horse2Manager.logger.isInfoEnabled()) {
				Horse2Manager.logger.info("[偷果实给物品buff] [添加成功] [" + p.getLogString() + "] [rate:" + offsetRate + "] [次数:" + offsetTimes + "]");
			}
		}
	}
	/**
	 * 每次偷取果实的时候调用
	 */
	public boolean checkBuffAct(Fighter owner){
		boolean result = false;
		if(owner instanceof Player){
			Player p = (Player) owner;
			offsetTimes --;
			p.setDirty(true, "buffs");
			if(offsetTimes > 0){
				result = true;
			} else {
				this.setInvalidTime(0);
			}
			if (offsetTimes >= 0) {
				p.sendNotice(String.format(Translate.偷果实剩余次数, offsetTimes));
				int ran = p.random.nextInt(1000);
				boolean give = false;
				if (ran <= offsetRate) {
					try {
						int ran2 = p.random.nextInt(10000);
						boolean bind = true;
						if (ran2 <= unBindProb) {
							bind = false;
						}
						Article a = ArticleManager.getInstance().getArticle(articleName);
						ArticleEntity ae = DefaultArticleEntityManager.getInstance().createEntity(a, bind, ArticleEntityManager.果实偷取额外产生, p, 
								a.getColorType(), 1, true);
						MailManager.getInstance().sendMail(p.getId(), new ArticleEntity[] {ae}, new int[] { articleNum }, Translate.果实偷取邮件title, Translate.果实偷取邮件内容, 0L, 0L, 0L, "果实偷取额外产生");
						if(Horse2Manager.logger.isWarnEnabled()) {
							Horse2Manager.logger.warn("[偷果实给物品buff][偷取成功发邮件奖励]["+p.getLogString()+"][articlename:" + ae.getArticleName() + "][color:" + ae.getColorType() + "][num:" + articleNum + "]");
						}
						give = true;
						p.sendError(Translate.获得额外物品);
					} catch (Exception e) {
						Horse2Manager.logger.error("[偷果实给物品buff] [异常] [" + p.getLogString() + "] [articleName:" + articleName + "]", e);
					}
				}
				if (Horse2Manager.logger.isDebugEnabled()) {
					Horse2Manager.logger.debug("[偷果实给物品buff] [偷取果实调用checkBuffAct] [剩余次数:"+offsetTimes+"] [物品名:"+articleName+"] [数量:"+articleNum+"] [随机数:" + ran + "] [给物品比率:" + offsetRate + "] [给予额外物品结果:" + give + "] [" + p.getLogString() + "]");
				}
			}
		}
		return result;
	}

	@Override
	public void end(Fighter owner) {
		// TODO Auto-generated method stub
		super.end(owner);
	}
	public String getArticleName() {
		return articleName;
	}
	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}
	public int getUnBindProb() {
		return unBindProb;
	}
	public void setUnBindProb(int unBindProb) {
		this.unBindProb = unBindProb;
	}

}
