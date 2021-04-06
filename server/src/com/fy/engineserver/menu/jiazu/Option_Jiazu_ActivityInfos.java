package com.fy.engineserver.menu.jiazu;

import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.ACTIVITY_INFO_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.septstation.ActivityInfo;
import com.fy.engineserver.septstation.SeptStationMapTemplet;
import com.fy.engineserver.sprite.Player;

public class Option_Jiazu_ActivityInfos extends Option implements NeedCheckPurview{

	@Override
	public void doSelect(Game game, Player player) {
		if (player.getJiazuId() == 0) {
			player.sendError(Translate.你没有家族);
			return;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			player.sendError(Translate.你没有家族);
			return;
		}
		List<ActivityInfo> activityInfos = SeptStationMapTemplet.getInstance().getActivityInfos().get(1);
		if (activityInfos == null || activityInfos.size() == 0) {
			player.sendError(Translate.text_jiazu_096);
			return;
		}

		String title = "";
		String[] icons = new String[activityInfos.size()];
		String[] notices = new String[activityInfos.size()];
		String[] names = new String[activityInfos.size()];
		for (int i = 0; i < activityInfos.size(); i++) {
			ActivityInfo activityInfo = activityInfos.get(i);
			title = activityInfo.getTitle();
			icons[i] = activityInfo.getIcon();
			notices[i] = activityInfo.getContent();
			names[i] = activityInfo.getName();
		}
		ACTIVITY_INFO_RES res = new ACTIVITY_INFO_RES(GameMessageFactory.nextSequnceNum(), title, icons, names, notices);
		player.addMessageToRightBag(res);

	}
	
	@Override
	public boolean canSee(Player player) {
		if(player.getLevel() < WindowManager.LMLV){
			return false;
		}
		return true;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
