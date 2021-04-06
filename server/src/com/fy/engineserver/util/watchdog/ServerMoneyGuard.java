package com.fy.engineserver.util.watchdog;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import org.slf4j.Logger;

import com.fy.engineserver.economic.BillingCenter;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.mail.JavaMailUtils;
import com.xuanzhi.tools.text.DateUtil;

/**
 * 检查库存wtx
 */
public abstract class ServerMoneyGuard implements Runnable {
	
	public static Logger logger = BillingCenter.loggerA;
	
	private ServerMoney last;
	
	public long warnChange = 49000000;
	
	public String receiver = "3472335707@qq.com,116004910@qq.com";
		
	private long chongzhi;
	
	private LinkedList<AddYingzi> yingziList;
		
	private ArrayList<AddYingzi> scanYingzi;
	
	private static ServerMoneyGuard self;
	
	private Thread thread;
	
	private boolean running;
	
	private long lastClearTime;
	
	private long lastScanTime;
	
	private long expireTime = 1000 * 60 * 60 * 24;
		
	private AddYingzi lastScanYingzi;
		
	private boolean openning = false;
	
	private long clearPeriod = 10 * 60 * 1000;
	
	private long scanPeriod = 30 * 1000;
		
	public static ServerMoneyGuard getInstance() {
		return self;
	}
	
	public void init() {
		yingziList = new LinkedList<AddYingzi>();
		scanYingzi = new ArrayList<AddYingzi>();
		if(openning) {
			start();
		}
		self = this;
		logger.info("[ServerGuard初始化完成] [openning:"+openning+"] [thread:"+thread+"]");
	}
	
	public void setClearPeriod(long clearPeriod) {
		this.clearPeriod = clearPeriod;
	}

	public void setScanPeriod(long scanPeriod) {
		this.scanPeriod = scanPeriod;
	}

	public void setOpenning(boolean openning) {
		this.openning = openning;
	}

	public boolean isOpenning() {
		return openning;
	}

	public void setWarnChange(long warnChange) {
		this.warnChange = warnChange;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	public void start() {
		if(running == false) {
			running = true;
			thread = new Thread(this, "ServerMoneyGuard");
			thread.start();
			logger.info("[ServerGuard启动扫描] [openning:"+openning+"] [thread:"+thread+"]");
		}
	}
	
	public void stop() {
		running = false;
	}
	
	/**
	 * 一元可以兑换多少游戏币
	 * @return
	 */
	public abstract double getExchangeRate();
	
	/**
	 * 游戏的名字
	 * @return
	 */
	public abstract String getGameName();
	
	/**
	 * 服务器的名字
	 * @return
	 */
	public abstract String getServerName();
	
	/**
	 * 获取服务器的游戏币存量
	 * @return
	 */
	public abstract long scanYingziInDB();
	
	public synchronized void notifyAddChongzhi(long amount) {
		this.chongzhi += amount;
	}
	
	public synchronized void notifyAddYingzi(long playerId, String playername, String username, long amount, String reason) {
		AddYingzi yingzi = new AddYingzi();
		yingzi.setPlayerId(playerId);
		yingzi.setName(playername);
		yingzi.setUsername(username);
		yingzi.setTime(System.currentTimeMillis());
		yingzi.setAmount(amount);
		yingzi.setReason(reason);
		yingziList.add(yingzi);
	}
	
	public void run() {
		while(running) {
			try {
				Thread.sleep(1000);
				long now = System.currentTimeMillis();
				if(now - lastClearTime > clearPeriod) {
					lastClearTime = now;
					clearRecords();
				}
				if(now - lastScanTime > scanPeriod) {
					lastScanTime = now;
					ServerMoney sm = scanServerMoney();
					checkServerMoney(sm);
				}
			} catch(Throwable e) {
				logger.error("[线程发生异常]", e);
				try {
					Thread.sleep(1000);
				} catch(Exception ee) {
					ee.printStackTrace();
				}
			}
		}
		running = false;
	}
	
	public void checkServerMoney(ServerMoney sm) {
		long start = System.currentTimeMillis();
		if(last == null) {
			last = sm;
			return;
		}
		long yingziChange = sm.getYingzi() - last.getYingzi();
		long chongzhiChange = sm.getChongzhi() - last.getChongzhi();
		//如果银子的变化比充值变化兑换成银子的50%还少，那么进行报警
		long objChongzhi = new Double(yingziChange/getExchangeRate()).longValue()*100/2;
		if(yingziChange >= warnChange && chongzhiChange < objChongzhi) {
			logger.warn("[警告：银子变化和充值变化不成比例!] [银子差值:"+(sm.getYingzi()-last.getYingzi())+"] [充值差值:"+(sm.getChongzhi()-last.getChongzhi())/100+"元] [最低充值要求:"+objChongzhi+"] [上一次银子:"+last.getYingzi()+"] [上一次充值:"+last.getChongzhi()+"] [本次银子:"+sm.getYingzi()+"] [本次充值:"+sm.getChongzhi()+"] ["+(System.currentTimeMillis()-start)+"ms] 以下为本次扫描的银子变化记录: ");
			String title = "["+getGameName()+"]["+getServerName()+"][银子异动报警!]";
			StringBuffer sb = new StringBuffer();
			sb.append("<html><body><br>本次扫描发现银子增量异常，银子共增加"+yingziChange+"，但是充值增量为:"+chongzhiChange/100 + "元，而最低充值要求为："+objChongzhi/100+"元，以下为本周期内扫描的银子变化记录: <br><br>");
			for(AddYingzi yingzi : scanYingzi) {
				logger.warn("[amount:"+yingzi.getAmount()+"] [playerId:"+yingzi.getPlayerId()+"] [name:"+yingzi.getName()+"] [user:"+yingzi.getUsername()+"] [reason:"+yingzi.getReason()+"] [time:"+DateUtil.formatDate(new Date(yingzi.getTime()),"yyyyMMdd_HH:mm:ss")+"]");
				sb.append("&nbsp;&nbsp;&nbsp;&nbsp;" + "[amount:"+yingzi.getAmount()+"] [playerId:"+yingzi.getPlayerId()+"] [name:"+yingzi.getName()+"] [user:"+yingzi.getUsername()+"] [reason:"+yingzi.getReason()+"] [time:"+DateUtil.formatDate(new Date(yingzi.getTime()),"yyyyMMdd_HH:mm:ss")+"]<br>");
			}
			sb.append("<br>" + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			sb.append("</body></html>");
			sendMail(title, sb.toString());
		} else {
			//logger.warn("[例常扫描存量银子] [正常<"+warnChange+"] [银子差值:"+(sm.getYingzi()-last.getYingzi())+"] [充值差值:"+(sm.getChongzhi()-last.getChongzhi())/100+"元] [上一次银子:"+last.getYingzi()+"] [上一次充值:"+last.getChongzhi()+"] [本次银子:"+sm.getYingzi()+"] [本次充值:"+sm.getChongzhi()+"] ["+(System.currentTimeMillis()-start)+"ms]");
		}
		last = sm;
	}
	
	public synchronized ServerMoney scanServerMoney() {
		long now = System.currentTimeMillis();
		boolean flag = false;
		scanYingzi.clear();
		for(AddYingzi item : yingziList) {
			if(flag) {
				scanYingzi.add(item);
				lastScanYingzi = item;
			}
			if(item == lastScanYingzi) {
				flag = true;
			}
		}
		if(!flag) {
			for(AddYingzi item : yingziList) {
				lastScanYingzi = item;
				scanYingzi.add(item);
			}
		}
		ServerMoney sm = new ServerMoney();
		sm.setChongzhi(new Long(chongzhi));
		sm.setScanTime(System.currentTimeMillis());
		long sy = scanYingziInDB();
		if(sy != -1) {
			sm.setYingzi(sy);
		}
		if(logger.isDebugEnabled()) {
			logger.debug("[扫描数据] [银子存量:"+sm.getYingzi()+"] [充值累计:"+sm.getChongzhi()+"] ["+(System.currentTimeMillis()-now)+"ms]");
		}
		return sm;
	}
	
	public synchronized void clearRecords() {
		long now = System.currentTimeMillis();
		//清除过期的银子增加记录
		Iterator<AddYingzi> ite2 = yingziList.iterator();
		int yingziNum = 0;
		while(ite2.hasNext()) {
			AddYingzi item = ite2.next();
			if(now - item.getTime() > expireTime) {
				ite2.remove();
				yingziNum++;
			}
		}
		if(logger.isDebugEnabled()) {
			logger.debug("[清除过期的数据] [清除银子:"+yingziNum+"] [yingziListSize:"+yingziList.size()+"] [scanYingziSize:"+scanYingzi.size()+"] ["+(System.currentTimeMillis()-now)+"ms]");
		}
	}
	
	
	
	public static void sendMail(String title, String content) {
		GameConstants gc = GameConstants.getInstance();
		StringBuffer sb = new StringBuffer();
		sb.append(content);
		sb.append("<HR>" + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
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

		String[] addresses = {"3472335707@qq.com","116004910@qq.com"};
		if (addresses != null) {
			for (String address : addresses) {
				address_to += address + ",";
			}
		}

		if (!"".equals(address_to)) {
			args.add(address_to);
			args.add("-subject");
			if(gc != null){
				args.add(title + "["+gc.getServerName()+"]");
			}else{
				args.add(title);
			}
			args.add("-message");
			args.add(sb.toString());
			args.add("-contenttype");
			args.add("text/html;charset=utf-8");
			//System.out.println(Arrays.toString(args.toArray(new String[0])));
			try {
				JavaMailUtils.sendMail(args.toArray(new String[0]));
				//System.out.println(Arrays.toString(args.toArray(new String[0])));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
//	public void sendMail(String title, String content) {
//		ArrayList<String> args = new ArrayList<String>();
//		args.add("-username");
//		args.add("sqagemail");
//		args.add("-password");
//		args.add("12qwaszx");
//		args.add("-smtp");
//		args.add("smtp.163.com");
//		args.add("-from");
//		args.add("sqagemail@163.com");
//		args.add("-to");
//		args.add(receiver);
//		args.add("-subject");
//		args.add(title);
//		args.add("-message");
//		args.add(content);
//		args.add("-contenttype");
//		args.add("text/html;charset=gbk");
//		try {
//			String str[] = args.toArray(new String[0]);
//			if(logger.isDebugEnabled()) {
//				logger.debug("[发送邮件] ["+StringUtil.arrayToString(str, " ")+"]");
//			}
//			JavaMailUtils.sendMail(str);
//		} catch (Exception e) {
//			logger.error("发送告警邮件失败", e);
//		}
//	}
}
