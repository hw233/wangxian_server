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
 {"恶徒试炼","12"},   
 {"猛蛇王之血","13"},   
 {"猛蛇王试炼","13"},   
 {"盗火贼试炼","14"},   
 {"山贼火把","15"},   
 {"情缘玫瑰","15"},   
 {"咆哮熊试炼","16"},   
 {"咆哮之胆","16"},   
 {"怒气来历","16"}, 
 {"怒气技能","16"}, 
 {"豪猪怪之血","17"},   
 {"斑斓猛虎试炼","17"},   
 {"九尾雉鸡之血","18"},   
 {"草原奔狼试炼","18"},   
 {"蛮猪灵之环","19"},   
 {"巨蛇之灵试炼","19"},   
 {"游荡野鬼之魂","20"},   
 {"游荡野鬼之魄","20"},   
 {"灵鬼试炼","21"},   
 {"灵鬼之魄","21"},   
 {"情魔试炼","22"},   
 {"撼心魔试炼","22"},   
 {"小隐邪魔之心","23"},   
 {"采集冬梅花","23"},   
 {"隐魔之血","23"},   
 {"无良游勇印章","24"},   
 {"凶狠匪帮试炼","24"},   
 {"黑石哨兵试炼","25"},   
 {"黑石护卫战甲","25"},   
 {"采集毒草","25"},   
 {"黑石小贼之矛","25"},   
 {"黑石新兵试炼","26"},   
 {"黑石中坚试炼","26"},   
 {"采集塔魄","26"},   
 {"黑石中坚战刀","26"},   
 {"黑石高手暗器","26"},   
 {"江湖高手盔甲","27"},   
 {"黑风怪试炼","27"},   
 {"黑石秘药","27"},   
 {"黑石拥趸铠甲","28"},   
 {"黑石精英试炼","28"},   
 {"采集塔魂","28"},   
 {"贴身丫鬟珠宝","28"},   
 {"黑石魔女试炼","29"},   
 {"风尘妖女试炼","29"},   
 {"采集降龙木","29"},   
 {"黑石鹰犬秘籍","30"},   
 {"迷踪幻魄试炼","30"},   
 {"堕落之影灰尘","30"},   
 {"邪影魔试炼","31"},   
 {"黑石走狗试炼","31"},   
 {"西域毒蛇之皮","31"},   
 {"包打听试炼","32"},   
 {"镇塔灵妖之魂","32"},   
 {"盘塔邪魔试炼","32"},   
 {"西域棕熊试炼","33"},   
 {"迷魂之胆","33"},   
 {"采集伏虎材","33"},   
 {"疯魔老者试炼","33"},   
 {"魔修野王试炼","34"},   
 {"斗罗力士之链","34"},   
 {"徘徊亡魂之魄","34"},   
 {"采集锁塔灵","34"},   
 {"灵尊真人试炼","35"},   
 {"鬼煞刺客试炼","35"},   
 {"九命女妖之冠","35"},   
 {"黑石匪徒之章","35"},   
 {"巫皇法师试炼","35"},   
 {"转轮座下试炼","36"},   
 {"怒灵试炼","36"},   
 {"入魔灵尊之杖","36"},   
 {"转轮分身之影","36"},   
 {"血手刺客试炼","37"},   
 {"孽情魔试炼","37"},   
 {"怨魂魔试炼","37"},   
 {"采集还魂草","37"},   
 {"锁心魔试炼","37"},   
 {"奸佞小人试炼","38"},   
 {"黑石强人试炼","38"},   
 {"黑石游魂之魄","38"},   
 {"黑石余孽之锤","38"},   
 {"采集八仙草","38"},   
 {"尖嘴鹤试炼","38"},   
 {"修仙小道之丹","39"},   
 {"勾魂使者试炼","39"},   
 {"饿虎星之皮","39"},   
 {"采集檀木","39"},   
 {"噩梦之灵试炼","39"},   
 {"摩天之鹿试炼","40"},   
 {"强悍匪首长刀","40"},   
 {"野匪头子之锤","40"},   
 {"采集藤蔓","40"},   
 {"野鹿试炼","41"},   
 {"暴躁巨熊试炼","41"},   
 {"野鹿之角","41"},   
 {"暴躁巨熊毛皮","41"},   
 {"通背猿试炼","42"},   
 {"锦鸡鸡冠","42"},   
 {"猪妖试炼","43"},   
 {"猪精之鬃","43"},   
 {"雉鸡试炼","44"},   
 {"土匪试炼","45"},   
 {"狂徒包裹","45"},   
 {"叛乱枪兵试炼","46"},   
 {"叛变斧兵试炼","46"},   
 {"江洋大盗试炼","47"},   
 {"巨蛇怪试炼","48"},   
 {"虎妖毛皮","48"},   
 {"独行狼试炼","49"},   
 {"狂狼眼","49"},   
 {"羊头怪试炼","50"},   
 {"恶狗精试炼","50"},   
 {"羊头怪之角","50"},   
 {"恶狗精之尾","50"},   
 {"火鸡精羽毛","51"},   
 {"仙鹤试炼","51"},   
 {"邪恶道士试炼","52"},   
 {"邪恶方士拂尘","52"},   
 {"紫金剑客试炼","53"},   
 {"刀疤客试炼","53"},   
 {"紫金剑客宝剑","53"},   
 {"上古黑熊试炼","54"},   
 {"上古野猪之胆","54"},   
 {"上古野猪试炼","54"},   
 {"上古黑熊之爪","54"},   
 {"上古雄鹿试炼","55"},   
 {"上古羊灵试炼","56"},   
 {"空冥者试炼","56"},   
 {"屠狼者狼毫","56"},   
 {"驭兽散仙试炼","57"},   
 {"采集八仙花","57"},   
 {"驱虎道人道人护符","57"},   
 {"驭兽散仙仙丹","57"},   
 {"邪恶教徒试炼","58"},   
 {"邪恶教徒法器","58"},   
 {"邪教喽啰试炼","58"},   
 {"邪教喽啰腰带","58"},   
 {"邪教道士试炼","59"},   
 {"道教帮凶试炼","59"},   
 {"邪教道士玉器","59"},   
 {"道教帮凶护牌","59"},   
 {"入魔修士试炼","60"},   
 {"叛贼侍卫试炼","61"},   
 {"叛逆武士试炼","61"},   
 {"叛贼侍卫信件","61"},   
 {"叛军头领试炼","62"},   
 {"叛军卫士试炼","63"},   
 {"叛军伏兵试炼","63"},   
 {"叛军伏兵盔甲","63"},   
 {"索命使试炼","64"},   
 {"凶神试炼","64"},   
 {"凶神魂魄","64"},   
 {"幽魂使者试炼","65"},   
 {"莽撞鬼试炼","65"},   
 {"莽撞鬼元神","65"},   
 {"采集鸡冠花","65"},   
 {"天道教符师试炼","66"},   
 {"天道教符师符文","66"},   
 {"黑无常试炼","67"},   
 {"白无常试炼","67"},   
 {"黑无常之令","67"},   
 {"煞灵试炼","68"},   
 {"斗灵禅杖","68"},   
 {"斗灵试炼","68"},   
 {"武灵试炼","69"},   
 {"法灵魔棒","69"},   
 {"四灵之尊试炼","69"},   
 {"恶灵试炼","70"},   
 {"幻灵试炼","70"},   
 {"幻灵魂魄","70"},   
 {"神秘杀手试炼","71"},   
 {"伏击刺客匕首","71"},   
 {"立功之徒试炼","72"},   
 {"名利之徒佩剑","72"},   
 {"马面喽啰试炼","73"},   
 {"迷途女鬼试炼","73"},   
 {"流寇披风","73"},   
 {"恶鬼试炼","74"},   
 {"牛头喽啰试炼","74"},   
 {"死魂灵之魂","74"},   
 {"邪魔试炼","75"},   
 {"亡命匪试炼","75"},   
 {"邪魔之眼","75"},   
 {"亡命匪令牌","75"},   
 {"天道教叛徒试炼","76"},   
 {"天道教余党试炼","76"},   
 {"天道教信徒头巾","76"},   
 {"天道教使者试炼","77"},   
 {"天道教门人试炼","77"},   
 {"天道教护卫战甲","77"},   
 {"江湖侠客试炼","78"},   
 {"游方侠客试炼","78"},   
 {"嗜杀恶鬼之魂","78"},   
 {"天道教亲卫试炼","79"},   
 {"猴将领试炼","79"},   
 {"鸡将军之冠","79"},   
 {"邪魔怪试炼","80"},   
 {"骷髅卫士试炼","80"},   
 {"恶虎试炼","81"},   
 {"疯熊试炼","81"},   
 {"牛头护卫试炼","82"},   
 {"采集芙蓉花","82"},   
 {"马面试炼","83"},   
 {"招魂鬼之魂","83"},   
 {"炼狱小鬼试炼","84"},   
 {"拔舌鬼只舌","84"},   
 {"采集幽冥草","84"},   
 {"炼狱无常试炼","85"},   
 {"勾魂鬼魂魄","85"},   
 {"虾兵试炼","86"},   
 {"蟹将试炼","86"},   
 {"水族兵丁之心","86"},   
 {"水族喽啰试炼","87"},   
 {"邪魔精试炼","87"},   
 {"浅海幽鬼之钉","87"},   
 {"妖道之眼","88"},   
 {"夜叉随从试炼","88"},   
 {"梦魇试炼","88"},   
 {"探海幽魂试炼","89"},   
 {"水族精英之镜","89"},   
 {"虾新兵试炼","90"},   
 {"冤魂试炼","91"},   
 {"野鬼试炼","92"},   
 {"天道教残魔试炼","92"},   
 {"三界恶灵试炼","93"},   
 {"天道教余孽试炼","94"},   
 {"糜天鹿试炼","101"},   
 {"黑眉锦蛇试炼","102"},   
 {"草冠鸡试炼","103"},   
 {"嗜血狼试炼","104"},   
 {"采集春梅花","104"},   
 {"幽灵刺客试炼","105"},   
 {"丛林野人试炼","106"},   
 {"采集金雀花","106"},   
 {"云梦精怪试炼","107"},   
 {"云梦幽鬼试炼","108"},   
 {"采集冬凌草","108"},   
 {"密林怪客试炼","109"}   
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
					out.print("<tr bgcolor='#EEEEBB'><td>任务组名称</td><td>接受数</td><td>完成数</td><td>完成比</td><td>下级人任务接受数</td></tr>");
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
				
				out.print("<tr bgcolor='#FFFFFF'><td>"+taskstat[0]+"</td><td>"+taskstat[1]+"</td><td>"+taskstat[2]+"</td><td>"+bi+"% &nbsp;</td><td>"+taskstat[3]+"</td>");
				out.println("</tr>");
					}
					}
					}
					
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>"+jieshousum+"</td><td>"+wanchengsum+"</td><td>&nbsp</td><td>&nbsp</td></tr>");
					out.println("</table><br>");
		    		%>
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
