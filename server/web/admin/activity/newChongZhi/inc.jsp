<%@page import="com.xuanzhi.tools.mail.JavaMailUtils"%><%@page import="com.xuanzhi.tools.text.DateUtil"%><%@page import="java.util.ArrayList"%><%@page import="java.util.Date"%><%@page import="com.xuanzhi.boss.game.GameConstants"%><%!
public static void sendMail(String title, String content) {
	GameConstants gc = GameConstants.getInstance();
	StringBuffer sb = new StringBuffer();
	sb.append(content);
	sb.append("<HR>" + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
	ArrayList<String> args = new ArrayList<String>();
	args.add("-username");
	args.add("sqage_wx_jhm");
	args.add("-password");
	args.add("1qazxsw2");

	args.add("-smtp");
	args.add("smtp.163.com");
	args.add("-from");
	args.add("sqage_wx_jhm@163.com");
	args.add("-to");
	// TODO mailAddress 修改邮件
	String address_to = "";

	String[] addresses = {""};
	if (addresses != null) {
		for (String address : addresses) {
			address_to += address + ",";
		}
	}

	if (!"".equals(address_to)) {
		args.add(address_to);
		args.add("-subject");
		args.add(title);
		args.add("-message");
		args.add(sb.toString());
		args.add("-contenttype");
		args.add("text/html;charset=utf-8");
		try {
			JavaMailUtils.sendMail(args.toArray(new String[0]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
%>