<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<style type="text/css">
body {
	background-color: #c8edcc;
}
</style>
<title>密保记录列表</title>
<link rel="stylesheet" href="../css/style.css"/>
<script type="text/javascript">
   function $(tag){
      return document.getElementById(tag);
   }
    function del(trid){
     if(window.confirm("你确定要删除该记录吗？")){
        window.location.replace("telrecord_list.jsp?trid="+trid);
     }
   }  
   function query(){
    $("ff").action="?action=query"
    var cphone = $("cphone").value;
    var cuname = $("cuname").value;
    var ctype = $("ctype").value;
    var qtype = $("qtype").value;
    var cname = $("cname").value;
    var crecorder = $("crecorder").value;
    if(cphone||cname||cuname||ctype||crecorder||qtype){
      $("ff").submit();
     }else
     alert(" 请输入正确的信息！！！！");
   }
    function querybytype(){
	    $("ff").action="?action=querybytype"
	    var ctype = $("ctype").value;
	    var qtype = $("qtype").value;
	    if(ctype&&qtype){
	      $("ff").submit();
	     }else
	     alert(" 2个类型都必须写啊！！！！");
   }
    function querybydate(){
     var syear = $("syear").value;
     var smonth = $("smonth").value;
     var sday = $("sday").value;
     var shour = $("shour").value;
     var smin = $("smin").value;
     var eyear = $("eyear").value;
     var emonth = $("emonth").value;
     var eday = $("eday").value;
     var ehour = $("ehour").value;
     var emin = $("emin").value;
     if(syear&&smonth&&sday&&shour&&smin&&eyear&&emonth&&eday&&ehour&&emin){
       var stime = syear+"-"+smonth +"-"+sday +" "+shour+":"+smin;
       var etime = eyear+"-"+emonth +"-"+eday +" "+ehour+":"+emin;
       window.location.replace("telrecord_list.jsp?stime="+stime+"&etime="+etime);
     }else
      alert("请输入正确的数据！");
   }
</script>
</head>
<body bgcolor="#FFFFFF" >
	<h1 align="d">密保记录列表</h1>
		<%
// 		try{
// 		  RecordManager rManager = RecordManager.getInstance();	  	
// 		  int start = 0;
// 	      int size = 20;	
// 	      long nums = rManager.getRecordAmounts();
	      
//           out.print("<form action='' method='post' id='ff'><table>");
//           out.print("<tr><th>根据状态筛选：</th><td><input type='text' id='cname' name='cname' value='' /></td></tr>");
//           out.print("<tr><th>根据账号筛选：</th><td><input type='text' id='cuname' name='cuname' value='' /></td></tr>");
//           out.print("<tr><th>根据角色名筛选：</th><td><select id='ctype' name='ctype' ><option value=''>--</option><option value='直接解答' >直接解答</option><option value='已提交' >已提交</option><option value='已解决' >已解决</option> </select></td></tr>");
//           out.print("<tr><th>根据密保问题筛选：</th><td><select id='qtype' name='qtype' ><option value=''>--</option><option value='角色恢复' >角色恢复</option><option value='物品丢失' >物品丢失</option><option value='无法登录' >无法登录</option><option value='无法解决' >无法解决</option><option value='任务' >任务</option><option value='外挂' >外挂</option><option value='建议' >建议</option><option value='充值' >充值</option><option value='其他' >其他</option> </select></td></tr>");
//           out.print("<tr><th>根据手机号筛选：</th><td><input type='text' id='crecorder' name='crecorder' value='' /></td></tr>");
//           out.print("<tr><td><input type='button' value='查询' onclick='query();' /></td>");
//           out.print("<td><input type='button' value='查询2' onclick='querybytype();' /></td></tr>");
//           out.print("</table></form><br/>");
//           out.print("      <input type='button' value='查询' onclick='querybydate();' />");
//           String startstr = request.getParameter("start");
//           if(startstr!=null&&startstr.trim().length()>0)
//            	 start = Integer.parseInt(startstr.trim());
//           if(nums>0){
//           	 out.print("总的数量为："+nums);
          
//           if(start>0)
//            out.print("<a href='?start="+(start-20)+"' >上一页</a>");
//           if((start+20)<nums)
//            out.print("<a href='?start="+(start+20)+"' >下一页</a>");
//            }
//            out.print("</font><table align='center' width='90%' ><caption>电话记录列表</caption>");
//            out.print("<tr><th>手机号 </th><th>角色名</th><th>账号</th><th>大区</th><th>服务器</th><th>密保问题</th><th>密保答案</th>");
//            out.print("<th>状态</th><th>提交时间</th><th>机型</th><th>渠道</th><th>clientid</th><th>最后一次充值日期</th><th>最后一次充值金额</th><th>最后一次登录日期</th></tr>");
//            List<PassportRecord> records = rManager.getPassportRecords(start, size); 
//            for(PassportRecord pr:records){
//               out.print("<tr><td>"+pr.getTelnumber()+"</td><td>"+pr.getName()+"</td><td>"+pr.getUsername()+"</td><td>"+pr.getAreaname()+"</td><td>"+pr.getServername()+"</td>");
//               out.print("<td>"+pr.getPassportquestion()+"</td><td>"+pr.getPassportanswer()+"</td>");
//               out.print("<td>"+pr.getState()+"</td><td>"+pr.getCommittime()+"</td><td>"+pr.getMobiletype()+"</td>");
//               out.print("<td>"+pr.getChannel()+"</td><td>"+pr.getLastchargedate()+"</td><td>"+pr.getLastchargeamount()+"</td><td>"+pr.getLastlogindate()+"</td>");
//           }
// 	      out.print("</table>"); 
// 	    }catch(Exception e){
// 	  	  	out.print(e);
// 	    }
%>
  
</body>
</html>
