package com.fy.engineserver.menu.activity.nianshou;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.NeedRecordNPC;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.npc.ShowTitleAsHpNPC;

public class Option_Bomb extends Option implements NeedRecordNPC {

	public static List<RateProp> props = new ArrayList<Option_Bomb.RateProp>();

	static {
		props.add(new RateProp("银块", 1, 20, 3));
		props.add(new RateProp("银块", 2, 150, 3));
		props.add(new RateProp("银块", 5, 500, 3));
		props.add(new RateProp("银块", 10, 160, 3));
		props.add(new RateProp("银块", 15, 100, 3));
		props.add(new RateProp("银块", 20, 50, 3));
		props.add(new RateProp("银块", 25, 20, 3));
		props.add(new RateProp("强化石", 1, 150, 0));
		props.add(new RateProp("强化石", 2, 300, 0));
		props.add(new RateProp("强化石", 5, 1500, 0));
		props.add(new RateProp("强化石", 10, 600, 0));
		props.add(new RateProp("强化石", 20, 285, 0));
		props.add(new RateProp("强化石", 50, 110, 0));
		props.add(new RateProp("强化石", 2, 600, 1));
		props.add(new RateProp("强化石", 5, 240, 1));
		props.add(new RateProp("强化石", 10, 110, 1));
		props.add(new RateProp("强化石", 15, 70, 1));
		props.add(new RateProp("强化石", 25, 35, 1));
		props.add(new RateProp("初级培养石", 1, 170, 3));
		props.add(new RateProp("初级培养石", 2, 300, 3));
		props.add(new RateProp("初级培养石", 5, 1500, 3));
		props.add(new RateProp("初级培养石", 10, 600, 3));
		props.add(new RateProp("初级培养石", 15, 350, 3));
		props.add(new RateProp("初级培养石", 20, 200, 3));
		props.add(new RateProp("初级培养石", 30, 200, 3));
		props.add(new RateProp("初级培养石", 40, 100, 3));
		props.add(new RateProp("初级培养石", 50, 80, 3));
		props.add(new RateProp("一品元气真丹(初级)", 1, 100, 0));
		props.add(new RateProp("一品元气真丹(初级)", 5, 400, 0));
		props.add(new RateProp("一品元气真丹(初级)", 10, 100, 0));
		props.add(new RateProp("二品元气真丹(初级)", 1, 70, 0));
		props.add(new RateProp("二品元气真丹(初级)", 5, 350, 0));
		props.add(new RateProp("二品元气真丹(初级)", 10, 70, 0));
		props.add(new RateProp("三品元气真丹(初级)", 1, 50, 0));
		props.add(new RateProp("三品元气真丹(初级)", 5, 300, 0));
		props.add(new RateProp("三品元气真丹(初级)", 10, 60, 0));

	}

	private NPC npc;

	private String articleName;

	private int reduceValue;

	private int prizerate;

	// private String prizename;

	static Random random = new Random();
	public static int rateMin = 1;
	public static int rateMax = 1;

	@Override
	public void doSelect(Game game, Player player) {
		if (!(npc instanceof ShowTitleAsHpNPC)) {
			return;
		}
		ShowTitleAsHpNPC asHpNPC = (ShowTitleAsHpNPC) npc;
		ArticleEntity ae = player.getArticleEntity(articleName);
		if (ae == null) {
			player.sendError(Translate.translateString(Translate.你没有所需道具, new String[][] { { Translate.STRING_1, articleName } }));
			return;
		}
		ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(ae.getId(), "年兽活动", true);
		if(aee==null){
			String description = Translate.删除物品不成功;
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
			player.addMessageToRightBag(hreq);
			if (ActivitySubSystem.logger.isWarnEnabled()) ActivitySubSystem.logger.warn("[年兽活动] ["+description+"] [id:"+ae.getId()+"]");
			return;
		}

		player.sendError(Translate.translateString(Translate.攻击年兽, new String[][] { { Translate.STRING_1, articleName }, { Translate.COUNT_1, String.valueOf(reduceValue) } }));
		asHpNPC.reduceValue(reduceValue);
		if (random.nextInt(100) <= prizerate) {
			RateProp randomRateProp = getRandomRateProp();
			boolean isLihua = Translate.礼花.equals(articleName);
			int mailNum = isLihua ? rateMax : rateMin;
			Article article = ArticleManager.getInstance().getArticle(randomRateProp.getPropName());
			if (article != null) {
				try {
					ArticleEntity prizeAe = ArticleEntityManager.getInstance().createEntity(article, !isLihua, ArticleEntityManager.活动, player, randomRateProp.getColor(), randomRateProp.num * mailNum, true);
					MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { prizeAe }, new int[] { randomRateProp.num * mailNum }, "系统邮件", "恭喜你获得攻击年兽礼包:" + randomRateProp.getPropName(), 0, 0, 0, "年兽活动");
				} catch (Exception e) {
					e.printStackTrace();
				}
				player.sendError(Translate.translateString(Translate.恭喜获得物品, new String[][] { { Translate.STRING_1, randomRateProp.getPropName() }, { Translate.COUNT_1, String.valueOf(randomRateProp.num * mailNum) } }));
			} else {
				ActivitySubSystem.logger.error("[年兽活动] [奖励不存在:" + articleName + "]");
			}
		}
		if (asHpNPC.getCurrentVlaue() < asHpNPC.getBornValue() / 2) {
			if (asHpNPC.getObjectScale() != 1500) {
				asHpNPC.setObjectScale((short) 1500);
				player.sendError("年兽被吓得浑身颤抖......");
			}
		}
	}

	public static RateProp getRandomRateProp() {
		int randomInt = random.nextInt(10000);
		int tempMin = 0;
		int tempMax = 0;
		for (RateProp tempRateProp : props) {
			int rate = tempRateProp.getRate();
			tempMax += rate;
			if (tempMin <= randomInt && randomInt <= tempMax) {
				return tempRateProp;
			}
			tempMin += rate;
		}
		return null;
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public int getReduceValue() {
		return reduceValue;
	}

	public void setReduceValue(int reduceValue) {
		this.reduceValue = reduceValue;
	}

	public int getPrizerate() {
		return prizerate;
	}

	public void setPrizerate(int prizerate) {
		this.prizerate = prizerate;
	}

	@Override
	public NPC getNPC() {
		return npc;
	}

	@Override
	public void setNPC(NPC npc) {
		this.npc = npc;
	}

	public static class RateProp {
		private String propName;
		private int num;
		private int rate;

		private int color;

		public RateProp(String propName, int num, int rate, int color) {
			this.propName = propName;
			this.num = num;
			this.rate = rate;
			this.color = color;
		}

		public int getColor() {
			return color;
		}

		public void setColor(int color) {
			this.color = color;
		}

		public String getPropName() {
			return propName;
		}

		public void setPropName(String propName) {
			this.propName = propName;
		}

		public int getNum() {
			return num;
		}

		public void setNum(int num) {
			this.num = num;
		}

		public int getRate() {
			return rate;
		}

		public void setRate(int rate) {
			this.rate = rate;
		}

	}

	@Override
	public Option copy(Game game, Player p) {
		Option_Bomb o = new Option_Bomb();
		o.setOptionId(this.getOptionId());
		o.setIconId(this.getIconId());
		o.setColor(this.getColor());
		o.setText(this.getText());
		o.setOId(this.getOId());
		o.setNPC(this.getNPC());
		o.setArticleName(this.getArticleName());
		o.setReduceValue(this.getReduceValue());
		o.setPrizerate(this.getPrizerate());
		return o;
	}
}
