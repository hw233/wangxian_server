package com.fy.engineserver.newBillboard.date.activity;

import com.fy.engineserver.activity.doomsday.DoomsdayManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;

public class MoshiCountryBillboard extends Billboard {

	//末世国家排行
	
	public void update()throws Exception {
		long[][] countryBillboard = DoomsdayManager.getInstance().getCountryBillboard();
		if (countryBillboard != null) {
			BillboardDate[] bbds = new BillboardDate[countryBillboard.length];
			for (int i = 0; i < bbds.length; i++) {
				BillboardDate date = new BillboardDate();
				date.setDateId(countryBillboard[i][0]);
				date.setType(BillboardDate.其他);
				
				String[] values = new String[2];
				values[0] = CountryManager.得到国家名((int)countryBillboard[i][0]);
				values[1] = ""+countryBillboard[i][1];
				date.setDateValues(values);
				bbds[i] = date;
			}
			setDates(bbds);
		}
	}
}
