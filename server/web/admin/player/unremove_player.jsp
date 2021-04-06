<%@ page contentType="text/html;charset=utf-8"%>
<%@page import="com.fy.engineserver.sprite.*,java.util.*,com.xuanzhi.tools.text.*,java.sql.*,com.xuanzhi.tools.dbpool.*" %>
<%!
	public List<Player> getOriginalPlayers(String username) {
		long now = System.currentTimeMillis();
		List<Player> plist = new ArrayList<Player>();
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select * from player where username=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int career = rs.getInt("career");
				int level = rs.getInt("playerlevel");
				int status = rs.getInt("removed");
				Player player = new Player();
				player.setId(id);
				player.setUsername(username);
				player.setName(name);
				player.setCareer((byte)career);
				player.setLevel((short)level);
				player.setAlive(status==0?true:false);
				plist.add(player);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return plist;
	}
	
	public void unremovePlayer(int id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "update player set removed=0 where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
	}
	
	public Connection getConnection() throws Exception {
		return DataSourceManager.getInstance().getConnection();
	}
%>
<%
String message = null;
String username = request.getParameter("username"); 
List<Player> plist = null;
if(username != null) {
	plist = getOriginalPlayers(username);
	message = "共找到用户" + username + "的角色共" + plist.size() + "个:";
}
String pid = request.getParameter("recoverid");
if(pid != null) {
	unremovePlayer(Integer.parseInt(pid));
	message = "成功恢复了id为" + pid + "的角色";
}
 %>
<form name=f1 action="" method="post">
	<%if(message != null) {%>
	<font color="red"><%=message %></font><br><br>
	<%} %>
	<br>
	用户名:<input type=text name=username size=10>
	<br>
	<%if(plist != null) {%>
	<table name=tb1 width=60% cellspacing=1 cellpadding=2 border=0 bgcolor="#000000">
		<tr>
			<td bgcolor="#cccccc">
				ID
			</td>
			<td bgcolor="#cccccc">
				角色名
			</td>
			<td bgcolor="#cccccc">
				用户名
			</td>
			<td bgcolor="#cccccc">
				目前等级
			</td>
			<td bgcolor="#cccccc">
				目前状态
			</td>
		</tr>
		<%for(Player p : plist) {%>
		<tr>
			<td bgcolor="#ffffff">
				<%=p.getId() %>
			</td>
			<td bgcolor="#ffffff">
				<%=p.getName() %>
			</td>
			<td bgcolor="#ffffff">
				<%=p.getUsername() %>
			</td>
			<td bgcolor="#ffffff">
				<%=p.getLevel() %>
			</td>
			<td bgcolor="#ffffff">
				<%=p.isAlive()?"正常":"已删除" %>&nbsp;&nbsp;&nbsp;&nbsp;
				<%if(!p.isAlive()) {%>
					<a href="unremove_player.jsp?recoverid=<%=p.getId() %>"> 恢 复 </a>
				<%} %>
			</td>
		</tr>
		<%} %>
	</table>
	<%} %>
	<input type=submit name=sub1 value="提交">
</form>
