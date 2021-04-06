package com.fy.engineserver.menu.cave.activity;

import java.util.Date;
import java.util.Random;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.HarvestTimeBar;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.cave.CaveOption;
import com.fy.engineserver.menu.cave.activity.CaveHarvestActivityManager.CaveHarvestActivityData;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_READ_TIMEBAR_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.TimerTask;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.TimeTool;

/**
 * 用金剪刀收获
 * 
 * 
 */
public class Option_Harvest_Gold extends CaveOption implements NeedCheckPurview {
	
	public static CaveHarvestActivityData activityData = new CaveHarvestActivityData() {

		
		@Override
		public String getNotice() {
			return null;
		}

		@Override
		public void doPrize(Player player) {
//			long exp = (long) (25000 * 10 * 0.8);// 2级经验树(橙色)的80%
//			player.addExp(exp, ExperienceManager.庄园活动);
//			ActivitySubSystem.logger.error("[庄园剪刀活动] [金剪刀] [" + player.getLogString() + "] [增加经验:" + exp + "]");
			Random r = new Random();
			String rewards [] = {"宠物基础天赋包","基础通用天赋包","宠物技能升级残片","圣兽精魄礼包"};
			int props [] = {70,13,12,5};
			int randomnum = r.nextInt(100);
			int value = 0;
			int index = -1;
			for(int i=0;i<props.length;i++){
				value += props[i];
				if(value>=randomnum){
					index = i;
					break;
				}
			}
			if(index ==-1){
				ActivitySubSystem.logger.warn("[庄园剪刀活动] [额外奖励] [出错:index ==-1] [金剪刀] [randomnum:"+randomnum+"] [value:"+value+"] [index:"+index+"] ["+player.getLogString()+"]");
				return;
			}
			String articlename = rewards[index];
			Article a = ArticleManager.getInstance().getArticleByCNname(articlename);
			if(a==null){
				ActivitySubSystem.logger.warn("[庄园剪刀活动] [额外奖励] [出错:物品不存在"+articlename+"] [金剪刀] [randomnum:"+randomnum+"] [value:"+value+"] [index:"+index+"] ["+player.getLogString()+"]");
				return;
			}
			try {
				ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, player, a.getColorType(), 1, true);
				if(ae==null){
					ActivitySubSystem.logger.warn("[庄园剪刀活动] [额外奖励] [出错:创建物品失败"+articlename+"] [金剪刀] [randomnum:"+randomnum+"] [value:"+value+"] [index:"+index+"] ["+player.getLogString()+"]");
					return;
				}
				boolean success = player.putAll(new ArticleEntity[]{ae}, "金剪刀活动");
				if (success) {
					ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0L, (byte) 0, 1L, "金剪刀活动", "");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public CompoundReturn doDeduct(Player player) {
			if (player.getSilver() < 100000) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setStringValue(Translate.元宝不足);
			}
			try {
				BillingCenter.getInstance().playerExpense(player,100000, CurrencyType.YINZI, ExpenseReasonType.活动, "金剪刀活动");
			} catch (Exception e) {
				e.printStackTrace();
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setStringValue(Translate.元宝不足);
			}
			return CompoundReturn.createCompoundReturn().setBooleanValue(true);
		}

		@Override
		public int getTimes() {
			return 3;
		}
	};

	public void doSelect(Game game, Player player) {

		Cave cave = getNpc().getCave();
		if (cave == null) {
			player.sendError(Translate.text_cave_047);
			return;
		}
		boolean isSelfCave = FaeryManager.isSelfCave(player, getNpc().getId());
		if (!isSelfCave) {
			HarvestTimeBar harvest = new HarvestTimeBar(cave, getNpc(), player, activityData);
			long time = FaeryManager.getInstance().getStealTime(player);
			player.getTimerTaskAgent().createTimerTask(harvest, time, TimerTask.type_采集);
			NOTICE_CLIENT_READ_TIMEBAR_REQ timebar_REQ = new NOTICE_CLIENT_READ_TIMEBAR_REQ(GameMessageFactory.nextSequnceNum(), time, Translate.偷偷);
			player.addMessageToRightBag(timebar_REQ);
		} else {
			String timeStr = TimeTool.formatter.varChar10.format(new Date());
			// TODO 判断钱,扣钱,通知摘取完成,给奖励
			boolean canPickup = CaveHarvestActivityManager.canPickup(player, timeStr);
			if(canPickup){
				cave.setRewardTimes(3+1);
			}
			
			CompoundReturn cr = cave.pickFruit(player, getNpc().getId());
			String failreason = Translate.text_cave_001;
			if (!cr.getBooleanValue()) {
				switch (cr.getIntValue()) {
				case 1:
					failreason = Translate.text_cave_002;
					break;
				case 2:

					failreason = Translate.text_cave_021;
					break;
				case 3:
					failreason = Translate.text_cave_050;

					break;
				case 4:
					failreason = Translate.text_cave_050;

					break;
				case 5:
					failreason = Translate.text_cave_051;

					break;
				case 6:
					failreason = Translate.text_cave_052;

					break;
				case 7:
					failreason = Translate.text_cave_053;
					break;
				case 8:
					failreason = Translate.text_cave_099;
					break;
				default:
					break;
				}
			}
			if (Translate.text_cave_001.equals(failreason)) {
				player.sendNotice(failreason);
				try {
					CompoundReturn doDeduct = activityData.doDeduct(player);
					if (!doDeduct.getBooleanValue()) {
						player.sendError(doDeduct.getStringValue());
						return;
					}
					if (canPickup) {
						activityData.doPrize(player);
						CaveHarvestActivityManager.noticePickup(player, timeStr);
					} else {
						player.sendError(Translate.摘取次数达到上限);
						ActivitySubSystem.logger.error("[庄园剪刀活动] [金剪刀] [" + player.getLogString() + "] [摘取上限]");
						return;
					}
				} catch (Exception e) {
					ActivitySubSystem.logger.error("[庄园剪刀活动] [金剪刀] [" + player.getLogString() + "] [异常]", e);
				}
			} else {
				player.sendError(failreason);
			}
		}

	}

	@Override
	public boolean canSee(Player player) {
		if (CaveHarvestActivityManager.activityOpening()) {
			return true;
		}
		return false;
	}
}
