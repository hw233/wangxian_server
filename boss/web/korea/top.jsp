<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导航栏</title>
<script src="SpryAssets/SpryTabbedPanels.js" type="text/javascript"></script>
<link href="SpryAssets/SpryTabbedPanels.css" rel="stylesheet" type="text/css" />
<style type="text/css">
body {
	background-image: url(images/beijing.gif);
}
</style>
<%@ include file="translateResouce.jsp"%>
</head>

<body>
<%
		String uname = (String)session.getAttribute("authorize.username");
  		if(uname!=null){
%>
<div id="TabbedPanels1" class="TabbedPanels">
  <ul class="TabbedPanelsTabGroup">
  
    <li class="TabbedPanelsTab" tabindex="0"><%=账号信息查询%></li>
    <li class="TabbedPanelsTab" tabindex="0"><%=充值信息查询 %></li>
    <li class="TabbedPanelsTab" tabindex="0"><%=发送公告 %></li>
     <li class="TabbedPanelsTab" tabindex="0">移动镖车</li>
    <div id='krose'><input type='button' value='언어전환' onclick='koreatranslate("<%=istran%>")'></input></div>
  </ul>
  <div class="TabbedPanelsContentGroup">
    <div class="TabbedPanelsContent">
    <table width="600">
        <td align="center" valign="middle">
            <a href="userquery.jsp" target="mainFrame"><input type="button" name="button" id="button" value="<%=账号查询 %>" /></a>
   </tr>
      </table>
    </div>
    <div class="TabbedPanelsContent">
    <td align="center" valign="middle">
        <table width="600">
              <tr>
                <td align="center" valign="middle"><a href="../gm/manager/orderlist.jsp" target="mainFrame">
                  <input type="button" name="button2" id="button2" value="<%=订单查询 %>" />
                </a></td>
              </tr>
            </table>
    <td>
    </div>
    <div class="TabbedPanelsContent">
          <table width="600">
        <td>
            <a href="../gm/manager/gmActivityNotice.jsp" target="mainFrame">
  <input type="button" name="button" id="button" value="<%=活动公告 %>" /></a>
  <td>
            <a href="../gm/manager/gmSystemNotice.jsp" target="mainFrame">
  <input type="button" name="button" id="button" value="<%=系统公告 %>" /></a>
  <td>
            <a href="../gm/manager/gmLoginNotice.jsp" target="mainFrame">
  <input type="button" name="button" id="button" value="<%=登陆公告 %>" /></a>
  <td>
            <a href="../systemTimeNotice.jsp" target="mainFrame">
  <input type="button" name="button" id="button" value="<%=定时公告 %>" /></a>
  <td>
        </tr>
      </table>
    </div>
     <div class="TabbedPanelsContent">
          <table width="600">
        <td>
            <a href="biaocar.jsp" target="mainFrame">
  <input type="button" name="button" id="button" value="移动镖车" /></a>
	</td>
        </tr>
      </table>
    </div>
  </div>
</div>
<%
	}else{
		out.print("<h1>请重新登录！</h1>");
	}
%>
<script type="text/javascript">
var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1");
function koreatranslate(index){
	if(index=="nocommit"){
		window.location.replace("top.jsp?istran=korea");
	}else if(index=="korea"){
		window.location.replace("top.jsp?istran=nocommit");
	}
	
}
</script>
</body>
</html>
