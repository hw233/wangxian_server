package com.fy.engineserver.homestead.cave;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fy.engineserver.gametime.SystemTime;

/**
 * 庄园动态
 * @author Administrator
 *         2014-5-13
 * 
 */
public class CaveDynamic {

	// 所有动态类型
	public enum Dynamic {
		开门,
		关门,
		偷菜,
		被炸,
		建筑升级完成,
		果实成熟,
		魯班令到期,
		扣除维护费用,
		仙府锁定,
		仙府降级,
		资源等级提升,
		;
	}
	public CaveDynamic() {
		// TODO Auto-generated constructor stub
	}
	public static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");

	private long time;
	private String message;

	private String causeBy;

	private Dynamic dynamic;

	public CaveDynamic(String causeBy, Dynamic dynamic, String message) {
		super();
		this.time = SystemTime.currentTimeMillis();
		this.message = message;
		this.causeBy = causeBy;
		this.dynamic = dynamic;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCauseBy() {
		return causeBy;
	}

	public void setCauseBy(String causeBy) {
		this.causeBy = causeBy;
	}

	public void setDynamic(Dynamic dynamic) {
		this.dynamic = dynamic;
	}

	/**
	 * 获得动态的描述
	 * @return
	 */
	public String getDynamic() {
		return sdf.format(new Date(time)) + " " + message;
	}

	@Override
	public String toString() {
		return "CaveDynamic [time=" + time + ", message=" + message + "]";
	}
}
