package com.fy.engineserver.sprite;

import com.fy.engineserver.society.SocialManager;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;


public class PlayerSimpleInfo implements Cloneable,Cacheable,CacheListener{

	public PlayerSimpleInfo(){
		
	}
	
	private long id; // 跟人物id一样
	private String name = "";
	private byte career;
	private long durationOnline;
	private String brithDay = "";
	private int star;
	private String icon = "";
	private int age;
	private int playerCountry = -1;
	private int province = -1;
	private int city = -1;
	private String loving = "";
	private String personShow = "";
	private String mood = "";
	private String zongPaiName = "";
	
	private byte seeState; // 可见状态 0完全公开 1仅好友可见 2完全保密

	// new 2011-9-15 13:50:40 by 
	/** 家族ID */
	private long jiazuId = -1;
	/** 家族名 */
	private String jiazuName;
	/** 境界 */
	private int classLevel = 0;
	/** 级别 */
	private int level = 1;
	/** 国家 */
	private byte country;
	/** 洞府ID */
	private long caveId = -1;
	/** 性别 */
	private byte sex;
	
	/** 用户名*/
	private String username = "";

	public String getIcon() {
		if(icon == null) icon = "";
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getBrithDay() {
		if(brithDay == null) brithDay = "";
		return brithDay;
	}
	public void setBrithDay(String brithDay) {
		this.brithDay = brithDay;

	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;

	}

	public String getLoving() {
		if(loving == null) loving = "";
		return loving;
	}

	public void setLoving(String loving) {
		this.loving = loving;

	}

	public String getPersonShow() {
		if(personShow == null) personShow = "";
		return personShow;
	}

	public void setPersonShow(String personShow) {
		this.personShow = personShow;

	}

	public String getMood() {
		if(mood == null) mood = "";
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;

	}

	public long getDurationOnline() {
		return durationOnline;
	}

	public void setDurationOnline(long durationOnline) {
		this.durationOnline = durationOnline;

	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;

	}

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;

	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;

	}

	public byte getSeeState() {
		return seeState;
	}

	public void setSeeState(byte seeState) {
		this.seeState = seeState;

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPlayerCountry() {
		return playerCountry;
	}
	public void setPlayerCountry(int playerCountry) {
		this.playerCountry = playerCountry;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getJiazuId() {
		return jiazuId;
	}

	public void setJiazuId(long jiazuId) {
		this.jiazuId = jiazuId;

	}

	public String getJiazuName() {
		if(jiazuName == null) jiazuName = "";
		return jiazuName;
	}

	public void setJiazuName(String jiazuName) {
		this.jiazuName = jiazuName;

	}

	public int getClassLevel() {
		return classLevel;
	}

	public void setClassLevel(int classLevel) {
		this.classLevel = classLevel;

	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;

	}
	
	public long getCaveId() {
		return caveId;
	}

	void setCaveId(long caveId) {
		this.caveId = caveId;

	}

	public byte getSex() {
		return sex;
	}

	public void setSex(byte sex) {
		this.sex = sex;
	}
	
	public byte getCareer() {
		return career;
	}
	public void setCareer(byte career) {
		this.career = career;

	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;

	}
	

	public String getZongPaiName() {
		if(zongPaiName == null) zongPaiName = "";
		return zongPaiName;
	}
	public void setZongPaiName(String zongPaiName) {
		this.zongPaiName = zongPaiName;
	}
	public PlayerSimpleInfo getinfoForPlayer(Player observer) {
		PlayerSimpleInfo o = null;
//		 0完全公开 1仅好友可见 2完全保密
		if(this.seeState == 0){
			o = this;
			return o;
		}else if(this.seeState == 1){
			int ship = SocialManager.getInstance().getRelationA2B(this.getId(), observer.getId());
			if(ship > 0){
				o = this;
				return o;
			}
		}
		try {
			o = (PlayerSimpleInfo) super.clone();
			o.setProvince(-1);
			o.setCity(-1);
			o.setLoving("");
			o.setPersonShow("");
			o.setStar(-1);
			o.setBrithDay("");
			o.setMood("");

		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;

	}
	@Override
	public void remove(int type) {
		
	}
	
	@Override
	public int getSize() {
		return 10;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		if (username == null) return "";
		return username;
	}
	
}
