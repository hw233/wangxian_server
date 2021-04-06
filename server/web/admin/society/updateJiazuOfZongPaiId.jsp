<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.society.Relation"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.marriage.MarriageInfo"%>
<%@page import="com.fy.engineserver.marriage.manager.MarriageManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfo"%>
<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfoManager"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page import="com.fy.engineserver.zongzu.data.ZongPai"%>
<%@page import="com.fy.engineserver.zongzu.manager.ZongPaiManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>



	<%
	
		String name = request.getParameter("name");
		if(name != null && !name.equals("")){
			Player player = PlayerManager.getInstance().getPlayer(name);
			Relation relation = SocialManager.getInstance().getRelationById(player.getId());
			
			Jiazu jz =  JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if(jz != null){
				long zongpaiId = jz.getZongPaiId();
				out.print("zpid"+zongpaiId);
				ZongPai zp = ZongPaiManager.getInstance().getZongPaiById(zongpaiId);
				if(zp == null){
					jz.setZongPaiId(-1);
					out.print("设置宗派id 为-1");
				}else{
					out.print("有宗派，不能设置");
				}
			}else{
				out.print( "jz null");
			}
			return;
		}
	%>

	<form action="">
		name:<input type="text" name="name" /><br/>
		<input type="submit" value="submit">
	</form>


</body>
</html>