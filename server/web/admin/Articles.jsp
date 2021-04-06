<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PropsCategory"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Weapon"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.career.CareerManager"%><%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<title>物品一览2</title>
<script type="text/javascript">
function onClickValueChange(value){
	document.getElementById("articlesName").value=value;
	document.ArticleForPageForm.submit();
	
}
function onclickImage(str){
	document.getElementById("articleName").value=str;
	document.ArticleByNameForm.submit();
}
function onclickButton(){
	if(document.getElementById("selectname").value != ""){
		document.getElementById("articleNames").value=document.getElementById("selectname").value;
		document.QueryArticleByNameForm.submit();
	}
}
function onclickLink(obj){
		document.getElementById("articleName").value=obj;
		document.ArticleByNameForm.submit();
}
function ajaxFunction(id)
{
	var xmlHttp;

	try
	   {
	  // Firefox, Opera 8.0+, Safari
	   xmlHttp=new XMLHttpRequest();
	   }
	catch (e)
	   {

	 // Internet Explorer
	  try
	     {
	     xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
	     }
	  catch (e)
	     {

	     try
	        {
	        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
	        }
	     catch (e)
	        {
	        alert("您的浏览器不支持AJAX！");
	        return false;
	        }
	     }
	   }

	xmlHttp.onreadystatechange=function()
	{
	if(xmlHttp.readyState==4)
	  {
	   //document.myForm.name.value=xmlHttp.ResponseText;
	   document.getElementById("showdiv").innerHTML = xmlHttp.responseText;
	   setposition(id);
	  }
	}
	xmlHttp.open("GET","ArticlesSelectajax.jsp?articleName="+id,true);
	xmlHttp.send(null);


	}
	function onClickValueChangeForTitle(id){
			document.body.style.cursor='hand';
			ajaxFunction(id);
		}
	function setposition(id){
		  var obj = document.getElementById("showdiv");
			obj.style.display="";
			obj.style.border="1px solid red";
			var objheight = obj.clientHeight;
			var objwidth = obj.clientWidth;
			var o = document.getElementById(id);
			var lefttopx = o.offsetLeft;
			var lefttopy = o.offsetTop;
			var oparent = o.offsetParent;
			while(oparent.tagName != "BODY"){
				lefttopx = lefttopx + oparent.offsetLeft;
				lefttopy = lefttopy + oparent.offsetTop;
				oparent = oparent.offsetParent;
			}
			
			
			var righttopx = lefttopx+o.offsetWidth;
			//var righttopy = o.offsetTop;
			//var leftbottomx = o.offsetLeft;
			var leftbottomy = lefttopy+o.offsetHeight;
			//var rightbottomx = o.offsetLeft+o.offsetWidth;
			//var rightbottomy = o.offsetTop+o.offsetHeight;
			var scrollTopvalue = document.documentElement.scrollTop;
			var scrollLeftvalue = document.documentElement.scrollLeft;
			var clientHeightvalue = document.documentElement.clientHeight;
			var clientWidthvalue = document.documentElement.clientWidth;
			//如果相对左边横坐标大于浏览器宽度的1/2，那么就把位置设置在这个对象的左边，以此类推就可以比较好的显示
			if((lefttopx-scrollLeftvalue)>(clientWidthvalue/2)){
				if((lefttopx-2)>objwidth){
					obj.style.left = (lefttopx-2-objwidth)+"px";
				}else{
					obj.style.left = (scrollLeftvalue+2)+"px";
				}
			}else{
				obj.style.left=(righttopx+2)+"px";
			}
			if((lefttopy-scrollTopvalue)> (clientHeightvalue/2)){
				if(objheight>clientHeightvalue){
					obj.style.top = (scrollTopvalue+2)+"px";
				}else{
					if((lefttopy-2)>objheight){
						obj.style.top = (lefttopy-2-objheight)+"px";
					}else{
						obj.style.top = (scrollTopvalue+2)+"px";
					}
				}
				
			}else{
				obj.style.top = (leftbottomy+2)+"px";
			}
	}
	function divHidden(){
		  var obj = document.getElementById("showdiv");
		  document.body.style.cursor='default';
		  obj.style.display="none";
	}
	function sortOnclick(){
		var obj = document.getElementById('sortflag'); //selectid
		  var index = obj.selectedIndex; // 选中索引
		  var value = obj.options[index].value; // 选中值  
				
		//document.getElementById("sortindex1").value = value;
		  document.ArticleForPageForm2.sortindex.value = value;
		document.ArticleForPageForm2.submit();
	}
</script>
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
td{
text-align:center;
}
.titlecolor{
background-color:#C2CAF5;
}
#showdiv{
background-color:#DCE0EB;
text-align:left;
margin:0px 0px;
}

#Weapondiv a{text-decoration:none;color:#D59916}
#Weapondiv a:visited{color:#D59916}
#Weapondiv a:hover{color:red}

#Equipmentdiv a{text-decoration:none;color:#D59916}
#Equipmentdiv a:visited{color:#D59916}
#Equipmentdiv a:hover{color:red}

#Expendablediv a{text-decoration:none;color:#D59916}
#Expendablediv a:visited{color:#D59916}
#Expendablediv a:hover{color:red}

#Stonediv a{text-decoration:none;color:#D59916}
#Stonediv a:visited{color:#D59916}
#Stonediv a:hover{color:red}

</style>
<%
String[] colors = new String[]{"白","绿","蓝","紫","橙","白","绿","蓝","紫","橙"};
String[] colorss = new String[]{"white","green","blue","purple","orange","white","green","blue","purple","orange"};
String articleName = request.getParameter("articlesName");
int PAGE_ROW_NO = 10000;
byte weaponType = -1;
byte equipmentType = -1;
if(articleName == null){
	articleName ="allArticles";
}
if(articleName != null){
	ArticleManager am = ArticleManager.getInstance();
	PropsCategory propsCategory[] = am.getAllPropsCategory();
	List<Article> articlesList = new ArrayList<Article>();
	Article[] articlesArray = null;
	if(articleName.equals("Knifes")){
		articlesArray = am.getAllWeaponKnifes();
	}else if(articleName.equals("Weapon")){
		articlesArray = am.getAllWeapons();
	}else if(articleName.equals("Equipment")){
		articlesArray = am.getAllEquipments();
	}else if(articleName.equals("Stone")){
		articlesList = new ArrayList<Article>();
		if(am.getAllInlayArticles() != null){
			for(Article a : am.getAllInlayArticles()){
				articlesList.add(a);
			}
		}
		if(am.getAllUpgradeArticles() != null){
			for(Article a : am.getAllUpgradeArticles()){
				articlesList.add(a);
			}
		}
		if(am.getAllAiguilleArticles() != null){
			for(Article a : am.getAllAiguilleArticles()){
				articlesList.add(a);
			}
		}
		articlesArray = articlesList.toArray(new Article[0]);
	}else if(articleName.equals("Expendable")){
		articlesList = new ArrayList<Article>();
		if(am.getAllSingleProps() != null){
			for(Article a : am.getAllSingleProps()){
				articlesList.add(a);
			}
		}
		if(am.getAllLastingProps() != null){
			for(Article a : am.getAllLastingProps()){
				articlesList.add(a);
			}
		}
		articlesArray = articlesList.toArray(new Article[0]);
	}else if(articleName.equals("Swords")){
		articlesArray = am.getAllWeaponSwords();
	}else if(articleName.equals("Skicks")){
		articlesArray = am.getAllWeaponSkicks();
	}else if(articleName.equals("Daggers")){
		articlesArray = am.getAllWeaponDaggers();
	}else if(articleName.equals("Bows")){
		articlesArray = am.getAllWeaponBows();
	}else if(articleName.equals("heads")){
		articlesArray = am.getAllEquipmentheads();
		equipmentType = EquipmentColumn.EQUIPMENT_TYPE_head;
	}else if(articleName.equals("shoulders")){
		articlesArray = am.getAllEquipmentshoulders();
		equipmentType = EquipmentColumn.EQUIPMENT_TYPE_shoulder;
	}else if(articleName.equals("bodys")){
		articlesArray = am.getAllEquipmentbodys();
		equipmentType = EquipmentColumn.EQUIPMENT_TYPE_body;
	}else if(articleName.equals("hands")){
		articlesArray = am.getAllEquipmenthands();
		equipmentType = EquipmentColumn.EQUIPMENT_TYPE_hand;
	}else if(articleName.equals("foots")){
		articlesArray = am.getAllEquipmentfoots();
		equipmentType = EquipmentColumn.EQUIPMENT_TYPE_foot;
	}else if(articleName.equals("jewelrys")){
		articlesArray = am.getAllEquipmentjewelrys();
		equipmentType = EquipmentColumn.EQUIPMENT_TYPE_jewelry;
	}else if(articleName.equals("necklace")){
		articlesArray = am.getAllEquipmentnecklace();
		equipmentType = EquipmentColumn.EQUIPMENT_TYPE_necklace;
	}else if(articleName.equals("fingerring")){
		articlesArray = am.getAllEquipmentfingerring();
		equipmentType = EquipmentColumn.EQUIPMENT_TYPE_fingerring;
	}else if(articleName.equals("SingleProps")){
		articlesArray = am.getAllSingleProps();
	}else if(articleName.equals("LastingProps")){
		articlesArray = am.getAllLastingProps();
	}else if(articleName.equals("TransferPaper")){
		articlesArray = am.getAllTransferPapers();
	}else if(articleName.equals("MountProps")){
		articlesArray = am.getAllMountProps();
	}else if(articleName.equals("TaskProps")){
		articlesArray = am.getAllTaskProps();
	}else if(articleName.equals("InlayArticle")){
		articlesArray = am.getAllInlayArticles();
	}else if(articleName.equals("UpgradeArticle")){
		articlesArray = am.getAllUpgradeArticles();
	}else if(articleName.equals("AiguilleArticle")){
		articlesArray = am.getAllAiguilleArticles();
	}else if(articleName.equals("OtherArticles")){
		articlesList = new ArrayList<Article>();
		if(am.getAllClearSkillPointsProps() != null){
			for(Article a : am.getAllClearSkillPointsProps()){
				articlesList.add(a);
			}
		}
		if(am.getAllTransferPapers() != null){
			for(Article a : am.getAllTransferPapers()){
				articlesList.add(a);
			}
		}
		if(am.getAllMountProps() != null){
			for(Article a : am.getAllMountProps()){
				articlesList.add(a);
			}
		}
		if(am.getAllPackageProps() != null){
			for(Article a : am.getAllPackageProps()){
				articlesList.add(a);
			}
		}
		if(am.getAllRandomPackageProps() != null){
			for(Article a : am.getAllRandomPackageProps()){
				articlesList.add(a);
			}
		}
		if(am.getAllMaterialArticle() != null){
			for(Article a : am.getAllMaterialArticle()){
				articlesList.add(a);
			}
		}
		if(am.getAllFormulaProps() != null){
			for(Article a : am.getAllFormulaProps()){
				articlesList.add(a);
			}
		}
		if(am.getAllOtherArticles() != null){
			for(Article a : am.getAllOtherArticles()){
				articlesList.add(a);
			}
		}
		articlesArray = articlesList.toArray(new Article[0]);
	}else if(articleName.equals("allArticles")){
		articlesArray = am.getAllArticles();
	}
	
	String sortindex = request.getParameter("sortindex");
	if(sortindex == null){
		sortindex = "0";
	}

	String pageindex = request.getParameter("pageindex");
	int m = 0;
	int index = 0;
	if(pageindex == null){
		pageindex = "0";
	}
	m = Integer.parseInt(pageindex)*PAGE_ROW_NO;
	index = Integer.parseInt(pageindex);
%>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
</head>
<body>
<div id="showdiv" style="position:absolute;  display:none;"></div>
<form name="ArticleByNameForm" action="ArticleByName.jsp" method="get">
<input type="hidden" id="articleName" name="articleName">
</form>
<form name="QueryArticleByNameForm" action="QueryArticles.jsp" method="get">
<input type="hidden" id="articleNames" name="articleNames">
</form>
<form name="ArticleForPageForm" action="Articles.jsp" method="get">
<input type="hidden" id="articlesName" name="articlesName">
<input type="hidden" name="sortindex" value="<%=sortindex %>">
</form>
<form name="ArticleForPageForm2" action="Articles.jsp" method="get">
<input type="hidden" name="articlesName" value="<%=articleName %>">
<input type="hidden" id="sortindex" name="sortindex">
</form>
<div id=pltsTipLayer style="display: none;position: absolute; z-index:10001"></div>
<div>
<span>
<a href="javascript:onClickValueChange('allArticles');">全部</a>
</span>|
<span>
<a href="javascript:onClickValueChange('Weapon');">武器</a>
</span>|
<span>
<a href="javascript:onClickValueChange('Equipment');">装备</a>
</span>|
<span>
<a href="javascript:onClickValueChange('Stone');" >宝石</a>
</span>|
<span>
<a href="javascript:onClickValueChange('TaskProps');" >触发任务道具</a>
</span>|
<span>
<a href="javascript:onClickValueChange('Expendable');" >消耗品</a>
</span>|
<span>
<a href="javascript:onClickValueChange('OtherArticles');">其他</a>
</span>
</div>
<div id="Weapondiv" style="display:<%=articleName.equals("Weapon")||articleName.equals("Knifes")||articleName.equals("Swords")||articleName.equals("Skicks")||articleName.equals("Daggers")||articleName.equals("Bows")?"":"none" %>">
<span>
<a class="acolor" href="javascript:onClickValueChange('Knifes');">刀</a>
</span>
<span>
<a class="acolor" href="javascript:onClickValueChange('Swords');">剑</a>
</span>
<span>
<a class="acolor" href="javascript:onClickValueChange('Skicks');">棍</a>
</span>
<span>
<a class="acolor" href="javascript:onClickValueChange('Daggers');">匕首</a>
</span>
<span>
<a class="acolor" href="javascript:onClickValueChange('Bows');">弓</a>
</span>
</div>
<div id="Equipmentdiv" style="display:<%=articleName.equals("Equipment")||articleName.equals("heads")||articleName.equals("shoulders")||articleName.equals("bodys")||articleName.equals("hands")||articleName.equals("foots")||articleName.equals("jewelrys")||articleName.equals("necklace")||articleName.equals("fingerring")?"":"none" %>">
<span>
<a class="acolor" href="javascript:onClickValueChange('heads');">头盔</a>
</span>
<span>
<a class="acolor" href="javascript:onClickValueChange('shoulders');">护肩</a>
</span>
<span>
<a class="acolor" href="javascript:onClickValueChange('bodys');">衣服</a>
</span>
<span>
<a class="acolor" href="javascript:onClickValueChange('hands');">护手</a>
</span>
<span>
<a class="acolor" href="javascript:onClickValueChange('foots');">靴子</a>
</span>
<span>
<a class="acolor" href="javascript:onClickValueChange('jewelrys');">首饰</a>
</span>
<span>
<a class="acolor" href="javascript:onClickValueChange('necklace');">项链</a>
</span>
<span>
<a class="acolor" href="javascript:onClickValueChange('fingerring');">戒指</a>
</span>
</div>
<div id="Expendablediv" style="display:<%=articleName.equals("Expendable")||articleName.equals("SingleProps")||articleName.equals("LastingProps")?"":"none" %>">
<span>
<a class="acolor" href="javascript:onClickValueChange('SingleProps');">简单道具</a>
</span>
<span>
<a class="acolor" href="javascript:onClickValueChange('LastingProps');">持续性道具</a>
</span>
</div>
<div id="Stonediv" style="display:<%=articleName.equals("Stone")||articleName.equals("InlayArticle")||articleName.equals("UpgradeArticle")||articleName.equals("AiguilleArticle")?"":"none" %>">
<span>
<a class="acolor" href="javascript:onClickValueChange('InlayArticle');">镶嵌类宝石</a>
</span>
<span>
<a class="acolor" href="javascript:onClickValueChange('UpgradeArticle');">升级类物品</a>
</span>
<span>
<a class="acolor" href="javascript:onClickValueChange('AiguilleArticle');">打孔类物品</a>
</span>
</div>
<br>
<%
int n = articlesArray.length;
if(n > 0){
	//每页60条数据
	int pageCount = 0;
	pageCount = n/PAGE_ROW_NO;
	if(n%PAGE_ROW_NO != 0){
		pageCount+=1;
	}
	out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>当前页为第<font color='red'>"+(index+1)+"</font>页&nbsp;&nbsp;总共<font color='#2D20CA'>"+pageCount+"</font>页&nbsp;&nbsp;"+n+"条数据&nbsp;&nbsp;每页"+PAGE_ROW_NO+"条数据</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id='selectname' style='width:80px'><input type='button' value='搜索' onclick='javascript:onclickButton();'>  <select id='sortflag' name='sortflag' onchange='javascript:sortOnclick();'><option value='0' "+(sortindex.equals("0")?"selected":"")+">未排序</option><option value='1' "+(sortindex.equals("1")?"selected":"")+">按照等级颜色排序</option></select><br><br>");
	out.println("<a href='Articles.jsp?articlesName="+articleName+"&pageindex="+0+"&sortindex="+sortindex+"'>首页  </a>");
	if(index > 0){
		out.println("<a href='Articles.jsp?articlesName="+articleName+"&pageindex="+(index-1)+"&sortindex="+sortindex+"'><<上一页<<  </a>");	
	}
	for(int i = 0; i < 10; i++){
		if(index+i < pageCount){
		out.println("<a href='Articles.jsp?articlesName="+articleName+"&pageindex="+(index+i)+"&sortindex="+sortindex+"'>第"+(index+i+1)+"页  </a>");
		}
	}
	if(index <(pageCount-1)){
		out.println("<a href='Articles.jsp?articlesName="+articleName+"&pageindex="+(index+1)+"&sortindex="+sortindex+"'>>>下一页>>  </a>");	
	}
	out.println("<a href='Articles.jsp?articlesName="+articleName+"&pageindex="+(pageCount-1)+"&sortindex="+sortindex+"'>末页  </a>");
}%>

<!-- 装备 -->
<%if(articleName.equals("Equipment")){ %>
	<table>
		<tr class="titlecolor" align="center">
		<%
		Equipment[] equipments = null;
		if(sortindex.equals("0")){
			if((m+PAGE_ROW_NO)>n){
				equipments = am.getEquipmentByPage(weaponType,m,(n-m),0);
			}else{
				equipments = am.getEquipmentByPage(weaponType,m,PAGE_ROW_NO,0);
			}
		}else{
			if((m+PAGE_ROW_NO)>n){
				equipments = am.getEquipmentByPage(weaponType,m,(n-m),1);
			}else{
				equipments = am.getEquipmentByPage(weaponType,m,PAGE_ROW_NO,1);
			}
		}
		%>
			<td colspan="18">服务器全部<font color="red">装备</font>列表</td>
		</tr>
		<tr class="titlecolor" align="center">
			<td>物品类</td><td>物品名</td><td>物品等级</td><td>使用等级</td><td>颜色</td><td>玩家职业限制</td><td>玩家境界限制</td><td>耐久度</td><td>套装名</td><td>装备最高级别</td>
		</tr>
		<%
		for(int i = 0; equipments != null && i < equipments.length; i++){
			if(equipments[i] != null){
				Equipment article = equipments[i];
				String bindStyleStr = "";
				if(article.getBindStyle()==0){
					bindStyleStr = "不绑定";
				}else if(article.getBindStyle()==1){
					bindStyleStr = "装备绑定";
				}else if(article.getBindStyle()==2){
					bindStyleStr = "拾取绑定";
				}
				if(i==0){
						%>
					<tr bgcolor="#FFFFFF" align="center">
					<td rowspan="<%= equipments.length%>">装备</td>
					<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
					<td><%=article.getArticleLevel() %></td>
					<td><%=article.getPlayerLevelLimit() %></td>
					<td bgcolor="<%=colorss[article.getColorType()] %>"><%=ArticleManager.getColorString(article,article.getColorType()) %></td>
					<td><%=CareerManager.careerNameByType(article.getCareerLimit())%></td>
					<td><%=article.getClassLimit()%></td>
					<td><%=article.getDurability()%></td>
					<td><%=article.getSuitEquipmentName()%></td>
					<td></td>
					</tr>
				<%
				}else{
					%>
					<tr bgcolor="#FFFFFF" align="center">
					<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
					<td><%=article.getArticleLevel() %></td>
					<td><%=article.getPlayerLevelLimit() %></td>
					<td bgcolor="<%=colorss[article.getColorType()] %>"><%=ArticleManager.getColorString(article,article.getColorType()) %></td>
					<td><%=CareerManager.careerNameByType(article.getCareerLimit())%></td>
					<td><%=article.getClassLimit()%></td>
					<td><%=article.getDurability()%></td>
					<td><%=article.getSuitEquipmentName()%></td>
					<td></td>
					</tr>
				<%
				}
			}
		} %>
	</table>
<%} %>

<!-- 所有物品 -->
<%if(articleName.equals("allArticles")){ %>
	<table>
		<tr class="titlecolor" align="center">
		<%
		Article[] equipments = null;
		if(sortindex.equals("0")){
			if((m+PAGE_ROW_NO)>n){
				equipments = am.getArticleByPage(m,(n-m),0);
			}else{
				equipments = am.getArticleByPage(m,PAGE_ROW_NO,0);
			}
		}else{
			if((m+PAGE_ROW_NO)>n){
				equipments = am.getEquipmentByPage(weaponType,m,(n-m),1);
			}else{
				equipments = am.getEquipmentByPage(weaponType,m,PAGE_ROW_NO,1);
			}
		}
		%>
			<td colspan="18">服务器全部<font color="red">物品</font>列表</td>
		</tr>
		<tr class="titlecolor" align="center">
			<td>物品类</td><td>物品名</td><td>物品等级</td><td>颜色</td>
		</tr>
		<%
		for(int i = 0; equipments != null && i < equipments.length; i++){
			if(equipments[i] != null){
				Article article = equipments[i];
				String bindStyleStr = "";
				if(article.getBindStyle()==0){
					bindStyleStr = "不绑定";
				}else if(article.getBindStyle()==1){
					bindStyleStr = "装备绑定";
				}else if(article.getBindStyle()==2){
					bindStyleStr = "拾取绑定";
				}
				if(i==0){
						%>
					<tr bgcolor="#FFFFFF" align="center">
					<td rowspan="<%= equipments.length%>">物品</td>
					<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
					<td><%=article.getArticleLevel() %></td>
					<td bgcolor="<%=colorss[article.getColorType()] %>"><%=ArticleManager.getColorString(article,article.getColorType()) %></td>
					</tr>
				<%
				}else{
					%>
					<tr bgcolor="#FFFFFF" align="center">
					<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
					<td><%=article.getArticleLevel() %></td>
					<td bgcolor="<%=colorss[article.getColorType()] %>"><%=ArticleManager.getColorString(article,article.getColorType()) %></td>
					</tr>
				<%
				}
			}
		} %>
	</table>
<%} %>

<%} %>

</body>
<script type="text/javascript" src="../js/title.js"></script>
</html>