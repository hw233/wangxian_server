<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*,com.fy.engineserver.downcity.stat.*"%><%!
	boolean running = true;
	Thread ttttt = null;
	
	/**
	 * 每个检查点的数据
	 */
	LinkedList<Double> al = new LinkedList<Double>();
	/**
	 * 获取一个玩家的网速情况
	 */
	public int[] 获取网速信息(Player p,int start,int num){
		int count[] = new int[num];
		long now = System.currentTimeMillis();
		Iterator<Player.HEARTBEATCHECKREQ> it = p.__checkReqList.iterator();
		while(it.hasNext()){
			Player.HEARTBEATCHECKREQ r = it.next();
			int k = (int)((now - r.receiveTime)/5000);
			if(k >= start && k <  start + num){
				count[k-start]++;
			}else if(k >= start + num){
				break;
			}
		}
		return count;
	}
	
	public double 计算方差(int count[]){
		double dd = 0;
		for(int i = 0  ; i < count.length ; i++){
			dd += (count[i] - 1) * (count[i] - 1);
		}
		return Math.sqrt(dd);
	}
	
	public void 记录检查点(double d){
		al.addFirst(d);
		if(al.size() > 2048){
			al.removeLast();
		}
	}
	
	public void check(double ddLimit){
		PlayerManager sm = PlayerManager.getInstance();
		Player players[] = sm.getOnlinePlayers();
		if(players.length == 0){
			记录检查点(0);
		}
			
		int nums = 0;
		for(int i = 0 ; i < players.length ; i++){
			Player p = players[i];
			int start = 0;
			int num = 12;
			if(p.getCurrentGame() != null && p.__checkReqList.size() > start + num && System.currentTimeMillis() - p.getEnterGameTime() > 5000 * num){
				double dd = 计算方差(获取网速信息(p,start,num));
				if(dd >= ddLimit){
					nums ++;
				}
			}
		}
		记录检查点(1.0*nums/players.length);
	}
	
%><%

if(String.valueOf("true").equals(request.getParameter("startthread"))){
	if(ttttt != null){
		throw new Exception("检查线程已经启动，你需要先停止，才能再次启动！");
	}
	ttttt = new Thread(new Runnable(){
		public void run(){
			while(running){
				try{
					Thread.sleep(60000);
					check(2);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			ttttt = null;
		}
	},"网络状况检查线程，JSP启动");

	ttttt.start();
	
//	Thread ttttt1 = new Thread(DownCityScheduleManager.getInstance(),"副本统计线程");
//	ttttt1.start();

}else if(String.valueOf("true").equals(request.getParameter("stopthread"))){
	running = false;
	if(ttttt != null){
		ttttt.interrupt();
	}
}


%>
<%@page import="com.fy.engineserver.gateway.GameNetworkFramework"%>
<%@include file="../IPManager.jsp" %><html><head>
<!--[if IE]><script language="javascript" type="text/javascript" src="/game_server/js/flotr/lib/excanvas.js"></script><![endif]-->
<script language="javascript" type="text/javascript" src="/game_server/js/flotr/lib/prototype-1.6.0.2.js"></script>
<script language="javascript" type="text/javascript" src="/game_server/js/flotr/flotr-0.2.0-alpha.js"></script>
</HEAD>
<BODY onload='draw_Flotr();'>
<h2>网络状况检查</h2>
<a href='./stat_network.jsp' >刷新此页面</a>
<%
	if(ttttt != null){
		out.println("<h2>网络状况检查线程："+ttttt.getName()+",状态："+ttttt.isAlive()+"</h2>");
	}else{
		out.println("<h2>网络状况检查线程，还没有启动</h2>");
	}
%>
<form id='ff'><input type='hidden' name='startthread' value='true'><input type='submit' value='启动网络状况检查线程'></form>
<form id='fff'><input type='hidden' name='stopthread' value='true'><input type='submit' value='停止网络状况检查线程'></form>
<br/>
<br/>
<div id="container" style="width:800px;height:400px;"></div>
<%
//Execute this when the page's finished loading
	out.println("<script>\n");
	out.println("function draw_Flotr(){\n");
	out.println("var f;\n");
	out.println("f = Flotr.draw($('container'), [");

	out.println("{ // => first series");
	out.println("data: [");
	for(int i = 0 ; i < al.size() ; i++){
		double d = al.get(i);
		out.print("["+i+","+d+"]");
		if(i < al.size() -1){
			out.print(",");
		}
	}
	out.println("],");
	out.println("label: \"网络不稳定人数占比\",");
	out.println("lines: {show: true, fill: false},");
	out.println("points: {show: false}");
	out.println("}");
	
	out.println("]");
	
	//option
	out.println(",{");
//	out.println("xaxis:{");
//	out.println("noTicks: 7,");	// Display 7 ticks.
//	out.println("tickFormatter: function(n){ return '('+n+')'; },"); // => displays tick values between brackets.
//	out.println("min: 1,");		// => part of the series is not displayed.
//	out.println("max: 7.5");	// => part of the series is not displayed.
//	out.println("},");
	out.println("yaxis:{");
//	out.println("ticks: [[ 0, \"Lower\"], 0.25, 0.5, 0.75, [1.0, \"Upper\"]],");
	out.println("min: 0.0,");
	out.println("max: 1.0");
	out.println("}");
	out.println("}");

	out.println("); // =>");
	
	out.println("} // end of function");
	out.println("</script>\n");
%>
<br><br>
<h2>在线用户网络数据</h2>
<table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>
<tr align='center' bgcolor='#FFFFFF'>
<td>编号</td>
<td>ID</td>
<td>用户</td>
<td>角色</td>
<td>延迟</td>
<td>在线时长</td>
<td>方差</td>
<td>网络稳定数据</td>
</tr>
<%
PlayerManager sm = PlayerManager.getInstance();
Player players[] = sm.getOnlinePlayers();
for(int i = 0 ; i < players.length ; i++){
	Player p = players[i];
	int start = 0;
	int num = 12;
	int count[] = 获取网速信息(p,start,num);
	double dd = 计算方差(count);
	
	  Connection conn = p.getConn();
      GameNetworkFramework.FLOWSTAT f;
      String delay = "未知";
      if(conn != null){
  		f = (GameNetworkFramework.FLOWSTAT)conn.getAttachmentData(GameNetworkFramework.FLOWSTAT);
  		delay = "" + conn.getAttachmentData("network.delay");
      }else{
      	f = new GameNetworkFramework.FLOWSTAT();
      }
  	StringBuffer sb = new StringBuffer();
	for(int ii = 0 ; ii < count.length ; ii++){
		
		sb.append(count[ii]);
		if(ii < count.length -1){
			sb.append(",");
		}
	}
	
	String color = "#FFFFFF";
	if(dd>=2 && System.currentTimeMillis() - p.getEnterGameTime() > 60000){
		color = "#888888";
	}
      
	out.println("<tr align='center' bgcolor='"+color+"'><td>"+(i+1)+"</td>");
	out.println("<td><a href='../player/mod_player.jsp?action=select_playerId&playerid="+p.getId()+"'>"+p.getId()+"</a></td>");
	out.println("<td>"+p.getUsername()+"</td><td>"+p.getName()+"</td>");
	out.println("<td>"+delay+"</td>");
	out.println("<td>"+((System.currentTimeMillis() - f.startTime)/60000)+"分钟</td>");
	out.println("<td>"+dd+"</td>");
	out.println("<td>"+sb+"</td>");
	out.println("</tr>");
}
%>
</table>
</BODY></html>
