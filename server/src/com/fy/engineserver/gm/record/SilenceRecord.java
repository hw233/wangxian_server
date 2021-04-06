package com.fy.engineserver.gm.record;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.language.Translate;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class SilenceRecord {
//	Logger logger = Logger.getLogger(SilenceRecord.class);
public	static Logger logger = LoggerFactory.getLogger(SilenceRecord.class);
	private DefaultDiskCache ddc;
	private File dataFile;
	private static SilenceRecord self;
	public static SilenceRecord getInstance(){
		return self;
	}
	public void initialize() {
		long old = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ddc = new DefaultDiskCache(dataFile, null, Translate.text_4228, 100L * 365 * 24
				* 3600 * 1000L);
		self = this;
		System.out.println(SilenceRecord.class.getName() + " init success "
				+ (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - old));
//		logger.info(SilenceRecord.class.getName() + "init success ");
		if(logger.isInfoEnabled())
			logger.info("{}init success ", new Object[]{SilenceRecord.class.getName()});

	}
	public void silence(String pid,String message){
		ddc.put(pid, message);
		if(logger.isInfoEnabled())
			logger.info("save success");
	}
	public String getMessage(String pid){
		return (ddc.get(pid)==null?null:(String)ddc.get(pid));
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
