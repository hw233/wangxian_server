<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.InlayArticle"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="java.io.Serializable"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.BaoShiXiaZiData"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%><%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="java.net.URLDecoder"%>
<%@ page
	language="java"
	contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
%>
<%
	String xid = request.getParameter("xiaZiId");
	String bid = request.getParameter("baoShiId");
	if(xid != null && bid != null){
		BaoShiXiaZiData data = ArticleManager.getInstance().getxiLianData(null,Long.parseLong(xid));
		Map<String,String> result = new HashMap<String,String>();
		if(data != null){
			int cellNums = 0;
			int removeIndex = -1;
			boolean isFirstCell = false;
			
			if(data.getIds()[0] == Long.parseLong(bid)){
				isFirstCell = true;
			}
			
			for(int i = 0;i < data.getIds().length;i++){
				if(data.getIds()[i] > 0){
					cellNums++;
				}
				if(data.getIds()[i] == Long.parseLong(bid)){
					removeIndex = i;
				}
			}
			
			//有孔的宝石大于1，且删除的是第一个孔
			if(isFirstCell && (cellNums > 1)){
				result.put("result","副孔有宝石，不能删除主孔宝石.");
				out.print(JsonUtil.jsonFromObject(result));
				return;
			}
			
			if(removeIndex == -1){
				result.put("result","没有找到id为:"+bid+"的宝石.");
				out.print(JsonUtil.jsonFromObject(result));
				return;
			}
			
			data.getIds()[removeIndex] = -1;
			data.getColors()[removeIndex] = -1;
			ArticleEntityManager.baoShiXiLianEM.notifyNewObject(data);
			for(BaoShiXiaZiData d : ArticleManager.baoShiXiaZiDatas){
				if(d.getId() == data.getId()){
					d.setIds(data.getIds());
					d.setColors(data.getColors());
				}
			}
			result.put("result","success");
			out.print(JsonUtil.jsonFromObject(result));
		}else{
			result.put("result","没有找到id为:"+xid+"的神匣.");
			out.print(JsonUtil.jsonFromObject(result));
		}
	}
%>
