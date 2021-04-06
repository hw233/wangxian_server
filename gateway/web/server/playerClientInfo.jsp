<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*,com.fy.gamegateway.stat.*"%><%

StatManager sm = StatManager.getInstance();
String username = request.getParameter("username");
if(username == null) username = "";
Client client = null;
List<ClientAccount> list = sm.em4Account.query(ClientAccount.class,"username='"+username+"'","loginTimeForLast desc",1,2);
if(list.size() > 0){
	ClientAccount ca = list.get(0);
	
	List<Client> al = sm.em4Client.query(Client.class,"clientId='"+ca.getClientId()+"'","",1,2);
	if(al.size() > 0){
		client = al.get(0);
	}
}

if(client != null){
	out.println("TRUE#"+client.getClientId()+"#"+client.getUuid()+"#"+client.getDeviceId()+"#"+client.getPhoneType()+"#"+client.getNetworkTypeOfLastConnected()+"#"+client.getClientProgramVersionOfLastConnected());
}else{
	out.println("FALSE#######");
}
	
%>
