package com.fy.boss.platform.yijie;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;

public class YiJieCallBackServlet2 extends HttpServlet {
	
	public static final String [] keys = {"F52F35C5A04A1876","11B5148EC73B3613","FC9729AE50FCAD69","FB589C6DEBE9A197","F64D801F9C555C7C","5FE465A070528702","F7373E8EBC2700D7","B4447B49BC295EFE","E7FDED8015C8FD56",
		"0993AC81D95D4E48","CE1EC10025C4FE44","C826E32D4F9C1C68","64A9CB039DF350BE","DD72FEA8BCEE13F4","A2D2F4AED400E281","4CB4C42B71641CB3","152E84D3CAB12856","3E1CD1D5A636F2C7","164B940D82A0EC42","81AB24E2162D54BC",
		"FC9729AE50FCAD69","3C3A138EB2E6A951","C06EF2BF7E87C46C","ABF07CC5115B9437","C6B5708195B3725C","B347B2EB94FF5130","42AC3F04D0229A31","8DD43FECE77A64DE","BB5448802D14C294","E8761771804DD523","F519CA22173BFFF9",
		"0EC79CB6652AE5E2","ECCE88F13CE492AE","7E8EA5F0817DB486","486D4210F3F79C16","4C2A9CE5D302B8B3","4F881D6467D18EC7","A15DC579667D6DA6","ACAA9433B9649EA6","8C65AC643E812F5B","C990BEDD5EDEEEDE","5F9E9900D9CCC2D0",
		"053B055E4AC30169","19B6CC0C3A315781","739B4522E6151085","B531C2AA5C613A8C","5B86A8C8B04A50B6","83219852FEA4191D","E9B05A0DBA742AFD","DB788E0BB6C3D980","57ADC33A8E62A919","BAA3FA045593BAA9","3481F08BF45DB0D6",
		"A16624D63EB4DC0E","FA06CBE517C89009","345491F880724180","5EFCB428547E62B1","B002C32EF2E0D892","D7719F425E17430C","055EFE11D0876B48","DCBAA62A619E0235","8F3AAA415105B8F1","9F6FFDC30F5F0E97","83606A06B70A4CC2",
		"60AA725A32D4C67B","48FA26AEA2335CF1","29FFEFB92721B2CE","BA206D445AA44821","2A801889C0452124","C12A391C3075D56B","A17E41DF8EE40915","EED4ADC3AB9B116E","CB9A60A7EA8598A6","2EDF698D411B1045","36F4275772C11F55",
		"325A8295826DA743","CE3AE153EE2A4D8F","3C1E04616D94B39A","C01C8CD8B0932588","DD6E1FD724FFB2CB","4100474C5367F564","48A6A788FC645BFC","71DCD3C368FB7F00","65C90E90AECD89B2","14A1382EC59A6F70","E15B769109F0967C",
		"72B557579C26DBFE","EF4AE8F54D0BC074","35A17DD513863B07","A0B7CD3DFDB20F80","6AAECAD37284DBF9","2BE115F03999EFA6","AC06E5258AB2FB95","C5486653AD4CC696","FB4D38794BE61CFF","17C5C7BBFDF423B6","6D00ED5B6E998C7D",
		"CE359513EF8FD4D6","6B322F0EF99DAA51","2907561AA5331E0D","06152E3E44A4595A","D6A6806578B689C4","3CBECEB4C84C816E","34F757322B6C556F","7F5EC1E2DF7BC99A","C9D65DE5AE5C64E9","8DC3001887058B7A","23D9C4791D64FA2A",
		"4D68933A04FBEA75","81D1B69AA649FF51","10248F0C02D31D88","43F76264F26FFB75","B7B9EFC590C9F8D7","4D0ADE607D0930FC","18D513CAF34CE724","9086277915B337E3","84C1B0C4356B6863","75B5169F023D2316","0DFA91FDD2DDF337",
		"607D2B2652250254","E278B61F66DF8ADD","0D3C65076B52DA30","09CE2B99C22E6D06","82CBE43F60FC012A","159A43C7136AFA2A","11AEE396DB063871","3CA844B9C4BCEE52","CAC0C8279EF1765E","5B9E4A0D39524DF2","253CF5644BD5BDF4",
		"F0009BDA633E32A9","F42B1CBEAE0E9C4A","6D29322C6D592D66","3436CD27FEA81860","EFA6E8090EB84A74","BBDACA3FED5DD8D9","71B7D03EC75425D1","36AA81668561907D","5303DCBCB9F92C98","906363003FC9CACC","4EE97C8C7CEF6424",
		"C276B9FBB57AE28D","CBCCC1B8A1A2A13F","65E8DDB846FC6F1D","1536DEC0CC92A1A4","C90B6EFAB47065A8","C05CA11A376B0383","01BC95DC2E53A05E","4FB30E90CB758620","4357B2282150D299","4223661A47626A66","8698C5B6DD175F01",
		"72F05AAD18F72BDA","FE74EAC01FA64B8E","047EBD3D9D8F3DDD","47590168C05A73A4","03564B7AE0F0E806","A81B428D362492AB","5D69888965327A57","08524CEB3F3A4534","7FA1F5DEDA8F2A93","A75217CBC81413BE","C9CA13AC79C901C6",
		"1AE18EDE8DA726FD","D129D0B8D016FC46","D743CF724EED5A85","C10E5843DBB02181","26132B53B4025907","91306CA8BE0D98DD","07E7CE8BE75365BF","D27752097FDD1921","76CA924DE79E3A4A","5D36F8D4E8692B04","7A32EAE9236C7CEB",
		"D36DBB44FCE1ACC6","2A5650167E745F96","2851AD0183A4F622","F17606A14FEE1F21","CBD299056C9CC89B","8BFB48D3BF9E847B","EBBBB2983DEC4C4D","8300D7FDB402F76E","B2E8612733F723DA","D8F3C871F2A72D58","B9D4568338D60384",
		"44D875DCE6A9BAC8","E11FCAE9D461E56A","ED0984868A11146C","BC3F66C70BD8F2B9","3F8C2687023BE3C9","9B5A4A8168809CFA","07441320eb983fc6","D3520910C84D9E98","B575C2045A93BC4A","A76C2CFB01A90853","03D895790E589890",
		"CBDB0FBB915C14D0","8025A436F20C5013","E43528026F4DC6EF","9A3AB3040B6417B1","D2B4090C508929FD","A58AFC6DD426DB29","F15E2FAE715BF541","3895A762CBF56075","E5EC4FC4C3BB9088","CDAB18655AF14186","EF6FB038337D27B4",
		"17A40FA5C1D3545D","1A7BFEC437B5D259","D3CC11CD6781F533","23C6F930A3747CC6","49934FCE1833D16D","1B8DD7DD6FC313A1","A7DE0BFE7DA2AA8A","89EAA3F8ADD96042","7782921DF9EBDCCF","189940613E97A0B6","C0FA00C64B2A50E4",
		"D155A43914D003C2","474BA6B845E663ED","14F276DE128501A3","F50C46132B0D1DEE","A526555BB0C778C4","0D1B6848691043A4","D1FC4205FCDADB9E","2FC37DC4B59D492B","ABC2CC00A562FABE","97AE00A5F7FB521C","9153A53BBA45B1B8",
		"A526555BB0C778C4","209918D2438E30E0","2883C2C815FEE227","E947CD879A7320FA","F3D9BFD1B1D00612","6C9840A98B3664F7","34C08597BA582BCC","2BD17544241BA536","16EC5B0A2CC811A1","9570A7542B8EAE7F","C59E4F2EF49E538B",
		"F74520D571B77263","8014394A0ECEE884","F2B4DADE2E94816F","2F34BC42013FCB68","D74620B09C2CD765","AAFA9D792B4AD6C6","E2B980AB2FD498CD","1B39D15ADB43A10A","9FADB99C9EE40718","78D5E22062B19324","F7FE04B856AB521C",
		"F53B01691FF0805F","CE275F244AFD1BB3","15DA0F07D51D4069","15B43FC56009D1BF","162355910346E8E9","FC4E6E216C90ACDE","72EC8249055C9647","AFA305D02BE21065","7B15DF77AC68181D","C17D5F160B3F3BFD","D3AC7292A318C661",
		"649257B9D6BFEAC8","2C844C3057B5670A","0BE8EE9E4F11EF20","2C6A620C21F8DDFA","372898979BF92CE8","941FA20B17C85FAE","DF5EACDE3483A34D","92176285E4705272","0D53A0FE08911F7D","C17D19C39FD4D3A1","0581F489A88F4D79",
		"A02D5BA0488426A5","538FAA3F9855C0EA","0C4BE88A5D3F485A","F11EB231603EA670","9265E84ED7DE75D8","1DDC847B6492005D","57065564FAEBB1F6","C265D51189B0E463","4080ADAF0623FDB1","B238F53F923855B1","A6E5C780DBC99F27",
		"FAE1D82D1B1303C5","AEC56BA8752D5D41","EE3D7FB918A73A0A","D3ED6BBB81B9D74E","66B5E99024A83695","4E9E11BD602DC6B6","A77166BA1FF6D123","EAE31C58805FAA50","0BE8EE9E4F11EF20","247D995D75544546","2704F34578E9051F",
		"954E93E0F4708063","A7496482ECE6D05F","86BEC8473BE16CCE","F009B70236A6C74A","264B2AE1F896098C","6C4F727598BE984E","7C54FDC58CCAF159","0F8E46942A20DC1E","2864DC9377BBC5DB","00B0119C64E63697","338DA83F922BADBD",
		"04B4779C1CC02464","59883F6CE819429B","9A030A07CE06D33D","B2936DD3643BF3B0","E2C2AFBF99807BB5","680110948C0FA25E","9C4F8C67AA7D0122","6615BBEB232CD76E","2C7EEBC5E7151F0B","E5A565C741209230","EDF2B53661D3DA2C",
		"5FE68F4B0E24E202","44812DC5583A86FC ","B9E046B35B808758","4877CB1120434F58","CE66A8B658FA8800","7255C79970BEC777","2A88ED136DCADB35","A9B817095EDB3A1C","7D2E9430C42699B3","381E58DB8C72A67C","77F3545443657BFE",
		"46E628FB014910E0","4069E088FC03EC95","F69683A014C13221","0304F6123950925D","E98AE363A61ABCE3","7D3A2A9A4D1C6C6C","052A1DB80CAAFC0E","49EA241F4DAC7059","758F557AB00810B9","E7C4E9FA44FC69C7","83A1B0315EE6889A",
		"C2E32C352793B67E","506180F21D4EE037","8A66104725B406EE","718CA908D43B9C49","D5FD043CFF660CD9","236C7ED312E8915D","BA0A3DE910406E9C","FED99454AA91865E","841B5068E48AB443","E5970A3C6787A201","DE80FA71C46A63F2",
		"CA140DCB1114F7FE","295681C818BF39DB","0A5D6D189FB1EA46","48D60D5B00657742","EC64A83B29436141","53037FF854C7A2EF","B7E6E625A2628693","7566A98448A4C033","1B68BD9DE36290F7","1D422D39B8D07225","7197DA7134CC008B",
		"FBFDA293232E6674","3ED70FC1F8AC78D9","2384408FF360AB7F","E794C4CC6B88230F","F45FC50EAA2F8DA0","CC6E74EDB98F1022","1EC25A61DD61437C","127A24B98B9CF84F","D0DCD98391D14383","B72CC110DFBAA1CD","177147C643262F90",
		"0840F71635C1197D","69153CE5B59724F5","1168DAC077D12A8E","283E88ABBE4D6D18","A0B719090953D801","88E1A36FF605C2E5","A33993A035C518F8","FDC2363814464A64","D92C465DEE44BA7D","50681C972141FE60","283E1E4E03F27A8F",
		"9B2C82EC75711B40","8CF7D9B526AE39DC","4D4CB1A9C98BC813","6C5D1AAC9802C8FE","FA8CFF8BBAE4408A","7ECF2CBE204E5F69","8F6AB202C5994366","21170591BD3FB18B","91F21DA1C2740CB1","40255A707E6070BA","AD49224920C28717",
		"896BA1F2447FD237","18431A0AD487897F","6D72C81B72B599FF","4F58813D04C7B303","FA04A64870052CF3","222F60FDAD087FEE","E59B64AA2D2CBC88","E18A8544392123D0","7E46B239916709DE","12F1C33AC0342061","08D0A86F8076DBCB",
		"EDB8069595BB4B46","C1D9D27F5928987B","FF4E0AFA0C0DE4E4","6427EF01E02BAE73","8893984D076DD8ED","4104D743749AAF17","AC0DBA84909D3B91","EC93EFF611D161A5","ACEAD8511530833C","6B4B68A188FAC891","91BFF174754647E4",
		"D67E7437127A99B7","D1A71D4F370418C9","143D97BBB793EC0B","E85FEB88A2ADB4D4","C6684985B26105F5","9BD5C99FE11848B0","7CFEB31E652793C5","789844F46533146F","3E9CB05FC4CA918D","1D99D074FCC7F205","0D5B5C82098FB86F",
		"0BD88B2E0C2CD3BE","850B62B1D329657D","267153B2FA653815","77BAE35261014545","7659F4F57368AE8F","E3978FDE39E2B432","27531466A56234C9","CEEFF30E1556119E","F5D6936D683EF318","20D96403E026F360","1F418017025E9A59",
		"702DA0B76E160D0C","5F85C1433E8964B1","2D724AFFF7AE22AC","DD0BD4E2FC993154","02234DABED48F38C","6DA320E6C14406D4","29B202099ADE42FD","5D8980616DAED3EA","3341ECCD044B66FC","6033A35122BDBA06","D0522319879C3E99",
		"03BD25F64DF56B33","E2E8B97653B80356","D239EF0B56A6C078","AFB2C7AD9298D703","5D7B9DA3567B5FF6","A2BBC8248C05D7C7","295F5A5E1B79BCC8","94507EB040D25426","EE440120665CD6FA","1BD505102826DF03","9DC2585660C15812",
		"635614F85AAC55CA","FF9E88462DF1A46A","56704260EE712716","36570B8FB1BD13B6","E3E4E967474994DD","54B00CBA97D070DD","68AE4A116525C7E1","BF679BAC315AEFAE","F4521F32C08D1B72","D61E8AC0DA70BD83","9CB9394D0C86D485",
		"91C98720497DABFC","B8B282A3F6BDC346","5DA6D22A3E038FDE","B83CC95651DB2BCC","08E71BB23B6F851E","1874E3790154FB42","3F0F5771DD78A405","840350A6E8137B96","5570182D9165F4BC","7305D4241AE6B394","BC4F6396017FD864",
		"D84484E41AEA562D","EE8121D906F37F74","97AAD491D2D85C41","3540E087F361A580","A7FF3609B628C66A","D04946EA7C22CEFE","A80FCDF462CC675F","EFD95AACA185A71D","5D103574C928CA46","D7A6A19DF61EDE9E","0C2B1D0FAF90F7F9",
		"10BEDF2731F29A71","4680E56B5B4559E7","87B8E6FE2AAF1C07","3810E10BF5E251E5","AA41112D0CCA498D","7395AC21834B7E6B","E36C39015AA935A8","AD04DBB99ADE3D63","ADC18EF3A58C425B","559FCA8524B454F9","9BDB0F4B78178B67",
		"B065604C9E468323","D04946EA7C22CEFE","877A302B65ACC069","E9D0636F00E768D1","85AF377C7170FF15","9B93FA8BB327FD07","C57B3EF172DB3ED6","E5C3CFD8F714D4E8","C3BAA0A903F9C643","C30F1E2CAE17021A","3F8EB560ABDEDDFC",
		"D7E3BDA09718DA1E","81F2CA0562A26636","66840AA7D171A7F7","754114E053228046","CD72E5CFE5FA4064","941C8962A3C1EE1D","EAAC3D31BBF8F2EC","9FC11F6A98A901B9","CBE600A74765863F","5B4E48C7D87ABF39","8A66104725B406EE",
		"8B5495BDFB0B9465","C7DD907515B158C6","EA968F0B6789F4D6","82342231B0181D53","6C50D0EFE64CE1C9","B99FC5AD0452CB37","8CB8805B292F4383","6FF74F1743CF5222","4A5B65B6A8DC61EB"};
	
	public static final String [] values = {"UC","斯凯科技（冒泡客）","木蚂蚁","当乐网","腾讯应用宝","电信爱游戏","联通沃商店","豌豆荚","360","金立    ","移动游戏基地","华为","丫丫玩   ","小米    ","安智市场    ","4399手机游戏","机锋网",
		"N多市场   ","OPPO   ","联想","木蚂蚁","糖果游戏","37玩","凤凰网","HTC聚乐","百度开放云","海马","魅族","Itools","五彩时空（柴米）","熊猫玩（游戏群）","好机友","宝软网","斯凯冒泡社区","七匣子","遥望DQ","优酷网游","百度移动","应用汇",
		"悠悠村","酷派彩客易付","酷派","腾凤易百","绿岸","丫丫手游","安锋网","PKPK","07073（圈圈游戏|数游）","逗逗","移动MM","PPS","千尺","手盟","微游汇","泡椒网","8849游戏多","VIVO(步步高)","欧朋","中青宝","乐视","泰通","蜗牛移动商店","游龙",
		"8868（聚好玩）","酷狗","掌阅","有信","卓易市场（卓悠）","点优","酷我","i9133","44755","欢流","艺果","65","睿悦","天海","海尔","威游","联旭","星尚（口袋）","乐逗","草花","偶玩（有米）","果盘（叉叉助手）","蘑菇网（酷动XXX）","鱼丸互动",
		"暴风游戏","乐游","49you（49游）","乐非凡","乐玩","yy游戏机","拇指游玩","手游天下","168yxSDK","PPTV","博雅科诺（三星）","琵琶网","人人网","迅雷","腾讯游戏币","搜狗","3G门户（久邦） ","爱游就游","魔苹","乐视手游","顺网","沃友玩（久游）",
		"爱贝","云荣天尙","斯凯冒泡（第三方支付）","九城","小葱","好玩点","17WO","拇指玩","7k7k","XY手游","陌陌","移动游戏基地渠道版","掌娱","91","爱上游戏（玖度）","晃游","靠谱助手","同步率","生菜支付","嗨游","新浪游戏","乐嗨嗨","手游咖啡","爱拍",
		"豆米游戏","今日头条","益玩","斗鱼","6816手游","93pk","虫虫游戏","华硕","任心游","要玩","商都游戏","91wan（好萌|美图）","游艺春秋","联通沃商店（联运版）","有玩","搜狐","11对战","游戏坛子","福利宝（笨手机）","友游（云点）","人人游戏",
		"游乐猿","安趣","玉米支付","芒果玩","唱吧","赏付","52游戏","来伊份","7723","聚玩（网易）","口袋巴士","松果（新浪手机助手）","游狐","找乐助手","TT语音","51wan","齐齐乐","星宿传媒","快玩","优贝","麟游","狐狸","3899玩","87873","快用（7659）",
		"3721","乐狗","7881（猎宝）","朋友玩","UU盟","ATET","力天（天天游戏）","东鑫科技","同游游","3456wan","娱玩","阿游戏","夜神模拟器","badam","榴莲科技","内涵段子","同步推","酷都","乐视体育","钱宝游戏","偶乐","萌玩","云霄堂","5Q(飓风)",
		"盛讯游戏","葡萄游戏","全民游戏","第一应用","笑傲游戏","直通车（逗逗）","云顶","齐游","奇葩盒子（长沙试玩）","小葱网络","啪啪移动支付","直播8","丫丫手游（新版）","豪邦网络","17173","九梦","小怡科技","BT玩","牦牛游戏","多多支付","游侠","说玩",
		"摩格","51iyx(爆游、旺御）","逍遥","箩筐","多多支付","征游网络","玩转","游戏多（多玩）","点点玩","19手游","大麦助手","优亿","追追动漫","努比亚","栗子","北京昆达天元（神灯）","鱼丸游戏","重庆晏门","酷游戏","腾讯游戏币（新版）","掌炫游戏",
		"悦玩","7U","爪游","虎扑","微客","悠讯","9665游戏   ","250游戏","玩客互动","必玩","惠游","顺玩","烽火互娱","桌游志","怪猫","牛丸","游途","钱柜","爱萌","心跳游戏（手游端享）","1881wan","纵思","骑士助手","余粮","黑鸭子","龙跃游戏","185sdk",
		"多游","5288wan","墨仙","蘑菇玩","灼游","芒果玩（新版）","汇娱","多元互动","44937手游天下","119手游","玖毛","重庆热岛","海信","185sy","妙智门","小七","天机游戏","遨游","同城游","爱萌","乐聚互动","闲玩","宝玩（冰趣）",
		"祖龙互动","爱乐游戏","随乐游","手游村","龙城手游","九趣","游泡泡","拇指玩2","点游联运","乐七游戏","无忧玩","禹乐","B游汇","七趣","手游BT","乱码手游","淘手游","C有","560游戏","东方二次元（悦动）","跃娱游戏","三星（爱贝定制）",
		"7566玩","乐8","啪啪游","湖南工会","悦动","奇趣","花生","YunOS","游戏fan","乐通","彩虹游戏（嗨皮游戏）","漫画人","第一波","哔哔游戏","西柚网络","狂玩","芒果游戏","杭州道盟","物象","疯趣","超多维","迅雷游戏","小辣椒","xd178",
		"龙虾","手游天堂","魔盒","易幻","8808wan","3733游戏","凯旋","TCL","果游","12玩","提子游戏","游窝","东东游戏","六城","米娱游戏","MyCard","天勤","一起玩","卓达","奇顽","HG6kwan","汉风","爱玩手游","逆光","1862sdk",
		"87870（幸福时空）","掌阅（新版）","现在支付","皮卡游戏","米奇玩","娱玩移动电竞","飞磨","流弊游戏","牛牛端","延梦游戏","点入游戏","手游熊","云客","睿玩","16wifi","魔苏","炉石游戏","逗鱼","天宇游","来游戏","零钱支付",
		"冰鸟","智蛛游","七米游戏(熊猫岩)","七忍网络","红手指","坚果触动","典游","团结游戏","老虎","鲸旗","新游","coco","pk玩","草花（新版）","战魂助手","爱娱乐","新快手游","顺玩游戏","畅玩乐园","星趣","游宝","聚游手游","纽扣支付",
		"175","天宏","宝亮","小草互娱","2166","掌触","WSFSDK（云系SDK）","91助手（168）","炎尚","逍遥游","蘑菇互娱","微度35游","350游戏","128sy","阿游威","掌娱炫动","51任性","葫芦侠","有量","小螳螂","官渡","掌鸿","武汉顺玩",
		"应天互娱","c1wan","嬉皮玩","9669","55手游","广州大游","暴走游戏","wa3","炫玩","57K","猎豹手游","楚风","触手","聚力互娱","奇葩","逗趣","猎游","微游戏（创游）","72G","仙豆","电玩宝","潮游科技","手游猪游戏","PPTV(新版）",
		"趣热","复娱","51手游","星光助手","游戏猫","呈天游","重庆掌玩","慧玩","中文互娱","成都亿客游","靠谱助手新版","Gtoken","奇点","爱返","遨游新版","37376","便玩家","麦游","奥飞","清流游戏","朕会玩","慧玩游戏","7477","乐米",
		"YXBOBO","菜鸟玩","金石","蓝彩","TK游戏","i苹果","果盘（越狱版）(叉叉助手)","乐嗨嗨（越狱版）","海马玩（越狱版）","百度移动游戏（越狱版）","同步推（越狱版）","快用（越狱版）","当乐（越狱版）","PP助手（越狱版）",
		"itools（越狱版）","爱思（越狱版）","9377苹果助手（越狱版）","xy苹果助手（越狱版）","爱上游戏（越狱版）","人人游戏（越狱版）","44755（越狱版）","乐8（越狱版）","07073（越狱版）","i苹果（越狱版）","51苹果助手（越狱版）",
		"狐狸（越狱版）","魔品（越狱版）","兔兔助手（越狱版）","小七手游（越狱版）","同游游（越狱版）","钱柜游戏（越狱版）","185（越狱版）","185sy（越狱）","熊猫玩（越狱）","魔盒（越狱版）","爱应用（越狱）","B游汇（越狱）",
		"全民助手（越狱）","朋友玩(疯趣端)（越狱）","要玩（越狱）","淘手游（越狱）","疯子（越狱）","朋友玩(朋友玩端)（越狱）","xd178","大麦助手（越狱）","流弊游戏（越狱）","七忍网络","掌玩游戏（越狱）","首充号手游（越狱）",
		"炫玩（越狱）","清流游戏(越狱)","乐读科技（越狱）","自由网络"};
	
	public static String getChannelStr(String code){
		for(int i=0;i<keys.length;i++){
			if(keys[i].equals(code)){
				return values[i];
			}
		}
		return "错误";
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long now = System.currentTimeMillis();
		PrintWriter w = response.getWriter();
		StringBuffer sbEnc = new StringBuffer (); 
		sbEnc.append ("app="); 
		sbEnc.append (request.getParameter("app"));
		sbEnc.append ("&cbi="); 
		sbEnc.append (request.getParameter("cbi")); 
		sbEnc.append ("&ct="); 
		sbEnc.append (request.getParameter("ct")); 
		sbEnc.append ("&fee="); 
		sbEnc.append (request.getParameter("fee"));  
		sbEnc.append ("&pt="); 
		sbEnc.append (request.getParameter("pt"));
		sbEnc.append ("&sdk="); 
		sbEnc.append (request.getParameter("sdk"));
		sbEnc.append ("&ssid="); 
		sbEnc.append (request.getParameter("ssid")); 
		sbEnc.append ("&st=");
		sbEnc.append (request.getParameter("st")); 
		sbEnc.append ("&tcd="); 
		sbEnc.append (request.getParameter("tcd"));
		sbEnc.append ("&uid="); 
		sbEnc.append (request.getParameter("uid")); 
		sbEnc.append ("&ver="); 
		sbEnc.append (request.getParameter("ver")); 
		
		String sign = request.getParameter("sign");
		
		boolean result = MD5.encode(sbEnc + "4UI2DGPJ08QKRNC3361D81PGWZIYGPZ6").equalsIgnoreCase(sign);
		
		if(result){
			// 此处CP可以持久化消费记录
			//st==1是支付成功，才能给用户发道具
			// 操作成功后需要返回SUCCESS告诉易接服务器已经接收成功
			int stat = Integer.parseInt(request.getParameter("st"));
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//查询订单
			OrderForm orderForm = orderFormManager.getOrderForm(request.getParameter("cbi"));
			if(orderForm!= null)
			{
				Passport p = PassportManager.getInstance().getPassport(orderForm.getPassportId());
				synchronized(orderForm) {
					if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
						PlatformSavingCenter.logger.error("[充值回调-易接] [失败:此订单已经回调过了] ["+sbEnc+"] [sign:"+sign+"] [costs:"+(System.currentTimeMillis()-now)+"ms]");
						w.write("SUCCESS");
						w.flush();
						w.close();
						return;
					}
				}
				try {
					orderForm.setResponseTime(System.currentTimeMillis());
					if(stat == 1) {
						orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
						//以返回的充值额度为准
						long oldPayMoney = orderForm.getPayMoney();
						orderForm.setPayMoney((long)(Double.valueOf((String)request.getParameter("fee"))*1));
						orderForm.setChannelOrderId(getChannelStr(request.getParameter("sdk")));
						orderFormManager.update(orderForm);
						if(PlatformSavingCenter.logger.isInfoEnabled())
							PlatformSavingCenter.logger.error("[充值回调-易接] [充值平台:"+orderForm.getSavingMedium()+"] [子平台:"+request.getParameter("sdk")+"] [成功] [OK] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [账号:"+p.getUserName()+"] [订单充值金额:"+oldPayMoney+"] [易接充值金额:"+orderForm.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-now)+"ms]");
					} else {
						orderForm.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
						orderForm.setResponseResult(OrderForm.RESPONSE_FAILED);
						
						//以返回的充值额度为准
						orderForm.setPayMoney((long)(Double.valueOf((String)request.getParameter("fee"))*1));
						orderForm.setChannelOrderId(getChannelStr(request.getParameter("sdk")));
						orderFormManager.update(orderForm);
						PlatformSavingCenter.logger.error("[充值回调-易接] [充值平台:"+orderForm.getSavingMedium()+"] [失败] [充值失败] ["+sbEnc+"] [sign:"+sign+"] [costs:"+(System.currentTimeMillis()-now)+"ms]");
					}
					w.write("SUCCESS");
				} catch (Exception e) {
					PlatformSavingCenter.logger.error("[充值回调-易接] [充值平台:"+orderForm.getSavingMedium()+"] [失败] [出现异常] ["+sbEnc+"] [sign:"+sign+"] [costs:"+(System.currentTimeMillis()-now)+"ms]",e);
					w.write("ERROR");
				}
		} else {
			PlatformSavingCenter.logger.error("[充值回调-易接] [失败] [未在数据库中找到匹配订单] ["+sbEnc+"] [sign:"+sign+"] [costs:"+(System.currentTimeMillis()-now)+"ms]");
			w.write("ERROR");
		}
		}else{
			// 返回ERROR易接服务器会根据一定的策略重新同步消费记录给CP
			w.write("ERROR");
		}
		w.flush();
		w.close();
		
	}
	
}
