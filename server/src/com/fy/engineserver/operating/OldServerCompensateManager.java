package com.fy.engineserver.operating;

import java.io.File;

import org.slf4j.Logger;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class OldServerCompensateManager {
	File dataFile;
	
	static OldServerCompensateManager self;
	
	DefaultDiskCache ddc;
	
	MailManager mm;
	
	Logger log=Game.logger;
	
	public OldServerCompensateManager(){
		
	}
	
	public void init(){
		long t=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		this.ddc = new DefaultDiskCache(this.dataFile, null,
				Translate.text_5552, 100L * 365 * 24 * 3600 * 1000L);
		this.mm=MailManager.getInstance();
		OldServerCompensateManager.self=this;
		System.out.println("[系统初始化] [老服务器补偿管理] [完成] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"]");
	}

	public File getDataFile() {
		return dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}
	
	public static OldServerCompensateManager getInstance(){
		return OldServerCompensateManager.self;
	}
	
	/**
	 * 领取老服回馈
	 * @param player
	 */
	public void takeOldServerCompensate(Player player){}
	
	/**
	 * 领取合服补偿
	 * @param player
	 */
	public void takeConsolidateServerCompensate(Player player){}
}
