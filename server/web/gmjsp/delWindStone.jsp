<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.InlayArticle"%>
<%@page import="com.fy.engineserver.wing.WingManager"%>
<%@page import="com.fy.engineserver.wing.WingPanel"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="java.net.URLDecoder"%><%@page import="com.fy.boss.gm.gmpagestat.GmEventManager"%><%@page import="net.sf.json.JSONArray"%><%@page import="java.util.HashMap"%><%@page import="java.util.Map"%><%@page import="com.fy.engineserver.mail.service.MailManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%><%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%><%@page import="com.fy.engineserver.sprite.Player"%><%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	Map<String, Object> result = new HashMap<String, Object>();
	long playerId = Long.valueOf(request.getParameter("playerId"));
	String sId = request.getParameter("stoneId");
	if (sId == null || sId.isEmpty()) {
		result.put("success", "false");
		result.put("message", "宝石id错误");
		JSONArray json = JSONArray.fromObject(result);
		out.print(json.toString()); 
		out.flush();
		out.close();
		return;
	}
	long stoneId = Long.valueOf(sId);
	String gmUser = URLDecoder.decode(request.getParameter("gmUserName"),"utf-8");
	String type = request.getParameter("type");		//1删除装备宝石   2删除翅膀宝石
	try {
		Player player = null;
		try {
			player = GamePlayerManager.getInstance().getPlayer(playerId);
		} catch (Exception e) {
			
		}
		if ("1".equals(type)) {
			long eId = Long.valueOf(request.getParameter("eId"));
			ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(eId);
			boolean b = false;				//是否存在在背包或者装备上
			for (Soul soul : player.getSouls()) {
				if (soul == null) {
					continue;
				}
				if (b) {
					break;
				}
				for (long id : soul.getEc().getEquipmentIds()) {
					if (id == ae.getId()) {
						b = true;
						break;
					}
				}
			}
			if (!b) {
				Knapsack bag = player.getKnapsack_common();
				if (bag.contains(ae)) {
					b = true;
				}
			}
			if (!b) {
				Knapsack bag = player.getKnapsack_fangbao();
				if (bag.contains(ae)) {
					b = true;
				}
			}
			if (!b) {
				Knapsack bag = player.getKnapsacks_cangku();
				if (bag.contains(ae)) {
					b = true;
				}
			}
			if (b) {
				if (ae instanceof EquipmentEntity) {
					EquipmentEntity ee = (EquipmentEntity)ae;
					long[] inlayIds = ee.getInlayArticleIds();
					long[] oldIds = ee.getInlayArticleIds();
					for (int i=0; i<inlayIds.length; i++) {
						if (inlayIds[i] == stoneId) {
							inlayIds[i] = -1;
							break;
						}
					}
					ee.setInlayArticleIds(inlayIds);
					result.put("success", "true");
					ArticleManager.logger.warn("[gm平台操作] [删除装备宝石] [修改者:"+gmUser+"] [" + player.getLogString() + "] [eeId:"+ae.getId()+"] [oldIds:" + Arrays.toString(oldIds) + "] [newId:" + Arrays.toString(ee.getInlayArticleIds()) + "]");
				}
			} else {
				result.put("success", "false");
				result.put("message", "装备不属于玩家");
			}
		} else if ("2".equals(type)) {
			WingPanel wp = WingManager.getInstance().getWingPanle(player);
			if (wp != null) {
				int index = 0;
				long[] inlayIds = wp.getInlayArticleIds();
				long[] oldIds = wp.getInlayArticleIds();
				boolean b = false;
				for (int i=0; i<inlayIds.length; i++) {
					if (inlayIds[i] == stoneId) {
						inlayIds[i] = -1;
						index = i;
						b = true;
						break;
					}
				}
				ArticleEntity st = DefaultArticleEntityManager.getInstance().getEntity(stoneId);
				Article ia2 = ArticleManager.getInstance().getArticle(st.getArticleName());
				wp.setInlayArticleIds(inlayIds);
				if (wp.getWingId() > 0) {
					((InlayArticle) ia2).removePropertyValue(player);
					wp.handleProps(player, wp.inlayAddPropType[index], -(float) wp.inlayAddPropValue[index] / 100, 1);
				}
				if (b) {
					result.put("success", "true");
				} else {
					result.put("success", "false");
					result.put("message", "翅膀上无此宝石");
				}
				ArticleManager.logger.warn("[gm平台操作] [删除翅膀宝石] [修改者:"+gmUser+"] [" + player.getLogString() + "] [result:"+b+"] [oldIds:" + Arrays.toString(oldIds) + "] [newId:" + Arrays.toString(wp.getInlayArticleIds()) + "]");
			}
		}
	} catch (Exception e) {
		result.put("result","页面出现异常" + e.getMessage());		
	}
	JSONArray json = JSONArray.fromObject(result);
	out.print(json.toString()); 
	out.flush();
	out.close();
%>