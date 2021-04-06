package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.dig.DigPropsEntity;
import com.fy.engineserver.activity.dig.DigTemplate;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;

public class Option_Diliver extends Option {

	private DigPropsEntity ae;
	private String transferArticleName=Translate.天录传送符;

	public Option_Diliver(DigPropsEntity ae) {
		this.ae = ae;
	}

	@Override
	public void doSelect(Game game, Player player) {
		// TODO 判断玩家是否有传送符
		Article a = ArticleManager.getInstance().getArticle(transferArticleName);
		if(a==null){
			player.sendError(Translate.translateString(Translate.物品不存在提示, new String[][]{{Translate.STRING_1,transferArticleName}}));
			return;
		}
		int hasNum = player.getArticleNum(a.getName(), a.getColorType(), BindType.BOTH);
		if (hasNum > 0) {
			try {
				player.removeArticleByNameColorAndBind(a.getName(), a.getColorType(), BindType.BOTH, "挖宝传送使用删除", true);
				DigTemplate dt = player.getDigInfo().get(ae.getId());
				// 传送
				Game game1 = GameManager.getInstance().getGameByName(dt.getMapName(), dt.getCountry());

				if (game1 == null) {
					game1 = GameManager.getInstance().getGameByName(dt.getMapName(), CountryManager.中立);
				}
				TransportData transportData = new TransportData(0, 0, 0, 0, dt.getMapName(), dt.getPoints().x, dt.getPoints().y);
				if (game1 != null) {
					player.setTransferGameCountry(dt.getCountry());
					game.transferGame(player, transportData);
				}
				TaskSubSystem.logger.error(player.getLogString() + "[挖宝传送到:" + dt.getMapName() + "] [(x,y)(" + dt.getPoints().x + "," + dt.getPoints().y + ")]");
			} catch (Exception e) {
				TaskSubSystem.logger.error(player.getLogString() + e);
				e.printStackTrace();
			}
		} else {
			// TODO 提示玩家没有传送道具
			player.sendError(Translate.没有传送道具+transferArticleName);
		}
	}

	@Override
	public byte getOType() {
		// TODO Auto-generated method stub
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public DigPropsEntity getAe() {
		return ae;
	}

	public void setAe(DigPropsEntity ae) {
		this.ae = ae;
	}

}
