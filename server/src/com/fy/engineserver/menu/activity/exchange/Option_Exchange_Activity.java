package com.fy.engineserver.menu.activity.exchange;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.NeedRecordNPC;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;

public class Option_Exchange_Activity extends Option implements NeedCheckPurview,NeedRecordNPC{
	private String activityName;
	private NPC npc;
	
	@Override
	public void doSelect(Game game, Player player) {
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setDescriptionInUUB(activityName);
		mw.setNpcId(this.getNPC().getId());
		
		ActivitySubSystem.logger.error("[兑换活动] [activityName:" + activityName + "]");
		List<ExchangeActivity> exchangeActivities = ExchangeActivityManager.getInstance().getMenuMap().get(activityName);
		List<Option_Exchange_Activity_Menu> options = new ArrayList<Option_Exchange_Activity_Menu>();
		for (ExchangeActivity ea : exchangeActivities) {
			Option_Exchange_Activity_Menu option = new Option_Exchange_Activity_Menu(ea.getMenuName(), ea);
			option.setText(ea.getMenuName());
			options.add(option);
		}
		mw.setOptions(options.toArray(new Option_Exchange_Activity_Menu[0]));
		QUERY_WINDOW_RES req = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(req);

	}

	@Override
	public boolean canSee(Player player) {
		ExchangeActivityLimit eaLimits = ExchangeActivityManager.getInstance().getExchangeActivityLimit(activityName);
		if(player.getLevel() < WindowManager.LMLV){
			return false;
		}
		if(eaLimits.isThisServerFit()==null){
			return true;
		}
		return false;
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	@Override
	public String toString() {
		return "Option_Exchange_Activity [activityName=" + activityName + "]";
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
