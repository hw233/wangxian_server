package com.fy.engineserver.lineup;


/**
 *
 * 
 * @version 创建时间：Mar 1, 2012 5:38:37 PM
 * 
 */
public class EnterServerTester implements Runnable  {
	
	private int newNum = 0;
	
	private int total = 0;
	
	private long enterPeriod = 200;
	private long newNumCutPeriod = 500;
	private long totalCutPeriod = 800;
	
	private boolean running;
	
	private Thread thread;
	
	private EnterServerAgent agent;
	
	public static EnterServerTester tester;
	
	public EnterServerTester(int enterPeriod, int newNumCutPeriod, int totalCutPeriod) {
		this.enterPeriod = enterPeriod;
		this.newNumCutPeriod = newNumCutPeriod;
		this.totalCutPeriod = totalCutPeriod;
	}
	
	public void start() {
		running = true;
		thread = new Thread(this, "EnterServerTester");
		thread.start();
	}
	
	public void stop() {
		running = false;
	}
	
	public int getNewNum() {
		return newNum;
	}


	public void setNewNum(int newNum) {
		this.newNum = newNum;
	}


	public int getTotal() {
		return total;
	}


	public void setTotal(int total) {
		this.total = total;
	}


	public EnterServerAgent getAgent() {
		return agent;
	}


	public void setAgent(EnterServerAgent agent) {
		this.agent = agent;
	}


	public long getEnterPeriod() {
		return enterPeriod;
	}

	public void setEnterPeriod(long enterPeriod) {
		this.enterPeriod = enterPeriod;
	}

	public long getNewNumCutPeriod() {
		return newNumCutPeriod;
	}

	public void setNewNumCutPeriod(long newNumCutPeriod) {
		this.newNumCutPeriod = newNumCutPeriod;
	}

	public long getTotalCutPeriod() {
		return totalCutPeriod;
	}

	public void setTotalCutPeriod(long totalCutPeriod) {
		this.totalCutPeriod = totalCutPeriod;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public void run() {
		int index = 0;
		long lastCutTotal = System.currentTimeMillis();
		long lastCutNew = System.currentTimeMillis();
		while(running) {
			try {
				Thread.sleep(enterPeriod);
				long start = System.currentTimeMillis();
				String userName = "TEST-" + (index++);
				boolean canEnter = agent.canEnterDirectly(userName, null);
				if(canEnter) {
					//发送进入游戏的协议
					newNum++;
					total++;
					agent.playerEnterCallback(userName);
				} else {
					agent.doLineup(userName, null);
				}
				
				if(start - lastCutNew > newNumCutPeriod)  {
					if(newNum > 0) {
						newNum--;
					}
					lastCutNew = start;
				}
				if(start - lastCutTotal > totalCutPeriod) {
					total--;
					lastCutTotal = start;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]) {
		tester = new EnterServerTester(200, 500, 800);
		EnterServerAgent agent = new EnterServerAgent();
		agent.init();
		//agent.setTester(tester);
		tester.setAgent(agent);
		Thread t = new Thread(tester);
		t.start();
	}
}
