package com.fy.engineserver.operating.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class JinLingJiangGiftManager {
	DefaultDiskCache ddc;
	
	Set<String> shuangBeiJingYanSet = new HashSet<String>();
	Set<String> qianLiZhuiFengSet = new HashSet<String>();
	Set<String> huiHunZhuSet = new HashSet<String>();
	
	File dataFile;
	
	File shuangBeiJingYanSerialFile;
	File qianLiZhuiFengFile;
	File huiHunZhuFile;
	
	static JinLingJiangGiftManager instance; 
	
	private final static String PLAYER_PREFIX = Translate.text_2317;
	
	public static JinLingJiangGiftManager getInstance(){
		return instance;
	}
	
	public void init() throws Exception{
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		
		instance = this;
		ddc = new DefaultDiskCache(dataFile, null,
				Translate.text_5536, 100L * 365 * 24 * 3600 * 1000L);
		
		if(shuangBeiJingYanSerialFile == null || !shuangBeiJingYanSerialFile.exists() || !shuangBeiJingYanSerialFile.isFile()){
			System.out.println("[系统初始化] [17sy金陵奖记录] [初始化失败] ["+getClass().getName()+"] [请检查记录双倍经验序列号的文件配置]");
		}else{
			shuangBeiJingYanSet = loadSerials(shuangBeiJingYanSerialFile);
		}
		
		if(qianLiZhuiFengFile == null || !qianLiZhuiFengFile.exists() || !qianLiZhuiFengFile.isFile()){
			System.out.println("[系统初始化] [17sy金陵奖记录] [初始化失败] ["+getClass().getName()+"] [请检查记录千里追风符序列号的文件配置]");
		}else{
			qianLiZhuiFengSet = loadSerials(qianLiZhuiFengFile);
		}
		
		if(huiHunZhuFile == null || !huiHunZhuFile.exists() || !huiHunZhuFile.isFile()){
			System.out.println("[系统初始化] [17sy金陵奖记录] [初始化失败] ["+getClass().getName()+"] [请检查记录回魂珠序列号的文件配置]");
		}else{
			huiHunZhuSet = loadSerials(huiHunZhuFile);
		}

		System.out.println("[系统初始化] [17sy金陵奖记录] [初始化完成] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
	}
	
	public DefaultDiskCache getDdc() {
		return ddc;
	}

	public void setDdc(DefaultDiskCache ddc) {
		this.ddc = ddc;
	}

	public Set<String> getShuangBeiJingYanSet() {
		return shuangBeiJingYanSet;
	}

	public void setShuangBeiJingYanSet(Set<String> shuangBeiJingYanSet) {
		this.shuangBeiJingYanSet = shuangBeiJingYanSet;
	}

	public Set<String> getQianLiZhuiFengSet() {
		return qianLiZhuiFengSet;
	}

	public void setQianLiZhuiFengSet(Set<String> qianLiZhuiFengSet) {
		this.qianLiZhuiFengSet = qianLiZhuiFengSet;
	}

	public Set<String> getHuiHunZhuSet() {
		return huiHunZhuSet;
	}

	public void setHuiHunZhuSet(Set<String> huiHunZhuSet) {
		this.huiHunZhuSet = huiHunZhuSet;
	}

	public File getDataFile() {
		return dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

	public File getShuangBeiJingYanSerialFile() {
		return shuangBeiJingYanSerialFile;
	}

	public void setShuangBeiJingYanSerialFile(File shuangBeiJingYanSerialFile) {
		this.shuangBeiJingYanSerialFile = shuangBeiJingYanSerialFile;
	}

	public File getQianLiZhuiFengFile() {
		return qianLiZhuiFengFile;
	}

	public void setQianLiZhuiFengFile(File qianLiZhuiFengFile) {
		this.qianLiZhuiFengFile = qianLiZhuiFengFile;
	}

	public File getHuiHunZhuFile() {
		return huiHunZhuFile;
	}

	public void setHuiHunZhuFile(File huiHunZhuFile) {
		this.huiHunZhuFile = huiHunZhuFile;
	}

	private Set<String> loadSerials(File file) throws Exception{
		Set<String> set = new HashSet<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			
			String line = null;
			while((line = br.readLine()) != null){
				line = line.trim();
				if(!line.equals("")){
					set.add(line);					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			if(br != null){
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return set;
	}
	
	public boolean isPlayerUsedSerial(Player p){
		return ddc.get(PLAYER_PREFIX + p.getId()) != null;
	}
	
	public String getGift(Player player , String serial) throws Exception{
		if(!isSerialValid(serial)){
//			Game.logger.warn("[领取金翎奖] [失败] [序列号非法] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+serial+"]");
			if(Game.logger.isWarnEnabled())
				Game.logger.warn("[领取金翎奖] [失败] [序列号非法] [{}] [{}] [{}] [{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),serial});
			return Translate.text_5533;
		}
		
		if(isSerialUsed(serial)){
//			Game.logger.warn("[领取金翎奖] [失败] [序列号已经使用] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+serial+"]");
			if(Game.logger.isWarnEnabled())
				Game.logger.warn("[领取金翎奖] [失败] [序列号已经使用] [{}] [{}] [{}] [{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),serial});
			return Translate.text_5537;
		}
		
		if(isPlayerUsedSerial(player)){
//			Game.logger.warn("[领取金翎奖] [失败] [您已经使用过序列号，本次活动不能再使用] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+serial+"]");
			if(Game.logger.isWarnEnabled())
				Game.logger.warn("[领取金翎奖] [失败] [您已经使用过序列号，本次活动不能再使用] [{}] [{}] [{}] [{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),serial});
			return Translate.text_5538;
		}
		
		String key = getKey(serial);
		
		if(key == null){
//			Game.logger.warn("[领取金翎奖] [失败] [序列号非法] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+serial+"]");
			if(Game.logger.isWarnEnabled())
				Game.logger.warn("[领取金翎奖] [失败] [序列号非法] [{}] [{}] [{}] [{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),serial});
			throw new Exception("序列号非法！！！");
		}
		
		ddc.put(key, player.getUsername()+"#"+player.getId()+"#");
		ddc.put(PLAYER_PREFIX + player.getId(), serial);
//		Game.logger.warn("[领取金翎奖] [成功] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+serial+"]");
		if(Game.logger.isWarnEnabled())
			Game.logger.warn("[领取金翎奖] [成功] [{}] [{}] [{}] [{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),serial});
		return null;
	}
	
	private String getKey(String serial){
		String key = null;
		if(shuangBeiJingYanSet.contains(serial)){
			key="[sbjy]";
		}else if(qianLiZhuiFengSet.contains(serial)){
			key="[qlzf]";
		}else if(huiHunZhuSet.contains(serial)){
			key="[hhz]";
		}
		
		return key + serial;
	}
	
	public String getArticleNameForSerial(String serial){
		String key = null;
		if(shuangBeiJingYanSet.contains(serial)){
			key=Translate.text_5539;
		}else if(qianLiZhuiFengSet.contains(serial)){
			key=Translate.text_3427;
		}else if(huiHunZhuSet.contains(serial)){
			key=Translate.text_2644;
		}
		
		return key;
	}
	
	public boolean isSerialValid(String serial){
		return shuangBeiJingYanSet.contains(serial) || qianLiZhuiFengSet.contains(serial) || huiHunZhuSet.contains(serial);
	}
	
	public boolean isSerialUsed(String serial){
		return ddc.get(getKey(serial)) != null;
	}
	
	public String[] getSerialsForShuangBeiJingYan(){
		return shuangBeiJingYanSet.toArray(new String[0]);
	}
	
	public String[] getSerialsForQianLiZhuiFeng(){
		return qianLiZhuiFengSet.toArray(new String[0]);
	}
	
	public String[] getSerialsForHuiHunZhuSet(){
		return huiHunZhuSet.toArray(new String[0]);
	}
	
}
