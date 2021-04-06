package com.fy.engineserver.util.whitelist;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.gateway.MieshiGatewayClientService;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WHITE_LIST_REQ;
import com.fy.engineserver.message.QUERY_WHITE_LIST_RES;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.server.TestServerConfigManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.mail.JavaMailUtils;
import com.xuanzhi.tools.text.DateUtil;

public class WhiteListManager implements Runnable {

	private static WhiteListManager self;

	private Map<String, WhitePlayer> infos = new Hashtable<String, WhiteListManager.WhitePlayer>();

	private boolean isStart = false;

	private long SLEEP_TIME = 10 * 60 * 1000;
	public static long SYNCH_DATA_TIME = 30 * 60 * 1000;

	public boolean isStartServer = true;

	public static Map<Platform, String[]> platFormMailAddress = new HashMap<Platform, String[]>();

	private String MAIL_TITLE_PART1 = "[飘渺寻仙曲] [" + GameConstants.getInstance().getServerName() + "] [" + PlatformManager.getInstance().getPlatform().toString() + "] [白名单操作]";
	private String MAIL_TITLE_PART2 = "<tr bgcolor='green'><td>登录渠道</td><td>公司人员</td><td>公司职称</td><td>白名单玩家账号</td><td>ID</td><td>角色名</td><td>登录渠道</td>";

	/**
	 * key: type
	 * value: mailContent
	 */
	private Map<String, ArrayList<String>> cache = new HashMap<String, ArrayList<String>>();

	public final static int SEND_MAIL_NUM = 10;

	public static WhiteListManager getInstance() {
		return self;
	}

	static {
		platFormMailAddress.put(Platform.官方, new String[] { "wtx062@126.com"});
	}

	public void init() {
		
		self = this;
		isStart = true;
		new Thread(this, "白名单管理").start();
		if (TestServerConfigManager.isTestServer()) {
			isStartServer = false;
		}
		 System.out.println("[报名单初始化] [成功] [收集是否启动："+isStartServer+"]");
	}

	@Override
	public void run() {
		while (isStart) {
			Game.logger.warn("[收集玩家信息] [2分钟后即将开启收集]");
			long now = SystemTime.currentTimeMillis();
			if ((now - lastSyncTime) > SYNCH_DATA_TIME) {
				collectWhiteListPlayer();
			}
			try {
				Thread.sleep(SLEEP_TIME);
				doSendMail(false);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	long lastSyncTime = 0L;

	/**
	 * 收集白名单玩家
	 */
	private void collectWhiteListPlayer() {
		// WhitePlayer wp2 = new WhitePlayer("112233", true, "测试");
		// WhiteListManager.getInstance().infos.put("112233",wp2);
		infos.clear();
		QUERY_WHITE_LIST_REQ req = new QUERY_WHITE_LIST_REQ(GameMessageFactory.nextSequnceNum(), new String[0]);
		try {
			QUERY_WHITE_LIST_RES res = (QUERY_WHITE_LIST_RES) MieshiGatewayClientService.getInstance().sendMessageAndWaittingResponse(req, 10 * 1000);
			if (res == null) {
				Game.logger.warn("[收集玩家信息] [出错] [网关返回协议为null]");
				return;
			}

			String[] infoes = res.getInfos();
			if (infoes == null) {
				Game.logger.warn("[收集玩家信息] [出错] [infoes为null]");
				return;
			}

			for (int i = 0; i < infoes.length; i++) {
				String mess = infoes[i];
				// Game.logger.warn("[收集玩家信息] [mess:"+mess+"]");
				String messes[] = mess.split(",");
				WhitePlayer wp = null;

				if (messes.length >= 5) {
					boolean isEffect = false;
					if (messes[1].equals("有效")) {
						isEffect = true;
					}
					wp = new WhitePlayer(messes[0], isEffect, messes[2], messes[4]);
				} else if (messes.length >= 3) {
					boolean isEffect = false;
					if (messes[1].equals("有效")) {
						isEffect = true;
					}
					wp = new WhitePlayer(messes[0], isEffect, messes[2]);
				} else if (messes.length >= 2) {
					boolean isEffect = false;
					if (messes[1].equals("有效")) {
						isEffect = true;
					}
					wp = new WhitePlayer();
					wp.userName = messes[0];
					wp.isEffect = isEffect;
				} else if (messes.length >= 1) {
					wp = new WhitePlayer();
					wp.userName = messes[0];
				}
				if (wp == null) {
					Game.logger.warn("[收集玩家信息] [出错] [mess:" + mess + "] [wp为null]");
					continue;
				}
				infos.put(wp.userName, wp);
				// if (TestServerConfigManager.isTestServer()) {
				// Game.logger.warn("[收集玩家信息] [信息：" + wp + "]");
				// }
			}

			Game.logger.warn("[收集玩家信息] [完成] [生成信息:" + infos.size() + "] [原始信息:" + infoes.length + "] ");// [infoes2:"+Arrays.toString(infoes)+"]");
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			lastSyncTime = SystemTime.currentTimeMillis();
		}

	}

	private void doSendMail(boolean isServerStop) {
		boolean issend = false;
		StringBuilder sbl = new StringBuilder("<H1>白名单玩家操作如下：</H1><HR><table style='font-size:12px;' border=1>");
		int count = 0;
		for (ActionType action : ActionType.values()) {
			List<String> list = cache.get(action.name());
			if (list == null || list.size() == 0) {
				continue;
			}
			count += list.size();
		}

		if (count >= SEND_MAIL_NUM || isServerStop) {
			for (ActionType action : ActionType.values()) {
				List<String> list = cache.get(action.name());
				if (list == null || list.size() == 0) {
					continue;
				}

				sbl.append(MAIL_TITLE_PART2).append(getMailTilte(action));
				for (int i = 0; i < list.size(); i++) {
					String content = list.get(i);
					if (content != null && content.trim().length() > 0) {
						sbl.append(content);
						issend = true;
					}
				}
			}
		}

		if (Game.logger.isWarnEnabled()) {
			Game.logger.warn("[白名单操作] [发送邮件] [测试] [count:" + count + "] [issend:" + issend + "] [cache:" + cache.size() + "] [" + (isServerStop == true ? "服务器关闭" : "正常发送") + "]");
		}

		if (issend) {
			sendMail(MAIL_TITLE_PART1, sbl.toString());
			if (Game.logger.isWarnEnabled()) {
				Game.logger.warn("[白名单操作] [发送邮件] [cache:" + cache.size() + "] [" + (isServerStop == true ? "服务器关闭" : "正常发送") + "]");
			}
			cache.clear();
		}
	}

	private String getMailTilte(ActionType type) {
		StringBuffer sb = new StringBuffer();
		sb.append("<td>");
		sb.append(type.otherPlayer);
		sb.append("</td>");
		sb.append("<td>");
		sb.append("id");
		sb.append("</td>");
		sb.append("<td>");
		sb.append("角色名");
		sb.append("</td>");
		sb.append("<td>");
		sb.append("<font color='red'>是否白名单</font>");
		sb.append("</td>");
		sb.append("<td>");
		sb.append(type.messstr);
		sb.append("</td>");
		sb.append("<td>");
		sb.append(type.colorstr);
		sb.append("</td>");
		sb.append("<td>");
		sb.append(type.countstr);
		sb.append("</td>");
		sb.append("<td>");
		sb.append(type.moneystr);
		sb.append("</td>");
		if (type.name().equals(ActionType.邮件)) {
			sb.append("<td>");
			sb.append(type.mailContent);
			sb.append("</td>");
		}
		sb.append("<td>");
		sb.append("操作时间");
		sb.append("</td>");
		sb.append("</tr>");
		return sb.toString();
	}

	/**
	 * 是否白名单用户
	 * @return
	 */
	public boolean isWhiteListPlayer(Player p) {
		if (isStartServer) {
			return infos.get(p.getUsername()) == null ? false : true;
		} else {
			return false;
		}
	}

	public void addMailRowData(Player whitePlayer, Player otherPlayer, ActionType type, long money, String names[], int colors[], int counts[], String mailContent) {
		StringBuffer sb = new StringBuffer();
		String name = "";
		String color = "";
		String count = "";

		sb.append("<tr>");
		sb.append("<td>");
		sb.append(whitePlayer.getPassport() == null ? "无" : whitePlayer.getPassport().getLastLoginChannel());
		sb.append("</td>");
		WhitePlayer wp = infos.get(whitePlayer.getUsername());
		if (wp != null) {
			sb.append("<td>");
			sb.append(wp.realName);
			sb.append("</td>");
			sb.append("<td>");
			sb.append(wp.gmJob);
			sb.append("</td>");
		} else {
			sb.append("<td>");
			sb.append("--");
			sb.append("</td>");
			sb.append("<td>");
			sb.append("--");
			sb.append("</td>");
		}
		sb.append("<td>");
		sb.append(whitePlayer.getUsername());
		sb.append("</td>");
		sb.append("<td>");
		sb.append(whitePlayer.getId());
		sb.append("</td>");
		sb.append("<td>");
		sb.append(whitePlayer.getName());
		sb.append("</td>");
		sb.append("<td>");
		sb.append(otherPlayer.getPassport() == null ? "无" : whitePlayer.getPassport().getLastLoginChannel());
		sb.append("</td>");
		sb.append("<td>");
		sb.append(otherPlayer.getUsername());
		sb.append("</td>");
		sb.append("<td>");
		sb.append(otherPlayer.getId());
		sb.append("</td>");
		sb.append("<td>");
		sb.append(otherPlayer.getName());
		sb.append("</td>");
		sb.append("<td>");
		sb.append(isWhiteListPlayer(otherPlayer) == true ? "是" : "否");
		sb.append("</td>");
		if (names != null) {
			for (int i = 0; i < names.length; i++) {
				if (names[i] != null && names[i].trim().length() > 0) {
					name += names[i] + ",";
					count += counts[i] + ",";
					color += colors[i] + ",";
				}
			}
		}
		sb.append("<td>");
		sb.append(name);
		sb.append("</td>");
		sb.append("<td>");
		sb.append(color);
		sb.append("</td>");
		sb.append("<td>");
		sb.append(count);
		sb.append("</td>");
		sb.append("<td>");
		sb.append(money > 0 ? "<font color='red'>" + BillingCenter.得到带单位的银两(money) + "</font>" : 0);
		sb.append("</td>");
		if (type.name().equals(ActionType.邮件)) {
			sb.append("<td>");
			sb.append(mailContent);
			sb.append("</td>");
		}
		sb.append("<td>");
		sb.append(TimeTool.formatter.varChar23.format(System.currentTimeMillis()));
		sb.append("</td>");
		sb.append("</tr>");
		ArrayList<String> list = cache.get(type.name());
		if (list == null) {
			list = new ArrayList<String>();
		}
		list.add(sb.toString());
		cache.put(type.name(), list);

		if (Game.logger.isWarnEnabled()) {
			Game.logger.warn("[白名单操作] [添加新数据] [" + type.name() + "] [数量：" + list.size() + "] [操作玩家：" + (whitePlayer == null ? "null" : whitePlayer.getName()) + "] [另一方：" + (otherPlayer == null ? "null" : otherPlayer.getName()) + "] [类型：" + type + "] [物品：" + name + "] [颜色：" + color + "] [数量:" + count + "]");
		}
	}

	public void sendMail(Player whitePlayer, Player otherPlayer, ActionType type, long money, String names[], int colors[], int counts[]) {
		String title = "[飘渺寻仙曲] [" + GameConstants.getInstance().getServerName() + "] [" + PlatformManager.getInstance().getPlatform().toString() + "] [白名单操作]";
		StringBuffer sb = new StringBuffer();
		String name = "";
		String color = "";
		String count = "";
		sb.append("<H1>白名单玩家操作如下：</H1>");
		sb.append("<HR>");
		sb.append("<table style='font-size:12px;' border=1>");
		sb.append("<tr bgcolor='#56ffa7'>");
		sb.append("<td>");
		sb.append("登录渠道");
		sb.append("</td>");
		sb.append("<td>");
		sb.append("公司人员");
		sb.append("</td>");
		sb.append("<td>");
		sb.append("公司职称");
		sb.append("</td>");
		sb.append("<td>");
		sb.append("白名单玩家账号");
		sb.append("</td>");
		sb.append("<td>");
		sb.append("id");
		sb.append("</td>");
		sb.append("<td>");
		sb.append("角色名");
		sb.append("</td>");
		sb.append("<td>");
		sb.append("登录渠道");
		sb.append("</td>");
		sb.append("<td>");
		sb.append(type.otherPlayer);
		sb.append("</td>");
		sb.append("<td>");
		sb.append("id");
		sb.append("</td>");
		sb.append("<td>");
		sb.append("角色名");
		sb.append("</td>");
		sb.append("<td>");
		sb.append(type.messstr);
		sb.append("</td>");
		sb.append("<td>");
		sb.append(type.colorstr);
		sb.append("</td>");
		sb.append("<td>");
		sb.append(type.countstr);
		sb.append("</td>");
		sb.append("<td>");
		sb.append(type.moneystr);
		sb.append("</td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td>");
		sb.append(whitePlayer.getPassport() == null ? "无" : whitePlayer.getPassport().getLastLoginChannel());
		sb.append("</td>");

		sb.append("<td>");
		sb.append(whitePlayer.getUsername());
		sb.append("</td>");
		sb.append("<td>");
		sb.append(whitePlayer.getId());
		sb.append("</td>");
		sb.append("<td>");
		sb.append(whitePlayer.getName());
		sb.append("</td>");
		sb.append("<td>");
		sb.append(otherPlayer.getPassport() == null ? "无" : whitePlayer.getPassport().getLastLoginChannel());
		sb.append("</td>");
		sb.append("<td>");
		sb.append(otherPlayer.getUsername());
		sb.append("</td>");
		sb.append("<td>");
		sb.append(otherPlayer.getId());
		sb.append("</td>");
		sb.append("<td>");
		sb.append(otherPlayer.getName());
		sb.append("</td>");
		if (name != null) {
			for (int i = 0; i < names.length; i++) {
				if (names[i] != null && names[i].trim().length() > 0) {
					name += names[i] + ",";
					count += counts[i] + ",";
					color += colors[i] + ",";
				}
			}
		}
		sb.append("<td>");
		sb.append(name);
		sb.append("</td>");
		sb.append("<td>");
		sb.append(color);
		sb.append("</td>");
		sb.append("<td>");
		sb.append(count);
		sb.append("</td>");
		sb.append("<td>");
		sb.append(money);
		sb.append("</td>");
		sb.append("</tr>");

		sb.append("</table>");
		sb.append("<HR>");
		sendMail(title, sb.toString());
		if (Game.logger.isWarnEnabled()) {
			Game.logger.warn("[白名单操作] [操作玩家：" + (whitePlayer == null ? "null" : whitePlayer.getName()) + "] [另一方：" + (otherPlayer == null ? "null" : otherPlayer.getName()) + "] [类型：" + type + "] [物品：" + name + "] [颜色：" + color + "] [数量:" + count + "]");
		}
	}

	public enum ActionType {
		当面交易("交易的玩家账号", "交易的物品", "数量", "颜色", "交易的银两"),
		邮件("收件账号", "邮件物品", "数量", "颜色", "金币", "邮件内容"),
		拍卖("购买玩家账号", "拍卖物品", "数量", "颜色", "拍卖消费银两"),
		竞拍("拍卖的玩家账号", "拍卖物品", "数量", "颜色", "竞拍消费银两"),
		求购("卖家账号", "求购物品", "数量", "颜色", "花费银两"),
		摆摊出售("购买玩家账号", "出售物品", "数量", "颜色", "获得银两"),
		死亡掉落("击杀玩家的人账号", "掉落物品", "数量", "颜色", "银两");
		String otherPlayer;
		String messstr;
		String countstr;
		String colorstr;
		String moneystr;
		String mailContent;

		ActionType(String otherPlayer, String mess, String count, String color, String moneystr, String mailContent) {
			this.otherPlayer = otherPlayer;
			this.messstr = mess;
			this.countstr = count;
			this.colorstr = color;
			this.moneystr = moneystr;
			this.mailContent = mailContent;
		}

		ActionType(String otherPlayer, String mess, String count, String color, String moneystr) {
			this.otherPlayer = otherPlayer;
			this.messstr = mess;
			this.countstr = count;
			this.colorstr = color;
			this.moneystr = moneystr;
		}
	}

	private class WhitePlayer {
		public boolean isEffect;
		public String userName, realName, gmJob;

		public WhitePlayer() {
		}

		public WhitePlayer(String userName, boolean isEffect, String realName) {
			this.userName = userName;
			this.isEffect = isEffect;
			this.realName = realName;
		}

		public WhitePlayer(String userName, boolean isEffect, String realName, String gmJob) {
			this.userName = userName;
			this.isEffect = isEffect;
			this.realName = realName;
			this.gmJob = gmJob;
		}

		@Override
		public String toString() {
			StringBuilder sbu = new StringBuilder("[");
			sbu.append("userName:").append(userName).append("] [").append("realName:").append(realName).append("] [").append("isEffect:").append(isEffect).append("]").append("] [").append(gmJob).append("]");
			return sbu.toString();
		}
	}

	private final void sendMail2(String title, String content) {
		StringBuffer sb = new StringBuffer();
		sb.append(content);
		sb.append("<hr>" + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		ArrayList<String> args = new ArrayList<String>();
		args.add("-username");
		args.add("sqage_wx_restart");
		args.add("-password");
		args.add("2wsxcde3");

		args.add("-smtp");
		args.add("smtp.163.com");
		args.add("-from");
		args.add("wtx062@126.com");
		args.add("-to");
		String address_to = "";

		String[] addresses = platFormMailAddress.get(PlatformManager.getInstance().getPlatform());
		if (addresses != null) {
			for (String address : addresses) {
				address_to += address + ",";
			}
		}

		if (!"".equals(address_to)) {
			args.add(address_to);
			args.add("-subject");
			args.add(title);
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


	public void destroy() {
		try {
			doSendMail(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, WhitePlayer> getInfos() {
		return this.infos;
	}

	public void setInfos(Map<String, WhitePlayer> infos) {
		this.infos = infos;
	}

	public long getSLEEP_TIME() {
		return this.SLEEP_TIME;
	}

	public void setSLEEP_TIME(long sLEEP_TIME) {
		this.SLEEP_TIME = sLEEP_TIME;
	}

}
