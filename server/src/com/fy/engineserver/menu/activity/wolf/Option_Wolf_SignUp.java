package com.fy.engineserver.menu.activity.wolf;

import com.fy.engineserver.activity.wolf.JoinNumData;
import com.fy.engineserver.activity.wolf.WolfManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.WOLF_SIGN_UP_QUERY_RES;
import com.fy.engineserver.sprite.Player;

public class Option_Wolf_SignUp extends Option {

	@Override
	public void doSelect(Game game, Player player) {
		boolean hasSignUp = WolfManager.signList.contains(player.getId());
		int joinNums = 0;
		JoinNumData data = WolfManager.getInstance().joinNums.get(player.getId());
		if(data != null){
			joinNums = data.getJoinNums();
		}
		joinNums = WolfManager.ONE_DAY_MAX_JOIN_NUMS - joinNums;
		if(joinNums < 0){
			joinNums = 0;
		}
		String timeMess = WolfManager.getInstance().currConfig != null ?WolfManager.getInstance().currConfig.getOpenTimeMess():Translate.暂未开放敬请期待;
		String joinTimes = Translate.translateString(Translate.次数, new String[][] { { Translate.COUNT_1, String.valueOf(3) }});
		WOLF_SIGN_UP_QUERY_RES res = new WOLF_SIGN_UP_QUERY_RES(GameMessageFactory.nextSequnceNum(),Translate.规则1,Translate.失败描述,Translate.成功描述,
				Translate.封神描述,Translate.单人,joinTimes,timeMess,hasSignUp,WolfManager.getInstance().signList.size(),joinNums);
		player.addMessageToRightBag(res);
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
