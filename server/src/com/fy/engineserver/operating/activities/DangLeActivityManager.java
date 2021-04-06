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

public class DangLeActivityManager {
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
	File f16;
	File f17;
	File f18;
	File f19;
	File f20;
	File f21;
	File f22;
	File f23;
	File f24;
	File f25;
	File f26;
	File f27;
	File f28;
	File f29;
	File f30;
	File f31;
	File f32;
	File f33;
	File f34;
	File f35;
	File f36;
	File f37;
	File f38;
	File f39;
	File f40;
	File f41;
	File f42;
	File f43;
	File f44;
	File f45;
	File f46;
	File f47;
	File f48;
	File f49;
	File f50;
	File f51;
	File f52;
	File f53;
	File f54;
	File f55;
	File f56;
	
	static DangLeActivityManager instance;
	
	Map<String,String> serialToArticleMap = new HashMap<String, String>();
	private final static String PLAYER_PREFIX = Translate.text_2317;
	public static DangLeActivityManager getInstance(){
		return instance;
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

	public void init() throws Exception{
		instance = this;
		
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ddc = new DefaultDiskCache(dataFile, null,
				Translate.text_5532, 100L * 365 * 24 * 3600 * 1000L);
		
		File[] fs = new File[]{f0,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18,f19,f20,
				f21,f22,f23,f24,f25,f26,f27,f28,f29,f30,f31,f32,f33,f34,f35,f36,f37,f38,f39,f40,f41,f42,f43
				,f44,f45,f46,f47,f48,f49,f50,f51,f52,f53,f54,f55,f56};
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
				System.out.println("[系统初始化] [当乐活动序列号管理者] [载入礼品序列号文件出错] ["+f.getName()+"] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
				e.printStackTrace();
			}
		}
		
		System.out.println("[系统初始化] [当乐活动序列号管理者] [初始化完成] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
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
//			Game.logger.warn("[领取当乐礼品] [物品不存在] ["+p.getUsername()+"] ["+p.getName()+"] ["+p.getId()+"] ["+serial+"]");
			if(Game.logger.isWarnEnabled())
				Game.logger.warn("[领取当乐礼品] [物品不存在] [{}] [{}] [{}] [{}]", new Object[]{p.getUsername(),p.getName(),p.getId(),serial});
			return Translate.text_5534;
		}
		
		if(isSerialUsed(serial)){
//			Game.logger.warn("[领取当乐礼品] [序列号已被使用] ["+p.getUsername()+"] ["+p.getName()+"] ["+p.getId()+"] ["+serial+"]");
			if(Game.logger.isWarnEnabled())
				Game.logger.warn("[领取当乐礼品] [序列号已被使用] [{}] [{}] [{}] [{}]", new Object[]{p.getUsername(),p.getName(),p.getId(),serial});
			return Translate.text_5535;
		}
		
//		if(isPlayerUsedSerial(p)){
//			Game.logger.warn("[领取当乐礼品] [您已经使用过序列号，本次活动不能再使用] ["+p.getUsername()+"] ["+p.getName()+"] ["+p.getId()+"] ["+serial+"]");
//			return "您已经使用过序列号，本次活动不能再使用";
//		}
		
		ddc.put(serial, p.getUsername() + "#" + p.getId());
		ddc.put(PLAYER_PREFIX + p.getId() , serial);
		
//		Game.logger.warn("[领取当乐礼品] [成功] ["+p.getUsername()+"] ["+p.getName()+"] ["+p.getId()+"] ["+serial+"] ["+articleName+"]");
		if(Game.logger.isWarnEnabled())
			Game.logger.warn("[领取当乐礼品] [成功] [{}] [{}] [{}] [{}] [{}]", new Object[]{p.getUsername(),p.getName(),p.getId(),serial,articleName});
		
		return null;
	}
	
	public boolean isSerialUsed(String serial){
		return ddc.get(serial) != null;
	}
	
	public String[] getSerials(){
		return serialToArticleMap.keySet().toArray(new String[0]);
	}

	public File getF14() {
		return f14;
	}

	public void setF14(File f14) {
		this.f14 = f14;
	}

	public File getF15() {
		return f15;
	}

	public void setF15(File f15) {
		this.f15 = f15;
	}

	public File getF16() {
		return f16;
	}

	public void setF16(File f16) {
		this.f16 = f16;
	}

	public File getF17() {
		return f17;
	}

	public void setF17(File f17) {
		this.f17 = f17;
	}

	public File getF18() {
		return f18;
	}

	public void setF18(File f18) {
		this.f18 = f18;
	}

	public File getF19() {
		return f19;
	}

	public void setF19(File f19) {
		this.f19 = f19;
	}

	public File getF20() {
		return f20;
	}

	public void setF20(File f20) {
		this.f20 = f20;
	}

	public File getF21() {
		return f21;
	}

	public void setF21(File f21) {
		this.f21 = f21;
	}

	public File getF22() {
		return f22;
	}

	public void setF22(File f22) {
		this.f22 = f22;
	}

	public File getF23() {
		return f23;
	}

	public void setF23(File f23) {
		this.f23 = f23;
	}

	public File getF24() {
		return f24;
	}

	public void setF24(File f24) {
		this.f24 = f24;
	}

	public File getF25() {
		return f25;
	}

	public void setF25(File f25) {
		this.f25 = f25;
	}

	public File getF26() {
		return f26;
	}

	public void setF26(File f26) {
		this.f26 = f26;
	}

	public File getF27() {
		return f27;
	}

	public void setF27(File f27) {
		this.f27 = f27;
	}

	public File getF28() {
		return f28;
	}

	public void setF28(File f28) {
		this.f28 = f28;
	}

	public File getF29() {
		return f29;
	}

	public void setF29(File f29) {
		this.f29 = f29;
	}

	public File getF30() {
		return f30;
	}

	public void setF30(File f30) {
		this.f30 = f30;
	}

	public File getF31() {
		return f31;
	}

	public void setF31(File f31) {
		this.f31 = f31;
	}

	public File getF32() {
		return f32;
	}

	public void setF32(File f32) {
		this.f32 = f32;
	}

	public File getF33() {
		return f33;
	}

	public void setF33(File f33) {
		this.f33 = f33;
	}

	public File getF34() {
		return f34;
	}

	public void setF34(File f34) {
		this.f34 = f34;
	}

	public File getF35() {
		return f35;
	}

	public void setF35(File f35) {
		this.f35 = f35;
	}

	public File getF36() {
		return f36;
	}

	public void setF36(File f36) {
		this.f36 = f36;
	}

	public File getF37() {
		return f37;
	}

	public void setF37(File f37) {
		this.f37 = f37;
	}

	public File getF38() {
		return f38;
	}

	public void setF38(File f38) {
		this.f38 = f38;
	}

	public File getF39() {
		return f39;
	}

	public void setF39(File f39) {
		this.f39 = f39;
	}

	public File getF40() {
		return f40;
	}

	public void setF40(File f40) {
		this.f40 = f40;
	}

	public File getF41() {
		return f41;
	}

	public void setF41(File f41) {
		this.f41 = f41;
	}

	public File getF42() {
		return f42;
	}

	public void setF42(File f42) {
		this.f42 = f42;
	}

	public File getF43() {
		return f43;
	}

	public void setF43(File f43) {
		this.f43 = f43;
	}

	public File getF44() {
		return f44;
	}

	public void setF44(File f44) {
		this.f44 = f44;
	}

	public File getF45() {
		return f45;
	}

	public void setF45(File f45) {
		this.f45 = f45;
	}

	public File getF46() {
		return f46;
	}

	public void setF46(File f46) {
		this.f46 = f46;
	}

	public File getF47() {
		return f47;
	}

	public void setF47(File f47) {
		this.f47 = f47;
	}

	public File getF48() {
		return f48;
	}

	public void setF48(File f48) {
		this.f48 = f48;
	}

	public File getF49() {
		return f49;
	}

	public void setF49(File f49) {
		this.f49 = f49;
	}

	public File getF50() {
		return f50;
	}

	public void setF50(File f50) {
		this.f50 = f50;
	}

	public File getF51() {
		return f51;
	}

	public void setF51(File f51) {
		this.f51 = f51;
	}

	public File getF52() {
		return f52;
	}

	public void setF52(File f52) {
		this.f52 = f52;
	}

	public File getF53() {
		return f53;
	}

	public void setF53(File f53) {
		this.f53 = f53;
	}

	public File getF54() {
		return f54;
	}

	public void setF54(File f54) {
		this.f54 = f54;
	}

	public File getF55() {
		return f55;
	}

	public void setF55(File f55) {
		this.f55 = f55;
	}

	public File getF56() {
		return f56;
	}

	public void setF56(File f56) {
		this.f56 = f56;
	}
}
