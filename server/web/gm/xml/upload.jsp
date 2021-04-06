<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page
	import="com.fy.engineserver.datasource.career.*,java.io.*,java.util.ArrayList,com.jspsmart.upload.*,com.fy.engineserver.datasource.skill.*,com.fy.engineserver.datasource.buff.*,com.fy.engineserver.datasource.props.*,com.fy.engineserver.sprite.monster.*,com.fy.engineserver.sprite.npc.*,com.fy.engineserver.task.*,com.fy.engineserver.core.*,com.xuanzhi.tools.text.*,com.fy.engineserver.menu.WindowManager,com.fy.engineserver.datasource.repute.*,com.fy.engineserver.shop.*,org.apache.log4j.*,com.xuanzhi.boss.game.*,com.fy.engineserver.downcity.*,com.fy.engineserver.menu.question.*"%>
<%@page import="com.fy.engineserver.gm.record.StorageManager"%>
<%@include file="../authority.jsp" %>
<%@page import="com.fy.engineserver.operating.activities.SerialNumberAndMagicCardManager"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>上传数据页面</title>
	</head>
	<body>
		<%
			//服务器类型为0代表为可修改的开发服务器

			//计算文件上传个数
			StorageManager smanager = StorageManager.getInstance();
			SerialNumberAndMagicCardManager snam = SerialNumberAndMagicCardManager
					.getInstance();
			try {
				SmartUpload mySmartUpload = new SmartUpload();
				//SmartUpload的初始化，使用这个jspsmart一定要在一开始就这样声明
				mySmartUpload.initialize(pageContext);

				//限制每个上传附件的最大长度。 
				mySmartUpload.setMaxFileSize(Long.parseLong("500000000000"));

				//限制总上传数据的长度。 
				mySmartUpload.setTotalMaxFileSize(Long
						.parseLong("500000000000"));

				//设定允许上传的附件（通过扩展名限制）。 
				//		mySmartUpload.setAllowedFilesList("txt,xml,ncm,mcm");

				//依据form的内容上传
				mySmartUpload.upload();

				String str = (String) mySmartUpload.getRequest().getParameter(
						"FILE1");

				//将上传的文件一个一个取出来处理
				for (int i = 0; i < mySmartUpload.getFiles().getCount(); i++) {
					//取出一个文件
					com.jspsmart.upload.File myFile = mySmartUpload.getFiles()
							.getFile(i);

					//如果文件存在，则做存档操作
					if (!myFile.isMissing()) {
						String content = myFile.getContentString();

						String[] cs = content.split("\n");
						for (String s : cs) {
							if (s != null && s.contains(",")) {
								String ss[] = s.split(",");
								
								if (ss.length == 3) {
								     out.print(ss[0]+"|"+ss[1]+"|"+ss[2]);
									snam.addMagicCard(ss[0], ss[1], Integer
											.parseInt(ss[2].trim()));
								}
							}
						}
						myFile.saveAs(smanager.getStorageRoot()
								+ myFile.getFileName(),
								mySmartUpload.SAVE_PHYSICAL);
						RequestDispatcher rd = request
								.getRequestDispatcher("mgcard_manager.jsp?upload=success");
						rd.forward(request, response);
					}
				}
			} catch (Exception e) {
				//e.printStackTrace();
				out.print("上传失败");
				  out.print(StringUtil.getStackTrace(e));
				out
						.print("<input type='button' value='返回上传页面' onclick='window.location.replace(\"mgcard_manager.jsp?upload=fail\")' />");
			}
		%>

	</body>
</html>


