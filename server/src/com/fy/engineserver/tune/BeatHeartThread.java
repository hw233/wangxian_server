package com.fy.engineserver.tune;

import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.transport.Connection;

public class BeatHeartThread implements Runnable {

	protected long amountOfBeatheart = 0;

	protected long amountOfOverflow = 0;

	protected RobotPlayer players[] = new RobotPlayer[0];
	
	protected boolean running = false;
	protected Thread thread = null;

	protected String name;
	
	public BeatHeartThread(String name) {
		this.name = name;
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

	public RobotPlayer[] getRobotPlayers() {
		if (players == null)
			return new RobotPlayer[0];
		return players;
	}

	public void addRobotPlayer(RobotPlayer player) {
		RobotPlayer gg[] = new RobotPlayer[players.length + 1];
		System.arraycopy(players, 0, gg, 0, players.length);
		gg[players.length] = player;
		players = gg;
	}

	public long getAmountOfBeatheart() {
		return amountOfBeatheart;
	}

	public void setAmountOfBeatheart(long amountOfBeatheart) {
		this.amountOfBeatheart = amountOfBeatheart;
	}

	public long getAmountOfOverflow() {
		return amountOfOverflow;
	}

	public void setAmountOfOverflow(long amountOfOverflow) {
		this.amountOfOverflow = amountOfOverflow;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void run() {
		try {
			long step = 200;
			int i = 0;
			while (running) {
				Thread.sleep(step);
				for (i = 0; players != null && i < players.length; i++) {
					try {
						long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
						if(!players[i].isEnd()) {
							players[i].heartbeat2(startTime);
						}  else {
							boolean offline = false;
							if(players[i].getServerConnection() != null && players[i].getServerConnection().getState() != Connection.CONN_STATE_CLOSE) {
								players[i].getServerConnection().close();
								offline = true;
							}
							if(players[i].getGatewayConnection() != null && players[i].getGatewayConnection().getState() != Connection.CONN_STATE_CLOSE) {
								players[i].getGatewayConnection().close();
							}
							if(offline)
								System.out.println("[玩家下线] ["+players[i].getUsername()+"]");
						}
					} catch (Throwable e) {
						System.out.println("[机器人心跳异常] ["+players[i].getUsername()+"]\n" + StringUtil.getStackTrace(e.getStackTrace()));
					}
				}
			}
		} catch(Throwable e){
			System.out.println("[" + Thread.currentThread().getName()+ "] [beatheart-stop]  [overflow:"+ amountOfOverflow + "] [amount" + amountOfBeatheart+ "]");
		}finally {
			running = false;
		}
	}
}
