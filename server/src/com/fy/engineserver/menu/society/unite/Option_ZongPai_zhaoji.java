package com.fy.engineserver.menu.society.unite;

import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.society.zongpai.Option_ZongPai_zhaoji_Agree;
import com.fy.engineserver.menu.society.zongpai.Option_ZongPai_zhaoji_DisAgree;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.tool.GlobalTool;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;

public class Option_ZongPai_zhaoji extends Option {

	private Player invite;
	private String articleName;

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doInput(Game game, Player player, String inputContent) {

		String result = ZongPaiManager.getInstance().checkUseCall(player);
		if (result == null) {
			
			boolean isXianjie = false;
			try {
				String mapname = player.getCurrentGame().gi.name;
				isXianjie = RobberyConstant.没飞升玩家不可进入的地图.contains(mapname);
			} catch (Exception e) {
				
			}
			// 删除宗主令
			ArticleEntity ae = player.getArticleEntity(articleName);
			if (ae == null) {
				player.sendError(Translate.你包里没有宗主令);
				SocialManager.logger.error("[使用宗主令] [" + player.getLogString() + "] [包里没有宗主令]");
				return;
			}
			player.removeFromKnapsacks(ae, Translate.宗主令, true);
			ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "使用宗主令", null);

			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu != null) {
				long zongPaiId = jiazu.getZongPaiId();

				Player[] ps = PlayerManager.getInstance().getOnlineInZongpai(zongPaiId);
				DevilSquareManager inst = DevilSquareManager.instance;
				
				for (Player p : ps) {
					if (p.getId() == player.getId()) {
						continue;
					}
					if(inst != null && inst.isPlayerInDevilSquare(p)) {		//恶魔广场不弹框
						continue;
					}
					if(GlobalTool.verifyTransByOther(p.getId(),player.getCurrentGame()) != null) {
						continue;
					}
					if (isXianjie && p.getLevel() <= 220) {
						continue;
					}
					try {
						WindowManager windowManager = WindowManager.getInstance();
						MenuWindow mw = windowManager.createTempMenuWindow(600);
						mw.setTitle(Translate.宗主令);
						// mw.setDescriptionInUUB(invite.getName()+Translate.使用了宗主令需要把你召唤到他身边+inputContent);
						mw.setDescriptionInUUB(Translate.translateString(Translate.xx使用了宗主令需要把你召唤到他身边xx, new String[][] { { Translate.STRING_1, invite.getName() }, { Translate.STRING_2, inputContent } }));

						Option_ZongPai_zhaoji_Agree agree = new Option_ZongPai_zhaoji_Agree(player.getId(), player.getX(), player.getY(), player.getCurrentGame().gi.getMapName(), player.getCurrentGameCountry());
						agree.setText(Translate.确定);
						Option_ZongPai_zhaoji_DisAgree disAgree = new Option_ZongPai_zhaoji_DisAgree();
						agree.setInviteId(player.getId());
						disAgree.setText(Translate.取消);
						mw.setOptions(new Option[] { agree, disAgree });

						QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
						p.addMessageToRightBag(res);
					} catch (Exception e) {
						ZongPaiManager.logger.error("[使用宗主令异常] [" + player.getLogString() + "]", e);
					}
				}

				player.sendError(Translate.使用宗主令完成请等待他们的到来);
				if (ZongPaiManager.logger.isWarnEnabled()) {
					ZongPaiManager.logger.warn("[使用宗主令完成] [" + player.getLogString() + "]");
				}
			}
		} else {
			player.sendError(result);
		}

	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public Player getInvite() {
		return invite;
	}

	public void setInvite(Player invite) {
		this.invite = invite;
	}

}
