<%@ page contentType="text/html;charset=utf-8" 
import="java.io.*,java.util.*,com.xuanzhi.tools.text.*,
com.xuanzhi.tools.authorize.*"%><%
	AuthorizeManager am = AuthorizeManager.getInstance();	
	String errorMessage = null;
	String action = request.getParameter("action");
	boolean submitted = "true".equals(request.getParameter("submitted"));
	
	String name = request.getParameter("name");
	String p1 = request.getParameter("p1");
	String p2 = request.getParameter("p2");
	int expire = 0;
	try{
		expire = Integer.parseInt(request.getParameter("expire"));
	}catch(Exception e){}
	
	User user = am.getUserManager().getUser(name);
	
	if(submitted && action != null){
		if(user == null){
			errorMessage = "制定的用户不存在！";
		}else
		if(p1 != null && p2 != null){
			if(p1.equals(p2) == false){
				errorMessage = "两次输入的密码不匹配，请重新输入！";
			}else{
				
				char chars[] = p1.toCharArray();
				boolean includeNum = false;
				boolean includeLittleLetter = false;
				boolean includeBigLetter = false;
				for(int i = 0 ; i < chars.length ; i++){
					if(chars[i] >='0' && chars[i] <='9') includeNum = true;
					if(chars[i] >='a' && chars[i] <='z') includeLittleLetter = true;
					if(chars[i] >='A' && chars[i] <='Z') includeBigLetter = true;
				}
				//if(includeNum == false || includeLittleLetter == false || includeBigLetter == false || chars.length < 6){
				//	errorMessage = "密码长度至少是6位，并且必须同时含有小写字母，大写字母和数字，请重新输入！";
				//}
				//else
				{
					am.getUserManager().setUserPassword(user,p1,expire);
					am.saveTo(am.getConfigFile());
					if(user.getEmail() != null && user.getEmail().length() > 0){
						ArrayList<Platform> pal = new ArrayList<Platform>();
						Platform ps[] = am.getPlatformManager().getPlatforms();
						for(int i = 0 ;i < ps.length ; i++){
							//if(am.isPlatformAccessEnable(user,ps[i].getUrl(),"")){
								pal.add(ps[i]);	
							//}
						}
						StringBuffer sb = new StringBuffer();
						sb.append("<b>"+user.getRealName()+"</b>您好:<br>");
						sb.append("&nbsp;&nbsp;&nbsp;&nbsp;您在神奇时代以下业务系统中的用户<b>"+user.getName()+"</b>的密码被管理员重置为<i>"+p1+"</i>,有效期为"+expire+"天<br>");
						sb.append("&nbsp;&nbsp;&nbsp;&nbsp;这些系统包括：<br>");
						for(int i = 0 ;i < pal.size() ; i++){
							Platform p = pal.get(i);
							sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp<a href='"+p.getUrl()+"'>"+p.getName()+"</a><br>");
						}
						sb.append("&nbsp;&nbsp;&nbsp;&nbsp;要修改您自己的密码，请登录<a href='http://116.213.142.183:8882/game_gateway/admin/'>用户管理系统</a>，在《权限管理》-->《修改密码》中修改您的密码<br>");
						sb.append("<br><br><br>技术部");
						sb.append("<br>" + DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
						
						ArrayList<String> args = new ArrayList<String>();
						args.add("-username");
						args.add("sqagemail");
						args.add("-password");
						args.add("12qwaszx");
				
						args.add("-smtp");
						args.add("smtp.163.com");
						args.add("-from");
						args.add("sqagemail@163.com");
						args.add("-to");
						args.add(user.getEmail());
						args.add("-subject");
						args.add("用户重置密码通知邮件");
						args.add("-message");
						args.add(sb.toString());
						args.add("-contenttype");
						args.add("text/html;charset=gbk");
	
						JavaMailUtils.sendMail(args.toArray(new String[0]));
						
						errorMessage = "设置成功！并已经发出邮件["+user.getEmail()+"]给此用户！";
					}else{
						errorMessage = "设置成功！但没有发出邮件["+user.getEmail()+"]给此用户！";
					}
				}
			}
		}else{
			errorMessage = "两次输入的密码不完整，请重新输入！";
		}
	}
%><%@page import="com.xuanzhi.tools.mail.JavaMailUtils"%>
<html>
<head>
</head>
<body>
<%
	String submitName = "未知操作";
	if(action.equals("modify")){
		out.println("<h3>修改用户"+user.getRealName()+"的密码</h3>");
		submitName="修改密码";
	}else{
		out.println("<h3>不能识别的action["+action+"]</h3>");
	}
	if(errorMessage != null){
		%>
		<table border='0' cellpadding='0' cellspacing='3' width='100%' bgcolor='#FF0000' align='center'>
		<tr bgcolor='#FFFFFF' align='center'><td><font color='red'><%=errorMessage %></font></td></tr>
		</table><br/><br/>
		<%
	}
%><form id='two' name='two'><input type='hidden' name='submitted' value='true'>
<input type='hidden' name='action' value='<%=action %>'>
<input type='hidden' name='name' value='<%=name %>'>
<table border='0' width='100%' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>
<tr bgcolor='#00FFFF' align='center'><td>属性名称</td><td>属性值</td><td>属性说明</td></tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>新的密码</td>
<td><input type='password' id='p1' name='p1' size='40'  value=''></td>
<td>密码长度至少是6位，并且必须同时含有小写字母，大写字母和数字</td>
</tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>重新输入一次</td>
<td><input type='password' id='p2' name='p2' size='40'  value=''></td>
<td>密码长度至少是6位，并且必须同时含有小写字母，大写字母和数字</td>
</tr>

<tr bgcolor='#FFFFFF' align='center'>
<td>有效期</td>
<td><input type='text' id='expire' name='expire' size='40'  value='0'></td>
<td>单位为天</td>
</tr>

<tr bgcolor='#00FFFF' align='center'><td colspan=3><input type='submit' value='<%=submitName %>'></td></tr>
</table>
</form>
<center><a href='./users.jsp'>返回用户列表页面</a></center>
</body>
</html> 
