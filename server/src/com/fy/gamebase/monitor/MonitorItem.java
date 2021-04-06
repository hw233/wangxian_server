package com.fy.gamebase.monitor;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

/**
 * 服务器监控条目；游戏活动的基本接口。
 * 
 * create on 2013年7月29日
 */
public class MonitorItem {
	public String className;
	public String desc;
	public boolean open;
	public List<MILink> links;
	public MILinkProc proc;
	
	public MILink addLink(String href, String showText){
		if(links == null){
			links = new LinkedList<MILink>();
		}
		MILink ret = new MILink(href, showText);
		ret.proc = proc;
		links.add(ret);
		return ret;
	}
	
	public void init(){
		
	}
	
	public void forceOpen(){
		
	}
	
	public void forceClose(){
		
	}
	
	/**
	 * 重新载入配置文件，主要目的在于更新数据时避免重新启动服务器。
	 */
	public void loadConf(){
		
	}
	
	public void destory(){
		
	}
	
	/**
	 * 用于向jsp页面输出内存中的信息。
	 * @param out
	 * @throws IOException
	 */
	public void dump(Writer out) throws IOException{
		
	}
}
