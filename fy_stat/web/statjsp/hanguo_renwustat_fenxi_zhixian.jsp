<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.*"
	%>

  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String taskType=request.getParameter("taskType");
	String fenqu=request.getParameter("fenqu");
	
	if("0".equals(taskType)){taskType=null;}
	if("0".equals(fenqu)){fenqu=null;}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	/**
	*获得渠道信息
	**/
	ArrayList<String> taskTypeList = null;
	
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
              TaskinfoManagerImpl taskinfoManager=TaskinfoManagerImpl.getInstance();
              TaskAnalysisManagerImpl taskAnalysisManager=TaskAnalysisManagerImpl.getInstance();
              ArrayList<String []>  fenQuList= userManager.getFenQu(null);//获得现有的分区信息
              taskTypeList=taskinfoManager.gettaskType(null);//任务类型
              List<String[]>  taskstatList= taskAnalysisManager.getTaskStat(startDay,endDay,fenqu,null);
             
             String renwuArray[][]={
                          {"악도시련","12","恶徒试炼"},   
 {"맹사왕의 혈","13","猛蛇王之血"},   
 {"맹사왕 시련","13","猛蛇王试炼"},   
 {"도화적 시련","14","盗火贼试炼"},   
 {"산적의 횃불","15","山贼火把"},   
 {"인연 장미","15","情缘玫瑰"},   
 {"포효곰 시련","16","咆哮熊试炼"},   
 {"포효지단","16","咆哮之胆"},   
 {"분노의 출처","16","怒气来历"},   
 {"분노의 스킬","16","怒气技能"},   
 {"호저괴지혈","17","豪猪怪之血"},   
 {"무늬 맹호 시련","17","斑斓猛虎试炼"},   
 {"구미추계의 혈","18","九尾雉鸡之血"},   
 {"초원 늑대 시련","18","草原奔狼试炼"},   
 {"만저령지환","19","蛮猪灵之环"},   
 {"거사지령 시련","19","巨蛇之灵试炼"},   
 {"방황하는 귀신의 혼","20","游荡野鬼之魂"},   
 {"방황하는 귀신의 백","20","游荡野鬼之魄"},   
 {"영귀 시련","21","灵鬼试炼"},   
 {"영귀의 혼백","21","灵鬼之魄"},   
 {"정마 시련","22","情魔试炼"},   
 {"감심마 시련","22","撼心魔试炼"},   
 {"소은 사마의 마음","23","小隐邪魔之心"},   
 {"동매화 수집","23","采集冬梅花"},   
 {"은마지혈","23","隐魔之血"},   
 {"무량유용의 도장","24","无良游勇印章"},   
 {"흉악 강도 시련","24","凶狠匪帮试炼"},   
 {"흑석 초소병 시련","25","黑石哨兵试炼"},   
 {"흑석 호위의 갑옷","25","黑石护卫战甲"},   
 {"독초 수집","25","采集毒草"},   
 {"흑석 산적의 전극","25","黑石小贼之矛"},   
 {"흑선 신병 시련","26","黑石新兵试炼"},   
 {"흑석 중견 시련","26","黑石中坚试炼"},   
 {"탑백 수집","26","采集塔魄"},   
 {"흑석 중견의 전도","26","黑石中坚战刀"},   
 {"흑석 고수의 암살무기","26","黑石高手暗器"},   
 {"강호 고수의 투구","27","江湖高手盔甲"},   
 {"흑풍괴 시련","27","黑风怪试炼"},   
 {"흑석 비밀약","27","黑石秘药"},   
 {"흑석 옹호자의 갑옷","28","黑石拥趸铠甲"},   
 {"흑석 정예 시련","28","黑石精英试炼"},   
 {"탑혼 수집","28","采集塔魂"},   
 {"하녀 보석","28","贴身丫鬟珠宝"},   
 {"흑석 마녀 시련","29","黑石魔女试炼"},   
 {"풍전요녀 시련","29","风尘妖女试炼"},   
 {"강룡목 수집","29","采集降龙木"},   
 {"흑석 응견 비급","30","黑石鹰犬秘籍"},   
 {"미종환백 시련","30","迷踪幻魄试炼"},   
 {"추락지영의 먼지","30","堕落之影灰尘"},   
 {"사영마 시련","31","邪影魔试炼"},   
 {"흑석 앞잡이 시련","31","黑石走狗试炼"},   
 {"서역 독사의 껍질","31","西域毒蛇之皮"},   
 {"포타청 시련","32","包打听试炼"},   
 {"진탑령요의 영혼","32","镇塔灵妖之魂"},   
 {"반탑사마 시련","32","盘塔邪魔试炼"},   
 {"서역 갈색곰 시련","33","西域棕熊试炼"},   
 {"미혼지단","33","迷魂之胆"},   
 {"복호재 수집","33","采集伏虎材"},   
 {"풍마노자 시련","33","疯魔老者试炼"},   
 {"마수야왕 시련","34","魔修野王试炼"},   
 {"두라역사 쇠사슬","34","斗罗力士之链"},   
 {"배회 망혼의 영혼","34","徘徊亡魂之魄"},   
 {"쇄탑령 수집","34","采集锁塔灵"},   
 {"영존지인 시련","35","灵尊真人试炼"},   
 {"귀살 자객 시련","35","鬼煞刺客试炼"},   
 {"구명여요의 관","35","九命女妖之冠"},   
 {"흑석 강도의 도장","35","黑石匪徒之章"},   
 {"무황 법사 시련","35","巫皇法师试炼"},   
 {"전륜 부하 시련","36","转轮座下试炼"},   
 {"노령 시련","36","怒灵试炼"},   
 {"입마영존의 장","36","入魔灵尊之杖"},   
 {"전륜 분신의 그림자","36","转轮分身之影"},   
 {"혈수 자객 시련","37","血手刺客试炼"},   
 {"얼정마 시련","37","孽情魔试炼"},   
 {"원혼마 시련","37","怨魂魔试炼"},   
 {"환혼초 수집","37","采集还魂草"},   
 {"쇄심마 시련","37","锁心魔试炼"},   
 {"간녕 소인 시련","38","奸佞小人试炼"},   
 {"흑석 강인 시련","38","黑石强人试炼"},   
 {"흑석 유혼의 영혼","38","黑石游魂之魄"},   
 {"흑성 여얼의 추","38","黑石余孽之锤"},   
 {"팔선초 수집","38","采集八仙草"},   
 {"첨취학 시련","38","尖嘴鹤试炼"},   
 {"수선 제자지단","39","修仙小道之丹"},   
 {"구혼 사자 시련","39","勾魂使者试炼"},   
 {"아호성의 껍질","39","饿虎星之皮"},   
 {"단목 수집","39","采集檀木"},   
 {"악몽지령 시련","39","噩梦之灵试炼"},   
 {"마천지록 시련","40","摩天之鹿试炼"},   
 {"강력한 비적 장도","40","强悍匪首长刀"},   
 {"야비 수령의 추","40","野匪头子之锤"},   
 {"줄기 수집","40","采集藤蔓"},   
 {"야생 사슴 시련","41","野鹿试炼"},   
 {"성격이 조급한 거웅 시련","41","暴躁巨熊试炼"},   
 {"야생 사슴뿔","41","野鹿之角"},   
 {"성격이 조급한 거웅의 껍질","41","暴躁巨熊毛皮"},   
 {"통배 원숭이 시련","42","通背猿试炼"},   
 {"금닭 관","42","锦鸡鸡冠"},   
 {"돼지 요괴 시련","43","猪妖试炼"},   
 {"돼지 정의 털","43","猪精之鬃"},   
 {"꿩 시련","44","雉鸡试炼"},   
 {"토비 시련","45","土匪试炼"},   
 {"광도 배낭","45","狂徒包裹"},   
 {"반란 창병 시련","46","叛乱枪兵试炼"},   
 {"배신 도끼병 시련","46","叛变斧兵试炼"},   
 {"강도 시련","47","江洋大盗试炼"},   
 {"거사괴 시련","48","巨蛇怪试炼"},   
 {"호랑이 요괴 껍질","48","虎妖毛皮"},   
 {"독행 늑대 시련","49","独行狼试炼"},   
 {"광랑눈","49","狂狼眼"},   
 {"양두괴 시련","50","羊头怪试炼"},   
 {"악구정 시련","50","恶狗精试炼"},   
 {"양두괴 뿔","50","羊头怪之角"},   
 {"악구정 꼬리","50","恶狗精之尾"},   
 {"칠면조정 깃털","51","火鸡精羽毛"},   
 {"선학 시련","51","仙鹤试炼"},   
 {"사악도사 시련","52","邪恶道士试炼"},   
 {"사악방사의 불진","52","邪恶方士拂尘"},   
 {"자금 검객 시련","53","紫金剑客试炼"},   
 {"칼자국 시련","53","刀疤客试炼"},   
 {"자금 검객 보검","53","紫金剑客宝剑"},   
 {"상고 흑곰 시련","54","上古黑熊试炼"},   
 {"상고 야생 돼지담","54","上古野猪之胆"},   
 {"상고 야생 돼지 시련","54","上古野猪试炼"},   
 {"상고 흑곰 발톱","54","上古黑熊之爪"},   
 {"상고 숫사슴 시련","55","上古雄鹿试炼"},   
 {"상고 양령 시련","56","上古羊灵试炼"},   
 {"공명자 시련","56","空冥者试炼"},   
 {"도랑자의 늑대 털","56","屠狼者狼毫"},   
 {"어수산선 시련","57","驭兽散仙试炼"},   
 {"팔선화 수집","57","采集八仙花"},   
 {"거호도인의 부적","57","驱虎道人道人护符"},   
 {"어수산선 선단","57","驭兽散仙仙丹"},   
 {"사악 교도 시련","58","邪恶教徒试炼"},   
 {"사악 교도 법기","58","邪恶教徒法器"},   
 {"사교 졸개 시련","58","邪教喽啰试炼"},   
 {"사교 졸개 허리띠","58","邪教喽啰腰带"},   
 {"사교 도사 시련","59","邪教道士试炼"},   
 {"도교 공범 시련","59","道教帮凶试炼"},   
 {"사교도사 옥기","59","邪教道士玉器"},   
 {"도교 공범 호패","59","道教帮凶护牌"},   
 {"입마 수련자 시련","60","入魔修士试炼"},   
 {"반적 호위 시련","61","叛贼侍卫试炼"},   
 {"반역 무사 시련","61","叛逆武士试炼"},   
 {"반적 호위 편지","61","叛贼侍卫信件"},   
 {"반군 수령 시련","62","叛军头领试炼"},   
 {"반군 호위병 시련","63","叛军卫士试炼"},   
 {"반군 매복병 시련","63","叛军伏兵试炼"},   
 {"반군 매복병 투구","63","叛军伏兵盔甲"},   
 {"쇄명사 시련","64","索命使试炼"},   
 {"흉신 시련","64","凶神试炼"},   
 {"흉신 영혼","64","凶神魂魄"},   
 {"유혼 사자 시련","65","幽魂使者试炼"},   
 {"망당귀 시련","65","莽撞鬼试炼"},   
 {"망당귀 원신","65","莽撞鬼元神"},   
 {"계관화 수집","65","采集鸡冠花"},   
 {"천도교 부적사 시련","66","天道教符师试炼"},   
 {"천도교 부적사 부적문","66","天道教符师符文"},   
 {"흑무상 시련","67","黑无常试炼"},   
 {"백무상 시련","67","白无常试炼"},   
 {"흑무상지령","67","黑无常之令"},   
 {"살령 시련","68","煞灵试炼"},   
 {"두령 선장","68","斗灵禅杖"},   
 {"두령 시련","68","斗灵试炼"},   
 {"무령 시련","69","武灵试炼"},   
 {"밥령마봉","69","法灵魔棒"},   
 {"사령지존 시령","69","四灵之尊试炼"},   
 {"악령 시련","70","恶灵试炼"},   
 {"환령 시련","70","幻灵试炼"},   
 {"환령 영혼","70","幻灵魂魄"},   
 {"신비한 살수 시련","71","神秘杀手试炼"},   
 {"매복 자격 비수","71","伏击刺客匕首"},   
 {"입공지도 시련","72","立功之徒试炼"},   
 {"명리지도의 패도","72","名利之徒佩剑"},   
 {"말면 수하 시련","73","马面喽啰试炼"},   
 {"미도여귀 시련","73","迷途女鬼试炼"},   
 {"풍류서생 망토","73","流寇披风"},   
 {"악귀 시련","74","恶鬼试炼"},   
 {"우도 졸개 시련","74","牛头喽啰试炼"},   
 {"사혼령의 혼","74","死魂灵之魂"},   
 {"사마 시련","75","邪魔试炼"},   
 {"망명비 시련","75","亡命匪试炼"},   
 {"사마지안","75","邪魔之眼"},   
 {"망명비 령패","75","亡命匪令牌"},   
 {"천도교 배신자 시련","76","天道教叛徒试炼"},   
 {"천도교 여당 시련","76","天道教余党试炼"},   
 {"천도교 신도의 두건","76","天道教信徒头巾"},   
 {"천도교 사자 시련","77","天道教使者试炼"},   
 {"천도교 문인 시련","77","天道教门人试炼"},   
 {"천도교 호위 갑옷","77","天道教护卫战甲"},   
 {"강호 협객 시련","78","江湖侠客试炼"},   
 {"유방 협객 시련","78","游方侠客试炼"},   
 {"기살 악귀 영혼","78","嗜杀恶鬼之魂"},   
 {"천도교 친위 시련","79","天道教亲卫试炼"},   
 {"후장령 시련","79","猴将领试炼"},   
 {"계병사의 관","79","鸡将军之冠"},   
 {"사마괴 시련","80","邪魔怪试炼"},   
 {"해골위사 시련","80","骷髅卫士试炼"},   
 {"악호 시련","81","恶虎试炼"},   
 {"미친 곰 시련","81","疯熊试炼"},   
 {"우두 호위 시련","82","牛头护卫试炼"},   
 {"부영화 수집","82","采集芙蓉花"},   
 {"말 얼굴 시련","83","马面试炼"},   
 {"조혼귀의 영혼","83","招魂鬼之魂"},   
 {"연옥 소귀 시련","84","炼狱小鬼试炼"},   
 {"발설귀의 혀","84","拔舌鬼只舌"},   
 {"유명초 수집","84","采集幽冥草"},   
 {"연옥 무상 시련","85","炼狱无常试炼"},   
 {"구혼귀 영혼","85","勾魂鬼魂魄"},   
 {"하병 시련","86","虾兵试炼"},   
 {"해장 시련","86","蟹将试炼"},   
 {"수족 병사의 심장","86","水族兵丁之心"},   
 {"수족 수하 시련","87","水族喽啰试炼"},   
 {"사마정 시련","87","邪魔精试炼"},   
 {"잔해 유귀의 못","87","浅海幽鬼之钉"},   
 {"요도지안","88","妖道之眼"},   
 {"야차 수정 시련","88","夜叉随从试炼"},   
 {"악몽 시련","88","梦魇试炼"},   
 {"탐해유혼 시련","89","探海幽魂试炼"},   
 {"수정 정예지경","89","水族精英之镜"},   
 {"하신병 시련","90","虾新兵试炼"},   
 {"원혼 시련","91","冤魂试炼"},   
 {"야귀 시련","92","野鬼试炼"},   
 {"천도교 잔마 시련","92","天道教残魔试炼"},   
 {"삼계 악령 시련","93","三界恶灵试炼"},   
 {"천도교 여얼 시련","94","天道教余孽试炼"},   
 {"미천록 시련","101","糜天鹿试炼"},   
 {"흑매금사 시련","102","黑眉锦蛇试炼"},   
 {"초관닭 시련","103","草冠鸡试炼"},   
 {"기혈 늑대 시련","104","嗜血狼试炼"},   
 {"춘매화 수집","104","采集春梅花"},   
 {"유령 자객 시련","105","幽灵刺客试炼"},   
 {"밀림 야인 시련","106","丛林野人试炼"},   
 {"금작화 수집","106","采集金雀花"},   
 {"운몽요정괴 시련","107","云梦精怪试炼"},   
 {"운몽 유귀 시련","108","云梦幽鬼试炼"},   
 {"동릉초 수집","108","采集冬凌草"},   
 {"밀림괴객 시련","109","密林怪客试炼"}
 };             
             
%>
		
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" href="../css/style.css" />
		<link rel="stylesheet" href="../css/atalk.css" />
		<script language="javascript" type="text/javascript" src="../js/flotr/lib/prototype-1.6.0.2.js"></script>
		<script language="javascript" type="text/javascript" src="../js/flotr/flotr-0.2.0-alpha.js"></script>
		<script language="JavaScript">
           function downloads(){
               $('form1').action="renwustat_download.jsp";
	           $('form1').submit();
            }
        </script>
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			支线任务完成统计
		</h2>
		<form   name="form1" id="form1" method="post">开始日期：<input type='text' name='day' value='<%=startDay %>'>
		-- 结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09)
		<br/><br/>
		       任务类型：<select name="taskType">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < taskTypeList.size() ; i++){
                       String _taskType = taskTypeList.get(i);
                       %>
                        <option value="<%=_taskType %>" 
                        <%
                        if(_taskType.equals(taskType)){
                        out.print(" selected=\"selected\"");
                        }
                         %>
                          ><%=_taskType %></option>
                       <%
                       }
                       %>
                      
                </select> &nbsp;&nbsp;
                                             分区：<select name="fenqu">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < fenQuList.size() ; i++){
                       String[] _fenqu = fenQuList.get(i);
                       %>
                        <option value="<%=_fenqu[1] %>" 
                        <%
                        if(_fenqu[1].equals(fenqu)){  out.print(" selected=\"selected\"");  }
                         %>
                          ><%=_fenqu[1] %></option>
                       <%
                       }
                       %>
                 </select>&nbsp;&nbsp;
                
		    		<input type='submit' value='提交'>
		    		</form>
		    		
		    		     <a href="renwustat_fenxi.jsp">主线任务热度统计</a>|
		    		     <a href="renwustat_fenxi_zhixian.jsp">支线任务热度统计</a>|
		    		     <a href="renwustat_fenxi_jingjie.jsp">境界任务热度统计</a>|
		    		
		    		<h3>任务完成统计</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>任务组名称</td><td>任务组别名</td><td>接受数</td><td>完成数</td><td>完成比</td></tr>");
					Long jieshousum=0L;
					Long wanchengsum=0L;
					
					for(int i = 0 ; i < taskstatList.size() ; i++){
					String[] taskstat=taskstatList.get(i);
					if(taskstat[1]!=null){jieshousum+=Long.parseLong(taskstat[1]);}
					if(taskstat[2]!=null){wanchengsum+=Long.parseLong(taskstat[2]);}
					}
					
					for(int j=0;j<renwuArray.length;j++){
					
					for(int i = 0 ; i < taskstatList.size() ; i++){
					String[] taskstat=taskstatList.get(i);
					Long bi=0L;
					if(taskstat[0]!=null&&taskstat[0].equals(renwuArray[j][0])){
				    if(taskstat[1]!=null&&!"0".equals(taskstat[1])&&taskstat[2]!=null){
				     bi=Long.parseLong(taskstat[2])*100/Long.parseLong(taskstat[1]);
				}
				
				out.print("<tr bgcolor='#FFFFFF'><td>"+taskstat[0]+"</td><td>"+renwuArray[j][2]+"</td><td>"+taskstat[1]+"</td><td>"+taskstat[2]+"</td><td>"+bi+"% &nbsp;</td>");
				out.println("</tr>");
					}
					}
					}
					
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>&nbsp;</td><td>"+jieshousum+"</td><td>"+wanchengsum+"</td><td>&nbsp</td></tr>");
					out.println("</table><br>");
		    		%>
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
