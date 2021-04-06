package com.fy.engineserver.gm.record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BroadManager {
	//广播记录操作
//	protected static Logger logger = Logger.getLogger(BroadManager.class
public	static Logger logger = LoggerFactory.getLogger(BroadManager.class
			.getName());
	private String broadConfigFile;
	private static BroadManager self;
	private List<BroadCastRecord> brs = new ArrayList<BroadCastRecord>();

	public void initialize() {
		// ......
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try {
			brs = RecordUtil.loadBroadCastPage(broadConfigFile);
			self = this;
			System.out.println(this.getClass().getName()
					+ " initialize successfully ["
					+ (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "]");
if(logger.isInfoEnabled())
	logger.info("{} initialize successfully [{}][saveFile:{}]", new Object[]{this.getClass().getName(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now),broadConfigFile});
		} catch (Exception e) {
			e.printStackTrace();
if(logger.isInfoEnabled())
	logger.info("{} initialize fail [saveFile:{}]", new Object[]{this.getClass().getName(),broadConfigFile});
		}
	}

	public static BroadManager getInstance() {
		return self;
	}

	public void saveRecord(BroadCastRecord bs) {
		try {
			brs.add(bs);
			RecordUtil.saveBroadCastPage(brs, broadConfigFile);
//			logger.info("save broadConfigFile success at " + new Date());
			if(logger.isInfoEnabled())
				logger.info("save broadConfigFile success at {}", new Object[]{new Date()});
		} catch (Exception e) {
			e.printStackTrace();
//			logger.info("save broadConfigFile fail at " + new Date());
			if(logger.isInfoEnabled())
				logger.info("save broadConfigFile fail at {}", new Object[]{new Date()});
		}
	}

	public String getBroadConfigFile() {
		return broadConfigFile;
	}

	public void setBroadConfigFile(String broadConfigFile) {
		this.broadConfigFile = broadConfigFile;
	}

	public List<BroadCastRecord> getBrs() {
		return brs;
	}

	public void setBrs(List<BroadCastRecord> brs) {
		this.brs = brs;
	}
  
}
