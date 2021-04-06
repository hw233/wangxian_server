package com.fy.engineserver.vip.vipinfo;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.client.BossClientService;
import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NEW_OPTION_SELECT_REQ;
import com.fy.engineserver.message.NEW_QUERY_WINDOW_REQ;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.vip.Option_VipInfo_Cancel;
import com.fy.engineserver.vip.Option_VipInfo_OK;
import com.fy.engineserver.vip.VipManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.transport.Connection;

public class VipInfoRecordManager {

	public static boolean isRun = true;
	public static boolean isRunning = false;
	public static long TimeOutValue = 1000l * 50;
	public static Map<String, VipInfo> menuWindowCache = new ConcurrentHashMap<String, VipInfo>();
	public static Map<Long, String> tempCache = new Hashtable<Long, String>(); // 客户端bug容错处理
	public static VipInfoRecordManager self = new VipInfoRecordManager();

	public static boolean isOpenRecord = false;
	
	public static VipInfoRecordManager getInstance() {
		if (self == null) {
			self = new VipInfoRecordManager();
		}
		return self;
	}

	// 判断是否满足收集vip信息条件
	public boolean isGather(Player player) {
		if(GameConstants.getInstance().getServerName().equals("客户端测试") || GameConstants.getInstance().getServerName().equals("飘渺寻仙") ){
			if (isOpenRecord && player.getVipLevel() >= (byte) VipManager.recordVipLevel2) {
				return true;
			}
		}
		if (isOpenRecord && player.getVipLevel() >= (byte) VipManager.recordVipLevel) {
			return true;
		}
		return false;
	}

	public void gatherVipInfo(Player player) {
		long startTime = System.currentTimeMillis();
		try {
			if (isGather(player)) {
				if (VipManager.logger.isWarnEnabled()) {
					VipManager.logger.warn("[VIP] [信息采集] [" + player.getName() + "]");
				}
				// 调用bossClientService的判断是否弹出窗口的方法
				// BossClientService bossClientService = BossClientService.getInstance();
				// // 获取player上的信息 用以判断是否弹出窗口给vip用户
				String[] infos = new String[7];
				infos[0] = player.getVipLevel() + "";
				infos[1] = player.getUsername();
				infos[2] = PlatformManager.getInstance().getPlatform().getPlatformName();
				infos[3] = player.getName();
				infos[4] = player.getLevel() + "";
				infos[5] = CountryManager.getInstance().getCountryByCountryId(player.getCountry()).getName();
				infos[6] = GameConstants.getInstance().getServerName();
				//
				// int canPopWindow = bossClientService.isPopWindow(infos);
				//
				// if (VipManager.logger.isWarnEnabled()) {
				// VipManager.logger.warn("[VIP] [信息采集] [" + Arrays.toString(infos) + "] [" + canPopWindow + "]");
				// }
				// if (canPopWindow == BossServerService.IS_POP_WINDOW_FOR_VIP) {
				if (player.getVipAgent().hasAuthority()) {
					MenuWindow mw = VipInfoRecordManager.createMenuWindow((int) VipInfoRecordManager.TimeOutValue);
					// 创建一个确认按钮和取消按钮
					Option_VipInfo_OK optionOk = new Option_VipInfo_OK();
					optionOk.setOType(Option.OPTION_TYPE_SERVER_FUNCTION);
					Option_VipInfo_Cancel optionCancel = new Option_VipInfo_Cancel();
					optionCancel.setOType(Option.OPTION_TYPE_SERVER_FUNCTION);
					optionOk.setText(com.fy.engineserver.datasource.language.Translate.确定);
					optionCancel.setText(com.fy.engineserver.datasource.language.Translate.取消);

					mw.setOptions(new Option[] { optionOk, optionCancel });
					VipInfo vipInfo = new VipInfo();
					vipInfo.menuWindow = mw;
					vipInfo.infos = infos;
					menuWindowCache.put(mw.getWinId() + "", vipInfo);
					// 创建标题
					String title = "录入信息";
					// 创建desc
					String desc = "尊敬的仙友您好，您的账号已经成功晋升高等级VIP，您将享受到最好的福利特权。如实填写以下信息有精美礼包领取。详情请加QQ群：816357986 联系群内管理！！";
					String[] inputTitles = new String[] { "您的姓名:", "QQ号码:", "手机号码:" };
					byte[] inputTypes = new byte[] { 1, 1, 1 };
					byte[] maxLength = new byte[] { 20, 20, 20 };
					String[] defaultContent = new String[] { "" };
					byte[] png = new byte[] { 0 };

					NEW_QUERY_WINDOW_REQ req = new NEW_QUERY_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getWinId(), title, desc, inputTitles, inputTypes, maxLength, defaultContent, png, mw.getOptions());
					// 发给客户端弹出窗口信息
					player.addMessageToRightBag(req, "");

				}

			}
		} catch (Exception e) {
			VipManager.logger.error("[弹出窗口获取vip信息] [错误] [出现异常] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
		}
	}

	public static MenuWindow createMenuWindow(int time) {
		MenuWindow mw = new MenuWindow();
		mw.setWinId(Integer.parseInt((System.currentTimeMillis() + "").substring(4)));
		mw.setDestoryTime(System.currentTimeMillis() + time);
		return mw;
	}

	private void checkWindowCache() {
		for (String key : menuWindowCache.keySet()) {
			VipInfo vipInfo = menuWindowCache.get(key);
			if (vipInfo != null) {
				String[] infos = vipInfo.infos;
				MenuWindow menuWindow = vipInfo.menuWindow;
				if (menuWindow.getDestoryTime() <= System.currentTimeMillis()) {
					menuWindowCache.remove(key);
					if (VipManager.logger.isInfoEnabled()) {
						VipManager.logger.info("[弹出窗口获取vip信息] [清理过期窗口缓存] [成功] [menuWindowId:" + menuWindow.getWinId() + "] [viplevel:" + (infos[0]) + "] [username:" + (infos[1]) + "] [name:" + infos[3] + "] [level:" + infos[4] + "] [country:" + infos[5] + "] [servername:" + infos[6] + "]");
					}
				}
			}
		}
	}

	private int validateInput(String[] infos) {
		int i = 0;
		for (String str : infos) {
			if (str.trim().length() == 0) {
				return i;
			}
			i++;
		}

		return 100;
	}

	private void sendHint(String message, Player player, String buttonName) {
		if (player == null) {
			VipManager.logger.error("[发送提示信息] [失败] [角色对象为空] [" + message.getClass().getName() + "]");
			return;
		}

		MenuWindow mw = createMenuWindow((int) VipInfoRecordManager.TimeOutValue);
		// 创建一个确认按钮和取消按钮
		Option optionOk = new Option();
		optionOk.setOType(Option.OPTION_TYPE_SERVER_FUNCTION);

		if (buttonName == null || buttonName.trim().length() == 0) {
			buttonName = "返回";
		}

		optionOk.setText(buttonName);

		mw.setOptions(new Option[] { optionOk });
		String title = "";
		// 创建desc
		String desc = message;
		String[] inputTitles = new String[] { "" };
		byte[] inputTypes = new byte[0];
		byte[] maxLength = new byte[0];
		String[] defaultContent = new String[] { "" };
		byte[] png = new byte[] { 0 };

		NEW_QUERY_WINDOW_REQ req = new NEW_QUERY_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getWinId(), title, desc, inputTitles, inputTypes, maxLength, defaultContent, png, mw.getOptions());
		// 发给客户端弹出窗口信息
		player.addMessageToRightBag(req, "");

		return;
	}

	//
	public void handle_NEW_OPTION_SELECT_REQ(NEW_OPTION_SELECT_REQ req, Player player, Connection conn) {
		long startTime = System.currentTimeMillis();
		try {
			String ques = tempCache.get(player.getId());
			if (HorseManager.logger.isDebugEnabled()) {
				HorseManager.logger.debug("[设置密保] [收到协议] [NEW_OPTION_SELECT_REQ] [" + player.getLogString() + "] [ques:" + ques + "]");
			}
			if (ques != null) {
				tempCache.remove(player.getId());
				Passport passport = (Passport) conn.getAttachment();
				String secretAnswer = player.getPassport().getSecretAnswer();
				if (secretAnswer != null && !"".equals(secretAnswer)) {
					player.sendError(Translate.您已经设置过密保不能再设置);
					return;
				}
				String newAnswer = req.getInputText()[0];
				if (newAnswer.getBytes().length > 60) {
					CoreSubSystem.logger.error(player.getLogString() + "[修改密保] [失败] [输入的新答案过长:" + newAnswer.getBytes().length + "] [输入的问题:" + ques + "] [输入的答案:" + newAnswer + "] ");
					player.sendError(Translate.输入的新密码过长请重新输入);
					return;
				}
				if (newAnswer.isEmpty()) {
					CoreSubSystem.logger.error(player.getLogString() + "[修改密保] [失败] [未输入的答案:" + newAnswer.getBytes().length + "] [输入的问题:" + ques + "] [输入的答案:" + newAnswer + "] ");
					player.sendError(Translate.设置失败);
					return;
				}
				// 设置新的数据
				passport.setSecretAnswer(newAnswer);
				passport.setSecretQuestion(ques);
				passport.setLastQuestionSetDate(new Date());

				boolean succ = CoreSubSystem.getInstance().update(passport);
				if (succ) {
					CoreSubSystem.logger.error(player.getLogString() + "[修改密保] [成功] [输入的问题:" + ques + "] [输入的答案:" + newAnswer + "]");
					player.sendError(Translate.设置成功);
				} else {
					CoreSubSystem.logger.error(player.getLogString() + "[修改密保] [失败] [update失败] [输入的问题:" + ques + "] [输入的答案:" + newAnswer + "] ");
					player.sendError(Translate.设置成功);
				}
			}
		} catch (Exception e) {
			CoreSubSystem.logger.error("[设置密保] [错误] [出现异常] [" + player.getLogString() + "]", e);
		}
		try {

			VipInfo vipInfo = menuWindowCache.get(req.getWId() + "");

			// 根据请求携带的winid查找到对应的窗口 同时检查过期的窗口并清除
			if (vipInfo != null) {
				if (req.getIndex() == 0) // 按下确认按钮
				{
					String[] infos = vipInfo.infos;
					// validate输入
					int isValid = validateInput(req.getInputText());
					// 发送提示信息
					if (isValid != 100) {
						String message = "";
						message = "提交信息不完整!";

						sendHint(message, player, null);
						return;
					} else {
						String[] vipRecords = new String[] { req.getInputText()[0], req.getInputText()[1], req.getInputText()[2], infos[0], infos[1], infos[2], infos[3], infos[4], infos[5], infos[6] };

						int result = BossClientService.getInstance().createNewVipRecord(vipRecords);
						if (result == 0) {
							sendHint("您的信息提交成功，您的专属VIP客户经理将于近期通过您留下的联系方式与您取得沟通，请确保信息准确、手机畅通。否则将无法领取专属福利。", player, "确定");

							player.getVipAgent().setLastRecordtime(System.currentTimeMillis());
							player.setVipAgent(player.getVipAgent());
							
							if (VipManager.logger.isInfoEnabled()) {
								VipManager.logger.info("[弹出窗口获取vip信息] [成功] [menuWindowId:" + req.getWId() + "] [viplevel:" + (infos[0]) + "] [username:" + (infos[1]) + "] [name:" + infos[3] + "] [level:" + infos[4] + "] [country:" + infos[5] + "] [servername:" + infos[6] + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
							}
						} else {
							VipManager.logger.error("[弹出窗口获取vip信息] [失败] [menuWindowId:" + req.getWId() + "] [viplevel:" + (infos[0]) + "] [username:" + (infos[1]) + "] [name:" + infos[3] + "] [level:" + infos[4] + "] [country:" + infos[5] + "] [servername:" + infos[6] + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
						}

					}
				} else {
					String[] infos = vipInfo.infos;
					if (VipManager.logger.isInfoEnabled()) {
						VipManager.logger.info("[弹出窗口获取vip信息] [失败] [用户点击取消按钮取消操作] [menuWindowId:" + req.getWId() + "] [viplevel:" + (infos[0]) + "] [username:" + (infos[1]) + "] [name:" + infos[3] + "] [level:" + infos[4] + "] [country:" + infos[5] + "] [servername:" + infos[6] + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
					}
				}

				menuWindowCache.remove(req.getWId());
				return;

			}
		} catch (Exception e) {
			VipManager.logger.error("[收集vip玩家信息] [错误] [出现异常]", e);
		} finally {
			checkWindowCache();
		}
	}

}

class MenuWindow {
	private int winId;

	// 窗口的标题
	private String title;

	// 窗口的描述，UUB格式的
	private String descriptionInUUB;

	// 窗口中的选项
	private Option options[] = new Option[0];

	// png图片数据
	private byte[] png = new byte[0];

	// 销毁时间
	private long destoryTime;

	public void setWinId(int winId) {
		this.winId = winId;
	}

	public int getWinId() {
		return winId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setDescriptionInUUB(String descriptionInUUB) {
		this.descriptionInUUB = descriptionInUUB;
	}

	public String getDescriptionInUUB() {
		return descriptionInUUB;
	}

	public void setOptions(Option options[]) {
		this.options = options;
	}

	public Option[] getOptions() {
		return options;
	}

	public void setPng(byte[] png) {
		this.png = png;
	}

	public byte[] getPng() {
		return png;
	}

	public void setDestoryTime(long destoryTime) {
		this.destoryTime = destoryTime;
	}

	public long getDestoryTime() {
		return destoryTime;
	}

}

class VipInfo {
	public MenuWindow menuWindow;
	public String[] infos;
}
