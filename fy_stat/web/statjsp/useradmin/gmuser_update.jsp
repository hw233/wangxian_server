<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.xuanzhi.boss.gmuser.model.*"%>
<%@ include file="header.jsp"   %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>角色添加</title>
<link rel="stylesheet" href="css/style.css"/>
<script type="text/javascript">
   function sub(){
     var username = document.getElementById("username").value;
     var password = document.getElementById("password").value;
     var relname = document.getElementById("relname").value;
     if(username&&password&&relname){
       var f1 = document.getElementById("f1");
       f1.submit();
     }else{
      alert("值不能为空");
     }
   }
     function overTag(tag){
	  tag.style.color = "red";	
	  tag.style.backgroundColor = "lightcyan";
   }
			
   function outTag(tag){
	  tag.style.color = "black";
	  tag.style.backgroundColor = "white";
   }
</script>
</head>
<body bgcolor="#FFFFFF" >
	<h1 align="d">角色修改</h1>
	<%!
	   public List<String> getroles(Gmuser gmuser){
	       List<String> rs = new ArrayList<String>();
	       List<Authority> as = gmuser.getAuthos();
	       for(Authority a : as){
	          rs.add(a.getRoleid());
	       }   
	       return rs;
	   }
	 %>
	<%    
	       out.print("<input type='button' value='刷新' onclick='window.location.replace(\"gmuser_update.jsp\")' />");
	      ActionManager amanager = ActionManager.getInstance();
	      GmUserManager gmanager = GmUserManager.getInstance();
	      XmlRoleManager rmanager = XmlRoleManager.getInstance();
	      Set<String> gmnames = gmanager.getGmnames();
	      String update = request.getParameter("update");
	      String gidstr = request.getParameter("gid");
	      Gmuser gmuser=null;
	      if(gidstr!=null){
	       gmuser = gmanager.getGmuser(Integer.parseInt(gidstr));
	       gmnames.remove(gmuser.getGmname());
	       }
	      if(update!=null){
	         String username =request.getParameter("username");
	         String password =request.getParameter("password");
	         String relname = request.getParameter("relname");
	         String gmname = request.getParameter("gmname");
	         String rids[] = request.getParameterValues("rid");
	         List<Authority> as = new ArrayList<Authority>();
	         if(rids!=null){
	         for(String rid :rids){
	           Authority auth = new Authority();
	           auth.setRoleid(rid);
	           auth.setId(Integer.parseInt(gidstr));
	           as.add(auth);
	         }
	         }
	         gmuser.setUsername(username);
	         gmuser.setPassword(password);
	         gmuser.setRealname(relname);
	         gmuser.setGmname(gmname);
	         gmuser.setAuthos(as);
	         Map<String,String> props = gmuser.getProperties();
	         if(props != null) {
		         String keys[] = props.keySet().toArray(new String[0]);
		         for(String key:  keys) {
		         	String pvalue = request.getParameter("p" + key);
		         	if(pvalue != null) {
		         		gmuser.setProperty(key, pvalue);
		         	}
		         }
		     }
	         String propkey = request.getParameter("propkey");
	         if(propkey != null && propkey.length() > 0) {
	         	String propvalue = ParamUtils.getParameter(request,"propvalue","");
	         	gmuser.setProperty(propkey, propvalue);
	         	System.out.println("set property : key: "+propkey+", value: " + propvalue );
	         }
	         gmanager.updateGmuser(gmuser);
	         amanager.save(session.getAttribute("username").toString()," 更新了一GM账号:"+username);
	         response.sendRedirect("gmuser_list.jsp");
	      }
	      out.print("<form action='?update=true' method ='post' id ='f1'><table width='70%'><tr><th>用户名</th><td class='top'> ");
	      out.print("<input type='hidden' name='gid' id='gid' value='"+gmuser.getId()+"' />");
	      out.print("<input type='text' name='username' id='username' value='"+gmuser.getUsername()+"' /></td></tr>");
	      out.print("<tr><th>密码</th><td><input type='password' name='password' id='password' value='"+gmuser.getPassword()+"' /></td></tr>");
	      out.print("<tr><th>真实姓名</th><td><input type='text' name='relname' id='relname' value='"+gmuser.getRealname()+"' /></td></tr>");
	        out.print("<tr><th>gm角色名</th><td>请选择角色名<select id ='gmname' name='gmname' >");
	      for(int i=0;i<999;i++){
	        String gn = "GM"+(i>9?"":"0")+i;
	        if(!gmnames.contains(gn)){
	        	out.print("<option value='"+gn+"' ");
	        if(gmuser.getGmname()!=null&&gmuser.getGmname().equals(gn))
	       		out.print("selected");
	        out.print(">"+gn+"</option>");
	        }
	      }
	      out.print("</select></td></tr>");
	      out.print("<tr><th>拥有角色权限</th><td>");
	      List<XmlRole> roles = rmanager.getRoles();
	      List<String> rids = getroles(gmuser);
	      for(XmlRole role:roles){
	         out.print("<input type='checkbox' name='rid' value='"+role.getId()+"'");
	         if(rids.contains(role.getId()))
	         out.print("checked");             
	         out.print(" />"+role.getId()+"        "+role.getName()+"</br>");
	      }
	      out.print("</td></tr><tr><th>角色属性</th><td>");
	      Map<String,String> props = gmuser.getProperties();
	      if(props != null) {
		      String[] keys = props.keySet().toArray(new String[0]);
		      for(int i=0; i<keys.length; i++) {
		      	out.println(keys[i] + " : <input type=text size=20 name='p"+keys[i]+"' value='"+props.get(keys[i])+"'><br>");
		      }
		  }
	      out.print("key: <input type=text size=10 name='propkey'> value: <input type=text size=40 name='propvalue'>");
	      out.print("</td></tr>");
	      out.print("<tr><td colspan=2 ><input type='button' onclick='sub();' value='确认' />");
	      out.print("<input type='button' onclick='window.location.replace(\"gmuser_list.jsp\")' value='返回gm账户列表' /></td></tr></table></form>");	       
	    %>
	     
  
</body>
</html>
