<%@ page contentType="text/html;charset=utf-8" import="java.util.*,java.io.*,com.xuanzhi.tools.text.*,com.fy.engineserver.menu.*"%><%!

	String format_window(MenuWindow mw){
		StringBuffer sb = new StringBuffer();
		sb.append("<a name='"+mw.getId()+"'></a>");
		sb.append("<table border='0' cellpadding='0' cellspacing='2' width='280' bgcolor='#000000' align='center'>");
		sb.append("<tr><td>");
		
		sb.append("<table border='0' cellpadding='0' cellspacing='0' width='100%' bgcolor='#888888' align='center'>");
		sb.append("<tr bgcolor='#050ed9' align='center'><td colspan='2' alt='"+(mw.isShowTask()?"显示任务":"不显示任务")+"'>"+mw.getTitle()+"</td></tr>");
		sb.append("<tr><td width='20%' height='15'></td><td></td></tr>");
		sb.append("<tr><td width='20%'></td><td>"+uub2Html(mw.getDescriptionInUUB())+"</td></tr>");
		sb.append("<tr><td width='20%' height='15'></td><td></td></tr>");

		Option ops[] = mw.getOptions();
		for(int i = 0 ; i < ops.length ; i++){
			sb.append("<tr><td colspan='2'><img src='/game_server/imageServlet?id="+ops[i].getIconId()+"'>");
			if(ops[i].getOType() == Option.OPTION_TYPR_WAITTING_NEXT_WINDOW){
				sb.append("&nbsp;&nbsp;<a href='#"+ops[i].getOId()+"'><font color='#"+Integer.toHexString(ops[i].getColor())+"'>"+ops[i].getText()+"</font></a></td></tr>");
			}else if(ops[i].getOType() == Option.OPTION_TYPR_CLIENT_FUNCTION){
				sb.append("&nbsp;&nbsp;<font color='#"+Integer.toHexString(ops[i].getColor())+"'><input type='text' value='"+ops[i].getText()+"' title='[客户端功能]'></font><td></tr>");
			}else{
				sb.append("&nbsp;&nbsp;<font color='#"+Integer.toHexString(ops[i].getColor())+"'><input type='text' value='"+ops[i].getText()+"' title='[服务器功能："+ops[i].toString()+"]'></font></td></tr>");
			}
			
		}
		sb.append("</table>");

		sb.append("</td></tr></table>");
		
		return sb.toString();
	}

	String uub2Html(String uub){
		uub = uub.replaceAll("\\[/color\\]","</font>");
		uub = uub.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
		uub = uub.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
		return uub;
	}
%><% 
	WindowManager manager = WindowManager.getInstance();
	MenuWindow windows[] = manager.getWindows();
	
%>	
<%@include file="IPManager.jsp" %><html><head>
</HEAD>
<BODY >
<h2>菜单系统</h2>
<table border='0' cellpadding='0' cellspacing='30' width='100%' bgcolor='#FFFFFF' align='center'>
<tr align='center'>
<%
	for(int i = 0 ; i < windows.length ; i++){
		if( ((i+1) % 4) == 0){
			out.println("</tr><tr align='center'>");
		}
		out.println("<td>"+format_window(windows[i])+"</td>");
	}
%>
</tr></table>

</BODY></html>
