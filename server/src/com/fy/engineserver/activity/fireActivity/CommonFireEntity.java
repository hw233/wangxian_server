package com.fy.engineserver.activity.fireActivity;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.res.MapPolyArea;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.Buff_FireRate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Team;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.Utils;

public class CommonFireEntity{

	//人数的加成
	public static int[] add = {0,5,10,15,20};
	public static int[] beerQualityAffect = {16,1,2,4,8};
	public static int[] addWood = {0,5,10,20};
	
	transient int expInterval;
	transient int distance;
	transient String bufferName;
	transient int x;
	transient int y;

	long lastUpdateTime;
	
	
	public boolean 喝酒区域(Player player) {
		MapPolyArea[] areas = player.getCurrentGame().getGameInfo().getMapPolyAreaByPoint(player.getX(), player.getY());
		for (int i = 0; i< areas.length;i++) {
			if (areas[i] != null&&areas[i].name.equals("篝火区")) {
				return true;
			}
		}
		return false;
	}

	
	public void heartbeat(Game game,NPC npc){
		
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if( now - lastUpdateTime < expInterval){
			return;
		}
		setLastUpdateTime(now);
		LivingObject[] los = game.getLivingObjects();
		for(LivingObject lo : los){
			if(lo instanceof Player){
				Player player = (Player)lo;
				int i = 0;
				
				for(Buff buff :player.getBuffs()){
					
					if(buff instanceof Buff_FireRate){
//						//喝酒卡请回退
//						if(Utils.getDistanceA2B(player.getX(), player.getY(), x, y ) <= distance){
						Buff_FireRate buf = (Buff_FireRate)buff;
//						Game.logger.warn("[喝酒buff执行中-0] ["+System.currentTimeMillis()+"] ["+player.getLogString()+"]");
						if(buf.getInvalidTime() > System.currentTimeMillis()){
							if(喝酒区域(player)){
								
								++i;
								if(player.getTeam() == null){
									
								}else{
									Team team = player.getTeam();
									Player[] ps = team.getMembers().toArray(new Player[0]);
									for(Player member:ps){
										if(member != player){
											for(Buff b :member.getBuffs()){
												if(b instanceof Buff_FireRate){
													if(Utils.getDistanceA2B(member.getX(), member.getY(), x, y) <= distance){
														++i;
													}
												}
											}
											
										}
									}
								}
								
								this.addExp(buff,player, i-1);
								
							}
						}else{
							Game.logger.warn("[喝酒buff错误执行---] ["+player.getLogString()+"]");
						}
						break;
					}
				}
			}
		}
	}
	
	
	private void addExp(Buff buff ,Player player,int n){
		
		CommonFireExpTemplate expTemplate = FireManager.getInstance().getCommonFireExpMap().get(player.getLevel());
		if(expTemplate != null){
			expTemplate.addExp(buff, player, n);
		}else{
			FireManager.logger.error("[普通篝火活动心跳错误] [没有这个等级的模板] [人物等级"+player.getLevel()+"]");
		}
		
	}

	
	public static void addExpJiazu(int level,Buff buff ,Player player,int n,boolean bln){
		
		CommonFireExpTemplate expTemplate = FireManager.getInstance().getCommonFireExpMap().get(player.getLevel());
		if(expTemplate != null){
			expTemplate.addExpJiazu(level,buff, player, n,bln);
		}else{
			FireManager.logger.error("[家族篝火活动心跳错误] [没有这个等级的模板] [人物等级"+player.getLevel()+"]");
		}
		
	}
	

	public int getExpInterval() {
		return expInterval;
	}


	public void setExpInterval(int expInterval) {
		this.expInterval = expInterval;
	}


	public int getDistance() {
		return distance;
	}


	public void setDistance(int distance) {
		this.distance = distance;
	}


	public String getBufferName() {
		return bufferName;
	}


	public void setBufferName(String bufferName) {
		this.bufferName = bufferName;
	}


	public long getLastUpdateTime() {
		return lastUpdateTime;
	}


	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}


	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}
	
}
