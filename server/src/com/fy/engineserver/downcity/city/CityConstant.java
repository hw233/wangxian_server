/**
 * 
 */
package com.fy.engineserver.downcity.city;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.downcity.DownCityManager;

/**
 * @author Administrator
 *
 */
public class CityConstant {

	public static long LAST_TIME_OF_WUDI = 30 * 60 * 1000L;
	public static int MAX_TASK_NUMS_OF_WUDI = 10;
	public static boolean START_RUN_OF_WUDI = true;
	public static long SLEEP_TIME_OF_WUDI = 200L;
	public static long PRINT_LOGGER_TIMES = 10000L;
	public static long WUDI_TIME_LENGTH = 60 * 1000L;
	public	static Logger logger = LoggerFactory.getLogger(DownCityManager.class);
	
	
	private static int cityId;
	public synchronized static int nextId(){
		return cityId++;
	}
	
}
