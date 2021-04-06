<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>


<%@page import="com.fy.engineserver.zongzu.manager.ZongPaiManager"%>
<%@page import="com.fy.engineserver.zongzu.data.ZongPai"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>

<%@page import="oracle.net.aso.p"%><html>
<head>
<title>查询宗主的宗派密码</title>
</head>
<body>

<%
	String name = request.getParameter("name");


	if(name != null && !name.equals("")){
		Player player = PlayerManager.getInstance().getPlayer(name);
		long jiazuId = player.getJiazuId();
		if(jiazuId > 0){
			
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuId);
			if(jiazu != null){
				
				long zongPaiId  = jiazu.getZongPaiId();
				if(zongPaiId > 0){
					
					ZongPai zp = ZongPaiManager.getInstance().getZongPaiById(zongPaiId);
					if(zp != null){
						
						String password = zp.getPassword();
						String hint = zp.getPasswordHint();
						
						long masterId = zp.getMasterId();
						if(masterId != player.getId()){
							out.print("这个人不是宗主，请问清楚在给密码。密码:"+password+"密码提示:"+hint);
						}else{
							out.print("这个人是宗主，密码:"+password+"密码提示:"+hint);
						}
					}else{
						out.print("这个人宗派    null");
					}
				}else{
					out.print("这个人的家族没有宗派");
				}
			}else{
				out.print("这个人家族    null");
			}
		}else{
			out.print("这个人没有家族");
		}
	}

%>


<form action="">
	
	name:<input type="text" name="name"/>
	<input type="submit" value="submit"/>
</form>



</body>

</html>
