package com.fy.engineserver.sprite.horse;

import static com.fy.engineserver.datasource.language.Translate.正在进行仙缘活动不能使用飞行坐骑;
import static com.fy.engineserver.datasource.language.Translate.正在进行论道活动不能使用飞行坐骑;

import com.fy.engineserver.activity.fateActivity.FateActivityType;
import com.fy.engineserver.activity.fateActivity.FateManager;
import com.fy.engineserver.activity.fateActivity.base.FateActivity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Callbackable;
import com.fy.engineserver.sprite.Player;

public class RideHorseProgressBar implements Callbackable {


	private long horseId;
	private Player player;
	public RideHorseProgressBar(long horseId,Player player){
		this.horseId = horseId;
		this.player = player;
	}
	
	@Override
	public void callback() {
		
		if(player != null){
			if(player.isSitdownState()){
				FateActivity  fa = FateManager.getInstance().getFateActivity(player, FateActivityType.国内仙缘.type);
				if(fa != null && fa.getState() == FateActivity.进行状态){
					player.send_HINT_REQ(正在进行仙缘活动不能使用飞行坐骑);
					if(HorseManager.logger.isWarnEnabled()){
						HorseManager.logger.warn("[骑飞行坐骑] [国内仙缘] ["+player.getLogString()+"] [horseId:"+horseId+"]");
					}
					return ;
				}
				fa = FateManager.getInstance().getFateActivity(player, FateActivityType.国外仙缘.type);
				if(fa != null && fa.getState() == FateActivity.进行状态){
					player.send_HINT_REQ(正在进行仙缘活动不能使用飞行坐骑);
					if(HorseManager.logger.isWarnEnabled()){
						HorseManager.logger.warn("[骑飞行坐骑] [国外仙缘] ["+player.getLogString()+"] [horseId:"+horseId+"]");
					}
					return ;
				}
				fa = FateManager.getInstance().getFateActivity(player, FateActivityType.国内论道.type);
				if(fa != null && fa.getState() == FateActivity.进行状态){
					player.send_HINT_REQ(正在进行论道活动不能使用飞行坐骑);
					if(HorseManager.logger.isWarnEnabled()){
						HorseManager.logger.warn("[骑飞行坐骑] [国内论道] ["+player.getLogString()+"] [horseId:"+horseId+"]");
					}
					return ;
				}
				fa = FateManager.getInstance().getFateActivity(player, FateActivityType.国外论道.type);
				if(fa != null && fa.getState() == FateActivity.进行状态){
					player.send_HINT_REQ(正在进行论道活动不能使用飞行坐骑);
					if(HorseManager.logger.isWarnEnabled()){
						HorseManager.logger.warn("[骑飞行坐骑] [国外论道] ["+player.getLogString()+"] [horseId:"+horseId+"]");
					}
					return ;
				}
			}
			if (player.isBoothState()) {
				player.sendError(Translate.摆摊中不能上坐骑);
				return;
			}
			this.player.finishupToHorse(horseId);
			if(HorseManager.logger.isWarnEnabled()){
				HorseManager.logger.warn("[骑飞行坐骑成功] ["+player.getLogString()+"] [horseId:"+horseId+"]");
			}
		}
	}

	public long getHorseId() {
		return horseId;
	}

	public void setHorseId(long horseId) {
		this.horseId = horseId;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	
}
