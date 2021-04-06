package com.fy.engineserver.menu.activity;

import java.util.Arrays;
import java.util.Iterator;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.dailyTurnActivity.DailyTurnManager;
import com.fy.engineserver.activity.treasure.TreasureActivityManager;
import com.fy.engineserver.activity.treasure.model.TreasureArticleModel;
import com.fy.engineserver.activity.treasure.model.TreasureModel;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.OPEN_TREASUREACTIVITY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
/**
 * 打开极地寻宝界面
 * @author Administrator
 *
 */
public class Option_Open_Treasure extends Option implements NeedCheckPurview{
	
	@Override
	public void doSelect(Game game, Player player) {
		if (!TreasureActivityManager.instance.isActivityOpen()) {
			player.sendError(Translate.活动未开启);
			return;
		}
		if (player.getLevel() < TreasureActivityManager.instance.openLevelLimit) {
			player.sendError(String.format(Translate.等级不够无法打开界面, DailyTurnManager.instance.openLevelLimit));
			return ;
		}
		long leftTime = TreasureActivityManager.instance.getActivityLeftTime() / 1000;		//返回给客户端秒数
		int len = TreasureActivityManager.instance.treasureModels.size();
		TreasureModel tm = null;			//默认打开第一个
		String description = "";			//玩法说明
		String[] treasureNames = new String[len];		//寻宝页签名
		int[] treasureIds = new int[len];			//寻宝id
		
		int tempIndex = 0;
		
		Iterator<Integer> ite = TreasureActivityManager.instance.treasureModels.keySet().iterator();
		while (ite.hasNext()) {
			int key = ite.next();
			TreasureModel value = TreasureActivityManager.instance.treasureModels.get(key);
			treasureNames[tempIndex] = value.getName();
			treasureIds[tempIndex] = value.getType();
			if (value.isIsopen() && tm == null) {
				tm = value;
			}
			tempIndex ++;
		}
		int[] digTimes = new int[tm.getDigTimes().size()];				//可挖取次数
		long[] costSilvers = new long[tm.getDigTimes().size()];			//对应消耗金钱
		for (int i=0; i<tm.getDigTimes().size(); i++) {
			digTimes[i] = tm.getDigTimes().get(i);
			costSilvers[i] = tm.getCostS().get(i);
		}
		long[] defaultArticleIds = new long[tm.getNeedShowGoods().size()];	//默认打开的寻宝界展示的物品临时id
		for (int i=0; i<defaultArticleIds.length; i++) {
			TreasureArticleModel tam = TreasureActivityManager.instance.articleModels.get(tm.getNeedShowGoods().get(i));
			defaultArticleIds[i] = tam.getTempEntityId();
		}
		description = tm.getDescription();
		
		OPEN_TREASUREACTIVITY_WINDOW_RES resp = new OPEN_TREASUREACTIVITY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), leftTime, description, 
				treasureNames, treasureIds, digTimes, costSilvers, defaultArticleIds);
		player.addMessageToRightBag(resp);
		if (ActivitySubSystem.logger.isDebugEnabled()) {
			ActivitySubSystem.logger.debug("[OPEN_TREASUREACTIVITY_WINDOW_RES_2] [" + player.getLogString() + "] [" + Arrays.toString(costSilvers) + "] [" + treasureIds + "]");;
		}
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		// TODO Auto-generated method stub
		if (!TreasureActivityManager.instance.isActivityOpen()) {
			return false;
		}
		return true;
	}

}
