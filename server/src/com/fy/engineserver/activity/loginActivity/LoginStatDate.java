package com.fy.engineserver.activity.loginActivity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class LoginStatDate implements Serializable {

	public long lasttime;
	
	public int value;

	/**维护活动的领取状态**/
	public List<String> hashGets;
	
	public long getLasttime() {
		return lasttime;
	}

	public void setLasttime(long lasttime) {
		this.lasttime = lasttime;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public List<String> getHashGets() {
		return hashGets;
	}

	public void setHashGets(List<String> hashGets) {
		this.hashGets = hashGets;
	}

	@Override
	public String toString() {
		return "LoginStatDate [lasttime=" + lasttime + ", value=" + value
				+ ", hashGets=" + hashGets + "]";
	}
	
}
