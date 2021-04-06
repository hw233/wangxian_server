<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.xuanzhi.tools.authorize.User"%>
<%@page import="com.xuanzhi.tools.authorize.AuthorizeManager"%>
<%@page import="com.xuanzhi.tools.mail.JavaMailUtils"%>
<%@page import="java.util.Date"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="java.util.ArrayList"%>
<%!public void sendMail(String title, String content,HttpServletRequest request) {
		GameConstants gc = GameConstants.getInstance();
		AuthorizeManager am = AuthorizeManager.getInstance();
		User user = am.getUserManager().getUser(String.valueOf(request.getSession().getAttribute("authorize.username")));
		content += "[发送者:" + user.getRealName() + "]";
		StringBuffer sb = new StringBuffer();
		sb.append(content);
		sb.append("<br>" + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));

		ArrayList<String> args = new ArrayList<String>();
		args.add("-username");
		args.add("wtx062@126.com");
		args.add("-password");
		args.add("wangtianxin1986");

		args.add("-smtp");
		args.add("smtp.126.com");
		args.add("-from");
		args.add("wtx062@126.com");
		args.add("-to");
		args.add("3472335707@qq.com" + "," + user.getEmail());
		args.add("-subject");
		args.add("[飘渺寻仙曲]" + title + "[" + user.getRealName() + "] ["+gc.getServerName()+"]");
		args.add("-message");
		args.add(sb.toString());
		args.add("-contenttype");
		args.add("text/html;charset=gbk");
		try {
			JavaMailUtils.sendMail(args.toArray(new String[0]));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}%>