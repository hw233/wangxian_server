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
String playerId = request.getParameter("playerId");
String textarea = request.getParameter("textarea1");
String fujian1 = request.getParameter("fujian1");
String fujian2 = request.getParameter("fujian2");
String fujian3 = request.getParameter("fujian3");
String fujian4 = request.getParameter("fujian4");
String fujian5 = request.getParameter("fujian5");
String fujian6 = request.getParameter("fujian6");
PlayerManager pmanager = PlayerManager.getInstance();
if(playerId != null){
	Player p = null;
	try{
		int id = Integer.parseInt(playerId);
		p = pmanager.getPlayer(id);
	}catch(Exception e){
	}
	if(p != null){
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
		try{
			int id = Integer.parseInt(playerId);
			gm.sendMailPresent(id,comment,list.toArray(new String[0]));
			out.println("邮件发送成功<br><br>");
		}catch(Exception e){
			out.println("请输入正确Id e:"+StringUtil.getStackTrace(e));
		}
	}else{
		out.println("没有这个角色<br><br>");
	}
}

%>
<b>潜龙GM</b><br><br>
<%

	%>
	<%
	ArticleEntityManager aemanager = ArticleEntityManager.getInstance();
	ArticleManager amanager = ArticleManager.getInstance();
	String[] articles = new String[]{"禅悟胸甲","浮屠胸甲","伏世外衫","逸仙外衫","水梦蝶外衫","望遥星外衫","贯九泉胸甲","捣黄龙胸甲","异端胸甲","天邪胸甲","禅悟头盔","浮屠头盔","伏世头饰","逸仙头饰","水梦蝶头饰","望遥星头饰","贯九泉面甲","捣黄龙面甲","异端头盔","天邪头盔","禅悟护手","浮屠护手","伏世手套","逸仙手套","水梦蝶手套","望遥星手套","贯九泉护手","捣黄龙护手","异端护手","天邪护手","禅悟战靴","浮屠战靴","伏世软靴","逸仙软靴","水梦蝶软靴","望遥星软靴","贯九泉皮靴","捣黄龙皮靴","异端战靴","天邪战靴","禅悟肩胛","浮屠肩胛","伏世护肩","逸仙护肩","水梦蝶护肩","望遥星护肩","贯九泉护肩","捣黄龙护肩","异端肩胛","天邪肩胛"};
	 %>
	 <form action="" name="form2" method="post">
	 <table>
		 <tr class="titlecolor">
		  <td style="text-align:center;" nowrap="nowrap"><b>收件人Id<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>发信内容<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>附件1<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>附件2<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>附件3<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>附件4<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>附件5<b></td>
		  <td style="text-align:center;" nowrap="nowrap"><b>附件6<b></td>
		 </tr>
		 
		 <tr>
		  <td align="center">
		  	<input type="text" name="playerId" id="playerId">
		  </td>
		  <td style="word-break:break-all;">
		  	<textarea style="width:200px;height:100px;" id="textarea1" name="textarea1"></textarea>
		  </td>
		  <td>
		  	<select style="width:100px;" id="fujian1" name="fujian1">
			  <option value=''>
			  <%
			  for(String str : articles){
				  out.println("<option value='"+str+"'>"+str);
			  }
			  %>
			</select>
		  </td>
		  <td>
		  	<select style="width:100px;" id="fujian2" name="fujian2">
			  <option value=''>
			  <%
			  for(String str : articles){
				  out.println("<option value='"+str+"'>"+str);
			  }
			  %>
			</select>
		  </td>
		  <td>
		  	<select style="width:100px;" id="fujian3" name="fujian3">
			  <option value=''>
			  <%
			  for(String str : articles){
				  out.println("<option value='"+str+"'>"+str);
			  }
			  %>
			</select>
		  </td>
		  <td>
		  	<select style="width:100px;" id="fujian4" name="fujian4">
			  <option value=''>
			  <%
			  for(String str : articles){
				  out.println("<option value='"+str+"'>"+str);
			  }
			  %>
			</select>
		  </td>
		  <td>
		  	<select style="width:100px;" id="fujian5" name="fujian5">
			  <option value=''>
			  <%
			  for(String str : articles){
				  out.println("<option value='"+str+"'>"+str);
			  }
			  %>
			</select>
		  </td>
		  <td>
		  	<select style="width:100px;" id="fujian6" name="fujian6">
			  <option value=''>
			  <%
			  for(String str : articles){
				  out.println("<option value='"+str+"'>"+str);
			  }
			  %>
			</select>
		  </td>
		  
		 </tr>
		 <tr>
		 <td colspan="2"><input type="submit" value="发信"></td><td colspan="16"><input type="reset" value="重置"></td>
		 </tr>
	    </table>
	    </form>
</body>
</html>
