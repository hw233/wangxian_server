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
			GamePlayerManager pm = (GamePlayerManager)PlayerManager.getInstance();
			long now = System.currentTimeMillis();
			now = now - 24*60*60*1000L*2;
			long[] ids = pm.em.queryIds(Player.class, "quitGameTime > ?", new Object[]{now});
			out.println(ids.length + "<br>");
			ArrayList<Player> cangkus = new ArrayList<Player>();
			ArrayList<Integer> cangkuIndex = new ArrayList<Integer>();
			ArrayList<Player> fangbaos = new ArrayList<Player>();
			ArrayList<Integer> fangbaoIndex = new ArrayList<Integer>();
			ArrayList<Player> commonNoYinPiao = new ArrayList<Player>();
			for (int i = 0; i < ids.length; i++) {
				Player p = pm.em.find(ids[i]);
				System.out.println("----"+p.getName()+" ["+i+"]");
				boolean needSave = false;
				{
					Cell[] cellCangKu = p.getKnapsacks_cangku().getCells();
					boolean isCangKuHave = false;
					for (int j = 0; j < cellCangKu.length; j++) {
						if (cellCangKu[j].getEntityId() > 0 && cellCangKu[j].count > 0) {
							ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(cellCangKu[j].getEntityId());
							if (entity != null) {
								if (entity.getArticleName().equals(GreenServerManager.bindpropName)) {
									cangkus.add(p);
									cangkuIndex.add(j);
									isCangKuHave = true;
									break;
								}
							}
						}
					}
					if (isCangKuHave) {
						p = pm.getPlayer(ids[i]);
						ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(cellCangKu[cangkuIndex.get(cangkuIndex.size()-1)].getEntityId());
						String msg = "";
						if (p.getKnapsack_common().getEmptyNum() > 0) {
							p.putToKnapsacks(entity, "从仓库挪到背包");
							GamePlayerManager.logger.warn("仓库有银票的直接放到背包的:"+p.getId()+"-"+p.getName()+"["+entity.getId()+"-"+entity.getArticleName()+"-"+((YinPiaoEntity)entity).getHaveMoney()+"]");
							msg = "到背包";
						}else {
							MailManager.getInstance().sendMail(-1,p.getId(), new ArticleEntity[]{entity}, "把银票从仓库中移出", "把银票从仓库中移出", 0, 0, 0, "把银票从仓库中移出",MailSendType.后台发送,p.getName(),ip,recorder);
							GamePlayerManager.logger.warn("仓库有银票发送邮件的:"+p.getId()+"-"+p.getName()+"["+entity.getId()+"-"+entity.getArticleName()+"-"+((YinPiaoEntity)entity).getHaveMoney()+"]");
							msg = "走邮件";
						}
						p.getKnapsacks_cangku().remove(cangkuIndex.get(cangkuIndex.size()-1), "银票从仓库挪到背包", false);
						out.println("仓库有银票的: ["+msg+"]"+p.getId()+"-"+p.getName()+"["+entity.getId()+"-"+entity.getArticleName()+"-"+((YinPiaoEntity)entity).getHaveMoney()+"]"+"<br>");
					}
				}
				if (p.getKnapsack_fangbao() != null) {
					Cell[] fangbaoCells = p.getKnapsack_fangbao().getCells();
					boolean isFangBaoHave = false;
					for (int j = 0; j < fangbaoCells.length; j++) {
						if (fangbaoCells[j].getEntityId() > 0 && fangbaoCells[j].count > 0) {
							ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(fangbaoCells[j].getEntityId());
							if (entity != null) {
								if (entity.getArticleName().equals(GreenServerManager.bindpropName)) {
									fangbaos.add(p);
									fangbaoIndex.add(j);
									isFangBaoHave = true;
									break;
								}
							}
						}
					}
					if (isFangBaoHave) {
						p = pm.getPlayer(ids[i]);
						ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(fangbaoCells[fangbaoIndex.get(fangbaoIndex.size()-1)].getEntityId());
						String msg = "";
						if (p.getKnapsack_common().getEmptyNum() > 0) {
							p.putToKnapsacks(entity, "从防暴挪到背包");
							GamePlayerManager.logger.warn("防暴有银票的直接放到背包的:"+p.getId()+"-"+p.getName()+"["+entity.getId()+"-"+entity.getArticleName()+"-"+((YinPiaoEntity)entity).getHaveMoney()+"]");
							msg = "到背包";
						}else {
							MailManager.getInstance().sendMail(-1,p.getId(), new ArticleEntity[]{entity}, "把银票从防暴中移出", "把银票从防暴中移出", 0, 0, 0, "把银票从防暴中移出",MailSendType.后台发送,p.getName(),ip,recorder);
							GamePlayerManager.logger.warn("防暴有银票发送邮件的:"+p.getId()+"-"+p.getName()+"["+entity.getId()+"-"+entity.getArticleName()+"-"+((YinPiaoEntity)entity).getHaveMoney()+"]");
							msg = "走邮件";
						}
						p.getKnapsack_fangbao().remove(fangbaoIndex.get(fangbaoIndex.size()-1), "银票从防暴挪到背包", false);
						out.println("防暴有银票的: ["+msg+"]"+p.getId()+"-"+p.getName()+"["+entity.getId()+"-"+entity.getArticleName()+"-"+((YinPiaoEntity)entity).getHaveMoney()+"]"+"<br>");
					}
				}
				if (p.getKnapsack_common() != null) {
					Cell[] cells = p.getKnapsack_common().getCells();
					boolean isCommonHave = false;
					for (int j = 0; j < cells.length; j++) {
						if (cells[j].getEntityId() > 0 && cells[j].count > 0) {
							ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(cells[j].getEntityId());
							if (entity != null) {
								if (entity.getArticleName().equals(GreenServerManager.bindpropName)) {
									isCommonHave = true;
									break;
								}
							}
						}
					}
					if (!isCommonHave) {
						p = pm.getPlayer(ids[i]);
						commonNoYinPiao.add(p);
						Article a = ArticleManager.getInstance().getArticle(Translate.银票);
						YinPiaoEntity entity = (YinPiaoEntity)ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.绿色服, null, a.getColorType(), 1, true);
						String msg = "";
						if (p.getKnapsack_common().getEmptyNum() > 0) {
							p.putToKnapsacks(entity, "背包中没有银票");
							GamePlayerManager.logger.warn("背包中没有银票放到背包的:"+p.getId()+"-"+p.getName()+"["+entity.getId()+"-"+entity.getArticleName()+"-"+((YinPiaoEntity)entity).getHaveMoney()+"]");
							msg = "到背包";
						}
						out.println("背包中没有银票的:"+p.getId()+"-"+p.getName()+"<br>");
						GamePlayerManager.logger.warn("背包中没有银票的:"+p.getId()+"-"+p.getName());
					}
				}
			}
			out.println("仓库有银票的玩家总数:"+cangkus.size()+"<br>");
			out.println("防暴有银票的玩家总数:"+fangbaos.size()+"<br>");
		%>

	</body>
</html>
