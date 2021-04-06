package com.fy.engineserver.activity.TransitRobbery.Robbery;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.TransitRobbery.model.EachLevelDetal;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
/**
 * 幻象劫
 *
 */
public class PhantomRobbery extends BaseRobbery{
	public RayRobbery base;
	private boolean refreashBoss = true;
	/** 雷劫结束时间 */
	private long rayOverTime;

	public PhantomRobbery(int currentLevel, Player player) {
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
					isStartFlag = false;
					oneRobberyUnitEnd(1);
//					int time = cm.getLevelDetails().get(1).getInterval();
//					sendRayNotify2Client(time, "距离幻象劫boss刷新还剩时间");
					rayOverTime = System.currentTimeMillis();
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
						log.info("[幻象劫][刷出bossid=" + monsterId + "]");
						TransitRobberyManager.getInstance().refreshMonster(game, Integer.parseInt(monsterId), player, tempI++, initX, initY);
						actImmortal(ran, rdm.getImmortalProbability(), rdm.getMaxImmortalAmount());
						actBeast(ran, rdm.getBeastProbability(), rdm.getMaxBeastAmount());
						refreashBoss = false;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						afterRobbery();
						log.error("[渡劫][e:" + e + "]");
					}
				} else {												//需要对boss剩余百分之30血量时触发的技能单做逻辑，目前还没有做
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
	public static int[][] points = new int[][]{{1284,2714}, {1328,3063}, {1405,3169}, {1371,2670}, {1209,2664},{1385,2948},{1517,3317},{1363,3294}};

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
