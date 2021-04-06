/**
 * 
 */
package com.fy.gamegateway.mieshi.server;

import java.io.Serializable;

import com.xuanzhi.tools.cache.Cacheable;


/**
 * 
 *
 */
public class MieshiSlientInfo implements Serializable {
	
	public String opuser;
	public long optime;
	public String servername;
	public String playername;
	public  long playerId;
	public int slienceHour;
	public int sliencelevel;
	public String reason;
	public String username;

}
