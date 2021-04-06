package com.fy.engineserver.operating.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import com.fy.engineserver.datasource.language.Translate;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;

public class SerialNumberAndMagicCardManager {
	DefaultDiskCache ddc;
	
	AdvancedFilePersistentQueue fileQueue10;
	AdvancedFilePersistentQueue fileQueue50;
	
	File magicCardDir10;
	File magicCardDir50;
	
	File dataFile;
	
	File serialNoFile;
	
	static SerialNumberAndMagicCardManager instance;
	
	Set<String> serialNoSet = new HashSet<String>();
	
	public final static int WITH_DRAW_OK = 0;
	public final static int WITH_DRAW_INVALID_SERIAL_NO = 1;
	public final static int WITH_DRAW_SERIAL_ALREADY_WITHDRAWN = 2;
	
	public final static String SERIAL_NO_PREFIX = "{PREFIX}";
	
	public File getSerialNoFile() {
		return serialNoFile;
	}

	public void setSerialNoFile(File serialNoFile) {
		this.serialNoFile = serialNoFile;
	}

	public File getMagicCardDir10() {
		return magicCardDir10;
	}

	public void setMagicCardDir10(File magicCardDir10) {
		this.magicCardDir10 = magicCardDir10;
	}

	public File getMagicCardDir50() {
		return magicCardDir50;
	}

	public void setMagicCardDir50(File magicCardDir50) {
		this.magicCardDir50 = magicCardDir50;
	}

	public File getDataFile() {
		return dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

	public void init() throws Exception{
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		
		instance = this;
		ddc = new DefaultDiskCache(dataFile, null,
				Translate.text_5540, 100L * 365 * 24 * 3600 * 1000L);
		
		
		magicCardDir10.mkdirs();
		magicCardDir50.mkdirs();		
		
		fileQueue10 = new AdvancedFilePersistentQueue(magicCardDir10 , 16, 32 * 1024 * 1024);
		fileQueue50 = new AdvancedFilePersistentQueue(magicCardDir50 , 16, 32 * 1024 * 1024);
		
		if(serialNoFile == null || !serialNoFile.exists() || !serialNoFile.isFile()){
			System.out.println("[系统初始化] [内测礼包序列号领取] [初始化失败] ["+getClass().getName()+"] [请检查记录序列号的文件配置]");
		}else{
			loadSerialNoFile(serialNoFile);
		}

		System.out.println("[系统初始化] [内测礼包序列号领取] [初始化完成] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
	}
	
	/**
	 * 获得10元神奇卡
	 */
	public MagicCard getMagicCard10(){
		return (MagicCard)fileQueue10.pop();
	}
	
	/**
	 * 获得50元神奇卡
	 */
	public MagicCard getMagicCard50(){
		return (MagicCard)fileQueue50.pop();
	}
	
	private void loadSerialNoFile(File file) throws Exception{
		BufferedReader bw = null;
		
		try {
			bw = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
			String line = null;
			while((line = bw.readLine()) != null){
				line = line.trim();
				serialNoSet.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("载入内测序列号出错[" + file.getAbsolutePath() + "]！！！");
		} finally{
			if(bw != null){
				try {
					bw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void addMagicCard(String cardNo , String password , int price) throws Exception{
		MagicCard card = new MagicCard();
		card.setCardNo(cardNo);
		card.setPassword(password);
		card.setPrice(price);
	
		if(price == 10){
			fileQueue10.push(card);			
		}else if(price == 50){
			fileQueue50.push(card);
		}
	}
	
	public static SerialNumberAndMagicCardManager getInstance(){
		return instance;
	}
	
	public boolean isSerialNoValid(String serialNo){
		return serialNoSet.contains(serialNo);
	}
	
	/**
	 * 用户领取内测
	 * 
	 * @param userName
	 * @param serialNo
	 * @return
	 */
	public synchronized void withdrawGift(String userName , String serialNo , MagicCard card){
		SerialNumberAndMagicCardRecord record = null;
		Object data = ddc.get(userName);
		
		if(data instanceof SerialNumberAndMagicCardRecord){
			record = (SerialNumberAndMagicCardRecord) data;
		}else{
			record = new SerialNumberAndMagicCardRecord();
		}
		
		record.recordSerialNo(serialNo, card);
		
		ddc.put(serialNo, userName);
		ddc.put(userName , record);
	}
	
	public boolean isSerialNoWithdrawn(String serialNo){
		return ddc.get(serialNo) != null;
	}
	
	public boolean isDailyMagicCardRequested(String userName , long time){
		SerialNumberAndMagicCardRecord record = (SerialNumberAndMagicCardRecord)ddc.get(userName);
		if(record == null){
			return false;
		}
		
		return record.isDailyMagicCardRequested(userName, time);
	}
	
	public synchronized void requestDailyMagicCard(String userName , MagicCard card , long time){
		SerialNumberAndMagicCardRecord record = null;
		Object data = ddc.get(userName);
		
		if(data instanceof SerialNumberAndMagicCardRecord){
			record = (SerialNumberAndMagicCardRecord) data;
		}else{
			record = new SerialNumberAndMagicCardRecord();
		}
		
		record.recordRequestMagicCard(userName, time , card);
		
		ddc.put(userName , record);
	}
	
	public AdvancedFilePersistentQueue getFileQueue10(){
		return fileQueue10;
	}
	
	public AdvancedFilePersistentQueue getFileQueue50(){
		return fileQueue50;
	}
	
	public int getCurrentAvailableMagicCards10Count(){
		return fileQueue10.elementNum();
	}
	
	public int getCurrentAvailableMagicCards50Count(){
		return fileQueue50.elementNum();
	}
}
