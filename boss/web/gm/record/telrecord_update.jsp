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
<title>更新问题记录</title>
<link rel="stylesheet" href="../css/style.css"/>
<script type="text/javascript">
   function $(tag){
      return document.getElementById(tag);
   }
   function sub(){
    var memo1=$("memo1");  
	var memo2=$("memo2");
	var type = document.getElementsByName("type");
    var tr = false;
	for(i=0;i<type.length;i++){
	  if(type[i].checked==true)
	    tr = true;
	}
    if(((memo1&&memo1.value)||(memo2&&memo2.value))&&(tr)){
      $("f1").action='?action=update';
      $("f1").submit();
   }else{
     alert("请填写正确的信息！");
   }
   }


</script>
</head>
<body bgcolor="#FFFFFF" >
	<h1 align="d">更新问题记录</h1>
	<%
	         try{
	         ActionManager amanager = ActionManager.getInstance();
	         TelRecordManager tmanager = TelRecordManager.getInstance(); 
	         String action = request.getParameter("action");
	         String trid = request.getParameter("trid");
	         TelRecord tr=null;
	         if(trid!=null&&trid.trim().length()>0){
	             tr = tmanager.findById(trid.trim());
	            if(tr==null){
	              out.print("<font color='red' >查询的记录不存在，请验证！</font>");
	              return;
	             }
	            }
	         if(action!=null&&action.trim().length()>0){
	            if("update".equals(action.trim())){
	              String now = DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm");
	                String memo1 = request.getParameter("memo1");
	                String memo2 = request.getParameter("memo2");
	                String recorder1 = request.getParameter("recorder1");
	                String recorder2 = request.getParameter("recorder2");
	                String type=request.getParameter("type");
	                if(type!=null)
	                  tr.setType(type);
	                if(memo1!=null&&memo1.trim().length()>0){
	                  tr.setMemo1(memo1);
	                  tr.setRecorder1(recorder1+"("+now+")");
	                }
	                if(memo2!=null&&memo2.trim().length()>0){
	                  tr.setMemo2(memo2);
	                  tr.setRecorder2(recorder2+"("+now +")");
	                }
	                if(type!=null){
		                tmanager.update(tr);
		                amanager.save(tr.getRecorder(),"update one telrecord");
	                }
	                out.print("<font color='red' >更新成功！！！</font>");
	            }
	         }
	         out.print("<input type='button' value='刷新' onclick='window.location.replace(\"telrecord_add.jsp\")' />");
	         out.print("<form action='' method='post' id='f1'>");
		     out.print("<table align='center' width='80%' ><caption>添加账号归属判定</caption>");
		     out.print("<th>游戏账号：</th><td class='top'>"+tr.getUname()+"</td></tr>");
		     out.print("<tr><th>服务器：</th><td>"+tr.getServername()+"</td>");
		     out.print("<th>人物昵称：</th><td>"+tr.getPlayername()+"</td></tr> ");
		      out.print("<tr><th>咨询问题内容：</th><td colspan='3'>");
		     out.print(tr.getQuestion()+"</td></tr>");
		      out.print("<tr><th>回答内容：</th><td colspan='3'>");
		     out.print(tr.getAnswer()+"</td></tr>");
		     
		     out.print("<tr><th>填写时间:</th><td colspan='3'>"+tr.getCreatedate()+"<input type='hidden' id='trid' name='trid' value='"+trid+"' /></td></tr>");
		     out.print("<tr><th>录入人：</th><td colspan='3'>"+tr.getRecorder()+"</td></tr>");  
		      if(tr.getMemo1()!=null&&!"".equals(tr.getMemo1().trim())){
			       out.print("<tr><th>备注一：</th><td>"+tr.getMemo1()+"</td>");
			       out.print("<th>备注填写人：</th><td>"+tr.getRecorder1()+"</td></tr>");
		     }
		     if(tr.getMemo1()==null||"".equals(tr.getMemo1().trim())){
			       out.print("<tr><th>备注一：</th><td><input type='text' value='' id='memo1' name='memo1' /></td>");
			       out.print("<th>备注填写人：</th><td><input type='text' value='' id='recorder1' name='recorder1' /></td></tr>");
		     }
		     if(tr.getMemo1()!=null&&!"".equals(tr.getMemo1().trim())&&(tr.getMemo2()==null||"".equals(tr.getMemo2().trim()))){
			     out.print("<tr><th>备注二：</th><td><input type='text' value='' id='memo2' name='memo2' /></td>");
			     out.print("<th>备注填写人：</th><td>"+tr.getRecorder()+"<input type='hidden' value='"+tr.getRecorder()+"' id='recorder2' name='recorder2' /></td></tr>");
		     }
		      if(tr.getMemo1()!=null&&!"".equals(tr.getMemo1().trim())&&tr.getMemo2()!=null&&!"".equals(tr.getMemo2().trim())){
			     out.print("<tr><th>备注二：</th><td>"+tr.getMemo2()+"</td>");
			     out.print("<th>备注填写人：</th><td>"+tr.getRecorder2()+"</td></tr>");
		     }

		     out.print("<tr><th >类型：</th><td colspan='3'> <input type='radio' value='直接解答'  name='type' />直接解答  <input type='radio' value='已提交'  name='type' />已提交           <input type='radio' value='已解决'  name='type' />已解决</td></tr>    ");
		     out.print("<tr><td colspan='4' ><input type='button' value='提交' onclick='sub();' /><input type='reset' value='重置' /></td></tr></table>");
		     out.print("</form>");
		     
		    }catch(Exception e){
		    out.print(StringUtil.getStackTrace(e));
		    }
	%>
  
</body>
</html>
