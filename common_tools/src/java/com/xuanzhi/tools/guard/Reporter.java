package com.xuanzhi.tools.guard;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONValue;
import com.xuanzhi.tools.servlet.HttpUtils;

/**
 * 
 * 
 */
public class Reporter implements Runnable {

	private HashMap<String, RobotGroup> groupMap = new HashMap<String, RobotGroup>();

	private boolean running;

	private long reportPeriod = 10000;
	
	private String staterUrl = "http://112.25.14.12:8889/webgame/robot_stater";

	private static Reporter instance;
	
	private Thread localThread;
	
	private long lastRefreshTime;

	public static Reporter getInstance() {
		if (instance == null) {
			synchronized (Reporter.class) {
				if (instance == null) {
					try {
						Class clazz = Class.forName(Reporter.class.getName());
						instance = (Reporter) clazz.newInstance();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return instance;
	}

	public HashMap<String, RobotGroup> getGroupMap() {
		return groupMap;
	}

	public void setGroupMap(HashMap<String, RobotGroup> groupMap) {
		this.groupMap = groupMap;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public long getReportPeriod() {
		return reportPeriod;
	}

	public void setReportPeriod(long reportPeriod) {
		this.reportPeriod = reportPeriod;
	}

	public String getStaterUrl() {
		return staterUrl;
	}

	public void setStaterUrl(String staterUrl) {
		this.staterUrl = staterUrl;
	}

	public void register(Robot robot, String group) {
		RobotGroup rg = groupMap.get(group);
		if (rg == null) {
			rg = new RobotGroup(group);
			groupMap.put(group, rg);
		}
		rg.getRobots().add(robot);
	}
	
	public void start() {
		if(!running) {
			running = true;
			localThread= new Thread(this, "Robot-Reporter");
			localThread.start();
		}
	}

	public void run() {
		while (running) {
			try {
				Thread.sleep(200);
				long start = System.currentTimeMillis();
				if(start - lastRefreshTime < this.reportPeriod) {
					continue;
				}
				HashMap<String, RobotGroup> groupMapPOJO = new HashMap<String, RobotGroup>();
				Set<Map.Entry<String, RobotGroup>> entrys = groupMap.entrySet();
				boolean finished = true;
				for (Map.Entry<String, RobotGroup> e : entrys) {
					RobotGroup rg = e.getValue();
					RobotGroup rgPOJO = new RobotGroup(rg.getName());
					rgPOJO.setLastRefreshTime(lastRefreshTime);
					List<Robot> robots = rg.getRobots();
					for (Robot r : robots) {
						RobotPOJO pojo = this.getPojo(r);
						rgPOJO.getRobots().add(pojo);
						if(r.getState() == 0) {
							finished = false;
						}
					}
					groupMapPOJO.put(e.getKey(), rgPOJO);
				}
				//下面进行报告
				postReport(groupMapPOJO);
				System.out.println("[提交报告] ["+groupMapPOJO.size()+"] ["+(start-lastRefreshTime)+"] [p:"+reportPeriod+"]");
				lastRefreshTime = start;
				if(finished) {
					running = false;
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		running = false;
	}

	public RobotPOJO getPojo(Robot robot) {
		RobotPOJO pojo = new RobotPOJO();
		pojo.setAction(robot.getAction());
		pojo.setLastActionTime(robot.getLastActionTime());
		pojo.setName(robot.getName());
		pojo.setSequence(robot.getSequence());
		pojo.setState(robot.getState());
		pojo.setCreateTime(robot.getCreateTime());
		return pojo;
	}
	
	public void postReport(HashMap<String, RobotGroup> groupMapPOJO) {
		RobotGroup groups[] = groupMapPOJO.values().toArray(new RobotGroup[0]);
		for(RobotGroup group : groups) {
			try {
				JSONValue json =  JSONMapper.toJSON(group);
				String jsonStr = json.render(false);
				HttpUtils.webPost(new URL(staterUrl), jsonStr.getBytes(), new HashMap(), 5000, 5000);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MapperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
