package com.fy.engineserver.septstation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeptStationBeatHeartThread implements Runnable, Comparable<SeptStationBeatHeartThread> {

	// static Logger logger =
	// Logger.getLogger(SeptStationBeatHeartThread.class);
	public static Logger logger = LoggerFactory.getLogger(SeptStationBeatHeartThread.class);
	protected int beatheart = 5;// 每秒心跳多少次
	protected boolean running = true;

	protected long amountOfBeatheart = 0;

	protected long amountOfOverflow = 0;
	protected Thread thread = null;
	protected SeptStation[] septStations = new SeptStation[0];// 所有的心跳驻地

	protected List<SeptStation> newSeptStations = new ArrayList<SeptStation>();
	protected List<SeptStation> removeGameList = new ArrayList<SeptStation>();

	protected String name;

	public void addToNewList(SeptStation station) {
		synchronized (newSeptStations) {
			newSeptStations.add(station);
		}
	}

	public void start() {
		running = true;
		thread = new Thread(this, name);
		thread.start();
	}

	public void stop() {
		running = false;
	}

	public int indexOf(SeptStation g) {
		for (int i = 0; septStations != null && i < septStations.length; i++) {
			if (septStations[i].equals(g)) return i;
		}
		return -1;
	}

	public boolean contains(SeptStation g) {
		return indexOf(g) != -1;
	}

	private void doToThread(SeptStation g) {
		if (contains(g)) return;
		if (septStations == null) septStations = new SeptStation[0];
		SeptStation gg[] = new SeptStation[septStations.length + 1];
		System.arraycopy(septStations, 0, gg, 0, septStations.length);
		gg[septStations.length] = g;
		septStations = gg;
	}

	private void doRemoveBattleField(SeptStation g) {
		int k = indexOf(g);
		if (k == -1) return;
		SeptStation gg[] = new SeptStation[septStations.length - 1];
		System.arraycopy(septStations, 0, gg, 0, k);
		System.arraycopy(septStations, k + 1, gg, k, gg.length - k);
		septStations = gg;
	}

	@Override
	public void run() {
		long step = 1000 / beatheart;
		while (running) {
			try {
				long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

				if (!com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished()) {
					Thread.sleep(step);
					continue;
				}
				// TODO LOGIC
				if (newSeptStations.size() > 0) {
					synchronized (newSeptStations) {
						if (newSeptStations.size() > 0) {
							SeptStation gs[] = newSeptStations.toArray(new SeptStation[0]);
							newSeptStations.clear();
							for (int j = 0; j < gs.length; j++) {
								doToThread(gs[j]);
							}
						}
					}
				}

				if (removeGameList.size() > 0) {
					synchronized (removeGameList) {
						if (removeGameList.size() > 0) {
							SeptStation gs[] = removeGameList.toArray(new SeptStation[0]);
							removeGameList.clear();
							for (int j = 0; j < gs.length; j++) {
								doRemoveBattleField(gs[j]);
							}
						}
					}
				}
				for (int i = 0; septStations != null && i < septStations.length; i++) {
					try {
						septStations[i].heartbeat();
					} catch (Exception e) {
						logger.error("heartbeat catch exception on " + septStations[i].getName(), e);
					}
				}

				long costTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime;
				com.xuanzhi.tools.mem.HeartbeatTrackerService.operationEnd("游戏心跳", "jiazu", costTime);
				if (costTime < step) {
					Thread.sleep(step);
				} else {
					logger.error("[{}] [beatheart-overflow]  [overflow:{}] [amount{}] [cost:{}ms]", new Object[] { Thread.currentThread().getName(), amountOfOverflow, amountOfBeatheart, costTime });
					amountOfOverflow++;
					step = 1000 / beatheart;
				}
				amountOfBeatheart++;
			} catch (Throwable e) {
				logger.error("[家族驻地心跳异常]", e);
			}
		}
	}

	/********************** getters and setters *******************************/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBeatheart() {
		return beatheart;
	}

	public void setBeatheart(int beatheart) {
		this.beatheart = beatheart;
		this.beatheart = 1;
	}

	@Override
	public int compareTo(SeptStationBeatHeartThread o) {
		return this.getSize() - o.getSize();
	}

	private int getSize() {
		return septStations.length;
	}
}
