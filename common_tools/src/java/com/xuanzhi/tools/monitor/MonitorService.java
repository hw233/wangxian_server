package com.xuanzhi.tools.monitor;

import java.net.URL;
import java.util.Map;

/**
 * 监控服务
 * 
 * 此类包含了某一个监控服务的所有配置，包括：
 * 1. 获取监控数据的URL
 * 2. 获取监控数据的时间间隔
 * 3. 报警的时间间隔
 * 4. 最低报警的级别
 * 5. 系统恢复时最高通知的级别
 * 
 * 通知，此服务还包含：
 * 1. 状态级别变迁的函数实现，此函数遵循下面的原则：
 *    第n个监控点（MonitorPoint）的状态级别=f(第n-1个监控点的数据,第n-1个监控点的状态，第n个监控点的数据)
 * 2. 报警函数的实施，比如发送邮件或者短信
 * 
 *
 */
public interface MonitorService {

	public String getName();
	
	public URL getDataUrl();
	
	public long getMonitorInterval();
	
	public long getAlertInterval();
	
	public int getMinAlertLevel();
	
	public int getMaxNotifyLevel();
	
	public int calculate(MonitorData dataOfLastPoint,int levelOfLastPoint,MonitorData dataOfCurrentPoint);
	
	public void alert(MonitorPoint p,long lastingTime);
	
	public void notify(MonitorPoint p,long lastingTime);
	
	public String getDescription();
	
	/**
	 * 返回需要生成图表的属性名称
	 * 就是对应每个MonitorData中的property名称，这个名称为Key，value是显示在图表上的名称。
	 * 比如：监控外网上行，返回的map可以是：
	 * 2000_hunan.918047.mosize=2000河南接入点
	 * 8228_shanghai.909143.mosize=8228上海接入点
	 * 
	 * @return 返回null表示不生成图表
	 */
	public Map<String,String> getChartSeriesPropertyName();
	
	public String getChartRangName();
}
