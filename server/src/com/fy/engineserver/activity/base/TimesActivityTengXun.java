package com.fy.engineserver.activity.base;

import java.util.Calendar;

import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.tengxun.TengXunDataManager;
import com.fy.engineserver.util.TimeTool;

public class TimesActivityTengXun extends TimesActivity {

	public TimesActivityTengXun(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
	}
	
	public boolean CanAdd(Player player) {
		if (this.isThisServerFit() == null) {
			if (TengXunDataManager.instance.getGameLevel(player.getId()) > 0) {
				return true;
			}
		}
		return false;
	}
	public static void main(String[] args) {

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -7);
		String defaultDate =  TimeTool.formatter.varChar10.format(calendar.getTimeInMillis());
		System.out.println(defaultDate);
	}
}
