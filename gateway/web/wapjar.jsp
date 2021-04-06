<%@ page contentType="text/vnd.wap.wml;charset=utf-8"
	import="java.util.*,java.io.*" pageEncoding="utf-8"%><%@page
	import="com.xuanzhi.tools.text.DateUtil,com.xuanzhi.gameresource.*,com.xuanzhi.boss.client.*"%><?xml version='1.0' encoding='utf-8'?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="潜龙online下载">
<p>
	<%  
		out.println("您好！目前推荐功能暂停使用，给您带来的不便，敬请谅解。");
		JarVersionManager jmanager = JarVersionManager.getInstance();
		String root = jmanager.getFileDir().getAbsolutePath();
		String brand = "";
		//out.print("<a href=\"wapjar.jsp\">刷新</a>(如果页面没有可下载的链接！可能jar包正在更新！请您可以尝试着刷新！谢谢！)<br/>");
		String rid = request.getParameter("r");
		//推荐信息,下标0-推荐人的角色,1-推荐人服务器,2-推荐人阵营, 3-被推荐人的手机,4-被推荐人的称呼
		String recinfo[] = new String[] { "", "", "", "", "" };
		if (rid != null) {
			BossClientService.getInstance().sendRecommendRelation(
					Long.parseLong(rid), (byte) 1);
			recinfo = BossClientService.getInstance().getRecommendInfo(
					Long.parseLong(rid));
			out.print("Hi " + recinfo[4]
					+ "，<br/>你的朋友正在玩《潜龙OL》，邀请你并肩战斗！他/她在\"" + recinfo[1]
					+ "\"服务器的\"" + recinfo[2] + "\"阵营，快去找他/她吧！点击自己的机型连接即可下载《潜龙OL》<br/>");
		}
		String pn = request.getParameter("pn");
		String pns = request.getParameter("pns");
		  out.print("最新更新时间为："+DateUtil.formatDate(new Date(jmanager.getFileDir().lastModified()),"yyyy-MM-dd HH:mm:ss"));
		//out.print("最近更新时间：" + jmanager.getLastupdate() + "<br/>");
		if (request.getParameter("brand") != null)
			brand = request.getParameter("brand").trim();
		List<Jaro> jrs = null;
		if (pn == null && pns == null)
			jrs = jmanager.getSlist().get(brand);
		if (pn != null) {
			jrs = new ArrayList<Jaro>();
			for (ArrayList<Jaro> js : jmanager.getSlist().values()) {
				for (Jaro j : js) {
					if (j.getName().toLowerCase()
							.contains(pn.toLowerCase())
							|| j.getMemo().toLowerCase().contains(
									pn.toLowerCase()))
						jrs.add(j);
				}
			}
		}
		if (pns != null) {
			jrs = new ArrayList<Jaro>();
			for (ArrayList<Jaro> js : jmanager.getSlist().values()) {
				for (Jaro j : js) {
					if (j.getName().toLowerCase().equals(pns.toLowerCase()))
						jrs.add(j);
				}
			}
		}
		out.print("<br/>【常用机型】");
		String hotmail[] = jmanager.getHotbrand().split(",");
		for (int k = 0; k < hotmail.length; k++) {
			if (k % 5 == 0)
				out.print("<br/>");
			if (pns != null && pns.equals(hotmail[k])) {
				out.print(" " + hotmail[k] + " ");
			} else {
				out.print(" <a href='wapjar.jsp?pns=" + hotmail[k]);
				if (rid != null)
					out.print("&amp;r=" + rid);
				out.print("' >" + hotmail[k] + "</a> ");
			}
		}
		if (jrs != null && jrs.size() == 0)  
			out.print("对不起！您所选择的机型暂时没有找到，请检查信息或者耐心等待更新！<br/>");
		if (jrs != null) {
			if (jrs.size() > 0)
				out.print("<br/>请选择jad包下载：<br/>");
			for (Jaro j : jrs) {
				String name = root + "/" + j.getHead() ;  
				if (pn != null)
					out.print(j.getBrand() + "-");
				out.print(j.getName());
				if (rid != null) {
					for(Jar jname:j.getJnames()){
						if ((new File(name+jname.getName()+j.getTail() + ".jar")).length() > 0)
							out.println("<a href ='/jars/jadrefactor?rid="
											+ rid
											+ "&amp;jad="
											+ name+jname.getName()+j.getTail()
											+ ".jad' >"+jname.getComment()+"</a>("
											+ ((new File(name+jname.getName()+j.getTail()+ ".jar"))
													.length() / 1000 + "kb")
											+ ")");
						
					}
					out.print("<br/>适配机型：" + j.getMemo() + "<br/><br/>");
				} else {
					for(Jar jname:j.getJnames()){
						if ((new File(name+jname.getName()+j.getTail()+ ".jar")).length() > 0){
								Random r = new Random();
								int t = r.nextInt(1000);
								out.print("<br/>("+jname.getComment()+")<a href ='/jars/"
										+ j.getHead()
										+ jname.getName()
										+ j.getTail()
										+ ".jad?t="+t+"' >在线安装</a> ("
										+ ((new File(name + jname.getName()
												+ j.getTail() + ".jar"))
												.length() / 1000 + "kb")
										+ ")" + "<a href ='/jars/"
										+ j.getHead() + jname.getName()
										+ j.getTail() + ".jar?t="+t+"' >下载安装</a><br/>");
																
							
						}
					}
					out.print("<br/>适配机型：" + j.getMemo() + "<br/><br/>");
				}
			}
		}
		int i = 0;
		out.print("<br/>【所有品牌】<br/>");
		for (String s : jmanager.getSlist().keySet()) {
			if (brand.equals(s))
				out.print("[" + s + "] ");
			if (!brand.equals(s)) {
				out.print("[<a href='wapjar.jsp?brand=" + s);
				if (rid != null)
					out.print("&amp;r=" + rid);
				out.print("'>" + s + "</a>] ");
			}
			i++;
			if (i % 4 == 0)
				out.print("<br/>");
		}
		out.print("<br/>");
		out.print("<br/>");
		out
				.print("<input type='text' name='pn' value='' size='' /><br/><a href='wapjar.jsp?pn=$pn' >搜客户端</a><br/>");
	%>
	<br />
	简介：《潜龙OL》以元末明初的乱世纷争为背景，构建了一个战火纷飞的铁血时代，游戏中的玩家分为日月盟和紫微宫两个阵营，为了自己的梦想而战斗，五大门派各路强人纷纷投身乱世。谁能取得战斗的胜利？域外邪阵何时才能开启？民族大义，兄弟情深，一切尽在《潜龙OL》

	<br />
	*******************
	<br />
	<a href="http://ql.sqage.com/">进入潜龙官方网站</a>
</p>
</card>
</wml>