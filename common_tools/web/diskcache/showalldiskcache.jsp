<%@ page language="java" pageEncoding="GBK" contentType="text/html;charset=GBK" import="java.util.*,com.xuanzhi.tools.cache.diskcache.concrete.*"%><%@ page import="com.xuanzhi.tools.text.StringUtil" %><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>Show All List Cache</title>

</head>
<%
	AbstractDiskCache caches[] = DiskCacheHelper.getAllDiskCache();

	String action = request.getParameter("action");
	String message = null;
	if("clear".equals(action)){
		int id = Integer.parseInt(request.getParameter("cache"));
		caches[id].clear();
		message = "Cache["+caches[id].getName()+"] 清空成功！";
	}else if("destory".equals(action)){
		int id = Integer.parseInt(request.getParameter("cache"));
		caches[id].destory();
		message = "Cache["+caches[id].getName()+"] 清除成功！";
	}

	caches = DiskCacheHelper.getAllDiskCache();

	long diskSize = 0;
	long memorySize = 0;
	int elementNum = 0;
	for(int i = 0 ; i < caches.length ; i++){
		diskSize += caches[i].getCurrentDiskSize();
		memorySize += caches[i].getCurrentMemorySize();
		elementNum += caches[i].getNumElements();
	}
%>
<body>
<br/><center><b>Total Cache Num: <%=caches.length%>, Element Num: <%=StringUtil.addcommas(elementNum)%>, Disk Size: <%=StringUtil.addcommas(diskSize) %>, Memory Size: <%=StringUtil.addcommas(memorySize) %></b></center><br/>
<%
	if(message != null){
		out.println("<b>"+message+"</b>，点击<a href='./showalldiskcache.jsp'>这里</a>刷新页面<br/>");
	}
%>
<table border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#000000" width="100%">
<tr align="center" bgcolor="#EE8E9A">
	<td>名称</td>
	<td width='50%'>数据文件示意图</td>
	<td>命中率</td>
	<td>元素个数</td>
	<td title='数据快/空块'>数据块个数</td>
	<td title='当前/最大'>内存(K)</td>
	<td title='当前/文件/最大'>磁盘(M)</td>
	<td>操作</td>
</tr>
<%
	for(int i = 0 ; i < caches.length ; i++){
		long totalHits = caches[i].getCacheHits() + caches[i].getCacheMisses();
		if(totalHits == 0) totalHits = 1;
		
		long fileLength = caches[i].getDataFile().length()/(1024*1024);
		
		%><tr align="center" bgcolor="#FFFFFF">
	<td><a href='./showdiskcache.jsp?cache=<%=i%>'><%=caches[i].getName() %></a></td>
	<td>
	<table border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" width="100%" height="100%">
	<tr>
		<%
			DataBlock dbs[] = caches[i].getDataBlocks();
			long totalLen  = 1;
			if(dbs.length > 0) 
				totalLen = dbs[dbs.length-1].offset+dbs[dbs.length-1].length;
				
			DataBlock current = null;
			ArrayList<DataBlock> al = new ArrayList<DataBlock>();
			for(int j = 0 ; j < dbs.length ; j++){
				if(current == null){
					current = dbs[j];
				}else{
					if(dbs[j].containsData == current.containsData){
						current.length = current.length + dbs[j].length;
					}else{
						if(current.length < totalLen * 0.005){
							dbs[j].offset = current.offset;
							dbs[j].length = current.length + dbs[j].length;
						}else{
							al.add(current);
						}
						current = dbs[j];
					}
				}
			}
			if(current != null)
				al.add(current);
			
			dbs = al.toArray(new DataBlock[0]);
			al.clear();
			current = null;
			for(int j = 0 ; j < dbs.length ; j++){
				if(current == null){
					current = dbs[j];
				}else{
					if(dbs[j].containsData == current.containsData){
						current.length = current.length + dbs[j].length;
					}else{
						al.add(current);
						current = dbs[j];
					}
				}
			}
			if(current != null)
				al.add(current);
				
			
			for(int j = 0 ; j < al.size() ; j++){
				DataBlock db = al.get(j);
				String color = "#98EAFC";
				if(db.containsData == false) color="#6700FF";
				float rate = db.length * 100f/totalLen;
				out.println("<td bgcolor='"+color+"' width='"+rate+"%'><img src='./empty.gif' width='1'/></td>");
			}
		%>
	</tr>
	</table>
	</td>
	<td><%=caches[i].getCacheHits()*100/totalHits%>%</td>
	<td><%=StringUtil.addcommas(caches[i].getNumElements()) %></td>
	<td><%=StringUtil.addcommas(caches[i].getDataBlockNum())+"/"+ StringUtil.addcommas(caches[i].getFreeBlockNum())%></td>
	<td><%=StringUtil.addcommas(caches[i].getCurrentMemorySize()/1024)+"/"+StringUtil.addcommas(caches[i].getMaxMemorySize()/1024) %></td>
	<td><%=StringUtil.addcommas(caches[i].getCurrentDiskSize()/(1024*1024))+"/"+StringUtil.addcommas(fileLength)+"/"+StringUtil.addcommas(caches[i].getMaxDiskSize()/(1024*1024)) %></td>
	<td><a href="./showalldiskcache.jsp?action=clear&cache=<%=i%>">清空</a>|<a href="./showalldiskcache.jsp?action=destory&cache=<%=i%>">清除</a></td>
</tr><%
	}
%>	
</table>	
</body>
</html>
