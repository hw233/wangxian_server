package com.fy.engineserver.menu.society.unite;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.unite.Unite;
import com.fy.engineserver.unite.UniteManager;

public class Option_Unite_zhaoji_Agree extends Option {

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	private long inviteId;
	
	@Override
	public void doSelect(Game game, Player player) {

		Relation relation = SocialManager.getInstance().getRelationById(player.getId());
		if(relation.getUniteId() > 0){
			Unite u = UniteManager.getInstance().getUnite(relation.getUniteId());
			if(u != null){
				
				if(u.getMemberIds().contains(inviteId)){
					
					try {
						Player invite = PlayerManager.getInstance().getPlayer(inviteId);
						if(!invite.isOnline()){
//							player.sendError(invite.getName()+"不在线，不能传送");
							player.sendError(Translate.translateString(Translate.xx不在线不能传送, new String[][]{{Translate.STRING_1,invite.getName()}}));
							if (UniteManager.logger.isWarnEnabled()) {
								UniteManager.logger.warn("[兄弟令传送不能完成] [不在线不能传送] ["+player.getLogString()+"] ["+invite.getLogString()+"]");
							}
							return;
						}
						
						if(!UniteManager.getInstance().callLimit(invite)){
							player.sendError(Translate.召唤您的伙伴在限制地图);
							return;
						}
//						if(JJCManager.isJJCBattle(player.getCurrentGame().gi.name)){
//							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.不能前往混元至圣);
//							player.addMessageToRightBag(hreq);
//							return;
//						}
						int country = invite.getTransferGameCountry();
						player.setTransferGameCountry(country);
						
						Game game1 = invite.getCurrentGame();
						if(game1 == null){
							return ;
						}
						if(game1.getGameInfo().getName().contains(SeptStationManager.jiazuMapName)){
							
							if(invite.getJiazuId() > 0 ){
								if(invite.getJiazuId() != player.getJiazuId()){
//									player.sendError(invite.getName()+"在家族地图中，你俩不是一个家族不能到达");
									player.sendError(Translate.translateString(Translate.xx在家族地图中你俩不是一个家族不能到达, new String[][]{{Translate.STRING_1,invite.getName()}}));
									
									
//									invite.sendError("你在家族地图中，"+player.getName()+"跟你不是一个家族，不能到达");
									invite.sendError(Translate.translateString(Translate.你在家族地图中xx跟你不是一个家族不能到达, new String[][]{{Translate.STRING_1,player.getName()}}));
									if (UniteManager.logger.isWarnEnabled()) {
										UniteManager.logger.warn("[兄弟令传送不能完成] [不是一个家族] ["+player.getLogString()+"] ["+invite.getLogString()+"]");
									}
									return ;
								}
							}else{
								if (UniteManager.logger.isWarnEnabled()) {
									UniteManager.logger.warn("[兄弟令传送异常] [没有家族但在家族地图] ["+player.getLogString()+"] ["+invite.getLogString()+"]");
								}
							}
						}
						
						game.transferGame(player, new TransportData(0, 0, 0, 0, game1.gi.name, invite.getX(), invite.getY()));
						
							if (UniteManager.logger.isWarnEnabled()) {
								UniteManager.logger.warn("[兄弟令传送完成] ["+player.getLogString()+"] []");
							}
						
					} catch (Exception e) {
						UniteManager.logger.warn("[兄弟令传送异常] ["+player.getLogString()+"]",e);
					}
				}else{
					
					SocialManager.logger.error("[你的结义里没有这个玩家] ["+player.getLogString()+"]");
				}
				
			}else{
				SocialManager.logger.error("[你还没有结义] ["+player.getLogString()+"]");
			}
		}else{
			SocialManager.logger.error("[你还没有结义] ["+player.getLogString()+"]");
		}
			
		
	
	}

	
	public long getInviteId() {
		return inviteId;
	}

	public void setInviteId(long inviteId) {
		this.inviteId = inviteId;
	}

	
	
}
