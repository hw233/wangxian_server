<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.xuanzhi.tools.text.StringUtil"%><%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@page import="com.fy.engineserver.sprite.monster.MonsterManager"%>
<%@page import="com.fy.engineserver.sprite.monster.MemoryMonsterManager.MonsterTempalte"%>
<%@page import="com.fy.engineserver.activity.flushmonster.FlushMonsterInGuoQingManager"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.sprite.monster.FlopSet"%>
<%@page import="com.fy.engineserver.flop.FlopManager"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.transport.Connection"%>
<%@page import="com.fy.engineserver.message.USER_CLIENT_INFO_REQ"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>检查多开</title>
<%
FlopManager fm = FlopManager.getInstance();

String flag = request.getParameter("action");
if(flag != null){
	if(flag.equals("1")){
		FlopManager.IP_LIMIT_FLAG = false;
	}else{
		FlopManager.IP_LIMIT_FLAG = true;
	}
}

try{
	fm.设备号Map.remove("");
}catch(Exception ex){
	
}

Hashtable<String,ArrayList<String>> 设备号组Map = fm.设备号组Map;
PlayerManager pm = PlayerManager.getInstance();
Player[] ps = pm.getOnlinePlayers();
%>

<body>
<form>
<input type="hidden" name="action" value="<%=(FlopManager.IP_LIMIT_FLAG==false?"0":"1") %>">
<input type="submit" value="<%=(FlopManager.IP_LIMIT_FLAG==false?"开启在线扫描":"关闭在线扫描") %>">
</form>
<table>
<tr><td>组名</td><td>组成员设备ID</td><td>设备型号</td><td>IP</td><td>IsExistOtherServer</td><td>IsStartServerBindFail</td><td>IsStartServerSucess</td><td>账号</td><td>角色</td><td>级别</td><td>地图</td></tr>

<%
int count = 0;
int tempCount = 0;
for(String key : 设备号组Map.keySet()){
	ArrayList<String> list = 设备号组Map.get(key);

	if(tempCount != count){
		tempCount = count;
		%>
		<tr><td colspan="100">分隔符------------------</td></tr>
		<%
	}
		for(String deviceId : list){
			if(list.size() > 3){
				count++;
				boolean has = false;
				for(Player p : ps){
					Connection conn = p.getConn();
					if(conn != null){
						Object o = conn.getAttachmentData("USER_CLIENT_INFO_REQ");
						if(o instanceof USER_CLIENT_INFO_REQ){
							USER_CLIENT_INFO_REQ req = (USER_CLIENT_INFO_REQ)o;
							if(deviceId.equals(req.getDEVICEID())){
								%>
								<tr><td><%=key %></td><td><%=deviceId %></td><td><%=req.getPhoneType() %></td><td><%=conn.getIdentity() %></td><td><%=req.getIsExistOtherServer() %></td><td><%=req.getIsStartServerBindFail() %></td><td><%=req.getIsStartServerSucess() %></td><td><%=p.getUsername() %></td><td><%=p.getName() %></td><td><%=p.getLevel() %></td><td><%=p.getGame() %></td></tr>
								
								<%
								has = true;
								break;
							}
						}
					}
				}
				if(!has){
					%>
					<tr><td><%=key %></td><td><%=deviceId %></td><td>--</td><td>--</td><td>--</td><td>--</td><td>--</td><td>--</td><td>--</td><td>--</td><td>--</td></tr>
					<%
				}
			}

		}


}
 %>
 </table>
</body>
</html>
