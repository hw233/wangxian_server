<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.*"%>
<%@page import="com.fy.engineserver.sprite.*"%>
<%@page import="java.text.*"%>

<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page import="com.fy.engineserver.septstation.SeptStation"%>
<%@page import="com.fy.engineserver.septstation.service.SeptStationManager"%>
<%@page import="com.fy.engineserver.activity.fireActivity.FireActivityNpcEntity"%>
<%@page import="com.fy.engineserver.billboard.special.SpecialEquipmentManager"%>
<%@page import="com.fy.engineserver.billboard.special.SpecialEquipmentBillBoard"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.Special_1EquipmentEntity"%><html>
<head>
<title>修改天榜宝魂值</title>
</head>
<body>

	<%
		
		String name = request.getParameter("name");
		if(name == null || name.equals("")){
			
			%>
				<form action="">
					请输入天榜装备名:<input type="text" name="name"/>
					<br/>
					<input type="submit" value="submit"/>
				</form>
			
			<%
		}else{
			
			SpecialEquipmentBillBoard sbb = SpecialEquipmentManager.getInstance().getSpecialEquipmentBillBoard();			
			if(sbb != null){
				
				LinkedHashMap<String, List<Long>> map = sbb.getSpecial1Map();
				List<Long> list =  map.get(name.trim());
				if(list == null || list.size() <= 0){
					out.print("list null");
				}else{
					long eqId = list.get(0);
					ArticleEntityManager aem = ArticleEntityManager.getInstance();
					ArticleEntity ae=  aem.getEntity(eqId);
					if(ae instanceof Special_1EquipmentEntity){
						
						Special_1EquipmentEntity se1 = (Special_1EquipmentEntity)ae;
						se1.setSpecialValue(1);
						out.print("设置成功");
						
					}else{
						out.print(ae.getClass());
					}
				}
			}else{
				out.print("sbb null");
			}
			
			
		}
	
	
	
	%>

</body>

</html>
