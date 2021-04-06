package com.fy.engineserver.enterlimit;

import java.util.HashMap;

import org.slf4j.Logger;

import com.fy.engineserver.message.BING_PHONENUM_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.OPEN_PHONENUM_SHOW_RES;
import com.fy.engineserver.message.PHONENUM_ASK_REQ;
import com.fy.engineserver.message.PHONENUM_ASK_RES;
import com.fy.engineserver.message.TRY_BING_PHONENUM_RES;
import com.fy.engineserver.message.TRY_SEND_PHONTNUM_RES;
import com.fy.engineserver.message.TRY_SEND_UNBIND_RES;
import com.fy.engineserver.message.TRY_UNBING_PHONENUM_RES;
import com.fy.engineserver.message.UNBIND_PHONENUM_RES;
import com.fy.engineserver.sprite.Player;

public class PhoneNumMananger {

	private static PhoneNumMananger instance = new PhoneNumMananger();
	
	public static Logger logger = null;

	public boolean isOpen = true;
	
	public static int SEND_JIAOYAN_SPACE = 6 * 1000;
	
	public static HashMap<Long, Long> playerSendJiaoYanTimes = new HashMap<Long, Long>();
	
	//暂时用的
	public static HashMap<Long, String> playerBindPhoneNum = new HashMap<Long, String>();
	
	public void init () {
		logger = EnterLimitManager.logger;
	}
	
	//客户端开关
	public void sendOpenMsg (Player player) {
		OPEN_PHONENUM_SHOW_RES res = new OPEN_PHONENUM_SHOW_RES(GameMessageFactory.nextSequnceNum(), isOpen, getPlayerIsBind(player));
		player.addMessageToRightBag(res);
	}
	
	//取 玩家是不是绑定了手机号
	public boolean getPlayerIsBind (Player player) {
		// TODO: 这个是假的
		String phoneNum = getPlayerBindNum(player);
		if (phoneNum == null) {
			return false;
		}
		return true;
	}
	
	public String getPlayerBindNum (Player player) {
		// TODO: 这个是假的
		String phoneNum = playerBindPhoneNum.get(player.getId());
		return phoneNum;
	}
	
	//尝试绑定手机号，这个协议到客户端客户端会打开绑定手机界面
	public void handle_TRY_BING_PHONENUM_REQ (Player player) {
		if (!isOpen) {
			return;
		}
		long jiaoyanTime = 0L;
		long now = System.currentTimeMillis();
		Long oldSendTime = playerSendJiaoYanTimes.get(player.getId());
		if (oldSendTime != null) {
			if (now - oldSendTime < SEND_JIAOYAN_SPACE) {
				jiaoyanTime = SEND_JIAOYAN_SPACE - (now - oldSendTime);
			}
		}
		TRY_BING_PHONENUM_RES res = new TRY_BING_PHONENUM_RES(GameMessageFactory.nextSequnceNum(), "仅需填写一个手机号，免费通过手机认证，通过审核后就可获得【手机认证大礼包】", jiaoyanTime, SEND_JIAOYAN_SPACE, "校验码已发出，请注意查收短信，如果没有收到，您可在60秒后点击再次获取，当前您还可以获取3次。");
		player.addMessageToRightBag(res);
	}
	
	public void handle_TRY_SEND_PHONTNUM_REQ (Player player, String phoneNum) {
		if (!isOpen) {
			return;
		}
		long now = System.currentTimeMillis();
		Long oldSendTime = playerSendJiaoYanTimes.get(player.getId());
		if (oldSendTime != null) {
			if (now - oldSendTime < SEND_JIAOYAN_SPACE) {
				player.sendError("不能频繁获取校验码，" + (SEND_JIAOYAN_SPACE/1000) + "秒内只能获取一次。");
				return;
			}
		}
		
		// TODO::去发短信
		
		playerSendJiaoYanTimes.put(player.getId(), now);
		TRY_SEND_PHONTNUM_RES res = new TRY_SEND_PHONTNUM_RES(GameMessageFactory.nextSequnceNum(), true, "");
		player.addMessageToRightBag(res);
		logger.warn("[绑定短信] [{}] [{}]", new Object[]{player.getLogString(), phoneNum});
	}
	
	public void handle_BING_PHONENUM_REQ (Player player, String phoneNum, String jiaoYanMa) {
		if (!isOpen) {
			return;
		}
		
		//TODO:检查校验码,绑定手机
		
		playerBindPhoneNum.put(player.getId(), phoneNum);
		BING_PHONENUM_RES res = new BING_PHONENUM_RES(GameMessageFactory.nextSequnceNum(), true, "");
		player.addMessageToRightBag(res);
		logger.warn("[绑定手机成功] [{}] [{}] [{}]", new Object[]{player.getLogString(), phoneNum, jiaoYanMa});
	}
	
	public void handle_TRY_UNBING_PHONENUM_REQ (Player player) {
		if (!isOpen) {
			return;
		}
		
		if (!getPlayerIsBind(player)) {
			player.sendError("您还未绑定手机号码。");
			return;
		}
		
		TRY_UNBING_PHONENUM_RES res = new TRY_UNBING_PHONENUM_RES(GameMessageFactory.nextSequnceNum(), "已经绑定", getPlayerBindNum(player), SEND_JIAOYAN_SPACE, "校验码已发出，请注意查收短信，如果没有收到，您可在60秒后点击再次获取，当前您还可以获取3次。");
		player.addMessageToRightBag(res);
	}
	
	public void handle_TRY_SEND_UNBIND_REQ (Player player) {
		if (!isOpen) {
			return;
		}
		
		long now = System.currentTimeMillis();
		Long oldSendTime = playerSendJiaoYanTimes.get(player.getId());
		if (oldSendTime != null) {
			if (now - oldSendTime < SEND_JIAOYAN_SPACE) {
				player.sendError("不能频繁获取校验码，" + (SEND_JIAOYAN_SPACE/1000) + "秒内只能获取一次。");
				return;
			}
		}
		
		// TODO::去发短信
		String phoneNum = getPlayerBindNum(player);
		
		playerSendJiaoYanTimes.put(player.getId(), now);
		TRY_SEND_UNBIND_RES res = new TRY_SEND_UNBIND_RES(GameMessageFactory.nextSequnceNum(), true, "");
		player.addMessageToRightBag(res);
		logger.warn("[解绑短信] [{}] [{}]", new Object[]{player.getLogString(), phoneNum});
	}
	
	public void handle_UNBIND_PHONENUM_REQ (Player player, String jiaoYanMa) {
		if (!isOpen) {
			return;
		}
		
		//TODO:检查校验码,解绑定
		
		playerBindPhoneNum.remove(player.getId());
		UNBIND_PHONENUM_RES res = new UNBIND_PHONENUM_RES(GameMessageFactory.nextSequnceNum(), true, "");
		player.addMessageToRightBag(res);
		logger.warn("[解绑手机成功] [{}] [{}]", new Object[]{player.getLogString(), jiaoYanMa});
	}
	
	public void send_PHONENUM_ASK_RES (Player player) {
		if (!isOpen) {
			return;
		}
		
		//TODO:假的
		long jiaoyanTime = 0L;
		long now = System.currentTimeMillis();
		Long oldSendTime = playerSendJiaoYanTimes.get(player.getId());
		if (oldSendTime != null) {
			if (now - oldSendTime < SEND_JIAOYAN_SPACE) {
				jiaoyanTime = SEND_JIAOYAN_SPACE - (now - oldSendTime);
			}
		}
		
		String phoneNum = getPlayerBindNum(player);
		if (phoneNum == null) {
			phoneNum = "";
		}
		
		PHONENUM_ASK_RES req = new PHONENUM_ASK_RES(GameMessageFactory.nextSequnceNum(), "请输入手机号，获取验证码，然后输入验证码后才能进行接下来的操作。", phoneNum, jiaoyanTime, SEND_JIAOYAN_SPACE, "校验码已发出，请注意查收短信，如果没有收到，您可在60秒后点击再次获取，当前您还可以获取3次。");
		player.addMessageToRightBag(req);
	}
	
	public void handle_PHONENUM_ASK_REQ (Player player, String phoneNum, String jiaoYanMa) {
		if (!isOpen) {
			return;
		}
		
		logger.warn("[手机答题] [{}] [{}] [{}]", new Object[]{player.getLogString(), phoneNum, jiaoYanMa});
	}
	
	public static void setInstance(PhoneNumMananger instance) {
		PhoneNumMananger.instance = instance;
	}

	public static PhoneNumMananger getInstance() {
		return instance;
	}
	
	
}
