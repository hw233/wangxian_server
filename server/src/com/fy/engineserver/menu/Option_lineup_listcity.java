package com.fy.engineserver.menu;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.DownCityInfo;
import com.fy.engineserver.lineup.LineupManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.text.StringUtil;

public class Option_lineup_listcity extends Option {
	
	public static final int PAGENUM = 5;
	
	public int index = 0;
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		
		LineupManager lum = LineupManager.getInstance();
		
		if(player.getLevel() < lum.getStartlevel()) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0, Translate.text_5292);
			player.addMessageToRightBag(req);
			return;
		}		
		
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(120);
		mw.setTitle(Translate.text_5308);
				
		List<DownCityInfo> dcs = lum.getPlayerCanEnterCommonCities(player);
		int total = dcs.size()/PAGENUM;
		if(dcs.size() % PAGENUM != 0) {
			total++;
		}
		List<DownCityInfo> shows = new ArrayList<DownCityInfo>();
		for(int i=0; i<dcs.size(); i++) {
			if(i>=PAGENUM*index && i<PAGENUM*(index+1)) {
				shows.add(dcs.get(i));
			}
		}
		Option ops[] = new Option[shows.size()];
		for(int i=0; i<shows.size(); i++) {
			Option_lineup_choosecity oc = new Option_lineup_choosecity(shows.get(i).getName());
			ops[i] = oc;
			String text = shows.get(i).getName();
			if(lum.isPlayerInQueue(player, shows.get(i).getName())) {
				text = text + Translate.text_222;
			}
			oc.setText(text);
			oc.setOptionId(StringUtil.randomString(10));
			oc.setColor(0xffffff);
			oc.setIconId("153");
		}
		if(index>0) {
			Option_lineup_listcity oc = new Option_lineup_listcity();
			oc.index = index-1;
			oc.setText(Translate.text_165);
			oc.setOptionId(StringUtil.randomString(10));
			oc.setColor(0xffffff);
			oc.setIconId("156");
			Option _ops[] = new Option[ops.length+1];
			System.arraycopy(ops, 0, _ops, 0, ops.length);
			_ops[_ops.length-1] = oc;
			ops = _ops;
		}
		if(index < total-1) {
			Option_lineup_listcity oc = new Option_lineup_listcity();
			oc.index = index+1;
			oc.setText(Translate.text_166);
			oc.setOptionId(StringUtil.randomString(10));
			oc.setColor(0xffffff);
			oc.setIconId("156");
			Option _ops[] = new Option[ops.length+1];
			System.arraycopy(ops, 0, _ops, 0, ops.length);
			_ops[_ops.length-1] = oc;
			ops = _ops;
		}
		
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
		return OptionConstants.SERVER_FUNCTION_排队副本列表;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_5309;
	}

}
