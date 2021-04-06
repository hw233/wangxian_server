package com.fy.engineserver.menu.enchant;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.horseInlay.HorseEquInlayEntityManager;
import com.fy.engineserver.datasource.article.data.horseInlay.HorseEquInlayManager;
import com.fy.engineserver.datasource.article.data.horseInlay.module.InlayModule;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.INLAYTAKE_WINDOW_RES;
import com.fy.engineserver.sprite.Player;

public class Option_HorseEquPunch extends Option implements NeedCheckPurview{
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		String[] needArticles = new String[HorseEquInlayManager.MAX_INLAY_NUM];
		int[] haveNums = new int[HorseEquInlayManager.MAX_INLAY_NUM];
		Knapsack bag = player.getKnapsack_common();
		for (int i=0; i<needArticles.length; i++) {
			InlayModule module = HorseEquInlayManager.getInst().inlayMap.get(i);
			String[] tempStr = module.getCostByType(HorseEquInlayEntityManager.开孔);
			needArticles[i] = tempStr[0];
			haveNums[i] = bag.countArticle(needArticles[i]);
		}
		INLAYTAKE_WINDOW_RES resp = new INLAYTAKE_WINDOW_RES(GameMessageFactory.nextSequnceNum(), 1, needArticles, haveNums);
		player.addMessageToRightBag(resp);
	}
	
	@Override
	public boolean canSee(Player player) {
		// TODO Auto-generated method stub
		if(player.getLevel() < WindowManager.LMLV){
			return false;
		}
		return true;
	}
	
}
