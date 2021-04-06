package com.fy.boss.gm.gmpagestat;

import java.io.File;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fy.boss.gm.XmlServer;
import com.fy.boss.gm.XmlServerManager;
import com.fy.boss.gm.gmpagestat.EventForJson.EventType;
import com.fy.boss.gm.gmpagestat.event.ArticleRecordEvent;
import com.fy.boss.gm.gmpagestat.event.MailEvent;
import com.fy.boss.gm.gmpagestat.event.PlayerPropertyChangeEvent;
import com.fy.boss.gm.gmpagestat.handler.ArticleEventHandle;
import com.fy.boss.gm.gmpagestat.handler.EventHandler;
import com.fy.boss.gm.gmpagestat.handler.MailEventHandle;
import com.fy.boss.gm.gmpagestat.handler.PropertyEventHandle;
import com.fy.boss.gm.gmpagestat.handler.ServerEventHandle;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.mail.JavaMailUtils;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.JsonUtil;

/**
 * 事件管理
 * 获得规则过滤后的事件，进行相应的处理
 * @author wtx
 *
 */
public class GmEventManager implements Runnable{

	public static Logger log = Logger.getLogger(GmEventManager.class);
	
	public String uname = "serverUser";
	public String upwd = "kj2#($1238!salkhdo978HGm).p";
	
	private static GmEventManager self;
	
	/**
	 * 规则及处理
	 */
	public List<EventHandler> rules;
	
	public static String [] addresses = {"3472335707@qq.com","116004910@qq.com"};
	
	public boolean isstart = false;
	
	private DefaultDiskCache ddc;
	
	public File dataFile;
	
	public static GmEventManager getInstance(){
		return self;
	}
	
	public void init() throws Exception{
		try{
			self = this;
			rules = new ArrayList<EventHandler>();
			ddc = new DefaultDiskCache(dataFile, null,"page事件管理", 100L * 365 * 24 * 3600 * 1000L);
			initEventHandlers();
			isstart = true;
			try{
//				if(iskorea()){
					new Thread(this,"page事件管理").start();
//				}else{
//				}
			}catch(Exception e){
				System.out.println("[GmEventManager] [初始化异常] [判断韩国开放异常] 【线程未开启】 ["+e+"]");
			}
			System.out.println("[GmEventManager] 初始化完成.");
		}catch(Exception e){
			throw e;
		}
	}
	
	/**
	 * 初始化规则
	 */
	private void initEventHandlers(){
		rules.add(new ArticleEventHandle());
		rules.add(new MailEventHandle());
		rules.add(new PropertyEventHandle());
	}
	
	@Override
	public void run() {
		log.warn("【线程开启】");
		while(isstart){
			try {
				Thread.sleep(1*60*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
			}
			handlerJsonstr();
		}
	}
	
	/**
	 * 只有韩国开
	 * @return
	 */
	private boolean iskorea(){
		XmlServerManager xsm = XmlServerManager.getInstance();
		List<XmlServer> xs =  xsm.getServers();
		for(XmlServer server : xs){
			if(server.getDescription().equals("ST")){
				return true;
			}
		}
		return false;
	}
	
	private void handlerJsonstr(){
		long now = System.currentTimeMillis();
		String currUrl = "";
		XmlServerManager xsm = XmlServerManager.getInstance();
		List<XmlServer> xs =  xsm.getServers();
		if(xs!=null && xs.size()>0){
			Map<EventType, String> typesnames = null;
			for(XmlServer server : xs){
					if(server.getDescription().contains("S")){
						if(server.getDescription().equals("ST")) {
							continue;
						}
						currUrl = server.getUri();
						String adminurl = currUrl.substring(0,currUrl.indexOf("gm"))+"eventsForJson.jsp";
						HashMap headers = new HashMap();
						String contentP = "&authorize.username="+uname+"&authorize.password="+upwd;
						String result = "";
						try {
							byte[] b = HttpUtils.webPost(new URL(adminurl), contentP.getBytes(), headers, 20000, 20000);
							if(b != null && b.length > 0){
								result = new String(b).trim();
								//如果数据处理出错，打印，请恢复
								try{
									if(result!=null && !"".equals(result.trim())){
										EventForJson eventobject = JsonUtil.objectFromJson(result,EventForJson.class);
										if(eventobject!=null){
											log.warn("[服务器收集数据] [成功] [服务器:"+server.getDescription()+"] [耗时："+(System.currentTimeMillis()-now)+"]");
											handlerEvent(eventobject);
										}
									}
								}catch(Exception e){
									log.warn("[处理数据出错] [服务器:"+server.getDescription()+"] [出错数据：-----"+result+"-----] ",e);
								}
							}
						} catch (SocketTimeoutException e) {
							e.printStackTrace();
							log.warn("[服务器收集数据] [异常] [服务器："+server.getDescription()+"] 【远程服务器关闭】 [耗时："+(System.currentTimeMillis()-now)+"]");
							continue;
						} catch(Throwable e){
							e.printStackTrace();
							log.warn("[服务器收集数据] [异常] [服务器："+server.getDescription()+"] [耗时："+(System.currentTimeMillis()-now)+"]",e);
							continue;
						}
					}
					
			}
			log.warn("【服务器收集数据】  【完毕】 [耗时："+(System.currentTimeMillis()-now)+"]");
		}
	}
	
	/**
	 * 处理数据
	 * @param eventobject
	 */
	public void handlerEvent(EventForJson eventobject){
		if(eventobject!=null){
			for(EventHandler handle:rules){
				handle.initFitRule(eventobject.getEvents(handle.eventtype));
			}
		}
	} 
	
	/**
	 * 更新统计数据
	 * @param es
	 */
	public void updateEventData(List es){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currkey = sdf.format(new Date());
		try{
			if(es!=null && es.size()>0){
				EventData data = (EventData) ddc.get(currkey);
				boolean isnew = false;
				if(data==null){
					isnew = true;
					EventData dd = new EventData();
					dd.setEventKey(currkey);
					ddc.put(currkey, dd);
				}
				data = (EventData) ddc.get(currkey);
				int alist_old = data.getAlist().size();
				int mlist_old = data.getMlist().size();
				int plist_old = data.getPlist().size();
				
				RecordEvent re = (RecordEvent)es.get(0);
				EventType type = re.getEventtype();
				switch (type) {
					case 发送邮件:
						List<MailEvent> mlist = data.getMlist();
						mlist.addAll(es);
						data.setMlist(mlist);
						break;
					case 发送物品:
						List<ArticleRecordEvent> alist = data.getAlist();
						alist.addAll(es);
						data.setAlist(alist);
						break;
					case 属性修改:
						List<PlayerPropertyChangeEvent> plist = data.getPlist();
						plist.addAll(es);
						data.setPlist(plist);	
						break;
				}
				data.setEventKey(currkey);
				ddc.put(currkey, data);
				if(isnew){
					log.warn("[统计数据] [隔天数据:"+currkey+"] [物品数量变化:"+alist_old+"<-->"+data.getAlist().size()+"] [邮件数量变化:"+mlist_old+"<-->"+data.getMlist().size()+"] [属性修改数量变化:"+plist_old+"<-->"+data.getPlist().size()+"]");
				}else{
					log.warn("[统计数据] [当天数据:"+currkey+"] [物品数量变化:"+alist_old+"<-->"+data.getAlist().size()+"] [邮件数量变化:"+mlist_old+"<-->"+data.getMlist().size()+"] [属性修改数量变化:"+plist_old+"<-->"+data.getPlist().size()+"]");
				}
			}
		}catch(Exception e){
			log.warn("[统计数据] [异常]",e);
		}
	}
	
	/**
	 * 获得物品相关规则数据,若新怎新的单服规则，请统一在这配置
	 * @param currkey
	 * @return
	 */
	public Map<String,ServerEventHandle> getServerEventMess(String currkey){
		Map<String,ServerEventHandle> map = new HashMap<String, ServerEventHandle>();
		EventData data = (EventData) ddc.get(currkey);
		if(data!=null){
			List<ArticleRecordEvent> alist = data.getAlist();	
			if(alist!=null && alist.size()>0){
				for(ArticleRecordEvent are : alist){
					if(map.containsKey(are.getServername())){
						ServerEventHandle seh = map.get(are.getServername());
						updateEventData(are,seh);
						map.put(currkey, seh);
					}else{
						ServerEventHandle seh = new ServerEventHandle();
						updateEventData(are,seh);
						map.put(currkey, seh);
					}
				}
				
			}
		}
		return map;
	}

	private void updateEventData(ArticleRecordEvent are ,ServerEventHandle seh){
		for(int i=0;i<are.getArticlenames().length;i++){
			if(are.getArticletype()[i]==1){
				if(are.getColors()[i]==5){
					seh.setEquipment_完美紫_num(seh.getEquipment_完美紫_num()+1);
				}else if(are.getColors()[i]==6){
					seh.setEquipment_橙色_num(seh.getEquipment_橙色_num()+1);
				}else if(are.getColors()[i]==7){
					seh.setEquipment_完美橙_num(seh.getEquipment_完美橙_num()+1);
				}
			}else if(are.getArticletype()[i]==4){
				if(are.getColors()[i]==6){
					seh.setJiu_橙色_num(seh.getJiu_橙色_num()+1);
				}
			}else if(are.getArticletype()[i]==3){
				if(are.getColors()[i]==5){
					seh.setTie_橙色_num(seh.getTie_橙色_num()+1);
				}else if(are.getColors()[i]==6){
					seh.setProp_橙色_num(seh.getProp_橙色_num()+1);
				}
			}else{
				if(are.getColors()[i]==5){
					seh.setProp_紫色_num(seh.getProp_紫色_num()+1);
				}else if(are.getColors()[i]==6){
					seh.setProp_橙色_num(seh.getProp_橙色_num()+1);
				}
			}
		}
	}
	
	public static void sendMail(String title, String content) {
		StringBuffer sb = new StringBuffer();
		sb.append(content);
		sb.append("<br>" + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		ArrayList<String> args = new ArrayList<String>();
		args.add("-username");
		args.add("wtx062@126.com");
		args.add("-password");
		args.add("wangtianxin1986");

		args.add("-smtp");
		args.add("smtp.126.com");
		args.add("-from");
		args.add("wtx062@126.com");
		args.add("-to");
		String address_to = "";

		if (addresses != null) {
			for (String address : addresses) {
				address_to += address + ",";
			}
		}

		if (!"".equals(address_to)) {
			args.add(address_to);
			args.add("-subject");
			args.add("【"+ title + "】");
			args.add("-message");
			args.add(sb.toString());
			args.add("-contenttype");
			args.add("text/html;charset=utf-8");
			try {
				JavaMailUtils.sendMail(args.toArray(new String[0]));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public File getDataFile() {
		return dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

	public DefaultDiskCache getDdc() {
		return ddc;
	}

	public void setDdc(DefaultDiskCache ddc) {
		this.ddc = ddc;
	}

}
