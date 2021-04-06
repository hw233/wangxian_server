<%@page import="com.fy.engineserver.util.TimeTool"%><%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%><%@page import="com.fy.engineserver.jiazu.JiazuMember4Client"%><%@page import="java.sql.Timestamp"%><%@page import="com.fy.engineserver.zongzu.data.ZongPai"%><%@page import="com.fy.engineserver.zongzu.manager.ZongPaiManager"%><%@page import="com.fy.engineserver.septstation.service.SeptStationManager"%><%@page import="com.fy.engineserver.septstation.SeptStation"%><%@page import="com.fy.engineserver.jiazu.JiazuMember"%><%@page import="com.fy.engineserver.jiazu.JiazuTitle"%><%@page import="com.fy.engineserver.country.manager.CountryManager"%><%@page import="java.util.Iterator"%><%@page import="com.fy.engineserver.jiazu.Jiazu"%><%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%><%@page import="java.util.ArrayList"%><%@page import="java.util.List"%><%@page import="com.fy.boss.gm.gmpagestat.GmEventManager"%><%@page import="net.sf.json.JSONArray"%><%@page import="java.util.HashMap"%><%@page import="java.util.Map"%><%@page import="com.fy.engineserver.mail.service.MailManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%><%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%><%@page import="com.fy.engineserver.sprite.Player"%><%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	String serverUrl = request.getParameter("serverUrl");		//服务器url
	long jiazuId = Long.parseLong(request.getParameter("jiazuID"));	
	
	Map<String, Object> resultMap = new HashMap<String, Object>();
	
	Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuId);
	if (jiazu != null) {
		Map<String, Object> jiazuMap = new HashMap<String, Object>();
		{			//jizuMap
			jiazuMap.put("JiazuID", jiazu.getJiazuID());
			jiazuMap.put("Name", jiazu.getName());
			jiazuMap.put("Country", CountryManager.得到国家名(jiazu.getCountry()));
			jiazuMap.put("Level", jiazu.getLevel());
			ArrayList<JiazuMember> jms = JiazuManager.getInstance().getJiazuMember(jiazu.getJiazuID(), JiazuTitle.master);
			jiazuMap.put("JiazuMaster", jiazu.getJiazuMaster());
			String zongpaiName = "";
			long zongpaiId = jiazu.getZongPaiId();
			if (zongpaiId > 0) {
				ZongPai zp = ZongPaiManager.getInstance().getZongPaiById(zongpaiId);
				zongpaiName = zp.getZpname();
			}
			
			jiazuMap.put("ZongPai", zongpaiId + "/" + zongpaiName);
			jiazuMap.put("getBaseID", jiazu.getBaseID());
			String ct = TimeTool.formatter.varChar19.format(jiazu.getCreateTime());
			jiazuMap.put("CreateTime", ct);
			String ct2 = TimeTool.formatter.varChar19.format(jiazu.getLastCallbossTime());
			jiazuMap.put("LastCallbossTime", ct2);
			jiazuMap.put("SingleMemberSalaryMax", "");	//个人家族工资上限，待确认
			long maintaiCost = 0;
			if (jiazu.getBaseID() > 0) {
				SeptStation septStation = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
				if (septStation != null) {
					maintaiCost = septStation.getInfo().getCurrMaintai();
				}
			}
			jiazuMap.put("CurrMaintai", maintaiCost);
			jiazuMap.put("JiazuMoney", jiazu.getJiazuMoney());
			jiazuMap.put("ConstructionConsume", jiazu.getConstructionConsume());
			jiazuMap.put("MemberNum", jiazu.getOnLineMembers().size());
			jiazuMap.put("maxMember", jiazu.getMemberNum());
			jiazuMap.put("Prosperity", jiazu.getProsperity());
			jiazuMap.put("JiazuPassword", jiazu.getJiazuPassword());
			jiazuMap.put("SalaryLeft", jiazu.getSalaryLeft());
		}
		resultMap.put("jiazuMap", jiazuMap);
		List<Map<String, Object>> jiazuMemberList = new ArrayList<Map<String, Object>>();
		{			//jiazuMemberList
			ArrayList<JiazuMember4Client> list = jiazu.getMember4Clients();
			if (list != null && list.size()>0) {
				for (JiazuMember4Client jm : list) {
					Map<String, Object> jmMap = new HashMap<String, Object>();					
					jmMap.put("JiazuMemID", jm.getJiazuMember().getJiazuMemID());
					jmMap.put("PlayerID", jm.getPlayerId());
					jmMap.put("PlayerName", jm.getPlayerName());
					jmMap.put("PlayerLevel", jm.getPlayerLevel());
					jmMap.put("Sex", jm.getSex());
					jmMap.put("TitleIndex", jm.getJiazuMember().getTitleIndex());
					jmMap.put("TitleName", jm.getJiazuMember().getTitle().getName());
					jmMap.put("LastWeekContribution", jm.getJiazuMember().getLastWeekContribution());
					jmMap.put("CurrentWeekContribution", jm.getJiazuMember().getCurrentWeekContribution());
					jmMap.put("SinglePlayerSalary", jiazu.getSinglePlayerSalary(jm.getPlayerId()));	//待确认
					jmMap.put("CurrentSalary", jm.getJiazuMember().getCurrentSalary());
					jmMap.put("isPaid", jm.getJiazuMember().isPaid());
					jmMap.put("isOnLine", GamePlayerManager.getInstance().isOnline(jm.getPlayerId()));
					jiazuMemberList.add(jmMap);
				}
			}
		}
		resultMap.put("jiazuMemberList", jiazuMemberList);
		List<Map<String, Object>> jiazuArticleList = new ArrayList<Map<String, Object>>();
		{		//jiazuArticleList
			ArrayList<Cell> wareHouse = jiazu.getWareHouse();
			Iterator<Cell> iterator = wareHouse.iterator();
			boolean found = false;
			while(iterator.hasNext()){
				Cell cell = iterator.next();
				Map<String, Object> tempMap = new HashMap<String, Object>();
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				ArticleEntity ae = aem.getEntity(cell.getEntityId());
				if(null == ae){
					out.print("没有该id对应的物品 id:"+cell.getEntityId());
				}else{
					found = true;
					tempMap.put("ArticleName", ae.getArticleName());
					tempMap.put("ReferenceCount", ae.getReferenceCount());
					tempMap.put("EntityId", ae.getId());
					jiazuArticleList.add(tempMap);
				}
			}
		}
		resultMap.put("jiazuArticleList", jiazuArticleList);
	}
	JSONArray json = JSONArray.fromObject(resultMap);
	out.print(json.toString()); 
	out.flush();
	out.close();
	
%>