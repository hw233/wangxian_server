package com.fy.engineserver.menu.activity.wolf;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;

public class Option_Wolf_HelpMESS extends Option {

	@Override
	public void doSelect(Game game, Player player) {
		String title = "天阙幻境说明";
		String des = "<f color='0xff0000'>规则说明：</f>\n"
+"1.开放等级：110\n"
+"2.每场活动参赛人数5人\n"
+"3.每场活动最大时间3分钟\n"
+"4.每场活动人员角色分配为随机分配（5人中其中一人为大灰狼其余为小羊咩咩）\n"
+"5.每场活动会在地图内随机刷新经验草，经验草只有小羊咩咩可以采集\n"
+"6.每场活动会在8个方向最顶端随机刷新天阙大宝箱，宝箱只有大灰狼和被淘汰的玩家可采集\n"
+"<f color='0xff0000'>任务说明：</f>\n"
+"任务失败：\n"
+"1.大灰狼在本场活动中没有捕捉到由玩家化身的小羊咩咩\n"
+"2.由玩家化身的小羊咩咩在本场活动全部被大灰狼捕捉\n"
+"任务成功：\n"
+"1.大灰狼在本场活动中至少捕捉1名由玩家化身的小羊咩咩\n"
+"2.由玩家化身的小羊咩咩在本场活动中至少存活1名\n"
+"任务封神：\n"
+"1.大灰狼在本场活动中捕捉全部由玩家化身的小羊咩咩\n"
+"2.由玩家化身的小羊咩咩在本场活动中全部存活\n"
+"奖励说明：\n"
+"海量经验、海量道具\n";
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
		mw.setDescriptionInUUB(des);
		mw.setTitle(title);
		Option_Cancel oc = new Option_Cancel();
		oc.setText("确定");
		mw.setOptions(new Option[]{oc});
		QUERY_WINDOW_RES req = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(req);
}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
