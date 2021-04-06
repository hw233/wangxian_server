package com.fy.engineserver.gametime;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.event.Event;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.gametime.GameTimeFactory.SourceType;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool.TimeDistance;

public class SystemTime {

	Logger logger = LoggerFactory.getLogger(SystemTime.class);

	private AbstractGameTime time;
	public int day = -1;
	public int hour = -1;
	public int minits = -1;
	public int fiveMinits = -1;

	private static SystemTime instance;

	/** 睡觉时间间隔.每sleepCycle毫秒获得一次系统时间 */
	private long sleepCycle = 1;

	private int type;

	private Timer timer;
	private TimerTask task;

	public static SystemTime getInstance() {
		return instance;
	}

	public static long currentTimeMillis() {
		return instance.getTime().currentTimeMillis();
	}

	public AbstractGameTime getTime() {
		return time;
	}

	public void setTime(AbstractGameTime time) {
		if (this.time != null) {
			this.time.cancel();
		}
		this.time = time;
	}

	private void print() {
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				if (logger.isDebugEnabled()) {
					logger.debug(time.getAverageVisitTimesInfo(TimeDistance.SECOND));
				}
				Calendar cInst = Calendar.getInstance();
				int curDay = cInst.get(Calendar.DATE);
				if (curDay != day) {
					day = curDay;
					EventWithObjParam evt = new EventWithObjParam(Event.SERVER_DAY_CHANGE, day);
					EventRouter.getInst().addEvent(evt);
				}
				int curHour = cInst.get(Calendar.HOUR_OF_DAY);
				if (curHour != hour) {
					hour = curHour;
					EventWithObjParam evt = new EventWithObjParam(Event.SERVER_HOUR_CHANGE, hour);
					EventRouter.getInst().addEvent(evt);
				}
//				int curMinit = cInst.get(Calendar.MINUTE);
//				if(curMinit != minits) {
//					minits = curMinit;
//					EventWithObjParam evt = new EventWithObjParam(Event.SERVER_MINU_CHANGE, minits);
//					EventRouter.getInst().addEvent(evt);
//				}
//				if (curMinit % 5 == 0 && curMinit != fiveMinits) {
//					fiveMinits = curMinit;
//					EventWithObjParam evt = new EventWithObjParam(Event.SERVER_5MINU_CHANGE, fiveMinits);
//					EventRouter.getInst().addEvent(evt);
//				}
			}
		};
		timer.schedule(task, 0, 5 * 1000L);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void init() {
		
		instance = this;
		SourceType sourceType = SourceType.SYSTEM;
		switch (type) {
		case 0:
			sourceType = SourceType.SYSTEM;
			break;
		case 1:
			sourceType = SourceType.SELF;
			break;
		}
		instance.setTime(GameTimeFactory.createCurrentTime(sourceType));
		if (sleepCycle <= 0) {
			sleepCycle = 1;
		}
		//
		Calendar cInst = Calendar.getInstance();
		day = cInst.get(Calendar.DATE);
		hour = cInst.get(Calendar.HOUR_OF_DAY);
		minits = cInst.get(Calendar.MINUTE);
		//
		instance.getTime().setPeriod(sleepCycle);
		instance.getTime().start();
		instance.print();
		ServiceStartRecord.startLog(this);
	}

	public void destroy() {
		if (instance.getTime() != null) {
			instance.getTime().cancel();
		}
		if (task != null) {
			task.cancel();
		}
		if (timer != null) {
			timer.cancel();
		}
	}

	public long getSleepCycle() {
		return sleepCycle;
	}

	public void setSleepCycle(long sleepCycle) {
		this.sleepCycle = sleepCycle;
	}

	public Timer getTimer() {
		return timer;
	}
}
