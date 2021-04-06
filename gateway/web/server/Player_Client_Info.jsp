<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,java.lang.reflect.*,com.xuanzhi.tools.queue.DefaultQueue,
com.xuanzhi.tools.transport.*,com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%@page import="com.fy.gamegateway.stat.StatManager"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.fy.gamegateway.stat.Client"%>
<%@page import="com.fy.gamegateway.stat.ClientAccount"%>
<%

StatManager sm = StatManager.getInstance();
SimpleEntityManager<ClientAccount> em = sm.em4Account;
String username = request.getParameter("username");
List<ClientAccount> clientList = new ArrayList<ClientAccount>();
String cb = "toWrite";
MieshiServerInfoManager mm = MieshiServerInfoManager.getInstance();
MieshiServerInfoManager.DenyUser denyuser = mm.getDenyUser(username);
if(username != null && !username.trim().equals("")){
	clientList = em.query(ClientAccount.class,"username = '"+username+"' or clientId='"+username+"'","",1,1000);
}
	
%>

<%
if(clientList != null){
	
	for(ClientAccount client : clientList){
		if(client.isHasSuccessLogin()){
			System.out.println(""+client.getClientId());
			List<Client> list = null;
			if(list.size() > 0){
				Client client0 = list.get(0);
				String clientJson0 = JsonUtil.jsonFromObject(client0);
				String clientJson =  JsonUtil.jsonFromObject(client);
				//为了不重启网关，现在这样做了
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
				String registerTime = sdf.format(client.getRegisterTime());
				String firstLoginTime = sdf.format(client.getLoginTimeForFirst());
				String lastLoginTime = sdf.format(client.getLoginTimeForLast());
				boolean deviceid = false;
				boolean clientid = false;
				boolean limitlogin = false;
				boolean foreverlimit = false;
				if(denyuser!=null){
					deviceid = denyuser.denyDeviceId;
					clientid = denyuser.denyClientId;
					limitlogin = denyuser.enableDeny;
					foreverlimit = denyuser.foroverDeny;
				}
				
				
				if(!StringUtils.isEmpty(clientJson)&&!StringUtils.isEmpty(clientJson0)){
				%>	
				
				<%=cb%>(<%=clientJson%>,<%=clientJson0%>,"<%=registerTime%>","<%=firstLoginTime%>","<%=lastLoginTime%>","<%=deviceid%>","<%=clientid%>","<%=limitlogin%>","<%=foreverlimit%>");

				<%
				}
			}
		}
		
	}

}
%>
