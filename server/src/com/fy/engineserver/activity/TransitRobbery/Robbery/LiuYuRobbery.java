package com.fy.engineserver.activity.TransitRobbery.Robbery;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.TransitRobbery.model.EachLevelDetal;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;

public class LiuYuRobbery extends BaseRobbery{
	public RayRobbery base;
	private boolean refreashBoss = true;
	/** 当前小关 */
	private int currentLevel = 1;
	/** 上一小关结束时间 */
	private long rayOverTime;
	

	public LiuYuRobbery(int currentLevel, Player player) {
		// TODO Auto-generated constructor stub
		super(currentLevel, player);
		base = new RayRobbery(currentLevel, player);
		base.isStartFlag = true;
	}

	@Override
	public void handlRobbery() {
		// TODO Auto-generated method stub
		if(base.passedRayRobbery()){			//通过雷劫
			if(base.isSucceed){			
				if(rayOverTime == 0){
					rayOverTime = System.currentTimeMillis();
					isStartFlag = false;
					oneRobberyUnitEnd(1);
					return;
				}
				if(!checkStartTime(currentLevel, rayOverTime)){
					log.info("[渡劫][倒计时时间]");
					return;
				}
				if(!isSucceed){					
					return;
				}
				if(refreashBoss){
					notifyActRay(false);
					refreashBoss();
					currentLevel ++ ;
					actImmortal(ran, rdm.getImmortalProbability(), rdm.getMaxImmortalAmount());
					actBeast(ran, rdm.getBeastProbability(), rdm.getMaxBeastAmount());
				} else {
					if(check4eachLevel(currentLevel - 1, false)){
						if(currentLevel > cm.getLevelDetails().size()){		//杀死所有怪物
							isSucceed = true;
							afterRobbery();
							return;
						}
						refreashBoss = true;
						isStartFlag = false;
						oneRobberyUnitEnd(currentLevel);
					}
				}
			} else {
				isSucceed = false;
			}
		}
	}
	
	private void refreashBoss(){
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			EachLevelDetal ed = cm.getLevelDetails().get(currentLevel);
//			String[] point = ed.getInitPoint().split(",");
			String monsterId = ed.getvCondition().split(",")[0];
			log.info("[六欲劫][刷出bossid=" + monsterId + "]");
			TransitRobberyManager.getInstance().refreshMonster(game, Integer.parseInt(monsterId), player, tempI++, initX, initY);
			refreashBoss = false;
			int time = ed.getDuration();
			sendRayNotify2Client(time, "六欲劫击杀倒计时:" + currentLevel, (byte) 4);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			afterRobbery();
			log.error("[渡劫][e:" + e + "]");
		}
	}
	
	@Override
	public void beforeRobbery() {
		// TODO Auto-generated method stub
		super.beforeRobbery();
		base.game = game;
	}
	
	@Override
	public void heartBeat() {
		// TODO Auto-generated method stub
		super.heartBeat();
		if(!isStartFlag){
			return;
		}
		base.heartBeat();
		this.handlRobbery();
	}
	
	public static int[][] points = new int[][]{{4020,2189}, {4245,2056}, {4200,2243}, {4099,2657}, {4394,2673},{3990,2745},{4526,3093},{4323,3230}};
	@Override
	public int[] getRanPoint() {
		// TODO Auto-generated method stub
		int a = ran.nextInt(points.length);
		return points[a];
	}

	@Override
	public int getPassLayer() {
		// TODO Auto-generated method stub
		if(!base.isSucceed) {
			return 0;
		}else if(isSucceed) {
			return 7;
		}
		
		return currentLevel-1;
	}

}
