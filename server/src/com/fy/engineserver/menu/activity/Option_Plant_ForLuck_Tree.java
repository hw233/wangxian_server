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
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.homestead.cave.resource.Point;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuFunction;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet.BuildingType;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.ForLuckFruitNPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.TimeTool;

/**
 * 菜单子选项，已经确定了颜色的
 * 
 * 
 */
public class Option_Plant_ForLuck_Tree extends Option {

	static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	private int color;

	public Option_Plant_ForLuck_Tree() {

	}

	public Option_Plant_ForLuck_Tree(int color) {
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
			long start = SystemTime.currentTimeMillis();
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
			// 如果最后一次完成时间和当前不是一天，要清除相关数据
			if (!TimeTool.instance.isSame(start, jiazu.getLastReleaseLianDanTime(), TimeTool.TimeDistance.DAY, 1)) {
				jiazu.setCurrentReleaseLianDan(0);
			}
//			if (jiazu.getCurrentReleaseLianDan() >= JiazuManager2.getLianDanNums(jiazu.getLevel())) {
//				player.sendError(Translate.text_forluck_022);
//				return;
//			}
			long cost = manager.getPlantCost()[color];
			CompoundReturn cr = AllActivityManager.instance.notifySthHappened(AllActivityManager.platLuckTree, armorLevel, color);
			if (cr != null && cr.getLongValue() > 0) {
				cost = cr.getLongValue();
			}
			/*synchronized (player) {
				if (player.getSilver() + player.getShopSilver() < cost) {
					player.sendError(Translate.translateString(Translate.text_forluck_015, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(cost) } }));
					return;
				}
				BillingCenter.getInstance().playerExpense(player, cost, CurrencyType.SHOPYINZI, ExpenseReasonType.CLIFFORD, "家族开炉炼丹", -1);
			}*/
			if (jiazu.getJiazuMoney() < cost || !jiazu.consumeJiazuMoney(cost)) {
				player.sendError(Translate.家族降级通知);
				return ;
			}
			Point point = manager.getTreePoints()[thisPlantIndex];
			if (ForLuckFruitManager.logger.isInfoEnabled()) {
				ForLuckFruitManager.logger.info("[开炉炼丹] [manager.getTreeNpcId():{}]", new Object[] { manager.getTreeNpcId() });
			}
			ForLuckFruitNPC npc = (ForLuckFruitNPC) MemoryNPCManager.getNPCManager().createNPC(manager.getTreeNpcId());
			if (npc == null) {
				player.sendError(Translate.translateString(Translate.text_forluck_012, new String[][] { { Translate.STRING_1, String.valueOf(manager.getTreeNpcId()) } }));
				ForLuckFruitManager.logger.error("NPC不存在:" + manager.getTreeNpcId());
				return;
			}
			jiazu.setLastReleaseLianDanTime(start);
			jiazu.setCurrentReleaseLianDan(jiazu.getCurrentReleaseLianDan() + 1);
			npc.setJiazuId(player.getJiazuId());
			npc.setGame(game);
			npc.setX(point.getX());
			npc.setY(point.getY());
			npc.setTotalNum(manager.getMaxFruitNum());
			npc.setLeftNum(manager.getMaxFruitNum());
			npc.setArticleColor(color);
			npc.setArticleName(manager.getFruitName());
			jiazu.getFruitNPCs()[thisPlantIndex] = npc;
			npc.onBourn();

			player.sendError(Translate.translateString(Translate.text_forluck_021, new String [][]{{Translate.STRING_1,BillingCenter.得到带单位的银两(cost)}}));

			if (ForLuckFruitManager.logger.isInfoEnabled()) {
				ForLuckFruitManager.logger.info(player.getLogString() + "[创造NPC：{}] [位置{}] [{},{}]", new Object[] { npc.getName(), thisPlantIndex, npc.getX(), npc.getY() });
			}
			StringBuffer sbf = new StringBuffer();
			sbf.append(sdf.format(new Date())).append(" ").append(player.getName()).append(Translate.消耗).append(BillingCenter.得到带单位的银两(cost)).append(Translate.text_jiazu_093).append(ArticleManager.color_article_Strings[color]).append(npc.getName());
			ChatMessageService.getInstance().sendMessageToJiazu(jiazu, sbf.toString(), "");
		} catch (Exception e) {
			ForLuckFruitManager.logger.error("[发布种子][异常]", e);
		}
	}
}
