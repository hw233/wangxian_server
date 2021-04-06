package com.fy.engineserver.activity.TransitRobbery.Robbery;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.TransitRobbery.model.EachLevelDetal;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;

public class WuXiangRobbery extends BaseRobbery{
	public RayRobbery base;
	private boolean refreashBoss = true;
	/** 上一小关结束时间 */
	private long rayOverTime;
	
	public WuXiangRobbery(int currentLevel, Player player) {
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
				if(refreashBoss){
					if(rayOverTime == 0){
						rayOverTime = System.currentTimeMillis();
						isStartFlag = false;
						oneRobberyUnitEnd(1);
						return;
					}
					try {
						Player player = GamePlayerManager.getInstance().getPlayer(playerId);
						EachLevelDetal ed = cm.getLevelDetails().get(1);
//						String[] point = ed.getInitPoint().split(",");
						String monsterId = ed.getvCondition().split(",")[0];
						log.info("[幻象劫][刷出bossid=" + monsterId + "]");
						TransitRobberyManager.getInstance().refreshMonster(game, Integer.parseInt(monsterId), player, tempI++, initX, initY);
						refreashBoss = false;
						actImmortal(ran, rdm.getImmortalProbability(), rdm.getMaxImmortalAmount());
						actBeast(ran, rdm.getBeastProbability(), rdm.getMaxBeastAmount());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						isSucceed = false;
						afterRobbery();
						log.error("[渡劫][e:" + e + "]");
					}
				} else {												
					if(check4eachLevel(1, false)){
						juqingAct();
					} 
				}
			} else {							//基础雷劫中已经败了
				isSucceed = false;
			}
		}
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
	
	/**
	 * 检测是否通关
	 * @return
	 */
	public void juqingAct(){
		log.info("[无相][剧情触发][" + playerId + "]");
		@SuppressWarnings("unused")
		Player player;
		try {
			player = GamePlayerManager.getInstance().getPlayer(playerId);
//			TransitRobberyManager.getInstance().refreshNPC(game, RobberyConstant.NPCID, player);
//			game.shenhun_juqing(player, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isSucceed = true;
		afterRobbery();
	}
	
	@Override
	public void beforeRobbery() {
		// TODO Auto-generated method stub
		super.beforeRobbery();
		base.game = game;
	}
	
	public static int[][] points = new int[][]{{2762,2199}, {2446,2176}, {2702,2054}, {2316,2565}, {2620,2640},{2066,2406},{3077,2502},{2810,2793}};

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
			return 2;
		}
		return 1;
	}

}
