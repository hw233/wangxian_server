<%@page import="com.fy.engineserver.achievement.*"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	AchievementManager aManager = AchievementManager.getInstance();
	List<Achievement> achList = aManager.getAchievementList();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>所有成就列表</title>
<style type="text/css">
<!--
body {
	background: #FFF;
	font: normal normal 12px Verdana, Geneva, Arial, Helvetica, sans-serif;
	margin: 10px;
	padding: 0
}

table,td,a {
	font: normal normal 12px Verdana, Geneva, Arial, Helvetica, sans-serif
}

.td {
	nowrap: 'true';
}

div.tableContainer {
	clear: both;
	border: 1px solid #963;
	height: auto;
	overflow: auto;
	width: 100%;
}

/* WinIE 6.x needs to re-account for it's scrollbar. Give it some padding */
\html div.tableContainer /* */ {
	padding: 0 16px 0 0
}

/* clean up for allowing display Opera 5.x/6.x and MacIE 5.x */
html>body div.tableContainer {
	height: auto;
	padding: 0;
	width: 740px
}

/* Reset overflow value to hidden for all non-IE browsers. */
/* Filter out Opera 5.x/6.x and MacIE 5.x */
head:first-child+body div[class].tableContainer {
	height: auto;
	overflow: hidden;
	width: 756px
}

/* define width of table. IE browsers only */
/* if width is set to 100%, you can remove the width */
/* property from div.tableContainer and have the div scale */
div.tableContainer table {
	float: left;
	width: 100%
}

/* WinIE 6.x needs to re-account for padding. Give it a negative margin */
\html div.tableContainer table /* */ {
	margin: 0 -16px 0 0
}

/* define width of table. Opera 5.x/6.x and MacIE 5.x */
html>body div.tableContainer table {
	float: none;
	margin: 0;
	width: 740px
}

/* define width of table. Add 16px to width for scrollbar. */
/* All other non-IE browsers. Filter out Opera 5.x/6.x and MacIE 5.x */
head:first-child+body div[class].tableContainer table {
	width: 756px
}

/* set table header to a fixed position. WinIE 6.x only */
/* In WinIE 6.x, any element with a position property set to relative and is a child of */
/* an element that has an overflow property set, the relative value translates into fixed. */
/* Ex: parent element DIV with a class of tableContainer has an overflow property set to auto */
thead.fixedHeader tr {
	position: relative;
	/* expression is for WinIE 5.x only. Remove to validate and for pure CSS solution */
	top: expression(document.getElementById (         "tableContainer") .
		    
		   scrollTop )
}

/* set THEAD element to have block level attributes. All other non-IE browsers */
/* this enables overflow to work on TBODY element. All other non-IE, non-Mozilla browsers */
/* Filter out Opera 5.x/6.x and MacIE 5.x */
head:first-child+body thead[class].fixedHeader tr {
	display: block
}

/* make the TH elements pretty */
thead.fixedHeader th {
	background: #C96;
	border-left: 1px solid #EB8;
	border-right: 1px solid #B74;
	border-top: 1px solid #EB8;
	font-weight: normal;
	padding: 4px 3px;
	text-align: center
}

/* make the A elements pretty. makes for nice clickable headers */
thead.fixedHeader a,thead.fixedHeader a:link,thead.fixedHeader a:visited
	{
	display: block;
	text-decoration: none;
	width: 100%
}

/* make the A elements pretty. makes for nice clickable headers */
/* WARNING: swapping the background on hover may cause problems in WinIE 6.x */
thead.fixedHeader a:hover {
	display: block;
	text-decoration: underline;
	width: 100%
}

/* define the table content to be scrollable */
/* set TBODY element to have block level attributes. All other non-IE browsers */
/* this enables overflow to work on TBODY element. All other non-IE, non-Mozilla browsers */
/* induced side effect is that child TDs no longer accept width: auto */
/* Filter out Opera 5.x/6.x and MacIE 5.x */
head:first-child+body tbody[class].scrollContent {
	display: block;
	height: 500px;
	overflow: auto;
	width: 100%
}

/* make TD elements pretty. Provide alternating classes for striping the table */
/* http://www.alistapart.com/articles/zebratables/ */
tbody.scrollContent td,tbody.scrollContent tr.normalRow td {
	background: #FFF;
	border-bottom: 1px solid #EEE;
	border-left: 1px solid #EEE;
	border-right: 1px solid #AAA;
	border-top: 1px solid #AAA;
	padding: 2px 3px
}

tbody.scrollContent tr.alternateRow td {
	background: #EEE;
	border-bottom: 1px solid #EEE;
	border-left: 1px solid #EEE;
	border-right: 1px solid #AAA;
	border-top: 1px solid #AAA;
	padding: 2px 3px
}

/* define width of TH elements: 1st, 2nd, and 3rd respectively. */
/* All other non-IE browsers. Filter out Opera 5.x/6.x and MacIE 5.x */
/* Add 16px to last TH for scrollbar padding */
/* http://www.w3.org/TR/REC-CSS2/selector.html#adjacent-selectors */
head:first-child+body thead[class].fixedHeader th {
	width: 230px
}

head:first-child+body thead[class].fixedHeader th+th {
	width: 250px
}

head:first-child+body thead[class].fixedHeader th+th+th {
	border-right: none;
	padding: 4px 4px 4px 3px;
	width: 316px
}

/* define width of TH elements: 1st, 2nd, and 3rd respectively. */
/* All other non-IE browsers. Filter out Opera 5.x/6.x and MacIE 5.x */
/* Add 16px to last TH for scrollbar padding */
/* http://www.w3.org/TR/REC-CSS2/selector.html#adjacent-selectors */
head:first-child+body tbody[class].scrollContent td {
	width: 200px
}

head:first-child+body tbody[class].scrollContent td+td {
	width: 250px
}

head:first-child+body tbody[class].scrollContent td+td+td {
	border-right: none;
	padding: 2px 4px 2px 3px;
	width: 300px
		/* expression is for WinIE 5.x only. Remove to validate and for pure CSS solution */
		
		
		
		
		 
     top :        
		  expression(document.getElementById (         "tableContainer") .    
		    scrollTop )
}
-->
</style>
</head>
<body>
	<div id="tableContainer" class="tableContainer" style="float: left;">
		<table border="0" cellpadding="0" cellspacing="0" width="100%"
			height="100%" style="font-size: 14px;">
			<thead class="fixedHeader">
				<tr bgcolor="#FFFF00">
					<th>ID</th>
					<th>名字</th>
					<th>描述</th>
					<th>icon</th>
					<th>记录类型</th>
					<th>达成数量</th>
					<th>奖励成就点</th>
					<th>奖励称号</th>
					<th>奖励物品</th>
				</tr>
			</thead>
			<tbody class="scrollContent">
				<%
					int index = 0;
					if (achList != null) {
						for (Achievement achievement : achList) {
				%>
				<tr bgcolor="<%=(index++ % 2 == 1 ? "#79F7C5" : "")%>">
					<td><a name="<%=achievement.getId()%>"><%=achievement.getId()%></a>
					</td>
					<td><%=achievement.getName()%></td>
					<td><%=achievement.getDes()%></td>
					<td><%=achievement.getIcon()%></td>
					<td><%=achievement.getAction()%></td>
					<td><%=achievement.getNum()%></td>
					<td><%=achievement.getPrizeAchievementNum()%></td>
					<td><%=achievement.getPrizeTitle()%></td>
					<td><%=achievement.getPrizeArticle()%></td>
				</tr>
				<%
					}
					}
				%>
			</tbody>
		</table>
	</div>
</body>
</html>