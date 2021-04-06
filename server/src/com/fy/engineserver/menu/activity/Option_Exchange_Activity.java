package com.fy.engineserver.menu.activity;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.activity.exchange.ExchangeActivity;
import com.fy.engineserver.menu.activity.exchange.ExchangeActivityManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_ARTICLE_EXCHANGE_RES;
import com.fy.engineserver.sprite.Player;
/**
 * 6.18新功能兑换活动
 * 
 *
 */
public class Option_Exchange_Activity extends Option {

	private int optionType;
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	
	@Override
	public void doSelect(Game game, Player player) {
		System.out.println("optionType:"+optionType+"--player:"+player.getLogString());
		List<ExchangeActivity> activities = ExchangeActivityManager.getInstance().activities;
		if(activities==null || activities.size()==0){
			QUERY_ARTICLE_EXCHANGE_RES  res = new QUERY_ARTICLE_EXCHANGE_RES(GameMessageFactory.nextSequnceNum(),optionType, Translate.没有活动配方, -1L);
			player.addMessageToRightBag(res);
			return;
		}
		
		StringBuffer sb = new StringBuffer();
		List<ExchangeActivity> list = new ArrayList<ExchangeActivity>();
		long now = System.currentTimeMillis();
		for(ExchangeActivity activity : activities){
			if(activity.getRewardType()==optionType){
				if(activity.getStartTime()<now && now<activity.getEndTime()){
					list.add(activity);
					sb.append(activity.getPeifangName()).append("\n");
				}
			}
		}
		if(sb.length()==0){
			sb.append(Translate.没有活动配方);
		}
		QUERY_ARTICLE_EXCHANGE_RES  res = new QUERY_ARTICLE_EXCHANGE_RES(GameMessageFactory.nextSequnceNum(), optionType,sb.toString(), -1L);
		player.addMessageToRightBag(res);
	}


	public int getOptionType() {
		return optionType;
	}
	public void setOptionType(int optionType) {
		this.optionType = optionType;
	}
	
}
