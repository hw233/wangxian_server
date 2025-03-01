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
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的分区信息
              taskTypeList=taskinfoManager.gettaskType(null);//任务类型
             List<String[]>  taskstatList= taskAnalysisManager.getTaskStat(startDay,endDay,fenqu,null);
             
             String renwuArray[][]={
				
 {"肩负使命","1"},      
 {"再救苍生","2"},   
 {"喜获神兵","2"},   
 {"宝甲护身","3"},   
 {"支援同道","3"},   
 {"证明实力","4"},   
 {"风林火山","4"},   
 {"锦囊妙计","5"},   
 {"一技傍身","5"},   
 {"助拳斗罗","6"},   
 {"义救灵尊","6"},   
 {"驰援鬼煞","7"},   
 {"营救巫皇","7"},   
 {"清理爪牙","8"},   
 {"美色诱惑","8"},   
 {"险成大错","8"},   
 {"恭候大驾","9"},   
 {"剿灭狐妖","9"},   
 {"除妖武器","10"},  
 {"鼎力相助","10"},  
 {"保管财物","10"},  
 {"邮件信使","10"},  
 {"仙宠达人","10"},  
 {"捕捉仙宠","10"},  
 {"仙宠加点","10"},  
 {"可信之人","10"},  
 {"略尽责任","11"},  
 {"似曾相识","11"},  
 {"寻鞋拜师","11"},  
 {"博采众家","12"},  
 {"身体力行","12"},  
 {"画符镇妖","12"},  
 {"尝尽百草","12"},  
 {"打草惊蛇","13"},  
 {"投桃报李","13"},  
 {"悟性非凡","13"},  
 {"万灵之长","14"},  
 {"刀耕火种","14"},  
 {"力擎苍生","14"},  
 {"伺机反扑","14"},  
 {"非同小可","14"},  
 {"神器材料","15"},  
 {"传道功成","15"},  
 {"八卦创世","15"},  
 {"战败魔狼","15"},  
 {"素昧平生","16"},  
 {"野兽咆哮","16"},  
 {"镇鬼灵丹","16"},  
 {"慧眼初开","16"},  
 {"不世之功","16"},  
 {"尘缘未了","16"},  
 {"贻误时机","17"},  
 {"安抚民心","17"},  
 {"敲山震虎","17"},  
 {"情深之术","17"},  
 {"致命毒药","17"},  
 {"天师名门","17"},  
 {"顺应天数","18"},  
 {"门规考验","18"},  
 {"人才济济","18"},  
 {"飞禽之翎","18"},  
 {"勤学苦练","18"},  
 {"名利责任","18"},  
 {"拜师资格","18"},  
 {"登堂弟子","19"},  
 {"乾坤逆转","19"},  
 {"再三刁难","19"},  
 {"混元玄铁","19"},  
 {"不知所终","19"},  
 {"难以置信","19"},  
 {"勇往直前","19"},  
 {"收放自如","19"},  
 {"独辟蹊径","20"},  
 {"妖孽横行","20"},  
 {"眼界狭窄","20"},  
 {"循序渐进","20"},  
 {"自立门户","20"},  
 {"难解情缘","20"},  
 {"大事不妙","21"},  
 {"藏身之处","21"},  
 {"结识陆虚","21"},  
 {"灵鬼肆虐","21"},  
 {"灵鬼魂魄","21"},  
 {"再施援手","22"},  
 {"见面之礼","22"},  
 {"五常法器","22"},  
 {"古道热肠","22"},  
 {"聚灵奇阵","22"},  
 {"阵中奥秘","22"},  
 {"人多势众","22"},  
 {"宝石巨匠","22"},  
 {"装备提升","22"},  
 {"回复高僧","22"},  
 {"争取时间","22"},  
 {"法器大成","22"},  
 {"求教方丈","22"},  
 {"前恶复来","23"},  
 {"命中注定","23"},  
 {"清心普善","23"},  
 {"非常女子","23"},  
 {"护寺安宁","23"},  
 {"易容高手","23"},  
 {"显形追踪","23"},  
 {"缺一不可","23"},  
 {"无良游勇","24"},  
 {"仗义出手","24"},  
 {"帮人到底","24"},  
 {"风尘仆仆","24"},  
 {"手到擒来","24"},  
 {"见贤思齐","24"},  
 {"珍稀药材","24"},  
 {"据实相告","24"},  
 {"黑石哨兵","25"},  
 {"不知去向","25"},  
 {"神行百变","25"},  
 {"掩人耳目","25"},  
 {"化妆潜入","25"},  
 {"放心大胆","25"},  
 {"临别赠言","25"},  
 {"黑石组织","26"},  
 {"树大招风","26"},  
 {"胜者为王","26"},  
 {"三大护法","26"},  
 {"不该低估","26"},  
 {"可接重任","26"},  
 {"进步惊人","26"},  
 {"史上最强","26"},  
 {"毫无瓜葛","27"},  
 {"来龙去脉","27"},  
 {"戏法大师","27"},  
 {"高端手法","27"},  
 {"足够精彩","27"},  
 {"毫无下落","27"},  
 {"前浪后浪","27"},  
 {"不予追究","27"},  
 {"所谓正义","28"},  
 {"能者多劳","28"},  
 {"一窍不通","28"},  
 {"惊扰美梦","28"},  
 {"当面再试","28"},  
 {"不经允许","28"},  
 {"既来则安","28"},  
 {"说到做到","28"},  
 {"眼前一亮","28"},  
 {"黑石密探","29"},  
 {"优胜劣汰","29"},  
 {"蛇蝎美女","29"},  
 {"美色蛊惑","29"},  
 {"威逼利诱","29"},  

{"斗罗来历","29"}, 
{"斗罗挑战","29"}, 
{"斗罗秘密","29"}, 
{"斗罗坐骑","29"}, 

 {"争锋吃醋","29"},  
 {"出笼猛兽","29"},  
 {"元神亏损","29"},  
 {"绽青绝技","29"},  
 {"苦苦相逼","30"},  
 {"源源不断","30"},  
 {"前尘往事","30"},  
{"卧底相托","30"},  
{"继续使命","30"},  
 {"黑石追兵","30"},  
 {"一网打尽","30"},  
 {"个人行为","30"},  
 {"有来无回","30"},  
 {"桃源后援","30"},  
 {"亡命天涯","31"},  
 {"穷追不舍","31"},  
 {"挡我者死","31"},  
 {"毒蛇猛兽","31"},  
 {"更有胜算","31"},  
 {"各有苦衷","31"},  
 {"一念报仇","32"},  
 {"前仇旧恨","32"},  
 {"不再相见","32"},  
 {"躲过一劫","32"},  
 {"以身为饵","32"},  
 {"无人能敌","32"},  
 {"漏网之鱼","32"},  
 {"九牛一毛","32"},  
 {"不见天日","32"},  
 {"尽力而为","32"},  
 {"卧虎藏龙","32"},  
 {"虎口拔牙","32"},  
 {"佛道联手","32"},  
 {"万兽法阵","33"},  
 {"负隅顽抗","33"},  
 {"驭兽恶灵","33"},  
 {"神仙眷侣","33"},  
 {"屡禁不止","33"},  
 {"黑石动作","33"},  
 {"并肩作战","33"},  
 {"恶鬼来袭","33"},  
 {"佛光普照","33"},  
 {"西域势力","33"},  
 {"暗地勾结","33"},  
 {"邪神力量","34"},  
 {"人中龙凤","34"},  
 {"黑云压城","34"},  
 {"恭候多时","34"},  
 {"皆有定数","34"},  
 {"战斗使命","34"},  
 {"黑石密件","34"},  
 {"层层杀机","34"},  
 {"步步惊心","34"},  
 {"乔装打扮","34"},  
 {"养兵千日","35"},  
 {"孤陋寡闻","35"},  
 {"一统江山","35"},  
 {"逆转强敌","35"},  
 {"转轮之力","35"},  
 {"荣华富贵","35"},  
 {"正确选择","35"},  
 {"训练门人","35"},  
 {"报仇雪恨","35"},  
 {"黑石近侍","35"},  
 {"进见资格","35"},  
 {"见证实力","36"},  
 {"美言夸赞","36"},  
 {"迎刃而解","36"},  
 {"饮水思源","36"},  
 {"改变未来","36"},  
 {"怨灵出窍","36"},  
 {"回归本心","36"},  
 {"分身解体","36"},  
 {"难逆大势","36"},  
 {"破碎虚空","36"},  
 {"不惜代价","36"},  
 {"本来面目","37"},  
 {"党羽众多","37"},  
 {"人非草木","37"},  
 {"前生注定","37"},  
 {"盘桓不去","37"},  
 {"驱赶心魔","37"},  
 {"生死相许","37"},  
 {"安得双全","37"},  
 {"决不罢休","37"},  
 {"铲除羽翼","37"},  
 {"黑石集结","37"},  
 {"三心纠缠","37"},  
 {"徒劳生命","38"},  
 {"长相厮守","38"},  
 {"天涯亡命","38"},  
 {"一见倾心","38"},  
 {"性格锋利","38"},  
 {"日夜诵经","38"},  
 {"黑石游魂","38"},  
 {"故人一面","38"},  
 {"加以制裁","38"},  
 {"野心三步","38"},  
 {"惑心之术","38"},  
 {"后继有力","38"},  
 {"三种物资","38"},  
 {"非同凡响","38"},  
 {"制造神器","38"},  
 {"霹雳雷鸣","39"},  
 {"威力惊人","39"},  
 {"强词夺理","39"},  
 {"世人三毒","39"},  
 {"神马浮云","39"},  
 {"劝说收买","39"},  
 {"皆是徒劳","39"},  
 {"勇不可挡","39"},  
 {"饿虎之袭","39"},  
 {"地狱王者","39"},  
 {"勾魂之力","39"},  
 {"同命相连","39"},  
 {"噩梦不断","39"},  
 {"修仙境界","39"},  
 {"坚定正义","39"},  
 {"同道中人","39"},  
 {"运功疗伤","39"},  
 {"朝圣之地","39"},  
 {"草寇作乱","40"},  
 {"罪魁祸首","40"},  
 {"问道于贤","40"},  
 {"激发灵力","40"},  
 {"车匪路霸","40"},  
 {"太平之路","40"},  
 {"王城问道","41"},  
 {"有能之人","41"},  
 {"捍卫天尊","41"},  
 {"修炼提升","41"},  
 {"国内奇珍","41"},  
 {"国外异宝","41"},  
 {"情义无价","41"},  
 {"同道好友","41"},  
 {"缘定今生","41"},  
 {"天尊之命","41"},  
 {"无济于事","41"},  
 {"野鹿躁动","41"},  
 {"在劫难逃","41"},  
 {"一起承担","41"},  
 {"分忧解难","41"},  
 {"殒身无悔","41"},  
 {"师叔李默","42"},  
 {"李默考验","42"},  
 {"收集猴耳","42"},  
 {"野兽癫狂","42"},  
 {"拜见李耳","42"},  
 {"道德妙用","42"},  
 {"安神明智","42"},  
 {"影响巨大","42"},  
 {"必经之路","42"},  
 {"玄鸟回忆","42"},  
 {"玄鸟担忧","43"},  
 {"玄鸟委托","43"},  
 {"微薄之力","43"},  
 {"制作皮衣","43"},  
 {"结识红虎","43"},  
 {"帮助村民","43"},  
 {"红虎疑惑","43"},  
 {"红虎回忆","43"},  
 {"难逃大难","43"},  
 {"请教药王","43"},  
 {"怪病难医","43"},  
 {"药王愧疚","44"},  
 {"药王深思","44"},  
 {"药王至交","44"},  
 {"保卫仙府","44"},  
 {"解药药引","44"},  
 {"学医道人","44"},  
 {"静心驱魔","44"},  
 {"前世积德","44"},  
 {"赢得好感","44"}, 
 {"大事相求","44"}, 
 {"事必亲为","44"}, 
 {"心如止水","45"}, 
 {"发怒医仙","45"}, 
 {"教训土匪","45"}, 
 {"回复医仙","45"}, 
 {"嫉恶如仇","45"}, 
 {"神秘药引","45"}, 
 {"一举两得","45"}, 
 {"苍穹之寒","45"}, 
 {"保管道德","46"}, 
 {"清除叛军","46"}, 
 {"叛军斧手","46"}, 
 {"清剿归来","46"}, 
 {"支援尹喜","46"}, 
 {"引注踪迹","46"}, 
 {"军用物资","46"}, 
 {"击杀叛军","46"}, 
 {"叛乱原因","46"}, 
 {"收集药品","46"}, 
 {"尽忠报国","46"}, 
 {"绝密文件","47"}, 
 {"樵夫身份","47"}, 
 {"除恶扬善","47"}, 
 {"被劫粮食","47"}, 
 {"金角银角","47"}, 
 {"神秘之人","47"}, 
 {"强悍土匪","47"}, 
 {"抢回包裹","47"}, 
 {"青云直上","47"}, 
 {"展现实力","47"}, 
 {"子虚物资","47"}, 
 {"官府卧底","47"}, 
 {"蒙混过关","48"}, 
 {"子虚推荐","48"}, 
 {"金角兄弟","48"}, 
 {"引注下落","48"}, 
 {"银角态度","48"}, 
 {"银角刁难","48"}, 
 {"银角密信","48"}, 
 {"猖狂巨蛇怪","48"},
 {"建立仙府","48"}, 
 {"霸下传说","48"}, 
 {"比武准备","48"}, 
 {"调侃银角","48"}, 
 {"绝色美女","48"}, 
 {"惩治黑山","48"}, 
 {"夺回宝珠","49"}, 
 {"玄奘法师","49"}, 
 {"聪明罗刹","49"}, 
 {"善良沙弥","49"}, 
 {"凶恶独行","49"}, 
 {"沙弥担心","49"}, 
 {"佛门大事","49"}, 
 {"找回通牒","49"}, 
 {"独门密锁","49"}, 
 {"乌金廿锁","49"}, 
 {"机关密钥","49"}, 
 {"击败银角","49"}, 
 {"击败金角","49"}, 
 {"事不宜迟","50"}, 
 {"道德真意","50"}, 
 {"大道相通","50"}, 
 {"疗伤草药","50"}, 
 {"宝物归来","50"}, 
 {"扫清路障","50"}, 
 {"救出玄奘","50"}, 
 {"风尘三侠","50"}, 
 {"初闻南华","50"}, 
 {"全本道德","51"}, 
 {"老仙轻视","51"}, 
 {"修道之行","51"}, 
 {"用心悟道","51"}, 
 {"老仙指引","51"}, 
 {"群匪无首","51"}, 
 {"夺回布匹","51"}, 
 {"为情所困","51"}, 
 {"难舍情缘","51"}, 
 {"救助李靖","52"}, 
 {"李靖请求","52"}, 
 {"灵丹妙药","52"}, 
 {"远大理想","52"}, 
 {"红拂怒气","52"}, 
 {"表示关心","52"}, 
 {"李靖回绝","52"}, 
 {"讨要饰物","52"}, 
 {"真铁如意","52"}, 
 {"回赠香囊","52"}, 
 {"忘魂之花","53"}, 
 {"红拂有信","53"}, 
 {"欲成大事","53"}, 
 {"虬髯考验","53"}, 
 {"收集金沙","53"}, 
 {"收集银沙","53"}, 
 {"北地之冰","54"}, 
 {"三者缺一","54"}, 
 {"时空扭曲","54"}, 
 {"千钧之担","54"}, 
 {"设法保温","54"}, 
 {"相助仙友","54"}, 
 {"联系东西","54"}, 
 {"上古雄鹿","55"}, 
 {"大有好处","55"}, 
 {"百姓解忧","55"}, 
 {"帮助苍生","55"}, 
 {"收集野参","55"}, 
 {"收集清丸","55"}, 
 {"阐教高人","55"}, 
 {"全力相助","56"}, 
 {"上古羊灵","56"}, 
 {"羊灵帮凶","56"}, 
 {"夺回灵气","56"}, 
 {"收集百味","56"}, 
 {"送搜神记","56"}, 
 {"送天刑剑","56"}, 
 {"礼尚往来","56"}, 
 {"拜访子牙","56"}, 
 {"门户之争","56"}, 
 {"散仙灵药","57"}, 
 {"问候公豹","57"}, 
 {"同门有仇","57"}, 
 {"子牙戒指","57"}, 
 {"心情舒畅","57"}, 
 {"离人之冷","57"}, 
 {"号令豪杰","57"}, 
 {"封赏武吉","57"}, 
 {"尽忠姬昌","57"}, 
 {"步步危机","57"}, 
 {"多加小心","58"}, 
 {"邪教喽啰","58"}, 
 {"邪恶教徒","58"}, 
 {"研究药方","58"}, 
 {"参透秘密","58"}, 
 {"贪狼之影","58"}, 
 {"镇魂使者","58"}, 
 {"收集魂令","58"}, 
 {"千丝万缕","58"}, 
 {"天生相克","58"}, 
 {"易经心得","58"}, 
 {"挚友南宫","58"}, 
 {"邪教道士","59"}, 
 {"变异蝮蛇","59"}, 
 {"蝮蛇之胆","59"}, 
 {"万事俱备","59"}, 
 {"商纣奸细","59"}, 
 {"作何反应","59"}, 
 {"有缘无份","59"}, 
 {"制作盔缨","59"}, 
 {"商纣妖孽","59"}, 
 {"断绝此患","59"}, 
 {"组织反商","59"}, 
 {"问清缘由","59"}, 
 {"心灰意冷","59"}, 
 {"推翻商纣","60"}, 
 {"悲鸣之鹤","60"}, 
 {"入魔修士","60"}, 
 {"摄魂利器","60"}, 
 {"留恋红尘","60"}, 
 {"摄魂魔王","60"}, 
 {"鬼气弥漫","60"}, 
 {"敌军喽啰","60"}, 
 {"小心应付","60"}, 
 {"阻挠天道","60"}, 
 {"息息相关","61"}, 
 {"叛贼侍卫","61"}, 
 {"苦修之丹","61"}, 
 {"拜访李玄","61"}, 
 {"深信不疑","61"}, 
 {"结成金丹","61"}, 
 {"静心修炼","61"}, 
 {"李玄担忧","61"}, 
 {"通知李玄","62"}, 
 {"敌军士兵","62"}, 
 {"敌军头目","62"}, 
 {"退还书信","62"}, 
 {"敌军将领","62"}, 
 {"叛军头领","62"}, 
 {"贡献力量","62"}, 
 {"青梅竹马","62"}, 
 {"始料未及","62"}, 
 {"最后托付","62"}, 
 {"势头正劲","62"}, 
 {"了无牵挂","63"}, 
 {"叛军卫士","63"}, 
 {"李家图谱","63"}, 
 {"神秘高人","63"}, 
 {"鬼岭女妖","63"}, 
 {"教训手下","63"}, 
 {"抢药吃药","63"}, 
 {"死亡证据","63"}, 
 {"应得报应","63"}, 
 {"元神出窍","63"}, 
 {"钟离将军","63"}, 
 {"天道爪牙","64"}, 
 {"落魄士兵","64"}, 
 {"从严治军","64"}, 
 {"将军愤怒","64"}, 
 {"敌军头颅","64"}, 
 {"难以突破","64"}, 
 {"救治伤员","64"}, 
 {"缓解压力","64"}, 
 {"突出重围","64"}, 
 {"死战破敌","64"}, 
 {"朝廷要员","64"},           
 {"后援无助","64"},           
 {"驱散幽魂使者","65"},       
 {"打败躁动鬼","65"},         
 {"取回宝玉","65"},           
 {"兵分两路","65"},           
 {"钟离豪气","65"},           
 {"近在咫尺","65"},           
 {"一鼓作气","65"},           
 {"最后屏障","65"},           
 {"阴阳界官","65"},           
 {"捏一把汗","65"},           
 {"一心求仙","65"},           
 {"采和揭秘","66"},           
 {"教训邪师","66"},           
 {"必受磨难","66"},           
 {"贪财之鬼","66"},           
 {"一封家书","66"},           
 {"蓝父态度","66"},           
 {"天道符师","66"},           
 {"辅助丹药","66"},           
 {"老幼有别","66"},           
 {"送药果老","67"},           
 {"果老担忧","67"},           
 {"战黑无常","67"},           
 {"战白无常","67"},           
 {"一生老实","67"},           
 {"灵魂火种","67"},           
 {"果老家信","67"},           
 {"回心转意","67"},           
 {"难忘女子","67"},           
 {"情深之伤","68"},           
 {"由爱生怨","68"},           
 {"煞魂之影","68"},           
 {"师徒之情","68"},           
 {"暗生情愫","68"},           
 {"痴情女子","68"},           
 {"无心逃脱","68"},           
 {"击败斗灵","68"},           
 {"斗魂之影","68"},           
 {"枉死之魂","69"},           
 {"打败法灵","69"},           
 {"挫败武灵","69"},           
 {"多层磨难","69"},           
 {"夺回元神","69"},           
 {"纯阳往事","69"},           
 {"一半玉佩","69"},           
 {"难舍孽缘","69"},           
 {"收集元神","69"},           
 {"真实原因","70"},           
 {"坦然面对","70"},           
 {"击杀幻灵","70"},           
 {"铲除恶灵","70"},           
 {"无常恶鬼","70"},           
 {"援助官员","71"},          
{"救助韩愈","71"},
{"虎视眈眈","71"},
{"抢夺暗器","71"},
{"韩愈苦衷","71"},
{"减轻痛苦","71"},
{"侄儿关心","71"},
{"善意谎言","71"},
{"武力震慑","71"},
{"著作师说","71"},
{"亡命江湖","71"},
{"官场黑暗","72"},
{"修仙之道","72"},
{"战胜自己","72"},
{"两大缺点","72"},
{"名利之心","72"},
{"屠戮令牌","72"},
{"一探究竟","72"},
{"胡作非为","72"},
{"身份证明","72"},
{"大人手令","72"},
{"魂归地府","73"},
{"流寇祸乱","73"},
{"地狱使者","73"},
{"稀有火石","73"},
{"光明火把","73"},
{"缉拿小六","73"},
{"邪魔附体","73"},
{"辨别真伪","73"},
{"慈悲受困","73"},
{"国舅求援","74"},
{"解锁钥匙","74"},
{"铲除后患","74"},
{"营救国舅","74"},
{"妖术之锁","74"},
{"强敌众多","74"},
{"解除禁制","74"},
{"拖延时间","74"},
{"国舅笏板","74"},
{"击杀邪魔","75"},
{"生死与共","75"},
{"亡命之匪","75"},
{"准备就绪","75"},
{"控制其身","75"},
{"天道令牌","75"},
{"百世邪魔","75"},
{"道教叛徒","75"},
{"收回令牌","75"},
{"怀疑对象","75"},
{"誓死效忠","75"},
{"组织贡献","75"},
{"削弱实力","75"},
{"假冒拂尘","75"},
{"天道拂尘","75"},
{"终获信任","75"},
{"天道护符","75"},

 {"难得人才","77"},
 {"志同道合","77"},
 {"终极目标","77"},
 {"降低危险","77"},
 {"收集凭证","77"},
 {"元神回归","77"},
 {"杀人灭口","77"},
 {"护卫腰牌","77"},
 {"神机妙算","77"},
 {"阻止阴谋","77"},
 {"肃清叛贼","78"},
 {"献血献祭","78"},
 {"江湖侠客","78"},
 {"忠犬之血","78"},
 {"琐碎之事","78"},
 {"帮助道教","78"},
 {"恶鬼元神","78"},
 {"护法疑心","78"},
 {"强力内应","78"},
 {"准提道人","79"},
 {"修仙奇才","79"},
 {"击败将军","79"},
 {"收集信件","79"},
 {"证明身份","79"},
 {"借口敷衍","79"},
 {"收集红丸","79"},
 {"回报护法","79"},
 {"追杀准提","79"},
 {"阐教法器","79"},
 {"极力推荐","80"},
 {"收集元气","80"},
 {"再收元气","80"},
 {"收集鬼骨","80"},
 {"邪魔灵药","80"},
 {"偷梁换柱","80"},
 {"是非论道","80"},
 {"天道法师","80"},
 {"相助女妖","81"},
 {"劝解小青","81"},
 {"小青关心","81"},
 {"妖兽祸乱","81"},
 {"夜观天象","82"},
 {"牛头拦路","82"},
 {"凡心大动","82"},
 {"一心向道","82"},
 {"素贞雨伞","82"},
 {"放弃道行","82"},
 {"萌生退意","82"},
 {"许仙之死","82"},
 {"小青玉镯","83"},
 {"回魂丹药","83"},
 {"凡人之体","83"},
 {"击败马面","83"},
 {"招魂之幡","83"},
 {"试图还魂","83"},
 {"勾掉生死","83"},
 {"小青宝剑","84"},
 {"另一办法","84"},
 {"借取仙草","84"},
 {"炼狱黒木","84"},
 {"为人正直","84"},
 {"取炼狱果","84"},
 {"白鹿仙毫","85"},
 {"炼狱朱砂","85"},
 {"白鹿鹿茸","85"},
 {"白鹿决定","85"},
 {"丢失仙草","85"},
 {"抢回仙草","85"},
 {"服用仙草","85"},
 {"夫妻失散","85"},
 {"战败法轮","85"},
 {"泾河龙王","86"},
 {"棘手难题","86"},
 {"教训虾兵","86"},
 {"教训蟹将","86"},
 {"龙宫至宝","86"},
 {"年轻气盛","86"},
 {"水族喽啰","87"},
 {"前因后果","87"},
 {"敬德回应","87"},
 {"转达敬意","87"},
 {"邪魔妖道","87"},
 {"驱魔药散","87"},
 {"寻找魏征","87"},
 {"事有蹊跷","87"},
 {"敬德保证","87"},
 {"化刃斩龙","88"},
 {"夜叉随从","88"},
 {"魏征担忧","88"},
 {"解除困意","88"},
 {"梦境秘石","88"},
 {"命数如此","88"},
 {"眼前之事","89"},
 {"收集水棒","89"},
 {"智囊丞相","89"},
 {"大病未愈","89"},
 {"龙王手令","89"},
 {"玄龟之甲","89"},
 {"又见丞相","90"},
 {"收集灵药","90"},
 {"水漫金山","90"},
 {"寂灭高人","90"},
 {"法海元神","90"},
 {"追究责任","91"},
 {"帮助素贞","91"},
 {"死忠魂魄","91"},
 {"寻找素贞","91"},
 {"素贞态度","91"},
 {"超度冤魂","92"},
 {"击败野鬼","92"},
 {"天道残魔","92"},
 {"下不为例","92"},
 {"法海禅杖","92"},
 {"替妹求情","92"},
 {"转告青蛇","93"},
 {"三界恶灵","93"},
 {"收集魔牌","93"},
 {"提前准备","94"},
 {"苍生百姓","94"},
 {"天道余孽","94"},
 {"问清原由","95"},
 {"四方妖魔","95"},
 {"接引道人","95"},
 {"巡游夜叉","95"},
 {"三界妖王","96"},
 {"幕后真凶","96"},
 {"可造之材","96"},
 {"尽力弥补","96"},
 {"身中剧毒","96"},
 {"元婴爆炸","96"},
 {"封锁魂魄","97"},
 {"深海恶灵","97"},
 {"寻找药材","97"},
 {"三界乱世","97"},
 {"太白化身","98"},
 {"蹈海魔灵","98"},
 {"元婴之毒","98"},
 {"灭世之谜","98"},
 {"邪魔灭世","99"},
 {"深海水妖","99"},
 {"灭世之鼎","99"},
 {"邪魔之散","99"},
 {"不复万劫","100"},
 {"合体之境","100"},
 {"恶灵魔君","100"},
 {"修仙坦途","101"},
 {"噬天之虎","101"},
 {"伏羲之言","101"},
 {"龙虎之气","102"},
 {"暴力之熊","102"},
 {"性命之忧","102"},
 {"请教伏羲","103"},
 {"菱斑之蛇","103"},
 {"草冠之羽","103"},
 {"超乎想象","104"},
 {"嗜血之狼","104"},
 {"麋鹿之角","104"},
 {"幽灵刺客","105"},
 {"结识颜回","105"},
 {"失之交臂","106"},
 {"丛林野人","106"},
 {"心系百姓","106"},
 {"家师文圣","107"},
 {"沼泽幽灵","107"},
 {"斩奸屠魔","107"},
 {"世出奇才","108"},
 {"云梦精怪","108"},
 {"七彩云梦","108"},
 {"隐匿未交","109"},
 {"密林怪客","109"},
 {"密林之甲","109"},
 {"忠义之礼","110"},
 {"虚无怪客","110"}};             
             
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
			主线任务热度统计
		</h2>
		<form   name="form1" id="form1" method="post">开始日期：
		            <input type='text' name='day' value='<%=startDay %>'>
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
		    		     <a href="renwustat_richang.jsp">日常任务热度统计</a>
		    		     
		    		     
		    		     
		    		
		    		<h3>主线任务热度统计</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>任务组名称</td><td>任务组级别</td><td>接受数</td><td>完成数</td><td>完成比</td></tr>");
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
					Long xiaJiJieshouLV=0L;
					Long bi=0L;
					if(taskstat[0]!=null&&taskstat[0].equals(renwuArray[j][0])){
				    if(taskstat[1]!=null&&!"0".equals(taskstat[1])&&taskstat[2]!=null){
				     bi=Long.parseLong(taskstat[2])*100/Long.parseLong(taskstat[1]);
				     }
				   if(taskstat[2]!=null&&!"0".equals(taskstat[2])&&taskstat[3]!=null){
				   xiaJiJieshouLV=Long.parseLong(taskstat[3])*100/Long.parseLong(taskstat[2]);
				   }
				//out.print("<tr bgcolor='#FFFFFF'><td>"+taskstat[0]+"</td><td>"+renwuArray[j][1]+"</td><td>"+taskstat[1]+"</td><td>"+taskstat[2]+"</td><td>"+bi+"% &nbsp;</td><td>"+taskstat[3]+"</td><td>"+xiaJiJieshouLV+"%</td>");
				
				out.print("<tr bgcolor='#FFFFFF'><td>"+taskstat[0]+"</td><td>"+renwuArray[j][1]+"</td><td>"+taskstat[1]+"</td><td>"+taskstat[2]+"</td><td>"+bi+"% &nbsp;</td>");
				out.println("</tr>");
					}
					}
					}
					
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>&nbsp</td><td>"+jieshousum+"</td><td>"+wanchengsum+"</td><td>&nbsp</td></tr>");
					out.println("</table><br>");
		    		%>
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
