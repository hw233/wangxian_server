<%@page
        import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page
        import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page
        import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page
        import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page
        import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
        String playerName = request.getParameter("playerName");
        out.print("playerName:" + playerName);
        out.print("<HR/>");
        if (playerName == null) {
                playerName = "";
        } else {
                Player player = GamePlayerManager.getInstance().getPlayer(playerName);
                if (player == null) {
                        out.print("<H2>输入的角色不存在:" + playerName + "</H2>");
                } else {
                        if (GamePlayerManager.getInstance().isOnline(player.getId())) {
                                out.print("<H2>输入的角色在线:" + playerName + "</H2>");
                        } else {
                                Knapsack common = player.getKnapsack_common();
                                boolean modified = check(common, "包裹",player,out);
                                if (modified) {
                                        player.setDirty(true,"knapsacks_common");
                                }
                                out.print("<HR/>");
                                Knapsack cangku = player.getKnapsacks_cangku();
                                modified = check(cangku, "仓库",player,out);
                                if (modified) {
                                        player.setDirty(true,"knapsacks_cangku");
                                }
                                out.print("<HR/>");
                                Knapsack fanngbao = player.getKnapsacks_fangBao();
                                modified = check(fanngbao, "防爆包",player,out);
                                if (modified) {
                                        player.setDirty(true,"knapsacks_fangBao");
                                }
                                out.print("<HR/>");
                                Knapsack knapsacks_QiLing = player.getKnapsacks_QiLing();
                                modified = check(knapsacks_QiLing, "器灵背包",player,out);
                                if (modified) {
                                        player.setDirty(true,"knapsacks_QiLing");
                                }
                                out.print("<HR/>");
                        }
                }
        }
%>
<%!public boolean check(Knapsack knapsack, String name,Player player,JspWriter out) {
                StringBuffer sbf = new StringBuffer(player.getLogString() + "[" + name + "]");
                Cell[] cells = knapsack.getCells();
                boolean modified = false;
                for (int i = 0; i < cells.length; i++) {
                        Cell cell = cells[i];
                        long entityId = cell.getEntityId();
                        if (entityId > 0) {
                                ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(entityId);
                                if (ae == null) {
                                        sbf.append("[发现不存在的物品] [aeId:" + entityId + "][数量:" + cell.getCount() + "]");
                                        cell.count = 0;
                                        cell.entityId = -1;
                                        modified = true;
                                }
                        }
                }
                System.out.println(sbf.toString());
               try {
                out.print(sbf.toString() + "<BR/>");
               } catch (Exception e) {
            	   
               }
                return modified;
        }%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
        <form action="" method="post">
                角色名<input type="text" name="playerName" value="<%=playerName%>">
                <input type="submit" value="查询">
        </form>
</body>
</html>