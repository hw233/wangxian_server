<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.zongzu.manager.ZongPaiManager"%>
<%@page import="com.fy.engineserver.zongzu.data.ZongPai"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>把宗派丢的家族加入到宗派中</title>
</head>
<body>

		
		<%
		
				
			long[] jiazuIds = JiazuManager.jiazuEm.queryIds(Jiazu.class,"");
			out.print(jiazuIds.length);
			for(long id : jiazuIds){
				
				Jiazu jiazu = JiazuManager.getInstance().getJiazu(id);
				if(jiazu.getZongPaiId() > 0 ){
					ZongPai zp = ZongPaiManager.getInstance().getZongPaiById(jiazu.getZongPaiId());
					boolean bln = true;
					List<Long> zpIds = zp.getJiazuIds();
					for(long idd : zpIds){
						if(idd == jiazu.getJiazuID()){
							bln = false;
							break;
						}
					}
					if(bln){
						zp.getJiazuIds().add(jiazu.getJiazuID());
						zp.setJiazuIds(zp.getJiazuIds());
					}
				}
			}
			
			out.print("finish");
		
		%>
		
</body>
</html>