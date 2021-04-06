<%@page import="com.fy.engineserver.datasource.article.data.magicweapon.huntLife.model.ShouHunModel"%>
<%@page import="com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeManager"%>
<%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.HuntLifeArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.jiazu2.manager.JiazuEntityManager2"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.NewMagicWeaponEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeEntityManager"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.activity.fairyBuddha.FairyBuddhaInfo"%>
<%@page import="com.fy.engineserver.activity.fairyBuddha.FairyBuddhaManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
</html>
<head>
<title>test</title>
</head>
<body>
<%
	if (FairyBuddhaManager.getInstance().getFairyBuddhaMap() != null ) {
		boolean has = false;
		for (FairyBuddhaInfo info : FairyBuddhaManager.getInstance().getFairyBuddhaMap().values()) {
			if (info.getId() > 0) {
				has = true;
				boolean b = false;
				Player player = GamePlayerManager.getInstance().getPlayer(info.getId());
				if (player.getHuntLifr() == null) {
					try {
						player.setHuntLifr(HuntLifeEntityManager.instance.getHuntLifeEntity(player));
						ArticleEntity ae = player.getEquipmentColumns().get(11);
						if (ae instanceof NewMagicWeaponEntity) {
							if (((NewMagicWeaponEntity)ae).owner == null) {
								((NewMagicWeaponEntity)ae).owner = player;
							}
						}
						if (ae != null && ae instanceof NewMagicWeaponEntity && ((NewMagicWeaponEntity) ae).getMdurability() > 0) {
							HuntLifeEntityManager.instance.loadAllAttr(player);
							out.println("[重新加载玩家兽魂属性] [成功] [" + player.getLogString() + "]<br>");
							b = true;
						}
						try {
							JiazuEntityManager2.instance.addPracticeAttr(player);
						} catch (Exception e) {
							
						}
					} catch (Exception e) {
						
					}
				} else {
					b = true;
					out.println("[玩家数据正常] [无需修复] [" + player.getLogString() + "] <br>");
					ArticleEntity ae = player.getEquipmentColumns().get(11);
					if (ae instanceof NewMagicWeaponEntity) {
						if (((NewMagicWeaponEntity)ae).owner == null) {
							((NewMagicWeaponEntity)ae).owner = player;
						}
					}
				}
				if (!b) {
					out.println("[重新加载玩家兽魂属性] [失败] [检查玩家数据是否异常] [" + player.getLogString() + "]<br>");
				}
			}
		}
		if (!has) {
			out.println("当前服务器没有仙尊当选2<br>");
		}
	} else {
		out.println("当前服务器没有仙尊当选<br>");
	} 
	
%>
</table>
</body>
</html>
