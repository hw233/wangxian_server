package com.xuanzhi.tools.timer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.watchdog.FileWatchdog;

/**
 * 计划执行者,通过一个map管理一个任务计划，以分钟为单位进行检索任务执行
 * 每天凌晨定义当天的任务计划至map中
 * 计划定义格式，类似crontab定义:
 * =================================================
 * =基本格式 : * * * * * Executeble.class args
 * 分　时　日　月　周　命令 参数
 * 第1列表示分钟0～59 每分钟
 * 第2列表示小时0～23（0表示0点）
 * 第3列表示日期1～31
 * 第4列表示月份1～12
 * 第5列标识号星期0～6（0表示星期天）
 * =================================================
 * @author frank
 *
 */
public class Scheduler extends Thread {
	
	protected static final Log cat = LogFactory.getLog("timer.Scheduler");
	
	private String file = null;
	
	private List<String> taskList = null;
	
	private HashMap<Integer, String> map = null;
	
	private int nDay = 0;
	
	public Scheduler(String name, String file) {
		super(name);
		this.file = file;
		File ioFile = new File(file);
		taskList = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ioFile)));
			String line = null;
			while((line = br.readLine()) != null) {
				if(line.trim().length() > 0 && !line.startsWith("#")) {
					taskList.add(line);
					cat.info("[add] ["+line+"]");
				}
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map = new HashMap<Integer, String>();
		buildSchedule();
		SchedulerFileWatchdog dog = new SchedulerFileWatchdog();
        dog.setDelay(60*1000);
        dog.setDaemon(true);
        dog.setName("Scheduler-File-Watchlog");
        dog.addFile(ioFile);
        dog.start();
		cat.info("[Scheduler] [init] ["+file+"] ["+taskList.size()+" tasks]");
		Integer keys[] = map.keySet().toArray(new Integer[0]);
		for(Integer key : keys) {
			String value = map.get(key);
			if(value != null) {
				String values[] = value.split("\t");
				for(String v : values) {
					cat.info("[map] ["+minToTime(key)+"] ["+getExecuteScript(v)+"]");
				}
			}
		}
	}
	
	private void reload() {
		List<String> newList = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			while((line = br.readLine()) != null) {
				if(line.trim().length() > 0 && !line.startsWith("#"))
					newList.add(line);
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		taskList = newList;
		buildSchedule();
		cat.info("[Scheduler] [reload] ["+file+"] ["+taskList.size()+" tasks]");
		Integer keys[] = map.keySet().toArray(new Integer[0]);
		for(Integer key : keys) {
			String value = map.get(key);
			if(value != null) {
				String values[] = value.split("\t");
				for(String v : values) {
					cat.info("[map] ["+minToTime(key)+"] ["+getExecuteScript(v)+"]");
				}
			}
		}
		
	}
	
	private void buildSchedule() {
		//生成当天的任务执行map
		HashMap<Integer, String> tMap = new HashMap<Integer, String>();
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		int week = cal.get(Calendar.DAY_OF_WEEK);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		nDay = day;
		for(String s : taskList) {
			String str[] = s.split(" ");
			if(!str[3].equals("*") && !str[3].equals(month+1)) {
				continue;
			}
			if(!str[2].equals("*") && !str[2].equals(day)) {
				continue;
			}
			if(!str[4].equals("*") && !str[4].equals(week-1)) {
				continue;
			}
			//剩下的均是当天需要执行的任务
			//如果min和hour都是*
			if(str[0].equals("*") && str[1].equals("*")) {
				for(int i=0; i<24*60; i++) {
					int nowMin = i;
					String value = tMap.get(nowMin);
					if(value == null) {
						tMap.put(nowMin, s);
					} else {
						value = value+ "\t" + s;
					}
				}
				continue;
			}
			//如果min是*/n,hour为*
			if(str[0].matches("\\*/\\d+") && str[1].equals("*")) {
				int minS = Integer.parseInt(str[0].substring(2));
				for(int i=0; i<24*60; i++) {
					int nowMin = i;
					if(nowMin%minS == 0) {
						String value = tMap.get(nowMin);
						if(value == null) {
							tMap.put(nowMin, s);
						} else {
							value = value+ "\t" + s;
						}
					}
				}
				continue;
			}
			//如果min为*,hour为n
			if(str[0].equals("*") && str[1].matches("\\n+")) {
				int h = Integer.parseInt(str[1]);
				for(int i=h*60; i<(h+1)*60; i++) {
					int nowMin = i;
					String value = tMap.get(nowMin);
					if(value == null) {
						tMap.put(nowMin, s);
					} else {
						value = value+ "\t" + s;
					}
				}
				continue;
			}
			//如果min是*/n,hour为n
			if(str[0].matches("\\*/\\d+") && str[1].matches("\\n+")) {
				int h = Integer.parseInt(str[1]);
				int minS = Integer.parseInt(str[0].substring(2));
				for(int i=h*60; i<(h+1)*60; i++) {
					int nowMin = i;
					if(nowMin%minS == 0) {
						String value = tMap.get(nowMin);
						if(value == null) {
							tMap.put(nowMin, s);
						} else {
							value = value+ "\t" + s;
						}
					}
				}
				continue;
			}
			//如果min为n,hour为*
			if(str[0].matches("\\d+") && str[1].equals("*")) {
				int m = Integer.parseInt(str[0]);
				for(int i=0; i<24; i++) {
					int nowMin = i*60+m;
					String value = tMap.get(nowMin);
					if(value == null) {
						tMap.put(nowMin, s);
					} else {
						value = value+ "\t" + s;
					}
				}
				continue;
			}
			//如果min为n,hour为n
			if(str[0].matches("\\d+") && str[1].matches("\\d+")) {
				int m = Integer.parseInt(str[0]);
				int h = Integer.parseInt(str[1]);
				int nowMin = h*60+m;
				String value = tMap.get(nowMin);
				if(value == null) {
					tMap.put(nowMin, s);
				} else {
					value = value+ "\t" + s;
					tMap.put(nowMin, value);
				}
			}
		}
		map = tMap;
	}
	
	private String minToTime(int min) {
		int hh = min/60;
		int mm = min%60;
		String hhS = String.valueOf(hh);
		String mmS = String.valueOf(mm);
		if(hh<10)
			hhS = "0"+hhS;
		if(mm<10)
			mmS = "0"+mmS;
		return hhS+":"+mmS;
	}
	
	private String getExecuteScript(String line) {
		String str[] = line.split(" ");
		StringBuffer sb = new StringBuffer();
		for(int i=5; i<str.length; i++) {
			sb.append(str[i]+" ");
		}
		return sb.toString().trim();
	}
	
	private String getExecuteArgs(String line) {
		String str[] = line.split(" ");
		StringBuffer sb = new StringBuffer();
		if(str.length >= 7) {
			for(int i=6; i<str.length; i++) {
				sb.append(str[i]+" ");
			}
		}
		return sb.toString().trim();
	}
	
	public void run() {
		while(true) {
			long now = System.currentTimeMillis();
			Calendar cal = Calendar.getInstance();
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			int min = cal.get(Calendar.MINUTE);
			if(day != nDay) {
				reload();
			}
			int nowMin = hour*60+min;
			cat.info("[listenning] ["+minToTime(nowMin)+"]");
			System.err.println("[time] ["+DateUtil.formatDate(cal.getTime(),"yyyy.MM.dd")+" "+minToTime(nowMin)+"]");
			String values = map.get(nowMin);
			if(values != null) {
				String vals[] = values.split("\t");
				for(String task : vals) {
					String str[] = task.split(" ");
					String clazzName = str[5];
					String args = getExecuteArgs(task);
					String ags[] = args.split(" ");
					Executable clazz = null;
					try {
						clazz = (Executable)Class.forName(clazzName).newInstance();
						clazz.execute(ags);
						cat.info("[execute] [success] ["+minToTime(nowMin)+"] ["+clazzName+"] [args_len:"+ags.length+"] ["+args+"] ["+minToTime(nowMin)+"]");
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						cat.error("[execute] [  failed ] ["+minToTime(nowMin)+"] ["+clazzName+"] [args_len:"+ags.length+"] ["+args+"] :", e);
					} 
				}
			}
			//判断当前时间，如果已经跳入下一分钟，则不休息进行下次循环
			nowMin = cal.get(Calendar.MINUTE);
			if(nowMin == min) {
				int nowSecond = cal.get(Calendar.SECOND);
				long wait = (61-nowSecond)*1000;
				synchronized (this) {
					try {
						wait(wait);
					} catch (InterruptedException ee) {
					}
				}
			}
		}
	}
	
    public class SchedulerFileWatchdog extends FileWatchdog {
	    public void doOnChange(File file) {
	        try {
	            reload();
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
    }
	
}
