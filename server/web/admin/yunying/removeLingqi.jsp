<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page
	import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><%=GameConstants.getInstance().getServerName()%></title>

<%!class Data {
		public long playerId;
		public int removeNum;
		public String serverName;

		public Data(String serverName, long playerId, int removeNum) {
			this.serverName = serverName;
			this.playerId = playerId;
			this.removeNum = removeNum;
		}
	}%>
<%
	String date = TimeTool.formatter.varChar10.format(new Date());
	if (!"2013-09-14".equals(date)) {
		out.print("时间不符");
		return;
	}
	List<Data> list = new ArrayList<Data>();
	{
		if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
			list.add(new Data("群雄逐鹿", 1282000000000020935L, 132));
			list.add(new Data("群雄逐鹿", 1282000000000307991L, 1716));
			list.add(new Data("群雄逐鹿", 1282000000000020703L, 17292));
			list.add(new Data("群雄逐鹿", 1282000000000021379L, 132));
			list.add(new Data("群雄逐鹿", 1282000000000024619L, 396));
			list.add(new Data("群雄逐鹿", 1282000000000022220L, 396));
			list.add(new Data("群雄逐鹿", 1282000000000021720L, 1173));
			list.add(new Data("群雄逐鹿", 1282000000000287390L, 5016));
			list.add(new Data("群雄逐鹿", 1282000000000409806L, 264));
			list.add(new Data("群雄逐鹿", 1282000000000471775L, 4356));
			list.add(new Data("群雄逐鹿", 1282000000000512460L, 1320));
			list.add(new Data("群雄逐鹿", 1282000000000020491L, 2112));
			list.add(new Data("群雄逐鹿", 1282000000000020740L, 2640));
			list.add(new Data("群雄逐鹿", 1282000000000103118L, 132));
			list.add(new Data("群雄逐鹿", 1282000000000184441L, 924));
			list.add(new Data("群雄逐鹿", 1282000000000026021L, 132));
			list.add(new Data("群雄逐鹿", 1282000000000287020L, 528));
			list.add(new Data("群雄逐鹿", 1282000000000022612L, 2112));
			list.add(new Data("群雄逐鹿", 1282000000000021913L, 2640));
			list.add(new Data("群雄逐鹿", 1282000000000021538L, 1452));
			list.add(new Data("群雄逐鹿", 1282000000000308142L, 7260));
			list.add(new Data("群雄逐鹿", 1282000000000020961L, 264));
			list.add(new Data("群雄逐鹿", 1282000000000027124L, 1860));
			list.add(new Data("群雄逐鹿", 1282000000000022411L, 396));
			list.add(new Data("群雄逐鹿", 1282000000000144267L, 924));
			list.add(new Data("群雄逐鹿", 1282000000000023971L, 264));
			list.add(new Data("群雄逐鹿", 1282000000000471899L, 660));
			list.add(new Data("群雄逐鹿", 1282000000000266573L, 9636));
			list.add(new Data("群雄逐鹿", 1282000000000492420L, 7260));
			list.add(new Data("群雄逐鹿", 1282000000000184942L, 528));
			list.add(new Data("群雄逐鹿", 1282000000000491780L, 132));
			list.add(new Data("群雄逐鹿", 1282000000000023920L, 3828));
			list.add(new Data("群雄逐鹿", 1282000000000266494L, 528));
			list.add(new Data("群雄逐鹿", 1282000000000512522L, 8580));
			list.add(new Data("群雄逐鹿", 1282000000000287174L, 2508));
			list.add(new Data("群雄逐鹿", 1282000000000512353L, 7524));
			list.add(new Data("群雄逐鹿", 1282000000000020637L, 264));
			list.add(new Data("群雄逐鹿", 1282000000000020960L, 660));
			list.add(new Data("群雄逐鹿", 1282000000000165074L, 396));
			list.add(new Data("群雄逐鹿", 1282000000000083276L, 972));
			list.add(new Data("群雄逐鹿", 1282000000000020539L, 2508));
			list.add(new Data("群雄逐鹿", 1282000000000020631L, 264));
			list.add(new Data("群雄逐鹿", 1282000000000512580L, 1320));
			list.add(new Data("群雄逐鹿", 1282000000000023670L, 924));
			list.add(new Data("群雄逐鹿", 1282000000000349144L, 792));
			list.add(new Data("群雄逐鹿", 1282000000000022455L, 3168));
			list.add(new Data("群雄逐鹿", 1282000000000021390L, 528));
			list.add(new Data("群雄逐鹿", 1282000000000022961L, 792));
			list.add(new Data("群雄逐鹿", 1282000000000205151L, 792));
			list.add(new Data("群雄逐鹿", 1282000000000308137L, 1980));
			list.add(new Data("群雄逐鹿", 1282000000000144171L, 396));
			list.add(new Data("群雄逐鹿", 1282000000000226234L, 1716));
			list.add(new Data("群雄逐鹿", 1282000000000022317L, 396));
			list.add(new Data("群雄逐鹿", 1282000000000026739L, 132));
			list.add(new Data("群雄逐鹿", 1282000000000021033L, 9768));
			list.add(new Data("群雄逐鹿", 1282000000000020827L, 2376));
			list.add(new Data("群雄逐鹿", 1282000000000491947L, 8712));
			list.add(new Data("群雄逐鹿", 1282000000000021415L, 924));
			list.add(new Data("群雄逐鹿", 1282000000000020752L, 264));
			list.add(new Data("群雄逐鹿", 1282000000000307219L, 3828));
			list.add(new Data("群雄逐鹿", 1282000000000247437L, 2112));
			list.add(new Data("群雄逐鹿", 1282000000000389294L, 9504));
			list.add(new Data("群雄逐鹿", 1282000000000083639L, 6468));
			list.add(new Data("群雄逐鹿", 1282000000000491945L, 396));
			list.add(new Data("群雄逐鹿", 1282000000000024956L, 132));
			list.add(new Data("群雄逐鹿", 1282000000000025463L, 2376));
			list.add(new Data("群雄逐鹿", 1282000000000307433L, 1056));
			list.add(new Data("群雄逐鹿", 1282000000000308179L, 8448));
			list.add(new Data("群雄逐鹿", 1282000000000042892L, 132));
			list.add(new Data("群雄逐鹿", 1282000000000020825L, 3168));
			list.add(new Data("群雄逐鹿", 1282000000000409934L, 1584));
			list.add(new Data("群雄逐鹿", 1282000000000164396L, 396));
			list.add(new Data("群雄逐鹿", 1282000000000021128L, 396));
			list.add(new Data("群雄逐鹿", 1282000000000287628L, 396));
			list.add(new Data("群雄逐鹿", 1282000000000024294L, 132));
			list.add(new Data("群雄逐鹿", 1282000000000286756L, 1716));
			list.add(new Data("群雄逐鹿", 1282000000000247640L, 132));
			list.add(new Data("群雄逐鹿", 1282000000000166449L, 1056));
			list.add(new Data("群雄逐鹿", 1282000000000430681L, 528));
			list.add(new Data("群雄逐鹿", 1282000000000471921L, 660));
			list.add(new Data("群雄逐鹿", 1282000000000225702L, 5148));
			list.add(new Data("群雄逐鹿", 1282000000000020854L, 132));
			list.add(new Data("群雄逐鹿", 1282000000000225537L, 660));
			list.add(new Data("群雄逐鹿", 1282000000000184987L, 264));
			list.add(new Data("群雄逐鹿", 1282000000000021264L, 12408));
			list.add(new Data("群雄逐鹿", 1282000000000184821L, 132));
			list.add(new Data("群雄逐鹿", 1282000000000021222L, 1320));
			list.add(new Data("群雄逐鹿", 1282000000000123707L, 792));
			list.add(new Data("群雄逐鹿", 1282000000000023404L, 132));
			list.add(new Data("群雄逐鹿", 1282000000000020768L, 132));
			list.add(new Data("群雄逐鹿", 1282000000000042439L, 792));
			list.add(new Data("群雄逐鹿", 1282000000000022241L, 6336));
			list.add(new Data("群雄逐鹿", 1282000000000042008L, 396));
			list.add(new Data("群雄逐鹿", 1282000000000082800L, 396));
			list.add(new Data("群雄逐鹿", 1282000000000024153L, 1716));
			list.add(new Data("群雄逐鹿", 1282000000000123719L, 1716));
			list.add(new Data("群雄逐鹿", 1282000000000022660L, 1188));
			list.add(new Data("群雄逐鹿", 1282000000000021025L, 3696));
			list.add(new Data("群雄逐鹿", 1282000000000266412L, 3168));
			list.add(new Data("群雄逐鹿", 1282000000000103257L, 4356));
			list.add(new Data("群雄逐鹿", 1282000000000020879L, 264));
			list.add(new Data("群雄逐鹿", 1282000000000022022L, 264));
			list.add(new Data("群雄逐鹿", 1282000000000205526L, 4488));
			list.add(new Data("群雄逐鹿", 1282000000000042341L, 3564));
			list.add(new Data("群雄逐鹿", 1282000000000308183L, 4488));
			list.add(new Data("群雄逐鹿", 1282000000000028462L, 396));
			list.add(new Data("群雄逐鹿", 1282000000000061909L, 4224));
			list.add(new Data("群雄逐鹿", 1282000000000143863L, 5280));
			list.add(new Data("群雄逐鹿", 1282000000000028435L, 5676));
			list.add(new Data("海纳百川", 1222000000000003400L, 132));
			list.add(new Data("海纳百川", 1222000000000696414L, 264));
			list.add(new Data("海纳百川", 1222000000000471239L, 396));
			list.add(new Data("海纳百川", 1222000000000779171L, 1056));
			list.add(new Data("海纳百川", 1222000000001229889L, 396));
			list.add(new Data("海纳百川", 1222000000000798838L, 132));
			list.add(new Data("海纳百川", 1222000000000124768L, 1056));
			list.add(new Data("海纳百川", 1222000000001188044L, 2376));
			list.add(new Data("海纳百川", 1222000000000779165L, 132));
			list.add(new Data("海纳百川", 1222000000000204869L, 132));
			list.add(new Data("海纳百川", 1222000000000186998L, 660));
			list.add(new Data("海纳百川", 1222000000000225740L, 264));
			list.add(new Data("海纳百川", 1222000000000027734L, 396));
			list.add(new Data("海纳百川", 1222000000000225475L, 2772));
			list.add(new Data("海纳百川", 1222000000000004091L, 132));
			list.add(new Data("海纳百川", 1222000000000430184L, 660));
			list.add(new Data("海纳百川", 1222000000000064261L, 132));
			list.add(new Data("海纳百川", 1222000000000349807L, 528));
			list.add(new Data("海纳百川", 1222000000001270947L, 3564));
			list.add(new Data("海纳百川", 1222000000000574231L, 1056));
			list.add(new Data("海纳百川", 1222000000000002938L, 132));
			list.add(new Data("海纳百川", 1222000000000023539L, 132));
			list.add(new Data("海纳百川", 1222000000001085728L, 132));
			list.add(new Data("海纳百川", 1222000000000003043L, 132));
			list.add(new Data("海纳百川", 1222000000000002977L, 1320));
			list.add(new Data("海纳百川", 1222000000001270058L, 264));
			list.add(new Data("海纳百川", 1222000000001249612L, 132));
			list.add(new Data("海纳百川", 1222000000001146944L, 660));
			list.add(new Data("海纳百川", 1222000000001230212L, 1716));
			list.add(new Data("海纳百川", 1222000000000327900L, 264));
			list.add(new Data("海纳百川", 1222000000001126470L, 2376));
			list.add(new Data("海纳百川", 1222000000000594511L, 1848));
			list.add(new Data("海纳百川", 1222000000000002353L, 396));
			list.add(new Data("海纳百川", 1222000000000308411L, 132));
			list.add(new Data("海纳百川", 1222000000000635655L, 4884));
			list.add(new Data("海纳百川", 1222000000000615395L, 2244));
			list.add(new Data("海纳百川", 1222000000000186127L, 3432));
			list.add(new Data("海纳百川", 1222000000000014674L, 528));
			list.add(new Data("海纳百川", 1222000000000696493L, 2640));
			list.add(new Data("海纳百川", 1222000000001270558L, 132));
			list.add(new Data("海纳百川", 1222000000000188430L, 1320));
			list.add(new Data("海纳百川", 1222000000001024029L, 924));
			list.add(new Data("海纳百川", 1222000000000208177L, 264));
			list.add(new Data("海纳百川", 1222000000000002187L, 528));
			list.add(new Data("海纳百川", 1222000000001249807L, 528));
			list.add(new Data("海纳百川", 1222000000001004368L, 132));
			list.add(new Data("海纳百川", 1222000000000676251L, 1320));
			list.add(new Data("海纳百川", 1222000000001249777L, 264));
			list.add(new Data("海纳百川", 1222000000000533805L, 132));
			list.add(new Data("海纳百川", 1222000000000020847L, 660));
			list.add(new Data("海纳百川", 1222000000000082246L, 264));
			list.add(new Data("海纳百川", 1222000000000144206L, 3036));
			list.add(new Data("海纳百川", 1222000000000799144L, 660));
			list.add(new Data("海纳百川", 1222000000000430169L, 132));
			list.add(new Data("海纳百川", 1222000000000246756L, 10560));
			list.add(new Data("海纳百川", 1222000000000064010L, 396));
			list.add(new Data("海纳百川", 1222000000000003021L, 924));
			list.add(new Data("海纳百川", 1222000000000004180L, 3036));
			list.add(new Data("海纳百川", 1222000000000861158L, 132));
			list.add(new Data("海纳百川", 1222000000000492209L, 132));
			list.add(new Data("海纳百川", 1222000000001147035L, 264));
			list.add(new Data("海纳百川", 1222000000001230778L, 396));
			list.add(new Data("海纳百川", 1222000000000082753L, 2376));
			list.add(new Data("海纳百川", 1222000000001249862L, 132));
			list.add(new Data("海纳百川", 1222000000000165452L, 660));
			list.add(new Data("海纳百川", 1222000000000246263L, 1188));
			list.add(new Data("海纳百川", 1222000000000225980L, 132));
			list.add(new Data("海纳百川", 1222000000000390111L, 132));
			list.add(new Data("海纳百川", 1222000000000012926L, 1188));
			list.add(new Data("海纳百川", 1222000000001188275L, 792));
			list.add(new Data("海纳百川", 1222000000000860311L, 1056));
			list.add(new Data("海纳百川", 1222000000001250734L, 132));
			list.add(new Data("海纳百川", 1222000000001065277L, 396));
			list.add(new Data("海纳百川", 1222000000000779143L, 396));
			list.add(new Data("海纳百川", 1222000000000003225L, 264));
			list.add(new Data("海纳百川", 1222000000000064354L, 1320));
			list.add(new Data("海纳百川", 1222000000000389209L, 660));
			list.add(new Data("海纳百川", 1222000000000004927L, 10032));
			list.add(new Data("海纳百川", 1222000000000003341L, 1624));
			list.add(new Data("海纳百川", 1222000000001230336L, 1188));
			list.add(new Data("海纳百川", 1222000000000082745L, 1320));
			list.add(new Data("海纳百川", 1222000000000860750L, 264));
			list.add(new Data("海纳百川", 1222000000000166335L, 132));
			list.add(new Data("海纳百川", 1222000000000002776L, 396));
			list.add(new Data("海纳百川", 1222000000000491919L, 3813));
			list.add(new Data("海纳百川", 1222000000000007265L, 132));
			list.add(new Data("海纳百川", 1222000000000145487L, 396));
			list.add(new Data("海纳百川", 1222000000000819525L, 132));
			list.add(new Data("海纳百川", 1222000000000574210L, 132));
			list.add(new Data("海纳百川", 1222000000000942400L, 3036));
			list.add(new Data("海纳百川", 1222000000000247444L, 132));
			list.add(new Data("海纳百川", 1222000000000983175L, 132));
			list.add(new Data("海纳百川", 1222000000000002478L, 132));
			list.add(new Data("海纳百川", 1222000000001085725L, 132));
			list.add(new Data("海纳百川", 1222000000000676527L, 396));
			list.add(new Data("海纳百川", 1222000000000002560L, 3168));
			list.add(new Data("海纳百川", 1222000000001230662L, 132));
			list.add(new Data("海纳百川", 1222000000001003991L, 1056));
			list.add(new Data("海纳百川", 1222000000000004101L, 528));
			list.add(new Data("海纳百川", 1222000000000002368L, 396));
			list.add(new Data("海纳百川", 1222000000000328601L, 396));
			list.add(new Data("海纳百川", 1222000000001249451L, 396));
			list.add(new Data("海纳百川", 1222000000000942607L, 132));
			list.add(new Data("海纳百川", 1222000000000145988L, 1056));
			list.add(new Data("海纳百川", 1222000000000389710L, 528));
			list.add(new Data("海纳百川", 1222000000000004290L, 1320));
			list.add(new Data("海纳百川", 1222000000000105149L, 528));
			list.add(new Data("海纳百川", 1222000000001086011L, 1848));
			list.add(new Data("海纳百川", 1222000000000328491L, 528));
			list.add(new Data("海纳百川", 1222000000000002178L, 132));
			list.add(new Data("海纳百川", 1222000000001044684L, 1452));
			list.add(new Data("海纳百川", 1222000000001147630L, 132));
			list.add(new Data("海纳百川", 1222000000001250452L, 264));
			list.add(new Data("海纳百川", 1222000000000901406L, 20853));
			list.add(new Data("海纳百川", 1222000000000245838L, 1320));
			list.add(new Data("海纳百川", 1222000000001024031L, 264));
			list.add(new Data("海纳百川", 1222000000001003561L, 264));
			list.add(new Data("海纳百川", 1222000000000003267L, 264));
			list.add(new Data("海纳百川", 1222000000000106242L, 264));
			list.add(new Data("海纳百川", 1222000000001229267L, 132));
			list.add(new Data("海纳百川", 1222000000001270616L, 15576));
			list.add(new Data("海纳百川", 1222000000000901585L, 132));
			list.add(new Data("海纳百川", 1222000000000082224L, 792));
			list.add(new Data("海纳百川", 1222000000001024377L, 660));
			list.add(new Data("海纳百川", 1222000000000061729L, 792));
			list.add(new Data("海纳百川", 1222000000000389766L, 2244));
			list.add(new Data("海纳百川", 1222000000000065270L, 528));
			list.add(new Data("海纳百川", 1222000000000184351L, 1584));
			list.add(new Data("海纳百川", 1222000000001085988L, 264));
			list.add(new Data("海纳百川", 1222000000000819966L, 924));
			list.add(new Data("海纳百川", 1222000000000675856L, 132));
			list.add(new Data("海纳百川", 1222000000000042198L, 132));
			list.add(new Data("海纳百川", 1222000000001147728L, 4092));
			list.add(new Data("海纳百川", 1222000000000002134L, 396));
			list.add(new Data("海纳百川", 1222000000000011090L, 1584));
			list.add(new Data("海纳百川", 1222000000001003823L, 132));
			list.add(new Data("海纳百川", 1222000000000839720L, 132));
			list.add(new Data("海纳百川", 1222000000000943003L, 660));
			list.add(new Data("明空梵天", 1138000000000637192L, 1835));
			list.add(new Data("明空梵天", 1138000000003338984L, 264));
			list.add(new Data("明空梵天", 1138000000001945826L, 264));
			list.add(new Data("明空梵天", 1138000000000922177L, 660));
			list.add(new Data("明空梵天", 1138000000000041979L, 1320));
			list.add(new Data("明空梵天", 1138000000002171661L, 528));
			list.add(new Data("明空梵天", 1138000000005509531L, 132));
			list.add(new Data("明空梵天", 1138000000000409739L, 264));
			list.add(new Data("明空梵天", 1138000000002764925L, 132));
			list.add(new Data("明空梵天", 1138000000000637044L, 660));
			list.add(new Data("明空梵天", 1138000000002683051L, 396));
			list.add(new Data("明空梵天", 1138000000005284412L, 132));
			list.add(new Data("明空梵天", 1138000000003952810L, 3696));
			list.add(new Data("明空梵天", 1138000000002212406L, 2508));
			list.add(new Data("明空梵天", 1138000000005346505L, 7788));
			list.add(new Data("明空梵天", 1138000000001085572L, 132));
			list.add(new Data("明空梵天", 1138000000002887921L, 396));
			list.add(new Data("明空梵天", 1138000000000492916L, 132));
			list.add(new Data("明空梵天", 1138000000005039825L, 264));
			list.add(new Data("明空梵天", 1138000000000006079L, 528));
			list.add(new Data("明空梵天", 1138000000002253471L, 396));
			list.add(new Data("明空梵天", 1138000000004363043L, 924));
			list.add(new Data("明空梵天", 1138000000002253131L, 8316));
			list.add(new Data("明空梵天", 1138000000000061447L, 125));
			list.add(new Data("明空梵天", 1138000000000022069L, 264));
			list.add(new Data("明空梵天", 1138000000002663079L, 132));
			list.add(new Data("明空梵天", 1138000000000003642L, 3960));
			list.add(new Data("明空梵天", 1138000000002540270L, 37884));
			list.add(new Data("明空梵天", 1138000000001986645L, 528));
			list.add(new Data("明空梵天", 1138000000000881417L, 924));
			list.add(new Data("明空梵天", 1138000000001701085L, 5412));
			list.add(new Data("明空梵天", 1138000000005346633L, 528));
			list.add(new Data("明空梵天", 1138000000006164546L, 1188));
			list.add(new Data("明空梵天", 1138000000000594403L, 396));
			list.add(new Data("明空梵天", 1138000000005836851L, 264));
			list.add(new Data("明空梵天", 1138000000004341902L, 1452));
			list.add(new Data("明空梵天", 1138000000002908462L, 264));
			list.add(new Data("明空梵天", 1138000000000002386L, 528));
			list.add(new Data("明空梵天", 1138000000005345596L, 528));
			list.add(new Data("明空梵天", 1138000000002867549L, 2772));
			list.add(new Data("百花深谷", 1130000000000023061L, 132));
			list.add(new Data("百花深谷", 1130000000000471932L, 132));
			list.add(new Data("百花深谷", 1130000000000942722L, 1452));
			list.add(new Data("百花深谷", 1130000000000024266L, 10956));
			list.add(new Data("百花深谷", 1130000000000696650L, 1980));
			list.add(new Data("百花深谷", 1130000000000041375L, 132));
			list.add(new Data("百花深谷", 1130000000000962671L, 132));
			list.add(new Data("百花深谷", 1130000000001658896L, 264));
			list.add(new Data("百花深谷", 1130000000000676128L, 1320));
			list.add(new Data("百花深谷", 1130000000001004824L, 11220));
			list.add(new Data("百花深谷", 1130000000000696731L, 660));
			list.add(new Data("百花深谷", 1130000000000246474L, 1716));
			list.add(new Data("百花深谷", 1130000000001658970L, 7128));
			list.add(new Data("百花深谷", 1130000000001105937L, 1584));
			list.add(new Data("百花深谷", 1130000000000287184L, 11088));
			list.add(new Data("百花深谷", 1130000000001843219L, 25344));
			list.add(new Data("飘渺王城", 1102000000002853014L, 2640));
			list.add(new Data("飘渺王城", 1102000000009636122L, 10956));
			list.add(new Data("飘渺王城", 1102000000009758886L, 1716));
			list.add(new Data("飘渺王城", 1102000000004070973L, 6600));
			list.add(new Data("飘渺王城", 1102000000000005603L, 264));
			list.add(new Data("飘渺王城", 1102000000009840862L, 25872));
			list.add(new Data("飘渺王城", 1102000000000012115L, 132));
			list.add(new Data("飘渺王城", 1102000000009288423L, 2376));
			list.add(new Data("傲视群雄", 1189000000003522683L, 132));
			list.add(new Data("傲视群雄", 1189000000002662774L, 264));
			list.add(new Data("傲视群雄", 1189000000003584427L, 264));
			list.add(new Data("傲视群雄", 1189000000002335853L, 132));
			list.add(new Data("傲视群雄", 1189000000002642627L, 264));
			list.add(new Data("傲视群雄", 1189000000001884211L, 528));
			list.add(new Data("傲视群雄", 1189000000000328852L, 264));
			list.add(new Data("傲视群雄", 1189000000000532871L, 132));
			list.add(new Data("傲视群雄", 1189000000002519520L, 2112));
			list.add(new Data("傲视群雄", 1189000000002970061L, 132));
			list.add(new Data("傲视群雄", 1189000000002969704L, 132));
			list.add(new Data("傲视群雄", 1189000000000022599L, 132));
			list.add(new Data("傲视群雄", 1189000000003727627L, 2376));
			list.add(new Data("傲视群雄", 1189000000003563889L, 528));
			list.add(new Data("傲视群雄", 1189000000003359227L, 660));
			list.add(new Data("傲视群雄", 1189000000003338577L, 3696));
			list.add(new Data("傲视群雄", 1189000000002642502L, 3300));
			list.add(new Data("傲视群雄", 1189000000003522687L, 132));
			list.add(new Data("傲视群雄", 1189000000002375732L, 396));
			list.add(new Data("傲视群雄", 1189000000000021067L, 660));
			list.add(new Data("傲视群雄", 1189000000000022467L, 264));
			list.add(new Data("傲视群雄", 1189000000002745302L, 132));
			list.add(new Data("傲视群雄", 1189000000002745243L, 264));
			list.add(new Data("傲视群雄", 1189000000003461180L, 528));
			list.add(new Data("傲视群雄", 1189000000002806529L, 132));
			list.add(new Data("傲视群雄", 1189000000000026189L, 924));
			list.add(new Data("傲视群雄", 1189000000000184321L, 264));
			list.add(new Data("傲视群雄", 1189000000000022660L, 8844));
			list.add(new Data("傲视群雄", 1189000000001905508L, 264));
			list.add(new Data("傲视群雄", 1189000000003666229L, 1056));
			list.add(new Data("傲视群雄", 1189000000003604765L, 2508));
			list.add(new Data("傲视群雄", 1189000000003481641L, 1584));
			list.add(new Data("傲视群雄", 1189000000003318713L, 8580));
			list.add(new Data("傲视群雄", 1189000000003154173L, 3036));
			list.add(new Data("傲视群雄", 1189000000003625116L, 660));
			list.add(new Data("傲视群雄", 1189000000002069279L, 132));
			list.add(new Data("傲视群雄", 1189000000000061894L, 264));
			list.add(new Data("傲视群雄", 1189000000003584234L, 1188));
			list.add(new Data("傲视群雄", 1189000000000553703L, 132));
			list.add(new Data("傲视群雄", 1189000000002396318L, 528));
			list.add(new Data("春暖花开", 1213000000000062299L, 11748));
			list.add(new Data("春暖花开", 1213000000000656118L, 9900));
			list.add(new Data("春暖花开", 1213000000000025207L, 4884));
			list.add(new Data("春暖花开", 1213000000000102492L, 18084));
			list.add(new Data("春暖花开", 1213000000000102406L, 1848));
			list.add(new Data("一方霸主", 1279000000000004495L, 1320));
			list.add(new Data("一方霸主", 1279000000000145018L, 528));
			list.add(new Data("一方霸主", 1279000000000003473L, 9900));
			list.add(new Data("一方霸主", 1279000000000020721L, 396));
			list.add(new Data("一方霸主", 1279000000000003289L, 14256));
			list.add(new Data("一方霸主", 1279000000000001025L, 2244));
			list.add(new Data("一方霸主", 1279000000000003862L, 1188));
			list.add(new Data("一方霸主", 1279000000000615080L, 528));
			list.add(new Data("一方霸主", 1279000000000144295L, 12804));
			list.add(new Data("一方霸主", 1279000000000062266L, 132));
			list.add(new Data("一方霸主", 1279000000000105554L, 528));
			list.add(new Data("洪荒古殿", 1214000000000226195L, 16764));
			list.add(new Data("洪荒古殿", 1214000000000066029L, 132));
			list.add(new Data("洪荒古殿", 1214000000000205031L, 11748));
			list.add(new Data("洪荒古殿", 1214000000000063712L, 264));
			list.add(new Data("洪荒古殿", 1214000000000022542L, 7392));
			list.add(new Data("洪荒古殿", 1214000000000594257L, 132));
			list.add(new Data("洪荒古殿", 1214000000000022357L, 792));
			list.add(new Data("洪武大帝", 1294000000000125472L, 528));
			list.add(new Data("洪武大帝", 1294000000000125358L, 12276));
			list.add(new Data("洪武大帝", 1294000000000125476L, 264));
			list.add(new Data("洪武大帝", 1294000000000125471L, 2772));
			list.add(new Data("洪武大帝", 1294000000000125336L, 660));
			list.add(new Data("洪武大帝", 1294000000000125479L, 396));
			list.add(new Data("洪武大帝", 1294000000000125468L, 132));
			list.add(new Data("洪武大帝", 1294000000000014794L, 1056));
			list.add(new Data("洪武大帝", 1294000000000125469L, 132));
			list.add(new Data("洪武大帝", 1294000000000012971L, 924));
			list.add(new Data("洪武大帝", 1294000000000125482L, 132));
			list.add(new Data("洪武大帝", 1294000000000125458L, 2772));
			list.add(new Data("上善若水", 1212000000000758741L, 3960));
			list.add(new Data("上善若水", 1212000000001210805L, 792));
			list.add(new Data("上善若水", 1212000000001190219L, 14256));
			list.add(new Data("云游魂境", 1276000000000143663L, 15708));
			list.add(new Data("云游魂境", 1276000000000020886L, 1848));
			list.add(new Data("绿野仙踪", 1298000000000012849L, 528));
			list.add(new Data("绿野仙踪", 1298000000000012842L, 132));
			list.add(new Data("绿野仙踪", 1298000000000012807L, 4224));
			list.add(new Data("绿野仙踪", 1298000000000012795L, 528));
			list.add(new Data("绿野仙踪", 1298000000000012580L, 10428));
			list.add(new Data("绿野仙踪", 1298000000000012748L, 1716));
			list.add(new Data("天下无双", 1220000000002130670L, 16368));
			list.add(new Data("程门立雪", 1241000000000450586L, 5016));
			list.add(new Data("程门立雪", 1241000000000123796L, 3300));
			list.add(new Data("程门立雪", 1241000000000450588L, 6864));
			list.add(new Data("程门立雪", 1241000000000450585L, 4752));
			list.add(new Data("半城烟沙", 1292000000000164762L, 396));
			list.add(new Data("半城烟沙", 1292000000000144803L, 924));
			list.add(new Data("半城烟沙", 1292000000000165183L, 13728));
			list.add(new Data("天上人间", 1245000000000246238L, 132));
			list.add(new Data("天上人间", 1245000000000020724L, 3168));
			list.add(new Data("天上人间", 1245000000000020941L, 132));
			list.add(new Data("天上人间", 1245000000000102690L, 6996));
			list.add(new Data("盛世永安", 1295000000000163855L, 132));
			list.add(new Data("盛世永安", 1295000000000163846L, 10032));
			list.add(new Data("天府之国", 1299000000000006851L, 1187));
			list.add(new Data("天府之国", 1299000000000006836L, 792));
			list.add(new Data("天府之国", 1299000000000006873L, 7920));
			list.add(new Data("琴瑟和鸣", 1289000000000697320L, 9372));
			list.add(new Data("琴瑟和鸣", 1289000000000269248L, 396));
			list.add(new Data("龙争虎斗", 1257000000000164249L, 10428));
			list.add(new Data("狂傲天下", 1300000000000007264L, 4752));
			list.add(new Data("狂傲天下", 1300000000000007238L, 3300));
			list.add(new Data("狂傲天下", 1300000000000007279L, 264));
			list.add(new Data("天魔神谭", 1278000000000143441L, 6336));
			list.add(new Data("侠骨柔情", 1288000000000022635L, 1044));
			list.add(new Data("侠骨柔情", 1288000000000023083L, 3828));
			list.add(new Data("侠骨柔情", 1288000000000124065L, 1452));
			list.add(new Data("天涯海角", 1271000000001048986L, 5148));
			list.add(new Data("天涯海角", 1271000000000248583L, 1056));
			list.add(new Data("破晓之穹", 1272000000000539313L, 132));
			list.add(new Data("西方灵山", 1128000000002376770L, 132));
			list.add(new Data("傲啸封仙", 1281000000000103485L, 132));
			list.add(new Data("诸神梦境", 1269000000000554712L, 132));
			list.add(new Data("浩瀚乾坤", 1233000000000163995L, 132));
			list.add(new Data("千秋北斗", 1227000000000041044L, 132));
			list.add(new Data("卧虎藏龙", 1285000000000245891L, 264));
			list.add(new Data("华夏战神", 1205000000000286984L, 264));
			list.add(new Data("龙翔凤舞", 1204000000001864384L, 264));
			list.add(new Data("鱼跃龙门", 1199000000000123122L, 132));
			list.add(new Data("鱼跃龙门", 1199000000000123122L, 132));
			list.add(new Data("问鼎江湖", 1192000000001413292L, 264));
			list.add(new Data("普陀梵音", 1249000000000126191L, 132));
			list.add(new Data("普陀梵音", 1249000000000102793L, 132));
			list.add(new Data("普陀梵音", 1249000000000102793L, 132));
			list.add(new Data("勇者无敌", 1184000000000840419L, 396));
			list.add(new Data("金蛇圣殿", 1167000000003666384L, 132));
			list.add(new Data("金蛇圣殿", 1167000000004075750L, 132));
			list.add(new Data("燃烧圣殿", 1105000000009236525L, 396));
			list.add(new Data("似水流年", 1254000000000082509L, 528));
			list.add(new Data("桃园仙境", 1101000000009630343L, 264));
			list.add(new Data("桃园仙境", 1101000000010921243L, 264));
			list.add(new Data("九仙揽月", 1210000000000020485L, 660));
			list.add(new Data("再续前缘", 1251000000000184574L, 1056));
			list.add(new Data("九霄龙吟", 1203000000001904667L, 528));
			list.add(new Data("九霄龙吟", 1203000000001679361L, 528));
			list.add(new Data("豪杰聚义", 1266000000000003385L, 924));
			list.add(new Data("豪杰聚义", 1266000000000006760L, 132));
			list.add(new Data("豪杰聚义", 1266000000000041220L, 132));
			list.add(new Data("雄霸天下", 1175000000000368958L, 1056));
			list.add(new Data("雄霸天下", 1175000000001926751L, 132));
			list.add(new Data("龙隐幽谷", 1291000000000024670L, 660));
			list.add(new Data("龙隐幽谷", 1291000000000002274L, 132));
			list.add(new Data("龙隐幽谷", 1291000000000003271L, 264));
			list.add(new Data("龙隐幽谷", 1291000000000002272L, 264));
			list.add(new Data("策马江湖", 1270000000000840505L, 660));
			list.add(new Data("策马江湖", 1270000000000368997L, 264));
			list.add(new Data("策马江湖", 1270000000000614480L, 264));
			list.add(new Data("策马江湖", 1270000000000799857L, 132));
			list.add(new Data("月满西楼", 1259000000000043467L, 1320));
			list.add(new Data("前尘忆梦", 1297000000000082379L, 1452));
			list.add(new Data("笑傲江湖", 1194000000001618884L, 528));
			list.add(new Data("笑傲江湖", 1194000000000021703L, 792));
			list.add(new Data("笑傲江湖", 1194000000000861705L, 264));
			list.add(new Data("一统江湖", 1286000000000007700L, 396));
			list.add(new Data("一统江湖", 1286000000000004294L, 528));
			list.add(new Data("一统江湖", 1286000000000103948L, 396));
			list.add(new Data("一统江湖", 1286000000000005961L, 132));
			list.add(new Data("一统江湖", 1286000000000004216L, 396));
			list.add(new Data("一统江湖", 1286000000000004466L, 396));
			list.add(new Data("仙山楼阁", 1275000000000009799L, 1782));
			list.add(new Data("飘渺仙道", 1230000000000779061L, 132));
			list.add(new Data("飘渺仙道", 1230000000000861097L, 264));
			list.add(new Data("飘渺仙道", 1230000000000185681L, 2112));
			list.add(new Data("奇门辉煌", 1290000000000452348L, 2772));
			list.add(new Data("菩提众生", 1247000000000186536L, 1584));
			list.add(new Data("菩提众生", 1247000000000064417L, 924));
			list.add(new Data("菩提众生", 1247000000000234034L, 132));
			list.add(new Data("菩提众生", 1247000000000159223L, 132));
			list.add(new Data("风雪之巅", 1100000000005230581L, 396));
			list.add(new Data("风雪之巅", 1100000000006643542L, 264));
			list.add(new Data("风雪之巅", 1100000000000827187L, 264));
			list.add(new Data("风雪之巅", 1100000000000805951L, 396));
			list.add(new Data("风雪之巅", 1100000000006602214L, 396));
			list.add(new Data("风雪之巅", 1100000000009776516L, 264));
			list.add(new Data("风雪之巅", 1100000000002117258L, 132));
			list.add(new Data("风雪之巅", 1100000000005250635L, 396));
			list.add(new Data("风雪之巅", 1100000000009776502L, 396));
			list.add(new Data("千娇百媚", 1225000000000020631L, 528));
			list.add(new Data("千娇百媚", 1225000000000021448L, 1848));
			list.add(new Data("千娇百媚", 1225000000000021448L, 1188));
			list.add(new Data("一战成名", 1267000000000143447L, 4620));
			list.add(new Data("金戈铁马", 1264000000000063798L, 5016));
			list.add(new Data("仙界至尊", 1170000000000063296L, 132));
			list.add(new Data("仙界至尊", 1170000000005059092L, 132));
			list.add(new Data("仙界至尊", 1170000000004137703L, 132));
			list.add(new Data("仙界至尊", 1170000000004813746L, 132));
			list.add(new Data("仙界至尊", 1170000000005243104L, 132));
			list.add(new Data("仙界至尊", 1170000000003400366L, 1452));
			list.add(new Data("仙界至尊", 1170000000004915937L, 924));
			list.add(new Data("仙界至尊", 1170000000005591508L, 660));
			list.add(new Data("仙界至尊", 1170000000000266777L, 1584));
			list.add(new Data("一代天骄", 1280000000000124114L, 6072));
			list.add(new Data("修罗转生", 1240000000000012501L, 3696));
			list.add(new Data("修罗转生", 1240000000002314314L, 132));
			list.add(new Data("修罗转生", 1240000000000011138L, 1452));
			list.add(new Data("修罗转生", 1240000000002316181L, 132));
			list.add(new Data("修罗转生", 1240000000000015600L, 660));
		} else if (PlatformManager.getInstance().isPlatformOf(Platform.腾讯)) {
			list.add(new Data("众仙归来",1114000000000004641L,297));
			list.add(new Data("众仙归来",1114000000000118704L,330));
			list.add(new Data("华山之巅",1111000000000001109L,247));
			list.add(new Data("幽冥山谷",1103000000000122895L,33));
			list.add(new Data("幽冥山谷",1103000000000054752L,198));
			list.add(new Data("幽冥山谷",1103000000000127133L,99));
			list.add(new Data("幽冥山谷",1103000000000024722L,165));
			list.add(new Data("幽冥山谷",1103000000000088676L,297));
			list.add(new Data("幽冥山谷",1103000000000045243L,264));
			list.add(new Data("幽冥山谷",1103000000000054348L,561));
			list.add(new Data("幽冥山谷",1103000000000087511L,924));
			list.add(new Data("幽冥山谷",1103000000000008715L,132));
			list.add(new Data("幽冥山谷",1103000000000014077L,132));
			list.add(new Data("幽冥山谷",1103000000000013684L,33));
			list.add(new Data("幽冥山谷",1103000000000156026L,429));
			list.add(new Data("幽冥山谷",1103000000000022582L,297));
			list.add(new Data("幽冥山谷",1103000000000107001L,627));
			list.add(new Data("幽冥山谷",1103000000000103787L,33));
			list.add(new Data("幽冥山谷",1103000000000102634L,561));
			list.add(new Data("幽冥山谷",1103000000000129620L,33));
			list.add(new Data("幽冥山谷",1103000000000121037L,2046));
			list.add(new Data("幽冥山谷",1103000000000111327L,2706));
			list.add(new Data("幽冥山谷",1103000000000078677L,528));
			list.add(new Data("幽冥山谷",1103000000000073514L,165));
			list.add(new Data("幽冥山谷",1103000000000087287L,231));
			list.add(new Data("幽冥山谷",1103000000000001926L,66));
			list.add(new Data("幽冥山谷",1103000000000147638L,33));
			list.add(new Data("幽冥山谷",1103000000000066682L,33));
			list.add(new Data("幽冥山谷",1103000000000092232L,2475));
			list.add(new Data("幽冥山谷",1103000000000137366L,957));
			list.add(new Data("幽冥山谷",1103000000000089844L,792));
			list.add(new Data("幽冥山谷",1103000000000120569L,2805));
			list.add(new Data("幽冥山谷",1103000000000122153L,297));
			list.add(new Data("幽冥山谷",1103000000000106590L,2772));
			list.add(new Data("幽冥山谷",1103000000000088404L,33));
			list.add(new Data("幽冥山谷",1103000000000129842L,33));
			list.add(new Data("幽冥山谷",1103000000000052698L,4323));
			list.add(new Data("幽冥山谷",1103000000000100561L,264));
			list.add(new Data("幽冥山谷",1103000000000015787L,1881));
			list.add(new Data("幽冥山谷",1103000000000072057L,594));
			list.add(new Data("幽冥山谷",1103000000000109311L,66));
			list.add(new Data("幽冥山谷",1103000000000105874L,165));
			list.add(new Data("幽冥山谷",1103000000000055772L,33));
			list.add(new Data("幽冥山谷",1103000000000081666L,66));
			list.add(new Data("幽冥山谷",1103000000000111310L,33));
			list.add(new Data("幽冥山谷",1103000000000155672L,726));
			list.add(new Data("幽冥山谷",1103000000000065419L,132));
			list.add(new Data("幽冥山谷",1103000000000047575L,33));
			list.add(new Data("幽冥山谷",1103000000000019436L,363));
			list.add(new Data("幽冥山谷",1103000000000156089L,132));
			list.add(new Data("幽冥山谷",1103000000000007779L,660));
			list.add(new Data("幽冥山谷",1103000000000044360L,165));
			list.add(new Data("幽冥山谷",1103000000000120881L,33));
			list.add(new Data("幽冥山谷",1103000000000124038L,33));
			list.add(new Data("幽冥山谷",1103000000000008533L,462));
			list.add(new Data("幽冥山谷",1103000000000031549L,528));
			list.add(new Data("幽冥山谷",1103000000000079867L,33));
			list.add(new Data("幽冥山谷",1103000000000080600L,132));
			list.add(new Data("幽冥山谷",1103000000000024554L,1386));
			list.add(new Data("幽冥山谷",1103000000000067036L,198));
			list.add(new Data("幽冥山谷",1103000000000140310L,99));
			list.add(new Data("幽冥山谷",1103000000000017274L,33));
			list.add(new Data("幽冥山谷",1103000000000083474L,231));
			list.add(new Data("幽冥山谷",1103000000000156764L,825));
			list.add(new Data("幽冥山谷",1103000000000035593L,297));
			list.add(new Data("幽冥山谷",1103000000000012283L,198));
			list.add(new Data("幽冥山谷",1103000000000137324L,66));
			list.add(new Data("幽冥山谷",1103000000000013470L,33));
			list.add(new Data("幽冥山谷",1103000000000072275L,132));
			list.add(new Data("幽冥山谷",1103000000000122039L,33));
			list.add(new Data("幽冥山谷",1103000000000042957L,198));
			list.add(new Data("幽冥山谷",1103000000000139469L,1320));
			list.add(new Data("幽冥山谷",1103000000000065624L,660));
			list.add(new Data("幽冥山谷",1103000000000062472L,792));
			list.add(new Data("幽冥山谷",1103000000000113451L,231));
			list.add(new Data("幽冥山谷",1103000000000137326L,66));
			list.add(new Data("幽冥山谷",1103000000000135288L,1188));
			list.add(new Data("霸气乾坤",1105000000000203759L,132));
			list.add(new Data("霸气乾坤",1105000000000128012L,330));
			list.add(new Data("霸气乾坤",1105000000000128650L,198));
			list.add(new Data("烟雨清山",1107000000000042042L,66));


		}
	}
	//	String playerNames[] = { "噯戀you", "相見*恨晚", "紫玉娥", "眼鏡兄", "暮成雪", "后", "戰神龍之王", "凌小晞、", "快樂吊卡手", "群魔亂舞", "鴉魔狩", "豬鼻雞=_=", "EVO安少", "三隻小熊", "老太婆", "魔比", "黑心鬼", "★靖少★", "萬中之明1", "★靖少★", "后", "銀子", "甜蜜蜜", "機車大哥", "煞星狼", "基爺", "一夫當關", "上等茶", "我就是我", "陌小猪", "情洛莎", "伊伊", "舞旋子", "真龍-光龍", "日曜", "夜夜呱", "夜夜萌", "軟Q", "Mr.zeng", "光龍", "小茵", "阿拉伯阿拉", "可汗", "寒冰°~", "赢仙仙", "詩情", "幻星龍", "秀智", "請勿挑戰神", "夜夜愛", "蒼紫楓", "暮成雪", "crazy幻影", "金純潔!", "秀智", "傅說木偶", "默默不得語", "ady", "戰將", "黃阿威" };
	//	int removeNums[] = { 3141, 2031, 1803, 1560, 1085, 977, 923, 775, 658, 505, 445, 360, 343, 273, 257, 211, 199, 196, 195, 190, 166, 159, 149, 128, 127, 124, 114, 107, 91, 76, 69, 64, 63, 63, 55, 52, 48, 47, 43, 42, 42, 39, 33, 32, 30, 29, 28, 28, 28, 27, 26, 26, 25, 24, 20, 18, 18, 17, 16, 14 };
	List<Data> thisServer = new ArrayList<Data>();
	String serverName = GameConstants.getInstance().getServerName();
	for (Data data : list) {
		if (data.serverName.equals(serverName)) {
			thisServer.add(data);
		}
	}
	out.print("<H1>" + serverName + "</H1>");
	String articleName = "灵兽内丹";
	int color = 4;
	Player p = null;
	PlayerManager pm = PlayerManager.getInstance();
	if (thisServer.size() == 0) {
		out.print("本服无任何数据!");
		return;
	}
	int thisServerTotalNum = 0;
	int thisServerRemoveNum = 0;
	for (Data data : thisServer) {
		if (data == null) {
			continue;
		}
		try {
			p = pm.getPlayer(data.playerId);
			int leftRemoveNum = data.removeNum;
			if (p != null) {
				//删除背包
				Knapsack knapsack = p.getKnapsack_common();
				int 实际删除数_背包 = 0;
				int 实际删除数_防爆 = 0;
				int 实际删除数_仓库 = 0;
				for (Cell cell : knapsack.getCells()) {
					if (leftRemoveNum <= 0) {
						break;
					}
					long eId = cell.getEntityId();
					if (eId > 0) {
						ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(eId);
						if (ae != null) {
							if (ae.getArticleName().equals(articleName) && ae.getColorType() == color) {
								int num = cell.getCount();
								int cellLeft = num;
								if (leftRemoveNum >= num) {
									cellLeft = 0;
									leftRemoveNum -= num;
									实际删除数_背包 += num;
								} else {
									实际删除数_背包 += leftRemoveNum;
									cellLeft -= leftRemoveNum;
									leftRemoveNum = 0;
								}
								if (cellLeft == 0) {
									cell.setCount(0);
									cell.setEntityId(-1);
								} else {
									cell.setCount(cellLeft);
								}
								knapsack.setModified(true);
							}
						}
					}
				}
				//out.print(p.getLogString() + "从【背包】删除" + articleName + ",需要删除" + data.removeNum + "个，实际删除了：" + 实际删除数_背包 + "<br>");
				//删除防爆背包
				knapsack = p.getKnapsack_fangbao();
				if (knapsack != null) {
					for (Cell cell : knapsack.getCells()) {
						if (leftRemoveNum <= 0) {
							break;
						}
						long eId = cell.getEntityId();
						if (eId > 0) {
							ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(eId);
							if (ae != null) {
								if (ae.getArticleName().equals(articleName) && ae.getColorType() == color) {
									int num = cell.getCount();
									int cellLeft = num;
									if (leftRemoveNum >= num) {
										cellLeft = 0;
										leftRemoveNum -= num;
										实际删除数_防爆 += num;
									} else {
										实际删除数_防爆 += leftRemoveNum;
										cellLeft -= leftRemoveNum;
										leftRemoveNum = 0;
									}
									if (cellLeft == 0) {
										cell.setCount(0);
										cell.setEntityId(-1);
									} else {
										cell.setCount(cellLeft);
									}
									knapsack.setModified(true);
								}
							}
						}
					}
				}
				//out.print(p.getLogString() + "从【防爆背包】删除" + articleName + ",需要删除" + data.removeNum + "个，实际删除了：" + 实际删除数_防爆 + "<br>");
				//删除仓库
				knapsack = p.getKnapsacks_cangku();
				if (knapsack != null) {
					for (Cell cell : knapsack.getCells()) {
						if (leftRemoveNum <= 0) {
							break;
						}
						long eId = cell.getEntityId();
						if (eId > 0) {
							ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(eId);
							if (ae != null) {
								if (ae.getArticleName().equals(articleName) && ae.getColorType() == color) {
									int num = cell.getCount();
									int cellLeft = num;
									if (leftRemoveNum >= num) {
										cellLeft = 0;
										leftRemoveNum -= num;
										实际删除数_仓库 += num;
									} else {
										实际删除数_仓库 += leftRemoveNum;
										cellLeft -= leftRemoveNum;
										leftRemoveNum = 0;
									}
									if (cellLeft == 0) {
										cell.setCount(0);
										cell.setEntityId(-1);
									} else {
										cell.setCount(cellLeft);
									}
									knapsack.setModified(true);
								}
							}
						}
					}
				}
				int totalRemove = 实际删除数_仓库 + 实际删除数_背包 + 实际删除数_防爆;
				thisServerTotalNum += data.removeNum;
				thisServerRemoveNum += totalRemove;
				out.print("<font color=red>" + p.getLogString() + "删除" + articleName + "完毕,删除比例:" + (totalRemove) + "/" + data.removeNum + "</font><br>");
				ActivitySubSystem.logger.warn("[台湾删除] [" + articleName + "] [成功] [角色名：" + p.getLogString() + "] [需要删除数：" + data.removeNum + "] [实际删除数:" + (totalRemove) + "]");
			}
		} catch (Exception e) {
			out.print("获得玩家异常：" + p.getLogString() + "<br>");
			ActivitySubSystem.logger.warn("[台湾删除令旗] [获得玩家异常] [角色名：" + p.getLogString() + "] [异常:" + e + "]");
			continue;
		}
	}
	out.print("<B>" + serverName + ".删除" + articleName + ",颜色:" + color + "完毕,总人数:" + thisServer.size() + "[" + thisServerRemoveNum + "/" + thisServerTotalNum + "]</B>");
%>

</body>
</html>