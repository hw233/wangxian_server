package com.fy.engineserver.notice;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.SavingFailedException;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.message.EFFECT_NOTICE_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.Utils;
import com.fy.boss.authorize.model.Passport;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class NoticeManager implements Runnable {

	public static Logger logger = LoggerFactory.getLogger(NoticeManager.class.getName());
	public static SimpleEntityManager<Notice> em;
	
	private String filename;
	
	public static int  开始 = 0;
	public static int  有效 = 1;
	public static int  失效 = 2;
	
	//初始化时赋值   添加新的  可修改
	public List<Notice> circleList = new ArrayList<Notice>();
	public List<Notice> onceList = new ArrayList<Notice>();
	
	//登陆游戏活动公告
	public Notice activityNotice;
	public List<Notice> activityList = new ArrayList<Notice>();
	
	/**永久公告**/
	public Map<String, List<NoticeForever>> foreverNotices = new LinkedHashMap<String, List<NoticeForever>>();
	
	public List<NoticeForever> foreverNotices_china = new LinkedList<NoticeForever>();
	
	//正在生效公告  会变化
	public Notice effectNotice;
	
	//最新的公告，没有过期
	public Notice newNotice;
	
	//新加的公告
	public List<Notice> addNotice = new Vector<Notice>();

	
	private static NoticeManager self;
	
	public boolean open = true;
	
	//凌晨标志 会清空记录
	public boolean temp = false;
	public int tempInt = 0;
	
	public Map<Integer,Notice> notices = new ConcurrentHashMap<Integer, Notice>();
	public void initNoticeList() throws Exception{
		List<Notice> list = em.query(Notice.class, " createTime > 0", "noticeId desc", 1, 10);
		for(Notice n : list){
			if(n != null){
				notices.put(n.getNoticeId(), n);
				logger.warn("[初始化登录公告] [成功] [id:"+n.getId()+"] [nId:"+n.getNoticeId()+"] [name:"+n.getNoticeName()+"]");
			}
		}
	}
	
	public int getTodayKeyI(){
		Calendar cl = Calendar.getInstance();
		int year = cl.get(Calendar.YEAR);
		int month = cl.get(Calendar.MONTH) + 1;
		int day = cl.get(Calendar.DAY_OF_MONTH);
		return Integer.parseInt(year+""+month+""+day);
	}
	
	public static void main(String[] args) {
		Calendar cl = Calendar.getInstance();
		int year = cl.get(Calendar.YEAR);
		int month = cl.get(Calendar.MONTH) + 1;
		int day = cl.get(Calendar.DAY_OF_MONTH);
		System.out.println(Integer.parseInt(year+""+month+""+day));
	}
	
	@Override
	public void run() {
		
		while(open){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(!com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished()){
				continue;
			}
			try{

				if(addNotice != null && addNotice.size() > 0){
					
					// 有新的公告  数据库
					for(Notice n:addNotice){
						if(n.getNoticeType() == 0){
							onceList.add(n);
						}else if(n.getNoticeType() == 1){
							circleList.add(n);
						}else if(n.getNoticeType() == 2){
							activityList.add(n);
						}
					}
					addNotice.clear();
				}
				
				long nowTime = SystemTime.currentTimeMillis();
				
				if(effectNotice != null){
					if(nowTime >= effectNotice.getEndTime() || effectNotice.getEffectState()== NoticeManager.失效){
						//没有有效的公告
						logger.warn("[一个登录公告失效] ["+effectNotice.getLogString()+"]");
						effectNotice = null;
					}
				}
				if(activityNotice != null){
					if(nowTime >= activityNotice.getEndTime() || activityNotice.getEffectState()== NoticeManager.失效){
						//没有有效的公告
						logger.warn("[一个活动公告失效] ["+activityNotice.getLogString()+"]");
						activityNotice = null;
					}
				}
				//心跳判断那个一次性公告生效
				for(Notice n:onceList){
					if(n.getEffectState() == NoticeManager.开始){
						if( nowTime - n.getBeginTime() > 0 && nowTime - n.getBeginTime() < 10000 && nowTime < n.getEndTime() && !n.isDelete()){
							n.setEffectState(NoticeManager.失效);
							logger.warn("[一个临时公告生效] ["+effectNotice.getLogString()+"]");
							EFFECT_NOTICE_RES  res = new EFFECT_NOTICE_RES(GameMessageFactory.nextSequnceNum(), n);
							Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
							for(Player p : ps){
								p.addMessageToRightBag(res);
							}
						}else{
							if(n.getBeginTime() < nowTime && nowTime < n.getEndTime() && !n.isDelete()){
								n.setEffectState(NoticeManager.失效);
								logger.warn("[一个过期临时公告生效] ["+effectNotice.getLogString()+"]");
								EFFECT_NOTICE_RES  res = new EFFECT_NOTICE_RES(GameMessageFactory.nextSequnceNum(), n);
								Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
								for(Player p : ps){
									p.addMessageToRightBag(res);
								}
							}
						}
					}
				}
				
				
				//心跳判断那个登陆公告生效
				for(Notice n:circleList){
					
					if(n.getEffectState() == NoticeManager.开始){
						if( nowTime - n.getBeginTime() > 0 && nowTime < n.getEndTime() && !n.isDelete()){
							n.setEffectState(NoticeManager.有效);
							if(effectNotice != null){
								effectNotice.setEffectState(NoticeManager.失效);
							}
							effectNotice = n;
							logger.warn("[一个登陆公告生效] ["+effectNotice.getLogString()+"]");
						}else{
							if(n.getBeginTime() < nowTime && nowTime < n.getEndTime() && !n.isDelete()){
								n.setEffectState(NoticeManager.有效);
								if(effectNotice != null){
									effectNotice.setEffectState(NoticeManager.失效);
								}
								effectNotice = n;
								logger.warn("[一个过期登陆公告生效] ["+effectNotice.getLogString()+"]");
							}
						}
					}
				}
				
				//心跳判断哪个活动公告生效
				for(Notice n : activityList){
					if(n.getEffectState() == NoticeManager.开始){
						if( nowTime - n.getBeginTime() > 0 && nowTime < n.getEndTime() && !n.isDelete()){
							n.setEffectState(NoticeManager.有效);
							if(activityNotice != null){
								activityNotice.setEffectState(NoticeManager.失效);
							}
							activityNotice = n;
							logger.warn("[一个活动公告生效] ["+activityNotice.getLogString()+"]");
						}else{
							if(n.getBeginTime() < nowTime && nowTime < n.getEndTime() && !n.isDelete()){
								n.setEffectState(NoticeManager.有效);
								if(activityNotice != null){
									activityNotice.setEffectState(NoticeManager.失效);
								}
								activityNotice = n;
								logger.warn("[一个过期活动公告生效] ["+activityNotice.getLogString()+"]");
							}
						}
					}
				}
			}catch(Throwable e){
				logger.error("[公告心跳线程错误]", e);
			}
		}
	}
	
//	//通知生效  清空记录
//	public void noticeEffect(){
////		loginSendList.clear();
////		obtainBindSivlerList.clear();
//	}
	
	public void createNotice(Notice notice){
		//直接放入add列表
		try {
			int noticeId = getTodayKeyI();
			notice.setNoticeId(noticeId);
			em.save(notice);
			notice.setCreateTime(System.currentTimeMillis());
			this.addNotice.add(notice);
			handlerAddNotice(noticeId,notice);
			newNotice = notice;
			logger.warn("[新增公告] ["+notice.getLogString()+"]");
		} catch (Exception e) {
			logger.error("[新增公告异常] ["+notice.getLogString()+"]",e);
		}
		
	}
	
	public void handlerAddNotice(int noticeId,Notice notice){
		notices.put(noticeId, notice);
		if(notices.size() > 10){
			Integer minid = Integer.MAX_VALUE;
			Iterator<Integer> it = notices.keySet().iterator();
			while(it.hasNext()){
				Integer key = it.next();
				if(key != null){
					if(minid > key){
						minid = key;
					}
				}
			}
			notices.remove(minid);
		}
	}
	
	
	public void getEffectNotice(Player player){
		
		if(this.newNotice!=null){
			if(this.newNotice.getId() == player.getObtainNoticeId()){
				try {
					Notice notice = (Notice) newNotice.clone();
					EFFECT_NOTICE_RES res = new EFFECT_NOTICE_RES(GameMessageFactory.nextSequnceNum(), notice);
					player.addMessageToRightBag(res);
					logger.warn("[公告请求] [已经领取过] ["+player.getLogString()+"] ["+notice.getLogString()+"]");
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}else {
				EFFECT_NOTICE_RES res = new EFFECT_NOTICE_RES(GameMessageFactory.nextSequnceNum(), newNotice);
				player.addMessageToRightBag(res);
				logger.warn("[公告请求] [没有领取] ["+player.getLogString()+"] ["+newNotice.getLogString()+"]");
			}
		}else{
			player.sendError(Translate.没有有效的公告);
			logger.warn("[公告请求] [没有有效的公告] ["+player.getLogString()+"]");
		}
		
		return;
		
//		if(this.effectNotice != null){
//			int num = effectNotice.getBindSivlerNum();
//			if(num > 0){
//				if(this.getEffectNotice().getId() == player.getObtainNoticeId()){
//					EFFECT_NOTICE_RES res = null;
//					try {
//						res = new EFFECT_NOTICE_RES(GameMessageFactory.nextSequnceNum(), (Notice) effectNotice.clone());
//						player.addMessageToRightBag(res);
//						logger.warn("[玩家查看登陆公告] [有绑银] [已经领取] ["+player.getLogString()+"] ["+effectNotice.getLogString()+"]");
//					} catch (CloneNotSupportedException e) {
//						logger.warn("[玩家查看登陆公告异常] [有绑银] [已经领取] ["+player.getLogString()+"] ["+effectNotice.getLogString()+"]",e);
//					}
//
//				}else{
//					EFFECT_NOTICE_RES res = new EFFECT_NOTICE_RES(GameMessageFactory.nextSequnceNum(), effectNotice);
//					player.addMessageToRightBag(res);
//					logger.warn("[玩家查看登陆公告] [有绑银] ["+player.getLogString()+"] ["+effectNotice.getLogString()+"]");
//				}
//			}else{
//				EFFECT_NOTICE_RES res = new EFFECT_NOTICE_RES(GameMessageFactory.nextSequnceNum(), effectNotice);
//				player.addMessageToRightBag(res);
//				logger.warn("[玩家查看登陆公告] [没有绑银] ["+player.getLogString()+"] ["+effectNotice.getLogString()+"]");
//			}
//		}else{
//			player.sendError(Translate.没有有效的公告);
//			logger.warn("[玩家查看登陆公告] [没有有效的公告] ["+player.getLogString()+"]");
//		}
	}
	
	//登陆游戏发送活动公告
	public void enterServerSendActivityNotice(Player player){
		try{
			if(this.activityNotice != null && this.activityNotice.effectState == 有效){
				long now = SystemTime.currentTimeMillis();
				if(this.getEffectNotice() != null && player.getNoticeId() != this.getEffectNotice().getId()){
					player.setPopActivityNoticeTime(now);
					player.setActivityNoticeId(this.activityNotice.getId());
					logger.warn("[登陆发送活动公告失败] [有登陆公告] ["+player.getLogString()+"] ["+effectNotice.getNoticeName()+"]");
				}else{
					if(player.getPopActivityNoticeTime() <= 0 || player.getActivityNoticeId() != this.activityNotice.getId() ||!Utils.checkSameDay(player.getPopActivityNoticeTime(), now)){
						if(this.activityNotice.getQudao()!=null && this.activityNotice.getQudao().trim().length()>0){
							Passport passport = player.getPassport();
							if(passport!=null){
								String qds [] = this.activityNotice.getQudao().split(",");
								logger.warn("[玩家登陆发送公告] [测试2] [qds:"+Arrays.toString(qds)+"] ["+passport.getLastLoginChannel()+"] ["+this.activityNotice.getQudao()+"] ["+player.getLogString()+"] ["+activityNotice.getLogString()+"]");
								if(passport.getLastLoginChannel()!=null && qds!=null){
									for(String q:qds){
										if(q.trim().equals(passport.getLastLoginChannel())){
											EFFECT_NOTICE_RES res = new EFFECT_NOTICE_RES(GameMessageFactory.nextSequnceNum(), activityNotice);
											player.addMessageToRightBag(res);
											player.setNoticeId(this.getEffectNotice().getId());
											logger.warn("[玩家登陆发送公告] [loginchannel:"+passport.getLastLoginChannel()+"] ["+player.getLogString()+"] ["+activityNotice.getLogString()+"]");
											return;
										}
									}
								}
							}
						}else{
							EFFECT_NOTICE_RES res = new EFFECT_NOTICE_RES(GameMessageFactory.nextSequnceNum(), activityNotice);
							player.addMessageToRightBag(res);
							player.setPopActivityNoticeTime(now);
							player.setActivityNoticeId(this.getActivityNotice().getId());
							logger.warn("[登陆发送活动公告成功] ["+player.getLogString()+"] ["+activityNotice.getNoticeName()+"]");
						}
						
					}else{
						logger.warn("[登陆发送活动公告失败] [已经弹过] ["+player.getLogString()+"] ["+activityNotice.getNoticeName()+"]");
					}
				}
			}else{
				logger.warn("[登陆发送活动公告成功] ["+player.getLogString()+"] [没有有效公告]");
			}
		}catch (Exception e) {
			logger.error("[登陆发送活动公告异常] ["+player.getLogString()+"]",e);
		}
	}
	
	public void enterServerSendNotice(Player player){
		try{
			if(this.effectNotice != null){
				if(player.getNoticeId() != this.getEffectNotice().getId()){
					if(this.effectNotice.getQudao()!=null && this.effectNotice.getQudao().trim().length()>0){
						Passport passport = player.getPassport();
						if(passport!=null){
							String qds [] = this.activityNotice.getQudao().split(",");
							logger.warn("[玩家登陆发送公告2] [测试2] [qds:"+Arrays.toString(qds)+"] ["+passport.getLastLoginChannel()+"] ["+this.effectNotice.getQudao()+"] ["+player.getLogString()+"] ["+effectNotice.getLogString()+"]");
							if(passport.getLastLoginChannel()!=null && qds!=null){
								for(String q:qds){
									if(q.trim().equals(passport.getLastLoginChannel())){
										EFFECT_NOTICE_RES res = new EFFECT_NOTICE_RES(GameMessageFactory.nextSequnceNum(), effectNotice);
										player.addMessageToRightBag(res);
										player.setNoticeId(this.getEffectNotice().getId());
										logger.warn("[玩家登陆发送公告2] [loginchannel:"+passport.getLastLoginChannel()+"] ["+player.getLogString()+"] ["+effectNotice.getLogString()+"]");
										return;
									}
								}
							}
						}
					}else{
						EFFECT_NOTICE_RES res = new EFFECT_NOTICE_RES(GameMessageFactory.nextSequnceNum(), effectNotice);
						player.addMessageToRightBag(res);
						player.setNoticeId(this.getEffectNotice().getId());
						logger.warn("[玩家登陆发送公告2] ["+player.getLogString()+"] ["+effectNotice.getLogString()+"]");
					}
				}
				}
		}catch(Exception e){
			logger.error("[玩家登陆接受公告错误] ["+player.getLogString()+"]",e);
		}
	}
	
	// 领取绑银
	public boolean obtainBindSivler(Player player){
		if(this.effectNotice != null){
			if(effectNotice.isHavaBindSivler()){
//				long playerNoticeId = player.getNoticeId();
				long obtainNoticeId = player.getObtainNoticeId();
				if(obtainNoticeId == this.getEffectNotice().getId()){
					logger.error("[已经领取了] ["+player.getLogString()+"] ["+effectNotice.getLogString()+"]");
					player.sendError(Translate.已经领取了没有绑银可以领取);
				}else{
					int num = effectNotice.getBindSivlerNum();
					if(num > 0){
					
						player.setObtainNoticeId(this.getEffectNotice().getId());
//						player.setBindSilver(player.getBindSilver()+num);
						
						try {
							BillingCenter.getInstance().playerSaving(player, num, CurrencyType.BIND_YINZI, SavingReasonType.通知奖励, "通知奖励");
//							player.send_HINT_REQ("領取绑银"+BillingCenter.getInstance().得到带单位的银两(num));
							player.send_HINT_REQ(Translate.translateString(Translate.領取绑银xx, new String[][]{{Translate.STRING_1,BillingCenter.getInstance().得到带单位的银两(num)}}));
							logger.error("[登录公告领取绑银] ["+num+"] ["+player.getLogString()+"] ["+effectNotice.getLogString()+"]");
							Notice notice = null;
							try {
								notice = (Notice) effectNotice.clone();
							} catch (CloneNotSupportedException e) {
								e.printStackTrace();
							}
							if(notice==null){
								notice = effectNotice;
							}
							EFFECT_NOTICE_RES res = new EFFECT_NOTICE_RES(GameMessageFactory.nextSequnceNum(), notice);
							player.addMessageToRightBag(res);
							return true;
						} catch (SavingFailedException e) {
							logger.error("[登陆领取绑银异常] ["+player.getLogString()+"]",e);
						}
					}else{
						logger.error("[没有绑银可以领取] ["+player.getLogString()+"] ["+effectNotice.getLogString()+"]");
						player.sendError(Translate.没有绑银可以领取);
					}
				}
				
			}else{
				logger.error("[当前登录公告没有绑银] ["+player.getLogString()+"] ["+effectNotice.getLogString()+"]");
			}
		}else{
			logger.error("[当前登录公告为null] ["+player.getLogString()+"]");
		}
		if(effectNotice!=null){
			EFFECT_NOTICE_RES res = new EFFECT_NOTICE_RES(GameMessageFactory.nextSequnceNum(), effectNotice);
			player.addMessageToRightBag(res);
		}
		
		return false;
	}
	
	
	public void init() throws Exception{
		
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		self = this;
		if(PlatformManager.getInstance().isPlatformOf(Platform.韩国)){
			initForeverNotice();
		}
		List<Notice> list = null;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(Notice.class);
		list = em.query(Notice.class, "effectState < ?", new Object[]{2},null, 1, 1000);
		
		if(list != null && list.size() >0){
			for(Notice n : list){
				if(n.getNoticeType() == 0){
					//临时
					onceList.add(n);
				}else if(n.getNoticeType() == 1){
					//登陆
					circleList.add(n);
				}else if(n.getNoticeType() == 2){
					activityList.add(n);
				}
			}
			
			for(Notice n:circleList){
				if(n.getEffectState() == NoticeManager.有效 && !n.isDelete()){
					if(start < n.getEndTime()){
						effectNotice = n;
						logger.warn("[初始化登录公告生效] ["+effectNotice.getLogString()+"]");
						break;
					}else{
						effectNotice = null;
						n.setEffectState(NoticeManager.失效);
					}
				}
			}
			
			for(Notice n:activityList){
				if(n.getEffectState() == NoticeManager.有效 && !n.isDelete()){
					if(start < n.getEndTime()){
						activityNotice = n;
						logger.warn("[初始化活动公告生效] ["+activityNotice.getLogString()+"]");
						break;
					}else{
						activityNotice = null;
						n.setEffectState(NoticeManager.失效);
					}
				}
			}
		}else{
			NoticeManager.logger.warn("[初始化NoticeManager] [没有公告]");
		}
		Thread t = new Thread(this, "NoticeManager");
		t.start();
		
		newNotice =  initNewNotice();
		initNoticeList();
		NoticeManager.logger.warn("[初始化NoticeManager成功]");
		ServiceStartRecord.startLog(this);
	}
	
	public Notice initNewNotice(){
		long now = System.currentTimeMillis();
		List<Notice> list = null;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(Notice.class);
		try {
			list = em.query(Notice.class, " createTime > 0", "createTime desc", 1, 10);
		} catch (Exception e) {
			e.printStackTrace();
			NoticeManager.logger.warn("[获得最新的公告] [异常] [没有记录] [耗时:"+(System.currentTimeMillis()-now)+"+ms]",e);
		}
		if(list != null && list.size() >0){
			Notice notice = list.get(0);
			NoticeManager.logger.warn("[获得最新的公告] [成功] ["+notice.getNoticeName()+"] ["+TimeTool.formatter.varChar23.format(notice.getCreateTime())+"] [耗时:"+(System.currentTimeMillis()-now)+"+ms]");
			return notice;
		}else{
			NoticeManager.logger.warn("[获得最新的公告] [失败] [没有记录] [耗时:"+(System.currentTimeMillis()-now)+"+ms]");
		}
		return null;
	}
	
	public void destroy(){
		em.destroy();
	}

	public void initForeverNotice(){
		File file =  new File(filename);
		try {
			Workbook book = Workbook.getWorkbook(file);
			Sheet sheet = book.getSheet(0);
			int allRows = sheet.getRows();
			List<NoticeForever> list = new LinkedList<NoticeForever>();
			
			for(int j=1;j<allRows;j++){
				Cell[] cells = sheet.getRow(j);
				if(cells!=null){
					int index = 0;
					NoticeForever nf = new NoticeForever();
					nf.setId(Integer.parseInt(cells[index++].getContents()));
					nf.setTypename(cells[index++].getContents());
					nf.setTitlename(cells[index++].getContents());
					nf.setNoticeContent(cells[index++].getContents());
					nf.setPlatformname(new Platform[] { Platform.韩国 });
					nf.setStarttime(TimeTool.formatter.varChar19.parse(cells[index++].getContents()));
					nf.setEndtime(TimeTool.formatter.varChar19.parse(cells[index++].getContents()));
					String[] showservers = new String[0];
					String value = (StringTool.modifyContent(cells[index++]));
					if (value == null || "".equals(value)) {

					} else {
						showservers = StringTool.string2Array(value, ",", String.class);
					}
					String[] limitservers = new String[0];
					value = (StringTool.modifyContent(cells[index]));
					if (value == null || "".equals(value)) {

					} else {
						limitservers = StringTool.string2Array(value, ",", String.class);
					}
					nf.setOpenServers(showservers);
					nf.setLimitServers(limitservers);
					list.add(nf);
					/**
					 * 记得要加载中文档
					 * 
					 */
					foreverNotices_china.add(nf);
				}else{
					continue;
				}
			}
			for(NoticeForever nf:list){
				if(foreverNotices.containsKey(nf.getTypename())){
					List<NoticeForever> nfs = foreverNotices.get(nf.getTypename());
					nfs.add(nf);
					foreverNotices.put(nf.getTypename(), nfs);
				}else{
					List<NoticeForever> nfs = new LinkedList<NoticeForever>();
					nfs.add(nf);
					foreverNotices.put(nf.getTypename(), nfs);
				}
			}
			
			System.out.println("初始化initForeverNotice 成功。。list长度："+list.size()+"--mapkey:"+foreverNotices.keySet().size());
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println("[初始化NoticeManager] [initForeverNotice()] [异常："+e+"]");
		}
		
		
	}
	
	public static NoticeManager getInstance(){
		return self;
	}
	
	public List<Notice> getCircleList() {
		return circleList;
	}
	public void setCircleList(List<Notice> circleList) {
		this.circleList = circleList;
	}
	public List<Notice> getOnceList() {
		return onceList;
	}
	public void setOnceList(List<Notice> onceList) {
		this.onceList = onceList;
	}
	public Notice getEffectNotice() {
		return effectNotice;
	}
	public void setEffectNotice(Notice effectNotice) {
		this.effectNotice = effectNotice;
	}

	public Notice getActivityNotice() {
		return activityNotice;
	}
	public void setActivityNotice(Notice activityNotice) {
		this.activityNotice = activityNotice;
	}
	public List<Notice> getActivityList() {
		return activityList;
	}
	public void setActivityList(List<Notice> activityList) {
		this.activityList = activityList;
	}

	public Map<String, List<NoticeForever>> getForeverNotices() {
		return foreverNotices;
	}

	public void setForeverNotices(Map<String, List<NoticeForever>> foreverNotices) {
		this.foreverNotices = foreverNotices;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public List<NoticeForever> getForeverNotices_china() {
		return foreverNotices_china;
	}

	public void setForeverNotices_china(List<NoticeForever> foreverNotices_china) {
		this.foreverNotices_china = foreverNotices_china;
	}
	
}
