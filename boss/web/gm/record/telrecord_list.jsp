<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.fy.boss.gm.record.*"%>
<%@ include file="../header.jsp"   %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<style type="text/css">
body {
	background-color: #c8edcc;
}
</style>
<title>问题记录列表</title>
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
    var cuname = $("cuname").value;
    var ctype = $("ctype").value;
    var cname = $("cname").value;
    var crecorder = $("crecorder").value;
    if(cname||cuname||ctype||crecorder){
      $("ff").submit();
     }else
     alert(" 请输入正确的信息！！！！");
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
	<h1 align="d">问题记录列表</h1>
	<%
	         try{
	         ActionManager amanager = ActionManager.getInstance();
	         TelRecordManager tmanager = TelRecordManager.getInstance(); 
	         out.print("<br/><font color='red'><input type='button' value='刷新' onclick='window.location.replace(\"telrecord_list.jsp\")' /><br/>");
	         String trid = request.getParameter("trid");
	          int start = 0;
	          int size = 20;
	         if(trid!=null&&trid.trim().length()>0){
	           if(tmanager.delete(trid.trim()))
	           out.print("删除成功！");  
	           else
	            out.print("删除失败！");
	         }
	         List<TelRecord> trs = tmanager.getTelrecords();
	          String cuname = request.getParameter("cuname");
	          String ctype = request.getParameter("ctype");
	          String cname = request.getParameter("cname");
	          String crecorder = request.getParameter("crecorder");
	          String action = request.getParameter("action");
	          String stime = request.getParameter("stime");
	          String etime = request.getParameter("etime");
	          if(stime!=null&&etime!=null){
	           Date sdate = DateUtil.parseDate(stime,"yyyy-MM-dd HH:mm");
	           Date edate = DateUtil.parseDate(etime,"yyyy-MM-dd HH:mm");
	           List<TelRecord> trs2 = new ArrayList<TelRecord>();
	           for(TelRecord t:trs){
	              Date d1 = DateUtil.parseDate(t.getCreatedate(),"yyyy-MM-dd HH:mm");
	              if(d1.getTime()>sdate.getTime()&&d1.getTime()<edate.getTime()){
	               trs2.add(t);
	              }
	           }
	           if(trs2.size()>0){
	           out.print("过滤长度为:"+trs2.size());
	           trs = trs2;
	           }else{
	            out.print("该条件之下没有满足条件的选项！");
	           }
	          }
	          
	          if(action!=null&&"query".equals(action.trim())){
	            List<TelRecord> trs1 = null;
	            
	            if(cuname!=null&&cuname.trim().length()>0){
	               trs1 = new ArrayList<TelRecord>();
	              for(TelRecord t:trs){
	                if(t!=null&&t.getUname()!=null&&t.getUname().toLowerCase().contains(cuname.toLowerCase()))
	                  trs1.add(t);
	              }
	            }else if(ctype!=null&&ctype.trim().length()>0){
	               trs1 = new ArrayList<TelRecord>();
	              for(TelRecord t:trs){
	                if(t!=null&&t.getType()!=null&&t.getType().trim().length()>0&&t.getType().toLowerCase().contains(ctype.toLowerCase()))
	                  trs1.add(t);
	              }
	            }
	           else if(crecorder!=null&&crecorder.trim().length()>0){
	               trs1 = new ArrayList<TelRecord>();
	              for(TelRecord t:trs){
	                if(t!=null&&(t.getRecorder()!=null&&t.getRecorder().toLowerCase().contains(crecorder.toLowerCase()))
	                   ||(t.getRecorder1()!=null&&t.getRecorder1().toLowerCase().contains(crecorder.toLowerCase()))
	                   ||(t.getRecorder2()!=null&&t.getRecorder2().toLowerCase().contains(crecorder.toLowerCase())))
	                  trs1.add(t);
	              }
	            }else if(cname!=null&&cname.trim().length()>0){
	              trs1 = new ArrayList<TelRecord>();
	              for(TelRecord t:trs){
	                if(t!=null&&t.getName()!=null&&t.getName().toLowerCase().contains(cname.toLowerCase()))
	                  trs1.add(t);
	              }
	            }
	            if(trs1.size()>0){
	               out.print("<br/>筛选后长度："+trs1.size()+"<br/>");
	               trs = trs1;
	               size =trs.size();
	            }else
	            {
	              out.print("<br/>过滤长度为零！！！！<br/>");
	            }
	          }
	         out.print("<input type='button' value='增加'  onclick='window.location.replace(\"telrecord_add.jsp\")' /><br/>");
	          
	            
	          out.print("<form action='' method='post' id='ff'><table>");
	          out.print("<tr><th>根据称呼筛选：</th><td><input type='text' id='cname' name='cname' value='' /></td></tr>");
	          out.print("<tr><th>根据账号筛选：</th><td><input type='text' id='cuname' name='cuname' value='' /></td></tr>");
	          out.print("<tr><th>根据类型筛选：</th><td><select id='ctype' name='ctype' ><option value=''>--</option><option value='直接解答' >直接解答</option><option value='已提交' >已提交</option><option value='已解决' >已解决</option> </select></td></tr>");
	          out.print("<tr><th>根据录入人员筛选：</th><td><input type='text' id='crecorder' name='crecorder' value='' /></td></tr>");
	          out.print("<tr><td><input type='button' value='查询' onclick='query();' /></td>");
	          out.print("</table></form><br/>");
	          String[] ss1 = DateUtil.formatDate(new Date(),"yyyy-MM-dd-HH-mm").split("-"); 
	          out.print("开始时间:<input type='text' value='"+ss1[0]+"' size='4' name='syear' id='syear' />年");
	          out.print("<input type='text' value='"+ss1[1]+"' size='4' name='smonth' id='smonth' />月");
	          out.print("<input type='text' value='"+ss1[2]+"' size='4' name='sday' id='sday' />日");
	          out.print("<input type='text' value='"+ss1[3]+"' size='4' name='shour' id='shour' />时");
	          out.print("<input type='text' value='"+ss1[4]+"' size='4' name='smin' id='smin' />分");
	          out.print("~结束时间<input type='text' value='"+ss1[0]+"' size='4' name='eyear' id='eyear' />年");
	          out.print("<input type='text' value='"+ss1[1]+"' size='4' name='emonth' id='emonth' />月");
	          out.print("<input type='text' value='"+ss1[2]+"' size='4' name='eday' id='eday' />日");
	          out.print("<input type='text' value='"+ss1[3]+"' size='4' name='ehour' id='ehour' />时"); 
	          out.print("<input type='text' value='"+ss1[4]+"' size='4' name='emin' id='emin' />分");
	          out.print("      <input type='button' value='查询' onclick='querybydate();' />");
	          String startstr = request.getParameter("start");
	          if(startstr!=null&&startstr.trim().length()>0)
	           start = Integer.parseInt(startstr.trim());
	         if(trs!=null&&trs.size()>0){
	           out.print("总的数量为："+trs.size());
	        
	          if(start>0)
	           out.print("<a href='?start="+(start-20)+"' >上一页</a>");
	          if((start+20)<trs.size())
	           out.print("<a href='?start="+(start+20)+"' >下一页</a>");
	           }
	           out.print("</font><table align='center' width='90%' ><caption>电话记录列表</caption>");
	           out.print("<tr><th>称呼</th><th>账号</th><th>服务器</th><th>人物名称 </th><th>咨询问题</th><th>回答内容</th>");
	           out.print("<th>填写人</th><th>填写时间</th><th>类型</th><th>备注1</th><th>填写人1</th><th>备注2</th><th>填写人2</th><th>操作 </th></tr>");
	          for(int i=trs.size()-start-1;i>=0&&i>(trs.size()-start-size-1);i--){
	            TelRecord tr = trs.get(i);
	           out.print("<tr><td>"+tr.getName()+"</td><td>"+tr.getUname()+"</td><td>"+tr.getServername()+"</td>");
               out.print("<td>"+tr.getPlayername()+"</td><td>"+tr.getQuestion()+"</td><td>"+tr.getAnswer()+"</td>");
               out.print("<td>"+tr.getRecorder()+"</td><td>"+tr.getCreatedate()+"</td><td>"+tr.getType()+"</td>");
               out.print("<td>"+(tr.getMemo1()==null?"":tr.getMemo1())+"</td><td>"+(tr.getRecorder1()==null?"":tr.getRecorder1())+"</td><td>"+(tr.getMemo2()==null?"":tr.getMemo2())+"</td><td>"+(tr.getRecorder2()==null?"":tr.getRecorder2())+"</td>");
               out.print("<td><input type='button' value='编辑' onclick='window.location.replace(\"telrecord_update.jsp?trid="+tr.getId()+"\")' /><br/>");
               out.print("<input type='button' value='删除' onclick='del(\""+tr.getId()+"\")' /></td></tr>");
	          }
		      out.print("</table>"); 
		    }catch(Exception e){
		    out.print(StringUtil.getStackTrace(e));
		    }
	%>
  
</body>
</html>
