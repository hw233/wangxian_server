<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@include file="../header.jsp" %>  
    <%@include file="../authority.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>交易信息 </title>
<link rel="stylesheet" href="../style.css"/>
<script type="text/javascript">
   function overTag(tag){
	  tag.style.color = "red";	
	  tag.style.backgroundColor = "lightcyan";
   }
			
   function outTag(tag){
	  tag.style.color = "black";
	  tag.style.backgroundColor = "white";
   }
   
   function cancer(){
  //取消操作 
   location.reload();
  }
</script>
</head>
<body>
  <%   
      try{
      out.print("<input type='button' value='刷新' onclick='window.location.replace(\"changeinfo.jsp\")' />");
      String username = session.getAttribute("username").toString();
      ChargeInfo ci = ChargeInfo.self;
      byte[] cardId = ci.getCardTypeIds();
      String[] cardTypeNames = ci.getCardTypeNames();
      String[] cardTypeInfos = ci.getCardTypeInfos();
      String[] cardFacevalues = ci.getCardFacevalues();
      String[] exchange = ci.getExchange();
      out.print("<table width=80% ><caption>充值卡</caption><tr><th width=10% >充值卡ID</th><th width=15% >充值卡名称</th><th width=35% >描述</th><th colspan=2 width='40%' >充值比例></tr>");
      if(cardTypeNames!=null&&cardTypeNames.length>0){
      /**
      *获取并显示充值信息
      */
      for(int i = 0;i<cardTypeNames.length;i++){
       String fvs [] = cardFacevalues[i].split(":");
       String cs [] = exchange[i].split(":"); 
       out.print("<tr><td rowspan="+(cs.length+1)+" >"+cardId[i]+"</td>" );
       out.print("<td rowspan="+(cs.length+1)+" >"+cardTypeNames[i]+"</td>" );
       out.print("<td rowspan="+(cs.length+1)+" >"+cardTypeInfos[i]+"</td>" );
       out.print("<th>金额</th><th>比率 </th></tr>");
       for(int j=0;j<cs.length;j++){
	           out.print("<tr><td width='20%' >"+fvs[j]+"</td><td>"+cs[j]+"</td></tr>");
       }
      }
      }
      out.print("</table><p></p><p></p><p></p><p></p>");
     String[] smsGateWay = ci.getSmsGateWay();
     String[] smsInfos = ci.getSmsInfos();
     int[] exchangeRate = ci.getSmsExchangeRate();
     String[] smsFaceValue = ci.getSmsFacevalues();
     String[] smsMiss = ci.getMsg();
     String[] smsNum = ci.getSmsNum();
     int[] smsunitprice = ci.getUnitPrice();
     String[] smsbindingMss = ci.getBindingMsg();
     String[] smsbindingsNum = ci.getBindingSmsNum();
     out.print("<table width=80% ><caption>短信方式</caption><tr><th>名称</th><th>描述</th><th>兑换率</th><th>短信金额</th><th>短信内容</th><th>短信号码</th><th>单条金额</th><th>绑定内容</th><th>绑定号码</th>");
     if(smsGateWay!=null&&smsGateWay.length>0){
     /**
     *获取并显示短信重置信息
     */
     for(int i=0;i<smsGateWay.length;i++){
        out.print("<tr><td>"+smsGateWay[i]+"</td><td>"+smsInfos[i]+"</td><td>"+exchangeRate[i]+"</td><td>"+smsFaceValue[i]+"</td>");
        out.print("<td>"+smsMiss[i]+"</td><td>"+smsNum[i]+"</td><td>"+smsunitprice[i]+"</td><td>"+smsbindingMss[i]+"</td><td>"+smsbindingsNum[i]+"</td></tr>");
     }
     }
     out.print("</table>");
     }catch(Exception e){
         out.print(StringUtil.getStackTrace(e));
		       //RequestDispatcher rdp = request.getRequestDispatcher("../gmuser/visitfobiden.jsp");
              // rdp.forward(request,response);
     }
  %>
</body>
</html>
