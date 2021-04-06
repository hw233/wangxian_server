<%@page import="java.io.*,com.mieshi.korea.BoardItem"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
此页面是韩国服务器的公告板、活动通知的测试页面<br/>
---<a href="router.jsp?class=com.mieshi.korea.BoardDataProc&action=pullNewData">拉取新数据</a>
---<a href="router.jsp?class=com.mieshi.korea.BoardDataProc&action=signalNewData">发送更新信号</a>
<br/>
当前的新数据拉取地址是（程序拉取数据时会在末尾添加?servername=****）
<form action="router.jsp?class=com.mieshi.korea.BoardDataProc&action=setPullUrl" method="post">
<input type="text" name="newUrl" style='width:500px'  value="<%
out.println(com.mieshi.korea.BoardDataProc.pollDataUrl);
%>"/>
<input type="submit" name="" value="提交修改"/>
</form>
<br/>

<form action="router.jsp?class=com.mieshi.korea.BoardDataProc&action=vote" method="post">
投票地址<input type="text" name="voteUrl" style='width:500px'  value="<%out.println(com.mieshi.korea.BoardDataProc.voteUrl);
 %>"/><br/>
ID<input type="text" name="id" value=""/><br/>
选项(填1/2/3的某一个)<input type="text" name="which" value=""/><br/>
<input type="submit" name="" value="投票"/>
</form>
<%
//com.mieshi.korea.BoardManager.votedMap.clear();out.println("ok");
for(BoardItem bi : com.mieshi.korea.BoardManager.events){
out.println(bi.getId()+":"+bi.voteNormal+"-"+bi.voteGood+","+bi.voteBetter+"<br/>");
}

%>
</body>
</html>
