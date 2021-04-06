<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="com.fy.engineserver.enterlimit.*,java.util.*,com.fy.engineserver.util.*,com.fy.engineserver.sprite.*,com.fy.engineserver.message.*,
				com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.enterlimit.*,com.fy.engineserver.smith.*"%>
<%!
public class ProcessCat implements Runnable {
	private Thread thread;
	
	private boolean running;
	
	private HashSet<String> symbol = new HashSet<String>();
	
	private long kickNum;
	
	private HashSet<Long> kickSet = new HashSet<Long>();
	
	public void start() {
		if(!running) {
			running = true;
			thread = new Thread(this, "ProcessCat");
			thread.start();
		}
	}
	
	public void stop() {
		this.running = false;
	}
	
	public Thread getThread() {
		return thread;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public HashSet<String> getSymbol() {
		return symbol;
	}
	
	public void setSymbol(HashSet<String> symbol) {
		this.symbol = symbol;
	}
	
	public void run() {
		while(ArticleRelationShipManager.getInstance().get不采样充值等级()==161 && running) {
			try {				
				Thread.sleep(5000);
				PlayerManager pm = PlayerManager.getInstance();
				Player ps[] = pm.getOnlinePlayers();
				HashMap<String,Integer> ipmap = new HashMap<String,Integer>();
				//HashSet<Long> forbidMap = new HashSet<Long>();
				for(Player p : ps) {
					USER_CLIENT_INFO_REQ o = (USER_CLIENT_INFO_REQ)p.getConn().getAttachmentData("USER_CLIENT_INFO_REQ");
                    String mt = null;
					if(o != null){
						mt = o.getPhoneType();
                    }
					Player_Process pp = EnterLimitManager.player_process.get(p.getId());
					if(pp != null && pp.getAndroidProcesss() != null && pp.getAndroidProcesss().length > 0) {
						String proces[] = pp.getAndroidProcesss();
						boolean isForbid = isForbid(proces, mt);
						if(isForbid) {
							//forbidMap.add(p.getId());
							String ip = p.getConn()!=null?p.getConn().getIdentity():null;
							if(ip != null) {
								ip = ip.substring(0, ip.lastIndexOf("."));
								Integer n = ipmap.get(ip);
								if(n == null) {
									n = 1;
								} else {
									n += 1;
								}
								ipmap.put(ip, n);

							}
						} else {

						}
					} else {

					}
				}
				for(Player p : ps) {
					//if(!forbidMap.contains(p.getId())) {
					//	continue;
					//}
					USER_CLIENT_INFO_REQ o = (USER_CLIENT_INFO_REQ)p.getConn().getAttachmentData("USER_CLIENT_INFO_REQ");
                    String mt = null;
					if(o != null){
						mt = o.getPhoneType();
                    }
					String ip = p.getConn()!=null?p.getConn().getIdentity():null;
					if(ip != null) {
						ip = ip.substring(0, ip.lastIndexOf("."));
						Integer n = ipmap.get(ip);
						if(n != null && n >= 2) {
							//确定是黑名单进程序列
							pm.kickPlayer(p.getId());
							kickNum++;
							kickSet.add(p.getId());
							EnterLimitManager.logger.warn("[黑名单进程包含序列] [踢下线] [机型:"+mt+"] ["+p.getLogString()+"] [kickPlayer:"+kickSet.size()+"] [kickNum:"+kickNum+"]");
						}
					}
				}
				EnterLimitManager.logger.warn("[检查黑名单进程包含序列] ["+ps.length+"] [kickPlayer:"+kickSet.size()+"] [kickNum:"+kickNum+"]");
			} catch(Throwable e) {
				e.printStackTrace();
			}
		}
		running = false;
	}
	
	public boolean isForbid(String userProcess[], String mt) {
		HashSet<String> pset = new HashSet<String>();
		for(String ss : userProcess) {
			pset.add(ss.trim());
		}
		for(String pstr : symbol) {
			String str[] = pstr.split(";");
			boolean meet = true;	
			if(str.length < 3) {
				if(mt != null && pstr.length() > 5 && pstr.trim().equals(mt)) {
					return true;
				}
				meet = false;
			} else {
				for(String pp : str) {
					if(!pset.contains(pp.trim())) {
						meet = false;
						break;
					}
				}
			}
			if(meet) {
				return true;
			}
		}
		return false;
	}
}
ProcessCat dog = null;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<title></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" href="css/style.css" />
	<link rel="stylesheet" href="css/atalk.css" />
	<script type="text/javascript">
	</script>
	</head>

	<body>
		<% 
		HashSet<String> sym = new HashSet<String>();
		
		sym.add("WallpaperWidget,com.amlogic.wallpaperwidget;MediaDateWidget,com.amlogic.webwidget;ThumbnailWidget,com.amlogic.thumbnailwidget;软件下载,com.onda.gfan;HDMI设置,com.amlogic.HdmiSwitch");
		sym.add("Shenzhen Chinaleap Electronics Co., Ltd.,ARMM7K");
		sym.add("酷比市场,com.cube.gfan;系统升级,android.rockchip.update.service;谷歌拼音输入法,com.android.inputmethod.pinyin");
		sym.add("Google 服务框架,com.google.process.gapps;Android 系统,system;SoHu,com.sohu");
		sym.add("unknown,MID-A13");
		sym.add("Ramos,W17PRO(Dualcore)");
		sym.add("rockchip,P76a双核(M4Q5)");
		sym.add("unknown,MOMO7Star");
		sym.add("com.android.smspush,com.android.smspush;Picasa Uploader,com.google.android.apps.uploader;台电市场,com.taclast.pad.gfan;System Update,android.rockchip.update.service;超级权限,eu.chainfire.supersu");
		sym.add("unknown,A78 Dual core");
		sym.add("rockchip,P85HD双核(K6N2)");
		sym.add("网络位置,com.google.process.gapps;谷歌拼音输入法,com.android.inputmethod.pinyin;索尼爱立信主页,com.sonyericsson.home;图库,com.cooliris.media;我上传的内容,com.google.android.apps.uploader;日历存储,com.android.providers.calendar;状态栏,com.android.systemui");
		sym.add("rockchip,U25GT_PRO");
		sym.add("rockchip,U18GT-S");
		sym.add("爱立顺软件园,com.luckystar.gfan;授权管理,com.baidu.easyroot;OTA,com.actions.ota;搜索应用提供商,android.process.acore;Google Play 商店,com.android.vending");
		sym.add("谷歌拼音输入法,com.android.inputmethod.pinyin;下载管理器,android.process.media;com.qihoo360.mobilesafe.opti:tiny;联系人存储,android.process.acore;飘渺寻仙曲,org.a360jiekou");
		sym.add("Teclast,P76s双核(DKH5)");
		sym.add("谷歌拼音输入法,com.android.inputmethod.pinyin;System Update,android.rockchip.update.service;Picasa Uploader,com.google.android.apps.uploader;授权管理,com.kingroot.kinguser;李有才,com.yiyoucai.yiyoucai;com.qihoo.daemon;360手机助手,com.qihoo.appstore");
		sym.add("rockchip,P76e(G6R8)");
		sym.add("rockchip,A78");
		sym.add("ONDA,ONDA MID");
		sym.add("rockchip,Teclast P76e(G6R8)");
		sym.add("中华万年历,cn.etouch.ecalendar;原道软件市场,com.yuandaopad.gfan;System Update,android.rockchip.update.service;com.zdworks.android.pad.zdclock.yuandaopad:remote");
		sym.add("unknown,pado_p7");
		sym.add("rockchip,N70-S");
		sym.add("unknown,voyo Q808四核");
		sym.add("rockchip,N12GJ-QZ");
		sym.add("getjar.android.client:GetJarWatchDogService;图库,com.cooliris.media;Appstore,com.amazon.venezia");
		sym.add("谷歌拼音输入法,com.android.inputmethod.pinyin;Picasa Uploader,com.google.android.apps.uploader;酷比市场,com.cube.gfan;系统升级,android.rockchip.update.service;SuperSU,eu.chainfire.supersu;ASUS Weather,com.asus.weather;com.qihoo.daemon;360手机助手,com.qihoo.appstore;mp3player,com.android.mp3player;Gmail,com.google.android.gm;BooksProvider,com.android.rk.books;QQ,com.tencent.hd.qq");
		sym.add("搜索,com.android.quicksearchbox;Appstore,com.amazon.venezia;com.google.android.apps.maps:GoogleLocationService;Android 键盘,com.android.inputmethod.latin;飘渺寻仙曲,org.cocos2dx.tests");
		sym.add("rockchip,U25GT");
		sym.add("K-Touch,K-Touch T789");
		sym.add("yuandao,N70");
		sym.add("谷歌拼音输入法,com.android.inputmethod.pinyin;下载管理器,android.process.media; 360卫士,com.qihoo360.mobilesafe;com.qihoo360.mobilesafe.opti:tiny;联系人存储,android.process.acore;飘渺寻仙曲,org.a360jiekou");
		sym.add("Google 服务框架,com.google.process.gapps;Appstore,com.amazon.venezia;谷歌拼音输入法,com.android.inputmethod.pinyin;索尼爱立信主页,com.sonyericsson.home");
		sym.add("unknown,SoftwinerEvb");
		sym.add("rockchip,N70DC-S");
		sym.add("Google 服务框架,com.google.process.gapps;Appstore,com.amazon.venezia;GetJar,getjar.android.client;getjar.android.client:GetJarWatchDogService");

		
		boolean started = false;
		if(dog == null || !dog.isRunning()) {
			started = false;
		} else {
			started = true;
			dog.setSymbol(sym);
		}
		String action = request.getParameter("action");
		if(action != null && action.equals("start")) {
			ArticleRelationShipManager.getInstance().set不采样充值等级(161);
			dog = new ProcessCat();
			dog.setSymbol(sym);
			dog.start();
			started = true;
		} else if(action != null && action.equals("stop")) { 
			ArticleRelationShipManager.getInstance().set不采样充值等级(160);
			dog.stop();
			started = false;
		}
		String symstr = request.getParameter("sym");
		if(symstr != null) {
			String ss[] = symstr.split("\n");
			sym.clear();
			for(String s : ss) {
				if(s.trim().length() > 0) {
					sym.add(s);
				}
			}
			dog.setSymbol(sym);
		}
	    %>                                              
	    <h2 style="font-weight:bold;">          
	    	ProcessCat
	    </h2> 
		<center>
			<form action="processCat.jsp" name=f0 method=post>
				当前状态<%=started?"开启":"关闭" %>
				<%if(started) {%>
				黑名单进程序列:<br>
				<textarea name=sym cols=150 rows=10><%
				for(String s : sym) {
					out.println(s+"\n");
				}
				%></textarea><br>
				<input type=submit name=sub1 value=' 更新进程库 '>
				<input type=button name=sub1 value=' 关 闭 ' onclick="location.replace('processCat.jsp?action=stop')">
				<%} else {%>
				<input type=button name=sub1 value=' 开 启 ' onclick="location.replace('processCat.jsp?action=start')">				
				<%} %>
				<br>
			</form>
			<center>
		<br>
		<br>
	</body>
</html>