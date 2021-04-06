package com.fy.engineserver.menu.fateActivity;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 活动  对方拒绝
 * @author Administrator
 *
 */
public class Option_FateActivity_Giveup extends Option {
	
	public byte type;
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		
			
//			if(player.getActivityId(type) > 0){
//				FateActivity fa = FateManager.getInstance().getFateActivity(player.getActivityId(type));
//				if(fa != null){
//					if(fa.getState() >= FateActivity.进行状态){
//						player.send_HINT_REQ("不能放弃");
//					}else{
//						fa.setState(FateActivity.选人状态);
//						fa.setInvitedId(-1);
//						player.setActivityId(-1,type);
//						player.send_HINT_REQ("你放弃了任务");
//						try {
//							Player invite = PlayerManager.getInstance().getPlayer(fa.getInviteId());
//							invite.send_HINT_REQ(player.getName()+"放弃了,请重新选人");
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}else{
//				FateManager.logger.error("[拒绝活动错误] [参数错误] ["+type+"]");
//			}
		
	}

}
