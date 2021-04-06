package com.fy.engineserver.activity.TransitRobbery.Robbery;

import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;



/**
 * 雷劫
 *
 */
public class RayRobbery extends BaseRobbery{
	/** 已经进入到第几重雷 */
	private int rayLevel = 1;
	/** 到遭雷劈的时候了 */
	private boolean isRayAct = true;
	/** 上次雷劫时间 */
//	private boolean lastRayTime;
	/** 上次小关触发时间 */
	public long lastDamageTime;
	/***/
	public boolean fail = false;
	/** 挨几次雷劈 */
	private int rayAmount;
	boolean isNotify = false;
//	private long tempStartTime = 0;
	
	public RayRobbery(int currentLevel, Player player) {
		// TODO Auto-generated constructor stub
		super(currentLevel, player);
		rayAmount = rayModel.getRayDamage().size();
		log.info("["+player.getId()+"][雷击次数" + rayAmount + "]");
	}
	
//	private boolean 开始倒计时(){
//		if(tempStartTime <= 0){
//			tempStartTime = System.currentTimeMillis();
////			sendRayNotify2Client(5, "雷劫开始倒计时");
//		} else if(System.currentTimeMillis() >= (tempStartTime + 5 * 1000)){
//			return true;
//		}
//		return false;
//	}
	@Override
	public int getPassLayer() {
		return isSucceed ? 1 : 0;
	}
	
	private boolean isNotify1 = false;
	
	/**
	 * 当前天劫具体操作
	 */
	@Override
	public void handlRobbery() {
		// TODO Auto-generated method stub
//		if(!开始倒计时()){
//			return;
//		}
		if(!isNotify1){
			notifyActRay(true);
			isNotify1 = true;
		}
		Player player = null;
		if(isRayAct){
			try {
				if(!GamePlayerManager.getInstance().isOnline(playerId)) {
					log.info("[基础雷劫][玩家不在线][渡劫失败" + playerId + "]");
					fail = true;
					isSucceed = false;
					return;
				}
				player = GamePlayerManager.getInstance().getPlayer(playerId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("[基础雷劫][出现异常][获取player异常,playerName=" + player.getName() + "][e:" + e + "]");
				fail = true;
				isSucceed = false;
				return;
			}
			if(!player.isOnline() || player.isDeath()){
				log.info("[基础雷劫][玩家下线或者死亡][渡劫失败,isOnline=" + player.isOnline() + "  isDeath=" + player.isDeath() + "]");
				fail = true;
				isSucceed = false;
				return;
			}
			Integer[] temp = rayModel.getDamageByTimes(rayLevel);
			if(temp == null){log.error("等级出错{}{}",player.getName(), rayLevel);return;}
			int damage = (player.getMaxHP() * temp[1]) / 100;
//			if(rdm.getSoulChallenge()){								//如果为元神挑战  雷的伤害按照元神的血上限做
//				damage = (player.getSoul(Soul.SOUL_TYPE_SOUL).getMaxHp() * temp[1]) / 100;
//			}
			
			log.info("[雷击总伤害=" + damage + "  次数=" + temp[0] + "  玩家血量=" + player.getMaxHP() + "  伤害百分比=" + temp[1] + "][" + playerId + "]");
			if(damage < rayModel.getDefaultDamage()){
				damage = rayModel.getDefaultDamage();
			} else if (damage > rayModel.getMaxDamage()) {
				damage = rayModel.getMaxDamage();
			}
			damage = damage / temp[0];
//			damage = 1000;
			boolean isPassR = true;
			for(int i=0; i<temp[0]; i++){
				playRayAct();
				if(player.causeDamageByRayRobbery(damage) == -1){
					isPassR = false;
					break;
				}
			}
			isRayAct = false;
			if(rayLevel < rayAmount){					
				log.info("[基础雷劫][伤害=" + damage + "][霹雷第几次=" + rayLevel + "][总次数=" + rayAmount + "]");
				actImmortal(player.random, rdm.getImmortalProbability(), rdm.getMaxImmortalAmount());
				actBeast(player.random, rdm.getBeastProbability(), rdm.getMaxBeastAmount());
				rayLevel ++ ;
			} else {
				if(isPassR){
					log.info("[基础雷劫][渡劫成功][player=" + playerId + "]");
					isSucceed = true;
				} else {
					isSucceed = false;
					fail = true;
				}
				rayLevel ++ ;
			}
			lastDamageTime = System.currentTimeMillis();
			sendRayNotify2Client(temp[2], "距离下次雷击:", (byte) 5);
//			sendRayNotice2Client(temp[2], "距离下次雷击还有：");
		}
	}

	private void updateRayActSta(){
		if(fail || rayLevel > rayAmount){
			return;
		}
		if(rayLevel <= 1){
			return;
		}
		int time = rayModel.getDamageByTimes(rayLevel-1)[2];
		if(System.currentTimeMillis() >= (lastDamageTime + time * 1000)){
			isRayAct = true;
		}
	}
	/**
	 * 判断是否已经通过雷劫
	 * @return
	 */
	public boolean passedRayRobbery(){
		return fail || rayLevel > rayAmount;
	}

	@Override
	public void heartBeat() {
		// TODO Auto-generated method stub
		super.heartBeat();
		if(!isStartFlag){
			return;
		}
		updateRayActSta();
		handlRobbery();
		if(passedRayRobbery() && !isNotify){
			notifyActRay(false);
			isNotify = true;
		}
		if(currentLevel == 1 && passedRayRobbery()){		//雷劫自己
			this.afterRobbery();
		}
	}
	
	public static int[][] points = new int[][]{{927,1299}, {894,1574}, {1091,1347}, {1036,1994}, {818,1720},{1082,1501},{1308,1535},{903,1531}};

	@Override
	public int[] getRanPoint() {
		// TODO Auto-generated method stub
		int a = ran.nextInt(points.length);
		return points[a];
	}
}
