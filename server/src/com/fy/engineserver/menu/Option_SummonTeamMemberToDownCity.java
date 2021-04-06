package com.fy.engineserver.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Team;
import com.xuanzhi.tools.text.StringUtil;

public class Option_SummonTeamMemberToDownCity extends Option {
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_召唤队友;
	}

	public void doSelect(Game game, Player player) {
		Team team = player.getTeam();
		if(team == null){
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5438);
			player.addMessageToRightBag(req);
//			CoreSubSystem.logger.info("[召唤队友进入副本][失败][" + player.getName() + "][没有组队]");
			if(CoreSubSystem.logger.isInfoEnabled())
				CoreSubSystem.logger.info("[召唤队友进入副本][失败][{}][没有组队]", new Object[]{player.getName()});
			return;
		}
		
		List<Player> tps = team.getMembers();
		List<Fighter> l = Arrays.asList(game.getVisbleFighter(player, false));
		
		List<Player> pl = new ArrayList<Player>();
		
		for(Player p : tps){
			//队伍里面，不是自己且不在身边
			if(p != player && !l.contains(p)){
				pl.add(p);
			}
		}
		
		for(Player p : pl){
			WindowManager wm = WindowManager.getInstance();
			MenuWindow mw = wm.createTempMenuWindow(120);
			mw.setTitle(Translate.text_5439);
			
			Option ops[] = new Option[2];
			Option_AgreenSummonToDownCity oc = new Option_AgreenSummonToDownCity();
			ops[0] = oc;
			oc.setText(Translate.text_5440);
			oc.setTargetGame(player.getCurrentGame());
			oc.setX((int)player.getX());
			oc.setY((int)player.getY());
			oc.setOptionId(StringUtil.randomString(10));
			oc.setColor(0xffffff);
			oc.setIconId("153");
			
			ops[1] = new Option_Cancel();
			ops[1].setOptionId(StringUtil.randomString(10));
			ops[1].setText(Translate.text_4055);
			ops[1].setColor(0xffffff);
			ops[1].setIconId("172");
			
			mw.setOptions(ops);
			
			mw.setDescriptionInUUB(p.getName()+Translate.text_5441 + player.getName() + Translate.text_5442);
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),mw,mw.getOptions());
			p.addMessageToRightBag(res);
		}
	}
}
