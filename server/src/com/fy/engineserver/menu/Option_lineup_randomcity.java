package com.fy.engineserver.menu;

import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.DownCityInfo;
import com.fy.engineserver.lineup.LineupManager;
import com.fy.engineserver.lineup.TeamRole;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.text.StringUtil;

public class Option_lineup_randomcity extends Option {
	
	public Option_lineup_randomcity() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		
		//LineupManager.log.debug("[trace_in] [Option_lineup_randomcity] ["+player.getName()+"]");
		
		LineupManager lum = LineupManager.getInstance();
		
		if(player.getLevel() < lum.getStartlevel()) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0, Translate.text_5292);
			player.addMessageToRightBag(req);
			return;
		}
		
		List<DownCityInfo> dcis = lum.getPlayerCanEnterRandomCommonCities(player);
		if(dcis == null) {
			return;
		}
		String[] downCityNames = new String[dcis.size()];
		for(int i = 0; i < dcis.size(); i++){
			DownCityInfo dci = dcis.get(i);
			if(dci != null){
				downCityNames[i] = dci.getName();
			}
		}
		
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(120);
		mw.setTitle(Translate.text_5296);
		
		Option ops[] = new Option[3];
		Option_lineup_randomchooserole oc = new Option_lineup_randomchooserole(downCityNames,TeamRole.坦克);
		ops[0] = oc;
		oc.setText(TeamRole.getRoleDesp(TeamRole.坦克));
		oc.setOptionId(StringUtil.randomString(10));
		oc.setColor(0xffffff);
		oc.setIconId(TeamRole.iconid[TeamRole.坦克]);
		
		
		oc = new Option_lineup_randomchooserole(downCityNames,TeamRole.奶妈);
		ops[1] = oc;
		oc.setText(TeamRole.getRoleDesp(TeamRole.奶妈));
		oc.setOptionId(StringUtil.randomString(10));
		oc.setColor(0xffffff);
		oc.setIconId(TeamRole.iconid[TeamRole.奶妈]);
		
		
		oc = new Option_lineup_randomchooserole(downCityNames,TeamRole.输出);
		ops[2] = oc;
		oc.setText(TeamRole.getRoleDesp(TeamRole.输出));
		oc.setOptionId(StringUtil.randomString(10));
		oc.setColor(0xffffff);
		oc.setIconId(TeamRole.iconid[TeamRole.输出]);
		
		
		mw.setOptions(ops);
		
		mw.setDescriptionInUUB("");
		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),mw,mw.getOptions());
		player.addMessageToRightBag(res);
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_排队进入副本选择副本;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_5114;
	}

}
