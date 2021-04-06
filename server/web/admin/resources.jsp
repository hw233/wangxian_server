<%@page contentType="text/html;charset=utf-8" import="com.fy.engineserver.operating.*,com.xuanzhi.gameresource.*"%>

<%@page import="com.fy.engineserver.core.CoreSubSystem"%><%@include file="IPManager.jsp" %><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title></title>
		<%
			DefaultDatabaseManager ddm = CoreSubSystem.getInstance().getResourceManager();
		%>
		<script language="javascript">
		</script>
	</head>
	<body>
		<a href="resources.jsp">刷新页面</a>
		<table border="1">
		<tr>
			<td>资源类型</td>
			<td>版本号</td>
			<td>数量</td>
			<td>数据长度</td>
		</tr>
		<%
			for(int j = 0 ;j <= 18 ;j ++){
				out.println("<tr>");
				out.println("<td><a href=\"resource_detail.jsp?resourceId="+j+"\">"+Constants.RESOURCE_NAMES[j]+"</a></td>");
				out.println("<td>"+ddm.getVersion((byte)j)+"</td>");
				
				int resourceId = j;
				
				StringBuilder sb = new StringBuilder();
				if(resourceId == Constants.对应关系){
					MappingData[] mds = ddm.getAllMappingDatas();
					int numBytes = 0;
					for(int i = 0 ; i < mds.length ;i ++){
						MappingData md = mds[i];
						numBytes += md.data.length;
					}
					
					out.println("<td>"+mds.length+"</td>");
					out.println("<td>"+numBytes+"字节/"+String.format("%.2f",numBytes * 1.0 / 1024)+" K"+"</td>");
				}else if(resourceId == Constants.地表数据){
					DoodadLandData[] lds = ddm.getAllDoodadLandDatas();
					
					int numBytes = 0;
					for(int i = 0 ; i < lds.length ;i ++){
						DoodadLandData md = lds[i];
						numBytes += md.data.length;
					}
					
					out.println("<td>"+lds.length+"</td>");
					out.println("<td>"+numBytes+"字节/"+String.format("%.2f",numBytes * 1.0 / 1024)+" K"+"</td>");
				}else if(resourceId == Constants.地物数据){
					DoodadBuildingData[] bds = ddm.getAllMDoodadBuildingDatas();
					
					int numBytes = 0;
					for(int i = 0 ; i < bds.length ;i ++){
						DoodadBuildingData md = bds[i];
						numBytes += md.data.length;
					}
					out.println("<td>"+bds.length+"</td>");
					out.println("<td>"+numBytes+"字节/"+String.format("%.2f",numBytes * 1.0 / 1024)+" K"+"</td>");
					
				}else if(resourceId == Constants.Avatar动画数据){
					AvatarAnimation[] ads = ddm.getAllAvatarAnimations();
					
					int numBytes = 0;
					for(int i = 0 ; i < ads.length ;i ++){
						AvatarAnimation ad = ads[i];
						numBytes += ad.data.length;
					}
					out.println("<td>"+ads.length+"</td>");
					out.println("<td>"+numBytes+"字节/"+String.format("%.2f",numBytes * 1.0 / 1024)+" K"+"</td>");
				}else if(resourceId == Constants.部件数据){
					PartData[] pds = ddm.getAllPartDatas();
					
					int numBytes = 0;
					for(int i = 0 ; i < pds.length ;i ++){
						PartData ad = pds[i];
						numBytes += ad.data.length;
					}
					out.println("<td>"+pds.length+"</td>");
					out.println("<td>"+numBytes+"字节/"+String.format("%.2f",numBytes * 1.0 / 1024)+" K"+"</td>");
				}else if(resourceId == Constants.普通动画数据){
					NormalAnimation[] pds = ddm.getAllNormalAnimations();
					
					int numBytes = 0;
					for(int i = 0 ; i < pds.length ;i ++){
						NormalAnimation ad = pds[i];
						numBytes += ad.data.length;
					}
					out.println("<td>"+pds.length+"</td>");
					out.println("<td>"+numBytes+"字节/"+String.format("%.2f",numBytes * 1.0 / 1024)+" K"+"</td>");
				}else if(resourceId == Constants.低端部件数据){
					PartData[] pds = ddm.getAllLowPartDatas();
					
					int numBytes = 0;
					for(int i = 0 ; i < pds.length ;i ++){
						PartData ad = pds[i];
						numBytes += ad.data.length;
					}
					out.println("<td>"+pds.length+"</td>");
					out.println("<td>"+numBytes+"字节/"+String.format("%.2f",numBytes * 1.0 / 1024)+" K"+"</td>");
				}else if(resourceId == Constants.中低端部件数据){
					PartData[] pds = ddm.getAllMidPartDatas();
					
					int numBytes = 0;
					for(int i = 0 ; i < pds.length ;i ++){
						PartData ad = pds[i];
						numBytes += ad.data.length;
					}
					
					out.println("<td>"+pds.length+"</td>");
					out.println("<td>"+numBytes+"字节/"+String.format("%.2f",numBytes * 1.0 / 1024)+" K"+"</td>");
				}else if(resourceId == Constants.小动画图片 ||
						resourceId == Constants.地表图片 ||
						resourceId == Constants.地物图片 ||
						resourceId == Constants.Avatar图片 ||
						resourceId == Constants.图标图片 ||
						resourceId == Constants.小版图标图片 ||
						resourceId == Constants.低端Avatar图片 ||
						resourceId == Constants.中低端Avatar图片 ||
						resourceId == Constants.大版图标图片 ||
						resourceId == Constants.普通动画怪物图片 ||
						resourceId == Constants.普通动画非怪物图片){
					ImageData[] pds = ddm.getImageDatas((byte)resourceId);
					
					int numBytes = 0;
					for(int i = 0 ; i < pds.length ;i ++){
						ImageData ad = pds[i];
						numBytes += ad.data.length;
					}
					
					out.println("<td>"+pds.length+"</td>");
					out.println("<td>"+numBytes+"字节/"+String.format("%.2f",numBytes * 1.0 / 1024)+" K"+"</td>");
				}else{
					
				}
				
				out.println("</tr>");
			}
		%>
		</table>
	</body>
</html>
