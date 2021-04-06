<%@ page contentType="text/vnd.wap.wml;charset=utf-8"
	import="java.util.*,java.io.*" pageEncoding="utf-8"%><%@page
	import="com.fy.gamegateway.jar.*,com.xuanzhi.boss.client.*"%><?xml version='1.0' encoding='utf-8'?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="潜龙online下载">
<p>
	<%
		JarManager jmanager = JarManager.getInstance();
		String root = jmanager.getFileRoot();
		String rid = request.getParameter("r");
		if (rid != null){
			BossClientService.getInstance().sendRecommendRelation(
					Long.parseLong(rid), (byte) 1);
			out.print("非常感谢您朋友的推荐,希望您能够游戏愉快！欢迎提出宝贵意见！<br/>");
			session.setAttribute("rid",rid);
			}
				out.print("搜索<input type='text' name='pn' value='' size='' /><a href='wapjar_show.jsp?pn=$pn' >确定</a><br/>");
		Map<String,ArrayList<Jaro>> jms = jmanager.getSlist();
		for(String s:jms.keySet()){
		  out.print(s+":<br/>");
		  ArrayList<Jaro> al = jms.get(s);
		  for(Jaro j : al){
		    out.print("<a href='wapjar_show.jsp?p="+j.getName()+"' >"+j.getName()+"</a>系: 包含");
		    String ss[] = j.getMemo().split(";");
		    if(ss.length<=3)
		     out.print(j.getMemo());
		    else
		     out.print(ss[0]+";"+ss[1]+";"+ss[2]+"等机型");
		     out.print("<br/>");
		  }
		 out.print("<br/>");
		}
	%>
	*******************
	<br />
	<a href="http://ql.sqage.com/">返回首页</a>
</p>
</card>
</wml>