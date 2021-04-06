package com.fy.engineserver.operating.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.TransferLanguage;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class XinShouCardManager {
DefaultDiskCache ddc;
	
	File dataFile;
	
	
	static XinShouCardManager instance;
	
	Map<String,String> serialToArticleMap = new HashMap<String, String>();
	private final static String PLAYER_PREFIX = Translate.text_2317;
	public static XinShouCardManager getInstance(){
		return instance;
	}
	
	File f0;
	
	public File getF0() {
		return f0;
	}

	public void setF0(File f0) {
		this.f0 = f0;
	}

	public void init() throws Exception{
		instance = this;
		
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		
		ddc = new DefaultDiskCache(dataFile, null,
				Translate.text_5532, 100L * 365 * 24 * 3600 * 1000L);
		
		File[] fs = new File[]{f0};
		for(File f:fs){
			try {
				Map<String,Set<String>> m = loadSerial(f);
				for(String key : m.keySet()){
					Set<String> set = m.get(key);
					for(String s : set){
						if(serialToArticleMap.containsKey(s)){
							throw new Exception("序列号重复，请检查序列号配置文件");
						}
						
						serialToArticleMap.put(s, key);
					}
				}
			} catch (Exception e) {
				System.out.println("[系统初始化] [帮会活动新手卡管理者] [载入礼品序列号文件出错] ["+f.getName()+"] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
				e.printStackTrace();
			}
		}
		
		System.out.println("[系统初始化] [帮会活动新手卡管理者] [初始化完成] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
	}
	
	public Map<String,Set<String>> loadSerial(File f) throws Exception{
		Map<String,Set<String>> map = new HashMap<String, Set<String>>();
		
		BufferedReader br = null;
		String line = null;
		
		try {
			br = new BufferedReader(new FileReader(f));
			String articleName = TransferLanguage.transferString(br.readLine());
			
			Set<String> set = new LinkedHashSet<String>();
			map.put(articleName, set);
			
			while((line = br.readLine()) != null){
				set.add(line.trim());
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
		
		return map;
	}
	
	public boolean isValid(String serial){
		return serialToArticleMap.containsKey(serial);
	}
	
	public String getArticleName(String serial){
		return serialToArticleMap.get(serial);
	}
	
	public boolean isPlayerUsedSerial(Player p){
		return ddc.get(PLAYER_PREFIX + p.getId()) != null;
	}
	
	public synchronized String getGift(Player p , String serial) throws Exception{
		if(GameConstants.getInstance().getServerName().equals(Translate.text_5316)||GameConstants.getInstance().getServerName().equals(Translate.text_5317)||GameConstants.getInstance().getServerName().equals(Translate.text_5318)||GameConstants.getInstance().getServerName().equals(Translate.text_5319)||GameConstants.getInstance().getServerName().equals(Translate.text_5320)){
			return Translate.text_5321;
		}
		if(!isValid(serial)){
			return Translate.text_5533;
		}
		
		String articleName = getArticleName(serial);
		if(articleName == null){
//			Game.logger.warn("[领取帮会活动新手礼品] [物品不存在] ["+p.getUsername()+"] ["+p.getName()+"] ["+p.getId()+"] ["+serial+"]");
			if(Game.logger.isWarnEnabled())
				Game.logger.warn("[领取帮会活动新手礼品] [物品不存在] [{}] [{}] [{}] [{}]", new Object[]{p.getUsername(),p.getName(),p.getId(),serial});
			return Translate.text_5534;
		}
		
		if(isSerialUsed(serial)){
//			Game.logger.warn("[领取帮会活动新手礼品] [序列号已被使用] ["+p.getUsername()+"] ["+p.getName()+"] ["+p.getId()+"] ["+serial+"]");
			if(Game.logger.isWarnEnabled())
				Game.logger.warn("[领取帮会活动新手礼品] [序列号已被使用] [{}] [{}] [{}] [{}]", new Object[]{p.getUsername(),p.getName(),p.getId(),serial});
			return Translate.text_5535;
		}
		
		if(isPlayerUsedSerial(p)){
//			Game.logger.warn("[领取帮会活动新手礼品] [您已经使用过序列号，本次活动不能再使用] ["+p.getUsername()+"] ["+p.getName()+"] ["+p.getId()+"] ["+serial+"]");
			if(Game.logger.isWarnEnabled())
				Game.logger.warn("[领取帮会活动新手礼品] [您已经使用过序列号，本次活动不能再使用] [{}] [{}] [{}] [{}]", new Object[]{p.getUsername(),p.getName(),p.getId(),serial});
			return Translate.text_5538;
		}
		
		ddc.put(serial, p.getUsername() + "#" + p.getId());
		ddc.put(PLAYER_PREFIX + p.getId() , serial);
		
//		Game.logger.warn("[领取帮会活动新手礼品] [成功] ["+p.getUsername()+"] ["+p.getName()+"] ["+p.getId()+"] ["+serial+"]");
		if(Game.logger.isWarnEnabled())
			Game.logger.warn("[领取帮会活动新手礼品] [成功] [{}] [{}] [{}] [{}]", new Object[]{p.getUsername(),p.getName(),p.getId(),serial});
		
		return null;
	}
	
	public boolean isSerialUsed(String serial){
		return ddc.get(serial) != null;
	}
	
	public DefaultDiskCache getDdc() {
		return ddc;
	}



	public void setDdc(DefaultDiskCache ddc) {
		this.ddc = ddc;
	}



	public File getDataFile() {
		return dataFile;
	}



	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}
	
}
