<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.warehouse.service.*,com.fy.engineserver.warehouse.*,com.fy.engineserver.datasource.props.*,com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.mail.*,
com.fy.engineserver.mail.service.*,com.xuanzhi.tools.text.*,com.fy.engineserver.closebetatest.*" %>
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>潜龙GM邮箱</title>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
td{
text-align:left;
}
.titlecolor{
background-color:#C2CAF5;
}
#showdiv{
background-color:#DCE0EB;
text-align:left;
margin:0px 0px;
}
</style>
<script type="text/javascript">
function onClickAddArticle(index){
	var obj;
	var playerId = document.getElementById("playerId_"+index).value;
	var zhanghao = document.getElementById("zhanghao_"+index).checked;
	var textarea = document.getElementById("textarea_"+index).value;
	var fujian1 = document.getElementById("fujian1_"+index).value;
	var fujian2 = document.getElementById("fujian2_"+index).value;
	var fujian3 = document.getElementById("fujian3_"+index).value;
	var fujian4 = document.getElementById("fujian4_"+index).value;
	var fujian5 = document.getElementById("fujian5_"+index).value;
	var fujian6 = document.getElementById("fujian6_"+index).value;
	document.getElementById("playerId").value = playerId;
	document.getElementById("zhanghao").value = zhanghao;
	document.getElementById("textarea").value = textarea;
	document.getElementById("fujian1").value = fujian1;
	document.getElementById("fujian2").value = fujian2;
	document.getElementById("fujian3").value = fujian3;
	document.getElementById("fujian4").value = fujian4;
	document.getElementById("fujian5").value = fujian5;
	document.getElementById("fujian6").value = fujian6;
	document.form1.submit();
}
</script>
</head>
<body>
<% 
FCAccountInfoManager fcm = FCAccountInfoManager.getInstance();
GMSendPresent gm = GMSendPresent.getInstance();
String stype = null;
String message = null; 
List<Mail> mails = null;
int PAGE_ROW_NO = 60;
String pageindex = request.getParameter("pageindex");
String playerId = request.getParameter("playerId");
String zhanghao = request.getParameter("zhanghao");
String textarea = request.getParameter("textarea");
String fujian1 = request.getParameter("fujian1");
String fujian2 = request.getParameter("fujian2");
String fujian3 = request.getParameter("fujian3");
String fujian4 = request.getParameter("fujian4");
String fujian5 = request.getParameter("fujian5");
String fujian6 = request.getParameter("fujian6");
String[] mailId = request.getParameterValues("mailId");
stype = request.getParameter("stype");
if(stype == null){
	stype = "sub1";
}
if(zhanghao != null && playerId != null){
	if(zhanghao.equals("true")){
		try{
			int id = Integer.parseInt(playerId);
			gm.sendAccount(id);
		}catch(Exception e){
			out.println("请输入正确Id e:"+StringUtil.getStackTrace(e));
		}
	}
}
String comment = null;
ArrayList<String> list = new ArrayList<String>();
if(textarea != null && !textarea.trim().equals("")){
	comment = textarea.trim();
}
if(fujian1 != null && !fujian1.trim().equals("")){
	list.add(fujian1.trim());
}
if(fujian2 != null && !fujian2.trim().equals("")){
	list.add(fujian2.trim());
}
if(fujian3 != null && !fujian3.trim().equals("")){
	list.add(fujian3.trim());
}
if(fujian4 != null && !fujian4.trim().equals("")){
	list.add(fujian4.trim());
}
if(fujian5 != null && !fujian5.trim().equals("")){
	list.add(fujian5.trim());
}
if(fujian6 != null && !fujian6.trim().equals("")){
	list.add(fujian6.trim());
}
if(playerId != null){
	try{
		int id = Integer.parseInt(playerId);
		gm.sendMailPresent(id,comment,list.toArray(new String[0]));
	}catch(Exception e){
		out.println("请输入正确Id e:"+StringUtil.getStackTrace(e));
	}
}
int m = 0;
int index = 0;
if(pageindex == null){
	pageindex = "0";
}
m = Integer.parseInt(pageindex)*PAGE_ROW_NO;
index = Integer.parseInt(pageindex);
PlayerManager pmanager = PlayerManager.getInstance();
String playerName = "潜龙GM";
%>
<form name="form1" action="" method="post">
<input type="hidden" name="playerId" id="playerId">
<input type="hidden" name="pageindex" id="pageindex" value="<%=pageindex %>">
<input type="hidden" name="zhanghao" id="zhanghao">
<input type="hidden" name="textarea" id="textarea">
<input type="hidden" name="fujian1" id="fujian1">
<input type="hidden" name="fujian2" id="fujian2">
<input type="hidden" name="fujian3" id="fujian3">
<input type="hidden" name="fujian4" id="fujian4">
<input type="hidden" name="fujian5" id="fujian5">
<input type="hidden" name="fujian6" id="fujian6">
<input type="hidden" name=stype value="sub1">
</form>
<b>潜龙GM邮箱</b><br><br>
<!-- <form action="" name=f1>
	GM:<input type=text name=playerName size=20 value="<%=playerName %>"><br>
	<input type=hidden name=stype value="sub1"><br>
	<input type=submit name=sub1 value=" 提 交 ">
</form> -->
<%
if(stype != null) {
	FCAccountInfo[] fcs = null;
	if(fcm != null){
		fcs = fcm.getFCArray();
	}

	Player p = pmanager.getPlayer(playerName);
	MailManager mmanager = MailManager.getInstance();
	if(mailId != null && stype.equals("delete")){
		for(String mailIdStr : mailId){
			if(mailIdStr != null){
				mmanager.deleteMail(Long.parseLong(mailIdStr));
			}
		}
	}
	if(message != null) { 
		out.println(message); 
	} 
	
	int n = mmanager.getCount(p.getId());
	if(n > 0){
		//每页60条数据
		int pageCount = 0;
		pageCount = n/PAGE_ROW_NO;
		if(n%PAGE_ROW_NO != 0){
			pageCount+=1;
		}
		out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>当前页为第<font color='red'>"+(index+1)+"</font>页&nbsp;&nbsp;总共<font color='#2D20CA'>"+pageCount+"</font>页&nbsp;&nbsp;"+n+"条数据&nbsp;&nbsp;每页"+PAGE_ROW_NO+"条数据</b><br><br>");
		out.println("<a href='gmmailstoplayer.jsp?stype=sub1&playerName="+playerName+"&pageindex="+0+"'>首页  </a>");
		if(index > 0){
			out.println("<a href='gmmailstoplayer.jsp?stype=sub1&playerName="+playerName+"&pageindex="+(index-1)+"'><<上一页<<  </a>");	
		}
		for(int i = 0; i < 10; i++){
			if(index+i < pageCount){
			out.println("<a href='gmmailstoplayer.jsp?stype=sub1&playerName="+playerName+"&pageindex="+(index+i)+"'>第"+(index+i+1)+"页  </a>");
			}
		}
		if(index <(pageCount-1)){
			out.println("<a href='gmmailstoplayer.jsp?stype=sub1&playerName="+playerName+"&pageindex="+(index+1)+"'>>>下一页>>  </a>");	
		}
		out.println("<a href='gmmailstoplayer.jsp?stype=sub1&playerName="+playerName+"&pageindex="+(pageCount-1)+"'>末页  </a>");
	}
	
	if((m+PAGE_ROW_NO)>n){
		mails = mmanager.getAllMails(p,m,(n-m));
	}else{
		mails = mmanager.getAllMails(p,m,PAGE_ROW_NO);
	}
	
	%>
	<%
	if(mails != null) {
	ArticleEntityManager aemanager = ArticleEntityManager.getInstance();
	ArticleManager amanager = ArticleManager.getInstance();
	String[] articles = new String[]{"禅悟胸甲","浮屠胸甲","伏世外衫","逸仙外衫","水梦蝶外衫","望遥星外衫","贯九泉胸甲","捣黄龙胸甲","异端胸甲","天邪胸甲","禅悟头盔","浮屠头盔","伏世头饰","逸仙头饰","水梦蝶头饰","望遥星头饰","贯九泉面甲","捣黄龙面甲","异端头盔","天邪头盔","禅悟护手","浮屠护手","伏世手套","逸仙手套","水梦蝶手套","望遥星手套","贯九泉护手","捣黄龙护手","异端护手","天邪护手","禅悟战靴","浮屠战靴","伏世软靴","逸仙软靴","水梦蝶软靴","望遥星软靴","贯九泉皮靴","捣黄龙皮靴","异端战靴","天邪战靴","禅悟肩胛","浮屠肩胛","伏世护肩","逸仙护肩","水梦蝶护肩","望遥星护肩","贯九泉护肩","捣黄龙护肩","异端肩胛","天邪肩胛"};
	 %>
	 <form action="" name="form2" method="post"><input type="hidden" name=playerName value="<%=playerName %>"><input type=hidden name=stype value="delete">
	 <table>
		 <tr class="titlecolor">
		 <td style="text-align:center;" nowrap="nowrap"><b>删除<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>ID<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>账户<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>发件人<b></td>
		  <td style="text-align:center; width='150px';" nowrap="nowrap"><b>标题<b></td>
		  <td style="text-align:center; width='300px';" nowrap="nowrap"><b>内容<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>发送时间<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>级别<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>性别<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>职业<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>给内测号<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>发信内容<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>附件1<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>附件2<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>附件3<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>附件4<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>附件5<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>附件6<b></td>
		  <td style="text-align:center; width='80px'; word-break:break-all;" ><b>此人已得封测号数<b></td>
		  <td style="text-align:center;" ><b>点击发送<b></td>
		 </tr>
		 <%for(int i = 0; i < mails.size(); i++) {
			 Mail mail = mails.get(i);
		 	long senderId = mail.getPoster();
		 	String sender = "系统";
		 	Player player = null;
		 	if(senderId > 0) {
		 		try{
			 		sender = pmanager.getPlayer(senderId).getName();
			 		player = pmanager.getPlayer(senderId);
		 		}catch(Exception e){
		 		}
		 	}
		 	String title = mail.getTitle();
		 	String content = mail.getContent();
		 	int coins = mail.getCoins();
		 	Date sendTime = mail.getCreateDate();
		 	int status = mail.getStatus();
		 	Knapsack.Cell cells[] = mail.getCells();
		 %>
		 <tr>
		 <td align="center">
		  	<input type="checkbox" name="mailId" value="<%=mail.getId() %>">
		  </td>
		  <td align="center">
		  	<%
		  if(player != null){
			  out.println(player.getId());
		  }%>
		  	<input type="hidden" name="playerId_<%=i %>" id="playerId_<%=i %>" value="<%=player != null ? player.getId() : "" %>">
		  </td>
		  <td>
		   <%
		  if(player != null){
			  out.println(player.getUsername());
		  }
		  %>
		  </td>
		  <td>
		   <%=sender
		  %>
		  </td>
		  <td style="word-break:break-all;">
		  	<%=title %>
		  </td>
		  <td style="word-break:break-all;">
		  	<%=content %>
		  </td>
		  <td>
		  	<%=DateUtil.formatDate(sendTime,"yyyy-MM-dd HH:mm:ss") %>
		  </td>
		  <td>
		   <%
		  if(player != null){
			  out.println(player.getLevel());
		  }
		  %>
		  </td>
		  <td>
		  <%
		  if(player != null){
			  out.println(player.getSex() == 0 ? "男":"女");
		  }
		  %>
		  </td>
		  <td>
		  <%
		  if(player != null){
			  out.println(player.getCareer() < CareerManager.careerNames.length ? CareerManager.careerNames[player.getCareer()] : "");
		  }
		  %>
		  </td>
		  <td nowrap="nowrap">
		  <%
		  if(player != null){
			  %>
			  <input type="checkbox" name="zhanghao_<%=i %>" id="zhanghao_<%=i %>" value="<%=player.getName() %>">给内测号
			  <%
		  }
		  %>
		  </td>
		  <td>
		  <%
		  if(player != null){
			  %>
		  	<textarea style="width:200px;height:100px;" id="textarea_<%=i %>" name="textarea_<%=i %>"></textarea>
		  	<%
		  } 
		  	%>
		  </td>
		  <td>
		  <%
		  if(player != null){
			  out.println("<select style='width:100px;' id='fujian1_"+i+"' name='fujian1_"+i+"'>");
			  out.println("<option value=''>");
			  for(String str : articles){
				  out.println("<option value='"+str+"'>"+str);
			  }
			  out.println("</select>");
		  }
		  %>
		  </td>
		  <td>
		  <%
		  if(player != null){
			  out.println("<select style='width:100px;' id='fujian2_"+i+"' name='fujian2_"+i+"'>");
			  out.println("<option value=''>");
			  for(String str : articles){
				  out.println("<option value='"+str+"'>"+str);
			  }
			  out.println("</select>");
		  }
		  %>
		  </td>
		  <td>
		  <%
		  if(player != null){
			  out.println("<select style='width:100px;' id='fujian3_"+i+"' name='fujian3_"+i+"'>");
			  out.println("<option value=''>");
			  for(String str : articles){
				  out.println("<option value='"+str+"'>"+str);
			  }
			  out.println("</select>");
		  }
		  %>
		  </td>
		  <td>
		  <%
		  if(player != null){
			  out.println("<select style='width:100px;' id='fujian4_"+i+"' name='fujian4_"+i+"'>");
			  out.println("<option value=''>");
			  for(String str : articles){
				  out.println("<option value='"+str+"'>"+str);
			  }
			  out.println("</select>");
		  }
		  %>
		  </td>
		  <td>
		  <%
		  if(player != null){
			  out.println("<select style='width:100px;' id='fujian5_"+i+"' name='fujian5_"+i+"'>");
			  out.println("<option value=''>");
			  for(String str : articles){
				  out.println("<option value='"+str+"'>"+str);
			  }
			  out.println("</select>");
		  }
		  %>
		  </td>
		  <td>
		  <%
		  if(player != null){
			  out.println("<select style='width:100px;' id='fujian6_"+i+"' name='fujian6_"+i+"'>");
			  out.println("<option value=''>");
			  for(String str : articles){
				  out.println("<option value='"+str+"'>"+str);
			  }
			  out.println("</select>");
		  }
		  %>
		  </td>
		  
		  	<%
		  	if(fcs != null){
		  		boolean exist = false;
		  		for(FCAccountInfo fc : fcs){
		  			if(fc != null && player != null){
		  				if(fc.getFcAccount() != null && player.getUsername() != null && player.getUsername().equals(fc.getFcAccount())){
		  					exist = true;
		  					if(fc.getCount() == 5){
		  						out.println("<td bgcolor='red' align='left'>"+fc.getCount()+"</td>");
		  					}else if(fc.getCount() > 2){
		  						out.println("<td bgcolor='orange' align='left'>"+fc.getCount()+"</td>");
		  					}else if(fc.getCount() > 0){
		  						out.println("<td bgcolor='#C2CAF5' align='left'>"+fc.getCount()+"</td>");
		  					}else{
		  						out.println("<td align='left'>"+fc.getCount()+"</td>");
		  					}
		  					
		  				}
		  			}
		  		}
		  		if(!exist){
		  			%>
		  			<td>0</td>
		  			<%
		  		}
		  	}else{
		  		%>
		  		<td>0</td>
		  		<%
		  	}
		  	%>
		 <td>
		  <%
		  if(player != null){
			  out.println("<input type='button' value='发信' onclick='javascript:onClickAddArticle("+i+")'>");
		  }
		  %>
		  </td>
		 </tr>
		 <%} %>
		 <tr>
		 <td colspan="2"><input type="submit" value="删除选中邮件"></td><td colspan="16"><input type="reset" value="重置"></td>
		 </tr>
	    </table>
	    </form>
	<%} 
}
%>
</body>
</html>
