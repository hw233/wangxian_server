<%@ page contentType="text/html;charset=utf-8"%>
<%@page import="java.util.*,com.xuanzhi.tools.text.*,java.sql.*,com.xuanzhi.tools.dbpool.*" %>
<%
String content = request.getParameter("content");
String message = null;
if(content != null) {
	String str[] = content.split("\n");
	String usernames[] = new String[str.length];
	String times[] = new String[str.length];
	int count = 0;
	for(int i=0; i<str.length; i++) {
		String arr[] = str[i].split(" ");
		if(arr.length == 3) {
			usernames[i] = arr[1].trim();
			times[i] = arr[2].trim();
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				con = DataSourceManager.getInstance().getConnection();
				String sql = "update player set lastleveluptime=? where username=? and playerlevel=50";
				pstmt = con.prepareStatement(sql);
				pstmt.setTimestamp(1, new Timestamp(DateUtil.parseDate(times[i],"yyyyMMdd_HH:mm:ss").getTime()));
				pstmt.setString(2, usernames[i]);
				pstmt.executeUpdate();
				count++;
			} catch (Throwable e) { 
				e.printStackTrace();
			} finally {
				try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
				try {con.close();} catch(Exception e) {e.printStackTrace();}
			}
		}
	}
	message = "成功更新 " + count + " 个玩家的升级时间";
}
%>
<form name=f1 action="" method="post">
	<%if(message != null) {%>
	<font color="red"><%=message %></font><br><br>
	<%} %>
	格式: 服务器 账号 时间<br>
	<textarea name=content cols=50 rows=20></textarea>
	<br>
	<input type=submit name=sub1 value="提交">
</form>
