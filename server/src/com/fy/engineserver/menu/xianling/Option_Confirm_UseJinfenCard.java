package com.fy.engineserver.menu.xianling;

import com.fy.engineserver.activity.xianling.XianLingManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;

public class Option_Confirm_UseJinfenCard extends Option {
	private int type;
	private Article a;

	public Option_Confirm_UseJinfenCard() {
		// TODO Auto-generated constructor stub
	}

	public Option_Confirm_UseJinfenCard(int type, Article a) {
		super();
		this.type = type;
		this.a = a;
	}

	@Override
	public void doSelect(Game game, Player player) {
		if(UnitServerFunctionManager.needCloseFunctuin(Function.仙灵大会)) {
			player.sendError(Translate.合服功能关闭提示);
			return ;
		}
		XianLingManager.instance.send_USE_SCORE_CARD_RES(player, a, type);
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Article getA() {
		return a;
	}

	public void setA(Article a) {
		this.a = a;
	}

}
