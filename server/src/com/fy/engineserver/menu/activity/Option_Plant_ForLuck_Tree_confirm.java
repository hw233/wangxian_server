package com.fy.engineserver.menu.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.activity.luckfruit.ForLuckFruitManager;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.homestead.cave.resource.Point;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuFunction;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_JianDingConfirm;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet.BuildingType;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.ForLuckFruitNPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.util.CompoundReturn;

/**
 * 菜单子选项，已经确定了颜色的
 * 
 * 
 */
public class Option_Plant_ForLuck_Tree_confirm extends Option {

	static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	private int color;

	public Option_Plant_ForLuck_Tree_confirm() {

	}

	public Option_Plant_ForLuck_Tree_confirm(int color) {
		this.color = color;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	/*
	 * (non-Javadoc)
	 * @see com.fy.engineserver.menu.Option#doSelect(com.fy.engineserver.core.Game, com.fy.engineserver.sprite.Player)
	 */
	@Override
	public void doSelect(Game game, Player player) {
		try {
			if (ForLuckFruitManager.logger.isInfoEnabled()) {
				ForLuckFruitManager.logger.info(player.getLogString() + "[点击了:{}颜色的树]", new Object[] { color });
			}
			if (!player.inSelfSeptStation()) {
				player.sendError(Translate.text_forluck_005);
				return;
			}
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			JiazuMember member = JiazuManager.getInstance().getJiazuMember(player.getId(), jiazu.getJiazuID());

			if (member == null) {
				player.sendError(Translate.text_forluck_005);
				return;
			}
			if (!JiazuTitle.hasPermission(member.getTitle(), JiazuFunction.handle_join_request)) {
				player.sendError(Translate.text_forluck_004);
				return;
			}
			SeptStation station = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
			if (station == null) {
				return ;
			}
			int armorLevel = station.getBuildingLevel(BuildingType.龙图阁);
			int colorType = JiazuManager2.getLuckTreePermisionLevel(armorLevel);
			if (colorType < color) {
				player.sendError(Translate.龙图阁等级不足);
				return ;
			}
			ForLuckFruitManager manager = ForLuckFruitManager.getInstance();

			int thisPlantIndex = -1;// 本次种植的位置
			for (int i = 0; i < jiazu.getFruitNPCs().length; i++) {
				if (jiazu.getFruitNPCs()[i] == null) {
					thisPlantIndex = i;
					break;
				}
			}
			if (thisPlantIndex == -1) {
				player.sendError(Translate.text_forluck_011);
				return;
			}
			long cost = manager.getPlantCost()[color];
			CompoundReturn cr = AllActivityManager.instance.notifySthHappened(AllActivityManager.platLuckTree, armorLevel, color);
			if (cr != null && cr.getLongValue() > 0) {
				cost = cr.getLongValue();
			}
			
			String des = Translate.translateString(Translate.需要花费银子, new String[][]{{Translate.STRING_1,BillingCenter.得到带单位的银两(cost)}});
			WindowManager wm = WindowManager.getInstance();
			MenuWindow mw = wm.createTempMenuWindow(600);
			mw.setTitle("");
			mw.setDescriptionInUUB(des);
			Option_Plant_ForLuck_Tree option = new Option_Plant_ForLuck_Tree(color);
			option.setText(Translate.确定);
			Option[] options = new Option[] { option, new Option_Cancel() };
			mw.setOptions(options);
			CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
			player.addMessageToRightBag(creq);
		} catch (Exception e) {
			ForLuckFruitManager.logger.error("[发布种子][异常]", e);
		}
	}
}
