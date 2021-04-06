<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.NewLoginHandler"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.* "%>
<%@page import="java.util.List"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.AuthorizeManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body><%!
	ArrayList<ClientAuthorization> caList = new  ArrayList<ClientAuthorization>();
	ArrayList<ClientEntity> ceList = new  ArrayList<ClientEntity>();
	int start = 1;
	int end = 50;
	int totalNum = 0;
	int deleteNum = 0;
%><%

	if(request.getParameter("start") != null){
		start = Integer.parseInt(request.getParameter("start"));
	}
	if(request.getParameter("end") != null){
		end = Integer.parseInt(request.getParameter("end"));
	}
	
	String sql = "select a.username,"+
    "a.authorizeclientid,"+
    "a.clientid,"+
    "a.authorizetime,"+
    "a.lastlogintime,"+
    "a.authorizetype,"+
    "a.authorizestate,"+
    "a.waitauthorizetime,"+
    "a.querynum,"+
    "a.ip,"+
    "b.mac,"+
    "b.platform,"+
    "b.phonetype,"+
    "b.gputype,"+
    "b.createtime"+
" from clientauthorization a"+
" left join cliententity b"+
"   on a.clientid = b.clientid"+
" where a.username in"+
"      (select username"+
"         from (select rownum RR, username"+
"                 from (select username"+
"                         from (select T1.authorizetime, T1.username"+
"                                 from (select a.clientid,"+
"                                              a.username,"+
"                                              a.authorizestate,"+
"                                              b.mac,"+
"                                              b.phonetype,"+
"                                              b.gputype,"+
"                                              a.authorizetime,a.lastlogintime"+
"                                         from clientauthorization a"+
"                                         left join cliententity b"+
"                                           on a.clientid = b.clientid) T1,"+
"                                       (select a.clientid,"+
"                                               a.username,"+
"                                              a.authorizestate,"+
"                                              b.mac,"+
"                                              b.phonetype,"+
"                                              b.gputype,"+
"                                              a.authorizetime,a.lastlogintime"+
"                                         from clientauthorization a"+
"                                         left join cliententity b"+
"                                           on a.clientid = b.clientid) T2"+
"                                where T1.username = T2.username"+
"                                  and T1.clientid != T2.clientid"+
"                                  and T1.phonetype = T2.phonetype"+
"                                  and T1.gputype = T2.gputype"+
"                                  and T1.authorizestate = 1"+
"                                  and (T2.authorizestate = 0 or"+
"                                      T2.authorizestate = 5)"+
"                                  and (T1.mac is null or T2.mac is null) and T1.lastlogintime>0 and T2.lastlogintime>0"+
"                                order by T1.authorizetime desc)"+
"                        group by username))"+
"        where RR >= "+start+""+
"          and RR <= "+end+""+
"       "+
"       ) order by a.username,a.authorizetime desc";
	
	String countSql = "select count(1) from (select username"+
	"                         from (select T1.authorizetime, T1.username"+
	"                                 from (select a.clientid,"+
	"                                              a.username,"+
	"                                              a.authorizestate,"+
	"                                              b.mac,"+
	"                                              b.phonetype,"+
	"                                              b.gputype,"+
	"                                              a.authorizetime,a.lastlogintime"+
	"                                         from clientauthorization a"+
	"                                         left join cliententity b"+
	"                                           on a.clientid = b.clientid) T1,"+
	"                                       (select a.clientid,"+
	"                                               a.username,"+
	"                                              a.authorizestate,"+
	"                                              b.mac,"+
	"                                              b.phonetype,"+
	"                                              b.gputype,"+
	"                                              a.authorizetime,a.lastlogintime"+
	"                                         from clientauthorization a"+
	"                                         left join cliententity b"+
	"                                           on a.clientid = b.clientid) T2"+
	"                                where T1.username = T2.username"+
	"                                  and T1.clientid != T2.clientid"+
	"                                  and T1.phonetype = T2.phonetype"+
	"                                  and T1.gputype = T2.gputype"+
	"                                  and T1.authorizestate = 1"+
	"                                  and (T2.authorizestate = 0 or"+
	"                                      T2.authorizestate = 5)"+
	"                                  and (T1.mac is null or T2.mac is null) and T1.lastlogintime>0 and T2.lastlogintime>0 "+
	"                                order by T1.authorizetime desc)"+
	"                        group by username)";
	
	AuthorizeManager am = AuthorizeManager.getInstance();
	
	String action = request.getParameter("action");
	
	boolean filter = false;
	if(action != null && action.equals("filter")){
		filter = true;
	}
	
	if(action != null && action.equals("query")){
		
		deleteNum = 0;
		
		SimpleEntityManager<ClientAuthorization> em = (SimpleEntityManager<ClientAuthorization>)am.em_ca;
		SimpleEntityManagerMysqlImpl<ClientAuthorization> emMySql = null;
		SimpleEntityManagerOracleImpl<ClientAuthorization> emOrcale = null;
		java.sql.Connection conn = null;
		if (em instanceof SimpleEntityManagerMysqlImpl) {
			emMySql = (SimpleEntityManagerMysqlImpl<ClientAuthorization>)em;
			conn = emMySql.getConnection();
		}else if (em instanceof SimpleEntityManagerOracleImpl) {
			emOrcale = (SimpleEntityManagerOracleImpl<ClientAuthorization>)em;
			conn = emOrcale.getConnection();
		}
		
		java.sql.ResultSet rs = conn.createStatement().executeQuery(countSql);
		
		if(rs.next()){
			totalNum = rs.getInt(1);
		}
		rs.close();
		
		rs = conn.createStatement().executeQuery(sql);
		
		
		while(rs.next()){
			ClientAuthorization ca = new ClientAuthorization();
			ca.setUsername(rs.getString("username"));
			ca.setAuthorizeClientId(rs.getString("authorizeclientid"));
			ca.setAuthorizeTime(rs.getLong("authorizetime"));
			ca.setAuthorizeType(rs.getInt("authorizetype"));
			ca.setClientID(rs.getString("clientid"));
			ca.setAuthorizeState(rs.getInt("authorizestate"));
			ca.setLastLoginTime(rs.getLong("lastlogintime"));
			ca.setQueryNum(rs.getInt("querynum"));
			ca.setWaitAuthorizeTime(rs.getLong("waitauthorizetime"));
			ca.setIp(rs.getString("ip"));
			caList.add(ca);
			
			ClientEntity ce = new ClientEntity();
			ce.setClientID(rs.getString("clientid"));
			ce.setCreateTime(rs.getLong("createtime"));
			ce.setGpuType(rs.getString("gputype"));
			ce.setMac(rs.getString("mac"));
			ce.setPhoneType(rs.getString("phonetype"));
			ce.setPlatform(rs.getString("platform"));
			
			ceList.add(ce);
			
		}
		conn.close();
	}else if(action != null && action.equals("delete")){
		String clientid = request.getParameter("clientid");
		String user = request.getParameter("username");
		filter  = Boolean.valueOf(request.getParameter("filter"));
		List<ClientAuthorization> cas = AuthorizeManager.getInstance().getUsernameClientAuthorization(user);
		java.util.Iterator<ClientAuthorization> it = cas.iterator();
		while(it.hasNext()){
			ClientAuthorization ca  = it.next();
			if(ca.getClientID().equals(clientid)){
				it.remove();
				ca.setLastLoginTime(0);
				AuthorizeManager.getInstance().em_ca.notifyFieldChange(ca,"lastLoginTime");
				AuthorizeManager.getInstance().em_ca.save(ca);
				deleteNum++;
				AuthorizeManager.logger.warn("[从页面删除一个CA] ["+ca.getId()+"] ["+ca.getClientID()+"] ["+ca.getAuthorizeState()+"] ["+ca.getUsername()+"] ["+ca.getAuthorizeTime()+"]");
			}
		}
		
		it = caList.iterator();
		int k = 0;
		while(it.hasNext()){
			ClientAuthorization ca  = it.next();
			if(ca.getClientID().equals(clientid) && ca.getUsername().equals(user)){
				it.remove();
				ceList.remove(k);
			}
			k++;
		}
	}else if(action != null && action.equals("filter_delete_all")){
		filter = true;
		String sss = request.getParameter("userList");
		String userNames[] = sss.split(",");
		for(int i = 0 ; i < userNames.length ; i++){
			if(userNames[i].trim().length() > 0){
				
				String user = userNames[i];
				List<ClientAuthorization> cas = AuthorizeManager.getInstance().getUsernameClientAuthorization(user);
				java.util.Iterator<ClientAuthorization> it = cas.iterator();
				while(it.hasNext()){
					ClientAuthorization ca  = it.next();
				//	it.remove();
					ca.setLastLoginTime(0);
					//AuthorizeManager.getInstance().em_ca.notifyFieldChange(ca,"lastLoginTime");
				//	AuthorizeManager.getInstance().em_ca.save(ca);
				//	AuthorizeManager.getInstance().em_ca.batchSave(ts)
					deleteNum++;
					AuthorizeManager.logger.warn("[从页面批量删除一个CA] ["+ca.getId()+"] ["+ca.getClientID()+"] ["+ca.getAuthorizeState()+"] ["+ca.getUsername()+"] ["+ca.getAuthorizeTime()+"]");
					out.println("[从页面批量删除一个CA] ["+ca.getId()+"] ["+ca.getClientID()+"] ["+ca.getAuthorizeState()+"] ["+ca.getUsername()+"] ["+ca.getAuthorizeTime()+"]<br>");
				}
				
				AuthorizeManager.getInstance().em_ca.batchSave(cas.toArray(new ClientAuthorization[0]));
				AuthorizeManager.logger.warn("[从页面批量删除CA]");
				
				for(int k = caList.size()-1 ; k >= 0 ; k--){
					ClientAuthorization ca  = caList.get(k);
					if(ca.getUsername().equals(user)){
						caList.remove(k);
						ceList.remove(k);
					}
				}
				
			}
		}
	}
	
	ArrayList<ClientAuthorization> caList2 = caList;
	ArrayList<ClientEntity> ceList2 = ceList;
	
	ArrayList<String> userListResult = new ArrayList<String> ();
	
	if(filter){
		ArrayList<String> userList = new ArrayList<String> ();
		for(int i = 0 ; i < caList.size() ; i++){
			ClientAuthorization ca  = caList.get(i);
			String u = ca.getUsername();
			if(userList.contains(u) == false){
				userList.add(u);
			}
		}
		
		for(int i = 0 ; i < userList.size() ; i++){
			String u = userList.get(i);
			ArrayList<ClientAuthorization> al1 = new ArrayList<ClientAuthorization>();
			ArrayList<ClientEntity> al2 = new ArrayList<ClientEntity>();
			for(int k = 0 ; k < caList.size() ; k++){
				ClientAuthorization ca  = caList.get(k);
				ClientEntity ce  = ceList.get(k);
				if(u.equals(ca.getUsername())){
					al1.add(ca);
					al2.add(ce);
				}
			}
			
			//只有一种机型 且 有授权的最后登录时间比较靠前
			if(al2.size() >= 0){
				java.util.HashSet<String> phonetypeMap = new java.util.HashSet<String>();
				java.util.HashSet<String> macMap = new java.util.HashSet<String>();
				java.util.HashSet<String> gpuMap = new java.util.HashSet<String>();
				long authorizeLastLoginTime = 0;
				for(int k = 0 ; k < al1.size() ; k++){
					ClientAuthorization ca  = al1.get(k);
					ClientEntity ce  = al2.get(k);
					if(ce.getMac() != null){
						macMap.add(ce.getMac());
					}
					phonetypeMap.add(ce.getPhoneType());
					gpuMap.add(ce.getGpuType());
					if(ca.getAuthorizeState() == 1){
						if(ca.getLastLoginTime() > authorizeLastLoginTime){
							authorizeLastLoginTime = ca.getLastLoginTime();
						}
					}
				}
				int waitNum = 0;
				for(int k = 0 ; k < al1.size() ; k++){
					ClientAuthorization ca  = al1.get(k);
					if(ca.getAuthorizeState() != 1 && ca.getLastLoginTime() > authorizeLastLoginTime){
						waitNum++;
					}
				}
				
				//out.println("u="+u+",phonetypeMap.size()="+phonetypeMap.size()+",al1.size()="+al1.size()+"<br>");
				if(waitNum > 0 && phonetypeMap.size() <= 1 && macMap.size() <= 1 && gpuMap.size() <= 1){
					userListResult.add(u);
				}
			}
		}
		
		//
		caList2 = new ArrayList<ClientAuthorization> ();
		ceList2 = new ArrayList<ClientEntity> ();
		for(int i = 0 ; i < caList.size() ; i++){
			ClientAuthorization ca  = caList.get(i);
			ClientEntity ce  = ceList.get(i);
			String u = ca.getUsername();
			
			if(userListResult.contains(u)){
				caList2.add(ca);
				ceList2.add(ce);
			}
		}
	}
		
	
%>
<h2>可能存在授权问题的玩家，一共<%= totalNum %>个,此次查询已删除<%=deleteNum %><%= filter?"(过滤状态)":"" %></h2>
<form><input type='hidden' name='action' value='query'>
从<input type='text' name='start' value='<%=start %>'>条到<input type='text' name='end' value='<%=end %>'>条，按授权时间倒序
<input type='submit' value='查询'>
</form> | <form><input type='hidden' name='action' value='filter'>
<input type='submit' value='在结果集中搜索单个机型无法登录的玩家'>
</form>
<%
	if(filter){
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < userListResult.size() ; i++){
			sb.append(userListResult.get(i)+",");
		}
%>
	<table border="0" cellpadding="0" cellspacing="0" width="100%" bgcolor="#FFFFFF">
	<tr align='right'><td align='right' width='100%'>
	<form method="post" action="xiugaiUserClientMsg_check.jsp">
		<input type='hidden' name='action' value='filter_delete_all'>
		<input type='hidden' name='userList' value='<%=sb %>'>
		<input type='submit' value='删除所有的项目'>
	</form>
	</td></tr>
	</table>
<%
	}
%>	
	<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000">
		<tr bgcolor="#00FFFF" align="center">
			<td>用户名</td>
			<td>授权状态</td>
			<td>授权方式</td>
			<td>授权时间</td>
			<td>最后登录时间</td>
			<td>最后登录IP</td>
			<td>登录次数</td>
			<td>请求授权次数</td>
			<td>客户端ID</td>
			<td>MAC地址</td>
			<td>平台</td>
			<td>机型</td>
			<td>GPU型号</td>
			<td>客户端创建时间</td>
			<td>授权客户端ID</td>
			<td>删除</td>
		</tr>
		<%
			String aa[] = new String[]{"等待", "正常", "被替代", "异常", "拒绝", "等待过期"};
			String bb[] = new String[]{"自动授权", "手动授权或拒绝"};
			String lastShowUsername = null;
			String bgcolor = "#FFFFFF";
			for (int i = 0 ; i< caList2.size(); i++) {
				ClientAuthorization ca = caList2.get(i);
				
				ClientEntity ce = ceList2.get(i);
								
				String mac = "-";							//mac地址
				
				String platform = "-";					//平台
				
				String phoneType = "-";					//机型
				
				String gpuType = "-";						//GPU类型
				
				long createTime = 0;		//创建时间
				
				if(ce != null){
					mac = ce.getMac();
					platform = ce.getPlatform();
					phoneType = ce.getPhoneType();
					gpuType = ce.getGpuType();
					createTime = ce.getCreateTime();
				}
				
				if(lastShowUsername != null && ca.getUsername().equals(lastShowUsername) == false){
					if(bgcolor.equals("#FFFFFF")){
						bgcolor="#FFeeBB";
					}else{
						bgcolor="#FFFFFF";
					}
				}
				lastShowUsername = ca.getUsername();
				
		%>
			<tr bgcolor="<%= bgcolor %>" align="center">
				<td><%=ca.getUsername() %></td>
				<td><%=aa[ca.getAuthorizeState()] %></td>
				<td><%=bb[ca.getAuthorizeType()] %></td>
				<td><%=DateUtil.formatDate(new Date(ca.getAuthorizeTime()), "yyyy-MM-dd HH:mm:ss") %></td>
				<td><%=DateUtil.formatDate(new Date(ca.getLastLoginTime()), "yyyy-MM-dd HH:mm:ss") %></td>
				<td><%=ca.getIp() %></td>
				<td><%=ca.getLoginNum() %></td>
				<td><%=ca.getQueryNum() %></td>
				<td><%=ca.getClientID() %></td>
				<td><%=mac %></td>
				<td><%=platform %></td>
				<td><%=phoneType %></td>
				<td><%=gpuType %></td>
				<td><%= DateUtil.formatDate(new Date(createTime), "yyyy-MM-dd HH:mm:ss")  %></td>
				<td><%=ca.getAuthorizeClientId() %></td>
				<td><form action=""><input type="hidden" name="action" value="delete"><input type="hidden" name="filter" value="<%= filter %>"><input type="hidden" name="username" value="<%=ca.getUsername() %>"><input type="hidden" name="clientid" value="<%= ca.getClientID() %>"><input type="submit" value="删除"></form></td>
			</tr>
		<%
			}
		%>
	</table>
	
	
	<br>
</body>
</html>
