<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.enterlimit.*,com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*,com.fy.engineserver.gateway.*,
com.fy.engineserver.message.*"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%><%
        PlayerManager sm = PlayerManager.getInstance();
     
CoreSubSystem core = CoreSubSystem.getInstance();

boolean checkIP = "true".equals(request.getParameter("checkip"));
boolean fengduokai = "true".equals(request.getParameter("fengduokai"));
boolean kickflag = "true".equals(request.getParameter("kickflag"));
boolean listdajing = "true".equals(request.getParameter("listdajing"));
boolean findduokai = "true".equals(request.getParameter("findduokai"));
boolean kickfindduokai = "true".equals(request.getParameter("kickfindduokai"));

String url = "http://"+Game.网关地址+":8882/game_gateway/player2client.jsp?username=";
		
Date date = new Date(org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getStartupDate());
ArrayList<String> duokaiMessage = new ArrayList<String>();

Player sprites[] = sm.getOnlinePlayers();
Arrays.sort(sprites,new Comparator<Player>(){
	public int compare(Player p1,Player p2){
		if(p1.getLevel() > p2.getLevel()) return -1;
		if(p1.getLevel() < p2.getLevel()) return 1;
		if(p1.getBindSilver() > p2.getBindSilver()) return -1;
		if(p1.getBindSilver() < p2.getBindSilver()) return 1;
		return 0;
	}
});



if(kickflag){
	String kickusers[] = request.getParameterValues("kickusers");
	String action = request.getParameter("action");
	if(action != null && action.equals("process")) {
		checkIP = true;
		HashMap<String, Integer> pmap = new HashMap<String, Integer>();
		StringBuffer sb = new StringBuffer();
		if(kickusers != null && kickusers.length > 0) {
			for(String s : kickusers) {
				long pid = -1;
				for(int i = 0 ; i < sprites.length ; i++){
					Player p = sprites[i];
					if(p.getUsername().equals(s)) {
						pid = p.getId();
						break;
					}
				}
				Player_Process pp = EnterLimitManager.player_process.get(pid);
				if(pp != null) {
					String ps[] = pp.getAndroidProcesss();
					for(String p : ps) {
						Integer n = pmap.get(p);
						if(n == null) {
							n = 1;
						} else {
							n += 1;
						}
						pmap.put(p, n);
						if(n == kickusers.length) {
							sb.append(p + ";");
						}
					}
				}
			}
			
			out.println(kickusers.length + "个账号中公共的进程:<br>"+sb.toString().replaceAll(";","<br>"));
		}
	} else if(action != null && action.equals("murder")) {
		checkIP = true;
		HashMap<String, Integer> pmap = new HashMap<String, Integer>();
		StringBuffer sb = new StringBuffer();
		if(kickusers != null && kickusers.length > 0) {
			for(String s : kickusers) {
				long pid = -1;
				for(int i = 0 ; i < sprites.length ; i++){
					Player p = sprites[i];
					if(p.getUsername().equals(s)) {
						pid = p.getId();
						break;
					}
				}
				Player_Process pp = EnterLimitManager.player_process.get(pid);
				if(pp != null) {
					String ps[] = pp.getAndroidProcesss();
					for(String p : ps) {
						Integer n = pmap.get(p);
						if(n == null) {
							n = 1;
						} else {
							n += 1;
						}
						pmap.put(p, n);
						if(n == kickusers.length) {
							sb.append(p + ";");
						}
					}
				}
			}
			
			out.println(kickusers.length + "个账号中公共的进程:<br>"+sb.toString().replaceAll(";","<br>"));
		}
	} else {
		for(int i = 0 ; i < sprites.length ; i++){
			Player p = sprites[i];
			boolean b = false;
			for(int j = 0 ; j < kickusers.length ; j++){
				if(p.getUsername().equals(kickusers[j])){
					b = true;
				}
			}
			if(b){
				Connection conn = p.getConn();
				USER_CLIENT_INFO_REQ o = null;
				if(conn != null)
					o = (USER_CLIENT_INFO_REQ)conn.getAttachmentData("USER_CLIENT_INFO_REQ");
				
				String deviceId = "";
				String phoneType = "";
				boolean isExistOtherServer = false;
				boolean isStartServerBindFail = false;
				
				if(o != null){
					 isExistOtherServer = o.getIsExistOtherServer();
		       		 isStartServerBindFail = o.getIsStartServerBindFail();
		       		 phoneType = o.getPhoneType();
		      		 deviceId = o.getMACADDRESS();
				}
				
				p.getConn().close();
				Game.logger.warn("[涉嫌打金] [踢下线] ["+conn.getIdentity()+"] [deviceId:"+deviceId+"] [phoneType:"+phoneType+"] [isExistOtherServer:"+isExistOtherServer+"] [isStartServerBindFail:"+isStartServerBindFail+"] "+ p.getLogString());
				duokaiMessage.add("[涉嫌打金] [踢下线] ["+conn.getIdentity()+"] [deviceId:"+deviceId+"] [phoneType:"+phoneType+"] [isExistOtherServer:"+isExistOtherServer+"] [isStartServerBindFail:"+isStartServerBindFail+"] "+ p.getLogString());
			}
		}
	}
}

if(kickfindduokai){
	String kickusers[] = request.getParameterValues("kickusers");
	for(int i = 0 ; i < sprites.length ; i++){
		Player p = sprites[i];
		boolean b = false;
		for(int j = 0 ; j < kickusers.length ; j++){
			if(p.getUsername().equals(kickusers[j])){
				b = true;
			}
		}
		if(b){
			Connection conn = p.getConn();
			
			String clientId = "";
			String uuid = "";
			String deviceId = "";
			String phoneType = "";
			String network = "";
			String version = "";
			
			try{
				byte bytes[] = HttpUtils.webGet(new java.net.URL(url+ sprites[i].getUsername()),null,1000,3000);
				String s = new String(bytes);
				String ss[] = s.split("#");
				if(ss[0].equals("TRUE")){
					clientId = ss[1];
					 uuid = ss[2];
					 deviceId = ss[3];
					 phoneType = ss[4];
					 network = ss[5];
					 version = ss[6];
					 if(uuid == null || uuid.equals("null")) uuid = "";
					 if(deviceId == null|| deviceId.equals("null")) deviceId = "";
				}
			}catch(Exception e){}
			
			boolean isExistOtherServer = false;
			boolean isStartServerBindFail = false;
			
			Game.logger.warn("[涉嫌修改客户端版本] [修改版本封号一周] ["+conn.getIdentity()+"] [clientId:"+clientId+"] [deviceId:"+deviceId+"] [phoneType:"+phoneType+"] [isExistOtherServer:"+isExistOtherServer+"] [isStartServerBindFail:"+isStartServerBindFail+"] "+ p.getLogString());
			
			if(clientId.length() > 0){
				DENY_USER_REQ req = new DENY_USER_REQ(GameMessageFactory.nextSequnceNum(), 
						clientId,p.getUsername(),"角色["+p.getName()+"]在["+com.xuanzhi.boss.game.GameConstants.getInstance().getServerName()+"]服涉嫌修改客户端版本号并使用多开外挂",
						com.xuanzhi.boss.game.GameConstants.getInstance().getServerName()+"-后台手动封号",
						true,true,false,7,0);
				MieshiGatewayClientService.getInstance().sendMessageToGateway(req);
			}
			if(p.getConn() != null) 
				p.getConn().close();
		
			duokaiMessage.add("[涉嫌修改客户端版本] [修改版本封号一周] ["+conn.getIdentity()+"] [clientId:"+clientId+"] [deviceId:"+deviceId+"] [phoneType:"+phoneType+"] [isExistOtherServer:"+isExistOtherServer+"] [isStartServerBindFail:"+isStartServerBindFail+"] "+ p.getLogString());
		}
			
	}
}

//检查多开
if(fengduokai){
	//同一IP+同一设备号+同一机型 --》多个玩家，如果有一个绑定成功，其他都绑定失败
	HashMap<String,ArrayList<Player>> duokaiMap = new HashMap<String,ArrayList<Player>>();
	for(int i = 0 ; i < sprites.length ; i++){
		Connection conn = sprites[i].getConn();
		USER_CLIENT_INFO_REQ o = null;
		if(conn != null)
			o = (USER_CLIENT_INFO_REQ)conn.getAttachmentData("USER_CLIENT_INFO_REQ");
		
		String deviceId = "";
		String phoneType = "";
		boolean isExistOtherServer = false;
		boolean isStartServerBindFail = false;
		String ip = "";
		if(conn != null) {
			String s = conn.getIdentity();
			ip = s.split(":")[0];
		}
		if(o != null){
			 isExistOtherServer = o.getIsExistOtherServer();
     		 isStartServerBindFail = o.getIsStartServerBindFail();
     		 phoneType = o.getPhoneType();
    		 deviceId = o.getMACADDRESS();
		}
		
		if(ip.length() > 0 && deviceId.length() > 0 && phoneType.length() > 0){
			String key = ip +"#"+ deviceId +"#"+ phoneType;
			ArrayList<Player> al = duokaiMap.get(key);
			if(al == null){
				al = new ArrayList<Player>();
				duokaiMap.put(key,al);
			}
			al.add(sprites[i]);
		}
	}
	
	Iterator<String> it = duokaiMap.keySet().iterator();
	while(it.hasNext()){
		String key = it.next();
		ArrayList<Player> al = duokaiMap.get(key);
		if(al.size() > 1){
			int bindFail = 0;
			int bindSucc = 0;
			for(int i = 0 ; i < al.size() ; i++){
				Player p = al.get(i);
				Connection conn = p.getConn();
				USER_CLIENT_INFO_REQ o = null;
				if(conn != null)
					o = (USER_CLIENT_INFO_REQ)conn.getAttachmentData("USER_CLIENT_INFO_REQ");
				if(o.getIsStartServerBindFail()){
					bindFail++;
				}else{
					bindSucc++;
				}
			}
			if(bindSucc == 1 && bindFail >= 1){
				//多开
				for(int i = 0 ; i < al.size() ; i++){
					Player p = al.get(i);
					Connection conn = p.getConn();
					USER_CLIENT_INFO_REQ o = null;
					if(conn != null)
						o = (USER_CLIENT_INFO_REQ)conn.getAttachmentData("USER_CLIENT_INFO_REQ");
					String clientId = "";
					String deviceId = "";
					String phoneType = "";
					boolean isExistOtherServer = false;
					boolean isStartServerBindFail = false;
					
					if(o != null){
						 isExistOtherServer = o.getIsExistOtherServer();
			       		 isStartServerBindFail = o.getIsStartServerBindFail();
			       		 phoneType = o.getPhoneType();
			      		 deviceId = o.getMACADDRESS();
			      		 clientId = o.getClientId();
					}
					
					Game.logger.warn("[涉嫌多开] [多开封号24小时] ["+conn.getIdentity()+"] [clientId:"+clientId+"] [deviceId:"+deviceId+"] [phoneType:"+phoneType+"] [isExistOtherServer:"+isExistOtherServer+"] [isStartServerBindFail:"+isStartServerBindFail+"] "+ p.getLogString());
					
					if(clientId.length() > 0){
						DENY_USER_REQ req = new DENY_USER_REQ(GameMessageFactory.nextSequnceNum(), 
								clientId,p.getUsername(),"角色["+p.getName()+"]在["+com.xuanzhi.boss.game.GameConstants.getInstance().getServerName()+"]服使用多开外挂",
								com.xuanzhi.boss.game.GameConstants.getInstance().getServerName()+"-后台手动封号",
								true,true,false,0,24);
						MieshiGatewayClientService.getInstance().sendMessageToGateway(req);
					}
					if(p.getConn() != null) 
						p.getConn().close();
				
					duokaiMessage.add("[涉嫌多开] [多开封号24小时] ["+conn.getIdentity()+"] [clientId:"+clientId+"] [deviceId:"+deviceId+"] [phoneType:"+phoneType+"] [isExistOtherServer:"+isExistOtherServer+"] [isStartServerBindFail:"+isStartServerBindFail+"] "+ p.getLogString());
				}
				
			}
		}
	}
	
}

if(checkIP){
	sprites = sm.getOnlinePlayers();
	final HashMap<String,Integer> ipmap = new HashMap<String,Integer>();
	for(int i = 0 ; i < sprites.length ; i++){
		Connection conn = sprites[i].getConn();
		//USER_CLIENT_INFO_REQ o = (USER_CLIENT_INFO_REQ)conn.getAttachmentData("USER_CLIENT_INFO_REQ");
		
		String ip = "";
		if(conn != null) {
			String s = conn.getIdentity();
			ip = s.split(":")[0];
		}
		Integer c = (Integer)ipmap.get(ip);
		if(c == null){
			c = 1;
		}else{
			c++;
		}
		ipmap.put(ip,c);
	}
	
	Arrays.sort(sprites,new Comparator<Player>(){
		public int compare(Player p1,Player p2){
			String ip1 = "";
			if(p1.getConn() != null) {
				String s = p1.getConn().getIdentity();
				ip1 = s.split(":")[0];
			}
			String ip2 = "";
			if(p2.getConn() != null) {
				String s = p2.getConn().getIdentity();
				ip2 = s.split(":")[0];
			}
			Integer c1 = ipmap.get(ip1);
			if(c1 == null) c1 = 0;
			Integer c2 = ipmap.get(ip2);
			if(c2 == null) c2 = 0;
			if(c1 > c2) return -1;
			if(c1 < c2) return 1;
			return ip1.compareTo(ip2);
		}
	});
}

HashMap<String,String[]> username2Client = new HashMap<String,String[]>();

if(findduokai){
	ArrayList<Player> ppList = new ArrayList<Player>();
	for(int i = 0 ; i < sprites.length ; i++){
		Connection conn = sprites[i].getConn();
		USER_CLIENT_INFO_REQ o = (USER_CLIENT_INFO_REQ)conn.getAttachmentData("USER_CLIENT_INFO_REQ");
		if(o != null) continue;
		String clientId = "";
		String uuid = "";
		String deviceId = "";
		String phoneType = "";
		String network = "";
		String version = "";
		try{
			byte bytes[] = HttpUtils.webGet(new java.net.URL(url+ sprites[i].getUsername()),null,1000,3000);
			String s = new String(bytes);
			String ss[] = s.split("#");
			if(ss[0].equals("TRUE")){
				clientId = ss[1];
				 uuid = ss[2];
				 deviceId = ss[3];
				 phoneType = ss[4];
				 network = ss[5];
				 version = ss[6];
				 if(uuid == null || uuid.equals("null")) uuid = "";
				 if(deviceId == null|| deviceId.equals("null")) deviceId = "";
			}
		}catch(Exception e){}
		
		username2Client.put(sprites[i].getUsername(),new String[]{clientId,uuid,deviceId,phoneType,network,version});
		
		ppList.add(sprites[i]);
	}
	sprites = ppList.toArray(new Player[0]);
	
	Arrays.sort(sprites,new Comparator<Player>(){
		public int compare(Player p1,Player p2){
			String ip1 = "";
			if(p1.getConn() != null) {
				String s = p1.getConn().getIdentity();
				ip1 = s.split(":")[0];
			}
			String ip2 = "";
			if(p2.getConn() != null) {
				String s = p2.getConn().getIdentity();
				ip2 = s.split(":")[0];
			}

			return ip1.compareTo(ip2);
		}
	});
}

HashMap<String,ArrayList<Player>> ip2GongZuoShi = new HashMap<String,ArrayList<Player>>();

if(listdajing){
	sprites = sm.getOnlinePlayers();
	final HashMap<String,String> ip2typeMap = new HashMap<String,String>();
	final HashMap<String,Integer> ipmap = new HashMap<String,Integer>();
	for(int i = 0 ; i < sprites.length ; i++){
		Connection conn = sprites[i].getConn();
		USER_CLIENT_INFO_REQ o = (USER_CLIENT_INFO_REQ)conn.getAttachmentData("USER_CLIENT_INFO_REQ");
		
		String ip = "";
		if(conn != null) {
			String s = conn.getIdentity();
			ip = s.split(":")[0];
		}
		String phoneType = "null";
		if(o != null) phoneType = o.getPhoneType();
		String network = "null";
		if(o != null) network = o.getNetwork();
		
		String type = ip2typeMap.get(ip);
		if(type == null || type.length() == 0 || type.equals("null")){
			ip2typeMap.put(ip,network);
		}else if(type.equals("WIFI") && network.length() > 0 && !network.equals("null")){
			ip2typeMap.put(ip,network);
		}
		Integer c = (Integer)ipmap.get(ip+phoneType);
		if(c == null){
			c = 1;
		}else{
			c++;
		}
		ipmap.put(ip+phoneType,c);
	}
	ArrayList<Player> ppList = new ArrayList<Player>();
	for(int i = 0 ; i < sprites.length ; i++){
		Connection conn = sprites[i].getConn();
		USER_CLIENT_INFO_REQ o = (USER_CLIENT_INFO_REQ)conn.getAttachmentData("USER_CLIENT_INFO_REQ");
		if(o == null) continue;
		String ip = "";
		if(conn != null) {
			String s = conn.getIdentity();
			ip = s.split(":")[0];
		}
		String network = ip2typeMap.get(ip);
		if(network == null) network = "null";
		String phoneType = "null";
		if(o != null) phoneType = o.getPhoneType();
		
		Integer c = ipmap.get(ip+phoneType);
		
		
		//if(phoneType.contains("iPhone") || phoneType.contains("iPad") || phoneType.contains("iPod")){
		//	continue;
		//}
		if(c == null || c < 4) continue;
		//if(network == null) continue;
		network = network.trim();

		if(network.equals("") == false && network.equals("WIFI") == false && network.equals("null") == false) continue;
		if(sprites[i].getRMB() >= 1000) continue;
		ppList.add(sprites[i]);
		
	}
	sprites = ppList.toArray(new Player[0]);
	
	
	for(int i = 0 ; i < sprites.length ; i++){
		Connection conn = sprites[i].getConn();
		USER_CLIENT_INFO_REQ o = (USER_CLIENT_INFO_REQ)conn.getAttachmentData("USER_CLIENT_INFO_REQ");
		String ip = "";
		if(conn != null) {
			String s = conn.getIdentity();
			ip = s.split(":")[0];
		}
		String network = ip2typeMap.get(ip);
		if(network == null) network = "null";
		String phoneType = "null";
		if(o != null) phoneType = o.getPhoneType();
		ArrayList<Player> al = ip2GongZuoShi.get(ip+"#"+phoneType);
		if(al == null) al = new ArrayList<Player>();
		al.add(sprites[i]);
		ip2GongZuoShi.put(ip+"#"+phoneType,al);
	}
	
	
	Arrays.sort(sprites,new Comparator<Player>(){
		public int compare(Player p1,Player p2){
			String ip1 = "";
			if(p1.getConn() != null) {
				String s = p1.getConn().getIdentity();
				ip1 = s.split(":")[0];
			}
			String ip2 = "";
			if(p2.getConn() != null) {
				String s = p2.getConn().getIdentity();
				ip2 = s.split(":")[0];
			}

			return ip1.compareTo(ip2);
		}
	});
	
}



%>
<%@page import="com.fy.engineserver.stat.StatData"%>
<%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%><html><head>
<script>
function checkProcess() {
	document.getElementById("action").value="process";
	f1.submit();
}
function checkProcess() {
	document.getElementById("action").value="murder";
	f1.submit();
}
</script>
</HEAD>
<BODY>
<h2>Game Server，在线人数：<%= sprites.length %>，正在等待的用户：<%= core.waitingEnterGameConnections.size() %>，各个在线玩家的情况</h2>
<br><a href="./playersonline.jsp">刷新此页面</a> | <a href="./playersonline.jsp?checkip=true">检测打金和多开</a>
<%  if(checkIP){out.println(" | <a href='./playersonline.jsp?checkip=true&fengduokai=true'>查封多开设备24小时</a> | <a href='./playersonline.jsp?checkip=true&findduokai=true'>检测篡改版本多开</a> | <a href='./playersonline.jsp?checkip=true&listdajing=true'>显示打金工作室</a>");} %><br><br>
<% 
	for(int i = 0 ; i < duokaiMessage.size() ;i++){
		out.println(duokaiMessage.get(i)+"<br>");
	}

	if(listdajing){
		%>
		<form id="f2" name="f2" method="post">
		<input type='hidden' name='creategongzuoshi' value='true'>
		<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
		<tr bgcolor="#00FFFF" align="center"><td></td><td>IP</td><td>机型</td><td>成员数量</td><td>打金工作室账户</td></tr>
		<%
			Iterator<String> it = ip2GongZuoShi.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				ArrayList<Player> al = ip2GongZuoShi.get(key);
				if(al.size() < 4) continue;
				String ss[] = key.split("#",2);
				StringBuffer sb = new StringBuffer();
				StringBuffer sb2 = new StringBuffer();
				for(int i = 0 ; i < al.size() ; i++){
					Player p = al.get(i);
					sb.append(p.getUsername()+"("+p.getLevel()+") ,");
					sb2.append(p.getUsername()+",");
				}
				out.println("<tr bgcolor='#FFFFFF'><td><input type='checkbox' name='gongzuoshis' value='"+key+"@@"+sb2+"' checked ></td><td>"+ss[0]+"</td><td>"+ss[1]+"</td><td>"+al.size()+"</td><td>"+sb+"</td></tr>");
			}
		%>
		</table><br><input type='submit' value='生成打金工作室，对他们实行产出限制'>
		</form><br><br>
		<% 
	}
%>
<form id="f1" name="f1" method="get">

<%
	if(findduokai){
		%><input type='hidden' name='kickfindduokai' value='true'><input type='hidden' name='findduokai' value='true'><% 
	}else{
		%><input type='hidden' name='kickflag' value='true'><% 
	}
%>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center"><td>编号</td><td>账户名</td><td>名字</td>
<td>Identity</td><td>设备</td><td>机型</td><td>多开(其他/绑定)</td><td>网络</td>

<td>级别</td><td>总时长</td><td>充值(元)</td><td>银子(锭)</td>
<td>血</td>
<td>蓝</td>
<td>经验值</td>

<td>最后时间</td><td>国家</td><td>地图</td><td>X坐标</td><td>Y坐标</td>
</tr>

<%
        //int n = sm.getAmountOfPlayers();

        for(int i = 0 ; i < sprites.length ; i++){
                Connection conn = sprites[i].getConn();
                
                StatData sdata =sprites[i].getStatData(StatData.STAT_ONLINE_TIME);
                String alltime = "";
                if(sdata != null){
                	alltime = "" + (sdata.getValue()/3600000) + "小时";
                }
                String queuePushNum = "0";
                String queuePopNum = "0";
                String size = "0";
                String register = "false"       ;
                String readyNum = "1";
                
                boolean isExistOtherServer = false;
				boolean isStartServerBindFail = false;
				String phoneType = "null";
				String deviceId = "";
				String network = "null";
                if(conn != null){
                        DefaultSelectableQueue dq = (DefaultSelectableQueue) conn.getAttachment2();
                        if(dq != null){
                                size = "" + dq.size();
                                queuePushNum = "" + dq.getPushNum();
                                queuePopNum  = "" + dq.getPopNum();
                                register = "" + dq.isRegistered();
                                readyNum = "" + dq.getReadyNum();
                        }
                        USER_CLIENT_INFO_REQ o = (USER_CLIENT_INFO_REQ)conn.getAttachmentData("USER_CLIENT_INFO_REQ");
                        if(o != null){
                        	 isExistOtherServer = o.getIsExistOtherServer();
                        	 isStartServerBindFail = o.getIsStartServerBindFail();
                        	 phoneType = o.getPhoneType();
                        	 deviceId = o.getMACADDRESS();
                        	 network = o.getNetwork();
                        }else{
                        	String ss[] = username2Client.get(sprites[i].getUsername());
                        	if(ss != null){
                        		deviceId = ss[1]+ss[2];
                        		phoneType = ss[3];
                        		network = ss[4]+"/"+ss[5];
                        	}
                        }
                        
                }
                %><tr bgcolor="#FFFFFF" align="center">
                <td><%=i+1 %><% if(checkIP){ out.println("<input type='checkbox' name='kickusers' value='"+sprites[i].getUsername()+"' "+(listdajing?"checked":"")+">");} %></td><td><%= sprites[i].getUsername()%></td>
                <td><a href='player/mod_player.jsp?action=select_playerId&playerid=<%=sprites[i].getId() %>'><%=sprites[i].getName() %></a></td>
                <td><%= (sprites[i].getConn()!=null?sprites[i].getConn().getIdentity():"无连接") %></td>
                <td><%= deviceId %></td>
                <td><%= phoneType %></td>
                <td><%= isExistOtherServer+"/"+isStartServerBindFail %></td>
                <td><%= network %></td>
                <td><%= sprites[i].getLevel() %></td>
                <td><%= alltime %></td>
                <td><%= sprites[i].getRMB()/100 %></td>
                <td><%= sprites[i].getSilver()/1000000 %></td>
                <td><%= sprites[i].getHp()+"/" + sprites[i].getMaxHP() %></td>
                <td><%= sprites[i].getMp()+"/" + sprites[i].getMaxMP() %></td>
                <td><%= sprites[i].getExp()+"/" + sprites[i].getNextLevelExp() %></td>
                <td><%= (System.currentTimeMillis() - sprites[i].getLastRequestTime())/1000 %>秒前</td>
                <td><%= CountryManager.得到国家名(sprites[i].getCountry()) %></td>
                <td><%= sprites[i].getGame() %></td>
                <td><%=sprites[i].getX() %></td>
                <td><%=sprites[i].getY() %></td>
                </tr><%
        }
%>
</table><br>
<%
	if(findduokai){
		%><input type='submit' value='将选择的用户封号，原因篡改客户端版本号'> </form><% 
	}else{
		%><input type='submit' value='将选择的用户踢下线'>
		<input type=hidden name=action id=action>
		<input type='button' value='比对用户的共有进程' onclick="checkProcess()">
		<input type='button' value='干死选中的' onclick="murder()"> </form><%
	}
%>

</BODY></html>
