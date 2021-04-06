package com.fy.engineserver.activity.TransitRobbery.Robbery;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.TransitRobbery.model.EachLevelDetal;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
/**
 * 
 *
 */
public class YihunRobbery extends BaseRobbery{
	public RayRobbery base;
	private boolean refreashBoss = true;
	/** 雷劫结束时间 */
	private long rayOverTime;

	public YihunRobbery(int currentLevel, Player player) {
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
				if(!checkStartTime(1, rayOverTime)){
					log.info("[渡劫][倒计时时间]");
					return;
				}
				if(refreashBoss){
					try {
						Player player = GamePlayerManager.getInstance().getPlayer(playerId);
						EachLevelDetal ed = cm.getLevelDetails().get(1);
//						String[] point = ed.getInitPoint().split(",");
						String monsterId = ed.getvCondition().split(",")[0];
						log.info("[移魂劫][刷出bossid=" + monsterId + "]");
						TransitRobberyManager.getInstance().refreshMonster(game, Integer.parseInt(monsterId), player, tempI++, initX, initY);
						refreashBoss = false;
						actImmortal(ran, rdm.getImmortalProbability(), rdm.getMaxImmortalAmount());
						actBeast(ran, rdm.getBeastProbability(), rdm.getMaxBeastAmount());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						afterRobbery();
						log.error("[渡劫][e:" + e + "]");
					}
				} else {												
					if(check4eachLevel(1, false)){
						isSucceed = true;
						afterRobbery();
					}
				}
			} else {							//基础雷劫中已经败了
				isSucceed = false;
			}
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
	public static int[][] points = new int[][]{{673,4367}, {926,4374}, {637,4555}, {848,4559}, {1023,4431},{673,4722},{747,4948},{1070,5065}};

	@Override
	public int[] getRanPoint() {
		// TODO Auto-generated method stub
		int a = ran.nextInt(points.length);
		return points[a];
	}

	@Override
	public int getPassLayer() {
		// TODO Auto-generated method stub
		int temp = 1;
		if(!base.isSucceed){
			temp = 0;
		} else if(isSucceed) {
			temp = 2;
		}
		return temp;
	}

}
