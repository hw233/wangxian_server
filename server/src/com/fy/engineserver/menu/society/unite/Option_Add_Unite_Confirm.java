package com.fy.engineserver.menu.society.unite;

import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.unite.Unite;
import com.fy.engineserver.unite.UniteManager;

/**
 * 玩家加入结义，成员都同意后确定消耗
 * @author Administrator
 *
 */
public class Option_Add_Unite_Confirm extends Option {
	
	
	private Unite unite;
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		
		
		if(unite.getMemberIds().size()>= 2){
		
			List<Long> members = unite.getMemberIds();
			
			if(members.size() >= UniteManager.UNITE_MEMBER_NUM ){
				player.sendError(Translate.text_结义玩家人数已达最大);
				return ;
			}
			
			if(player.getSilver() + player.getShopSilver() > UniteManager.加入结义消耗){
				
				BillingCenter bc = BillingCenter.getInstance();
				try {
					bc.playerExpense(player, UniteManager.加入结义消耗, CurrencyType.SHOPYINZI, ExpenseReasonType.JIEYI_COST, "加入结义消耗");
				} catch (Exception e) {
					UniteManager.logger.error("[结义消耗] ["+player.getLogString()+"]",e);
					return ;
				}
			}else{
				player.sendError(Translate.银子不足);
				return ;
			}
			// 添加好友
			for(long id : members){
				try {
					Player member = PlayerManager.getInstance().getPlayer(id);
					SocialManager.getInstance().addFriend(player, member);
					SocialManager.getInstance().addFriend(member, player);
					member.send_HINT_REQ(Translate.translateString(Translate.text_xx加入了结义, new String[][] {{Translate.PLAYER_NAME_1,player.getName()}}));
				} catch (Exception e) {
					UniteManager.logger.error("[加入结义添加好友] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []",e);
				}
			}
			unite.addMember(player);
			player.send_HINT_REQ(Translate.text_你加入了结义);
			Relation re = SocialManager.getInstance().getRelationById(player.getId());
			re.setUniteId(unite.getUniteId());
			UniteManager.logger.warn("[加入结义成功] ["+player.getLogString()+"] ["+unite.getLogString()+"]");
		}else{
			player.sendError(Translate.结义已经解散);
		}
	}

	public Unite getUnite() {
		return unite;
	}

	public void setUnite(Unite unite) {
		this.unite = unite;
	}
}
