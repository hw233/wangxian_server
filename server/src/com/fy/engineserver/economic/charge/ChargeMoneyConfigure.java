package com.fy.engineserver.economic.charge;

import java.util.ArrayList;
import java.util.List;

/**
 * 充值金额配置
 * 
 * 
 */
public class ChargeMoneyConfigure {
	/** ID */
	private String id;
	/** 面额(单位:分) */
	private long denomination;
	/** 配置类型:0-固定面值,1-输入面值 */
	private int type;
	/** 显示的文字 */
	private String showText;
	/** 描述 */
	private String description;
	/** 简短描述 */
	private String short_des;


	/** 特殊的配置 */
	private String specialConf;
//	/背景图片
	private String backageIcon;

	private transient List<ChargeMode> chargeMode = new ArrayList<ChargeMode>();

	public ChargeMoneyConfigure() {
		// TODO Auto-generated constructor stub
	}

	public ChargeMoneyConfigure(String id, long denomination, int type, String showText, String description, String specialConf) {
		super();
		this.id = id;
		this.denomination = denomination;
		this.type = type;
		this.showText = showText;
		this.description = description;
		this.specialConf = specialConf;
	}

	// public boolean containsMode(String modeName) {
	// if (chargeMode == null || chargeMode.size() == 0) {
	// return false;
	// }
	// for (ChargeMode cm : chargeMode) {
	// if (cm != null && cm.getModeName().equals(modeName)) {
	// return true;
	// }
	// }
	// return false;
	// }

	public ChargeMode getChargeMode(String modeName) {
		if (chargeMode == null || chargeMode.size() == 0) {
			return null;
		}
		for (ChargeMode cm : chargeMode) {
			if (cm != null && cm.getModeName().equals(modeName)) {
				return cm;
			}
		}
		return null;
	}

	public String getId() {
		return id;
	}

	public String getBackageIcon() {
		return backageIcon;
	}

	public void setBackageIcon(String backageIcon) {
		this.backageIcon = backageIcon;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getDenomination() {
		return denomination;
	}

	public void setDenomination(long denomination) {
		this.denomination = denomination;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getShowText() {
		return showText;
	}

	public void setShowText(String showText) {
		this.showText = showText;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public String getShort_des() {
		return short_des;
	}

	public void setShort_des(String short_des) {
		this.short_des = short_des;
	}
	public List<ChargeMode> getChargeMode() {
		return chargeMode;
	}

	public void setChargeMode(List<ChargeMode> chargeMode) {
		this.chargeMode = chargeMode;
	}

	public String getSpecialConf() {
		return specialConf;
	}

	public void setSpecialConf(String specialConf) {
		this.specialConf = specialConf;
	}

	@Override
	public String toString() {
		return "ChargeMoneyConfigure [id=" + id + ", denomination=" + denomination + ", " +
				"type=" + type + ", showText=" + showText + ", description=" + description+ ", short_des=" + short_des + ", specialConf=" + specialConf + "backageIcon:"+backageIcon+"]";
	}
}
