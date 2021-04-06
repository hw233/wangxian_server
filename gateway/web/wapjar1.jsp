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
		String brand = "诺基亚系";
		out.print("<a href=\"wapjar.jsp\">刷新</a>(如果页面没有可下载的链接！可能jar包正在更新！请您可以尝试着刷新！谢谢！)<br/>");
		String rid = request.getParameter("r");
		if (rid != null)
			BossClientService.getInstance().sendRecommendRelation(
					Long.parseLong(rid), (byte) 1);
		if (rid != null)
			out.print("非常感谢您朋友的推荐,希望您能够游戏愉快！欢迎提出宝贵意见！<br/>");
		if (request.getParameter("brand") != null)
			brand = request.getParameter("brand").trim();
		List<Jaro> jrs = null;
		String pn = request.getParameter("pn");
		if (pn == null)
			jrs = jmanager.getSlist().get(brand);
		if (pn != null) {
			jrs = new ArrayList<Jaro>();
			for (ArrayList<Jaro> js : jmanager.getSlist().values()) {
				for (Jaro j : js) {
					if (j.getName().toLowerCase().contains(pn.toLowerCase())
							|| j.getMemo().toLowerCase().contains(pn.toLowerCase()))
						jrs.add(j);
				}
			}
		}
		int i=0;
		for (String s : jmanager.getSlist().keySet()) {
			if (brand.equals(s))
				out.print("["+s + "] ");
			if (!brand.equals(s)) {
				out.print("[<a href='wapjar.jsp?brand=" + s);
				if (rid != null)
					out.print("&amp;r=" + rid);
				out.print("'>" + s + "</a>] ");
			}
			i++;
			if(i%4==0)
			 out.print("<br/>");
		}
		out.print("<br/>");
		out
				.print("搜索<input type='text' name='pn' value='' size='' /><a href='wapjar.jsp?pn=$pn' >确定</a><br/>");
		if (jrs!=null&&jrs.size() == 0)
			out.print("对不起！您所选择的机型暂时没有找到，请检查信息或者耐心等待更新！<br/>");
		if (jrs != null) {
			for (Jaro j : jrs) {
			    if(pn!=null)
			    out.print(j.getBrand()+"-");
				out.print(j.getName());
				if (rid != null) {
					if (jmanager.getJarName(j.getName()) != null)
						out.print("<a href ='/jars/jadrefactor?rid=" + rid
								+ "&amp;jad=" + root + "/QL_bjnet_"
								+ jmanager.getJarName(j.getName())
								+ ".jad' >马上下载</a>");
					out.print("<br/>适配机型：" + j.getMemo() + "<br/><br/>");
				} else {
					if (jmanager.getJarName(j.getName()) != null)
						out.print("<a href ='/jars/QL_bjnet_"
								+ jmanager.getJarName(j.getName())
								+ ".jad' >马上下载</a>");
					out.print("<br/>适配机型：" + j.getMemo() + "<br/><br/>");
				}
			}
		}
	%>
	*******************
	<br />
	<a href="http://ql.sqage.com/">返回首页</a>
</p>
</card>
</wml>