package com.fy.engineserver.activity.TransitRobbery.Robbery;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.TransitRobbery.model.EachLevelDetal;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.monster.Monster;

public class SiXiangRobbery extends BaseRobbery{
	public RayRobbery base;
	private boolean refreashBoss = true;
	/** 当前小关 */
	private int currentLevel = 1;
	/** 是否通过第一关雷神 */
	private boolean passRay = false;
	/** 基础雷击通过次数 */
	private int rayTimes = 0;
	/** 上次雷击时间 */
	private long lastRayTime = 0;
	/** 上一小关结束时间 */
	private long rayOverTime;
	
	private int oldBoss;
	/** 火神技能触发时间 */
	private long huoshenTime = 0;
	private int level = 0;
	private static int[] lastTime = new int[]{50, 110, 58};		//最后一次buff时间少2s 以防玩家通过多掉血
	private static int[] chufaTime = new int[]{0,60,180};
	
	private boolean notifyFlag = false;
	
	private Monster huoshenBoss = null;

	public SiXiangRobbery(int currentLevel, Player player) {
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
					return;
				}
				if(!passRay){					//基础雷劫通过以后还有一次雷击
					checkAndHandleRay();
					return;
				}
				if(!isSucceed){					//四相劫的雷劫也有可能死人
					return;
				}
				if(refreashBoss){
					refreashBoss();
					currentLevel ++ ;
					actImmortal(ran, rdm.getImmortalProbability(), rdm.getMaxImmortalAmount());
					actBeast(ran, rdm.getBeastProbability(), rdm.getMaxBeastAmount());
				} else {
//					boolean flag = currentLevel == 3 ? false : true;
					if(check4eachLevel(currentLevel - 1, true)){
						game.removeAllMonsterById(oldBoss, 1);
						if(currentLevel > cm.getLevelDetails().size()){		//杀死所有怪物
							isSucceed = true;
							afterRobbery();
							return;
						}
						refreashBoss = true;
						isStartFlag = false;
						oneRobberyUnitEnd(currentLevel);
						try {
							Player player = GamePlayerManager.getInstance().getPlayer(playerId);
							player.sendError(Translate.boss即将刷新);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							log.error("[四像劫出错] [" + playerId + "]" + e);
						}
					} else {
						if(currentLevel == 3 && level < 3){
							if(System.currentTimeMillis() >= (huoshenTime + chufaTime[level] * 1000)){
								log.info("[四象劫触发火神buff：" + huoshenTime + "][触发间隔时间=" + chufaTime[level] + "][当前时间=" + System.currentTimeMillis() + "] [playerId=" + this.playerId + "]");
								actHuoShenBuff();
								level += 1;
							}
						}
					}
				}
			} else {
				isSucceed = false;
			}
		}
	}
	
	public Monster getHuoshen(int monsterId){
		if(huoshenBoss == null){
			for(LivingObject l : game.getLivingObjects()){
				if(l instanceof Monster){
					Monster m = (Monster) l;
					if(m.getSpriteCategoryId() == monsterId){
						huoshenBoss =  (Monster) l;
					}
				}
			}
		}
		return huoshenBoss;
	}
	
	private void actHuoShenBuff(){
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			TransitRobberyManager.getInstance().placeBuff(Translate.四象火神, player, lastTime[level], level, getHuoshen(900009));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean isRayBossFreash = false;
	
	private void checkAndHandleRay(){
		if(!notifyFlag){
			notifyFlag = true;
			notifyActRay(true);
//			sendRayNotify2Client(RobberyConstant.INTERVAL[rayTimes], "四象雷劫倒计时");
		}
		if(lastRayTime == 0 || rayTimes >= 3 || System.currentTimeMillis() >= (lastRayTime + 1000 * RobberyConstant.INTERVAL[rayTimes])){
			lastRayTime = System.currentTimeMillis();
			try {
				Player player = GamePlayerManager.getInstance().getPlayer(playerId);
				if(!isRayBossFreash){
					isRayBossFreash = true;
					currentLevel = 1;
					refreashBoss();
					currentLevel = 2;
					int damage = player.getMaxHP() * RobberyConstant.RAYDAMAGE[0] / 100;
					if(damage < RobberyConstant.MINRAYDAMAGE){
						damage = RobberyConstant.MINRAYDAMAGE;
					} else if (damage > RobberyConstant.MAXRAYDAMAGE[0]) {
						damage = RobberyConstant.MAXRAYDAMAGE[0];
					}
					sendRayNotify2Client(RobberyConstant.INTERVAL[0], "距离下次雷击:", (byte) 5);
					player.causeDamageByRayRobbery(damage);
					playRayAct();
				} else {
					rayTimes ++;
					int damage = player.getMaxHP() * RobberyConstant.RAYDAMAGE[rayTimes] / 100;
					if(damage < RobberyConstant.MINRAYDAMAGE){
						damage = RobberyConstant.MINRAYDAMAGE;
					} else if (damage > RobberyConstant.MAXRAYDAMAGE[rayTimes]) {
						damage = RobberyConstant.MAXRAYDAMAGE[rayTimes];
					}
					player.causeDamageByRayRobbery(damage);
					playRayAct();
					if(rayTimes >= 3){
						passRay = true;
						notifyActRay(false);
						isStartFlag = false;
						oneRobberyUnitEnd(2);
						refreashBoss = true;
						return;
					}
					sendRayNotify2Client(RobberyConstant.INTERVAL[rayTimes], "距离下次雷击:", (byte) 5);
//					sendRayNotice2Client(RobberyConstant.INTERVAL[rayTimes], "距离下次雷击:");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void refreashBoss(){
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			EachLevelDetal ed = cm.getLevelDetails().get(currentLevel);
//			String[] point = ed.getInitPoint().split(",");
			String monsterId = ed.getvCondition().split(",")[0];
			log.info("[四相劫][刷出bossid=" + monsterId + "]");
			TransitRobberyManager.getInstance().refreshMonster(game, Integer.parseInt(monsterId), player, tempI++, initX, initY);
			if(currentLevel == 2 && level == 0){
				game.notifyPlayersAndClearNotifyFlagThroughBucket();
				game.heartbeat();
				int time = cm.getLevelDetails().get(2).getDuration();
				sendRayNotify2Client(time, "生存倒计时", (byte) 6);
				huoshenTime = System.currentTimeMillis();
				level = 0;
				TransitRobberyManager.getInstance().placeBuff(Translate.四象火神, player, lastTime[level], level, getHuoshen(900009));
				log.info("[四象劫触发火神buff：" + huoshenTime + "][触发间隔时间=" + chufaTime[level] + "][当前时间=" + System.currentTimeMillis() + "] [playerId=" + this.playerId + "]");
				level = 1;
			}
			if(currentLevel == 3 || currentLevel == 4){
				int time = cm.getLevelDetails().get(currentLevel).getDuration();
				sendRayNotify2Client(time, "四象劫击杀倒计时:" + currentLevel, (byte) 4);
				if (currentLevel == 3) {
					player.removeFireBuff();//
				}
			}
			oldBoss = Integer.parseInt(monsterId);
			refreashBoss = false;
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
	
	public static int[][] points = new int[][]{{2181,4279}, {2343,4489}, {2340,4303}, {2676,4309}, {2735,4938},{3082,4397},{3034,4514},{2703,4487}};

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
		} else if(isSucceed) {
			return 5;
		}
		return currentLevel-1;
	}
}
