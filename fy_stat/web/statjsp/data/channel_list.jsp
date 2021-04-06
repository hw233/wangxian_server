<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="../inc/header.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>...</title>
<link rel="stylesheet" href="../css/style.css"/>
<script language="JavaScript">
<!--


function overTag(tag){
	tag.style.color = "red";	
	tag.style.backgroundColor = "gray";
}
			
function outTag(tag){
	tag.style.color = "black";
	tag.style.backgroundColor = "white";
}

function checksub() {
	if(window.confirm("确定要提交数据加工的系数配置么？")) {
		document.f1.submit();
	}
}
-->
</script>
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
	<h1>渠道数据加工配置</h1>
	<%
	ChannelManager camanager = ChannelManager.getInstance();
	ChannelItemManager cimanager = ChannelItemManager.getInstance();
	List<Channel> channels = camanager.getChannels();
	String subed = request.getParameter("subed");
	String message = null;
	if(subed != null) {
		int changed = 0;
		for(Channel channel : channels) {
			List<ChannelItem> items = cimanager.getChannelItemsByChannel(channel);
			for(ChannelItem item : items) {
				String s = "prate-"  + item.getKey();
				String prate = request.getParameter(s);
				String ss = "cmode-"  + item.getKey();
				String cmode = request.getParameter(ss);
				boolean cc = false;
				if(prate != null) {
					try {
						float rate = Float.parseFloat(prate);
						float old = item.getPrate();
						if(rate != old) {
							item.setPrate(rate);
							cc = true;
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				if(cmode != null) {
					try {
						long mode = Long.parseLong(cmode);
						long old = item.getCmode();
						if(mode != old) {
							item.setCmode(mode);
							cc = true;
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				if(cc) {
					cimanager.updateChannelItem(item);
					changed++;
				}
			}
		}
		message = "更新配置成功，共更新" + changed + "个渠道的配置!";
	}
	if(message != null) {
	%> 
	<h2><font color="red"><%=message %></font></h2>
	<%} %>
	<form action="" method=post name=f1>
	<table id="test1" align="center" width="80%" cellpadding="0" cellspacing="0" border="0" class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
	 <tr>
	  <th align=center width=20%><b>渠道Key</b></th>
	  <th align=center width=20%><b>渠道名</b></th>
	  <th align=center width=20%><b>子渠道Key</b></th>
	  <th align=center width=20%><b>子渠道名</b></th>
	  <th align=center width=20%><b>分成模式</b></th>
	  <th align=center width=20%><b>统计系数</b></th>
	 </tr>
	
	 <%for(Channel channel:channels) {	
	 	List<ChannelItem> items = cimanager.getChannelItemsByChannel(channel);
			for(ChannelItem item : items) {
	 %>
	 <tr onmouseover="overTag(this);" onmouseout="outTag(this);";>
	  <td align="left">
	  	<%=channel.getKey() %>
	  </td>
	  <td align="left">
	  	<%=channel.getName() %>
	  </td>
	  <td align="left">
	  	<%=item.getKey() %>
	  </td>
	  <td align="left">
	  	<%=item.getName() %>
	  </td>
	  <td align="left">
	  	<input type=radio name=cmode-<%=item.getKey() %> value=0 <%if(item.getCmode().intValue() == 0) out.print("checked");%>>注册
	  	<input type=radio name=cmode-<%=item.getKey() %> value=1 <%if(item.getCmode().intValue() == 1) out.print("checked");%>>充值
	  </td>
	  <td align="left">
	  	<input type=text size=10 name="prate-<%=item.getKey() %>" value="<%=NumberUtils.round(item.getPrate(),2) %>">
	  </td>
	 </tr>
	 <%}} %>
    </table>
    <input type="hidden" name="subed" value="true" />
    <input type="button" onclick="checksub()" name="sub1" value=" 提交数据配置 " />
	</form>
</body>
</html>
