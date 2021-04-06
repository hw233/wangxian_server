<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.IOException"%>
<%@page import="java.net.URL"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.jspsmart.upload.SmartUpload"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="../css/style.css" /> 
<title>资源更新</title>
<%@include file="gameResourceUpdateTool_config.jsp" %>
</head>
	<body bgcolor='#c1c1c0' style="azimuth: center;">
	<%!
	public class StreamGobbler extends Thread {  
		  
	    InputStream is;  
	    String type;  
	  
	    public StreamGobbler(InputStream is, String type) {  
	        this.is = is;  
	        this.type = type;  
	    }  
	  
	    public void run() {  
	        try {  
	            InputStreamReader isr = new InputStreamReader(is);  
	            BufferedReader br = new BufferedReader(isr);  
	            String line = null;  
	            while ((line = br.readLine()) != null) {  
	                if (type.equals("Error")) {  
	                    System.out.println("Error   :" + line);  
	                    NewFeedbackManager.logger.warn("Error:"+line);
	                } else {  
	                    System.out.println("Debug:" + line);  
	                    NewFeedbackManager.logger.warn("Debug:"+line);
	                }  
	            }  
	        } catch (IOException ioe) {  
	            ioe.printStackTrace(); 
	            NewFeedbackManager.logger.warn("异常:"+ioe);
	        }  
	    }  
	}  
	%>
		<%
			Object o = new String("cl");//session.getAttribute("authorize.username");
			if(o==null && !GameConstants.getInstance().getServerName().equals("潘多拉星")){
				out.print("请重新登录再上传");
				return;
			}
			SmartUpload mySmartUpload = new SmartUpload();
			boolean isstart = false;
			boolean isupload = false;
			String uploadfile = "";
			String action = request.getParameter("action");
			String path = request.getRealPath("/");
			String servername = GameConstants.getInstance().getServerName();
			if(action!=null && action.equals("restart")){
				try{
					String shell = path.replace("webapps/game_server/", "") +"bin/httpd.sh restart";
					out.print(recordLog(o.toString(), System.currentTimeMillis(), "重启", uploadfile));
					Process process = Runtime.getRuntime().exec(shell);
					//读取，清空执行命令所产生的输出
	 				BufferedReader inputBufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	 		            String line = null;
	 		            while ((line = inputBufferedReader.readLine()) != null) {
	 		                out.println(line);
	 		                NewFeedbackManager.logger.warn(line);
	 		            }
						StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "Error");  
						StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "Output");  
						errorGobbler.start();  
						outputGobbler.start();  
						process.waitFor(); 
			            String url = "http://116.213.192.200:8110/game_boss/viewalllog.jsp?sid="+servername+"&logname=stdout.log&linenum=25";
						out.print("<script>window.location.href=\""+url+"\";</script>");
				}catch(Exception e){
					e.printStackTrace();
					NewFeedbackManager.logger.warn("[jspRestart] [exception] ["+e+"]");
				}
// 				response.sendRedirect("http://116.213.192.200:8110/game_boss/viewalllog.jsp?sid="+servername+"&logname=stdout.log&linenum=25");	 	
			}else if(action!=null && action.equals("isupload")){
				uploadfile= request.getParameter("uploadfile");
				if(uploadfile!=null && uploadfile.length()>0){
					out.print("【"+uploadfile+"】正在监测。。");
					if(uploadfile.equals("article.xls")){
						
					}else if(uploadfile.equals("shops.xls")){
						
					}
					out.print(recordLog(o.toString(), System.currentTimeMillis(), "检查", uploadfile));
				}else{
					out.print("监测的数据文件为空！");	
				}
			}else if(action!=null && action.equals("upload")){
				try{
// 					String url = "http://116.213.192.200:8110/game_boss/viewalllog.jsp?sid="+servername+"&logname=stdout.log&linenum=25";
// 					out.print("<script>window.location.href=\""+url+"\";</script>");
					//response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
					//response.setHeader("Location", url);
					//byte[]b=HttpUtils.webGet(new URL("http://116.213.192.200:8110/game_boss/viewalllog.jsp?sid="+servername+"&logname=stdout.log&linenum=25"), new HashMap(), 10000, 10000);
					
					// 					response.sendRedirect("http://116.213.192.200:8110/game_boss/viewalllog.jsp?sid="+servername+"&logname=stdout.log&linenum=25"); 	
					mySmartUpload.initialize(pageContext);
					mySmartUpload.setMaxFileSize(Long.parseLong("5000000000"));
					mySmartUpload.setTotalMaxFileSize(Long.parseLong("5000000000"));
					mySmartUpload.setAllowedFilesList("txt,xml,xls,xlsx");
					mySmartUpload.upload();
					if(mySmartUpload.getFiles().getSize()==0){
						out.print("<font color='red'><b>请选择要上传的文件<b></font>");
						return;
					}
					
					//针对不同服务器,不同目录
					String saveurl = request.getRealPath("/")+"WEB-INF/game_init_config";
					boolean upsucc = true;
					
					for (int i = 0; i < mySmartUpload.getFiles().getCount(); i++) {
						com.jspsmart.upload.File myFile = mySmartUpload.getFiles().getFile(i);
						if (!myFile.isMissing()) {
							String filename = myFile.getFilePathName();
							String pathname = paths.get(filename);
							if(pathname!=null){
								saveurl += pathname + filename;
							}else{
								out.print("<font color='red'><b>传的表["+filename+"]未做处理，请联系相关人员处理。<b></font>");
								upsucc = false;
							}
							
							if(upsucc){
								uploadfile = filename;
								myFile.saveAs(saveurl, mySmartUpload.SAVE_PHYSICAL);
// 								isupload = true;
								isstart = true;
								out.print(recordLog(o.toString(), System.currentTimeMillis(), "上传", uploadfile));
							}
						}
					}
				}catch(SecurityException e){
					out.print("<font color='red'><b>上传的文件类型限制<b></font>");
				}catch(Exception e2){
					out.print("异常："+e2);
				}
			}
		%>
		<%
			if(isupload){
				%>
				<form method="post" action="gameResourceUpdateTool_handle.jsp?action=isupload&uploadfile=<%=uploadfile%>">
					<table>
						<tr>
							<td><input type="submit" value="数据检查"></td>
						</tr>
					</table>
				</form>
				<%
			}
		
			if(isstart){
			%>
				<form method="post" action="gameResourceUpdateTool_handle.jsp?action=restart">
					<table>
						<tr>
							<td><input type="submit" value="重启"></td>
						</tr>
					</table>
				</form>
			<%
			}
		%>
		
	</body>
</html>
