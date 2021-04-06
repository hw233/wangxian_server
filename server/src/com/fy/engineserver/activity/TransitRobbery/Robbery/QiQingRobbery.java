package com.fy.engineserver.activity.TransitRobbery.Robbery;

import java.util.List;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.country.data.Country;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.masterAndPrentice.MasterPrentice;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.unite.Unite;
import com.fy.engineserver.unite.UniteManager;

public class QiQingRobbery extends BaseRobbery{
	public RayRobbery base;
	private boolean refreashBoss = true;
	private int currentLevel = 1;
	/** 上一小关结束时间 */
	private long rayOverTime;

	public QiQingRobbery(int currentLevel, Player player) {
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
					rayOverTime = System.currentTimeMillis();
					return;
				}
				if(!checkStartTime(currentLevel, rayOverTime)){
					log.info("[渡劫][倒计时时间]");
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
						if(currentLevel > cm.getLevelDetails().size()){
							isSucceed = true;
							afterRobbery();
							return;
						}
						refreashBoss = true;
						isStartFlag = false;
						oneRobberyUnitEnd(currentLevel);
					}
				}
			} else {							//基础雷劫中已经败了
				isSucceed = false;
			}
		}
	}
	
	private void refreashBoss(){
		try {
			Player p = GamePlayerManager.getInstance().getPlayer(playerId);				//渡劫本人
			Player player = null;		
			try {
				player = getPlayer(p);
			} catch (Exception e) {
				log.error("[七情劫] [出错 ] [" + p.getLogString() + "]", e );
			}
			if(player == null) {
				player = p;
			}
			String monsterName = "default";
//			EachLevelDetal ed = cm.getLevelDetails().get(currentLevel);
//			String[] point = ed.getInitPoint().split(",");
			int monsterId = RobberyConstant.BOSS_MODEL_IDS[0];
			log.info("刷出来的怪物id=" + monsterId);
			log.info("坐标=" + monsterId);
			log.info("currentLevel=" + currentLevel);
			if(player != null){
				monsterId = RobberyConstant.BOSS_MODEL_IDS[player.getMainCareer()];
				monsterName = player.getName();			
				if(player.getId() == p.getId()) {
					int tempIndex = currentLevel;
					if(tempIndex >= RobberyConstant.qiqingjieBoss.length) {
						tempIndex = RobberyConstant.qiqingjieBoss.length - 1;
					}
					monsterName = RobberyConstant.qiqingjieBoss[tempIndex];
				}
			}
			TransitRobberyManager.getInstance().refreshMonster(game, monsterId, initX, 
					initY, p, monsterName, RobberyConstant.MUTI_PROPERTIES[currentLevel-1], RobberyConstant.MUTI_HP_PROPERTIES[currentLevel-1], tempI++, (byte) 2);
			refreashBoss = false;
			if(currentLevel == 7){
				int time = cm.getLevelDetails().get(7).getDuration();
				sendRayNotify2Client(time, "击杀倒计时", (byte) 4);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			afterRobbery();
			log.error("[渡劫][获取player出错,playerId=" + playerId + "]", e);
		}
	}
	
	private Player getPlayer(Player p){			//需要再此处制定相应的规则取得玩家好友或者其他备用的player
		Player player = null;
		SocialManager socialManager = SocialManager.getInstance();
		Relation relation = socialManager.getRelationById(p.getId());
		MasterPrentice mp = relation.getMasterPrentice();
		try{
		switch (currentLevel) {
			case 1: 			//徒弟
				List<Long> preTices = mp.getPrentices();				//徒弟列表
				if(preTices != null && preTices.size() > 0){
					player = GamePlayerManager.getInstance().getPlayer(p.random.nextInt(preTices.size()));
				}
				break;
			case 2: 			//师父
				long masterId = mp.getMasterId();						//师父id
				if(masterId > 0){
					player = GamePlayerManager.getInstance().getPlayer(masterId);
				}
				break;
			case 3: 			//配偶
				String coupleName = relation.getCoupleName();			//夫妻名字
				if(coupleName != null){
					player = GamePlayerManager.getInstance().getPlayer(coupleName);
				}
				break;
			case 4: 			//仇人
				List<Long> chourenList = relation.getChourenlist();
				if(chourenList != null && chourenList.size() > 0){
					player = GamePlayerManager.getInstance().getPlayer(p.random.nextInt(chourenList.size()));
				}
				break;
			case 5: 			//家族族长
				Jiazu jiazu = JiazuManager.getInstance().getJiazu(p.getJiazuId());
				if(jiazu != null){
					player = GamePlayerManager.getInstance().getPlayer(jiazu.getJiazuMaster());
				}
				break;
			case 6: 			//兄弟
				long unitedId = relation.getUniteId();
				if(unitedId != -1){
					Unite u = UniteManager.getInstance().getUnite(unitedId);
					if(u != null){
						List<Long> list = u.getMemberIds();
						if(list != null && list.size() > 0){
							player = GamePlayerManager.getInstance().getPlayer(p.random.nextInt(list.size()));
						}
					}
				}
				break;
			case 7: 			//混元至圣？
				CountryManager cm = CountryManager.getInstance();
				if(cm == null || cm.countryMap == null){
					break;
				}
				Country country = cm.countryMap.get(p.getCountry());
				if(country != null){
					long kingId = country.getKingId();
					try{
						player = GamePlayerManager.getInstance().getPlayer(kingId);
					}catch(Exception e){
						player = null;
						log.error("获取混元至圣信息出错:" , e);
					}
				}
				break;
			default:
				log.error("[七情劫][传进来的参数不对level=" + currentLevel + "]");
				break;
			}
		
			if(player == null){				//之前没有查到玩家   先查好友列表
				List<Long> friendList =relation.getFriendlist();
				if(friendList != null && friendList.size() > 0){
					player = GamePlayerManager.getInstance().getPlayer(p.random.nextInt(friendList.size()));
				}
			}
			if(player == null){			//好友列表没有查仇人
				List<Long> chourenList =relation.getChourenlist();
				if(chourenList != null && chourenList.size() > 0){
					player = GamePlayerManager.getInstance().getPlayer(p.random.nextInt(chourenList.size()));
				}
			}
			try {
				if(player == null){			//仇人列表都没有查排行榜----------------
					Billboard bb = BillboardsManager.getInstance().getBillboard("等级", "全部");
					BillboardDate[] datas = bb.getDates();
					if(datas != null){
						String[] str = datas[0].getDateValues();
						String name = str[0];
						player = GamePlayerManager.getInstance().getPlayer(name);
					}
				}
			} catch(Exception e) {
				log.warn("[七情劫] [ 取排行榜角色数据错误 ] [" + p.getLogString() + "]", e);
			}
			if(player == null){
				player = p;
			}
			log.info("获取七情劫人物===" + player.getName());
		} catch(Exception e){
			player = p;
			log.error("[七情劫][" + player.getLogString() + "][获取好友player错误,e:" + e + "]");
		}
		return player;
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

	public static int[][] points = new int[][]{{4411,965}, {4244,1129}, {4939,1167}, {4949,1360}, {4909,1362},{5110,434},{4450,886},{5070,1279}};

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
