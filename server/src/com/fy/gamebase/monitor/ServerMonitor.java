package com.fy.gamebase.monitor;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.xuanzhi.boss.game.GameConstants;

public class ServerMonitor {
	static ServerMonitor inst;
	public List<MonitorItem> map;
	public static boolean close = true;
	public static ServerMonitor getInst(){
		if(inst == null){
			new ServerMonitor();
		}
		return inst;
	}
	public ServerMonitor(){
		inst = this;
		map = new LinkedList<MonitorItem>();
	}
	public void dumpServerInfo(Writer w) throws IOException{
		if (close) {
			return;
		}
		GameConstants gs = GameConstants.getInstance();
		if(gs != null){
			w.append("服务器名称:");		w.append(gs.getServerName());		w.append("<br/>");
			w.append("平台:");		w.append(gs.getPlatForm());		w.append("<br/>");
		}
		//
		w.append("注册的信息:<br/>");
		Iterator<MonitorItem> it = map.iterator();
		w.append("<table border='1'>");
		w.append("<tr>");
		td(w, "类名");	td(w, "说明");  
		td(w, "操作");
		//td(w, "总体状态");
		w.append("</tr>");
		while(it.hasNext()){
			MonitorItem mi = it.next();
			w.append("<tr>");
			td(w,mi.className);
			td(w,mi.desc);
			w.append("<td>");
			genLinks(w, mi);
			w.append("</td>");
//			td(w,mi.open ? "开启" : "关闭");
			w.append("</tr>");
		}
		w.append("</table>");
	}
	public void genLinks(Writer w, MonitorItem mi) throws IOException {
		List<MILink> list = mi.links;
		if(list == null){
			w.append("无操作链接");
			return;
		}
		w.append("|");
		int len = list.size();
		for(int i=0; i<len; i++){
			MILink ln = list.get(i);
			w.append("<a href='monitorProc.jsp?mappingKey2link=");
			w.append(String.valueOf(ln.id));
			w.append("&");			w.append(ln.href);	w.append("'>");
			w.append(ln.showText);
			w.append("</a>");
			w.append("|");
		}
	}
	private void td(Writer w, String str) throws IOException {
		w.append("<td>");
		w.append(str);
		w.append("</td>");
	}
	/**
	 * @param o
	 * @param desc
	 * @param open
	 * @return null if o is null
	 */
	public MonitorItem addInfo(MILinkProc o, String desc, boolean open){
		if( o == null){
			return null;
		}
		MonitorItem mi = new MonitorItem();
		mi.className = o.getClass().getSimpleName();
		mi.desc = desc;
		mi.open = open;
		mi.proc = o;
		map.add(mi);
		return mi;
	}
	
	public void doRequest(Map<String, String[]> params, Writer out) throws IOException{
		String[] o = params.get("mappingKey2link");
		if(o == null){
			out.append("mappingKey2link miss");
			return;
		}
		Integer i = null;
		try{
			i = Integer.valueOf(o[0]);
		}catch(NumberFormatException e){
			out.append("Exception e"+e);
			return;
		}
		MILink link = MILink.map.get(i);
		if(link == null){
			out.append("link not found, id "+i);
			return;
		}
		MILinkProc proc = link.proc;
		if(proc == null){
			out.append("proc is null, id "+i);
			return;
		}
		proc.procClick(link, params, out);
		out.append("<br/>处理完毕");
	}
}
