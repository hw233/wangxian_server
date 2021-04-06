package com.fy.engineserver.validate;

import java.util.*;

import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.transport.Connection;

import java.util.concurrent.*;

public class DefaultValidateManager implements ValidateManager {

	private static DefaultValidateManager instance;

	public static DefaultValidateManager getInstance() {
		if (instance == null) {
			instance = new DefaultValidateManager();
		}
		return instance;
	}

	// ip对应的数据，记录都是当天的数据
	public static class IpData {

		public String ip;

		public ArrayList<String> loginUserList = new ArrayList<String>();
		public int takeTaskCount = 0;
		public String network = "";
		public long lastModifyTime = 0;
		public int requestBuySaleCount = 0;

		public boolean update() {
			long now = System.currentTimeMillis();

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(lastModifyTime);
			int y1 = cal.get(Calendar.YEAR);
			int d1 = cal.get(Calendar.DAY_OF_YEAR);

			cal.setTimeInMillis(now);
			int y2 = cal.get(Calendar.YEAR);
			int d2 = cal.get(Calendar.DAY_OF_YEAR);

			if (y1 != y2 || d1 != d2) {
				clear();
			}

			if (d1 + 2 < d2) return true;
			return false;
		}

		public void clear() {
			loginUserList.clear();
			takeTaskCount = 0;
			requestBuySaleCount = 0;
		}
	}

	public ConcurrentHashMap<String, IpData> ipDataMap = new ConcurrentHashMap<String, IpData>();
	public ConcurrentHashMap<String, UserData> userDataMap = new ConcurrentHashMap<String, UserData>();

	public long lastUpdateTime = 0;

	public static int maxWrongTimes = 5;
	public static int maxRightTimes = 2;
	public static int maxTakeTaskNumForOneIp = 5;
	public static int maxRequestBuySaleNumForOneIp = 30;			//快速出售10次才答题
	public static int maxLoginUserNumForOneIp = 1;
	public static int chargeMoneyLess = 30000;

	/**
	 * 更新数据，按天刷新
	 */
	protected void update(boolean nowait) {
		long now = System.currentTimeMillis();

		if (!nowait && now - lastUpdateTime < 5000) return;
		lastUpdateTime = now;
		try {
			Iterator<String> it = ipDataMap.keySet().iterator();
			while (it.hasNext()) {
				IpData id = ipDataMap.get(it.next());
				if (id.update()) {
					it.remove();
				}
			}
			it = userDataMap.keySet().iterator();
			while (it.hasNext()) {
				UserData id = userDataMap.get(it.next());
				if (id.update()) {
					it.remove();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized IpData getIpData(String ip) {
		IpData id = ipDataMap.get(ip);
		if (id == null) {
			id = new IpData();
			id.ip = ip;
			id.lastModifyTime = System.currentTimeMillis();
			ipDataMap.put(ip, id);
		}
		return id;
	}

	public synchronized UserData getUserData(Player p) {
		UserData id = userDataMap.get(p.getUsername());
		if (id == null) {
			id = new UserData();
			id.userName = p.getUsername();
			id.playerId = p.getId();
			id.playerName = p.getName();

			id.lastModifyTime = System.currentTimeMillis();
			userDataMap.put(p.getUsername(), id);
		}
		return id;
	}

	/**
	 * 获取验证状态,有三种:
	 * 
	 * 通过,直接可以接取任务
	 * 拒绝,不能接取任务
	 * 需要答题,需要答题正确后才能接取任务
	 * 
	 * 玩家不在线直接拒绝.
	 * 玩家所在IP如果完成了一定的次数后,需要答题.
	 * 玩家多次答题错误,也拒绝.
	 * 
	 * @param p
	 * @return
	 */
	public int getValidateState(Player p, int askType) {

		if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
			return ValidateManager.VALIDATE_STATE_通过;
		}
		
		update(false);

		if (p.isOnline() == false) return ValidateManager.VALIDATE_STATE_拒绝;
		if (p.getRMB() >= chargeMoneyLess) return ValidateManager.VALIDATE_STATE_通过;

		
		UserData ud = getUserData(p);

		if (ud.answerWrongTimes >= maxWrongTimes) {
			return ValidateManager.VALIDATE_STATE_拒绝;
		}
		
		if(ud.directPassCount > 0){
			ud.directPassCount--;
			return ValidateManager.VALIDATE_STATE_通过;
		}
		
		if(ud.answerRightTimes >= maxRightTimes && ud.answerWrongTimes == 0){
			return ValidateManager.VALIDATE_STATE_通过;
		}

		if (ud.currentAsk != null) {
			return ValidateManager.VALIDATE_STATE_需要答题;
		}

		Connection conn = p.getConn();
		if (conn == null) return ValidateManager.VALIDATE_STATE_拒绝;

		String remoteAddress = conn.getRemoteAddress();
		if (remoteAddress.indexOf(":") > 0) {
			remoteAddress = remoteAddress.substring(0, remoteAddress.indexOf(":"));
		}

		IpData id = getIpData(remoteAddress);
		if (askType == 0) {
			if (id.loginUserList.size() >= maxLoginUserNumForOneIp) {
				ud.reason = "今天同一个IP上有太多用户登陆";
				ValidateAskManager vam = ValidateAskManager.getInstance();
				ud.currentAsk = vam.getRandomAsk();
				return ValidateManager.VALIDATE_STATE_需要答题;
			}
			
			if (id.takeTaskCount >= maxTakeTaskNumForOneIp) {
				ud.reason = "今天同一个IP上有太多接取了体力任务";
				ValidateAskManager vam = ValidateAskManager.getInstance();
				ud.currentAsk = vam.getRandomAsk();
				return ValidateManager.VALIDATE_STATE_需要答题;
			}
		}else if (askType == 1) {
			if (id.requestBuySaleCount >= maxRequestBuySaleNumForOneIp) {
				ud.reason = "今天同一个IP上有太多快速出售";
				ValidateAskManager vam = ValidateAskManager.getInstance();
				ud.currentAsk = vam.getRandomAsk();
				return ValidateManager.VALIDATE_STATE_需要答题;
			}
		}
		return ValidateManager.VALIDATE_STATE_通过;
	}

	/**
	 * 获取验证状态,有三种:
	 * 
	 * 通过,直接可以接取任务
	 * 拒绝,不能接取任务
	 * 需要答题,需要答题正确后才能接取任务
	 * 
	 * 玩家不在线直接拒绝.
	 * 玩家所在IP如果完成了一定的次数后,需要答题.
	 * 玩家多次答题错误,也拒绝.
	 * 
	 * 返回各种状态的原因.
	 * @param p
	 * @return
	 */
	public String getValidateStateReason(Player p, int askType) {
		if (p.isOnline() == false) return "玩家不在线被拒绝";
		if (p.getRMB() >= chargeMoneyLess) return "玩家是高级VIP直接通过";

		UserData ud = getUserData(p);

		if (ud.answerWrongTimes >= maxWrongTimes) {
			return "多次答题错误";
		}

		
		if (ud.currentAsk != null) {
			return ud.reason;
		}

		Connection conn = p.getConn();
		if (conn == null) return "玩家不在线";

		String remoteAddress = conn.getRemoteAddress();
		if (remoteAddress.indexOf(":") > 0) {
			remoteAddress = remoteAddress.substring(0, remoteAddress.indexOf(":"));
		}

		IpData id = getIpData(remoteAddress);
		if (askType == 0) {
			if (id.loginUserList.size() >= maxLoginUserNumForOneIp) {
				return "今天同一个IP上有太多用户登陆";
			}
			
			if (id.takeTaskCount >= maxTakeTaskNumForOneIp) {
				return "今天同一个IP上有太多接取了体力任务";
			}
		}else if (askType == 1) {
			if (id.requestBuySaleCount >= maxRequestBuySaleNumForOneIp) {
				return "今天同一个IP上有太多快速出售。";
			}
		}


		return "通过";
	}

	/**
	 * 此方法必须确保玩家需要答题的情况下调用.
	 * 否则返回null
	 * @param p
	 * @return
	 */
	public ValidateAsk getNextValidteAsk(Player p, int askType) {
		UserData ud = getUserData(p);
		if (ud.currentAsk != null) return ud.currentAsk;
		int s = getValidateState(p, askType);
		if (s == ValidateManager.VALIDATE_STATE_需要答题) {
			ud._needAnswerTimes ++;
			return ud.currentAsk;
		}
		return null;
	}

	/**
	 * 通知此玩家接取了一个体力任务
	 */
	public void notifyTakeOneTask(Player p) {

		update(false);

		if (p.isOnline() == false) return;
		Connection conn = p.getConn();
		if (conn == null) return;
		String remoteAddress = conn.getRemoteAddress();
		if (remoteAddress.indexOf(":") > 0) {
			remoteAddress = remoteAddress.substring(0, remoteAddress.indexOf(":"));
		}

		IpData id = getIpData(remoteAddress);
		id.update();

		UserData ud = getUserData(p);
		ud.update();

		if (id.loginUserList.contains(p.getUsername()) == false) {
			id.loginUserList.add(p.getUsername());
		}
		id.takeTaskCount++;
		id.lastModifyTime = System.currentTimeMillis();

		ud.takeTaskCount++;
		ud.lastModifyTime = System.currentTimeMillis();

	}
	
	/**
	 * 通知此玩家快速出售了一次
	 * */
	public void notifyRequestBuySale(Player p){
		update(false);

		if (p.isOnline() == false) return;
		Connection conn = p.getConn();
		if (conn == null) return;
		String remoteAddress = conn.getRemoteAddress();
		if (remoteAddress.indexOf(":") > 0) {
			remoteAddress = remoteAddress.substring(0, remoteAddress.indexOf(":"));
		}

		IpData id = getIpData(remoteAddress);
		id.update();

		UserData ud = getUserData(p);
		ud.update();
		
		if (id.loginUserList.contains(p.getUsername()) == false) {
			id.loginUserList.add(p.getUsername());
		}
		
		id.requestBuySaleCount++;
		id.lastModifyTime = System.currentTimeMillis();
		
		ud.requestBuySaleCount++;
		ud.lastModifyTime = System.currentTimeMillis();
	}

	/**
	 * 通知此玩家答题了,对与错由ask判断
	 * 并返回答对了还是错了.
	 * true表示答对了
	 */
	public boolean notifyAnswerAsk(Player p, ValidateAsk ask, String inputStr, int asktype) {
		if (p.isOnline() == false) return false;
		Connection conn = p.getConn();
		if (conn == null) return false;
		String remoteAddress = conn.getRemoteAddress();
		if (remoteAddress.indexOf(":") > 0) {
			remoteAddress = remoteAddress.substring(0, remoteAddress.indexOf(":"));
		}

		IpData id = getIpData(remoteAddress);
		id.update();

		UserData ud = getUserData(p);
		ud.update();

		if (id.loginUserList.contains(p.getUsername()) == false) {
			id.loginUserList.add(p.getUsername());
			id.lastModifyTime = System.currentTimeMillis();
		}
		if (ud.currentAsk == null) return false;
		if (ud.currentAsk != ask) return false;

		if (ask.isRight(inputStr.trim())) {
			if (asktype == 0) {
//				ud.answerRightTimes++;
				ud._answerRightTimes++;
				ud.currentAsk = null;
				ud.lastModifyTime = System.currentTimeMillis();
				ud.directPassCount+=2;
			}else if (asktype == 1) {
				ud._answerRightTimes++;
				ud.currentAsk = null;
				ud.lastModifyTime = System.currentTimeMillis();
			}
			return true;
		} else {
			ud.answerWrongTimes++;
			ud._answerWrongTimes++;
			if (ud.answerWrongTimes < maxWrongTimes) {
				ValidateAskManager vam = ValidateAskManager.getInstance();
				ud.currentAsk = vam.getRandomAsk();
			} else {
				ud.currentAsk = null;
			}
			// TODO
			ud.lastModifyTime = System.currentTimeMillis();
			return false;
		}
	}
}