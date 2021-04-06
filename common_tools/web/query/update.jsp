<%@ page import="com.xuanzhi.tools.query.QueryItem" %>
<%@ page import="com.xuanzhi.tools.query.net.MobileServer" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.TreeSet" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>Update Mobile</title>
</head>
<%
    String sub = request.getParameter("sub");
    StringBuffer msg = new StringBuffer();
    StringBuffer msg1 = new StringBuffer();
    Set<String> keys = new TreeSet<String>();
    if("sub".equalsIgnoreCase(sub)){
        MobileServer ms = MobileServer.getInstance();
        String input = request.getParameter("input");
        long l = System.currentTimeMillis();
        int succ = 0;
        int total = 0;
        if(input != null){
            input = input.replaceAll(":"," ");
            String[] ss = input.split("\n");
            for(String s : ss){
                try{
                    QueryItem qi = new QueryItem(s.trim());
                    if(ms.updateMobile(qi)){
                        keys.add(qi.getKey().substring(0,3));
                        succ++;
                    }

                }catch(Exception e){
                }
                total++;
            }
        }
        msg.append("finished succ = "+succ+" total = "+total+" cost = "+(System.currentTimeMillis() - l)+" ms");
        for(String k : keys){
            if(ms.save(k,k)){
                msg1.append("[save] ["+k+"] [success]");
            }else{
                msg1.append("[save] ["+k+"] [failed]");
            }
            msg1.append("<br>");
        }
    }
%>
<body>
<a href="manager.jsp">Manager</a>
<br>
<br>
<br>
格式：手机号码:VIP|BLACK|WHITE|RED<br>e.g:13810000001:true|false|false|false
<form id="form1" name="form1" method="post" action="">
  <label>
  <textarea name="input" cols="100" rows="10"></textarea>
  </label>
  <br><br><br>
  <label>
  <input type="hidden" name="sub" value="sub"/>
  <input type="submit" name="Submit" value="提交" />
  </label>
</form>
<%=msg%><br>
<%=msg1%>
</body>
</html>
