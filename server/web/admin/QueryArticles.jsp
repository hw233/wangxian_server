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
<link rel="stylesheet" type="text/css" href="../css/common.css" />
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
<body>
<%
String[] colors = new String[]{"白","绿","蓝","紫","橙","白","绿","蓝","紫","橙"};
String[] colorss = new String[]{"white","green","blue","purple","orange","white","green","blue","purple","orange"};
String articleName = request.getParameter("articleNames");
out.println("您要查询"+articleName+"，下面是查询到结果");
if(articleName != null){
	Article[] as = ArticleManager.getInstance().getAllArticles();
	if(as != null){
		ArrayList<Article> al = new ArrayList<Article>();
		for(Article article : as){
			if(article != null && (articleName.indexOf(article.getName())>=0 || article.getName().indexOf(articleName) >=0 )){
				al.add(article);

			}
		}
		%>
		<table>
		<tr><td colspan="10">查询到的物品</td></tr>
		<tr>
		<td>物品名</td><td>物品等级</td>
		</tr>
		<%
		if(!al.isEmpty()){
			for(Article article : al){
				if(article != null){
					%>
					<tr>
					<td><a href="ArticleByName.jsp?articleName=<%=article.getName() %>"><%=article.getName() %></a></td><td><%=article.getArticleLevel() %></td>
					</tr>
					<%
				}
			}
		}
		%>
		</table>
		<%
	}
} %>

</body>
<script type="text/javascript" src="../js/title.js"></script>
</html>