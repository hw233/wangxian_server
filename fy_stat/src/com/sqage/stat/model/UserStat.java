package com.sqage.stat.model;

import java.util.Date;

/**
 * UserLevel entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class UserStat implements java.io.Serializable {
	
	public static final int REGTYPE_NORMAL = 0;
	public static final int REGTYPE_QUICK = 1;
	public static final int REGTYPE_MODIFY = 2;

	// Fields

	private Long id;
	private String username;
	/**
	 * 注册日期
	 */
	private Date regtime;
	/**
	 * 注册类型，普通、快速或转注册(用户信息修改)
	 */
	private Long regtype = 0L;
	/**
	 * 是否是白名单用户
	 */
	private Long whiteuser = 0L;
	/**
	 * 当前级别
	 */
	private Long nowlevel = 0L;
	/**
	 * 最近升级时间
	 */
	private Date lastleveluptime;
	/**
	 * 渠道
	 */
	private Long channel = 0L;
	/**
	 * 子渠道
	 */
	private Long channelitem = 0L;
	/**
	 * 登陆次数
	 */
	private Long logintimes = 0L;
	/**
	 * 最近登陆日期
	 */
	private Date lastlogintime;
	/**
	 * 角色数量
	 */
	private Long playernum = 0L;
	/**
	 * 在线时长,s
	 */
	private Long onlinetime = 0L;
	/**
	 * 充值次数
	 */
	private Long savingtimes = 0L;
	/**
	 * 消费次数
	 */
	private Long expensetimes = 0L;
	/**
	 * 充值元宝总量
	 */
	private Long savingamount = 0L;
	/**
	 * 充入的人名币总量
	 */
	private Long savingrmb = 0L;
	/**
	 * 消费总量
	 */
	private Long expenseamount = 0L;
	
	/**
	 * 目前账户上的人名币元宝
	 */
	private Long rmbyuanbao = 0L;
	
	/**
	 * 首次登陆时间
	 */
	private Date firstlogintime;
	
	/**
	 * 最后一次付费时间，方便计算停费用户
	 */
	private Date lastexpensetime;
	
	private Long province;
	
	private Long city;
	
	private String mobile;
	
	private String mobiletype;

	// Constructors

	/** default constructor */
	public UserStat() {
	}

	/** full constructor */
	public UserStat(String username, Date regtime, Long regtype,
			Long whiteuser, Long nowlevel, Date lastleveluptime,
			String channelname, String channelitemname) {
		this.username = username;
		this.regtime = regtime;
		this.regtype = regtype;
		this.whiteuser = whiteuser;
		this.nowlevel = nowlevel;
		this.lastleveluptime = lastleveluptime;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getRegtime() {
		return this.regtime;
	}

	public void setRegtime(Date regtime) {
		this.regtime = regtime;
	}

	public Long getRegtype() {
		return this.regtype;
	}

	public void setRegtype(Long regtype) {
		this.regtype = regtype;
	}

	public Long getWhiteuser() {
		return this.whiteuser;
	}

	public void setWhiteuser(Long whiteuser) {
		this.whiteuser = whiteuser;
	}

	public Long getNowlevel() {
		return this.nowlevel;
	}

	public void setNowlevel(Long nowlevel) {
		this.nowlevel = nowlevel;
	}

	public Date getLastleveluptime() {
		return this.lastleveluptime;
	}

	public void setLastleveluptime(Date lastleveluptime) {
		this.lastleveluptime = lastleveluptime;
	}

	public Long getChannel() {
		return channel;
	}

	public void setChannel(Long channel) {
		this.channel = channel;
	}

	public Long getChannelitem() {
		return channelitem;
	}

	public void setChannelitem(Long channelitem) {
		this.channelitem = channelitem;
	}

	public Long getLogintimes() {
		return logintimes;
	}

	public void setLogintimes(Long logintimes) {
		this.logintimes = logintimes;
	}

	public Date getLastlogintime() {
		return lastlogintime;
	}

	public void setLastlogintime(Date lastlogintime) {
		this.lastlogintime = lastlogintime;
	}

	public Long getPlayernum() {
		return playernum;
	}

	public void setPlayernum(Long playernum) {
		this.playernum = playernum;
	}

	public Long getOnlinetime() {
		return onlinetime;
	}

	public void setOnlinetime(Long onlinetime) {
		this.onlinetime = onlinetime;
	}

	public Long getSavingtimes() {
		return savingtimes;
	}

	public void setSavingtimes(Long savingtimes) {
		this.savingtimes = savingtimes;
	}

	public Long getExpensetimes() {
		return expensetimes;
	}

	public void setExpensetimes(Long expensetimes) {
		this.expensetimes = expensetimes;
	}

	public Long getSavingamount() {
		return savingamount;
	}

	public void setSavingamount(Long savingamount) {
		this.savingamount = savingamount;
	}

	public Long getExpenseamount() {
		return expenseamount;
	}

	public void setExpenseamount(Long expenseamount) {
		this.expenseamount = expenseamount;
	}

	public Date getFirstlogintime() {
		return firstlogintime;
	}

	public void setFirstlogintime(Date firstlogintime) {
		this.firstlogintime = firstlogintime;
	}

	public Long getRmbyuanbao() {
		return rmbyuanbao;
	}

	public void setRmbyuanbao(Long rmbyuanbao) {
		this.rmbyuanbao = rmbyuanbao;
	}

	public Date getLastexpensetime() {
		return lastexpensetime;
	}

	public void setLastexpensetime(Date lastexpensetime) {
		this.lastexpensetime = lastexpensetime;
	}

	public Long getSavingrmb() {
		return savingrmb==null?0:savingrmb;
	}

	public void setSavingrmb(Long savingrmb) {
		this.savingrmb = savingrmb;
	}

	public Long getProvince() {
		return province;
	}

	public void setProvince(Long province) {
		this.province = province;
	}

	public Long getCity() {
		return city;
	}

	public void setCity(Long city) {
		this.city = city;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobiletype() {
		return mobiletype;
	}

	public void setMobiletype(String mobiletype) {
		this.mobiletype = mobiletype;
	}

}
