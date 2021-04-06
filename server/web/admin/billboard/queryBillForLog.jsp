<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.fateActivity.*"%>
<%@page import="java.util.*"%>

<%@page import="com.fy.engineserver.sprite.ActivityRecordOnPlayer"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.newBillboard.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.*"%>



<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查询排行榜为了打印日志</title>
</head>
<body>

	<%
		
		BillboardsManager bbm = BillboardsManager.getInstance();
		
		String defaults = request.getParameter("defaults");
		if(defaults != null && !defaults.equals("")){
			
			Billboard bb = BillboardsManager.getInstance().getBillboard("魅力","当日鲜花");
			if(bb != null){
				BillboardDate[] data = bb.getDates();
				if(data != null){
					for(BillboardDate bbd : data){
						BillboardsManager.logger.error("[当日鲜花打印日志] ["+bbd.getDateId()+"] ["+bbd.getDateValues()[0]+"]");
					}
					out.print("打印当日鲜花完成<br/>");
				}else{
					out.print("当日鲜花  data null");
				}
			}else{
				out.print("当日鲜花 null");
			}
			
			Billboard bb2 = BillboardsManager.getInstance().getBillboard("魅力","当日糖果");
			if(bb2 != null){

				BillboardDate[] data2 = bb2.getDates();
				if(data2 != null){
					for(BillboardDate bbd : data2){
						BillboardsManager.logger.error("[当日糖果打印日志] ["+bbd.getDateId()+"] ["+bbd.getDateValues()[0]+"]");
					}
					out.print("打印当日糖果完成<br/>");
				}else{
					out.print("当日糖果 data null");
				}
			}else{
				out.print("当日糖果 null");
			}
			return;
		}
		String menu = request.getParameter("menu");	
		String sub = request.getParameter("sub");	
		
		if(menu != null && sub != null){
			
			Billboard bb3 = BillboardsManager.getInstance().getBillboard(menu,sub);
			if(bb3 != null){

				BillboardDate[] data3 = bb3.getDates();
				if(data3!= null){
					for(BillboardDate bbd : data3){
						BillboardsManager.logger.error("[打印日志] ["+sub+"] ["+bbd.getDateId()+"] ["+bbd.getDateValues()[0]+"]");
					}
					out.print("打印"+sub+"完成<br/>");
				}else{
					out.print(sub+"data null");
				}
			
			}else{
				out.print("指定排行榜不存在");
			}
			return;	
		}
	%>

	<form action="">
		默认(设置的话就是取(当日鲜花，糖果)):<input type="text" name="defaults" value="默认"/><br />
		一级目录:<input type="text" name="menu"/><br />
		二级目录:<input type="text" name="sub"/><br />
		<input type="submit" value="submit"/><br />
	
	</form>

</body>

</html>
