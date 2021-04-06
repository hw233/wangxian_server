package com.fy.engineserver.menu.fireActivity;

import com.fy.engineserver.activity.fireActivity.FireActivityNpc;
import com.fy.engineserver.activity.fireActivity.FireActivityNpcEntity;
import com.fy.engineserver.activity.fireActivity.FireManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Player;

/**
 * 篝火  点火
 * @author Administrator
 *
 */
public class Option_FireActivity_Fire extends Option {
	
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	/* (non-Javadoc)
	 * @see com.fy.engineserver.menu.Option#doSelect(com.fy.engineserver.core.Game, com.fy.engineserver.sprite.Player)
	 */
	@Override
	public void doSelect(Game game, Player player) {
		
		
		long jiazuId = player.getJiazuId();
		if(jiazuId <= 0){
			player.send_HINT_REQ(Translate.你还没有家族);
			return;
		}
		Jiazu jz = JiazuManager.getInstance().getJiazu(jiazuId);
		if(jz == null){
			if(FireManager.logger.isInfoEnabled())
				FireManager.logger.info("[篝火活动点火] [指定家族不存在] ["+jiazuId+"] ["+player.getLogString()+"]");
			return;
		}else{
			if(jz.getBaseID() >0){
				if(jz.getJiazuMaster() != player.getId()){
					player.send_HINT_REQ(Translate.不是家族族长);
					return;
				}
				LivingObject[] los = game.getLivingObjects();
				long septId = -1; 
				if(los != null){
					for(LivingObject lo :los){
						if(lo instanceof FireActivityNpc){
							septId = ((FireActivityNpc)lo).getSeptId();
							break;
						}
					}
				}
				if(septId >0){
					if(jz.getBaseID() == septId){
						
						SeptStation ss = SeptStationManager.getInstance().getSeptStationById(septId);
						if(ss == null){
							if(FireManager.logger.isInfoEnabled())
								FireManager.logger.info("[篝火活动点火] [指定的驻地为null] ["+septId+"] ["+player.getLogString()+"]");
							return;
						}
						FireActivityNpcEntity fe = ss.getFireActivityNpcEntity();
						if(fe != null){
							if(fe.点火(player))
							player.send_HINT_REQ(Translate.布阵成功);
						}else{
							if(FireManager.logger.isInfoEnabled())
								FireManager.logger.info("[篝火活动点火] [驻地的篝火实体为null] ["+septId+"] ["+player.getLogString()+"]");
						}
						
					}else{
						player.send_HINT_REQ(Translate.不是在你家族驻地);
					}
				}else{
					if(FireManager.logger.isInfoEnabled())
						FireManager.logger.info("[篝火活动点火] [没有找到篝火npc] ["+septId+"] ["+player.getLogString()+"]");
				}
				
			}else{
				player.send_HINT_REQ(Translate.你还没有驻地);
			}
		}
	}
}
