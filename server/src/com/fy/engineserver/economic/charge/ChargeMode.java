package com.fy.engineserver.economic.charge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.client.BossClientService;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.A_GET_ORDERID_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 充值通道(各种充值方式)
 * 各种充值方式都要继承此类.且需要覆盖doCharge()方法,
 * (由于发协议的需要,此类不能是抽象,故doCharge()方法需要自行覆盖)
 * 
 * 
 */
public class ChargeMode {

	/** 用户渠道 */
	private String userChannel;
	/** 模式名字 cardType */
	private String modeName;
	/** 模式显示名字 */
	private String modeShowName;
	/** 上描述 */
	private String topDescription;
	/** 下描述 */
	private String footDescription;
	/** 按键显示文字 */
	private String buttonText;
	/** 显示顺序 */
	private int showOrder;
	/** 充值提示框 卡号.密码 等等 */
	private String[] chargeTexts;
	/** 充值列表 */
	private List<ChargeMoneyConfigure> moneyConfigures = new ArrayList<ChargeMoneyConfigure>();
	/** 充值列表选项对应的等级.(低于这个等级不开放)可能是NULL,NULL表示吧 */
	private Integer[] showConfiguresLevelLimit;
	/** 各种充值兑换比例 */
	private List<ChargeRatio> chargeRatios = new ArrayList<ChargeRatio>();
	private String[] moneyConfigureIds;

	/** 充值的详细描述 */
	private String particularDes;

	public ChargeMode() {

	}

	public ChargeMode(String modeName) {
		this.modeName = modeName;
	}

	public void setAllParm(String userChannel, String modeShowName, String topDescription, String footDescription, String buttonText, int showOrder, String[] moneyConfigureIds, String[] chargeTexts, String particularDes, List<ChargeRatio> chargeRatios, Integer[] showConfiguresLevelLimit) {
		this.userChannel = userChannel;
		this.modeShowName = modeShowName;
		this.topDescription = topDescription;
		this.footDescription = footDescription;
		this.buttonText = buttonText;
		this.showOrder = showOrder;
		this.moneyConfigureIds = moneyConfigureIds;
		this.chargeTexts = chargeTexts;
		this.particularDes = particularDes;
		this.chargeRatios = chargeRatios;
		this.showConfiguresLevelLimit = showConfiguresLevelLimit;
	}

	/**
	 * 得到充值可以获得多少银两
	 * @param money
	 * @return
	 */
	public long getChargeYinzi(long money) {
		ChargeRatio chargeRatio = getFitChargeRatio((int) money);
		double ratio = 1;
		if (chargeRatio != null) {
			ratio = chargeRatio.getRatio();
			if (ratio <= 0) {
				ratio = 1;
			}
		}
		int yinzi = 0;
		if (!ChargeManager.getInstance().allowFen(getModeName())) {
			yinzi = (int) (ChargeRatio.DEFAULT_CHARGE_RATIO * ratio * money);
			if (yinzi <= 0) {
				return 0;
			}
		} else {
			yinzi = (int) (ChargeRatio.DEFAULT_CHARGE_RATIO / 100 * ratio * money);// 这里的RMB是分----
		}
		return yinzi;
	}

	/**
	 * 得到符合的充值区间,没找到的按照默认的比例
	 * @param rmb
	 * @return
	 */
	public ChargeRatio getFitChargeRatio(int rmb) {
		for (ChargeRatio cr : chargeRatios) {
			if (cr.fitthisRatio(rmb)) {
				return cr;
			}
		}
		return null;
	}

	/**
	 * 执行用户充值
	 * @param player
	 * @param passport
	 * @param chargeMoneyConfigure
	 * @param parms各种参数服务器与客户端匹配
	 *            public void playerSaving(String playername, String username, String servername, String channel, String cardtype, String cardno,
	 *            String cardpass, int mianzhi, int goodsType, int amount, String orderID) throws SavingFailedException
	 * @return
	 */
	public synchronized String doCharge(Player player, Passport passport, String clientChannel, String os, ChargeMoneyConfigure chargeMoneyConfigure, String... parms) {
		BossClientService bossClientService = BossClientService.getInstance();
		GameConstants gameConstants = GameConstants.getInstance();
		String notice = Translate.充值提交成功请稍后在充值记录中查询;
		try {
			if (clientChannel.equalsIgnoreCase("YOUAI_XUNXIAN")) {// 自己渠道的
				if (ChargeManager.useNewChargeInterface) {
					bossClientService.playerSaving(os, player.getName(), player.getUsername(), gameConstants.getServerName(), clientChannel, getModeName(), parms[0], parms[1], (int) chargeMoneyConfigure.getDenomination(), -1, -1, "",new String[]{String.valueOf(player.getId())});
				} else {
					bossClientService.playerSaving(os, player.getName(), player.getUsername(), gameConstants.getServerName(), clientChannel, getModeName(), parms[0], parms[1], (int) chargeMoneyConfigure.getDenomination(), -1, -1, "");
				}
			} else {
				String modeName = getModeName();
				String clientChannelTmp = ChargeManager.getInstance().modifyChannelName(clientChannel);
//				boolean isBaiduDuoku = clientChannelTmp.toLowerCase().equals("duokuapi_mieshi") || clientChannelTmp.toLowerCase().equals("baiduyy_mieshi");
				boolean isBaiduDuoku = false;
				if (isBaiduDuoku) {
					if(!modeName.contains("支付宝"))
					{
						if (parms.length >= 3) {
							modeName += ("@" + parms[2]);
						} else {
						ChargeManager.logger.error(player.getLogString() + "[充值异常] [doCharge] [卡类型:" + modeName + "] [充值面额ID:" + chargeMoneyConfigure.getId() + "] [渠道:" + clientChannel + "] [平台:" + os + "] [parms:" + Arrays.toString(parms) + "]");
						throw new IllegalStateException("充值异常,参数不符合约定:" + Arrays.toString(parms));
						}
					}
					else
					{
						modeName += ("@" + parms[0]);
					}
				}
				String bossReturnContent = "";
				if (ChargeManager.useNewChargeInterface ) {
					if(isBaiduDuoku && modeName.contains("支付宝"))
						bossReturnContent = bossClientService.savingForChannelUser(player.getUsername(), 1, modeName, parms[0], parms[0], (int) chargeMoneyConfigure.getDenomination(), gameConstants.getServerName(), clientChannel, os,new String[]{String.valueOf(player.getId())});
					else
						bossReturnContent = bossClientService.savingForChannelUser(player.getUsername(), 1, modeName, parms[0], parms[1], (int) chargeMoneyConfigure.getDenomination(), gameConstants.getServerName(), clientChannel, os,new String[]{String.valueOf(player.getId())});
				} else {
					if(isBaiduDuoku && modeName.contains("支付宝"))
						bossReturnContent =	bossClientService.savingForChannelUser(player.getUsername(), 1, modeName, parms[0], parms[0], (int) chargeMoneyConfigure.getDenomination(), gameConstants.getServerName(), clientChannel, os);
					else
						bossReturnContent =	bossClientService.savingForChannelUser(player.getUsername(), 1, modeName, parms[0], parms[1], (int) chargeMoneyConfigure.getDenomination(), gameConstants.getServerName(), clientChannel, os);
				}
				
				if(isBaiduDuoku && modeName.contains("支付宝"))
				{
					if (bossReturnContent != null) {
						if (ChargeManager.logger.isWarnEnabled()) {
							ChargeManager.logger.warn("[生成支付宝订单] [username:" + player.getUsername() + "] [" +  (int) chargeMoneyConfigure.getDenomination() + "] [channel:" + clientChannel + "] [servername:" + GameConstants.getInstance().getServerName() + "] [orderID:" + bossReturnContent+ "]");
						}
						A_GET_ORDERID_RES alipayRes =  new A_GET_ORDERID_RES(GameMessageFactory.nextSequnceNum(), "", bossReturnContent, "", 0);
						player.addMessageToRightBag(alipayRes);
					}
				}
			}
		} catch (Exception e) {
			notice = Translate.提交异常请联系GM;
			ChargeManager.logger.error(player.getLogString() + "[充值异常] [doCharge] [卡类型:" + modeName + "] [充值面额ID:" + chargeMoneyConfigure.getId() + "] [渠道:" + clientChannel + "] [平台:" + os + "] [parms:" + Arrays.toString(parms) + "]", e);
		}
		if (ChargeManager.logger.isWarnEnabled()) {
			ChargeManager.logger.warn(player.getLogString() + "[充值提交] [doCharge] [卡类型:" + modeName + "] [充值面额ID:" + chargeMoneyConfigure.getId() + "] [渠道:" + clientChannel + "] [平台:" + os + "] [parms:" + Arrays.toString(parms) + "] [结果:" + notice + "]");
		}
		return notice;
	}

	public List<ChargeMoneyConfigure> getMoneyConfigures(Player player) {
		if (showConfiguresLevelLimit == null) {
			if (ChargeManager.logger.isWarnEnabled()) {
				ChargeManager.logger.warn(player.getLogString() + "[查询充值方式:" + modeName + "] [没有配置充值等级限制,看见所有的充值方式]");
			}
			return moneyConfigures;
		}
		List<ChargeMoneyConfigure> returnList = new ArrayList<ChargeMoneyConfigure>();
		for (int i = 0; i < showConfiguresLevelLimit.length; i++) {
			int levelLimit = showConfiguresLevelLimit[i];
			if (player.getLevel() >= levelLimit) {
				returnList.add(moneyConfigures.get(i));
				ChargeManager.logger.warn(player.getLogString() + "[查询充值方式:" + modeName + "] [等级限制:" + levelLimit + "] [可以看到充值金额:" + moneyConfigures.get(i).getShowText() + "]");
			} else {
				ChargeManager.logger.warn(player.getLogString() + "[查询充值方式:" + modeName + "] [等级限制:" + levelLimit + "] [看不到金额:" + moneyConfigures.get(i).getShowText() + "]");
			}
		}
		return returnList;
	}

	public String getUserChannel() {
		return userChannel;
	}

	public void setUserChannel(String userChannel) {
		this.userChannel = userChannel;
	}

	public String getModeName() {
		return modeName;
	}

	public void setModeName(String modeName) {
		this.modeName = modeName;
	}

	public String getModeShowName() {
		return modeShowName;
	}

	public void setModeShowName(String modeShowName) {
		this.modeShowName = modeShowName;
	}

	public String getTopDescription() {
		return topDescription;
	}

	public void setTopDescription(String topDescription) {
		this.topDescription = topDescription;
	}

	public String getFootDescription() {
		return footDescription;
	}

	public void setFootDescription(String footDescription) {
		this.footDescription = footDescription;
	}

	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	public List<ChargeMoneyConfigure> getMoneyConfigures() {
		return moneyConfigures;
	}

	public void setMoneyConfigures(List<ChargeMoneyConfigure> moneyConfigures) {
		this.moneyConfigures = moneyConfigures;
	}

	public int getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(int showOrder) {
		this.showOrder = showOrder;
	}

	public String[] getMoneyConfigureIds() {
		return moneyConfigureIds;
	}

	public void setMoneyConfigureIds(String[] moneyConfigureIds) {
		this.moneyConfigureIds = moneyConfigureIds;
	}

	public String[] getChargeTexts() {
		return chargeTexts;
	}

	public void setChargeTexts(String[] chargeTexts) {
		this.chargeTexts = chargeTexts;
	}

	public String getParticularDes() {
		return particularDes;
	}

	public void setParticularDes(String particularDes) {
		this.particularDes = particularDes;
	}

	public List<ChargeRatio> getChargeRatios() {
		return chargeRatios;
	}

	public void setChargeRatios(List<ChargeRatio> chargeRatios) {
		this.chargeRatios = chargeRatios;
	}

	public Integer[] getShowConfiguresLevelLimit() {
		return showConfiguresLevelLimit;
	}

	public void setShowConfiguresLevelLimit(Integer[] showConfiguresLevelLimit) {
		this.showConfiguresLevelLimit = showConfiguresLevelLimit;
	}

	@Override
	public String toString() {
		return "ChargeMode [userChannel=" + userChannel + ", modeName=" + modeName + ", modeShowName=" + modeShowName + ", topDescription=" + topDescription + ", footDescription=" + footDescription + ", buttonText=" + buttonText + ", showOrder=" + showOrder + ", chargeTexts=" + Arrays.toString(chargeTexts) + ", moneyConfigures=" + moneyConfigures + ", showConfiguresLevelLimit=" + Arrays.toString(showConfiguresLevelLimit) + ", chargeRatios=" + chargeRatios + ", moneyConfigureIds=" + Arrays.toString(moneyConfigureIds) + ", particularDes=" + particularDes + "]";
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		if (GameConstants.getInstance() == null) {
			return this;
		}
		if ("幽恋蝶谷".equals(GameConstants.getInstance().getServerName())) {
			ChargeMode a = new ChargeMode();
			a.userChannel = this.userChannel;
			a.modeName = "MalaiAppstore";
			a.modeShowName = this.modeShowName;
			a.topDescription = this.topDescription;
			a.footDescription = this.footDescription;
			a.buttonText = this.buttonText;
			a.showOrder = this.showOrder;
			a.chargeTexts = this.chargeTexts;
			a.moneyConfigures = this.moneyConfigures;
			a.showConfiguresLevelLimit = this.showConfiguresLevelLimit;
			a.chargeRatios = this.chargeRatios;
			a.moneyConfigureIds = this.moneyConfigureIds;
			a.particularDes = this.particularDes;
			return a;
		}

		return this;
	}
}
