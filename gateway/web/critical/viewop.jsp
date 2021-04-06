<%@page import="java.util.ArrayList"%>
<%@page import="java.io.File"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.DiskCache"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%
DiskCache opCache = new DefaultDiskCache(new File("/home/game/resin_gateway/webapps/game_gateway/WEB-INF/diskCacheFileRoot/op.ddc"), "操作日志", 1000L*60*60*24*MieshiGatewaySubSystem.CACHE_DISTIME);
ArrayList arrayList = (ArrayList)opCache.get("oplogstr");



%>
<script>
function overTag(tag){
	tag.style.color = "red";	
//		tag.style.fontWeight = "600";
//		tag.style.backgroundColor = "#E9E4E4";
}
			
function outTag(tag){
	tag.style.color = "black";
//		tag.style.fontWeight = "300";
//		tag.style.backgroundColor = "white";
}	
</script>
<table style="font-size: 12px;text-align: center;width: 100%">
<tr bgcolor="#00FFFF" align="center">
<td>操作时间</td><td>操作人</td><td>操作</td><td>操作页面</td><td>备注</td>
</tr>
<%if(arrayList != null){ 
	for(int i=0;i<arrayList.size();i++)
	{

		String infos = ((StringBuffer)arrayList.get(i)).toString();
		if(infos != null)
		{
			String[] ss = infos.split("@@");
			if(ss.length == 5)
			{
%>
	<tr bgcolor='#FFFFFF' align='center' onmouseover = 'overTag(this);' onmouseout = 'outTag(this);'><td><%=ss[0] %></td><td><%=ss[1] %></td><td><%=ss[2] %></td><td><%=ss[3] %></td><td><%=ss[4] %></td></tr>
<%
			}
		}
	}
}
%>
</table>