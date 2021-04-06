package com.fy.engineserver.activity.TransitRobbery.Robbery;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;

public class ShenHunRobbery extends BaseRobbery{
	public RayRobbery base;
	private boolean refreashBoss = true;
	/** 雷劫结束时间 */
	private long rayOverTime;

	public ShenHunRobbery(int currentLevel, Player player) {
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
						int monsterId = RobberyConstant.SHENHUN_BOSSID;			//此怪物id需要策划单独给，怎么给再定
						log.info("[神魂劫][刷出bossid=" + monsterId + "]");
						TransitRobberyManager.getInstance().refreshMonster(game, monsterId, player, tempI++, initX, initY);
						refreashBoss = false;
						actImmortal(ran, rdm.getImmortalProbability(), rdm.getMaxImmortalAmount());
						actBeast(ran, rdm.getBeastProbability(), rdm.getMaxBeastAmount());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						afterRobbery();
						log.error("[渡劫[e:" + e + "]");
					}
				} else {												
					if(check4eachLevel(1, false)){
						juqingAct();
					} 
//					else {
//						log.error("[神魂劫][这个不科学，玩家杀死怪物了？][" + playerId + "]");
//						isSucceed = true;
//						afterRobbery();
//					}
				}
			} else {							//基础雷劫中已经败了
				isSucceed = false;
			}
		}
	}
	/**
	 * 触发神魂劫剧情
	 */
	public void juqingAct(){
		Player p;
		try {
			log.info("[触发神魂劫剧情-----------神魂劫类！！] [" + playerId + "]");
			p = GamePlayerManager.getInstance().getPlayer(playerId);
			game.shenhun_juqing(p, true);
			BuffTemplateManager btm = BuffTemplateManager.getInstance();					//触发剧情后给玩家加个无敌buff。。防止玩家挂掉
			BuffTemplate buffTemplate = btm.getBuffTemplateByName(Translate.无敌buff);
			Buff buff1 = buffTemplate.createBuff(1);
			if (buff1 != null) {
				buff1.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 2000);
				p.placeBuff(buff1);
			}
			playRayAct();
			isSucceed = true;
			afterRobbery();
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
	
	public static int[][] points = new int[][]{{2726,586}, {2495,603}, {2503,475}, {2612,717}, {2624,955},{2910,979},{2211,1213},{3111,1258}};

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
