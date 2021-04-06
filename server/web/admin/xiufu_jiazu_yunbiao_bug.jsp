<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*
,com.xuanzhi.tools.simplejpa.*,com.xuanzhi.tools.simplejpa.impl.*,com.xuanzhi.tools.page.PageUtil,
com.fy.engineserver.gateway.* "%><%
	
	GameNetworkFramework gf = GameNetworkFramework.getInstance();	
	java.lang.reflect.Field f = GameNetworkFramework.class.getDeclaredField("message2SubSysMap");
	f.setAccessible(true);
	HashMap<String, GameSubSystem[]> message2SubSysMap = (HashMap<String, GameSubSystem[]>)f.get(gf);

	String action = request.getParameter("action");
	if(action != null && action.equals("delete")){
		String key = request.getParameter("key");
		
		message2SubSysMap.remove(key);
		
		out.println("<h2>协议"+key+"被从底层协议MAP中删除，此协议将不再被处理!</h2>");
		System.out.println("<h2>协议"+key+"被从底层协议MAP中删除，此协议将不再被处理!</h2>");
	}
%>
<html><head>
</HEAD>
<BODY>
<h2>修复家族运镖的bug，不要轻易使用此功能</h2>

<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>编号</td>
<td>协议</td>
<td>子系统</td>
<td>删除</td>
</tr>

<%
	String keys[] = message2SubSysMap.keySet().toArray(new String[0]);

        for(int i = 0 ; i < keys.length       ; i++){
        	 GameSubSystem[] gss = message2SubSysMap.get(keys[i]);
        	 StringBuffer sb = new StringBuffer();
        	 for(int j = 0 ; gss != null && j < gss.length ; j++){
        		 sb.append(gss[j].getName());
        		 if(j < gss.length -1){
        			 sb.append(",");
        		 }
        	 }
        	 
                %><tr bgcolor="#FFFFFF" align="center">
                <td><%=i+1 %></td>
                <td><%=  keys[i] %></td>
                <td><%= sb.toString() %></td>
              <% 
              	if(keys[i].equals("JIAZU_MASTER_RESIGN_REQ") ||keys[i].equals("JIAZU_APPOINT_REQ")){
              %>
                <td><a href='./xiufu_jiazu_yunbiao_bug.jsp?action=delete&key=<%=keys[i]%>'>从系统中删除，此操作将导致此协议不再被处理</a></td>
               <%
              	}else{
              		out.println("<td>----</td>");	
              	}
               %> 
                </tr><%
        }
%>
</table>
</BODY></html>
