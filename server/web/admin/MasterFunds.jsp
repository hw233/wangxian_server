<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page
	import="com.fy.engineserver.warehouse.service.*,com.fy.engineserver.warehouse.*,com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.mail.*,com.fy.engineserver.mail.service.*,com.xuanzhi.tools.text.*,com.fy.engineserver.closebetatest.*"%>
<%@page import="com.fy.engineserver.economic.BillingCenter"%>
<%@page import="com.fy.engineserver.economic.CurrencyType"%>
<%@page import="com.fy.engineserver.economic.SavingReasonType"%>
<%@include file="IPManager.jsp" %><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>师徒积分</title>
		<link rel="stylesheet" type="text/css" href="../css/common.css" />
		<link rel="stylesheet" type="text/css" href="../css/table.css" />
	</head>
	<body>
		<script type="text/javascript">
	  function setIds(){
	    var ids = document.getElementById("ids").value;
	    if(ids&&ids!=""){
	    alert(ids);
	     window.location.replace("?ids="+ids);
	    }else
	     alert("输入的内容不能为空！");
	  }
</script>

		
		输入玩家ID，以","分割。
		<br>
		<textarea id='ids' name='ids' value='' rows="10" cols="40"></textarea>
		<input type='button' value='提交' onclick='setIds();' />
		<br>
		<%
			try {
				String ids = request.getParameter("ids");
				if (ids != null) {
					String[] ss = ids.split(",");
					BillingCenter bc = BillingCenter.getInstance();
					PlayerManager pm = PlayerManager.getInstance();
					for (int i = 0; i < ss.length; i++) {
						Player master = null;
						try{
							master = pm.getPlayer(Integer.parseInt(ss[i]));													
						}catch(Exception e){
							e.printStackTrace();
						}
						if(master!=null){
							//master.setMasterFunds(0);
							//bc.playerSaving(master,200,CurrencyType.MASTER_FUNDS,SavingReasonType.FINISH_APPRENTICESHIP,null);						
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		%>
	</body>
</html>
