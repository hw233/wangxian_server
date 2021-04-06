<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<title>物品一览</title>
<script type="text/javascript">

function ajaxFunction()
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
    document.getElementById("showTable").innerHTML = xmlHttp.ResponseText;
   }
 }
 var str = document.getElementById("articlesName").value;
xmlHttp.open("GET","ArticlesForPage.jsp?articlesName="+str,true);
xmlHttp.send(null);

 
 }
function onClickValueChange(value){
	document.getElementById("articlesName").value=value;
	//ajaxFunction();
	document.ArticleForPageForm.submit();
}
function onclickImage(str){
	document.getElementById("articleName").value=str;
	document.ArticleByNameForm.submit();
}
</script>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
</head>
<body onload="javascript:onClickValueChange('Knifes');">
<form name="ArticleByNameForm" action="ArticleByName.jsp" method="post">
<input type="hidden" id="articleName" name="articleName">
</form>
<form name="ArticleForPageForm" action="ArticlesForPage.jsp" method="post">
<input type="hidden" id="articlesName" name="articlesName">
</form>
<a href="ArticlesView.jsp">刷新此页面</a>
<br>
<div id=pltsTipLayer style="display: none;position: absolute; z-index:10001"></div>
<table>
<tr>
<td align="left" width="38px">
<a href="javascript:onClickValueChange('Knifes');" >刀</a>
</td>
<td align="left" width="38px">
<a href="javascript:onClickValueChange('Swords');" >剑</a>
</td>
<td align="left" width="38px">
<a href="javascript:onClickValueChange('Skicks');" >棍</a>
</td>
<td align="left" width="38px">
<a href="javascript:onClickValueChange('Daggers');" >匕首</a>
</td>
<td align="left" width="38px">
<a href="javascript:onClickValueChange('Bows');" >弓</a>
</td>
<td align="left" width="36px">
<a href="javascript:onClickValueChange('heads');" >头盔</a>
</td>
<td align="left" width="36px">
<a href="javascript:onClickValueChange('shoulders');" >护肩</a>
</td>
<td align="left" width="36px">
<a href="javascript:onClickValueChange('bodys');" >衣服</a>
</td>
<td align="left" width="36px">
<a href="javascript:onClickValueChange('hands');" >护手</a>
</td>
<td align="left" width="36px">
<a href="javascript:onClickValueChange('foots');" >靴子</a>
</td>
</tr>
<tr>
<td align="left" width="36px">
<a href="javascript:onClickValueChange('jewelrys');" >首饰</a>
</td>
<td align="left" width="72px">
<a href="javascript:onClickValueChange('SingleProps');" >简单道具</a>
</td>
<td align="left" width="90px">
<a href="javascript:onClickValueChange('LastingProps');" >持续性道具</a>
</td>
<td align="left" width="100px">
<a href="javascript:onClickValueChange('TransferPaper');" >回城符类道具</a>
</td>
<td align="left" width="100px">
<a href="javascript:onClickValueChange('MountProps');" >坐骑类道具</a>
</td>
<td align="left" width="90px">
<a href="javascript:onClickValueChange('InlayArticle');" >镶嵌类宝石</a>
</td>
<td align="left" width="90px">
<a href="javascript:onClickValueChange('UpgradeArticle');" >升级类物品</a>
</td>
<td align="left" width="90px">
<a href="javascript:onClickValueChange('AiguilleArticle');" >打孔类物品</a>
</td>
<td align="left" width="36px">
<a href="javascript:onClickValueChange('OtherArticles');" >其他</a>
</td>
<td align="left" width="36px">
<a href="javascript:onClickValueChange('allArticles');" >全部</a>
</td>
</tr> 
</table>

<br>
<input type="hidden" id="articlesName">
<div id="showTable"></div>
</body>
<script type="text/javascript" src="../js/title.js"></script>
</html>
