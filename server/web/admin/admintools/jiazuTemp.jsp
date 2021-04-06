<%@page import="com.fy.engineserver.zongzu.manager.ZongPaiManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="java.util.Set"%>
<%@page import="com.fy.engineserver.jiazu.JiazuTitle"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.jiazu.JiazuMember"%>
<%@page import="com.fy.engineserver.jiazu2.manager.JiazuManager2"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	Jiazu jiazu = JiazuManager.getInstance().getJiazu(1114000000000000012L);
	JiazuManager jiazuManager = JiazuManager.getInstance();
	final long targetId = 1118000000001208679L;
	Player player = GamePlayerManager.getInstance().getPlayer(1118000000005203035L);
	JiazuMember member = jiazuManager.getJiazuMember(player.getId(), jiazu.getJiazuID());
	JiazuMember memberTarget = jiazuManager.getJiazuMember(targetId, jiazu.getJiazuID());
	if (player.getCountryPosition() == CountryManager.国王) {
		out.print(Translate.text_jiazu_119);
		return;
	}
	if (member == null) {
		out.print(player.getJiazuLogString() + "[族长禅让] [失败] [家族成员不存在] ");
		return;
	}
	if (member.getTitle() != JiazuTitle.master) {
		out.print(player.getJiazuLogString() + "[族长禅让] [失败] [不是族长] [{}] [jiazuName:{}] [jiazuID:{}] [title:{}] [{}ms]");
		return;
	} else {
		Set<JiazuMember> list = jiazuManager.getJiazuMember(jiazu.getJiazuID());
		if (list == null || list.size() == 0) {
			out.print("[族长禅让] [失败] [没有族长]");// [{}] [jiazuName:{}] [jiazuID:{}] [title:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), member.getTitle().toString(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			return;
		} else {
			JiazuMember viceMember = memberTarget;
			PlayerManager playerManager = PlayerManager.getInstance();
			try {
				Player vicePlayer = playerManager.getPlayer(viceMember.getPlayerID());
				jiazuManager.resignMaster(member, viceMember, jiazu.getJiazuID());

				vicePlayer.initJiazuTitleAndIcon();
				player.initJiazuTitleAndIcon();
				jiazu.setMemberModify(true);

				ZongPaiManager.getInstance().setPlayerAsZongPaiMaster(player, vicePlayer);
				out.print("操作成功");
			} catch (Exception e) {
			}

		}

	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>