<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.boss.gm.newfeedback.GmTalk"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.boss.gm.newfeedback.NewFeedback"%>
<%@page import="com.fy.boss.gm.newfeedback.service.NewFeedbackQueueManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>GM回复</title>
<style type="text/css">
body,td,th {
	font-size: 14px;
	color: #009;
}
body,h1 {
	color:#000;
	font-size:14px;
}
body {
	background-color: #666;
	font-size: 12px;
	color: #003;
	background-image: url(../images/beijing.gif);
}
#apDiv1 {
	position:absolute;
	width:215px;
	height:35px;
	z-index:1;
	left: 450px;
	top: 65px;
	background-color: #FFFFFF;
	visibility: hidden;
}
#apDiv1 table tr td {
	color: #F00;
	font-size: 14px;
	font-weight: bold;
	text-align: center;
}
</style>
<script type="text/javascript">
function MM_showHideLayers() { //v9.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) 
  with (document) if (getElementById && ((obj=getElementById(args[i]))!=null)) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
    obj.visibility=v; }
}
</script>
</head>

<body>
<table width="550" height="520" border="1" align="center" cellspacing="2">
  <tr>
    <td height="60" colspan="2"><p>    <link href="css/style.css" rel="stylesheet" type="text/css"/>
<body>    
      <p>  显示GM编号：当前普通用户在线排队数量：0  当前VIP用户在线排队数量：0
      <select name="select" size="1" id="select">
        <option>请选择</option>
        <option>运行问题</option>
        <option>玩家建议</option>
        <option>数据异常</option>
        <option>充值问题</option>
        <option>账号问题</option>
        <option>丢失问题</option>
        <option>游戏咨询</option>
        <option>其他问题</option>
      </select>
      <ul id="nav">
        <li id="selected"><a href="#" onclick="MM_showHideLayers('apDiv1','','show')">暂离</a></li>
        <li><a href="#">挂起</a></li>
        <li><a href="#">关闭</a></li>
        <li><a href="zhuxiao.html" target="_parent">注销</a></li>
    </ul>
    </div>
<p>
  <script src="js/jquery.min.js" type="text/javascript"></script>
  <script type="text/javascript" src="js/jquery-ui.min.js"></script>
  <script type="text/javascript" src="js/jquery.spasticNav.js"></script>
  <script type="text/javascript">
    $('#nav').spasticNav();
  </script>
      <div id="apDiv1">
        <table width="211" border="1">
          <tr>
            <td align="center" valign="middle">当前状态为：暂离</td>
            <td><input name="button2" type="submit" id="button2" onclick="MM_showHideLayers('apDiv1','','hide')" value="取消暂离" /></td>
          </tr>
        </table>
      </div>
    <tr>
    <td height="210" colspan="2"><label for="textarea"> </label>
      <%
      	/**提供一个获得GM编号的接口**/	
      	String handler = "GM95";
      
  	    NewFeedbackQueueManager fq = NewFeedbackQueueManager.getInstance();
		NewFeedback nf = fq.getGuaQiFeedback(handler);
		List<GmTalk> talks = new ArrayList<GmTalk>();
		if(nf==null){
			nf = fq.getQueue().remove(0);
		}
		if(nf!=null){
			fq.setRecordid(nf.getId());
			talks = fq.getGmTalkByFId(nf.getId());
		}
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		for(GmTalk talk:talks){
			if(talk.getName()!=null&&talk.getSendDate()>0&&talk.getTalkcontent()!=null){
				if(!talk.getName().contains("GM")){
					sb.append(talk.getId()+"@@##$$<input type='hidden' id='"+talk.getId()+"' value='"+talk.getId()+"'><font color = 'blue'>"+talk.getName()+"(玩家)&nbsp;&nbsp;"+format.format(new Date(talk.getSendDate()))+"</font><br/>");
					sb.append("&nbsp;&nbsp;&nbsp;"+talk.getTalkcontent()+"<br/>@#$%^");
				}else{
					sb.append(talk.getId()+"@@##$$<input type='hidden' id='"+talk.getId()+"' value='"+talk.getId()+"'><font color = 'red'>"+talk.getName()+"(GM)&nbsp;&nbsp;"+format.format(new Date(talk.getSendDate()))+"</font><br/>");
					sb.append("&nbsp;&nbsp;&nbsp;"+talk.getTalkcontent()+"<br/>@#$%^");	
				}
			}
		}
		if(sb.length()==0){
			sb.append("正在加载中.....");
		}
      %>
      <textarea name="textarea" id="textarea" cols="75" rows="15">&nbsp;<%=sb.toString() %></textarea>
    </td>
  </tr>
  <tr>
    <td colspan="2" align="right" valign="middle"><p>
      <label for="textarea2"></label>
      <textarea name="textarea2" id="textarea2" cols="75" rows="6"></textarea>
      <input type="submit" name="button" id="button" value="回复玩家" />
    </p>
      <table width="100%"  border="1" align="center">
        <tr>
          <th bgcolor="yellow">GM编号</th>
          <th bgcolor="yellow">领取问题数</th>
          <th bgcolor="yellow">主动关闭数</th>
          <th bgcolor="yellow">挂起数</th>
          <th bgcolor="yellow">暂离次数</th>
          <th bgcolor="yellow">暂离总时长</th>
          <th bgcolor="yellow">满意</th>
          <th bgcolor="yellow">一般</th>
          <th bgcolor="yellow">不满意</th>
          <th bgcolor="yellow">交互条数</th>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
        </tr>
    </table></td>
  </tr>
</table></td>
  </tr>
</table>
</body>
</html>
