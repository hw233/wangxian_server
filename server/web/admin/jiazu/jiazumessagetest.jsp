<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.*"%>
<%@page import="com.fy.engineserver.core.*"%>
<%@page import="com.fy.engineserver.jiazu.dao.*"%>
<%@page import="com.fy.engineserver.message.*"%>
<%@page import="java.lang.reflect.*"%>
<%@page import="java.nio.*"%>
<%@page import="java.nio.channels.*"%>
<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.util.*"%>
<%@page import="com.xuanzhi.tools.transport.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	function changevalue(value) {
		var t = document.getElementById("messagetype");
		t.value = value;
		var form1 = document.getElementById("form1");
		form1.submit();
	}

	function sendmessage() {
		var t = document.getElementById("messagetype");
		var t2 = document.getElementById("messageselect");
		t.value = t2.value;
		var t = document.getElementById("flag");
		t.value = 1;
		var form1 = document.getElementById("form1");
		form1.submit();
	}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<%
	String message = null;
	Jiazu jiazu = null;
	 String logmessage=null;
	 String type=null;
	 String logfile=null;
	 String classname=null;
	 String resmessage=null;
	PlayerManager playerManager = PlayerManager.getInstance();
	JiazuSubSystem jiazuSubsystem=JiazuSubSystem.getInstance();

	List<String>list=new ArrayList<String>();

for(String sr:jiazuSubsystem.getInterestedMessageTypes())
{
list.add(sr);
}
	logfile=request.getParameter("logfile");
	if(logfile==null||logfile.trim().length()==0)
	{
		logfile="/home/game/resin/log/game_server/jiazuSubSystem.log";
	}
	StringBuffer buf=new StringBuffer("<table border='1' ><tr><td>属性名</td><td>类型</td><td>值</td></tr>");
	Player player = null;
	try {
		String playername = request.getParameter("playername");
		if (playername == null || playername.length() == 0)
			playername = "dape";
		player=playerManager.getPlayer(playername);
		JiazuManager manager = JiazuManager.getInstance();
	
		type=request.getParameter("messagetype");
		if(type==null||type.length()==0||type.trim().equals("null"))
		{
			type="JIAZU_CREATE_REQ";
		}
	//	System.out.println("message type is **"+type+"**");
		
		try {
		classname="com.fy.engineserver.message."+type;
		 Class cls =
		 Class.forName(classname);
		 Field field[] = cls.getDeclaredFields();
		 int sum = 0;
		
		 for (Field f : field) {
		 int d = f.getModifiers();
		 if (Modifier.isStatic(d) || Modifier.isPrivate(d)||f.getName().equals("seqNum")) {
		 continue;
		 } else {
		 buf.append("<tr><td>"+f.getName()+"</td>").append("<td>"+f.getType()+"</td>").append("<td><input type='text' name='"+f.getName()+"'></td></tr>");
		// System.out.println(f.getName() + "\t" + f.getType());
		 sum++;
		 }
		 }
		 
		 } catch (ClassNotFoundException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
		
		 String flag=request.getParameter("flag");
		 if(flag!=null&&flag.trim().length()>0&&flag.equals("1"))
		 {
		 StringBuffer messagebuf=new StringBuffer();
		 FileChannel   fc   =   new   FileInputStream(logfile).getChannel(); 
		 long size=fc.size();
		 
		 Map<String,String[]>map=(Map<String,String[]>)(request.getParameterMap());
		 
		RequestMessage req=(RequestMessage)(MessageReflectHelper.createNewInstance(classname,map));
		  for(Map.Entry<String,String[]>entry:map.entrySet())
		 {
		 	String key=entry.getKey();
		 	if(key.equals("flag")||key.equals("messagetype")){
		 		continue;
		 	}
		 	else
		 	{
		 		//System.out.println(key+"\t");
		 		if(entry.getValue().length>=1)
		 		{
		 			System.out.println(entry.getValue()[0]);
		 		}
		 }
		 }
		 
		// JIAZU_CREATE_REQ res = new JIAZU_CREATE_REQ(GameMessageFactory.nextSequnceNum(), "明教31");
		Class clazz = jiazuSubsystem.getClass();
		Method m1 = clazz.getDeclaredMethod("handle_" + type, Connection.class, RequestMessage.class, Player.class);
		try{
		ResponseMessage res=(ResponseMessage) m1.invoke(jiazuSubsystem, null, req, player);
		Thread.sleep(1000);
		if(res!=null)
		{
		  resmessage=MessageReflectHelper.getMessageInfo(res);
		}
		}catch(Exception e)
		{
			e.printStackTrace();
			message=e.getMessage();
		}
		
		//  jiazuSubsystem.handle_JIAZU_CREATE_REQ(null,req,player);
		//	Class clazz = jiazuSubsystem.getClass();
	//	Thread.sleep(1000);
			ByteBuffer buffer = ByteBuffer.allocate(480);
			int readbyte=0;
			int sumread=0;
		//	long currsize=fc.size();
			while((readbyte=fc.read(buffer,size+sumread))!=-1){
				sumread+=readbyte;
				buffer.flip();
				byte t2[]=buffer.array();
			//	System.out.println(" readbyte "+readbyte+" left="+(currsize-size-sumread));
				messagebuf.append(new String(t2,0,readbyte));
			//	if(currsize>=size+sumread)
				//	break;
				buffer.clear();
			}
			
			fc.close();
			if(messagebuf.length()>0)
			{
				logmessage=messagebuf.toString();
				logmessage=logmessage.replaceAll("[\r\n]{1,}","<p>");
				
			}
		 }
		 
		//Method m1 = clazz.getDeclaredMethod("handle_" + message.getTypeDescription(), Connection.class, RequestMessage.class, Player.class);
		//return (ResponseMessage) m1.invoke(this, conn, message, player);
	}	catch (Exception e) {
			e.printStackTrace();
			message = e.getMessage();
			
		}
	 buf.append("</table>");
%>
<body>
	<h1 align="center">家族消息测试</h1>
	<form method="post" id="form1">

		<input type="hidden" name="messagetype" id="messagetype"></input> <input
			type="hidden" name="flag" id="flag" value="0"></input>
		<table border="1" align="center" width="60%">
			<tr>
				<td>消息名称</td>
				<td>消息格式</td>
				<td></td>
			</tr>
			<tr>
				<td><select onchange="changevalue(this.value)"
					id="messageselect">
						<% for(String str:list) 
					{ %>
						<option value="<%out.print(str); %>"
							<% if(str.equals(type)) out.print("selected");%>><%=str %></option>
						<%} %>

				</select>
				</td>
				<td><div id='messagediv'><%=buf.toString() %></div>
				</td>
				<td><button type="button" onclick="sendmessage()">发送消息</button>
				</td>
			</tr>
		</table>
		<div align="center">
			日志文件名:<select name="logfile">
			<option value="/home/game/resin/log/game_server/jiazuSubSystem.log" selected>jiazuSubSystem.log</option>
			<option value="/home/game/resin/log/stderr.log">stderr.log</option>
			<option value="/home/game/resin/log/stderr.log">stdout.log</option>
			<option value="/home/game/resin/log/game_server/objectDbJiazuDao.log">objectDbJiazuDao.log</option>
			</select>&nbsp;玩家角色名:<input type="text" name="playername"></input>
		</div>
	</form>
	<hr>
	<h2 align="center">回复</h2>
	<% if(resmessage!=null) {out.print(resmessage);}else{out.print("<p align=\"center\">无消息回复！</p>");}%>

	<hr>
	<h2 align="center">日志消息</h2>
	<div align="center">
		<% if(logmessage!=null){%>

		<% 
		out.println(logmessage); %>
		<%} %>
	</div>

</body>
</html>