<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PropsCategory"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Weapon"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.IntProperty"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>物品一览</title>
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
		document.getElementById("articleName").value=document.getElementById("selectname").value;
		document.ArticleByNameForm.submit();
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
String[] colors = new String[]{"白","绿","蓝","紫","橙"};
String[] colorss = new String[]{"white","green","blue","purple","orange"};
String articleName = request.getParameter("articlesName");
int PAGE_ROW_NO = 10000;
byte weaponType = -1;
byte equipmentType = -1;
if(articleName == null){
	articleName ="allArticles";
}
if(articleName != null){
 	Gson gson = new Gson();
	ArticleManager am = ArticleManager.getInstance();
	PropsCategory propsCategory[] = am.getAllPropsCategory();
	List<Article> articlesList = new ArrayList<Article>();
	Article[] articlesArray = null;
	if(articleName.equals("Knifes")){
		articlesArray = am.getAllWeaponKnifes();
//		weaponType = Weapon.WEAPON_TYPE_KNIFE;
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
//		weaponType = Weapon.WEAPON_TYPE_SWORD;
	}else if(articleName.equals("Skicks")){
		articlesArray = am.getAllWeaponSkicks();
//		weaponType = Weapon.WEAPON_TYPE_STICK;
	}else if(articleName.equals("Daggers")){
		articlesArray = am.getAllWeaponDaggers();
//		weaponType = Weapon.WEAPON_TYPE_DAGGER;
	}else if(articleName.equals("Bows")){
		articlesArray = am.getAllWeaponBows();
//		weaponType = Weapon.WEAPON_TYPE_BOW;
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
<form name="ArticleForPageForm" action="ArticlesForAll.jsp" method="get">
<input type="hidden" id="articlesName" name="articlesName">
<input type="hidden" name="sortindex" value="<%=sortindex %>">
</form>
<form name="ArticleForPageForm2" action="ArticlesForAll.jsp" method="get">
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
	out.println("<a href='ArticlesForAll.jsp?articlesName="+articleName+"&pageindex="+0+"&sortindex="+sortindex+"'>首页  </a>");
	if(index > 0){
		out.println("<a href='ArticlesForAll.jsp?articlesName="+articleName+"&pageindex="+(index-1)+"&sortindex="+sortindex+"'><<上一页<<  </a>");	
	}
	for(int i = 0; i < 10; i++){
		if(index+i < pageCount){
		out.println("<a href='ArticlesForAll.jsp?articlesName="+articleName+"&pageindex="+(index+i)+"&sortindex="+sortindex+"'>第"+(index+i+1)+"页  </a>");
		}
	}
	if(index <(pageCount-1)){
		out.println("<a href='ArticlesForAll.jsp?articlesName="+articleName+"&pageindex="+(index+1)+"&sortindex="+sortindex+"'>>>下一页>>  </a>");	
	}
	out.println("<a href='ArticlesForAll.jsp?articlesName="+articleName+"&pageindex="+(pageCount-1)+"&sortindex="+sortindex+"'>末页  </a>");
}%>
<%if(articleName.equals("Knifes")||articleName.equals("Swords")||articleName.equals("Skicks")||articleName.equals("Daggers")||articleName.equals("Bows")){ %>

<table>
<tr class="titlecolor" align="center">
<%
Weapon[] weapons = null;
if(sortindex.equals("0")){
	if((m+PAGE_ROW_NO)>n){
		weapons = am.getWeaponByPage(weaponType,m,(n-m),0);
	}else{
		weapons = am.getWeaponByPage(weaponType,m,PAGE_ROW_NO,0);
	}
}else{
	if((m+PAGE_ROW_NO)>n){
		weapons = am.getWeaponByPage(weaponType,m,(n-m),1);
	}else{
		weapons = am.getWeaponByPage(weaponType,m,PAGE_ROW_NO,1);
	}
}
%>
<td colspan="18">服务器全部<font color="red"><%=(weapons!=null&&weapons[0]!=null)?(Weapon.getWeaponTypeName(weapons[0].getWeaponType())):"" %></font>列表</td>
</tr>
<tr class="titlecolor" align="center">
<td>物品类</td><td>物品名</td><td>物品等级</td><td>需要等级</td><td>颜色</td><td>攻击<td>智力</td><td>耐力</td><td>力量</td><td>敏捷</td><td>命中</td><td>暴击</td><td>闪避</td><td>物防</td><td>法防</td><td>物攻强</td><td>法攻强</td><td>韧性</td>
</tr>
<%
for(int i = 0; weapons != null && i < weapons.length; i++){ 
if(weapons[i] != null){
Weapon article = weapons[i]; 
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
	<td rowspan="<%= weapons.length%>"><%=Equipment.getEquipmentTypeValue(article.getEquipmentType()) %></td>
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><%=article.getArticleLevel() %></td>
	<td><%=article.getPlayerLevelLimit() %></td>
	<td bgcolor="<%=colorss[article.getColorType()] %>"><%=colors[article.getColorType()] %></td>
	<td><%=article.getPhysicalDamageLowerLimit()[0]+"-"+article.getPhysicalDamageUpperLimit()[0] %></td>
	<%
	IntProperty[][] ipss = article.getIps();
	IntProperty[] ips = null;
	if(ipss != null && ipss.length != 0){
		ips = ipss[0];
	}
	if(ips != null){
		%>
		<%
		boolean exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "spellpowerB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "constitutionB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "strengthB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "dexterityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "attackRatingB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "criticalHitB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "dodgeB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "defenceB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "resistanceB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "meleeAttackIntensityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "spellAttackIntensityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "toughness".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
	}else{
		%>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<%
	}
	%>
	</tr>
			
		<%
		}else{
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><%=article.getArticleLevel() %></td>
	<td><%=article.getPlayerLevelLimit() %></td>
	<td bgcolor="<%=colorss[article.getColorType()] %>"><%=colors[article.getColorType()] %></td>
	<td><%=article.getPhysicalDamageLowerLimit()[0]+"-"+article.getPhysicalDamageUpperLimit()[0] %></td>
	<%
	IntProperty[][] ipss = article.getIps();
	IntProperty[] ips = null;
	if(ipss != null && ipss.length != 0){
		ips = ipss[0];
	}
	if(ips != null){
		%>
		<%
		boolean exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "spellpowerB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "constitutionB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "strengthB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "dexterityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "attackRatingB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "criticalHitB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "dodgeB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "defenceB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "resistanceB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "meleeAttackIntensityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "spellAttackIntensityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "toughness".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
	}else{
		%>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<%
	}
	%>
	</tr>
<%
}}} %>
</table>
<%} %>
<%if(articleName.equals("Weapon")){ %>

<table>
<tr class="titlecolor" align="center">
<%
Weapon[] weapons = null;

if(sortindex.equals("0")){
	if((m+PAGE_ROW_NO)>n){
		weapons = am.getWeaponByPage(weaponType,m,(n-m),0);
	}else{
		weapons = am.getWeaponByPage(weaponType,m,PAGE_ROW_NO,0);
	}
}else{
	if((m+PAGE_ROW_NO)>n){
		weapons = am.getWeaponByPage(weaponType,m,(n-m),1);
	}else{
		weapons = am.getWeaponByPage(weaponType,m,PAGE_ROW_NO,1);
	}
}
%>
<td colspan="18">服务器全部<font color="red">武器</font>列表</td>
</tr>
<tr class="titlecolor" align="center">
<td>物品类</td><td>物品名</td><td>物品等级</td><td>需要等级</td><td>颜色</td><td>攻击<td>智力</td><td>耐力</td><td>力量</td><td>敏捷</td><td>命中</td><td>暴击</td><td>闪避</td><td>物防</td><td>法防</td><td>物攻强</td><td>法攻强</td><td>韧性</td>
</tr>
<%
for(int i = 0; weapons != null && i < weapons.length; i++){ 
if(weapons[i] != null){
Weapon article = weapons[i]; 
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
	<td rowspan="<%= weapons.length%>"><%=Equipment.getEquipmentTypeValue(article.getEquipmentType()) %></td>
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><%=article.getArticleLevel() %></td>
	<td><%=article.getPlayerLevelLimit() %></td>
	<td bgcolor="<%=colorss[article.getColorType()] %>"><%=colors[article.getColorType()] %></td>
	<td><%=article.getPhysicalDamageLowerLimit()[0]+"-"+article.getPhysicalDamageUpperLimit()[0] %></td>
	<%
	IntProperty[][] ipss = article.getIps();
	IntProperty[] ips = null;
	if(ipss != null && ipss.length != 0){
		ips = ipss[0];
	}
	if(ips != null){
		%>
		<%
		boolean exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "spellpowerB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "constitutionB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "strengthB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "dexterityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "attackRatingB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "criticalHitB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "dodgeB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "defenceB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "resistanceB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "meleeAttackIntensityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "spellAttackIntensityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "toughness".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
	}else{
		%>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<%
	}
	%>
	</tr>
			
		<%
		}else{
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><%=article.getArticleLevel() %></td>
	<td><%=article.getPlayerLevelLimit() %></td>
	<td bgcolor="<%=colorss[article.getColorType()] %>"><%=colors[article.getColorType()] %></td>
	<td><%=article.getPhysicalDamageLowerLimit()[0]+"-"+article.getPhysicalDamageUpperLimit()[0] %></td>
	<%
	IntProperty[][] ipss = article.getIps();
	IntProperty[] ips = null;
	if(ipss != null && ipss.length != 0){
		ips = ipss[0];
	}
	if(ips != null){
		%>
		<%
		boolean exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "spellpowerB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "constitutionB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "strengthB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "dexterityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "attackRatingB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "criticalHitB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "dodgeB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "defenceB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "resistanceB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "meleeAttackIntensityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "spellAttackIntensityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "toughness".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
	}else{
		%>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<%
	}
	%>
	</tr>
<%
}}} %>
</table>
<%} %>
<%if(articleName.equals("heads")||articleName.equals("shoulders")||articleName.equals("bodys")||articleName.equals("hands")||articleName.equals("foots")||articleName.equals("jewelrys")||articleName.equals("necklace")||articleName.equals("fingerring")){ %>

<table>
<tr class="titlecolor" align="center">
<%
Equipment[] equipments = null;

if(sortindex.equals("0")){
	if((m+PAGE_ROW_NO)>n){
		equipments = am.getEquipmentByPage(equipmentType,m,(n-m),0);
	}else{
		equipments = am.getEquipmentByPage(equipmentType,m,PAGE_ROW_NO,0);
	}
}else{
	if((m+PAGE_ROW_NO)>n){
		equipments = am.getEquipmentByPage(equipmentType,m,(n-m),1);
	}else{
		equipments = am.getEquipmentByPage(equipmentType,m,PAGE_ROW_NO,1);
	}
}
%>
<td colspan="18">服务器全部<font color="red"><%=(equipments!=null&&equipments[0]!=null)?(Equipment.getEquipmentTypeValue(equipments[0].getEquipmentType())):"" %></font>列表</td>
</tr>
<tr class="titlecolor" align="center">
<td>物品类</td><td>物品名</td><td>物品等级</td><td>需要等级</td><td>颜色</td><td>智力</td><td>耐力</td><td>力量</td><td>敏捷</td><td>命中</td><td>暴击</td><td>闪避</td><td>物防</td><td>法防</td><td>物攻强</td><td>法攻强</td><td>韧性</td>
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
	<td rowspan="<%= equipments.length%>"><%=Equipment.getEquipmentTypeValue(article.getEquipmentType()) %></td>
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><%=article.getArticleLevel() %></td>
	<td><%=article.getPlayerLevelLimit() %></td>
	<td bgcolor="<%=colorss[article.getColorType()] %>"><%=colors[article.getColorType()] %></td>
	<%
	IntProperty[][] ipss = article.getIps();
	IntProperty[] ips = null;
	if(ipss != null && ipss.length != 0){
		ips = ipss[0];
	}
	if(ips != null){
		%>
		<%
		boolean exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "spellpowerB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "constitutionB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "strengthB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "dexterityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "attackRatingB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "criticalHitB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "dodgeB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "defenceB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "resistanceB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "meleeAttackIntensityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "spellAttackIntensityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "toughness".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
	}else{
		%>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<%
	}
	%>
	</tr>
			
		<%
		}else{
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><%=article.getArticleLevel() %></td>
	<td><%=article.getPlayerLevelLimit() %></td>
	<td bgcolor="<%=colorss[article.getColorType()] %>"><%=colors[article.getColorType()] %></td>
	<%
	IntProperty[][] ipss = article.getIps();
	IntProperty[] ips = null;
	if(ipss != null && ipss.length != 0){
		ips = ipss[0];
	}
	if(ips != null){
		%>
		<%
		boolean exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "spellpowerB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "constitutionB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "strengthB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "dexterityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "attackRatingB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "criticalHitB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "dodgeB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "defenceB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "resistanceB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "meleeAttackIntensityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "spellAttackIntensityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "toughness".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
	}else{
		%>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<%
	}
	%>
	</tr>
<%
}}} %>
</table>
<%} %>
<%if(articleName.equals("Equipment")){ %>

<table>
<tr class="titlecolor" align="center">
<%
Equipment[] equipments = null;

if(sortindex.equals("0")){
	if((m+PAGE_ROW_NO)>n){
		equipments = am.getEquipmentByPage(equipmentType,m,(n-m),0);
	}else{
		equipments = am.getEquipmentByPage(equipmentType,m,PAGE_ROW_NO,0);
	}
}else{
	if((m+PAGE_ROW_NO)>n){
		equipments = am.getEquipmentByPage(equipmentType,m,(n-m),1);
	}else{
		equipments = am.getEquipmentByPage(equipmentType,m,PAGE_ROW_NO,1);
	}
}
%>
<td colspan="18">服务器全部<font color="red">装备</font>列表</td>
</tr>
<tr class="titlecolor" align="center">
<td>物品类</td><td>物品名</td><td>物品等级</td><td>需要等级</td><td>颜色</td><td>智力</td><td>耐力</td><td>力量</td><td>敏捷</td><td>命中</td><td>暴击</td><td>闪避</td><td>物防</td><td>法防</td><td>物攻强</td><td>法攻强</td><td>韧性</td>
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
	<td rowspan="<%= equipments.length%>"><%=Equipment.getEquipmentTypeValue(article.getEquipmentType()) %></td>
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><%=article.getArticleLevel() %></td>
	<td><%=article.getPlayerLevelLimit() %></td>
	<td bgcolor="<%=colorss[article.getColorType()] %>"><%=colors[article.getColorType()] %></td>
	<%
	IntProperty[][] ipss = article.getIps();
	IntProperty[] ips = null;
	if(ipss != null && ipss.length != 0){
		ips = ipss[0];
	}
	if(ips != null){
		%>
		<%
		boolean exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "spellpowerB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "constitutionB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "strengthB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "dexterityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "attackRatingB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "criticalHitB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "dodgeB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "defenceB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "resistanceB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "meleeAttackIntensityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "spellAttackIntensityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "toughness".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
	}else{
		%>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<%
	}
	%>
	</tr>
			
		<%
		}else{
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><%=article.getArticleLevel() %></td>
	<td><%=article.getPlayerLevelLimit() %></td>
	<td bgcolor="<%=colorss[article.getColorType()] %>"><%=colors[article.getColorType()] %></td>
	<%
	IntProperty[][] ipss = article.getIps();
	IntProperty[] ips = null;
	if(ipss != null && ipss.length != 0){
		ips = ipss[0];
	}
	if(ips != null){
		%>
		<%
		boolean exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "spellpowerB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "constitutionB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "strengthB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "dexterityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "attackRatingB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "criticalHitB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "dodgeB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "defenceB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "resistanceB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "meleeAttackIntensityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "spellAttackIntensityB".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
		exist = false;
		for(IntProperty ip : ips){
			if(ip != null && "toughness".equals(ip.getFieldName())){
				exist = true;
				out.println("<td bgcolor='red'>"+ip.getFieldValue()+"</td>");
				break;
			}
		}
		if(!exist){
			out.println("<td>0</td>");
		}
		%>
		<%
	}else{
		%>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<td>0</td>
		<%
	}
	%>
	</tr>
<%
}}} %>
</table>
<%} %>
<%if(articleName.equals("SingleProps")){ 

%>
<table id="singlePropsList">
<tr class="titlecolor" align="center">
<td colspan="10">服务器<font color="red">简单道具</font>列表</td>
</tr>
<tr class="titlecolor" align="center">
<td>道具分组</td><td>物品名</td><td>物品图片</td><td>道具类型</td><td>物品类</td><td>物品等级</td><td>能否重叠</td><td>卖出价格</td><td>绑定方式</td><td>物品描述</td>
</tr>
<%
Article[] articles = null;

if(sortindex.equals("0")){
	if((m+PAGE_ROW_NO)>n){
		articles = am.getAllSinglePropsByPage(m,(n-m),0);
	}else{
		articles = am.getAllSinglePropsByPage(m,PAGE_ROW_NO,0);
	}
}else{
	if((m+PAGE_ROW_NO)>n){
		articles = am.getAllSinglePropsByPage(m,(n-m),1);
	}else{
		articles = am.getAllSinglePropsByPage(m,PAGE_ROW_NO,1);
	}
}

if(articles != null){
	List<List<SingleProps>> singlePropsList = new ArrayList<List<SingleProps>>();
for(Article a : articles){
	SingleProps article = (SingleProps)a;
	boolean existFlag = false;
	for(List<SingleProps> iTree : singlePropsList){
		if(article != null && iTree != null && iTree.size()>0 && iTree.get(0) != null && article.getCategoryName().equals(iTree.get(0).getCategoryName())){
			existFlag = true;
			iTree.add(article);
			break;
		}
	}
	if(!existFlag && article != null){
		List<SingleProps> iTree = new ArrayList<SingleProps>();
		iTree.add(article);
		singlePropsList.add(iTree);
	}
}
for(List<SingleProps> singleProps : singlePropsList){
	for(int i = 0; i < singleProps.size(); i++){
		SingleProps article = singleProps.get(i);
		if(article != null){
			String bindStyleStr = "";
			if(article.getBindStyle()==0){
				bindStyleStr = "不绑定";
			}else if(article.getBindStyle()==1){
				bindStyleStr = "装备绑定";
			}else if(article.getBindStyle()==2){
				bindStyleStr = "拾取绑定";
			}
			String propsType = "";
			if(article.getPropsType() == 0){
				propsType = "未定义";
			}else if(article.getPropsType()==1){
				propsType = "食品";
			}else if(article.getPropsType()==2){
				propsType = "加血药品";
			}else if(article.getPropsType()==3){
				propsType = "加蓝药品";
			}else if(article.getPropsType()==4){
				propsType = "加血和蓝药品";
			}else if(article.getPropsType()==5){
				propsType = "传送符";
			}else if(article.getPropsType()==6){
				propsType = "坐骑";
			}else if(article.getPropsType()==7){
				propsType = "复活道具";
			}else if(article.getPropsType()==8){
				propsType = "洗点";
			}else if(article.getPropsType()==9){
				propsType = "接任务道具";
			}else if(article.getPropsType()==10){
				propsType = "背包扩展道具";
			}else if(article.getPropsType()==11){
				propsType = "离线经验道具";
			}
		if(i==0){
	%>
		<tr bgcolor="#FFFFFF" align="center">
		<td rowspan="<%=singleProps.size() %>"><a href="propcategorybyname.jsp?categoryName=<%=article.getCategoryName()%>"><%=article.getCategoryName()%></a></td>
		<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
		<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" id="<%=article.getName()%>" onmouseover="javascript:onClickValueChangeForTitle('<%=article.getName() %>');" onmouseout="javascript:divHidden();" onmouseup="javascript:onclickImage('<%=article.getName() %>');" ></td>
		<td><%= propsType %></td>
		<td><%
		out.println("简单道具");
		%></td>
		<td><%= article.getLevelLimit() %></td>
		<td><%= article.isOverlap()?"能":"不能" %></td>
		<td><%=article.getPrice() %></td>
		<td><%= bindStyleStr %></td>
		<td><%= article.getDescription() %></td>
		</tr>
	<%
	}else{
		%>
		<tr bgcolor="#FFFFFF" align="center">
		<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
		<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" id="<%=article.getName()%>" onmouseover="javascript:onClickValueChangeForTitle('<%=article.getName() %>');" onmouseout="javascript:divHidden();" onmouseup="javascript:onclickImage('<%=article.getName() %>');" ></td>
		<td><%= propsType %></td>
		<td><%
		out.println("简单道具");
		%></td>
		<td><%= article.getLevelLimit() %></td>
		<td><%= article.isOverlap()?"能":"不能" %></td>
		<td><%=article.getPrice() %></td>
		<td><%= bindStyleStr %></td>
		<td><%= article.getDescription() %></td>
		</tr>
	<%
	}}}}}
%>
</table>
<%} %>
<%if(articleName.equals("Expendable")){ %>
<table id="ExpendableList">
<tr class="titlecolor" align="center">
<td colspan="10">服务器<font color="red">消耗品</font>列表</td>
</tr>
<tr class="titlecolor" align="center">
<td>道具分组</td><td>物品名</td><td>物品图片</td><td>道具类型</td><td>物品类</td><td>物品等级</td><td>能否重叠</td><td>卖出价格</td><td>绑定方式</td><td>物品描述</td>
</tr>
<%
Article[] articles = null;

if(sortindex.equals("0")){
	if((m+PAGE_ROW_NO)>n){
		articles = am.getExpendableByPage(m,(n-m),0);
	}else{
		articles = am.getExpendableByPage(m,PAGE_ROW_NO,0);
	}
}else{
	if((m+PAGE_ROW_NO)>n){
		articles = am.getExpendableByPage(m,(n-m),1);
	}else{
		articles = am.getExpendableByPage(m,PAGE_ROW_NO,1);
	}
}

if(articles != null){
	List<List<Props>> singlePropsList = new ArrayList<List<Props>>();
for(Article a : articles){
	Props article = (Props)a;
	boolean existFlag = false;
	for(List<Props> iTree : singlePropsList){
		if(article != null && iTree != null && iTree.size()>0 && iTree.get(0) != null && article.getCategoryName().equals(iTree.get(0).getCategoryName())){
			existFlag = true;
			iTree.add(article);
			break;
		}
	}
	if(!existFlag && article != null){
		List<Props> iTree = new ArrayList<Props>();
		iTree.add(article);
		singlePropsList.add(iTree);
	}
}
for(List<Props> singleProps : singlePropsList){
	for(int i = 0; i < singleProps.size(); i++){
		Props article = singleProps.get(i);
		if(article != null){
			String bindStyleStr = "";
			if(article.getBindStyle()==0){
				bindStyleStr = "不绑定";
			}else if(article.getBindStyle()==1){
				bindStyleStr = "装备绑定";
			}else if(article.getBindStyle()==2){
				bindStyleStr = "拾取绑定";
			}
			String propsType = "";
			if(article.getPropsType() == 0){
				propsType = "未定义";
			}else if(article.getPropsType()==1){
				propsType = "食品";
			}else if(article.getPropsType()==2){
				propsType = "加血药品";
			}else if(article.getPropsType()==3){
				propsType = "加蓝药品";
			}else if(article.getPropsType()==4){
				propsType = "加血和蓝药品";
			}else if(article.getPropsType()==5){
				propsType = "传送符";
			}else if(article.getPropsType()==6){
				propsType = "坐骑";
			}else if(article.getPropsType()==7){
				propsType = "复活道具";
			}else if(article.getPropsType()==8){
				propsType = "洗点";
			}else if(article.getPropsType()==9){
				propsType = "接任务道具";
			}else if(article.getPropsType()==10){
				propsType = "背包扩展道具";
			}else if(article.getPropsType()==11){
				propsType = "离线经验道具";
			}
		if(i==0){
	%>
		<tr bgcolor="#FFFFFF" align="center">
		<td rowspan="<%=singleProps.size() %>"><a href="propcategorybyname.jsp?categoryName=<%=article.getCategoryName()%>"><%=article.getCategoryName()%></a></td>
		<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
		<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" id="<%=article.getName()%>" onmouseover="javascript:onClickValueChangeForTitle('<%=article.getName() %>');" onmouseout="javascript:divHidden();" onmouseup="javascript:onclickImage('<%=article.getName() %>');" ></td>
		<td><%= propsType %></td>
		<td><%
	if(article instanceof SingleProps){
		out.println("简单道具");
	}else if(article instanceof LastingProps){
		out.println("持续性道具");
	}
	
	%></td>
		<td><%= article.getLevelLimit() %></td>
		<td><%= article.isOverlap()?"能":"不能" %></td>
		<td><%=article.getPrice() %></td>
		<td><%= bindStyleStr %></td>
		<td><%= article.getDescription() %></td>
		</tr>
	<%
	}else{
		%>
		<tr bgcolor="#FFFFFF" align="center">
		<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
		<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" id="<%=article.getName()%>" onmouseover="javascript:onClickValueChangeForTitle('<%=article.getName() %>');" onmouseout="javascript:divHidden();" onmouseup="javascript:onclickImage('<%=article.getName() %>');" ></td>
		<td><%= propsType %></td>
		<td><%
	if(article instanceof SingleProps){
		out.println("简单道具");
	}else if(article instanceof LastingProps){
		out.println("持续性道具");
	}
	
	%></td>
		<td><%= article.getLevelLimit() %></td>
		<td><%= article.isOverlap()?"能":"不能" %></td>
		<td><%=article.getPrice() %></td>
		<td><%= bindStyleStr %></td>
		<td><%= article.getDescription() %></td>
		</tr>
	<%
	}}}}}
%>
</table>
<%} %>
<%if(articleName.equals("LastingProps")){ %>
<table id="lastingPropsList">
<tr class="titlecolor" align="center">
<td colspan="10">服务器<font color="red">持续性道具</font>列表</td>
</tr>
<tr class="titlecolor" align="center">
<td>道具分组</td><td>物品名</td><td>物品图片</td><td>道具类型</td><td>物品类</td><td>物品等级</td><td>能否重叠</td><td>卖出价格</td><td>绑定方式</td><td>物品描述</td>
</tr>
<%
Article[] articles = null;

if(sortindex.equals("0")){
	if((m+PAGE_ROW_NO)>n){
		articles = am.getAllLastingPropsByPage(m,(n-m),0);
	}else{
		articles = am.getAllLastingPropsByPage(m,PAGE_ROW_NO,0);
	}
}else{
	if((m+PAGE_ROW_NO)>n){
		articles = am.getAllLastingPropsByPage(m,(n-m),1);
	}else{
		articles = am.getAllLastingPropsByPage(m,PAGE_ROW_NO,1);
	}
}

if(articles != null){
	List<List<LastingProps>> propsList = new ArrayList<List<LastingProps>>();
	for(Article a : articles){
		LastingProps article = (LastingProps)a;
		boolean existFlag = false;
		for(List<LastingProps> iTree : propsList){
			if(article != null && iTree != null && iTree.size()>0 && iTree.get(0) != null && article.getCategoryName().equals(iTree.get(0).getCategoryName())){
				existFlag = true;
				iTree.add(article);
				break;
			}
		}
		if(!existFlag && article != null){
			List<LastingProps> iTree = new ArrayList<LastingProps>();
			iTree.add(article);
			propsList.add(iTree);
		}
	}
	
	for(List<LastingProps> props : propsList){
	for(int i = 0; i < props.size(); i++){
		LastingProps article = props.get(i);
	if(article != null){
		String bindStyleStr = "";
		if(article.getBindStyle()==0){
			bindStyleStr = "不绑定";
		}else if(article.getBindStyle()==1){
			bindStyleStr = "装备绑定";
		}else if(article.getBindStyle()==2){
			bindStyleStr = "拾取绑定";
		}
		String propsType = "";
		if(article.getPropsType() == 0){
			propsType = "未定义";
		}else if(article.getPropsType()==1){
			propsType = "食品";
		}else if(article.getPropsType()==2){
			propsType = "加血药品";
		}else if(article.getPropsType()==3){
			propsType = "加蓝药品";
		}else if(article.getPropsType()==4){
			propsType = "加血和蓝药品";
		}else if(article.getPropsType()==5){
			propsType = "传送符";
		}else if(article.getPropsType()==6){
			propsType = "坐骑";
		}else if(article.getPropsType()==7){
			propsType = "复活道具";
		}else if(article.getPropsType()==8){
			propsType = "洗点";
		}else if(article.getPropsType()==9){
			propsType = "接任务道具";
		}else if(article.getPropsType()==10){
			propsType = "背包扩展道具";
		}else if(article.getPropsType()==11){
			propsType = "离线经验道具";
		}
		if(i == 0){
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td rowspan="<%=props.size() %>"><a href="propcategorybyname.jsp?categoryName=<%=article.getCategoryName()%>"><%=article.getCategoryName()%></a></td>
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" id="<%=article.getName()%>" onmouseover="javascript:onClickValueChangeForTitle('<%=article.getName() %>');" onmouseout="javascript:divHidden();" onmouseup="javascript:onclickImage('<%=article.getName() %>');" ></td>
	<td><%= propsType %></td>
	<td><%
		out.println("持续性道具");
	%></td>
	<td><%= article.getLevelLimit() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%=article.getPrice() %></td>
	<td><%= bindStyleStr %></td>
	<td><%= article.getDescription() %></td>
	</tr>
<%}else{
	%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" id="<%=article.getName()%>" onmouseover="javascript:onClickValueChangeForTitle('<%=article.getName() %>');" onmouseout="javascript:divHidden();" onmouseup="javascript:onclickImage('<%=article.getName() %>');" ></td>
	<td><%= propsType %></td>
	<td><%
		out.println("持续性道具");
	%></td>
	<td><%= article.getLevelLimit() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%=article.getPrice() %></td>
	<td><%= bindStyleStr %></td>
	<td><%= article.getDescription() %></td>
	</tr>
	<%
}
}}}}
%>
</table>
<%} %>
<%if(articleName.equals("TransferPaper")){ %>
<table id="transferPaperList">
<tr class="titlecolor" align="center">
<td colspan="10">服务器<font color="red">传送符</font>列表</td>
</tr>
<tr class="titlecolor" align="center">
<td>道具分组</td><td>物品名</td><td>物品图片</td><td>道具类型</td><td>物品类</td><td>物品等级</td><td>能否重叠</td><td>卖出价格</td><td>绑定方式</td><td>物品描述</td>
</tr>
<%
if(articlesArray != null){
	List<List<TransferPaper>> propsList = new ArrayList<List<TransferPaper>>();
	for(Article a : articlesArray){
		TransferPaper article = (TransferPaper)a;
		boolean existFlag = false;
		for(List<TransferPaper> iTree : propsList){
			if(article != null && iTree != null && iTree.size()>0 && iTree.get(0) != null && article.getCategoryName().equals(iTree.get(0).getCategoryName())){
				existFlag = true;
				iTree.add(article);
				break;
			}
		}
		if(!existFlag && article != null){
			List<TransferPaper> iTree = new ArrayList<TransferPaper>();
			iTree.add(article);
			propsList.add(iTree);
		}
	}
	for(List<TransferPaper> props : propsList){
	
		for(int i = 0; i < props.size(); i++){
		TransferPaper article = props.get(i);
	if(article != null){
		String bindStyleStr = "";
		if(article.getBindStyle()==0){
			bindStyleStr = "不绑定";
		}else if(article.getBindStyle()==1){
			bindStyleStr = "装备绑定";
		}else if(article.getBindStyle()==2){
			bindStyleStr = "拾取绑定";
		}
		String propsType = "";
		if(article.getPropsType() == 0){
			propsType = "未定义";
		}else if(article.getPropsType()==1){
			propsType = "食品";
		}else if(article.getPropsType()==2){
			propsType = "加血药品";
		}else if(article.getPropsType()==3){
			propsType = "加蓝药品";
		}else if(article.getPropsType()==4){
			propsType = "加血和蓝药品";
		}else if(article.getPropsType()==5){
			propsType = "传送符";
		}else if(article.getPropsType()==6){
			propsType = "坐骑";
		}else if(article.getPropsType()==7){
			propsType = "复活道具";
		}else if(article.getPropsType()==8){
			propsType = "洗点";
		}else if(article.getPropsType()==9){
			propsType = "接任务道具";
		}else if(article.getPropsType()==10){
			propsType = "背包扩展道具";
		}else if(article.getPropsType()==11){
			propsType = "离线经验道具";
		}
		if(i == 0){
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td rowspan="<%=props.size() %>"><a href="propcategorybyname.jsp?categoryName=<%=article.getCategoryName()%>"><%=article.getCategoryName()%></a></td>
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" id="<%=article.getName()%>" onmouseover="javascript:onClickValueChangeForTitle('<%=article.getName() %>');" onmouseout="javascript:divHidden();" onmouseup="javascript:onclickImage('<%=article.getName() %>');" ></td>
	<td><%= propsType %></td>
	<td><%
		out.println("回城符");
	%></td>
	<td><%= article.getLevelLimit() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%=article.getPrice() %></td>
	<td><%= bindStyleStr %></td>
	<td><%= article.getDescription() %></td>
	</tr>
<%}else{
	%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" id="<%=article.getName()%>" onmouseover="javascript:onClickValueChangeForTitle('<%=article.getName() %>');" onmouseout="javascript:divHidden();" onmouseup="javascript:onclickImage('<%=article.getName() %>');" ></td>
	<td><%= propsType %></td>
	<td><%
		out.println("回城符");
	%></td>
	<td><%= article.getLevelLimit() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%=article.getPrice() %></td>
	<td><%= bindStyleStr %></td>
	<td><%= article.getDescription() %></td>
	</tr>
	<%
}
}}}}
%>
</table>
<%} %>
<%if(articleName.equals("MountProps")){ %>
<table id="MountPropsList">
<tr class="titlecolor" align="center">
<td colspan="10">服务器<font color="red">坐骑</font>列表</td>
</tr>
<tr class="titlecolor" align="center">
<td>道具分组</td><td>物品名</td><td>物品图片</td><td>道具类型</td><td>物品类</td><td>物品等级</td><td>能否重叠</td><td>卖出价格</td><td>绑定方式</td><td>物品描述</td>
</tr>
<%
if(articlesArray != null){
	List<List<MountProps>> propsList = new ArrayList<List<MountProps>>();
	for(Article a : articlesArray){
		MountProps article = (MountProps)a;
		boolean existFlag = false;
		for(List<MountProps> iTree : propsList){
			if(article != null && iTree != null && iTree.size()>0 && iTree.get(0) != null && article.getCategoryName().equals(iTree.get(0).getCategoryName())){
				existFlag = true;
				iTree.add(article);
				break;
			}
		}
		if(!existFlag && article != null){
			List<MountProps> iTree = new ArrayList<MountProps>();
			iTree.add(article);
			propsList.add(iTree);
		}
	}
	for(List<MountProps> props : propsList){
	
		for(int i = 0; i < props.size(); i++){
			MountProps article = props.get(i);
	if(article != null){
		String bindStyleStr = "";
		if(article.getBindStyle()==0){
			bindStyleStr = "不绑定";
		}else if(article.getBindStyle()==1){
			bindStyleStr = "装备绑定";
		}else if(article.getBindStyle()==2){
			bindStyleStr = "拾取绑定";
		}
		String propsType = "";
		if(article.getPropsType() == 0){
			propsType = "未定义";
		}else if(article.getPropsType()==1){
			propsType = "食品";
		}else if(article.getPropsType()==2){
			propsType = "加血药品";
		}else if(article.getPropsType()==3){
			propsType = "加蓝药品";
		}else if(article.getPropsType()==4){
			propsType = "加血和蓝药品";
		}else if(article.getPropsType()==5){
			propsType = "传送符";
		}else if(article.getPropsType()==6){
			propsType = "坐骑";
		}else if(article.getPropsType()==7){
			propsType = "复活道具";
		}else if(article.getPropsType()==8){
			propsType = "洗点";
		}else if(article.getPropsType()==9){
			propsType = "接任务道具";
		}else if(article.getPropsType()==10){
			propsType = "背包扩展道具";
		}else if(article.getPropsType()==11){
			propsType = "离线经验道具";
		}
		if(i == 0){
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td rowspan="<%=props.size() %>"><a href="propcategorybyname.jsp?categoryName=<%=article.getCategoryName()%>"><%=article.getCategoryName()%></a></td>
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" id="<%=article.getName()%>" onmouseover="javascript:onClickValueChangeForTitle('<%=article.getName() %>');" onmouseout="javascript:divHidden();" onmouseup="javascript:onclickImage('<%=article.getName() %>');" ></td>
	<td><%= propsType %></td>
	<td><%
		out.println("坐骑");
	%></td>
	<td><%= article.getLevelLimit() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%=article.getPrice() %></td>
	<td><%= bindStyleStr %></td>
	<td><%= article.getDescription() %></td>
	</tr>
<%}else{
	%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" id="<%=article.getName()%>" onmouseover="javascript:onClickValueChangeForTitle('<%=article.getName() %>');" onmouseout="javascript:divHidden();" onmouseup="javascript:onclickImage('<%=article.getName() %>');" ></td>
	<td><%= propsType %></td>
	<td><%
		out.println("坐骑");
	%></td>
	<td><%= article.getLevelLimit() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%=article.getPrice() %></td>
	<td><%= bindStyleStr %></td>
	<td><%= article.getDescription() %></td>
	</tr>
	<%
}
}}}}
%>
</table>
<%} %>
<%if(articleName.equals("TaskProps")){ %>
<table id="TaskPropsList">
<tr class="titlecolor" align="center">
<td colspan="10">服务器<font color="red">触发任务</font>的道具列表</td>
</tr>
<tr class="titlecolor" align="center">
<td>道具分组</td><td>物品名</td><td>物品图片</td><td>道具类型</td><td>物品类</td><td>物品等级</td><td>能否重叠</td><td>卖出价格</td><td>绑定方式</td><td>物品描述</td>
</tr>
<%
Article[] articles = null;

if(sortindex.equals("0")){
	if((m+PAGE_ROW_NO)>n){
		articles = am.getAllTaskPropsByPage(m,(n-m),0);
	}else{
		articles = am.getAllTaskPropsByPage(m,PAGE_ROW_NO,0);
	}
}else{
	if((m+PAGE_ROW_NO)>n){
		articles = am.getAllTaskPropsByPage(m,(n-m),1);
	}else{
		articles = am.getAllTaskPropsByPage(m,PAGE_ROW_NO,1);
	}
}

if(articles != null){
	List<List<TaskProps>> propsList = new ArrayList<List<TaskProps>>();
	for(Article a : articles){
		TaskProps article = (TaskProps)a;
		boolean existFlag = false;
		for(List<TaskProps> iTree : propsList){
			if(article != null && iTree != null && iTree.size()>0 && iTree.get(0) != null && article.getCategoryName().equals(iTree.get(0).getCategoryName())){
				existFlag = true;
				iTree.add(article);
				break;
			}
		}
		if(!existFlag && article != null){
			List<TaskProps> iTree = new ArrayList<TaskProps>();
			iTree.add(article);
			propsList.add(iTree);
		}
	}
	for(List<TaskProps> props : propsList){
	
		for(int i = 0; i < props.size(); i++){
			TaskProps article = props.get(i);
	if(article != null){
		String bindStyleStr = "";
		if(article.getBindStyle()==0){
			bindStyleStr = "不绑定";
		}else if(article.getBindStyle()==1){
			bindStyleStr = "装备绑定";
		}else if(article.getBindStyle()==2){
			bindStyleStr = "拾取绑定";
		}
		String propsType = "";
		if(article.getPropsType() == 0){
			propsType = "未定义";
		}else if(article.getPropsType()==1){
			propsType = "食品";
		}else if(article.getPropsType()==2){
			propsType = "加血药品";
		}else if(article.getPropsType()==3){
			propsType = "加蓝药品";
		}else if(article.getPropsType()==4){
			propsType = "加血和蓝药品";
		}else if(article.getPropsType()==5){
			propsType = "传送符";
		}else if(article.getPropsType()==6){
			propsType = "坐骑";
		}else if(article.getPropsType()==7){
			propsType = "复活道具";
		}else if(article.getPropsType()==8){
			propsType = "洗点";
		}else if(article.getPropsType()==9){
			propsType = "接任务道具";
		}else if(article.getPropsType()==10){
			propsType = "背包扩展道具";
		}else if(article.getPropsType()==11){
			propsType = "离线经验道具";
		}
		if(i == 0){
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td rowspan="<%=props.size() %>"><a href="propcategorybyname.jsp?categoryName=<%=article.getCategoryName()%>"><%=article.getCategoryName()%></a></td>
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" id="<%=article.getName()%>" onmouseover="javascript:onClickValueChangeForTitle('<%=article.getName() %>');" onmouseout="javascript:divHidden();" onmouseup="javascript:onclickImage('<%=article.getName() %>');" ></td>
	<td><%= propsType %></td>
	<td><%
		out.println("携带任务道具");
	%></td>
	<td><%= article.getLevelLimit() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%=article.getPrice() %></td>
	<td><%= bindStyleStr %></td>
	<td><%= article.getDescription() %></td>
	</tr>
<%}else{
	%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" id="<%=article.getName()%>" onmouseover="javascript:onClickValueChangeForTitle('<%=article.getName() %>');" onmouseout="javascript:divHidden();" onmouseup="javascript:onclickImage('<%=article.getName() %>');" ></td>
	<td><%= propsType %></td>
	<td><%
		out.println("携带任务道具");
	%></td>
	<td><%= article.getLevelLimit() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%=article.getPrice() %></td>
	<td><%= bindStyleStr %></td>
	<td><%= article.getDescription() %></td>
	</tr>
	<%
}
}}}}
%>
</table>
<%} %>
<%if(articleName.equals("ClearSkillPointsProps")){ %>
<table id="ClearSkillPointsPropsList">
<tr class="titlecolor" align="center">
<td colspan="10">服务器<font color="red">洗点</font>道具列表</td>
</tr>
<tr class="titlecolor" align="center">
<td>道具分组</td><td>物品名</td><td>物品图片</td><td>道具类型</td><td>物品类</td><td>物品等级</td><td>能否重叠</td><td>卖出价格</td><td>绑定方式</td><td>物品描述</td>
</tr>
<%
if(articlesArray != null){
	List<List<ClearSkillPointsProps>> propsList = new ArrayList<List<ClearSkillPointsProps>>();
	for(Article a : articlesArray){
		ClearSkillPointsProps article = (ClearSkillPointsProps)a;
		boolean existFlag = false;
		for(List<ClearSkillPointsProps> iTree : propsList){
			if(article != null && iTree != null && iTree.size()>0 && iTree.get(0) != null && article.getCategoryName().equals(iTree.get(0).getCategoryName())){
				existFlag = true;
				iTree.add(article);
				break;
			}
		}
		if(!existFlag && article != null){
			List<ClearSkillPointsProps> iTree = new ArrayList<ClearSkillPointsProps>();
			iTree.add(article);
			propsList.add(iTree);
		}
	}
	for(List<ClearSkillPointsProps> props : propsList){
	
		for(int i = 0; i < props.size(); i++){
			ClearSkillPointsProps article = props.get(i);
	if(article != null){
		String bindStyleStr = "";
		if(article.getBindStyle()==0){
			bindStyleStr = "不绑定";
		}else if(article.getBindStyle()==1){
			bindStyleStr = "装备绑定";
		}else if(article.getBindStyle()==2){
			bindStyleStr = "拾取绑定";
		}
		String propsType = "";
		if(article.getPropsType() == 0){
			propsType = "未定义";
		}else if(article.getPropsType()==1){
			propsType = "食品";
		}else if(article.getPropsType()==2){
			propsType = "加血药品";
		}else if(article.getPropsType()==3){
			propsType = "加蓝药品";
		}else if(article.getPropsType()==4){
			propsType = "加血和蓝药品";
		}else if(article.getPropsType()==5){
			propsType = "传送符";
		}else if(article.getPropsType()==6){
			propsType = "坐骑";
		}else if(article.getPropsType()==7){
			propsType = "复活道具";
		}else if(article.getPropsType()==8){
			propsType = "洗点";
		}else if(article.getPropsType()==9){
			propsType = "接任务道具";
		}else if(article.getPropsType()==10){
			propsType = "背包扩展道具";
		}else if(article.getPropsType()==11){
			propsType = "离线经验道具";
		}
		if(i == 0){
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td rowspan="<%=props.size() %>"><a href="propcategorybyname.jsp?categoryName=<%=article.getCategoryName()%>"><%=article.getCategoryName()%></a></td>
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" id="<%=article.getName()%>" onmouseover="javascript:onClickValueChangeForTitle('<%=article.getName() %>');" onmouseout="javascript:divHidden();" onmouseup="javascript:onclickImage('<%=article.getName() %>');" ></td>
	<td><%= propsType %></td>
	<td><%
		out.println("洗点类道具");
	%></td>
	<td><%= article.getLevelLimit() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%=article.getPrice() %></td>
	<td><%= bindStyleStr %></td>
	<td><%= article.getDescription() %></td>
	</tr>
<%}else{
	%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" id="<%=article.getName()%>" onmouseover="javascript:onClickValueChangeForTitle('<%=article.getName() %>');" onmouseout="javascript:divHidden();" onmouseup="javascript:onclickImage('<%=article.getName() %>');" ></td>
	<td><%= propsType %></td>
	<td><%
		out.println("洗点类道具");
	%></td>
	<td><%= article.getLevelLimit() %></td>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%=article.getPrice() %></td>
	<td><%= bindStyleStr %></td>
	<td><%= article.getDescription() %></td>
	</tr>
	<%
}
}}}}
%>
</table>
<%} %>
<%if(articleName.equals("Stone")){ %>
<table id="StoneList">
<tr class="titlecolor" align="center">
<td colspan="18">服务器<font color="red">宝石</font>列表</td>
</tr>
<tr class="titlecolor" align="center">
<td>物品名</td><td>物品图片</td><td>物品类</td><td>物品颜色</td><td>能否重叠</td><td>卖出价格</td><td>绑定方式</td><td>物品描述</td>
</tr>
<%
Article[] articles = null;

if(sortindex.equals("0")){
	if((m+PAGE_ROW_NO)>n){
		articles = am.getStoneByPage(m,(n-m),0);
	}else{
		articles = am.getStoneByPage(m,PAGE_ROW_NO,0);
	}
}else{
	if((m+PAGE_ROW_NO)>n){
		articles = am.getStoneByPage(m,(n-m),1);
	}else{
		articles = am.getStoneByPage(m,PAGE_ROW_NO,1);
	}
}

if(articles != null){
	for(Article a : articles){
		Article article = (Article)a;
	if(article != null){
		String bindStyleStr = "";
		if(article.getBindStyle()==0){
			bindStyleStr = "不绑定";
		}else if(article.getBindStyle()==1){
			bindStyleStr = "装备绑定";
		}else if(article.getBindStyle()==2){
			bindStyleStr = "拾取绑定";
		}
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" id="<%=article.getName()%>" onmouseover="javascript:onClickValueChangeForTitle('<%=article.getName() %>');" onmouseout="javascript:divHidden();" onmouseup="javascript:onclickImage('<%=article.getName() %>');" ></td>
	<td>
	<%
	if(a instanceof InlayArticle){
		out.println("镶嵌类宝石");
	}else if(a instanceof UpgradeArticle){
		out.println("升级类物品");
	}else if(a instanceof AiguilleArticle){
		out.println("打孔类物品");
	}
	%>
	</td>
	<%
	if(a instanceof InlayArticle){
		out.println("<td bgcolor='"+colorss[((InlayArticle)a).getColorType()]+"'>"+colors[((InlayArticle)a).getColorType()]+"</td>");
	}else if(a instanceof UpgradeArticle){
		out.println("<td bgcolor='"+colorss[((UpgradeArticle)a).getColorType()]+"'>"+colors[((UpgradeArticle)a).getColorType()]+"</td>");
	}else if(a instanceof AiguilleArticle){
		out.println("<td bgcolor='"+colorss[((AiguilleArticle)a).getColorType()]+"'>"+colors[((AiguilleArticle)a).getColorType()]+"</td>");
	}
	%>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%=article.getPrice() %></td>
	<td><%= bindStyleStr %></td>
	<td><%= article.getDescription() %></td>
	</tr>
<%
}}}
%>
</table>
<%} %>
<%if(articleName.equals("InlayArticle")){ %>
<table id="inlayArticleList">
<tr class="titlecolor" align="center">
<td colspan="18">服务器<font color="red">镶嵌物品</font>列表</td>
</tr>
<tr class="titlecolor" align="center">
<td>物品名</td><td>物品图片</td><td>物品类</td><td>物品颜色</td><td>能否重叠</td><td>卖出价格</td><td>绑定方式</td><td>物品描述</td>
</tr>
<%
Article[] articles = null;

if(sortindex.equals("0")){
	if((m+PAGE_ROW_NO)>n){
		articles = am.getAllInlayArticlesByPage(m,(n-m),0);
	}else{
		articles = am.getAllInlayArticlesByPage(m,PAGE_ROW_NO,0);
	}
}else{
	if((m+PAGE_ROW_NO)>n){
		articles = am.getAllInlayArticlesByPage(m,(n-m),1);
	}else{
		articles = am.getAllInlayArticlesByPage(m,PAGE_ROW_NO,1);
	}
}

if(articles != null){
	for(Article a : articles){
		InlayArticle article = (InlayArticle)a;
	if(article != null){
		String bindStyleStr = "";
		if(article.getBindStyle()==0){
			bindStyleStr = "不绑定";
		}else if(article.getBindStyle()==1){
			bindStyleStr = "装备绑定";
		}else if(article.getBindStyle()==2){
			bindStyleStr = "拾取绑定";
		}
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" id="<%=article.getName()%>" onmouseover="javascript:onClickValueChangeForTitle('<%=article.getName() %>');" onmouseout="javascript:divHidden();" onmouseup="javascript:onclickImage('<%=article.getName() %>');" ></td>
	<td>
	<%
	if(a instanceof InlayArticle){
		out.println("镶嵌类宝石");
	}else if(a instanceof UpgradeArticle){
		out.println("升级类物品");
	}else if(a instanceof AiguilleArticle){
		out.println("打孔类物品");
	}
	%>
	</td>
	<%
	if(a instanceof InlayArticle){
		out.println("<td bgcolor='"+colorss[((InlayArticle)a).getColorType()]+"'>"+colors[((InlayArticle)a).getColorType()]+"</td>");
	}else if(a instanceof UpgradeArticle){
		out.println("<td bgcolor='"+colorss[((UpgradeArticle)a).getColorType()]+"'>"+colors[((UpgradeArticle)a).getColorType()]+"</td>");
	}else if(a instanceof AiguilleArticle){
		out.println("<td bgcolor='"+colorss[((AiguilleArticle)a).getColorType()]+"'>"+colors[((AiguilleArticle)a).getColorType()]+"</td>");
	}
	%>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%=article.getPrice() %></td>
	<td><%= bindStyleStr %></td>
	<td><%= article.getDescription() %></td>
	</tr>
<%
}}}
%>
</table>
<%} %>
<%if(articleName.equals("UpgradeArticle")){ %>
<table id="upgradeArticleList">
<tr class="titlecolor" align="center">
<td colspan="18">服务器<font color="red">升级物品</font>列表</td>
</tr>
<tr class="titlecolor" align="center">
<td>物品名</td><td>物品图片</td><td>物品类</td><td>物品颜色</td><td>能否重叠</td><td>卖出价格</td><td>绑定方式</td><td>物品描述</td>
</tr>
<%
Article[] articles = null;

if(sortindex.equals("0")){
	if((m+PAGE_ROW_NO)>n){
		articles = am.getAllUpgradeArticlesByPage(m,(n-m),0);
	}else{
		articles = am.getAllUpgradeArticlesByPage(m,PAGE_ROW_NO,0);
	}
}else{
	if((m+PAGE_ROW_NO)>n){
		articles = am.getAllUpgradeArticlesByPage(m,(n-m),1);
	}else{
		articles = am.getAllUpgradeArticlesByPage(m,PAGE_ROW_NO,1);
	}
}

if(articles != null){
	for(Article a : articles){
		UpgradeArticle article = (UpgradeArticle)a;
	if(article != null){
		String bindStyleStr = "";
		if(article.getBindStyle()==0){
			bindStyleStr = "不绑定";
		}else if(article.getBindStyle()==1){
			bindStyleStr = "装备绑定";
		}else if(article.getBindStyle()==2){
			bindStyleStr = "拾取绑定";
		}
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" id="<%=article.getName()%>" onmouseover="javascript:onClickValueChangeForTitle('<%=article.getName() %>');" onmouseout="javascript:divHidden();" onmouseup="javascript:onclickImage('<%=article.getName() %>');" ></td>
	<td>
	<%
	if(a instanceof InlayArticle){
		out.println("镶嵌类宝石");
	}else if(a instanceof UpgradeArticle){
		out.println("升级类物品");
	}else if(a instanceof AiguilleArticle){
		out.println("打孔类物品");
	}
	%>
	</td>
	<%
	if(a instanceof InlayArticle){
		out.println("<td bgcolor='"+colorss[((InlayArticle)a).getColorType()]+"'>"+colors[((InlayArticle)a).getColorType()]+"</td>");
	}else if(a instanceof UpgradeArticle){
		out.println("<td bgcolor='"+colorss[((UpgradeArticle)a).getColorType()]+"'>"+colors[((UpgradeArticle)a).getColorType()]+"</td>");
	}else if(a instanceof AiguilleArticle){
		out.println("<td bgcolor='"+colorss[((AiguilleArticle)a).getColorType()]+"'>"+colors[((AiguilleArticle)a).getColorType()]+"</td>");
	}
	%>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%=article.getPrice() %></td>
	<td><%= bindStyleStr %></td>
	<td><%= article.getDescription() %></td>
	</tr>
<%
}}}
%>
</table>
<%} %>
<%if(articleName.equals("AiguilleArticle")){ %>
<table id="aiguilleArticleList">
<tr class="titlecolor" align="center">
<td colspan="18">服务器<font color="red">打孔物品</font>列表</td>
</tr>
<tr class="titlecolor" align="center">
<td>物品名</td><td>物品图片</td><td>物品类</td><td>物品颜色</td><td>能否重叠</td><td>卖出价格</td><td>绑定方式</td><td>物品描述</td>
</tr>
<%
Article[] articles = null;

if(sortindex.equals("0")){
	if((m+PAGE_ROW_NO)>n){
		articles = am.getAllAiguilleArticlesByPage(m,(n-m),0);
	}else{
		articles = am.getAllAiguilleArticlesByPage(m,PAGE_ROW_NO,0);
	}
}else{
	if((m+PAGE_ROW_NO)>n){
		articles = am.getAllAiguilleArticlesByPage(m,(n-m),1);
	}else{
		articles = am.getAllAiguilleArticlesByPage(m,PAGE_ROW_NO,1);
	}
}

if(articles != null){
	for(Article a : articles){
		AiguilleArticle article = (AiguilleArticle)a;
	if(article != null){
		String bindStyleStr = "";
		if(article.getBindStyle()==0){
			bindStyleStr = "不绑定";
		}else if(article.getBindStyle()==1){
			bindStyleStr = "装备绑定";
		}else if(article.getBindStyle()==2){
			bindStyleStr = "拾取绑定";
		}
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" id="<%=article.getName()%>" onmouseover="javascript:onClickValueChangeForTitle('<%=article.getName() %>');" onmouseout="javascript:divHidden();" onmouseup="javascript:onclickImage('<%=article.getName() %>');" ></td>
	<td>
	<%
	if(a instanceof InlayArticle){
		out.println("镶嵌类宝石");
	}else if(a instanceof UpgradeArticle){
		out.println("升级类物品");
	}else if(a instanceof AiguilleArticle){
		out.println("打孔类物品");
	}
	%>
	</td>
	<%
	if(a instanceof InlayArticle){
		out.println("<td bgcolor='"+colorss[((InlayArticle)a).getColorType()]+"'>"+colors[((InlayArticle)a).getColorType()]+"</td>");
	}else if(a instanceof UpgradeArticle){
		out.println("<td bgcolor='"+colorss[((UpgradeArticle)a).getColorType()]+"'>"+colors[((UpgradeArticle)a).getColorType()]+"</td>");
	}else if(a instanceof AiguilleArticle){
		out.println("<td bgcolor='"+colorss[((AiguilleArticle)a).getColorType()]+"'>"+colors[((AiguilleArticle)a).getColorType()]+"</td>");
	}
	%>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%=article.getPrice() %></td>
	<td><%= bindStyleStr %></td>
	<td><%= article.getDescription() %></td>
	</tr>
<%
}}}
%>
</table>
<%} %>
<%if(articleName.equals("OtherArticles")){ %>

<table id="articleList">
<tr class="titlecolor" align="center">
<td colspan="9">服务器<font color="red">其他物品</font>列表</td>
</tr>
<tr class="titlecolor" align="center">
<td>物品名</td><td>物品图片</td><td>物品等级</td><td>物品类</td><td>物品颜色</td><td>能否重叠</td><td>卖出价格</td><td>绑定方式</td><td>物品描述</td>
</tr>
<%
Article[] articles = null;

if(sortindex.equals("0")){
	if((m+PAGE_ROW_NO)>n){
		articles = am.getOtherArticlesByPage(m,(n-m),0);
	}else{
		articles = am.getOtherArticlesByPage(m,PAGE_ROW_NO,0);
	}
}else{
	if((m+PAGE_ROW_NO)>n){
		articles = am.getOtherArticlesByPage(m,(n-m),1);
	}else{
		articles = am.getOtherArticlesByPage(m,PAGE_ROW_NO,1);
	}
}

if(articles != null){
	for(Article a : articles){
		Article article = (Article)a;
	if(article != null){
		String bindStyleStr = "";
		if(article.getBindStyle()==0){
			bindStyleStr = "不绑定";
		}else if(article.getBindStyle()==1){
			bindStyleStr = "装备绑定";
		}else if(article.getBindStyle()==2){
			bindStyleStr = "拾取绑定";
		}
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" id="<%=article.getName()%>" onmouseover="javascript:onClickValueChangeForTitle('<%=article.getName() %>');" onmouseout="javascript:divHidden();" onmouseup="javascript:onclickImage('<%=article.getName() %>');" ></td>
	<td><%
	if(a instanceof Equipment){
		out.println(((Equipment)a).getPlayerLevelLimit());
	}else if(a instanceof Props){
		out.println(((Props)a).getLevelLimit());
	}else{
		out.println("--");
	}
	
	
	%></td>
		<td><%
	if(a instanceof Weapon){
		out.println(Weapon.getWeaponTypeName(((Weapon)a).getWeaponType()));
	}else if(a instanceof Equipment){
		out.println(Equipment.getEquipmentTypeValue(((Equipment)a).getEquipmentType()));
	}else if(a instanceof TransferPaper){
		out.println("回城符");
	}else if(a instanceof MountProps){
		out.println("坐骑");
	}else if(a instanceof TaskProps){
		out.println("携带任务道具");
	}else if(a instanceof SingleProps){
		out.println("简单道具");
	}else if(a instanceof LastingProps){
		out.println("持续性道具");
	}else if(a instanceof InlayArticle){
		out.println("镶嵌类宝石");
	}else if(a instanceof UpgradeArticle){
		out.println("升级类物品");
	}else if(a instanceof AiguilleArticle){
		out.println("打孔类物品");
	}else if(a instanceof ClearSkillPointsProps){
		out.println("洗点类道具");
	}else{
		out.println("其他");
	}
	
	
	%></td>
<%
	if(a instanceof Equipment){
		out.println("<td bgcolor='"+colorss[((Equipment)a).getColorType()]+"'>"+colors[((Equipment)a).getColorType()]+"</td>");
	}else if(a instanceof InlayArticle){
		out.println("<td bgcolor='"+colorss[((InlayArticle)a).getColorType()]+"'>"+colors[((InlayArticle)a).getColorType()]+"</td>");
	}else if(a instanceof UpgradeArticle){
		out.println("<td bgcolor='"+colorss[((UpgradeArticle)a).getColorType()]+"'>"+colors[((UpgradeArticle)a).getColorType()]+"</td>");
	}else if(a instanceof AiguilleArticle){
		out.println("<td bgcolor='"+colorss[((AiguilleArticle)a).getColorType()]+"'>"+colors[((AiguilleArticle)a).getColorType()]+"</td>");
	}else{
		out.println("<td>--</td>");
	}
	%>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%=article.getPrice() %></td>
	<td><%= bindStyleStr %></td>
	<td><%= article.getDescription() %></td>
	</tr>
<%
}}}
%>
</table>
<%} %>
<%if(articleName.equals("allArticles")){ %>

<table id="articleList">
<tr class="titlecolor" align="center">
<%
Article[] articles = null;
if(sortindex.equals("0")){
	if((m+PAGE_ROW_NO)>n){
		articles = am.getArticleByPage(m,(n-m),0);
	}else{
		articles = am.getArticleByPage(m,PAGE_ROW_NO,0);
	}
}else{
	if((m+PAGE_ROW_NO)>n){
		articles = am.getArticleByPage(m,(n-m),1);
	}else{
		articles = am.getArticleByPage(m,PAGE_ROW_NO,1);
	}
}

%>
<td colspan="9">服务器全部<font color="red">物品</font>列表</td>
</tr>
<tr class="titlecolor" align="center">
<td>物品名</td><td>物品图片</td><td>物品等级</td><td>物品类</td><td>物品颜色</td><td>能否重叠</td><td>卖出价格</td><td>绑定方式</td><td>物品描述</td>
</tr>
<%
if(articles != null){
	for(Article a : articles){
		Article article = (Article)a;
	if(article != null){
		String bindStyleStr = "";
		if(article.getBindStyle()==0){
			bindStyleStr = "不绑定";
		}else if(article.getBindStyle()==1){
			bindStyleStr = "装备绑定";
		}else if(article.getBindStyle()==2){
			bindStyleStr = "拾取绑定";
		}
%>
	<tr bgcolor="#FFFFFF" align="center">
	<td><a href="javascript:onclickLink('<%=article.getName() %>');"><%= article.getName()%></a></td>
	<td><img src="/game_server/imageServlet?id=<%= article.getIconId()%>" id="<%=article.getName()%>" onmouseover="javascript:onClickValueChangeForTitle('<%=article.getName() %>');" onmouseout="javascript:divHidden();" onmouseup="javascript:onclickImage('<%=article.getName() %>');" ></td>
	<td><%
	if(a instanceof Equipment){
		out.println(((Equipment)a).getPlayerLevelLimit());
	}else if(a instanceof Props){
		out.println(((Props)a).getLevelLimit());
	}else{
		out.println("--");
	}
	
	
	%></td>
		<td><%
	if(a instanceof Weapon){
		out.println(Weapon.getWeaponTypeName(((Weapon)a).getWeaponType()));
	}else if(a instanceof Equipment){
		out.println(Equipment.getEquipmentTypeValue(((Equipment)a).getEquipmentType()));
	}else if(a instanceof TransferPaper){
		out.println("回城符");
	}else if(a instanceof MountProps){
		out.println("坐骑");
	}else if(a instanceof TaskProps){
		out.println("携带任务道具");
	}else if(a instanceof SingleProps){
		out.println("简单道具");
	}else if(a instanceof LastingProps){
		out.println("持续性道具");
	}else if(a instanceof InlayArticle){
		out.println("镶嵌类宝石");
	}else if(a instanceof UpgradeArticle){
		out.println("升级类物品");
	}else if(a instanceof AiguilleArticle){
		out.println("打孔类物品");
	}else if(a instanceof ClearSkillPointsProps){
		out.println("洗点类道具");
	}else{
		out.println("其他");
	}
	
	
	%></td>
<%
	if(a instanceof Equipment){
		out.println("<td bgcolor='"+colorss[((Equipment)a).getColorType()]+"'>"+colors[((Equipment)a).getColorType()]+"</td>");
	}else if(a instanceof InlayArticle){
		out.println("<td bgcolor='"+colorss[((InlayArticle)a).getColorType()]+"'>"+colors[((InlayArticle)a).getColorType()]+"</td>");
	}else if(a instanceof UpgradeArticle){
		out.println("<td bgcolor='"+colorss[((UpgradeArticle)a).getColorType()]+"'>"+colors[((UpgradeArticle)a).getColorType()]+"</td>");
	}else if(a instanceof AiguilleArticle){
		out.println("<td bgcolor='"+colorss[((AiguilleArticle)a).getColorType()]+"'>"+colors[((AiguilleArticle)a).getColorType()]+"</td>");
	}else{
		out.println("<td>--</td>");
	}
	%>
	<td><%= article.isOverlap()?"能":"不能" %></td>
	<td><%=article.getPrice() %></td>
	<td><%= bindStyleStr %></td>
	<td><%= article.getDescription() %></td>
	</tr>
<%
}}}
%>
</table>
<%} %>

<%} %>

</body>
<script type="text/javascript" src="../js/title.js"></script>
</html>
