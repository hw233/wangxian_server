package com.sqage.stat.commonstat.dao;

import java.util.ArrayList;
import com.sqage.stat.commonstat.entity.YinZiKuCun;

public interface YinZiKuCunDao {

	
	public ArrayList<YinZiKuCun> getBySql(String sql);
	//public YinZiKuCun add(YinZiKuCun yinZiKuCun);
	public void addList(ArrayList<YinZiKuCun> yinZiKuCunList);
}
