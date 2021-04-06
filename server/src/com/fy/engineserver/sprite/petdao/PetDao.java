package com.fy.engineserver.sprite.petdao;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.article.data.props.RefreshMonsterPackage;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.FlopCaijiNpc;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;

/**
 * 宠物迷城
 * 一个玩家只能拥有一个迷城副本进度
 * 
 *
 */
public class PetDao implements Serializable{

	private int typelevel;
	
	private int enterlevel;
	
	public transient Game game;
	
	private MiChengSpeed mc;
	
	public int openingnum; //当前迷城正在开的箱子数量
	
	private ArrayList<RefreshMonsterPackage> overBox;
	private MonsterData overmonsters;
	
	private transient List<NPC> removeNps = new ArrayList<NPC>();
	
	private long clientreqtime = 20 * 1000L;
	
	public PetDao(MiChengSpeed mc,Game game){
		this.mc = mc;
		this.game = game;
	}
	
	public void heartbeat(){
		try{
			if(mc!=null){
				long now = System.currentTimeMillis();
				if(mc.STAT ==1 || mc.STAT ==2){
					//玩家断网回来
					if(mc.getP().isOnline() && PetDaoManager.getInstance().isPetDao(mc.getP().getCurrentGame().gi.name)){
						mc.STAT = 0;
					}
					return;
				}
				
				if(mc.getP()==null){
					try{
						Player p = PlayerManager.getInstance().getPlayer(mc.getPid());
						mc.setP(p);
					}catch(Exception e){
						PetDaoManager.log.warn("[宠物迷城心跳] [异常] [获取玩家] ");
					}
				}
				
				try{
					if(mc.STAT==0){
						//台湾服务器很卡，心跳已经开始了，但是客户端还没有请求enter_game，这样getcurrentgame()可能会有错,做个限制
						 if(!mc.getP().getCurrentGame().gi.name.equals(game.gi.name)){
							 	PetDaoManager.getInstance().savePetDao(mc.getP(), this);
							 	mc.setContinuetime(mc.getEndtime()-now);
							 	if(mc.getContinuetime()<1794000){
									mc.setSTAT(2);
							 	}
//								PetDaoManager.log.warn("[宠物迷城心跳] [结束] [原因："+(mc.getSTAT()==2?"玩家离开副本":"保存数据,即将结束")+"] [game1:"+mc.getP().getCurrentGame().gi.name+"] [game2:"+mc.getP().getCurrentGame().gi.name+"] [剩余时间："+mc.getContinuetime()+"] ["+mc.getP().getName()+"] [箱子剩余数量:"+mc.getBoxovernum()+"] [钥匙剩余数量:"+mc.getKeyovernum()+"] [已开始钥匙："+mc.getKeyrefreshnum()+"]");
								return;
						 }
					}
					
				}catch(Exception e){
					try{
					 	PetDaoManager.getInstance().savePetDao(mc.getP(), this);
					 	mc.setContinuetime(mc.getEndtime()-now);
					 	if(mc.getContinuetime()<1794000){
							mc.setSTAT(2);
					 	}
						PetDaoManager.log.warn("[宠物迷城心跳] [结束] [原因2："+(mc.getSTAT()==2?"玩家离开副本":"保存数据,即将结束")+"] [剩余时间："+mc.getContinuetime()+"] ["+mc.getP().getName()+"] [箱子剩余数量:"+mc.getBoxovernum()+"] [钥匙剩余数量:"+mc.getKeyovernum()+"] [已开始钥匙："+mc.getKeyrefreshnum()+"]");
					}catch(Exception e2){
						PetDaoManager.log.warn("[宠物迷城心跳] [异常] [原因3：玩家离开副本] ["+mc.getP().getName()+"]",e2);
					}
					return;
				}
				
				if(mc.STAT ==0 && !mc.getP().isOnline()){
					mc.setContinuetime(mc.getEndtime()-now);
					mc.setSTAT(2);
					PetDaoManager.getInstance().savePetDao(mc.getP(), this);
					PetDaoManager.log.warn("[宠物迷城心跳] [结束] [原因：玩家不在线] [剩余时间："+mc.getContinuetime()+"] ["+mc.getP().getName()+"] [箱子剩余数量:"+mc.getBoxovernum()+"] [钥匙剩余数量:"+mc.getKeyovernum()+"] [已开始钥匙："+mc.getKeyrefreshnum()+"]");
					return;
				}
				
				if(mc.getContinuetime()<=0){
					PetDaoManager.log.warn("[宠物迷城心跳] [结束] [原因：副本时间到] ["+mc.getP().getName()+"] [箱子剩余数量:"+mc.getBoxovernum()+"] [钥匙剩余数量:"+mc.getKeyovernum()+"] [已开始钥匙："+mc.getKeyrefreshnum()+"]");
					回城(mc.getP());
					mc.STAT = 1;
					return;
				}
				
				if(mc.getP().getConn()==null ||mc.getP().getConn().getState()==9 || mc.STAT ==0 && (now - mc.getP().getLastRequestTime() > clientreqtime)){
					mc.setContinuetime(mc.getEndtime()-now);
					mc.setSTAT(2);
					PetDaoManager.getInstance().savePetDao(mc.getP(), this);
					mc.getP().getConn().close();
					PetDaoManager.log.warn("[宠物迷城心跳] [结束] [原因：玩家断网] [剩余时间："+mc.getContinuetime()+"] ["+mc.getP().getName()+"] [箱子剩余数量:"+mc.getBoxovernum()+"] [钥匙剩余数量:"+mc.getKeyovernum()+"] [已开始钥匙："+mc.getKeyrefreshnum()+"]");
					return;
				}
				
				if(game!=null){
					game.heartbeat();
					mc.setContinuetime(mc.getEndtime()-now);
					try {
						LivingObject los[] = game.getLivingObjects();
						for (int j = 0; j < los.length; j++) {
							if(los[j] == null){
								continue;
							}
							if (los[j] instanceof FlopCaijiNpc) {
								FlopCaijiNpc n = (FlopCaijiNpc)los[j];
								if(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() > n.endTime){
									RefreshMonsterPackage pa = (RefreshMonsterPackage)n.getArticle();
									try{
										if(pa != null && pa.use(game, mc.getP(), n.getAe())){
											openingnum = 0;
											n.setAlive(false);
											n.ae = null;
											n.hasBeenPickUp = true;
											MemoryNPCManager.getNPCManager().removeNPC(n);
										}
									}catch(Exception e){
										e.printStackTrace();
										if (PetDaoManager.log.isWarnEnabled()) 
											PetDaoManager.log.warn("[出现异常2] [pa:{}] [{}]",new Object[]{pa,e});
									}
									
								}
							}
							if(los[j] instanceof Monster){
								Monster n= (Monster)los[j];
								if(n.isDeath() || !n.isAlive()){
									for(int i=0;i<overmonsters.getMonsterids().size();i++){
										if(overmonsters.getMonsterids().get(i).intValue()==n.getSpriteCategoryId()){
											overmonsters.getMonsterids().remove(i);
											overmonsters.getXys().remove(i);
										}
									}
//									PetDaoManager.log.warn("[n.isAlive():"+n.isAlive()+"] ["+n.getName()+"] [overmonsters.getMonsterids():"+overmonsters.getMonsterids().size()+"] [overmonsters.getXys():"+overmonsters.getXys().size()+"] [================] [4]");
								}
							}
						}
					} catch (Exception e) {
						if (PetDaoManager.log.isWarnEnabled()) PetDaoManager.log.warn("出现异常", e);
					}
					
//					System.out.println("[pnum:"+game.getNumOfPlayer()+"] [conn:"+(mc.getP().getConn()==null)+"] [connstat:"+(mc.getP().getConn()==null?"nul":mc.getP().getConn().getState())+"] [isOnline:"+(mc.getP().isOnline())+"]");
//					PetDaoManager.log.warn("[宠物迷城心跳] [上次请求时间："+mc.getP().getLastRequestTime()+"](0:正常-1:时间到-2:掉线):【"+mc.STAT+"】 [剩余时间："+mc.getContinuetime()+"] [迷城总数据:"+PetDaoManager.getInstance().datas.size()+"] [账号："+mc.getP().getUsername()+"] [角色名："+mc.getP().getName()+"] [箱子剩余数量:"+mc.getBoxovernum()+"] [钥匙剩余数量:"+mc.getKeyovernum()+"] [已开始钥匙："+mc.getKeyrefreshnum()+"]");
				}else{
					return;
				}
			}
		}catch(Exception e){
			try{
//				回城(mc.getP());
				PetDaoManager.log.warn("[宠物迷城心跳] [异常] [迷城总数据:"+PetDaoManager.getInstance().datas.size()+"] ["+mc.getP().getLogString()+"] "+e+"");
			}catch(Exception e2){
				PetDaoManager.log.warn("[宠物迷城心跳2] [异常]"+e2+"");
			}
		}
	}
	
	private void 回城(Player p){
		try{
//			Game defaultTransferGame = GameManager.getInstance().getGameByName("miemoshenyu", CountryManager.中立);
			game.transferGame(p, new TransportData(0, 0, 0, 0, p.getResurrectionMapName(), p.getResurrectionX(), p.getResurrectionY()));
//			MapArea area = game.gi.getMapAreaByName(Translate.出生点);
//			game.transferGame(p, new TransportData(0, 0, 0, 0, "miemoshenyu", defaultTransferGame.gi.name, (int) (area.getX() + Math.random() * area.getWidth()), (int) (area.getY() + Math.random() * area.getHeight())));
			PetDaoManager.log.warn("[宠物迷城心跳] [回城] [成功] ["+p.getLogString()+"]");
		}catch(Exception e){
			PetDaoManager.log.warn("[宠物迷城心跳] [回城] [异常]"+e);
		}
	}
	
	
	public int getTypelevel() {
		return typelevel;
	}

	public void setTypelevel(int typelevel) {
		this.typelevel = typelevel;
	}

	public int getEnterlevel() {
		return enterlevel;
	}

	public void setEnterlevel(int enterlevel) {
		this.enterlevel = enterlevel;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public MiChengSpeed getMc() {
		return mc;
	}

	public void setMc(MiChengSpeed mc) {
		this.mc = mc;
	}

	public ArrayList<RefreshMonsterPackage> getOverBox() {
		return overBox;
	}

	public void setOverBox(ArrayList<RefreshMonsterPackage> overBox) {
		this.overBox = overBox;
	}

	public MonsterData getOvermonsters() {
		return overmonsters;
	}

	public void setOvermonsters(MonsterData overmonsters) {
		this.overmonsters = overmonsters;
	}

	public List<NPC> getRemoveNps() {
		return removeNps;
	}

	public void setRemoveNps(List<NPC> removeNps) {
		this.removeNps = removeNps;
	}

	public int getOpeningnum() {
		return openingnum;
	}

	public void setOpeningnum(int openingnum) {
		this.openingnum = openingnum;
	}

}
