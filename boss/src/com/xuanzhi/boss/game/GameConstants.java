package com.xuanzhi.boss.game;

import java.io.UnsupportedEncodingException;

import com.xuanzhi.tools.text.FileUtils;

public class GameConstants {
	public static final int TYPE_TEST = 0; //测试类型的服务器
	public static final int TYPE_FORMAL = 1;	//正式发布的服务器
	
	private String gameName;
	
	private String serverName;
	
	private String serverAddress;
	
	private int serverType;
	
	private byte hostid;
	
	private byte serverid;
	
	private boolean isTaiWanGame;
	
	private String domainName;
	
	private String platForm;
	

	protected static GameConstants self;
	
	/**
	 * 国家或地区
	 */
	private String countryOrArea="";
	
	/**
	 * 第三方ID，台湾合作方指定
	 */
	public static final String TAIWAN_PID="14";
	
	/**
	 * 第三方账户，台湾合作方指定
	 */
	public static final String TAIWAN_ACCOUNT="qlonline";
	
	/**
	 * 第三方密码，台湾合作方指定
	 */
	public static final String TAIWAN_PWD="f786669d2d536b96b961d9d9489eee3e";
	
	/**
	 * 第三方付费代码，台湾合作方指定
	 * 分别对应50，100，150，250，500的Q币消费
	 */
	public static final int[] TAIWAN_CONTENT_ID={181,182,183,184,185};
	
	/**
	 * Q币消费额度，与付费代码相对应
	 */
	public static final int[] TAIWAN_QBI_EXPENSE_ITEM={50,100,150,250,500};
	
	public static final int[] TAIWAN_QBI_EXPENSE_ITEM_HK={15,30,45,75,150};
	
	public static final String TAIWAN_SMBODY="joyaa0c01p169";
	
	/**
	 * Q币与元宝的兑换比率
	 */
	public static final float TAIWAN_QBI_EXCHANGE_RATE=100f;
	
	public static GameConstants getInstance() {
		return self;
	}
	
	public void init() {
		self = this;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public int getServerType() {
		return serverType;
	}

	public void setServerType(int serverType) {
		this.serverType = serverType;
	}

	public byte getTheHostid() {
		return hostid;
	}

	public void setHostid(String value) {
		this.hostid = Integer.decode(value).byteValue();;
	}

	public byte getTheServerid() {
		return serverid;
	}

	public void setServerid(String value) {
		this.serverid = Integer.decode(value).byteValue();
	}

	public boolean isTaiWanGame() {
		return isTaiWanGame;
	}

	public void setIsTaiWanGame(boolean isTaiWanGame) {
		this.isTaiWanGame = isTaiWanGame;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getCountryOrArea() {
		return countryOrArea;
	}

	public void setCountryOrArea(String countryOrArea) {
		this.countryOrArea = countryOrArea;
	}
	
	public String getPlatForm() {
		return platForm;
	}

	public void setPlatForm(String platForm) {
		this.platForm = platForm;
	}
	
	public static void main(String args[]) {
		String file = "D:/gamepj/mieshi_game_boss/src/com/mieshi/boss/platform/qq/QQMessageService.java";
		String content = FileUtils.readFile(file);
		try {
			System.out.println(new String(content.getBytes("iso-8859-1"),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
