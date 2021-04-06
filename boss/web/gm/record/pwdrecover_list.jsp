<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.fy.boss.gm.record.AccountRecord"%>
<%@page import="com.fy.boss.gm.record.*"%>
<%@ include file="../header.jsp"   %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<%-- <%@include file="../authority.jsp" %> --%>
<head>
<style type="text/css">
body {
	background-color: #c8edcc;
}
</style>
<title>账号恢复列表</title>
<link rel="stylesheet" href="../css/style.css"/>
<script type="text/javascript">
   function $(tag){
      return document.getElementById(tag);
   }
   function del(rid){
     if(window.confirm("你确定要删除该记录吗？")){
        window.location.replace("recover_list.jsp?rid="+rid);
     }
   }  
   function query(){
    $("ff").action="?action=query"
    var cphone = $("cphone").value;
    var cuname = $("cuname").value;
    var ctype = $("ctype").value;
    var cname = $("cname").value;
    var crecorder = $("crecorder").value;
    if(cphone||cuname||ctype||crecorder||cname){
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
       window.location.replace("recover_list.jsp?stime="+stime+"&etime="+etime);
     }else
      alert("请输入正确的数据！");
   }
</script>
</head>
<body bgcolor="#FFFFFF" >
	<h1 align="d">账号恢复列表</h1>
	<%
	         try{
	         ActionManager amanager = ActionManager.getInstance();
	         RecoverManager rmanager = RecoverManager.getInstance();
	         out.print("<br/><font color='red' ><input type='button' value='刷新' onclick='window.location.replace(\"recover_list.jsp\")' /><br/>");
	         String rid = request.getParameter("rid");
	          int start = 0;
	          int size = 20;
	         if(rid!=null&&rid.trim().length()>0){
	            if(rmanager.delete(rid.trim()))
	              out.print("<br/>删除成功<br/>");
	            else
	              out.print("<br/>删除失败<br/>");
	         } 
	          List<Recover> rrs = rmanager.getRecoverrecords();
	          String cphone = request.getParameter("cphone");
	          String cuname = request.getParameter("cuname");
	          String cname = request.getParameter("cname");
	          String ctype = request.getParameter("ctype");
	          String crecorder = request.getParameter("crecorder");
	          String action = request.getParameter("action");
	          String stime = request.getParameter("stime");
	          String etime = request.getParameter("etime");
	          if(stime!=null&&etime!=null){
		           Date sdate = DateUtil.parseDate(stime,"yyyy-MM-dd HH:mm");
		           Date edate = DateUtil.parseDate(etime,"yyyy-MM-dd HH:mm");
		           List<Recover> rs2 = new ArrayList<Recover>();
		           for(Recover r:rrs){
		              Date d1 = DateUtil.parseDate(r.getCreatetime(),"yyyy-MM-dd HH:mm");
		              if(d1.getTime()>sdate.getTime()&&d1.getTime()<edate.getTime()){
		               rs2.add(r);
		              }
		           }
		           if(rs2.size()>0){
		           		rrs = rs2;
		           		out.print("<br/>筛选后长度："+rs2.size()+"<br/>");
		           }else{
		                out.print("<br/>筛选后长度为0<br/>");
		           }
		           
	          }
	          if(action!=null&&"query".equals(action.trim())){
	            List<Recover> rs1 = null;
	            if(cphone!=null&&cphone.trim().length()>0){
	               rs1 = new ArrayList<Recover>();
	              for(Recover r:rrs){
	                if(r!=null&&r.getPhone()!=null&&r.getPhone().contains(cphone))
	                  rs1.add(r);
	              }
	            }else if(cuname!=null&&cuname.trim().length()>0){
	              rs1 = new ArrayList<Recover>();
	              for(Recover r:rrs){
	                if(r!=null&&r.getUsername()!=null&&r.getUsername().toLowerCase().contains(cuname.toLowerCase()))
	                  rs1.add(r);
	              }
	            }else if(ctype!=null&&ctype.trim().length()>0){
	              rs1 = new ArrayList<Recover>();
	              for(Recover r:rrs){
	                if(r!=null&&r.getType()!=null&&r.getType().trim().length()>0&&r.getType().toLowerCase().contains(ctype.toLowerCase()))
	                  rs1.add(r);
	              }
	            }else if(crecorder!=null&&crecorder.trim().length()>0){
	              rs1 = new ArrayList<Recover>();
	              for(Recover r:rrs){
	                if(r!=null&&((r.getRecoder()!=null&&r.getRecoder().toLowerCase().contains(crecorder.toLowerCase()))
	                    ||(r.getRecoder1()!=null&&r.getRecoder1().toLowerCase().contains(crecorder.toLowerCase()))
	                    ||(r.getRecoder2()!=null&&r.getRecoder2().toLowerCase().contains(crecorder.toLowerCase()))
	                    ||(r.getRecoder3()!=null&&r.getRecoder3().toLowerCase().contains(crecorder.toLowerCase()))
	                    ||(r.getRecoder4()!=null&&r.getRecoder4().toLowerCase().contains(crecorder.toLowerCase()))
	                   )){
	                  rs1.add(r);
	                  }
	              }
	            }else if(cname!=null&&cname.trim().length()>0){
	               rs1 = new ArrayList<Recover>();
	              for(Recover r:rrs){
	                if(r!=null&&r.getName()!=null&&r.getName().toLowerCase().contains(cname.toLowerCase()))
	                  rs1.add(r);
	              }
	            }
	            
	            if(rs1.size()>0){
	               out.print("<br/>筛选后长度："+rs1.size()+"<br/>");
	               rrs = rs1;
	               size = rrs.size();
	            }else
	            {
	              out.print("<br/>过滤长度为零！！！！<br/>");
	            }
	          }
	          out.print("<input type='button' value='增加' onclick='window.location.replace(\"recover_add.jsp\")' /><br/>");
	         
	          out.print("<form action='' method='post' id='ff'><table>");
	          out.print("<tr><th>根据手机号筛选：</th><td class='top'><input type='text' id='cphone' name='cphone' value='' /></td></tr>");
	          out.print("<tr><th>根据账号筛选：</th><td><input type='text' id='cuname' name='cuname' value='' /></td></tr>");
	          out.print("<tr><th>根据称呼筛选：</th><td><input type='text' id='cname' name='cname' value='' /></td></tr>");
	          out.print("<tr><th>根据类型筛选：</th><td><select  name='ctype' id='ctype' ><option value='' >--</option><option value='已提交' >已提交</option><option value='已解决' >已解决</option><option value='已结束' >已结束</option></select></td></tr>");
	          out.print("<tr><th>根据录入人员筛选：</th><td><input type='text' id='crecorder' name='crecorder' value='' /></td></tr>");
	          out.print("<tr><td colspan='2' ><input type='button' value='查询' onclick='query();' /></td></tr>");
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
	         if((stime==null&&etime==null)&&(crecorder==null||crecorder.trim().length()<1)&&(ctype==null||ctype.trim().length()<1)&&(cuname==null||cuname.trim().length()<1)&&(cname==null||cname.trim().length()<1)&&(cphone==null||cphone.trim().length()<1)&&rrs!=null&&rrs.size()>0){
		          out.print("总的数量为："+rrs.size());
		          if(start>0)
		           out.print("<a href='?start="+(start-20)+"' >上一页</a>");
		          if((start+20)<rrs.size())
		           out.print("<a href='?start="+(start+20)+"' >下一页</a>");
	           }
	           out.print("</font><table width='90%' align='center' ><caption>账号归属列表 </caption>");
	           out.print("<tr><th>手机号</th><th>称呼</th><th>账号</th><th>服务器</th><th>人物名称</th>");
	           out.print("<th>录入员</th><th>录入时间</th><th>类型</th><th>备注一</th>");
	           out.print("<th>备注一填写人</th><th>备注二</th><th>备注二填写人</th><th>备注三</th><th>备注三填写人</th><th>备注四</th><th>备注四填写人</th><th>最终结果</th><th>外呼记录</th><th>外呼人员</th><th>操作</th></tr>");
	          for(int i=rrs.size()-start-1;i>=0&&i>(rrs.size()-start-size-1);i--){
	           Recover rr = rrs.get(i);
	           out.print("<tr><td>"+rr.getPhone()+"</td><td>"+rr.getName()+"</td><td>"+rr.getUsername()+"</td><td>"+rr.getServer()+"</td><td>"+rr.getPlayer()+"</td>");
	           out.print("<td>"+rr.getRecoder()+"</td><td>"+rr.getCreatetime()+"</td><td>"+rr.getType()+"</td><td>"+(rr.getMemo1()==null?"":rr.getMemo1())+"</td>");
	           out.print("<td>"+(rr.getRecoder1()==null?"":rr.getRecoder1())+"</td><td>"+(rr.getMemo2()==null?"":rr.getMemo2())+"</td><td>"+(rr.getRecoder2()==null?"":rr.getRecoder2())+"</td>");
	           out.print("<td>"+(rr.getMemo3()==null?"":rr.getMemo3())+"</td><td>"+(rr.getRecoder3()==null?"":rr.getRecoder3())+"</td>");
	           out.print("<td>"+(rr.getMemo4()==null?"":rr.getMemo4())+"</td><td>"+(rr.getRecoder4()==null?"":rr.getRecoder4())+"</td><td>"+(rr.getResult()==null?"":rr.getResult())+"</td>");
	           out.print("<td>"+(rr.getWaihumemo()==null?"":rr.getWaihumemo())+"</td><td>"+(rr.getWaihuname()==null?"":rr.getWaihuname())+"</td>");
	           out.print("<td><input type='button' value='编辑' onclick='window.location.replace(\"recover_update.jsp?rid="+rr.getId()+"\");' /><br/>");
	           out.print("<input type='button' value='删除' onclick='del(\""+rr.getId()+"\")' /></td>");
	           out.print("</tr>");
	          }  
	         out.print("</table>");
		     
		    }catch(Exception e){
		    out.print(StringUtil.getStackTrace(e));
		    }
	%>
  
</body>
</html>
