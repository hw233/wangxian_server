package com.fy.engineserver.menu.activity;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuFunction;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_UseCancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet.BuildingType;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

/**
 * 点击种植祝福果树。此option将向用户弹出N个选项，分别表示N个颜色的炉子选项
 * 
 * 
 */
public class Option_Plant_ForLuck_Tree_Wanna extends Option implements NeedCheckPurview {

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public static String[] strs = Translate.炼丹炉银子;

	@Override
	public void doSelect(Game game, Player player) {
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(36000);
		mw.setDescriptionInUUB(Translate.开炉炼丹消耗银子);
		Option_UseCancel oc = new Option_UseCancel();
		oc.setText(Translate.取消);
		oc.setColor(0xffffff);
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		List<Option> options = new ArrayList<Option>();
		SeptStation station = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
		if (station == null) {
			return ;
		}
		int armorLevel = station.getBuildingLevel(BuildingType.龙图阁);

		for (int i = 0; i < ArticleManager.color_article_Strings.length; i++) {
			CompoundReturn cr = AllActivityManager.instance.notifySthHappened(AllActivityManager.platLuckTree, armorLevel, i);
			String ss = null;
			long cost = -1;
			if (cr != null && cr.getLongValue() > 0) {
				cost = cr.getLongValue();
			}
			if (cost > 0) {
				ss = String.format(Translate.炼丹炉银子2, BillingCenter.得到带单位的银两(cost));
			}
			Option_Plant_ForLuck_Tree_confirm forLuck_Tree = new Option_Plant_ForLuck_Tree_confirm(i);
			if (ss == null) {
				forLuck_Tree.setText(ArticleManager.color_article_Strings[i] + Translate.炼丹炉 + strs[i]);
			} else {
				forLuck_Tree.setText(ArticleManager.color_article_Strings[i] + Translate.炼丹炉 + ss);
				
			}
			forLuck_Tree.setColor(ArticleManager.color_article[i]);
			options.add(forLuck_Tree);
		}
		options.add(oc);
		mw.setOptions(options.toArray(new Option[0]));
		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(res);
	}

	@Override
	public boolean canSee(Player player) {
		if (!player.inSelfSeptStation()) {
			return false;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu.getLevel() <= 1) {
			return false;
		}
		JiazuMember jiazuMember = JiazuManager.getInstance().getJiazuMember(player.getId(), player.getJiazuId());
		if (jiazuMember == null) {
			return false;
		}
		return JiazuTitle.hasPermission(jiazuMember.getTitle(), JiazuFunction.plant_blessing_fruit);
//		return true;
	}
}
