<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*"%>
<%

		
//String beanName = "outer_gateway_connection_selector";
MieshiPlayerInfoManager mm = MieshiPlayerInfoManager.getInstance();
	
String username = request.getParameter("username");


	
%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache"%>
<%
/* 		Class cl = MieshiPlayerInfoManager.class;
		Field f = cl.getDeclaredField("cache");
		f.setAccessible(true);
		DefaultDiskCache cache = (DefaultDiskCache)f.get(mm);
		ArrayList<MieshiPlayerInfo> al = (ArrayList<MieshiPlayerInfo>)cache.get(username);
		if(al != null){
			for(int i = al.size() - 1; i >= 0; i--){
				if(al.get(i) == null){
					al.remove(i);
				}
			}
			cache.put(username,al);
		} */
		MieshiPlayerInfo pis[] = mm.getMieshiPlayerInfoByUsername(username);
		MieshiServerInfoManager msm = MieshiServerInfoManager.getInstance();
		List<MieshiPlayerInfo> lst = new ArrayList<MieshiPlayerInfo>();
		for(MieshiPlayerInfo mpi : pis)
		{
			MieshiServerInfo ms = msm.getServerInfoByName(mpi.getServerRealName());
			if(ms != null)
			{
				if(ms.getServerType() == MieshiServerInfo.SERVERTYPE_苹果正式服务器)
				{
					lst.add(mpi);
				}
			}
		}
		%>
		<%
		String jsons = "";
		jsons = JsonUtil.jsonFromObject(lst.toArray());
		out.println(jsons);
%>

