<%@ page import="com.xuanzhi.tools.query.net.MobileServer" %>
<%@ page import="com.xuanzhi.tools.query.net.MobileClient" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>Query Mobile</title>
</head>
<%
    String sub = request.getParameter("sub");
    StringBuffer msg = new StringBuffer();
    StringBuffer outl = new StringBuffer();
    Map<String,MobileClient.STATUS> map = new HashMap<String,MobileClient.STATUS>();
    if("sub".equalsIgnoreCase(sub)){
        MobileServer ms = MobileServer.getInstance();
        String input = request.getParameter("input");
        long l = System.currentTimeMillis();
        int succ = 0;
        int total = 0;
        if(input != null){
            String[] ss = input.split("\n");
            for(String s : ss){
                try{
                    byte b = ms.query(s.trim());
                    MobileClient.STATUS st = new MobileClient.STATUS(b);
                    if(st != null){
                        succ++;
                        map.put(s,st);
                    }

                }catch(Exception e){
                }
                total++;
            }
            %>
<script language="javascript">
    form1.input.value = '<%=input%>'
</script>
        <%

            for(String key : map.keySet()){
                outl.append(key);
                outl.append(" ");
                outl.append(map.get(key).toString());
                outl.append("<br>");
            }
        }
        msg.append("finished succ = "+succ+" total = "+total+" cost = "+(System.currentTimeMillis() - l)+" ms");
    }
%>
<body>
<a href="manager.jsp">Manager</a>
<br>
<br>
<form id="form1" name="form1" method="post" action="">
  <table width="96%" border="0">
    <tr>
      <td width="52%"><label>
        <textarea name="input" cols="50" rows="10"></textarea>
      </label></td>
      <td width="48%"><label><%=outl.toString()%></label></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
  </table>
  <label></label>
  <br><br><br>
  <label>
  <input type="hidden" name="sub" value="sub"/>
  <input type="submit" name="Submit" value="提交" />
  </label>
</form>
<%=msg%>
</body>
</html>
