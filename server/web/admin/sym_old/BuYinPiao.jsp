<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.YinPiaoEntity"%>
<%@page import="com.fy.engineserver.green.GreenServerManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.TeamSubSystem"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>修改组队锁bug</title>
		<link rel="stylesheet" href="gm/style.css" />
		<script type="text/javascript">
	
</script>
	</head>
	<body>
		<br> 
		<%
		String ip = request.getRemoteAddr();
		String recorder = "";
		Object o = session.getAttribute("authorize.username");
		if(o!=null){
			recorder = o.toString();
		}
			Long[] pIds = new Long[]{1240000000002868431L,
				1240000000002480221L,
				1230000000001598906L,
				1232000000000656588L,
				1240000000002376075L,
				1240000000002624177L,
				1240000000000655381L,
				1230000000001476971L,
				1230000000000225385L,
				1240000000002723953L,
				1240000000002171440L,
				1240000000000063019L,
				1240000000000145695L,
				1230000000001045946L,
				1236000000000042520L,
				1236000000001659181L,
				1236000000001372372L,
				1240000000002868339L,
				1240000000002990602L,
				1230000000001355510L,
				1240000000002888042L,
				1232000000000204876L,
				1240000000002969838L,
				1240000000002315842L,
				1230000000001270659L,
				1232000000000021765L,
				1240000000002191784L,
				1240000000002910020L,
				1240000000001966636L,
				1240000000002869418L,
				1240000000000007269L,
				1240000000000267239L,
				1240000000001578905L,
				1232000000000124577L,
				1240000000002786047L,
				1232000000000145246L,
				1240000000002398172L,
				1236000000001700056L,
				1240000000002643511L,
				1230000000001375330L,
				1230000000000006411L,
				1230000000001557506L,
				1240000000001229812L,
				1240000000002130392L,
				1240000000001331467L,
				1240000000002337698L,
				1240000000002622072L,
				1236000000000308049L,
				1230000000000013928L,
				1240000000002685070L,
				1240000000002724932L,
				1232000000000702764L,
				1236000000000085280L,
				1232000000000677990L,
				1230000000001374242L,
				1232000000000721028L,
				1240000000002827703L};
			Long[] eIds = new Long[]{1240000000187546812L,
					1240000000142093846L,
					1230000000136438421L,
					1232000000069004731L,
					1240000000132902310L,
					1240000000161051108L,
					1240000000033919997L,
					1230000000117342592L,
					1230000000008972631L,
					1240000000169517266L,
					1240000000115628837L,
					1240000000003584535L,
					1240000000009109737L,
					1230000000069424209L,
					1236000000000044892L,
					1236000000143621559L,
					1236000000068734482L,
					1240000000187428002L,
					1240000000204875209L,
					1230000000104640485L,
					1240000000190037752L,
					1232000000009715920L,
					1240000000201987456L,
					1240000000128861481L,
					1230000000093814345L,
					1232000000001417027L,
					1240000000116698960L,
					1240000000195538586L,
					1240000000099818916L,
					1240000000188185276L,
					1240000000000070838L,
					1240000000016859137L,
					1240000000075739263L,
					1232000000005559689L,
					1240000000180125923L,
					1232000000007121443L,
					1240000000135590109L,
					1236000000147357948L,
					1240000000162219816L,
					1230000000106851399L,
					1230000000000088984L,
					1230000000125914826L,
					1240000000058181694L,
					1240000000111113065L,
					1240000000063363536L,
					1240000000130771647L,
					1240000000159705606L,
					1236000000008523687L,
					1230000000000770113L,
					1240000000166073048L,
					1240000000170759219L,
					1232000000079360354L,
					1236000000000926630L,
					1232000000072154296L,
					1230000000106494470L,
					1232000000085963531L,
					1240000000184201661L};
			GamePlayerManager pm = (GamePlayerManager)PlayerManager.getInstance();
			for (int i = 0; i < pIds.length; i++) {
				Player p = pm.getPlayer(pIds[i]);
				System.out.println("补丢弃银票" + p.getLogString() + " ["+i+"/"+pIds.length+"]");
				ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(eIds[i]);
				if (p != null && entity != null) {
					if (p.getKnapsack_common().getEmptyNum() > 0) {
						p.putToKnapsacks(entity, "丢弃银票补的");
						out.println("丢弃银票补的放背包 ["+p.getLogString()+"] ["+((YinPiaoEntity)entity).getHaveMoney()+"]");
						GamePlayerManager.logger.warn("丢弃银票补的放背包 ["+p.getLogString()+"] ["+((YinPiaoEntity)entity).getHaveMoney()+"]");
					}else {
						MailManager.getInstance().sendMail(-1,p.getId(), new ArticleEntity[]{entity}, "补丢弃银票", "补丢弃银票", 0, 0, 0, "补丢弃银票",MailSendType.后台发送,p.getName(),ip,recorder);
						GamePlayerManager.logger.warn("丢弃银票补的放背包 ["+p.getLogString()+"] ["+((YinPiaoEntity)entity).getHaveMoney()+"]");
						out.println("丢弃银票补的发邮件 ["+p.getLogString()+"] ["+((YinPiaoEntity)entity).getHaveMoney()+"]");
					}
				}
			}
		%>

	</body>
</html>
