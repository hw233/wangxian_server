package com.fy.engineserver.menu;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.stat.ArticleStatManager;
public class Option_Cangku_ConfirmAddCell2 extends Option{

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){

		ArticleEntity ae = player.getArticleEntity(Player.扩展仓库物品);
		if(ae == null){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.您的没有提升仓库空间的材料);
			player.addMessageToRightBag(hreq);
			return;
		}
		//getKnapsacks_cangku
		if(player.getKnapsacks_warehouse().getCells().length >= Player.仓库最大格子数量){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.仓库已经扩展到最大);
			player.addMessageToRightBag(hreq);
			return;
		}
		
		ArticleEntity remove = player.removeFromKnapsacks(ae,"增加仓库格子删除", true);
		if(remove != null){
			
			//统计
			ArticleStatManager.addToArticleStat(player, null, remove, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "增加仓库格子删除", null);

			player.getKnapsacks_warehouse().addCells(Player.每次增加格子数量);
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.translateString(Translate.增加仓库格子成功,new String[][]{{Translate.COUNT_1,player.getKnapsacks_warehouse().getCells().length+""}}));
			player.addMessageToRightBag(hreq);
			if(AchievementManager.getInstance() != null){
				AchievementManager.getInstance().record(player, RecordAction.扩展仓库格数, Player.每次增加格子数量);
			}
			return;
		}else{
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.扣除物品失败);
			player.addMessageToRightBag(hreq);
			return;
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_OPEN_CANGKU;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.服务器选项;
	}
}
