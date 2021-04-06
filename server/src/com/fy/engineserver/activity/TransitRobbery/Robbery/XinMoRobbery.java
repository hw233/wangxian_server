package com.fy.engineserver.activity.TransitRobbery.Robbery;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;

public class XinMoRobbery extends BaseRobbery{
	public RayRobbery base;
	private boolean refreashBoss = true;
	private long refreashBossTime;
	private long extralRayTime = 0;
	/** 雷劫结束时间 */
	private long rayOverTime;

	public XinMoRobbery(int currentLevel, Player player) {
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
//						EachLevelDetal ed = cm.getLevelDetails().get(1);
//						String[] point = ed.getInitPoint().split(",");
						int monsterId = RobberyConstant.BOSS_MODEL_IDS[player.getMainCareer()];
						TransitRobberyManager.getInstance().refreshMonster(game, monsterId, initX, initY, player, player.getName() + "("+Translate.镜像+")", 1.5f, RobberyConstant.MUTI_HP_XINMO, tempI++, (byte) 1);
						refreashBossTime = System.currentTimeMillis();
						refreashBoss = false;
						actImmortal(ran, rdm.getImmortalProbability(), rdm.getMaxImmortalAmount());
						actBeast(ran, rdm.getBeastProbability(), rdm.getMaxBeastAmount());
						sendRayNotify2Client(120, "心魔雷击倒计时", (byte) 5);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						afterRobbery();
						log.error("[渡劫][获取player出错,playerId=" + playerId + "][e:" + e + "]");
					}
				} else {
					if(check4eachLevel(1, false)){
						isSucceed = true;
						afterRobbery();
						return;
					}
					long ct = System.currentTimeMillis();
					if((ct - refreashBossTime) >= RobberyConstant.ONE_MINIT_MIl * 2){	
						if(extralRayTime == 0 || (ct - extralRayTime) >= 10 * 1000){
							extralRayTime = ct;
							rayCaustDamage();
							sendRayNotify2Client(10, "心魔雷击倒计时", (byte) 5);
						}
					}
				}
			} else {							//基础雷劫中已经败了
				isSucceed = false;
			}
		}
	}
	
	private void rayCaustDamage(){
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			player.causeDamageByRayRobbery(300000);
			playRayAct();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	public static int[][] points = new int[][]{{4391,4296}, {4139,4477}, {4377,4537}, {3881,4842}, {3719,4854},{3633,5058},{4490,4269},{4013,4326}};

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
