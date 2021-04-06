package com.fy.engineserver.datasource.article.data.articles;

import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.time.SystemMaintainManager;

/**
 * 普通物品类
 * 
 * 物品有如下的属性或者操作： 买卖 销毁 同类是否可以重叠防止（所谓同类是指同一个物种） 有效期多少天
 * 
 * 
 */
public class Article {
	/**
	 * 专门为背包分类设置的物品分类，和背包分类一样
	 */
	public static final int KNAP_装备 = 0;
	public static final int KNAP_奇珍 = 0;
	public static final int KNAP_异宝 = 0;
	public static final int KNAP_任务 = 0;
	public static final int KNAP_宠物 = 5;
	public static final int KNAP_NUM = 5;
	public static final String[] KNAP_TYPE_NAME = new String[] { "装备", "奇珍", "异宝", "任务", "宠物" };

	public static final int ARTICLE_TYPE_WEAPON = 1;
	public static final int ARTICLE_TYPE_EQUIPMENT = 2;

	public static final int ARTICLE_TYPE_INLAY_ARTICLE = 3;

	public static final byte ARTICLE_TYPE_HUNSHI= 77;
	public static final byte ARTICLE_TYPE_TAOZHUANGHUNSHI= 78;
	public static final byte ARTICLE_TYPE_MAGIC_PROPS = 80;
	public static final byte ARTICLE_TYPE_PET_EGG = 81;
	public static final byte ARTICLE_TYPE_PET = 82;
	public static final byte ARTICLE_TYPE_FANGBAO = 83;
	public static final byte ARTICLE_TYPE_TUZHI = 84;
	public static final byte ARTICLE_TYPE_BRIGHTINLAY = 85;
	public static final byte ARTICLE_TYPE_WING= 86;
	public static final byte ARTICLE_TYPE_PET_FLY= 87;
	public static final byte ARTICLE_TYPE_SKILL_CD= 88;

	/**
	 * 不绑定
	 */
	public static final byte BINDTYPE_NOBIND = 0;

	/**
	 * 使用（装备）绑定
	 */
	public static final byte BINDTYPE_USE = 1;
	/**
	 * 拾取绑定
	 */
	public static final byte BINDTYPE_PICKUP = 2;

	/**
	 * 不提示拾取绑定，用于一些物品直接绑定
	 */
	public static final byte BINDTYPE_NOHINT_PICKUP = 3;

	/**
	 * 激活方式使用激活
	 */
	public static final byte ACTIVATION_OPTION_USE = 0;

	/**
	 * 激活方式生成激活
	 */
	public static final byte ACTIVATION_OPTION_CREATE = 1;

	/**
	 * 失效后不做处理
	 */
	public static final byte INVALID_AFTER_NOACTION = 0;

	/**
	 * 失效后续费
	 */
	public static final byte INVALID_AFTER_CONTINUE = 1;

	/**
	 * 失效后消失
	 */
	public static final byte INVALID_AFTER_DISAPPEAR = 2;

	// public static String getTypeDesp(byte type) {
	// try {
	// return TYPE_DESP[type];
	// }catch(Exception e) {
	// return Translate.translate.text_1211;
	// }
	// }

	public byte getArticleType() {
		return 0;
	}

	/**
	 * 客户端用的，0为不能合成 1为能合成
	 */
	public byte composeArticleType = 0;

	public byte getComposeArticleType() {
		return composeArticleType;
	}

	/**
	 * 物品的名字，此名字代表了这个物品， 所以此名词在物品种类中必须唯一
	 */
	protected String name;

	protected String name_stat;

	/**
	 * 背包类型，用于区分放入哪种背包
	 * KNAP_装备 = 0;
	 * KNAP_奇珍 = 1;
	 * KNAP_异宝 = 2;
	 * KNAP_任务 = 3;
	 * KNAP_宠物 = 4;
	 */
	protected int knapsackType = 2;

	/**
	 * 物品的图标编号
	 */
	protected String iconId = "";

	/**
	 * 掉落NPC的形象
	 */
	public String flopNPCAvata = "baoxiang";

	/**
	 * 物品的描述
	 */
	protected String description;

	/**
	 * 物品的故事
	 */
	protected String stroy;

	/**
	 * 物品的价格
	 */
	protected int price;

	/**
	 * 可卖的标志，false表示不可卖
	 */
	protected boolean sailFlag = true;

	/**
	 * 同类是否可以重叠防止（所谓同类是指同一个物种）<br>
	 * 如果为true，那么对应的此物种的物体上，有一个总体的数量<br>
	 * 多个此物体公用一个物体
	 */
	protected boolean overlap;

	/**
	 * 堆叠数量
	 */
	protected int overLapNum;

	/**
	 * 最大的有效期，0表示无有效期限制，单位为分。
	 */
	protected long maxValidDays;

	/**
	 * 有效期标识，true表示有有效期限制，此时maxValidDays != 0的情况下，有效期为maxValidDays;false表示没有有效期，此时maxValidDays无效
	 */
	protected boolean haveValidDays;

	/**
	 * 此属性只为堆叠类物品，有有效期的使用。为了防止产生过多的物体，采用分段类生成(同一个时间段，产生一个物体)，此数值为分段的间隔，单位为分。
	 * 比如60分，即一天分为24段，从零点到23点，1点1分产生的物体和1点50分产生的物体都用同一个物体该物体产生时间为2点(后延一时间段)
	 */
	protected long timeInterval;

	/**
	 * 过期后动作0无动作，1续费，2消失
	 */
	protected byte invalidAfterAction = 2;

	/**
	 * 激活方式0使用激活，1产生物体时就激活
	 */
	protected byte activationOption;

	/**
	 * 有有效期时有用，此时间类型表示是剔除维护时间还是下线时间还是暂停时间等等
	 * 时间类型_绝对型 = 0;
	 * 时间类型_剔除维护型 = 1;
	 * 时间类型_剔除下线型 = 2;
	 * 时间类型_剔除暂停型 = 3;
	 * 时间类型_剔除维护且下线型 = 4;
	 * 时间类型_剔除维护且暂停型 = 5;
	 * 时间类型_剔除下线且暂停型 = 6;
	 * 时间类型_剔除维护且下线且暂停型 = 7;
	 */
	protected byte timeType = SystemMaintainManager.时间类型_剔除维护型;

	/**
	 * 是不是加入统计
	 */
	public boolean tongji = false;

	/**
	 * 续费所需元宝
	 */
	protected long validNeedRuanbao;

	/**
	 * 是否可以被销毁
	 */
	protected boolean destroyed;

	/**
	 * 绑定方式0不绑定，1装备绑定，2拾取绑定
	 */
	protected byte bindStyle;

	/**
	 * 物品等级
	 */
	protected int articleLevel = 1;

	/**
	 * 品质颜色
	 */
	protected int colorType;

	protected String 物品一级分类 = "";

	protected String 物品二级分类 = "";

	protected String 物品一级分类_stat = "";

	protected String 物品二级分类_stat = "";
	/**
	 * 掉落标记，0
	 */
	protected byte flopMark;

	/**
	 * 使用方法描述
	 */
	protected String useMethod;

	/**
	 * 获得方法描述
	 */
	protected String getMethod;

	/**
	 * 对于获得临时物品的描述，不用通过临时创建物品实体来获取
	 * @return
	 */
	public String getInfoShow() {
		return "";
	}

	public String get物品一级分类() {
		return 物品一级分类;
	}

	public void set物品一级分类(String 物品一级分类) {
		this.物品一级分类 = 物品一级分类;
	}

	public String get物品二级分类() {
		if(物品二级分类 != null && 物品二级分类.equals("封魔录")){
			return  "封魔录";
		}
		return 物品二级分类;
	}

	public void set物品二级分类(String 物品二级分类) {
		this.物品二级分类 = 物品二级分类;
	}

	public String getFlopNPCAvata() {
		return flopNPCAvata;
	}

	public void setFlopNPCAvata(String flopNPCAvata) {
		this.flopNPCAvata = flopNPCAvata;
	}

	public int getKnapsackType() {
		return knapsackType;
	}

	public void setKnapsackType(int knapsackType) {
		this.knapsackType = knapsackType;
	}

	public String getStroy() {
		return stroy;
	}

	public void setStroy(String stroy) {
		this.stroy = stroy;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public boolean isSailFlag() {
		return sailFlag;
	}

	public void setSailFlag(boolean sailFlag) {
		this.sailFlag = sailFlag;
	}

	public byte getBindStyle() {
		return bindStyle;
	}

	public void setBindStyle(byte bindStyle) {
		this.bindStyle = bindStyle;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isOverlap() {
		return overlap;
	}

	public void setOverlap(boolean overlap) {
		this.overlap = overlap;
	}

	public int getOverLapNum() {
		return overLapNum;
	}

	public void setOverLapNum(int overLapNum) {
		this.overLapNum = overLapNum;
	}

	public long getMaxValidDays() {
		return maxValidDays;
	}

	public void setMaxValidDays(long maxValidDays) {
		this.maxValidDays = maxValidDays;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public boolean isHaveValidDays() {
		return haveValidDays;
	}

	public void setHaveValidDays(boolean haveValidDays) {
		this.haveValidDays = haveValidDays;
	}

	public byte getInvalidAfterAction() {
		return invalidAfterAction;
	}

	public void setInvalidAfterAction(byte invalidAfterAction) {
		this.invalidAfterAction = invalidAfterAction;
	}

	public byte getActivationOption() {
		return activationOption;
	}

	public void setActivationOption(byte activationOption) {
		this.activationOption = activationOption;
	}

	public long getValidNeedRuanbao() {
		return validNeedRuanbao;
	}

	public void setValidNeedRuanbao(long validNeedRuanbao) {
		this.validNeedRuanbao = validNeedRuanbao;
	}

	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}

	public String getIconId() {
		return iconId;
	}

	public void setIconId(String iconName) {
		this.iconId = iconName;
	}

	public int getArticleLevel() {
		return articleLevel;
	}

	public void setArticleLevel(int articleLevel) {
		this.articleLevel = articleLevel;
	}

	public int getColorType() {
		return colorType;
	}

	public void setColorType(int colorType) {
		this.colorType = colorType;
	}

	public byte getFlopMark() {
		return flopMark;
	}

	public void setFlopMark(byte flopMark) {
		this.flopMark = flopMark;
	}

	public String getUseMethod() {
		return useMethod;
	}

	public void setUseMethod(String useMethod) {
		this.useMethod = useMethod;
	}

	public String getGetMethod() {
		return getMethod;
	}

	public void setGetMethod(String getMethod) {
		this.getMethod = getMethod;
	}

	public long getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(long timeInterval) {
		this.timeInterval = timeInterval;
	}

	public byte getTimeType() {
		return timeType;
	}

	public void setTimeType(byte timeType) {
		this.timeType = timeType;
	}

	public String getName_stat() {
		return name_stat;
	}

	public void setName_stat(String name_stat) {
		this.name_stat = name_stat;
	}

	public String get物品一级分类_stat() {
		return 物品一级分类_stat;
	}

	public void set物品一级分类_stat(String 物品一级分类_stat) {
		this.物品一级分类_stat = 物品一级分类_stat;
	}

	public String get物品二级分类_stat() {
		return 物品二级分类_stat;
	}

	public void set物品二级分类_stat(String 物品二级分类_stat) {
		this.物品二级分类_stat = 物品二级分类_stat;
	}

	private String logString = null;

	public String getInfoShowHead(Player player, int colorType) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("<i imagePath='").append(this.getIconId()).append("' bgImagePath='/ui/texture_map1n2.png' rectBgX='665' rectBgY='145' rectBgW='68' rectBgH='68' width='68' height='68'></i>");
		String nameShow = this.getName();
		if (this instanceof Equipment) {
			if (getColorType() == 4 || getColorType() == 6) {
				nameShow = nameShow + "●" + Translate.完美;
			}
		}
		sbf.append("<f color='").append(ArticleManager.getColorValue(this, colorType)).append("' size='28'>").append(nameShow).append(" ").append(this.getSpecialInfo(player, colorType)).append("</f>\n\n");
		return sbf.toString();
	}

	public String getSpecialInfo(Player player, int colorType) {
		return "";
	}

	public String getInfoShowBody(Player player) {
		StringBuffer sb = new StringBuffer();
		if (this.getDescription() != null && !this.getDescription().trim().equals("")) {
			sb.append("\n<f color='0x00FF00'>").append(this.getDescription()).append("</f>");
		}
		if (this.getStroy() != null && !this.getStroy().trim().equals("")) {
			sb.append("\n<f color='0xFFFFFF'>").append(this.getStroy()).append("</f>");
		}
		if (this instanceof InlayArticle) {
			sb.append("\n<f color='0x00FF00'>").append(Translate.镶嵌境界限定).append(":").append(PlayerManager.classlevel[((InlayArticle) this).getClassLevel()]).append("</f>");
			int[] values = ((InlayArticle) this).getPropertysValues();
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					int value = values[i];
					if (value != 0) {
						switch (i) {
						case 0:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.最大血量).append("+").append(value).append("</f>");
							break;
						case 1:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.物理攻击).append("+").append(value).append("</f>");
							break;
						case 2:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.法术攻击).append("+").append(value).append("</f>");
							break;
						case 3:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.物理防御).append("+").append(value).append("</f>");
							break;
						case 4:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.法术防御).append("+").append(value).append("</f>");
							break;
						case 5:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.闪躲).append("+").append(value).append("</f>");
							break;
						case 6:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.免暴).append("+").append(value).append("</f>");
							break;
						case 7:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.命中).append("+").append(value).append("</f>");
							break;
						case 8:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.暴击).append("+").append(value).append("</f>");
							break;
						case 9:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.精准).append("+").append(value).append("</f>");
							break;
						case 10:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.破甲).append("+").append(value).append("</f>");
							break;
						case 11:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.庚金攻击).append("+").append(value).append("</f>");
							break;
						case 23:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.庚金攻击).append("+").append(value).append("</f>");
							break;
						case 26:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.庚金攻击).append("+").append(value).append("</f>");
							break;
						case 12:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.庚金抗性).append("+").append(value).append("</f>");
							break;
						case 24:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.庚金抗性).append("+").append(value).append("</f>");
							break;
						case 27:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.庚金抗性).append("+").append(value).append("</f>");
							break;
						case 13:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.庚金减抗).append("+").append(value).append("</f>");
							break;
						case 25:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.庚金减抗).append("+").append(value).append("</f>");
							break;
						case 28:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.庚金减抗).append("+").append(value).append("</f>");
							break;
						case 14:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.葵水攻击).append("+").append(value).append("</f>");
							break;
						case 29:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.葵水攻击).append("+").append(value).append("</f>");
							break;
						case 32:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.葵水攻击).append("+").append(value).append("</f>");
							break;
						case 15:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.葵水抗性).append("+").append(value).append("</f>");
							break;
						case 30:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.葵水抗性).append("+").append(value).append("</f>");
							break;
						case 33:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.葵水抗性).append("+").append(value).append("</f>");
							break;
						case 16:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.葵水减抗).append("+").append(value).append("</f>");
							break;
						case 31:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.葵水减抗).append("+").append(value).append("</f>");
							break;
						case 34:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.葵水减抗).append("+").append(value).append("</f>");
							break;
						case 17:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.离火攻击).append("+").append(value).append("</f>");
							break;
						case 41:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.离火攻击).append("+").append(value).append("</f>");
							break;
						case 44:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.离火攻击).append("+").append(value).append("</f>");
							break;
						case 18:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.离火抗性).append("+").append(value).append("</f>");
							break;
						case 42:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.离火抗性).append("+").append(value).append("</f>");
							break;
						case 45:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.离火抗性).append("+").append(value).append("</f>");
							break;
						case 19:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.离火减抗).append("+").append(value).append("</f>");
							break;
						case 43:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.离火减抗).append("+").append(value).append("</f>");
							break;
						case 46:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.离火减抗).append("+").append(value).append("</f>");
							break;
						case 20:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.乙木攻击).append("+").append(value).append("</f>");
							break;
						case 35:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.乙木攻击).append("+").append(value).append("</f>");
							break;
						case 38:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.乙木攻击).append("+").append(value).append("</f>");
							break;
						case 21:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.乙木抗性).append("+").append(value).append("</f>");
							break;
						case 36:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.乙木抗性).append("+").append(value).append("</f>");
							break;
						case 39:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.乙木抗性).append("+").append(value).append("</f>");
							break;
						case 22:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.乙木减抗).append("+").append(value).append("</f>");
							break;
						case 37:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.乙木减抗).append("+").append(value).append("</f>");
							break;
						case 40:
							sb.append("\n<f color='0x00e4ff'>").append(Translate.乙木减抗).append("+").append(value).append("</f>");
							break;
						}
					}
				}
			}
		}
		if (this instanceof PetFoodArticle) {
			PetFoodArticle pfa = (PetFoodArticle) this;
			if (pfa.getType() == 1) {
				sb.append("\n<f color='0xFFFFFF'>").append(Translate.可增加快乐值).append(pfa.getValueByColor(this.getColorType())).append("</f>");
			} else if (pfa.getType() == 2) {
				sb.append("\n<f color='0xFFFFFF'>").append(Translate.可增加寿命值).append(pfa.getValueByColor(this.getColorType())).append("</f>");
			}

		}
		if (this.getUseMethod() != null && !this.getUseMethod().trim().equals("")) {
			sb.append("\n<f color='0xFFFF00'>").append(this.getUseMethod()).append("</f>");
		}
		if (this.getGetMethod() != null && !this.getGetMethod().trim().equals("")) {
			sb.append("\n<f color='0xFFFF00'>").append(this.getGetMethod()).append("</f>");
		}
		return sb.toString();
	}

	public String getLogString() {
		if (logString == null) {
			logString = "[" + this.getName() + "," + this.getName_stat() + "]";
		}
		return logString;
	}
}
