package com.xuanzhi.tools.mem;

import java.util.HashMap;
import java.util.List;

/**
 *
 * 
 */
public interface CollectionClient {

	
	public String getClientName();
	
	/**
	 * 得到集合客户统计
	 * @return
	 */
	public List<ClientStat> getClientStats();
}
