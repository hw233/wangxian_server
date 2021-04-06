<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="org.apache.log4j.*,com.fy.engineserver.warehouse.service.*,com.fy.engineserver.warehouse.*,com.fy.engineserver.datasource.props.*,com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.mail.*,
com.fy.engineserver.mail.service.*,com.xuanzhi.tools.text.*,com.fy.engineserver.closebetatest.*" %>
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>自动发系统消息</title>
<script language="JavaScript">
</script>
</head>
<body>
<%
String action = request.getParameter("action");
String textarea1 = request.getParameter("textarea1");
String startTime = request.getParameter("startTime");
String endTime = request.getParameter("endTime");
String lastingTime = request.getParameter("lastingTime");
String danwei = request.getParameter("danwei");
if(action != null && action.equals("kaiqi")){
GMSendMessage gms = GMSendMessage.getInstance();
if(textarea1 != null){
	gms.message = textarea1;
}
if(startTime != null){
	gms.startTime = Integer.parseInt(startTime);
}
if(endTime != null){
	gms.endTime = Integer.parseInt(endTime);
}
if(lastingTime != null){
	int n = 60;
	if(danwei != null){
		n = Integer.parseInt(danwei);
	}
	gms.sleepingTime = Long.parseLong(lastingTime)*n*1000;
}
gms.start();
if(danwei != null){
	if(danwei.equals("60")){
		out.println("系统自动在"+startTime+"点到"+endTime+"点之间每隔"+lastingTime+"分钟发送消息：");
	}else{
		out.println("系统自动在"+startTime+"点到"+endTime+"点之间每隔"+lastingTime+"秒钟发送消息：");
	}
}

out.println(textarea1);
}else if(action != null && action.equals("guanbi")){
	GMSendMessage gms = GMSendMessage.getInstance();
	out.println("自动发系统消息停止");
	gms.stop();
}
%>
<form name="f1" action="" method="post">
<input type="hidden" name="action" value="kaiqi">
开始时间:<select id="startTime" name="startTime">
<option value="0" <%=(startTime != null && startTime.equals("0"))?"selected":"" %> >0点</option>
<option value="1" <%=(startTime != null && startTime.equals("1"))?"selected":"" %>>1点</option>
<option value="2" <%=(startTime != null && startTime.equals("2"))?"selected":"" %>>2点</option>
<option value="3" <%=(startTime != null && startTime.equals("3"))?"selected":"" %>>3点</option>
<option value="4" <%=(startTime != null && startTime.equals("4"))?"selected":"" %>>4点</option>
<option value="5" <%=(startTime != null && startTime.equals("5"))?"selected":"" %>>5点</option>
<option value="6" <%=(startTime != null && startTime.equals("6"))?"selected":"" %>>6点</option>
<option value="7" <%=(startTime != null && startTime.equals("7"))?"selected":"" %>>7点</option>
<option value="8" <%=(startTime != null && startTime.equals("8"))?"selected":"" %>>8点</option>
<option value="9" <%=(startTime != null && startTime.equals("9"))?"selected":"" %>>9点</option>
<option value="10" <%=(startTime != null && startTime.equals("10"))?"selected":"" %>>10点</option>
<option value="11" <%=(startTime != null && startTime.equals("11"))?"selected":"" %>>11点</option>
<option value="12" <%=(startTime != null && startTime.equals("12"))?"selected":"" %>>12点</option>
<option value="13" <%=(startTime != null && startTime.equals("13"))?"selected":"" %>>13点</option>
<option value="14" <%=(startTime != null && startTime.equals("14"))?"selected":"" %>>14点</option>
<option value="15" <%=(startTime != null && startTime.equals("15"))?"selected":"" %>>15点</option>
<option value="16" <%=(startTime != null && startTime.equals("16"))?"selected":"" %>>16点</option>
<option value="17" <%=(startTime != null && startTime.equals("17"))?"selected":"" %>>17点</option>
<option value="18" <%=(startTime != null && startTime.equals("18"))?"selected":"" %>>18点</option>
<option value="19" <%=(startTime != null && startTime.equals("19"))?"selected":"" %>>19点</option>
<option value="20" <%=(startTime != null && startTime.equals("20"))?"selected":"" %>>20点</option>
<option value="21" <%=(startTime != null && startTime.equals("21"))?"selected":"" %>>21点</option>
<option value="22" <%=(startTime != null && startTime.equals("22"))?"selected":"" %>>22点</option>
<option value="23" <%=(startTime != null && startTime.equals("23"))?"selected":"" %>>23点</option>
</select>&nbsp;
停止时间:<select id="endTime" name="endTime">
<option value="0" <%=(endTime != null && endTime.equals("0"))?"selected":"" %>>0点</option>
<option value="1" <%=(endTime != null && endTime.equals("1"))?"selected":"" %>>1点</option>
<option value="2" <%=(endTime != null && endTime.equals("2"))?"selected":"" %>>2点</option>
<option value="3" <%=(endTime != null && endTime.equals("3"))?"selected":"" %>>3点</option>
<option value="4" <%=(endTime != null && endTime.equals("4"))?"selected":"" %>>4点</option>
<option value="5" <%=(endTime != null && endTime.equals("5"))?"selected":"" %>>5点</option>
<option value="6" <%=(endTime != null && endTime.equals("6"))?"selected":"" %>>6点</option>
<option value="7" <%=(endTime != null && endTime.equals("7"))?"selected":"" %>>7点</option>
<option value="8" <%=(endTime != null && endTime.equals("8"))?"selected":"" %>>8点</option>
<option value="9" <%=(endTime != null && endTime.equals("9"))?"selected":"" %>>9点</option>
<option value="10" <%=(endTime != null && endTime.equals("10"))?"selected":"" %>>10点</option>
<option value="11" <%=(endTime != null && endTime.equals("11"))?"selected":"" %>>11点</option>
<option value="12" <%=(endTime != null && endTime.equals("12"))?"selected":"" %>>12点</option>
<option value="13" <%=(endTime != null && endTime.equals("13"))?"selected":"" %>>13点</option>
<option value="14" <%=(endTime != null && endTime.equals("14"))?"selected":"" %>>14点</option>
<option value="15" <%=(endTime != null && endTime.equals("15"))?"selected":"" %>>15点</option>
<option value="16" <%=(endTime != null && endTime.equals("16"))?"selected":"" %>>16点</option>
<option value="17" <%=(endTime != null && endTime.equals("17"))?"selected":"" %>>17点</option>
<option value="18" <%=(endTime != null && endTime.equals("18"))?"selected":"" %>>18点</option>
<option value="19" <%=(endTime != null && endTime.equals("19"))?"selected":"" %>>19点</option>
<option value="20" <%=(endTime != null && endTime.equals("20"))?"selected":"" %>>20点</option>
<option value="21" <%=(endTime != null && endTime.equals("21"))?"selected":"" %>>21点</option>
<option value="22" <%=(endTime != null && endTime.equals("22"))?"selected":"" %>>22点</option>
<option value="23" <%=(endTime != null && endTime.equals("23"))?"selected":"" %>>23点</option>
</select>&nbsp;
发送时间间隔:<select id="lastingTime" name="lastingTime">
<option value="1" <%=(lastingTime != null && lastingTime.equals("1"))?"selected":"" %>>1</option>
<option value="2" <%=(lastingTime != null && lastingTime.equals("2"))?"selected":"" %>>2</option>
<option value="3" <%=(lastingTime != null && lastingTime.equals("3"))?"selected":"" %>>3</option>
<option value="4" <%=(lastingTime != null && lastingTime.equals("4"))?"selected":"" %>>4</option>
<option value="5" <%=(lastingTime != null && lastingTime.equals("5"))?"selected":"" %>>5</option>
<option value="6" <%=(lastingTime != null && lastingTime.equals("6"))?"selected":"" %>>6</option>
<option value="7" <%=(lastingTime != null && lastingTime.equals("7"))?"selected":"" %>>7</option>
<option value="8" <%=(lastingTime != null && lastingTime.equals("8"))?"selected":"" %>>8</option>
<option value="9" <%=(lastingTime != null && lastingTime.equals("9"))?"selected":"" %>>9</option>
<option value="10" <%=(lastingTime != null && lastingTime.equals("10"))?"selected":"" %>>10</option>
<option value="11" <%=(lastingTime != null && lastingTime.equals("11"))?"selected":"" %>>11</option>
<option value="12" <%=(lastingTime != null && lastingTime.equals("12"))?"selected":"" %>>12</option>
<option value="13" <%=(lastingTime != null && lastingTime.equals("13"))?"selected":"" %>>13</option>
<option value="14" <%=(lastingTime != null && lastingTime.equals("14"))?"selected":"" %>>14</option>
<option value="15" <%=(lastingTime != null && lastingTime.equals("15"))?"selected":"" %>>15</option>
<option value="16" <%=(lastingTime != null && lastingTime.equals("16"))?"selected":"" %>>16</option>
<option value="17" <%=(lastingTime != null && lastingTime.equals("17"))?"selected":"" %>>17</option>
<option value="18" <%=(lastingTime != null && lastingTime.equals("18"))?"selected":"" %>>18</option>
<option value="19" <%=(lastingTime != null && lastingTime.equals("19"))?"selected":"" %>>19</option>
<option value="20" <%=(lastingTime != null && lastingTime.equals("20"))?"selected":"" %>>20</option>
<option value="21" <%=(lastingTime != null && lastingTime.equals("21"))?"selected":"" %>>21</option>
<option value="22" <%=(lastingTime != null && lastingTime.equals("22"))?"selected":"" %>>22</option>
<option value="23" <%=(lastingTime != null && lastingTime.equals("23"))?"selected":"" %>>23</option>
<option value="24" <%=(lastingTime != null && lastingTime.equals("24"))?"selected":"" %>>24</option>
<option value="25" <%=(lastingTime != null && lastingTime.equals("25"))?"selected":"" %>>25</option>
<option value="26" <%=(lastingTime != null && lastingTime.equals("26"))?"selected":"" %>>26</option>
<option value="27" <%=(lastingTime != null && lastingTime.equals("27"))?"selected":"" %>>27</option>
<option value="28" <%=(lastingTime != null && lastingTime.equals("28"))?"selected":"" %>>28</option>
<option value="29" <%=(lastingTime != null && lastingTime.equals("29"))?"selected":"" %>>29</option>
<option value="30" <%=(lastingTime != null && lastingTime.equals("30"))?"selected":"" %>>30</option>
<option value="31" <%=(lastingTime != null && lastingTime.equals("31"))?"selected":"" %>>31</option>
<option value="32" <%=(lastingTime != null && lastingTime.equals("32"))?"selected":"" %>>32</option>
<option value="33" <%=(lastingTime != null && lastingTime.equals("33"))?"selected":"" %>>33</option>
<option value="34" <%=(lastingTime != null && lastingTime.equals("34"))?"selected":"" %>>34</option>
<option value="35" <%=(lastingTime != null && lastingTime.equals("35"))?"selected":"" %>>35</option>
<option value="36" <%=(lastingTime != null && lastingTime.equals("36"))?"selected":"" %>>36</option>
<option value="37" <%=(lastingTime != null && lastingTime.equals("37"))?"selected":"" %>>37</option>
<option value="38" <%=(lastingTime != null && lastingTime.equals("38"))?"selected":"" %>>38</option>
<option value="39" <%=(lastingTime != null && lastingTime.equals("39"))?"selected":"" %>>39</option>
<option value="40" <%=(lastingTime != null && lastingTime.equals("40"))?"selected":"" %>>40</option>
<option value="41" <%=(lastingTime != null && lastingTime.equals("41"))?"selected":"" %>>41</option>
<option value="42" <%=(lastingTime != null && lastingTime.equals("42"))?"selected":"" %>>42</option>
<option value="43" <%=(lastingTime != null && lastingTime.equals("43"))?"selected":"" %>>43</option>
<option value="44" <%=(lastingTime != null && lastingTime.equals("44"))?"selected":"" %>>44</option>
<option value="45" <%=(lastingTime != null && lastingTime.equals("45"))?"selected":"" %>>45</option>
<option value="46" <%=(lastingTime != null && lastingTime.equals("46"))?"selected":"" %>>46</option>
<option value="47" <%=(lastingTime != null && lastingTime.equals("47"))?"selected":"" %>>47</option>
<option value="48" <%=(lastingTime != null && lastingTime.equals("48"))?"selected":"" %>>48</option>
<option value="49" <%=(lastingTime != null && lastingTime.equals("49"))?"selected":"" %>>49</option>
<option value="50" <%=(lastingTime != null && lastingTime.equals("50"))?"selected":"" %>>50</option>
<option value="51" <%=(lastingTime != null && lastingTime.equals("51"))?"selected":"" %>>51</option>
<option value="52" <%=(lastingTime != null && lastingTime.equals("52"))?"selected":"" %>>52</option>
<option value="53" <%=(lastingTime != null && lastingTime.equals("53"))?"selected":"" %>>53</option>
<option value="54" <%=(lastingTime != null && lastingTime.equals("54"))?"selected":"" %>>54</option>
<option value="55" <%=(lastingTime != null && lastingTime.equals("55"))?"selected":"" %>>55</option>
<option value="56" <%=(lastingTime != null && lastingTime.equals("56"))?"selected":"" %>>56</option>
<option value="57" <%=(lastingTime != null && lastingTime.equals("57"))?"selected":"" %>>57</option>
<option value="58" <%=(lastingTime != null && lastingTime.equals("58"))?"selected":"" %>>58</option>
<option value="59" <%=(lastingTime != null && lastingTime.equals("59"))?"selected":"" %>>59</option>
<option value="60" <%=(lastingTime != null && lastingTime.equals("60"))?"selected":"" %>>60</option>
<option value="61" <%=(lastingTime != null && lastingTime.equals("61"))?"selected":"" %>>61</option>
<option value="62" <%=(lastingTime != null && lastingTime.equals("62"))?"selected":"" %>>62</option>
<option value="63" <%=(lastingTime != null && lastingTime.equals("63"))?"selected":"" %>>63</option>
<option value="64" <%=(lastingTime != null && lastingTime.equals("64"))?"selected":"" %>>64</option>
<option value="65" <%=(lastingTime != null && lastingTime.equals("65"))?"selected":"" %>>65</option>
<option value="66" <%=(lastingTime != null && lastingTime.equals("66"))?"selected":"" %>>66</option>
<option value="67" <%=(lastingTime != null && lastingTime.equals("67"))?"selected":"" %>>67</option>
<option value="68" <%=(lastingTime != null && lastingTime.equals("68"))?"selected":"" %>>68</option>
<option value="69" <%=(lastingTime != null && lastingTime.equals("69"))?"selected":"" %>>69</option>
<option value="70" <%=(lastingTime != null && lastingTime.equals("70"))?"selected":"" %>>70</option>
<option value="71" <%=(lastingTime != null && lastingTime.equals("71"))?"selected":"" %>>71</option>
<option value="72" <%=(lastingTime != null && lastingTime.equals("72"))?"selected":"" %>>72</option>
<option value="73" <%=(lastingTime != null && lastingTime.equals("73"))?"selected":"" %>>73</option>
<option value="74" <%=(lastingTime != null && lastingTime.equals("74"))?"selected":"" %>>74</option>
<option value="75" <%=(lastingTime != null && lastingTime.equals("75"))?"selected":"" %>>75</option>
<option value="76" <%=(lastingTime != null && lastingTime.equals("76"))?"selected":"" %>>76</option>
<option value="77" <%=(lastingTime != null && lastingTime.equals("77"))?"selected":"" %>>77</option>
<option value="78" <%=(lastingTime != null && lastingTime.equals("78"))?"selected":"" %>>78</option>
<option value="79" <%=(lastingTime != null && lastingTime.equals("79"))?"selected":"" %>>79</option>
<option value="80" <%=(lastingTime != null && lastingTime.equals("80"))?"selected":"" %>>80</option>
<option value="81" <%=(lastingTime != null && lastingTime.equals("81"))?"selected":"" %>>81</option>
<option value="82" <%=(lastingTime != null && lastingTime.equals("82"))?"selected":"" %>>82</option>
<option value="83" <%=(lastingTime != null && lastingTime.equals("83"))?"selected":"" %>>83</option>
<option value="84" <%=(lastingTime != null && lastingTime.equals("84"))?"selected":"" %>>84</option>
<option value="85" <%=(lastingTime != null && lastingTime.equals("85"))?"selected":"" %>>85</option>
<option value="86" <%=(lastingTime != null && lastingTime.equals("86"))?"selected":"" %>>86</option>
<option value="87" <%=(lastingTime != null && lastingTime.equals("87"))?"selected":"" %>>87</option>
<option value="88" <%=(lastingTime != null && lastingTime.equals("88"))?"selected":"" %>>88</option>
<option value="89" <%=(lastingTime != null && lastingTime.equals("89"))?"selected":"" %>>89</option>
<option value="90" <%=(lastingTime != null && lastingTime.equals("90"))?"selected":"" %>>90</option>
<option value="91" <%=(lastingTime != null && lastingTime.equals("91"))?"selected":"" %>>91</option>
<option value="92" <%=(lastingTime != null && lastingTime.equals("92"))?"selected":"" %>>92</option>
<option value="93" <%=(lastingTime != null && lastingTime.equals("93"))?"selected":"" %>>93</option>
<option value="94" <%=(lastingTime != null && lastingTime.equals("94"))?"selected":"" %>>94</option>
<option value="95" <%=(lastingTime != null && lastingTime.equals("95"))?"selected":"" %>>95</option>
<option value="96" <%=(lastingTime != null && lastingTime.equals("96"))?"selected":"" %>>96</option>
<option value="97" <%=(lastingTime != null && lastingTime.equals("97"))?"selected":"" %>>97</option>
<option value="98" <%=(lastingTime != null && lastingTime.equals("98"))?"selected":"" %>>98</option>
<option value="99" <%=(lastingTime != null && lastingTime.equals("99"))?"selected":"" %>>99</option>
<option value="100" <%=(lastingTime != null && lastingTime.equals("100"))?"selected":"" %>>100</option>
<option value="101" <%=(lastingTime != null && lastingTime.equals("101"))?"selected":"" %>>101</option>
<option value="102" <%=(lastingTime != null && lastingTime.equals("102"))?"selected":"" %>>102</option>
<option value="103" <%=(lastingTime != null && lastingTime.equals("103"))?"selected":"" %>>103</option>
<option value="104" <%=(lastingTime != null && lastingTime.equals("104"))?"selected":"" %>>104</option>
<option value="105" <%=(lastingTime != null && lastingTime.equals("105"))?"selected":"" %>>105</option>
<option value="106" <%=(lastingTime != null && lastingTime.equals("106"))?"selected":"" %>>106</option>
<option value="107" <%=(lastingTime != null && lastingTime.equals("107"))?"selected":"" %>>107</option>
<option value="108" <%=(lastingTime != null && lastingTime.equals("108"))?"selected":"" %>>108</option>
<option value="109" <%=(lastingTime != null && lastingTime.equals("109"))?"selected":"" %>>109</option>
<option value="110" <%=(lastingTime != null && lastingTime.equals("110"))?"selected":"" %>>110</option>
<option value="111" <%=(lastingTime != null && lastingTime.equals("111"))?"selected":"" %>>111</option>
<option value="112" <%=(lastingTime != null && lastingTime.equals("112"))?"selected":"" %>>112</option>
<option value="113" <%=(lastingTime != null && lastingTime.equals("113"))?"selected":"" %>>113</option>
<option value="114" <%=(lastingTime != null && lastingTime.equals("114"))?"selected":"" %>>114</option>
<option value="115" <%=(lastingTime != null && lastingTime.equals("115"))?"selected":"" %>>115</option>
<option value="116" <%=(lastingTime != null && lastingTime.equals("116"))?"selected":"" %>>116</option>
<option value="117" <%=(lastingTime != null && lastingTime.equals("117"))?"selected":"" %>>117</option>
<option value="118" <%=(lastingTime != null && lastingTime.equals("118"))?"selected":"" %>>118</option>
<option value="119" <%=(lastingTime != null && lastingTime.equals("119"))?"selected":"" %>>119</option>
<option value="120" <%=(lastingTime != null && lastingTime.equals("120"))?"selected":"" %>>120</option>

</select>
<select name="danwei">
<option value="60" <%=(danwei != null && danwei.equals("60"))?"selected":"" %>>分</option>
<option value="1" <%=(danwei != null && danwei.equals("1"))?"selected":"" %>>秒</option>
</select>
<br>
<textarea style="width:600;height:200;" id="textarea1" name="textarea1" value="<%= textarea1== null? "":textarea1 %>"></textarea><br>
<input type="submit" name="submit1" value="开启并发送">
</form>
<form name="f2" action="" method="post">
<input type="hidden" name="action" value="guanbi">
<input type="submit" name="submit2" value="停止发送消息">
</form>
</body>
</html>
