package com.fy.engineserver.compensation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.stat.model.CurrencyType;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class CompensationManager {
	DefaultDiskCache ddc;
	File dataFile;

	File articleCompensationFile;
	File playerConsumeRecordFile;
	
	private final static String KEY_GONG_CE_LIBAO_40 = "gclb40";
	public final static String COMPENSATE_GONG_CE_LIBAO_ARTICLE_NAME = Translate.text_2631;
	
	File gongCeLiBao40ConsumeRecordFile;
	
	static class ConsumeRecord{
		long playerId;
		String articleName;
		int amount;
		
		
		public long getPlayerId() {
			return playerId;
		}
		public void setPlayerId(long playerId) {
			this.playerId = playerId;
		}
		public String getArticleName() {
			return articleName;
		}
		public void setArticleName(String articleName) {
			this.articleName = articleName;
		}
		public int getAmount() {
			return amount;
		}
		public void setAmount(int amount) {
			this.amount = amount;
		}
	}
	
	static class CompensationRecord implements Serializable{
		private static final long serialVersionUID = -1647400565088709903L;
		
		long playerId;
		int yuanBaoCount;
		public long getPlayerId() {
			return playerId;
		}
		public void setPlayerId(long playerId) {
			this.playerId = playerId;
		}
		public int getYuanBaoCount() {
			return yuanBaoCount;
		}
		public void setYuanBaoCount(int yuanBaoCount) {
			this.yuanBaoCount = yuanBaoCount;
		}
	}
	
	Map<String,Integer> articleCompensationMap = new HashMap<String, Integer>();
	
	Map<Long , List<ConsumeRecord>> consumeRecordMap = new HashMap<Long, List<ConsumeRecord>>();
	
	Set<Long> consumeGongCeLiBao40Set = new HashSet<Long>();
	
	public File getArticleCompensationFile() {
		return articleCompensationFile;
	}

	public void setArticleCompensationFile(File articleCompensationFile) {
		this.articleCompensationFile = articleCompensationFile;
	}

	public File getPlayerConsumeRecordFile() {
		return playerConsumeRecordFile;
	}

	public void setPlayerConsumeRecordFile(File playerConsumeRecordFile) {
		this.playerConsumeRecordFile = playerConsumeRecordFile;
	}

	public File getDataFile() {
		return dataFile;
	}
	
	public File getGongCeLiBao40ConsumeRecordFile() {
		return gongCeLiBao40ConsumeRecordFile;
	}

	public void setGongCeLiBao40ConsumeRecordFile(
			File gongCeLiBao40ConsumeRecordFile) {
		this.gongCeLiBao40ConsumeRecordFile = gongCeLiBao40ConsumeRecordFile;
	}

	public int[] getPlayerIdsWithConsumeRecord(){
		Integer[] ivs = consumeRecordMap.keySet().toArray(new Integer[0]);
		int[] ids = new int[ivs.length];
		
		for(int i = 0 ;i < ivs.length ; i++){
			ids[i] = ivs[i];
		}
		
		return ids;
	}
	
	static CompensationManager instance;

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

	public final static CompensationManager getInstance(){
		return instance;
	}

	public void init(){
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		
		ddc = new DefaultDiskCache(dataFile, null,
				Translate.text_2632, 100L * 365 * 24 * 3600 * 1000L);
		
		
		if(articleCompensationFile == null || !articleCompensationFile.exists()){
			System.out.println("[系统初始化] [补偿玩家管理者] [失败] [没有设置物品补偿价格文件] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
		}else{
			try {
				loadArticleCompensationFile(articleCompensationFile);
			} catch (Exception e) {
				System.out.println("[系统初始化] [补偿玩家管理者] [失败] [解析物品补偿价格文件出错] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
				e.printStackTrace(System.out);
			}
		}
		
		if(playerConsumeRecordFile == null || !playerConsumeRecordFile.exists()){
			System.out.println("[系统初始化] [补偿玩家管理者] [失败] [没有设置玩家消费记录文件] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
		}else{
			try {
				loadPlayerConsumeRecordFile(playerConsumeRecordFile);
			} catch (Exception e) {
				System.out.println("[系统初始化] [补偿玩家管理者] [失败] [解析玩家消费记录文件出错] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
				e.printStackTrace(System.out);
			}
		}
		
		if(gongCeLiBao40ConsumeRecordFile == null || !gongCeLiBao40ConsumeRecordFile.exists()){
			System.out.println("[系统初始化] [补偿玩家管理者] [失败] [没有设置玩家消费40级公测礼包记录文件] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
		}else{
			try {
				loadGongCeLiBao40File(gongCeLiBao40ConsumeRecordFile);
			} catch (Exception e) {
				System.out.println("[系统初始化] [补偿玩家管理者] [失败] [解析玩家消费40级公测礼包记录文件出错] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
				e.printStackTrace(System.out);
			}
		}
		
		System.out.println("[系统初始化] [补偿玩家管理者] [初始化完成] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
		
		instance = this;
	}
	
	public void reload() throws Exception{
		loadArticleCompensationFile(articleCompensationFile);
		loadPlayerConsumeRecordFile(playerConsumeRecordFile);
		loadGongCeLiBao40File(gongCeLiBao40ConsumeRecordFile);
	}
	
	/**
	 * 文件格式如下
	 * 物品名称\t元宝个数
	 * 
	 * @param file
	 * @throws Exception
	 */
	private void loadArticleCompensationFile(File file) throws Exception{
		articleCompensationMap.clear();
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(file));
			
			String line = null;
			while((line = br.readLine()) != null){
				line = line.trim();
				String[] ss = line.split(",");
				
				if(ss.length != 2){
					throw new Exception("数据格式错误:"+line);
				}
				
				String name = ss[0];
				int price = 0;
				try {
					price = Integer.parseInt(ss[1]);
				} catch (Exception e) {
					throw new Exception("解析补偿物品价格错误:"+line+" " + ss[1]);
				}
				
				articleCompensationMap.put(name, price);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if(br != null){
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void loadGongCeLiBao40File(File file) throws Exception{
		consumeGongCeLiBao40Set.clear();
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(file));
			
			String line = null;
			while((line = br.readLine()) != null){
				line = line.trim();
				try {
					long playerId = Integer.parseInt(line);
					consumeGongCeLiBao40Set.add(playerId);
				} catch (Exception e) {
					throw new Exception("解析补偿玩家使用40级公测礼包出错，玩家id非法["+line+"]！！！");
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if(br != null){
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void loadPlayerConsumeRecordFile(File file) throws Exception{
		consumeRecordMap.clear();
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(file));
			
			String line = null;
			while((line = br.readLine()) != null){
				line = line.trim();
				String[] ss = line.split(",");
				
				if(ss.length != 3){
					throw new Exception("数据格式错误:"+line);
				}
				
				int amount = 0;
				try {
					amount = Integer.parseInt(ss[0]);
				} catch (Exception e) {
					throw new Exception("解析消费记录错误:"+line+" " + ss[1]);
				}
				
				String articleName = ss[1];
				
				long playerId = 0;
				try {
					playerId = Integer.parseInt(ss[2]);
				} catch (Exception e) {
					throw new Exception("解析消费记录错误:"+line+" " + ss[1]);
				}
				
				List<ConsumeRecord> cl = consumeRecordMap.get(playerId);
				if(cl == null){
					cl = new ArrayList<ConsumeRecord>();
					consumeRecordMap.put(playerId, cl);
				}
				
				ConsumeRecord cr = new ConsumeRecord();
				cr.setPlayerId(playerId);
				cr.setAmount(amount);
				cr.setArticleName(articleName);
				
				
				cl.add(cr);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if(br != null){
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean hasConsumeRecord(long playerId){
		List<ConsumeRecord> cl = consumeRecordMap.get(playerId);
		return  cl != null && cl.size() > 0; 
	}
	
	public boolean hasConsumedGongCeLiBao40(long playerId){
		return consumeGongCeLiBao40Set.contains(playerId);
	}
	
	public boolean isCompensatedGongCeLiBao40(long playerId){
		return ddc.get(KEY_GONG_CE_LIBAO_40 + playerId) != null;
	}
	
	public void compensateGongCeLiBao40(Player player) throws Exception{}
	
	public int getCompensation(long playerId){
		List<ConsumeRecord> cl = consumeRecordMap.get(playerId);
		
		if(cl == null) return 0;
		
		int yuanBaoCount = 0;
		
		for(ConsumeRecord cr : cl){
			String name = cr.getArticleName();
			int amount = cr.getAmount();
			
			int price = 0;
			if(articleCompensationMap.containsKey(name))
				price = articleCompensationMap.get(name);
			
			yuanBaoCount += (amount * price);
		}
		
		return yuanBaoCount;
	}
	
	public String[] getConsumeRecords(long playerId){
		List<ConsumeRecord> cl = consumeRecordMap.get(playerId);
		
		if(cl == null) return new String[0];
		ArrayList<String> al = new ArrayList<String>();
		for(ConsumeRecord cr : cl){
			String name = cr.getArticleName();
			int amount = cr.getAmount();
			int price = 0;
			if(articleCompensationMap.containsKey(name))
				price = articleCompensationMap.get(name);
			String s = "[color="+0xC731FF+"]" + name+Translate.text_2633+amount+Translate.text_2634+(amount * price)+Translate.text_592;
			al.add(s);
		}
		return al.toArray(new String[0]);
	}
	
	public int compensate(Player player) throws Exception{
		
		synchronized(player){
			if(isPlayerCompensated(player.getId())){
				return 0;
			}
			
			if(!hasConsumeRecord(player.getId())){
				return 0;
			}
			
			int yuanBaoCount = getCompensation(player.getId());
			
			if(yuanBaoCount < 0){
				return 0;
			}
			
			BillingCenter bc = BillingCenter.getInstance();
			bc.playerSaving(player, yuanBaoCount , CurrencyType.RMB_YUANBAO, SavingReasonType.COMPENSATE_PLAYER, "");
			
			CompensationRecord cr = new CompensationRecord();
			cr.setPlayerId(player.getId());
			cr.setYuanBaoCount(yuanBaoCount);
			
			ddc.put(player.getId(), cr);
			
//			Game.logger.warn("[领取降价补偿] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] [金额："+yuanBaoCount+"]");
			if(Game.logger.isWarnEnabled())
				Game.logger.warn("[领取降价补偿] [{}] [{}] [{}] [金额：{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),yuanBaoCount});
			
			return yuanBaoCount;
		}
	}
	
	public boolean isPlayerCompensated(long playerId){
		return ddc.get(playerId) != null;
	}
}
