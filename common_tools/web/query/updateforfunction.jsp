<%@ page import="com.xuanzhi.tools.query.QueryItem" %>
<%@ page import="com.xuanzhi.tools.query.net.MobileServer" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.xuanzhi.tools.query.ByteUtil" %>
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
    String utype = request.getParameter("usertype");
    String ty = request.getParameter("ty");
    List<String> faileds = new ArrayList<String>();
    Set<String> keys = new TreeSet<String>();
    if("sub".equalsIgnoreCase(sub)){
        MobileServer ms = MobileServer.getInstance();
        String input = request.getParameter("input");
        String reason = request.getParameter("reason");
        if(reason == null){
            reason = "default reason";
        }
        if(input != null){
			input = input.replaceAll(",","\n");
		}
        long l = System.currentTimeMillis();
        int succ = 0;
        int total = 0;
        if(input != null){
            String[] ss = input.split("\n");
            for(String s : ss){
                byte ret = 0;
                try{
                    ret = ms.query(s);
                    if("VIP".equalsIgnoreCase(utype)){
                        if("add".equalsIgnoreCase(ty)){
                            ret = (byte) (ret | 8);
                        }else{
                            ret = (byte) (ret & 7);
                        }
                    }else if("BLACK".equalsIgnoreCase(utype)){
                        if("add".equalsIgnoreCase(ty)){
                            ret = (byte) (ret | 4);
                        }else{
                            ret = (byte) (ret & 11);
                        }
                    }else if("WHITE".equalsIgnoreCase(utype)){
                        if("add".equalsIgnoreCase(ty)){
                            ret = (byte) (ret | 2);
                        }else{
                            ret = (byte) (ret & 13);
                        }
                    }else{
                        if("add".equalsIgnoreCase(ty)){
                            ret = (byte) (ret | 1);
                        }else{
                            ret = (byte) (ret & 14);
                        }
                    }
                    QueryItem qi = new QueryItem(s.trim(), ByteUtil.getPropsFromByte(ret,4));
                    if(ms.updateMobile(qi,reason)){
                        keys.add(qi.getKey().substring(0,3));
                        succ++;
                    }else{
                        faileds.add(s);
                    }

                }catch(Exception e){
                }
                total++;
            }
        }
        msg.append(utype);
        msg.append(" ");
        msg.append(ty);
        msg.append(" finished succ = "+succ+" total = "+total+" cost = "+(System.currentTimeMillis() - l)+" ms");
        msg.append("<br>");
        msg.append("failed Number:");
        msg.append("<br>");
        for(String ts : faileds){
            msg.append(ts);
            msg.append("<br>");
        }
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
<body><br><%=msg%><br>
<%if(!"sub".equalsIgnoreCase(sub)){%>
<form id="form1" name="form1" method="post" action="">
  <label>
  <textarea name="input" cols="100" rows="10"></textarea>
  </label>
  <br><br>
  <label>
  <select name="usertype">
    <option value="VIP" selected="selected">VIP</option>
    <option value="BLACK">黑名单</option>
    <option value="WHITE">白名单</option>
  </select>
  </label>
  <br>
  <label>
  <select name="ty">
    <option value="add" selected="selected">添加</option>
    <option value="del">删除</option>
  </select>
  </label>
  <label>
  <input type="hidden" name="sub" value="sub"/>
  <input type="submit" name="Submit" value="提交" />
  </label>
</form>
<br>
<%}%>
<%=msg1%>
</body>
</html>
