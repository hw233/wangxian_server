<%@ page contentType="text/html;charset=utf-8" import="java.util.*,java.util.concurrent.*,
com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,com.xuanzhi.tools.servlet.HttpUtils,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,
com.xuanzhi.tools.queue.*,com.fy.engineserver.gateway.*,org.w3c.dom.*,
com.fy.engineserver.message.*,com.fy.engineserver.dajing.*"%><%! 

	public String getIPInfo(String ip) throws Exception{
	return "--";
/**		String url = "http://www.youdao.com/smartresult-xml/search.s?type=ip&q="+ip;
		byte content[] = HttpUtils.webGet(new java.net.URL(url),null,1000,1000);
		String s = new String(content,"gbk");
		Document dom = XmlUtil.loadString(s);
		Element root = dom.getDocumentElement();
		Element product = XmlUtil.getChildByName(root,"product");
		Element location = XmlUtil.getChildByName(product,"location");
		return XmlUtil.getText(location,null);
		*/
	}

	public DajingStudio getDajingStudio(DajingStudioManager dm,String ip){
		if(ip == null || ip.length() == 0) return null;
		Iterator<String> it = dm.dajingMap.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			if(key.contains(ip)) return dm.dajingMap.get(key);
		}
		return null;
	}
	%><%
DajingStudioManager dsManager = DajingStudioManager.getInstance();
	if(dsManager.initialized == false){
		dsManager.init();
	}

ArrayList<String> duokaiMessage = new ArrayList<String>();

String action = request.getParameter("action");
if(action == null ) action = "";

if(action.equals("delete_studio")){
	int i = Integer.parseInt(request.getParameter("ds"));
	DajingStudio dds[] = dsManager.getDajingStudios();
	dsManager.dajingMap.remove(dds[i].ip);
	dsManager.saveForManual();
	
	duokaiMessage.add("[删除工作室] [成功] ["+dds[i].ip+"]");
}else if(action.equals("delete_group")){
	int i = Integer.parseInt(request.getParameter("ds"));
	int j = Integer.parseInt(request.getParameter("dg"));
	DajingStudio dds[] = dsManager.getDajingStudios();
	DajingGroup dg = dds[i].groupList.remove(j);
	
	dsManager.saveForManual();
	
	duokaiMessage.add("[删除工作组] [成功] ["+dds[i].ip+"] ["+dg.phoneType+"] ["+dg.usernameList+"]");
}
PlayerManager sm = PlayerManager.getInstance();
     


Player sprites[] = sm.getOnlinePlayers();

	HashMap<String,ArrayList<Player>> ip2GongZuoShi = new HashMap<String,ArrayList<Player>>();


	HashMap<String,String> ip2typeMap = new HashMap<String,String>();
	HashMap<String,Integer> ipmap = new HashMap<String,Integer>();
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
		}else if(type.equals("WIFI") && network.length() > 0 && !network.equals("null")&& !network.equals("unknow")){
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
		if(c == null || c < 3) continue;
		//if(network == null) continue;
		network = network.trim();

		if(network.equals("") == false && network.equals("WIFI") == false && network.equals("null") == false) continue;
		if(sprites[i].getRMB() >= 10000) continue;
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
		String phoneType = "null";
		if(o != null) phoneType = o.getPhoneType();
		ArrayList<Player> al = ip2GongZuoShi.get(ip+"#"+phoneType);
		if(al == null) al = new ArrayList<Player>();
		al.add(sprites[i]);
		ip2GongZuoShi.put(ip+"#"+phoneType,al);
	}
	
	if(action.equals("new_gongzuoshi")){
		String keys[] = request.getParameterValues("gongzuoshi");
		Iterator<String> it = ip2GongZuoShi.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			boolean b =false;
			for(int i = 0 ; i < keys.length ; i++){
				if(keys[i].equals(key)){
					b = true;
				}
			}
			if(b){
				ArrayList<Player> al = ip2GongZuoShi.get(key);
				String ss[] = key.split("#",2);
				String ip = ss[0];
				String phoneType = ss[1];
				DajingStudio ds = getDajingStudio(dsManager,ip);
				if(ds != null){
					DajingGroup dg = ds.getDajingGroup(phoneType);
					if(dg != null){
						for(int i = 0 ; i < al.size() ; i++){
							Player p = al.get(i);
							if(dg.usernameList.contains(p.getUsername()) == false){
								dg.usernameList.add(p.getUsername());
								duokaiMessage.add("[添加工作组新成员] [成功] ["+ds.ip+"] ["+dg.phoneType+"] ["+p.getUsername()+"]");
							}
						}
					}else{
						dg = new DajingGroup();
						dg.phoneType = phoneType;
						for(int i = 0 ; i < al.size() ; i++){
							Player p = al.get(i);
							dg.usernameList.add(p.getUsername());
						}
						duokaiMessage.add("[添加工作组] [成功] ["+ds.ip+"] ["+dg.phoneType+"] ["+dg.usernameList+"]");
						ds.groupList.add(dg);
					}
				}else{
					//判断是否存在工作室换ip的情况，
					HashMap<DajingStudio,Integer> map2 = new HashMap<DajingStudio,Integer>();
					for(int i = 0 ; i < al.size() ; i++){
						Player p = al.get(i);
						DajingStudio d = dsManager.getDajingStudioByPlayer(p);
						if(d != null){
							Integer k = map2.get(d);
							if(k == null) k = 1;
							else k++;
							map2.put(d,k);
						}
					}
					DajingStudio maxCountDs = null;
					int maxC = 0;
					Iterator<DajingStudio> it2 = map2.keySet().iterator();
					while(it2.hasNext()){
						DajingStudio d = it2.next();
						Integer c = map2.get(d);
						if(c >=2 && c > maxC){
							maxCountDs = d;
							maxC = c;
						}
					}
					if(maxCountDs != null){
						ds = maxCountDs;
						
						dsManager.dajingMap.remove(ds.ip);
						ds.ip = ds.ip+","+ip;
						dsManager.dajingMap.put(ds.ip,ds);
						
						DajingGroup dg = ds.getDajingGroup(phoneType);
						if(dg != null){
							for(int i = 0 ; i < al.size() ; i++){
								Player p = al.get(i);
								if(dg.usernameList.contains(p.getUsername()) == false){
									dg.usernameList.add(p.getUsername());
									duokaiMessage.add("[添加工作组新成员] [成功] ["+ds.ip+"] ["+dg.phoneType+"] ["+p.getUsername()+"]");
								}
							}
						}else{
							dg = new DajingGroup();
							dg.phoneType = phoneType;
							for(int i = 0 ; i < al.size() ; i++){
								Player p = al.get(i);
								dg.usernameList.add(p.getUsername());
							}
							duokaiMessage.add("[添加工作组] [成功] ["+ds.ip+"] ["+dg.phoneType+"] ["+dg.usernameList+"]");
							ds.groupList.add(dg);
						}
					}else{
						ds = new DajingStudio();
						ds.ip = ip;
						DajingGroup dg = new DajingGroup();
						dg.phoneType = phoneType;
						for(int i = 0 ; i < al.size() ; i++){
							Player p = al.get(i);
							dg.usernameList.add(p.getUsername());
						}
						duokaiMessage.add("[添加工作室] [成功] ["+ds.ip+"] ["+dg.phoneType+"] ["+dg.usernameList+"]");
						ds.groupList.add(dg);
						
						dsManager.dajingMap.put(ip,ds);
					}
				}
			}
			
		}
		dsManager.saveForManual();
	}
	
	Iterator<String> it = ip2GongZuoShi.keySet().iterator();
	while(it.hasNext()){
		String key = it.next();
		ArrayList<Player> al = ip2GongZuoShi.get(key);
		String ss[] = key.split("#",2);
		String ip = ss[0];
		String phoneType = ss[1];
		DajingStudio ds = getDajingStudio(dsManager,ip);
		if(ds != null){
			DajingGroup dg = ds.getDajingGroup(phoneType);
			if(dg != null){
				boolean b = false;
				for(int i = 0 ; i < al.size() ; i++){
					Player p = al.get(i);
					if(dg.usernameList.contains(p.getUsername()) == false){
						b = true;
					}
				}
				if(b == false){
					//此工作组已经存在
					it.remove();
				}
			}
			
		}
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

	
	


%><html><head>
</HEAD>
<BODY>
<h2>疑似打金工作室成员在线人数：<%= sprites.length %></h2>
<br><a href="./playersonline.jsp">玩家在线页面</a> | <a href="./dajing_gongzuoshi.jsp">刷新此页面</a><br>
<% 
	for(int i = 0 ; i < duokaiMessage.size() ;i++){
		out.println(duokaiMessage.get(i)+"<br>");
	}

if(action.equals("list_studio")){
	int ii = Integer.parseInt(request.getParameter("ds"));
	DajingStudio dds[] = dsManager.getDajingStudios();
	DajingStudio ds = dds[ii];
	
	out.println("工作室["+ds.ip+"]详细信息:<br>");
	out.println("<table border='0' cellpadding='0' cellspacing='1'  bgcolor='#000000'>");
	out.println("<tr bgcolor='#00FFFF'><td>项目</td><td>值</td></tr>");
	out.println("<tr bgcolor='#FFFFFF'><td align='right'>创建时间</td><td>"+DateUtil.formatDate(ds.createTime,"yyyy-MM-dd HH:mm:ss")+"</td></tr>");
	out.println("<tr bgcolor='#FFFFFF'><td align='right'>最后活动时间</td><td>"+DateUtil.formatDate(new Date(ds.lastCanChuTime),"yyyy-MM-dd HH:mm:ss")+"</td></tr>");
	for(int i = 0 ; i < DajingStudioManager.工作室产出限制名称.length ; i++){
		out.println("<tr bgcolor='#EEEEEE'><td align='right'>"+DajingStudioManager.工作室产出限制名称[i]+"(今天当前值/最大值)</td><td>"+ds.canchuData[i]+"/"+DajingStudioManager.打金工作室产出限制表[i]+"</td></tr>");
	}
	for(int i = 0 ; i < DajingStudioManager.STATDATANAMES.length ; i++){
		out.println("<tr bgcolor='#EEEEEE'><td align='right'>"+DajingStudioManager.STATDATANAMES[i]+"</td><td>"+ds.statData[i]+"</td></tr>");
	}
	out.println("</table>");
}

%>
<h4>打金工作室列表</h4>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center"><td>编号</td><td>打金工作室</td><td>一共充值</td><td>交易出去的银锭</td><td>未交易的银锭</td>
<td>创建时间</td><td>最后活动时间</td>
<td>工作组</td><td>成员</td><td>组操作</td><td>操作</td></tr>
<%
	DajingStudio dss[] = dsManager.getDajingStudios();
	for(int i = 0 ; i < dss.length ; i++){
		for(int j = 0 ; j < dss[i].groupList.size() ; j++){
			DajingGroup dg = dss[i].groupList.get(j);
			out.println("<tr bgcolor='#FFFFFF'>");

			if(j == 0){
				long silver = 0;
				long rmb = 0;
				for(int k = 0 ; k < dss[i].groupList.size() ; k++){
					DajingGroup dd = dss[i].groupList.get(k);
					for(int l = 0 ; l < dd.usernameList.size() ; l++){
						try{
							Player p = sm.getPlayerByUser(dd.usernameList.get(l))[0];
							silver+=p.getSilver();
							rmb = p.getRMB()/100;
						}catch(Exception e){}
					}
				}
				silver = silver/1000000;
				long silver2 = dss[i].statData[DajingStudioManager.摆摊交易出工作室以外的银锭的数量]
				    +dss[i].statData[DajingStudioManager.面对面交易出工作室以外的银锭的数量]
				    +dss[i].statData[DajingStudioManager.面对面交易出工作室以外的银块的数量]*10000;
				silver2 = silver2/1000000;
				out.println("<td rowspan='"+dss[i].groupList.size()+"'>"+(i+1)+"</td>");
				
				out.println("<td rowspan='"+dss[i].groupList.size()+"'>");
				String ips[] = dss[i].ip.split(",");
				for(int k = 0 ; k < ips.length ; k++){
					out.println("<a href='./dajing_gongzuoshi.jsp?action=list_studio&ds="+i+"'>"+ips[k]+"["+getIPInfo(ips[k])+"]</a><br>");
				}
				
				out.println("</td>");

				out.println("<td rowspan='"+dss[i].groupList.size()+"'>"+rmb+"</td>");
				out.println("<td rowspan='"+dss[i].groupList.size()+"'>"+silver2+"</td>");
				out.println("<td rowspan='"+dss[i].groupList.size()+"'>"+silver+"</td>");
				out.println("<td rowspan='"+dss[i].groupList.size()+"'>"+DateUtil.formatDate(dss[i].createTime,"yyyy-MM-dd")+"</td>");
				out.println("<td rowspan='"+dss[i].groupList.size()+"'>"+DateUtil.formatDate(new Date(dss[i].lastCanChuTime),"yyyy-MM-dd")+"</td>");
			}
			out.println("<td>"+dg.phoneType+"</td>");
			StringBuffer sb = new StringBuffer();
			for(int k = 0 ; k < dg.usernameList.size() ; k++){
				try{
					Player p = sm.getPlayerByUser(dg.usernameList.get(k))[0];
					sb.append(p.getUsername()+"("+p.getLevel()+") ,");
				}catch(Exception e){}
			}
			out.println("<td>"+sb+"</td>");
			out.println("<td><a href='./dajing_gongzuoshi.jsp?action=delete_group&ds="+i+"&dg="+j+"'>删除组</a></td>");
			if(j == 0){
				out.println("<td rowspan='"+dss[i].groupList.size()+"'><a href='./dajing_gongzuoshi.jsp?action=delete_studio&ds="+i+"'>删除工作室</a></td>");
			}
			out.println("</tr>");
		}
	}
%>
</table>

<h4>新的打金工作室或者工作组或者工作组成员</h4>
<form id="f2" name="f2" method="get">
<input type='hidden' name='action' value='new_gongzuoshi'>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center"><td></td><td>添加类型</td><td>IP</td><td>机型</td><td>成员数量</td><td>打金工作室账户</td></tr>
<%
			it = ip2GongZuoShi.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				ArrayList<Player> al = ip2GongZuoShi.get(key);
				String ss[] = key.split("#",2);
				String ip = ss[0];
				String phoneType = ss[1];
				DajingStudio ds = dsManager.getDajingStudio(ip);
				String type = "";
				if(ds != null){
					DajingGroup dg = ds.getDajingGroup(phoneType);
					if(dg != null){
						type = "新添成员";
					}else{
						type = "新添工作组";
						if(al.size() < 3) continue;
					}
				}else{
					//判断是否存在工作室换ip的情况，
					HashMap<DajingStudio,Integer> map2 = new HashMap<DajingStudio,Integer>();
					for(int i = 0 ; i < al.size() ; i++){
						Player p = al.get(i);
						DajingStudio d = dsManager.getDajingStudioByPlayer(p);
						if(d != null){
							Integer k = map2.get(d);
							if(k == null) k = 1;
							else k++;
							map2.put(d,k);
						}
					}
					DajingStudio maxCountDs = null;
					int maxC = 0;
					Iterator<DajingStudio> it2 = map2.keySet().iterator();
					while(it2.hasNext()){
						DajingStudio d = it2.next();
						Integer c = map2.get(d);
						if(c >=2 && c > maxC){
							maxCountDs = d;
							maxC = c;
						}
					}
					if(maxCountDs != null){
						type = "工作室新的IP";
						if(al.size() < 2) continue;
					}else{
						type = "新添工作室";
						if(al.size() < 3) continue;
					}
					
				}
				
				StringBuffer sb = new StringBuffer();
				for(int i = 0 ; i < al.size() ; i++){
					Player p = al.get(i);
					DajingStudio dd = dsManager.getDajingStudioByPlayer(p);
					if(dd != null){
						sb.append("<i>"+p.getUsername()+"("+p.getLevel()+","+dd.ip+")</i> ,");
					}else{
						sb.append(p.getUsername()+"("+p.getLevel()+") ,");
					}

				}
				out.println("<tr bgcolor='#FFFFFF'><td><input type='checkbox' name='gongzuoshi' value='"+key+"' checked ></td><td>"+type+"</td><td>"+ss[0]+"["+(getIPInfo(ss[0]))+"]</td><td>"+ss[1]+"</td><td>"+al.size()+"</td><td>"+sb+"</td></tr>");
			}
		%>
		</table><br><input type='submit' value='添加打金工作室或打金成员'>
		</form><br><br>
		<% 
	
%>

<h4>在线打金工作室成员</h4>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center"><td>编号</td><td>账户名</td><td>名字</td><td>工作室</td>
<td>Identity</td><td>设备</td><td>机型</td><td>多开(其他/绑定)</td><td>网络</td>
<td>地图</td>
<td>级别</td><td>充值(元)</td><td>银子(锭)</td>
<td>血</td>
<td>蓝</td>
<td>经验值</td>

<td>最后时间</td><td>X坐标</td><td>Y坐标</td>
</tr>

<%
        //int n = sm.getAmountOfPlayers();

        for(int i = 0 ; i < sprites.length ; i++){
                Connection conn = sprites[i].getConn();
                
                boolean isExistOtherServer = false;
				boolean isStartServerBindFail = false;
				String phoneType = "null";
				String deviceId = "";
				String network = "null";
				String gongzuoshi = "--";
				DajingStudio dajing = dsManager.getDajingStudioByPlayer(sprites[i]);
				if(dajing != null){
					gongzuoshi = dajing.ip;
					gongzuoshi = gongzuoshi.replaceAll(",","<br>");
				}
                if(conn != null){
                      
                        USER_CLIENT_INFO_REQ o = (USER_CLIENT_INFO_REQ)conn.getAttachmentData("USER_CLIENT_INFO_REQ");
                        if(o != null){
                        	 isExistOtherServer = o.getIsExistOtherServer();
                        	 isStartServerBindFail = o.getIsStartServerBindFail();
                        	 phoneType = o.getPhoneType();
                        	 deviceId = o.getUUID()+o.getDEVICEID();
                        	 network = o.getNetwork();
                        }
                }
                %><tr bgcolor="#FFFFFF" align="center">
                <td><%=i+1 %></td><td><%= sprites[i].getUsername()%></td>
                <td><a href='player/mod_player.jsp?action=select_playerId&playerid=<%=sprites[i].getId() %>'><%=sprites[i].getName() %></a></td>
                <td><%= gongzuoshi %></td>
                <td><%= (sprites[i].getConn()!=null?sprites[i].getConn().getIdentity():"无连接") %></td>
                <td><%= deviceId %></td>
                <td><%= phoneType %></td>
                <td><%= isExistOtherServer+"/"+isStartServerBindFail %></td>
                <td><%= network %></td>
                <td><%= sprites[i].getGame() %></td>
                <td><%= sprites[i].getLevel() %></td>
                <td><%= sprites[i].getRMB()/100 %></td>
                <td><%= sprites[i].getSilver()/1000000 %></td>
                <td><%= sprites[i].getHp()+"/" + sprites[i].getMaxHP() %></td>
                <td><%= sprites[i].getMp()+"/" + sprites[i].getMaxMP() %></td>
                <td><%= sprites[i].getExp()+"/" + sprites[i].getNextLevelExp() %></td>
                <td><%= (System.currentTimeMillis() - sprites[i].getLastRequestTime())/1000 %>秒前</td>
                <td><%=sprites[i].getX() %></td>
                <td><%=sprites[i].getY() %></td>
                </tr><%
        }
%>
</table><br>


</BODY></html>
