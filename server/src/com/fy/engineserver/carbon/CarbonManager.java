package com.fy.engineserver.carbon;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;

import com.fy.engineserver.carbon.devilSquare.DevilSquareConstant;
import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;

/**
 * 副本开启通知管理线程
 * @author Administrator
 *
 */
public class CarbonManager implements Runnable{
	public static Logger logger = DevilSquareManager.logger;
	
	public static final byte 翅膀副本 = 1;
	
	public static boolean isStart = true;
	
	private List<CarbonMgIns> carbonMgList = new ArrayList<CarbonMgIns>();
	
	private static CarbonManager inst;

	public void init() {
		inst = this;
		Thread t = new Thread(inst, "副本开启管理线程");
		t.start();
	}
	
	public static CarbonManager getInst() {
		return inst;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		DevilSquareManager.logger.warn("[CarbonManager 初始化成功]");
		while(isStart) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				logger.error("[副本启动管理线程出错]", e);
			}
			if (!com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished() || carbonMgList.size() <= 0) {
				continue;
			}	
			Calendar currentCalender = Calendar.getInstance();
			int currentHour = currentCalender.get(Calendar.HOUR_OF_DAY);
			int min = currentCalender.get(Calendar.MINUTE);
			for(CarbonMgIns ins : carbonMgList) {
				List<Integer> hours = ins.getActHour();
//				if(logger.isInfoEnabled()) {
//					logger.info("[副本启动管理线程][acttype=" + ins.getActType() + "] [ min=" + ins.maxNotifyMinits() + "]");
//				}
				switch (ins.getActType()) {
				case DevilSquareConstant.DAILY_HOUR:
					if(isDailyHour(hours, currentHour) && min <= ins.maxNotifyMinits()) {
						try {
							ins.doAct();
						} catch(Exception e) {
							DevilSquareManager.logger.error("[CarbonManager 通知执行方法出错][" + ins + "]", e);
						}
					} else {
						if(preNotifyHour(hours, currentHour,min, ins.preNotifyMinits())) {
							try {
								ins.doPreAct();
							} catch(Exception e) {
								DevilSquareManager.logger.error("[CarbonManager 通知执行pre方法出错][" + ins + "]", e);
							}
						}
					}
					break;
				default:
					DevilSquareManager.logger.warn("[副本开启时间规则未实现][" + ins + "]");
					break;
				}
			}
		}
		if(!isStart) {
			DevilSquareManager.logger.error("[CarbonManager 系统已关闭!!]");
		}
	}
	
	private boolean preNotifyHour(List<Integer> hours, int currentHour, int currentMinits, int preMinits) {
		for(int ii : hours) {
			if((ii - 1) == currentHour) {
				if((currentMinits + preMinits) == 60) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 每天固定点开启副本
	 */
	private boolean isDailyHour(List<Integer> hours, int currentHour) {
		if(hours.contains(currentHour)) {
			return true;
		}
		return false;
	}
	
	public void registManager(CarbonMgIns mgInst) {
		carbonMgList.add(mgInst);
	}
	
	public List<CarbonMgIns> getCarbonMgList() {
		return carbonMgList;
	}

}
