<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
											com.fy.engineserver.shop.*,
											com.fy.engineserver.sprite.*,
											com.fy.engineserver.datasource.props.*,
											com.xuanzhi.tools.text.*,
											com.xuanzhi.boss.game.*"%>
											<%!Hashtable<String,Goods> goodsTable = new Hashtable<String, Goods>(); %>
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="../css/common.css"/>
<link rel="stylesheet" href="../css/table.css"/>
<style type="text/css">
.titlecolor{
background-color:#C2CAF5;
}

</style>
<script language="JavaScript">
<!--
function subcheck() {
	f1.submit();
}
-->
</script>
<script type="text/javascript">
function buyArticles(mapStr,index){
	var idStr = mapStr + index;
	document.getElementById("articleCount").value = document.getElementById(idStr).value;
	document.getElementById("mapStr").value = mapStr;
	document.getElementById("articleIndex").value = index;
	document.form1.submit();
}
function computeValueByCount(index){
	var fpArticlePrice = 0;
	var sailValue = 0;
	var fpArticleCount = 0;
	
	for(var i = 0; i < 100; i++){
		try{
		fpArticlePrice = document.getElementById("fpArticlePrice"+index+"_"+i).value;
		fpArticleCount = document.getElementById("fpArticleCount"+index+"_"+i).value;
		sailValue = sailValue + fpArticlePrice*fpArticleCount;
		}catch(e){
			break;
		}
	}
	var allhiddenfpSailValue = 0;
	allhiddenfpSailValue = document.getElementById("allhiddenfpSailValue"+index).value
	document.getElementById("fpSailValue"+index).innerHTML = sailValue;
	document.getElementById("allfpSailValue"+index).innerHTML = sailValue + allhiddenfpSailValue*1;
}
function computeValueBySailValue(index1,index2){
	var time = new Date();
	var fpArticleName = "";
	fpArticleName = document.getElementById("fpArticleName"+index1+"_"+index2).innerHTML;
	var fpArticlePrice = document.getElementById("fpArticlePrice"+index1+"_"+index2).value;
	if(fpArticleName != null){
		var count = 0;
		try{
			for(var i = 0; i < 10000; i++){
				document.getElementById("fp"+i).innerHTML;
				for(var j = 0; j < 100; j++){
					try{
						if(document.getElementById("fpArticleName"+i+"_"+j).innerHTML == fpArticleName){
							document.getElementById("fpArticlePrice"+i+"_"+j).value = fpArticlePrice;
							computeValueByCount(i);
							count++;
							break;
						}
					}catch(e){
						break;
					}
				}
			}
		}catch(e){
			alert("刷新完，共修改"+count+"个数值。共用时"+(new Date()-time)+"毫秒");
		}
	}
}
</script>
</head>
<body>
<br>

<%
int level = 0;
String levelStr = request.getParameter("level");
if(levelStr != null){
	try{
		level = Integer.parseInt(levelStr);
	}catch(Exception e){
		
	}
}
String colors = request.getParameter("colors");
if(colors == null){
	colors = "-1";
}
String mingcheng = request.getParameter("mingcheng");
if(mingcheng == null){
	mingcheng = "";
}
%>
<form>
输入要查看多少级的装备<input type="text" name="level" value="<%=level %>">&nbsp;
颜色
<select name="colors">
<option value="-1" <%=(colors.equals("-1") ? "selected":"") %>>全部
<option value="0" <%=(colors.equals("0") ? "selected":"") %>>白
<option value="1" <%=(colors.equals("1") ? "selected":"") %>>绿
<option value="2" <%=(colors.equals("2") ? "selected":"") %>>蓝
<option value="3" <%=(colors.equals("3") ? "selected":"") %>>紫
<option value="4" <%=(colors.equals("4") ? "selected":"") %>>橙
</select>
&nbsp;
名称<input type="text" name="mingcheng" value="<%=mingcheng %>"><br>
<input type="submit" value="提交">
</form>
<%
ShopManager sm = ShopManager.getInstance();
Map<String,Shop> map = sm.getShops();
int playerId = -1;
//服务器类型为0代表为可修改的开发服务器
if(GameConstants.getInstance().getServerType() == 0){
}
ArticleManager am = ArticleManager.getInstance();
FormulaProps[] fps = am.getAllFormulaProps();
if(map != null && map.keySet() != null){
	for(String key : map.keySet()){
		if(key != null && map.get(key) != null){
			Shop shop = map.get(key);
			if(shop.getName().indexOf("奇珍商店") >= 0 || shop.getName().indexOf("打折抢购") >= 0){
				continue;
			}
			Goods[] goods = shop.getGoods();
			if(goods != null){
				for(Goods good : goods){
					if(good != null && (good.getBindYuanboPrice() > 0 || good.getYuanboPrice() > 0)){
						if(goodsTable.get(good.getArticleName()) == null){
							goodsTable.put(good.getArticleName(),good);
						}
					}
				}
			}
		}
	}
	%>
	<table>
	<tr class="titlecolor">
	<td rowspan="2">合成卷轴</td><td rowspan="2">合成的物品</td><td colspan="3">材料概况</td><td rowspan="2">总价</td><td rowspan="2">加上卷轴的价格</td>
	</tr>
	<tr class="titlecolor">
		<td>材料</td>
		<td>个数</td>
		<td>单价:元宝</td>
	</tr>
<%
if(fps != null){
	for(int j = 0; j < fps.length; j++){
		FormulaProps fp = fps[j];
			if(fp != null){
				int sailsValue = 0;
				int allSailsValue = 0;
				String composeName = fp.getComposedArticleName();
				Article comArticle = am.getArticle(composeName);
				if(level != 0){
					if(!(comArticle instanceof Equipment)){
						continue;
					}
				if(comArticle instanceof Equipment){
					if(((Equipment)comArticle).getPlayerLevelLimit() != level){
						continue;	
					}
				}
				}
				if(!colors.equals("-1")){
					if(!(comArticle instanceof Equipment)){
						continue;
					}
				if(comArticle instanceof Equipment){
					if(((Equipment)comArticle).getColorType() != Integer.parseInt(colors)){
						continue;	
					}
				}
				}
				if(!mingcheng.trim().equals("")){

				if(fp.getName().indexOf(mingcheng) < 0){
					continue;
				}
				}
				%>
				<tr>
				<td id="fp<%=j %>" rowspan="<%=fp.getApps() != null?fp.getApps().length:1 %>"><a href="ArticleByName.jsp?articleName=<%=fp.getName() %>"><%=fp.getName() %></a></td>
				<%

				if(comArticle != null){
					if(comArticle instanceof Equipment){
						out.println("<td rowspan='"+(fp.getApps() != null?fp.getApps().length:1) +"'><a href='ArticleByName.jsp?articleName="+comArticle.getName()+"'>"+((Equipment)comArticle).getPlayerLevelLimit()+"级"+Equipment.COLOR_TYPE_NAMES[((Equipment)comArticle).getColorType()]+"色"+comArticle.getName()+"</a></td>");
					}else if(comArticle instanceof Props){
						out.println("<td rowspan='"+(fp.getApps() != null?fp.getApps().length:1) +"'><a href='ArticleByName.jsp?articleName="+comArticle.getName()+"'>"+((Props)comArticle).getLevelLimit()+"级"+comArticle.getName()+"</a></td>");
					}else{
						out.println("<td rowspan='"+(fp.getApps() != null?fp.getApps().length:1) +"'><a href='ArticleByName.jsp?articleName="+comArticle.getName()+"'>"+comArticle.getName()+"</a></td>");
					}
				}else{
					out.println("<td rowspan='"+(fp.getApps() != null?fp.getApps().length:1) +"' bgcolor='red'>没有合成物品</td>");
				}
				if(fp.getApps() != null){
					if(fp.getApps().length >= 1){
						ArticleProperty ap = fp.getApps()[0];
						if(ap != null){
							%>
							<td><label id="fpArticleName<%=j %>_<%=0 %>"><%=ap.getArticleName() %></label></td>
							<td><input type="text" style="width:100px" id="fpArticleCount<%=j %>_<%=0 %>" value="<%=ap.getValue() %>" ondblclick="javascript:computeValueByCount('<%=j %>');">个</td>
							<%
							boolean exist = false;
							if(goodsTable.get(ap.getArticleName()) != null){
								Goods good = goodsTable.get(ap.getArticleName());
								exist = true;
								int value = (int)good.getYuanboPrice()/good.getBundleNum();
								out.println("<td><input type='text' style='width:100px' id='fpArticlePrice"+j+"_"+0+"' value='"+value+"' ondblclick='javascript:computeValueBySailValue("+j+","+0+");'>"+"</td>");
								value = value*ap.getValue();
								sailsValue +=value;
							}
							if(goodsTable.get(fp.getName()) != null){
								Goods good = goodsTable.get(fp.getName());
								int value = (int)good.getYuanboPrice()/good.getBundleNum();
								allSailsValue =value;
							}
							if(!exist){
								sailsValue +=1000000;
								out.println("<td><input type='text' style='width:100px' id='fpArticlePrice"+j+"_"+0+"' value='"+1000000+"' ondblclick='javascript:computeValueBySailValue("+j+","+0+");'></td>");
							}
						}
					}
				}
				%>
				<td rowspan="<%=fp.getApps() != null ? fp.getApps().length:1 %>"><label id="fpSailValue<%=j %>" ><%=sailsValue %>"</label></td>
				<td rowspan="<%=fp.getApps() != null ? fp.getApps().length:1 %>"><label id="allfpSailValue<%=j %>" ><%=allSailsValue+sailsValue %>"</label>	<input type = "hidden" id="allhiddenfpSailValue<%=j %>" value = <%=allSailsValue %>></td>
				</tr>
				<%
				if(fp.getApps() != null){
					for(int i = 1; i < fp.getApps().length; i++){
						ArticleProperty ap = fp.getApps()[i];
						if(ap != null){
							%>
				<tr>
							<td><label id="fpArticleName<%=j %>_<%=i %>"><%=ap.getArticleName() %></label></td>
							<td><input type="text" style="width:100px" id="fpArticleCount<%=j %>_<%=i %>" value="<%=ap.getValue() %>" ondblclick="javascript:computeValueByCount('<%=j %>')">个</td>
							<%
							boolean exist = false;
							if(goodsTable.get(ap.getArticleName()) != null){
								Goods good = goodsTable.get(ap.getArticleName());
								exist = true;
								int value = (int)good.getYuanboPrice()/good.getBundleNum();
								out.println("<td><input type='text' style='width:100px' id='fpArticlePrice"+j+"_"+0+"' value='"+value+"' ondblclick='javascript:computeValueBySailValue("+j+","+0+");'>"+"</td>");
								value = value*ap.getValue();
								sailsValue +=value;
							}
							if(!exist){
								sailsValue +=1000000;
								out.println("<td><input type='text' style='width:100px' id='fpArticlePrice"+j+"_"+i+"' value='"+1000000+"' ondblclick='javascript:computeValueBySailValue("+j+","+i+");'></td>");
							}
							%>
				</tr>
							<%
						}
					}
					%>
					<script>
					document.getElementById("fpSailValue<%=j%>").innerHTML = <%=sailsValue%>;
					document.getElementById("allfpSailValue<%=j%>").innerHTML = <%=allSailsValue+sailsValue%>;
					</script>
					<%
				}
			}
		}
	}

%>
	</table>
	<%
}
%>
</body>
</html>
