package com.fy.engineserver.qiancengta;

import java.util.ArrayList;

public class QianCengTa_Thread implements Runnable {
	
	long step = 200;

	protected long amountOfBeatheart = 0;

	protected long amountOfOverflow = 0;
	
	protected boolean running = false;
	protected Thread thread = null;
	
	protected String name;
	
	protected ArrayList<QianCengTa_Ta> taLists = new ArrayList<QianCengTa_Ta>();
	public ArrayList<QianCengTa_Ta> getTaLists() {
		return taLists;
	}

	public void setTaLists(ArrayList<QianCengTa_Ta> taLists) {
		this.taLists = taLists;
	}

	protected ArrayList<QianCengTa_Ta> removeLists = new ArrayList<QianCengTa_Ta>();
	
	public int getTaSize() {
		return taLists.size();
	}
	
	public void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this, name);
		thread.start();
	}

	public void stop() {
		running = false;
	}
	
	public void addTa(QianCengTa_Ta ta) {
		synchronized (taLists) {
			taLists.add(ta);
		}
	}
	
	public void removeTa(QianCengTa_Ta ta) {
		synchronized (removeLists) {
			removeLists.add(ta);
		}
	}
	
	@Override
	public void run() {
		while (running) {
			try{
				long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				
				if (taLists.size() > 0) {
					QianCengTa_Ta[] tas = new QianCengTa_Ta[0];
					synchronized (taLists) {
						if (removeLists.size() > 0) {
							synchronized (removeLists) {
								for (int i = 0; i < removeLists.size(); i++) {
									taLists.remove(removeLists.get(i));
								}
							}
							removeLists.clear();
						}
						tas = taLists.toArray(new QianCengTa_Ta[0]);
					}
					for (int i = 0; i < tas.length; i++) {
						try{
							tas[i].heartbeat(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						}catch(Exception e) {
							QianCengTaManager.getInstance().logger.error("千层塔心跳出错" + name + " [" + tas[i].getPlayerId() +"]", e);
						}
					}
//					QianCengTaManager.getInstance().logger.warn("[心跳] [{}] [{}] [{}] [{}]", new Object[]{taLists.size(), amountOfBeatheart, amountOfOverflow, tas.length});
				}
				long costTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime;
				if (costTime < step) {
					try {
						Thread.sleep(step - costTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}else {
					if (QianCengTaManager.getInstance().logger.isWarnEnabled()) {
						QianCengTaManager.getInstance().logger.warn("千层塔心跳超时 [{}] [{}] [{}] [{}]", new Object[]{name, amountOfBeatheart, amountOfOverflow, costTime});
					}
					amountOfOverflow++;
				}
				amountOfBeatheart++;
			}catch(Exception e) {
				QianCengTaManager.getInstance().logger.error("千层塔心跳出错" + name, e);
			}
		}
	}

}
