<%@page import="com.fy.engineserver.gm.custom.ModuleCheck"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%
  String result = "";
   try{
   String gmusername1 = session.getAttribute("username").toString();
   String url = request.getRequestURI();
   ModuleCheck mc = ModuleCheck.getInstance();
   
   result = mc.checkModule(gmusername1,url);
 
   if(result==null||"null".equals(result.trim())){
    out.print("对不起，你的权限不够！");
    return;
    }
    }catch(Exception e){
      out.print(StringUtil.getStackTrace(e));
      out.print("请登录后在访问！");
      return;
    }
%>
