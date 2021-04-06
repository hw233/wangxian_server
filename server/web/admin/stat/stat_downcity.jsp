<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*,
com.xuanzhi.tools.cache.diskcache.*,com.xuanzhi.tools.cache.diskcache.concrete.*,
com.fy.engineserver.downcity.*,com.fy.engineserver.downcity.stat.*"%><%


DownCityScheduleManager mmmm = DownCityScheduleManager.getInstance();
DefaultDiskCache ddc = mmmm.getDdc();
String downcityinfoid = request.getParameter("downcityinfoid");
String month = request.getParameter("month");
String day = request.getParameter("day");
ShopManager sm = ShopManager.getInstance();
if(downcityinfoid == null){
	downcityinfoid = "请选择";
}
if(month == null){
	month = "-1";
}
if(day == null){
	day = "-1";
}
ArrayList<String> idList = new ArrayList<String>();
if("-1".equals(downcityinfoid)){
	idList = (ArrayList)ddc.get(DownCityScheduleManager.DID);
}else{
	ArrayList<String> listTemp = (ArrayList)ddc.get(DownCityScheduleManager.DID);
	for(String str : listTemp){
		if(str != null && str.indexOf(downcityinfoid) == 0){
			idList.add(str);
		}
	}
}

if("-1".equals(month)){

}else{
	ArrayList<String> listTemp = new ArrayList<String>();
	for(String str : idList){
		if(str != null){
			String strTemp = str.substring(2);
			if(strTemp != null && strTemp.indexOf(month) == 0){
				listTemp.add(str);
			}
		}
	}
	idList = listTemp;
}

if("-1".equals(day)){

}else{
	ArrayList<String> listTemp = new ArrayList<String>();
	for(String str : idList){
		if(str != null){
			String strTemp = str.substring(4);
			if(strTemp != null && strTemp.indexOf(day) == 0){
				listTemp.add(str);
			}
		}
	}
	idList = listTemp;
}

DownCityManager dcm = DownCityManager.getInstance();
List<DownCityInfo> dciList = dcm.getDciList();

int npage = 0;
int size = 100;

npage = ParamUtils.getIntParameter(request,"npage",0);
size = ParamUtils.getIntParameter(request,"size",100);

ArrayList<DownCitySchedule> al = new ArrayList<DownCitySchedule>();

for(int i = npage * size ; i < (npage+1)*size ; i++){
	if(i >= idList.size()) break;
	String downcityId = idList.get(i);
	DownCitySchedule dcs = (DownCitySchedule)ddc.get(downcityId);
	if(dcs != null)
		al.add(dcs);
}

%>
<%@page import="com.fy.engineserver.gateway.GameNetworkFramework"%>

<%@page import="com.fy.engineserver.shop.ShopManager"%><%@include file="../IPManager.jsp" %><html><head>
<link rel="stylesheet" type="text/css" href="../../css/common.css">
<script type="text/javascript">
function selectDownCity(){
	document.f1.submit();
}
</script>
</HEAD>
<BODY>
<h2>副本统计情况</h2><br><h1>内存使用情况:<%=ddc.getCurrentMemorySize() %>&nbsp;disksize:<%=ddc.getCurrentDiskSize() %>&nbsp;元素:<%=ddc.getNumElements() %></h1>
<form name="f1" id="f1">
<select name="downcityinfoid" id="downcityinfoid" onchange="javascript:selectDownCity();">
<option value="请选择" <%=("请选择".equals(downcityinfoid)? "selected":"") %>>请选择
<option value="-1" <%=("-1".equals(downcityinfoid)? "selected":"") %>>全部
<%
if(dciList != null){
	for(DownCityInfo dci : dciList){
		if(dci != null){
			out.println("<option value='"+dci.getSeqNum()+"' "+(dci.getSeqNum().equals(downcityinfoid)? "selected":"")+">"+dci.getName());
		}
	}
}
%>
</select>&nbsp;月份
<select name="month" id="month" onchange="javascript:selectDownCity();">
<option value="-1" <%=("-1".equals(month)? "selected":"") %>>全部
<option value="01" <%=("01".equals(month)? "selected":"") %>>1月
<option value="02" <%=("02".equals(month)? "selected":"") %>>2月
<option value="03" <%=("03".equals(month)? "selected":"") %>>3月
<option value="04" <%=("04".equals(month)? "selected":"") %>>4月
<option value="05" <%=("05".equals(month)? "selected":"") %>>5月
<option value="06" <%=("06".equals(month)? "selected":"") %>>6月
<option value="07" <%=("07".equals(month)? "selected":"") %>>7月
<option value="08" <%=("08".equals(month)? "selected":"") %>>8月
<option value="09" <%=("09".equals(month)? "selected":"") %>>9月
<option value="10" <%=("10".equals(month)? "selected":"") %>>10月
<option value="11" <%=("11".equals(month)? "selected":"") %>>11月
<option value="12" <%=("12".equals(month)? "selected":"") %>>12月
</select>&nbsp;日期
<select name="day" id="day" onchange="javascript:selectDownCity();">
<option value="-1" <%=("-1".equals(day)? "selected":"") %>>全部
<option value="01" <%=("01".equals(day)? "selected":"") %>>1日
<option value="02" <%=("02".equals(day)? "selected":"") %>>2日
<option value="03" <%=("03".equals(day)? "selected":"") %>>3日
<option value="04" <%=("04".equals(day)? "selected":"") %>>4日
<option value="05" <%=("05".equals(day)? "selected":"") %>>5日
<option value="06" <%=("06".equals(day)? "selected":"") %>>6日
<option value="07" <%=("07".equals(day)? "selected":"") %>>7日
<option value="08" <%=("08".equals(day)? "selected":"") %>>8日
<option value="09" <%=("09".equals(day)? "selected":"") %>>9日
<option value="10" <%=("10".equals(day)? "selected":"") %>>10日
<option value="11" <%=("11".equals(day)? "selected":"") %>>11日
<option value="12" <%=("12".equals(day)? "selected":"") %>>12日
<option value="13" <%=("13".equals(day)? "selected":"") %>>13日
<option value="14" <%=("14".equals(day)? "selected":"") %>>14日
<option value="15" <%=("15".equals(day)? "selected":"") %>>15日
<option value="16" <%=("16".equals(day)? "selected":"") %>>16日
<option value="17" <%=("17".equals(day)? "selected":"") %>>17日
<option value="18" <%=("18".equals(day)? "selected":"") %>>18日
<option value="19" <%=("19".equals(day)? "selected":"") %>>19日
<option value="20" <%=("20".equals(day)? "selected":"") %>>20日
<option value="21" <%=("21".equals(day)? "selected":"") %>>21日
<option value="22" <%=("22".equals(day)? "selected":"") %>>22日
<option value="23" <%=("23".equals(day)? "selected":"") %>>23日
<option value="24" <%=("24".equals(day)? "selected":"") %>>24日
<option value="25" <%=("25".equals(day)? "selected":"") %>>25日
<option value="26" <%=("26".equals(day)? "selected":"") %>>26日
<option value="27" <%=("27".equals(day)? "selected":"") %>>27日
<option value="28" <%=("28".equals(day)? "selected":"") %>>28日
<option value="29" <%=("29".equals(day)? "selected":"") %>>29日
<option value="30" <%=("30".equals(day)? "selected":"") %>>30日
<option value="31" <%=("31".equals(day)? "selected":"") %>>31日
</select>
</form>
<table border='0' cellpadding='0' cellspacing='0' width='100%' bgcolor='#FFFFFF' align='center'>
<tr>
<%
	int tt = idList.size()/size;
	if(tt * size < idList.size()) tt++;
	for(int i = 1 ; i <= tt ; i++){
		if(i-1 == npage){
			out.println("第"+i+"页");
		}else{
			out.println("<a href='./stat_downcity.jsp?npage="+(i-1)+"&size="+size+"&downcityinfoid="+downcityinfoid+"&month="+month+"&day="+day+"'>"+i+"</a>");
		}
	}
%>
</tr>
</table>
<table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>
<tr align='center' bgcolor='#FFFFFF'>
<td>编号</td>
<td>副本ID</td>
<td>副本名</td>
<td>开始时间</td>
<td>通过时间</td>
<td>人数</td>
<td>平均等级</td>
<td>平均外功</td>
<td>平均内功</td>
<td>平均暴击率</td>
<td>平均闪避率</td>
<td>平均物理减伤</td>
<td>平均法术减伤</td>
<td>平均韧性</td>
<td>死亡次数</td>
<td>缓回血</td>
<td>缓回血</td>
<td>瞬回血</td>
<td>瞬回蓝</td>
<td>受到的伤害</td>
<td>受到的治疗</td>
<td>伤害输出</td>
<td>治疗输出</td>
<td>修装花费</td>
<td>掉落的金币</td>

<td>副本消耗</td>
<td>副本掉落</td>
</tr>
<%
for(int i = 0 ; i < al.size() ; i++){
	DownCitySchedule dcs = al.get(i);
	
	HashMap<Long,DownCityPlayerInfo> map = dcs.getDownCityPlayerInfos();
	DownCityPlayerInfo ps[] = map.values().toArray(new DownCityPlayerInfo[0]);
	int 死亡次数 = 0;
	int 缓回血= 0;
	int 缓回蓝= 0;
	int 瞬回血= 0;
	int 瞬回蓝= 0;
	int 受到的伤害= 0;
	int 受到的治疗= 0;
	int 伤害输出= 0;
	int 治疗输出= 0;
	int 修装花费= 0;
	int 人数 = ps.length;
	
	int 平均等级= 0;
	int 平均外功= 0;
	int 平均内功= 0;
	int 平均暴击率= 0;
	int 平均闪避率= 0;
	int 平均物理减伤= 0;
	int 平均法术减伤= 0;
	int 平均韧性= 0;
	
	for(int j = 0 ; j < ps.length ; j++){
		死亡次数 += ps[j].getDeadCount();
		缓回血 += ps[j].getHuanhuiHp();
		缓回蓝 += ps[j].getHuanhuiMp();
		瞬回血 += ps[j].getShunhuiHp();
		瞬回蓝 += ps[j].getShunhuiMp();
		受到的伤害 += ps[j].getReceiveDamage();
		受到的治疗 += ps[j].getReceiveZhiliao();
		伤害输出 += ps[j].getShanghaiShuChu();
		治疗输出 += ps[j].getZhiliaoShuChu();
		修装花费 += ps[j].getMoneyForNaijiu();
		
		平均等级 += ps[j].getLevel();
		平均外功 += ps[j].getMeleeAttackIntensity();
		平均内功 += ps[j].getSpellAttackIntensity();
		平均暴击率 += ps[j].getCriticalHit();
		平均闪避率 += ps[j].getDefence();
		平均物理减伤 += ps[j].getDodge();
		平均法术减伤 += ps[j].getResistance();
		平均韧性 += ps[j].getToughness();
		
	}
	
	if(ps.length > 0){
		平均等级 /= ps.length;
		平均外功 /= ps.length;
		平均内功 /= ps.length;
		平均暴击率 /= ps.length;
		平均闪避率 /= ps.length;
		平均物理减伤 /= ps.length;
		平均法术减伤 /= ps.length;
		平均韧性 /= ps.length;
	}
	
	int 掉落的金币= 0;
	DownCityOutputInfo ds[] = dcs.getDownCityOutputInfos().toArray(new DownCityOutputInfo[0]);
	ArrayList<String> 掉落集合 = new ArrayList<String>();
	for(int j = 0 ; j < ds.length ; j++){
		掉落的金币 += ds[j].getFlopMoney();
		String ss[] = ds[j].getPropName().split(",");
		for(int k = 0 ; k < ss.length ; k++){
			if(ss[k].trim().length() > 0){
				掉落集合.add(ss[k]);
			}
		}
	}
	
	ArrayList<String> 消耗集合 = new ArrayList<String>();
	DownCityConsumeInfo cs[] = dcs.getDownCityConsumeInfos().toArray(new DownCityConsumeInfo[0]);
	for(int j = 0 ; j < cs.length ; j++){
		
		String ss[] = cs[j].getPropName().split(",");
		for(int k = 0 ; k < ss.length ; k++){
			if(ss[k].trim().length() > 0){
				消耗集合.add(ss[k]);
			}
		}
	}
	
	int columns = Math.max(掉落集合.size(),消耗集合.size());
	if(columns == 0) columns = 1;
	
	columns = 1;
	
	for(int j = 0 ; j < columns ; j++){
		out.println("<tr align='center' bgcolor='#FFFFFF'>");
		if(j == 0){
			out.println("<td rowspan='"+columns+"'>"+(npage * size + i+1)+"</td>");
			out.println("<td rowspan='"+columns+"'><a href='./stat_downcity_detail.jsp?id="+dcs.getDownCityId()+"'>"+dcs.getDownCityId()+"</a></td>");
			out.println("<td rowspan='"+columns+"'>"+dcs.getName()+"</td>");
			out.println("<td rowspan='"+columns+"'>"+DateUtil.formatDate(new Date(dcs.getStartTime()),"yyyy-MM-dd HH:mm:ss")+"</td>");
			out.println("<td rowspan='"+columns+"'>"+(dcs.getTongguanTime() <= 0 ? "未通关":DateUtil.formatDate(new Date(dcs.getTongguanTime()),"yyyy-MM-dd HH:mm:ss"))+"</td>");
			out.println("<td rowspan='"+columns+"'>"+人数+"</td>");
			out.println("<td rowspan='"+columns+"'>"+平均等级+"</td>");
			out.println("<td rowspan='"+columns+"'>"+平均外功+"</td>");
			out.println("<td rowspan='"+columns+"'>"+平均内功+"</td>");
			out.println("<td rowspan='"+columns+"'>"+平均暴击率+"%</td>");
			out.println("<td rowspan='"+columns+"'>"+平均闪避率+"%</td>");
			out.println("<td rowspan='"+columns+"'>"+平均物理减伤+"%</td>");
			out.println("<td rowspan='"+columns+"'>"+平均法术减伤+"%</td>");
			out.println("<td rowspan='"+columns+"'>"+平均韧性+"</td>");
			out.println("<td rowspan='"+columns+"'>"+死亡次数+"</td>");
			out.println("<td rowspan='"+columns+"'>"+缓回血+"</td>");
			out.println("<td rowspan='"+columns+"'>"+缓回血+"</td>");
			out.println("<td rowspan='"+columns+"'>"+瞬回血+"</td>");
			out.println("<td rowspan='"+columns+"'>"+瞬回蓝+"</td>");
			out.println("<td rowspan='"+columns+"'>"+受到的伤害+"</td>");
			out.println("<td rowspan='"+columns+"'>"+受到的治疗+"</td>");
			out.println("<td rowspan='"+columns+"'>"+伤害输出+"</td>");
			out.println("<td rowspan='"+columns+"'>"+治疗输出+"</td>");
			out.println("<td rowspan='"+columns+"'>"+修装花费+"</td>");
			out.println("<td rowspan='"+columns+"'>"+掉落的金币+"</td>");
		}

		out.println("<td><a href='./stat_downcity_detail.jsp?id="+dcs.getDownCityId()+"'>"+消耗集合.size()+"</a></td>");

		out.println("<td><a href='./stat_downcity_detail.jsp?id="+dcs.getDownCityId()+"'>"+掉落集合.size()+"</a></td>");
		
		out.println("</tr>");
	}
}
%>
</table>
</BODY></html>
