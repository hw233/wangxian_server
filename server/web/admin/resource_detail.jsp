<%@page contentType="text/html;charset=utf-8" import="com.fy.engineserver.operating.*,com.xuanzhi.gameresource.*,java.util.*"%>

<%@page import="com.fy.engineserver.core.CoreSubSystem"%><%@include file="IPManager.jsp" %><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title></title>
		<script language="javascript">
		</script>
	</head>
	<body>
		<%
			String resourceIdStr = request.getParameter("resourceId");
			
			int resourceId = -1;
			try{
				resourceId = Integer.parseInt(resourceIdStr);
			}catch(Exception e){
			}
			
			out.println("<table border=\"1\">");
			
			DefaultDatabaseManager ddm = CoreSubSystem.getInstance().getResourceManager();
			
			StringBuilder sb = new StringBuilder();
			if(resourceId == Constants.对应关系){
				MappingData[] mds = ddm.getAllMappingDatas();
				int numBytes = 0;
				for(int i = 0 ; i < mds.length ;i ++){
					MappingData md = mds[i];
					numBytes += md.data.length;
				}
				
				sb.append("数量"+mds.length+" 个 ，总大小 "+numBytes+" 字节.<br/>");
				
				sb.append("<tr><td>编号</td><td>内容长度(字节)</td><td>版本号</td></tr>");
				for(int i = 0 ; i < mds.length ;i ++){
					MappingData md = mds[i];
					sb.append("<tr><td>"+md.id+"</td><td>"+md.data.length+"</td><td>"+md.version+"</td></tr>");
				}
			}else if(resourceId == Constants.地表数据){
				DoodadLandData[] lds = ddm.getAllDoodadLandDatas();
				
				int numBytes = 0;
				for(int i = 0 ; i < lds.length ;i ++){
					DoodadLandData md = lds[i];
					numBytes += md.data.length;
				}
				
				sb.append("数量"+lds.length+" 个 ，总大小 "+numBytes+" 字节.<br/>");
				
				sb.append("<tr><td>编号</td><td>内容长度(字节)</td><td>版本号</td></tr>");
				for(int i = 0 ; i < lds.length ;i ++){
					DoodadLandData md = lds[i];
					sb.append("<tr><td>"+md.id+"</td><td>"+md.data.length+"</td><td>"+md.version+"</td></tr>");
				}
			}else if(resourceId == Constants.地物数据){
				DoodadBuildingData[] bds = ddm.getAllMDoodadBuildingDatas();
				
				int numBytes = 0;
				for(int i = 0 ; i < bds.length ;i ++){
					DoodadBuildingData md = bds[i];
					numBytes += md.data.length;
				}
				
				sb.append("数量"+bds.length+" 个 ，总大小 "+numBytes+" 字节.<br/>");
				
				sb.append("<tr><td>编号</td><td>内容长度(字节)</td><td>版本号</td></tr>");
				for(int i = 0 ; i < bds.length ;i ++){
					DoodadBuildingData md = bds[i];
					sb.append("<tr><td>"+md.id+"</td><td>"+md.data.length+"</td><td>"+md.version+"</td></tr>");
				}
				
			}else if(resourceId == Constants.Avatar动画数据){
				AvatarAnimation[] ads = ddm.getAllAvatarAnimations();
				
				int numBytes = 0;
				for(int i = 0 ; i < ads.length ;i ++){
					AvatarAnimation ad = ads[i];
					numBytes += ad.data.length;
				}
				
				sb.append("数量"+ads.length+" 个 ，总大小 "+numBytes+" 字节.<br/>");
				
				sb.append("<tr><td>编号</td><td>内容长度(字节)</td><td>版本号</td></tr>");
				for(int i = 0 ; i < ads.length ;i ++){
					AvatarAnimation md = ads[i];
					sb.append("<tr><td>"+md.id+"</td><td>"+md.data.length+"</td><td>"+md.version+"</td></tr>");
				}
			}else if(resourceId == Constants.部件数据){
				PartData[] pds = ddm.getAllPartDatas();
				
				int numBytes = 0;
				for(int i = 0 ; i < pds.length ;i ++){
					PartData ad = pds[i];
					numBytes += ad.data.length;
				}
				
				sb.append("数量"+pds.length+" 个 ，总大小 "+numBytes+" 字节.<br/>");
				
				sb.append("<tr><td>编号</td><td>内容长度(字节)</td><td>版本号</td></tr>");
				for(int i = 0 ; i < pds.length ;i ++){
					PartData md = pds[i];
					sb.append("<tr><td>"+md.id+"</td><td>"+md.data.length+"</td><td>"+md.version+"</td></tr>");
				}
			}else if(resourceId == Constants.普通动画数据){
				NormalAnimation[] pds = ddm.getAllNormalAnimations();
				
				int numBytes = 0;
				for(int i = 0 ; i < pds.length ;i ++){
					NormalAnimation ad = pds[i];
					numBytes += ad.data.length;
				}
				
				sb.append("数量"+pds.length+" 个 ，总大小 "+numBytes+" 字节.<br/>");
				
				sb.append("<tr><td>编号</td><td>内容长度(字节)</td><td>版本号</td></tr>");
				for(int i = 0 ; i < pds.length ;i ++){
					NormalAnimation md = pds[i];
					sb.append("<tr><td>"+md.id+"</td><td>"+md.data.length+"</td><td>"+md.version+"</td></tr>");
				}
			}else if(resourceId == Constants.低端部件数据){
				PartData[] pds = ddm.getAllLowPartDatas();
				
				int numBytes = 0;
				for(int i = 0 ; i < pds.length ;i ++){
					PartData ad = pds[i];
					numBytes += ad.data.length;
				}
				
				sb.append("数量"+pds.length+" 个 ，总大小 "+numBytes+" 字节.<br/>");
				
				sb.append("<tr><td>编号</td><td>内容长度(字节)</td><td>版本号</td></tr>");
				for(int i = 0 ; i < pds.length ;i ++){
					PartData md = pds[i];
					sb.append("<tr><td>"+md.id+"</td><td>"+md.data.length+"</td><td>"+md.version+"</td></tr>");
				}
			}else if(resourceId == Constants.中低端部件数据){
				PartData[] pds = ddm.getAllMidPartDatas();
				
				int numBytes = 0;
				for(int i = 0 ; i < pds.length ;i ++){
					PartData ad = pds[i];
					numBytes += ad.data.length;
				}
				
				sb.append("数量"+pds.length+" 个 ，总大小 "+numBytes+" 字节.<br/>");
				
				sb.append("<tr><td>编号</td><td>内容长度(字节)</td><td>版本号</td></tr>");
				for(int i = 0 ; i < pds.length ;i ++){
					PartData md = pds[i];
					sb.append("<tr><td>"+md.id+"</td><td>"+md.data.length+"</td><td>"+md.version+"</td></tr>");
				}
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
				
				sb.append("数量"+pds.length+" 个 ，总大小 "+numBytes+" 字节.<br/>");
				
				sb.append("<tr><td>编号</td><td>内容长度(字节)</td><td>版本号</td><td>路径</td></tr>");
				for(int i = 0 ; i < pds.length ;i ++){
					ImageData md = pds[i];
					sb.append("<tr><td>"+md.id+"</td><td>"+md.data.length+"</td><td>"+md.version+"</td><td>"+java.util.Arrays.toString(ddm.getFilePathsByImageDataId((byte)resourceId , md.id))+"</td></tr>");
				}
			}else{
				
			}
			
			out.println(sb.toString());
			
			out.println("<table>");
		%>
	</body>
</html>
