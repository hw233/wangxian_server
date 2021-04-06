<%@ page contentType="text/html; charset=GBK" language="java" import="java.sql.*,java.util.*,java.io.*" %><%
String fileName = request.getParameter("fn");
String delId = request.getParameter("delId");
String pd = request.getParameter("pd");
boolean b =false;
ArrayList<String> links = new ArrayList<String>();
if (fileName!=null){
	String filePath = fileName;
	File file = new File(filePath);
	
	if (file.exists()){
		//读取文件
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String s = null;
		while((s = reader.readLine()) != null){
			if(s.trim().startsWith("#")) continue;
			s = s.trim();
			if(s.startsWith("BEGIN{") && s.endsWith("}END") && s.indexOf("=====") > 0){
				s = s.substring(6);
				s = s.substring(0,s.length()-4);
				links.add(s);
			}
		}
		
		//修改顺序或删除
		if (pd==null && delId!=null){
			//删除
			links.remove(Integer.parseInt(delId));
			b = true;
		}else{
			//修改顺序
			String[] getGrants = request.getParameterValues("grant");
			if (getGrants!=null && getGrants.length>0){
				ArrayList<String> temp = new ArrayList<String>(getGrants.length);
				for (int i=0;i<links.size();i++){
                    temp.add(links.get(i));
           		} 
				for (int i=0;i<links.size();i++){
					temp.set(Integer.parseInt(getGrants[i])-1,links.get(i));
				}
				links = temp;
				b = true;
			}
		}
		if (b){
			BufferedWriter writer = new BufferedWriter(new FileWriter(file,false));
			for (int j=0;j<links.size();j++){
				writer.write("BEGIN{"+links.get(j)+"}END\n");
			}
			writer.close();
		}
		links.clear();
		//读取文件
		reader = new BufferedReader(new FileReader(file));
		s = null;
		while((s = reader.readLine()) != null){
			if(s.trim().startsWith("#")) continue;
			s = s.trim();
			if(s.startsWith("BEGIN{") && s.endsWith("}END") && s.indexOf("=====") > 0){
				s = s.substring(6);
				s = s.substring(0,s.length()-4);
				links.add(s);
			}
		}
	}
	
}

%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link href="../css/ltcss.css" rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>Product</title></head>
<body>
<div align="center"><strong style="font-size:14px">快速查询链接修改</strong></div>
<hr />
<form id="form1" name="form1" method="post" action="txtEdit.jsp">
<input name="fn" type="hidden" value="<%=fileName %>" />
<input name="pd" type="hidden" value="edit" />
<table width="95%" border="1" align="center" cellpadding="0" cellspacing="1" bordercolor="#CCCCCC">
  <tr bgcolor="#BADAFE">
    <td width="30" height="25">序号</td>
    <td>优先级</td>
    <td>名称</td>
    <td>操作</td>
  </tr>
  <%
  if (links.size()>0){
  	for (int i=0;i<links.size();i++){
  		String line = links.get(i);
  		String s[] = line.split("=====");
  %>
  <tr onmousemove='bgColor="#FFD5BF"' onmouseout='bgColor="#DDEDFF"' bgcolor="#DDEDFF">
    <td height="20"><%=i+1 %></td>
    <td>
      <input name="grant" type="text" id="grant<%=i %>" size="10" value="<%=i+1 %>" />
    </td>
    <td><label>
      <input type="text" name="name" id="name<%=i %>" value="<%=s[0] %>" />
    </label></td>
    <td>
		<a href="txtEdit.jsp?delId=<%=i %>&fn=<%=fileName %>" onclick="return window.confirm('警告！！确定要删除吗？')">删除</a>	</td>
  </tr>
  <%}}else{%>
  <tr>
    <td height="20" colspan="4" align="center" bgcolor="#DDEDFF">此文件为空！</td>
  </tr>
  <%}%>
  <tr bgcolor="#BADAFE">
    <td height="25" colspan="4" align="center"><label>
      <input type="submit" name="button" id="button" value="-修改顺序-" />
    </label></td>
  </tr>
</table>
</form>
</body>
</html>
