package com.sqage.stat.commonstat.manager;

import java.util.ArrayList;

import com.sqage.stat.commonstat.entity.YinZiKuCun;

public interface YinZiKuCunManager {

	public ArrayList<YinZiKuCun> getBySql(String sql);
	public void addList(ArrayList<YinZiKuCun> yinZiKuCunList);


}
