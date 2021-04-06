<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.shop.Goods"%>
<%@page import="com.fy.engineserver.shop.Shop"%>
<%@page import="com.fy.engineserver.shop.ShopManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
		String playerid = ParamUtils.getParameter(request, "playerid");
		String eids = ParamUtils.getParameter(request, "eids");
		String pwd = ParamUtils.getParameter(request, "pwd");
		if(pwd!=null && pwd.trim().length()>0){
			if(pwd.trim().equals("曹大爷爱闫超")){
				if(playerid!=null && playerid.trim().length()>0 && eids!=null && eids.trim().length()>0){
					Player player = PlayerManager.getInstance().getPlayer(Long.parseLong(playerid));
					if(player != null){
						ArticleEntityManager articleEntityManager = ArticleEntityManager.getInstance();	
						long succClearCount = 0l;
						String eqids [] = eids.split(","); 
						List<Integer> relist = new ArrayList<Integer>();
				        for(String idstr : eqids){
				        	long id = 0;
				        	try{
				        		id = Long.parseLong(idstr);	
					        	ArticleEntity articleEntity = articleEntityManager.em.find(id);
					        	if(articleEntity != null){
					        		if(articleEntity instanceof EquipmentEntity){
					        			EquipmentEntity equipmentEntity = (EquipmentEntity)articleEntity;
					        			int starNum = equipmentEntity.getStar();
				 	         			equipmentEntity.setStar(0);
				 	         			articleEntityManager.em.flush(equipmentEntity);
					         			
					         			//怕清除不成功，所以特意再从数据库中查询一次，然后查看是否更新完毕
					         			articleEntity = articleEntityManager.em.find(id);
					         			equipmentEntity = (EquipmentEntity)articleEntity;
					         			succClearCount++;
					         			relist.add(starNum);
					        			ActivitySubSystem.logger.warn("["+Thread.currentThread().getName()+"] [批量根据playerId清除星] ["+id+"] ["+equipmentEntity.getArticleName()+"] [清除前星数:"+starNum+"] [清除后星数:"+equipmentEntity.getStar()+"]");
					        			out.println("["+Thread.currentThread().getName()+"] [批量根据playerId清除星] ["+id+"] ["+equipmentEntity.getArticleName()+"] [清除前星数:"+starNum+"] [清除后星数:"+equipmentEntity.getStar()+"]"+"<br/>");
					        		}
					        	}else{
					        		ActivitySubSystem.logger.warn("["+Thread.currentThread().getName()+"] [对应id未查找到articleentity] [装备id："+id+"] [玩家："+player.getLogString()+"]");
					        		out.println("["+Thread.currentThread().getName()+"] [对应id未查找到articleentity] [玩家："+player.getLogString()+"] [装备id："+id+"]"+"<br/>");
					        	}
				        	}catch(Exception e){
				        		ActivitySubSystem.logger.warn("["+Thread.currentThread().getName()+"] [批量根据playerId清除星] [出现异常] [装备id:"+id+"]",e);
				        		out.println("["+Thread.currentThread().getName()+"] [批量根据playerId清除] [玩家："+player.getLogString()+"] [装备id："+id+"] [批量根据playerId清除星] [出现异常]"+e.getMessage()+"<br/>");
				        	}
				        }
				        
				        ActivitySubSystem.logger.warn("["+Thread.currentThread().getName()+"] [该次修改装备清理前星的状态："+relist.toString()+"] [成功清除道具上星数:"+succClearCount+"] [总道具数:"+eqids.length+"]");
				        out.println("["+Thread.currentThread().getName()+"] [该次修改装备清理前对应的星："+relist.toString()+"] [成功清除道具上星数:"+succClearCount+"] [总道具数:"+eqids.length+"]"+"<br/>");
						
					}else{
						ActivitySubSystem.logger.warn("["+Thread.currentThread().getName()+"] [批量根据playerId清除星] [无对应player] ["+playerid+"]");
						out.println("["+Thread.currentThread().getName()+"] [批量根据playerId清除星] [无对应player] ["+playerid+"]<br/>");
					}
				}else{
					out.print("请输入正确的玩家id和装备id，多件装备id之间用英文的逗号(,)号隔开。");
				}	
			}else{
				out.print("<font color = 'red'>请出示正确的验证码！</font>");
			}
			
		}
				

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>清除玩家的装备的星</title>
</head>
<body>
<form action="">
	<table>
		<tr><th>验证码：</th><td><input type='text' name="pwd" value=""/></td></tr>
		<tr><th>玩家id：</th><td><input type='text' name="playerid" value="<%=playerid==null?"":playerid%>"/></td></tr>
		<tr><th>装备id集：</th><td><textarea name="eids" ><%=eids==null?"":eids%></textarea></td></tr>
		<tr><td><input type="submit" value='确认清除'></td></tr>
	</table>
</form>

</body>
</html>