package com.fy.engineserver.menu.fairyBuddha;

import java.util.List;

import com.fy.engineserver.activity.fairyBuddha.FairyBuddhaManager;
import com.fy.engineserver.activity.fairyBuddha.ThankAward;
import com.fy.engineserver.activity.fairyBuddha.Voter;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.FAIRY_DOTHANK_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;

public class Option_Confirm_Thank extends Option {
	private ThankAward ta;
	private List<Voter> voterList;
	private long cost;
	private byte level;
	private long messageSequnceNum;

	public Option_Confirm_Thank(ThankAward ta, List<Voter> voterList, long cost, byte level, long messageSequnceNum) {
		this.ta = ta;
		this.voterList = voterList;
		this.cost = cost;
		this.level = level;
		this.messageSequnceNum = messageSequnceNum;
	}

	@Override
	public void doSelect(Game game, Player player) {
		String result = "";
		try {
			FairyBuddhaManager fbm = FairyBuddhaManager.getInstance();
			String notice = "";
			String articleCNName = ta.getArticleCNNames()[level];
			if (cost <= player.getSilver()) {
				BillingCenter.getInstance().playerExpense(player, cost, CurrencyType.YINZI, ExpenseReasonType.仙尊答谢, "仙尊答谢");
			} else {
				player.sendError(Translate.余额不足);
				return;
			}
			Article a = ArticleManager.getInstance().getArticleByCNname(articleCNName);
			if (a != null) {
				ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, a.getBindStyle() == 0 ? false : true, ArticleEntityManager.仙尊答谢, player, a.getColorType(), 1, true);
				if (ae != null) {
					notice = ae.getArticleName() + "*1";
					// List<Player> playerList = new ArrayList<Player>();
					for (Voter voter : voterList) {
						if (voter.getId() != player.getId()) {
							Player p = PlayerManager.getInstance().getPlayer(voter.getId());
							if (p != null) {
								MailManager.getInstance().sendMail(p.getId(), new ArticleEntity[] { ae }, Translate.答谢标题, Translate.translateString(Translate.答谢内容, new String[][] { { Translate.STRING_1, player.getName() } }), 0, 0, 0, "仙尊答谢");
							}
						}
					}
				}
			}
			fbm.disk.put(fbm.getKey(0) + fbm.KEY_答谢奖励等级 + "_" + player.getId(), level);
			result = Translate.translateString(Translate.答谢成功, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(cost) }, { Translate.STRING_2, notice } });
		} catch (Exception e) {
			result = Translate.出现异常;
			e.printStackTrace();
		}
		FAIRY_DOTHANK_RES res = new FAIRY_DOTHANK_RES(messageSequnceNum, result);
		player.addMessageToRightBag(res);
	}

	public ThankAward getTa() {
		return ta;
	}

	public void setTa(ThankAward ta) {
		this.ta = ta;
	}

	public List<Voter> getVoterList() {
		return voterList;
	}

	public void setVoterList(List<Voter> voterList) {
		this.voterList = voterList;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
