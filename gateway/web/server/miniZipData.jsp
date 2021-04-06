<%@page import="java.util.zip.ZipEntry"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.util.zip.ZipOutputStream"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.gamegateway.mieshi.resource.manager.ResourceData"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.fy.gamegateway.mieshi.resource.manager.ResourceManager"%>
<%@page import="com.fy.gamegateway.mieshi.resource.manager.ResourceMD5"%>
<%@page import="com.fy.gamegateway.mieshi.resource.manager.MiniResourceZipData"%>
<%@page import="com.fy.gamegateway.mieshi.resource.manager.MiniResourceZipManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<%
	String action = request.getParameter("action");
	if (action != null) {
		if (action.equals("addOne")) {
			String ver = request.getParameter("Fver");
			String channel = request.getParameter("channel");
			String plat = request.getParameter("platform");
			String gpu = request.getParameter("gpu");
			String url = request.getParameter("url");
			MiniResourceZipData data = MiniResourceZipManager.instance.getMiniResourceUrl(ver);
			if (data != null) {
				data.addOneChannel(channel, gpu, plat, url);
			}
			MiniResourceZipManager.diskCache.put(MiniResourceZipManager.DISK_CACHE_KEY, MiniResourceZipManager.miniDatas);
		}else if (action.equals("deleteOne")) {
			String ver = request.getParameter("Fver");
			String channel = request.getParameter("channel");
			String plat = request.getParameter("platform");
			String gpu = request.getParameter("gpu");
			MiniResourceZipData data = MiniResourceZipManager.instance.getMiniResourceUrl(ver);
			if (data != null) {
				data.getDownloadUrl().remove(data.createKey(channel, plat, gpu));
			}
			MiniResourceZipManager.diskCache.put(MiniResourceZipManager.DISK_CACHE_KEY, MiniResourceZipManager.miniDatas);
		}else if (action.equals("deleteAll")) {
			String ver = request.getParameter("Fver");
			String channel = request.getParameter("channel");
			MiniResourceZipData data = MiniResourceZipManager.instance.getMiniResourceUrl(ver);
			if (data != null) {
				if (channel == null || channel.length() == 0) {
					MiniResourceZipManager.miniDatas.remove(ver);
				}else {
					for (int i = 0; i < ResourceMD5.GPU_NAMES.length; i++) {
						data.getDownloadUrl().remove(data.createKey(channel, ResourceMD5.PLATFORM_NAMES[1], ResourceMD5.GPU_NAMES[i]));
					}
					for (int i = 0; i < ResourceMD5.GPU_NAMES.length; i++) {
						data.getDownloadUrl().remove(data.createKey(channel, ResourceMD5.PLATFORM_NAMES[0], ResourceMD5.GPU_NAMES[i]));
					}
					for (int i = 0; i < ResourceMD5.GPU_NAMES.length; i++) {
						data.getDownloadUrl().remove(data.createKey(channel, ResourceMD5.PLATFORM_NAMES[2], ResourceMD5.GPU_NAMES[i]));
					}
				}
			}
			MiniResourceZipManager.diskCache.put(MiniResourceZipManager.DISK_CACHE_KEY, MiniResourceZipManager.miniDatas);
		}else if (action.equals("addCTOC")) {
			String channelA = request.getParameter("channelA");
			String[] channelAs = channelA.split(",");
			String channelB = request.getParameter("channelB");
			for (int i = 0; i < channelAs.length; i++) {
				MiniResourceZipManager.cTOc.put(channelAs[i], channelB);
			}
			MiniResourceZipManager.diskCache.put(MiniResourceZipManager.DISK_CACHE_KEY_CTOC, MiniResourceZipManager.cTOc);
		}else if (action.equals("removeCTOC")) {
			String channelA = request.getParameter("channelA");
			String[] channelAs = channelA.split(",");
			for (int i = 0; i < channelAs.length; i++) {
				MiniResourceZipManager.cTOc.remove(channelAs[i]);
			}
			MiniResourceZipManager.diskCache.put(MiniResourceZipManager.DISK_CACHE_KEY_CTOC, MiniResourceZipManager.cTOc);
		}else if ("miniZip".equals(action)) {
			String re = MiniResourceZipManager.instance.createMiniResourceZip();
			out.println(re + "<br>");
		}
	}
%>

<form name="f0">
<input type='hidden' name='action' value='miniZip'>
<input type="submit" value="根据测试资源包和正式资源包生成miniZip包">
</form>
<br/>

<h2>miniZip共享节点</h2>
<br><a href="./miniZipData.jsp">刷新此页面</a><br>
<br>
添加一个
<form>
	<input type="hidden" name="action" value="addOne">
	起始版本:<input type="text" name="Fver">
	渠道:<input type="text" name="channel">
	平台(Android):<input type="text" name="platform", value="Android">
	<br>
	GPU("pvr_res",attic_res,etc_res,png_res, dxt3_res, etc_general, wdds_res):<input type="text" name="gpu">
	url:<input type="text" name="url">
	<input type="submit" value="确定">
</form>
<br><br>
删除一个
<form>
	<input type="hidden" name="action" value="deleteOne">
	起始版本:<input type="text" name="Fver">
	渠道:<input type="text" name="channel">
	平台(Android):<input type="text" name="platform", value="Android">
	<br>
	GPU("pvr_res",attic_res,etc_res,png_res, dxt3_res, etc_general, wdds_res):<input type="text" name="gpu">
	<input type="submit" value="确定">
</form>
<br><br>
<table border="1" cellpadding="0" cellspacing="1" width="100%" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>版本</td>
	<td>渠道</td>
	<td>平台</td>
	<td>GPU</td>
	<td>URL</td>
	<td>是否有效</td>
	<td>大小</td>
</tr>
<%

	for (String key : MiniResourceZipManager.miniDatas.keySet()) {
		MiniResourceZipData data = MiniResourceZipManager.miniDatas.get(key);
		for (String kk : data.getDownloadUrl().keySet()) {
			String[] CAndG = kk.split(MiniResourceZipData.SPACE);
			out.println("<tr>");
			out.println("<td>"+key+"到"+data.getNowResourceVersion()+"</td>");
			out.println("<td>"+CAndG[0]+"</td>");
			out.println("<td>"+CAndG[1]+"</td>");
			out.println("<td>"+CAndG[2]+"</td>");
			out.println("<td>"+data.getDownloadUrl().get(kk)+"</td>");
			out.println("<td>"+data.getDownloadEnable().get(kk)+"</td>");
			out.println("<td>"+data.getSize(CAndG[2], CAndG[1])+"</td>");
			out.println("</tr>");
		}
	}
%>
</table>
<br><br>
删除一组，如果只是输入起始版本就删除起始版本，如果输入起始版本和渠道就删除这个起始版本下的这个渠道的全部
<form>
	<input type="hidden" name="action" value="deleteAll">
	起始版本:<input type="text" name="Fver">
	渠道:<input type="text" name="channel">
	<input type="submit" value="确定">
</form>
<br><br>

<h2>渠道下载映射</h2>
<table border="1" cellpadding="0" cellspacing="1" width="50%" align="center">
	<tr>
		<td>渠道</td>
		<td>映射到渠道</td>
	</tr>
	<%
		for (String key : MiniResourceZipManager.cTOc.keySet()) {
			out.println("<tr>");
			out.println("<td>"+key+"</td>");
			out.println("<td>"+MiniResourceZipManager.cTOc.get(key)+"</td>");
			out.println("</tr>");
		}
	%>
</table>
<br><br>
添加渠道映射，渠道A映射到渠道B 这样渠道A来了就先去检查渠道B的连接是不是可以下载。
<form>
	<input type="hidden" name="action" value="addCTOC">
	渠道A:<input type="text" name="channelA">
	渠道B:<input type="text" name="channelB">
	<input type="submit" value="确定">
</form>
<br>
<form>
	<input type="hidden" name="action" value="removeCTOC">
	渠道A:<input type="text" name="channelA">
	<input type="submit" value="确定">
</form>

</body>
</html>