/**
 * 
 */
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
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

/**
 * @author Administrator
 *
 */
public class UniqueGiftManager {
	
	static UniqueGiftManager self;

	DefaultDiskCache ddc;
	
	File dataFile;
	
	File f0;
	File f1;
	File f2;
	File f3;
	File f4;
	File f5;
	File f6;
	File f7;
	File f8;
	File f9;
	File f10;
	File f11;
	File f12;
	File f13;
	File f14;
	File f15;
	
	Map<String,String> serialToArticleMap = new HashMap<String, String>();
	
	private final static String PLAYER_PREFIX = Translate.text_2317;
	/**
	 * 
	 */
	public UniqueGiftManager() {
		// TODO Auto-generated constructor stub
	}
	
	public void init(){
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ddc = new DefaultDiskCache(dataFile, null,
				Translate.text_5541, 100L * 365 * 24 * 3600 * 1000L);
		
		File[] fs = new File[]{f0,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15};
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
				System.out.println("[系统初始化] [角色唯一礼品管理器] [载入礼品序列号文件出错] ["+f.getName()+"] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
				e.printStackTrace();
			}
		}
		
		System.out.println("[系统初始化] [角色唯一礼品管理器] [初始化完成] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
		UniqueGiftManager.self=this;
	}
	
	public static UniqueGiftManager getInstance(){
		return UniqueGiftManager.self;
	}
	public File getDataFile() {
		return dataFile;
	}
	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}
	public File getF0() {
		return f0;
	}
	public void setF0(File f0) {
		this.f0 = f0;
	}
	public File getF1() {
		return f1;
	}
	public void setF1(File f1) {
		this.f1 = f1;
	}
	public File getF2() {
		return f2;
	}
	public void setF2(File f2) {
		this.f2 = f2;
	}
	public File getF3() {
		return f3;
	}
	public void setF3(File f3) {
		this.f3 = f3;
	}
	public File getF4() {
		return f4;
	}
	public void setF4(File f4) {
		this.f4 = f4;
	}
	public File getF5() {
		return f5;
	}
	public void setF5(File f5) {
		this.f5 = f5;
	}
	public File getF6() {
		return f6;
	}
	public void setF6(File f6) {
		this.f6 = f6;
	}
	public File getF7() {
		return f7;
	}
	public void setF7(File f7) {
		this.f7 = f7;
	}
	public File getF8() {
		return f8;
	}
	public void setF8(File f8) {
		this.f8 = f8;
	}
	public File getF9() {
		return f9;
	}
	public void setF9(File f9) {
		this.f9 = f9;
	}
	public File getF10() {
		return f10;
	}
	public void setF10(File f10) {
		this.f10 = f10;
	}
	public File getF11() {
		return f11;
	}
	public void setF11(File f11) {
		this.f11 = f11;
	}
	public File getF12() {
		return f12;
	}
	public void setF12(File f12) {
		this.f12 = f12;
	}
	public File getF13() {
		return f13;
	}
	public void setF13(File f13) {
		this.f13 = f13;
	}
	public File getF14() {
		return f14;
	}
	public void setF14(File f14) {
		this.f14 = f14;
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
		if(!isValid(serial)){
			return Translate.text_5533;
		}
		
		String articleName = getArticleName(serial);
		if(articleName == null){
//			Game.logger.warn("[领取角色唯一礼品] [物品不存在] ["+p.getUsername()+"] ["+p.getName()+"] ["+p.getId()+"] ["+serial+"]");
			if(Game.logger.isWarnEnabled())
				Game.logger.warn("[领取角色唯一礼品] [物品不存在] [{}] [{}] [{}] [{}]", new Object[]{p.getUsername(),p.getName(),p.getId(),serial});
			return Translate.text_5534;
		}
		
		if(isSerialUsed(serial)){
//			Game.logger.warn("[领取角色唯一礼品] [序列号已被使用] ["+p.getUsername()+"] ["+p.getName()+"] ["+p.getId()+"] ["+serial+"]");
			if(Game.logger.isWarnEnabled())
				Game.logger.warn("[领取角色唯一礼品] [序列号已被使用] [{}] [{}] [{}] [{}]", new Object[]{p.getUsername(),p.getName(),p.getId(),serial});
			return Translate.text_5542;
		}
		
		if(isPlayerUsedSerial(p)){
//			Game.logger.warn("[领取角色唯一礼品] [您已经使用过序列号，本次活动不能再使用] ["+p.getUsername()+"] ["+p.getName()+"] ["+p.getId()+"] ["+serial+"]");
			if(Game.logger.isWarnEnabled())
				Game.logger.warn("[领取角色唯一礼品] [您已经使用过序列号，本次活动不能再使用] [{}] [{}] [{}] [{}]", new Object[]{p.getUsername(),p.getName(),p.getId(),serial});
			return Translate.text_5543;
		}
		
		ddc.put(serial, p.getUsername() + "#" + p.getId());
		ddc.put(PLAYER_PREFIX + p.getId() , serial);
		
//		Game.logger.warn("[领取角色唯一礼品] [成功] ["+p.getUsername()+"] ["+p.getName()+"] ["+p.getId()+"] ["+serial+"] ["+articleName+"]");
		if(Game.logger.isWarnEnabled())
			Game.logger.warn("[领取角色唯一礼品] [成功] [{}] [{}] [{}] [{}] [{}]", new Object[]{p.getUsername(),p.getName(),p.getId(),serial,articleName});
		
		return null;
	}
	
	public boolean isSerialUsed(String serial){
		return ddc.get(serial) != null;
	}
	
	public String[] getSerials(){
		return serialToArticleMap.keySet().toArray(new String[0]);
	}

	public File getF15() {
		return f15;
	}

	public void setF15(File f15) {
		this.f15 = f15;
	}

}
