<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>


<%@page import="com.fy.engineserver.zongzu.manager.ZongPaiManager"%>
<%@page import="com.fy.engineserver.zongzu.data.ZongPai"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<html>
<head>
<title>查询一个人的宗派</title>
</head>
<body>

<%
	String set = request.getParameter("set");
	String name = request.getParameter("name");

	if(set != null && !set.equals("") && name != null  ){
		
		if(name != null && !name.equals("")){
			Player player = PlayerManager.getInstance().getPlayer(name);
		
			ZongPaiManager zm = ZongPaiManager.getInstance();
			ZongPai zp = zm.getZongPaiByPlayerId(player.getId());
			if(zp != null){
				List<Long> list = zp.getJiazuIds();
				JiazuManager jm = JiazuManager.getInstance();
				for(long id : list){
					Jiazu jz = jm.getJiazu(id);
					if(jz != null){
						out.print(jz.getName()+" "+id+"<br/>");
					}else{
						out.print("jz null"+id+"<br/>");
					}
				}
			}else{
				out.print("zp null");
			}
		}
		
	}else{
		
		if(name != null && !name.equals("")){
			Player player = PlayerManager.getInstance().getPlayer(name);
		
			ZongPaiManager zm = ZongPaiManager.getInstance();
			ZongPai zp = zm.getZongPaiByPlayerId(player.getId());
			if(zp != null){
				List<Long> list = zp.getJiazuIds();
				JiazuManager jm = JiazuManager.getInstance();
				List<Long> realList = new ArrayList<Long>();
				for(long id : list){
					Jiazu jz = jm.getJiazu(id);
					if(jz != null){
						realList.add(id);
						out.print(jz.getName()+" "+id+"<br/>");
					}else{
						out.print("jz null"+id+"<br/>");
						ZongPaiManager.logger.error("[家族null删除家族] ["+zp.getLogString()+"] [家族Id:"+id+"]");
					}
				}
				
				if(realList.size() != list.size()){
					zp.setJiazuIds(realList);
					out.print("设置成功");
				}else{
					out.print("不需要设置");
				}
			}else{
				out.print("zp null");
			}
			return;
		}
	}
%>


<form action="">
	
	不设置(设值只是查询):<input type="text" name="set" value="a"/>
	name:<input type="text" name="name"/>
	<input type="submit" value="submit"/>
</form>



</body>

</html>
