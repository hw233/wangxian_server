<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%! public static final String[] VALID_IPS = new String[]{"119.57.32.10"}; %>
<%
boolean valid = true;
for(String ip : VALID_IPS){
	if(ip.equals(request.getRemoteAddr())){
		//valid = true;
	}
}
if(!valid){
	out.println("please send the message to admin");
	return;
}
%>
<html>
<head></head>
<body>
</body>
</html>