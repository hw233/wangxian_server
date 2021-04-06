package com.fy.engineserver.newBillboard;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.fy.engineserver.activity.fireActivity.FireManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
@SimpleIndices({
	
	@SimpleIndex(name="BillboardStatDate_country",members = { "country" },compress=1),
	@SimpleIndex(name="BillboardStatDate_flowers",members = { "flowersNum" }),
	@SimpleIndex(name="BillboardStatDate_sweets",members = { "sweetsNum" }),
	@SimpleIndex(name="BillboardStatDate_drinkBeer",members = { "drinkBeerNum" }),
	@SimpleIndex(name="BillboardStatDate_peek",members = { "peekNum" }),
	@SimpleIndex(name="BillboardStatDate_brick",members = { "brickNum" }),
	@SimpleIndex(name="BillboardStatDate_stealFruit",members = { "stealFruitNum" }),
	@SimpleIndex(name="BillboardStatDate_countryWar",members = { "countryWarNum" }),
	@SimpleIndex(name="BillboardStatDate_Join_S",members = { "killJoinNum","killJoinScore" }),
	@SimpleIndex(name="BillboardStatDate_Ta_S",members = { "jie","ceng" }),

	@SimpleIndex(name="BillboardStatDate_Join_m",members = { "killJoinNum","monthKillJoin","killJoinScore" },compress=1),
	@SimpleIndex(name="BillboardStatDate_Join_D",members = { "dayKillJoinNum","dayKillJoin","dayKillJoinScore" },compress=1),
	@SimpleIndex(name="BillboardStatDate_flowers_d",members = { "dayFlowersNum" ,"dayFlower"},compress=1),
	@SimpleIndex(name="BillboardStatDate_flowers_m",members = { "flowersNum","monthFlower" },compress=1),
	@SimpleIndex(name="BillboardStatDate_sweets_d",members = { "daySweetsNum","daySweet" },compress=1),
	@SimpleIndex(name="BillboardStatDate_sweets_m",members = { "sweetsNum","monthSweet" },compress=1),
	@SimpleIndex(name="BillboardStatDate_tongtianta",members = { "finishTongTianTaTime" },compress=1),
	@SimpleIndex(name="BillboardStatDate_MHT_d",members = { "dayFlowersMHTNum" ,"dayFlowerMHT"},compress=1)
})
public class BillboardStatDate implements Cacheable, CacheListener{

	public static int[] 酒类得分 = {10,20,30,40,50};
	public static int[] 酒颜色得分 = {10,1,2,5,8};
	// playerId;
	@SimpleId
	private long id;
	//冗余
	private byte country;
	
	//当日连斩此数
	private int dayKillJoinNum;
	//当日连斩得分
	private int dayKillJoinScore;
	//当日连斩时间  天(1 - 365)
	private int dayKillJoin;
	
	//当月连斩此数
	private int killJoinNum;
	//当月连斩得分
	private int killJoinScore;
	//当月连斩时间 月(0 - 11)
	private int monthKillJoin;
	
	//当日送花数量
	private int dayFlowersNum ;
	//当日送花时间  天
	private int dayFlower;
	
	//当日送花数量
	private int dayFlowersMHTNum ;
	//当日送花时间  天
	private int dayFlowerMHT;
	
	//当月送花
	private int flowersNum ;
	//当月送花时间  月
	private int monthFlower;
	
	//当日送糖
	private int daySweetsNum ;
	//当日送花时间  天
	private int daySweet;
	//当月送糖
	private int sweetsNum ;
	//当月送花时间 月
	private int monthSweet;
	
	
	
	//喝酒
	private int drinkBeerNum;
	//偷砖
	private int peekNum;
	//刺探
	private int brickNum;
	//偷果实
	private int stealFruitNum;
	//国战得分
	private long countryWarNum;

	//幽冥幻域 界
	private int jie;
	//幽冥幻域 层
	private int ceng;
	
	//完成幽冥幻域时间
	private long finishTongTianTaTime = 0;
	
	//大使技能重数和重数更新时间
	private int skillChongNum;
	private int chongnum;
	private long skillUpdateTime;
	
	//跨服pk积分
	private int honorPoint;
	//增加积分的时间，积分相同，时间升序显示、
	//2016-08-22改成了每周
	private long addHonorPointTime;
	//增加积分的具体时间
	private long rewardPointTime;
	
	//用于出缓存存库
	private transient boolean dirtyState;
	@SimpleVersion
	protected int version;
	

	//	分数：白玉泉=10、金浆醒=20、香桂郢酒=30；白=1、绿=2、蓝=5、紫=8、橙=10公式：（酒等级+酒颜色）
	public void drinkBeerStat(Player player,ArticleEntity ae,int level){
		try{
//			int type = (int)Math.ceil(1.0*level /5);
			int type = (int)Math.floor(1.0*level /5);
			// 酒buff level 从0开始
			int beerLevel = (level+1)%5;
			
			int score = 酒类得分[type] + 酒颜色得分[beerLevel];
			setDrinkBeerNum(score);
			if(FireManager.logger.isWarnEnabled()){
				FireManager.logger.warn("[喝酒计算得分成功] ["+player.getLogString()+"] ["+score+"]");
			}
		}catch(Exception e){
			FireManager.logger.error("[喝酒计算得分错误] ["+player.getLogString()+"]",e); 
			e.printStackTrace();
		}
	}
	
	
	public void 设置连斩(Player player,int killJoinNum,int killJoinScore){
		
		try{
			//不是同一天  直接替换   是同一天判断是否大于在替换
			Calendar cal = Calendar.getInstance();
			int day = cal.get(Calendar.DAY_OF_YEAR);
			if(day == this.dayKillJoin){
				if(killJoinNum > this.dayKillJoinNum){
					setDayKillJoinNum(killJoinNum);
					setDayKillJoinScore(killJoinScore);
					if(BillboardsManager.logger.isWarnEnabled()){
						BillboardsManager.logger.warn("[设置当日连斩成功] [同一天] ["+player.getLogString()+"] ["+killJoinNum+"] ["+killJoinScore+"]");
					}
				}else if(killJoinNum == this.dayKillJoinNum){
					if(killJoinScore > this.dayKillJoinScore){
						setDayKillJoinScore(killJoinScore);
					}
					if(BillboardsManager.logger.isWarnEnabled()){
						BillboardsManager.logger.warn("[设置当日连斩成功] [同一天] ["+player.getLogString()+"] ["+killJoinScore+"]");
					}
				}
			}else{
				setDayKillJoin(day);
				setDayKillJoinNum(killJoinNum);
				setDayKillJoinScore(killJoinScore);
				if(BillboardsManager.logger.isWarnEnabled()){
					BillboardsManager.logger.warn("[设置当日连斩成功] [不是同一天] ["+player.getLogString()+"] ["+killJoinNum+"] ["+killJoinScore+"]");
				}
			}
			
			//设置月
			if(BillboardsManager.是真当月){
				int month = cal.get(Calendar.MONTH);
				
				if(month == this.monthKillJoin){
					if(killJoinNum > this.killJoinNum){
						setKillJoinNum(killJoinNum);
						setKillJoinScore(killJoinScore);
						if(BillboardsManager.logger.isWarnEnabled()){
							BillboardsManager.logger.warn("[设置当月连斩成功] [同一月] ["+player.getLogString()+"] ["+killJoinNum+"] ["+killJoinScore+"]");
						}
					}else if(killJoinNum == this.killJoinNum){
						if(killJoinScore>this.killJoinScore){
							setKillJoinScore(killJoinScore);
						}
						if(BillboardsManager.logger.isWarnEnabled()){
							BillboardsManager.logger.warn("[设置当月连斩成功] [同一月] ["+player.getLogString()+"] ["+killJoinScore+"]");
						}
					}
				}else{
					setMonthKillJoin(month);
					setKillJoinNum(killJoinNum);
					setKillJoinScore(killJoinScore);
					if(BillboardsManager.logger.isWarnEnabled()){
						BillboardsManager.logger.warn("[设置当月连斩成功] [不是同一月] ["+player.getLogString()+"] ["+killJoinNum+"] ["+killJoinScore+"]");
					}
				}
			}else{
				if(killJoinNum > this.killJoinNum){
					setKillJoinNum(killJoinNum);
					setKillJoinScore(killJoinScore);
					if(BillboardsManager.logger.isWarnEnabled()){
						BillboardsManager.logger.warn("[设置连斩成功] [不是真当月] ["+player.getLogString()+"] ["+killJoinNum+"] ["+killJoinScore+"]");
					}
				}else if(killJoinNum == this.killJoinNum){
					if(killJoinScore>this.killJoinScore){
						setKillJoinScore(killJoinScore);
					}
					if(BillboardsManager.logger.isWarnEnabled()){
						BillboardsManager.logger.warn("[设置连斩成功] [不是真当月] ["+player.getLogString()+"] ["+killJoinScore+"]");
					}
				}
			}
		}catch(Exception e){
			BillboardsManager.logger.error("[设置连斩异常] ["+player.getLogString()+"] ["+killJoinNum+"] ["+killJoinScore+"]",e);
		}
	}
	
	
	public synchronized void 设置送花(Player player,int num){
		
		try{
			//不是同一天  直接替换   是同一天判断是否大于在替换
			Calendar cal = Calendar.getInstance();
			int day = cal.get(Calendar.DAY_OF_YEAR);
			if(day == this.dayFlower){
				
				setDayFlowersNum(getDayFlowersNum()+num);
				if(BillboardsManager.logger.isWarnEnabled()){
					BillboardsManager.logger.warn("[设置当日送花成功] [同一天] ["+player.getLogString()+"] ["+num+"]");
				}
			}else{
				setDayFlower(day);
				setDayFlowersNum(num);
				if(BillboardsManager.logger.isWarnEnabled()){
					BillboardsManager.logger.warn("[设置当日送花成功] [不是同一天] ["+player.getLogString()+"] ["+num+"]");
				}
			}
			
			//设置月
			if(BillboardsManager.是真当月){
				int month = cal.get(Calendar.MONTH);
				if(month == this.monthFlower){
					setFlowersNum(getFlowersNum()+num);
					if(BillboardsManager.logger.isWarnEnabled()){
						BillboardsManager.logger.warn("[设置当月送花成功] [同一月] [是真当月] ["+player.getLogString()+"] ["+getFlowersNum()+"]");
					}
				}else{
					setMonthFlower(month);
					setFlowersNum(num);
					if(BillboardsManager.logger.isWarnEnabled()){
						BillboardsManager.logger.warn("[设置当月送花成功] [不同一月] [是真当月] ["+player.getLogString()+"] ["+getFlowersNum()+"]");
					}
				}
			}else{
				setFlowersNum(getFlowersNum()+num);
				if(BillboardsManager.logger.isWarnEnabled()){
					BillboardsManager.logger.warn("[设置当月送花成功] [不是真当月] ["+player.getLogString()+"] ["+getFlowersNum()+"]");
				}
			}
			
			//活动设置
			List<ActivityBillboard> configs = BillboardStatDateManager.configs;
			if(configs!=null && configs.size()>0){
				String keyname = BillboardStatDateManager.getKeyName();
				for(ActivityBillboard data:configs){
					if(data.isEffective()){
						BillboardInfo info = BillboardStatDateManager.getInstance().getInfoByKey(keyname,player.getId());
						if(info!=null){
							info.setValue(info.getValue()+num);
						}else{
							BillboardInfo newinfo = new BillboardInfo();
							newinfo.setKeyname(keyname);
							newinfo.setPid(player.getId());
							newinfo.setValue(num);
							newinfo.setMenuname(Translate.魅力);
							newinfo.setPlayername(player.getName());
							newinfo.setCountry(player.getCountry());
							newinfo.setStarttime(System.currentTimeMillis());
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
							String ymd = sdf.format(System.currentTimeMillis());
							newinfo.setEndtiem(sdf.parse(ymd).getTime()+60*60*1000);
							newinfo.setSubmenuname(Translate.鲜花活动);
							BillboardStatDateManager.getInstance().saveInfo(newinfo);
						}
					}
				}	
			}
		}catch(Exception e){
			BillboardsManager.logger.error("[设置送花异常] ["+player.getLogString()+"] ["+num+"]",e);
		}
	}
	
	
	public synchronized void 设置送糖(Player player,int num){
		
		try{
			//不是同一天  直接替换   是同一天判断是否大于在替换
			Calendar cal = Calendar.getInstance();
			int day = cal.get(Calendar.DAY_OF_YEAR);
			if(day == this.daySweet){
				setDaySweetsNum(getDaySweetsNum()+num);
				if(BillboardsManager.logger.isWarnEnabled()){
					BillboardsManager.logger.warn("[设置当日送糖成功] [同一天] ["+player.getLogString()+"] ["+num+"]");
				}
			}else{
				setDaySweet(day);
				setDaySweetsNum(num);
				if(BillboardsManager.logger.isWarnEnabled()){
					BillboardsManager.logger.warn("[设置当日送糖成功] [不是同一天] ["+player.getLogString()+"] ["+num+"]");
				}
			}
			
			//设置月
			if(BillboardsManager.是真当月){
				int month = cal.get(Calendar.MONTH);
				if(month == this.monthSweet){
					setSweetsNum(getSweetsNum()+num);
					if(BillboardsManager.logger.isWarnEnabled()){
						BillboardsManager.logger.warn("[设置当月送糖成功] [同一月] [是真当月] ["+player.getLogString()+"] ["+getFlowersNum()+"]");
					}
				}else{
					setMonthSweet(month);
					setSweetsNum(num);
					if(BillboardsManager.logger.isWarnEnabled()){
						BillboardsManager.logger.warn("[设置当月送糖成功] [不同一月] [是真当月] ["+player.getLogString()+"] ["+getFlowersNum()+"]");
					}
				}
			}else{
				setSweetsNum(getSweetsNum()+num);
				if(BillboardsManager.logger.isWarnEnabled()){
					BillboardsManager.logger.warn("[设置当月送糖成功] [不是真当月] ["+player.getLogString()+"] ["+getFlowersNum()+"]");
				}
			}
			
			//活动设置
			List<ActivityBillboard> configs = BillboardStatDateManager.configs;
			if(configs!=null && configs.size()>0){
				String keyname = BillboardStatDateManager.getKeyName();
				BillboardsManager.logger.warn("[韩国排行榜活动] [configs:"+configs.size()+"] [keyname："+keyname+"]");
				for(ActivityBillboard data:configs){
					if(data.isEffective()){
						BillboardsManager.logger.warn("[韩国排行榜活动] [configs:"+configs.size()+"] [keyname："+keyname+"] [isEffective:"+data.isEffective()+"]");
						BillboardInfo info = BillboardStatDateManager.getInstance().getInfoByKey(keyname,player.getId());
						if(info!=null){
							info.setValue(info.getValue()+num);
							BillboardsManager.logger.warn("[韩国排行榜活动] [configs:"+configs.size()+"] [infononull]");
						}else{
							BillboardsManager.logger.warn("[韩国排行榜活动] [configs:"+configs.size()+"] [infisnull]");
							BillboardInfo newinfo = new BillboardInfo();
							newinfo.setKeyname(keyname);
							newinfo.setPid(player.getId());
							newinfo.setValue(num);
							newinfo.setPlayername(player.getName());
							newinfo.setCountry(player.getCountry());
							newinfo.setMenuname(Translate.魅力);
							newinfo.setStarttime(System.currentTimeMillis());
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
							String ymd = sdf.format(System.currentTimeMillis());
							newinfo.setEndtiem(sdf.parse(ymd).getTime()+60*60*1000);
							newinfo.setSubmenuname(Translate.糖果活动);
							BillboardStatDateManager.getInstance().saveInfo(newinfo);
						}
					}
				}	
			}
		}catch(Exception e){
			BillboardsManager.logger.error("[设置送糖异常] ["+player.getLogString()+"] ["+num+"]",e);
		}
	}
	
	public synchronized void 设置送棉花糖(Player player,int num){
		
		try{
			//不是同一天  直接替换   是同一天判断是否大于在替换
			Calendar cal = Calendar.getInstance();
			int day = cal.get(Calendar.DAY_OF_YEAR);
			if(day == this.dayFlowerMHT){
				setDayFlowersMHTNum(getDayFlowersMHTNum()+num);
				if(BillboardsManager.logger.isWarnEnabled()){
					BillboardsManager.logger.warn("[设置当日送棉花糖成功] [同一天] [day:"+day+"] ["+player.getLogString()+"] ["+num+"] [数量："+getDayFlowersMHTNum()+"]");
				}
			}else{
				setDayFlowerMHT(day);
				setDayFlowersMHTNum(num);
				if(BillboardsManager.logger.isWarnEnabled()){
					BillboardsManager.logger.warn("[设置当日送棉花糖成功] [不是同一天] [day:"+day+"] ["+player.getLogString()+"] ["+num+"] [数量："+getDayFlowersMHTNum()+"]");
				}
			}
		}catch(Exception e){
			BillboardsManager.logger.error("[设置送棉花糖异常] ["+player.getLogString()+"] ["+num+"]",e);
		}
	}
	
	/**
	 * 活动排行榜：排行榜数据一天一存
	 * eg:2013-08-12为key这一天的存储数据
	 * 	  value:一天中总数据	
	 * @param player
	 * @param num
	 */
	public synchronized void 设置活动排行榜(Player player,int num){
		try{
			Calendar cal = Calendar.getInstance();
			int day = cal.get(Calendar.DAY_OF_YEAR);
			if(day == this.dayFlowerMHT){
				setDayFlowersMHTNum(getDayFlowersMHTNum()+num);
				if(BillboardsManager.logger.isWarnEnabled()){
					BillboardsManager.logger.warn("[设置当日送棉花糖成功] [同一天] [day:"+day+"] ["+player.getLogString()+"] ["+num+"] [数量："+getDayFlowersMHTNum()+"]");
				}
			}else{
				setDayFlowerMHT(day);
				setDayFlowersMHTNum(num);
				if(BillboardsManager.logger.isWarnEnabled()){
					BillboardsManager.logger.warn("[设置当日送棉花糖成功] [不是同一天] [day:"+day+"] ["+player.getLogString()+"] ["+num+"] [数量："+getDayFlowersMHTNum()+"]");
				}
			}
		}catch(Exception e){
			BillboardsManager.logger.error("[设置送棉花糖异常] ["+player.getLogString()+"] ["+num+"]",e);
		}
	}
	
	public void 通知大使技能重数改变(Player player , int newValue){
//		DaShiSkillBillboard b = 
		int oldvalue = skillChongNum;
		String oldUpdateTime = TimeTool.formatter.varChar23.format(System.currentTimeMillis());
		setSkillChongNum(newValue);
		setSkillUpdateTime(SystemTime.currentTimeMillis());
		Billboard bb = BillboardsManager.getInstance().getBillboard("大师技能", "世界");
		Billboard bb2 = BillboardsManager.getInstance().getBillboard("大师技能", "昆仑");
		Billboard bb3 = BillboardsManager.getInstance().getBillboard("大师技能", "九州");
		Billboard bb4 = BillboardsManager.getInstance().getBillboard("大师技能", "万法");
		bb.updateData(player, newValue);
		//123 昆仑 九州 仓月
		if(player.getCountry()==1 && bb2!=null){
			bb2.updateData(player, newValue);
		}else if(player.getCountry()==2 && bb3!=null){
			bb3.updateData(player, newValue);
		}else if(player.getCountry()==3 && bb4!=null){
			bb4.updateData(player, newValue);
		}
		
		if(BillboardsManager.logger.isWarnEnabled()){
			BillboardsManager.logger.warn("[通知大师技能重数改变] [重数变化："+oldvalue+"-->"+newValue+"] [国家："+player.getCountry()+"] [上次更新时间："+oldUpdateTime+"] [玩家信息："+player.getLogString()+"]");
		}
	}
	
	
	
	/***************************get set**************************************/
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getKillJoinNum() {
		return killJoinNum;
	}

	public void setKillJoinNum(int killJoinNum) {
		this.killJoinNum = killJoinNum;
		setDirty("killJoinNum");
	}

	public int getFlowersNum() {
		return flowersNum;
	}

	public void setFlowersNum(int flowersNum) {
		this.flowersNum = flowersNum;
		setDirty("flowersNum");
	}

	public int getSweetsNum() {
		return sweetsNum;
	}
	
	public void setSweetsNum(int sweetsNum) {
		this.sweetsNum = sweetsNum;
		setDirty("sweetsNum");
	}


	public int getDrinkBeerNum() {
		return drinkBeerNum;
	}

	public void setDrinkBeerNum(int drinkBeerNum) {
		this.drinkBeerNum += drinkBeerNum;
		setDirty("drinkBeerNum");
	}

	public int getPeekNum() {
		return peekNum;
	}

	public void setPeekNum(int peekNum) {
		this.peekNum = peekNum;
		BillboardsManager.logger.warn("[改变刺探] [onwerId:"+this.getId()+"] [add:"+peekNum+"] [now:"+this.peekNum+"]");
		setDirty("peekNum");
	}

	public int getBrickNum() {
		return brickNum;
	}

	public void setBrickNum(int brickNum) {
		this.brickNum = brickNum;
		BillboardsManager.logger.warn("[改变偷砖] [onwerId:"+this.getId()+"] [add:"+brickNum+"] [now:"+this.brickNum+"]");
		setDirty("brickNum");
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
		setDirty("country");
	}

	public int getStealFruitNum() {
		return stealFruitNum;
	}

	public void setStealFruitNum(int stealFruitNum) {
		this.stealFruitNum = stealFruitNum;
		BillboardsManager.logger.warn("[改变偷果实] [onwerId:"+this.getId()+"] [add:"+stealFruitNum+"] [now:"+this.stealFruitNum+"]");
		setDirty("stealFruitNum");
	}
	
	
	public void setDirty(String field) {
		try {
			SimpleEntityManager<BillboardStatDate> em = BillboardStatDateManager.em;
			if (em != null) {
				em.notifyFieldChange(this, field);
			}
			this.dirtyState = true;
		} catch (java.lang.IllegalArgumentException e) {
			BillboardsManager.logger.error("[改变数据保存异常] ["+field+"]",e);
		}
	}

	public long getCountryWarNum() {
		return countryWarNum;
	}

	public void setCountryWarNum(long countryWarNum) {
		this.countryWarNum = countryWarNum;
		BillboardsManager.logger.warn("[改变国战积分] [onwerId:"+this.getId()+"] [add:"+countryWarNum+"] [now:"+this.countryWarNum+"]");
		setDirty("countryWarNum");
	}

	@Override
	public int getSize() {

		return 0;
	}


	public int getVersion() {
		return version;
	}


	public void setVersion(int version) {
		this.version = version;
	}

	public int getKillJoinScore() {
		return killJoinScore;
	}

	public void setKillJoinScore(int killJoinScore) {
		this.killJoinScore = killJoinScore;
		setDirty("killJoinScore");
	}
	
	public int getJie() {
		return jie;
	}
	public void setJie(int jie) {
		this.jie = jie;
		setDirty("jie");
	}
	public int getCeng() {
		return ceng;
	}
	public void setCeng(int ceng) {
		this.ceng = ceng;
		setFinishTongTianTaTime(SystemTime.currentTimeMillis());
		setDirty("ceng");
	}

	public int getDayKillJoinNum() {
		return dayKillJoinNum;
	}

	public void setDayKillJoinNum(int dayKillJoinNum) {
		this.dayKillJoinNum = dayKillJoinNum;
		setDirty("dayKillJoinNum");
	}

	public int getDayKillJoinScore() {
		return dayKillJoinScore;
	}

	public void setDayKillJoinScore(int dayKillJoinScore) {
		this.dayKillJoinScore = dayKillJoinScore;
		setDirty("dayKillJoinScore");
	}

	public int getDayFlowersNum() {
		return dayFlowersNum;
	}
	
	public void setDayFlowersNum(int dayFlowersNum) {
		this.dayFlowersNum = dayFlowersNum;
		setDirty("dayFlowersNum");
	}
	public int getDaySweetsNum() {
		return daySweetsNum;
	}
	public void setDaySweetsNum(int daySweetsNum) {
		this.daySweetsNum = daySweetsNum;
		setDirty("daySweetsNum");
	}
	public boolean isDirtyState() {
		return dirtyState;
	}
	public void setDirtyState(boolean dirtyState) {
		this.dirtyState = dirtyState;
	}

	public int getDayKillJoin() {
		return dayKillJoin;
	}


	public void setDayKillJoin(int dayKillJoin) {
		this.dayKillJoin = dayKillJoin;
		setDirty("dayKillJoin");
	}


	public int getMonthKillJoin() {
		return monthKillJoin;
	}


	public void setMonthKillJoin(int monthKillJoin) {
		this.monthKillJoin = monthKillJoin;
		setDirty("monthKillJoin");
	}


	public int getDayFlower() {
		return dayFlower;
	}


	public void setDayFlower(int dayFlower) {
		this.dayFlower = dayFlower;
		setDirty("dayFlower");
	}


	public int getMonthFlower() {
		return monthFlower;
	}


	public void setMonthFlower(int monthFlower) {
		this.monthFlower = monthFlower;
		setDirty("monthFlower");
	}


	public int getDaySweet() {
		return daySweet;
	}


	public void setDaySweet(int daySweet) {
		this.daySweet = daySweet;
		setDirty("daySweet");
	}


	public int getMonthSweet() {
		return monthSweet;
	}


	public void setMonthSweet(int monthSweet) {
		this.monthSweet = monthSweet;
		setDirty("monthSweet");
	}

	
	

	public long getFinishTongTianTaTime() {
		return finishTongTianTaTime;
	}


	public void setFinishTongTianTaTime(long finishTongTianTaTime) {
		this.finishTongTianTaTime = finishTongTianTaTime;
		setDirty("finishTongTianTaTime");
	}

	public int getDayFlowersMHTNum() {
		return dayFlowersMHTNum;
	}


	public void setDayFlowersMHTNum(int dayFlowersMHTNum) {
		this.dayFlowersMHTNum = dayFlowersMHTNum;
		setDirty("dayFlowersMHTNum");
	}


	public int getDayFlowerMHT() {
		return dayFlowerMHT;
	}


	public void setDayFlowerMHT(int dayFlowerMHT) {
		this.dayFlowerMHT = dayFlowerMHT;
		setDirty("dayFlowerMHT");
	}

	public int getSkillChongNum() {
		return skillChongNum;
	}


	public void setSkillChongNum(int skillChongNum) {
		this.skillChongNum = skillChongNum;
		setDirty("skillChongNum");
	}


	public long getSkillUpdateTime() {
		return skillUpdateTime;
	}


	public void setSkillUpdateTime(long skillUpdateTime) {
		this.skillUpdateTime = skillUpdateTime;
		setDirty("skillUpdateTime");
	}


	public int getChongnum() {
		return chongnum;
	}


	public int getHonorPoint() {
		return honorPoint;
	}


	public void setHonorPoint(int honorPoint) {
		this.honorPoint = honorPoint;
		setDirty("honorPoint");
	}


	public long getAddHonorPointTime() {
		return addHonorPointTime;
	}


	public void setAddHonorPointTime(long addHonorPointTime) {
		this.addHonorPointTime = addHonorPointTime;
		setDirty("addHonorPointTime");
	}
	public void setChongnum(int chongnum) {
		this.chongnum = chongnum;
		setDirty("chongnum");
	}
	public long getRewardPointTime() {
		return rewardPointTime;
	}

	public void setRewardPointTime(long rewardPointTime) {
		this.rewardPointTime = rewardPointTime;
		setDirty("rewardPointTime");
	}


	@Override
	public void remove(int type) {

		try {
			if(dirtyState){
				BillboardStatDateManager.em.save(this);
				this.dirtyState = false;
			}
		} catch (Exception e) {
			BillboardsManager.logger.error("[从缓存删除异常]",e);
		}
	}

	
}
