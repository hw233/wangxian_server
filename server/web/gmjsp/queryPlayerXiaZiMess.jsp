
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
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
<%!
	String baoxiachuan[] = {"绿色", "橙色", "黑色", "蓝色", "红色", "白色", "黄色", "紫色"};
	public String getColorMess(int index){
		return baoxiachuan[index];
	}
%>
<%
	Map<String,Object> map = new HashMap<String,Object>();
	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	String xiaZiId = request.getParameter("xiaZiId");
	if (xiaZiId == null || xiaZiId.isEmpty()) {
		out.print("请输入正确的神匣id");
		return;
	}
	xiaZiId = URLDecoder.decode(xiaZiId, "UTF-8");
	
	BaoShiXiaZiData data = null;
	try{
		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(Long.parseLong(xiaZiId));
		if(ae != null){
			map.put("xiaZiName",ae.getArticleName());
		}
		data = ArticleManager.getInstance().getxiLianData(null,Long.parseLong(xiaZiId));
	}catch(Exception e){
		
	}
	if (data == null) {
		out.print("匣子不存在："+xiaZiId);
		return;
	}
	
	int index = 0;
	for(long id : data.getIds()){
		Map<String,Object> map2 = new HashMap<String,Object>();
		if(id > 0){
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(id);
			if(ae != null){
				Article baoshi = ArticleManager.getInstance().getArticle(ae.getArticleName());
				if(baoshi != null && baoshi instanceof InlayArticle){
					map2.put("name",ae.getArticleName());
					map2.put("bid",id+"");
					StringBuffer sb = new StringBuffer();
					int[] values = ((InlayArticle)baoshi).getPropertysValues();
					if(values != null){
						for(int i = 0; i < values.length; i++){
							int value = values[i];
							if(value != 0){
								switch(i){
								case 0 :
									sb.append("<br>").append(Translate.最大气血值).append("+").append(value);
									break;
								case 1 :
									sb.append("<br>").append(Translate.外功修为).append("+").append(value);
									break;
								case 2 :
									sb.append("<br>").append(Translate.内法修为).append("+").append(value);
									break;
								case 3 :
									sb.append("<br>").append(Translate.外功防御).append("+").append(value);
									break;
								case 4 :
									sb.append("<br>").append(Translate.内法防御).append("+").append(value);
									break;
								case 5 :
									sb.append("<br>").append(Translate.闪躲).append("+").append(value);
									break;
								case 6 :
									sb.append("<br>").append(Translate.免暴).append("+").append(value);
									break;
								case 7 :
									sb.append("<br>").append(Translate.命中).append("+").append(value);
									break;
								case 8 :
									sb.append("<br>").append(Translate.暴击).append("+").append(value);
									break;
								case 9 :
									sb.append("<br>").append(Translate.精准).append("+").append(value);
									break;
								case 10 :
									sb.append("<br>").append(Translate.破甲).append("+").append(value);
									break;
								case 11 :
									sb.append("<br>").append(Translate.阳炎攻击).append("+").append(value);
									break;
								case 23:
									sb.append("<br>").append(Translate.阳炎攻击).append("+").append(value);
									break;
								case 26:
									sb.append("<br>").append(Translate.阳炎攻击).append("+").append(value);
									break;
								case 12 :
									sb.append("<br>").append(Translate.阳炎抗性).append("+").append(value);
									break;
								case 24 :
									sb.append("<br>").append(Translate.阳炎抗性).append("+").append(value);
									break;
								case 27 :
									sb.append("<br>").append(Translate.阳炎抗性).append("+").append(value);
									break;
								case 13 :
									sb.append("<br>").append(Translate.阳炎减抗).append("+").append(value);
									break;
								case 25 :
									sb.append("<br>").append(Translate.阳炎减抗).append("+").append(value);
									break;
								case 28 :
									sb.append("<br>").append(Translate.阳炎减抗).append("+").append(value);
									break;
								case 14 :
									sb.append("<br>").append(Translate.玄冰攻击).append("+").append(value);
									break;
								case 29 :
									sb.append("<br>").append(Translate.玄冰攻击).append("+").append(value);
									break;
								case 32 :
									sb.append("<br>").append(Translate.玄冰攻击).append("+").append(value);
									break;
								case 15 :
									sb.append("<br>").append(Translate.玄冰抗性).append("+").append(value);
									break;
								case 30 :
									sb.append("<br>").append(Translate.玄冰抗性).append("+").append(value);
									break;
								case 33 :
									sb.append("<br>").append(Translate.玄冰抗性).append("+").append(value);
									break;
								case 16 :
									sb.append("<br>").append(Translate.玄冰减抗).append("+").append(value);
									break;
								case 31 :
									sb.append("<br>").append(Translate.玄冰减抗).append("+").append(value);
									break;
								case 34 :
									sb.append("<br>").append(Translate.玄冰减抗).append("+").append(value);
									break;
								case 17 :
									sb.append("<br>").append(Translate.疾风攻击).append("+").append(value);
									break;
								case 41 :
									sb.append("<br>").append(Translate.疾风攻击).append("+").append(value);
									break;
								case 44 :
									sb.append("<br>").append(Translate.疾风攻击).append("+").append(value);
									break;
								case 18 :
									sb.append("<br>").append(Translate.疾风抗性).append("+").append(value);
									break;
								case 42 :
									sb.append("<br>").append(Translate.疾风抗性).append("+").append(value);
									break;
								case 45 :
									sb.append("<br>").append(Translate.疾风抗性).append("+").append(value);
									break;
								case 19 :
									sb.append("<br>").append(Translate.疾风减抗).append("+").append(value);
									break;
								case 43 :
									sb.append("<br>").append(Translate.疾风减抗).append("+").append(value);
									break;
								case 46 :
									sb.append("<br>").append(Translate.疾风减抗).append("+").append(value);
									break;
								case 20 :
									sb.append("<br>").append(Translate.狂雷攻击).append("+").append(value);
									break;
								case 35 :
									sb.append("<br>").append(Translate.狂雷攻击).append("+").append(value);
									break;
								case 38 :
									sb.append("<br>").append(Translate.狂雷攻击).append("+").append(value);
									break;
								case 21 :
									sb.append("<br>").append(Translate.狂雷抗性).append("+").append(value);
									break;
								case 36 :
									sb.append("<br>").append(Translate.狂雷抗性).append("+").append(value);
									break;
								case 39 :
									sb.append("<br>").append(Translate.狂雷抗性).append("+").append(value);
									break;
								case 22 :
									sb.append("<br>").append(Translate.狂雷减抗).append("+").append(value);
									break;
								case 37 :
									sb.append("<br>").append(Translate.狂雷减抗).append("+").append(value);
									break;
								case 40 :
									sb.append("<br>").append(Translate.狂雷减抗).append("+").append(value);
									break;
								}
							}
						}
						sb.append("<br>");
					}
					map2.put("shuXing",sb.toString());
				}
			}
		}
		map2.put("color",getColorMess(index));
		list.add(map2);
		index++;
	}
	map.put("xiaZiKong",list);
	String result = JsonUtil.jsonFromObject(map);
	out.print(result);
%>
