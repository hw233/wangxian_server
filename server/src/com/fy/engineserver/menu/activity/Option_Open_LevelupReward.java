package com.fy.engineserver.menu.activity;

import java.util.Iterator;

import com.fy.engineserver.activity.levelUpReward.LevelUpRewardManager;
import com.fy.engineserver.activity.levelUpReward.model.LevelUpRewardModel;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.OPEN_LEVELUPREWARD_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
/**
 * 打开购买等级礼包界面
 * @author Administrator
 *
 */
public class Option_Open_LevelupReward extends Option implements NeedCheckPurview{
	
	@Override
	public void doSelect(Game game, Player player) {
		if (!LevelUpRewardManager.instance.isActivityAct()) {
			player.sendError(Translate.活动未开启);
			return;
		}
		int len = LevelUpRewardManager.instance.rewardMaps.size();
		int[] rewardIds = new int[len];
		String[] rewardNames = new String[len];
		String[] descriptions = new String[len];
		int[] lowLevels = new int[len];
		int[] highLevels = new int[len];
		int index = 0;
		Iterator<Integer> ito = LevelUpRewardManager.instance.rewardMaps.keySet().iterator();
		while (ito.hasNext()) {
			int key = ito.next();
			LevelUpRewardModel value = LevelUpRewardManager.instance.rewardMaps.get(key);
			rewardIds[index] = value.getType();
			rewardNames[index] = value.getName();
			descriptions[index] = value.getDescription();
			lowLevels[index] = value.getLowLevelLimit();
			highLevels[index] = value.getHighLevelLimit();
			index++;
		}
		OPEN_LEVELUPREWARD_WINDOW_RES resp = new OPEN_LEVELUPREWARD_WINDOW_RES(GameMessageFactory.nextSequnceNum(), rewardIds, rewardNames, 
				descriptions, lowLevels, highLevels);
		player.addMessageToRightBag(resp);
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		// TODO Auto-generated method stub
		if (!LevelUpRewardManager.instance.isActivityAct()) {
			return false;
		}
		return true;
	}

}
