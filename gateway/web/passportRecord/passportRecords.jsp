<%@page import="com.sun.jndi.url.rmi.rmiURLContext"%>
<%@page import="com.fy.gamegateway.getbackpassport.*"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="java.util.*,java.io.*"%>
<%-- <%@ include file="../import.jsp"   %> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<style type="text/css">
body {
	background-color: #c8edcc;
}
</style>
<title>密保记录列表</title>
<link rel="stylesheet" href="record.css"/>
<script type="text/javascript">
   function $(tag){
      return document.getElementById(tag);
   }
   function query(){
    $("ff").action="?action=query"
    var cphone = $("cphone").value;
    var cuname = $("cuname").value;
    var ctype = $("ctype").value;
    var qtype = $("qtype").value;
    if(cphone||cuname||ctype||qtype){
      $("ff").submit();
     }else
     alert(" 请输入正确的信息！！！！");
   }
</script>
</head>
<body bgcolor="#FFFFFF" >
	<h1 align="d">密保记录列表</h1>
		<%
		String mess = "";	
		try{
		  RecordManager rManager = RecordManager.getInstance();	  	
		  int start = 0;
	      int size = 20;	
	      long nums = rManager.getRecordAmounts();
	      
          out.print("<form action='' method='post' id='ff'><table>");
          out.print("<tr><th>根据账号筛选：</th><td><input type='text' id='cuname' name='cuname' value='' /></td></tr>");
          out.print("<tr><th>根据状态筛选：</th><td><select id='ctype' name='ctype' ><option value=''>--</option><option value='等待处理' >等待处理</option><option value='已经解答' >已经解答</option><option value='等待核实' >等待核实</option><option value='无效信息' >无效信息</option> </select></td></tr>");
          out.print("<tr><th>根据密保问题筛选：</th><td><select id='qtype' name='qtype' ><option value=''>--</option><option value='您最爱人的名字' >您最爱人的名字</option><option value='您父亲的名字' >您父亲的名字</option><option value='您母亲的名字' >您母亲的名字</option><option value='您小学的名字' >您小学的名字</option><option value='您手机号的后四位' >您手机号的后四位</option><option value='您的出生日期' >您的出生日期</option><option value='您的真实姓名' >您的真实姓名</option></select></td></tr>");
          out.print("<tr><th>根据手机号筛选：</th><td><input type='text' id='cphone' name='cphone' value='' /></td></tr>");
          out.print("<tr><td><input type='button' value='查询' onclick='query();' /></td></tr>");
          out.print("</table></form><br/>");
          String startstr = request.getParameter("start");
          if(startstr!=null&&startstr.trim().length()>0)
           	 start = Integer.parseInt(startstr.trim());
          if(nums>0){
          	 out.print("总的数量为："+nums);
          
          if(start>0)
           out.print("<a href='?start="+(start-20)+"' >上一页</a>");
          if((start+20)<nums)
           out.print("<a href='?start="+(start+20)+"' >下一页</a>");
           }
          
          String action = request.getParameter("action");
		  
          List<PassportRecord> records = rManager.getPassportRecords(start, size);
          if(action!=null&&action.trim().equals("query")){
              String cphone = request.getParameter("cphone");
              String cuname = request.getParameter("cuname");
              String ctype = request.getParameter("ctype");
              String qtype = request.getParameter("qtype");
        	  if(cphone!=null&&!cphone.trim().equals("")){
       			  records = rManager.getPassportRecordByTel(cphone, 1, nums);
       			  if(records==null){
       				mess = "<font color='red' >没有电话为<"+cphone+">的记录！！！</font>";
       			  }
        	  }
        	  if(ctype!=null&&!ctype.trim().equals("")){
       			  records = rManager.getPassportRecordByStat(ctype, 1, nums);
       			  if(records==null){
       				  mess = "<font color='red' >没有状态为<"+ctype+">的记录！！！";
       			  }
        	  }
        	  if(qtype!=null&&!qtype.trim().equals("")){
       			  records = rManager.getPassportRecordByquestion(qtype, 1, nums);
       			  if(records==null){
       				mess = "<font color='red' >没有密保问题<"+qtype+">的记录！！！";
       			  }
        	  }
        	  if(cuname!=null&&!cuname.trim().equals("")){
       			  records = rManager.getPassportRecordByusername(cuname, 1, nums);
       			  if(records==null){
            		 mess = "<font color='red' >没有用户名为<"+cuname+">的记录！！！";
            	  }
        	  }
          }
          
          
          
           out.print("</font><table align='center' width='90%' ><caption>密保记录列表</caption>");
           out.print("<tr><th>手机号 </th><th>角色名</th><th>账号</th><th>大区</th><th>服务器</th><th>密保问题</th><th>密保答案</th>");
           out.print("<th>机型</th><th>渠道</th><th>clientid</th><th>最后一次充值日期</th><th>最后一次充值金额</th><th>上次登录几天前</th><th>提交时间</th><th>状态</th><th>编辑</th></tr>");
           if(records.size()>0){
        	   for(PassportRecord pr:records){
                   out.print("<tr><td>"+pr.getTelnumber()+"</td><td>"+pr.getName()+"</td><td>"+pr.getUsername()+"</td><td>"+pr.getAreaname()+"</td><td>"+pr.getServername()+"</td>");
                   out.print("<td>"+pr.getPassportquestion()+"</td><td>"+pr.getPassportanswer()+"</td>");
                   out.print("<td>"+pr.getMobiletype()+"</td>");
                   out.print("<td>"+pr.getChannel()+"</td><td>"+pr.getClientId()+"</td><td>"+pr.getLastchargedate()+"</td><td>"+pr.getLastchargeamount()+"</td><td>"+pr.getLastlogindate()+"</td><td>"+pr.getCommittime()+"</td>");
                   if(pr.getState().equals("等待处理")){
                	   out.print("<td bgcolor='red'>"+pr.getState()+"</td>");
                   }else if(pr.getState().equals("等待核实")){
                	   out.print("<td bgcolor='yellow'>"+pr.getState()+"</td>");
                   }else if(pr.getState().equals("无效信息")){
                	   out.print("<td bgcolor='black'>"+pr.getState()+"</td>");
                   }
                   else{
                	   out.print("<td>"+pr.getState()+"</td>");
                   }
                	   
                   
                   out.print("<td><input type='button' value='状态编辑' onclick='window.location.replace(\"passRecord_update.jsp?trid="+pr.getId()+"\")' /></td></tr>");
                }
           }
           
	      out.print("</table>"); 
	    }catch(Exception e){
	  	  	out.print(mess);
	    }
	%>
  
</body>
</html>
