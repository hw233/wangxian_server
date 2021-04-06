<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@include file="../header.jsp"%>
<%//include file="../authority.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>自定义回复内容</title>
		<link rel="stylesheet" href="../style.css" />
		<script type="text/javascript">
 			function $(id){
 			   return document.getElementById(id);
 			}
 			function tchange(){
 			   var tc = $("ttype");
 			   var i = tc.value;
 			   if(tc.value==1){
 			      $("stime").innerHTML = $("stime").innerHTML + yes()+mos();
 			   }
 			   if(tc.value==0){
 			      $("stime").innerHTML = "时间：<select id='ttype' name ='ttype' onchange='tchange()' ><option value=''>--</option><option value='0'>今天</option><option value='1'>选择</option></select>"
 			   }
 			   $("ttype").value = i;
 			}
 			function yes(){
 			   var str = "<select id='year' name='year' ><option value=''>--</opton>";
 			   str +="<option value='2010'>2010</option>";
 			   str +="<option value='2011'>2011</option>";
 			   str +="<option value='2012'>2012</option>";
 			   str +="</select>年";
 			   return str;
 			}
 			function mos(){
 			   var str ="<select id='month' name='month'>";
 			   for(var i=1;i<13;i++){
 			   	   str +="<option value='"+i+"'>"+i+"</option>";
 			   }
 			   str +="</select>月<select id='day' name='day'>";
 			   for(var i=1;i<32;i++){
 			       str +="<option value='"+i+"'>"+i+"</option>";
 			   }
 			   str +="</select>日"
 			   return str;
 			}
 			 var index = 1;
 			function add(){
 			  var str = "<div id='d"+index+"'>"+"<select id='s"+index+"' onchange='schange("+index+")' >";
 			   str +="<option value=''>--</option>";
 			  str +="<option value='0'>不区分大小写</option>";
 			  str +="<option value='1'>排除</option></select>";
 			  str +="内容：<input type='text' id='mes"+index+"' name='mes' value='' />"
 			  $("scontent").innerHTML = $("scontent").innerHTML + str;
 			  index ++;
 			}
 			function schange(inc){
 			   var a = $("s"+inc).value;
 			   if(a=='0'){
 			   	  $("mes"+inc).value= " -i "+$("mes"+inc).value;
 			   }
 			   if(a=='1'){
 			      $("mes"+inc).value= " -v "+$("mes"+inc).value;
 			   }
 			}
 			function sub(){
 			  $("f1").action="?action=1";
 			  $("f1").submit();
 			}
 			function sub1(){
 			  $("f1").action="?action=2";
 			  $("f1").submit();
 			}
		</script>
	</head>
	<body>
		<%
			try {
			     Map<String,String> logmap = new HashMap<String,String>();
			     logmap.put("游戏日志","game.log");
			     logmap.put("邮件日志","mailManager.log");
			     String action = request.getParameter("action");
			     String mingling = "date";
			     if(action!=null){
			        if(action.contains("1")){
			           	 String tt = request.getParameter("ttype");
			           	 mingling = "cat ";
			           	 String year = "*";
			           	 String month = "*";
			           	 String day = "*";
			           	 if(tt.contains("1")){
			           	   mingling = "zcat ";
			           	    if(request.getParameter("year")!=null&&request.getParameter("year").trim().length()>0){
			           	    	year = request.getParameter("year");
			           	    }
			           	    if(request.getParameter("month")!=null&&request.getParameter("month").trim().length()>0){
			           	    	month = request.getParameter("month");
			           	    }
			           	    if(request.getParameter("day")!=null&&request.getParameter("day").trim().length()>0){
			           	    	day = request.getParameter("day");
			           	    }
			           	  // out.print("***"+year+"年"+month+"月"+day +"日"); 
			           	 }
			           	 String lname = request.getParameter("lname");
			           	 mingling =mingling+logmap.get(lname);
			           //	 out.print("a["+mingling+"]");
			           	 if(tt.contains("1")){
			           	    mingling =mingling+"."+year+"-"+month+"-day";
			           	 }
			           //	 out.print("b["+mingling+"]");
			           	 String[]  greps = request.getParameterValues("mes");
			           	 for(String g :greps){
			           	   mingling =mingling+"|grep "+g;
			           	 }
			           	 out.print("***************************<br/>命令为"+mingling+"<br/>****************************");
			        }
					if(action.contains("2")){
					   String sx = request.getParameter("sw");
					   out.print("**************************<br/>");
					   out.print(sx);
					   out.print("<br/>**************************");
					   mingling = sx;
					}		     
			     }
			     String message = "";
			          { 
			           //查询日志
			           Process child = Runtime
			                    .getRuntime()
			                    .exec(
			                            new String[] {
			                                    "/bin/sh",
			                                    "-c",
			                                    "cd /home/game/resin/log/game_server;"+mingling },
			                            null,
			                            null);
			
			            BufferedReader in = new BufferedReader(new InputStreamReader(child
			                    .getInputStream()));
			
			            String c = null;
			            while ((c = in.readLine()) != null) {
			                message += c + "<br/>";
			            }
			            child.waitFor();
			            in.close();
			          } 
			     out.print("<form id='f1' action='' method='post' >");
			     out.print("<span id='stime' style='margin-left:50px;width:30%;height:150px;display:inline;border:1px solid red;int:left;'>时间：<select id='ttype' name ='ttype' onchange='tchange()' >");
			     out.print("<option value=''>--</option>");
			     out.print("<option value='0'>今天</option>");
			     out.print("<option value='1'>选择</option></select></span>");
			     out.print("<span id='sname' style='width:20%;height:150px;display:inline;border:1px solid red;int:left;'><select id='lname' name='lname' ><option value=''>--</option> ");
			     for(Map.Entry<String,String> entry : logmap.entrySet()){
			       out.print("<option vlue='"+entry.getValue()+"'>"+entry.getKey()+"</option>");
			     }
			     out.print("</select></span>");
			     out.print("<span id='scontent' style='width:40%;height:150px;display:inline;border:1px solid red;int:left;' >");
			     out.print("<input type='button' value='添加' onclick='add();' /><br/>");
			     out.print("</span><br/>");
			     out.print("<div style='margin-left:100px;width:80%;margin-top:150px;int:center;height:100px;border:1px dotted purple' >");
			     out.print("<textarea id='sw' name='sw' style='width:80%' height:80px;'></textarea>");
			     out.print("</div>");
			     out.print("<br/><input type='button' value='提交 ' onclick='sub();' />");
			     out.print("<br/><input type='button' value='手写提交 ' onclick='sub1();' />");
			     out.print("</form>");
			     
			     out.print("<br/><br/><div style='margin-left:100px;border:1px solid green;int:center;width:80%;height:600px;overflow:scroll;' >"+message+"</div>");       
			            
			            
			} catch (Exception e) {
				//RequestDispatcher rdp = request
				//		.getRequestDispatcher("../gmuser/visitfobiden.jsp");
				//rdp.forward(request, response);
				out.print(StringUtil.getStackTrace(e));
			}
		%>
	</body>
</html>
