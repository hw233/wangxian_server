package com.fy.engineserver.util.gmstat;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.gmstat.EventForJson.EventType;
import com.fy.engineserver.util.gmstat.event.ArticleRecordEvent;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.text.JsonUtil;

/**
 * 事件管理
 * 如果增加新的事件，请配置EventForJson
 * 
 *
 */
public class EventManager {

	private static EventManager self;
	
	public static Logger logger = LoggerFactory.getLogger(EventManager.class);
	
	private List<RecordEvent> events;
	
	private EventForJson eventjson;
	
	private String filename;
	
	private DiskCache ddc;
	
	/**
	 * 防止处理方出错，clear事件数据的时候丢数据
	 */
	private String JSON串;
	
	public static EventManager getInstance(){
		return self;
	}
	
	public void init() throws Exception{
		
		self = this;
		
		try{
			File file = new File(filename);
			ddc = new DefaultDiskCache(file, null, "事件管理", 100L * 365 * 24 * 3600 * 1000L);
			initEvents();
			if(events==null){
				events = new LinkedList<RecordEvent>();
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("[EventManager 初始化异常]"+e);
			throw new Exception("EventManager事件管理器初始化异常");
		}
		ServiceStartRecord.startLog(this);
	}

	private void initEvents(){
		events = (List<RecordEvent>)ddc.get("EventManager20130616");
	}
	
	public void eventAdd(RecordEvent e){
		events.add(e);
		logger.warn("【生成新事件】 [数量："+events.size()+"] ["+e.toString()+"]");
	}
	
	/**
	 * 获得事件json串
	 * @return
	 */
	public String getEventsForJson(){
		try{	
			if(eventjson==null){
				eventjson = new EventForJson();
			}
			for(RecordEvent e : events){
				eventjson.addEvents(e);
			}
			JSON串 = JsonUtil.jsonFromObject(eventjson);
			events.clear();
			eventjson.clearEvents();
			return JSON串;
		}catch(Exception e){
			e.printStackTrace();
			logger.warn("[事件管理] [获得EventsForJson] [异常]",e);
		}
		return "";
	}
	
	public void destroy(){
		ddc.put("EventManager20130616", (Serializable) events);
		System.out.println("[EventManager destory] [events:"+events.size()+"]");
	}
	
	public DiskCache getDdc() {
		return ddc;
	}

	public void setDdc(DiskCache ddc) {
		this.ddc = ddc;
	}

	public List<RecordEvent> getEvents() {
		return events;
	}

	public void setEvents(List<RecordEvent> events) {
		this.events = events;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	/**判断物品类型
	 * * 1:装备
	 * 2:一般道具
	 * 3:帖
	 * 4:酒
	 */
	public int getArticleType(Article article){
		int type = -1;
		if(article.get物品二级分类().equals("酒")){
			type = 4;
		}else if(article.get物品二级分类().equals("封魔录")){
			type = 3;
		}else if(article instanceof Equipment){
			type = 1;
		}else{
			type = 2;
		}
		return type;
	}
	
	public static void main(String[] args) throws Exception {
		EventManager em = new EventManager();
		ArticleRecordEvent a = new ArticleRecordEvent(new String[]{"xinhuac","xinhua2c"}, new int[]{3}, "wtx", "116.234.44.2", "liuyang");
//		PlayerPropertyChangeEvent p = new PlayerPropertyChangeEvent("敏捷", 1, 100, "wtx", "111.222.333.444", "wtxw");
//		MailEvent m = new MailEvent("hello world", "2", new String[]{"xinhuac222"}, new int[]{3}, 2, "ww",  "116.234.44.2", "liuyang");
		em.setEvents(new ArrayList<RecordEvent>());
		em.getEvents().add(a);
//		em.getEvents().add(p);
//		em.getEvents().add(m);
		String ss = em.getEventsForJson();
		EventForJson eventobject = JsonUtil.objectFromJson(ss,EventForJson.class);
		List<ArticleRecordEvent> alist = eventobject.getEvents(EventType.发送物品);
		System.out.println(alist);
//		List<ArticleRecordEvent> alist = eventobject.getAlist();
//		List<MailEvent> mlist = eventobject.getMlist();
//		List<PlayerPropertyChangeEvent> pList = eventobject.getPlist();
		for(ArticleRecordEvent ae : alist){
			System.out.println("发送物品："+Arrays.toString(ae.getArticlenames()));
			System.out.println("发送物品："+ae.getIpaddress()+"--"+ae.getEventtype());
		}
//		for(MailEvent me : mlist){
//			System.out.println("发送邮件："+me.getMailTitle()+"--"+me.getEventtype());
//		}
//		for(PlayerPropertyChangeEvent me : pList){
//			System.out.println("属性："+me.getPropertyName()+"--"+me.getEventtype());
//		}
//		System.out.println(alist);
		
//		Map<EventType, List<ArticleRecordEvent>> map = eventobject.getTypename();
//		System.out.println(map);
		
		
		
//		for(EventType type:EventType.values()){
//			if(map.get(type).size()>0){
//				if(type == EventType.发送物品){
//					List<ArticleRecordEvent> alist = JsonUtil.objectFromJson(map.get(type).toString(),ArrayList<ArticleRecordEvent>().class);;
//					for(int i=0;i<alist.size();i++){
//						ArticleRecordEvent ae = JsonUtil.objectFromJson(alist.get(i).toString(),ArticleRecordEvent.class);
//					}
////					for(ArticleRecordEvent ae : alist){
////						System.out.println(ae.getArticlenames());
////					}
//				}
//			}
//		}
		
		
//		Object[] events = JsonUtil.objectFromJson(ss,Object[].class);
//		System.out.println(ss);
//		if(events!=null && events.length>0){
//			for(Object e:events){
//				
//				Map map = (Map)e;
//		
//					
//				if(map.get("className").equals("ArticleRecordEvent"))
//				{
//					ArticleRecordEvent articleRecordEvent = new ArticleRecordEvent();
//					Method[] methods = ArticleRecordEvent.class.getDeclaredMethods();
//
//					for(Method me : methods)
//					{
//						me.setAccessible(true);
//
//						for(Object oo : map.keySet())
//						{
//							String str = (String)oo;
//							if(me.getName().toLowerCase().contains("set") && (me.getName().toLowerCase().contains(str) ))
//							{
//								Class cl = me.getParameterTypes()[0];
//								
//								System.out.println(cl.getName());
//								System.out.println("value:"+map.get(str));
//								if(cl.getName().contains("[") && map.get(str))
//								
//								me.invoke(articleRecordEvent, cl.cast(map.get(str)));
//							}
//						}
//					}
//
//					System.out.println(articleRecordEvent.getArticlenames()[0]);
//				}
//				
////				if(e.getClassName().equals("ArticleRecordEvent")){
////					ArticleRecordEvent are = (ArticleRecordEvent)e;
////					System.out.println(are.getArticlenames()+"=======================");
////				}else if(e.getClassName().equals("MailEvent")){
////					MailEvent me = (MailEvent)e;
////				}else if(e.getClassName().equals("PlayerPropertyChangeEvent")){
////					PlayerPropertyChangeEvent pe = (PlayerPropertyChangeEvent)e;
////				}
//			}
//		}
//		
	}
	
}
