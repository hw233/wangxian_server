<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page
	import="com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*,java.io.*,java.lang.reflect.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.*"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.*"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.IntProperty"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.AddPlan"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.equipments.*"%><%@include
	file="IPManager.jsp"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<style type="text/css">
.tablestyle1 {
	width: 96%;
	border: 0px solid #69c;
	border-top: 1px solid #69c;
	border-right: 1px solid #69c;
	border-bottom: 1px solid #69c;
	border-left: 1px solid #69c;
	border-collapse: collapse;
}

.tablestyle2 {
	width: 100%;
	border: 0px solid #69c;
	border-top: 0px solid #69c;
	border-right: 0px solid #69c;
	border-bottom: 0px solid #69c;
	border-left: 0px solid #69c;
	border-collapse: collapse;
}

td {
	border: 1px solid #69c;
}

.tdtable {
	padding: 0px;
	margin: 0px;
	border-top: 0px solid #69c;
	border-right: 0px solid #69c;
	border-bottom: 0px solid #69c;
	border-left: 0px solid #69c;
}

.lefttd {
	border-left: 0px solid #69c;
}

.toptd {
	border-top: 0px solid #69c;
}

.righttd {
	border-right: 0px solid #69c;
}

.bottomtd {
	border-bottom: 0px solid #69c;
}
</style>
<title>物品</title>
<%
String articleName = request.getParameter("articleName");
String articleCNName = request.getParameter("articleCNName");
ArticleManager am = ArticleManager.getInstance();
PropsCategory pcs[] = am.getAllPropsCategory();
Gson gson = new Gson();
HashMap<String,String> map = new HashMap<String,String>();
map.put("name","物品的名字");
map.put("iconName","图标名");
map.put("description","描述");
map.put("overlap","同类是否可以重叠");
map.put("maxValidDays","最大的有效期，0表示无有效期限制");
map.put("destroyed","是否可以被销毁");
map.put("bindStyle","绑定方式0不绑定，1装备绑定，2拾取绑定，3拾取绑定但不提示");
map.put("equipmentType","装备类型，0武器,1:头盔,2护肩,3衣服,4护手,5靴子,6首饰,7项链 ,8戒指 。");
map.put("durability","耐久度");
map.put("playerLevelLimit","玩家等级限制");
map.put("ips","要修改的属性及其值");
map.put("inLayFlag","可否镶嵌");
map.put("inLayMax","可镶嵌最大数目");
map.put("inLayNum","已经拥有的孔的数量");
map.put("avata","avata值");
map.put("addPlan","附加方案");
map.put("careerConfine","角色的门派或职业限制");
map.put("maxLevel","装备最高级别");
map.put("lastingTimeInSeconds","持续的时间，单位为秒");
map.put("validIntervalTime","多长时间作用一次");
map.put("usingTimesLimit","是否有使用次数限制");
map.put("maxUsingTimes","使用次数限制");
map.put("categoryName","道具类分类类名");
map.put("propsType","0未定义 1食品 2加血药品 3加蓝药品 4加血和蓝药品 5传送符 6坐骑 7复活道具  8洗点 9带任务道具");
map.put("dateLimitType","使用时间限制 0:天,1:月,2:一生");
map.put("dateLimit","使用时间");
map.put("usedTimesInDateLimit","在一定的时限中可使用的次数");
map.put("stalemateTime","僵持时间 ");
map.put("upgradeEnergy","升级能量");
map.put("weaponType","0空手,1刀,2剑,3棍,4匕首,5弓");
map.put("buffName","Buff名");
map.put("buffLevel","Buff级别");
map.put("buffLastingTime","Buff持续时间");
map.put("suitEquipmentName","套装名");
map.put("material","材质");
map.put("levelLimit","等级限制");
map.put("fightStateLimit","战斗状态不可用");
map.put("gameMapLimitFlag","有游戏地图使用限制");
map.put("aiguilleEnergy","打孔能量");
map.put("strength","力量");
map.put("dexterity","灵巧");
map.put("constitution","体质");
map.put("spellpower","内力");
map.put("materialType","装备材质");
map.put("physicalDamageLowerLimit","物理攻击最小值");
map.put("physicalDamageUpperLimit","物理攻击最大值");
map.put("attackSpeed","攻击速度(毫秒)");
map.put("overLapNum","堆叠数量");
map.put("stroy","故事");
map.put("usedUndisappear","使用后不消失");
map.put("speed","速度百分比");
map.put("colorType","颜色0白,1绿,2蓝,3紫,4橙");
map.put("addTotalHpPresent","增加血百分比");
map.put("addTotalMpPresent","增加蓝百分比");
map.put("id","任务ID");
map.put("dependencyName","任务归属地");
map.put("taskName","任务名");
map.put("","");

%>
</head>
<body>
<input width="68" type="button" value="返回"
	onclick="javascript:history.back()">
<br>
<br>
<%
	Article article=new Article();
	if(articleName!=null&&!"".equals(articleName)){
		article = am.getArticle(articleName);
	}else{
		article = am.getArticleByCNname(articleCNName);
	}
		if(article != null){
			//
//			if(article instanceof SingleProps){
//				SingleProps sp = (SingleProps)article;
//				if(sp.getName().equals("普通喜酒")){
//					sp.setValues(new int[]{0,0,50000});
//				}else if(sp.getName().equals("白银喜酒")){
//					sp.setValues(new int[]{0,0,100000});
//				}else if(sp.getName().equals("黄金喜酒")){
//					sp.setValues(new int[]{0,0,200000});
//				}else if(sp.getName().equals("白金喜酒")){
//					sp.setValues(new int[]{0,0,400000});
//				}else if(sp.getName().equals("钻石喜酒")){
//					sp.setValues(new int[]{0,0,800000});
//				}
//			}
//			out.println("class=" + article.getClass().getName());

			
			Method ms[] = article.getClass().getMethods();
	  		ArrayList<Method> al = new ArrayList<Method>();
	  		for(int j = 0 ; j < ms.length ; j++){
	  			if(ms[j].getName().length() > 3 && (ms[j].getName().startsWith("get") || ms[j].getName().startsWith("is")) 
	  					&& (ms[j].getModifiers() & Modifier.PUBLIC) != 0
	  					&& ms[j].getParameterTypes().length == 0){
	  				String name = ms[j].getName();
	  				//属性为布尔值时
	  				if(name.indexOf("is") == 0){
	  					name = "set" + name.substring(2);
	  				}else{
	  					name = "s" + name.substring(1);
	  				}
	  				Class c = ms[j].getReturnType();
	  				if(c.isPrimitive() || c == String.class || c.toString().indexOf("class [") >= 0 || c.toString().indexOf("class com.fy.engineserver.datasource.props.AddPlan") >= 0){
	  					try{
	  						Method m = article.getClass().getMethod(name,new Class[]{c});
	  						//if(m != null){
	  							al.add(ms[j]);
	  						//}
	  					}catch(Exception e){
	  						
	  					}
	  				}
	  			}
	  		}
	%>
<table class="tablestyle1">

	<tr bgcolor="#C2CAF5" align="center">
		<td>属性</td>
		<td>描述</td>
		<td>值</td>
	</tr>
	<%
  		int alSize = al.size();
  		for(int j = 0; j < alSize; j++){
  			%>
	<tr bgcolor="#FFFFFF">
		<%
  				Method method = al.get(j);
  		  		String name ="";
  		  		if(method.getName().indexOf("is") == 0){
  		  			name = method.getName().substring(2);
  		  		}else{
  		  			name = method.getName().substring(3);
  		  		}
  		  		name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
  		  		%>
		<td align="center" style="word-wrap: break-word;"><%= name%></td>
		<td align="left" style="word-wrap: break-word;"><%=map.get(name) %>
		</td>
		<%
  				if(name.equals("ips")){
  					if(method.invoke(article) instanceof IntProperty[][]){
  						IntProperty[][] ipss = (IntProperty[][])method.invoke(article);
  						%>
		<td class="tdtable">
		<%
  						if(ipss != null && ipss.length != 0){
  							%>
		<table class="tablestyle2">
			<tr bgcolor="#C2CAF5" align="center">
				<td class="lefttd">级别</td>
				<td>属性名</td>
				<td>属性描述</td>
				<td class="righttd">属性值</td>
			</tr>
			<%
	  						for(int i = 0; i < ipss.length; i++){
	  							IntProperty ips[] = ipss[i];
	  							if(ips != null){
	  								out.println("<tr bgcolor='#FFFFFF' align='center'><td class='lefttd' rowspan='"+(ips.length == 0? 1 : ips.length)+"'>第"+(i+1)+"级</td>");
	  								if(ips.length == 0){
	  									out.println("<td></td><td></td><td class='righttd'></td></tr>");
	  								}
	  								for(int k = 0; k < ips.length; k++){
	  									IntProperty ip = ips[k];
	  									if(ip != null){
	  										if(k==0){
	  											out.println("<td>"+ip.getFieldName()+"</td><td>"+ip.getComment()+"</td><td class='righttd'>"+ip.getFieldValue()+"</td></tr>");
	  										}else{
	  											out.println("<tr bgcolor='#FFFFFF' align='center'><td>"+ip.getFieldName()+"</td><td>"+ip.getComment()+"</td><td class='righttd'>"+ip.getFieldValue()+"</td></tr>");	
	  										}
	  									}else{
	  										if(k==0){
	  											out.println("<td></td><td></td><td class='righttd'></td></tr>");
	  										}else{
	  											out.println("<tr bgcolor='#FFFFFF' align='center'><td></td><td></td><td class='righttd'></td></tr>");	
	  										}
	  									}
	  								}
	  							}else{
	  								out.println("<tr bgcolor='#FFFFFF' align='center'><td class='lefttd'></td><td></td><td></td><td class='righttd'></td></tr>");
	  							}
	  						}
  							%>
		</table>
		<%
  						}
  						%>
		</td>
		<%
  					}else if(method.invoke(article) instanceof IntProperty[]){
  						IntProperty[] ips = (IntProperty[])method.invoke(article);
  						%>
		<td class="tdtable">
		<%
  						if(ips != null && ips.length != 0){
  							%>
		<table class="tablestyle2">
			<tr bgcolor="#C2CAF5" align="center">
				<td class="lefttd">属性名</td>
				<td>属性描述</td>
				<td class="righttd">属性值</td>
			</tr>
			<%
	  						for(int i = 0; i < ips.length; i++){
								IntProperty ip = ips[i];
								if(ip != null){
									out.println("<tr bgcolor='#FFFFFF' align='center'><td class='lefttd'>"+ip.getFieldName()+"</td><td>"+ip.getComment()+"</td><td class='righttd'>"+ip.getFieldValue()+"</td></tr>");
								}else{
									out.println("<tr bgcolor='#FFFFFF' align='center'><td class='lefttd'></td><td></td><td class='righttd'></td></tr>");
								}
	  						}
  							%>
		</table>
		<%
  						}
  						%>
		</td>
		<%
  					}
  					
  				}else if(name.equals("careerConfine")){
  					StringBuffer sb = new StringBuffer();
  					int[] careerConfine = (int[])method.invoke(article);
  					if(careerConfine != null && careerConfine.length != 0){
  						for(int i : careerConfine){
  							if(i == 0){
  								sb.append("武当,");
  							}
  							if(i == 1){
  								sb.append("少林,");
  							}
  							if(i == 2){
  								sb.append("峨眉,");
  							}
  							if(i == 3){
  								sb.append("明教,");
  							}
  							if(i == 4){
  								sb.append("五毒,");
  							}
  						}
  					}
  					%>
		<td align="center" style="word-wrap: break-word;"><%= sb.length()!=0?  sb.toString().substring(0,sb.toString().lastIndexOf(",")):"全都限制"%></td>
		<%
  				}else if(name.equals("name")){
  					%>
		<td class="tdtable" align="center" style="word-wrap: break-word;"><%= method.invoke(article)%></td>
		<%
  				}else if(name.equals("categoryName")){
  					%>
		<td class="tdtable" align="center" style="word-wrap: break-word;"><a
			href="propcategorybyname.jsp?categoryName=<%=method.invoke(article) %>"><%=method.invoke(article)%></a></td>
		<%
  				}else if(name.equals("addPlan")){
  					if(method.invoke(article) instanceof AddPlan){
  						AddPlan ap = (AddPlan)method.invoke(article);
  						if(ap != null){
  						IntProperty[][] ipss = ap.getIps();
  						%>
		<td class="tdtable">
		<%
  						if(ipss != null && ipss.length != 0){
  							%>
		<table class="tablestyle2">
			<tr bgcolor="#C2CAF5" align="center">
				<td colspan="4">附加方案名:<%=ap.getPlanName() %></td>
			</tr>
			<tr bgcolor="#C2CAF5" align="center">
				<td>级别</td>
				<td>属性名</td>
				<td>属性描述</td>
				<td>属性值</td>
			</tr>
			<%
  							int addPlanCount = 0;
	  						for(int i = 0; i < ipss.length; i++){
	  							IntProperty ips[] = ipss[i];
	  							addPlanCount += 4;
	  							if(ips != null){
	  								out.println("<tr bgcolor='#FFFFFF' align='center'><td rowspan='"+(ips.length == 0? 1 : ips.length)+"'>"+(addPlanCount)+"级</td>");
	  								if(ips.length == 0){
	  									out.println("<td></td><td></td><td></td></tr>");
	  								}
	  								for(int k = 0; k < ips.length; k++){
	  									IntProperty ip = ips[k];
	  									if(ip != null){
	  										if(k==0){
	  											out.println("<td>"+ip.getFieldName()+"</td><td>"+ip.getComment()+"</td><td>"+ip.getFieldValue()+"</td></tr>");
	  										}else{
	  											out.println("<tr bgcolor='#FFFFFF' align='center'><td>"+ip.getFieldName()+"</td><td>"+ip.getComment()+"</td><td>"+ip.getFieldValue()+"</td></tr>");	
	  										}
	  									}else{
	  										if(k==0){
	  											out.println("<td></td><td></td><td></td></tr>");
	  										}else{
	  											out.println("<tr bgcolor='#FFFFFF' align='center'><td></td><td></td><td></td></tr>");	
	  										}
	  									}
	  								}
	  							}else{
	  								out.println("<tr bgcolor='#FFFFFF' align='center'><td></td><td></td><td></td><td></td></tr>");
	  							}
	  						}
  							%>
		</table>
		<%
  						}
  						%>
		</td>
		<%
  					}}
  				}else if(name.equals("buffName")){
  					String buffName = (String)method.invoke(article);
  					if(buffName != null ){
  						String buffNameEncode = java.net.URLEncoder.encode(buffName);
  	  					%>
		<td align="center" style="word-wrap: break-word;"><a
			href="buffbyname.jsp?buffName=<%=buffNameEncode %>"><%=buffName %></a></td>
		<%
  					}else{
	  					%>
		<td align="center" style="word-wrap: break-word;"></td>
		<%
  					}
  				}else if(name.equals("suitEquipmentName")){
  					String suitEquipmentName = (String)method.invoke(article);
  					if(suitEquipmentName != null ){
  	  					%>
		<td align="center" style="word-wrap: break-word;"><%=suitEquipmentName %></td>
		<%
  					}else{
	  					%>
		<td align="center" style="word-wrap: break-word;">非套装</td>
		<%
  					}
  				}else if(name.equals("id")){
  	  					%>
		<td align="center" style="word-wrap: break-word;"><a
			href="TaskByTaskId.jsp?taskId=<%=method.invoke(article) %>"><%=method.invoke(article) %></a></td>
		<%
  				}else if(name.equals("materialType")){

  				}else{
  					if(method.getReturnType().getName().indexOf("[") == 0){
					%>
		<td align="center" style="word-wrap: break-word;"><%= gson.toJson(method.invoke(article))%></td>

		<%
					}else{
					%>
		<td align="center" style="word-wrap: break-word;"><%= method.invoke(article)%></td>
		<% 
					}
  				}
  				%>
	</tr>
	<%} %>
</table>
<%}else{
			out.println("<h2>物品不存在</h2>");
		}
	
	%>
<br />
<input width="68" type="button" value="返回"
	onclick="javascript:history.back()">


</body>
</html>
