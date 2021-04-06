<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
											com.fy.engineserver.auction.*,
											com.fy.engineserver.auction.service.*,
											com.fy.engineserver.auction.dao.*,
											com.fy.engineserver.sprite.*,
											com.fy.engineserver.datasource.props.*,
											com.xuanzhi.tools.text.*"%>
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="../../css/common.css"/>
<link rel="stylesheet" href="../../css/style.css"/>
<style type="text/css">
#tablenoborder{
border:0px;
}
#tablenoborder td{
border:0px;
}
</style>
<%
AuctionManager amanager = AuctionManager.getInstance();
PlayerManager pmanager = PlayerManager.getInstance(); 
ArticleEntityManager aem = ArticleEntityManager.getInstance();
String[] parentStrs = aem.物品一级分类;
String[][] childrenStrs = aem.物品二级分类;
String atypeStr = request.getParameter("atypeStr");
int atype = ParamUtils.getIntParameter(request, "atype", -1);
if(atype != -1){
	atypeStr = ""+atype;
}
String action = request.getParameter("action");
String pageindex = request.getParameter("pageindex");
String aname = ParamUtils.getParameter(request,"aname", null);
String asubtype = request.getParameter("asubtype");
String acolor = request.getParameter("acolor");
String minlevel = request.getParameter("minlevel");
String maxlevel = request.getParameter("maxlevel");
String sort = request.getParameter("sort");
int[] levelrange = new int[0];
int asubtypevalue = -1;
int acolorvalue = -1;
int sortvalue = -1;
if(asubtype != null){
	asubtypevalue = Integer.parseInt(asubtype);
}
if(pageindex == null){
	pageindex = "0";
}
int index = Integer.parseInt(pageindex);
if(minlevel != null && maxlevel != null){
	try{
	int miLevel = Integer.parseInt(minlevel);
	int maLevel = Integer.parseInt(maxlevel);
	levelrange = new int[]{miLevel,maLevel};
	}catch(Exception e){
		
	}
}
if(acolor != null){
	acolorvalue = Integer.parseInt(acolor);
}
if(sort != null){
	sortvalue = Integer.parseInt(sort);
}
List<Auction> alist = null;
if(action != null){
alist = amanager.searchAuctions(atype, asubtypevalue, levelrange, acolorvalue, aname != null? aname : "", Integer.parseInt(pageindex)*100, 100, sortvalue);
}
String[] children = null;
if(atypeStr != null && !atypeStr.equals("-1")){
	children = childrenStrs[Integer.parseInt(atypeStr)];
}
%>
<script language="JavaScript">
<!--
function subcheck(index) {
	document.getElementById("pageindex").value = index;
	document.f1.submit();
}

function bidding(aid) {
	f2.elements[0].value = aid;
	f2.submit();
}

function buy(aid) {
	f3.elements[0].value = aid;
	f3.submit();
}
-->
</script>
<script language="JavaScript">
function onchangeselect(obj){
	document.getElementById("atypeStr").value = obj.value;
	form1.submit();
	
}
</script>
</head>
<body>
<form name="form1" id="form1">
<input type="hidden" id="atypeStr" name="atypeStr">
</form>
<h2>正在拍卖的物品(每页显示100条)</h2>
<input type=hidden name=childrenStrs id=childrenStrs value=<%=childrenStrs %>>
<form name="f1" id="f1">
<input type="hidden" name="action" value="search">
<input type="hidden" name="pageindex" id="pageindex" value=<%=pageindex %>>
<table id="tablenoborder">
<tr><td>
按大类型检索:
<select name=atype onchange="javascript:onchangeselect(this)">
	<option value="-1">请选择</option>
	<%
	for(int i = 0; parentStrs != null && i < parentStrs.length; i++){
		if(atypeStr != null && Integer.parseInt(atypeStr) == i){
			out.println("<option value='"+i+"' selected>"+parentStrs[i]+"</option>");
		}else{
			out.println("<option value='"+i+"'>"+parentStrs[i]+"</option>");
		}
	}
	%>
</select>
</td><td>
按小类型检索:
<select name=asubtype>
	<option value="-1">请选择</option>
	<%
	for(int i = 0; children != null && i < children.length; i++){
		if(asubtypevalue == i){
			out.println("<option value='"+i+"' selected>"+children[i]+"</option>");
		}else{
			out.println("<option value='"+i+"'>"+children[i]+"</option>");
		}
	}
	%>
</select>
</td><td>
按品质检索:
<select name=acolor>
	<option value="-1">请选择</option>
	<option value="0" <%=acolorvalue == 0 ? "selected":"" %>>白</option>
	<option value="1" <%=acolorvalue == 1 ? "selected":"" %>>绿</option>
	<option value="2" <%=acolorvalue == 2 ? "selected":"" %>>蓝</option>
	<option value="3" <%=acolorvalue == 3 ? "selected":"" %>>紫</option>
	<option value="4" <%=acolorvalue == 4 ? "selected":"" %>>橙</option>
</select>
</td></tr><tr>  
<td>
最小等级:<input type=text size=10 name=minlevel id="minlevel" <%=minlevel != null ? "value='"+minlevel+"'":"" %>>
</td><td>
  最大等级:<input type=text size=10 name=maxlevel id="maxlevel" <%=maxlevel != null ? "value='"+maxlevel+"'":"" %>>
  </td><td>
按名称检索:
<input type=text size=10 name=aname <%=aname != null ? "value='"+aname+"'":"" %>>
</td></tr><tr><td>    
排序:
<select name=sort>
	<option value="0" <%=sortvalue == 0 ? "selected":"" %>>按等级升序</option>
	<option value="1" <%=sortvalue == 1 ? "selected":"" %>>按等级降序</option>
	<option value="2" <%=sortvalue == 2 ? "selected":"" %>>按出价升序</option>
	<option value="3" <%=sortvalue == 3 ? "selected":"" %>>按出价降序</option>
	<option value="4" <%=sortvalue == 4 ? "selected":"" %>>按结束时间升序</option>
	<option value="5" <%=sortvalue == 5 ? "selected":"" %>>按结束时间降序</option>
	<option value="6" <%=sortvalue == 6 ? "selected":"" %>>按一口价升序</option>
	<option value="7" <%=sortvalue == 7 ? "selected":"" %>>按一口价降序</option>
	<option value="8" <%=sortvalue == 8 ? "selected":"" %>>按元宝升序</option>
	<option value="9" <%=sortvalue == 9 ? "selected":"" %>>按元宝降序</option>
	<option value="10" <%=sortvalue == 10 ? "selected":"" %>>按单价升序</option>
	<option value="11" <%=sortvalue == 11 ? "selected":"" %>>按单价降序</option>
</select></td><td colspan="2">
<input type=submit name=sub1 value=" 提 交 "></td></tr>
</table>
</form>
<form name=f2 action="bidding_auction.jsp">
	<input type=hidden name=aid id=aid>
</form>
<form name=f3 action="buy_auction.jsp">
	<input type=hidden name=aid id=aid>
</form>
<a href="javascript:subcheck(0);">首页</a>
<%if(index != 0){
	%>
	<a href="javascript:subcheck(<%=index-1 %>);">前一页</a>
	<%
} %>
<a href="javascript:subcheck(<%=index+1 %>);">后一页</a>
<table id="test1" align="center" width="96%" cellpadding="0" cellspacing="0" border="0" class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
 <tr>
  <th align=center width=16%><b>物品<b></th>
  <th align=center width=6%><b>等级<b></th>
  <th align=center width=10%><b>剩余时间<b></th>
  <th align=center width=10%><b>出售人<b></th>
  <th align=center width=8%><b>初始价<b></th>
  <th align=center width=8%><b>一口价<b></th>
  <th align=center width=10%><b>当前出价<b></th>
  <th align=center width=10%><b>当前出价人<b></th>
 </tr>
 
 <%
 if(alist != null){
 for(Auction auc : alist) {
 java.util.Date endDate = auc.getEndDate();
 System.out.println(endDate);
 java.util.Date nowDate = new java.util.Date();
 long timeleft = endDate.getTime() - nowDate.getTime();
 long minleft = timeleft/(1000*60);
 long day = minleft/(60*24);
 minleft = minleft % (60*24);
 long hour = minleft/60;
 minleft = minleft % 60;
 String timedesp = day+"天"+hour+"小时"+minleft+"分";
 if(day == 0) {
 	timedesp = hour+"小时"+minleft+"分";
 }
 if(day == 0 && hour == 0) {
 	timedesp = minleft+"分";
 }
 Player Oseller = pmanager.getPlayer(new Long(auc.getSellerId()).intValue());
 Player Opricer = null;
 if(auc.getMaxPricePlayer() > 0)	
 	Opricer = pmanager.getPlayer(new Long(auc.getMaxPricePlayer()).intValue());
 %>
 <tr>
  <td align="left">
  	<%=auc.getName() %>
  </td>
  <td align="center">
  	<%=auc.getLevel() %>
  </td>
  <td align="center">
  	<%=timedesp %>
  </td>
  <td align="center">
  	<%=Oseller.getName() %>
  </td>
  <td align="center">
  	<%=auc.getStartPrice() %>
  </td>
  <td align="center">
  	<%=auc.getBuyPrice() %>
  </td>
  <td align="center">
  	<%=auc.getNowPrice()==0?"无人出价":auc.getNowPrice() %>
  </td>
  <td align="center">
  	<%=Opricer==null?"无人出价":Opricer.getName() %>
  </td>
 </tr>
 <%}
 } %>
   </table>
<a href="javascript:subcheck(0);">首页</a>
<%if(index != 0){
	%>
	<a href="javascript:subcheck(<%=index-1 %>);">前一页</a>
	<%
} %>
<a href="javascript:subcheck(<%=index+1 %>);">后一页</a>
</body>
</html>
