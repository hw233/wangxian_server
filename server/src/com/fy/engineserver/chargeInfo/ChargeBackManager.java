/**
 * 
 */
package com.fy.engineserver.chargeInfo;

import java.io.File;

import org.slf4j.Logger;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

/**
 * @author Administrator
 *
 */
public class ChargeBackManager {
	
	File timeOperationFile;
	
	File upgradeOperationFile;
	
	public DefaultDiskCache timeOperationCache;
	
	public DefaultDiskCache upgradeOperationCache;
	
	public static Logger log=Game.logger;
	
	static ChargeBackManager self;

	/**
	 * 
	 */
	public ChargeBackManager() {
		// TODO Auto-generated constructor stub
		
	}
	
	public ChargeBackManager(File timeOperationFile,File upgradeOperationFile){
		this.timeOperationCache=new DefaultDiskCache(timeOperationFile,null,
				"TimeOperation", 100L * 365 * 24 * 3600 * 1000L);
		this.upgradeOperationCache=new DefaultDiskCache(upgradeOperationFile,null,
				"UpgradeOperation", 100L * 365 * 24 * 3600 * 1000L);
	}
	
	public void init(){
		this.timeOperationCache=new DefaultDiskCache(this.timeOperationFile,null,
				"TimeOperation", 100L * 365 * 24 * 3600 * 1000L);
		this.upgradeOperationCache=new DefaultDiskCache(this.upgradeOperationFile,null,
				"UpgradeOperation", 100L * 365 * 24 * 3600 * 1000L);
		ChargeBackManager.self=this;
	}
	
	public boolean isExistTimeOperation(int playerId){
		ChargeBackTimeOperation cbto=(ChargeBackTimeOperation)this.timeOperationCache.get(playerId);
		return cbto!=null?true:false;
	}
	
	public boolean isExistUpgradeOperation(long playerId){
		ChargeBackUpgradeOperation cbuo=(ChargeBackUpgradeOperation)this.upgradeOperationCache.get(playerId);
		return cbuo!=null?true:false;
	}
	
	public void addTimeOperation(int playerId,int amount){
		ChargeBackTimeOperation cbto=(ChargeBackTimeOperation)this.timeOperationCache.get(playerId);
		if(cbto==null){
			this.timeOperationCache.put(playerId, new ChargeBackTimeOperation(playerId,amount));
		}else{
			if(log.isInfoEnabled()){
//				log.info("[充值返还] [时间规则] [添加失败] [已经存在] [玩家ID："+playerId+"]");
				if(log.isInfoEnabled())
					log.info("[充值返还] [时间规则] [添加失败] [已经存在] [玩家ID：{}]", new Object[]{playerId});
			}
		}
	}
	
	public void doTimeOperation(long playerId){
		ChargeBackTimeOperation cbto=(ChargeBackTimeOperation)this.timeOperationCache.get(playerId);
		if(cbto==null){
			cbto=(ChargeBackTimeOperation)this.timeOperationCache.get((int)playerId);
		}
		if(cbto!=null){
			if(cbto.isDisabled()){
				this.timeOperationCache.remove(playerId);
				Player player=null;
				String playerName="";
				try {
					player=PlayerManager.getInstance().getPlayer(playerId);
					playerName=player.getName();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(log.isInfoEnabled()){
//					log.info("[充值返还] [时间规则] [失效移除] [玩家："+playerName+"] [玩家ID："+playerId+"]");
					if(log.isInfoEnabled())
						log.info("[充值返还] [时间规则] [失效移除] [玩家：{}] [玩家ID：{}]", new Object[]{playerName,playerId});
				}
			}else{
				cbto.doOperation();
				this.timeOperationCache.put(playerId, cbto);
			}
		}
	}
	
	public void addUpgradeOperation(long playerId,int amount,int levels){
		try{
			Player player=PlayerManager.getInstance().getPlayer(playerId);
			if (ChargeBackManager.log.isInfoEnabled()) {
//ChargeBackManager.log.info("[充值返还] [添加升级规则] [玩家："
//+ player.getName() + "] [玩家ID："
//+ playerId + "] [玩家等级："+player.getLevel()+"] [金额：" + amount
//+ "] [次数："+levels+"]");
if(ChargeBackManager.log.isInfoEnabled())
	ChargeBackManager.log.info("[充值返还] [添加升级规则] [玩家：{}] [玩家ID：{}] [玩家等级：{}] [金额：{}] [次数：{}]", new Object[]{player.getName(),playerId,player.getLevel(),amount,levels});
			}
			ChargeBackUpgradeOperation cbuo=new ChargeBackUpgradeOperation(playerId,amount,levels);
			cbuo.doOperation();
			this.upgradeOperationCache.put(playerId, cbuo);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void putUpgradeOperation(long playerId,ChargeBackUpgradeOperation cbuo){
		this.upgradeOperationCache.put(playerId, cbuo);
	}
	
	public ChargeBackUpgradeOperation getUpgradeOperation(int playerId){
		return (ChargeBackUpgradeOperation)this.upgradeOperationCache.get(playerId);
	}
	
	public void doUpgradeOperation(long playerId){
		ChargeBackUpgradeOperation cbuo=(ChargeBackUpgradeOperation)this.upgradeOperationCache.get(playerId);
		if(cbuo==null){
			cbuo=(ChargeBackUpgradeOperation)this.upgradeOperationCache.get((int)playerId);
		}
		if(cbuo!=null){
			if(cbuo.isDisabled()){
				this.upgradeOperationCache.remove(playerId);
				Player player=null;
				String playerName="";
				try {
					player=PlayerManager.getInstance().getPlayer(playerId);
					playerName=player.getName();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(log.isInfoEnabled()){
//					log.info("[充值返还] [升级规则] [失效移除] [玩家："+playerName+"] [玩家ID："+playerId+"]");
					if(log.isInfoEnabled())
						log.info("[充值返还] [升级规则] [失效移除] [玩家：{}] [玩家ID：{}]", new Object[]{playerName,playerId});
				}
			}else{
				cbuo.doOperation();
				this.upgradeOperationCache.put(playerId,cbuo);
			}
		}
	}

	public File getTimeOperationFile() {
		return timeOperationFile;
	}

	public void setTimeOperationFile(File timeOperationFile) {
		this.timeOperationFile = timeOperationFile;
	}

	public File getUpgradeOperationFile() {
		return upgradeOperationFile;
	}

	public void setUpgradeOperationFile(File upgradeOperationFile) {
		this.upgradeOperationFile = upgradeOperationFile;
	}
	
	public static ChargeBackManager getInstance(){
		return ChargeBackManager.self;
	}
	
}
