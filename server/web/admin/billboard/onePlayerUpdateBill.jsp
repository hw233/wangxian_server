<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">

<%@page import="com.fy.engineserver.sprite.Player"%>


<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardStatDateManager"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardStatDate"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>玩家修改排行榜数据</title>
</head>
<body>



	<%
	
		String name = request.getParameter("name");
		String typeS = request.getParameter("bType");
		String valueS = request.getParameter("value");
		if(name != null && typeS != null && valueS != null){
			
			Player p = PlayerManager.getInstance().getPlayer(name);
			
			BillboardStatDate data = BillboardStatDateManager.getInstance().getBillboardStatDate(p.getId());
			
			int type = Integer.parseInt(typeS);
			int value = Integer.parseInt(valueS);
			
			if(type == 0){
				data.设置连斩(p,value,10);
			}else if(type == 1){
				data.设置送花(p,value);
			}else if(type == 2){
				data.设置送糖(p,value);
			}
			out.print("over");
			return;
		}
	
	
	%>


	
	<form action="">
	
		玩家名:<input type="text" name="name"/><br/>
		排行榜类型(0连斩，1送花，2送糖):<input type="text" name="bType"/><br/>
		value:<input type="text" name="value"/><br/>
		<input type="submit" value="submit"/>
	
	</form>
</body>

</html>
