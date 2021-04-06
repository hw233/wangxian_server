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
<title>test</title>
</head>
<body>

	<%
		
		BillboardsManager bbm = BillboardsManager.getInstance();
		
		String menu = request.getParameter("menu");	
		String sub = request.getParameter("sub");	
		
		if(menu != null && sub != null){
			
			Billboard bb = bbm.getBillboard(menu,sub);
			
			if(bb != null){
				
				BillboardDate[] ds = bb.getDates();
				if(ds == null){
					bb.update();
				}
				if(ds == null){
					out.print("ds null");
				}
				BillboardDate[] nds = null;
				if(ds.length >= BillboardsManager.显示条数){
					nds = new BillboardDate[BillboardsManager.显示条数];
					for(int i=0;i<BillboardsManager.显示条数;i++){
						nds[i] = ds[i];
					}
				}else{
					nds = ds;
				}
									
				String titleTh = "<th>id</th>";
				for(String st : bb.getTitles()){
					titleTh += "<th>"+st+"</th>";
				}
				
				%>
				
				<table cellspacing="1" cellpadding="2" border="1">
					<tr>
						<%= titleTh %>
					</tr>
				<%
					for(BillboardDate date : ds){
						%>
						<tr>
							<%
								String[] values = date.getDateValues();
								String titleTd = "<td>"+date.getDateId()+"</td>";
								for(String st : values){
									titleTd += "<td>"+st+"</td>";
								}
								out.print(titleTd);
							%>
						</tr>
						<%
					}
				
				
				%>
				</table>
				<%
			
			}
		}
	%>


</body>

</html>
