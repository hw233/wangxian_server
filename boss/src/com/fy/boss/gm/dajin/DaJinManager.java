package com.fy.boss.gm.dajin;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fy.boss.gm.XmlServer;
import com.fy.boss.gm.XmlServerManager;
import com.xuanzhi.tools.servlet.HttpUtils;

public class DaJinManager implements Runnable{

	boolean isStart;
	
	public static Logger logger = Logger.getLogger(DaJinManager.class) ;
	
	private static DaJinManager self;
	
	public static DaJinManager getInstance(){
		return self;
	}
	
	private List<DaJinMember> list = new ArrayList<DaJinMember>();
	
	private List<DaJinMember> articles = new ArrayList<DaJinMember>();
	
	public void init(){
		self = this;
		isStart = true;
		new Thread(this,"DaJinManager").start();
		new Thread(new ArticleMove(),"ArticleMove").start();
		System.out.println("[初始化DaJinManager成功]。。。。。");
	}
	
	@Override
	public void run() {
		logger.warn("[DaJinManager] [银子] [正式启动] [------]");
		String pageUrl = "admin/smith/playerMoneyMoveMess.jsp";
		while(isStart){
			try {
				Thread.sleep(2*60*1000);
				list.clear();
				doRequestMess(pageUrl,5,5);
			} catch (Throwable e) {
				logger.error("[DaJinManager] [Exception]", e);
				try {
					Thread.sleep(1000);
				} catch(Exception ew) {}
			}
		}
	}

	public void doRequestMess(String pageUrl,int downCount,int layerCount){
		long now = System.currentTimeMillis();
		String currUrl = "";
		XmlServerManager xsm = XmlServerManager.getInstance();
		List<XmlServer> xs =  xsm.getServers();
		if(xs!=null && xs.size()>0){
			for(XmlServer server : xs){
				if(!server.getDescription().equals("网关")){
					currUrl = server.getUri();
					String adminurl = currUrl.substring(0,currUrl.indexOf("gm"))+pageUrl;
					HashMap headers = new HashMap();
					String contentP = "downCount="+downCount+"&layerCount="+layerCount+"&authorize.username=lvfei&authorize.password=lvfei321";//authorize.username=zhangjianqin&authorize.password=Qinshou7hao";
					String result = "";
					try {
						byte[] b = HttpUtils.webPost(new URL(adminurl), contentP.getBytes(), headers, 20000, 20000);
						result = new String(b).trim();
						String messes[] = result.trim().split("@@@@@");
						for(int i=0;i<messes.length;i++){
							String str[] = messes[i].split("#####");
							if(str.length>0){
								DaJinMember member = new DaJinMember();
								if(str[0].trim().length()>0){
									member.setId(Long.parseLong(str[0].trim()));
								}
								member.setDownNums(Integer.parseInt(str[1].trim()));
								member.setMaxLayers(Integer.parseInt(str[2].trim()));
								member.setSilerCounts(Long.parseLong(str[3].trim()));
								member.setServerName(str[4].trim());
								member.setStatus(str[5].trim());
								list.add(member);
							}
						}
					} catch (Throwable e) {
						e.printStackTrace();
						logger.warn("[服务器收集数据] [银子] [异常] [服务器："+server.getDescription()+"] ["+result+"] [耗时："+(System.currentTimeMillis()-now)+"]");
						continue;
					}
				}
				
			}
			logger.warn("【服务器收集数据】 【银子】 【完毕】 [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"]");
		}
	}

	
	class ArticleMove implements Runnable{
		@Override
		public void run() {
			String pageUrl = "admin/smith/playerArticleMoveMess.jsp";
			while(isStart){
				try {
					Thread.sleep(2*60*1000);
					articles.clear();
					doRequestArticleMess(pageUrl,5,5);
				} catch (Throwable e) {
					logger.error("[DaJinManager_Article] [Exception]", e);
					try {
						Thread.sleep(1000);
					} catch(Exception ew) {}
				}
			}
		}
	}
	public void doRequestArticleMess(String pageUrl,int downCount,int layerCount){
		long now = System.currentTimeMillis();
		String currUrl = "";
		List<String> 成功访问服务器列表 = new LinkedList<String>();
		List<String> 访问异常服务器列表 = new LinkedList<String>();
		XmlServerManager xsm = XmlServerManager.getInstance();
		List<XmlServer> xs =  xsm.getServers();
		if(xs!=null && xs.size()>0){
			for(XmlServer server : xs){
				if(!server.getDescription().equals("网关")){
					currUrl = server.getUri();
					String adminurl = currUrl.substring(0,currUrl.indexOf("gm"))+pageUrl;
					if(server.getDescription().equals("桃李春风")){
						System.out.println("服务器："+server.getDescription()+"--adminurl:"+adminurl+"--测试");
					}
					HashMap headers = new HashMap();
					String contentP = "downCount="+downCount+"&layerCount="+layerCount+"&authorize.username=lvfei&authorize.password=lvfei321";//authorize.username=zhangjianqin&authorize.password=Qinshou7hao";
					String result = "";
					try {
						byte[] b = HttpUtils.webPost(new URL(adminurl), contentP.getBytes(), headers, 20000, 20000);
						result = new String(b).trim();
						String messes[] = result.trim().split("@@@@@");
						for(int i=0;i<messes.length;i++){
							String str[] = messes[i].split("#####");
							if(str.length>0){
								DaJinMember member = new DaJinMember();
								member.setId(Long.parseLong(str[0].trim()));
								member.setDownNums(Integer.parseInt(str[1].trim()));
								member.setMaxLayers(Integer.parseInt(str[2].trim()));
								member.setSilerCounts(Long.parseLong(str[3].trim()));
								member.setServerName(str[4].trim());
								member.setStatus(str[5].trim());
								articles.add(member);
							}
						}
						成功访问服务器列表.add(server.getDescription());
					}  catch (Throwable e) {
						访问异常服务器列表.add(server.getDescription());
						e.printStackTrace();
//						logger.warn("[服务器收集数据] [物品] [异常] [服务器："+server.getDescription()+"] ["+result+"] [耗时："+(System.currentTimeMillis()-now)+"]");
						continue;
					}
				}
			}
			logger.warn("【服务器收集数据】 【物品】 【完毕】【成功访问服务器列表:】"+成功访问服务器列表+" ----------【访问异常服务器列表:】"+访问异常服务器列表+" [数量："+articles.size()+"] [耗时："+(System.currentTimeMillis()-now)+"]");
		}
	}
	
	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public List<DaJinMember> getList() {
		return list;
	}

	public void setList(List<DaJinMember> list) {
		this.list = list;
	}

	public List<DaJinMember> getArticles() {
		return articles;
	}

	public void setArticles(List<DaJinMember> articles) {
		this.articles = articles;
	}
	
	
}
