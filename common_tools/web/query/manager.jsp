<%@ page import="com.xuanzhi.tools.query.net.MobileServer" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>Manager</title>
</head>
<%
    MobileServer ms = MobileServer.getInstance();
    List<String> list = ms.getMobilePortList();
    StringBuffer sb = new StringBuffer();

    String sub = request.getParameter("sub");
    if("sub".equalsIgnoreCase(sub)){
        String action = request.getParameter("action");
        String confkey = request.getParameter("confkey");
        String key = request.getParameter("key");
        String filename = request.getParameter("filename");
        if("save".equalsIgnoreCase(action)){
            if(ms.save(key,confkey)){
                sb.append("save success");
            }else{
                sb.append("save failed");
            }
        }else if("saveto".equalsIgnoreCase(action)){
            if(ms.saveTo(filename,key)){
                sb.append("saveto success");
            }else{
                sb.append("saveto failed");
            }
        }else if("reload".equalsIgnoreCase(action)){
            if(ms.reload(confkey,key)){
                sb.append("reload success");
            }else{
                sb.append("reload failed");
            }
        }else if("reloadFrom".equalsIgnoreCase(action)){
            if(ms.loadFrom(filename,key)){
                sb.append("reloadFrom success");
            }else{
                sb.append("reloadFrom failed");
            }
        }else if("reloadAll".equalsIgnoreCase(action)){
            if(ms.reloadAll()){
                sb.append("reloadAll success");
            }else{
                sb.append("reloadAll failed");
            }
        }
    }
%>
<body>
<a href="update.jsp">update</a>
<br>
<br>
<br>
<p>所有配置文件对应的key</p>
<%
    for(String confkey : list){
        %>
    <%=confkey%><br>
<%
    }
%>
<form id="form1" name="form1" method="post" action="">
  <label>
  confkey:<input type="text" name="confkey" /><br>
  key:<input type="text" name="key" /> <br>
  fileName: <input type="text" name="filename" /> <br>
  </label>
  <p>action:
    <label>
    <select name="action" id="action">
      <option value="save">save</option>
      <option value="saveto">saveTo</option>
      <option value="reload">reload</option>
      <option value="reloadFrom">reloadFrom</option>
      <option value="reloadAll">reloadAll</option>
    </select>
    </label>
  </p>
  <p>
    <label>
    <input type="hidden" name="sub" value="sub"/>
    <input type="submit" name="Submit" value="提交" />
    </label>
  </p>
</form>
<p><%=sb.toString()%></p>
</body>
</html>
