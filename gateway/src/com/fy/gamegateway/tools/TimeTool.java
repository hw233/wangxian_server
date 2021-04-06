package com.fy.gamegateway.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public enum TimeTool {

	instance;

	public static final long oneSecond = 1000L;
	public static final long oneMinute = oneSecond * 60L;
	public static final long oneHour = oneMinute * 60L;
	public static final long oneDay = oneHour * 24L;
	public static final long oneWeek = oneDay * 7L;

	public static final long eightHours = 8 * oneHour;

	public enum TimeDistance {
		MILLISECOND(1L, "毫秒"),//毫秒
		SECOND(1000L, "秒"),//秒
		MINUTE(SECOND.getTimeMillis() * 60, "分钟"),
		HOUR(MINUTE.getTimeMillis() * 60, "小时"),
		DAY(HOUR.getTimeMillis() * 24, "天"),//天
		WEEK(DAY.getTimeMillis() * 7, "周"), ;

		private long timeMillis;
		private String name;

		private TimeDistance(long timeMillis, String name) {
			this.timeMillis = timeMillis;
			this.name = name;
		}

		public long getTimeMillis() {
			return timeMillis;
		}

		public void setTimeMillis(long timeMillis) {
			this.timeMillis = timeMillis;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	TimeTool() {

	}

	// /**
	// * 两个时间是否在同一个周期
	// * @param time1
	// * @param time2
	// * @param cycle
	// * @return
	// */
	// @Deprecated
	// private boolean isInSameCycle(long time1, long time2, long cycle) {
	// long group1 = time1 / cycle;
	// long group2 = time2 / cycle;
	// return group1 == group2;
	// }

	/**
	 * 是否是同一时间单位内<BR/>
	 * 是常识概念上的"内",如,同一分钟内,同一小时内,同一天内.等等[目前只支持到天]<BR/>
	 * 本方法力求不产生额外的对象,只通过数学计算解决
	 * @param timeBase
	 * @param timeCheck
	 * @param distance
	 * @return
	 */
	public boolean isSame(long timeBase, long timeCheck, TimeDistance distance) {
		return isSame(timeBase, timeCheck, distance, 1);
	}

	/**
	 * 是否是同一时间单位内<BR/>
	 * 是常识概念上的"内",如,同一分钟内,同一小时内,同一天内.等等[目前只支持到天]<BR/>
	 * 本方法力求不产生额外的对象,只通过数学计算解决
	 * @param timeBase
	 * @param timeCheck
	 * @param distance
	 * @param howManyCycles
	 * @return
	 */

	public boolean isSame(long timeBase, long timeCheck, TimeDistance distance, int howManyCycles) {
		if (howManyCycles <= 0) {
			throw new IllegalStateException("参数非法,howManyCycles=" + howManyCycles);
		}
		if (howManyCycles == 7) {					//判断是否在同一周内---为任务刷新做的。以后优化
			return isSameWeek(timeBase, timeCheck);
		}
		long distanceDiff = cycleDistance(timeBase, timeCheck, distance);
		return distanceDiff + 1 == howManyCycles;
	}
	/**
	 * 是否在同一周内
	 * @param timeBase
	 * @param timeCheck
	 * @return
	 */
	public boolean isSameWeek(long timeBase, long timeCheck) {
		Calendar car = Calendar.getInstance();
		car.setTime(new Date(timeBase));
		int a = car.get(Calendar.WEEK_OF_YEAR);
		int aa = car.get(Calendar.DAY_OF_WEEK);
		car.setTime(new Date(timeCheck));
		int b = car.get(Calendar.WEEK_OF_YEAR);
		int bb = car.get(Calendar.DAY_OF_WEEK);
		if (aa == 1) {		//周天
			a = a-1;
		}
		if (bb == 1) {
			b = b - 1;
		}
		return a==b;
	}

	/**
	 * 计算2个时间按照一定的时间间隔计算间隔数
	 * 第二个时间到第一个时间的时间间隔数
	 * @param timeBase
	 * @param timeCheck
	 * @param distance
	 * @return
	 */
	public long cycleDistance(long timeBase, long timeCheck, TimeDistance distance) {
		if (distance == TimeDistance.DAY) {
			// 是天的话.由于时间0表示1970年1月1日8时,以天为周期的话,都加上8小时做比较
			timeBase += eightHours;
			timeCheck += eightHours;
		}
		long group1 = timeBase / (distance.getTimeMillis());
		long group2 = timeCheck / (distance.getTimeMillis());
		return group2 - group1;
	}

	/**
	 * 获得显示的时间
	 * @param time
	 * @return
	 */
	public String getShowTime(long time) {
		StringBuffer sbf = new StringBuffer();
		for (int i = TimeDistance.values().length - 1; i >= 0; i--) {
			TimeDistance distance = TimeDistance.values()[i];
			if (distance.equals(TimeDistance.MILLISECOND)) {
				break;
			}
			long value = time / distance.getTimeMillis();
			if (value > 0) {
				sbf.append(value).append(distance.getName());
				time = time % distance.getTimeMillis();
			}
		}
		return sbf.toString();
	}

	/**
	 * 获得显示的时间
	 * @param time
	 * @param maxTimeDistance
	 *            显示的最大单位
	 * @return
	 */
	public String getShowTime(long time, TimeDistance maxTimeDistance) {
		StringBuffer sbf = new StringBuffer();
		int index = TimeDistance.values().length;
		for (int i = TimeDistance.values().length - 1; i >= 0; i--) {
			TimeDistance distance = TimeDistance.values()[i];
			if (maxTimeDistance == distance) {
				index = i;
				break;
			}
		}
		for (int i = index; i >= 0; i--) {
			TimeDistance distance = TimeDistance.values()[i];
			if (distance.equals(TimeDistance.MILLISECOND)) {
				break;
			}
			long value = time / distance.getTimeMillis();
			if (value > 0) {
				sbf.append(value).append(distance.getName());
				time = time % distance.getTimeMillis();
			}
		}
		return sbf.toString();
	}

	public enum formatter {
		varChar23(0, "varChar23", "时间类型:yyyy-MM-dd HH:mm;ss.SSS", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")),
		varChar19(1, "varChar19", "时间类型:yyyy-MM-dd HH:mm:ss", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")),
		varChar10(2, "varChar10", "时间类型:yyyy-MM-dd", new SimpleDateFormat("yyyy-MM-dd")),
		varChar7(3, "varChar7", "时间类型:yyyy-MM", new SimpleDateFormat("yyyy-MM"));

		formatter(int id, String name, String des, DateFormat df) {
			this.id = id;
			this.name = name;
			this.des = des;
			this.df = df;
		}

		public String getDes() {
			return des;
		}

		public int getId() {
			return id;
		}

		public String format(Date d) {
			return df.format(d);
		}

		public long parse(String time) {
			synchronized (df) {
				Calendar calendar = Calendar.getInstance();
				try {
					if (time != null && !"".equals(time)) {
						Date date = df.parse(time);
						calendar.setTime(date);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return calendar.getTimeInMillis();
			}
		}

		public String format(long time) {
			synchronized (df) {
				Date date = new Date(time);
				return df.format(date);
			}
		}

		public String getMeg() {
			return getDes();
		}

		public String getName() {
			return name;
		}

		private int id;
		private String name;
		private String des;
		DateFormat df;
	}

	public static void main(String[] args) {
		System.out.println("------------------------------------------------");
		System.out.println("------------------------------------------------");
	}
}
