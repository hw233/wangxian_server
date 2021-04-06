package com.fy.engineserver.menu.confirmation;

import java.util.ArrayList;
import java.util.List;

import com.fy.confirm.bean.GameActivity;
import com.fy.confirm.service.client.ClientService;
import com.fy.confirm.service.message.QUERY_ALL_ACTIVITY_RES;
import com.fy.confirm.service.server.DataManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedRecordNPC;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 
 * 
 * 
 */
public class Option_Query_Activity extends Option implements NeedRecordNPC {

	private NPC npc;

	@Override
	public void doSelect(Game game, Player player) {
		GameConstants gameConstants = GameConstants.getInstance();
		if(DataManager.logger.isInfoEnabled())
			DataManager.logger.info(player.getLogString() + "[点击查询活动] [channel:" + player.getPassport().getRegisterChannel() + "][gameName:" + gameConstants.getGameName() + "] [areaName:" + gameConstants.getServerName() + "]");
		QUERY_ALL_ACTIVITY_RES bossRes = (QUERY_ALL_ACTIVITY_RES) ClientService.getInstance().doQueryActivity(player.getPassport().getRegisterChannel(), gameConstants.getGameName(), gameConstants.getServerName());
		if(DataManager.logger.isInfoEnabled())
			DataManager.logger.info(player.getLogString() + "[点击查询活动] [返回]" + bossRes);
		if (bossRes != null) {
			GameActivity[] activities = bossRes.getActivitys();
			if (activities.length > 0) {
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(60000);
				mw.setNpcId(getNPC() == null ? 0 : getNPC().getId());
				mw.setDescriptionInUUB(Translate.text_codeactivity_002);
				List<Option> optionList = new ArrayList<Option>();
				for (int i = 0; i < activities.length; i++) {
					Option_Activity option_Activity = new Option_Activity(activities[i]);
					option_Activity.setText(activities[i].getName());
					optionList.add(option_Activity);
				}
				Option[] options = optionList.toArray(new Option[0]);
				mw.setOptions(options);
				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, options);
				player.addMessageToRightBag(res);
				if(DataManager.logger.isInfoEnabled())
					DataManager.logger.info(player.getLogString() + "[点击查询活动] [NPCID:" + mw.getNpcId() + "] [" + getNPC() + "]");
				// QUERY_CONFIRMACTION_ACTIVITY_RES res = new QUERY_CONFIRMACTION_ACTIVITY_RES(GameMessageFactory.nextSequnceNum(), activities);
				// player.addMessageToRightBag(res);
				return;
			}
		}
		player.sendError(Translate.text_codeactivity_001);
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public NPC getNPC() {
		return npc;
	}

	@Override
	public void setNPC(NPC npc) {
		this.npc = npc;
	}

}
