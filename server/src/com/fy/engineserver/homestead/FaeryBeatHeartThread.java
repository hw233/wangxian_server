package com.fy.engineserver.homestead;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.homestead.faery.Faery;
import com.fy.engineserver.homestead.faery.service.FaeryManager;

public class FaeryBeatHeartThread implements Runnable, Comparable<FaeryBeatHeartThread> {

	private int size;
	private String name;
	private List<Faery> faeries = new ArrayList<Faery>();

	// public static Logger logger = Logger.getLogger(FaeryBeatHeartThread.class);
	public static Logger logger = LoggerFactory.getLogger(FaeryBeatHeartThread.class);

	public static boolean running = true;

	protected long amountOfBeatheart = 0;

	protected long amountOfOverflow = 0;

	public FaeryBeatHeartThread(String name) {
		this.name = name;
	}

	static long sept = 200;

	@Override
	public void run() {
		while (running) {
			long start = SystemTime.currentTimeMillis();
			try {
				if (!com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished()) {
					Thread.sleep(sept);
					continue;
				}
			} catch (InterruptedException e) {
				logger.error("run error", e);
			}
			
			try {
				for (int i = 0; i < faeries.size(); i++) {
					faeries.get(i).beatHeart();
				}

				long costTime = SystemTime.currentTimeMillis() - start;
				com.xuanzhi.tools.mem.HeartbeatTrackerService.operationEnd("游戏心跳", "xianfu", costTime);
				if (costTime > sept) {
					logger.error("[{}] [beatheart-overflow]  [overflow:{}] [amount{}] [cost:{}ms]", new Object[] { Thread.currentThread().getName(), amountOfOverflow, amountOfBeatheart, costTime });
					amountOfOverflow++;
				}
				amountOfBeatheart++;
				
				if(sept > costTime){
					try {
						Thread.sleep(sept - costTime);
					} catch (Exception e) {
						FaeryManager.logger.error(getName() + "异常", e);
					}
				}
				
			} catch (Throwable e) {
				FaeryManager.logger.error(getName() + "异常", e);
			}
		}
	}

	public synchronized void addFaery(Faery faery) {
		faeries.add(faery);
	}

	@Override
	public int compareTo(FaeryBeatHeartThread o) {
		return getSize() - o.getSize();
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Faery> getFaeries() {
		return faeries;
	}

	public void setFaeries(List<Faery> faeries) {
		this.faeries = faeries;
	}

}
