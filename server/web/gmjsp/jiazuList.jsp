<%@page import="com.fy.engineserver.septstation.service.SeptStationManager"%><%@page import="com.fy.engineserver.septstation.SeptStation"%><%@page import="com.fy.engineserver.jiazu.JiazuMember"%><%@page import="com.fy.engineserver.jiazu.JiazuTitle"%><%@page import="com.fy.engineserver.country.manager.CountryManager"%><%@page import="java.util.Iterator"%><%@page import="com.fy.engineserver.jiazu.Jiazu"%><%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%><%@page import="java.util.ArrayList"%><%@page import="java.util.List"%><%@page import="com.fy.boss.gm.gmpagestat.GmEventManager"%><%@page import="net.sf.json.JSONArray"%><%@page import="java.util.HashMap"%><%@page import="java.util.Map"%><%@page import="com.fy.engineserver.mail.service.MailManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%><%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%><%@page import="com.fy.engineserver.sprite.Player"%><%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	String serverUrl = request.getParameter("serverUrl");		//服务器url
	List<Map<String, Object>> jiazuList = new ArrayList<Map<String, Object>>();
	JiazuManager jiazuManager = JiazuManager.getInstance();
	Map<Long, Jiazu> jiazuMap = jiazuManager.getJiazuLruCacheByID();
	try {
		int[] countryNum = new int[4];
		List<Jiazu> jlist = new ArrayList<Jiazu>();
		for (Iterator<Long> itor = jiazuMap.keySet().iterator(); itor.hasNext();) {
			Map<String, Object> maps = new HashMap<String,Object>();
			//jlist.add(jiazuMap.get(itor.next()));
			Jiazu jiazu = jiazuMap.get(itor.next());
			maps.put("jiazuID", jiazu.getJiazuID());
			maps.put("name", jiazu.getName());
			maps.put("countryName", CountryManager.得到国家名(jiazu.getCountry()));
			maps.put("level", jiazu.getLevel());
			ArrayList<JiazuMember> jms = jiazuManager.getJiazuMember(jiazu.getJiazuID(), JiazuTitle.master);
			maps.put("master", jiazu.getJiazuMaster());
			long maintaiCost = 0;
			if (jiazu.getBaseID() > 0) {
				SeptStation septStation = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
				if (septStation != null) {
					maintaiCost = septStation.getInfo().getCurrMaintai();
				}
			}
			maps.put("currMaintai", maintaiCost);
			maps.put("money", jiazu.getJiazuMoney());
			maps.put("constructionConsume", jiazu.getConstructionConsume());
			maps.put("onlineMember", (jiazu.getOnLineMembers().size()+"/"+jiazu.getMemberNum()));
			maps.put("prosperity", jiazu.getProsperity());
			jiazuList.add(maps);
		}
	} catch (Exception e) {
		JiazuManager.logger.warn("[gm查询家族列表] [异常]", e);
		out.println("查询异常");
	}
	JSONArray json = JSONArray.fromObject(jiazuList);
	out.print(json.toString()); 
	out.flush();
	out.close();
	
%>