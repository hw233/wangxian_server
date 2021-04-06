<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="java.sql.*,java.io.*,com.fy.engineserver.util.*,com.fy.engineserver.sprite.*,com.fy.engineserver.protobuf.*,com.fy.engineserver.datasource.props.*,com.xuanzhi.tools.dbpool.*" %>
<%
//从一台服务器转服到另一台服务器
String message = null;
String operation = request.getParameter("operation");
if(operation != null && operation.equals("start")) {
	long start = System.currentTimeMillis();
	HefuManager hm = new HefuManager();
	hm.setArticleMapFile(new File("/tmp/article.dat"));
	hm.setPlayerMapFile(new File("/home/game/import/hefuPlayer.data"));
	hm.initialize();
	PooledDataSource ds = new PooledDataSource();
    ds.setAlias("hefu");
    ds.setCheckoutTimeout(120);
    ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
    ds.setUrl("jdbc:oracle:thin:@117.135.139.52:1521:ora10g");
    ds.setUsername("qianlong48");
    ds.setPassword("oracle4qianlong48");
    ds.setMaxActive(100);
    ds.setIdleTimeout(120);
    ds.initialize();
    hm.setDataSource(ds);
    hm.dstServerName = "四服兄弟情深";
    int playerid = Integer.parseInt(request.getParameter("playerid"));
	try {
    	int newId = hm.getPlayerNewId(playerid);
    	//已经导入，那么删除重新导入
    	hm.playerMap.remove(playerid);
	} catch(Exception e) {
	}
    hm.logger.info("[从目标服务器导入玩家] [id:"+playerid+"]");
    Player hefuPlayer = getHefuPlayer(playerid, ds);
    HefuManager.ImportPlayerTask task = hm.new ImportPlayerTask(hefuPlayer, 0);
    task.run();
    int newId = hm.getPlayerNewId(playerid);
    Player newPlayer = PlayerManager.getInstance().getPlayer(newId);
    if(newPlayer != null) {
    	hm.refactPlayerRelationship(newPlayer);
    }
    message = "导入圆满完成！";
} 
%>
<%@include file="IPManager.jsp" %><html>
<script>
function start() {
	document.getElementById("operation").value="start";
	document.f1.submit();
}
</script>
<body>
 <h1>合服玩家导入修补</h1>
 <%if(message != null) {out.println("<h2 color=red>"+message+"</h2>");}%>
<form name=f1 action="hefu_player.jsp">
	原服玩家id:<input type=text size=10 name="playerid">
	<input type=button name="bt1" value="开始导入玩家" onclick="start()">
	<input type=hidden name=operation id=operation value="">
</form>
</body>
</html>
<%!
public Player getHefuPlayer(int oldPlayerId, PooledDataSource ds) {
	// TODO Auto-generated method stub
	Connection con = null;
	PreparedStatement pstmt = null;
	try {
		con = ds.getConnection();
		String sql = "select * from player where removed=0 and id=?";
		pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, oldPlayerId);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {					
			int pid = rs.getInt("id");
			String name = rs.getString("name");
			try {
				java.sql.Blob blob = rs.getBlob("bindata");
			    InputStream ins = blob.getBinaryStream();
			    ByteArrayOutputStream out = new ByteArrayOutputStream();
			    byte[] buffer = new byte[1024];
			    int n = 0;
			    while((n = ins.read(buffer)) != -1) {
			    	out.write(buffer,0,n);
			    }
			    ins.close();
			    out.close();
			    byte[] protobuf = out.toByteArray();
			    PlayerProtos.Player player = PlayerProtos.Player.parseFrom(protobuf);
				Player pp = ProtoPlayerWrapper.toRealPlayer(player);
				return pp;
			} catch(Exception e) {
				System.err.println("[加载合服玩家时出错] ["+pid+"] ["+name+"]");
			}
		}
	} catch(Exception e) {
		e.printStackTrace();
	} finally {
		try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
		try {con.close();} catch(Exception e) {e.printStackTrace();}
	}
	return null;
}
%>
