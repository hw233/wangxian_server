<%@page import="com.fy.boss.gm.dajin.DaJinManager"%>
<%@page import="com.fy.boss.gm.dajin.DaJinMember"%>
<%@page import="com.fy.boss.gm.gmuser.NoticeTimeTypeDay"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.boss.gm.gmuser.GmSystemNotice"%>
<%@page import="com.fy.boss.gm.gmuser.server.GmSystemNoticeManager"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="java.util.Date"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="com.fy.boss.gm.record.TelRecord"%>
<%@page import="com.fy.boss.gm.record.TelRecordManager"%>
<%@page import="com.fy.boss.gm.record.ActionManager"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.gm.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="style.css"/>
<script type='text/javascript' src='jquery-1.6.2.js'></script>
<script src="sortable.js" type="text/javascript"></script> 
<title>打金网络</title>
</head>
<script language="javascript"> 
	function openDialogue(index){
		window.open(index);		
	}
	
</script>
<body bgcolor="#c8edcc">
	<%
		Object obj = session.getAttribute("authorize.username");
		DaJinManager dm = DaJinManager.getInstance();
		List<DaJinMember> list = dm.getList();
		String downCount = request.getParameter("downCount");
		String layerCount = request.getParameter("layerCount");
		List<DaJinMember> list2 = new ArrayList<DaJinMember>();
		XmlServerManager smm = XmlServerManager.getInstance();
		
		boolean iscommit = false;
		if(downCount!=null && downCount.matches("\\d+") && "".equals(layerCount)){
			iscommit = true;
			for(int i=0;i<list.size();i++){
				if(list.get(i).getDownNums()>=Integer.parseInt(downCount)){
					list2.add(list.get(i));
				}
			}
		}
		else if(layerCount!=null && layerCount.matches("\\d+") && "".equals(downCount)){
			iscommit = true;
			for(int i=0;i<list.size();i++){
				if(list.get(i).getMaxLayers()>=Integer.parseInt(layerCount)){
					list2.add(list.get(i));
				}
			}
		}else if(layerCount!=null && layerCount.matches("\\d+") && downCount!=null && downCount.matches("\\d+")){
			iscommit = true;
			for(int i=0;i<list.size();i++){
				if(list.get(i).getMaxLayers()>=Integer.parseInt(layerCount) && list.get(i).getDownNums()>=Integer.parseInt(downCount)){
					list2.add(list.get(i));
				}
			}
		}
		
		if(iscommit){
			list = list2;
		}
		
	%>
	<form method="post">
	<table>
		 <h2>银子汇聚组织网络</h2> 
			<tr>
				<th>过滤器1:</th>
				<td>单层下线超过<input type=text name=downCount size=10>(最小为3)</td>
			</tr>
			<tr>
				<th>过滤器2:</th>
				<td>总层级超过<input type=text name=layerCount size=10>(最小为2)</td>
			</tr>
			<tr>
				<td><input type='submit'  value="提取"></td>
			</tr>
 	</table>
 	</form>
 	<hr>
 	<table id="tableId" align="center" cellpadding="0" cellspacing="0" border="0"
				class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow"><caption>数量：<%=list.size() %></caption>
			<thead> 
				<tr>
					<th >
						<b>关系Id</b>
					</th>
					<th onclick="sortAble('tableId', 1,'int')"    
                         style="cursor:pointer">单层次最大下线<img height="20" width="20" src="images/jt.png"/></th>  
                    <th onclick="sortAble('tableId', 2, 'int')"  
                         style="cursor:pointer">最大层次<img height="20" width="20" src="images/jt.png"/></th>  
                    <th onclick="sortAble('tableId', 3, 'int')"   
                         style="cursor:pointer">最大层次 x 单层次最大下线<img height="20" width="20" src="images/jt.png"/></th>  
					<th >
						<b>累计汇集银两(两)</b>
					</th>
					<th >
						<b>服务器</b>
					</th>
					<th >
						<b>状态</b>
					</th>
					<th>
						<b>操作</b>
					</th>
				</tr>
			</thead> 
			<%
				if(list.size()>0){
					for(DaJinMember dme:list){
						try{
							String url = smm.getXmlServerByName(dme.getServerName().trim()).getUri();
							String newurl = url.substring(0,url.indexOf("gm"))+"admin/smith/mm_detail.jsp?id="+dme.getId();
							%>
							<tr><td><%=dme.getId() %></td><td><%=dme.getDownNums() %></td><td><%=dme.getMaxLayers() %></td><td><%=dme.getDownNums()*dme.getMaxLayers() %></td><td><%=dme.getSilerCounts() %></td><td><%= dme.getServerName()%></td><td><%= dme.getStatus()%></td><td><a href='javascript:openDialogue("<%=newurl%>")'>查看详情</a></td></tr>
						<%			
						}catch(Exception e){
							out.print(dme.getServerName()+"--"+e);
						}
					}
				}
			%>
	</table>
</body>
</html>

