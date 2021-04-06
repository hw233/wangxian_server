package com.fy.engineserver.newBillboard.monitorLog;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.util.ServiceStartRecord;

//活动日志记录
public class LogRecordManager {

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String 家族 = "家族";
	public static String 仙府 = "仙府";
	public static String 等级 = "等级";
	public static String 酒仙 = "酒仙";
	public static String 宠物 = "宠物";
	public static String 连斩 = "当月连斩";
	public static String 当日连斩 = "当日连斩";
	public static String 当日鲜花 = "当日鲜花";
	public static String 当日糖果 = "当日糖果";
	public static String 当月糖果 = "当月糖果";
	public static String 当月鲜花 = "当月鲜花";
	public static String 当月连斩 = "当月连斩";
	public static String 当日棉花糖 = "当日棉花糖";
	public static String 大使技能重数 = "大使技能重数";
	
	public static LogRecordManager manager = null;
	public Map<String, LogRecord> map;
	
	public void init()throws Exception{
		
		
		this.manager = this;
		Map<String, LogRecord> map = new HashMap<String, LogRecord>();
		LogRecord jiazu  = new JiazuLogRecord();
		jiazu.init();
		map.put(LogRecordManager.家族, jiazu);
		
		LogRecord cave  = new CaveLogRecord();
		cave.init();
		map.put(LogRecordManager.仙府, cave);
		
		LogRecord level  = new LevelLogRecord();
		level.init();
		map.put(LogRecordManager.等级, level);
		
		LogRecord beer  = new BeerLogRecord();
		beer.init();
		map.put(LogRecordManager.酒仙, beer);
		
		LogRecord pet  = new PetLogRecord();
		pet.init();
		map.put(LogRecordManager.宠物, pet);
		
		
		LogRecord kill = new KillJoinLogRecord();
		kill.init();
		map.put(LogRecordManager.连斩, kill);
		
		LogRecord daykill = new DayKillJoinLogRecord();
		daykill.init();
		map.put(LogRecordManager.当日连斩, daykill);

		
		LogRecord dayFlower = new DayFlowerLogRecord();
		dayFlower.init();
		map.put(LogRecordManager.当日鲜花, dayFlower);
		
		LogRecord daySweet = new DaySweetLogRecord();
		daySweet.init();
		map.put(LogRecordManager.当日糖果, daySweet);
		
		LogRecord monthSweet = new MonthSweetLogRecord();
		monthSweet.init();
		map.put(LogRecordManager.当月糖果, monthSweet);
		
		LogRecord monthFlower = new MonthFlowerLogRecord();
		monthFlower.init();
		map.put(LogRecordManager.当月鲜花, monthFlower);
		
		LogRecord monthKill = new MonthKillJoinLogRecord();
		monthKill.init();
		map.put(LogRecordManager.当月连斩, monthKill);
		
		LogRecord dayFlowerMHT = new DayFlowerMHTLogRecord();
		dayFlowerMHT.init();
		map.put(LogRecordManager.当日棉花糖, dayFlowerMHT);
		
		this.map = map;
		
		BillboardsManager.logger.error("[LogRecordManager初始化完成]");
		ServiceStartRecord.startLog(this);
	}
	
	
	public void 活动记录日志(String type,Billboard billboard){
		BillboardsManager.logger.error("[准备打印日志] ["+type+"] ["+billboard.getClass()+"]");
		try{
			if(map != null){
				LogRecord record = map.get(type);
				if(record != null){
//					boolean bln = record.isMatchTime();
//					if(bln){
						record.打印日志(billboard);
//					}
				}else{
					BillboardsManager.logger.error("[记录活动日志错误] ["+type+"]");
				}
			}
		}catch(Exception e){
			BillboardsManager.logger.error("[活动记录日志异常] ["+type+"]",e);
		}
	}
	
	
	public synchronized static LogRecordManager getInstance(){
		
		return manager;
		
	}
	
}
